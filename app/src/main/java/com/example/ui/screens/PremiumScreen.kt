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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.data.repository.BillingRepository
import com.example.ui.components.AppTopBar
import com.example.ui.theme.Background
import com.example.ui.theme.Blue600
import com.example.ui.theme.Border
import com.example.ui.theme.Navy900
import com.example.ui.theme.NeutralSlateBg
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningAmberBg

@Composable
fun PremiumScreen(
    bannerMsg: String?,
    isPremium: Boolean,
    lang: AppLanguage,
    onBackClick: () -> Unit,
    onHelpNowClick: () -> Unit,
    onDevTogglePremium: () -> Unit,
    onShowToast: (String) -> Unit
) {
    var selectedPlan by remember { mutableStateOf("yearly") }

    Scaffold(
        topBar = {
            AppTopBar(
                lang = lang,
                onHelpNowClick = onHelpNowClick,
                showBackButton = true,
                onBackClick = onBackClick,
                title = I18n.t("premium.title", lang),
                onTitleLongClick = onDevTogglePremium
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
                .testTag("premium_screen")
        ) {
            // Optional Limit Banner
            if (!bannerMsg.isNullOrBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(WarningAmberBg)
                        .border(1.dp, WarningAmber.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Filled.Info,
                        contentDescription = null,
                        tint = WarningAmber,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = bannerMsg,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = WarningAmber
                    )
                }
                Spacer(modifier = Modifier.height(18.dp))
            }

            // Hero Badge
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Navy900),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.WorkspacePremium,
                    contentDescription = null,
                    tint = Surface,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = I18n.t("premium.title", lang),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = I18n.t("premium.subtitle", lang),
                fontSize = 15.sp,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Two-column Feature Comparison Table
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Surface)
                    .border(1.dp, Border, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                // Table Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = I18n.t("premium.col_feature", lang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        modifier = Modifier.weight(1.4f)
                    )
                    Text(
                        text = I18n.t("premium.col_free", lang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.weight(0.8f)
                    )
                    Text(
                        text = I18n.t("premium.col_premium", lang),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue600,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Border))
                Spacer(modifier = Modifier.height(10.dp))

                // Row 1: Checks per day
                ComparisonTableRow(
                    feature = I18n.t("premium.row_checks", lang),
                    freeVal = I18n.t("premium.row_checks_free", lang),
                    premVal = I18n.t("premium.row_checks_prem", lang)
                )

                // Row 2: Screenshot checks
                ComparisonTableRow(
                    feature = I18n.t("premium.row_screenshots", lang),
                    freeVal = I18n.t("premium.row_screenshots_free", lang),
                    premVal = I18n.t("premium.row_screenshots_prem", lang)
                )

                // Row 3: Ads
                ComparisonTableRow(
                    feature = I18n.t("premium.row_ads", lang),
                    freeVal = I18n.t("premium.row_ads_free", lang),
                    premVal = I18n.t("premium.row_ads_prem", lang)
                )

                // Row 4: History
                ComparisonTableRow(
                    feature = I18n.t("premium.row_history", lang),
                    freeVal = I18n.t("premium.row_history_free", lang),
                    premVal = I18n.t("premium.row_history_prem", lang),
                    isLast = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Pricing Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Monthly Plan
                PlanCard(
                    title = "Monthly",
                    price = BillingRepository.PRICE_MONTHLY,
                    subline = null,
                    isSelected = selectedPlan == "monthly",
                    onClick = { selectedPlan = "monthly" },
                    modifier = Modifier.weight(1f)
                )

                // Yearly Plan
                PlanCard(
                    title = "Yearly",
                    price = BillingRepository.PRICE_YEARLY,
                    subline = BillingRepository.YEARLY_SUBLINE,
                    isSelected = selectedPlan == "yearly",
                    onClick = { selectedPlan = "yearly" },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Primary Button: Start 7-day free trial
            Button(
                onClick = {
                    onShowToast(I18n.t("premium.soon", lang))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("start_trial_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600)
            ) {
                Text(
                    text = I18n.t("premium.trial", lang),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Surface
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = I18n.t("premium.cancel", lang),
                fontSize = 14.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Tertiary: Restore purchase
            TextButton(
                onClick = {
                    onShowToast(I18n.t("premium.soon", lang))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("restore_purchase_button")
            ) {
                Text(
                    text = I18n.t("premium.restore", lang),
                    fontSize = 15.sp,
                    color = Blue600
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun ComparisonTableRow(
    feature: String,
    freeVal: String,
    premVal: String,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = feature,
            fontSize = 15.sp,
            color = TextPrimary,
            modifier = Modifier.weight(1.4f)
        )
        Text(
            text = freeVal,
            fontSize = 14.sp,
            color = TextMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(0.8f)
        )
        Text(
            text = premVal,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Blue600,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
    if (!isLast) {
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(NeutralSlateBg))
    }
}

@Composable
private fun PlanCard(
    title: String,
    price: String,
    subline: String?,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Blue600 else Border,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onClick() }
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary
            )
            if (isSelected) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .clip(CircleShape)
                        .background(Blue600),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = null,
                        tint = Surface,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = price,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Blue600 else TextPrimary
        )

        if (!subline.isNullOrBlank()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subline,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = SafeGreen
            )
        }
    }
}
