package com.zh.uuid;

import android.content.Context;
import android.content.res.Configuration;
import android.telephony.TelephonyManager;
import android.util.Log;

import static android.telephony.TelephonyManager.PHONE_TYPE_NONE;

/**
 * @Author: Administrator
 * @Time 2020/12/25 0025 17:21
 * @Email: zhaohang@zhizhangyi.com
 * @Describe:
 */
public class RootUtils {
    private static final String TAG="RootUtils";


    /**
     * @return
     * 有的手机虽然是正式发布版，但是还是test-keys
     */
    public static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        Log.e(TAG,"checkRootMethod1: "+buildTags);
        //test-keys  release-keys
        return buildTags != null && buildTags.contains("test-keys");
    }





    public static String getOsModel(Context context){
        try {
            TelephonyManager telephoneManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            return (telephoneManager.getPhoneType()!=PHONE_TYPE_NONE || !isTablet(context))?"Android Phone":"Android Pad";
        }catch (Throwable e){
            Log.e(TAG,e.getMessage());
        }
        return null;
    }

    /**
     * @param context
     * @return
     * 是否是平板
     */
    public static boolean isTablet(Context context){
        try {
            Log.e(TAG,context.getResources().getConfiguration().screenLayout+"  "+(context.getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK));
            return (context.getResources().getConfiguration().screenLayout &
                    Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
        } catch (Throwable t) {
            Log.e(TAG,t.getMessage());
            return false;
        }
    }
}
