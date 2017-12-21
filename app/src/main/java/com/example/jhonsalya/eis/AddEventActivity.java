package com.example.jhonsalya.eis;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.kd.dynamic.calendar.generator.ImageGenerator;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity{

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

    private StorageReference storageReference;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUsers;
    private FirebaseUser mCurrentUser;

    Calendar mCurrentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        editName = (EditText) findViewById(R.id.editEventName);
        editDesc = (EditText) findViewById(R.id.editEventDescription);
        editLocation = (EditText) findViewById(R.id.editEventLocation);
        editParticipant = (EditText) findViewById(R.id.editEventParticipant);
        editStartDate = (EditText) findViewById(R.id.editEventStartDate);
        editFinishDate = (EditText) findViewById(R.id.editEventFinishDate);
        editCategory = (EditText) findViewById(R.id.editEventCategory);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = database.getInstance().getReference().child("EventApp");

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

                DatePickerDialog mDatePicker = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
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

                DatePickerDialog mDatePicker = new DatePickerDialog(AddEventActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int selectedYear, int selectedMonth, int selectedDay) {
                        editFinishDate.setText(selectedDay+"/"+selectedMonth+"/"+selectedYear);
                        mCurrentDate.set(selectedYear, selectedMonth, selectedDay);
                    }
                }, year, month, day);
                mDatePicker.show();
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
        final ProgressDialog mDialog = new ProgressDialog(AddEventActivity.this);
        mDialog.setMessage("Please Wait.....");
        mDialog.show();

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
                    Toast.makeText(AddEventActivity.this, "Upload Complete", Toast.LENGTH_LONG).show();
                    final DatabaseReference newPost = databaseReference.push();

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
                            newPost.child("desc").setValue(descValue);
                            newPost.child("uid").setValue(mCurrentUser.getUid()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        mDialog.dismiss();
                                        Intent mainActivityIntent = new Intent(AddEventActivity.this, MainActivity.class);
                                        startActivity(mainActivityIntent);
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
