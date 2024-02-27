package com.nolan.utills.threaddemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import android.widget.Button;

import com.nolan.utills.permissonlib.PermissionManager;
import com.nolan.utills.threaddemo.databinding.ActivityMainBinding;

import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final int BLUETOOTH = 100;
    private static final int CAMERA = 101;
    private static final String TAG = "zf";
    private ActivityMainBinding binding;

    private Button btn_1;
    private Button btn_2;
    private Button btn_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mHandlerThread mHandlerThread = new mHandlerThread("mHandlerThread");
        mHandlerThread.start();
        btn_1 = binding.btn1;
        btn_2 = binding.btn2;
        btn_3 = binding.btn3;
        btn_1.setOnClickListener(v -> {
            Log.e(TAG, "onCreate: 主线程");
            Thread threadMain = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + Thread.currentThread().getName());
                }
            });
            new Handler().post(threadMain);
        });
        btn_2.setOnClickListener(v -> {
            Log.e(TAG, "onCreate: Handler线程");
            Thread threadMain = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + Thread.currentThread().getName());
                }
            });
            new Handler(mHandlerThread.getLooper()).post(threadMain);
        });
        btn_3.setOnClickListener(v -> {
            Log.e(TAG, "onCreate: 其它创建的子线程");
            Thread threadMain = new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "run: " + Thread.currentThread().getName());
                }
            });
        });


        if (EasyPermissions.hasPermissions(this, PermissionManager.Permissions)) {
            for (String permission : PermissionManager.Permissions) {
                Log.e(TAG, "onCreate: " + permission + "已获取");
            }

        } else {
            EasyPermissions.requestPermissions(this, "bluetooth", CAMERA, PermissionManager.Permissions);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG, "onPermissionsGranted: " + requestCode);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.e(TAG, "onPermissionsDenied: " + requestCode);
    }

    private class mHandlerThread extends HandlerThread {
        public mHandlerThread(String name) {
            super(name);
        }

        public mHandlerThread(String name, int priority) {
            super(name, priority);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}