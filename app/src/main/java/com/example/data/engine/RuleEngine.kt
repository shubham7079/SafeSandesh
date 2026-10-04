package com.example.data.engine

import com.example.data.model.AnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.CheckKind
import com.example.data.model.RiskLevel
import com.example.data.model.Verdict
import java.net.URI
import java.util.UUID

object RuleEngine {

    private val urlPattern = Regex(
        """(https?://[^\s]+|www\.[^\s]+|[a-zA-Z0-9-]+\.(?:com|in|co\.in|org|net|xyz|top|live|app|gov\.in|nic\.in)[^\s]*)""",
        RegexOption.IGNORE_CASE
    )

    private val officialBrandDomains = mapOf(
        "sbi" to listOf("sbi.co.in", "onlinesbi.sbi", "onlinesbi.com"),
        "hdfc" to listOf("hdfcbank.com", "hdfc.com"),
        "icici" to listOf("icicibank.com"),
        "axis" to listOf("axisbank.com"),
        "paytm" to listOf("paytm.com", "paytmbank.com"),
        "phonepe" to listOf("phonepe.com"),
        "gpay" to listOf("google.com", "pay.google.com"),
        "irctc" to listOf("irctc.co.in"),
        "income tax" to listOf("incometax.gov.in"),
        "uidai" to listOf("uidai.gov.in"),
        "aadhaar" to listOf("uidai.gov.in"),
        "epfo" to listOf("epfindia.gov.in"),
        "india post" to listOf("indiapost.gov.in"),
        "amazon" to listOf("amazon.in", "amazon.com"),
        "flipkart" to listOf("flipkart.com")
    )

    private val brandDisplayNames = mapOf(
        "sbi" to "State Bank of India (SBI)",
        "hdfc" to "HDFC Bank",
        "icici" to "ICICI Bank",
        "axis" to "Axis Bank",
        "paytm" to "Paytm",
        "phonepe" to "PhonePe",
        "gpay" to "Google Pay",
        "irctc" to "IRCTC",
        "income tax" to "Income Tax Department",
        "uidai" to "UIDAI (Aadhaar)",
        "aadhaar" to "UIDAI (Aadhaar)",
        "epfo" to "EPFO India",
        "india post" to "India Post",
        "amazon" to "Amazon",
        "flipkart" to "Flipkart"
    )

