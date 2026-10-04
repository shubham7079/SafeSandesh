package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.data.model.Verdict
import com.example.ui.theme.DangerRed
import com.example.ui.theme.DangerRedBg
import com.example.ui.theme.NeutralSlate
import com.example.ui.theme.NeutralSlateBg
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.SafeGreenBg
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningAmberBg

data class VerdictVisuals(
    val color: Color,
    val bgColor: Color,
    val icon: ImageVector,
    val wordKey: String,
    val descKey: String
)

fun getVerdictVisuals(verdict: Verdict): VerdictVisuals {
    return when (verdict) {
        Verdict.SAFE -> VerdictVisuals(
            color = SafeGreen,
            bgColor = SafeGreenBg,
            icon = Icons.Filled.CheckCircle,
            wordKey = "verdict.SAFE",
            descKey = "verdict.desc.SAFE"
        )
        Verdict.SUSPICIOUS -> VerdictVisuals(
            color = WarningAmber,
            bgColor = WarningAmberBg,
            icon = Icons.Filled.Warning,
            wordKey = "verdict.SUSPICIOUS",
            descKey = "verdict.desc.SUSPICIOUS"
        )
        Verdict.SCAM -> VerdictVisuals(
            color = DangerRed,
            bgColor = DangerRedBg,
            icon = Icons.Filled.Shield,
            wordKey = "verdict.SCAM",
            descKey = "verdict.desc.SCAM"
        )
        Verdict.UNSURE -> VerdictVisuals(
            color = NeutralSlate,
            bgColor = NeutralSlateBg,
            icon = Icons.Filled.HelpOutline,
            wordKey = "verdict.UNSURE",
            descKey = "verdict.desc.UNSURE"
        )
    }
}

@Composable
fun VerdictBanner(
    verdict: Verdict,
    scamType: String?,
    impersonates: String?,
    lang: AppLanguage,
    modifier: Modifier = Modifier
) {
    val visuals = getVerdictVisuals(verdict)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(visuals.bgColor)
            .border(width = 1.dp, color = visuals.color.copy(alpha = 0.3f), shape = RoundedCornerShape(12.dp))
            .testTag("verdict_banner")
    ) {
        // 6px left border strip in solid verdict color
        Box(
            modifier = Modifier
                .width(6.dp)
                .matchParentSize()
                .background(visuals.color)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 6.dp)
                .padding(20.dp)
        ) {
                // Icon + Verdict Name
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = visuals.icon,
                        contentDescription = I18n.t(visuals.wordKey, lang),
                        tint = visuals.color,
                        modifier = Modifier.size(36.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = I18n.t(visuals.wordKey, lang),
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = visuals.color
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // One-line summary
                Text(
                    text = I18n.t(visuals.descKey, lang),
                    fontSize = 17.sp,
                    lineHeight = 24.sp,
                    color = visuals.color
                )

                // Scam type chip and Impersonation note
                if (!scamType.isNullOrBlank() || !impersonates.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!scamType.isNullOrBlank()) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(visuals.color.copy(alpha = 0.12f))
                                    .border(1.dp, visuals.color.copy(alpha = 0.4f), CircleShape)
                                    .padding(horizontal = 12.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = scamType,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = visuals.color
                                )
                            }
                        }

                        if (!impersonates.isNullOrBlank()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = I18n.t("result.pretending", lang, impersonates),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = visuals.color
                            )
                        }
                    }
                }
            }
        }
}
