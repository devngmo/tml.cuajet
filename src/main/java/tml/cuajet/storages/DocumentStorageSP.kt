package tml.cuajet.storages

import android.content.SharedPreferences
import tml.libs.cku.event.TaskResultListener
import tml.libs.cku.storages.DocumentStorageInterface
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.util.*

@Suppress("unused")
class DocumentStorageSP(val sp: SharedPreferences) : DocumentStorageInterface {
    override fun add(fileInfo:String, data: String, callback: TaskResultListener<String, String>) {
        val docID = UUID.randomUUID().toString()
        sp.edit()
            .putString("${docID}_info", fileInfo)
            .putString("${docID}_data", data)
            .apply()
        callback.onSuccess(docID)
    }

    override fun add(
        fileInfo: String,
        data: ByteArray,
        callback: TaskResultListener<String, String>
    ) {
        add(fileInfo, String(data, kotlin.text.Charsets.UTF_8), callback)
    }

    override fun add(
        fileInfo: String,
        data: InputStream,
        callback: TaskResultListener<String, String>
    ) {
        val bytes = data.readBytes()
        add(fileInfo, bytes, callback)
    }

    override fun update(
        id: String,
        docInfo:String,
        data: String,
        listener: TaskResultListener<Boolean, String>
    ) {
        sp.edit()
            .putString("${id}_info", docInfo)
            .putString("${id}_data", data)
            .apply()
        listener.onSuccess(true)
    }

    override fun update(
        id: String,
        fileInfo: String,
        data: ByteArray,
        callback: TaskResultListener<Boolean, String>
    ) {
        update(id, fileInfo, String(data, kotlin.text.Charsets.UTF_8), callback)
    }

    override fun update(
        id: String,
        fileInfo: String,
        data: InputStream,
        callback: TaskResultListener<Boolean, String>
    ) {
        update(id, fileInfo, String(data.readBytes(), kotlin.text.Charsets.UTF_8), callback)
    }

    override fun getFileDataAsText(id: String, callback: TaskResultListener<String, String>) {
        val data = sp.getString("${id}_data", null)
        if (data == null)
            callback.onError("sp.getstring(${id}_data) is null")
        else
            callback.onSuccess(data)
    }

    override fun getFileInfo(id: String, callback: TaskResultListener<String, String>) {
        val data = sp.getString("${id}_info", null)
        if (data == null)
            callback.onError("sp.getstring(${id}_info) is null")
        else
            callback.onSuccess(data)
    }

    override fun getFileDataAsBytes(id: String, callback: TaskResultListener<ByteArray, String>) {
        val data = sp.getString("${id}_data", null)
        if (data == null)
            callback.onError("sp.getstring(${id}_data) is null")
        else
            callback.onSuccess(data.toByteArray())
    }

    override fun getFileDataAsStream(
        id: String,
        callback: TaskResultListener<InputStream, String>
    ) {
        val data = sp.getString("${id}_data", null)
        if (data == null)
            callback.onError("sp.getstring(${id}_data) is null")
        else {
            val stream = ByteArrayInputStream(data.toByteArray())
            callback.onSuccess(stream)
        }
    }

//    override fun getByIDAsText(id: String, callback: TaskResultListener<String, String>) {
//        callback.onSuccess(sp.getString(id, null))
//    }
//
//    override fun getByIDAsBytes(id: String, callback: TaskResultListener<ByteArray, String>) {
//        val text = sp.getString(id, null)
//        if (text == null)
//            callback.onError(null)
//        else
//            callback.onSuccess(text.toByteArray())
//    }
}