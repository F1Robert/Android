package com.nolan.utills.permissonlib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.util.Log;

/**
 * 作者 zf
 * 日期 2024/1/15
 */
public class PermissionManager {
    public static PermissionManager instance;

    public static PermissionManager getInstance() {
        return instance;
    }

    public static String[] Permissions;

    public static void init(Context context) {
        instance = new PermissionManager();
        PackageManager packageManager = context.getPackageManager();
        try {
            // 获取应用的包信息
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);

            // 获取应用声明的所有权限
            String[] requestedPermissions = packageInfo.requestedPermissions;

            if (requestedPermissions != null) {
                for (String permission : requestedPermissions) {
                    PermissionInfo permissionInfo = packageManager.getPermissionInfo(permission, 0);
                    Log.d("PermissionUtils", "Permission: " + permissionInfo.name);
                }
            }
            Permissions = requestedPermissions;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String[] getPermissions() {
        return Permissions;
    }
}
