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

    private TextView colorText, moodLabel, energyLabel;
    private Button btnOption1, btnOption2, btnSave;
    private LinearLayout gameLayout, inputLayout;
    private SeekBar moodSeekBar, energySeekBar;
    
    private long startTime;
    private int reactionTimeResult;
    private int correctColor;
    
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        dbHelper = new DbHelper(this);

        colorText = findViewById(R.id.colorText);
        btnOption1 = findViewById(R.id.btnOption1);
        btnOption2 = findViewById(R.id.btnOption2);
        gameLayout = findViewById(R.id.gameLayout);
        inputLayout = findViewById(R.id.inputLayout);
        
        moodSeekBar = findViewById(R.id.moodSeekBar);
        energySeekBar = findViewById(R.id.energySeekBar);
        moodLabel = findViewById(R.id.moodLabel);
        energyLabel = findViewById(R.id.energyLabel);
        btnSave = findViewById(R.id.btnSave);

        moodSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                moodLabel.setText("المزاج: " + progress + "/10");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        energySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                energyLabel.setText("الطاقة: " + progress + "/10");
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnSave.setOnClickListener(v -> saveResults());

        startGame();
    }

    private void startGame() {
        gameLayout.setVisibility(View.VISIBLE);
        inputLayout.setVisibility(View.GONE);

        Random random = new Random();
        int colorIndex = random.nextInt(2); 
        
        if (colorIndex == 0) {
            colorText.setText("أزرق"); 
            colorText.setTextColor(Color.RED); 
            correctColor = Color.RED;
        } else {
            colorText.setText("أحمر");
            colorText.setTextColor(Color.BLUE);
            correctColor = Color.BLUE;
        }

        btnOption1.setBackgroundColor(Color.RED);
        btnOption1.setText("أحمر");
        btnOption1.setOnClickListener(v -> checkAnswer(Color.RED));

        btnOption2.setBackgroundColor(Color.BLUE);
        btnOption2.setText("أزرق");
        btnOption2.setOnClickListener(v -> checkAnswer(Color.BLUE));

        startTime = System.currentTimeMillis();
    }

    private void checkAnswer(int selectedColor) {
        if (selectedColor == correctColor) {
            reactionTimeResult = (int) (System.currentTimeMillis() - startTime);
            Toast.makeText(this, "رائع! " + reactionTimeResult + "ms", Toast.LENGTH_SHORT).show();
            
            gameLayout.setVisibility(View.GONE);
            inputLayout.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "خطأ! حاول مرة أخرى", Toast.LENGTH_SHORT).show();
            startGame(); 
        }
    }

    private void saveResults() {
        int mood = moodSeekBar.getProgress();
        int energy = energySeekBar.getProgress();
        
        dbHelper.insertLog(reactionTimeResult, mood, energy);
        
        Toast.makeText(this, "تم الحفظ!", Toast.LENGTH_SHORT).show();
        finish(); 
    }
}
