package com.example.demo;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.bcsquiz.R;
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

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTxt, questionCounterTxt, sumCurrent, topPoints;
    private Button firstBtn, secBtn, thirdBtn, fourthBtn;
    //    private ImageView nextBtn, prevBtn;
    private int currentQuestionIndex = 0;
    private List<Question> questionList;

    private int scoreCount = 0;
    private Score score;
    private Prefs prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = new Score();
        prefs = new Prefs(MainActivity.this);
        getSupportActionBar().hide();
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
        sumCurrent.setText(MessageFormat.format("{0} current points", String.valueOf(prefs.getCount())));
        currentQuestionIndex = prefs.getState();
        topPoints.setText(MessageFormat.format("Highest points: {0}", String.valueOf(prefs.getHighScore())));

        questionList = new QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTxt.setText(questionArrayList.get(currentQuestionIndex).getQuestion());
                questionCounterTxt.setText(MessageFormat.format("{0} / {1}", currentQuestionIndex, questionArrayList.size()));
            }
        });

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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.f_btn:


//            case R.id.true_btn:
//                checkAnswer(true);
//                updateQuestion();
//                break;
//            case R.id.false_btn:
//                checkAnswer(false);
//                updateQuestion();
//                break;
        }
    }

    private void checkAnswer(String userChooseCorrect) {
        String answerIsTrue = questionList.get(currentQuestionIndex).getAnswerTrue();
        int toastMessageId = 0;
        if (userChooseCorrect == answerIsTrue) {
            addPoints();
            fadeView();
            toastMessageId = R.string.correct_answer;
        } else {
            shakeAnim();
            toastMessageId = R.string.wrong_answer;
        }
        Toast.makeText(this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

    private void addPoints() {
        scoreCount += 10;
        score.setScore(scoreCount);
        sumCurrent.setText(MessageFormat.format("{0} points", String.valueOf(score.getScore())));
    }

//    private void deductPoints() {
//        scoreCount -= 10;
//        if (scoreCount <= 0) {
//            scoreCount = 0;
//        }
//        score.setScore(scoreCount);
//        sumCurrent.setText(MessageFormat.format("{0} current points", String.valueOf(score.getScore())));
//    }

    @SuppressLint("SetTextI18n")
    private void updateQuestion() {
        String question = questionList.get(currentQuestionIndex).getQuestion();
        questionTxt.setText(question);
        questionCounterTxt.setText(currentQuestionIndex + " / " + questionList.size()); // 0 or 234
    }

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
        Animation shake = AnimationUtils.loadAnimation(this,
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

    private void goNext(){
        currentQuestionIndex = (currentQuestionIndex + 1) % questionList.size();
        updateQuestion();
    }

    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        prefs.setState(this.currentQuestionIndex);
        prefs.saveCount(score.getScore());
        super.onPause();
    }
}