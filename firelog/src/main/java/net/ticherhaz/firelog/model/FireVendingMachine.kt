package net.ticherhaz.firelog.model

data class FireVendingMachine(
    val deviceFirebaseId: String = "",
    val token: String = "",
    val versionName: String = "",
    val versionCode: String = "",
    val vendingMachineType: String = "",
    //outletId, secretKey, gatewayId, washingDeviceId and kioskSerialNumber are for M5 Dobi
    val outletId: String = "",
    val secretKey: String = "",
    val gatewayId: String = "",
    val washingDeviceId: String = "",
    val kioskSerialNumber: String = "",
    //-------------------------------------
    val merchantCode: String = "",
    val merchantKey: String = "",
    val franchiseId: String = "",
    val machineId: String = "",
    val totalLog: Int = 0,
    val createdDate: String = System.currentTimeMillis().toString()
)