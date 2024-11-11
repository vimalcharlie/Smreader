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
import com.example.newsmreader.Models.StateMentModel;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.example.newsmreader.statement.onReprintListner;

import java.util.ArrayList;

public class StatementAdapter2 extends RecyclerView.Adapter<StatementAdapter2.ViewHolder>{


    Context context;
    ArrayList<StateMentModel> list=new ArrayList<>();
    private onReprintListner onReprintListner;


    public StatementAdapter2(Context context, ArrayList<StateMentModel> list,onReprintListner onReprintListner) {
        this.context = context;
        this.list=list;
        this.onReprintListner=onReprintListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cardstatement2,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final StateMentModel obj=list.get(position);
        holder.txt_name.setText(obj.getConsumer());
        holder.txt_consumer.setText(obj.getConsumer_no());
        holder.txt1.setText(S_P.R+obj.getTotal());
        holder.txt2.setText(S_P.R+obj.getReceived());
        holder.root_Rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onReprintListner.onReprint(obj.getId());

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class  ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_name,txt_consumer,txt1,txt2;
        RelativeLayout root_Rl;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_consumer=itemView.findViewById(R.id.txt_consumer);
            txt1=itemView.findViewById(R.id.txt1);
            txt2=itemView.findViewById(R.id.txt2);
            root_Rl=itemView.findViewById(R.id.root_Rl);
        }
    }


    public void setItems(ArrayList<StateMentModel> list) {
        this.list = list;
    }
}
