package com.example.data.i18n

import com.example.data.model.AppLanguage

object I18n {

    fun t(key: String, lang: AppLanguage, vararg args: Any): String {
        val map = if (lang == AppLanguage.HI) hindiStrings else englishStrings
        val template = map[key] ?: englishStrings[key] ?: key
        return if (args.isNotEmpty()) {
            var formatted = template
            args.forEachIndexed { index, arg ->
                formatted = formatted.replace("{$index}", arg.toString())
                    .replace("{n}", arg.toString())
                    .replace("{name}", arg.toString())
            }
            formatted
        } else {
            template
        }
    }

    private val englishStrings = mapOf(
        "app.name" to "SafeSandesh",
        "app.tagline" to "Check before you trust.",
        "nav.check" to "Check",
        "nav.history" to "History",
        "nav.learn" to "Learn",
        "nav.settings" to "Settings",
        "nav.help_now" to "Help now",

        // Check screen
        "check.title" to "Check a message",
        "check.subtitle" to "Verify any suspicious message, screenshot, or web link.",
        "check.tab.message" to "Message",
        "check.tab.screenshot" to "Screenshot",
        "check.tab.link" to "Link",
        "check.placeholder" to "Paste the message here",
        "check.button" to "Check now",
        "check.safety_line" to "Never share your OTP, PIN or password with anyone.",
        "check.masked" to "Sensitive numbers are hidden for your safety.",
        "check.recent" to "Recent checks",
        "check.loading" to "Checking message...",
        "check.error" to "We could not check this right now. Please try again.",
        "check.limit_left" to "{n} of 5 free checks left today",
        "check.limit_used" to "You have used today's free checks.",
        "check.paste_clipboard" to "Paste from clipboard",
        "check.clear" to "Clear",
        "check.choose_screenshot" to "Choose a screenshot",
        "check.remove_screenshot" to "Remove",
        "check.screenshot_hint" to "Type what the message says (optional)",
        "check.link_placeholder" to "Paste the link here",
        "check.invalid_link" to "This does not look like a link.",
        "check.file_too_large" to "File exceeds 5 MB. Please choose a smaller image.",

        // Verdicts
        "verdict.SAFE" to "Looks Safe",
        "verdict.SUSPICIOUS" to "Suspicious",
        "verdict.SCAM" to "Scam",
        "verdict.UNSURE" to "Not Sure",

        // Verdict Subtitles
        "verdict.desc.SAFE" to "This message contains standard informational patterns.",
        "verdict.desc.SUSPICIOUS" to "Exercise caution. Some warning signs were found.",
        "verdict.desc.SCAM" to "High risk of fraud. Do not follow any instructions.",
        "verdict.desc.UNSURE" to "Insufficient details to make a confident evaluation.",

        // Result screen
        "result.why" to "Why we think so",
        "result.todo" to "What to do now",
        "result.another" to "Check another message",
        "result.share" to "Share this warning",
        "result.report" to "Report wrong result",
        "result.footer" to "This is guidance, not a guarantee. If money was lost, call 1930.",
        "result.pretending" to "Pretending to be: {name}",
        "result.lost_money_title" to "Lost money or shared details?",
        "result.call_1930" to "Call 1930",
        "result.report_portal" to "Report at cybercrime.gov.in",

        // History
        "history.title" to "History",
        "history.delete_all" to "Delete all",
        "history.empty" to "No checks yet. Your results will appear here.",
        "history.delete_confirm_title" to "Delete all check history?",
        "history.delete_confirm_desc" to "This will remove all your saved check results permanently.",
        "history.today" to "Today",
        "history.yesterday" to "Yesterday",

        // Settings
        "settings.title" to "Settings",
        "settings.language" to "Language",
        "settings.text_size" to "Text size",
        "settings.normal" to "Normal",
        "settings.large" to "Large",
        "settings.preview_sample" to "This is a preview of the text size.",
        "settings.account" to "Account & Sync",
        "settings.sign_in_google" to "Sign in with Google",
        "settings.sign_out" to "Sign out",
        "settings.signed_in_as" to "Signed in as {name}",
        "settings.cloud_sync_desc" to "Sync your checks securely across devices",
        "settings.premium" to "SafeSandesh Premium",
        "settings.premium_status_free" to "Free Plan",
        "settings.premium_status_active" to "Premium Active",
        "settings.privacy" to "Privacy Policy",
        "settings.terms" to "Terms of Use",
        "settings.disclaimer" to "Disclaimer",
        "settings.delete_data" to "Delete my data",
        "settings.delete_data_confirm_title" to "Delete all local data?",
        "settings.delete_data_confirm_desc" to "This will reset your checks, history, and preferences.",
        "settings.contact" to "Contact us",
        "settings.version" to "App version",
        "settings.data_cleared" to "All local data has been cleared.",

        // Premium
        "premium.title" to "SafeSandesh Premium",
        "premium.subtitle" to "Protect yourself and your family with complete peace of mind.",
        "premium.trial" to "Start 7-day free trial",
        "premium.cancel" to "Cancel anytime.",
        "premium.restore" to "Restore purchase",
        "premium.unlimited" to "Unlimited checks",
        "premium.no_ads" to "No ads",
        "premium.soon" to "Subscriptions will be available soon.",
        "premium.monthly_price" to "Rs. 99 / month",
        "premium.yearly_price" to "Rs. 799 / year",
        "premium.yearly_subline" to "Less than Rs. 3 a day",
        "premium.col_feature" to "Feature",
        "premium.col_free" to "Free",
        "premium.col_premium" to "Premium",
        "premium.row_checks" to "Checks per day",
        "premium.row_checks_free" to "5",
        "premium.row_checks_prem" to "Unlimited",
        "premium.row_screenshots" to "Screenshot checks",
        "premium.row_screenshots_free" to "Yes",
        "premium.row_screenshots_prem" to "Yes, priority",
        "premium.row_ads" to "Ads",
        "premium.row_ads_free" to "Shown",
        "premium.row_ads_prem" to "None",
        "premium.row_history" to "History",
        "premium.row_history_free" to "Last 100",
        "premium.row_history_prem" to "Last 100 and backup",

        // Help now
        "help.title" to "Help now",
        "help.call" to "Lost money? Call 1930 immediately.",
        "help.cybercrime_title" to "Report cyber fraud: cybercrime.gov.in",
        "help.chakshu_title" to "Report suspicious calls and messages: Chakshu on sancharsaathi.gov.in",
        "help.do_this_first" to "Do this first",
        "help.step1" to "Stop all contact with the sender.",
        "help.step2" to "Call your bank's official number (printed on your card).",
        "help.step3" to "Do not delete messages - keep screenshots as proof.",

        // Report sheet
        "report.title" to "What is wrong with this result?",
        "report.o1" to "This was a genuine message",
        "report.o2" to "This was a scam but shown as safe",
        "report.o3" to "I could not understand the reasons",
        "report.o4" to "Something else",
        "report.comment_placeholder" to "Additional comments (max 300 characters)",
        "report.send" to "Send",
        "report.thanks" to "Thank you. We will review this.",

        // Onboarding & Language
        "lang_select.title" to "Choose your language / अपनी भाषा चुनें",
        "lang_select.continue" to "Continue",
        "onboarding.slide1_title" to "Paste or upload any message",
        "onboarding.slide1_desc" to "Check suspicious SMS, WhatsApp forwards, links, or screenshots instantly.",
        "onboarding.slide2_title" to "Get a clear verdict",
        "onboarding.slide2_desc" to "See if a message looks safe, suspicious, or a scam in simple words.",
        "onboarding.slide3_title" to "Know exactly what to do",
        "onboarding.slide3_desc" to "Get clear instructions on how to protect your money and identity.",
        "onboarding.next" to "Next",
        "onboarding.get_started" to "Get started",
        "onboarding.skip" to "Skip",

        // Learn screen
        "learn.title" to "Learn about scams",
        "learn.read_time" to "Read in 1 minute",
        "learn.signs_title" to "Warning signs",
        "learn.todo_title" to "What to do",
        "learn.check_button" to "Check a message now",

        // Shared general
        "common.cancel" to "Cancel",
        "common.confirm" to "Confirm",
        "common.back" to "Back",
        "common.copied" to "Copied to clipboard"
    )

