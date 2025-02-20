package net.ticherhaz.firelog.model

data class FireLogDetail(
    val fireLogId: String = "",
    val deviceFirebaseId: String = "",
    val className: String = "",
    val function: String = "",
    val messageInfo : String = "",
    val messageDetail: String = "",
    val createdDate: String = "",
    val logType: String = ""
)