package com.example.newsmreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.R;
import com.example.newsmreader.billing.onReprintListner;

import java.util.ArrayList;

public class BillFragAdapter2 extends  RecyclerView.Adapter<BillFragAdapter2.ViewHolder>{

    Context context;
    ArrayList<CustomerModel> list=new ArrayList<>();

    private onReprintListner onReprintListner;


    public BillFragAdapter2(Context context, ArrayList<CustomerModel> list,onReprintListner onReprintListner) {
        this.context = context;
        this.list = list;
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
        CustomerModel model=list.get(position);
        holder.txt_name.setText(list.get(position).getConsumer());
        holder.txt_consumer_no.setText(list.get(position).getConsumer_no());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onReprintListner.onReprint(model.getCustomerID());




            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder {


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
