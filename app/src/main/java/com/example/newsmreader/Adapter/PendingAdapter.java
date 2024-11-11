package com.example.newsmreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.Models.StamentreadModel;
import com.example.newsmreader.R;
import com.example.newsmreader.statement.onReprintListner;

import java.util.ArrayList;

public class PendingAdapter extends RecyclerView.Adapter<PendingAdapter.ViewHolder>{
    Context context;
    ArrayList<StamentreadModel> list=new ArrayList<>();
    public PendingAdapter(Context context, ArrayList<StamentreadModel> list) {
        this.context = context;
        this.list = list;
    }
    public class  ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name,txt_consumer;
        RelativeLayout root_Rl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_consumer=itemView.findViewById(R.id.txt_consumer);
            root_Rl=itemView.findViewById(R.id.root_Rl);
        }
    }

    public void setItems(ArrayList<StamentreadModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.pendingcrad,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final   StamentreadModel  obj=list.get(position);
        holder.txt_name.setText(obj.getConsumer());
        holder.txt_consumer.setText(obj.getConsumer_no());


    }

    @Override
    public int getItemCount() {

        return list.size();
    }


}
