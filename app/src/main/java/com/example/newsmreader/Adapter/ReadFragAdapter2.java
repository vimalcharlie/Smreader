package com.example.newsmreader.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.PrintClass;
import com.example.newsmreader.R;
import com.example.newsmreader.reading.ReadProfile;
import com.example.newsmreader.reading.onReprintListner;

import java.util.ArrayList;

public class ReadFragAdapter2  extends RecyclerView.Adapter<ReadFragAdapter2.ViewHolder>{


    Context context;
    ArrayList<CustomerModel> list=new ArrayList<>();
    PrintClass printClass;
    Activity activity;
    private onReprintListner onReprintListner;
    public ReadFragAdapter2(Context context, ArrayList<CustomerModel> list, Activity activity,onReprintListner onReprintListner) {
        this.context = context;
        this.list = list;
        printClass=new PrintClass();
        this.activity=activity;
        this.onReprintListner=onReprintListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.readingcard_2,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerModel customerModel=list.get(position);
        holder.txt_name.setText(list.get(position).getConsumer());
        holder.txt_consumer_no.setText(list.get(position).getConsumer_no());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             onReprintListner.onReprint(customerModel.getCustomerID());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView txt_name,txt_consumer_no;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.root_Rl);
            txt_consumer_no=itemView.findViewById(R.id.txt_consumer_no);
            txt_name=itemView.findViewById(R.id.txt_name);
        }




    }


    public void setItems(ArrayList<CustomerModel> list) {
        this.list = list;
    }
}
