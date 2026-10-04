package com.example.ui.screens

import android.content.ClipboardManager
import android.content.Context
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.data.model.CheckKind
import com.example.data.model.DailyUsage
import com.example.data.model.HistoryEntry
import com.example.ui.components.EmptyState
import com.example.ui.components.HistoryItemCard
import com.example.ui.components.SegmentedKindControl
import com.example.ui.theme.Background
import com.example.ui.theme.Blue600
import com.example.ui.theme.Border
import com.example.ui.theme.DangerRed
import com.example.ui.theme.Navy900
import com.example.ui.theme.NeutralSlate
import com.example.ui.theme.NeutralSlateBg
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun CheckScreen(
    lang: AppLanguage,
    dailyUsage: DailyUsage,
    historyList: List<HistoryEntry>,
    isAnalyzing: Boolean,
    analysisError: String?,
    onRunCheck: (kind: CheckKind, text: String) -> Unit,
    onHistoryItemClick: (HistoryEntry) -> Unit,
    onNavigateToPremium: () -> Unit
) {
    val context = LocalContext.current
    var selectedKind by remember { mutableStateOf(CheckKind.TEXT) }
    var messageText by remember { mutableStateOf("") }
    var linkText by remember { mutableStateOf("") }
    var screenshotUri by remember { mutableStateOf<Uri?>(null) }
    var screenshotTypedText by remember { mutableStateOf("") }
    var fileError by remember { mutableStateOf<String?>(null) }
    var linkError by remember { mutableStateOf<String?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri)
                val byteCount = inputStream?.available() ?: 0
                inputStream?.close()

                if (byteCount > 5 * 1024 * 1024) {
                    fileError = I18n.t("check.file_too_large", lang)
                    screenshotUri = null
                } else {
                    fileError = null
                    screenshotUri = uri
                }
            } catch (_: Exception) {
                fileError = null
                screenshotUri = uri
            }
        }
    }

    val checksLeft = (5 - dailyUsage.count).coerceAtLeast(0)
    val isInputValid = when (selectedKind) {
        CheckKind.TEXT -> messageText.trim().isNotEmpty()
        CheckKind.IMAGE -> screenshotUri != null || screenshotTypedText.trim().isNotEmpty()
        CheckKind.LINK -> linkText.trim().isNotEmpty()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("check_screen")
    ) {
        // Heading
        Text(
            text = I18n.t("check.title", lang),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = I18n.t("check.subtitle", lang),
            fontSize = 15.sp,
            color = TextMuted
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Kind Switcher (Message | Screenshot | Link)
        SegmentedKindControl(
            selectedKind = selectedKind,
            onKindSelected = { selectedKind = it },
            lang = lang
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Input Content by Kind
        when (selectedKind) {
            CheckKind.TEXT -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Surface)
                        .border(1.dp, Border, RoundedCornerShape(12.dp))
                        .padding(14.dp)
                ) {
                    Column {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = {
                                if (it.length <= 3000) messageText = it
                            },
                            placeholder = {
                                Text(
                                    text = I18n.t("check.placeholder", lang),
                                    fontSize = 17.sp,
                                    color = TextMuted
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .testTag("message_input_textarea"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = androidx.compose.ui.graphics.Color.Transparent,
                                unfocusedBorderColor = androidx.compose.ui.graphics.Color.Transparent
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${messageText.length}/3000",
                                fontSize = 13.sp,
                                color = TextMuted
                            )

                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                if (messageText.isNotEmpty()) {
                                    TextButton(
                                        onClick = { messageText = "" },
                                        modifier = Modifier.testTag("clear_text_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.Clear,
                                            contentDescription = null,
                                            tint = TextMuted,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = I18n.t("check.clear", lang),
                                            fontSize = 14.sp,
                                            color = TextMuted
                                        )
                                    }
                                }

                                OutlinedButton(
                                    onClick = {
                                        try {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = clipboard.primaryClip
                                            if (clip != null && clip.itemCount > 0) {
                                                val text = clip.getItemAt(0).text?.toString() ?: ""
                                                messageText = text.take(3000)
                                            }
                                        } catch (_: Exception) {
                                            // Handle clipboard reading gracefully
                                        }
                                    },
                                    modifier = Modifier.testTag("paste_clipboard_button"),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Blue600),
                                    border = ButtonDefaults.outlinedButtonBorder.copy(
                                        brush = androidx.compose.ui.graphics.SolidColor(Blue600)
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Filled.ContentPaste,
                                        contentDescription = null,
                                        tint = Blue600,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = I18n.t("check.paste_clipboard", lang),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Blue600
                                    )
                                }
                            }
                        }
                    }
                }
            }

            CheckKind.IMAGE -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Surface)
                            .border(1.dp, Border, RoundedCornerShape(12.dp))
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("choose_screenshot_area"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (screenshotUri != null) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                val bitmap = remember(screenshotUri) {
                                    try {
                                        context.contentResolver.openInputStream(screenshotUri!!)?.use {
                                            BitmapFactory.decodeStream(it)
                                        }
                                    } catch (_: Exception) {
                                        null
                                    }
                                }

                                if (bitmap != null) {
                                    Image(
                                        bitmap = bitmap.asImageBitmap(),
                                        contentDescription = "Screenshot preview",
                                        modifier = Modifier
                                            .height(90.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Filled.Image,
                                        contentDescription = null,
                                        tint = Blue600,
                                        modifier = Modifier.size(40.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                TextButton(
                                    onClick = { screenshotUri = null },
                                    modifier = Modifier.testTag("remove_screenshot_button")
                                ) {
                                    Text(
                                        text = I18n.t("check.remove_screenshot", lang),
                                        fontSize = 14.sp,
                                        color = DangerRed
                                    )
                                }
                            }
                        } else {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Filled.Image,
                                    contentDescription = null,
                                    tint = Blue600,
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = I18n.t("check.choose_screenshot", lang),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Blue600
                                )
                            }
                        }
                    }

                    if (fileError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = fileError!!,
                            fontSize = 14.sp,
                            color = DangerRed
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    OutlinedTextField(
                        value = screenshotTypedText,
                        onValueChange = { screenshotTypedText = it },
                        placeholder = {
                            Text(
                                text = I18n.t("check.screenshot_hint", lang),
                                fontSize = 15.sp,
                                color = TextMuted
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("screenshot_hint_input"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Blue600,
                            unfocusedBorderColor = Border,
                            focusedContainerColor = Surface,
                            unfocusedContainerColor = Surface
                        )
                    )
                }
            }

            CheckKind.LINK -> {
                Column(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = linkText,
                        onValueChange = {
                            linkText = it
                            linkError = null
                        },
                        placeholder = {
                            Text(
                                text = I18n.t("check.link_placeholder", lang),
                                fontSize = 16.sp,
                                color = TextMuted
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Filled.Link,
                                contentDescription = null,
                                tint = Blue600
                            )
                        },
                        trailingIcon = {
                            if (linkText.isNotEmpty()) {
                                IconButtonClick(onClick = { linkText = "" }) {
                                    Icon(
                                        imageVector = Icons.Filled.Clear,
                                        contentDescription = null,
                                        tint = TextMuted
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("link_input_field"),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Blue600,
                            unfocusedBorderColor = Border,
                            focusedContainerColor = Surface,
                            unfocusedContainerColor = Surface
                        )
                    )

                    if (linkError != null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = linkError!!,
                            fontSize = 14.sp,
                            color = DangerRed
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Daily limit status line
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (dailyUsage.isPremium) {
                    I18n.t("premium.unlimited", lang)
                } else {
                    I18n.t("check.limit_left", lang, checksLeft)
                },
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (checksLeft == 0 && !dailyUsage.isPremium) DangerRed else TextMuted
            )

            if (!dailyUsage.isPremium && checksLeft <= 2) {
                Text(
                    text = I18n.t("settings.premium", lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Blue600,
                    modifier = Modifier.clickable { onNavigateToPremium() }
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Loading State Progress Bar
        if (isAnalyzing) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(CircleShape),
                    color = Blue600,
                    trackColor = NeutralSlateBg
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = I18n.t("check.loading", lang),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium,
                    color = Navy900
                )
            }
        } else {
            // Primary Check Now Button (Min height 52dp)
            Button(
                onClick = {
                    when (selectedKind) {
                        CheckKind.TEXT -> onRunCheck(CheckKind.TEXT, messageText)
                        CheckKind.IMAGE -> onRunCheck(CheckKind.IMAGE, screenshotTypedText)
                        CheckKind.LINK -> {
                            val trimmed = linkText.trim()
                            val looksLikeLink = trimmed.contains(".") && !trimmed.contains(" ")
                            if (!looksLikeLink) {
                                linkError = I18n.t("check.invalid_link", lang)
                            } else {
                                linkError = null
                                onRunCheck(CheckKind.LINK, trimmed)
                            }
                        }
                    }
                },
                enabled = isInputValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("check_now_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue600,
                    disabledContainerColor = Blue600.copy(alpha = 0.45f)
                )
            ) {
                Text(
                    text = I18n.t("check.button", lang),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Surface
                )
            }
        }

        if (analysisError != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = analysisError,
                fontSize = 14.sp,
                color = DangerRed
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Safety Reminder Line
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = I18n.t("check.safety_line", lang),
                fontSize = 13.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Recent Checks Section (Last 3)
        Text(
            text = I18n.t("check.recent", lang),
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(12.dp))

        val recentChecks = historyList.take(3)
        if (recentChecks.isEmpty()) {
            EmptyState(
                title = I18n.t("history.empty", lang),
                modifier = Modifier.fillMaxWidth()
            )
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                recentChecks.forEach { entry ->
                    HistoryItemCard(
                        entry = entry,
                        lang = lang,
                        onClick = { onHistoryItemClick(entry) }
                    )
                }
            }
        }
    }
}

@Composable
private fun IconButtonClick(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
