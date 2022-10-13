package com.example.myapplication.ListDog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

class ViewHolder extends RecyclerView.ViewHolder {
    public TextView dogName;
    public TextView breed;
    public TextView lastNum;

    ViewHolder(Context context, View itemView) {
        super(itemView);

        dogName = itemView.findViewById(R.id.dogName);
        breed = itemView.findViewById(R.id.breed);
        lastNum = itemView.findViewById(R.id.lastNum);

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
    }
}