package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DogListActivity extends FragmentActivity {

    RecyclerView recyclerView;
    ItemAdapter itemAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doglist);
        getWindow().setWindowAnimations(0); //화면 전환 애니메이션 제거

        recyclerView = findViewById(R.id.recycler_view);



        //아래구분선 세팅
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        // 리사이클러뷰에 레이아웃 매니저와 어댑터를 설정한다.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
        recyclerView.setLayoutManager(layoutManager); ////만든 레이아웃매니저 객체를(설정을) 리사이클러 뷰에 설정해줌
        itemAdapter = new ItemAdapter();



        recyclerView.setAdapter(itemAdapter);

                //조회 전 화면 클리어
                itemAdapter.removeAllItem();

                //샘플 데이터 생성
                for(int i = 0; i < 20; i++){

                    DogListItem item = new DogListItem();
                    item.setDogName("애견"+i);
                    //item.setBreed("description" + i);

                    //데이터 등록
                    itemAdapter.addItem(item);

                }

                //적용
                itemAdapter.notifyDataSetChanged();



    }

    public void messageClick(View view) {
        Intent intent = new Intent(DogListActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void calculatorClick(View view) {
        Intent intent = new Intent(DogListActivity.this, Calcul.class);
        startActivity(intent);
        finish();
    }

}


