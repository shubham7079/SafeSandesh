package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.ui.theme.Blue600
import com.example.ui.theme.Border
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportBottomSheet(
    resultId: String,
    lang: AppLanguage,
    onDismiss: () -> Unit,
    onSubmit: (resultId: String, reason: String, comment: String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val options = listOf(
        "report.o1",
        "report.o2",
        "report.o3",
        "report.o4"
    )

    var selectedOption by remember { mutableStateOf(options[0]) }
    var comment by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Surface,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .testTag("report_bottom_sheet")
        ) {
            Text(
                text = I18n.t("report.title", lang),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            options.forEach { optionKey ->
                val optionText = I18n.t(optionKey, lang)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { selectedOption = optionKey }
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedOption == optionKey,
                        onClick = { selectedOption = optionKey },
                        colors = RadioButtonDefaults.colors(selectedColor = Blue600)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = optionText,
                        fontSize = 16.sp,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { if (it.length <= 300) comment = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("report_comment_input"),
                placeholder = {
                    Text(
                        text = I18n.t("report.comment_placeholder", lang),
                        fontSize = 15.sp,
                        color = TextMuted
                    )
                },
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    onSubmit(resultId, I18n.t(selectedOption, lang), comment)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("report_send_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600)
            ) {
                Text(
                    text = I18n.t("report.send", lang),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Surface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
