package com.example.newsmreader.Adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.newsmreader.Login;
import com.example.newsmreader.Models.CustomerModel;
import com.example.newsmreader.Models.StamentreadModel;
import com.example.newsmreader.R;
import com.example.newsmreader.S_P;
import com.example.newsmreader.reading.ReadProfile;
import com.skydoves.elasticviews.ElasticButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Locale;

public class ReadFragAdapter extends RecyclerView.Adapter<ReadFragAdapter.ViewHolder>{

    Context context;
    ArrayList<CustomerModel> list=new ArrayList<>();


    public ReadFragAdapter(Context context,ArrayList<CustomerModel> list) {
        this.context = context;
        this.list=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.readingcard_1,parent,false);



        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CustomerModel cModel=list.get(position);
        holder.txt_name.setText(cModel.getConsumer());
        holder.txt_consumer_no.setText(cModel.getConsumer_no());

        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cModel.getReadingexists().equalsIgnoreCase("1")){



                    View dialogView = LayoutInflater.from(context).inflate(R.layout.alertbox3,null, false);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setView(dialogView);
                    AlertDialog dialog = builder.create();
                    TextView textView=dialogView.findViewById(R.id.txt);
                    textView.setText("Reading  already  taken for this\nmonth?");
                    ElasticButton bt_yes = dialogView.findViewById(R.id.bt_yes);
                    ElasticButton bt_no = dialogView.findViewById(R.id.bt_no);
                    bt_yes.setText("Continue");
                    bt_no.setText("Cancel");
                    bt_yes.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            Intent i = new Intent(context,ReadProfile.class);
                            i.putExtra("key1", cModel.getConsumer_no());
                            context.startActivity(i);
                            dialog.dismiss();



                        }
                    });
                    bt_no.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {


                            dialog.cancel();


                        }
                    });


                    dialog.show();
                    dialog.setCancelable(false);








                }else {
                    Intent i = new Intent(context,ReadProfile.class);
                    i.putExtra("key1", cModel.getConsumer_no());
                    context.startActivity(i);
                }








            }
        });
        holder.imv_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double latitude = Double.parseDouble(cModel.getLatitude());
                double longitude = Double.parseDouble(cModel.getLongitude());
                String url = "https://www.google.com/maps?q=" + latitude + "," + longitude;
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

                try {
                   context.startActivity(mapIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No app found to handle the request", Toast.LENGTH_SHORT).show();
                }

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
        ImageView imv_location;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.root_Rl);
            txt_name=itemView.findViewById(R.id.txt_name);
            txt_consumer_no=itemView.findViewById(R.id.txt_consumer_no);
            imv_location=itemView.findViewById(R.id.imv_location);
        }
    }



    public void setItems(ArrayList<CustomerModel> list) {
        this.list = list;
    }
}
