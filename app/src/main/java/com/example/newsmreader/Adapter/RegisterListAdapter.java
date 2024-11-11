package com.example.newsmreader.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.R;
import com.example.newsmreader.RegisterPage;

import java.util.ArrayList;

public class RegisterListAdapter extends RecyclerView.Adapter<RegisterListAdapter.ViewHolder> {

    Context  context;
    ArrayList<CustomerModel> list=new ArrayList<>();

    public RegisterListAdapter(Context context, ArrayList<CustomerModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.readingcard_1,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.imv_location.setVisibility(View.GONE);
        holder.txt_name.setText(list.get(position).getConsumer());
        holder.txt_consumer_no.setText(list.get(position).getConsumer_no());
        holder.root_Rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, RegisterPage.class);
                intent.putExtra("key1",list.get(position));
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name,txt_consumer_no;
        ImageView imv_location;
        RelativeLayout root_Rl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_consumer_no=itemView.findViewById(R.id.txt_consumer_no);
            imv_location=itemView.findViewById(R.id.imv_location);
            root_Rl=itemView.findViewById(R.id.root_Rl);
        }
    }

    public void setItems(ArrayList<CustomerModel> list){


        this.list=list;
    }
}
