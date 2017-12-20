package com.example.jhonsalya.eis;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
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
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jhonsalya.eis.Model.Category;
import com.example.jhonsalya.eis.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        ExpandableListView.OnGroupClickListener,
        ExpandableListView.OnChildClickListener{

    private ExpandableListView sidebarList;
    private SidebarAdapter mAdapter;
    private DrawerLayout drawer;
    private List<String> listParentSidebar;
    private List<String> sortChild;
    private List<String> manageChild;
    private HashMap<String, List<String>> listChildSidebar;

    //drawer for navigation
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    //end of drawer

    private RecyclerView mEventList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    MaterialSearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //setting navigation view

        /*mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();*/

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        setListData(); // load data
        mAdapter = new SidebarAdapter(this, listParentSidebar, listChildSidebar); // init adapter

        // initialize expandable list
        sidebarList = (ExpandableListView) findViewById(R.id.sidebar_list);
        sidebarList.setAdapter(mAdapter);
        sidebarList.setOnGroupClickListener(this);
        sidebarList.setOnChildClickListener(this);

        /*getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);*/

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

                //Detail Event
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
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
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /*public static class EventViewHolder extends RecyclerView.ViewHolder{

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
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setListData() {
        listParentSidebar = new ArrayList<String>();
        listChildSidebar = new HashMap<String, List<String>>();

        // Adding parent data
        listParentSidebar.add(Constant.S_POS_ACCOUNT, "Account");
        listParentSidebar.add(Constant.S_POS_CATEGORY, "Category");
        listParentSidebar.add(Constant.S_POS_SORT, "Sort");
        listParentSidebar.add(Constant.S_POS_MANAGE, "Manage");
        listParentSidebar.add(Constant.S_POS_LOGOUT, "Logout");

        // Adding child data
        sortChild = new ArrayList<String>();
        sortChild.add("Sort Alphabetically");
        sortChild.add("Sort by Time");

        //Child Manage
        manageChild = new ArrayList<String>();
        manageChild.add("Add Event");
        manageChild.add("Add Category");

        // Set child to particular parent
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_ACCOUNT), new ArrayList<String>()); // adding empty child
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_CATEGORY), new ArrayList<String>()); // adding empty child
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_SORT), sortChild);
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_MANAGE), manageChild);
        listChildSidebar.put(listParentSidebar.get(Constant.S_POS_LOGOUT), new ArrayList<String>()); // adding empty child
    }

    // Handling click on child item
    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (groupPosition == 2) { // index program
            showToast(sortChild.get(childPosition));
        }
        if (groupPosition == 3){
            showToast(manageChild.get(childPosition));
            if(childPosition == 0){
                Intent intent = new Intent(MainActivity.this, AddEventActivity.class);
                startActivity(intent);
            }
            else{
                Intent intent = new Intent(MainActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        }

        drawer.closeDrawer(GravityCompat.START); // close drawer
        return true;
    }

    // Handling click on parent item
    @Override
    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
        // Flag for group that have child, then parent will expand or collapse
        boolean isHaveChild = false;

        switch (groupPosition) {
            case Constant.S_POS_ACCOUNT:
                showToast("Login");
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

            case Constant.S_POS_CATEGORY:
                showToast("Event Category");
                Intent categoryIntent = new Intent(MainActivity.this, CategoryActivity.class);
                startActivity(categoryIntent);
                break;

            case Constant.S_POS_SORT:
                isHaveChild = true; // have child

                if (parent.isGroupExpanded(groupPosition)) // if parent expanded
                    parent.collapseGroup(groupPosition); // collapse parent
                else
                    parent.expandGroup(groupPosition); // expand parent
                break;

            case Constant.S_POS_MANAGE:
                isHaveChild = true; // have child

                if (parent.isGroupExpanded(groupPosition)) // if parent expanded
                    parent.collapseGroup(groupPosition); // collapse parent
                else
                    parent.expandGroup(groupPosition); // expand parent
                break;

            case Constant.S_POS_LOGOUT:
                showToast("Logout");
                mAuth.signOut();
                break;

            default:
                break;
        }

        if (!isHaveChild) { // if don't have child, close drawer
            drawer.closeDrawer(GravityCompat.START);
        }

        return true;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /*
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.nav_account){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_category){
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivity(intent);
        }
        else if(id == R.id.nav_sort){

        }
        else if(id == R.id.nav_logout){
            mAuth.signOut();
        }

        return true;
    }*/
}
