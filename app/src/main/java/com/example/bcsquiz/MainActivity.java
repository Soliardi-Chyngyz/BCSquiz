package com.example.bcsquiz;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bcsquiz.data.AnswerListAsyncResponse;
import com.example.bcsquiz.data.QuestionBank;
import com.example.bcsquiz.model.Question;
import com.example.demo.model.Score;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import util.Prefs;

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
        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("name", etName.getText().toString().trim());
        setResult(RESULT_OK, intent);
        startActivity(intent);
        finish();
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