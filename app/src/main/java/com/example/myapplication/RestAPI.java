package com.example.myapplication;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RestAPI {
    Disposable backgroundTask;
    HttpURLConnection conn;
    URL url;
    String urlString="https://talkapi.lgcns.com/request/kakao.json";//요청 보낼 주소
    // backgroundTask를 실행하는 메소드

    public void sampleMethod() {
        // task에서 반환할 Hashmap
        HashMap<String, String> map = new HashMap<>();

        //onPreExecute(task 시작 전 실행될 코드 여기에 작성)
        System.out.println("백그라운드 시작전");

        JSONObject json = new JSONObject();

        try {
            url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");

            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");

            //header
            conn.setRequestProperty("authToken","DV42BI+mL8AzHHw2mrWcFQ==");
            conn.setRequestProperty("serverName","ding_api");
            conn.setRequestProperty("paymentType","P");

            conn.setDoOutput(true);
            conn.setDoInput(true);


        } catch (Exception e) {
            e.printStackTrace();
        }

        backgroundTask = Observable.fromCallable(() -> {
            //doInBackground(task에서 실행할 코드 여기에 작성)
            System.out.println("백그라운드 실행");
            map.put("key","value");
            //요청 보낼 body
            json.put("service","2210077160");
            json.put("mobile","01040788094");
            json.put("message","이용해주셔서 감사합니다.\n\n"+
                    "추후 더 나은 서비스 운영과 의견 수렴 및 반영을 위해 5분만 시간을 내어주셔서 설문에 응해주시면 감사하겠습니다!");
            json.put("template","10011");

            //JSONObject buttons_object=new JSONObject();
            //buttons_object.put("name","설문조사");
            //buttons_object.put("url","https://forms.gle/Qgs8YSM1PbHXwTDe7");
            //json.put("buttons",buttons_object);


            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(json.toString()); // 버퍼에 담기
            bw.flush(); // 버퍼에 담긴 데이터 전달
            bw.close();

            // 서버로부터 데이터 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            while((line = br.readLine()) != null) { // 읽을 수 있을 때 까지 반복
                sb.append(line);
            }

            System.out.println(sb.toString());

            conn.disconnect();
            return map;

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<HashMap<String, String>>() {
            @Override
            public void accept(HashMap<String, String> map) {
                //onPostExecute(task 끝난 후 실행될 코드 여기에 작성)
                System.out.println("백그라운드 끝난후");
                System.out.println(map.get("key"));
                backgroundTask.dispose();
            }
        });
    }

}


