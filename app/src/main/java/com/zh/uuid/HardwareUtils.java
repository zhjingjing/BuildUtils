package com.zh.uuid;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: Administrator
 * @Time 2020/12/25 00259:41
 * @Email: zhaohang@zhizhangyi.com
 * @Describe:
 */
public class HardwareUtils {

    public static String getWifiMacAddressFromNet(Context context) {
        String macAddress = null;
        try {
            if (23 <= Build.VERSION.SDK_INT) {
                Log.e("HardwareUtils", "23 <= Build.VERSION.SDK_INT");
                NetworkInterface intf;
                Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
                while (interfaces.hasMoreElements()) {
                    intf = interfaces.nextElement();
                    if (null == intf ||
                            !TextUtils.equals("wlan0", intf.getName().toLowerCase())
                    ) {
                        continue;
                    }

                    final byte[] mac = intf.getHardwareAddress();
                    if (mac != null) {
                        StringBuilder buf = new StringBuilder();
                        for (byte aMac : mac) {
                            buf.append(String.format("%02X:", aMac));
                        }
                        if (buf.length() > 0) {
                            buf.deleteCharAt(buf.length() - 1);
                        }
                        macAddress = buf.toString();
                    }

                    break;
                }
            } else {
                Log.e("HardwareUtils", "<23");
                /*
                 * add try-catch to handle android sdk crash:
                 *
                 * E/AndroidRuntime(30481): java.lang.ClassCastException: AEA555BA07CBA87C8B01BDE35033FCD6874CB3FD0 cannot be cast to java.lang.Enum[]
                 * E/AndroidRuntime(30481): 	at java.lang.Enum.getSharedConstants(Enum.java:209)
                 * E/AndroidRuntime(30481): 	at java.lang.Enum.valueOf(Enum.java:189)
                 * E/AndroidRuntime(30481): 	at android.net.wifi.SupplicantState.valueOf(SupplicantState.java:33)
                 * E/AndroidRuntime(30481): 	at android.net.wifi.SupplicantState$1.createFromParcel(SupplicantState.java:255)
                 * E/AndroidRuntime(30481): 	at android.net.wifi.SupplicantState$1.createFromParcel(SupplicantState.java:253)
                 * E/AndroidRuntime(30481): 	at android.net.wifi.WifiInfo$1.createFromParcel(WifiInfo.java:360)
                 * E/AndroidRuntime(30481): 	at android.net.wifi.WifiInfo$1.createFromParcel(WifiInfo.java:343)
                 * E/AndroidRuntime(30481): 	at android.net.wifi.IWifiManager$Stub$Proxy.getConnectionInfo(IWifiManager.java:687)
                 * E/AndroidRuntime(30481): 	at android.net.wifi.WifiManager.getConnectionInfo(WifiManager.java:829)
                 * E/AndroidRuntime(30481): 	at com.uusafe.appmaster.n.aa.f(Unknown Source)
                 */
                WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                if (wifi != null) {
                    WifiInfo wifiInfo = wifi.getConnectionInfo();
                    if (wifiInfo != null) {
                        macAddress = wifiInfo.getMacAddress();
                    }
                }
            }
        } catch (Throwable e) {
            Log.e("HardwareUtils", "getWifiMacAddressFromNet:" + e.getMessage());
        }
        return macAddress;
    }

    private static String sDefaultWifiMac = null;
    private static String sDefaultBluetoothMac = null;

    static boolean isValidBluetoothMac(String _addr) {
        if (TextUtils.isEmpty(_addr)) return false;
        final String addr = _addr.toLowerCase();
        if (TextUtils.equals("02:00:00:00:00:00", addr)) return false;
        if (TextUtils.isEmpty(sDefaultBluetoothMac)) {
            try {
                final Class<?> cls = Class.forName("android.bluetooth.BluetoothAdapter");
                final Field f = cls.getDeclaredField("DEFAULT_MAC_ADDRESS");
                f.setAccessible(true);
                final String mac = (String) f.get(null);
                if (!TextUtils.isEmpty(mac)) {
                    sDefaultBluetoothMac = mac.toLowerCase();
                }
            } catch (Throwable e) {
                Log.e("HardwareUtils", "isValidBluetoothMac:" + e.getMessage());
            }
        }
        return !TextUtils.equals(sDefaultBluetoothMac, addr);
    }


    public static String getBTMacAddress(Context context) {
        String addr = null;
        try {
            if (23 <= Build.VERSION.SDK_INT) {
                addr = android.provider.Settings.Secure.getString(context.getContentResolver(), "bluetooth_address");
            }
            if (TextUtils.isEmpty(addr)) {
                Log.e("HardwareUtils", "获取不到啊，走默认了");
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                //获取已经保存过的设备信息
                Set<BluetoothDevice> devices = adapter.getBondedDevices();
                if (devices.size()>0) {
                    for(Iterator<BluetoothDevice> iterator = devices.iterator(); iterator.hasNext();){
                        BluetoothDevice bluetoothDevice=(BluetoothDevice)iterator.next();
                        Log.e("HardwareUtils", "设备："+bluetoothDevice.getName() + " " + bluetoothDevice.getAddress());
                        addr=bluetoothDevice.getAddress();
                    }
                }
                if (TextUtils.isEmpty(addr)){
                    addr = adapter.getAddress();
                }
            }
        } catch (Throwable e) {
            Log.e("HardwareUtils", "getBTMacAddress:" + e.getMessage());
        }
        return addr;
    }

    /**
     * 获取蓝牙地址
     *
     * @return
     */
    public static String getBluetoothAddress() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Field field = bluetoothAdapter.getClass().getDeclaredField("mService");
            // 参数值为true，禁用访问控制检查
            field.setAccessible(true);
            Object bluetoothManagerService = field.get(bluetoothAdapter);
            if (bluetoothManagerService == null) {
                return "bluetoothManagerService==null";
            }
            Method method = bluetoothManagerService.getClass().getMethod("getAddress");
            Object address = method.invoke(bluetoothManagerService);
            if (address != null && address instanceof String) {
                return (String) address;
            } else {
                return "空的、、";
            }

        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "空的、、";
    }
}
