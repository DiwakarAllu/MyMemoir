package com.example.mystickynotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MyDiaryEntryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_diary_entry);

        // send the dairy entry and title and date to next intent
        TextView descriptioninput = findViewById(R.id.descriptioninput);
        TextView titleinput=findViewById(R.id.titleinput);
        TextView todayDate=findViewById(R.id.todayDate);

        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyDiaryEntryActivity.this,MainActivity.class);
                intent.putExtra("descriptioninput",descriptioninput.getText().toString());
                intent.putExtra("titleinput",titleinput.getText().toString());
                intent.putExtra("todayDate",todayDate.getText().toString());
                startActivity(intent);
            }
        });



        TextView date=findViewById(R.id.todayDate);
        TextView mood = findViewById(R.id.mood);


        // get the current date on the to from calendar section
        Intent intent1 = getIntent();
        date.setText(intent1.getStringExtra("currentDate").toString());

        // to show the date picker  on clicking the date
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Calendar calendar = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        MyDiaryEntryActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                // Set the selected date to the text view
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                                calendar.set(year, month, day);
                                date.setText(dateFormat.format(calendar.getTime()));
                            }
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.show();

            }
        });

        // to add emoji according to the mood of the user on the day
        mood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(MyDiaryEntryActivity.this);
                builder.setTitle("How is your Day? ");
               // builder.setIcon(R.drawable.thumb_up);
                builder.setMessage("🥰\t 😭 \t 😀 \n😫  \t 😵 \t ‍🤪");
                builder.create();
                builder.show();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater= getMenuInflater();
        menuInflater.inflate(R.menu.diary_menu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.save:
                Toast.makeText(this, "😅", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}