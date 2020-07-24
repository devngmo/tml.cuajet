package tml.cuajet.apis

import tml.cuajet.storages.StorageCloudAPIInterface
import tml.libs.cku.TaskResultListener
import java.io.InputStream

class SimpleFileCloudStorageAPI : StorageCloudAPIInterface {
    override fun addFile(
        info: String,
        data: ByteArray,
        callback: TaskResultListener<String, String>
    ) {
        TODO("Not yet implemented")
    }

    override fun addFile(
        info: String,
        data: InputStream,
        callback: TaskResultListener<String, String>
    ) {
        TODO("Not yet implemented")
    }

    override fun updateFile(
        id: String,
        info: String,
        data: ByteArray,
        callback: TaskResultListener<Boolean, String>
    ) {
        TODO("Not yet implemented")
    }

    override fun updateFile(
        id: String,
        info: String,
        data: InputStream,
        callback: TaskResultListener<Boolean, String>
    ) {
        TODO("Not yet implemented")
    }

    override fun getFileAsStream(id: String, callback: TaskResultListener<InputStream, String>) {
        TODO("Not yet implemented")
    }

    override fun getFileAsBytes(id: String, callback: TaskResultListener<ByteArray, String>) {
        TODO("Not yet implemented")
    }

    override fun getFileInfo(id: String, callback: TaskResultListener<String, String>) {
        TODO("Not yet implemented")
    }
}