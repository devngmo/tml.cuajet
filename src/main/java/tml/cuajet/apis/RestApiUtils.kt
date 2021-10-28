package tml.cuajet.apis

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import java.nio.charset.Charset


class RestApiUtils {
    companion object {
        fun _post(context: Context, url:String,
                 contentType:String,
                 headers: HashMap<String, String>? = null,
                 params: HashMap<String, String>? = null,
                 body: String? = null,
                 onResult:(result:String)->Unit,
                 onError:(error: String) ->Unit)
        {
            val queue = Volley.newRequestQueue(context)
            val req = object: StringRequest(Request.Method.POST, url,
                { onResult(it) },
                { onError(it.localizedMessage) }
            ) {
                override fun getBodyContentType(): String {
                    return contentType
                }

                override fun getHeaders(): MutableMap<String, String>? {
                    return headers
                }

                override fun getParams(): MutableMap<String, String>? {
                    return params
                }

                override fun getBody(): ByteArray? {
                    if (body == null) return null
                    return body.toByteArray(Charset.forName("utf-8"))
                }
            }
            queue.add(req)
        }
        fun _get(context: Context, url:String,
                  contentType:String,
                  headers: HashMap<String, String>? = null,
                  params: HashMap<String, String>? = null,
                  body: String? = null,
                  onResult:(result:String)->Unit,
                  onError:(error: String) ->Unit)
        {
            val queue = Volley.newRequestQueue(context)
            val req = object: StringRequest(Request.Method.GET, url,
                { onResult(it) },
                { onError(it.localizedMessage) }
            ) {
                override fun getHeaders(): MutableMap<String, String>? {
                    return headers
                }

                override fun getParams(): MutableMap<String, String>? {
                    return params
                }
            }
            queue.add(req)
        }
        fun getString(context: Context, url:String,
                      contentType:String,
                      headers: HashMap<String, String>? = null,
                      params: HashMap<String, String>? = null,
                      body: String? = null,
                      onResult:(result:String)->Unit,
                      onError:(error: String) ->Unit)
        {
            _get(context, url, contentType, headers, params, body, onResult, onError)
        }


        fun postGetString(context: Context, url:String,
                          contentType:String,
                         headers: HashMap<String, String>? = null,
                         params: HashMap<String, String>? = null,
                         body: String? = null,
                         onResult:(result:String)->Unit,
                          onError:(error: String) ->Unit
        ) {
            _post(context,url, contentType, headers, params, body, onResult, onError)
        }

    }
}