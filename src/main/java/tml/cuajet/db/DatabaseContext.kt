package tml.cuajet.db

import android.content.Context
import android.content.ContextWrapper
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CursorFactory
import android.os.Environment
import tml.libs.cku.io.StaticLogger
import java.io.File

class DatabaseContext(
    base: Context,
    var loadAsNew: Boolean,
    var dbLocalFilePath: String
) :
    ContextWrapper(base) {
    override fun getDatabasePath(name: String): File {
        return util_getDatabasePath()
    }

    override fun openOrCreateDatabase(
        name: String, mode: Int,
        factory: CursorFactory
    ): SQLiteDatabase {

        return super.openOrCreateDatabase(
            name, mode,
            factory
        )
    }

    fun util_getDatabasePath(): File {
        val sdcard = Environment.getExternalStorageDirectory()
        var dbFile =
            File(sdcard.absolutePath, dbLocalFilePath)
        var dbFilePath = dbFile.absolutePath
        if (!dbFilePath.endsWith(".db")) {
            dbFilePath += ".db"
        }
        dbFile = File(dbFilePath)
        if (!dbFile.parentFile.exists()) {
            dbFile.parentFile.mkdirs()
        }
        StaticLogger.D("DatabaseContext", "db file path $dbFilePath")
        return dbFile
    }
}