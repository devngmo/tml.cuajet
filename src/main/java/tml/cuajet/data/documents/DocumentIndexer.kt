package tml.cuajet.data.documents

import android.content.Context
import tml.libs.cku.TaskResultListener
import java.util.*
import kotlin.concurrent.thread

class DocumentIndexerFilterResult(val hits: Long, val totalHits: Long, val scrollID: String)

class DocumentIndexer(val context: Context) {

    init {

    }

    fun filter(hashMapOf: HashMap<String, Any>, callback: TaskResultListener<DocumentIndexerFilterResult, String>) {
        var nHits = 0L
        val scrollID = UUID.randomUUID().toString()

        thread(start=true) {

            val result = DocumentIndexerFilterResult(nHits, scrollID)
            callback.onSuccess(result)
        }
    }
}