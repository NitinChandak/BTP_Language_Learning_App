package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class PronunciationTestPage extends AppCompatActivity {

    Button test1,test2,test3;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pronunciation_test_page);

        test1 = findViewById(R.id.button32);
        test2 = findViewById(R.id.button33);
        test3 = findViewById(R.id.button10);

        test1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PronunciationTest1.class));
            }
        });

        test2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PronunciationTest2.class));
            }
        });

        test3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), PronunciationTest3.class));
            }
        });


    }

}
