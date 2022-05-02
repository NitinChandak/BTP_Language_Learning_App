package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Word_2 extends AppCompatActivity {

    // creating a variable for our array list, adapter class,
    // recycler view, progressbar, nested scroll view
    private ProgressBar loadingPB;
    private TextView FirstName;
    private TextView LastName;
    private TextView Email;
    private ImageView Avatar;

    int counter = 0;
    String firstName =" ";
    String lastName = " ";
    String email = " ";
    String avatar = " ";
    ArrayList<WordClass> queList = new ArrayList<>();
    Button button4;
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    DatabaseReference myref;
    DatabaseReference myref1;
    DatabaseReference myref2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_2);

        // creating a new array list.
        FirstName = findViewById(R.id.firstName);
        LastName = findViewById(R.id.secondTextView);
        Email = findViewById(R.id.thirdTextView);
        Avatar = findViewById(R.id.imageView);
        button4 = findViewById(R.id.button8);
        // calling a method to load our API.
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        myref = databaseReference.child("1Ko0UxG3wO5Jk1flOmiue4DhvbebvFNyhq_u50HH7rqA");
        myref2 = myref.child("Sheet2");
        int i=1;

        myref1 = myref2.child(Integer.toString(i));
        getDataFromAPI();
        /*button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), Word2.class));

            }
        });*/

    }

    private void getDataFromAPI() {

        myref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // this method is call to get the realtime
                // updates in the data.
                // this method is called when the data is
                // changed in our Firebase console.
                // below line is for getting the data from
                // snapshot of our database.
                /*List<String> list = new ArrayList<>();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    String uid = ds.getKey();
                    list.add(uid);
                }*/

                Object f = snapshot.child("first_name").getValue();
                Object l = snapshot.child("last_name").getValue();
                Object e = snapshot.child("email").getValue();
                Object a = snapshot.child("avatar").getValue();

                firstName = String.valueOf(f);
                lastName = String.valueOf(l);
                email = String.valueOf(e);
                avatar = String.valueOf(a);

                //Picasso.get().load(avatar).resize(250,250).into(Avatar);
                FirstName.setText(firstName);
                LastName.setText(lastName);
                Email.setText(email);

                button4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (counter < 20) {
                            counter++;
                            myref1 = myref2.child(Integer.toString(counter));
                            getDataFromAPI();
                        }
                        else{
                            startActivity(new Intent(getApplicationContext(),Testing2.class));
                        }
                        //startActivity(new Intent(getApplicationContext(),Word2.class));
                    }
                });

                //for(DataSnapshot ds :snapshot.getChildren()) {
                //Object title = snapshot.child("tejaswini").getValue();

                //email = ds.child("tejaswini").getValue().toString();

                //email = snapshot.getValue(String.class);
                //Toast.makeText(MainActivity.this,email, Toast.LENGTH_SHORT).show();


                //}
                // String first_name = ds.child("first_name").getValue(String.class);

                // after getting the value we are setting
                // our value to our text view in below line.

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(Word_2.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }
        });


    }
}






