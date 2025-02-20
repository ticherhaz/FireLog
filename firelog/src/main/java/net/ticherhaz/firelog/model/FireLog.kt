package net.ticherhaz.firelog.model

data class FireLog(
    val fireLogId: String = "",
    val deviceFirebaseId: String = "",
    val className: String = "",
    val function: String = "",
    val messageInfo : String = "",
    val messageDetail: String = "",
    val createdDate: String = "",
    val logType: String = ""
)