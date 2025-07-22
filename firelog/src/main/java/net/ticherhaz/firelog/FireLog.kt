package net.ticherhaz.firelog

import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.pm.PackageInfoCompat
import com.google.firebase.Firebase
import com.google.firebase.database.ServerValue
import com.google.firebase.database.database
import com.google.firebase.installations.FirebaseInstallations
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import net.ticherhaz.firelog.model.FireLogDetail
import net.ticherhaz.firelog.model.FireVendingMachine

object FireLog {

    private const val TAG = "FireLog"
    private const val FIRE_LOG_ROOT = "FireLog"
    private const val FIRE_LOG_DETAIL_NODE = "FireLogDetail"
    private const val VENDING_MACHINE_NODE = "FireVendingMachine"

    enum class VendingMachineType {
        VENDING_MACHINE_M3,
        VENDING_MACHINE_M4,
        VENDING_MACHINE_M5,
        VENDING_MACHINE_M5_FLORIST,
        VENDING_MACHINE_M5_DOBI,
        VENDING_MACHINE_M5_MINI
    }

    enum class LogType { INFO, ERROR }

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    /**
     * Initializes the FireLogDetail with device information and writes it to Firebase.
     * @param context Application context
     * @param vendingMachineType Type of vending machine
     */
    fun initialize(
        context: Context,
        vendingMachineType: VendingMachineType
    ) {

        coroutineScope.launch {
            try {
                val deviceId = getFirebaseId()
                val token = FirebaseInstallations.getInstance().getToken(false).await().token

                val packageInfo = try {
                    context.packageManager.getPackageInfo(context.packageName, 0)
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e(TAG, "Package not found", e)
                    return@launch
                }

                val machine = FireVendingMachine(
                    deviceFirebaseId = deviceId,
                    token = token,
                    versionName = packageInfo.versionName ?: "",
                    versionCode = PackageInfoCompat.getLongVersionCode(packageInfo).toString(),
                    vendingMachineType = vendingMachineType.name
                )

                getVendingMachineRef(deviceId).setValue(machine)
            } catch (e: Exception) {
                Log.e(TAG, "Initialization failed", e)
            }
        }
    }

    /* Configuration updaters */
    fun updateOutletId(outletId: String) = updateConfiguration("outletId", outletId)
    fun updateSecretKey(secretKey: String) = updateConfiguration("secretKey", secretKey)
    fun updateGatewayId(gatewayId: String) = updateConfiguration("gatewayId", gatewayId)
    fun updateWashingDeviceId(washingDeviceId: String) =
        updateConfiguration("washingDeviceId", washingDeviceId)

    fun updateKioskSerialNumber(kioskSerialNumber: String) =
        updateConfiguration("kioskSerialNumber", kioskSerialNumber)

    fun updateMerchantCode(merchantCode: String) = updateConfiguration("merchantCode", merchantCode)
    fun updateMerchantKey(merchantKey: String) = updateConfiguration("merchantKey", merchantKey)
    fun updateFranchiseId(fid: String) = updateConfiguration("franchiseId", fid)
    fun updateMachineId(mid: String) = updateConfiguration("machineId", mid)

    /**
     * Logs a message to Firebase with automatic timestamp
     * @param logType Log severity level
     * @param className Originating class name
     * @param functionName Originating function name
     * @param message Primary log message
     * @param details Additional debug details
     */
    fun log(
        logType: LogType,
        className: String,
        functionName: String,
        message: String,
        details: String = ""
    ) {
        coroutineScope.launch {
            try {
                val deviceId = getFirebaseId()
                val logId = getLogDetailRef(deviceId).push().key ?: run {
                    Log.e(TAG, "Failed to generate log ID")
                    return@launch
                }

                val logEntry = FireLogDetail(
                    fireLogId = logId,
                    deviceFirebaseId = deviceId,
                    className = className,
                    function = functionName,
                    messageInfo = message,
                    messageDetail = details,
                    createdDate = System.currentTimeMillis().toString(),
                    logType = logType.name
                )

                getLogDetailRef(deviceId).child(logId).setValue(logEntry)
                incrementTotalLogs(deviceId)
            } catch (e: Exception) {
                Log.e(TAG, "Logging failed", e)
            }
        }
    }

    /** Clears all log entries for this device */
    fun clearLogs() {
        coroutineScope.launch {
            try {
                val deviceId = getFirebaseId()
                getLogDetailRef(deviceId).removeValue()
                getVendingMachineRef(deviceId).child("totalLog").setValue(0)
            } catch (e: Exception) {
                Log.e(TAG, "Log clearance failed", e)
            }
        }
    }

    private fun updateConfiguration(key: String, value: Any) {
        coroutineScope.launch {
            try {
                getVendingMachineRef(getFirebaseId()).child(key).setValue(value)
            } catch (e: Exception) {
                Log.e(TAG, "Configuration update failed for $key", e)
            }
        }
    }

    private suspend fun getFirebaseId() = FirebaseInstallations.getInstance().id.await()

    private fun getVendingMachineRef(deviceId: String) =
        Firebase.database.reference.child(FIRE_LOG_ROOT).child(VENDING_MACHINE_NODE).child(deviceId)

    private fun getLogDetailRef(deviceId: String) =
        Firebase.database.reference.child(FIRE_LOG_ROOT).child(FIRE_LOG_DETAIL_NODE).child(deviceId)

    private suspend fun incrementTotalLogs(deviceId: String) {
        getVendingMachineRef(deviceId).child("totalLog")
            .setValue(ServerValue.increment(1)).await()
    }
}