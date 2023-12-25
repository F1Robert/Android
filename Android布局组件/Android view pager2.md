# Android view pager2

使用场景

它是用于实现页面滑动（Swiping）的控件，通常用于创建可滑动的视图页面。`ViewPager2` 是 AndroidX 库中的一部分，取代了早期的 `ViewPager`，并提供了更强大的功能和性能改进。

以下是一些关于 `ViewPager2` 的主要特点和用途：

1. **支持垂直和水平滑动**：与 `ViewPager` 不同，`ViewPager2` 允许您轻松地配置页面垂直或水平滑动，从而支持更多的滑动方向。
2. **数据适配器**：您可以通过提供自定义的 `RecyclerView.Adapter` 来填充 `ViewPager2` 的内容，这使得它更加灵活，可以适应各种数据源。
3. **生命周期感知**：`ViewPager2` 支持与 `Fragment` 和 `FragmentState` 相关联，可以在视图层次结构发生变化时适当管理页面的生命周期。
4. **动画和转换**：`ViewPager2` 允许您设置页面切换时的动画效果和页面之间的转换效果。
5. **内置滑动手势处理**：`ViewPager2` 内置了滑动手势处理，使得您可以轻松实现触摸滑动效果。
6. **触摸事件分发**：`ViewPager2` 支持自定义触摸事件分发，允许您根据需要拦截或处理触摸事件。
7. **指示器**：虽然 `ViewPager2` 本身不提供指示器，但您可以轻松地将自定义指示器与其集成，以显示当前页面位置。
8. **性能优化**：与 `ViewPager` 相比，`ViewPager2` 在性能方面有所改进，更好地适应大量页面和数据。
9. **AndroidX 兼容性**：`ViewPager2` 属于 AndroidX 库，因此适用于 AndroidX 项目。



使用方法

1.确保您的项目已经添加了 `ViewPager2` 的依赖。在您的模块的 `build.gradle` 文件中添加以下依赖：

```
gradleCopy code
implementation 'androidx.viewpager2:viewpager2:1.1.0'
```

**2. 创建布局文件**：

创建一个包含 `ViewPager2` 的布局文件，例如 `activity_main.xml`：

```
xmlCopy code<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <androidx.viewpager2.widget.ViewPager2
        android:id="@+id/viewPager"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</androidx.constraintlayout.widget.ConstraintLayout>
```

**3. 创建数据模型**：

创建一个数据模型类，表示每个页面的数据。例如，我们创建一个 `PageModel` 类：

```
javaCopy codepublic class PageModel {
    private String title;
    private int imageResId;

    public PageModel(String title, int imageResId) {
        this.title = title;
        this.imageResId = imageResId;
    }

    public String getTitle() {
        return title;
    }

    public int getImageResId() {
        return imageResId;
    }
}
```

**4. 创建适配器**：

创建一个自定义的 `RecyclerView.Adapter` 来填充 `ViewPager2` 的页面。在适配器中，您可以设置页面的布局和数据绑定。以下是一个示例适配器：

```
javaCopy codepublic class MyPagerAdapter extends RecyclerView.Adapter<MyPagerAdapter.MyViewHolder> {
    private List<PageModel> pages;

    public MyPagerAdapter(List<PageModel> pages) {
        this.pages = pages;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.page_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PageModel page = pages.get(position);
        holder.titleTextView.setText(page.getTitle());
        holder.imageView.setImageResource(page.getImageResId());
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
```

**5. 创建页面布局**：

为每个页面创建一个布局文件，例如 `page_item.xml`，并在其中放置页面的 UI 元素，如标题和图像：

```
xmlCopy code<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <ImageView
        android:id="@+id/imageView"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:scaleType="centerCrop" />

    <TextView
        android:id="@+id/titleTextView"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:textSize="18sp"
        android:textColor="@android:color/black"
        android:padding="16dp" />

</LinearLayout>
```

**6. 在活动中使用 `ViewPager2`**：

在活动中初始化 `ViewPager2`、创建页面数据列表、设置适配器、设置页面切换动画等。以下是示例代码：

```
javaCopy codeimport androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private List<PageModel> pages;
    private MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);

        // 创建页面数据列表
        pages = new ArrayList<>();
        pages.add(new PageModel("Page 1", R.drawable.image1));
        pages.add(new PageModel("Page 2", R.drawable.image2));
        pages.add(new PageModel("Page 3", R.drawable.image3));

        // 创建并设置适配器
        adapter = new MyPagerAdapter(pages);
        viewPager.setAdapter(adapter);

        // 设置页面切换动画（可选）
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
    }
}
```

**7. 自定义页面切换动画**（可选）：

如果需要自定义页面切换动画，您可以创建自己的页面切换器类。以下是一个示例页面切换器 `ZoomOutPageTransformer`：

```
javaCopy codeimport android.view.View;
import androidx.viewpager2.widget.ViewPager2;
import androidx.viewpager2.widget.ViewPager2.PageTransformer;

public class ZoomOutPageTransformer implements PageTransformer {
    private static final float MIN_SCALE = 0.85f;

    @Override
    public void transformPage(View page, float position) {
        int pageWidth = page.getWidth();
        int pageHeight = page.getHeight();

        if (position < -1) { // 页面位于左侧屏幕外
            page.setAlpha(0f);
        } else if (position <= 1) { // 页面位于屏幕内或部分可见
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
            float vertMargin = pageHeight * (1 - scaleFactor) / 2;
            float horzMargin = pageWidth * (1 - scaleFactor) / 2;

            if (position < 0) {
                page.setTranslationX(horzMargin - vertMargin / 2);
            } else {
                page.setTranslationX(-horzMargin + vertMargin / 2);
            }

            page.setScaleX(scaleFactor);
            page.setScaleY(scaleFactor);

```

