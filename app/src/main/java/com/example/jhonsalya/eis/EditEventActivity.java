package com.example.jhonsalya.eis;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class EditEventActivity extends AppCompatActivity {

    private String post_key = null;
    private static final int GALLERY_REQUEST = 2;
    private Uri uri = null;
    private ImageButton imageButton;
    private EditText editName;
    private EditText editDesc;
    private EditText editLocation;
    private EditText editParticipant;
    private EditText editStartDate;
    private EditText editFinishDate;
    private EditText editCategory;

    Calendar mCurrentDate;

    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        post_key = getIntent().getExtras().getString("PostId");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("EventApp");
        storageReference = FirebaseStorage.getInstance().getReference();


        editName = (EditText) findViewById(R.id.editEventName);
        editDesc = (EditText) findViewById(R.id.editEventDescription);
        editLocation = (EditText) findViewById(R.id.editEventLocation);
        editParticipant = (EditText) findViewById(R.id.editEventParticipant);
        editStartDate = (EditText) findViewById(R.id.editEventStartDate);
        editFinishDate = (EditText) findViewById(R.id.editEventFinishDate);
        editCategory = (EditText) findViewById(R.id.editEventCategory);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());


        //show calendar in start date text view event
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        editStartDate.setText(selectedDay+"/"+selectedMonth+"/"+selectedYear);
                        mCurrentDate.set(selectedYear, selectedMonth, selectedDay);
                    }
                }, year, month, day);
                mDatePicker.show();
            }
        });

        //show calendar in start date text view event
        editFinishDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePicker = new DatePickerDialog(EditEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        editFinishDate.setText(selectedDay+"/"+selectedMonth+"/"+selectedYear);
                        mCurrentDate.set(selectedYear, selectedMonth, selectedDay);
                    }
                }, year, month, day);
                mDatePicker.show();
            }
        });

        databaseReference.child(post_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String post_image = (String) dataSnapshot.child("image").getValue();
                String post_title = (String) dataSnapshot.child("title").getValue();
                String post_category = (String) dataSnapshot.child("category").getValue();
                String post_start_date = (String) dataSnapshot.child("start_date").getValue();
                String post_finish_date = (String) dataSnapshot.child("finish_date").getValue();
                String post_participant = (String) dataSnapshot.child("participant").getValue();
                String post_location = (String) dataSnapshot.child("location").getValue();
                String post_description = (String) dataSnapshot.child("desc").getValue();
                String post_uid = (String) dataSnapshot.child("uid").getValue();

                //Picasso.with(EditEventActivity.this).load(post_image).into(addImage);
                editName.setText(post_title, TextView.BufferType.EDITABLE);
                editCategory.setText(post_category);
                editStartDate.setText(post_start_date);
                editFinishDate.setText(post_finish_date);
                editParticipant.setText(post_participant);
                editLocation.setText(post_location);
                editDesc.setText(post_description);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void imageClicked(View view){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            uri = data.getData();
            imageButton = (ImageButton) findViewById(R.id.addImage);
            imageButton.setImageURI(uri);
        }
    }
    public void submitButtonClicked(View view){
        final String titleValue = editName.getText().toString().trim();
        final String descValue = editDesc.getText().toString().trim();
        final String locationValue = editLocation.getText().toString().trim();
        final String participantValue = editParticipant.getText().toString().trim();
        final String startDateValue = editStartDate.getText().toString().trim();
        final String finishDateValue = editFinishDate.getText().toString().trim();
        final String categoryValue =  editCategory.getText().toString().trim();

        if(!TextUtils.isEmpty(titleValue) && !TextUtils.isEmpty(descValue) && !TextUtils.isEmpty(locationValue)
                && !TextUtils.isEmpty(participantValue) && !TextUtils.isEmpty(startDateValue) && !TextUtils.isEmpty(finishDateValue)
                && !TextUtils.isEmpty(categoryValue)){
            StorageReference filePath = storageReference.child("PostEvent").child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    final Uri downloadurl = taskSnapshot.getDownloadUrl();
                    Toast.makeText(EditEventActivity.this, "Update Complete", Toast.LENGTH_LONG).show();
                    //final DatabaseReference newPost = databaseReference.push();
                    final DatabaseReference newPost = databaseReference.child(post_key);

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newPost.child("title").setValue(titleValue);
                            newPost.child("image").setValue(downloadurl.toString());
                            newPost.child("location").setValue(locationValue);
                            newPost.child("participant").setValue(participantValue);
                            newPost.child("start_date").setValue(startDateValue);
                            newPost.child("finish_date").setValue(finishDateValue);
                            newPost.child("category").setValue(categoryValue);
                            newPost.child("desc").setValue(descValue).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent eventDetailActivity = new Intent(EditEventActivity.this, DetailEventActivity.class);
                                        eventDetailActivity.putExtra("PostId", post_key);
                                        startActivity(eventDetailActivity);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            });

        }
    }
}
