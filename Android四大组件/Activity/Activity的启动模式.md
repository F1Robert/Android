# Activity的启动模式

### 什么是Activity启动模式？

Activity启动模式是Android应用程序中用于管理Activity实例的规则和策略。Android系统提供了不同的启动模式，以允许开发者控制Activity的行为，例如如何创建新的Activity实例、如何与现有实例交互等。

### 常见的Activity启动模式

1. **Standard（标准模式）：** 这是默认的启动模式，每次启动Activity都会创建一个新的实例，不管是否已经存在相同的Activity实例。适用于大多数情况。
2. **SingleTop（单顶模式）：** 如果要启动的Activity已经位于任务栈的顶部（即位于栈顶），则不会创建新的实例，而是重新使用已有的实例。适用于处理通知等情况。
3. **SingleTask（单任务模式）：** 无论要启动的Activity是否已经存在，都会将其移动到任务栈的顶部并且只创建一个实例。适用于需要单一入口点的情况，如应用程序的主屏幕。
4. **SingleInstance（单实例模式）：** 每个Activity都会有一个独立的任务栈，并且只有一个实例。适用于需要单独的、全局的实例，如电话应用。

### 为什么需要这些启动模式？

Android的Activity启动模式是为了允许开发者更好地控制Activity的生命周期和行为，以适应不同的应用场景和需求：

1. **避免重复创建：** 使用SingleTop和SingleTask模式可以避免重复创建相同的Activity实例，提高性能和用户体验。
2. **任务栈管理：** 启动模式允许您管理Activity所属的任务栈，以便在多个Activity之间进行导航和控制。
3. **单一入口点：** SingleInstance模式允许创建全局单一实例，适用于需要单一入口点的情况。
4. **通知处理：** 通过选择适当的启动模式，您可以更好地处理通知和外部事件，以确保用户界面的正确显示和交互。

### 示例代码

以下是一个示例代码，演示如何在AndroidManifest.xml中定义Activity启动模式：

```
<activity
    android:name=".MainActivity"
    android:label="Standard Activity"
    android:launchMode="standard">
</activity>

<activity
    android:name=".SingleTopActivity"
    android:label="SingleTop Activity"
    android:launchMode="singleTop">
</activity>

<activity
    android:name=".SingleTaskActivity"
    android:label="SingleTask Activity"
    android:launchMode="singleTask">
</activity>

<activity
    android:name=".SingleInstanceActivity"
    android:label="SingleInstance Activity"
    android:launchMode="singleInstance">
</activity>
```

