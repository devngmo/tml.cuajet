package tml.cuajet

import android.app.Activity
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tml.cuajet.app.LogcatStreamer
import tml.cuajet.storages.DocumentStorageSP
import tml.libs.cku.io.StaticLogger

@RunWith(AndroidJUnit4::class)
class TestDocumentStorageSP : DocumentStorageTestBaseClass() {
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
        docStorage = DocumentStorageSP(appContext.getSharedPreferences("TEST_SP", Activity.MODE_PRIVATE))
        test_add_and_get_one_document()
    }

}