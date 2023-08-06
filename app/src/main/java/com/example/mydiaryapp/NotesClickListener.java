package com.example.mydiaryapp;

import androidx.cardview.widget.CardView;

import com.example.mydiaryapp.Models.Notes;

public interface NotesClickListener {
    void onClick(Notes notes);
    void onLongClick(Notes notes, CardView cardView);
}
