package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.renderscript.ScriptGroup;
import android.telephony.SmsManager;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    Button send_sms_button;
    EditText input_phone_num;
    SwipeRefreshLayout swipeRefreshLayout;
    Button remove;
    TextView printCall;

    int i=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, MODE_PRIVATE);
        send_sms_button = (Button) findViewById(R.id.send_sms_button);
        input_phone_num = (EditText) findViewById(R.id.input_phone_num);
        remove = (Button) findViewById(R.id.remove);
        printCall = (TextView) findViewById(R.id.printCall);

        printCall.setText(getCalPrint());

        input_phone_num.setText(getCallHistory());

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_phone_num.requestFocus();
                input_phone_num.setText("010-");

                Editable etext = input_phone_num.getText();
                Selection.setSelection(etext, etext.length());

                InputMethodManager imm= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(input_phone_num, InputMethodManager.SHOW_IMPLICIT);

            }
        });
        send_sms_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = input_phone_num.getText().toString();
                String inputText2 = "위탁 이용 동의 안내서입니다.\n 아래 링크 들어가셔서 작성 부탁드립니다!";
                String inputText3 = "https://forms.gle/aYL14QNhzDZbpWABA";
                if (inputText.length() > 0 && inputText2.length() > 0) {
                    sendSMS(inputText, inputText2);
                    sendSMS(inputText, inputText3);
                    Toast.makeText(getBaseContext(), inputText + "\n" + inputText2, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "전송 성공", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getBaseContext(), "전화번호와 메시지를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });


        swipeRefreshLayout = findViewById(R.id.swipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    @Override
    public void onRefresh() {

        //새로 고침 코드
        updateLayoutView();

        //새로 고침 완
        swipeRefreshLayout.setRefreshing(false);

    }

    // 당겨서 새로고침 했을 때 뷰 변경 메서드
    public void updateLayoutView() {
        i=0;
        input_phone_num.setText(getCallHistory());
        printCall.setText(getCalPrint());
    }

    private void sendSMS(String phoneNumber, String message) {

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    public String getCallHistory() {
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

    public String getCalPrint(){
        String[] callSet = new String[] { CallLog.Calls.DATE, CallLog.Calls.TYPE, CallLog.Calls.NUMBER, CallLog.Calls.DURATION };
        Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, callSet, null, null, null);
        System.out.println(c);
        if ( c == null)
        {
            return "통화기록 없음";
        }
        StringBuffer callBuff = new StringBuffer();
        callBuff.append("\n         날짜         :    구분   :   전화번호   :  통화시간\n\n");
        c.moveToFirst();

        do{
            i++;
            long callDate = c.getLong(0);
            SimpleDateFormat datePattern = new SimpleDateFormat("MM-dd / HH:MM");
            String date_str = datePattern.format(new Date(callDate));
            callBuff.append(date_str + " - ");
            if (c.getInt(1) == CallLog.Calls.INCOMING_TYPE)
            {
                callBuff.append("착신 : ");
            }
            else
            {
                callBuff.append("발신 : ");
            }
            callBuff.append(c.getString(2)+ " : ");
            callBuff.append(c.getString(3)+ "초\n\n");
        } while (c.moveToNext()  && i<=5 );
        c.close();
        return callBuff.toString();
    }
}