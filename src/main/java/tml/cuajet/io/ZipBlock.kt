package tml.cuajet.io

import android.content.Context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.Exception
import java.lang.Integer.parseInt

class ZipBlockEntry(val offset:Int, var length: Int)

public class ZipBlock(val folder:File, val id:String) {
    val indexer = HashMap<String, ZipBlockEntry>()
    var curSize : Int = 0

    val DATA_FIS : FileInputStream
        get() {
            val fp = File(folder, "data")
            return FileInputStream(fp)
        }
    val DATA_FOS : FileOutputStream
        get() {
            val fp = File(folder, "data")
            return FileOutputStream(fp)
        }

    val MNF_FILE: File
        get() = File(folder, "manifest")

    companion object {
        fun fromFile(zbFolder: File):ZipBlock? {
            try {
                val fpManifest = File(zbFolder, "manifest")
                val lines = tml.libs.cku.io.FileUtils.readStringLines (fpManifest.absolutePath, true)
                val id = lines[0]
                val zb = ZipBlock(zbFolder, id)
                for (i in 1 until lines.size) {
                    val firstDelimiter = lines[i].indexOf(' ')
                    val offset = parseInt(lines[i].substring(0, firstDelimiter))
                    val secondDelimiter = lines[i].indexOf(' ', startIndex = firstDelimiter + 1)
                    val length = parseInt(lines[i].substring(firstDelimiter + 1, secondDelimiter))
                    val itemID =lines[i].substring(secondDelimiter + 1)
                    val zbEntry = ZipBlockEntry(offset, length)
                    zb.indexer[itemID] = zbEntry
                }
                return zb
            }
            catch (ex:Exception) {
                return null
            }
        }
    }

    fun add(ctx:Context, id:String, data: ByteArray):Boolean {
        try {
            val zbEntry = ZipBlockEntry(curSize, curSize + data.size)
            curSize += data.size
            indexer[id] = zbEntry
            FileUtils.appendLines(ctx,  listOf("${zbEntry.offset} ${zbEntry.length} $id"), "\n", MNF_FILE, true)
            val fos = DATA_FOS
            fos.write(data)
            fos.close()
            return true
        }
        catch (ex:Exception) {
            return false
        }
    }

    fun replace(ctx: Context, id: String, newData: ByteArray):Boolean {
        try {
//            val zbEntry = ZipBlockEntry(curSize, curSize + data.size)
//            curSize += data.size
//            indexer[id] = zbEntry
//            FileUtils.appendLines(ctx,  listOf("${zbEntry.offset} ${zbEntry.length} $id"), "\n", MNF_FILE, true)
//            val fos = DATA_FOS
//            fos.write(data)
//            fos.close()
            return true
        }
        catch (ex:Exception) {
            return false
        }
    }

    fun getByID(id:String):ByteArray? {
        try {
            val entry = indexer[id]!!
            val fis = DATA_FIS
            val data = ByteArray(entry.length)
            fis.read(data, entry.offset, entry.length)
            fis.close()
            return data
        }
        catch (ex:Exception) {
            return null
        }
    }

    fun containsItem(id: String): Boolean {
        return indexer.containsKey(id)
    }


}