package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.WorkspacePremium
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.data.model.UserSettings
import com.example.ui.LegalType
import com.example.ui.theme.Background
import com.example.ui.theme.Blue600
import com.example.ui.theme.Border
import com.example.ui.theme.DangerRed
import com.example.ui.theme.NeutralSlate
import com.example.ui.theme.NeutralSlateBg
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

import com.google.firebase.auth.FirebaseUser
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator

@Composable
fun SettingsScreen(
    settings: UserSettings,
    isPremium: Boolean,
    currentUser: FirebaseUser?,
    isAuthLoading: Boolean,
    onSignInGoogle: () -> Unit,
    onSignOut: () -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onTextScaleChange: (Float) -> Unit,
    onNavigateToPremium: () -> Unit,
    onNavigateToHelpNow: () -> Unit,
    onNavigateToLegal: (LegalType) -> Unit,
    onDeleteAllData: () -> Unit
) {
    val context = LocalContext.current
    val lang = settings.lang
    var showDeleteConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("settings_screen")
    ) {
        Text(
            text = I18n.t("settings.title", lang),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Account & Cloud Sync Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Surface)
                .border(1.dp, Border, RoundedCornerShape(12.dp))
                .padding(16.dp)
                .testTag("account_sync_card")
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = if (currentUser != null) Icons.Filled.CloudDone else Icons.Filled.AccountCircle,
                    contentDescription = null,
                    tint = if (currentUser != null) SafeGreen else Blue600,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = I18n.t("settings.account", lang),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = if (currentUser != null) {
                            I18n.t("settings.signed_in_as", lang, currentUser.displayName ?: currentUser.email ?: "")
                        } else {
                            I18n.t("settings.cloud_sync_desc", lang)
                        },
                        fontSize = 13.sp,
                        color = TextMuted
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            if (currentUser != null) {
                TextButton(
                    onClick = onSignOut,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("auth_sign_out_button")
                ) {
                    Icon(
                        imageVector = Icons.Filled.Logout,
                        contentDescription = null,
                        tint = DangerRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = I18n.t("settings.sign_out", lang),
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = DangerRed
                    )
                }
            } else {
                Button(
                    onClick = onSignInGoogle,
                    enabled = !isAuthLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("auth_sign_in_google_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Blue600)
                ) {
                    if (isAuthLoading) {
                        CircularProgressIndicator(
                            color = Surface,
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Filled.Login,
                            contentDescription = null,
                            tint = Surface,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = I18n.t("settings.sign_in_google", lang),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Surface
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preference Card 1: Language
        SettingsGroupCard {
            SettingsRow(
                icon = Icons.Filled.Language,
                title = I18n.t("settings.language", lang),
                value = if (lang == AppLanguage.HI) "हिन्दी" else "English",
                onClick = {
                    val nextLang = if (lang == AppLanguage.EN) AppLanguage.HI else AppLanguage.EN
                    onLanguageChange(nextLang)
                },
                testTag = "settings_language_row"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Preference Card 2: Text Size with Live Preview
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Surface)
                .border(1.dp, Border, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.FormatSize,
                        contentDescription = null,
                        tint = Blue600,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = I18n.t("settings.text_size", lang),
                        fontSize = 17.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                }

                // Normal / Large Segmented Selector
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(NeutralSlateBg)
                        .border(1.dp, Border, RoundedCornerShape(8.dp))
                        .padding(2.dp)
                ) {
                    val isNormal = settings.textScale <= 1.05f
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isNormal) Surface else NeutralSlateBg)
                            .clickable { onTextScaleChange(1.0f) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("text_size_normal")
                    ) {
                        Text(
                            text = I18n.t("settings.normal", lang),
                            fontSize = 13.sp,
                            fontWeight = if (isNormal) FontWeight.Bold else FontWeight.Normal,
                            color = if (isNormal) Blue600 else TextMuted
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (!isNormal) Surface else NeutralSlateBg)
                            .clickable { onTextScaleChange(1.2f) }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                            .testTag("text_size_large")
                    ) {
                        Text(
                            text = I18n.t("settings.large", lang),
                            fontSize = 13.sp,
                            fontWeight = if (!isNormal) FontWeight.Bold else FontWeight.Normal,
                            color = if (!isNormal) Blue600 else TextMuted
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Live preview sentence
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(NeutralSlateBg)
                    .padding(12.dp)
            ) {
                Text(
                    text = I18n.t("settings.preview_sample", lang),
                    fontSize = 15.sp,
                    color = TextPrimary
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Subscription & Support Card
        SettingsGroupCard {
            SettingsRow(
                icon = Icons.Filled.WorkspacePremium,
                title = I18n.t("settings.premium", lang),
                value = if (isPremium) I18n.t("settings.premium_status_active", lang)
                else I18n.t("settings.premium_status_free", lang),
                onClick = onNavigateToPremium,
                testTag = "settings_premium_row"
            )

            SettingsDivider()

            SettingsRow(
                icon = Icons.Filled.Phone,
                title = I18n.t("help.title", lang),
                onClick = onNavigateToHelpNow,
                testTag = "settings_help_now_row"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Legal & Info Card
        SettingsGroupCard {
            SettingsRow(
                icon = Icons.Filled.PrivacyTip,
                title = I18n.t("settings.privacy", lang),
                onClick = { onNavigateToLegal(LegalType.PRIVACY) },
                testTag = "settings_privacy_row"
            )

            SettingsDivider()

            SettingsRow(
                icon = Icons.Filled.Description,
                title = I18n.t("settings.terms", lang),
                onClick = { onNavigateToLegal(LegalType.TERMS) },
                testTag = "settings_terms_row"
            )

            SettingsDivider()

            SettingsRow(
                icon = Icons.Filled.Info,
                title = I18n.t("settings.disclaimer", lang),
                onClick = { onNavigateToLegal(LegalType.DISCLAIMER) },
                testTag = "settings_disclaimer_row"
            )

            SettingsDivider()

            SettingsRow(
                icon = Icons.Filled.Mail,
                title = I18n.t("settings.contact", lang),
                value = "support@safesandesh.in",
                onClick = {
                    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
                        data = Uri.parse("mailto:support@safesandesh.in")
                        putExtra(Intent.EXTRA_SUBJECT, "SafeSandesh Support Inquiry")
                    }
                    try {
                        context.startActivity(emailIntent)
                    } catch (_: Exception) {
                        // Ignore
                    }
                },
                testTag = "settings_contact_row"
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Data Management Card
        SettingsGroupCard {
            SettingsRow(
                icon = Icons.Filled.DeleteSweep,
                title = I18n.t("settings.delete_data", lang),
                textColor = DangerRed,
                onClick = { showDeleteConfirm = true },
                testTag = "settings_delete_data_row"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // App Version
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "${I18n.t("settings.version", lang)} 1.0.0",
                fontSize = 13.sp,
                color = TextMuted
            )
        }

        Spacer(modifier = Modifier.height(20.dp))
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            containerColor = Surface,
            title = {
                Text(
                    text = I18n.t("settings.delete_data_confirm_title", lang),
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary
                )
            },
            text = {
                Text(
                    text = I18n.t("settings.delete_data_confirm_desc", lang),
                    color = TextMuted,
                    fontSize = 15.sp
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteConfirm = false
                        onDeleteAllData()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = DangerRed)
                ) {
                    Text(text = I18n.t("settings.delete_data", lang), fontWeight = FontWeight.Bold)
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

@Composable
private fun SettingsGroupCard(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Surface)
            .border(1.dp, Border, RoundedCornerShape(12.dp))
            .padding(vertical = 4.dp)
    ) {
        content()
    }
}

@Composable
private fun SettingsRow(
    icon: ImageVector,
    title: String,
    value: String? = null,
    textColor: androidx.compose.ui.graphics.Color = TextPrimary,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = if (textColor == DangerRed) DangerRed else Blue600,
            modifier = Modifier.size(22.dp)
        )

        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = textColor,
            modifier = Modifier.weight(1f)
        )

        if (value != null) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = if (value.contains("Active") || value.contains("सक्रिय")) SafeGreen else TextMuted
            )
            Spacer(modifier = Modifier.width(6.dp))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextMuted,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .padding(horizontal = 16.dp)
            .background(Border)
    )
}
