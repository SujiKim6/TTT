package com.sujikim.ttt;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sujikim.ttt.model.Jackets;
import com.sujikim.ttt.model.LongPants;
import com.sujikim.ttt.model.LongT;
import com.sujikim.ttt.model.Padding;
import com.sujikim.ttt.model.ShortPants;
import com.sujikim.ttt.model.ShortT;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    // 옷 등록 버튼
    private Button addClothes;

    // 옷추천을 위한 변수들
    private double average;
    private Button recommendClothes;

    // 날씨 출력 변수들
    private TextView currentCity;
    private TextView currentTemperature;
    private TextView highTemperature;
    private TextView lowTemperature;

    // GPS를 위한 변수들
    private LocationManager locationManager;
    private double lat = 37.6291;
    private double lon = 127.0897;

    // 나의 옷장에 들어가기 위한 변수
    private Button closet;

    // Realm DB 날리기 위해 임시방편으로 사용
//    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addClothes = (Button)findViewById(R.id.addClothesBtn);

        currentCity = (TextView)findViewById(R.id.cityName);
        currentTemperature = (TextView)findViewById(R.id.currentTemp);
        highTemperature = (TextView)findViewById(R.id.highTemp);
        lowTemperature = (TextView)findViewById(R.id.lowTemp);
        closet = (Button) findViewById(R.id.btnCloset);

        recommendClothes = (Button)findViewById(R.id.clothes);

        // Realm DB 날리기 위해 임시방편으로 사용
//        realm = Realm.getDefaultInstance();
//        final RealmResults<LongPants> lp =  realm.where(LongPants.class).findAll();
//        final RealmResults<LongT> lt =  realm.where(LongT.class).findAll();
//        final RealmResults<ShortPants> sp =  realm.where(ShortPants.class).findAll();
//        final RealmResults<ShortT> st =  realm.where(ShortT.class).findAll();
//        final RealmResults<Padding> pad =  realm.where(Padding.class).findAll();
//        final RealmResults<Jackets> jac =  realm.where(Jackets.class).findAll();
//
//        realm.executeTransaction(new Realm.Transaction(){
//            @Override
//            public void execute(Realm realm){
//                lp.deleteAllFromRealm();
//                lt.deleteAllFromRealm();
//                sp.deleteAllFromRealm();
//                st.deleteAllFromRealm();
//                pad.deleteAllFromRealm();
//                jac.deleteAllFromRealm();
//            }
//        });



        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent addClothesIntent = new Intent(MainActivity.this, AddClothesActivity.class);
                MainActivity.this.startActivity(addClothesIntent);
            }
        });
        closet.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent closetIntent = new Intent(MainActivity.this, ClosetActivity.class);
                MainActivity.this.startActivity(closetIntent);
            }
        });
        recommendClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recommendIntent = new Intent(MainActivity.this, RecommendActivity.class);
                MainActivity.this.startActivity(recommendIntent);
            }
        });

        currentMyLocation();
    }

    // 위치 정보 확인 메서드
    public void currentMyLocation() {

        // LocationManager 객체 생성 (LOCATION_SERVICE 사용)
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // GPSListener 객체 생성 (LocationListener 인터페이스 정의 필요)
        GPSListener gpsListener = new GPSListener();
        long minTime = 10000; //1초마다
        float minDistance = 0;

        try {
            // GPS를 이용한 위치 요청
            locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 네트워크를 이용한 위치 요청
            locationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER,
                    minTime,
                    minDistance,
                    gpsListener);

            // 위치요청을 한 상태에서 위치추적되는 동안 먼저 최근 위치를 조회해서 set
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                lat = lastLocation.getLatitude();
                lon = lastLocation.getLongitude();

//                Toast.makeText(getApplicationContext(), "마지막 위치 : " + "Latitude : " + lat + "\nLongitude:" + lon, Toast.LENGTH_LONG).show();
                getWeatherData(lat,lon);
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }

//        Toast.makeText(getApplicationContext(), "위치 확인 시작", Toast.LENGTH_SHORT).show();
    }

    // LocationListener 정의
    private class GPSListener implements LocationListener {

        // LocationManager 에서 위치정보가 변경되면 호출
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();

            String msg = "Latitude : "+ lat + "\nLongitude:"+ lon;
            Log.i("GPSListener", msg);


//            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
            getWeatherData(lat,lon);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    // 위도 경도에 따른 현재 온도 가져오기
    private void getWeatherData (double lat, double lng) {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+
                lng+"&appid=2207f2763b70f27dae0bd41076b886a0&units=metric&lang=kr";
        ReceiveWeatherTask receiverUseTask = new ReceiveWeatherTask();
        receiverUseTask.execute(url);
    }

    private class ReceiveWeatherTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected  void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected JSONObject doInBackground(String... datas) {
            try {
                HttpURLConnection conn = (HttpURLConnection) new URL(datas[0]).openConnection();
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.connect();

                if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream is = conn.getInputStream();
                    InputStreamReader reader = new InputStreamReader(is);
                    BufferedReader in = new BufferedReader(reader);

                    String readed;
                    while ((readed = in.readLine()) != null) {
                        JSONObject jObject = new JSONObject(readed);
                        return jObject;
                    }
                } else {
                    return null;
                }
                return null;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(JSONObject result) {
//        Log.i(TAG, result.toString());

            // JSON객체에서 온도를 가져와서 결과값 화면에 출력하기
            if (result != null) {
                String description = "";
                String maxTemp = "";
                String minTemp = "";
                String city = "";
                String curTemp = "";

                try {
                    minTemp = result.getJSONObject("main").getString("temp_min");
                    maxTemp = result.getJSONObject("main").getString("temp_max");
                    curTemp = result.getJSONObject("main").getString("temp");

                    description = result.getJSONArray("weather").getJSONObject(0).getString("description");
                    city = result.getString("name");

                    calculateAverage(Double.parseDouble(minTemp), Double.parseDouble(maxTemp));

                    currentCity.setText(String.valueOf(city));
                    currentTemperature.setText(String.valueOf(curTemp));
                    highTemperature.setText("   "+String.valueOf(maxTemp)+"  ");
                    lowTemperature.setText("   "+String.valueOf(minTemp)+"  ");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String msg = description + "최저" +minTemp + "/ 최고:"+maxTemp;
            }
        }
    }

    private void calculateAverage(double min, double max) {
        average = min + ((max-min)/2.0);
    }

    // Realm DB 날리기 위해 임시방편으로 사용
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        realm.close();
//    }

}
