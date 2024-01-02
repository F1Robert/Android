package com.nolan.utills.nldatastore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.nolan.utills.nldata.NLData;

public class MainActivity extends AppCompatActivity {
    private TextView textView;
    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.hello);
        editText = findViewById(R.id.edit_hello);
        NLData.putString("hello", "你好");

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                NLData.putString("hello",charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                refresh(textView);
            }
        });
        textView.setText(NLData.getString("hello"));
    }

    public void refresh(TextView textView){
        textView.setText(NLData.getString("hello"));
    }
}