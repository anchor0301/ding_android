package com.example.myapplication;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Half;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Calcul extends AppCompatActivity {

    ImageView messageImageView;
    ImageView calculatorImageView;
    NumberPicker numberPickerWeight;
    TextView resultOutput;
    TextView textUseTime;
    TextView currentTime;
    TimePicker tp;  //타임피커
    Calendar c;     //켈린더
    CheckBox checkBox1;
    CheckBox checkBox2;
    CheckBox checkBox3;
    EditText EditText1;
    EditText EditText2;
    EditText EditText3;

    //시간 포멧
    public static String format_date = "MM/dd HH:mm:ss";
    public static String format_hour = "HH";
    public static String format_half = "mm";



    int moneyHour;  //시간당 돈
    int Half; //분
    int moneyHalf; //시간당 분


    private long mLastClickTime = 0; //더블클릭후 실행  시간제한

    final int[] addOnsPickUp = {0};     //픽업
    final int[] addOnsDrop = {0};       //드롭
    final int[] addOnShower = {0};      //목욕
    final int[] weight = {5};           //몸무게

    //시간 계산
    final int[] useHour = {0};        //시간
    final int[] useTime = {0};         //총 시간
    //useTime = useHour + useHalf
    // 총 시간 = (시간 + 분)

    int CurrentTime = Integer.parseInt(getCurrentDate_hour());  //현재시간
    int CurrentHalf = Integer.parseInt(getCurrentDate_half());  //현재분

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calcul);

        getWindow().setWindowAnimations(0); //화면 전환 애니메이션 제거

        initNumberPicker(); //초기화

        playroom();//놀이방 기준 계산


        //출력화면 떠블클릭시 요약 화면 출력
        resultOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 700) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(Calcul.this);
                    dlg.setTitle("요약"); //제목
                    dlg.setMessage("몸무게 : "+weight[0]+"\n시간 비용 : "+moneyHour+"  분당 비용 : "+moneyHalf+"\n______추가 서비스 ___________"+
                            "\n     픽업 : "+addOnsPickUp[0] +"\n     드랍 : "+addOnsDrop[0]+"\n     목욕 : "+addOnShower[0]+"\n     총 : "+useTime[0]); // 메시지

                    dlg.show();
                }
                mLastClickTime = SystemClock.elapsedRealtime();

            }
        });

        //입실시간 떠블클릭시 하하 출력
        textUseTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //더블클릭시 다이어로그 뷰 출력
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1500) {
                    Toast.makeText(getApplicationContext(), "하하", Toast.LENGTH_SHORT).show();
                }
                mLastClickTime = SystemClock.elapsedRealtime();


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

        tp = (TimePicker) findViewById(R.id.tp); //타임피커
        messageImageView = findViewById(R.id.messageImage);
        messageImageView.setImageResource(R.drawable.message);
        calculatorImageView = findViewById(R.id.calculatorImage);
        calculatorImageView.setImageResource(R.drawable.calculator2);
        currentTime = (TextView) findViewById(R.id.currentTime);
        resultOutput = (TextView) findViewById(R.id.resultOutput);
        textUseTime = (TextView) findViewById(R.id.useTime);
        numberPickerWeight = (NumberPicker) findViewById(R.id.numberPicker);
        checkBox1 = (CheckBox) findViewById(R.id.checkBox1);
        checkBox2 = (CheckBox) findViewById(R.id.checkBox2);
        checkBox3 = (CheckBox) findViewById(R.id.checkBox3);
        EditText1 = (EditText) findViewById(R.id.EditText1);
        EditText2 = (EditText) findViewById(R.id.EditText2);
        EditText3 = (EditText) findViewById(R.id.EditText3);

        resultOutput.setText("몸무게를 설정해주세요.");
        //타임피커
        tp.setIs24HourView(true);


        //부가 기능 사전 설정
        EditText1.setEnabled(false);
        EditText1.setHint("비활성화");

        EditText2.setEnabled(false);
        EditText2.setHint("비활성화");

        EditText3.setEnabled(false);
        EditText3.setHint("비활성화");


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
                useTime[0] = 0;
                useHour[0] = 0;

            }
        });


        //타임피커
            //현재 시간을 타임 피커의 초기값으로 사용
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {


                //todo if(분>=30,무게*2,무게)
                if ((CurrentHalf - minute) < 0) {
                    //moneyHalf = weight[0] * 2;
                    moneyHour =( CurrentTime - hourOfDay - 1) * weight[0] * 2; //시간당 돈;
                    Half = CurrentHalf - minute + 60;
                } else {

                    moneyHour = (CurrentTime - hourOfDay) * weight[0] * 2; //시간당 돈;
                    Half = CurrentHalf - minute;
                }

                if (Half >= 30) {
                    moneyHalf = weight[0] * 2;

                }else{
                    moneyHalf = weight[0];
                }

                useHour[0] = moneyHour + moneyHalf; //시간+분 돈 더하기

                useTime[0] = (useHour[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
                NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); //컴마 생성
                resultOutput.setText(String.format(" %s", moneyFormat.format( useTime[0])));      //출력


            }
        });
        //타임피커 끝


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
                //에딧택스트 변화감지
                EditText1.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String input = EditText1.getText().toString();
                        int plus;
                        if(input.equals("")){
                            addOnsPickUp[0]=5000;
                        }else{
                            plus = Integer.parseInt(input);
                            addOnsPickUp[0]=(plus*1000)+5000;
                        }

                        useTime[0] = (useHour[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
                        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); //컴마 생성
                        resultOutput.setText(String.format(" %s", moneyFormat.format(useTime[0])));      //출력

                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });


                useTime[0] = (useHour[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
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

                EditText2.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String input = EditText2.getText().toString();
                        int plus;
                        if(input.equals("")){

                            addOnsDrop[0]=5000;
                        }else{
                            plus = Integer.parseInt(input);
                            addOnsDrop[0]=(plus*1000)+5000;

                        }

                        useTime[0] = (useHour[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
                        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); //컴마 생성
                        resultOutput.setText(String.format(" %s", moneyFormat.format(useTime[0])));      //출력

                    }

                    @Override
                    public void afterTextChanged(Editable s) {}
                });

                useTime[0] = (useHour[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
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
                EditText3.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {


                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String input = EditText3.getText().toString();
                        int plus;
                        if(input.equals("")){

                            addOnShower[0]=15000;
                        }else{
                            plus = Integer.parseInt(input);
                            addOnShower[0]=plus;

                        }

                        useTime[0] = (useHour[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
                        NumberFormat moneyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); //컴마 생성
                        resultOutput.setText(String.format(" %s", moneyFormat.format(useTime[0])));      //출력

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                useTime[0] = ((int) useHour[0] + addOnsPickUp[0] + addOnsDrop[0] + addOnShower[0]);
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

    public static String getCurrentDate_half() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_half, Locale.getDefault());
        return format.format(currentTime);
    }

    public static String getCurrentDate_date() {
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat format = new SimpleDateFormat(format_date, Locale.getDefault());
        return format.format(currentTime);
    }

    //실시간 시간 출력
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
                        currentTime.setText(getCurrentDate_date());

                        CurrentTime = Integer.parseInt(getCurrentDate_hour());  //현재시간
                        CurrentHalf = Integer.parseInt(getCurrentDate_half());  //현재분
                    }
                });
            }
        }
    };


}