    fun evaluate(input: String, kind: CheckKind, lang: AppLanguage): AnalysisResult {
        val trimmed = input.trim()
        val lower = trimmed.lowercase()

        // Empty or whitespace check
        if (trimmed.isEmpty()) {
            return buildUnsureResult(
                reasonEn = "No text provided to check.",
                reasonHi = "जाँचने के लिए कोई संदेश नहीं दिया गया।",
                lang = lang
            )
        }

        // Image without text fallback
        if (kind == CheckKind.IMAGE && trimmed.length < 5) {
            return buildImageEmptyResult(lang)
        }

        // Very short input check
        if (trimmed.length < 20 && kind != CheckKind.LINK) {
            return buildUnsureResult(
                reasonEn = "The message is too short to find reliable patterns.",
                reasonHi = "संदेश बहुत छोटा है, इसलिए भरोसेमंद नतीजे पर पहुँचना मुश्किल है।",
                lang = lang
            )
        }

        val foundUrls = urlPattern.findAll(trimmed).map { it.value }.toList()
        val hasUrl = foundUrls.isNotEmpty()

        var score = 0
        val reasonsEn = mutableListOf<String>()
        val reasonsHi = mutableListOf<String>()
        val categoryScores = mutableMapOf<String, Int>()
        var detectedImpersonation: String? = null

        fun addSignal(cat: String, points: Int, reasonEn: String, reasonHi: String) {
            score += points
            categoryScores[cat] = (categoryScores[cat] ?: 0) + points
            if (reasonsEn.size < 4) {
                reasonsEn.add(reasonEn)
                reasonsHi.add(reasonHi)
            }
        }

        // 1. Sensitive Credentials Demand (+40)
        val otpPattern = Regex(
            """\b(otp|cvv|password|passcode|upi\s*pin|card\s*number|card\s*details|atm\s*pin)\b|ओटीपी|पासवर्ड|सीवीवी|पिन|कार्ड नंबर""",
            RegexOption.IGNORE_CASE
        )
        if (otpPattern.containsMatchIn(lower) && !isInformationalOtpOnly(lower, hasUrl)) {
            addSignal(
                "Credential Theft",
                40,
                "Asks for confidential security details like OTP, PIN, or password.",
                "ओटीपी, पिन या पासवर्ड जैसी गोपनीय सुरक्षा जानकारी मांगता है।"
            )
        }

        // 2. Threat or Extreme Urgency (+20)
        val urgencyPattern = Regex(
            """\b(immediately|today|within 24 hours|last warning|blocked|suspended|deactivated|legal action|arrest|action will be taken)\b|तुरंत|आज ही|24 घंटे|बंद हो जाएगा|खाता बंद|कानूनी कार्रवाई|अंतिम चेतावनी""",
            RegexOption.IGNORE_CASE
        )
        if (urgencyPattern.containsMatchIn(lower)) {
            addSignal(
                "Urgency Tactic",
                20,
                "Creates artificial panic with threats of account blocking or arrest.",
                "खाता बंद होने या गिरफ्तारी का डर दिखाकर घबराहट पैदा करता है।"
            )
        }

        // 3. KYC / PAN / Aadhaar Update with Link (+30)
        val kycTerms = Regex(
            """\b(kyc|pan|aadhaar|pan\s*card)\b|केवाईसी|पैन कार्ड|आधार""",
            RegexOption.IGNORE_CASE
        )
        val blockTerms = Regex(
            """\b(block|suspend|deactivate|expire)\b|बंद|ब्लॉक""",
            RegexOption.IGNORE_CASE
        )
        if ((kycTerms.containsMatchIn(lower) || blockTerms.containsMatchIn(lower)) && hasUrl) {
            addSignal(
                "KYC fraud",
                30,
                "Directs you to an unofficial link to update personal KYC or PAN details.",
                "केवाईसी या पैन अपडेट करने के लिए एक गैर-आधिकारिक लिंक पर भेजता है।"
            )
        }

        // 4. Lottery / Lucky Draw / KBC / Prize (+35)
        val lotteryPattern = Regex(
            """\b(lottery|lucky\s*draw|prize|kbc|you\s*have\s*won|won\s*rs|cashback\s*claim|congratulations.*won)\b|लॉटरी|इनाम|लकी ड्रा|आप जीते हैं|बधाई हो""",
            RegexOption.IGNORE_CASE
        )
        if (lotteryPattern.containsMatchIn(lower)) {
            addSignal(
                "Lottery scam",
                35,
                "Promises prize money or lottery winnings from a contest you never entered.",
                "लॉटरी या ऐसे किसी प्रतियोगिता में ईनाम का दावा करता है जिसमें आपने भाग नहीं लिया।"
            )
        }

        // 5. Fee / Deposit / Processing Charge (+30)
        val feePattern = Regex(
            """\b(registration\s*fee|processing\s*charge|security\s*deposit|advance\s*payment|fee\s*to\s*claim|pay\s*rs)\b|पंजीकरण शुल्क|प्रोसेसिंग चार्ज|एडवांस|फीस जमा करें""",
            RegexOption.IGNORE_CASE
        )
        if (feePattern.containsMatchIn(lower)) {
            addSignal(
                "Upfront Fee",
                30,
                "Demands an advance fee or deposit to release funds or services.",
                "पैसे या सेवा देने के बदले पहले शुल्क या डिपॉजिट की मांग करता है।"
            )
        }

        // 6. Work From Home / Like-and-Earn / Telegram Job (+25)
        val jobPattern = Regex(
            """\b(work\s*from\s*home|part\s*time\s*job|earn\s*per\s*day|earn\s*daily|like\s*and\s*earn|telegram\s*job|youtube\s*like)\b|घर बैठे कमाएं|पार्ट टाइम काम|प्रति दिन कमाई|टेलीग्राम""",
            RegexOption.IGNORE_CASE
        )
        if (jobPattern.containsMatchIn(lower)) {
            addSignal(
                "Fake job offer",
                25,
                "Offers unrealistic daily earnings for simple online tasks via Telegram or WhatsApp.",
                "टेलीग्राम या वॉट्सऐप पर साधारण काम के बदले भारी दैनिक कमाई का झांसा देता है।"
            )
        }

        // 7. Digital Arrest / Police / CBI / Customs / Narcotics (+40)
        val digitalArrestPattern = Regex(
            """\b(digital\s*arrest|cbi|customs|narcotics|police\s*custody|video\s*call\s*custody|parcel.*illegal|court\s*summons|arrest\s*warrant)\b|डिजिटल अरेस्ट|सीबीआई|नारकोटिक्स|कस्टम|पुलिस कस्टडी|वारंट""",
            RegexOption.IGNORE_CASE
        )
        if (digitalArrestPattern.containsMatchIn(lower)) {
            addSignal(
                "Digital arrest scam",
                40,
                "Impersonates law enforcement threatening digital arrest or illegal parcel claims.",
                "पुलिस, सीबीआई या कस्टम अधिकारी बनकर डिजिटल अरेस्ट की धमकी देता है।"
            )
        }

        // 8. Remote Access / APK / AnyDesk / TeamViewer (+40)
        val remoteAppPattern = Regex(
            """\b(anydesk|teamviewer|quicksupport|screen\s*share|install\s*app|download\s*apk|\.apk)\b|एपीके|ऐप इंस्टॉल करें|स्क्रीन शेयर""",
            RegexOption.IGNORE_CASE
        )
        if (remoteAppPattern.containsMatchIn(lower)) {
            addSignal(
                "Remote access scam",
                40,
                "Instructs you to install unknown applications or screen sharing software.",
                "अज्ञात ऐप या स्क्रीन शेयरिंग सॉफ्टवेयर डाउनलोड करने को कहता है।"
            )
        }

        // 9. UPI Refund / Scan QR to Receive (+35)
        val upiRefundPattern = Regex(
            """\b(refund|scan\s*qr|scan.*receive|enter\s*upi\s*pin\s*to\s*receive|upi\s*refund)\b|रिफंड|क्यूआर कोड स्कैन|पैसे पाने के लिए पिन""",
            RegexOption.IGNORE_CASE
        )
        if (upiRefundPattern.containsMatchIn(lower)) {
            addSignal(
                "UPI refund scam",
                35,
                "Falsely claims you need to scan a QR code or enter a PIN to receive money.",
                "पैसे प्राप्त करने के लिए क्यूआर कोड स्कैन करने या पिन डालने का झूठ बोलता है।"
            )
        }

        // 10. Investment / Double Money / Guaranteed Returns (+35)
        val investmentPattern = Regex(
            """\b(guaranteed\s*return|double\s*your\s*money|forex\s*trading|crypto\s*profit|stock\s*tips.*guaranteed)\b|गारंटीड रिटर्न|पैसा डबल|शेयर बाजार टिप्स""",
            RegexOption.IGNORE_CASE
        )
        if (investmentPattern.containsMatchIn(lower)) {
            addSignal(
                "Investment scam",
                35,
                "Promises guaranteed stock, crypto, or forex profits with zero risk.",
                "शेयर बाजार, क्रिप्टो या फॉरेक्स में बिना जोखिम दोगुने मुनाफे का लालच देता है।"
            )
        }

        // 11. Courier / Parcel Release (+30)
        val courierPattern = Regex(
            """\b(parcel|courier|delivery.*held|pay.*release\s*parcel|undelivered\s*package)\b|पार्सल|कूरियर|डिलीवरी रुकी है""",
            RegexOption.IGNORE_CASE
        )
        if (courierPattern.containsMatchIn(lower) && (hasUrl || feePattern.containsMatchIn(lower))) {
            addSignal(
                "Fake courier message",
                30,
                "Claims a delivery is withheld and asks for an online fee or link verification.",
                "पार्सल रुका होने का बहाना बनाकर शुल्क या लिंक पर क्लिक करने को कहता है।"
            )
        }

        // 12. Link Analysis
        foundUrls.forEach { urlStr ->
            val host = extractDomain(urlStr).lowercase()

            // URL Shorteners (+20)
            val shorteners = listOf("bit.ly", "tinyurl.com", "t.co", "cutt.ly", "rb.gy", "is.gd", "goo.gl")
            if (shorteners.any { host.contains(it) }) {
                addSignal(
                    "Phishing link",
                    20,
                    "Uses an obscured short link to hide the real web destination.",
                    "असली वेबसाइट को छिपाने के लिए शॉर्ट लिंक का उपयोग किया गया है।"
                )
            }

            // Raw IP address (+30)
            val ipPattern = Regex("""\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b""")
            if (ipPattern.containsMatchIn(host)) {
                addSignal(
                    "Phishing link",
                    30,
                    "Points directly to a raw numerical IP address instead of an official domain.",
                    "किसी वैध वेबसाइट के बजाय सीधे एक संदिग्ध आईपी पते पर भेजता है।"
                )
            }

            // Punycode (+25)
            if (host.contains("xn--")) {
                addSignal(
                    "Phishing link",
                    25,
                    "Uses deceptive punycode characters to visually mimic a real site.",
                    "असली वेबसाइट जैसा दिखने के लिए बनावटी अक्षरों का उपयोग करता है।"
                )
            }

            // Brand in fake domain (+35)
            officialBrandDomains.forEach { (brand, validDomains) ->
                if (host.contains(brand)) {
                    val isOfficial = validDomains.any { host == it || host.endsWith(".$it") } ||
                            host.endsWith(".gov.in") || host.endsWith(".nic.in")
                    if (!isOfficial) {
                        detectedImpersonation = brandDisplayNames[brand] ?: brand.uppercase()
                        addSignal(
                            "Phishing link",
                            35,
                            "Domain mimics $detectedImpersonation but does not belong to their official website.",
                            "वेब पता $detectedImpersonation जैसा दिखता है पर उनकी असली वेबसाइट नहीं है।"
                        )
                    }
                }
            }

            // Deceptive hyphens / long subdomains (+10)
            val hyphenCount = host.count { it == '-' }
            if (hyphenCount >= 3 || host.length > 35) {
                addSignal(
                    "Phishing link",
                    10,
                    "Web link features a suspicious, overly complex domain structure.",
                    "वेब लिंक में संदेहास्पद और असामान्य अक्षरों की भरमार है।"
                )
            }
        }

        // Move to Telegram/WhatsApp (+10)
        if (Regex("""\b(join\s*telegram|t\.me|wa\.me|whatsapp\s*group)\b""", RegexOption.IGNORE_CASE).containsMatchIn(lower)) {
            addSignal(
                "Suspicious Platform",
                10,
                "Urges you to move communication to an untracked messaging channel.",
                "बातचीत को किसी बाहरी मेसेंजर या टेलीग्राम चैनल पर ले जाने का दबाव डालता है।"
            )
        }

        // Genuine Signals (subtract points)
        var genuineSignalMatched = false

        // Genuine OTP notification without link (-30)
        if (isInformationalOtpOnly(lower, hasUrl)) {
            score -= 30
            genuineSignalMatched = true
            reasonsEn.add("Matches genuine security alert format reminding you not to share codes.")
            reasonsHi.add("यह एक असली सुरक्षा अलर्ट जैसा है जिसमें कोड साझा न करने की सलाह दी गई है।")
        }

        // Genuine debit/credit transaction alert without links or requests (-25)
        val transactionPattern = Regex(
            """\b(debited|credited|spent|withdrawn|available\s*balance|a/c.*debited|a/c.*credited)\b|खाते से कट गए|जमा हुए""",
            RegexOption.IGNORE_CASE
        )
        val scamActionPattern = Regex(
            """\b(click\s*here|click\s*link|update\s*kyc|update\s*pan|pay\s*now|download\s*app|install\s*apk|enter\s*pin|send\s*otp)\b|क्लिक करें|अपडेट करें""",
            RegexOption.IGNORE_CASE
        )
        if (transactionPattern.containsMatchIn(lower) && !hasUrl && !scamActionPattern.containsMatchIn(lower)) {
            score -= 25
            genuineSignalMatched = true
            reasonsEn.add("Standard automated bank transaction confirmation with no links or calls to action.")
            reasonsHi.add("सामान्य बैंक लेन-देन की सूचना है जिसमें कोई लिंक या मांग नहीं है।")
        }

        // Plain informational message (-10)
        if (!hasUrl && !scamActionPattern.containsMatchIn(lower) && !urgencyPattern.containsMatchIn(lower) && score <= 0) {
            score -= 10
            genuineSignalMatched = true
        }

        // If detected brand in text without link
        if (detectedImpersonation == null) {
            brandDisplayNames.forEach { (keyword, display) ->
                if (lower.contains(keyword)) {
                    detectedImpersonation = display
                }
            }
        }

        // Verdict Decision Logic
        val verdict: Verdict
        val riskLevel: RiskLevel

        if (score >= 60) {
            verdict = Verdict.SCAM
            riskLevel = RiskLevel.HIGH
        } else if (score in 30..59) {
            verdict = Verdict.SUSPICIOUS
            riskLevel = RiskLevel.MEDIUM
        } else if (score < 30 && genuineSignalMatched && !hasUrl) {
            verdict = Verdict.SAFE
            riskLevel = RiskLevel.LOW
        } else if (trimmed.length < 20 || reasonsEn.isEmpty()) {
            verdict = Verdict.UNSURE
            riskLevel = RiskLevel.MEDIUM
        } else if (score <= 10 && !hasUrl && trimmed.length >= 40) {
            verdict = Verdict.SAFE
            riskLevel = RiskLevel.LOW
        } else {
            verdict = Verdict.UNSURE
            riskLevel = RiskLevel.MEDIUM
        }

        // Best scamType category
        val topCategory = when {
            categoryScores.containsKey("KYC fraud") -> "KYC fraud"
            categoryScores.containsKey("Lottery scam") -> "Lottery scam"
            categoryScores.containsKey("Fake job offer") -> "Fake job offer"
            categoryScores.containsKey("Digital arrest scam") -> "Digital arrest scam"
            categoryScores.containsKey("UPI refund scam") -> "UPI refund scam"
            categoryScores.containsKey("Fake courier message") -> "Fake courier message"
            categoryScores.containsKey("Investment scam") -> "Investment scam"
            categoryScores.containsKey("Remote access scam") -> "Remote access scam"
            categoryScores.containsKey("Phishing link") -> "Phishing link"
            else -> categoryScores.maxByOrNull { it.value }?.key ?: when (verdict) {
                Verdict.SCAM -> "Fraudulent message"
                Verdict.SUSPICIOUS -> "Suspicious alert"
                Verdict.SAFE -> "Routine notification"
                Verdict.UNSURE -> "Unverified message"
            }
        }

        // Fallback reasons if empty
        if (reasonsEn.isEmpty()) {
            when (verdict) {
                Verdict.SAFE -> {
                    reasonsEn.add("Standard informative structure with no requests for personal credentials.")
                    reasonsHi.add("सामान्य सूचनात्मक संदेश जिसमें कोई निजी जानकारी नहीं मांगी गई है।")
                }
                Verdict.SUSPICIOUS -> {
                    reasonsEn.add("Contains phrasing often associated with unverified promotions or alerts.")
                    reasonsHi.add("इसमें ऐसे शब्द हैं जिनका इस्तेमाल अक्सर अनपेक्षित विज्ञापनों में होता है।")
                }
                Verdict.SCAM -> {
                    reasonsEn.add("Presents multiple high-risk indicators commonly seen in cyber fraud.")
                    reasonsHi.add("इसमें वित्तीय धोखाधड़ी से जुड़े कई गंभीर खतरे पाए गए हैं।")
                }
                Verdict.UNSURE -> {
                    reasonsEn.add("Message lacks specific context to determine authenticity.")
                    reasonsHi.add("सटीक जाँच के लिए संदेश में आवश्यक विवरण मौजूद नहीं है।")
                }
            }
        }

        val reasons = if (lang == AppLanguage.HI) reasonsHi else reasonsEn
        val whatToDo = getStepsForVerdict(verdict, lang)

        return AnalysisResult(
            id = UUID.randomUUID().toString(),
            verdict = verdict,
            riskLevel = riskLevel,
            scamType = topCategory,
            impersonates = detectedImpersonation,
            reasons = reasons,
            whatToDo = whatToDo
        )
    }

