package com.example.newsmreader.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.AllstaticObj;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.example.newsmreader.onClickPrintSetting;
import com.skydoves.elasticviews.ElasticCardView;

public class settingRecycler extends RecyclerView.Adapter<settingRecycler.ViewHolder> {


    int img[] = {
            R.drawable.ciontech,
            R.drawable.ciontech,
            R.drawable.ciontech,


    };


    String title[] = {
            "Ciontech",
            "Imin/Sunmi",
            "uiovo"


    };

    Context context;
    private onClickPrintSetting onClickPrintSetting;

    public settingRecycler(Context context, com.example.newsmreader.onClickPrintSetting onClickPrintSetting) {
        this.context = context;
        this.onClickPrintSetting = onClickPrintSetting;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_setting_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int cnt=position;

        if((AllstaticObj.whichPrinter-1)==cnt){
            holder.lnrbg.setBackgroundResource(R.drawable.strokebutton);
        }else {
            holder.lnrbg.setBackgroundResource(R.color.white);
        }
        holder.imageView.setImageResource(img[position]);
        holder.textView.setText(title[position]);
        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
           if(cnt==1) {
                    onClickPrintSetting.onClick(context, cnt);
                }else {
                    S_P.setPREFERENCESint(context,S_P.printSet,+cnt+1);
                    AllstaticObj.getPREFERENCES(context);
                    notifyDataSetChanged();
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        LinearLayout lnrbg;
        ElasticCardView btn;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lnrbg=itemView.findViewById(R.id.lnrbg);
            imageView=itemView.findViewById(R.id.imv);
            textView=itemView.findViewById(R.id.txt);
            btn=itemView.findViewById(R.id.btn);


        }
    }














}
