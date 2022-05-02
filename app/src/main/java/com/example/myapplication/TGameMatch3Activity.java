package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class TGameMatch3Activity extends AppCompatActivity implements TaskCompleted {

    //Variables
    // String URL_STRING = "https://shopicruit.myshopify.com/admin/products.json?page=1&access_token=c32313df0d0ef512ca64d5b336a0d7c6";
    ArrayList<String> img_urls;
    ArrayList<String>Img_urls;
    ArrayList<String> email;
    ArrayList<String> Email;
    ArrayList<String> first_Name;
    ArrayList<String> First_Name;
    int score=0;
    final int NUM_IMGS = 6;


    AlertDialog.Builder builder;
    ArrayList<ImageButton> buttons;
    Map<ImageButton, Card> hm;
    Set<ImageButton> matchedCards;
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our
    // Database Reference for Firebase.
    DatabaseReference databaseReference;
    DatabaseReference myref;
    DatabaseReference myref1;
    DatabaseReference myref2;
    DatabaseReference Myref;

    boolean isBoardLocked;
    ImageButton firstCard;
    ImageButton secondCard;
    /*String email;
    String firstName;*/
    String avatar;
    TextView flips;
    TextView matches;
    TextView Hindi;
    TextView English;
    int flip_count = 0;
    int match_count = 0;
    int i=1;
    Button shuffle;

    //initializes data structures and makes API call
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match3);

        builder = new AlertDialog.Builder(this);

        flips = findViewById(R.id.textView1);
        matches = findViewById(R.id.textView2);


        flips.setText("Flips: " + flip_count);
        matches.setText("Matches: " + match_count);



        shuffle = findViewById(R.id.button_shuffle);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
        myref = databaseReference.child("1Ko0UxG3wO5Jk1flOmiue4DhvbebvFNyhq_u50HH7rqA");
        myref2 = myref.child("Sheet6");
        Myref=myref2.child("Travel");


        myref1 = myref2.child(Integer.toString(i));
        img_urls = new ArrayList<>();
        email=new ArrayList<>();
        first_Name=new ArrayList<>();
        loop(new UserListCallback() {
            @Override
            public void onCallback(ArrayList<String> img_urls,ArrayList<String>email,ArrayList<String>first_Name) {
                System.out.println("Loaded "+img_urls.size()+" contacts");
                Img_urls=new ArrayList<>(6);
                Email=new ArrayList<>(6);
                // First_Name=new ArrayList<>(10);
                for(int j=0;j<6;j++) {
                    Img_urls.add(img_urls.get(j));
                    Email.add(email.get(j));
                    //First_Name.add(first_Name.get(j));
                }
                System.out.println("Work "+Img_urls.size()+" Images");
                onTaskComplete();
            }
        });
        // System.out.println("Kaam "+Img_urls+" Images");
        //getDataFromAPI();

        //new AsyncComplex(Game5x4Activity.this).execute(URL_STRING);
    }

    //upon retrieving JSON object, extracts images from it
    //then assigns images to each button


    private void loop(final UserListCallback myCallback){

        // myref1 = myref2.child(Integer.toString(i));

        Myref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               /* Object e = snapshot.child("email").getValue();
                Object f=snapshot.child("first_name").getValue();
                Object a=snapshot.child("avatar").getValue();
                email = String.valueOf(e);
                firstName=String.valueOf(f);
                avatar = String.valueOf(a);

                img_urls.add(avatar);

                if(i<NUM_IMGS){
                    i=i+1;
                    loop();
                }*/

                for (DataSnapshot Snapshot : snapshot.getChildren()) {
                    TextImage w = Snapshot.getValue(TextImage.class);
                    assert w != null;
                    String avatar = w.getImg();
                    String e=w.getPair_img();
                    //String f=w.getFirst_name();
                    System.out.println("first "+e+" contacts");
                    img_urls.add(avatar);
                    email.add(e);
                    //first_Name.add(f);
                    //mContactsFromFirebase.add(contact_found);
                    System.out.println("Loaded img "+img_urls+" contacts");
                    System.out.println("Loaded email "+email+" contacts");
                    //System.out.println("Loaded first "+first_Name+" contacts");
                }


                myCallback.onCallback(img_urls,email,first_Name);



            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // calling on cancelled method when we receive
                // any error or we are not able to get the data.
                Toast.makeText(TGameMatch3Activity.this, "Fail to get data.", Toast.LENGTH_SHORT).show();
            }

        });

    }
    @Override
    public void onTaskComplete(){
        //loop();
        // img_urls=new ArrayList<>(10);
        /*img_urls.add("https://images.unsplash.com/photo-1504197832061-98356e3dcdcf?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTd8fHRpbWV8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        img_urls.add("https://images.unsplash.com/photo-1557862921-37829c790f19?ixid=MnwxMjA3fDB8MHxzZWFyY2h8M3x8bWFufGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        img_urls.add("https://images.unsplash.com/photo-1508214751196-bcfd4ca60f91?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxzZWFyY2h8NHx8d29tYW58ZW58MHx8MHx8&auto=format&fit=crop&w=500&q=60");
        img_urls.add("https://images.unsplash.com/photo-1621440318464-72633426377b?ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8b25lfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        img_urls.add("https://images.unsplash.com/photo-1554126807-6b10f6f6692a?ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8Ym95fGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        img_urls.add("https://images.unsplash.com/photo-1517677129300-07b130802f46?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MTd8fGdpcmx8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        img_urls.add("https://images.unsplash.com/flagged/photo-1552863473-6e5ffe5e052f?ixid=MnwxMjA3fDB8MHxzZWFyY2h8MXx8ZXZvbHV0aW9ufGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        img_urls.add("https://images.unsplash.com/photo-1534294228306-bd54eb9a7ba8?ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8d29ybGR8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        img_urls.add("https://images.unsplash.com/photo-1534294228306-bd54eb9a7ba8?ixid=MnwxMjA3fDB8MHxzZWFyY2h8OHx8d29ybGR8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
        img_urls.add("https://images.unsplash.com/photo-1627662056598-dcfc5000f769?ixid=MnwxMjA3fDF8MHxzZWFyY2h8MXx8Zm9vZHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60");
*/
        //display image on each button


        buttons = new ArrayList<>();
        initializeBoard(buttons);

        //shuffle cards when shuffle button is clicked
        shuffle.setOnClickListener(v -> {
            for(ImageButton b : buttons){
                Random random = new Random();
                int idx = random.nextInt(buttons.size());
                if(!matchedCards.contains(b) && !matchedCards.contains(buttons.get(idx))){
                    Card temp = hm.get(b);
                    hm.put(b, hm.get(buttons.get(idx)));
                    hm.put(buttons.get(idx), temp);
                }
            }
        });
    }



    //input: arraylist of buttons representing cards
    //output: void (set images, onClickListener logic to each button)
    private void initializeBoard(ArrayList<ImageButton> buttons){

        //assign images to buttons
        buttons.add((ImageButton)findViewById(R.id.imageButton1));
        buttons.add((ImageButton)findViewById(R.id.imageButton2));
        buttons.add((ImageButton)findViewById(R.id.imageButton3));
        buttons.add((ImageButton)findViewById(R.id.imageButton4));
        buttons.add((ImageButton)findViewById(R.id.imageButton5));
        buttons.add((ImageButton)findViewById(R.id.imageButton6));
        buttons.add((ImageButton)findViewById(R.id.imageButton7));
        buttons.add((ImageButton)findViewById(R.id.imageButton8));
        buttons.add((ImageButton)findViewById(R.id.imageButton9));
        buttons.add((ImageButton)findViewById(R.id.imageButton10));
        buttons.add((ImageButton)findViewById(R.id.imageButton11));
        buttons.add((ImageButton)findViewById(R.id.imageButton12));



        Collections.shuffle(buttons, new Random());

        //set onClickListener logic for the buttons
        hm = new HashMap<>();
        matchedCards = new HashSet<>();
        isBoardLocked = false;
        firstCard = null;

        //assign each img_id to two of the buttons
        //onClickListener: for every two cards flipped, check if they are a match
        for(int i = 0; i < buttons.size(); i++) {

            if (Img_urls.size() != 0) {
                final int img_id = i % Img_urls.size();
                final ImageButton card = buttons.get(i);
                hm.put(card, new Card(img_id, true));

                int finalI = i;
                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!canFlipCard(card))
                            return;

                        //update the scoreboard
                        flips.setText("Flips: " + Integer.toString(++flip_count));

                        //if current card is first of the two, keep record and flip it
                        if (firstCard == null) {
                            firstCard = card;
                            // pairCard=card1;
                            if(finalI <6){
                                Picasso.get().load(Img_urls.get(hm.get(firstCard).img_id)).into(firstCard);}
                            else{
                                Picasso.get().load(Email.get(hm.get(firstCard).img_id)).into(firstCard);
                            }
                            //Picasso
                            //     Hindi.setVisibility(View.VISIBLE);
                            //    Hindi.setText(First_Name.get(hm.get(firstCard).img_id));
//                            System.out.println("Hindi"+First_Name.get(hm.get(firstCard).img_id));
                            return;
                        }

                        //if current card is second of the two, check if it's a match
                        //if(pairCard==Card)
                        if (hm.get(firstCard).img_id == hm.get(card).img_id) {
                            if(finalI<6) {
                                Picasso.get().load(Img_urls.get(hm.get(card).img_id)).into(card);
                            }
                            else{
                                Picasso.get().load(Email.get(hm.get(card).img_id)).into(card);
                            }
                            //   English.setVisibility(View.VISIBLE);
                            //  English.setText(Email.get(hm.get(firstCard).img_id));
                            processCardMatch(firstCard, card);
                        } else {
                            //English.setVisibility(View.VISIBLE);
                            processCardNoMatch(firstCard, card);

                        }
                    }

                    private boolean canFlipCard(ImageButton card) {
                        return !isBoardLocked && (card != firstCard) && !matchedCards.contains(card);
                    }

                    private void processCardMatch(ImageButton card1, ImageButton card2) {
                        matchedCards.add(card1);
                        matchedCards.add(card2);
                        card1.setEnabled(false);
                        card2.setEnabled(false);
                        matches.setText("Matches: " + matchedCards.size() / 2);
                        firstCard = null;
                        if (matchedCards.size() == 2 * NUM_IMGS) {
                            if(flip_count<=40){
                                score=10;

                            }
                            store(score);
                            builder.setMessage("Do you want to attempt again?")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent = getIntent();
                                            finish();
                                            startActivity(intent);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert = builder.create();
                            alert.setTitle("You Win!!!");
                            alert.show();
                        }
                    }

                    private void processCardNoMatch(ImageButton card1, ImageButton card2) {
                        isBoardLocked = true;
                        firstCard = card1;
                        secondCard = card2;
                        Picasso.get().load(Img_urls.get(hm.get(secondCard).img_id)).into(secondCard);
                        // English.setText(Email.get(hm.get(secondCard).img_id));
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                firstCard.setImageResource(0);
                                secondCard.setImageResource(0);
                                firstCard = null;
                                secondCard = null;
                                isBoardLocked = false;

                            }
                        }, 4000);
                    }
                });
            }
        }
    }

    public void store(int score){
        FirebaseAuth fAuth = FirebaseAuth.getInstance();
        String userId = Objects.requireNonNull(fAuth.getCurrentUser()).getUid();
        FirebaseDatabase rootNode = FirebaseDatabase.getInstance();
        DatabaseReference reference = rootNode.getReference();
        DatabaseReference reference1=reference.child("users");
        DatabaseReference reference2=reference1.child(userId);
        reference2.child("Travel").child("Test3").setValue(score);

        //reference2.child("score").setValue(score);
    }


}
