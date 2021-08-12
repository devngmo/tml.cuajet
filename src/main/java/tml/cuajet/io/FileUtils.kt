@file:Suppress("unused")

package tml.cuajet.io

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import android.util.StateSet
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipInputStream

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

        fun makeFolderIfNeed(folder: File) : Boolean {
            return makeFolderIfNeed(folder.absolutePath)
        }
        fun makeFolderIfNeed(path: String) : Boolean {
            val d = File(path)
            if (d.exists() && d.isDirectory) return false
            d.mkdirs()
            Log.i("FileUtils", "make folder: ${d.absolutePath}")
            return true
        }

        fun extract(zipFile: File, outputFolder: File): Boolean {
            Log.i("FileUtils", "extract ${zipFile.absolutePath} to ${outputFolder.absolutePath}")
            try {
                if (!zipFile.exists())
                {
                    Log.e(
                        "FileUtils",
                        "extract(zipFile, outputFolder) error: zip file not exists: ${zipFile.absolutePath}"
                    )
                    return false
                }
                makeFolderIfNeed(outputFolder)


                val fin = FileInputStream(zipFile)
                val zin = ZipInputStream(fin)
                var ze: ZipEntry? = null
                while (zin.nextEntry.also { ze = it } != null) {
                    //create dir if required while unzipping
                    if (ze!!.isDirectory) {
                        val dirOut = File(outputFolder, ze!!.name)
                        makeFolderIfNeed(dirOut)
                    } else {

                        val fout = File(outputFolder, ze!!.name)

                        val fos = FileOutputStream(fout)

                        Log.i("FileUtils", "write file ${fout.absolutePath}")

                        var c: Int = zin.read()
                        while (c != -1) {
                            fos.write(c)
                            c = zin.read()
                        }
                        zin.closeEntry()
                        fos.close()
                    }
                }
                zin.close()
                return true
            }
            catch (ex: Exception) {
                Log.e("FileUtils", ex.message)
                ex.printStackTrace()
                return false
            }
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