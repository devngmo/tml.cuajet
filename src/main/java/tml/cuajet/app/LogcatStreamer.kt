package tml.cuajet.app

import android.util.Log

class LogcatStreamer : LogStreamer {
    override fun D(appTag: String, className: String, msg: String?) {
        Log.d(appTag, "$className::$msg")
    }

    override fun E(appTag: String, className: String, msg: String?) {
        Log.e(appTag, "$className::$msg")
    }

    override fun E(
        appTag: String,
        className: String,
        msg: String?,
        ex: Exception
    ) {
        Log.e(appTag, className + "::" + msg + " EXCEPTION: " + ex.message)
        ex.printStackTrace()
    }

    override fun W(appTag: String, className: String, msg: String?) {
        Log.w(appTag, "$className::$msg")
    }

    override fun I(appTag: String, className: String, msg: String?) {
        Log.i(appTag, "$className::$msg")
    }

}