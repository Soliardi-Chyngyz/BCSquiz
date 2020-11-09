package com.example.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bcsquiz.R;
import com.example.bcsquiz.ThirdActivity.ThirdActivity;
import com.example.bcsquiz.data.QuestionBank;
import com.example.bcsquiz.model.Question;
import com.example.bcsquiz.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.MessageFormat;
import java.util.List;

import util.Prefs;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTxt, questionCounterTxt, sumCurrent, topPoints, timer;
    private Button firstBtn, secBtn, thirdBtn, fourthBtn;

    //    private ImageView nextBtn, prevBtn;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    private int scoreCount = 0;
    private Users users;
    private Prefs prefs;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds = 20000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // изменения цвета статус бара
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(R.color.transparent);
//        }
        // убрали статус бар
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(R.layout.activity_game);
        getSupportActionBar().hide();

        ProgressBar progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(ProgressBar.VISIBLE);

        users = new Users();
//        prefs = new Prefs(GameActivity.this);
        init();
//        initMessage();

//        nextBtn.setOnClickListener(this);
//        prevBtn.setOnClickListener(this);
        firstBtn.setOnClickListener(this);
        secBtn.setOnClickListener(this);
        thirdBtn.setOnClickListener(this);
        fourthBtn.setOnClickListener(this);

//        sumCurrent.setText(MessageFormat.format("{0} current points", String.valueOf(score.getScore())));

        // Prefs
//        sumCurrent.setText(MessageFormat.format("{0} current points", String.valueOf(prefs.getCount())));
//        currentQuestionIndex = prefs.getState();
//        topPoints.setText(MessageFormat.format("Highest points: {0}", String.valueOf(prefs.getHighScore())));

        questionList = new QuestionBank().getQuestions(questionArrayList -> {
            questionTxt.setText(questionArrayList.get(currentQuestionIndex).getQuestion());
            questionCounterTxt.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionArrayList.size()));
            firstBtn.setText(questionArrayList.get(currentQuestionIndex).getV1());
            secBtn.setText(questionArrayList.get(currentQuestionIndex).getV2());
            thirdBtn.setText(questionArrayList.get(currentQuestionIndex).getV3());
            fourthBtn.setText(questionArrayList.get(currentQuestionIndex).getV4());
        });
        progressBar.setVisibility(View.INVISIBLE);
        startTime();
    }

    private void startTime() {
        timeLeftInMilliSeconds = 20000;
        countDownTimer = new CountDownTimer(timeLeftInMilliSeconds, 1000) {
            @Override
            public void onTick(long l) {
                timeLeftInMilliSeconds = l;
                updateTimer();
            }

            @Override
            public void onFinish() {
                shakeAnim();
            }
        }.start();
    }

    private void updateTimer() {
        int seconds = (int) (timeLeftInMilliSeconds % 20000 / 1000);
        timer.setText(String.valueOf(seconds));
    }

    private void initMessage() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("TAG", "Fetching FCM registration token failed", task.getException());

                    } else {
                        String token = task.getResult();
                        Log.w("TOKEN", token);
                    }
                });
    }

    private void init() {
//        nextBtn = findViewById(R.id.next_btn);
//        prevBtn = findViewById(R.id.prev_btn);
        firstBtn = findViewById(R.id.f_btn);
        secBtn = findViewById(R.id.s_btn);
        thirdBtn = findViewById(R.id.t_btn);
        fourthBtn = findViewById(R.id.fo_btn);
        questionCounterTxt = findViewById(R.id.counter);
        questionTxt = findViewById(R.id.question_txt);
        sumCurrent = findViewById(R.id.sum_counter);
        topPoints = findViewById(R.id.record);
        timer = findViewById(R.id.timer);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.f_btn:
                checkAnswer(questionList.get(currentQuestionIndex).getV1());
                updateData();
                break;
            case R.id.s_btn:
                checkAnswer(questionList.get(currentQuestionIndex).getV2());
                updateData();
                break;
            case R.id.t_btn:
                checkAnswer(questionList.get(currentQuestionIndex).getV3());
                updateData();
                break;
            case R.id.fo_btn:
                checkAnswer(questionList.get(currentQuestionIndex).getV4());
                updateData();
                break;
        }
    }

    private void checkAnswer(String userChooseCorrect) {
        String answerIsTrue = questionList.get(currentQuestionIndex).getAnswerTrue();
        int toastMessageId = 0;
        if (userChooseCorrect.equals(answerIsTrue)) {
            addPoints();
            fadeView();
            toastMessageId = R.string.correct_answer;
            firstBtn.setEnabled(false);
            secBtn.setEnabled(false);
            thirdBtn.setEnabled(false);
            fourthBtn.setEnabled(false);
        } else {
            shakeAnim();
            toastMessageId = R.string.wrong_answer;
        }
        Toast.makeText(GameActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void addPoints() {
        int scoreCount = (users.getPoints() + (int) (timeLeftInMilliSeconds / 1000));
        users.setPoints(scoreCount);
        users.getUser();
        sumCurrent.setText(MessageFormat.format("{0} points", String.valueOf(users.getPoints())));
    }

//    private void deductPoints() {
//        scoreCount -= 10;
//        if (scoreCount <= 0) {
//            scoreCount = 0;
//        }
//        score.setScore(scoreCount);
//        sumCurrent.setText(MessageFormat.format("{0} current points", String.valueOf(score.getScore())));
//    }


    private void fadeView() {
        CardView cardView = findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(350);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void shakeAnim() {
        CardView cardView = findViewById(R.id.cardView);
        Animation shake = AnimationUtils.loadAnimation(GameActivity.this,
                R.anim.shake_anim);
        cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
                goNext();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void goNext() {
        if (currentQuestionIndex != 23) {
            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
            firstBtn.setEnabled(true);
            secBtn.setEnabled(true);
            thirdBtn.setEnabled(true);
            fourthBtn.setEnabled(true);
            updateData();
            startTime();
        } else {
            saveOnFS();
        }
    }

    private void saveOnFS() {
        FirebaseFirestore.getInstance()
                .collection("Users")
                .add(users)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        transfer();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GameActivity.this, "Couldn't saved on FIRESTORE! ! !", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void transfer() {
        Intent intent = new Intent(this, ThirdActivity.class);
        startActivity(intent);
        finish();
    }

    @SuppressLint("SetTextI18n")
    private void updateData() {
        countDownTimer.cancel();
        String answer1 = questionList.get(currentQuestionIndex).getV1();
        String answer2 = questionList.get(currentQuestionIndex).getV2();
        String answer3 = questionList.get(currentQuestionIndex).getV3();
        String answer4 = questionList.get(currentQuestionIndex).getV4();
        String question = questionList.get(currentQuestionIndex).getQuestion();
        questionTxt.setText(question);
        questionCounterTxt.setText(currentQuestionIndex + " / " + questionList.size()); // 0 or 234
        firstBtn.setText(answer1);
        secBtn.setText(answer2);
        thirdBtn.setText(answer3);
        fourthBtn.setText(answer4);
    }

//    @Override
//    protected void onPause() {
//        prefs.saveHighScore(score.getScore());
//        prefs.setState(this.currentQuestionIndex);
//        prefs.saveCount(score.getScore());
//        super.onPause();
//    }
}