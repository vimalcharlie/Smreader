package com.example.newsmreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.Models.ComplaintModel;
import com.example.newsmreader.Models.Complaint_List;
import com.example.newsmreader.R;

import java.util.ArrayList;

public class ComplaintAdapter extends RecyclerView.Adapter<ComplaintAdapter.ViewHolder>{

    Context context;
    ArrayList<ComplaintModel> list=new ArrayList<>();

    public ComplaintAdapter(Context context, ArrayList<ComplaintModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardcompalint,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ComplaintModel model=list.get(position);
        holder.txt2.setText(model.getService_date());
        holder.txt_consumer.setText(model.getConsumer_no());
        holder.txt1.setText(model.getComplaint());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_consumer,txt1,txt2;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt1=itemView.findViewById(R.id.txt1);
            txt_consumer=itemView.findViewById(R.id.txt_consumer);
            txt2=itemView.findViewById(R.id.txt2);
        }
    }

    public void setList(ArrayList<ComplaintModel> list){

        this.list=list;
        notifyDataSetChanged();

    }
}
