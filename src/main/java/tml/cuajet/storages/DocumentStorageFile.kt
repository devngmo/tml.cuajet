@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package tml.cuajet.storages

import android.content.Context
import android.content.SharedPreferences
import android.os.Environment
import tml.libs.cku.TaskResultListener
import tml.libs.cku.io.FileUtils
import tml.libs.cku.io.StaticLogger
import tml.libs.cku.storages.DocumentStorageInterface
import java.io.ByteArrayInputStream
import java.io.File
import java.io.InputStream
import java.util.*
@Suppress("unused")
class DocumentStorageFile(val context: Context) : DocumentStorageInterface {
    init {

    }

    companion object {
        const val CURRENT_PACK_NUMBER = "current.pack.number"
    }

    val sp : SharedPreferences
        get() = context.getSharedPreferences("DSF.INDEXER", Context.MODE_PRIVATE)

    val storageFolder : File
    get() {
        val sd = Environment.getExternalStorageDirectory()
        return File(sd, "DocStorage")
    }

    val currentFolderPack: File
        get() {
            return getFolderPack(currentPackNumber)
        }

    val currentPackNumber: Int
        get() {
            return sp.getInt(CURRENT_PACK_NUMBER, 1)
        }

    fun getFolderPack(packNo: Int):File {
        return File(storageFolder, "$packNo")
    }

    override fun add(fileInfo: String, data: String, callback: TaskResultListener<String, String>) {
        add(fileInfo, data.toByteArray(), callback)
    }

    override fun add(fileInfo: String, data: ByteArray, callback: TaskResultListener<String, String>) {
        val docID = UUID.randomUUID().toString()
        val packFolder = currentFolderPack
        if (!packFolder.exists())
            packFolder.mkdirs()

        // write file data
        val fData = File(packFolder, docID)
        if (fData.exists())
            fData.delete()

        FileUtils.writeAllBytes(data, fData)

        sp.edit()
            .putInt(docID, currentPackNumber)
            // write file manifest
            .putString("${docID}_info", fileInfo)
            .apply()
        val filesInFolder = packFolder.list()
        filesInFolder?.let {
            if (it.size > 9999) {
                sp.edit().putInt(CURRENT_PACK_NUMBER, currentPackNumber + 1).apply()
            }
        }
        callback.onSuccess(docID)
    }

    override fun add(
        fileInfo: String,
        data: InputStream,
        callback: TaskResultListener<String, String>
    ) {
        add(fileInfo, data.readBytes(), callback)
    }

    override fun update(
        id: String,
        fileInfo: String,
        data: String,
        callback: TaskResultListener<Boolean, String>
    ) {
        update(id, fileInfo, data.toByteArray(), callback)
    }

    override fun update(
        id: String,
        fileInfo: String,
        data : ByteArray,
        callback: TaskResultListener<Boolean, String>
    ) {
        val packNo = sp.getInt(id, -1)
        if (packNo == -1) {
            callback.onError("File not found: $id")
            return
        }
        val folder = getFolderPack(packNo)
        val fData = File(folder, id)
        if (fData.exists())
            fData.delete()

        FileUtils.writeAllBytes(data, fData)
        callback.onSuccess(true)
    }

    override fun update(
        id: String,
        fileInfo: String,
        data: InputStream,
        callback: TaskResultListener<Boolean, String>
    ) {
        update(id, fileInfo, data.readBytes(), callback)
    }

    override fun getFileDataAsText(id: String, callback: TaskResultListener<String, String>) {
        getFileDataAsBytes(id, object : TaskResultListener<ByteArray, String> {
            override fun onSuccess(data: ByteArray) {
                callback.onSuccess(String(data, Charsets.UTF_8))
            }

            override fun onError(data: String?) {
                callback.onError(data)
            }
        })
    }

    override fun getFileInfo(id: String, callback: TaskResultListener<String, String>) {
        val info = sp.getString("${id}_info", null)
        if (info == null)
            callback.onError("sp.getString(${id}_info) return null")
        else
            callback.onSuccess(info)
    }

    override fun getFileDataAsBytes(id: String, callback: TaskResultListener<ByteArray, String>) {
        val packNo = sp.getInt(id, -1)
        if (packNo == -1) {
            callback.onError("File not found: $id")
            return
        }
        val folder = getFolderPack(packNo)
        val f = File(folder, id)
        val bytes = FileUtils.readAllBytes(f)
        if (bytes == null)
            StaticLogger.E(this, "Read all bytes from ${f.absolutePath} return null")
        else
            callback.onSuccess(bytes)
    }

    override fun getFileDataAsStream(
        id: String,
        callback: TaskResultListener<InputStream, String>
    ) {
        getFileDataAsBytes(id, object : TaskResultListener<ByteArray, String> {
            override fun onSuccess(data: ByteArray) {
                val stream = ByteArrayInputStream(data)
                callback.onSuccess(stream)
            }

            override fun onError(data: String?) {
                callback.onError(data)
            }
        })
    }
}