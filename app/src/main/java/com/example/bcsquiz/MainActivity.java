package com.example.bcsquiz;


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

import com.example.bcsquiz.model.Users;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

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
}