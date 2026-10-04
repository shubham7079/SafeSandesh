package com.example.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.Verdict
import com.example.ui.components.AppDisclaimer
import com.example.ui.components.AppTopBar
import com.example.ui.components.LostMoneyCard
import com.example.ui.components.ReasonList
import com.example.ui.components.StepList
import com.example.ui.components.VerdictBanner
import com.example.ui.theme.Background
import com.example.ui.theme.Blue600
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun ResultScreen(
    result: AnalysisResult,
    lang: AppLanguage,
    onBackClick: () -> Unit,
    onCheckAnother: () -> Unit,
    onReportClick: () -> Unit,
    onHelpNowClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppTopBar(
                lang = lang,
                onHelpNowClick = onHelpNowClick,
                showBackButton = true,
                onBackClick = onBackClick,
                title = I18n.t("nav.check", lang)
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
                .testTag("result_screen")
        ) {
            // Verdict Banner
            VerdictBanner(
                verdict = result.verdict,
                scamType = result.scamType,
                impersonates = result.impersonates,
                lang = lang
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Why we think so
            Text(
                text = I18n.t("result.why", lang),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            ReasonList(reasons = result.reasons)

            Spacer(modifier = Modifier.height(24.dp))

            // What to do now
            Text(
                text = I18n.t("result.todo", lang),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            StepList(steps = result.whatToDo)

            // Emergency card for Scam / Suspicious
            if (result.verdict == Verdict.SCAM || result.verdict == Verdict.SUSPICIOUS) {
                Spacer(modifier = Modifier.height(24.dp))
                LostMoneyCard(lang = lang)
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Primary: Check another message
            Button(
                onClick = onCheckAnother,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("check_another_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = null,
                    tint = Surface,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = I18n.t("result.another", lang),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Surface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Secondary: Share this warning
            OutlinedButton(
                onClick = {
                    val verdictText = I18n.t("verdict.${result.verdict.name}", lang)
                    val firstReason = result.reasons.firstOrNull() ?: ""
                    val shareContent = "SafeSandesh check: $verdictText - ${result.scamType}. " +
                            "Why: $firstReason. " +
                            "Check your messages before you trust them. https://safesandesh.in"

                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, shareContent)
                        type = "text/plain"
                    }
                    val shareIntent = Intent.createChooser(sendIntent, I18n.t("result.share", lang))
                    context.startActivity(shareIntent)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("share_warning_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue600),
                border = ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Blue600)
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Share,
                    contentDescription = null,
                    tint = Blue600,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = I18n.t("result.share", lang),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Blue600
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tertiary: Report wrong result
            TextButton(
                onClick = onReportClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("report_wrong_result_button")
            ) {
                Icon(
                    imageVector = Icons.Filled.Flag,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = I18n.t("result.report", lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextMuted
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Footer Disclaimer
            AppDisclaimer(lang = lang)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
