package com.example.neurolog;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DbHelper dbHelper;
    private TextView reportTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        reportTextView = findViewById(R.id.reportTextView);
        Button startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                startActivity(intent);
            }
        });

        loadReport();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadReport();
    }

    private void loadReport() {
        Cursor cursor = dbHelper.getAllLogs();
        StringBuilder report = new StringBuilder();
        report.append("History Logs:\n\n");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TIMESTAMP));
                int reaction = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_REACTION_TIME));
                int mood = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MOOD));
                int energy = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ENERGY));

                report.append("Date: ").append(timestamp).append("\n")
                        .append("Reaction: ").append(reaction).append("ms | ")
                        .append("Mood: ").append(mood).append("/10 | ")
                        .append("Energy: ").append(energy).append("/10\n")
                        .append("---------------------------\n");
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            report.append("No logs found. Start a test!");
        }
        reportTextView.setText(report.toString());
    }
}
