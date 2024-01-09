# Android相机开发

在Android相机开发中有几个关键的类，最重要的入口类为Camera

Camera类在Android 2已废弃

推荐使用Camera2

# Android Camera2 API

`android.hardware.camera2` 是 Android 提供的先进的摄像头 API，用于更精细地控制摄像头的功能和参数。Camera2 API 引入了相机管道（Camera Pipeline）的概念，允许您对捕获图像的每个阶段进行精确控制。

Camera2 API 的主要特点包括：

- 支持多个摄像头：允许同时访问设备上的多个摄像头，例如前置摄像头和后置摄像头。
- 摄像头参数控制：您可以更精细地控制曝光、焦距、闪光灯、白平衡等摄像头参数。
- 高级捕获功能：提供了高级功能，如原始图像捕获、连拍、拍摄 RAW 图像等。
- 预览界面自定义：您可以自定义摄像头预览界面，实现特定的界面效果。
- 更好的性能：Camera2 API 提供了更高效的相机操作，适用于需要高性能摄像头操作的应用程序。

# Camera2 API 主要方法：

1. `CameraManager.openCamera(String cameraId, CameraDevice.StateCallback callback, Handler handler)`：打开指定 ID 的摄像头。
2. `CameraDevice.createCaptureSession(List<Surface> outputs, CameraCaptureSession.StateCallback callback, Handler handler)`：创建捕获会话，用于处理图像捕获请求。
3. `CaptureRequest.Builder`：用于构建捕获请求，包括设置曝光、焦距、闪光灯等参数。
4. `CameraCaptureSession.capture(CaptureRequest request, CameraCaptureSession.CaptureCallback callback, Handler handler)`：捕获单个图像。
5. `CameraCaptureSession.setRepeatingRequest(CaptureRequest request, CameraCaptureSession.CaptureCallback callback, Handler handler)`：连续捕获图像，用于预览。
6. `CameraCaptureSession.stopRepeating()`：停止连续捕获。
7. `CaptureRequest.CONTROL_AF_MODE`：设置自动对焦模式。
8. `CaptureRequest.CONTROL_AE_MODE`：设置自动曝光模式。
9. `CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER`：设置触发预曝光。
10. `CaptureRequest.FLASH_MODE`：设置闪光灯模式。

# 相机预览

使用

1.`SurfaceView`：`SurfaceView` 是一种用于显示摄像头预览的常用视图。它可以直接与底层图像缓冲区进行交互，通常用于实现相机预览界面。您可以使用 `Camera2 API` 或 `CameraX` 将摄像头的预览帧渲染到一个 `SurfaceView` 上。

```
SurfaceView surfaceView = findViewById(R.id.camera_preview);
SurfaceHolder surfaceHolder = surfaceView.getHolder();

// 在 Camera2 API 中设置预览目标为 SurfaceView
captureRequestBuilder.addTarget(surfaceHolder.getSurface());

// 或者在 CameraX 中配置预览用例的预览界面为 SurfaceView
Preview preview = new Preview.Builder().setTargetSurface(surfaceHolder.getSurface()).build();
```

2.`TextureView`：`TextureView` 是另一种用于渲染摄像头预览的视图，它提供更多的灵活性，支持实时缩放和旋转。您可以使用 `Camera2 API` 或 `CameraX` 将摄像头的预览帧渲染到一个 `TextureView` 上。

```
TextureView textureView = findViewById(R.id.camera_preview);
SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();

// 在 Camera2 API 中设置预览目标为 TextureView
surfaceTexture.setDefaultBufferSize(previewSize.getWidth(), previewSize.getHeight());
Surface surface = new Surface(surfaceTexture);
captureRequestBuilder.addTarget(surface);

// 或者在 CameraX 中配置预览用例的预览界面为 TextureView
Preview preview = new Preview.Builder().setTargetRotation(textureView.getDisplay().getRotation()).build();
```

# 相机美颜


如果您想在摄像头的预览界面中实时显示美颜效果，并且希望能够通过开关来启用或禁用美颜效果，您需要处理以下几个方面：

1. **美颜效果算法**：首先，您需要选择或实现一个美颜效果算法，例如磨皮、美白、色彩校正等。这个算法通常是基于图像处理的，您可以使用开源库或自己编写算法来实现。例如，可以使用 GPU 图像处理库（如 OpenGL ES）来执行美颜效果。
2. **预览界面**：您需要在摄像头预览界面上实时应用美颜效果。通常，这可以通过在预览帧上应用美颜算法来实现。如果您使用的是 CameraX，您可以使用 `ImageAnalysis` 用例来获取摄像头的预览帧，并在每一帧上应用美颜效果。如果您使用的是 Camera2 API，则需要在 `CameraCaptureSession` 中设置一个 `Surface`，然后将该 `Surface` 与美颜效果的处理流程连接起来。
3. **开关控制**：为了实现开关美颜效果的功能，您需要添加一个 UI 控件，例如开关按钮，供用户启用或禁用美颜效果。当用户切换开关状态时，您需要相应地启用或禁用美颜效果的处理。
4. **性能优化**：美颜效果通常会增加图像处理的计算负担，因此需要考虑性能优化。您可以使用多线程或 GPU 加速来提高处理速度，并确保不会影响摄像头预览的流畅性。

总之，要在预览界面中实现美颜效果，您需要处理图像处理算法、预览界面、开关控制和性能优化等方面。这需要一些图像处理和相机编程的知识，同时还需要了解您选择的相机 API（CameraX 或 Camera2 API）的用法。一旦实现了这些部分，用户就可以在摄像头预览界面上看到实时的美颜效果，并且可以通过开关控制来启用或禁用美颜。

# 图片美化


在拍照完成之后，您可以将照片打开到一个界面，然后对照片进行各种美化和调整操作，例如色彩校正、滤镜应用、裁剪、旋转、调整亮度/对比度等。这些美化和调整操作通常是通过图像处理库或框架来实现的。

以下是一些常见的图像处理库和框架，您可以使用它们来进行图像美化和调整：

1. **Android Bitmap 和 Canvas API**：Android 提供了一些基本的图像处理功能，如缩放、旋转、绘制文本和图形等，可以使用 `Bitmap` 和 `Canvas` 类来执行这些操作。
2. **OpenCV**：OpenCV 是一个强大的开源计算机视觉库，它提供了大量的图像处理算法和工具，可以用于实现各种图像美化和调整功能，包括滤镜、边缘检测、颜色校正等。
3. **GPU 图像处理库**：一些 GPU 图像处理库如 OpenGL ES、Vulkan 可以用于实现高性能的图像处理效果，例如应用滤镜和特效。
4. **第三方图像处理库**：还有许多第三方的 Android 图像处理库，如 Glide、Picasso、Fresco 等，它们提供了便捷的方式来加载、缓存和显示图像，并且支持一些基本的图像转换和处理。
5. **图像编辑 SDK**：如果您需要更高级的图像编辑功能，可以考虑使用专业的图像编辑 SDK，如 PhotoEditor SDK、Aviary SDK 等，它们提供了丰富的图像编辑功能和界面组件，可以嵌入到您的应用中。

您可以根据您的需求选择适合您应用的图像处理工具和库。通常，您需要加载拍摄的照片，然后使用选定的库或框架来应用所需的美化和调整效果。一旦完成，您可以将处理后的图像显示在界面上供用户查看，或者将其保存到设备或分享给其他应用。图像处理是一个广泛的主题，具体的实现方式和效果取决于您的创意和需求。