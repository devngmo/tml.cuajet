package tml.cuajet

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import com.google.gson.reflect.TypeToken
import org.junit.Assert
import tml.libs.cku.event.TaskResultListener
import tml.libs.cku.storages.DocumentStorageInterface
import java.lang.reflect.Type

open class DocumentStorageTestBaseClass {
    lateinit var docStorage: DocumentStorageInterface
    val BOOK_1_CONTENT = "Content of book 1"

    fun test_add_and_get_one_document() {
        val bookInfo = MockDataUtils.createBook()
        var docID :String? = null

        docStorage.add(Gson().toJson(bookInfo), BOOK_1_CONTENT, object :
            TaskResultListener<String, String> {
            override fun onSuccess(data: String) {
                docID = data
            }

            override fun onError(data: String?) {
                Assert.fail(data)
            }
        })

        var b2 : LinkedTreeMap<String, String>? = null
        val entClassType: Type = object :
            TypeToken<LinkedTreeMap<String, String>>() {}.type

        docStorage.getFileInfo(docID!!, object : TaskResultListener<String, String> {
            override fun onSuccess(data: String) {
                b2 = Gson().fromJson<LinkedTreeMap<String, String>>(data, entClassType)
            }

            override fun onError(data: String?) {
                Assert.fail(data)
            }
        })
        b2!!.let {
            Assert.assertEquals(bookInfo["id"], it["id"])
            Assert.assertEquals(bookInfo["title"], it["title"])
            Assert.assertEquals(bookInfo["summary"], it["summary"])
            Assert.assertEquals(bookInfo["maxPageNo"], it["maxPageNo"])
            Assert.assertEquals(bookInfo["status"], it["status"])
        }

        docStorage.getFileDataAsText(docID!!, object : TaskResultListener<String, String> {
            override fun onSuccess(data: String) {
                Assert.assertEquals(BOOK_1_CONTENT, data)
            }

            override fun onError(data: String?) {
                Assert.fail(data)
            }
        })
    }
}