package com.example.neurolog;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DbHelper dbHelper;
    private TextView logsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // تهيئة قاعدة البيانات
        dbHelper = new DbHelper(this);
        
        logsTextView = findViewById(R.id.logsTextView);
        Button startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, GameActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadLogs();
    }

    private void loadLogs() {
        // استخدام الدالة الجديدة من DbHelper
        Cursor cursor = dbHelper.getAllLogs();
        StringBuilder builder = new StringBuilder();
        
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // قراءة البيانات باستخدام الأعمدة الصحيحة
                String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_TIMESTAMP));
                int reactionTime = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_REACTION_TIME));
                int mood = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_MOOD));
                int energy = cursor.getInt(cursor.getColumnIndexOrThrow(DbHelper.COLUMN_ENERGY));

                builder.append("📅 ").append(timestamp).append("\n");
                builder.append("⚡ السرعة: ").append(reactionTime).append(" ms\n");
                builder.append("😊 المزاج: ").append(mood).append("/10  |  🔋 الطاقة: ").append(energy).append("/10\n");
                builder.append("────────────────────\n");

            } while (cursor.moveToNext());
            cursor.close();
        } else {
            builder.append("لا توجد سجلات. ابدأ اختبارك الأول الآن!");
        }
        
        logsTextView.setText(builder.toString());
    }
}
