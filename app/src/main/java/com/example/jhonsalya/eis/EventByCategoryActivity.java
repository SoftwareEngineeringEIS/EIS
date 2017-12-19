package com.example.jhonsalya.eis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.jhonsalya.eis.Interface.ItemClickListener;
import com.example.jhonsalya.eis.ViewHolder.EventByCategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class EventByCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference eventList;

    String categoryId=""; //kemungkinan id kategori akan berupa namanya cth. education

    FirebaseRecyclerAdapter<Event, EventByCategoryViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_by_category);

        //Firebase
        database = FirebaseDatabase.getInstance();
        eventList = database.getReference("EventApp");

        recyclerView = (RecyclerView)findViewById(R.id.recycler_event);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get Intent here
        if(getIntent() != null)
            categoryId = getIntent().getStringExtra("CategoryId");
        if(!categoryId.isEmpty() && categoryId != null){
            Toast.makeText(EventByCategoryActivity.this, categoryId, Toast.LENGTH_SHORT).show();
            loadListEvent(categoryId);
        }
    }

    private void loadListEvent(String categoryId) {
        adapter = new FirebaseRecyclerAdapter<Event, EventByCategoryViewHolder>(Event.class,
                R.layout.event_row,
                EventByCategoryViewHolder.class,
                eventList.orderByChild("category").equalTo(categoryId)
                ) {
            @Override
            protected void populateViewHolder(EventByCategoryViewHolder viewHolder, Event model, int position) {
                final String post_key = getRef(position).getKey().toString();

                viewHolder.setTitle(model.getTitle());
                viewHolder.setDesc(model.getDesc());
                viewHolder.setImage(getApplicationContext(),model.getImage());

                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent eventDetailActivity = new Intent(EventByCategoryActivity.this, DetailEventActivity.class);
                        eventDetailActivity.putExtra("PostId", post_key);
                        startActivity(eventDetailActivity);
                    }
                });
                /*viewHolder.event_name.setText(model.getTitle());
                viewHolder.event_desc.setText(model.getDesc());
                Picasso.with(getApplicationContext()).load(model.getImage()).into(viewHolder.event_image);


                final Event local = model;
                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        final String post_key = getRef(position).getKey().toString();
                        Intent eventDetailActivity = new Intent(EventByCategoryActivity.this, DetailEventActivity.class);
                        eventDetailActivity.putExtra("PostId", post_key);
                        startActivity(eventDetailActivity);
                    }
                });*/

            }
        };
        Log.d("TAG","" + adapter.getItemCount());
        recyclerView.setAdapter(adapter);
    }
}
