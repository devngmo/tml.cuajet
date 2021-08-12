package tml.cuajet.net

import java.lang.Exception

class DownloadService : DownloadServiceInterface {
    lateinit var impl : DownloadServiceImplementor
    private var mIsReady = false

    fun setup(impl : DownloadServiceImplementor) {
        this.impl = impl
        mIsReady = true
    }

    fun close() {
        mIsReady = false
        listenerList.clear()
    }

    val listenerList = ArrayList<DownloadServiceListener>()
    fun addListener(lis : DownloadServiceListener) {
        if (!listenerList.contains(lis))
            listenerList.add(lis)
    }

    fun removeListener(lis : DownloadServiceListener) {
        if (listenerList.contains(lis))
            listenerList.remove(lis)
    }

    val isReady: Boolean
        get() = mIsReady

    override fun enqueue(request: DownloadRequest) {
        impl.enqueue(request)
    }

    override fun onDownloadSuccess(
        downloadRequest: DownloadRequest, response: DownloadResponseContent
    ) {
        for (lis in listenerList) {
            try {
                lis.onDownloadSuccess(downloadRequest, response)
            }
            catch (ex:Exception) {}
        }
    }

    override fun onDownloadFailed(
        downloadRequest: DownloadRequest,
        response: DownloadResponseTextContent
    ) {
        for (lis in listenerList) {
            try {
                lis.onDownloadFailed(downloadRequest, response)
            }
            catch (ex:Exception) {}
        }
    }

    companion object {
        val ins = DownloadService()
    }

}