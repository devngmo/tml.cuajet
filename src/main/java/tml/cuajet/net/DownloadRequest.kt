package tml.cuajet.net

import java.io.File

const val ERR_NONE = 0
const val ERR_NETWORK_NOT_AVAILABLE = 100
const val ERR_SERVER_NOT_AVAILABLE = 200
const val ERR_SERVER_RUNTIME_ERROR = 201


enum class DownloadContentType { Binary, Text }
class DownloadRequest(val url : String, var saveAs: String? = null, val downloadContentType: DownloadContentType, val tag: String? = null) {
}

open abstract class DownloadResponseContent(val downloadContentType: DownloadContentType, val errCode: Int = ERR_NONE) {
    abstract fun asText():String?
    abstract fun asFile(): File?
}

class DownloadResponseTextContent(val text: String? = null, errCode: Int = ERR_NONE):DownloadResponseContent(DownloadContentType.Text, errCode) {
    override fun asText():String? {
        return text
    }
    override fun asFile(): File? {
        return null
    }
}

class DownloadResponseBinaryContent(val file: File? = null, errCode: Int = ERR_NONE):DownloadResponseContent(DownloadContentType.Binary, errCode) {
    override fun asText():String? {
        return null
    }
    override fun asFile(): File? {
        return file
    }
}

