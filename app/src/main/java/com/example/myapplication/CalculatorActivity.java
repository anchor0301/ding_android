package com.example.myapplication;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CalculatorActivity extends AppCompatActivity {
    ImageView messageImageView;
    ImageView calculatorImageView;
    NumberPicker numberPickerWeight;
    NumberPicker numberPickerUseHour;
    NumberPicker numberPickerUseHalf;
    TextView resultOutput;
    TextView textUseTime;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    EditText EditText1;
    EditText EditText2;
    EditText EditText3;


    final int[] addOnsPickUp = {0}; //픽업
    final int[] addOnsDrop = {0};   //드롭
    final int[] addOnShower = {0};  //목욕
    final int[] weight = {5};       //몸무게
    final int[] useHour = {0};      //이용 시간
    final int[] useHalf = {0};      //이용 분
    final int[] useTime = {0};         //총 이용시간


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        getWindow().setWindowAnimations(0);//화면 전환 애니메이션 제거

        initNumberPicker(); //초기화 및 초기 선언

        playroom();//놀이방 기준 계산

        textUseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "추가 시간 출력", Toast.LENGTH_SHORT).show();
            }
        });
    }
    //메시지 전송 뷰로 전환
    public void messageClick(View view) {
        Intent intent = new Intent(CalculatorActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    //초기화 및 초기 선언
    public void initNumberPicker() {
        messageImageView = findViewById(R.id.messageImage);
        messageImageView.setImageResource(R.drawable.message);
        calculatorImageView = findViewById(R.id.calculatorImage);
        calculatorImageView.setImageResource(R.drawable.calculator2);
        resultOutput = (TextView) findViewById(R.id.resultOutput);
        textUseTime = (TextView) findViewById(R.id.useTime);
        numberPickerWeight = (NumberPicker) findViewById(R.id.numberPicker);
        numberPickerUseHour = (NumberPicker) findViewById(R.id.numberPickerUseHour);
        numberPickerUseHalf = (NumberPicker) findViewById(R.id.numberPickerUseHalf);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        EditText1 = (EditText) findViewById(R.id.EditText1);
        EditText2 = (EditText) findViewById(R.id.EditText2);
        EditText3 = (EditText) findViewById(R.id.EditText3);

        resultOutput.setText("몸무게를 설정해주세요.");


        //부가 기능 사전 설정

        EditText1.setEnabled(false);
        EditText1.setHint("비활성화");

        EditText2.setEnabled(false);
        EditText2.setHint("비활성화");

        EditText3.setEnabled(false);
        EditText3.setHint("비활성화");

        //이용 시간
        numberPickerUseHour.setMinValue(6);      //시간 최소 6
        numberPickerUseHour.setMaxValue(10);     //시간 최소10
        numberPickerUseHour.setValue(6);         //기본 6
        numberPickerUseHour.setWrapSelectorWheel(false); //무한 휠 안됨
        numberPickerUseHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //키보드 입력 방지

        //이용 분
        numberPickerUseHalf.setMinValue(0);      //분 최소 0
        numberPickerUseHalf.setMaxValue(1);     //분 최소 1
        numberPickerUseHalf.setWrapSelectorWheel(false); //무한 휠 안됨
        numberPickerUseHalf.setDisplayedValues(new String[]{
                "0", "30"
        });
        numberPickerUseHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //키보드 입력 방지

        //몸무게
        numberPickerWeight.setMinValue(1);      //몸무게 최소 1
        numberPickerWeight.setMaxValue(20);     //몸무게 최소20
        numberPickerWeight.setValue(5);         //기본값 5
        weight[0] = 1500; //5kg 이면 1,500원
        numberPickerWeight.setWrapSelectorWheel(false); //무한 휠 허용
        numberPickerWeight.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //키보드 입력 방지


    }

    //놀이방
    public void playroom() {

        //몸무게 1~20
        numberPickerWeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { //몸무게
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if (newVal <= 5) {  //5kg 이하
                    weight[0] = 1250;
                } else if (newVal > 5 && newVal <= 10) { //5kg 초과 10kg 이하
                    weight[0] = 1500;
                } else if (newVal > 10 && newVal < 15) { //10kg 초과 15kg 미만
                    weight[0] = 1750;
                } else if (newVal >= 15) {//15kg 이하
                    weight[0] = 2500;
                }


                resultOutput.setText("이용 시간을 설정해주세요");
                numberPickerUseHour.setValue(0);
                numberPickerUseHalf.setValue(0);
                useTime[0] = 0;
                useHour[0] = 0;
                useHalf[0] = 0;
            }
        });

        numberPickerUseHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { //몸무게
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                useHour[0] = (newVal * 2);
                useTime[0] = (weight[0] * useHour[0] + useHalf[0]);
                NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); //컴마 생성
                resultOutput.setText(String.format(" %s", moneyFormat.format(useTime[0])));      //출력
                numberPickerUseHalf.setValue(0);
            }
        });

        numberPickerUseHalf.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { //몸무게
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                useHalf[0] = newVal * weight[0];
                useTime[0] = (weight[0] * useHour[0] + useHalf[0]);

                NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); //컴마 생성
                resultOutput.setText(String.format(" %s", moneyFormat.format(useTime[0])));      //출력

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

                resultOutput.setText("테스트1");
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
                resultOutput.setText("테스트2");
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
                resultOutput.setText("테스트3");
            }
        });

    }

    //원화 및 컴마 출력
    public static String moneyFormatToWon(int inputMoney) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(decimalFormat);
    }


}

