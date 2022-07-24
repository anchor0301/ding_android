package com.example.myapplication;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String INFO="정보";
    private static final String PHONE="전화번호";

    ArrayList<HashMap<String, Object>> itemList;
    ListView listView;
    SwipeRefreshLayout mysrl;
    ImageView messageImageView;
    ImageView calculatorImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //전화기록 불러올수 있게
        ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CALL_LOG}, MODE_PRIVATE);

        //리스트 뷰에 넣을거
        itemList = new ArrayList<HashMap<String, Object>>();
        listView = findViewById(R.id.listView);

        //밑에 메뉴
        messageImageView=findViewById(R.id.messageImage);
        calculatorImageView=findViewById(R.id.calculatorImage);

        //리스트뷰 새로고침 시 동작
        mysrl = findViewById(R.id.content_srl);
        mysrl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 새로고침시 동작
                getCallHistory();

                // 종료
                mysrl.setRefreshing(false);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//리스트 클릭 이벤트
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String phone=String.valueOf(itemList.get(position).get("전화번호"));//클릭한 리스트의 전화번호

                //알림창
                AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("메세지 전송");
                ad.setMessage(phone+" 님께 메시지를 보내겠습니까?");
                ad.setPositiveButton("전송", new DialogInterface.OnClickListener() {//전송버튼 클릭시 메시지 전송
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //메시지 전송 코드 작성
                    }
                });
                ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {//전송 취소 버튼 클릭시 메시지 전송 취소
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                ad.show();
            }
        });
        getCallHistory();
    }
    public void messageSend(View view){//직접 전화번호 작성해서 전송 버튼 클릭시 이벤트
        EditText editText=findViewById(R.id.editText);
        String phone=editText.getText().toString();

        if(phone==null || phone.equals("")){
            Toast.makeText(getApplicationContext(),"잘못된 입력입니다.",Toast.LENGTH_SHORT).show();
        }else{
            AlertDialog.Builder ad=new AlertDialog.Builder(MainActivity.this);
            ad.setTitle("메세지 전송");
            ad.setMessage(phone+" 님께 메시지를 보내겠습니까?");
            ad.setPositiveButton("전송", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            ad.show();
        }


    }

    public void getCallHistory() {//통화기록 조회
        String[] callSet = new String[]{CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);

        if (c.getCount()==0) {//통화기록 없을 시
            Toast.makeText(MainActivity.this,"통화 기록이 없습니다.",Toast.LENGTH_SHORT).show();

        }else if(c.getCount()==1) {//통화기록 1개
            c.moveToFirst();
            String timeLog1 = timeLog(c);
            String phoneLog1 = phoneLog(c);

            itemList.clear();
            addItemList(timeLog1,phoneLog1);

            makeListAdapter();

        }else if(c.getCount()==2) {//통화기록 2개

            c.moveToFirst();
            itemList.clear();

            for(int i=0;i<2;i++){
                String timeLog=timeLog(c);
                String phoneLog=phoneLog(c);
                addItemList(timeLog,phoneLog);
                c.moveToNext();
            }

            makeListAdapter();

        }else{//그외
            c.moveToFirst();
            itemList.clear();

            for(int i=0;i<3;i++){
                String timeLog=timeLog(c);
                String phoneLog=phoneLog(c);
                addItemList(timeLog,phoneLog);
                c.moveToNext();
            }

            makeListAdapter();

        }

    }
    public String timeLog(Cursor c){//전화 시간 로그

        StringBuffer callBuff = new StringBuffer();

        //시간
        long callDate=c.getLong(0);
        SimpleDateFormat datePattern = new SimpleDateFormat("MM-dd HH:mm:ss");
        String date_str = datePattern.format(new Date(callDate));
        callBuff.append(date_str);

        return callBuff.toString();
    }
    public String phoneLog(Cursor c){//전화번호 로그
        return c.getString(2);
    }

    public void addItemList(String timeLog, String phoneLog){//리스트에 아이템 추가

        HashMap<String, Object> p = new HashMap<>();
        p.put(INFO, timeLog);
        p.put(PHONE,phoneLog);

        itemList.add(p);
    }

    public void makeListAdapter(){//리스트뷰 어댑터

        //리스트 어뎁터
        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this, itemList, R.layout.list,
                new String[]{INFO,PHONE},
                new int[]{R.id.infomation,R.id.phone}
        );

        listView.setAdapter(adapter);
    }
}