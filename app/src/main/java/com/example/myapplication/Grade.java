package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Grade extends AppCompatActivity {
    Button Introduction, Relations ,Greetings,Travels,Questions;
    FirebaseAuth fAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_learning);

        Introduction = findViewById(R.id.button14);
        Relations = findViewById(R.id.button17);
        Greetings = findViewById(R.id.button18);
        Travels = findViewById(R.id.button19);
        Questions = findViewById(R.id.button20);
        fAuth = FirebaseAuth.getInstance();


        Introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Score1.class));

            }
        });

        Relations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Score2.class));
            }
        });
        Greetings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Score3.class));
            }
        });
        Travels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Score4.class));
            }
        });
        Questions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Score5.class));
            }
        });

    }
}