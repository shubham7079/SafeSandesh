package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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

enum class NavTab {
    CHECK,
    HISTORY,
    LEARN,
    SETTINGS
}

data class TabItem(
    val tab: NavTab,
    val labelKey: String,
    val activeIcon: ImageVector,
    val inactiveIcon: ImageVector
)

@Composable
fun AppBottomNav(
    currentTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    lang: AppLanguage
) {
    val tabs = listOf(
        TabItem(NavTab.CHECK, "nav.check", Icons.Filled.Shield, Icons.Outlined.Shield),
        TabItem(NavTab.HISTORY, "nav.history", Icons.Filled.History, Icons.Outlined.History),
        TabItem(NavTab.LEARN, "nav.learn", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
        TabItem(NavTab.SETTINGS, "nav.settings", Icons.Filled.Settings, Icons.Outlined.Settings)
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Surface)
            .border(width = 1.dp, color = Border)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { item ->
                val isSelected = currentTab == item.tab
                val tint = if (isSelected) Blue600 else TextMuted

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .height(64.dp)
                        .clickable { onTabSelected(item.tab) }
                        .testTag("nav_tab_${item.tab.name.lowercase()}"),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top 3dp indicator for active tab
                    if (isSelected) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .height(3.dp)
                                .background(Blue600)
                        )
                    } else {
                        Spacer(modifier = Modifier.height(3.dp))
                    }

                    Icon(
                        imageVector = if (isSelected) item.activeIcon else item.inactiveIcon,
                        contentDescription = I18n.t(item.labelKey, lang),
                        tint = tint,
                        modifier = Modifier.size(24.dp)
                    )

                    Text(
                        text = I18n.t(item.labelKey, lang),
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        color = tint,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                }
            }
        }
    }
}
