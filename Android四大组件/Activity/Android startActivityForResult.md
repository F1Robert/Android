# Android startActivityForResult

`startActivityForResult` 是Android中的一个方法，用于启动一个新的Activity并期望在新Activity执行完后返回结果给调用它的Activity。通常，这个方法用于实现两个Activity之间的交互，其中一个Activity通过`startActivityForResult`启动另一个Activity，然后等待另一个Activity执行完毕并返回结果。

以下是使用 `startActivityForResult` 方法的一般步骤：

1.在调用Activity（称为"发起Activity"）中调用 `startActivityForResult` 方法以启动目标Activity。

```
Intent intent = new Intent(this, TargetActivity.class);
startActivityForResult(intent, requestCode);
```

2.在目标Activity中，当执行完所需的操作后，通过`setResult`方法设置要返回的数据，并关闭目标Activity。

```
Intent resultIntent = new Intent();
resultIntent.putExtra("key", data); // 将结果数据放入Intent中
setResult(Activity.RESULT_OK, resultIntent);
finish(); // 关闭目标Activity
```

3.在发起Activity中，重写 `onActivityResult` 方法以接收从目标Activity返回的结果。

```
@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    

if (requestCode == requestCode && resultCode == Activity.RESULT_OK) {
    // 处理从目标Activity返回的数据
    if (data != null) {
        String resultData = data.getStringExtra("key"); // 从Intent中获取结果数据
        // 进一步处理结果数据
    }
}
}
```

在 `onActivityResult` 方法中，您可以根据 `requestCode` 和 `resultCode` 来处理不同的请求和结果。

这就是使用 `startActivityForResult` 方法来实现Activity之间交互的基本步骤。通过这种方式，您可以在Android应用程序中实现诸如获取用户输入、选择文件、拍照等操作，并在返回结果时进行适当的处理。