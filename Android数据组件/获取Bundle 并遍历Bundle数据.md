# 获取Bundle 并遍历Bundle数据

### 获取一个bundle : val bundle: Bundle = data.extras!!

### 遍历输出bundle的所有内容

```
override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    Log.d(TAG, "onActivityResult: ")
    if (data != null && data.extras != null) {
        val bundle: Bundle = data.extras!!
        for (key in bundle.keySet()) {
            val value = bundle.get(key)
            Log.d(TAG, "Key: $key Value: $value")
        }
    }
    super.onActivityResult(requestCode, resultCode, data)
}
```