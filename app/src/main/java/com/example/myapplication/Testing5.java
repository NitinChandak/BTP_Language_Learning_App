package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Testing5 extends AppCompatActivity {
    Button Testing1, Testing2 ,Testing3,Testing4,Testing5;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);

        Testing1 = findViewById(R.id.button21);
        Testing2 = findViewById(R.id.button22);
        Testing3 = findViewById(R.id.button23);
        Testing4 = findViewById(R.id.button24);
        Testing5 = findViewById(R.id.button25);
        fAuth = FirebaseAuth.getInstance();


        Testing1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Quiz5.class));

            }
        });

        Testing2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QTest2.class));
            }
        });
        Testing3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), QTest3.class));
            }
        });
        Testing4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Testing.class));
            }
        });
        Testing5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Testing.class));
            }
        });

    }
}
