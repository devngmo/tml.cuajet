package tml.cuajet

import android.Manifest
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import tml.cuajet.app.LogcatStreamer
import tml.cuajet.storages.DocumentStorageFile
import tml.libs.cku.TaskResultListener
import tml.libs.cku.io.StaticLogger
import java.lang.reflect.Type


@RunWith(AndroidJUnit4::class)
class TestDocumentStorageFile : DocumentStorageTestBaseClass() {
    @Rule
    @JvmField
    val rules = GrantPermissionRule.grant(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )

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
        docStorage = DocumentStorageFile(appContext)
        test_add_and_get_one_document()
    }

}