package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import com.example.myapplication.RestAPI;
public class CheckoutActivity extends AppCompatActivity {
    TextView printPhoneNumber;

    String phone = "01089000137";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getWindow().setWindowAnimations(0); //화면 전환 애니메이션 제거


        printPhoneNumber = (TextView) findViewById(R.id.printPhoneNumber);

        printPhoneNumber.setText(phone);

    }

    public void messageClick(View view){
        Intent intent=new Intent(CheckoutActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void calculatorClick(View view){
        Intent intent=new Intent(CheckoutActivity.this,Calcul.class);
        startActivity(intent);
        finish();
    }

    public void kakao(View view){
        RestAPI restAPI=new RestAPI(phone);
        restAPI.sampleMethod();
    }



}
