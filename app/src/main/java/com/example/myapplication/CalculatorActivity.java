package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {
    ImageView messageImageView;
    ImageView calculatorImageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        messageImageView=findViewById(R.id.messageImage);
        messageImageView.setImageResource(R.drawable.message);

        calculatorImageView=findViewById(R.id.calculatorImage);
        calculatorImageView.setImageResource(R.drawable.calculator2);
    }

    public void messageClick(View view){
        Intent intent=new Intent(CalculatorActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
