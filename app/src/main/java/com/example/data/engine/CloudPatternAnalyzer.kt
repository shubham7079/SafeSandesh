package com.example.data.engine

import com.example.data.model.AnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.RiskLevel
import com.example.data.model.Verdict
import com.google.firebase.FirebaseApp
import com.google.firebase.ai.FirebaseAI
import com.google.firebase.ai.type.generationConfig
import kotlinx.coroutines.withTimeoutOrNull
import org.json.JSONObject
import java.util.UUID

object CloudPatternAnalyzer {

    /**
     * Analyzes input text against common phishing and fraud patterns using the Firebase Vertex AI SDK.
     * Evaluates threats such as KYC fraud, digital arrest, fake job offers, lottery scams,
     * UPI refunds, parcel withholding fees, and credential theft.
     */
    suspend fun analyzeInputText(
        inputText: String,
        lang: AppLanguage = AppLanguage.EN
    ): Result<AnalysisResult> {
        val trimmed = inputText.trim()
        if (trimmed.isEmpty()) {
            return Result.failure(IllegalArgumentException("Input text cannot be empty"))
        }

        return try {
            val firebaseAi = FirebaseAI.getInstance(FirebaseApp.getInstance())
            val model = firebaseAi.generativeModel(
                modelName = "gemini-2.5-flash",
                generationConfig = generationConfig {
                    temperature = 0.1f
                    responseMimeType = "application/json"
                }
            )

            val languageName = if (lang == AppLanguage.HI) "Hindi" else "English"
            val prompt = """
                Analyze the following text against common digital scam, phishing, and cyber fraud patterns in India (e.g. KYC expiry threats, fake job offers with registration fees, lottery winnings, digital arrest/police threats, parcel held for customs fee, UPI QR/PIN refund traps, screen-sharing app demands).
                
                Input text:
                "$trimmed"
                
                Respond ONLY with a JSON object adhering to this schema:
                {
                  "verdict": "SAFE" | "SUSPICIOUS" | "SCAM" | "UNSURE",
                  "riskLevel": "LOW" | "MEDIUM" | "HIGH",
                  "scamType": "KYC fraud" | "Lottery scam" | "Fake job offer" | "Digital arrest scam" | "UPI refund scam" | "Fake courier message" | "Phishing link" | "Investment scam" | "Routine message",
                  "impersonates": string or null,
                  "reasons": [string],
                  "whatToDo": [string]
                }
                
                Rules:
                - Do NOT claim 100% safety.
                - Reasons must be in $languageName, concise, each under 18 words.
                - What to do must be in $languageName, 3 to 4 actionable advice steps.
            """.trimIndent()

            val response = withTimeoutOrNull(9000L) {
                model.generateContent(prompt)
            }

            val responseText = response?.text
                ?: throw IllegalStateException("Received empty response from Firebase Vertex AI SDK")

            val cleanedJsonText = responseText.trim()
                .removePrefix("```json")
                .removePrefix("```")
                .removeSuffix("```")
                .trim()

            val json = JSONObject(cleanedJsonText)

            val verdictStr = json.optString("verdict", "UNSURE")
            val riskLevelStr = json.optString("riskLevel", "MEDIUM")
            val scamType = json.optString("scamType", "Scam check")
            val impersonates = if (json.isNull("impersonates")) null else json.optString("impersonates")

            val reasonsArray = json.optJSONArray("reasons")
            val reasons = mutableListOf<String>()
            if (reasonsArray != null) {
                for (i in 0 until reasonsArray.length()) {
                    reasons.add(reasonsArray.getString(i))
                }
            }

            val whatToDoArray = json.optJSONArray("whatToDo")
            val whatToDo = mutableListOf<String>()
            if (whatToDoArray != null) {
                for (i in 0 until whatToDoArray.length()) {
                    whatToDo.add(whatToDoArray.getString(i))
                }
            }

            val verdict = try {
                Verdict.valueOf(verdictStr)
            } catch (_: Exception) {
                Verdict.UNSURE
            }

            val riskLevel = try {
                RiskLevel.valueOf(riskLevelStr)
            } catch (_: Exception) {
                RiskLevel.MEDIUM
            }

            val finalReasons = if (reasons.isEmpty()) {
                listOf(if (lang == AppLanguage.HI) "पैटर्न जाँच पूरी हुई।" else "Pattern check complete.")
            } else reasons

            val finalSteps = if (whatToDo.isEmpty()) {
                RuleEngine.getStepsForVerdict(verdict, lang)
            } else whatToDo

            Result.success(
                AnalysisResult(
                    id = UUID.randomUUID().toString(),
                    verdict = verdict,
                    riskLevel = riskLevel,
                    scamType = scamType,
                    impersonates = impersonates,
                    reasons = finalReasons,
                    whatToDo = finalSteps,
                    checkedAt = System.currentTimeMillis()
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
