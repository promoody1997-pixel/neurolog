package com.example.neurolog;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private TextView stroopTextView;
    private LinearLayout gameLayout, inputLayout;
    private SeekBar moodSeekBar, energySeekBar;
    private DbHelper dbHelper;

    private String[] colors = {"RED", "GREEN", "BLUE", "YELLOW", "BLACK"};
    private int[] colorValues = {Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.BLACK};
    
    private int targetColorIndex;
    private long startTime;
    private int reactionTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dbHelper = new DbHelper(this);
        stroopTextView = findViewById(R.id.stroopTextView);
        gameLayout = findViewById(R.id.gameLayout);
        inputLayout = findViewById(R.id.inputLayout);
        moodSeekBar = findViewById(R.id.moodSeekBar);
        energySeekBar = findViewById(R.id.energySeekBar);
        Button saveButton = findViewById(R.id.saveButton);

        setupGameButtons();
        startStroopTest();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int mood = moodSeekBar.getProgress();
                int energy = energySeekBar.getProgress();
                dbHelper.insertLog(reactionTime, mood, energy);
                Toast.makeText(GameActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private void setupGameButtons() {
        LinearLayout buttonContainer = findViewById(R.id.buttonContainer);
        for (int i = 0; i < colors.length; i++) {
            Button btn = new Button(this);
            btn.setText(colors[i]);
            final int index = i;
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(index);
                }
            });
            buttonContainer.addView(btn);
        }
    }

    private void startStroopTest() {
        Random random = new Random();
        int textIndex = random.nextInt(colors.length);
        targetColorIndex = random.nextInt(colors.length);

        stroopTextView.setText(colors[textIndex]);
        stroopTextView.setTextColor(colorValues[targetColorIndex]);
        
        startTime = System.currentTimeMillis();
    }

    private void checkAnswer(int selectedIndex) {
        if (selectedIndex == targetColorIndex) {
            reactionTime = (int) (System.currentTimeMillis() - startTime);
            showInputLayout();
        } else {
            Toast.makeText(this, "Wrong! Try again.", Toast.LENGTH_SHORT).show();
            startStroopTest();
        }
    }

    private void showInputLayout() {
        gameLayout.setVisibility(View.GONE);
        inputLayout.setVisibility(View.VISIBLE);
        TextView resultText = findViewById(R.id.resultText);
        resultText.setText("Reaction Time: " + reactionTime + "ms");
    }
}
