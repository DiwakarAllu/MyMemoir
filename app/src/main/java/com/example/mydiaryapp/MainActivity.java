package com.example.mydiaryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.example.mydiaryapp.Adapters.NotesListAdapter;
import com.example.mydiaryapp.Database.RoomDB;
import com.example.mydiaryapp.Models.Notes;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    RecyclerView recyclerView;
    NotesListAdapter notesListAdapter;
    List<Notes>notes = new ArrayList<>();
    RoomDB database;
    FloatingActionButton fab_add;
    SearchView searchView_home;
    Notes selectedNote;

    RelativeLayout homeFragment,calendarFragment,userFragment;
    private MeowBottomNavigation bottomNavigation;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView=findViewById(R.id.recycler_home);
        fab_add=findViewById(R.id.fab_add);
        searchView_home=findViewById(R.id.searchView_home);



        database=RoomDB.getInstance(this);
        notes=database.mainDAO().getAll();

        updateRecycler(notes);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,NotesTakerActivity.class);
                startActivityForResult(intent,101);
            }
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        // bootom nav
        //........... meow navbar...................
        bottomNavigation=findViewById(R.id.bottomNavigation);
        calendarFragment=findViewById(R.id.calendarFragment);
        homeFragment=findViewById(R.id.homeFragment);
        userFragment=findViewById(R.id.userFragment);

        // to show  home button first
        bottomNavigation.show(2,true);


        bottomNavigation.add(new MeowBottomNavigation.Model(1,R.drawable.calendar));
        bottomNavigation.add(new MeowBottomNavigation.Model(2,R.drawable.home));
        bottomNavigation.add(new MeowBottomNavigation.Model(3,R.drawable.person));

        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()){

                    case 1:
                        calendarFragment.setVisibility(View.VISIBLE);
                        homeFragment.setVisibility(View.GONE);
                        userFragment.setVisibility(View.GONE);

                        //for navigation bar color
                        getWindow().setNavigationBarColor(Color.parseColor("#68B984"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#68b984"));

                        //for statusbar color
                        getWindow().setStatusBarColor(Color.parseColor("#68b984"));

                        // for icon color change
                        bottomNavigation.setSelectedIconColor(Color.parseColor("#68b984"));

                        break;
                    case 2:
                        calendarFragment.setVisibility(View.GONE);
                        homeFragment.setVisibility(View.VISIBLE);
                        userFragment.setVisibility(View.GONE);

                        //for navigation bar color
                        getWindow().setNavigationBarColor(Color.parseColor("#E91E63"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#E91E63"));

                        //for statusbar color
                        getWindow().setStatusBarColor(Color.parseColor("#E91E63"));

                        // for icon color change
                        bottomNavigation.setSelectedIconColor(Color.parseColor("#E91E63"));

                        // did some animation effect at the top
                        //bottomNavigation.startAnimation(fade_in);

                        break;
                    case 3:
                        calendarFragment.setVisibility(View.GONE);
                        homeFragment.setVisibility(View.GONE);
                        userFragment.setVisibility(View.VISIBLE);

                        //for navigation bar color
                        getWindow().setNavigationBarColor(Color.parseColor("#B9E9FC"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#B9E9FC"));

                        //for statusbar color
                        getWindow().setStatusBarColor(Color.parseColor("#B9E9FC"));

                        // for icon color change
                        bottomNavigation.setSelectedIconColor(Color.parseColor("#B9E9FC"));

                        break;
                }

                return null;
            }
        });

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()){
                    case 1:
                        calendarFragment.setVisibility(View.VISIBLE);
                        homeFragment.setVisibility(View.GONE);
                        userFragment.setVisibility(View.GONE);
                        break;
                }
                return null;
            }
        });

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()){
                    case 2:
                        calendarFragment.setVisibility(View.GONE);
                        homeFragment.setVisibility(View.VISIBLE);
                        userFragment.setVisibility(View.GONE);
                        break;
                }
                return null;
            }
        });

        bottomNavigation.setOnShowListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {

                switch (model.getId()){
                    case 3:
                        calendarFragment.setVisibility(View.GONE);
                        homeFragment.setVisibility(View.GONE);
                        userFragment.setVisibility(View.VISIBLE);
                        break;
                }
                return null;
            }
        });


        //  tool bar and drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // to make the toolbar transparent
        toolbar.getBackground().setAlpha(0);

        drawerLayout=findViewById(R.id.drawerLayout);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void filter(String newText) {
        List<Notes>filteredList = new ArrayList<>();
        for(Notes singleNote:notes){
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
            ||singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())){
               filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==101){
            if (resultCode== Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().insert(new_notes);
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
        else if(requestCode==102){
            if (resultCode==Activity.RESULT_OK){
                Notes new_notes = (Notes) data.getSerializableExtra("note");
                database.mainDAO().update(new_notes.getID(),new_notes.getTitle(),new_notes.getNotes());
                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
            }
        }
    }

    private void updateRecycler(List<Notes> notes) {
        recyclerView.setHasFixedSize(true);
        //
       // recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        notesListAdapter=new NotesListAdapter(MainActivity.this,notes,notesClickListener);
        recyclerView.setAdapter(notesListAdapter);
    }

    private final NotesClickListener notesClickListener=new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this,NotesTakerActivity.class);
            intent.putExtra("old_note",notes);
            startActivityForResult(intent,102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = new Notes();
            selectedNote = notes;
            showPopup(cardView);
        }
    };

    private void showPopup(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this,cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.pin:
                if (selectedNote.isPinned()){
                    database.mainDAO().pin(selectedNote.getID(),false);
                    Toast.makeText(this, "Unpinned!", Toast.LENGTH_SHORT).show();
                }else{
                    database.mainDAO().pin(selectedNote.getID(),true);
                    Toast.makeText(this, "Pinned!", Toast.LENGTH_SHORT).show();
                }

                notes.clear();
                notes.addAll(database.mainDAO().getAll());
                notesListAdapter.notifyDataSetChanged();
                return true;

            case R.id.delete:
                database.mainDAO().delete(selectedNote);
                notes.remove(selectedNote);
                notesListAdapter.notifyDataSetChanged();
                Toast.makeText(this, "Deleted!", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return false;
        }



    }
}