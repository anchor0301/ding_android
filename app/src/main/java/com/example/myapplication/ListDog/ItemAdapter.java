package com.example.myapplication.ListDog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    ArrayList<DogItem> items = new ArrayList<DogItem>();

    Context context;

    int lastPosition = -1;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.dog_item_list, viewGroup, false);

        context = viewGroup.getContext();

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if(viewHolder.getAdapterPosition() > lastPosition){


            DogItem item = items.get(position);
            viewHolder.setItem(item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(DogItem item){

        items.add(item);
    }

    /**
     * 아이템 추가
     * @param position 등록 위치
     * @param item 아이템
     */
    public void addItem(int position, DogItem item){

        items.add(position, item);
    }

    /**
     * 아이템 삭제
     * @param position 삭제 위치
     */
    public void removeItem(int position){

        items.remove(position);
    }

    public void removeAllItem(){
        items.clear();
    }



    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView DogName;
        TextView breed;
        TextView lastNum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            DogName = itemView.findViewById(R.id.dogName);
            breed = itemView.findViewById(R.id.breed);
            lastNum = itemView.findViewById(R.id.lastNum);
        }

        public void setItem(DogItem item){

            DogName.setText(item.getDogName());
            breed.setText(item.getBreed());
            lastNum.setText(item.getLastNum());
        }
    }
}