package tml.cuajet.app

interface LogStreamer {
    fun D(
        appTag: String,
        className: String,
        msg: String?
    )

    fun E(
        appTag: String,
        className: String,
        msg: String?
    )

    fun E(
        appTag: String,
        className: String,
        msg: String?,
        ex: Exception
    )

    fun W(
        appTag: String,
        className: String,
        msg: String?
    )

    fun I(
        appTag: String,
        className: String,
        msg: String?
    )
}