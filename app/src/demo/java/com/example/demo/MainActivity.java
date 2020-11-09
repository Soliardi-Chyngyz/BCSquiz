package com.example.demo;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bcsquiz.GameActivity;
import com.example.bcsquiz.R;
import com.example.bcsquiz.model.Users;

public class MainActivity extends AppCompatActivity {
    private EditText etName;
    private Button btnName;

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
        getSupportActionBar().hide();

        init();
    }

    private void init() {
        etName = findViewById(R.id.ed_name);
        btnName = findViewById(R.id.btn_name);
        btnName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                transfer();
            }
        });
    }

    private void transfer() {
        Users users = new Users();
        users.setUser(etName.getText().toString());
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);
        finish();
    }
}