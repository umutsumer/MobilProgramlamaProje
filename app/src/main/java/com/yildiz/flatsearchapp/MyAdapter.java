package com.yildiz.flatsearchapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<NoticeItem> noticeItemArrayList;
    private AdapterView.OnItemClickListener mItem;
    public MyAdapter(Context context,ArrayList<NoticeItem> noticeItems){

    this.context = context;
    this.noticeItemArrayList = noticeItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.feed_item,parent,false);
        return new MyViewHolder(v);

    }



    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NoticeItem noticeItem = noticeItemArrayList.get(position);
        holder.name.setText(noticeItem.name);
        holder.content.setText(noticeItem.content);
        holder.timePassed.setText(noticeItem.timePassed);

    }

    @Override
    public int getItemCount() {
        return noticeItemArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name,timePassed,content;


        public MyViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.itemUserText);
            content = itemView.findViewById(R.id.itemContentText);
            timePassed= itemView.findViewById(R.id.itemDateText);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Toast.makeText(context.getApplicationContext(),String.valueOf(getAdapterPosition()),Toast.LENGTH_LONG).show();
                }
            });

        }
    }
}


