# Android配置和键值对的存储

```
sharedPreferences = context.getSharedPreferences("NLPrefs", context.MODE_PRIVATE);
editor = sharedPreferences.edit();
```

```
//存储一个字符串数据，需要传入的参数只需要类型，和名称，即可
public static void putString(String key, String value) {
    editor.putString(key, value);
    editor.apply();
    commit();
    Log.e(TAG, "数据已暂存" + value);
}

//读取一个字符串数据，只需要传入参数即可
public static String getString(String key) {
    return sharedPreferences.getString(key, "");
}


//存储一个int的数据
public static void putInt(String key, int value) {
    editor.putInt(key, value);
    editor.apply();
    commit();
    Log.e(TAG, "数据已暂存" + value);
}

//读取一个int的数据
public static int getInt(String key) {
    return sharedPreferences.getInt(key, 0);
}

//存储一个boolean的数据
public static void putBoolean(String key, boolean value) {
    editor.putBoolean(key, value);
    editor.apply();
    commit();
    Log.e(TAG, "数据已暂存" + value);
}

//读取一个boolean的数据
public static boolean getBoolean(String key) {
    return sharedPreferences.getBoolean(key, false);
}

//存储一个long值
public static void putLong(String key, long value) {
    editor.putLong(key, value);
    editor.apply();
    commit();
    Log.e(TAG, "数据已暂存" + value);
}

//读取一个long值
public static long getLong(String key) {
    return sharedPreferences.getLong(key, 0);
}

//存储一个float值
public static void putFloat(String key, float value) {
    editor.putFloat(key, value);
    editor.apply();
    commit();
    Log.e(TAG, "数据已暂存" + value);
}

//读取一个float值
public static float getFloat(String key) {
    return sharedPreferences.getFloat(key, 0);
}

//存储String 键值对集合
public static void putStringSet(String key, Set<String> set) {
    editor.putStringSet(key, set);
    editor.apply();
    commit();
    Log.e(TAG, "数据已暂存" + set.toString());
}

//读取String 键值对集合
public static Set<String> getStringSet(String key) {
    return sharedPreferences.getStringSet(key, null);
}

//提交更改
public static void commit() {
    editor.commit();
    Log.e(TAG, "数据已保存");
}

//清除缓存存储的数据
public static void clear() {
    editor.clear();
    editor.apply();
    commit();
    Log.e(TAG, "数据已清除，若要保存请调用 NLData.commit()");
}
```