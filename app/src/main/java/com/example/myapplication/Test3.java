package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Test3 extends AppCompatActivity {

    Button button5x4;
    Button button6x5;
    Button buttonMatch3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        button5x4 = findViewById(R.id.button1);
        button5x4.setOnClickListener(v -> openGame5x4Activity());

        button6x5 = findViewById(R.id.button2);
        button6x5.setOnClickListener(v -> openGame6x5Activity());

        buttonMatch3 = findViewById(R.id.button3);
        buttonMatch3.setOnClickListener(v -> openGameMatch3Activity());
    }

    public void openGame5x4Activity(){
        Intent intent5x4 = new Intent(this, Game5x4Activity.class);
        startActivity(intent5x4);
    }

    public void openGame6x5Activity(){
        Intent intent4x4 = new Intent(this, Game4x4Activity.class);
        startActivity(intent4x4);
    }

    public void openGameMatch3Activity(){
        Intent intentMatch3 = new Intent(this, GameMatch3Activity.class);
        startActivity(intentMatch3);
    }
}
