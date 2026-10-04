package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import com.example.data.model.Verdict

@Composable
fun VerdictChip(
    verdict: Verdict,
    lang: AppLanguage,
    modifier: Modifier = Modifier
) {
    val visuals = getVerdictVisuals(verdict)

    Row(
        modifier = modifier
            .clip(CircleShape)
            .background(visuals.bgColor)
            .border(width = 1.dp, color = visuals.color.copy(alpha = 0.35f), shape = CircleShape)
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .testTag("verdict_chip_${verdict.name.lowercase()}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = visuals.icon,
            contentDescription = null,
            tint = visuals.color,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = I18n.t(visuals.wordKey, lang),
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = visuals.color
        )
    }
}
