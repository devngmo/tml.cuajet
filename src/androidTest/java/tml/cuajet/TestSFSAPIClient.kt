package tml.cuajet

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tml.cuajet.apis.SFSAPIClient
import tml.cuajet.app.LogcatStreamer
import tml.libs.cku.event.TaskResultListener
import tml.libs.cku.io.StaticLogger

@RunWith(AndroidJUnit4::class)
class TestSFSAPIClient {
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
        val apiClient = SFSAPIClient()
        val K_FILE_CONTENT = "Sample document"
        val docInfo = MockDataUtils.createBook()
        apiClient.v4_addFile(docInfo, object : TaskResultListener<String, String> {
            override fun onSuccess(data: String) {

            }

            override fun onError(data: String?) {
            }
        })
    }
}