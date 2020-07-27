package tml.cuajet.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import tml.libs.cku.io.StaticLogger

@Suppress("unused", "FunctionName")
abstract class LocalDB : ILocalDB {
    var isLoaded = false
        protected set
    private lateinit var dbh: LocalDBHelper

    override var createQueryList = ArrayList<String>()
    override var dropQueryList = ArrayList<String>()

    override fun getDBVersion(): Int {
        return 1
    }

    fun b2i(value: Boolean): Int {
        return if (value) 1 else 0
    }

    fun i2b(value: Int): Boolean {
        return value > 0
    }

    private var mContext: Context? = null
    private var mIsDBVersionUpgraded = false
    var isUpdated = false
        protected set
    private var mHasUpdateRequest = false

    /**
     * Request application update DB from server
     */
    private fun requestUpdate() {
        mHasUpdateRequest = true
    }

    fun hasUpdateRequest(): Boolean {
        return mHasUpdateRequest
    }

    fun onUpdateContentFinished() {
        StaticLogger.I(this, "onUpdateContentFinished() isUpdated = true")
        isUpdated = true
        mIsDBVersionUpgraded = false
        mHasUpdateRequest = false
    }

    override fun onUpgradeVersionFinished() {
        StaticLogger.I(this, "onUpgradeVersionFinished()")
        mIsDBVersionUpgraded = true
        isUpdated = false
        requestUpdate()
    }

    fun open(c: Context) {
        StaticLogger.D(this, "open()")
        try {
            mContext = c
            dbh = LocalDBHelper(c, true, this)
            //dbh.L = L;
            onOpenSuccess()
            isLoaded = true
        } catch (ex: Exception) {
            StaticLogger.E(this, "open(): Exception: " + ex.message)
            ex.printStackTrace()
        }
    }

    fun close() {
        StaticLogger.I(this, "close")
        dbh.close()
    }

    val isReadyToUse: Boolean
        get() {
            if (isUpdated) return true
            StaticLogger.I(this, "DB not ready: not updated")
            return false
        }

    fun deleteTable(tableName: String) {
        StaticLogger.I(this, "deleteTable: $tableName")
        try {
            dbh.deleteTable(tableName)
        } catch (ex: Exception) {
        }
    }

    fun Select(sql: String?): Cursor {
        return dbh.rawQuery(sql)
    }

    fun Insert(sql: String): Boolean {
        return Exec(sql)
    }

    fun Update(sql: String): Boolean {
        return Exec(sql)
    }

    private fun Exec(sql: String): Boolean {
        StaticLogger.I(this, "Exec: $sql")
        return dbh.exec(sql)
    }

    fun execGetInt32(columnIndex: Int, selectSql: String, defaultValue: Int): Int {
        val c: Cursor = dbh.rawQuery(selectSql)
        if (c.count == 0) return defaultValue
        if (c.count > 1) StaticLogger.W(
            this,
            "execGetInt32() expected return only 1 row, but actual return " + c.count + " rows"
        )
        if (c.columnCount <= columnIndex) {
            StaticLogger.E(
                this,
                "execGetInt32(col " + columnIndex + ") but select only " + c.columnCount + " columns"
            )
            return defaultValue
        }
        try {
            c.moveToFirst()
            val value = c.getInt(columnIndex)
            c.close()
            return value
        } catch (ex: Exception) {
            StaticLogger.E(
                this,
                "execGetInt32(col $columnIndex as Int) but EXCEPTION Occur! SQL:$selectSql",
                ex
            )
        }
        return defaultValue
    }

    fun execGetString(columnIndex: Int, selectSql: String): String? {
        val c: Cursor = dbh.rawQuery(selectSql)
        if (c.count == 0) return null
        if (c.count > 1) StaticLogger.W(
            this,
            "execGetString() expected return only 1 row, but actual return " + c.count + " rows"
        )
        if (c.columnCount <= columnIndex) {
            StaticLogger.E(
                this,
                "execGetString(col " + columnIndex + ") but select only " + c.columnCount + " columns"
            )
            return null
        }
        try {
            c.moveToFirst()
            val s = c.getString(columnIndex)
            c.close()
            return s
        } catch (ex: Exception) {
            StaticLogger.E(
                this,
                "execGetString(col $columnIndex as String) but EXCEPTION Occur! SQL:$selectSql",
                ex
            )
        }
        return null
    }

