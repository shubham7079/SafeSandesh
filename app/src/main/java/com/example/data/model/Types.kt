package com.example.data.model

enum class Verdict(val rawValue: String) {
    SAFE("SAFE"),
    SUSPICIOUS("SUSPICIOUS"),
    SCAM("SCAM"),
    UNSURE("UNSURE")
}

enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH
}

enum class CheckKind {
    TEXT,
    IMAGE,
    LINK
}

enum class AppLanguage(val code: String, val displayName: String) {
    EN("en", "English"),
    HI("hi", "हिन्दी")
}

data class AnalysisResult(
    val id: String,
    val verdict: Verdict,
    val riskLevel: RiskLevel,
    val scamType: String,
    val impersonates: String? = null,
    val reasons: List<String>,
    val whatToDo: List<String>,
    val checkedAt: Long = System.currentTimeMillis()
)

data class HistoryEntry(
    val id: String,
    val createdAt: Long,
    val kind: CheckKind,
    val preview: String,
    val lang: AppLanguage,
    val result: AnalysisResult
)

data class UserSettings(
    val lang: AppLanguage = AppLanguage.EN,
    val textScale: Float = 1.0f,
    val onboardingDone: Boolean = false
)

data class DailyUsage(
    val dateString: String,
    val count: Int,
    val isPremium: Boolean = false
)

data class ReportSubmission(
    val id: String,
    val resultId: String,
    val reason: String,
    val comment: String,
    val timestamp: Long = System.currentTimeMillis()
)

data class LearnArticle(
    val id: String,
    val titleEn: String,
    val titleHi: String,
    val summaryEn: String,
    val summaryHi: String,
    val introEn: String,
    val introHi: String,
    val warningSignsEn: List<String>,
    val warningSignsHi: List<String>,
    val whatToDoEn: List<String>,
    val whatToDoHi: List<String>
)
