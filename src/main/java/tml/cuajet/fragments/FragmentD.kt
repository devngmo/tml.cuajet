package tml.cuajet.fragments

import android.util.Log
import androidx.fragment.app.Fragment

open class FragmentD : Fragment() {
    fun d(msg:String) {
        Log.d("-DBG-", "${this.javaClass.simpleName}: $msg")
    }
}