    /**
     * db is opened, you can access it now ( cache something here,... )
     */
    open fun onOpenSuccess() {
        StaticLogger.I(this, getLocalDBName() + "::onOpenSuccess")
    }

    val isEmpty: Boolean
        get() = true

    private lateinit var mDBLocalFilePath: String

    //    protected String mDBName;
    override fun getDBLocalFilePath(): String {
        return mDBLocalFilePath
    }

    //    @Override
    //    public String getLocalDBName() {
    //        return mDBName;
    //    }
    fun isTableExist(tableName: String?): Boolean {
        return dbh.isTableExist(tableName)
    }

    fun getTableRowCount(tableName: String?): Int {
        try {
            return dbh.getTableRowCount(tableName)
        } catch (ex: Exception) {
        }
        return 0
    }

    override fun onUpgrade_beforeDropOldTables(db: SQLiteDatabase) {

    }
    fun GetInt(columnIndex: Int, sql: String?, defaultValue: Int): Int {
        val c: Cursor = dbh.rawQuery(sql)
        if (!c.moveToFirst()) return defaultValue
        val `val` = c.getInt(columnIndex)
        c.close()
        return `val`
    }

    fun ExecGet(ncols: Int, sql: String?): Array<String?>? {
        val c: Cursor = dbh.rawQuery(sql)
        if (!c.moveToFirst()) return null
        if (c.count == 0) return null
        val fields = arrayOfNulls<String>(ncols)
        for (i in 0 until ncols) fields[i] = c.getString(i)
        c.close()
        return fields
    }

    fun selectArray(sql: String?): JSONArray? {
        val c: Cursor = dbh.rawQuery(sql)
        if (!c.moveToFirst()) return null
        val ar = JSONArray()
        c.moveToFirst()
        for (i in 0 until c.count) {
            val row = JSONObject()
            for (col in 0 until c.columnCount) {
                val colName = c.getColumnName(col)
                val colIdx = c.getColumnIndex(colName)
                try {
                    row.put(colName, c.getString(colIdx))
                } catch (e: JSONException) {
                    e.printStackTrace()
                    return null
                }
            }
            ar.put(row)
            c.moveToNext()
        }
        return ar
    }

    protected fun getFloat(
        c: Cursor,
        index: Int,
        defaultValue: Float
    ): Float {
        return try {
            c.getFloat(index)
        } catch (ex: Exception) {
            defaultValue
        }
    }

    protected fun getInt(c: Cursor, index: Int, defaultValue: Int): Int {
        return try {
            c.getInt(index)
        } catch (ex: Exception) {
            defaultValue
        }
    }

    protected fun getString(
        c: Cursor,
        index: Int,
        defaultValue: String
    ): String {
        return try {
            c.getString(index)
        } catch (ex: Exception) {
            defaultValue
        }
    }

    private fun getJSON(c: Cursor, cols: Array<ColumnInfo>): JSONObject? {
        return try {
            val o = JSONObject()
            for (i in cols.indices) {
                val cidx = c.getColumnIndex(cols[i].name)
                if (cols[i].type.equals("text")) o.put(
                    cols[i].name,
                    c.getString(cidx)
                ) else if (cols[i].type.equals("float")) o.put(
                    cols[i].name,
                    c.getFloat(cidx)
                ) else if (cols[i].type.equals("int")) o.put(cols[i].name, c.getInt(cidx))
            }
            o
        } catch (ex: Exception) {
            null
        }
    }

    protected fun getItemAsJSONArray(
        tableName: String,
        where: String,
        orderby: String,
        cols: Array<ColumnInfo>
    ): JSONArray? {
        return try {
            var sql = "SELECT * FROM $tableName"
            if (where.trim { it <= ' ' }.isNotEmpty()) sql += " WHERE $where"
            if (orderby.trim { it <= ' ' }.isNotEmpty()) sql += " ORDER BY $orderby"
            val c: Cursor = dbh.rawQuery(sql)
            val ar = JSONArray()
            if (c.count == 0) return ar
            c.moveToFirst()
            while (!c.isAfterLast) {
                ar.put(getJSON(c, cols))
                c.moveToNext()
            }
            ar
        } catch (ex: Exception) {
            null
        }
    }

