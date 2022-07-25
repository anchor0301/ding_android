package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {
    ImageView messageImageView;
    ImageView calculatorImageView;
    NumberPicker numberPickerWeight;
    NumberPicker numberPickerSize;
    TextView resultOutput;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    String[] allSize;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        getWindow().setWindowAnimations(0);//화면 전환 애니메이션 제거
        messageImageView=findViewById(R.id.messageImage);
        messageImageView.setImageResource(R.drawable.message);
        calculatorImageView=findViewById(R.id.calculatorImage);
        calculatorImageView.setImageResource(R.drawable.calculator2);
        resultOutput = (TextView) findViewById(R.id.resultOutput);
        numberPickerWeight = (NumberPicker) findViewById(R.id.numberPicker);
        numberPickerSize = (NumberPicker) findViewById(R.id.size);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);


        allSize =getResources().getStringArray(R.array.allSize); //소,중,대 리스트 가져오기



        final int[] addOnsPickUp = {0};
        final int[] addOnsDrop  = {0};
        final  int[] addOnsshower = {0};
        final int[] weight = {0};


        //몸무게 다이얼 1~20
        numberPickerWeight.setMinValue(1);//몸무게 최소 1
        numberPickerWeight.setMaxValue(20); //몸무게 최소20
        numberPickerWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {


                    resultOutput.setText(String.format("%s원",weight[0]+(addOnsPickUp[0]+addOnsDrop[0]+addOnsshower[0])));
            }
        });


        numberPickerSize.setDisplayedValues(allSize);//소,중,대형
        numberPickerSize.setMinValue(0);//크기 최소 1
        numberPickerSize.setMaxValue(2); //크기 최소 3
        numberPickerSize.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                resultOutput.setText(String.format(String.valueOf(newVal)));
                }
        });



        checkBox1.setOnClickListener(new CheckBox.OnClickListener() {//체크 박스1 상태 감지 (픽업)
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    // TODO : CheckBox is checked.
                    addOnsPickUp[0] = 5000;
                } else {
                    // TODO : CheckBox is unchecked.

                    addOnsPickUp[0] = 0;
                }
            }
        }) ;
        checkBox2.setOnClickListener(new CheckBox.OnClickListener() {//체크 박스2 상태 감지 (드랍)
            @Override
            public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    // TODO : CheckBox is checked.
                    addOnsDrop[0] = 5000;

                } else {
                    // TODO : CheckBox is unchecked.
                    addOnsDrop[0] = 0;
                }
            }
        }) ;
        checkBox3.setOnClickListener(new CheckBox.OnClickListener() {//체크 박스3 상태 감지 (목욕)
            @Override
           public void onClick(View v) {
                if (((CheckBox)v).isChecked()) {
                    // TODO : CheckBox is checked.
                    addOnsshower[0]=15000;
                } else {
                    // TODO : CheckBox is unchecked.
                    addOnsshower[0]=0;

                }
            }
        }) ;

    }

    public void messageClick(View view){
        Intent intent=new Intent(CalculatorActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
