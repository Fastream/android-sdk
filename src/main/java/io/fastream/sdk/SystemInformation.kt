package io.fastream.sdk

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import java.lang.reflect.InvocationTargetException
import java.lang.reflect.Method

private const val LOGTAG = "Fastream.sysinfo"

internal class SystemInformation(
    private val context: Context
) {

    fun getPhoneRadioType(): String? {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return when (telephonyManager.phoneType) {
            0x00000000 -> "none"
            0x00000001 -> "gsm"
            0x00000002 -> "cdma"
            0x00000003 -> "sip"
            else -> null
        }
    }

    fun getCurrentNetworkOperator(): String? {
        val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as? TelephonyManager
        return telephonyManager?.networkOperatorName
    }

    fun isWifiConnected(): Boolean? {
        var ret: Boolean? = null
        if (PackageManager.PERMISSION_GRANTED == context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_NETWORK_STATE)) {
            val connManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val wifiInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            ret = wifiInfo.isConnected
        }
        return ret
    }

    fun isBluetoothEnabled(): Boolean? {
        var isBluetoothEnabled: Boolean? = null
        try {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (bluetoothAdapter != null) {
                isBluetoothEnabled = bluetoothAdapter.isEnabled
            }
        } catch (e: SecurityException) {
            // do nothing since we don't have permissions
        }
        return isBluetoothEnabled
    }

    fun getBluetoothVersion(): String? {
        var bluetoothVersion: String? = null
        if (Build.VERSION.SDK_INT >= 8) {
            bluetoothVersion = "none"
            if (Build.VERSION.SDK_INT >= 18 &&
                context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)
            ) {
                bluetoothVersion = "ble"
            } else if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH)) {
                bluetoothVersion = "classic"
            }
        }
        return bluetoothVersion
    }

    // Unchanging facts
    val hasNFC: Boolean
    val hasTelephony: Boolean
    val displayMetrics: DisplayMetrics
    val appVersionName: String?
    val appVersionCode: Int?

    init {
        val packageManager = context.packageManager
        var foundAppVersionName: String? = null
        var foundAppVersionCode: Int? = null
        try {
            val packageInfo = packageManager.getPackageInfo(context.packageName, 0)
            foundAppVersionName = packageInfo.versionName
            foundAppVersionCode = packageInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            Log.w(LOGTAG, "System information constructed with a context that apparently doesn't exist.")
        }
        appVersionName = foundAppVersionName
        appVersionCode = foundAppVersionCode

        val packageManagerClass: Class<out PackageManager> = packageManager.javaClass
        var hasSystemFeatureMethod: Method? = null
        try {
            hasSystemFeatureMethod = packageManagerClass.getMethod("hasSystemFeature", String::class.java)
        } catch (e: NoSuchMethodException) {
            // Nothing, this is an expected outcome
        }
        var foundNFC: Boolean? = null
        var foundTelephony: Boolean? = null
        if (null != hasSystemFeatureMethod) {
            try {
                foundNFC = hasSystemFeatureMethod.invoke(packageManager, "android.hardware.nfc") as Boolean
                foundTelephony = hasSystemFeatureMethod.invoke(packageManager, "android.hardware.telephony") as Boolean
            } catch (e: InvocationTargetException) {
                Log.w(LOGTAG, "System version appeared to support PackageManager.hasSystemFeature, but we were unable to call it.")
            } catch (e: IllegalAccessException) {
                Log.w(LOGTAG, "System version appeared to support PackageManager.hasSystemFeature, but we were unable to call it.")
            }
        }
        hasNFC = foundNFC!!
        hasTelephony = foundTelephony!!
        displayMetrics = DisplayMetrics()
        val display = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
        display.getMetrics(displayMetrics)
    }
}
