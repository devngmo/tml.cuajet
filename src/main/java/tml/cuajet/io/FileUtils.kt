@file:Suppress("unused")

package tml.cuajet.io

import android.content.res.AssetManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader

class FileUtils {
    companion object {
        fun readAssetTextFromFile(
            am: AssetManager,
            path: String,
            lineBreak: String = "\n"
        ): String? {
            var reader: BufferedReader? = null
            val sb = StringBuilder()
            try {
                reader = BufferedReader(
                    InputStreamReader(am.open(path))
                )
                var mLine : String? = null
                var isFirstLine = true
                while (reader.readLine().also { mLine = it } != null) {
                    if (isFirstLine) {
                        sb.append(mLine)
                        isFirstLine = false
                    } else sb.append(lineBreak + mLine)
                }
                return sb.toString()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
            return ""
        }
    }
}