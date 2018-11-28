package com.example.huni.weekendplaner.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.huni.weekendplaner.Sidebar.IntrestsActivity;
import com.example.huni.weekendplaner.Login.LoginActivity;
import com.example.huni.weekendplaner.Sidebar.ProfilActivity;
import com.example.huni.weekendplaner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private int valtozo23123;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    private DrawerLayout mdrawerlayout;
    private ActionBarDrawerToggle mtoogle;
    NavigationView navigationView;
    FirebaseDatabase database;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        mdrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mtoogle = new ActionBarDrawerToggle(this,mdrawerlayout,R.string.action_open,R.string.action_close);
        mdrawerlayout.addDrawerListener(mtoogle);
        mtoogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //database
        database = FirebaseDatabase.getInstance();
        ref = database.getReference("Event");

        List<ListDataEvent> listDataEvents =  getDataFromDatabase();
        //recyclerView
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter(listDataEvents);
        recyclerView.setAdapter(adapter);
    }


    public List<ListDataEvent> getDataFromDatabase(){
        final List<ListDataEvent> list = new ArrayList<>();
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    ListDataEvent Event = dataSnapshot1.getValue(ListDataEvent.class);
                    ListDataEvent listDataEvent = new ListDataEvent();
                    assert Event != null;
                    String address = Event.getAddress();
                    String author = Event.getAuthor();
                    String descriptionOfEvent = Event.getDescriptionOfEvent();
                    String start_date = Event.getStart_date();
                    String end_date = Event.getEnd_date();
                    String nameOfEvent = Event.getNameOfEvent();
                    listDataEvent.setAddress(address);
                    listDataEvent.setAuthor(author);
                    listDataEvent.setDescriptionOfEvent(descriptionOfEvent);
                    listDataEvent.setStart_date(start_date);
                    listDataEvent.setEnd_date(end_date);
                    listDataEvent.setNameOfEvent(nameOfEvent);
                    list.add(listDataEvent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Gone wrong",Toast.LENGTH_LONG).show();
            }
        });
                return list;
    }

    public boolean onOptionsItemSeleted(MenuItem itme){
        if(mtoogle.onOptionsItemSelected(itme)){
            return true;
        }
        return super.onOptionsItemSelected(itme);
    }

    public boolean onNavigationItemSelected(MenuItem menuItem){
        menuItem.setChecked(true);

        switch (menuItem.getItemId()){
            case R.id.sidebar_Profile:
                startActivity(new Intent(getApplicationContext(),ProfilActivity.class));
                break;

            case R.id.sidebar_Intrests:
                startActivity(new Intent(getApplicationContext(), IntrestsActivity.class));
                break;

            case R.id.sidebar_Logout:
                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = settings.edit();
                editor.remove("username");

                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        return super.onOptionsItemSelected(item);
    }
}
