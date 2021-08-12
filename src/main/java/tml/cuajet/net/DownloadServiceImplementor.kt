package tml.cuajet.net

interface DownloadServiceListener {
    fun onDownloadSuccess(downloadRequest: DownloadRequest, response: DownloadResponseContent)
    fun onDownloadFailed(downloadRequest: DownloadRequest, response: DownloadResponseContent)
}


interface DownloadServiceImplementor {
    fun enqueue(request: DownloadRequest)

}
interface DownloadServiceInterface : DownloadServiceImplementor {
    fun onDownloadSuccess(downloadRequest: DownloadRequest, response: DownloadResponseContent)
    fun onDownloadFailed(
        downloadRequest: DownloadRequest,
        response: DownloadResponseTextContent
    )
}
