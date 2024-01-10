# Android TextureView

`TextureView` 是 Android 提供的一个用于显示图像或视频的视图组件，它是 `View` 的子类，与常见的 `SurfaceView` 不同，`TextureView` 可以用于在 UI 层面渲染 GPU 图像，支持实时变换和动画，并提供更多的灵活性。

以下是 `TextureView` 的一些重要特点和用法：

1. **实时渲染**：`TextureView` 可以用于在 UI 层面实时渲染 GPU 图像，这意味着您可以将图像、视频、相机预览等内容直接绘制到 `TextureView` 上，而不需要在 `SurfaceView` 上绘制。
2. **支持动画和变换**：与 `SurfaceView` 不同，`TextureView` 支持动画和变换，您可以通过属性动画或矩阵变换来实现平移、缩放、旋转等效果。
3. **透明度和遮罩**：`TextureView` 支持设置透明度和遮罩，可以创建更丰富的视觉效果。
4. **硬件加速**：`TextureView` 利用硬件加速来提高性能，对于图像渲染非常有效。
5. **与 Camera2 API 或相机库配合使用**：`TextureView` 可以与 Camera2 API 或一些相机库（如 CameraX）一起使用，将相机预览实时显示在 `TextureView` 上，从而实现自定义相机应用。
6. **自定义绘制**：除了与相机一起使用，您还可以在 `TextureView` 上自定义绘制图像、图形、文本等，从而创建自己的图像处理效果。

