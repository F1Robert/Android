# Android权限申请框架

https://github.com/googlesamples/easypermissions

```
dependencies {
    // For developers using AndroidX in their applications
    implementation 'pub.devrel:easypermissions:3.0.0'
 
    // For developers using the Android Support Library
    implementation 'pub.devrel:easypermissions:2.0.1'
}
```

```
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
```

监听回调

```
public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
}

@Override
public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

​    // Forward results to EasyPermissions
​    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
}

@Override
public void onPermissionsGranted(int requestCode, List<String> list) {
    // Some permissions have been granted
    // ...
}

@Override
public void onPermissionsDenied(int requestCode, List<String> list) {
    // Some permissions have been denied
    // ...
}

}
```

