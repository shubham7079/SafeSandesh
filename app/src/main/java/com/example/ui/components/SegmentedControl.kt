package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.data.model.CheckKind
import com.example.ui.theme.Blue600
import com.example.ui.theme.Border
import com.example.ui.theme.NeutralSlateBg
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun SegmentedKindControl(
    selectedKind: CheckKind,
    onKindSelected: (CheckKind) -> Unit,
    lang: AppLanguage,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        CheckKind.TEXT to "check.tab.message",
        CheckKind.IMAGE to "check.tab.screenshot",
        CheckKind.LINK to "check.tab.link"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(NeutralSlateBg)
            .border(1.dp, Border, RoundedCornerShape(12.dp))
            .padding(4.dp)
            .testTag("segmented_kind_control")
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            items.forEach { (kind, labelKey) ->
                val isSelected = selectedKind == kind
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) Surface else NeutralSlateBg)
                        .then(
                            if (isSelected) Modifier.border(1.dp, Border, RoundedCornerShape(8.dp))
                            else Modifier
                        )
                        .clickable { onKindSelected(kind) }
                        .testTag("segment_${kind.name.lowercase()}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = I18n.t(labelKey, lang),
                        fontSize = 15.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                        color = if (isSelected) Blue600 else TextMuted
                    )
                }
            }
        }
    }
}
