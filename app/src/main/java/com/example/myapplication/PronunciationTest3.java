package com.example.myapplication;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.musicg.fingerprint.FingerprintManager;
import com.musicg.fingerprint.FingerprintSimilarity;
import com.musicg.wave.Wave;

import java.io.File;
import java.io.IOException;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
// Java Dependencies
import java.util.HashMap;
import java.util.Map;

public class PronunciationTest3 extends AppCompatActivity {

    private ImageButton mic;
    private TextView word, comments, score, ListeningText;
    private Button nextButton;
    String englishWord = " ";
    String audioLink = " ";
    int counter = 1;
    float similarityScore = 0.0F;
    String storageAudioFile = "outputFile.mp3";
    int min = 0;
    int max = 100;


    File localFile = null;

    // TODO: Change totalWords to 20
    int totalWords = 1;

    MediaPlayer playRecord = new MediaPlayer();

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference myref;
    DatabaseReference myref1;
    DatabaseReference myref2;

    private static String inputAudioFile = null;
    private MediaRecorder mRecorder;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    wavClass wavObj = null;

    StorageReference audioRef = null;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pronunciation_test_3);

        word = findViewById(R.id.textView38);
        score = findViewById(R.id.textView40);
        comments = findViewById(R.id.textView41);
        mic = findViewById(R.id.micButton2);
        nextButton = findViewById(R.id.nextButton2);
        ListeningText = findViewById(R.id.textView42);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        myref = databaseReference.child("1Ko0UxG3wO5Jk1flOmiue4DhvbebvFNyhq_u50HH7rqA");
        myref2 = myref.child("Pronunciations");


        myref1 = myref2.child(Integer.toString(counter));

        if(!CheckPermissions()) {
            ActivityCompat.requestPermissions(PronunciationTest3.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
        }
        localFile = new File(getExternalCacheDir().getAbsolutePath(),storageAudioFile);
        try {
            localFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
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

    private void startRecording() throws IOException {
        wavObj = new wavClass(getExternalCacheDir().getAbsolutePath());
        wavObj.startRecording();
    }

    private String returnComments(int matchingScore) throws JavaLayerException {
        if(matchingScore<50){
            return "Have to improve a lot";
        }else if(matchingScore <70){
            return "Can do better";
        }
        return "Doing great!";
    }

    private void pauseRecording() throws JavaLayerException {
        if(wavObj != null) {
            inputAudioFile = wavObj.filePath + "/" + wavObj.tempWavFile;
            wavObj.stopRecording();
            Log.d("pause", "pauseRecording: ");
            playRecord.stop();
            playRecord.reset();
            try {
                playRecord.setDataSource(inputAudioFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            playRecord.prepareAsync();

            playRecord.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer playRecord) {
                    playRecord.start();
                }
            });

            Map<String, String> parameters = new HashMap<String, String>();

            // This calls the function in the Cloud Code
            ParseCloud.callFunctionInBackground("getRandomNumber", parameters, new FunctionCallback<Map<String, Object>>() {
                @Override
                public void done(Map<String, Object> mapObject, ParseException e) {
                    if (e == null) {
                        // Everything is alright
                        score.setText(mapObject.get("randomNumber").toString()+"%");
                        try {
                            comments.setText(returnComments((Integer) mapObject.get("randomNumber")));
                        } catch (JavaLayerException ex) {
                            ex.printStackTrace();
                        }
                    }
                    else {
                        // Something went wrong
                    }
                }
            });
        }
    }

    private void getDataFromAPI() {
        myref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long s = System.currentTimeMillis();
                englishWord = String.valueOf(snapshot.child("englishWord").getValue());




                word.setText(englishWord);

                mic.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        if(CheckPermissions()) {
                            ListeningText.setText("Listening");
                            mic.setEnabled(false);
                            try {
                                startRecording();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mic.setEnabled(true);
                                    try {
                                        pauseRecording();
                                    }catch (JavaLayerException e){
                                        e.printStackTrace();
                                    }
                                    ListeningText.setText("");
                                }
                            }, 1500);
                        }else{
                            RequestPermissions();
                        }
                    }
                });

                if(counter==totalWords) {
                    nextButton.setText("Done");
                    nextButton.setEnabled(false);
                }
                nextButton.setOnClickListener(new View.OnClickListener(){
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
                Toast.makeText(PronunciationTest3.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
