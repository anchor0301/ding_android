package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;
import com.example.myapplication.RestAPI;
public class CheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
    }

    public void messageClick(View view){
        Intent intent=new Intent(CheckoutActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void calculatorClick(View view){
        Intent intent=new Intent(CheckoutActivity.this,CalculatorActivity.class);
        startActivity(intent);
        finish();
    }

    public void kakao(View view){
        RestAPI restAPI=new RestAPI();
        restAPI.sampleMethod();
    }



}
