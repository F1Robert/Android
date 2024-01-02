# Android导航栏和view pager的组合使用

## 1.配置导航栏

![image-20240102145822220](https://s2.loli.net/2024/01/02/eCEnW7h9PoXQNYb.png)

```
<com.google.android.material.bottomnavigation.BottomNavigationView
    android:id="@+id/bottomNavigationView"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:background="@color/gray14"
    app:itemTextColor="@color/bottom_nav_color"
    app:labelVisibilityMode="labeled"
    app:menu="@menu/main_bottom_navigation" />
```

app:menu="@menu/main_bottom_navigation" 这个属性中会对导航栏的元素进行定义，包括以下属性

id

icon

titile

![image-20240102145857155](https://s2.loli.net/2024/01/02/dvXDUQHwPMjTLY2.png)



## 2.结合view pager使用导航栏

```
//初始化view pager
private List<Fragment> mFragmentList = new ArrayList<>(4);
mFragmentList.add(conversationListFragment);
        mFragmentList.add(contactListFragment);
        boolean showWorkSpace = !TextUtils.isEmpty(Config.WORKSPACE_URL);
        if (showWorkSpace) {
            mFragmentList.add(WebViewFragment.loadUrl(Config.WORKSPACE_URL));
        }
        mFragmentList.add(discoveryFragment);
        mFragmentList.add(meFragment);
contentViewPager.setAdapter(new HomeFragmentPagerAdapter(getSupportFragmentManager(), mFragmentList));
```

```
//在点击导航栏时使用view pager切换页面
bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
    switch (item.getItemId()) {
        case R.id.conversation_list:
            contentViewPager.setCurrentItem(0, false);
            setTitle("野火");
            if (!isDarkTheme()) {
                setTitleBackgroundResource(R.color.gray5, false);
            }
            break;
        case R.id.contact:
            contentViewPager.setCurrentItem(1, false);
            setTitle("通讯录");
            if (!isDarkTheme()) {
                setTitleBackgroundResource(R.color.gray5, false);
            }
            break;
        case R.id.workspace:
            contentViewPager.setCurrentItem(2, false);
            setTitle("工作台");
            if (!isDarkTheme()) {
                setTitleBackgroundResource(R.color.gray5, false);
            }
            break;
        case R.id.discovery:
            contentViewPager.setCurrentItem(showWorkSpace ? 3 : 2, false);
            setTitle("发现");
            if (!isDarkTheme()) {
                setTitleBackgroundResource(R.color.gray5, false);
            }
            break;
        case R.id.me:
            contentViewPager.setCurrentItem(showWorkSpace ? 4 : 3, false);
            setTitle("我的");
            if (!isDarkTheme()) {
                setTitleBackgroundResource(R.color.white, false);
            }
            break;
        default:
            break;
    }
    return true;
});
```