package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.Quiz1;
import com.example.myapplication.Quiz5;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class Fin5 extends AppCompatActivity {
    int score= Quiz5.getScore4();
    String s=String.valueOf(score);
    private TextView Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fin5);
        Result= findViewById(R.id.textView9);
        // creating a new array list.
        Result.setText(s);
        //getDatafromAPI();

        //calling a method to load our API.

    }
    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }


}
