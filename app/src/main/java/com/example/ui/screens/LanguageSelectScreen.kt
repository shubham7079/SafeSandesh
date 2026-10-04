package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.ui.theme.Background
import com.example.ui.theme.Blue600
import com.example.ui.theme.Border
import com.example.ui.theme.Navy900
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun LanguageSelectScreen(
    currentLang: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onContinue: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .padding(20.dp)
            .testTag("language_select_screen"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .width(480.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // App Shield Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Navy900),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = Surface,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = I18n.t("app.name", currentLang),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Navy900
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = I18n.t("app.tagline", currentLang),
                fontSize = 15.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = I18n.t("lang_select.title", currentLang),
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(20.dp))

            // English Option
            LanguageCard(
                title = "English",
                subtitle = "Check before you trust",
                isSelected = currentLang == AppLanguage.EN,
                onClick = { onLanguageSelected(AppLanguage.EN) },
                testTag = "lang_opt_en"
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Hindi Option
            LanguageCard(
                title = "हिन्दी",
                subtitle = "भरोसा करने से पहले जाँच लें",
                isSelected = currentLang == AppLanguage.HI,
                onClick = { onLanguageSelected(AppLanguage.HI) },
                testTag = "lang_opt_hi"
            )

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("lang_continue_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600)
            ) {
                Text(
                    text = I18n.t("lang_select.continue", currentLang),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Surface
                )
            }
        }
    }
}

@Composable
private fun LanguageCard(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Blue600 else Border,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(18.dp)
            .testTag(testTag),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 14.sp,
                color = TextMuted
            )
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(if (isSelected) Blue600 else Border),
            contentAlignment = Alignment.Center
        ) {
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = null,
                    tint = Surface,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
