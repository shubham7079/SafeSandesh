package com.example.data.repository

import android.content.Context
import com.example.R
import com.example.data.model.AnalysisResult
import com.example.data.model.AppLanguage
import com.example.data.model.CheckKind
import com.example.data.model.HistoryEntry
import com.example.data.model.ReportSubmission
import com.example.data.model.RiskLevel
import com.example.data.model.UserSettings
import com.example.data.model.Verdict
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreRepository(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {

    constructor(context: Context) : this(
        db = FirebaseFirestore.getInstance(
            context.applicationContext.getString(R.string.firestore_database_id)
        ),
        auth = FirebaseAuth.getInstance()
    )

    private fun requireUserId(): String {
        return auth.currentUser?.uid ?: throw IllegalStateException("User not authenticated")
    }

    suspend fun saveCheck(entry: HistoryEntry) {
        val uid = auth.currentUser?.uid ?: return
        val docRef = db.collection("users").document(uid).collection("checks").document(entry.id)

        val data = hashMapOf(
            "id" to entry.id,
            "userId" to uid,
            "createdAt" to entry.createdAt,
            "kind" to entry.kind.name,
            "preview" to entry.preview,
            "verdict" to entry.result.verdict.name,
            "riskLevel" to entry.result.riskLevel.name,
            "scamType" to entry.result.scamType,
            "impersonates" to (entry.result.impersonates ?: ""),
            "reasons" to entry.result.reasons,
            "whatToDo" to entry.result.whatToDo
        )

        docRef.set(data).await()
    }

    fun observeCloudHistory(): Flow<List<HistoryEntry>> = callbackFlow {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            trySend(emptyList())
            close()
            return@callbackFlow
        }

        val listener = db.collection("users").document(uid).collection("checks")
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .limit(100)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val list = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        val id = doc.getString("id") ?: doc.id
                        val createdAt = doc.getLong("createdAt") ?: System.currentTimeMillis()
                        val kindStr = doc.getString("kind") ?: CheckKind.TEXT.name
                        val preview = doc.getString("preview") ?: ""
                        val verdictStr = doc.getString("verdict") ?: Verdict.UNSURE.name
                        val riskLevelStr = doc.getString("riskLevel") ?: RiskLevel.MEDIUM.name
                        val scamType = doc.getString("scamType") ?: ""
                        val impersonatesRaw = doc.getString("impersonates")
                        val impersonates = if (impersonatesRaw.isNullOrBlank()) null else impersonatesRaw

                        @Suppress("UNCHECKED_CAST")
                        val reasons = (doc.get("reasons") as? List<String>) ?: emptyList()
                        @Suppress("UNCHECKED_CAST")
                        val whatToDo = (doc.get("whatToDo") as? List<String>) ?: emptyList()

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
                        val kind = try {
                            CheckKind.valueOf(kindStr)
                        } catch (_: Exception) {
                            CheckKind.TEXT
                        }

                        val result = AnalysisResult(
                            id = id,
                            verdict = verdict,
                            riskLevel = riskLevel,
                            scamType = scamType,
                            impersonates = impersonates,
                            reasons = reasons,
                            whatToDo = whatToDo,
                            checkedAt = createdAt
                        )

                        HistoryEntry(
                            id = id,
                            createdAt = createdAt,
                            kind = kind,
                            preview = preview,
                            lang = AppLanguage.EN,
                            result = result
                        )
                    } catch (_: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(list)
            }

        awaitClose { listener.remove() }
    }

    suspend fun saveReport(report: ReportSubmission) {
        val uid = auth.currentUser?.uid ?: return
        val docRef = db.collection("reports").document(report.id)

        val data = hashMapOf(
            "id" to report.id,
            "userId" to uid,
            "resultId" to report.resultId,
            "reason" to report.reason,
            "comment" to report.comment,
            "createdAt" to report.timestamp
        )

        docRef.set(data).await()
    }

    suspend fun syncUserProfile(settings: UserSettings, isPremium: Boolean) {
        val uid = auth.currentUser?.uid ?: return
        val docRef = db.collection("users").document(uid)

        val data = hashMapOf(
            "userId" to uid,
            "lang" to settings.lang.code,
            "textScale" to settings.textScale.toDouble(),
            "isPremium" to isPremium,
            "updatedAt" to System.currentTimeMillis()
        )

        docRef.set(data).await()
    }

    suspend fun deleteCloudHistory() {
        val uid = auth.currentUser?.uid ?: return
        val snapshot = db.collection("users").document(uid).collection("checks").get().await()
        for (doc in snapshot.documents) {
            doc.reference.delete().await()
        }
    }
}
