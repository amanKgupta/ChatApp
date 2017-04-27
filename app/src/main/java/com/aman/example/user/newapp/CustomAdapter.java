package com.aman.example.user.newapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by User on 26-04-2017.
 */

public class CustomAdapter extends ArrayAdapter {
    private final Context context;
    private final ArrayList<Items> itemsArrayList;

    public CustomAdapter(@NonNull Context context ,ArrayList<Items> itemsArrayList) {
        super(context,R.layout.chat_item,itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater =(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.chat_item,parent,false);
        TextView send =(TextView)view.findViewById(R.id.textView);
        TextView rece =(TextView)view.findViewById(R.id.textView1);
        rece.setText(itemsArrayList.get(position).getSendmsg());
        if (send.getText()==null)
        {
            RelativeLayout layout1 = (RelativeLayout)view.findViewById(R.id.relativeLayout2);
            layout1.setVisibility(View.GONE);

        }
        send.setText(itemsArrayList.get(position).getReceivedmsg());

        return view;
    }
}
