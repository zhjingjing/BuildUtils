package com.zh.uuid;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Administrator
 * @Time 2020/12/25 0025 11:40
 * @Email: zhaohang@zhizhangyi.com
 * @Describe:
 *
 * 这里获取不到的可以通过设置deviceOwner获取到
 */
public class SimcardUtils {

    public static String getDeviceIdFromTelephony(Context context) {
        String deviceId = null;
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) { // > 4.4
                Method m = TelephonyManager.class.getMethod("getImei");
                m.setAccessible(true);
                deviceId = (String) m.invoke(telephonyManager);
                Log.e("SimcardUtils", "invoke获取不到啊"+deviceId);
            }
            if (TextUtils.isEmpty(deviceId)) {
                if (Build.VERSION.SDK_INT>Build.VERSION_CODES.M){
                    deviceId = telephonyManager.getDeviceId(0);
                    Log.e("SimcardUtils", "getDeviceId(0)"+deviceId);
                }
                if (TextUtils.isEmpty(deviceId)){
                    deviceId = telephonyManager.getDeviceId();
                    Log.e("SimcardUtils", "getDeviceId "+deviceId);
                }
            }
            Log.e("SimcardUtils", BuildUtils.getSN()+" ---BuildUtils.getSN()获取不到啊");
            if (TextUtils.equals(deviceId, BuildUtils.getSN())) {
                deviceId = null;
            }
        } catch (Throwable e) {
            Log.e("SimcardUtils", "获取不到啊");
        }
        return deviceId;
    }

    public static String getDeviceId2FromTelephony(Context context) {
        try {
            String defaultDeviceId = getDeviceIdFromTelephony(context);
            if (TextUtils.isEmpty(defaultDeviceId)) {
                return null;
            }
            final TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final int simCount = getCount(telephonyManager);
            for (int slotId = simCount - 1; slotId >= 0; slotId--) {
                String imei = getImei(telephonyManager, slotId);
                if (TextUtils.isEmpty(imei)) {
                    continue;
                }
                if (!defaultDeviceId.equals(imei)) {
                    return imei;
                }
            }
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                for (int slotId = simCount - 1; slotId >= 0; slotId--) {
                    Method m = TelephonyManager.class.getDeclaredMethod("getDeviceId", new Class[]{int.class});
                    m.setAccessible(true);
                    @SuppressLint("HardwareIds")
                    String deviceId =(String) m.invoke(telephonyManager, slotId);
                    if (TextUtils.isEmpty(deviceId)) {
                        continue;
                    }
                    if (!defaultDeviceId.equals(deviceId)) {
                        return deviceId;
                    }
                }
            }
        } catch (Throwable e) {
            Log.e("SimcardUtils", "获取不到啊 就出错了"+e.getMessage());
        }
        return null;
    }

    private static int getCount(TelephonyManager telephonyManager) {
        try {
            Method m = TelephonyManager.class.getDeclaredMethod("getSimCount");
            m.setAccessible(true);
            final int cnt = (int) m.invoke(telephonyManager);
            return Math.max(cnt, 2);
        } catch (Throwable e) {
            Log.e("SimcardUtils", "获取不到啊 就出错了"+e.getMessage());
        }
        return 0;
    }

    private static String getImei(TelephonyManager telephonyManager, int slotId) {
        try {
            Method m = TelephonyManager.class.getDeclaredMethod("getImei", new Class[]{int.class});
            m.setAccessible(true);
            return (String) m.invoke(telephonyManager, slotId);
        } catch (Throwable e) {
            Log.e("SimcardUtils", "获取不到啊 就出错了"+e.getMessage());
        }
        return null;
    }

}