    fun addJSONArrayToTable(
        tableName: String,
        ar: JSONArray,
        cols: Array<ColumnInfo>
    ): Boolean {
        var passed = 0
        try {
            for (i in 0 until ar.length()) {
                val success = addJSONToTable(tableName, ar.getJSONObject(i), cols)
                if (success) passed++
            }
        } catch (ex: Exception) {
            StaticLogger.E(
                this, String.format(
                    "addJSONArrayToTable %s passed %d/%d",
                    tableName, passed, ar.length()
                ), ex
            )
        }
        return passed == ar.length()
    }

    private fun addJSONToTable(
        tableName: String,
        json: JSONObject,
        cols: Array<ColumnInfo>
    ): Boolean {
        StaticLogger.I(this, "addJSONToTable $tableName")
        var colNames = ""
        var values: String? = ""
        try {
            for (c in cols) {
                if (json.has(c.name)) {
                    if (colNames.isNotEmpty()) {
                        colNames += ","
                        values += ","
                    }
                    colNames += c.name
                    values += if (c.type === "text") {
                        String.format("'%s'", json.getString(c.name))
                    } else if (c.type === "int") {
                        val fieldVal = json.getString(c.name)
                        if (fieldVal.isEmpty()) "0" else if (fieldVal == "true") "1" else if (fieldVal == "false") "0" else fieldVal
                    } else {
                        String.format("%s", json.getString(c.name))
                    }
                } else {
                    StaticLogger.W(this, "addJSONToTable: missing column " + c.name)
                }
            }
            val sql =
                String.format("INSERT INTO %s(%s) VALUES (%s)", tableName, colNames, values)
            val success = Exec(sql)
            if (!success) {
                StaticLogger.W(this, "addJSONToTable($tableName) $json")
            }
            return true
        } catch (ex: Exception) {
            StaticLogger.E(this, "addJSONToTable($json) Unexpected EX: ", ex)
        }
        return false
    }

    companion object {
        fun CText(name: String): ColumnInfo {
            return ColumnInfo(name, "text")
        }

        fun CInt(name: String): ColumnInfo {
            return ColumnInfo(name, "int")
        }

        fun CFloat(name: String): ColumnInfo {
            return ColumnInfo(name, "float")
        }

        fun genCreateTableQuery(
            tableName: String,
            primaryKeys: Array<String>,
            cols: Array<ColumnInfo>
        ): String {
            var sql = "create table $tableName("
            var colDec = ""
            if (primaryKeys.size > 1) {
                var primKeys = ""
                for (keyName in primaryKeys) {
                    if (colDec.isNotEmpty()) {
                        colDec += ","
                        primKeys += ","
                    }
                    val c: ColumnInfo? = getColName(keyName, cols)
                    c?.let {
                        colDec += keyName + " " + it.type
                        primKeys += keyName
                    }

                }
                for (c in cols) {
                    if (itemInArray(c.name, primaryKeys)) continue
                    if (colDec.isNotEmpty()) colDec += ","
                    colDec += c.name + " " + c.type
                }
                colDec += ", PRIMARY KEY($primKeys)"
            } else {
                for (key in primaryKeys) {
                    val c: ColumnInfo = getColName(key, cols)!!
                    colDec += key + " " + c.type + " PRIMARY KEY"
                }
                for (c in cols) {
                    if (itemInArray(c.name, primaryKeys)) continue
                    if (colDec.isNotEmpty()) colDec += ","
                    colDec += c.name + " " + c.type
                }
            }
            sql += "$colDec)"
            return sql
        }

        private fun itemInArray(
            item: String,
            list: Array<String>
        ): Boolean {
            for (e in list) {
                if (item == e) return true
            }
            return false
        }

        private fun getColName(
            fieldName: String,
            cols: Array<ColumnInfo>
        ): ColumnInfo? {
            for (c in cols) if (c.name.equals(fieldName)) return c
            return null
        }
    }
}
