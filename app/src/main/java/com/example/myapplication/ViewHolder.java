package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

class ViewHolder extends RecyclerView.ViewHolder {
    public TextView dogName;

    ViewHolder(Context context, View itemView) {
        super(itemView);
        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION)
                {

                    String strText = dogName.getText().toString();
                   Toast.makeText(context,strText,Toast.LENGTH_SHORT).show();
                }
            }
        });
        dogName = itemView.findViewById(R.id.dogName);

    }
}