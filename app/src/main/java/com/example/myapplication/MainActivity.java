package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.PhoneStateListener;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String INFO = "정보";
    private static final String PHONE = "전화번호";

    ArrayList<HashMap<String, Object>> itemList;
    ListView listView;
    TextView textView;
    EditText input_phone_num;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //전화기록 불러올수 있게
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, MODE_PRIVATE);

        //리스트 뷰에 넣을거
        itemList = new ArrayList<HashMap<String, Object>>();
        listView = findViewById(R.id.listView);
        textView = findViewById(R.id.infomation);
        input_phone_num = (EditText) findViewById(R.id.editText);

        input_phone_num.setText(CallHistory());
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//리스트 클릭 이벤트
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String phone = String.valueOf(itemList.get(position).get("전화번호"));//클릭한 리스트의 전화번호

                //알림창
                AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                ad.setTitle("메세지 전송");
                ad.setMessage(phone + " 님께 메시지를 보내겠습니까?");
                ad.setPositiveButton("전송", new DialogInterface.OnClickListener() {//전송버튼 클릭시 메시지 전송
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //메시지 전송 코드 작성
                        String inputText2 = "위탁 이용 동의 안내서입니다.\n 아래 링크 들어가셔서 작성 부탁드립니다!";
                        String inputText3 = "https://forms.gle/s5JEQbmjGLjkv7Br8";


                        if (phone.length() > 0 && inputText2.length() > 0) {
                            sendSMS(phone, inputText2);
                            sendSMS(phone, inputText3);
                            Toast.makeText(getBaseContext(), phone + "\n" + inputText2, Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "전송 성공", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(getBaseContext(), "전화번호와 메시지를 입력해주세요.", Toast.LENGTH_SHORT).show();

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

        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void sendSMS(String phoneNumber, String message) {

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public void messageSend(View view) {//직접 전화번호 작성해서 전송 버튼 클릭시 이벤트
        EditText editText = findViewById(R.id.editText);
        String phone = editText.getText().toString();

        if (phone == null || phone.equals("")) {
            Toast.makeText(getApplicationContext(), "잘못된 입력입니다.", Toast.LENGTH_SHORT).show();
        } else {
            AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
            ad.setTitle("메세지 전송");
            ad.setMessage(phone + " 님께 메시지를 보내겠습니까?");
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

    public void btnClick(View view) {//통화기록 조회 버튼 클릭
        getCallHistory();
    }

    public void getCallHistory() {//통화기록 조회
        String[] callSet = new String[]{CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);

        if (c.getCount() == 0) {//통화기록 없을 시
            Toast.makeText(MainActivity.this, "통화 기록이 없습니다.", Toast.LENGTH_SHORT).show();

        } else if (c.getCount() == 1) {//통화기록 1개
            c.moveToFirst();
            String timeLog1 = timeLog(c);
            String phoneLog1 = phoneLog(c);

            itemList.clear();
            addItemList(timeLog1, phoneLog1);

            makeListAdapter();

        } else if (c.getCount() == 2) {//통화기록 2개

            c.moveToFirst();
            itemList.clear();

            for (int i = 0; i < 2; i++) {
                String timeLog = timeLog(c);
                String phoneLog = phoneLog(c);
                addItemList(timeLog, phoneLog);
                c.moveToNext();
            }

            makeListAdapter();

        } else {//그외
            c.moveToLast();
            itemList.clear();

            for (int i = 0; i < 3; i++) {
                String timeLog = timeLog(c);
                String phoneLog = phoneLog(c);
                addItemList(timeLog, phoneLog);
                c.moveToPrevious();
            }

            makeListAdapter();

        }

    }

    public String timeLog(Cursor c) {//전화 시간 로그

        StringBuffer callBuff = new StringBuffer();

        //시간
        long callDate = c.getLong(0);
        SimpleDateFormat datePattern = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String date_str = datePattern.format(new Date(callDate));
        callBuff.append(date_str);

        return callBuff.toString();
    }

    public String phoneLog(Cursor c) {//전화번호 로그
        return c.getString(2);
    }

    public void addItemList(String timeLog, String phoneLog) {//리스트에 아이템 추가

        HashMap<String, Object> p = new HashMap<>();
        p.put(INFO, timeLog);
        p.put(PHONE, phoneLog);

        itemList.add(p);
    }

    public void makeListAdapter() {//리스트뷰 어댑터

        //리스트 어뎁터
        ListAdapter adapter = new SimpleAdapter(
                MainActivity.this, itemList, R.layout.list,
                new String[]{INFO, PHONE},
                new int[]{R.id.infomation, R.id.phone}
        );

        listView.setAdapter(adapter);
    }


    public String CallHistory() {
        String[] callSet = new String[]{CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION};
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);

        if (c == null) {
            return "통화기록 없음";
        }
        StringBuffer callBuff = new StringBuffer();
        c.moveToFirst();
        callBuff.append(c.getString(2));

        c.close();
        return callBuff.toString();
    }

    // 당겨서 새로고침 했을 때 뷰 변경 메서드
    public void updateLayoutView() {
        input_phone_num.setText(CallHistory());
        getCallHistory();

    }

    @Override
    public void onRefresh() {

        //새로 고침 코드
        updateLayoutView();

        //새로 고침 완
        swipeRefreshLayout.setRefreshing(false);

    }

}