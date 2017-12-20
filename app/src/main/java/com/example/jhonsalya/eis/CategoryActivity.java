package com.example.jhonsalya.eis;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.jhonsalya.eis.Interface.ItemClickListener;
import com.example.jhonsalya.eis.Model.Category;
import com.example.jhonsalya.eis.ViewHolder.CategoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CategoryActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference category;

    RecyclerView recycler_event;

    FirebaseRecyclerAdapter<Category,CategoryViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        //Init Firebase
        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");

        //load menu
        recycler_event = (RecyclerView) findViewById(R.id.recycler_event);
        recycler_event.setHasFixedSize(true);
        recycler_event.setLayoutManager(new LinearLayoutManager(this));

        loadMenu();
    }

    private void loadMenu() {

        adapter = new FirebaseRecyclerAdapter<Category, CategoryViewHolder>(Category.class,
                R.layout.category_item,
                CategoryViewHolder.class,
                category) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, Category model, int position) {
                viewHolder.txtCategoryName.setText(model.getName());
                final String post_key = model.getName();
                final Category clickItem = model;

                viewHolder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //get categoryID and send to new activity
                        Intent eventList = new Intent(CategoryActivity.this, EventByCategoryActivity.class);
                        //because categoryID is key, so we just get key of this item
                        //eventList.putExtra("CategoryId", adapter.getRef(position).getKey());
                        eventList.putExtra("CategoryId", post_key);
                        startActivity(eventList);
                    }
                });
            }
        };
        Log.d("TAG","" + adapter.getItemCount());
        recycler_event.setAdapter(adapter);

    }
}
