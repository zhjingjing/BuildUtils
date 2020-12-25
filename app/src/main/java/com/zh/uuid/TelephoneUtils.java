package com.zh.uuid;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * @Author: Administrator
 * @Time 2020/12/25 002515:49
 * @Email: zhaohang@zhizhangyi.com
 * @Describe:
 */
public class TelephoneUtils {

    public static String getLine1Number(Context context){
        TelephonyManager  telephoneManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return  telephoneManager.getLine1Number();
    }




}
