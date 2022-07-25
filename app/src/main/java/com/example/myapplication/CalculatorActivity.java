package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CalculatorActivity extends AppCompatActivity {
    ImageView messageImageView;
    ImageView calculatorImageView;
    NumberPicker numberPickerWeight;
    NumberPicker numberPickerHour;
    NumberPicker numberPickerHalf;
    TextView resultOutput;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    EditText EditText1;
    EditText EditText2;
    EditText EditText3;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        getWindow().setWindowAnimations(0);//화면 전환 애니메이션 제거
        messageImageView = findViewById(R.id.messageImage);
        messageImageView.setImageResource(R.drawable.message);
        calculatorImageView = findViewById(R.id.calculatorImage);
        calculatorImageView.setImageResource(R.drawable.calculator2);
        resultOutput = (TextView) findViewById(R.id.resultOutput);
        numberPickerWeight = (NumberPicker) findViewById(R.id.numberPicker);
        numberPickerHour = (NumberPicker) findViewById(R.id.numberPickerHour);
        numberPickerHalf = (NumberPicker) findViewById(R.id.numberPickerHalf);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        EditText1 = (EditText) findViewById(R.id.EditText1);
        EditText2 = (EditText) findViewById(R.id.EditText2);
        EditText3 = (EditText) findViewById(R.id.EditText3);


        final int[] addOnsPickUp = {0}; //픽업
        final int[] addOnsDrop = {0};   //드롭
        final int[] addOnShower = {0};  //목욕
        final int[] weight = {0};       //몸무게
        final int[] size = {0};         //크기

        initNumberPicker();
        //몸무게 다이얼 1~20

        numberPickerWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { //몸무게
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal <= 5) {
                    weight[0] = 25000;
                } else if (newVal > 5 && newVal <= 10) {
                    weight[0] = 30000;
                } else if (newVal > 10 && newVal < 15) {
                    weight[0] = 35000;
                } else if (newVal >= 15) {
                    weight[0] = 50000;
                }

                resultOutput.setText(String.format("%s원", weight[0] + (addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0])));

            }
        });

        checkBox1.setOnClickListener(new CheckBox.OnClickListener() {//체크 박스1 상태 감지 (픽업)
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    // TODO : CheckBox is checked.
                    addOnsPickUp[0] = 5000;
                    EditText1.setEnabled(true);
                    EditText1.setHint("추가 거리");

                } else {
                    // TODO : CheckBox is unchecked.

                    addOnsPickUp[0] = 0;
                    EditText1.setEnabled(false);
                    EditText1.setHint("비활성화");
                }

                resultOutput.setText(String.format("%s원", weight[0] + (addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0])));
            }
        });

        checkBox2.setOnClickListener(new CheckBox.OnClickListener() {//체크 박스2 상태 감지 (드랍)
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    // TODO : CheckBox is checked.
                    addOnsDrop[0] = 5000;
                    EditText2.setEnabled(true);
                    EditText2.setHint("추가 거리");
                } else {
                    // TODO : CheckBox is unchecked.
                    addOnsDrop[0] = 0;

                    EditText2.setEnabled(false);
                    EditText2.setHint("비활성화");
                }
                resultOutput.setText(String.format("%s원", weight[0] + (addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0])));
            }
        });

        checkBox3.setOnClickListener(new CheckBox.OnClickListener() {//체크 박스3 상태 감지 (목욕)
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    // TODO : CheckBox is checked.
                    addOnShower[0] = 15000;
                    EditText3.setEnabled(true);
                    EditText3.setHint("15,000");
                } else {
                    // TODO : CheckBox is unchecked.
                    addOnShower[0] = 0;

                    EditText3.setEnabled(false);
                    EditText3.setHint("비활성화");
                }
                resultOutput.setText(String.format("%s원", weight[0] + (addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0])));
            }
        });

    }

    public void messageClick(View view) {
        Intent intent = new Intent(CalculatorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void initNumberPicker() {
        //부가 기능 사전 설정

        EditText1.setEnabled(false);
        EditText1.setHint("비활성화");

        EditText2.setEnabled(false);
        EditText2.setHint("비활성화");

        EditText3.setEnabled(false);
        EditText3.setHint("비활성화");


        //몸무게
        numberPickerWeight.setMinValue(1);      //몸무게 최소 1
        numberPickerWeight.setMaxValue(20);     //몸무게 최소20
        numberPickerWeight.setValue(5);         //기본값 5
        numberPickerWeight.setWrapSelectorWheel(false); //무한 휠 허용
        numberPickerWeight.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //키보드 입력 방지

        //초과 시간
        numberPickerHour.setMinValue(0);      //시간 최소 0
        numberPickerHour.setMaxValue(23);     //시간 최소23
        numberPickerHour.setValue(0);         //기본 0
        numberPickerHour.setOnLongPressUpdateInterval(1000);

        numberPickerHour.setWrapSelectorWheel(false); //무한 휠 허용
        numberPickerHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //키보드 입력 방지

        //초과 분


        numberPickerHalf.setMinValue(0);      //몸무게 최소 0
        numberPickerHalf.setMaxValue(3);     //몸무게 최소59
        numberPickerHalf.setDisplayedValues(new String[]{
                "0", "15", "30", "45"
        });
        numberPickerHalf.setValue(0);         //기본값 0
        numberPickerHalf.setOnLongPressUpdateInterval(1000);
        numberPickerHalf.setWrapSelectorWheel(false); //무한 휠 허용
        numberPickerHalf.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //키보드 입력 방지

    }

}
