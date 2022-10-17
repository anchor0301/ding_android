package com.example.myapplication.ListDog;

import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.myapplication.Calculator.Calcul;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loopj.android.http.AsyncHttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DogListActivity extends FragmentActivity {

    RecyclerView recyclerView;
    ItemAdapter itemAdapter;
    SwipeRefreshLayout  SwipeRefresh;
    ArrayList<DogItem> items = new ArrayList<DogItem>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doglist);
        //리스트뷰 새로고침 시 동작
        SwipeRefresh = findViewById(R.id.content_srl);

        getWindow().setWindowAnimations(0); //화면 전환 애니메이션 제거

        RecyclerView();
        SwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 새로고침시 동작
                RecyclerView();
                // 종료
                SwipeRefresh.setRefreshing(false);
            }
        });


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

    public void RecyclerView() {
        recyclerView = findViewById(R.id.recycler_view);

        //아래구분선 세팅
        recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));
        // 리사이클러뷰에 레이아웃 매니저와 어댑터를 설정한다.
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false); //레이아웃매니저 생성
        recyclerView.setLayoutManager(layoutManager); ////만든 레이아웃매니저 객체를(설정을) 리사이클러 뷰에 설정해줌

        itemAdapter = new ItemAdapter();
        recyclerView.setAdapter(itemAdapter);
       


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                int position = viewHolder.getBindingAdapterPosition();

                switch (direction) {

                    case ItemTouchHelper.LEFT:

                        //삭제할 아이템 담아두기
                        DogItem deleteItem = items.get(position);

                        //삭제 기능

                        items.remove(position);
                        itemAdapter.removeItem(position);
                        itemAdapter.notifyItemRemoved(position);
                        itemAdapter.notifyDataSetChanged();
                        //복구 기능
                        Snackbar.make(recyclerView, deleteItem.getDogName(), Snackbar.LENGTH_LONG)
                                .setAction("복구", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {

                                        items.add(position, deleteItem);
                                        itemAdapter.addItem(position, deleteItem);
                                        itemAdapter.notifyItemInserted(position);
                                    }
                                }).show();
                        break;


                }
            }



            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder,
                        dX, dY, actionState, isCurrentlyActive)
                        .addSwipeLeftBackgroundColor(Color.RED)
                        .addSwipeLeftActionIcon(R.drawable.ic_delete)
                        .addSwipeLeftLabel("퇴실")
                        .setSwipeLeftLabelColor(Color.WHITE)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        }).attachToRecyclerView(recyclerView);


        //thread 생성
        callPOST callPost = new callPOST();
        //thread 실행
        callPost.start();

        try {
            callPost.join();
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            Log.d( "RecyclerView: ","종료");
            callPost.interrupt();
        }


        try {
            //callPost 스레드에서 json결과값 가져오기
            String jsonData = callPost.getResult();


            JSONObject jsonObject = new JSONObject(jsonData);

            Log.i("Response1 : ", jsonObject.getString("results"));

            JSONArray dogsInfoArray = jsonObject.getJSONArray("results");

            for (int i = 0; i < dogsInfoArray.length(); i++) {
                JSONObject dogObject = dogsInfoArray.getJSONObject(i);
                JSONObject properties = dogObject.getJSONObject("properties");

                //애견 이름
                JSONObject nameProperties = properties.getJSONObject("이름");
                JSONArray dogNameTitleArray = nameProperties.getJSONArray("title");
                JSONObject dogNameTitle = dogNameTitleArray.getJSONObject(0);
                JSONObject dogNameText = dogNameTitle.getJSONObject("text");
                //애견 이름 저장
                String dogName = dogNameText.getString("content");


                //애견 견종
                JSONObject breedProperties = properties.getJSONObject("견종");
                JSONObject breedselect = breedProperties.getJSONObject("select");
                String dogBreed = breedselect.getString("name");  //애견 견종 저장


                //애견 성별
                JSONObject sexProperties = properties.getJSONObject("성별");
                JSONObject sexSelect = sexProperties.getJSONObject("select");
                String dogSex = sexSelect.getString("name");//애견 성별 저장


                //애견 몸무게
                JSONObject weightProperties = properties.getJSONObject("몸무게");
                String dogWeight = weightProperties.getString("number");//애견 성별 저장

                //애견 몸무게
                JSONObject totalDayProperties = properties.getJSONObject("예약날짜");
                JSONObject totalDayFormula = totalDayProperties.getJSONObject("formula");//애견 성별 저장
                String totalDay = totalDayFormula.getString("string");//애견 성별 저장

                //점심 유무
                JSONObject lunchProperties = properties.getJSONObject("점심");
                Boolean lunch = Boolean.parseBoolean(lunchProperties.getString("checkbox"));//점심 먹었니?

                //저녁 유무
                JSONObject dinnerProperties = properties.getJSONObject("저녁");
                Boolean dinner = Boolean.parseBoolean(dinnerProperties.getString("checkbox"));//저녁 먹었니?

                //샘플데이터 생성
                DogItem item = new DogItem();
                item.setDogName(dogName);
                item.setBreed(dogBreed);
                item.setSex(dogSex);
                item.setWeight("몸무게 : "+dogWeight);
                item.setTotalDay(totalDay);
                item.setLunchBoxSelected(lunch);
                item.setDinnerBoxSelected(dinner);
                item.setLastNum("213" + i);

                //데이터 등록
                items.add(item);
                itemAdapter.addItem(item);

            }



        } catch (Exception e) {
            e.printStackTrace();
        }
        //적용
        itemAdapter.notifyDataSetChanged();

        //애니메이션 실행
        recyclerView.startLayoutAnimation();
    }


    public class callPOST extends Thread {
        private String Result;

        @Override
        public void run() {

            try {
                //인스턴스를 생성합니다.
                OkHttpClient client = new OkHttpClient();
                //URL
                String strURL = "https://api.notion.com/v1/databases/5ae1d1a61f5f4efe9f9557d62b9adf5e/query";
                //parameter를 JSON object로 전달합니다

                //TODO 주석제거
                //String strBody = "{\"parameter\":\"NA\"}";

                String strBody = "{\n" +
                        "  \"page_size\":100,\n" +
                        "  \"filter\": {\n" +
                        "    \"and\": [\n" +
                        "      {\n" +
                        "        \"property\": \"입실 여부\",\n" +
                        "        \"select\": {\n" +
                        "          \"equals\": \"입실완료\"\n" +
                        "        }\n" +
                        "      }          \n" +
                        "    ]\t\n" +
                        "  },\n" +
                        "  \"sorts\": [\n" +
                        "    {\n" +
                        "      \"timestamp\": \"last_edited_time\",\n" +
                        "      \"direction\": \"descending\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}\n";
                //POST요청을 위한 request body를 구성합니다.
                RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), strBody);
                //POST요청을 위한 build작업
                Request.Builder builder = new Request.Builder()
                        .url(strURL).post(requestBody);
                //json을 주고받는 경우, 헤더에 추가
                builder.addHeader("Content-type", "application/json");
                builder.addHeader("Authorization", "Bearer secret_gNvpkrPcYOkO3RmvNdBB5RXSvwFS2B0ZHLGubmWDBx1");
                builder.addHeader("Notion-Version", "2022-02-22");
                //request 객체를 생성
                Request request = builder.build();
                //request를 요청하고 그 결과를 response 객체로 응답을 받음.
                Response response = client.newCall(request).execute();
                //응답처리
                Result = response.body().string();

                //Log.i("request1 : ", request.toString());
                Log.i("Response1 : ", Result);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        public String getResult() {
            return this.Result;
        }

    }

}



