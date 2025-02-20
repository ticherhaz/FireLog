package net.ticherhaz.firelog.model

data class FireVendingMachine(
    val deviceFirebaseId: String = "",
    val token: String = "",
    val versionName: String = "",
    val versionCode: String = "",
    val vendingMachineType: String = "",
    //outletId, secretKey, and kioskSerialNumber are for M5 Dobi
    val outletId: String = "",
    val secretKey: String = "",
    val kioskSerialNumber: String = "",
    val merchantCode: String = "",
    val merchantKey: String = "",
    val fid: String = "",
    val mid: String = "",
    val totalLog: Int = 0
)