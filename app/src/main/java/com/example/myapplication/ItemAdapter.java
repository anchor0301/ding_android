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

public class Adapter extends RecyclerView.Adapter<ViewHolder> {
    ArrayList<Item> items = new ArrayList<Item>();

    Context context;

    int lastPosition = -1;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.item_layout, viewGroup, false);

        context = viewGroup.getContext();

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {

        if(viewHolder.getAdapterPosition() > lastPosition){

            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in_row);
            ((ViewHolder) viewHolder).itemView.startAnimation(animation);

            Item item = items.get(position);
            viewHolder.setItem(item);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    public void addItem(Item item){

        items.add(item);
    }

    /**
     * 아이템 추가
     * @param position 등록 위치
     * @param item 아이템
     */
    public void addItem(int position, Item item){

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

        TextView title_view;
        TextView description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title_view = itemView.findViewById(R.id.title_text);
            description = itemView.findViewById(R.id.desc_text);
        }

        public void setItem(Item item){

            title_view.setText(item.getTitle());
            description.setText(item.getDescription());
        }
    }
}