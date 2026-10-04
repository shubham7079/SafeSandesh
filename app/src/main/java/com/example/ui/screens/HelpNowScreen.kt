package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.ui.components.AppTopBar
import com.example.ui.theme.Background
import com.example.ui.theme.Blue600
import com.example.ui.theme.Border
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DangerRedBg
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun HelpNowScreen(
    lang: AppLanguage,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            AppTopBar(
                lang = lang,
                onHelpNowClick = {},
                showBackButton = true,
                onBackClick = onBackClick,
                title = I18n.t("help.title", lang)
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
                .testTag("help_now_screen")
        ) {
            Text(
                text = I18n.t("help.title", lang),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Urgent Card: Call 1930
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(DangerRedBg)
                    .border(1.dp, DangerRed.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                    .padding(18.dp)
                    .testTag("emergency_1930_card")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(DangerRed),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Call,
                            contentDescription = null,
                            tint = Surface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = I18n.t("help.call", lang),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DangerRed
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:1930"))
                        context.startActivity(intent)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dial_1930_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DangerRed)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Call,
                        contentDescription = null,
                        tint = Surface,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "1930",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Surface
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Portal Card 1: cybercrime.gov.in
            HelpLinkCard(
                title = I18n.t("help.cybercrime_title", lang),
                url = "https://cybercrime.gov.in",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://cybercrime.gov.in"))
                    context.startActivity(intent)
                },
                testTag = "help_portal_cybercrime"
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Portal Card 2: Chakshu (Sanchar Saathi)
            HelpLinkCard(
                title = I18n.t("help.chakshu_title", lang),
                url = "https://sancharsaathi.gov.in/sfc/",
                onClick = {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://sancharsaathi.gov.in/sfc/"))
                    context.startActivity(intent)
                },
                testTag = "help_portal_chakshu"
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Checklist Card: Do this first
            Text(
                text = I18n.t("help.do_this_first", lang),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Surface)
                    .border(1.dp, Border, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                ChecklistRow(stepNumber = "1", text = I18n.t("help.step1", lang))
                Spacer(modifier = Modifier.height(12.dp))
                ChecklistRow(stepNumber = "2", text = I18n.t("help.step2", lang))
                Spacer(modifier = Modifier.height(12.dp))
                ChecklistRow(stepNumber = "3", text = I18n.t("help.step3", lang))
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun HelpLinkCard(
    title: String,
    url: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, Border, RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(16.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Shield,
            contentDescription = null,
            tint = Blue600,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = url,
                fontSize = 13.sp,
                color = TextMuted
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Icon(
            imageVector = Icons.Filled.OpenInNew,
            contentDescription = null,
            tint = Blue600,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun ChecklistRow(
    stepNumber: String,
    text: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(24.dp)
                .clip(CircleShape)
                .background(Blue600),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stepNumber,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Surface
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text,
            fontSize = 16.sp,
            lineHeight = 23.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1f)
        )
    }
}
