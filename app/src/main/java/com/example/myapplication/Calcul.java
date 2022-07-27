package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Half;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Calcul extends AppCompatActivity {

    ImageView messageImageView;
    ImageView calculatorImageView;
    NumberPicker numberPickerWeight;
    NumberPicker numberPickerUseHour;
    NumberPicker numberPickerUseHalf;
    TextView resultOutput;
    TextView textUseTime;
    TextView currentTime;
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    EditText EditText1;
    EditText EditText2;
    EditText EditText3;
    public static String format_date = "MM/dd HH:mm:ss";
    public static String format_hour = "HH";
    final int[] addOnsPickUp = {0}; //픽업
    final int[] addOnsDrop = {0};   //드롭
    final int[] addOnShower = {0};  //목욕
    final int[] weight = {5};       //몸무게
    final int[] useHour = {0};      //이용 시간
    final int[] useHalf = {0};      //이용 분
    final int[] useTime = {0};         //총 이용시간

    //int CurrentTime = Integer.parseInt(getCurrentDate_hour()); TODO 주석제거
    int CurrentTime = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calcul);

        getWindow().setWindowAnimations(0);//화면 전환 애니메이션 제거

        //초기화
        initNumberPicker();

        playroom();//놀이방 기준 계산
        textUseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "아부지 사랑해요", Toast.LENGTH_SHORT).show();
            }
        });

        new Thread(r).start();


    }

    public void messageClick(View view) {
        Intent intent = new Intent(Calcul.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void initNumberPicker() {

        messageImageView = findViewById(R.id.messageImage);
        messageImageView.setImageResource(R.drawable.message);
        calculatorImageView = findViewById(R.id.calculatorImage);
        calculatorImageView.setImageResource(R.drawable.calculator2);
        currentTime = (TextView) findViewById(R.id.currentTime);
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

        //현재 시간 출력
        currentTime.setText(getCurrentDate_date());

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
        //numberPickerUseHour.setMaxValue(CurrentTime + 1);     //시간 현재시간이 최대       TODO 주석제거
        //numberPickerUseHour.setValue(CurrentTime - 1);         //기본 6                 TODO 주석제거!

        numberPickerUseHour.setMaxValue(12); //TODO : 테스트용 삭제해야함!

        numberPickerUseHour.setOnLongPressUpdateInterval(1000);
        numberPickerUseHour.setWrapSelectorWheel(false); //무한 휠 안됨
        numberPickerUseHour.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //키보드 입력 방지

        //이용 분
        numberPickerUseHalf.setMinValue(0);      //분 최소 0
        numberPickerUseHalf.setMaxValue(5);     //분 최대55
        numberPickerUseHalf.setValue(0);
        numberPickerUseHalf.setWrapSelectorWheel(true); //무한 휠 안됨
        numberPickerUseHalf.setDisplayedValues(new String[]{
                "0","10","20","30","40","50"
                });

        //몸무게
        numberPickerWeight.setMinValue(1);      //몸무게 최소 1
        numberPickerWeight.setMaxValue(20);     //몸무게 최대20
        numberPickerWeight.setValue(3);         //기본값 3
        weight[0] = 1250; //3kg 이면 1,250원
        numberPickerWeight.setWrapSelectorWheel(false); //무한 휠 허용
        numberPickerWeight.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS); //키보드 입력 방지


    }

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
                //numberPickerUseHour.setValue(CurrentTime - 1);        TODO 주석제거!
                numberPickerUseHalf.setValue(1);
                useTime[0] = 0;
                useHour[0] = 0;
                useHalf[0] = 0;
            }
        });

        numberPickerUseHour.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { //몸무게
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                useHour[0] = (weight[0]*2)*(CurrentTime - newVal);
                useTime[0] = ( useHour[0] + useHalf[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
                NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); //컴마 생성
                resultOutput.setText(String.format(" %s", moneyFormat.format( useTime[0])));      //출력
            }
        });

        numberPickerUseHalf.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() { //몸무게
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal==0){
                    useHalf[0]=0;
                }else if(newVal<=3){
                   useHalf[0]=-weight[0];
                }else{
                    useHalf[0]=-weight[0]*2;
                }
                useTime[0] = ( useHour[0] + useHalf[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
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
                useTime[0] = (weight[0] * useHour[0] + useHalf[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
                NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); //컴마 생성
                resultOutput.setText(String.format(" %s", moneyFormat.format(useTime[0])));      //출력
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
                useTime[0] = (weight[0] * useHour[0] + useHalf[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
                NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); //컴마 생성
                resultOutput.setText(String.format(" %s", moneyFormat.format(useTime[0])));      //출력
            }
        });

        checkBox3.setOnClickListener(new CheckBox.OnClickListener() {//체크 박스3 상태 감지 (목욕)
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    // TODO : CheckBox is checked.
                    EditText3.setEnabled(true);
                    EditText3.setHint("15,000");
                    addOnShower[0] = 15000;
                } else {
                    // TODO : CheckBox is unchecked.
                    addOnShower[0] = 0;

                    EditText3.setEnabled(false);
                    EditText3.setHint("비활성화");
                }
                useTime[0] = (weight[0] * useHour[0] + useHalf[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
                NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); //컴마 생성
                resultOutput.setText(String.format(" %s", moneyFormat.format(useTime[0])));      //출력
            }
        });

    }

    public static String moneyFormatToWon(int inputMoney) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        return decimalFormat.format(decimalFormat);
    }

    public static String getCurrentDate_hour() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_hour, Locale.getDefault());
        return format.format(currentTime);
    }

    public static String getCurrentDate_date() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_date, Locale.getDefault());
        return format.format(currentTime);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(1000);

                } catch (Exception e) {

                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //currentTime.setText(getCurrentDate_date()); TODO 주석 제거

                        //numberPickerUseHour.setMaxValue(CurrentTime + 1);     //시간 현재시간이 최대  TODO 주석제거!
                    }
                });
            }
        }
    };


}