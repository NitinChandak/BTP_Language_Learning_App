package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
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

public class Score3 extends AppCompatActivity {
    int score=0;
    TextView textView, textView1, textView2;
    static String fName,eName,gName;
    Button V;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);
        textView=findViewById(R.id.textView22);
        textView1=findViewById(R.id.textView19);
        textView2=findViewById(R.id.textView21);
        V=findViewById(R.id.button9);
        display();
        V.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Bar3.class));

                //startActivity(new Intent(getApplicationContext(),Word2.class));
            }
        });




    }
    public void display(){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference();
        DatabaseReference reference1=reference.child("users");
        DatabaseReference reference2=reference1.child(userId);
        DatabaseReference reference3=reference2.child("Greetings");

        reference3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    Object f = snapshot.child("Test1").getValue();
                    Object e = snapshot.child("Test2").getValue();
                    Object g = snapshot.child("Test3").getValue();
                    fName = String.valueOf(f);
                    eName = String.valueOf(e);
                    gName = String.valueOf(g);

                    textView.setText(fName);
                    textView1.setText(eName);
                    textView2.setText(gName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Score3.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static ArrayList<String> Give(){

        ArrayList<String> a=new ArrayList<String>();
        a.add(fName);
        a.add(eName);
        a.add(gName);
        return a;

    }
}


