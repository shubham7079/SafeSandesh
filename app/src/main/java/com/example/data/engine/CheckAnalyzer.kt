package com.example.data.engine

import com.example.data.model.AnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.CheckKind
import kotlinx.coroutines.delay

class CheckAnalysisException(message: String) : Exception(message)

object CheckAnalyzer {

    /**
     * Calls the Firebase Vertex AI SDK to analyze input text against common phishing and fraud patterns.
     */
    suspend fun callSecureBackend(
        text: String,
        kind: CheckKind,
        lang: AppLanguage
    ): AnalysisResult {
        val result = CloudPatternAnalyzer.analyzeInputText(text, lang)
        return result.getOrThrow()
    }

    suspend fun analyzeMessage(
        text: String,
        kind: CheckKind,
        lang: AppLanguage
    ): AnalysisResult {
        val trimmed = text.trim()
        if (trimmed.isEmpty() && kind != CheckKind.IMAGE) {
            throw CheckAnalysisException("Input text cannot be empty")
        }

        // Simulate smooth UI latency
        delay(1050L)

        return try {
            if (kind == CheckKind.TEXT) {
                callSecureBackend(trimmed, kind, lang)
            } else {
                RuleEngine.evaluate(trimmed, kind, lang)
            }
        } catch (_: Exception) {
            // Deterministic local fallback
            RuleEngine.evaluate(
                input = trimmed,
                kind = kind,
                lang = lang
            )
        }
    }
}
