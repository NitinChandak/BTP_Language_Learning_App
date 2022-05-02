package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import java.io.*;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Fin2 extends AppCompatActivity {
    int score= Quiz2.getScore1();
   // System.out.print(score);
    String s=String.valueOf(score);
    private TextView Result;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fin2);
        Result= findViewById(R.id.textView1);
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
