# Android中启用ViewBinding

## 概念:ViewBinding是Android中对findviewbyid(0)操作的简化功能，在Androidstudio3.6之后启用，在视图组件很多的情况下能大幅简化开发，加快速度

![image-20240102143607044](https://s2.loli.net/2024/01/02/RPNTFtGQoM8YvyB.png)

1.在build.gradle中配置 android.viewBinding { enabled = true}

2.syc

3.使用

```
public class MainActivity extends AppCompatActivity {
	//绑定类名称ActivityMain对应activity_main
    private ActivityMainBinding binding;
    private TextView textView;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定方法
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //替代方法
        //setContentView(R.layout.activity_main);
        //textView = findViewById(R.id.hello);
        //editText = findViewById(R.id.edit_hello);
        setContentView(binding.getRoot());
        textView = binding.hello;
        editText = binding.editHello;
    }
}
```

# 总结：Android启用视图组件的步骤包括

# 1.配置build.gradle文件，启用android{viewBindle {enabled = true}}

# 2.进行使用声明和使用