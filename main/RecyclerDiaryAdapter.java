package com.example.mystickynotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerDiaryAdapter extends RecyclerView.Adapter<RecyclerDiaryAdapter.MyHolder> {

    Context context;
    ArrayList<DairyData> arrDairy;

    RecyclerDiaryAdapter(Context context, ArrayList<DairyData> arrDairy){
        this.context=context;
        this.arrDairy=arrDairy;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view= LayoutInflater.from(context).inflate(R.layout.item_design,parent,false);
       MyHolder myHolder =new MyHolder(view);
       return myHolder;
      // return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.imgMood.setImageResource(arrDairy.get(position).img);
        holder.dayDescription.setText(arrDairy.get(position).desc);
        holder.eventTitle.setText(arrDairy.get(position).title1);
        holder.dayOfDiary.setText(arrDairy.get(position).date);
    }

    @Override
    public int getItemCount() {
        return arrDairy.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder{
        TextView dayDescription,eventTitle,dayOfDiary;
        ImageView imgMood;
        public MyHolder(View itemView){
            super(itemView);

            dayDescription=itemView.findViewById(R.id.dayDescription);
            eventTitle=itemView.findViewById(R.id.eventTitle);
            imgMood=itemView.findViewById(R.id.imgMood);
            dayOfDiary=itemView.findViewById(R.id.dayOfDiary);

        }
    }

}