    private fun isInformationalOtpOnly(lower: String, hasUrl: Boolean): Boolean {
        val hasOtpWord = lower.contains("otp") || lower.contains("verification code") || lower.contains("ओटीपी")
        val hasDoNotShare = lower.contains("do not share") || lower.contains("never share") ||
                lower.contains("किसी को न बताएँ") || lower.contains("किसी से साझा न करें")
        return hasOtpWord && hasDoNotShare && !hasUrl
    }

    private fun extractDomain(rawUrl: String): String {
        return try {
            val formatted = if (!rawUrl.startsWith("http://") && !rawUrl.startsWith("https://")) {
                "http://$rawUrl"
            } else {
                rawUrl
            }
            URI(formatted).host ?: rawUrl
        } catch (_: Exception) {
            rawUrl.substringAfter("://").substringBefore("/").substringBefore("?")
        }
    }

    private fun buildUnsureResult(reasonEn: String, reasonHi: String, lang: AppLanguage): AnalysisResult {
        return AnalysisResult(
            id = UUID.randomUUID().toString(),
            verdict = Verdict.UNSURE,
            riskLevel = RiskLevel.MEDIUM,
            scamType = if (lang == AppLanguage.HI) "पक्का नहीं कह सकते" else "Unverified content",
            impersonates = null,
            reasons = listOf(if (lang == AppLanguage.HI) reasonHi else reasonEn),
            whatToDo = getStepsForVerdict(Verdict.UNSURE, lang)
        )
    }

