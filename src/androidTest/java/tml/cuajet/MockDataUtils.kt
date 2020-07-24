@file:Suppress("unused")

package tml.cuajet

class MockDataUtils {
    companion object {
        fun createBook():  Map<String, String> {
            return mapOf(
                "id" to "B001",
                "name" to "Book 1",
                "summary" to "Summary of Book 1",
                "maxPageNo" to "100",
                "status" to "1"
            )
        }
    }
}