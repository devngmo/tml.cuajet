package tml.cuajet.storages

import tml.libs.cku.storages.ILogStorage
import kotlin.collections.ArrayList

@Suppress("unused")
class LogStorageMemory() : ILogStorage {
    val logs = ArrayList<String>()
    override fun count(): Int {
        return logs.size
    }

    override fun add(msg: String) {
        logs.add(msg)
    }

    override fun getAt(index: Int): String? {
        return logs[index]
    }

}