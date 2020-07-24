package tml.cuajet

import android.app.Activity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tml.cuajet.apis.SFSAPIClient
import tml.cuajet.app.LogcatStreamer
import tml.cuajet.storages.DocumentStorageCloud
import tml.cuajet.storages.DocumentStorageSP
import tml.libs.cku.TaskResultListener
import tml.libs.cku.io.StaticLogger
import java.lang.reflect.Type

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
        apiClient.v4_addFile(docInfo)
    }
}