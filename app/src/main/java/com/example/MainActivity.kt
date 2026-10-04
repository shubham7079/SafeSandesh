package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.AppScreen
import com.example.ui.MainViewModel
import com.example.data.firebase.configureAppCheck
import com.example.ui.components.AppBottomNav
import com.example.ui.components.AppTopBar
import com.example.ui.components.NavTab
import com.example.ui.components.ReportBottomSheet
import com.example.ui.screens.CheckScreen
import com.example.ui.screens.HelpNowScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.LanguageSelectScreen
import com.example.ui.screens.LearnArticleScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.LegalScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.PremiumScreen
import com.example.ui.screens.ResultScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.Background
import com.example.ui.theme.SafeSandeshTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configureAppCheck(this, intent)
        enableEdgeToEdge()

        setContent {
            val viewModel: MainViewModel = viewModel()
            val uiState by viewModel.uiState.collectAsState()

            // Handle Toast Messages
            uiState.toastMessage?.let { msg ->
                LaunchedEffect(msg) {
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                    viewModel.clearToast()
                }
            }

            // Masked Numbers Safety Notice Toast
            if (uiState.wasMaskedNotice) {
                LaunchedEffect(Unit) {
                    val notice = if (uiState.settings.lang == com.example.data.model.AppLanguage.HI) {
                        "आपकी सुरक्षा के लिए संवेदनशील नंबर छिपा दिए गए हैं।"
                    } else {
                        "Sensitive numbers are hidden for your safety."
                    }
                    Toast.makeText(this@MainActivity, notice, Toast.LENGTH_SHORT).show()
                }
            }

            SafeSandeshTheme(textScale = uiState.settings.textScale) {
                SafeSandeshApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun SafeSandeshApp(viewModel: MainViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val lang = uiState.settings.lang

    // Custom back navigation handling
    BackHandler(enabled = true) {
        val handled = viewModel.navigateBack()
        if (!handled && uiState.currentTab != NavTab.CHECK) {
            viewModel.selectTab(NavTab.CHECK)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        when (val screen = uiState.currentScreen) {
            is AppScreen.LanguageSelect -> {
                LanguageSelectScreen(
                    currentLang = lang,
                    onLanguageSelected = { viewModel.setLanguage(it) },
                    onContinue = { viewModel.startOnboarding() }
                )
            }

            is AppScreen.Onboarding -> {
                OnboardingScreen(
                    lang = lang,
                    onComplete = { viewModel.completeOnboarding() }
                )
            }

            is AppScreen.Main -> {
                Scaffold(
                    topBar = {
                        AppTopBar(
                            lang = lang,
                            onHelpNowClick = { viewModel.navigateTo(AppScreen.HelpNow) }
                        )
                    },
                    bottomBar = {
                        AppBottomNav(
                            currentTab = uiState.currentTab,
                            onTabSelected = { viewModel.selectTab(it) },
                            lang = lang
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (uiState.currentTab) {
                            NavTab.CHECK -> {
                                CheckScreen(
                                    lang = lang,
                                    dailyUsage = uiState.dailyUsage,
                                    historyList = uiState.historyList,
                                    isAnalyzing = uiState.isAnalyzing,
                                    analysisError = uiState.analysisError,
                                    onRunCheck = { kind, text -> viewModel.runCheck(kind, text) },
                                    onHistoryItemClick = { entry ->
                                        viewModel.navigateTo(AppScreen.Result(entry.result, entry.preview))
                                    },
                                    onNavigateToPremium = { viewModel.navigateTo(AppScreen.Premium()) }
                                )
                            }

                            NavTab.HISTORY -> {
                                HistoryScreen(
                                    historyList = uiState.historyList,
                                    lang = lang,
                                    onItemClick = { entry ->
                                        viewModel.navigateTo(AppScreen.Result(entry.result, entry.preview))
                                    },
                                    onDeleteAll = { viewModel.deleteAllHistory() }
                                )
                            }

                            NavTab.LEARN -> {
                                LearnScreen(
                                    lang = lang,
                                    onArticleClick = { articleId ->
                                        viewModel.navigateTo(AppScreen.LearnDetail(articleId))
                                    }
                                )
                            }

                            NavTab.SETTINGS -> {
                                SettingsScreen(
                                    settings = uiState.settings,
                                    isPremium = uiState.dailyUsage.isPremium,
                                    currentUser = uiState.currentUser,
                                    isAuthLoading = uiState.isAuthLoading,
                                    onSignInGoogle = { viewModel.signInWithGoogle() },
                                    onSignOut = { viewModel.signOut() },
                                    onLanguageChange = { viewModel.setLanguage(it) },
                                    onTextScaleChange = { viewModel.setTextScale(it) },
                                    onNavigateToPremium = { viewModel.navigateTo(AppScreen.Premium()) },
                                    onNavigateToHelpNow = { viewModel.navigateTo(AppScreen.HelpNow) },
                                    onNavigateToLegal = { legalType ->
                                        viewModel.navigateTo(AppScreen.Legal(legalType))
                                    },
                                    onDeleteAllData = { viewModel.clearAllData() }
                                )
                            }
                        }
                    }
                }
            }

            is AppScreen.Result -> {
                ResultScreen(
                    result = screen.result,
                    lang = lang,
                    onBackClick = { viewModel.navigateBack() },
                    onCheckAnother = {
                        viewModel.selectTab(NavTab.CHECK)
                    },
                    onReportClick = { viewModel.openReportSheet(screen.result.id) },
                    onHelpNowClick = { viewModel.navigateTo(AppScreen.HelpNow) }
                )
            }

            is AppScreen.LearnDetail -> {
                LearnArticleScreen(
                    articleId = screen.articleId,
                    lang = lang,
                    onBackClick = { viewModel.navigateBack() },
                    onCheckNowClick = {
                        viewModel.selectTab(NavTab.CHECK)
                    },
                    onHelpNowClick = { viewModel.navigateTo(AppScreen.HelpNow) }
                )
            }

            is AppScreen.Premium -> {
                PremiumScreen(
                    bannerMsg = screen.bannerMsg,
                    isPremium = uiState.dailyUsage.isPremium,
                    lang = lang,
                    onBackClick = { viewModel.navigateBack() },
                    onHelpNowClick = { viewModel.navigateTo(AppScreen.HelpNow) },
                    onDevTogglePremium = { viewModel.togglePremiumDevSwitch() },
                    onShowToast = { viewModel.showToast(it) }
                )
            }

            is AppScreen.HelpNow -> {
                HelpNowScreen(
                    lang = lang,
                    onBackClick = { viewModel.navigateBack() }
                )
            }

            is AppScreen.Legal -> {
                LegalScreen(
                    type = screen.type,
                    lang = lang,
                    onBackClick = { viewModel.navigateBack() },
                    onHelpNowClick = { viewModel.navigateTo(AppScreen.HelpNow) }
                )
            }
        }

        // Report Sheet
        uiState.activeReportResultId?.let { resultId ->
            ReportBottomSheet(
                resultId = resultId,
                lang = lang,
                onDismiss = { viewModel.closeReportSheet() },
                onSubmit = { id, reason, comment ->
                    viewModel.submitReport(id, reason, comment)
                }
            )
        }
    }
}
