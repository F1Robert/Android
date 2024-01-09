# Activity生命周期

### 什么是Activity？

在Android中，Activity是一种用于构建用户界面的基本组件之一。每个Activity都代表了用户与应用程序的一个互动界面。Activity可以执行各种操作，如显示UI、处理用户输入、启动其他Activity等。

### Activity生命周期

Activity的生命周期是指从Activity创建到销毁的整个过程。了解Activity生命周期非常重要，因为您可以在不同的生命周期阶段执行特定的操作，以确保应用程序正常运行和用户体验。

Android中的Activity生命周期有以下主要阶段：

1. **创建阶段（onCreate）：** 在Activity第一次创建时调用，用于初始化Activity。在此阶段通常执行一次性的操作，如设置布局、初始化变量等。
2. **启动阶段（onStart）：** 在Activity可见但未与用户交互时调用。在此阶段可以执行一些初始化操作。
3. **恢复阶段（onResume）：** 在Activity可见且可以与用户交互时调用。在此阶段可以开始处理用户输入、启动动画等。
4. **暂停阶段（onPause）：** 当其他Activity被启动或部分遮挡当前Activity时调用。在此阶段通常需要保存临时数据或停止不必要的操作。
5. **停止阶段（onStop）：** 当Activity不再可见时调用。在此阶段可以释放资源或停止后台任务。
6. **销毁阶段（onDestroy）：** 在Activity销毁之前调用，用于清理资源和执行必要的收尾操作。
7. **重启阶段（onRestart）：** 当Activity从停止状态重新启动时调用，通常会在这里进行一些初始化操作。

下面是一个示例Activity的代码，演示了生命周期方法的调用：

```
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MyActivity extends Activity {

@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_my);

​    Log.d("MyActivity", "onCreate");
}

@Override
protected void onStart() {
    super.onStart();
    Log.d("MyActivity", "onStart");
}

@Override
protected void onResume() {
    super.onResume();
    Log.d("MyActivity", "onResume");
}

@Override
protected void onPause() {
    super.onPause();
    Log.d("MyActivity", "onPause");
}

@Override
protected void onStop() {
    super.onStop();
    Log.d("MyActivity", "onStop");
}

@Override
protected void onDestroy() {
    super.onDestroy();
    Log.d("MyActivity", "onDestroy");
}

@Override
protected void onRestart() {
    super.onRestart();
    Log.d("MyActivity", "onRestart");
}

}
```

在这个示例中，我们重写了Activity的生命周期方法，并在每个方法中添加了Log来显示生命周期的调用情况。

### 生命周期示例

假设我们从应用程序的主界面启动了上述Activity。以下是Activity的典型生命周期顺序：

1. 首先调用 `onCreate` 方法，进行初始化。
2. 接着调用 `onStart` 和 `onResume` 方法，Activity变得可见并且可以与用户交互。
3. 如果我们按下了Home按钮或启动了其他Activity，将会依次调用 `onPause`、`onStop` 方法，Activity变得不可见。
4. 如果再次返回到Activity，将会调用 `onRestart`、`onStart`、`onResume` 方法，Activity重新变得可见。
5. 最后，如果我们按下了返回按钮或调用了 `finish()` 方法，将会依次调用 `onPause`、`onStop`、`onDestroy` 方法，Activity被销毁。