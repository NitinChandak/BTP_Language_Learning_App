package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Quiz5 extends AppCompatActivity {

    // creating a variable for our array list, adapter class,
    // recycler view, progressbar, nested scroll view

    TextView tvQueConter, tvTimer, tvQue;

    Button btOpt1, btOpt2, btOpt3, btOpt4;
    int size=10;

    //list for que
    ArrayList<QuestionClass> queList = new ArrayList<>();
    ArrayList<String> ansList=new ArrayList<>();
    String firstName =" ";
    String lastName = " ";
    String email = " ";
    String rightans = " ";
    int conter = 1;
    static int score= 0;
    FirebaseDatabase firebaseDatabase;
    int i=1;
    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    DatabaseReference myref;
    DatabaseReference myref1;
    DatabaseReference myref2;

    CountDownTimer tm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz5);

        // creating a new array list.

        // initializing our views.
        tvQue = findViewById(R.id.tvQue);
        tvQueConter = findViewById(R.id.tvQueConter);
        // tvTimer = findViewById(R.id.tvTimer);

        btOpt1 = findViewById(R.id.btOpt1);
        btOpt2 = findViewById(R.id.btOpt2);
        btOpt3 = findViewById(R.id.btOpt3);
        btOpt4 = findViewById(R.id.btOpt4);

        // calling a method to load our API.
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        myref = databaseReference.child("1Ko0UxG3wO5Jk1flOmiue4DhvbebvFNyhq_u50HH7rqA");
        myref2 = myref.child("Sheet5");


        myref1 = myref2.child(Integer.toString(i));
        loop();
        // calling a method to load our API.

    }
    private void loop() {
        Random rand = new Random();
        int j = rand.nextInt(7);
        int a = rand.nextInt(10);
        while (a == conter || a==0) {
            a = rand.nextInt(10);
        }
        int b = rand.nextInt(10);
        while (b == conter || a == b || b == 0) {
            b = rand.nextInt(10);
        }
        int c = rand.nextInt(10);
        while (c == conter || c == b || c == a || c == 0) {
            c = rand.nextInt(10);

        }


        //  tvTimer.setText("12");
        //countdown time
       /* tm = new CountDownTimer(100 * 1000, 1000) {


            @Override
            public void onTick(long millisUntilFinished) {
                tvTimer.setText("" + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {

                Toast.makeText(Quiz1.this, "Quiz Over", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),Fin.class));
            }
        };*/

        getDataFromAPI1(conter);
        if(j==0){
            getDataFromAPI(conter,btOpt1);
            getDataFromAPI(a,btOpt2);
            getDataFromAPI(b,btOpt3);
            getDataFromAPI(c,btOpt4);
        }
        else if(j==1){
            getDataFromAPI(conter,btOpt2);
            getDataFromAPI(a,btOpt1);
            getDataFromAPI(b,btOpt3);
            getDataFromAPI(c,btOpt4);

        }
        else if(j==2){
            getDataFromAPI(conter,btOpt3);
            getDataFromAPI(a,btOpt2);
            getDataFromAPI(b,btOpt1);
            getDataFromAPI(c,btOpt4);

        }
        else if(j==3){
            getDataFromAPI(conter,btOpt4);
            getDataFromAPI(a,btOpt2);
            getDataFromAPI(b,btOpt3);
            getDataFromAPI(c,btOpt1);

        }
        else if(j==4){
            getDataFromAPI(conter,btOpt4);
            getDataFromAPI(a,btOpt3);
            getDataFromAPI(b,btOpt2);
            getDataFromAPI(c,btOpt1);

        }
        else if(j==5){
            getDataFromAPI(conter,btOpt2);
            getDataFromAPI(a,btOpt3);
            getDataFromAPI(b,btOpt1);
            getDataFromAPI(c,btOpt4);
        }
        else if(j==6){
            getDataFromAPI(conter,btOpt4);
            getDataFromAPI(a,btOpt2);
            getDataFromAPI(b,btOpt1);
            getDataFromAPI(c,btOpt3);

        }
        else if(j==7){
            getDataFromAPI(conter,btOpt3);
            getDataFromAPI(a,btOpt1);
            getDataFromAPI(b,btOpt2);
            getDataFromAPI(c,btOpt4);

        }

    }

    private void getDataFromAPI1(int n) {
        tvQueConter.setText(conter+"/"+size);

        /*tvTimer.setText("" + 100);
        if (tm != null) {
            tm.start();
        }*/

        myref1 = myref2.child(Integer.toString(n));
        myref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object e = snapshot.child("email").getValue();
                Object f=snapshot.child("first_name").getValue();
                email = String.valueOf(e);
                rightans=String.valueOf(f);

                tvQue.setText("#" + n + " "+ "Choose Right word for" + " " + email);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Quiz5.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void  getDataFromAPI(int n, Button b) {
        myref1 = myref2.child(Integer.toString(n));
        myref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object e = snapshot.child("first_name").getValue();
                firstName = String.valueOf(e);
                b.setText(firstName);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (conter < size) {
                            if (b.getText().equals(rightans)) {
                                Toast.makeText(Quiz5.this, "Correct Answer", Toast.LENGTH_LONG).show();
                                conter++;
                                score++;
                                loop();
                            }
                            else {
                                Toast.makeText(Quiz5.this, "Wrong Answer", Toast.LENGTH_LONG).show();
                                conter++;
                                loop();
                            }
                        }
                        else {
                            Toast.makeText(Quiz5.this, "All Que Completed!", Toast.LENGTH_LONG).show();
                            String s = String.valueOf(score);
                            Toast.makeText(Quiz5.this, s, Toast.LENGTH_LONG).show();
                            store(score);
                            startActivity(new Intent(getApplicationContext(), Fin5.class));

                        }

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Quiz5.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public static Integer getScore4(){
        return score;
    }
    public void store(int score) {
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference();
        DatabaseReference reference1 = reference.child("users");
        DatabaseReference reference2 = reference1.child(userId);
        reference2.child("Questions").child("Test1").setValue(score);
    }
}





