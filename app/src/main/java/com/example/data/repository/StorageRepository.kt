package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.CheckKind
import com.example.data.model.DailyUsage
import com.example.data.model.HistoryEntry
import com.example.data.model.RiskLevel
import com.example.data.model.UserSettings
import com.example.data.model.Verdict
import org.json.JSONArray
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class StorageRepository(private val context: Context) {

    private val prefsName = "ss_preferences"
    private var prefs: SharedPreferences? = null

    // In-memory fallback
    private val memoryStore = mutableMapOf<String, String>()

    init {
        try {
            prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        } catch (_: Exception) {
            prefs = null
        }
    }

    private fun getString(key: String, defaultVal: String = ""): String {
        return try {
            prefs?.getString(key, defaultVal) ?: memoryStore[key] ?: defaultVal
        } catch (_: Exception) {
            memoryStore[key] ?: defaultVal
        }
    }

    private fun setString(key: String, value: String) {
        memoryStore[key] = value
        try {
            prefs?.edit()?.putString(key, value)?.apply()
        } catch (_: Exception) {
            // Keep memory copy
        }
    }

    // --- Settings ---

    fun loadSettings(): UserSettings {
        val langCode = getString("ss_lang", "en")
        val textScale = try {
            getString("ss_text_scale", "1.0").toFloat()
        } catch (_: Exception) {
            1.0f
        }
        val onboardingDone = getString("ss_onboarding_done", "false") == "true"
        val lang = if (langCode == "hi") AppLanguage.HI else AppLanguage.EN
        return UserSettings(lang = lang, textScale = textScale, onboardingDone = onboardingDone)
    }

    fun saveSettings(settings: UserSettings) {
        setString("ss_lang", settings.lang.code)
        setString("ss_text_scale", settings.textScale.toString())
        setString("ss_onboarding_done", settings.onboardingDone.toString())
    }

    // --- Daily Usage & Free Checks ---

    private fun getTodayDateString(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getDailyUsage(): DailyUsage {
        val today = getTodayDateString()
        val storedDate = getString("ss_usage_date", today)
        val isPremium = getString("ss_is_premium", "false") == "true"

        val count = if (storedDate == today) {
            try {
                getString("ss_usage_count", "0").toInt()
            } catch (_: Exception) {
                0
            }
        } else {
            // Reset at local midnight
            0
        }

        return DailyUsage(
            dateString = today,
            count = count,
            isPremium = isPremium
        )
    }

    fun incrementDailyUsage(): DailyUsage {
        val current = getDailyUsage()
        val updatedCount = current.count + 1
        setString("ss_usage_date", current.dateString)
        setString("ss_usage_count", updatedCount.toString())
        return current.copy(count = updatedCount)
    }

    fun setPremium(premium: Boolean) {
        setString("ss_is_premium", premium.toString())
    }

    // --- History (Max 100 items) ---

    fun loadHistory(): List<HistoryEntry> {
        val rawJson = getString("ss_history", "[]")
        val list = mutableListOf<HistoryEntry>()
        try {
            val jsonArray = JSONArray(rawJson)
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val resultObj = obj.getJSONObject("result")

                val reasonsArray = resultObj.getJSONArray("reasons")
                val reasons = mutableListOf<String>()
                for (r in 0 until reasonsArray.length()) {
                    reasons.add(reasonsArray.getString(r))
                }

                val whatToDoArray = resultObj.getJSONArray("whatToDo")
                val whatToDo = mutableListOf<String>()
                for (w in 0 until whatToDoArray.length()) {
                    whatToDo.add(whatToDoArray.getString(w))
                }

                val verdict = try {
                    Verdict.valueOf(resultObj.getString("verdict"))
                } catch (_: Exception) {
                    Verdict.UNSURE
                }

                val riskLevel = try {
                    RiskLevel.valueOf(resultObj.getString("riskLevel"))
                } catch (_: Exception) {
                    RiskLevel.MEDIUM
                }

                val result = AnalysisResult(
                    id = resultObj.getString("id"),
                    verdict = verdict,
                    riskLevel = riskLevel,
                    scamType = resultObj.getString("scamType"),
                    impersonates = if (resultObj.isNull("impersonates")) null else resultObj.getString("impersonates"),
                    reasons = reasons,
                    whatToDo = whatToDo,
                    checkedAt = resultObj.optLong("checkedAt", System.currentTimeMillis())
                )

                val kind = try {
                    CheckKind.valueOf(obj.getString("kind"))
                } catch (_: Exception) {
                    CheckKind.TEXT
                }

                val lang = if (obj.optString("lang", "en") == "hi") AppLanguage.HI else AppLanguage.EN

                list.add(
                    HistoryEntry(
                        id = obj.getString("id"),
                        createdAt = obj.getLong("createdAt"),
                        kind = kind,
                        preview = obj.getString("preview"),
                        lang = lang,
                        result = result
                    )
                )
            }
        } catch (_: Exception) {
            // Return empty list on parse issue
        }
        return list
    }

    fun saveHistoryEntry(entry: HistoryEntry) {
        val existing = loadHistory().toMutableList()
        // Add newest at beginning
        existing.add(0, entry)
        // Keep max 100 items
        val trimmed = if (existing.size > 100) existing.take(100) else existing

        try {
            val jsonArray = JSONArray()
            trimmed.forEach { item ->
                val obj = JSONObject()
                obj.put("id", item.id)
                obj.put("createdAt", item.createdAt)
                obj.put("kind", item.kind.name)
                obj.put("preview", item.preview)
                obj.put("lang", item.lang.code)

                val resultObj = JSONObject()
                resultObj.put("id", item.result.id)
                resultObj.put("verdict", item.result.verdict.name)
                resultObj.put("riskLevel", item.result.riskLevel.name)
                resultObj.put("scamType", item.result.scamType)
                resultObj.put("impersonates", item.result.impersonates ?: JSONObject.NULL)
                resultObj.put("checkedAt", item.result.checkedAt)

                val reasonsArray = JSONArray()
                item.result.reasons.forEach { reasonsArray.put(it) }
                resultObj.put("reasons", reasonsArray)

                val whatToDoArray = JSONArray()
                item.result.whatToDo.forEach { whatToDoArray.put(it) }
                resultObj.put("whatToDo", whatToDoArray)

                obj.put("result", resultObj)
                jsonArray.put(obj)
            }
            setString("ss_history", jsonArray.toString())
        } catch (_: Exception) {
            // Graceful fallback
        }
    }

    fun clearHistory() {
        setString("ss_history", "[]")
    }

    fun clearAllData() {
        try {
            prefs?.edit()?.clear()?.apply()
        } catch (_: Exception) {
            // Ignore
        }
        memoryStore.clear()
    }
}
