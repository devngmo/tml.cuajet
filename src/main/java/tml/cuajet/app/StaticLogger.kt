package tml.cuajet.app

class StaticLogger {
    companion object {
        var excludeMap: HashMap<String, String> = HashMap()

        fun addExclude(className: String,verboses: String) {
            if (excludeMap.containsKey(className)) {
                excludeMap[className] = excludeMap[className].toString() + verboses
            }
        }

        var logStreamer: LogStreamer? = null

        var enableLogClasses: MutableList<String> = ArrayList()
        var curAppTag: String? = null
        var logErrorOnAnyClasses = true

        var showInfo = true
        var showDebug = true
        var showAllClass = false

        fun enableLog(obj: Any) {
            enableLogClasses.add(obj.javaClass.simpleName)
        }

        fun enableLog(className: String) {
            if (enableLogClasses.contains(className)) return
            enableLogClasses.add(className)
        }

        fun D(sender: Any?, msg: String?) {
            if (!isPrintable()) return
            if (!showDebug) return
            var clsName = "null"
            if (sender is String) clsName =
                sender else if (sender != null) clsName =
                sender.javaClass.simpleName
            if (excludeMap.containsKey(clsName)) return
            if (showAllClass || enableLogClasses.contains(clsName)) {
                logStreamer!!.D(curAppTag!!, clsName, msg)
            }
        }

        fun W(sender: Any?, msg: String?) {
            if (!isPrintable()) return
            var clsName = "null"
            if (sender is String) clsName =
                sender else if (sender != null) clsName =
                sender.javaClass.simpleName
            if (excludeMap.containsKey(clsName)) if (excludeMap[clsName]!!.contains("W")) return
            if (showAllClass || enableLogClasses.contains(clsName)) {
                logStreamer!!.W(curAppTag!!, clsName, msg)
            }
        }

        fun W(tag: String?, msg: String?) {
            if (!isPrintable()) return
            logStreamer!!.W(curAppTag!!, tag!!, msg)
        }

        fun W(msg: String?) {
            if (!isPrintable()) return
            logStreamer!!.W(curAppTag!!, "", msg)
        }

        fun I(sender: Any?, msg: String?) {
            if (!isPrintable()) return
            if (!showInfo) return
            var clsName = "null"
            if (sender is String) clsName =
                sender else if (sender != null) clsName =
                sender.javaClass.simpleName
            if (excludeMap.containsKey(clsName)) if (excludeMap[clsName]!!.contains("I")) return
            if (showAllClass || enableLogClasses.contains(clsName)) {
                logStreamer!!.I(curAppTag!!, clsName, msg)
            }
        }

        fun D(className: String, msg: String?) {
            if (!isPrintable()) return
            if (!showDebug) return
            if (excludeMap.containsKey(className)) if (excludeMap[className]!!.contains("D")) return
            if (showAllClass || enableLogClasses.contains(className)) {
                logStreamer!!.D(curAppTag!!, className, msg)
                //System.out.println(className + "::" + msg);
            }
        }

        fun E(sender: Any?, msg: String?) {
            if (!isPrintable()) return
            var clsName = "null"
            if (sender is String) clsName =
                sender else if (sender != null) clsName =
                sender.javaClass.simpleName
            if (excludeMap.containsKey(clsName)) if (excludeMap[clsName]!!.contains("E")) return
            if (logErrorOnAnyClasses) {
                logStreamer!!.E(curAppTag!!, clsName, msg)
            }
        }

        fun E(msg: String?) {
            if (!isPrintable()) return
            if (logErrorOnAnyClasses) {
                logStreamer!!.E(curAppTag!!, "", msg)
            }
        }

        fun E(ex: Exception?) {
            if (!isPrintable()) return
            if (logErrorOnAnyClasses) {
                logStreamer!!.E(curAppTag!!, "", "", ex!!)
            }
        }

        fun E(msg: String?, ex: Exception?) {
            if (!isPrintable()) return
            if (logErrorOnAnyClasses) {
                logStreamer!!.E(curAppTag!!, "", msg, ex!!)
            }
        }

        fun E(sender: Any?, msg: String?, ex: Exception?) {
            if (!isPrintable()) return
            var clsName = "null"
            if (sender != null) clsName = sender.javaClass.simpleName
            if (excludeMap.containsKey(clsName)) if (excludeMap[clsName]!!.contains("E")) return
            if (logErrorOnAnyClasses) {
                logStreamer!!.E(curAppTag!!, clsName, msg, ex!!)
            }
        }

        fun E(clsName: String, msg: String?) {
            if (!isPrintable()) return
            if (logErrorOnAnyClasses) {
                logStreamer!!.E(curAppTag!!, clsName, msg)
            } else if (enableLogClasses.contains(clsName)) logStreamer!!.E(
                curAppTag!!,
                clsName,
                msg
            )
        }

        fun isPrintable(): Boolean {
            return curAppTag != null && logStreamer != null
        }

        fun D(msg: String?) {
            if (!isPrintable()) return
            if (!showDebug) return
            logStreamer!!.D(curAppTag!!, "", msg)

        }

        fun printConfig() {
            logStreamer?.let {
                it.I(curAppTag!!, "StaticLogger", "Show debug: ${showDebug}")
            }
        }
    }
}