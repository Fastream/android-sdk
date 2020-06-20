package io.fastream.sdk

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import com.google.gson.JsonObject

class DefaultPropertiesFactory (
    private val context: Context
) {

    private val mSystemInformation = SystemInformation(context)

    fun createDefaultEventProperties(): JsonObject {
        val defaultProperties = JsonObject()
        defaultProperties.addProperty("fastream_sdk", "android")
        defaultProperties.addProperty("\$lib_version", BuildConfig.VERSION_NAME)

        // For querying together with data from other libraries
        defaultProperties.addProperty("distinct_id", mSystemInformation.getDeviceId())
        defaultProperties.addProperty("\$os", "Android")
        defaultProperties.addProperty("\$os_version", if (Build.VERSION.RELEASE == null) "UNKNOWN" else Build.VERSION.RELEASE)
        defaultProperties.addProperty("\$manufacturer", if (Build.MANUFACTURER == null) "UNKNOWN" else Build.MANUFACTURER)
        defaultProperties.addProperty("\$brand", if (Build.BRAND == null) "UNKNOWN" else Build.BRAND)
        defaultProperties.addProperty("\$model", if (Build.MODEL == null) "UNKNOWN" else Build.MODEL)
        val displayMetrics: DisplayMetrics = mSystemInformation.displayMetrics
        defaultProperties.addProperty("\$screen_dpi", displayMetrics.densityDpi)
        defaultProperties.addProperty("\$screen_height", displayMetrics.heightPixels)
        defaultProperties.addProperty("\$screen_width", displayMetrics.widthPixels)
        val applicationVersionName: String? = mSystemInformation.appVersionName
        if (null != applicationVersionName) defaultProperties.addProperty("\$app_version", applicationVersionName)
        val hasNFC: Boolean = mSystemInformation.hasNFC
        if (null != hasNFC) defaultProperties.addProperty("\$has_nfc", hasNFC)
        val hasTelephony: Boolean = mSystemInformation.hasTelephony
        if (null != hasTelephony) defaultProperties.addProperty("\$has_telephone", hasTelephony)
        val carrier: String? = mSystemInformation.getCurrentNetworkOperator()
        if (null != carrier) defaultProperties.addProperty("\$carrier", carrier)
        val isWifi: Boolean? = mSystemInformation.isWifiConnected()
        if (null != isWifi) defaultProperties.addProperty("\$wifi", isWifi)
        val isBluetoothEnabled: Boolean? = mSystemInformation.isBluetoothEnabled()
        if (isBluetoothEnabled != null) defaultProperties.addProperty("\$bluetooth_enabled", isBluetoothEnabled)
        val bluetoothVersion: String? = mSystemInformation.getBluetoothVersion()
        if (bluetoothVersion != null) defaultProperties.addProperty("\$bluetooth_version", bluetoothVersion)
        return defaultProperties
    }

}
