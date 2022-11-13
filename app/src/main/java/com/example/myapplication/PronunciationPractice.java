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
    File localFile = null;

    String hindiWord = " ";
    String englishWord = " ";
    String teluguWord = " ";
    String meaning = " ";
    String audioLink = " ";
    String storageAudioFile = "outputFile.mp3";


    // TODO: Change totalWords to 20
    int totalWords = 1;


    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference myref;
    DatabaseReference myref1;
    DatabaseReference myref2;

    private static String inputAudioFile = null;
    private MediaRecorder mRecorder;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    MediaPlayer mp = new MediaPlayer();
    wavClass wavObj = null;

    StorageReference audioRef = null;


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

    public void pauseRecording() throws JavaLayerException {
        if(wavObj != null) {
            wavObj.stopRecording();
            Log.d("pause", "pauseRecording: ");
            try {



                new Converter().convert(localFile.getAbsolutePath(), getExternalCacheDir().getAbsolutePath()+"/outputFile.wav");
                inputAudioFile = wavObj.filePath + "/" + wavObj.tempWavFile;
                Wave Wav1 = new Wave(inputAudioFile);
                Wave Wav2 = new Wave(getExternalCacheDir().getAbsolutePath()+"/outputFile.wav");

                Spectrogram spec1 = new Spectrogram(Wav1);
                Spectrogram spec2 = new Spectrogram(Wav2);
                double[][] res1 = spec1.getNormalizedSpectrogramData();
                double[][] res2 = spec2.getNormalizedSpectrogramData();
                System.out.println(res1.length + " " + res1[0].length);
                System.out.println(res2.length + " " + res2[0].length);

                double total = 0;
                int count = 0;
                for(int i=0;i<res2.length;i++){
                    for(int j=0;j<res2[i].length;j++) {
                        total += Math.abs(res2[i][j] - res1[i][j]);
                        count += 1;
                    }
                }
                System.out.println(total);
                System.out.println(count);


                byte[] firstFingerPrint = new FingerprintManager().extractFingerprint(Wav1);
                byte[] secondFingerPrint = new FingerprintManager().extractFingerprint(Wav2);
                Log.e("Here", String.valueOf(firstFingerPrint));
                Log.e("Here", String.valueOf(secondFingerPrint));
                FingerprintSimilarity similarity = Wav1.getFingerprintSimilarity(Wav2);
                Log.i("Similarity Here", String.valueOf(similarity.getScore()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void getDataFromAPI() {
        myref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SpeakerButton.setEnabled(false);
                Mic.setEnabled(false);
                SpeakerMessage.setText("");
                Loading.setText("Loading...");
                ListeningText.setText("Loading...");
                long s = System.currentTimeMillis();
                hindiWord = String.valueOf(snapshot.child("hindiWord").getValue());
                englishWord = String.valueOf(snapshot.child("englishWord").getValue());
                meaning = String.valueOf(snapshot.child("meaning").getValue());
                teluguWord = String.valueOf(snapshot.child("teluguWord").getValue());
                audioLink = String.valueOf(snapshot.child("audio").getValue());


                audioRef = FirebaseStorage.getInstance().getReferenceFromUrl(audioLink);
                audioRef.getFile(localFile);


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
                        Mic.setEnabled(true);
                        ListeningText.setText("");
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
                            Log.i("TAG", "onClick:" + audioLink);
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
                            try {
                                startRecording();
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Mic.setEnabled(true);
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
