package com.example.newsmreader.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.R;
import com.example.newsmreader.reading.OnAverageChangeListner;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.ViewHolder>{
    String month[]={"1","2","3","4","5","6","7","8","9","10","11","12"};
     Context context;
    OnAverageChangeListner  listner;

    public MonthAdapter(Context context ,OnAverageChangeListner listner) {
        this.context = context;
        this.listner=listner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.monthtext,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.txt_number.setText(month[position]);
        if (AllstaticObj.avgmonth.equalsIgnoreCase(month[position])){

            holder.txt_number.setTextColor(Color.parseColor("#000000"));
            holder.bglnr.setBackground(context.getResources().getDrawable(R.drawable.dot2));
        }else {

            holder.txt_number.setTextColor(Color.parseColor("#ffffff"));
            holder.bglnr.setBackgroundColor(Color.parseColor("#00FFFFFF"));

        }
        holder.bglnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AllstaticObj.avgmonth=month[position];
                listner.OnChange();
                notifyDataSetChanged();


            }
        });

    }

    @Override
    public int getItemCount() {
        return month.length;
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout bglnr;
        TextView txt_number;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bglnr=itemView.findViewById(R.id.lnr);
            txt_number=itemView.findViewById(R.id.txtone);
        }
    }
}
