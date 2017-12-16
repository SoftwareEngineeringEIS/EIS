package com.example.jhonsalya.eis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener{

    //drawer for navigation
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //end of drawer

    private RecyclerView mEventList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setting navigation view

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mEventList = (RecyclerView) findViewById(R.id.eventList);
        mEventList.setHasFixedSize(true);
        mEventList.setLayoutManager(new LinearLayoutManager(this));
        mDatabase = FirebaseDatabase.getInstance().getReference().child("EventApp");

        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        //mAuth.addAuthStateListener(mAuthListener);

        FirebaseRecyclerAdapter <Event, EventViewHolder> FBRA = new FirebaseRecyclerAdapter<Event, EventViewHolder>(
                Event.class,
                R.layout.event_row,
                EventViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(EventViewHolder viewHolder, Event model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                //Detail Event belum selesai
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent eventDetailActivity = new Intent(MainActivity.this, DetailEventActivity.class);
                        eventDetailActivity.putExtra("PostId", post_key);
                        startActivity(eventDetailActivity);
                    }
                });
            }
        };
        mEventList.setAdapter(FBRA);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public EventViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title){
            TextView post_title = (TextView) mView.findViewById(R.id.textTitle);
            post_title.setText(title);
        }

        public void setDesc(String desc){
            TextView post_desc = (TextView) mView.findViewById(R.id.textDescription);
            post_desc.setText(desc);
        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.eventPost);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        else if (id == R.id.add_icon){
            Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_account){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_category){

        }
        else if(id == R.id.nav_sort){

        }
        else if(id == R.id.nav_logout){
            mAuth.signOut();
        }

        return true;
    }
}
