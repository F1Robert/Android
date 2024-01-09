# Android的不同版本调用Service的方法

![image-20240103115127692](https://s2.loli.net/2024/01/03/DCzjXoSyRfeV5Jk.png)

```
public static void start(Context context, Intent intent) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        context.startForegroundService(intent);
    } else {
        context.startService(intent);
    }
}
```