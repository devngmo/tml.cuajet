package tml.cuajet.storages

import tml.libs.cku.TaskResultListener
import tml.libs.cku.storages.DocumentStorageInterface
import java.io.InputStream

@Suppress("unused")
interface StorageCloudAPIInterface {
    fun addFile(info:String, data: ByteArray, callback: TaskResultListener<String, String>)
    fun addFile(info:String, data: InputStream, callback: TaskResultListener<String, String>)

    fun updateFile(id:String, info:String, data: ByteArray, callback: TaskResultListener<Boolean, String>)
    fun updateFile(id:String, info:String, data: InputStream, callback: TaskResultListener<Boolean, String>)

    fun getFileAsStream(id:String, callback: TaskResultListener<InputStream, String>)
    fun getFileAsBytes(id:String, callback: TaskResultListener<ByteArray, String>)

    fun getFileInfo(id: String, callback: TaskResultListener<String, String>)
}
@Suppress("unused")
class DocumentStorageCloud(val api: StorageCloudAPIInterface) : DocumentStorageInterface {
    override fun add(fileInfo: String, data: String, callback: TaskResultListener<String, String>) {
        api.addFile(fileInfo, data.toByteArray(), callback)
    }

    override fun add(
        fileInfo: String,
        data: ByteArray,
        callback: TaskResultListener<String, String>
    ) {
        api.addFile(fileInfo, data, callback)
    }

    override fun add(
        fileInfo: String,
        data: InputStream,
        callback: TaskResultListener<String, String>
    ) {
        api.addFile(fileInfo, data.readBytes(), callback)
    }

    override fun update(
        id: String,
        fileInfo: String,
        data: String,
        callback: TaskResultListener<Boolean, String>
    ) {
        api.updateFile(id, fileInfo, data.toByteArray(), callback)
    }

    override fun update(
        id: String,
        fileInfo: String,
        data: ByteArray,
        callback: TaskResultListener<Boolean, String>
    ) {
        api.updateFile(id, fileInfo, data, callback)
    }

    override fun update(
        id: String,
        fileInfo: String,
        data: InputStream,
        callback: TaskResultListener<Boolean, String>
    ) {
        api.updateFile(id, fileInfo, data.readBytes(), callback)
    }

    override fun getFileDataAsText(id: String, callback: TaskResultListener<String, String>) {
        api.getFileAsBytes(id, object : TaskResultListener<ByteArray, String> {
            override fun onSuccess(data: ByteArray) {
                callback.onSuccess(String(data, Charsets.UTF_8))
            }

            override fun onError(data: String?) {
                callback.onError(data)
            }
        })
    }

    override fun getFileInfo(id: String, callback: TaskResultListener<String, String>) {
        api.getFileInfo(id, callback)
    }

    override fun getFileDataAsBytes(id: String, callback: TaskResultListener<ByteArray, String>) {
        api.getFileAsBytes(id, callback)
    }

    override fun getFileDataAsStream(
        id: String,
        callback: TaskResultListener<InputStream, String>
    ) {
        api.getFileAsStream(id, callback)
    }

}