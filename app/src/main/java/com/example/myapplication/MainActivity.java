package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    Button send_sms_button;
    EditText input_phone_num;
    Button remove;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG}, MODE_PRIVATE);

        send_sms_button = (Button) findViewById(R.id.send_sms_button);
        input_phone_num = (EditText) findViewById(R.id.input_phone_num);
        remove = (Button) findViewById(R.id.remove);
        input_phone_num.setText(getCallHistory());
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input_phone_num.setText(null);
            }
        });
        send_sms_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputText = input_phone_num.getText().toString();
                String inputText2 = "위탁 이용 동의 안내서입니다.\n 아래 링크 들어가셔서 작성 부탁드립니다!";
                String inputText3="https://forms.gle/aYL14QNhzDZbpWABA";
                if (inputText.length() > 0 && inputText2.length() > 0) {
                    sendSMS(inputText, inputText2);
                    sendSMS(inputText,inputText3);
                    Toast.makeText(getBaseContext(), inputText + "\n" + inputText2, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "전송 성공", Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getBaseContext(), "전화번호와 메시지를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });
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
}