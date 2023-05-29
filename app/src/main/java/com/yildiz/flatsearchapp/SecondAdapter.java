package com.yildiz.flatsearchapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserItem> userItemArrayList;
    String[] status = new String[]{"Ev/Ev Arkadaşı Aramıyor","Ev Arıyor","Ev Arkadaşı Arıyor"};

    private AdapterView.OnItemClickListener mItem;
    public SecondAdapter(Context context,ArrayList<UserItem> userItems){

        this.context = context;
        this.userItemArrayList = userItems;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.user_item,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        UserItem userItem = userItemArrayList.get(position);
        holder.nameSurname.setText(userItem.name+" "+userItem.surname);
        holder.userStatus.setText(status[userItem.status]);
        holder.userDepartment.setText(userItem.department);


    }

    @Override
    public int getItemCount() {
        return userItemArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nameSurname,userStatus,userDepartment;
        //ImageView pictureSmall;


        public MyViewHolder(View itemView){
            super(itemView);
            nameSurname = itemView.findViewById(R.id.userItemNameSurnameText); // changed
            userStatus = itemView.findViewById(R.id.userStatusText);
            userDepartment = itemView.findViewById(R.id.userBolumText);
            //pictureSmall = itemView.findViewById(R.id.userImageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Toast.makeText(context.getApplicationContext(),userItemArrayList.get(getAdapterPosition()).name,Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context.getApplicationContext(),FilteredUserActivity.class);
                    UserItem usertoSend = userItemArrayList.get(getAdapterPosition());
                    intent.putExtra("userItem",usertoSend);
                    context.startActivity(intent); // TODO
                }
            });

        }
    }
}


