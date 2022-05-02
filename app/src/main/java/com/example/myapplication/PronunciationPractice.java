package com.example.myapplication;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import static com.example.myapplication.Register.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
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
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Objects;

public class PronunciationPractice extends AppCompatActivity {

    private TextView OriginalWord;
    private TextView WordInHindi;
    private TextView WordInTelugu;
    private TextView ListeningText;
    private TextView WordMeaning;
    private TextView SpeakerMessage;
    private TextView Loading;
    private Button NextButton;
    private ImageButton SpeakerButton;
    private ImageButton Mic;
    int counter = 1;

    String hindiWord = " ";
    String englishWord = " ";
    String teluguWord = " ";
    String meaning = " ";
    String audioLink = " ";


    // TODO: Change totalWords to 20
    int totalWords = 1;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference myref;
    DatabaseReference myref1;
    DatabaseReference myref2;

    private static String mFileName = null;
    private MediaRecorder mRecorder;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    MediaPlayer mp = new MediaPlayer();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pronunciation_practice);

        OriginalWord = findViewById(R.id.originalword);
        WordInHindi = findViewById(R.id.wordInHindi);
        WordInTelugu = findViewById(R.id.wordInTelugu);
        ListeningText = findViewById(R.id.listening);
        NextButton = findViewById(R.id.nextButton);
        WordMeaning = findViewById(R.id.wordMeaning);
        SpeakerButton = findViewById(R.id.soundButton);
        SpeakerMessage = findViewById(R.id.textView24);
        Loading = findViewById(R.id.textView27);
        Mic = findViewById(R.id.micButton);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        myref = databaseReference.child("1Ko0UxG3wO5Jk1flOmiue4DhvbebvFNyhq_u50HH7rqA");
        myref2 = myref.child("Pronunciations");


        myref1 = myref2.child(Integer.toString(counter));

        if(!CheckPermissions()) {
            ActivityCompat.requestPermissions(PronunciationPractice.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
        }
        getDataFromAPI();
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

    private void startRecording() {
        mFileName = getExternalCacheDir().getAbsolutePath();
        Log.d(TAG, "startRecording: "+mFileName);
        mFileName += "/AudioRecording.3gp";
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        mRecorder.setOutputFile(mFileName);
        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
            System.out.println(e);
        }
        mRecorder.start();
    }

    public void pauseRecording() {
        if(mRecorder != null) {
            mRecorder.stop();
            Log.d("pause", "pauseRecording: ");
            mRecorder.release();
            mRecorder = null;
            MediaPlayer m = new MediaPlayer();
            try {
                m.setDataSource(mFileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                m.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
            m.start();
        }
    }


    private void getDataFromAPI() {
        myref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SpeakerButton.setEnabled(false);
                SpeakerMessage.setText("");
                Loading.setText("Loading...");
                long s = System.currentTimeMillis();
                hindiWord = String.valueOf(snapshot.child("hindiWord").getValue());
                englishWord = String.valueOf(snapshot.child("englishWord").getValue());
                meaning = String.valueOf(snapshot.child("meaning").getValue());
                teluguWord = String.valueOf(snapshot.child("teluguWord").getValue());
                audioLink = String.valueOf(snapshot.child("audio").getValue());

                OriginalWord.setText(englishWord);
                WordInHindi.setText(hindiWord);
                WordInTelugu.setText(teluguWord);
                WordMeaning.setText("Meaning:\n"+meaning);

                mp.stop();
                mp.reset();
                try {
                    mp.setDataSource(audioLink);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mp.prepareAsync();

                mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        SpeakerButton.setEnabled(true);
                        SpeakerMessage.setText("Click on the speaker icon to listen to the word");
                        Loading.setText("");
                    }
                });

                SpeakerButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(mp.isPlaying()) {
                            mp.pause();
                        }
                        else{
                            mp.start();
                        }
                    }
                });

                Mic.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(CheckPermissions()) {
                            ListeningText.setText("Listening");
                            Mic.setEnabled(false);
                            startRecording();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Mic.setEnabled(true);
                                    pauseRecording();
                                    ListeningText.setText("");
                                }
                            }, 3000);
                        }else{
                            RequestPermissions();
                        }
                    }
                });

                if(counter==totalWords) {
                    NextButton.setText("Test");
                }
                NextButton.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){

                        if(counter<totalWords){
                            counter++;
                            myref1 = myref2.child(Integer.toString(counter));
                            getDataFromAPI();
                        }else{
                            startActivity(new Intent(getApplicationContext(), PronunciationTestPage.class));
                        }
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PronunciationPractice.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
