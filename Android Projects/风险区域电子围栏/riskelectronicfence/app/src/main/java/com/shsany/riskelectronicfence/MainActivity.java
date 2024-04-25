package com.shsany.riskelectronicfence;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.util.UnstableApi;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.rtsp.RtspMediaSource;
import androidx.media3.exoplayer.source.MediaSource;
import androidx.media3.ui.PlayerView;

import com.bumptech.glide.Glide;

@UnstableApi
public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    ImageView st;

    // 设置RTSP流地址
    String rtspUrl = "rtsp://admin:abc12345@192.168.1.99:554/Streaming/Channels/201";
    String rtspUrl2 = "rtsp://admin:abc12345@192.168.1.252/cam/realmonitor?channel=7&subtype=0";

    PlayerView playerView;
    PlayerView playerView2;

    /*
     * 摄像头相关
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
         * 加载动画gif
         * */
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.gifImageView);
        st = findViewById(R.id.st);
        Glide.with(this).asGif().load(R.raw.anime).into(imageView);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /*
         * 播放rtsp流
         * */
        playerView = findViewById(R.id.player1);
        playerView2 = findViewById(R.id.player2);

        MediaSource mediaSource =
                new RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(rtspUrl));
        ExoPlayer player = new ExoPlayer.Builder(this).build();
        player.setMediaSource(mediaSource);
        playerView.setPlayer(player);
        player.prepare();

        MediaSource mediaSource2 =
                new RtspMediaSource.Factory().createMediaSource(MediaItem.fromUri(rtspUrl));
        ExoPlayer player2 = new ExoPlayer.Builder(this).build();
        player2.setMediaSource(mediaSource2);

        playerView2.setPlayer(player2);
        player2.prepare();

        st.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSettingsDialog();
            }
        });
    }

    private void showOptionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("请选择");
        final String[] options = {"读卡器设置", "工作人员设置", "数据管理", "网络参数设置", "防护范围设置", "检修人员设置", "测距信息设置"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_item, options) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view.findViewById(android.R.id.text1);

                // 设置图标
                Drawable icon = getIconForOption(position); // 自定义方法，用于获取图标
                if (icon != null) {
                    int iconSize = (int) textView.getTextSize();
                    icon.setBounds(0, 0, iconSize, iconSize);
                    textView.setCompoundDrawables(icon, null, null, null);
                    textView.setCompoundDrawablePadding(iconSize / 2);
                }

                return view;
            }
        };
        builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 处理按钮点击事件
                String selectedOption = options[i];
                if (selectedOption.equals("网络参数设置")) {
                    // 关闭当前对话框
                    dialogInterface.dismiss();
                    // 显示新的对话框
                    showNetworkSettingsDialog();
                } else if (selectedOption.equals("工作人员设置")) {
                    showStaffSettingsDialog();
                } else {
                    handleOptionClick(selectedOption);
                }

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showNetworkSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("网络参数设置");

        // 构建4行2列的数据布局
        GridLayout layout = new GridLayout(this);
        layout.setRowCount(4);
        layout.setColumnCount(1);
        layout.setPadding(50, 0, 50, 0); // 设置布局的左右内边距

        // 添加AI摄像头RTSP流地址标签和输入框
        TextView aiRtspLabel = createLabel("AI摄像头RTSP流地址");
        EditText aiRtspEditText = createEditText("rtsp://admin:abc12345@192.168.1.99:554/Streaming/Channels/201");
        layout.addView(aiRtspLabel);
        layout.addView(aiRtspEditText);

        // 添加热成像摄像头RTSP流地址标签和输入框
        TextView thermalRtspLabel = createLabel("热成像摄像头RTSP流地址");
        EditText thermalRtspEditText = createEditText("rtsp://admin:abc12345@192.168.1.99:554/Streaming/Channels/201");
        layout.addView(thermalRtspLabel);
        layout.addView(thermalRtspEditText);

        // 设置空标签的布局参数，使其占据空间，间接设置间距
        TextView placeholderLabel1 = createLabel("");
        TextView placeholderLabel2 = createLabel("");
        layout.addView(placeholderLabel1, new GridLayout.LayoutParams(GridLayout.spec(2, 1), GridLayout.spec(0, 1)));
        layout.addView(placeholderLabel2, new GridLayout.LayoutParams(GridLayout.spec(3, 1), GridLayout.spec(0, 1)));

        builder.setView(layout);

        // 添加保存设置按钮
        builder.setPositiveButton("保存设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 处理保存设置按钮点击事件
                // 在这里处理保存设置的逻辑
            }
        });

        // 显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showStaffSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("工作人员设置");

        // 构建4行1列的数据布局
        GridLayout layout = new GridLayout(this);
        layout.setRowCount(4);
        layout.setColumnCount(1);
        layout.setPadding(50, 0, 50, 0); // 设置布局的左右内边距

        // 添加工作人员姓名标签和编辑框
        TextView staffNameLabel = createLabel("工作人员姓名");
        EditText staffNameEditText = createEditText("张士成");
        layout.addView(staffNameLabel);
        layout.addView(staffNameEditText);
        //staffNameEditText.setBackgroundColor(Color.TRANSPARENT);
        //staffNameEditText.setWidth(200);
        // 添加工作人员编号标签和编辑框
        TextView staffIdLabel = createLabel("工作人员编号");
        EditText staffIdEditText = createEditText("SY00608");
        layout.addView(staffIdLabel);
        layout.addView(staffIdEditText);
        //staffIdEditText.setBackgroundColor(Color.TRANSPARENT);
        //staffIdEditText.setWidth(200);
        // 设置空标签的布局参数，使其占据空间，间接设置间距
        TextView placeholderLabel1 = createLabel("");
        TextView placeholderLabel2 = createLabel("");
        layout.addView(placeholderLabel1, new GridLayout.LayoutParams(GridLayout.spec(2, 1), GridLayout.spec(0, 1)));
        layout.addView(placeholderLabel2, new GridLayout.LayoutParams(GridLayout.spec(3, 1), GridLayout.spec(0, 1)));

        builder.setView(layout);

        // 添加保存设置按钮
        builder.setPositiveButton("保存设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 处理保存设置按钮点击事件
                // 在这里处理保存设置的逻辑
            }
        });

        // 显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // 自定义方法，根据位置获取图标
    private Drawable getIconForOption(int position) {
        switch (position) {
            case 0: // 读卡器设置
                return getResources().getDrawable(R.drawable.dkq); // 替换成你的图标资源
            case 1: // 工作人员设置
                return getResources().getDrawable(R.drawable.gzry); // 替换成你的图标资源
            case 2: // 数据管理
                return getResources().getDrawable(R.drawable.sj); // 替换成你的图标资源
            case 3: // 网络参数设置
                return getResources().getDrawable(R.drawable.wl); // 替换成你的图标资源
            case 4: // 防护范围设置
                return getResources().getDrawable(R.drawable.fhfw); // 替换成你的图标资源
            case 5: // 检修人员设置
                return getResources().getDrawable(R.drawable.jx); // 替换成你的图标资源
            case 6: // 测距信息设置
                return getResources().getDrawable(R.drawable.cj); // 替换成你的图标资源
            default:
                return null;
        }
    }


    private void handleOptionClick(String option) {
        // 根据选项处理点击事件，这里只是简单的示例，你可以根据需要跳转到不同的界面
        //Intent intent = new Intent(this, NextActivity.class);
        //intent.putExtra("option", option);
        //startActivity(intent);
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomDialogTheme);
        builder.setTitle("设置");

        // 创建一个滚动布局，用于放置网络参数设置和工作人员设置的选项
        ScrollView scrollView = new ScrollView(this);
        int paddingInDp = 24; // 设置padding值，单位是dp
        float scale = getResources().getDisplayMetrics().density;
        int paddingInPx = (int) (paddingInDp * scale + 0.5f); // 将dp转换为像素
        scrollView.setPadding(paddingInPx, 0, paddingInPx, paddingInPx);
        // 创建一个线性布局，用于放置网络参数设置和工作人员设置的选项
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        // 添加网络参数设置选项
        TextView networkSettingsLabel = createLabel("网络参数设置");
        layout.addView(networkSettingsLabel);

        // 构建4行2列的数据布局，用于网络参数设置
        GridLayout networkLayout = new GridLayout(this);
        networkLayout.setRowCount(4);
        networkLayout.setColumnCount(1);
        networkLayout.setPadding(50, 0, 50, 0); // 设置布局的左右内边距

        // 添加AI摄像头RTSP流地址标签和输入框
        TextView aiRtspLabel = createLabel("AI摄像头RTSP流地址");
        EditText aiRtspEditText = createEditText("rtsp://admin:abc12345@192.168.1.99:554/Streaming/Channels/201");
        networkLayout.addView(aiRtspLabel);
        networkLayout.addView(aiRtspEditText);

        // 添加热成像摄像头RTSP流地址标签和输入框
        TextView thermalRtspLabel = createLabel("热成像摄像头RTSP流地址");
        EditText thermalRtspEditText = createEditText("rtsp://admin:abc12345@192.168.1.99:554/Streaming/Channels/201");
        networkLayout.addView(thermalRtspLabel);
        networkLayout.addView(thermalRtspEditText);

        // 设置空标签的布局参数，使其占据空间，间接设置间距
        TextView placeholderLabel1 = createLabel("");
        TextView placeholderLabel2 = createLabel("");
        networkLayout.addView(placeholderLabel1, new GridLayout.LayoutParams(GridLayout.spec(2, 1), GridLayout.spec(0, 1)));
        networkLayout.addView(placeholderLabel2, new GridLayout.LayoutParams(GridLayout.spec(3, 1), GridLayout.spec(0, 1)));

        // 添加网络参数设置布局到整体布局中
        layout.addView(networkLayout);

        // 添加工作人员设置选项
        TextView staffSettingsLabel = createLabel("工作人员设置");
        layout.addView(staffSettingsLabel);

        // 构建4行1列的数据布局，用于工作人员设置
        GridLayout staffLayout = new GridLayout(this);
        staffLayout.setRowCount(4);
        staffLayout.setColumnCount(1);
        staffLayout.setPadding(50, 0, 50, 0); // 设置布局的左右内边距

        // 添加工作人员姓名标签和编辑框
        TextView staffNameLabel = createLabel("工作人员姓名");
        EditText staffNameEditText = createEditText("张士成");
        staffLayout.addView(staffNameLabel);
        staffLayout.addView(staffNameEditText);

        // 添加工作人员编号标签和编辑框
        TextView staffIdLabel = createLabel("工作人员编号");
        EditText staffIdEditText = createEditText("SY00608");
        staffLayout.addView(staffIdLabel);
        staffLayout.addView(staffIdEditText);

        // 设置空标签的布局参数，使其占据空间，间接设置间距
        TextView staffPlaceholderLabel1 = createLabel("");
        TextView staffPlaceholderLabel2 = createLabel("");
        staffLayout.addView(staffPlaceholderLabel1, new GridLayout.LayoutParams(GridLayout.spec(2, 1), GridLayout.spec(0, 1)));
        staffLayout.addView(staffPlaceholderLabel2, new GridLayout.LayoutParams(GridLayout.spec(3, 1), GridLayout.spec(0, 1)));

        // 添加工作人员设置布局到整体布局中
        layout.addView(staffLayout);

        // 添加布局到滚动布局中
        scrollView.addView(layout);

        // 设置布局到对话框
        builder.setView(scrollView);

        // 创建对话框并显示
        AlertDialog dialog = builder.create();
        dialog.show();

        // 设置对话框的大小为 match_parent
        Window window = dialog.getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }

        // 将按钮布局添加到对话框的布局中
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        buttonLayoutParams.gravity = Gravity.END | Gravity.BOTTOM; // 将按钮布局放置在右下角
        buttonLayout.setLayoutParams(buttonLayoutParams);

        // 添加保存设置标签
        TextView saveButton = new TextView(this);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // 隐藏对话框
            }
        });
        saveButton.setText("保存设置");
        saveButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16); // 设置字体大小
        saveButton.setTextColor(Color.WHITE); // 设置字体颜色
        saveButton.setTypeface(null, Typeface.ITALIC); // 设置斜体
        saveButton.setPadding(0, 0, 24, 16); // 设置右下内边距
        LinearLayout.LayoutParams saveButtonParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        saveButtonParams.rightMargin = 12;
        saveButtonParams.gravity = Gravity.END; // 将按钮放置在右侧
        saveButton.setLayoutParams(saveButtonParams);
        buttonLayout.addView(saveButton);

// 将按钮布局添加到对话框的布局中
        layout.addView(buttonLayout);
    }


    // 创建标签
    private TextView createLabel(String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        textView.setTextColor(Color.WHITE); // 设置文字颜色为白色
        textView.setTextSize(16); // 设置文字大小为16sp
        textView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC)); // 设置文字为斜体
        textView.setPadding(50, 20, 50, 20); // 设置标签的内边距
        return textView;
    }

    // 创建编辑框
    private EditText createEditText(String text) {
        EditText editText = new EditText(this);
        editText.setText(text);
        editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        editText.setTextColor(Color.WHITE); // 设置文字颜色为白色
        editText.setTextSize(16); // 设置文字大小为16sp
        editText.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC)); // 设置文字为斜体
        editText.setPadding(50, 20, 50, 20); // 设置编辑框的内边距
        editText.setBackground(null); // 移除默认背景
        return editText;
    }
}