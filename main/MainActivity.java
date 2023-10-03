package com.example.mystickynotes;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ArrayList<DairyData> arrDairy=new ArrayList<>();

    private DrawerLayout drawerLayout;
    private MeowBottomNavigation bottomNavigation;

    private RelativeLayout settings,home,user;

    private mySQLiteDBHandler dbHandler;
    private CalendarView calendarView;
    private String selectedDate;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //
//        calendarView=findViewById(R.id.calendar);
//
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
//
//                selectedDate=Integer.toString(year) + Integer.toString(month) + Integer.toString(dayOfMonth);
//            }
//        });
// // ctrl + alt+T
//        try {
//            dbHandler= new mySQLiteDBHandler(this,"CalendarDatabase",null,1);
//            sqLiteDatabase=dbHandler.getWritableDatabase();
//            sqLiteDatabase.execSQL("CREATE TABLE DiaryCalendar(Date TEXT,DiaryTitle TEXT,Diary TEXT)");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }



// Recycler view
        RecyclerView recyclerView=findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DairyData model = new DairyData(R.drawable.home,"A","3435","12");
        arrDairy.add(model);
        // or
        arrDairy.add(new DairyData(R.drawable.calendar,"B","1234","12"));

//        arrDairy.add(new DairyData(R.drawable.person,"B","1234"));
//        arrDairy.add(new DairyData(R.drawable.arrow_back,"B","1234"));
//        arrDairy.add(new DairyData(R.drawable.book,"B","1234"));
//        arrDairy.add(new DairyData(R.drawable.developer,"B","1234"));
//        arrDairy.add(new DairyData(R.drawable.diary_logo,"B","1234"));
//        arrDairy.add(new DairyData(R.drawable.lovablemoon,"B","1234"));
//        arrDairy.add(new DairyData(R.drawable.moon,"B","1234"));
//        arrDairy.add(new DairyData(R.drawable.settings,"B","1234"));


        // Get the Intent that launched this activity
        Intent intent = getIntent();
        String title = intent.getStringExtra("titleinput");
        String description = intent.getStringExtra("descriptioninput");
        String date = intent.getStringExtra("todayDate");

        addData(title,description,date);

// Create a new item object with the data
//        Item item = new Item(title, description, date);

// Add the item to the RecyclerView adapter
//        adapter.addItem(item);
        RecyclerDiaryAdapter adapter=new RecyclerDiaryAdapter(this,arrDairy);
        recyclerView.setAdapter(adapter);

// to show/hide FloatingActionButton when scrolling RecyclerView
        FloatingActionButton mActionButton = (FloatingActionButton) findViewById(R.id.fab);
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    //dy > 0: scroll up; dy < 0: scroll down
                    if (dy > 0) mActionButton.hide();
                    else mActionButton.show();
                    super.onScrolled(recyclerView, dx, dy);
                }
            });

            // pulse animation actually not same but similar to that
//        Animation pulseAnim = AnimationUtils.loadAnimation(this, R.anim.pulse_anim);
//        mActionButton.startAnimation(pulseAnim);

        ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(mActionButton, "alpha", 0.5f, 1f);
        alphaAnimator.setRepeatCount(ValueAnimator.INFINITE);
        alphaAnimator.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(mActionButton, "scaleX", 1f, 1.2f);
        scaleXAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleXAnimator.setRepeatMode(ValueAnimator.REVERSE);

        ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(mActionButton, "scaleY", 1f, 1.2f);
        scaleYAnimator.setRepeatCount(ValueAnimator.INFINITE);
        scaleYAnimator.setRepeatMode(ValueAnimator.REVERSE);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(alphaAnimator, scaleXAnimator, scaleYAnimator);
        animatorSet.setDuration(1000);

        animatorSet.start();

        // next intent== Diary entry intent  with fab button
        mActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get the current date
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
                String currentDate = dateFormat.format(calendar.getTime());

                // Start the diary activity with the current date as an extra
                Intent intent = new Intent(MainActivity.this, MyDiaryEntryActivity.class);
                intent.putExtra("currentDate", currentDate);
                startActivity(intent);
            }
        });

        // to change the fab button color
       // mActionButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.color_dark1)));


        //.......... to make underline in calendar section.........
        TextView textView = findViewById(R.id.eventTitle ) ;
        SpannableString content = new SpannableString( "Today,Apr 11,2023" ) ;
        content.setSpan( new UnderlineSpan() , 0 , content.length() , 0 ) ;
        textView.setText(content) ;


// just testing purpose
        ScaleAnimation fade_in = new ScaleAnimation(0f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        fade_in.setDuration(1000);
        fade_in.setFillAfter(true);


//  tool bar and drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // to make the toolbar transparent
        toolbar.getBackground().setAlpha(0);

        drawerLayout=findViewById(R.id.drawerLayout);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.nav_open,R.string.nav_close);

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState==null){
            //getSupportFragmentManager().beginTransaction().replace()
            //navigationView.setCheckedItem(R.id.nav);
        }

        //........... meow navbar...................
        bottomNavigation=findViewById(R.id.bottomNavigation);
        settings=findViewById(R.id.settings);
        home=findViewById(R.id.home);
        user=findViewById(R.id.person);

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
                        settings.setVisibility(View.VISIBLE);
                        home.setVisibility(View.GONE);
                        user.setVisibility(View.GONE);

                        //for navigation bar color
                        getWindow().setNavigationBarColor(Color.parseColor("#68B984"));
                        bottomNavigation.setBackgroundBottomColor(Color.parseColor("#68b984"));

                        //for statusbar color
                        getWindow().setStatusBarColor(Color.parseColor("#68b984"));

                        // for icon color change
                        bottomNavigation.setSelectedIconColor(Color.parseColor("#68b984"));

                        break;
                    case 2:
                        settings.setVisibility(View.GONE);
                        home.setVisibility(View.VISIBLE);
                        user.setVisibility(View.GONE);

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
                        settings.setVisibility(View.GONE);
                        home.setVisibility(View.GONE);
                        user.setVisibility(View.VISIBLE);

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
                        settings.setVisibility(View.VISIBLE);
                        home.setVisibility(View.GONE);
                        user.setVisibility(View.GONE);
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
                        settings.setVisibility(View.GONE);
                        home.setVisibility(View.VISIBLE);
                        user.setVisibility(View.GONE);
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
                        settings.setVisibility(View.GONE);
                        home.setVisibility(View.GONE);
                        user.setVisibility(View.VISIBLE);
                        break;
                }
                return null;
            }
        });

    }




    // nav drawer implementations
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.nav_about:

                    break;

                case R.id.nav_review:
                    Toast.makeText(this, "Themes", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_socialMedia:
                    Toast.makeText(this, "Website", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_share:

                    //Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name is Unify");
                    String shareMessage= "\nLet me recommend you this application\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose your ❤ one️"));
                    break;

            }
           drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
        super.onBackPressed();
        }
    }


//    public void InsertDatabase(View view){
//        ContentValues contentValues=new ContentValues();
//        contentValues.put("Date",selectedDate);
//        contentValues.put("DiaryTitle",);
//        contentValues.put("Diary",);
//        sqLiteDatabase.insert("DiaryCalendar",null,contentValues);
//
//    }
//
//    public void ReadDatabase(View view){
//        String query = "Select *from DiaryCalendar where Date = "+selectedDate;
//        Cursor cursor =sqLiteDatabase.rawQuery(query,null);
//        cursor.moveToFirst();
//        // editText.setText(cursor.getString(0));
//    }

    public void addData(String title,String description,String date){
        arrDairy.add(new DairyData(R.drawable.book,title,description,date));
    }
}