    private fun buildImageEmptyResult(lang: AppLanguage): AnalysisResult {
        val reasons = if (lang == AppLanguage.HI) {
            listOf(
                "तस्वीर से टेक्स्ट पढ़ने के लिए कृपया संदेश को नीचे टाइप या पेस्ट करें।",
                "संदिग्ध लिंक या फोन नंबर को सीधे टेक्स्ट बॉक्स में जाँचें।"
            )
        } else {
            listOf(
                "Please type or paste the message text in the box for an accurate check.",
                "Check any link or phone number directly for instant results."
            )
        }
        return AnalysisResult(
            id = UUID.randomUUID().toString(),
            verdict = Verdict.UNSURE,
            riskLevel = RiskLevel.MEDIUM,
            scamType = if (lang == AppLanguage.HI) "स्क्रीनशॉट जाँच" else "Screenshot verification",
            impersonates = null,
            reasons = reasons,
            whatToDo = getStepsForVerdict(Verdict.UNSURE, lang)
        )
    }

    fun getStepsForVerdict(verdict: Verdict, lang: AppLanguage): List<String> {
        return when (verdict) {
            Verdict.SCAM -> if (lang == AppLanguage.HI) listOf(
                "किसी भी लिंक पर क्लिक न करें और कोई जवाब न दें।",
                "अपना OTP, PIN, पासवर्ड या बैंक विवरण किसी को न बताएँ।",
                "भेजने वाले नंबर को तुरंत ब्लॉक और रिपोर्ट करें।",
                "यदि पैसे कट गए हों, तो तुरंत 1930 पर कॉल करें और cybercrime.gov.in पर रिपोर्ट दर्ज करें।"
            ) else listOf(
                "Do not click any links or reply to the sender.",
                "Never share your OTP, PIN, password, or bank details.",
                "Block and report the sender immediately on your phone.",
                "If money was lost, call 1930 immediately and report at cybercrime.gov.in."
            )

            Verdict.SUSPICIOUS -> if (lang == AppLanguage.HI) listOf(
                "संदेश में दिए गए लिंक या नंबर पर भरोसा न करें।",
                "संबंधित संस्था के आधिकारिक नंबर (जो आपके बैंक कार्ड या वेबसाइट पर हो) पर संपर्क करें।",
                "अपने बैंक या वॉलेट ऐप में सीधे लॉग इन करके खाता स्थिति जाँचें।"
            ) else listOf(
                "Do not click the link or follow instructions.",
                "Contact the organization using the official number printed on your card or website.",
                "Verify your account status directly inside your official banking application."
            )

            Verdict.SAFE -> if (lang == AppLanguage.HI) listOf(
                "हमेशा सावधान रहें; अपना OTP या PIN कभी किसी के साथ साझा न करें।",
                "बिना माँगे आए किसी भी लिंक या संदेश को नजरअंदाज करें।",
                "काम पूरा होने के बाद पुराने सुरक्षा कोड हटा दें।"
            ) else listOf(
                "Always stay careful; never share an OTP or PIN with anyone.",
                "Ignore any unexpected links or messages you did not request.",
                "Delete temporary verification codes once used."
            )

            Verdict.UNSURE -> if (lang == AppLanguage.HI) listOf(
                "संदेश पर तुरंत कोई कदम न उठाएँ और अनजान लिंक पर क्लिक न करें।",
                "भेजने वाले की पहचान आधिकारिक माध्यमों से खुद सत्यापित करें।",
                "अधिक सटीक परिणाम के लिए पूरा संदेश कॉपी करके यहाँ पेस्ट करें।"
            ) else listOf(
                "Do not act on this message or click unfamiliar links.",
                "Verify with the sender through verified official channels.",
                "Paste or type the full message text for a more comprehensive check."
            )
        }
    }
}
