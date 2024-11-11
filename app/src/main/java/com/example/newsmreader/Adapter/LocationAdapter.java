package com.example.newsmreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.Models.LocationModel;
import com.example.newsmreader.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.ViewHolder> {

    Context context;
    ArrayList<LocationModel> list=new ArrayList<>();


    public LocationAdapter(Context context, ArrayList<LocationModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardtextview,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LocationModel obj=list.get(position);
        holder.txt.setText(obj.getName());
        if (AllstaticObj.WhichLocation.equalsIgnoreCase(obj.getId())){
            holder.lnr.setBackgroundResource(R.drawable.rey3);
            holder.txt.setTextColor(context.getResources().getColor(R.color.white));


        }else {
            holder.lnr.setBackgroundResource(R.drawable.reybg1);
            holder.txt.setTextColor(context.getResources().getColor(R.color.black));

        }





        holder.lnr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AllstaticObj.WhichLocation=obj.getId();

                notifyDataSetChanged();


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout lnr;
        TextView txt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lnr=itemView.findViewById(R.id.lnr);
            txt=itemView.findViewById(R.id.txt);

        }
    }


    public void setItems(ArrayList<LocationModel> list) {
        this.list = list;
        this.list.get(0).setId("0");
    }

}
