package com.example.ui.screens

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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatListNumbered
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.I18n
import com.example.data.model.AppLanguage
import com.example.ui.theme.Background
import com.example.ui.theme.Blue600
import com.example.ui.theme.Border
import com.example.ui.theme.DangerRed
import com.example.ui.theme.Navy900
import com.example.ui.theme.NeutralSlateBg
import com.example.ui.theme.SafeGreen
import com.example.ui.theme.Surface
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.WarningAmber

@Composable
fun OnboardingScreen(
    lang: AppLanguage,
    onComplete: () -> Unit
) {
    var currentSlide by remember { mutableIntStateOf(0) }

    val slides = listOf(
        Triple(
            "onboarding.slide1_title",
            "onboarding.slide1_desc",
            0
        ),
        Triple(
            "onboarding.slide2_title",
            "onboarding.slide2_desc",
            1
        ),
        Triple(
            "onboarding.slide3_title",
            "onboarding.slide3_desc",
            2
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .statusBarsPadding()
            .padding(20.dp)
            .testTag("onboarding_screen")
    ) {
        // Skip Button in top right
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = I18n.t("onboarding.skip", lang),
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Blue600,
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable { onComplete() }
                    .padding(horizontal = 12.dp, vertical = 6.dp)
                    .testTag("onboarding_skip_button")
            )
        }

        // Center Content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 40.dp, bottom = 90.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Flat Illustration Box
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(Surface)
                    .border(1.dp, Border, RoundedCornerShape(24.dp)),
                contentAlignment = Alignment.Center
            ) {
                when (currentSlide) {
                    0 -> SlideOneGraphic()
                    1 -> SlideTwoGraphic()
                    else -> SlideThreeGraphic()
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Text(
                text = I18n.t(slides[currentSlide].first, lang),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = I18n.t(slides[currentSlide].second, lang),
                fontSize = 16.sp,
                lineHeight = 24.sp,
                color = TextMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Dots indicator
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                slides.forEachIndexed { index, _ ->
                    Box(
                        modifier = Modifier
                            .size(if (currentSlide == index) 24.dp else 8.dp, 8.dp)
                            .clip(CircleShape)
                            .background(if (currentSlide == index) Blue600 else Border)
                    )
                }
            }
        }

        // Bottom CTA Button
        Button(
            onClick = {
                if (currentSlide < 2) {
                    currentSlide++
                } else {
                    onComplete()
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(52.dp)
                .testTag("onboarding_next_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue600)
        ) {
            Text(
                text = if (currentSlide == 2) I18n.t("onboarding.get_started", lang) else I18n.t("onboarding.next", lang),
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
                color = Surface
            )
        }
    }
}

@Composable
private fun SlideOneGraphic() {
    Column(
        modifier = Modifier
            .width(130.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NeutralSlateBg)
            .border(1.dp, Border, RoundedCornerShape(12.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.MailOutline,
            contentDescription = null,
            tint = Navy900,
            modifier = Modifier.size(36.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(8.dp)
                .clip(CircleShape)
                .background(Border)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .height(8.dp)
                .clip(CircleShape)
                .background(Border)
        )
    }
}

@Composable
private fun SlideTwoGraphic() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Filled.Shield,
            contentDescription = null,
            tint = Navy900,
            modifier = Modifier.size(48.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(SafeGreen)
            )
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(WarningAmber)
            )
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(DangerRed)
            )
        }
    }
}

@Composable
private fun SlideThreeGraphic() {
    Column(
        modifier = Modifier
            .width(140.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(NeutralSlateBg)
            .border(1.dp, Border, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Blue600),
                contentAlignment = Alignment.Center
            ) {
                Text("1", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Surface)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Border)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Blue600),
                contentAlignment = Alignment.Center
            ) {
                Text("2", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Surface)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Border)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Blue600),
                contentAlignment = Alignment.Center
            ) {
                Text("3", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Surface)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(CircleShape)
                    .background(Border)
            )
        }
    }
}
