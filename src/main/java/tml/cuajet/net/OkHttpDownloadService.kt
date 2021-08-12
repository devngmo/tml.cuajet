package tml.cuajet.net

import android.content.Context
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Request.Builder
import okio.*
import org.apache.http.conn.ssl.SSLSocketFactory
import org.apache.http.conn.ssl.X509HostnameVerifier
import tml.libs.cku.io.StaticLogger
import java.io.File
import java.util.*
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory


class OkHttpDownloadService(val context: Context, val config: DownloadServiceConfig) :DownloadServiceImplementor {
    lateinit var sslContext: SSLContext
    init {
        setupSelfSignedSSL()
    }

    private fun setupSelfSignedSSL() {
        val hostnameVerifier: HostnameVerifier = SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER
//
//        val client = DefaultHttpClient()
//
//        val registry = SchemeRegistry()
//        val socketFactory: SSLSocketFactory = SSLSocketFactory.getSocketFactory()
//        socketFactory.setHostnameVerifier(hostnameVerifier as X509HostnameVerifier)
//        registry.register(Scheme("https", socketFactory, 443))
//        val mgr = SingleClientConnManager(client.getParams(), registry)
//        val httpClient = DefaultHttpClient(mgr, client.getParams())
//
//// Set verifier
//
//// Set verifier
//        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier)
//
//// Example send http request
//
//// Example send http request
//        val url = "https://encrypted.google.com/"
//        val httpPost = HttpPost(url)
//        val response: HttpResponse = httpClient.execute(httpPost)

    }

    override fun enqueue(downloadRequest: DownloadRequest) {
        StaticLogger.I(this, "enqueue [${downloadRequest.tag}] ${downloadRequest.url} as [${downloadRequest.downloadContentType} save to ${downloadRequest.saveAs}]")
        try {
            val client = OkHttpClient()
            val request: Request = Builder()
                .url(downloadRequest.url)
                .build()
            val response = client.newCall(request).execute()
            StaticLogger.I(this, "isSuccessful ${response.isSuccessful}")
            StaticLogger.I(this, "networkResponse ${response.networkResponse!!.message}")
            StaticLogger.I(this, "code ${response.code}")

            if (downloadRequest.downloadContentType == DownloadContentType.Binary) {
                if (response.body == null) {
                    StaticLogger.I(this, "downloaded [${downloadRequest.tag}] file  is NULL")
                    DownloadService.ins.onDownloadSuccess(
                        downloadRequest,
                        DownloadResponseBinaryContent(null)
                    )
                    return
                }
                val src: BufferedSource = response.body!!.source()

                var saveFile: File
                if (downloadRequest.saveAs != null)
                    saveFile = File(downloadRequest.saveAs!!)
                else {
                    saveFile = File(config.workDir, UUID.randomUUID().toString())
                    downloadRequest.saveAs = saveFile.absolutePath
                }
                val sink = saveFile.sink().buffer()
                sink.writeAll(src)
                sink.close()
                StaticLogger.I(
                    this,
                    "downloaded [${downloadRequest.tag}] file size ${saveFile.length()}"
                )
                DownloadService.ins.onDownloadSuccess(
                    downloadRequest,
                    DownloadResponseBinaryContent(saveFile)
                )
            } else {
                if (response.body == null) {
                    StaticLogger.I(this, "downloaded [${downloadRequest.tag}] text is NULL")
                    DownloadService.ins.onDownloadSuccess(
                        downloadRequest,
                        DownloadResponseTextContent(null)
                    )
                    return
                }
                StaticLogger.I(
                    this,
                    "downloaded [${downloadRequest.tag}] text size ${response.body.toString().length}"
                )
                DownloadService.ins.onDownloadSuccess(
                    downloadRequest,
                    DownloadResponseTextContent(response.body.toString())
                )
            }
        }
        catch(ex: Exception) {
            StaticLogger.E(this, ex.message, ex)
            DownloadService.ins.onDownloadFailed(
                downloadRequest,
                DownloadResponseTextContent(ex.message, ERR_SERVER_RUNTIME_ERROR)
            )
        }
    }

}