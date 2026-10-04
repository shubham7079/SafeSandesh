package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.data.repository.LearnArticles
import com.example.ui.components.LearnCardView
import com.example.ui.theme.Background
import com.example.ui.theme.TextPrimary

@Composable
fun LearnScreen(
    lang: AppLanguage,
    onArticleClick: (articleId: String) -> Unit
) {
    val articles = LearnArticles.articles

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("learn_screen")
    ) {
        Text(
            text = I18n.t("learn.title", lang),
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(articles, key = { it.id }) { article ->
                LearnCardView(
                    article = article,
                    lang = lang,
                    onClick = { onArticleClick(article.id) }
                )
            }
        }
    }
}
