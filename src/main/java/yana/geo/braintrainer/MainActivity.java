package yana.geo.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private TextView textViewScore;
    private TextView textViewQuestion;
    private TextView textViewVar1;
    private TextView textViewVar2;
    private TextView textViewVar3;
    private TextView textViewVar4;
    private String question;
    private int rightAnswer;
    private int rightAnswerPosition;
    private boolean isPositive;
    private int min = 5;
    private int max = 30;
    private ArrayList <TextView> options = new ArrayList<>();
    private int QuestionCount = 0;
    private int RightAnswerCount =0;
    private boolean gameOver = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewTimer = findViewById(R.id.textViewTimer);
        textViewScore = findViewById(R.id.textViewScore);
        textViewQuestion = findViewById(R.id.textViewQuestion);
        textViewVar1 = findViewById(R.id.textView1);
        textViewVar2 = findViewById(R.id.textView2);
        textViewVar3 = findViewById(R.id.textView3);
        textViewVar4 = findViewById(R.id.textView4);
        options.add(textViewVar1);
        options.add(textViewVar2);
        options.add(textViewVar3);
        options.add(textViewVar4);
        playNext();
        CountDownTimer timer = new CountDownTimer(20000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(getTime(millisUntilFinished));
                if (millisUntilFinished < 10000){
                    textViewTimer.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                }

            }

            @Override
            public void onFinish() {
            gameOver=true;
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            int max = preferences.getInt("max", 0);
            if (RightAnswerCount >=max){
                preferences.edit().putInt("max", RightAnswerCount).apply();
            }
            Intent intent = new Intent(MainActivity.this, ScoreActivity.class);
            intent.putExtra("result", RightAnswerCount);
            startActivity(intent);
            }
        };
        timer.start();



    }
    private void playNext(){
        generateQuestion();
        for (int i =0; i < options.size(); i++)
        {
            if (i == rightAnswerPosition)
            {
                options.get(i).setText(Integer.toString(rightAnswer));
            }
            else {
                options.get(i).setText(Integer.toString(generateWrongAnswer()));
            }
        }
        String score = String.format("%s/%s", RightAnswerCount, QuestionCount );
        textViewScore.setText(score);
    }

    private void generateQuestion (){
        int a = (int)(Math.random() * (max-min+1)+min);
        int b = (int)(Math.random() * (max-min+1)+min);
        int sign = (int)(Math.random() * 2);
        isPositive = sign == 1;
        if (isPositive){
            rightAnswer = a +b;
            question = String.format("%s + %s", a, b);
        }
        else {
            rightAnswer = a - b;
            question = String.format("%s - %s", a, b);
        }
        textViewQuestion.setText(question);
        rightAnswerPosition = (int)(Math.random() *4);
    }

    private int generateWrongAnswer(){
        int result;
        do {
           result = (int)(Math.random() * max * 2 + 1) - (max-min);
        }
        while (result == rightAnswer);
        return result;
    }

    private String getTime (long millis){
        int seconds =(int)(millis/1000);
        int minutes = seconds/60;
        seconds = seconds % 60;
        return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);


    }

    public void answerOnClick(View view) {
        if (!gameOver) {
            TextView textView = (TextView) view;
            String answer = textView.getText().toString();
            int chosenAnswer = Integer.parseInt(answer);
            if (chosenAnswer == rightAnswer) {
                RightAnswerCount++;
                Toast.makeText(this, "Right answer!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Wrong answer!", Toast.LENGTH_SHORT).show();
            }
            QuestionCount++;

            playNext();
        }
    }
}