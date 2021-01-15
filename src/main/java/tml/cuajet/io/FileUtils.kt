@file:Suppress("unused")

package tml.cuajet.io

import android.content.Context
import android.content.res.AssetManager
import java.io.*

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

        fun appendLines(
            c: Context?,
            lines: List<String?>,
            lineBreak: String?,
            f: File?,
            addLineBreakFirst: Boolean
        ) {
            try {
                val fos = FileOutputStream(f, true)
                val osw = OutputStreamWriter(fos)
                if (addLineBreakFirst) osw.append(lineBreak)
                for (i in lines.indices) {
                    osw.append(lines[i])
                    if (i < lines.size - 1) osw.append(lineBreak)
                }
                osw.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}