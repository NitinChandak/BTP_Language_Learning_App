package com.example.myapplication;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Objects;

public class PronunciationTest1 extends AppCompatActivity
        implements View.OnClickListener {

    private ImageButton SoundButton1, SoundButton2, SoundButton3, SoundButton4;
    private Button Option1, Option2, Option3, Option4, NextQuestion;
    private TextView question;

    String englishWord = "";
    // TODO: get correct and from database
    int correctAns = 2;

    int counter = 1;
    int totalWords = 20;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference myref;
    DatabaseReference myref1;
    DatabaseReference myref2;

    private static String mFileName = null;
    private MediaRecorder mRecorder;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    MediaPlayer mp1 = new MediaPlayer();
    MediaPlayer mp2 = new MediaPlayer();
    MediaPlayer mp3 = new MediaPlayer();
    MediaPlayer mp4 = new MediaPlayer();


    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pronunciation_test1);
        SoundButton1 = findViewById(R.id.soundButton1);
        SoundButton2 = findViewById(R.id.soundButton2);
        SoundButton3 = findViewById(R.id.soundButton3);
        SoundButton4 = findViewById(R.id.soundButton4);
        Option1 = findViewById(R.id.option1);
        Option2 = findViewById(R.id.option2);
        Option3 = findViewById(R.id.option3);
        Option4 = findViewById(R.id.option4);
        question = findViewById(R.id.textView34);
        NextQuestion = findViewById(R.id.nextQuestion);

        Option1.setOnClickListener(this);
        Option2.setOnClickListener(this);
        Option3.setOnClickListener(this);
        Option4.setOnClickListener(this);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        myref = databaseReference.child("1Ko0UxG3wO5Jk1flOmiue4DhvbebvFNyhq_u50HH7rqA");
        myref2 = myref.child("Pronunciations");
        myref1 = myref2.child(Integer.toString(counter));

        getDataFromAPI();
    }


    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.option1 || v.getId() == R.id.option2 || v.getId() == R.id.option3 || v.getId() == R.id.option4){
            Option1.setEnabled(false);
            Option2.setEnabled(false);
            Option3.setEnabled(false);
            Option4.setEnabled(false);
            switch (v.getId()){
                case R.id.option1:
                case R.id.option2:
                case R.id.option3:
                case R.id.option4:
                    if(correctAns == Integer.parseInt((String) ((Button)findViewById(v.getId())).getText())){
                        ((Button)(findViewById(v.getId()))).setBackgroundColor(0xFF00FF00);
                    }else{
                        ((Button)(findViewById(v.getId()))).setBackgroundColor(0xFFFF0000);
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    Log.d("TAG", "permissions: "+permissionToRecord+"  "+grantResults[0]);
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result1 == PackageManager.PERMISSION_GRANTED && result == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        Log.d("TAG", "going for permissions");
        ActivityCompat.requestPermissions(this,
                new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE},
                REQUEST_AUDIO_PERMISSION_CODE);
    }

    private void getDataFromAPI() {
        final boolean[] audioLoaded = {false};
        myref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Option1.setEnabled(true);
                Option2.setEnabled(true);
                Option3.setEnabled(true);
                Option4.setEnabled(true);
                Option1.setBackgroundColor(0xFFE3DBDB);
                Option2.setBackgroundColor(0xFFE3DBDB);
                Option3.setBackgroundColor(0xFFE3DBDB);
                Option4.setBackgroundColor(0xFFE3DBDB);


                Log.d("Option", "onDataChange: "+Option1.getText());
                englishWord = String.valueOf(snapshot.child("englishWord").getValue());

                question.setText("Listen to the sounds above and choose which sound is the correct pronunciation of the word "+englishWord);

                NextQuestion.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        if(counter<totalWords){
                            counter++;
                            myref1 = myref2.child(Integer.toString(counter));
                            if(counter==totalWords) {
                                NextQuestion.setText("Test");
                            }
                            getDataFromAPI();
                        }else{
                            startActivity(new Intent(getApplicationContext(), PronunciationTestPage.class));
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PronunciationTest1.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