    private val hindiStrings = mapOf(
        "app.name" to "SafeSandesh",
        "app.tagline" to "भरोसा करने से पहले जाँच लें।",
        "nav.check" to "जाँचें",
        "nav.history" to "इतिहास",
        "nav.learn" to "सीखें",
        "nav.settings" to "सेटिंग्स",
        "nav.help_now" to "अभी मदद लें",

        // Check screen
        "check.title" to "संदेश जाँचें",
        "check.subtitle" to "किसी भी संदिग्ध संदेश, स्क्रीनशॉट या वेब लिंक की तुरंत जाँच करें।",
        "check.tab.message" to "संदेश",
        "check.tab.screenshot" to "स्क्रीनशॉट",
        "check.tab.link" to "लिंक",
        "check.placeholder" to "यहाँ संदेश पेस्ट करें",
        "check.button" to "अभी जाँचें",
        "check.safety_line" to "OTP, PIN या पासवर्ड किसी को न बताएँ।",
        "check.masked" to "आपकी सुरक्षा के लिए संवेदनशील नंबर छिपा दिए गए हैं।",
        "check.recent" to "हाल की जाँच",
        "check.loading" to "संदेश जाँचा जा रहा है...",
        "check.error" to "अभी जाँच नहीं हो पाई। कृपया दोबारा कोशिश करें।",
        "check.limit_left" to "आज की 5 मुफ़्त जाँच में से {n} बाकी हैं",
        "check.limit_used" to "आज की मुफ़्त जाँच पूरी हो गई हैं।",
        "check.paste_clipboard" to "क्लिपबोर्ड से पेस्ट करें",
        "check.clear" to "साफ़ करें",
        "check.choose_screenshot" to "स्क्रीनशॉट चुनें",
        "check.remove_screenshot" to "हटाएँ",
        "check.screenshot_hint" to "संदेश में क्या लिखा है टाइप करें (वैकल्पिक)",
        "check.link_placeholder" to "यहाँ लिंक पेस्ट करें",
        "check.invalid_link" to "यह लिंक जैसा नहीं लग रहा है।",
        "check.file_too_large" to "फ़ाइल 5 MB से बड़ी है। कृपया छोटी फ़ोटो चुनें।",

        // Verdicts
        "verdict.SAFE" to "सुरक्षित लगता है",
        "verdict.SUSPICIOUS" to "संदिग्ध",
        "verdict.SCAM" to "धोखा (स्कैम)",
        "verdict.UNSURE" to "पक्का नहीं कह सकते",

        // Verdict Subtitles
        "verdict.desc.SAFE" to "इस संदेश में सामान्य सूचनात्मक पैटर्न हैं।",
        "verdict.desc.SUSPICIOUS" to "सावधानी बरतें। इसमें कुछ संदेहास्पद संकेत मिले हैं।",
        "verdict.desc.SCAM" to "धोखाधड़ी का भारी जोखिम है। दिए गए निर्देशों पर अमल न करें।",
        "verdict.desc.UNSURE" to "सटीक नतीजे के लिए संदेश में पर्याप्त विवरण नहीं है।",

        // Result screen
        "result.why" to "हम ऐसा क्यों मानते हैं",
        "result.todo" to "अब क्या करें",
        "result.another" to "दूसरा संदेश जाँचें",
        "result.share" to "यह चेतावनी शेयर करें",
        "result.report" to "गलत नतीजे की शिकायत करें",
        "result.footer" to "यह सिर्फ़ सलाह है, गारंटी नहीं। पैसे कट गए हों तो 1930 पर कॉल करें।",
        "result.pretending" to "इसके नाम पर भेजा गया: {name}",
        "result.lost_money_title" to "क्या पैसे कट गए या जानकारी साझा कर दी?",
        "result.call_1930" to "1930 पर कॉल करें",
        "result.report_portal" to "cybercrime.gov.in पर रिपोर्ट करें",

        // History
        "history.title" to "इतिहास",
        "history.delete_all" to "सब हटाएँ",
        "history.empty" to "अभी कोई जाँच नहीं हुई। आपके नतीजे यहाँ दिखेंगे।",
        "history.delete_confirm_title" to "क्या इतिहास मिटाना चाहते हैं?",
        "history.delete_confirm_desc" to "यह आपकी सभी सहेजी गई जाँच को हमेशा के लिए हटा देगा।",
        "history.today" to "आज",
        "history.yesterday" to "कल",

        // Settings
        "settings.title" to "सेटिंग्स",
        "settings.language" to "भाषा",
        "settings.text_size" to "अक्षर का आकार",
        "settings.normal" to "सामान्य",
        "settings.large" to "बड़ा",
        "settings.preview_sample" to "यह अक्षर के आकार का एक नमूना है।",
        "settings.account" to "खाता और सिंक",
        "settings.sign_in_google" to "Google से साइन इन करें",
        "settings.sign_out" to "साइन आउट करें",
        "settings.signed_in_as" to "{name} के रूप में साइन इन हैं",
        "settings.cloud_sync_desc" to "संदेशों की जाँच को सभी डिवाइस पर सुरक्षित सिंक करें",
        "settings.premium" to "सेफ़संदेश प्रीमियम",
        "settings.premium_status_free" to "मुफ़्त प्लान",
        "settings.premium_status_active" to "प्रीमियम सक्रिय",
        "settings.privacy" to "गोपनीयता नीति",
        "settings.terms" to "उपयोग की शर्तें",
        "settings.disclaimer" to "अस्वीकरण",
        "settings.delete_data" to "मेरा डेटा हटाएँ",
        "settings.delete_data_confirm_title" to "सारा स्थानीय डेटा हटाएँ?",
        "settings.delete_data_confirm_desc" to "यह आपकी सभी जाँच, इतिहास और प्राथमिकताओं को रीसेट कर देगा।",
        "settings.contact" to "संपर्क करें",
        "settings.version" to "ऐप संस्करण",
        "settings.data_cleared" to "सभी स्थानीय डेटा हटा दिया गया है।",

        // Premium
        "premium.title" to "सेफ़संदेश प्रीमियम",
        "premium.subtitle" to "अपने और अपने परिवार के वित्तीय लेन-देन को सुरक्षित रखें।",
        "premium.trial" to "7 दिन मुफ़्त आज़माएँ",
        "premium.cancel" to "कभी भी रद्द करें।",
        "premium.restore" to "खरीदारी बहाल करें",
        "premium.unlimited" to "असीमित जाँच",
        "premium.no_ads" to "कोई विज्ञापन नहीं",
        "premium.soon" to "सब्सक्रिप्शन जल्द उपलब्ध होंगे।",
        "premium.monthly_price" to "रु. 99 / महीना",
        "premium.yearly_price" to "रु. 799 / वर्ष",
        "premium.yearly_subline" to "प्रति दिन रु. 3 से भी कम",
        "premium.col_feature" to "सुविधा",
        "premium.col_free" to "मुफ़्त",
        "premium.col_premium" to "प्रीमियम",
        "premium.row_checks" to "प्रति दिन जाँच",
        "premium.row_checks_free" to "5",
        "premium.row_checks_prem" to "असीमित",
        "premium.row_screenshots" to "स्क्रीनशॉट जाँच",
        "premium.row_screenshots_free" to "हाँ",
        "premium.row_screenshots_prem" to "हाँ, प्राथमिकता",
        "premium.row_ads" to "विज्ञापन",
        "premium.row_ads_free" to "दिखाए जाएँगे",
        "premium.row_ads_prem" to "कोई नहीं",
        "premium.row_history" to "इतिहास",
        "premium.row_history_free" to "अंतिम 100",
        "premium.row_history_prem" to "अंतिम 100 व बैकअप",

        // Help now
        "help.title" to "अभी मदद लें",
        "help.call" to "पैसे कट गए? तुरंत 1930 पर कॉल करें।",
        "help.cybercrime_title" to "साइबर धोखाधड़ी रिपोर्ट करें: cybercrime.gov.in",
        "help.chakshu_title" to "संदिग्ध कॉल और संदेश रिपोर्ट करें: Chakshu पोर्टल",
        "help.do_this_first" to "सबसे पहले यह करें",
        "help.step1" to "भेजने वाले से तुरंत संपर्क बंद करें।",
        "help.step2" to "अपने बैंक के आधिकारिक नंबर पर कॉल करें (जो आपके कार्ड पर छपा है)।",
        "help.step3" to "संदेश मिटाएँ नहीं - सबूत के तौर पर स्क्रीनशॉट रखें।",

        // Report sheet
        "report.title" to "इस नतीजे में क्या गलत है?",
        "report.o1" to "यह असली संदेश था",
        "report.o2" to "यह धोखा था पर सुरक्षित बताया गया",
        "report.o3" to "मुझे कारण समझ नहीं आए",
        "report.o4" to "कुछ और",
        "report.comment_placeholder" to "अतिरिक्त टिप्पणी (अधिकतम 300 अक्षर)",
        "report.send" to "भेजें",
        "report.thanks" to "धन्यवाद। हम इसकी समीक्षा करेंगे।",

        // Onboarding & Language
        "lang_select.title" to "Choose your language / अपनी भाषा चुनें",
        "lang_select.continue" to "जारी रखें",
        "onboarding.slide1_title" to "कोई भी संदेश पेस्ट या अपलोड करें",
        "onboarding.slide1_desc" to "संदिग्ध एसएमएस, वॉट्सऐप संदेश, लिंक या स्क्रीनशॉट तुरंत जाँचें।",
        "onboarding.slide2_title" to "स्पष्ट और आसान नतीजा पाएँ",
        "onboarding.slide2_desc" to "आसान शब्दों में जानें कि संदेश सुरक्षित है, संदिग्ध है या धोखा।",
        "onboarding.slide3_title" to "जानें कि अब क्या कदम उठाना है",
        "onboarding.slide3_desc" to "अपने पैसे और पहचान को सुरक्षित रखने के लिए स्पष्ट निर्देश पाएँ।",
        "onboarding.next" to "आगे बढ़ें",
        "onboarding.get_started" to "शुरू करें",
        "onboarding.skip" to "छोड़ें",

        // Learn screen
        "learn.title" to "धोखाधड़ी के बारे में जानें",
        "learn.read_time" to "1 मिनट में पढ़ें",
        "learn.signs_title" to "चेतावनी के संकेत",
        "learn.todo_title" to "क्या करें",
        "learn.check_button" to "संदेश जाँचें",

        // Shared general
        "common.cancel" to "रद्द करें",
        "common.confirm" to "पुष्टि करें",
        "common.back" to "पीछे",
        "common.copied" to "क्लिपबोर्ड पर कॉपी हो गया"
    )
}
