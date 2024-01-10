package com.nolan.utills.windowmanagerutils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.nolan.utills.nolanwindowmanagerlib.WindowManagerUtils;

public class MainActivity extends AppCompatActivity {
    public View root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        /*
         * 在x=100 y =100的位置为起点添加一布局个窗口
         * */
        root = WindowManagerUtils.setContentViewWithWindowManagerWithLocation(this, R.layout.dialog_main, root, 2, 100, 100);
        root.findViewById(R.id.hello_android).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "hello android", Toast.LENGTH_SHORT).show();
            }
        });
    }
}