package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
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
import com.example.data.repository.LearnArticles
import com.example.ui.components.AppTopBar
import com.example.ui.components.StepList
import com.example.ui.theme.Background
import com.example.ui.theme.Blue600
import com.example.ui.theme.Border
import com.example.ui.theme.NeutralSlate
import com.example.ui.theme.NeutralSlateBg
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.WarningAmber
import com.example.ui.theme.WarningAmberBg

@Composable
fun LearnArticleScreen(
    articleId: String,
    lang: AppLanguage,
    onBackClick: () -> Unit,
    onCheckNowClick: () -> Unit,
    onHelpNowClick: () -> Unit
) {
    val article = LearnArticles.getArticleById(articleId) ?: LearnArticles.articles.first()
    val title = if (lang == AppLanguage.HI) article.titleHi else article.titleEn
    val intro = if (lang == AppLanguage.HI) article.introHi else article.introEn
    val warningSigns = if (lang == AppLanguage.HI) article.warningSignsHi else article.warningSignsEn
    val whatToDo = if (lang == AppLanguage.HI) article.whatToDoHi else article.whatToDoEn

    Scaffold(
        topBar = {
            AppTopBar(
                lang = lang,
                onHelpNowClick = onHelpNowClick,
                showBackButton = true,
                onBackClick = onBackClick,
                title = title
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
                .testTag("learn_article_screen")
        ) {
            // Read in 1 min chip
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(NeutralSlateBg)
                    .border(1.dp, Border, CircleShape)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.AccessTime,
                    contentDescription = null,
                    tint = NeutralSlate,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = I18n.t("learn.read_time", lang),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = NeutralSlate
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Intro card
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Surface)
                    .border(1.dp, Border, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = intro,
                    fontSize = 17.sp,
                    lineHeight = 25.sp,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Warning signs
            Text(
                text = I18n.t("learn.signs_title", lang),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Surface)
                    .border(1.dp, Border, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                warningSigns.forEachIndexed { index, sign ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(top = 2.dp)
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(WarningAmberBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Warning,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(13.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = sign,
                            fontSize = 17.sp,
                            lineHeight = 24.sp,
                            color = TextPrimary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (index < warningSigns.size - 1) {
                        Spacer(modifier = Modifier.height(4.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // What to do
            Text(
                text = I18n.t("learn.todo_title", lang),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(10.dp))

            StepList(steps = whatToDo)

            Spacer(modifier = Modifier.height(28.dp))

            // CTA Button: Check a message now
            Button(
                onClick = onCheckNowClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("learn_check_now_button"),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue600)
            ) {
                Icon(
                    imageVector = Icons.Filled.Shield,
                    contentDescription = null,
                    tint = Surface,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = I18n.t("learn.check_button", lang),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Surface
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
