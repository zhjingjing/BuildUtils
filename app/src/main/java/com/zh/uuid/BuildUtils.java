package com.zh.uuid;

import android.content.Context;
import android.provider.Settings;
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
}
