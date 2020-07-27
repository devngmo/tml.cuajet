package tml.cuajet.db

import android.database.sqlite.SQLiteDatabase


@Suppress("unused")
interface ILocalDB {
    fun getDBVersion(): Int

    var createQueryList: ArrayList<String>
    var dropQueryList: ArrayList<String>

    fun getDBLocalFilePath(): String
    fun getLocalDBName(): String

    fun onUpgradeVersionFinished()

    fun onUpgrade_beforeDropOldTables(db: SQLiteDatabase)
}