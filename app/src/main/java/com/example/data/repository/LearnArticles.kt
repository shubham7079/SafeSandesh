package com.example.data.repository

import com.example.data.model.LearnArticle

object LearnArticles {

    val articles: List<LearnArticle> = listOf(
        LearnArticle(
            id = "kyc_fraud",
            titleEn = "KYC fraud",
            titleHi = "KYC धोखा",
            summaryEn = "Fake alerts claiming your bank or wallet will be suspended.",
            summaryHi = "बैंक खाता या वॉलेट बंद होने का डर दिखाकर ठगी।",
            introEn = "A message says your bank account or wallet will be blocked unless you update KYC now.",
            introHi = "संदेश में कहा जाता है कि अगर तुरंत KYC अपडेट नहीं किया गया तो आपका बैंक खाता या वॉलेट बंद कर दिया जाएगा।",
            warningSignsEn = listOf(
                "Threat of blocking within hours",
                "A link to 'update' PAN or Aadhaar",
                "Sender is an unknown mobile number or odd name"
            ),
            warningSignsHi = listOf(
                "कुछ ही घंटों में खाता बंद करने की धमकी",
                "पैन या आधार 'अपडेट' करने के लिए दिया गया अनजान लिंक",
                "संदेश किसी अनजान मोबाइल नंबर या अजीब नाम से आना"
            ),
            whatToDoEn = listOf(
                "Do not click the link",
                "Call the bank using the number on your card",
                "Block and report the sender"
            ),
            whatToDoHi = listOf(
                "लिंक पर कभी क्लिक न करें",
                "अपने एटीएम कार्ड के पीछे छपे बैंक नंबर पर कॉल करें",
                "भेजने वाले नंबर को ब्लॉक और रिपोर्ट करें"
            )
        ),
        LearnArticle(
            id = "fake_job_offer",
            titleEn = "Fake job offer",
            titleHi = "नकली नौकरी का ऑफ़र",
            summaryEn = "Unrealistic work-from-home offers demanding upfront fees.",
            summaryHi = "घर बैठे मोटी कमाई का लालच देकर पंजीकरण शुल्क मांगना।",
            introEn = "A job with high pay and no interview, but you must pay a fee first.",
            introHi = "बिना किसी इंटरव्यू के अत्यधिक वेतन वाली नौकरी का वादा, लेकिन पहले कोई फीस या शुल्क मांगा जाता है।",
            warningSignsEn = listOf(
                "Registration or training fee demanded",
                "Salary far too high for simple online tasks",
                "Contact only on Telegram or WhatsApp"
            ),
            warningSignsHi = listOf(
                "रजिस्ट्रेशन या ट्रेनिंग के नाम पर पैसे मांगना",
                "सरल काम के लिए जरूरत से ज्यादा वेतन का वादा",
                "केवल टेलीग्राम या वॉट्सऐप पर ही बातचीत करना"
            ),
            whatToDoEn = listOf(
                "Never pay to get a job",
                "Check the company on its official website",
                "Report the number"
            ),
            whatToDoHi = listOf(
                "नौकरी पाने के लिए कभी भी पैसे न दें",
                "कंपनी की असली आधिकारिक वेबसाइट पर जाकर पुष्टि करें",
                "उस नंबर को तुरंत रिपोर्ट करें"
            )
        ),
        LearnArticle(
            id = "lottery_scam",
            titleEn = "Lottery scam",
            titleHi = "लॉटरी स्कैम",
            summaryEn = "Fake prize messages requiring release fees.",
            summaryHi = "बिना टिकट खरीदे लॉटरी जीतने का झांसा।",
            introEn = "You are told you won a prize in a contest you never entered.",
            introHi = "आपको बताया जाता है कि आपने एक ऐसे लकी ड्रा या प्रतियोगिता में इनाम जीता है जिसमें आपने कभी हिस्सा ही नहीं लिया था।",
            warningSignsEn = listOf(
                "A prize you did not expect",
                "A fee or tax to 'release' the prize money",
                "Urgent pressure to reply quickly"
            ),
            warningSignsHi = listOf(
                "अचानक किसी अनजान लॉटरी का ईनाम निकलना",
                "ईनाम की रकम पाने के लिए टैक्स या प्रोसेसिंग शुल्क मांगना",
                "जल्दबाजी करने और तुरंत जवाब देने का दबाव"
            ),
            whatToDoEn = listOf(
                "Ignore and delete the offer",
                "Never send money to receive money",
                "Report the sender"
            ),
            whatToDoHi = listOf(
                "ऐसे संदेश को नजरअंदाज करें और तुरंत मिटा दें",
                "पैसे प्राप्त करने के लिए कभी किसी को पैसे न भेजें",
                "संदेश भेजने वाले को तुरंत रिपोर्ट करें"
            )
        ),
        LearnArticle(
            id = "upi_refund_scam",
            titleEn = "UPI refund scam",
            titleHi = "UPI रिफ़ंड स्कैम",
            summaryEn = "Tricking users into scanning QR codes or entering PINs.",
            summaryHi = "रिफंड देने के नाम पर क्यूआर कोड स्कैन या पिन दर्ज करवाना।",
            introEn = "Someone says they will send you a refund and asks you to scan a QR code or enter your UPI PIN.",
            introHi = "कोई कहता है कि वह आपको रिफंड या पैसे भेज रहा है और आपसे क्यूआर कोड स्कैन करने या यूपीआई पिन डालने को कहता है।",
            warningSignsEn = listOf(
                "Scanning or PIN is requested to RECEIVE money",
                "A 'collect request' notification you did not expect",
                "Caller creates panic or extreme hurry"
            ),
            warningSignsHi = listOf(
                "पैसे पाने के लिए क्यूआर कोड स्कैन या पिन डालने की मांग",
                "अचानक आपके ऐप पर 'कलेक्ट रिक्वेस्ट' का आना",
                "कॉल करने वाले द्वारा अत्यधिक जल्दबाजी दिखाना"
            ),
            whatToDoEn = listOf(
                "You never need a PIN to receive money",
                "Decline the collect request immediately",
                "Call 1930 if money was taken"
            ),
            whatToDoHi = listOf(
                "पैसे प्राप्त करने के लिए कभी भी पिन की जरूरत नहीं होती",
                "ऐसी किसी भी कलेक्ट रिक्वेस्ट को तुरंत अस्वीकार (Decline) करें",
                "यदि पैसे कट गए हों तो तुरंत 1930 पर कॉल करें"
            )
        ),
        LearnArticle(
            id = "fake_courier_message",
            titleEn = "Fake courier message",
            titleHi = "नकली कूरियर संदेश",
            summaryEn = "Withheld parcel claims with fake payment links.",
            summaryHi = "पार्सल रुकने का बहाना बनाकर फर्जी लिंक से पैसे वसूलना।",
            introEn = "A message says your parcel is held and asks for a small fee through a link.",
            introHi = "संदेश में कहा जाता है कि आपका पार्सल रुका हुआ है और उसे छुड़ाने के लिए एक लिंक के जरिए छोटा सा शुल्क भरें।",
            warningSignsEn = listOf(
                "A parcel you were not expecting",
                "A small fee requested through an unknown web link",
                "A look-alike unofficial courier website"
            ),
            warningSignsHi = listOf(
                "कोई ऐसा पार्सल जिसका आपने कभी ऑर्डर नहीं दिया था",
                "किसी अनजान लिंक के जरिए 5 या 10 रुपये जैसी छोटी रकम मांगना",
                "असली कूरियर कंपनी जैसी दिखने वाली नकली वेबसाइट"
            ),
            whatToDoEn = listOf(
                "Do not pay through the link",
                "Track the parcel on the courier's official app",
                "Delete the message"
            ),
            whatToDoHi = listOf(
                "लिंक के जरिए कभी भी कोई भुगतान न करें",
                "कूरियर कंपनी की असली ऐप या वेबसाइट पर जाकर ट्रैकिंग करें",
                "संदेश को तुरंत मिटा दें"
            )
        ),
        LearnArticle(
            id = "digital_arrest_scam",
            titleEn = "Digital arrest scam",
            titleHi = "डिजिटल अरेस्ट स्कैम",
            summaryEn = "Fake police or customs calls threatening arrest over video.",
            summaryHi = "पुलिस या कस्टम अधिकारी बनकर वीडियो कॉल पर गिरफ्तारी का डर।",
            introEn = "A caller claims to be police, CBI or customs, says a parcel in your name has illegal items, and keeps you on a video call.",
            introHi = "कॉल करने वाला खुद को पुलिस, सीबीआई या कस्टम अधिकारी बताता है, कहता है कि आपके नाम के पार्सल में गैरकानूनी सामान मिला है और वीडियो कॉल पर रखता है।",
            warningSignsEn = listOf(
                "Threat of digital arrest over a phone or video call",
                "Told not to inform family or disconnect the call",
                "Demanded to transfer money for 'verification' or 'escrow'"
            ),
            warningSignsHi = listOf(
                "फोन या वीडियो कॉल पर 'डिजिटल अरेस्ट' की धमकी देना",
                "परिवार को न बताने और कॉल न काटने का कड़ा दबाव",
                "जाँच या वेरिफिकेशन के नाम पर बैंक ट्रांसफर करने को कहना"
            ),
            whatToDoEn = listOf(
                "Real government agencies never arrest someone over video calls",
                "Hang up immediately and inform a family member",
                "Call 1930 to report the incident"
            ),
            whatToDoHi = listOf(
                "कानून में डिजिटल अरेस्ट जैसी कोई चीज नहीं होती; पुलिस कभी वीडियो कॉल पर अरेस्ट नहीं करती",
                "तुरंत फोन काटें और अपने परिवार को सूचित करें",
                "घटना की जानकारी देने के लिए तुरंत 1930 पर कॉल करें"
            )
        )
    )

    fun getArticleById(id: String): LearnArticle? {
        return articles.find { it.id == id }
    }
}
