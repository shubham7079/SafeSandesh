package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.auth.AuthManager
import com.example.data.engine.CheckAnalyzer
import com.example.data.engine.Redactor
import com.example.data.i18n.I18n
import com.example.data.model.AnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.CheckKind
import com.example.data.model.DailyUsage
import com.example.data.model.HistoryEntry
import com.example.data.model.ReportSubmission
import com.example.data.model.UserSettings
import com.example.data.repository.FirestoreRepository
import com.example.data.repository.ReportRepository
import com.example.data.repository.StorageRepository
import com.example.ui.components.NavTab
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.UUID

enum class LegalType {
    PRIVACY,
    TERMS,
    DISCLAIMER
}

sealed interface AppScreen {
    object LanguageSelect : AppScreen
    object Onboarding : AppScreen
    object Main : AppScreen
    data class Result(val result: AnalysisResult, val preview: String) : AppScreen
    data class LearnDetail(val articleId: String) : AppScreen
    data class Premium(val bannerMsg: String? = null) : AppScreen
    object HelpNow : AppScreen
    data class Legal(val type: LegalType) : AppScreen
}

data class MainUiState(
    val currentScreen: AppScreen = AppScreen.Main,
    val currentTab: NavTab = NavTab.CHECK,
    val settings: UserSettings = UserSettings(),
    val dailyUsage: DailyUsage = DailyUsage("", 0),
    val historyList: List<HistoryEntry> = emptyList(),
    val isAnalyzing: Boolean = false,
    val analysisError: String? = null,
    val wasMaskedNotice: Boolean = false,
    val activeReportResultId: String? = null,
    val toastMessage: String? = null,
    val currentUser: FirebaseUser? = null,
    val isAuthLoading: Boolean = false
)

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val storageRepo = StorageRepository(application)
    private val reportRepo = ReportRepository(application)
    private val firestoreRepo = FirestoreRepository(application)
    private val authManager = AuthManager(application)

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val screenStack = mutableListOf<AppScreen>()

    init {
        loadInitialData()
        observeAuth()
    }

    private fun loadInitialData() {
        val settings = storageRepo.loadSettings()
        val usage = storageRepo.getDailyUsage()
        val history = storageRepo.loadHistory()

        val initialScreen = when {
            !settings.onboardingDone -> AppScreen.LanguageSelect
            else -> AppScreen.Main
        }

        _uiState.value = MainUiState(
            currentScreen = initialScreen,
            currentTab = NavTab.CHECK,
            settings = settings,
            dailyUsage = usage,
            historyList = history,
            currentUser = authManager.currentUser
        )
    }

    private fun observeAuth() {
        viewModelScope.launch {
            authManager.observeAuthState().collect { user ->
                _uiState.value = _uiState.value.copy(currentUser = user)
                if (user != null) {
                    // Sync user preferences
                    try {
                        firestoreRepo.syncUserProfile(
                            _uiState.value.settings,
                            _uiState.value.dailyUsage.isPremium
                        )
                    } catch (_: Exception) {}

                    // Listen to cloud checks
                    launch {
                        firestoreRepo.observeCloudHistory().collect { cloudHistory ->
                            if (cloudHistory.isNotEmpty()) {
                                // Merge cloud history with local history
                                val local = storageRepo.loadHistory()
                                val combinedIds = mutableSetOf<String>()
                                val merged = mutableListOf<HistoryEntry>()

                                cloudHistory.forEach {
                                    if (combinedIds.add(it.id)) merged.add(it)
                                }
                                local.forEach {
                                    if (combinedIds.add(it.id)) merged.add(it)
                                }
                                merged.sortByDescending { it.createdAt }
                                val trimmed = merged.take(100)
                                _uiState.value = _uiState.value.copy(historyList = trimmed)
                            }
                        }
                    }
                }
            }
        }
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isAuthLoading = true)
            val result = authManager.signInWithGoogle()
            _uiState.value = _uiState.value.copy(isAuthLoading = false)
            result.onSuccess { user ->
                val name = user.displayName ?: user.email ?: ""
                _uiState.value = _uiState.value.copy(
                    currentUser = user,
                    toastMessage = if (_uiState.value.settings.lang == AppLanguage.HI) {
                        "Google से साइन इन सफल"
                    } else {
                        "Signed in with Google successfully"
                    }
                )
            }.onFailure {
                // If cancelled by user, do not show error
                if (it !is androidx.credentials.exceptions.GetCredentialCancellationException) {
                    _uiState.value = _uiState.value.copy(
                        toastMessage = it.message ?: "Sign-in failed"
                    )
                }
            }
        }
    }

    fun signOut() {
        authManager.signOut()
        _uiState.value = _uiState.value.copy(
            currentUser = null,
            toastMessage = if (_uiState.value.settings.lang == AppLanguage.HI) {
                "साइन आउट हो गया"
            } else {
                "Signed out"
            }
        )
    }

    fun setLanguage(lang: AppLanguage) {
        val updated = _uiState.value.settings.copy(lang = lang)
        storageRepo.saveSettings(updated)
        _uiState.value = _uiState.value.copy(settings = updated)
        if (_uiState.value.currentUser != null) {
            viewModelScope.launch {
                try {
                    firestoreRepo.syncUserProfile(updated, _uiState.value.dailyUsage.isPremium)
                } catch (_: Exception) {}
            }
        }
    }

    fun setTextScale(scale: Float) {
        val updated = _uiState.value.settings.copy(textScale = scale)
        storageRepo.saveSettings(updated)
        _uiState.value = _uiState.value.copy(settings = updated)
        if (_uiState.value.currentUser != null) {
            viewModelScope.launch {
                try {
                    firestoreRepo.syncUserProfile(updated, _uiState.value.dailyUsage.isPremium)
                } catch (_: Exception) {}
            }
        }
    }

    fun startOnboarding() {
        navigateTo(AppScreen.Onboarding)
    }

    fun completeOnboarding() {
        val updated = _uiState.value.settings.copy(onboardingDone = true)
        storageRepo.saveSettings(updated)
        screenStack.clear()
        _uiState.value = _uiState.value.copy(
            settings = updated,
            currentScreen = AppScreen.Main,
            currentTab = NavTab.CHECK
        )
    }

    fun selectTab(tab: NavTab) {
        _uiState.value = _uiState.value.copy(
            currentScreen = AppScreen.Main,
            currentTab = tab
        )
    }

    fun navigateTo(screen: AppScreen) {
        screenStack.add(_uiState.value.currentScreen)
        _uiState.value = _uiState.value.copy(currentScreen = screen)
    }

    fun navigateBack(): Boolean {
        if (screenStack.isNotEmpty()) {
            val previous = screenStack.removeAt(screenStack.size - 1)
            _uiState.value = _uiState.value.copy(currentScreen = previous)
            return true
        }
        if (_uiState.value.currentScreen !is AppScreen.Main) {
            _uiState.value = _uiState.value.copy(currentScreen = AppScreen.Main)
            return true
        }
        return false
    }

    fun runCheck(kind: CheckKind, text: String) {
        val currentUsage = _uiState.value.dailyUsage
        val lang = _uiState.value.settings.lang

        // Daily limit check: 5 checks per day for free users
        if (!currentUsage.isPremium && currentUsage.count >= 5) {
            navigateTo(AppScreen.Premium(I18n.t("check.limit_used", lang)))
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isAnalyzing = true,
                analysisError = null,
                wasMaskedNotice = false
            )

            try {
                // Apply Redactor before analysis
                val redactResult = Redactor.redact(text)
                val cleanText = redactResult.cleaned

                val result = CheckAnalyzer.analyzeMessage(
                    text = cleanText,
                    kind = kind,
                    lang = lang
                )

                // Update usage
                val updatedUsage = storageRepo.incrementDailyUsage()

                // Save to history
                val preview = cleanText.ifBlank {
                    if (lang == AppLanguage.HI) "स्क्रीनशॉट जाँच" else "Screenshot check"
                }

                val historyEntry = HistoryEntry(
                    id = UUID.randomUUID().toString(),
                    createdAt = System.currentTimeMillis(),
                    kind = kind,
                    preview = preview,
                    lang = lang,
                    result = result
                )

                storageRepo.saveHistoryEntry(historyEntry)
                val updatedHistory = storageRepo.loadHistory()

                // If authenticated, also sync to Firestore
                if (_uiState.value.currentUser != null) {
                    try {
                        firestoreRepo.saveCheck(historyEntry)
                    } catch (_: Exception) {}
                }

                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    dailyUsage = updatedUsage,
                    historyList = updatedHistory,
                    wasMaskedNotice = redactResult.masked
                )

                navigateTo(AppScreen.Result(result, preview))

            } catch (_: Exception) {
                _uiState.value = _uiState.value.copy(
                    isAnalyzing = false,
                    analysisError = I18n.t("check.error", lang)
                )
            }
        }
    }

    fun openReportSheet(resultId: String) {
        _uiState.value = _uiState.value.copy(activeReportResultId = resultId)
    }

    fun closeReportSheet() {
        _uiState.value = _uiState.value.copy(activeReportResultId = null)
    }

    fun submitReport(resultId: String, reason: String, comment: String) {
        val lang = _uiState.value.settings.lang
        reportRepo.reportResult(resultId, reason, comment)

        if (_uiState.value.currentUser != null) {
            viewModelScope.launch {
                try {
                    val report = ReportSubmission(
                        id = UUID.randomUUID().toString(),
                        resultId = resultId,
                        reason = reason,
                        comment = comment,
                        timestamp = System.currentTimeMillis()
                    )
                    firestoreRepo.saveReport(report)
                } catch (_: Exception) {}
            }
        }

        _uiState.value = _uiState.value.copy(
            activeReportResultId = null,
            toastMessage = I18n.t("report.thanks", lang)
        )
    }

    fun deleteAllHistory() {
        storageRepo.clearHistory()
        _uiState.value = _uiState.value.copy(historyList = emptyList())
        if (_uiState.value.currentUser != null) {
            viewModelScope.launch {
                try {
                    firestoreRepo.deleteCloudHistory()
                } catch (_: Exception) {}
            }
        }
    }

    fun clearAllData() {
        storageRepo.clearAllData()
        if (_uiState.value.currentUser != null) {
            viewModelScope.launch {
                try {
                    firestoreRepo.deleteCloudHistory()
                } catch (_: Exception) {}
            }
        }
        screenStack.clear()
        val defaultSettings = UserSettings(lang = AppLanguage.EN, textScale = 1.0f, onboardingDone = false)
        _uiState.value = MainUiState(
            currentScreen = AppScreen.LanguageSelect,
            settings = defaultSettings,
            dailyUsage = DailyUsage("", 0),
            historyList = emptyList(),
            currentUser = authManager.currentUser,
            toastMessage = I18n.t("settings.data_cleared", AppLanguage.EN)
        )
    }

    fun togglePremiumDevSwitch() {
        val current = _uiState.value.dailyUsage
        val toggled = !current.isPremium
        storageRepo.setPremium(toggled)
        _uiState.value = _uiState.value.copy(
            dailyUsage = current.copy(isPremium = toggled),
            toastMessage = if (toggled) "Developer switch: Premium enabled" else "Developer switch: Free plan"
        )
        if (_uiState.value.currentUser != null) {
            viewModelScope.launch {
                try {
                    firestoreRepo.syncUserProfile(_uiState.value.settings, toggled)
                } catch (_: Exception) {}
            }
        }
    }

    fun showToast(msg: String) {
        _uiState.value = _uiState.value.copy(toastMessage = msg)
    }

    fun clearToast() {
        _uiState.value = _uiState.value.copy(toastMessage = null)
    }
}
