package com.example.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.example.ui.theme.Blue600
import com.example.ui.theme.Border
import com.example.ui.theme.Navy900
import com.example.ui.theme.Surface
import com.example.ui.theme.TextPrimary

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppTopBar(
    lang: AppLanguage,
    onHelpNowClick: () -> Unit,
    showBackButton: Boolean = false,
    onBackClick: (() -> Unit)? = null,
    title: String? = null,
    onTitleLongClick: (() -> Unit)? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .border(width = 1.dp, color = Border)
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (showBackButton && onBackClick != null) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("topbar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = I18n.t("common.back", lang),
                        tint = Navy900
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            } else {
                // Shield Logo
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Navy900),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Shield,
                        contentDescription = "SafeSandesh Logo",
                        tint = Surface,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
            }

            // Wordmark or Title
            Text(
                text = title ?: I18n.t("app.name", lang),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier
                    .weight(1f)
                    .testTag("topbar_title")
                    .then(
                        if (onTitleLongClick != null) {
                            Modifier.combinedClickable(onClick = {}, onLongClick = onTitleLongClick)
                        } else Modifier
                    )
            )

            // Help now action button
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Surface)
                    .border(1.dp, Border, CircleShape)
                    .clickable { onHelpNowClick() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("topbar_help_button"),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Phone,
                    contentDescription = null,
                    tint = Blue600,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = I18n.t("nav.help_now", lang),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Blue600
                )
            }
        }
    }
}
