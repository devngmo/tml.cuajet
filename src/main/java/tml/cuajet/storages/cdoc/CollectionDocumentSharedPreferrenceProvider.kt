package tml.cuajet.storages.cdoc

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.JsonObject
import org.json.JSONObject
import tml.libs.cku.io.StaticLogger
import tml.libs.cku.storages.cdoc.CollectionDocumentProviderInterface

class CollectionDocumentSharedPreferrenceProvider(val context: Context) : CollectionDocumentProviderInterface {
    val jsonConvert = Gson()
    fun getCollectionSP(name: String): SharedPreferences
    {
        return context.getSharedPreferences("COLLECTION_$name", Activity.MODE_PRIVATE)
    }

    fun getCollectionSPArchived(name: String): SharedPreferences
    {
        return context.getSharedPreferences(
            "COLLECTION_" + name + "_ARCHIVED_",
            Activity.MODE_PRIVATE
        )
    }

    override fun getDocumentByID(collectionName: String, id: String): JsonObject? {
        val jsonStr = getCollectionSP(collectionName).getString(id, null)
        if (jsonStr == null || !jsonStr.startsWith("{")) return null
        val o = jsonConvert.fromJson(jsonStr, JsonObject::class.java)
        o.addProperty("_id", id)
        return o
    }

    override fun getAllDocuments(collectionName: String): List<JsonObject> {
        val map = getCollectionSP(collectionName).all as Map<String, String>
        val ls = arrayListOf<JsonObject>()
        for (e in map.entries) {
            val jsonStr = e.value
            var o = JsonObject()
            if (jsonStr != null && jsonStr.startsWith("{")) {
                o = jsonConvert.fromJson(jsonStr, JsonObject::class.java)
            }
            o.addProperty("_id", e.key)
        }
        return ls
    }

    override fun add(collectionName: String, id:String, contentJson: String) {
        getCollectionSP(collectionName).edit().putString(id, contentJson).apply()
    }

    override fun add(collectionName: String, id: String, jObject: JsonObject) {
        getCollectionSP(collectionName).edit().putString(id, jObject.toString()).apply()
    }

    override fun count(collectionName: String): Int {
        return getCollectionSP(collectionName).all.count()
    }

    override fun updateByID(collectionName: String, id: String, content: String) {
        getCollectionSP(collectionName).edit().putString(id, content).apply()
    }

    override fun archive(collectionName: String, id: String) {
        val content = getCollectionSP(collectionName).getString(id, null)
        if (content != null) {
            StaticLogger.I(this, "archive document [$id] with content: $content")
            getCollectionSP(collectionName).edit().remove(id).apply()
            getCollectionSPArchived(collectionName).edit().putString(id, content).apply()
        } else {
            StaticLogger.I(this, "document [$id] content is NULL => not archive")
        }
    }

    override fun unArchive(collectionName: String, id: String) {
        val content = getCollectionSPArchived(collectionName).getString(id, null)
        if (content != null) {
            getCollectionSP(collectionName).edit().putString(id, content).apply()
            getCollectionSPArchived(collectionName).edit().remove(id).apply()
        }
    }

    override fun clear(collectionName: String) {
        getCollectionSP(collectionName).edit().clear().apply()
        getCollectionSPArchived(collectionName).edit().clear().apply()
    }

    override fun getAt(collectionName: String, index: Int): JsonObject? {
        val all = getCollectionSPArchived(collectionName).all as Map<String, String>
        val keys = all.keys
        return getDocumentByID(collectionName, keys.elementAt(index))
    }
}