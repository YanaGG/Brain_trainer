package yana.geo.braintrainer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import java.util.function.Predicate;

public class ScoreActivity extends AppCompatActivity {

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Intent intent = getIntent();
        if (intent!=null && intent.hasExtra("result")){
            int result = intent.getIntExtra("result", 0);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            int max = preferences.getInt("max", 0);
            String score = String.format("Your result is: %s\n Max result is: %s", result, max);
            textViewResult = findViewById(R.id.textViewResult);
            textViewResult.setText(score);
        }


    }

    public void startNewGame(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}