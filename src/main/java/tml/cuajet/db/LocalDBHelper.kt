package tml.cuajet.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import tml.libs.cku.io.StaticLogger

class LocalDBHelper(
    context: Context,
    loadAsNew: Boolean,
    iLocalDB: ILocalDB
) :
    SQLiteOpenHelper(
        DatabaseContext(context, loadAsNew, iLocalDB.getDBLocalFilePath()),
        iLocalDB.getLocalDBName(),
        null,
        iLocalDB.getDBVersion()
    ) {
    val sQLLite: SQLiteDatabase
    var idb: ILocalDB
    var c: Context?
    override fun onCreate(db: SQLiteDatabase) {
        StaticLogger.D(this, "onCreate() dbpath:" + db.path + " dbver:" + db.version)
        for (i in idb.createQueryList.indices) {
            StaticLogger.D(this, """    CREATE QUERY $i:
                |${idb.createQueryList[i]}""".trimMargin()
            )
            db.execSQL(idb.createQueryList[i])
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        StaticLogger.D(
            this,
            "LocalDBHelper::" + String.format(
                idb.getLocalDBName() + "::onUpgrade(oldVersion %d to newVersion %d)",
                oldVersion,
                newVersion
            )
        )
        idb.onUpgrade_beforeDropOldTables(db)
        for (i in idb.dropQueryList.indices)
            db.execSQL(idb.dropQueryList[i])
        onCreate(db)
        idb.onUpgradeVersionFinished()
    }

    fun rawQuery(sql: String?): Cursor {
        return sQLLite.rawQuery(sql, null)
    }

    fun deleteTable(name: String) {
        sQLLite.execSQL("DELETE FROM $name")
    }

    /**
     * UPDATE TABLE SET COL = value (bit: 1 or 0)
     * @param table
     * @param column
     */
    //    public void setColBool(String table, String column, boolean b) {
    //        sqldb.execSQL(
    //                String.format("UPDATE %s SET %s = %d", table, column, b ? 1 : 0));
    //    }
    fun setColInt(table: String?, column: String?, value: Int) {
        sQLLite.execSQL(String.format("UPDATE %s SET %s = %d", table, column, value))
    }

    /**
     * DELETE TABLE WHERE COL = value (bit: 1 or 0)
     * @param table
     * @param column
     * @param b
     */
    fun deleteByColBool(
        table: String?,
        column: String?,
        b: Boolean
    ) {
        sQLLite.execSQL(
            String.format(
                "DELETE FROM %s WHERE %s=%d",
                table,
                column,
                if (b) 1 else 0
            )
        )
    }

    fun exec(sql: String?): Boolean {
        //DBG.LD(TAG, "exec: " + sql);
        try {
            sQLLite.execSQL(sql)
            return true
        } catch (ex: Exception) {
            StaticLogger.E(this, "unexpected ex: ", ex)
        }
        return false
    }

    fun isTableExist(tableName: String?): Boolean {
        val sql = String.format(
            "SELECT name FROM sqlite_master WHERE type='table' AND name='%s'",
            tableName
        )
        val c = rawQuery(sql)
        if (!c.moveToFirst()) {
            c.close()
            return false
        }

//        String name = c.getString(0);
        c.close()
        //        return name.equals(tableName);
        return true
    }

    fun getTableRowCount(tableName: String?): Int {
        StaticLogger.D(this, idb.getLocalDBName() + "::getTableRowCount")
        val sql = String.format("SELECT COUNT(*) FROM [%s]", tableName)
        val c = rawQuery(sql)
        if (!c.moveToFirst()) return 0
        val n = c.getInt(0)
        c.close()
        return n
    }

    init {
        StaticLogger.D(
            this,
            "getWritableDatabase() FILE PATH: " + iLocalDB.getDBLocalFilePath() + " ver " + iLocalDB.getDBVersion()
        )
        c = context
        idb = iLocalDB
        sQLLite = writableDatabase
    }
}
