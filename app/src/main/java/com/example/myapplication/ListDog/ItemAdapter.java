package com.example.myapplication.ListDog;

import android.animation.ValueAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;

import java.util.ArrayList;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {
    ArrayList<DogItem> items = new ArrayList<DogItem>();

    private SparseBooleanArray selectedItems = new SparseBooleanArray();
    Context context;

    // 직전에 클릭됐던 Item의 position
    private int prePosition = -1;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.dog_item_list, viewGroup, false);

        context = viewGroup.getContext();

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // final로 선언해야 체크박스의 체크 상태값(T/F)이 바뀌지 않는다
        final DogItem item = items.get(position);
        // 먼저 체크박스의 리스너를 null로 초기화한다
        holder.lunchBox.setOnCheckedChangeListener(null);
        holder.dinnerBox.setOnCheckedChangeListener(null);

        // 모델 클래스의 getter로 체크 상태값을 가져온 다음, setter를 통해 이 값을 아이템 안의 체크박스에 set한다
        holder.lunchBox.setChecked(item.getLunchBoxSelected());
        holder.dinnerBox.setChecked(item.getDinnerBoxSelected());

        // 체크박스의 상태값을 알기 위해 리스너 부착
        holder.lunchBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 여기의 item은 final 키워드를 붙인 모델 클래스의 객체와 동일하다
                item.setLunchBoxSelected(isChecked);
            }
        });

        // 체크박스의 상태값을 알기 위해 리스너 부착
        holder.dinnerBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 여기의 item은 final 키워드를 붙인 모델 클래스의 객체와 동일하다
                item.setDinnerBoxSelected(isChecked);
            }
        });


        ViewHolder viewHolderMovie = (ViewHolder) holder;
        viewHolderMovie.onBind(items.get(position), position, selectedItems);
        viewHolderMovie.setOnViewHolderItemClickListener(new OnViewHolderItemClickListener() {
            @Override
            public void onViewHolderItemClick() {
                if (selectedItems.get(position)) {
                    // 펼쳐진 Item을 클릭 시
                    selectedItems.delete(position);
                } else {
                    // 직전의 클릭됐던 Item의 클릭상태를 지움
                    selectedItems.delete(prePosition);
                    // 클릭한 Item의 position을 저장
                    selectedItems.put(position, true);
                }
                // 해당 포지션의 변화를 알림
                if (prePosition != -1) notifyItemChanged(prePosition);
                notifyItemChanged(position);
                // 클릭된 position 저장
                prePosition = position;
            }
        });
    }


    @Override
    public int getItemCount() {
        return items.size();
    }


    public void addItem(DogItem item) {

        items.add(item);
    }

    /**
     * 아이템 추가
     *
     * @param position 등록 위치
     * @param item     아이템
     */
    public void addItem(int position, DogItem item) {

        items.add(position, item);
    }

    /**
     * 아이템 삭제
     *
     * @param position 삭제 위치
     */
    public void removeItem(int position) {

        items.remove(position);
    }

    public void removeAllItem() {
        items.clear();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dogName;
        public TextView breed;
        public TextView lastNum;
        public TextView dogSex;
        public TextView weight;
        public TextView totalDay;
        public CheckBox lunchBox;
        public CheckBox dinnerBox;

        LinearLayout linearlayout;
        LinearLayout linearlayout2;
        OnViewHolderItemClickListener onViewHolderItemClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            dogName = itemView.findViewById(R.id.dogName);
            breed = itemView.findViewById(R.id.breed);
            lastNum = itemView.findViewById(R.id.lastNum);
            dogSex = itemView.findViewById(R.id.dogSex);
            weight = itemView.findViewById(R.id.weight);
            totalDay = itemView.findViewById(R.id.totalDay);
            lunchBox = itemView.findViewById(R.id.lunchBox);
            dinnerBox = itemView.findViewById(R.id.dinnerBox);

            linearlayout = itemView.findViewById(R.id.linearlayout);
            linearlayout2 = itemView.findViewById(R.id.linearlayout2);

            linearlayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onViewHolderItemClickListener.onViewHolderItemClick();
                }
            });
        }

        public void setItem(DogItem item) {

            dogName.setText(item.getDogName());
            breed.setText(item.getBreed());
            lastNum.setText(item.getLastNum());
            dogSex.setText(item.getSex());
            weight.setText(item.getWeight());
            totalDay.setText(item.getTotalDay());

        }

        public void onBind(DogItem data, int position, SparseBooleanArray selectedItems) {
            dogName.setText(data.getDogName());
            breed.setText(data.getBreed());
            lastNum.setText(data.getLastNum());
            dogSex.setText(data.getSex());
            weight.setText(data.getWeight());
            totalDay.setText(data.getTotalDay());
            changeVisibility(selectedItems.get(position));
        }

        private void changeVisibility(final boolean isExpanded) {
            // ValueAnimator.ofInt(int... values)는 View가 변할 값을 지정, 인자는 int 배열
            ValueAnimator va = isExpanded ? ValueAnimator.ofInt(0, 200) : ValueAnimator.ofInt(200, 0);
            // Animation이 실행되는 시간, n/1000초
            va.setDuration(600);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // imageView의 높이 변경
                    totalDay.getLayoutParams().height = (int) animation.getAnimatedValue();
                    totalDay.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    totalDay.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va.start();

            ValueAnimator va1 = isExpanded ? ValueAnimator.ofInt(0, 300) : ValueAnimator.ofInt(300, 0);
            // Animation이 실행되는 시간, n/1000초
            va1.setDuration(500);
            va1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    // imageView의 높이 변경
                    linearlayout2.getLayoutParams().height = (int) animation.getAnimatedValue();
                    linearlayout2.requestLayout();
                    // imageView가 실제로 사라지게하는 부분
                    linearlayout2.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
                }
            });
            // Animation start
            va1.start();


        }

        public void setOnViewHolderItemClickListener(OnViewHolderItemClickListener onViewHolderItemClickListener) {
            this.onViewHolderItemClickListener = onViewHolderItemClickListener;
        }

    }


}