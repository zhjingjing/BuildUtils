package com.zh.uuid

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.util.UUID.randomUUID
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import java.util.*
import android.hardware.usb.UsbDevice.getDeviceId
import android.content.Context.TELEPHONY_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import android.telephony.TelephonyManager
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import kotlinx.android.synthetic.main.activity_main.*




class MainActivity : AppCompatActivity() {
    var stringBuffer:StringBuffer = StringBuffer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvTest.text=getMyUUID()
        stringBuffer.append(getMyUUID()).append("\n CPUABI:"+BuildUtils.getCPUABI())
            .append("\n "+BuildUtils.getManufacturer())
            .append("\n osVersion:"+BuildUtils.getOsVersion())
            .append("\n model:"+BuildUtils.getModel())
            .append("\n sdkVersion:"+BuildUtils.getSdkVersion())
            .append("\n getUDID:"+BuildUtils.getUDID(this))
        tvTest.text=stringBuffer.toString()

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            requestPermissions(arrayOf(android.Manifest.permission.READ_PRECISE_PHONE_STATE,Manifest.permission.READ_PHONE_STATE),1101)
//        }else{
//        }
    }

//    private fun getMyUUID(): String {
//        val uuid = UUID.randomUUID()
//        val uniqueId = uuid.toString()
//        Log.d("debug", "----->UUID$uuid")
//        return uniqueId
//    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            tvTest.text=getMyUUID()
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
            UUID(androidId.hashCode().toLong(), tmDevice.hashCode().toLong() shl 32 or tmSerial.hashCode().toLong())
        val uniqueId = deviceUuid.toString()
        Log.d("debug", "uuid=$uniqueId")
        return uniqueId
    }
}
