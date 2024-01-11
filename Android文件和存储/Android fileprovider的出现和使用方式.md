# Android fileprovider的出现和使用方式

Android FileProvider 是 Android 提供的一种内容提供器，用于安全地共享应用程序的内部文件或文件存储目录中的文件，以便其他应用程序可以访问这些文件。FileProvider 主要用于替代 `file://` URI 方案，以增加应用程序之间的文件共享的安全性和隐私性。

原因： Android引入了FileProvider的主要原因是出于安全性和隐私性的考虑。在过去，应用程序可能会共享 `file://` URI，但这会带来一些潜在的风险：

1. 权限不受控制：使用 `file://` URI 可能导致敏感数据泄露，因为其他应用程序可以访问您的文件，而不受您的应用程序权限的限制。
2. Android 7.0+ 版本中的文件共享变更：从 Android 7.0 开始，应用程序不再能够通过传递 `file://` URI 来访问其他应用程序的文件，因为这可能会导致安全问题。

FileProvider 通过将文件访问限制在应用程序之间，提供了更安全的文件共享方式。它允许您在清单文件中声明哪些文件可以与其他应用程序共享，并使用 `content://` URI 替代 `file://` URI。

使用方法：

以下是使用 FileProvider 的基本步骤：

在 AndroidManifest.xml 文件中注册 FileProvider：

在 `<application>` 元素内添加以下代码：

```
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="com.example.yourapp.fileprovider"
    android:grantUriPermissions="true"
    android:exported="false">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths"/>
</provider>
```

创建一个 XML 资源文件来定义共享文件的路径规则。在 `res/xml/` 目录下创建一个 `file_paths.xml` 文件，内容可能如下：

```
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path name="external_files" path="."/>
</paths>
```

在应用程序中创建一个文件并获取其 URI：

```
File file = new File(getExternalFilesDir(null), "example.txt");
Uri fileUri = FileProvider.getUriForFile(this, "com.example.yourapp.fileprovider", file);
```

如果要将文件发送给其他应用程序，您可以在 Intent 中使用文件的 URI：

```
Intent intent = new Intent(Intent.ACTION_SEND);
intent.setType("text/plain");
intent.putExtra(Intent.EXTRA_STREAM, fileUri);
```

如果其他应用程序需要访问您的文件，它们可以请求 URI 权限。这通常涉及到检查权限并请求权限，然后使用该权限来访问文件。

FileProvider 提供了一个更安全的方式来共享文件，以确保敏感数据不会泄露给不应该访问它们的应用程序。它是 Android 中的一个重要工具，用于改进应用程序之间的文件共享。

