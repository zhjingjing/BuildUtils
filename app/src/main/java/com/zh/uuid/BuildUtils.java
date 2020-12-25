package com.zh.uuid;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

/**
 * @Author: Administrator
 * @Time 2020/12/24 002416:14
 * @Email: zhaohang@zhizhangyi.com
 * @Describe:
 */
public class BuildUtils {
    private static final String TAG = "BuildUtils";

    public static String getOsVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static String getId(){
        return Build.ID;
    }

    public static String  getProduct(){
        return Build.PRODUCT;
    }

    public static String  getDevice(){
        return Build.DEVICE;
    }
    public static String  getBoard(){
        return Build.BOARD;
    }

    public static String  getBrand(){
        return Build.BRAND;
    }

    public static String  getBootLoader(){
        return Build.BOOTLOADER;
    }

    public static String  getHardware(){
        return Build.HARDWARE;
    }
    /**
     * @return e.g. G521-L076 or (HUAWEI G521-L076)
     */
    public static String getModel() {
        return android.os.Build.MODEL;
    }

    public static String getCPUABI() {
        return android.os.Build.CPU_ABI;
    }

    /**
     * 获取厂商
     *
     * @return e.g. HUAWEI
     */
    public static String getManufacturer() {
        return android.os.Build.MANUFACTURER;
    }

    public static int getSdkVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    public static String getUDID(Context context) {
        try {
            return Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        } catch (Throwable e) {
            Log.e(TAG, "getUDID", e);
            return "android_id_unknown";
        }
    }

    public static String getSN() {
        String val = null;
        if (26 <= Build.VERSION.SDK_INT) {
            try {
                val = Build.getSerial();
            } catch (Throwable ignore) {
            }
        }
        if (TextUtils.isEmpty(val)) {
            try {
                val = Build.SERIAL;
            } catch (Throwable ignore) {
            }
        }
        return TextUtils.isEmpty(val) ? Build.UNKNOWN : val;
    }

}
