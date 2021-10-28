package tml.cuajet

import android.util.Log

open class LogD {
    fun d(msg:String) {
        Log.d("-DBG-", "${this.javaClass.simpleName}: $msg")
    }
}