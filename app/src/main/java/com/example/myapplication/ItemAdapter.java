package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    ArrayList<DogListItem> items = new ArrayList<DogListItem>();

    Context context;

    int lastPosition = -1;
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.dog_item_list, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        DogListItem item = items.get(position);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(DogListItem item){

        items.add(item);
    }

    /**
     * 아이템 추가
     * @param position 등록 위치
     * @param item 아이템
     */
    public void addItem(int position, DogListItem item){

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

        TextView dogName;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dogName = itemView.findViewById(R.id.dogName);
            //breed = itemView.findViewById(R.id.breed);
        }

        public void setItem(DogListItem item){

            dogName.setText(item.getDogName());
            //description.setText(item.getBreed());
        }
    }
}