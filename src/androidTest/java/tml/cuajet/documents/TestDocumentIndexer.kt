package tml.cuajet.documents

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tml.cuajet.DocumentStorageTestBaseClass
import tml.cuajet.app.LogcatStreamer
import tml.cuajet.data.documents.DocumentIndexer
import tml.cuajet.data.documents.DocumentIndexerFilterResult
import tml.libs.cku.event.TaskResultListener
import tml.libs.cku.io.StaticLogger

@RunWith(AndroidJUnit4::class)
class TestDocumentIndexer : DocumentStorageTestBaseClass() {
    @Before
    fun setup() {
        StaticLogger.showAllClass = true
        StaticLogger.logStreamer = LogcatStreamer()
        StaticLogger.curAppTag = "UNITTEST"
        StaticLogger.I(this, "setup...")
    }

    @Test
    fun test_one_document() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val indexer = DocumentIndexer(appContext)
        indexer.filter(hashMapOf<String, Any>("vip" to true, "opened" to false), object :
            TaskResultListener<DocumentIndexerFilterResult, String> {
            override fun onSuccess(data: DocumentIndexerFilterResult) {

            }

            override fun onError(data: String?) {
            }
        })
    }

}