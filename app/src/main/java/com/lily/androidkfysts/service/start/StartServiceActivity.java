package com.lily.androidkfysts.service.start;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.lily.androidkfysts.R;
import com.lily.androidkfysts.ipc.Book;

public class StartServiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Intent intent = new Intent(StartServiceActivity.this, IntentServiceDemo.class);
        Intent intent = new Intent(StartServiceActivity.this, StartServiceDemo.class);

        intent.putExtra("book",new Book(1,"Android 开发艺术探索"));
        startService(intent);
        final Button btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn.setBackgroundColor(Color.GRAY);
            }
        });
    }
}
