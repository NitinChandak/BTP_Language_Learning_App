package com.example.myapplication;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class PronunciationTest3 extends AppCompatActivity {

    private ImageButton mic;
    private TextView word, comments, score;
    private Button nextButton;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pronunciation_test_3);
    }

}
