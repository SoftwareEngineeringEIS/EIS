package com.example.jhonsalya.eis;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class DetailEventActivity extends AppCompatActivity {

    private String post_key = null;
    private DatabaseReference mDatabase;
    private ImageView detailPostImage;
    private TextView detailPostTitle;
    private TextView detailPostCategory;
    private TextView detailPostStartDate;
    private TextView detailPostFinishDate;
    private TextView detailPostParticipant;
    private TextView detailPostLocation;
    private TextView detailPostDescription;
    private Button deleteButton;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        detailPostImage = (ImageView) findViewById(R.id.detailImageView);
        detailPostTitle = (TextView) findViewById(R.id.detailTitle);
        detailPostCategory = (TextView) findViewById(R.id.detailCategory);
        detailPostStartDate = (TextView) findViewById(R.id.detailStartDate);
        detailPostFinishDate = (TextView) findViewById(R.id.detailFinishDate);
        detailPostParticipant = (TextView) findViewById(R.id.detailParticipant);
        detailPostLocation = (TextView) findViewById(R.id.detailLocation);
        detailPostDescription = (TextView) findViewById(R.id.detailDescription);

        mAuth = FirebaseAuth.getInstance();
        deleteButton = (Button) findViewById(R.id.detailDeleteButton);
        deleteButton.setVisibility(View.INVISIBLE);

        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_category = (String) dataSnapshot.child("category").getValue();
                String post_start_date = (String) dataSnapshot.child("start_date").getValue();
                String post_finish_date = (String) dataSnapshot.child("finish_date").getValue();
                String post_participant = (String) dataSnapshot.child("participant").getValue();
                String post_location = (String) dataSnapshot.child("location").getValue();
                String post_description = (String) dataSnapshot.child("description").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
