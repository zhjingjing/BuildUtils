package com.zh.uuid;

import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.nfc.Tag;
import android.os.Build;
import android.util.Log;
import android.util.Pair;
import android.util.Size;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Administrator
 * @Time 2020/12/24 002418:29
 * @Email: zhaohang@zhizhangyi.com
 * @Describe:
 */
public class CameraUtils {

    public static int getCameraCount(Context ctx) {
        if (21 <= Build.VERSION.SDK_INT) {// 5.0 <=
            CameraManager manager = (CameraManager) ctx.getSystemService(Context.CAMERA_SERVICE);
            try {
                Log.e("CameraUtils","manager.getCameraIdList().length");
                return manager.getCameraIdList().length;
            } catch (Throwable e) {
                Log.e("CameraUtils",e.getMessage());
            }
        }
        try {
            Log.e("CameraUtils","Camera.getNumberOfCameras()");
            return Camera.getNumberOfCameras();
        } catch (Throwable e) {
            Log.e("CameraUtils",e.getMessage());
        }
        return -1;
    }


    public static String getCameraPixelsInString(Context ctx) {
        List<Pair<Boolean, Integer>> pixelArr = getCameraPixels(ctx);
        if (pixelArr == null || pixelArr.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < pixelArr.size(); i++) {
            if (i > 0) {
                sb.append(";");
            }
            sb.append(pixelArr.get(i).first).append(",").append(pixelArr.get(i).second);
        }
        return sb.toString();
    }

    public static List<Pair<Boolean, Integer>> getCameraPixels(Context ctx) {
        /*
         * native crash if not pre checked
         */
        DevicePolicyManager dpm = (DevicePolicyManager) ctx.getSystemService(Context.DEVICE_POLICY_SERVICE);
        boolean isDisabled = dpm.getCameraDisabled(null);
        if (isDisabled) {
            return null;
        }
        if (21 <= Build.VERSION.SDK_INT) {// 5.0 <=
            return getCameraPixelsAboveLollipop(ctx);
        } else {
            return getCameraPixelLegacy();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private static List<Pair<Boolean, Integer>> getCameraPixelsAboveLollipop(Context ctx) {
        try {
            CameraManager manager = (CameraManager) ctx.getSystemService(Context.CAMERA_SERVICE);
            String[] ids = manager.getCameraIdList();
            List<Pair<Boolean, Integer>> pixels = new ArrayList<>();
            for (String id : ids) {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(id);

                StringBuilder stringBuilder=new StringBuilder();
                for (CameraCharacteristics.Key key:characteristics.getKeys()){
                    stringBuilder.append(key.getName()+"---"+key.toString()+"\n");
                }
                Log.e("CameraUtils",stringBuilder.toString());


                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                Size arraySize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);

                Log.e("CameraUtils","arraySize= "+arraySize+"   CameraCharacteristics.LENS_FACING="+facing);
                if (arraySize == null || facing == null) {
                    continue;
                }
                Pair<Boolean, Integer> pixel = new Pair<Boolean, Integer>(facing == CameraMetadata.LENS_FACING_FRONT, arraySize.getHeight() * arraySize.getWidth());
                pixels.add(pixel);
            }
            return pixels;
        } catch (Throwable e) {
            Log.e("CameraUtils",e.getMessage());
        }
        return null;
    }

    public static List<Pair<Boolean, Integer>> getCameraPixelLegacy() {
        // Camera maybe disabled.
        int num;
        try {
            num = Camera.getNumberOfCameras();
            if (num == 0) {
                return null;
            }
        } catch (Throwable e) {
            return null;
        }
        List<Pair<Boolean, Integer>> pixels = new ArrayList<>(num);
        for (int i = 0; i < num; i++) {
            Camera camera = null;
            try {
                camera = Camera.open(i);
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);
                boolean isFront = cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT;

                int maxPixel = 0;
                Camera.Parameters params = camera.getParameters();
                List<Camera.Size> supportedList = params.getSupportedPictureSizes();
                for (Camera.Size size : supportedList) {
                    int curPixel = size.width * size.height;
                    if (curPixel > maxPixel) {
                        maxPixel = curPixel;
                    }
                }
                pixels.add(new Pair<Boolean, Integer>(isFront, maxPixel));
            } catch (Throwable e) {
                Log.e("CameraUtils",e.getMessage());
            } finally {
                if (camera != null) {
                    camera.release();
                }
            }
        }
        return pixels;
    }
}
