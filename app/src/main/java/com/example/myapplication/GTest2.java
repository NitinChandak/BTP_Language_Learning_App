package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class GTest2 extends AppCompatActivity {

    private int presCounter = 0;
    private static int maxPresCounter = 6;
    //ArrayList<String> keys=new ArrayList<>();

    // String firstName =" ";
    String email = " ";
    String A=" ";
    String[][] keys= new String[20][];
    private String firstName = "अमरूद";
    int conter = 1;
    int counter=0;
    int score=0;
    FirebaseDatabase firebaseDatabase;
    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    DatabaseReference myref;
    DatabaseReference myref1;
    DatabaseReference myref2;
    TextView textScreen, textQuestion, textTitle;
    Animation smallbigforth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        textQuestion = (TextView) findViewById(R.id.textQuestion);
        textScreen = (TextView) findViewById(R.id.textScreen);
        textTitle = (TextView) findViewById(R.id.textTitle);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        myref = databaseReference.child("1Ko0UxG3wO5Jk1flOmiue4DhvbebvFNyhq_u50HH7rqA");
        myref2 = myref.child("Sheet3");
        smallbigforth = AnimationUtils.loadAnimation(this, R.anim.smallbigforth);
        loop(conter);
        //loop(4);
        maxPresCounter = 10;
    }
    private void loop(int n){
        myref1 = myref2.child(Integer.toString(n));
        myref1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Object e = snapshot.child("email").getValue();
                Object f=snapshot.child("first_name").getValue();
                email = String.valueOf(e);
                firstName=String.valueOf(f);

                String[] keys= new String[firstName.length()];
                for(int i=0; i<firstName.length();i++) {
                    keys[i] = (String.valueOf(Character.toUpperCase(firstName.charAt(i))));
                }
                System.out.println(Arrays.toString(keys) +"hello");
                GTest2.maxPresCounter=firstName.length();
                textQuestion.setText(String.format("Choose the right word for %s", email));
                keys=shuffleArray(keys);
                EditText editText = findViewById(R.id.editText);
                LinearLayout linearLayout = findViewById(R.id.layoutParent);

                editText.setText("");
                linearLayout.removeAllViews();
                int i=0,m=0,m1=0;
                if(firstName.length()%2==0){
                    m=firstName.length()/2;
                    m1=firstName.length()/2;
                }
                else{
                    m=(int)firstName.length()/2;
                    m1=(int)(firstName.length()/2)+1;
                }
                String[] keys1= new String[m];
                String[] keys2= new String[m1];
                int j=0;
                for (String h:keys) {
                    if(i< m) {
                        keys1[i]=h;
                        //addView(linearLayout, h, editText, keys1, firstName);
                    }
                    else{
                        keys2[j]=h;
                        j=j+1;
                    }
                    i=i+1;
                }

                maxPresCounter = firstName.length();
                for(String k:keys1){
                    addView(linearLayout, k, editText, keys1, firstName, keys1,keys2);
                }
                LinearLayout linearLayout1 = findViewById(R.id.Layout);
                linearLayout1.removeAllViews();
                //maxPresCounter=m1;
                for(String k:keys2){
                    addView(linearLayout1, k, editText, keys2, firstName,keys1,keys2);
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(GTest2.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }

        });

    }
    public String[] shuffleArray(String[] K) {
        Random rnd = new Random();
        if(K.length!=0) {
            for (int i = K.length - 1; i > 0; i--) {
                int index = rnd.nextInt(i + 1);
                String a = K[index];
                K[index] = K[i];
                K[i] = a;
            }
        }
        return K;
    }
    /*
    private void shuffleArray(ArrayList<String> ar) {
        Random rnd = new Random();
        for (int j = ar.size() - 1; j > 0; j--) {
            int index = rnd.nextInt(j+1);
            String a = ar.get(index);
            ar.set(index,ar.get(j));
            ar.set(j,a);
        }
    }*/

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addView(LinearLayout viewParent, final String text, final EditText editText, String[] K, String firstName, String[] k1, String[] k2) {
        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        linearLayoutParams.rightMargin = 5;



        final TextView textView = new TextView(this);

        textView.setLayoutParams(linearLayoutParams);
        textView.setBackground(this.getResources().getDrawable(R.drawable.bgpink));
        textView.setTextColor(this.getResources().getColor(R.color.colorPurple));
        textView.setGravity(Gravity.CENTER_VERTICAL);
        textView.setText(text);
        textView.setClickable(true);
        textView.setFocusable(true);
        textView.setTextSize(28);

        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/FredokaOneRegular.ttf");

        textQuestion.setTypeface(typeface);
        textScreen.setTypeface(typeface);
        textTitle.setTypeface(typeface);
        editText.setTypeface(typeface);
        textView.setTypeface(typeface);

        textView.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if(presCounter < maxPresCounter) {
                    if (presCounter == 0)
                        editText.setText("");

                    editText.setText(editText.getText().toString() + text);
                    textView.startAnimation(smallbigforth);
                    textView.animate().alpha(0).setDuration(300);
                    presCounter++;

                    if (presCounter == maxPresCounter)
                        doValidate(K,firstName, k1, k2);
                }
            }
        });


        viewParent.addView(textView);


    }


    private void doValidate(String[] K, String firstName, String[] k1, String[] k2) {
        presCounter = 0;

        EditText editText = findViewById(R.id.editText);
        LinearLayout linearLayout = findViewById(R.id.layoutParent);
        LinearLayout linearLayout1=findViewById(R.id.Layout);


        if (editText.getText().toString().equals(firstName.toUpperCase())) {
            Toast.makeText(GTest2.this, "Correct", Toast.LENGTH_SHORT).show();
            if (conter < 6) {
                //firstName=" ";
                conter++;
                linearLayout.removeAllViews();
                if(counter<=5){
                    score++;
                }
                else{
                    counter=0;
                }
                loop(conter);

            } else {

                store(score);
                Intent a = new Intent(GTest2.this, Complete.class);
                startActivity(a);
            }

        }
        else {
            Toast.makeText(GTest2.this, "Wrong", Toast.LENGTH_SHORT).show();
            editText.setText("");
            shuffleArray(k1);
            shuffleArray(k2);
            linearLayout.removeAllViews();
            linearLayout1.removeAllViews();
            counter++;

           //maxPresCounter = firstName.length();
            for(String k:k1){
                addView(linearLayout, k, editText, k1, firstName,k1,k2);
            }

            //maxPresCounter=m1;
            for(String k:k2){
                addView(linearLayout1, k, editText, k2, firstName,k1,k2);
            }

        }
    }

    public Integer getScore(){
        return score;
    }

    public void store(int score){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference();
        DatabaseReference reference1=reference.child("users");
        DatabaseReference reference2=reference1.child(userId);
        reference2.child("Greetings").child("Test2").setValue(score);

        //reference2.child("score").setValue(score);
    }


}
