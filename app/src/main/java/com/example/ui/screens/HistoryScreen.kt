package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.data.model.HistoryEntry
import com.example.ui.components.EmptyState
import com.example.ui.components.HistoryItemCard
import com.example.ui.theme.Background
import com.example.ui.theme.DangerRed
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun HistoryScreen(
    historyList: List<HistoryEntry>,
    lang: AppLanguage,
    onItemClick: (HistoryEntry) -> Unit,
    onDeleteAll: () -> Unit
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("history_screen")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = I18n.t("history.title", lang),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            if (historyList.isNotEmpty()) {
                TextButton(
                    onClick = { showDeleteConfirm = true },
                    modifier = Modifier.testTag("delete_all_history_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.DeleteOutline,
                        contentDescription = null,
                        tint = DangerRed
                    )
                    Spacer(modifier = Modifier.padding(start = 4.dp))
                    Text(
                        text = I18n.t("history.delete_all", lang),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = DangerRed
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (historyList.isEmpty()) {
            EmptyState(
                title = I18n.t("history.empty", lang),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 60.dp)
            )
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(historyList, key = { it.id }) { entry ->
                    HistoryItemCard(
                        entry = entry,
                        lang = lang,
                        onClick = { onItemClick(entry) }
                    )
                }
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            containerColor = Surface,
            title = {
                Text(
                    text = I18n.t("history.delete_confirm_title", lang),
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = I18n.t("history.delete_confirm_desc", lang),
                    color = TextMuted,
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteAll()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = DangerRed)
                ) {
                    Text(text = I18n.t("history.delete_all", lang), fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(text = I18n.t("common.cancel", lang), color = TextMuted)
                }
            }
        )
    }
}
