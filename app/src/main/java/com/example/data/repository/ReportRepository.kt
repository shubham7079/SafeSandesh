package com.example.data.repository

import android.content.Context
import com.example.data.model.ReportSubmission
import org.json.JSONArray
import org.json.JSONObject
import java.util.UUID

class ReportRepository(private val context: Context) {

    private val prefsName = "ss_reports_pref"
    private val key = "ss_reports"

    fun reportResult(resultId: String, reason: String, comment: String): Boolean {
        // NOTE: A production server call to submit the report for review will be inserted here.
        return try {
            val prefs = context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            val currentRaw = prefs.getString(key, "[]") ?: "[]"
            val array = JSONArray(currentRaw)

            val report = ReportSubmission(
                id = UUID.randomUUID().toString(),
                resultId = resultId,
                reason = reason,
                comment = comment.take(300),
                timestamp = System.currentTimeMillis()
            )

            val obj = JSONObject().apply {
                put("id", report.id)
                put("resultId", report.resultId)
                put("reason", report.reason)
                put("comment", report.comment)
                put("timestamp", report.timestamp)
            }
            array.put(obj)

            prefs.edit().putString(key, array.toString()).apply()
            true
        } catch (_: Exception) {
            true
        }
    }
}
