package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.ui.LegalType
import com.example.ui.components.AppTopBar
import com.example.ui.theme.Background
import com.example.ui.theme.Border
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun LegalScreen(
    type: LegalType,
    lang: AppLanguage,
    onBackClick: () -> Unit,
    onHelpNowClick: () -> Unit
) {
    val titleKey = when (type) {
        LegalType.PRIVACY -> "settings.privacy"
        LegalType.TERMS -> "settings.terms"
        LegalType.DISCLAIMER -> "settings.disclaimer"
    }

    val pageTitle = I18n.t(titleKey, lang)

    Scaffold(
        topBar = {
            AppTopBar(
                lang = lang,
                onHelpNowClick = onHelpNowClick,
                showBackButton = true,
                onBackClick = onBackClick,
                title = pageTitle
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
                .testTag("legal_screen_${type.name.lowercase()}")
        ) {
            Text(
                text = pageTitle,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (lang == AppLanguage.HI) "अंतिम अपडेट: 04 अक्टूबर 2026" else "Last updated: 04 October 2026",
                fontSize = 13.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Surface)
                    .border(1.dp, Border, RoundedCornerShape(12.dp))
                    .padding(18.dp)
            ) {
                Column {
                    when (type) {
                        LegalType.PRIVACY -> PrivacyContent(lang)
                        LegalType.TERMS -> TermsContent(lang)
                        LegalType.DISCLAIMER -> DisclaimerContent(lang)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PrivacyContent(lang: AppLanguage) {
    if (lang == AppLanguage.HI) {
        LegalSection(
            heading = "1. क्या जानकारी ली जाती है",
            body = "SafeSandesh केवल वही संदेश, स्क्रीनशॉट या वेब लिंक प्रोसेस करता है जिन्हें आप स्वयं जाँच के लिए दर्ज करते हैं।"
        )
        LegalSection(
            heading = "2. संवेदनशील नंबरों का स्वतः छिपाव",
            body = "किसी भी जाँच से पहले, OTP, पासवर्ड और बैंक खाता संख्याओं जैसे गोपनीय 4+ अंकों के अनुक्रमों को स्वचालित रूप से छिपा दिया जाता है।"
        )
        LegalSection(
            heading = "3. डेटा का स्थानीय भंडारण",
            body = "आपकी पिछली जाँच का इतिहास केवल आपके डिवाइस की स्थानीय मेमोरी में रहता है। इसे किसी बाहरी सर्वर पर नहीं बेचा जाता है।"
        )
        LegalSection(
            heading = "4. डेटा मिटाने का अधिकार",
            body = "आप सेटिंग्स में 'मेरा डेटा हटाएँ' विकल्प दबाकर किसी भी समय अपना पूरा स्थानीय इतिहास और प्राथमिकताएँ तुरंत मिटा सकते हैं।"
        )
        LegalSection(
            heading = "5. संपर्क सूत्र",
            body = "किसी भी प्रश्न के लिए support@safesandesh.in पर संपर्क करें।"
        )
    } else {
        LegalSection(
            heading = "1. Information We Process",
            body = "SafeSandesh processes text messages, screenshot content, and web URLs that you explicitly choose to submit for verification."
        )
        LegalSection(
            heading = "2. Automatic Redaction of Sensitive Numbers",
            body = "Before evaluation, sequences resembling OTPs, passwords, and account numbers are automatically masked (****) to protect your privacy."
        )
        LegalSection(
            heading = "3. On-Device Storage",
            body = "Your check history and app settings remain strictly on your local device storage. We do not sell or monetize personal user data."
        )
        LegalSection(
            heading = "4. Data Deletion",
            body = "You have complete control over your data. Tapping 'Delete my data' in the Settings tab permanently clears all history from your phone."
        )
        LegalSection(
            heading = "5. Contact Information",
            body = "For privacy inquiries, please reach out to us at support@safesandesh.in."
        )
    }
}

@Composable
private fun TermsContent(lang: AppLanguage) {
    if (lang == AppLanguage.HI) {
        LegalSection(
            heading = "1. सेवा की प्रकृति",
            body = "SafeSandesh उपयोगकर्ताओं को संदिग्ध संदेशों और लिंक की सुरक्षा जाँच करने में सहायता प्रदान करता है।"
        )
        LegalSection(
            heading = "2. गैर-संबद्धता की घोषणा",
            body = "SafeSandesh किसी भी सरकारी एजेंसी, बैंक या वित्तीय संस्था से संबद्ध नहीं है। यह एक स्वतंत्र सुरक्षा उपयोगिता है।"
        )
        LegalSection(
            heading = "3. आयु सीमा",
            body = "यह सेवा केवल 18 वर्ष या उससे अधिक आयु के व्यक्तियों के उपयोग के लिए है।"
        )
        LegalSection(
            heading = "4. उचित उपयोग",
            body = "उपयोगकर्ताओं से अपेक्षा की जाती है कि वे सेवा का उपयोग केवल व्यक्तिगत सुरक्षा सत्यापन के लिए करें।"
        )
    } else {
        LegalSection(
            heading = "1. Nature of Service",
            body = "SafeSandesh offers guidance to help users recognize known fraud and scam indicators in digital communications."
        )
        LegalSection(
            heading = "2. No Official Affiliation",
            body = "SafeSandesh is an independent informational utility and is not affiliated with, endorsed by, or representing any bank or government department."
        )
        LegalSection(
            heading = "3. Age Requirements",
            body = "This application is intended for use by individuals 18 years of age or older."
        )
        LegalSection(
            heading = "4. Permitted Use",
            body = "You agree to use this application solely for personal protection and genuine message assessment purposes."
        )
    }
}

@Composable
private fun DisclaimerContent(lang: AppLanguage) {
    if (lang == AppLanguage.HI) {
        LegalSection(
            heading = "1. केवल मार्गदर्शन, कोई पूर्ण गारंटी नहीं",
            body = "जाँच परिणाम केवल सामान्य सुरक्षा मार्गदर्शन के उद्देश्य से प्रदान किए जाते हैं। हम किसी संदेश के 100% सुरक्षित होने का दावा नहीं करते।"
        )
        LegalSection(
            heading = "2. आपातकालीन कदम",
            body = "यदि आपके खाते से अनाधिकृत रूप से पैसे कट गए हैं, तो तुरंत आधिकारिक राष्ट्रीय हेल्पलाइन 1930 पर कॉल करें और अपने बैंक को सूचित करें।"
        )
        LegalSection(
            heading = "3. उपयोगकर्ता का विवेक",
            body = "अंतिम वित्तीय निर्णय हमेशा स्वयं सतर्क रहकर और आधिकारिक माध्यमों से पुष्टि करने के बाद ही लें।"
        )
    } else {
        LegalSection(
            heading = "1. Guidance Only - No 100% Safety Guarantee",
            body = "Evaluation results represent heuristic fraud detection guidance. SafeSandesh never claims or guarantees 100% safety of any communication."
        )
        LegalSection(
            heading = "2. Immediate Actions in Case of Loss",
            body = "If you have shared financial credentials or lost funds, do not delay: dial the national cyber financial helpline at 1930 immediately and block your cards."
        )
        LegalSection(
            heading = "3. User Responsibility",
            body = "Always verify critical requests through official phone numbers printed on your cards or verified government portals."
        )
    }
}

@Composable
private fun LegalSection(heading: String, body: String) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(
            text = heading,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = body,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = TextMuted
        )
    }
}
