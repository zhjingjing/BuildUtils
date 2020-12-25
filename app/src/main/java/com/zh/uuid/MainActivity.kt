package com.zh.uuid

import android.content.Context
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import java.util.*
import android.telephony.TelephonyManager
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var stringBuffer: StringBuffer = StringBuffer()
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTest.text = getMyUUID()
        stringBuffer.append(getMyUUID()).append("\n CPUABI:" + BuildUtils.getCPUABI())
            .append("\n getId:" + BuildUtils.getId())
            .append("\n getProduct:" + BuildUtils.getProduct())
            .append("\n getDevice:" + BuildUtils.getDevice())
            .append("\n getBoard:" + BuildUtils.getBoard())
            .append("\n getBrand:" + BuildUtils.getBrand())
            .append("\n getBootLoader:" + BuildUtils.getBootLoader())
            .append("\n getHardware:" + BuildUtils.getHardware())
            .append("\n getManufacturer:" + BuildUtils.getManufacturer())
            .append("\n getOsVersion:" + BuildUtils.getOsVersion())
            .append("\n getModel:" + BuildUtils.getModel())
            .append("\n getSdkVersion:" + BuildUtils.getSdkVersion())
            .append("\n getUDID:" + BuildUtils.getUDID(this))
            .append("\n getSN:" + BuildUtils.getSN())
            .append("\n getCameraCount:" + CameraUtils.getCameraCount(this))
            .append("\n getCameraPixelsInString:" + CameraUtils.getCameraPixelsInString(this))
            .append("\n getWifiMacAddressFromNet: " + HardwareUtils.getWifiMacAddressFromNet(this))
            .append("\n getBTMacAddress: " + HardwareUtils.getBTMacAddress(this))
            .append("\n getBTMacAddress: " + HardwareUtils.getBluetoothAddress())
            .append("\n getDeviceIdFromTelephony: " + SimcardUtils.getDeviceIdFromTelephony(this))
            .append("\n getDeviceId2FromTelephony: " + SimcardUtils.getDeviceId2FromTelephony(this))
            .append("\n getDeviceId2FromTelephony: " + SimcardUtils.getDeviceId2FromTelephony(this))

        tvTest.text = stringBuffer.toString()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        tvTest.text = getMyUUID()
    }

    private fun getMyUUID(): String {
        val tm = baseContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        val tmDevice: String
        val tmSerial: String
        val tmPhone: String
        val androidId: String
        tmDevice = "" + tm.deviceId
        tmSerial = "" + tm.simSerialNumber
        androidId = "" + android.provider.Settings.Secure.getString(
            contentResolver,
            android.provider.Settings.Secure.ANDROID_ID
        )
        val deviceUuid =
            UUID(
                androidId.hashCode().toLong(),
                tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong()
            )
        val uniqueId = deviceUuid.toString()
        Log.d("debug", "uuid=$uniqueId")
        return uniqueId
    }
}
