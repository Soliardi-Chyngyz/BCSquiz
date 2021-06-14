package com.example.demo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bcsquiz.GameActivity;
import com.example.bcsquiz.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private EditText etName;

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // изменения цвета статус бара
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(R.color.transparent);
        }
        // убрали статус бар
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();

        init();
    }

    private void init() {
        etName = findViewById(R.id.ed_name);
        Button btnName = findViewById(R.id.btn_name);
        btnName.setOnClickListener(view -> {
            transfer();
        });
    }

    public static void start(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }
    private void transfer() {
        String name = etName.getText().toString();
        if (!TextUtils.isEmpty(name)) {
            Intent intent = new Intent(this, GameActivity.class);
            intent.putExtra("name", name);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "PUT YOUR NAME ! ! !", Toast.LENGTH_SHORT).show();
        }
    }
}