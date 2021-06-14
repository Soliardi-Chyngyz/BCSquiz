package com.example.bcsquiz;

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

import com.example.bcsquiz.ThirdActivity.ThirdActivity;
import com.example.bcsquiz.data.AnswerListAsyncResponse;
import com.example.bcsquiz.data.QuestionBank;
import com.example.bcsquiz.model.Question;
import com.example.bcsquiz.model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTxt, questionCounterTxt, sumCurrent, topPoints, timer;
    private Button firstBtn, secBtn, thirdBtn, fourthBtn;

    //    private ImageView nextBtn, prevBtn;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    private final int scoreCount = 0;
    private Users users;

    private CountDownTimer countDownTimer;
    private long timeLeftInMilliSeconds = 20000;
    private ProgressBar progressBar;
    private Boolean isDownloaded = false;
    /*private int progressStatus = 0;
    private Handler handler = new Handler();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        init();

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                if (!questionArrayList.isEmpty()) {
                    isDownloaded = true;
                    questionTxt.setText(questionArrayList.get(currentQuestionIndex).getQuestion());
                    questionCounterTxt.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionArrayList.size()));
                    firstBtn.setText(questionArrayList.get(currentQuestionIndex).getV1());
                    secBtn.setText(questionArrayList.get(currentQuestionIndex).getV2());
                    thirdBtn.setText(questionArrayList.get(currentQuestionIndex).getV3());
                    fourthBtn.setText(questionArrayList.get(currentQuestionIndex).getV4());
                }
                checkDownloaded();
            }
        });


        // изменения цвета статус бара
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setStatusBarColor(R.color.transparent);
//        }
        // убрали статус бар
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        users = new Users();
//        prefs = new Prefs(GameActivity.this);
        initMessage();

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

        startTime();
    }

    private void checkDownloaded() {
        if (isDownloaded) {
            progressBar.setVisibility(View.GONE);
            btnDisEnabled(true);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            btnDisEnabled(false);
        }
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
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                        } else {
                            String token = task.getResult();
                            Log.w("TOKEN", token);
                        }
                    }
                });
    }

    private void init() {
        progressBar = findViewById(R.id.progressBar);
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

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (questionList != null) {
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
    }

    private void checkAnswer(String userChooseCorrect) {
        if (userChooseCorrect != null) {
            String answerIsTrue = questionList.get(currentQuestionIndex).getAnswerTrue();
//            int toastMessageId = 0;
            if (userChooseCorrect.equals(answerIsTrue)) {
                addPoints();
                fadeView();
//            toastMessageId = R.string.correct_answer;
                btnDisEnabled(false);
            } else {
                btnDisEnabled(false);
                shakeAnim();
//            toastMessageId = R.string.wrong_answer;
            }
//        Toast.makeText(GameActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
        }
    }


    private void btnDisEnabled(boolean value) {
        firstBtn.setEnabled(value);
        secBtn.setEnabled(value);
        thirdBtn.setEnabled(value);
        fourthBtn.setEnabled(value);
    }

    private void addPoints() {
        int scoreCount = (users.getPoints() + (int) (timeLeftInMilliSeconds / 1000));
        users.setPoints(scoreCount);
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
                if (currentQuestionIndex == questionList.size() - 1) {
                    btnDisEnabled(false);
                    saveOnFS();
                } else {
                    goNext();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
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
                if (currentQuestionIndex == questionList.size() - 1) {
                    btnDisEnabled(false);
                    saveOnFS();
                } else {
                    goNext();
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void goNext() {
        if (currentQuestionIndex != questionList.size() - 1) {
            currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
            firstBtn.setEnabled(true);
            secBtn.setEnabled(true);
            thirdBtn.setEnabled(true);
            fourthBtn.setEnabled(true);
            updateData();
            startTime();
        }
    }

    private void saveOnFS() {
        String name = getIntent().getStringExtra("name");
        users.setUser(name);
        FirebaseFirestore.getInstance()
                .collection("Users")
                .add(users)
                .addOnCompleteListener(task ->
                        intent()).addOnFailureListener(e ->
                Toast.makeText(GameActivity.this, "Couldn't saved on FIRESTORE! ! !", Toast.LENGTH_SHORT).show());
    }

    private void intent() {
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
        questionCounterTxt.setText(currentQuestionIndex + " / " + (questionList.size() - 1)); // 0 or 234
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