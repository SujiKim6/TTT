package com.sujikim.ttt;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;

import android.provider.MediaStore;
import android.provider.Settings;

import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements LocationListener {

    // 옷등록 버튼
    private Button addClothes;

    // 날씨 API 출력
    private TextView averageTemperature;

    // 지역 출력
    private TextView currentplace;

    private LocationManager locationManager;
    private double lat;
    private double lon;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addClothes = (Button) findViewById(R.id.addClothesBtn);
        averageTemperature = (TextView) findViewById(R.id.averTempText);
        currentplace = (TextView) findViewById(R.id.locationText);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 1, this);

        // 옷을 추가하기 위한 버튼을 클릭시 실행
        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                // 알림창 띄우기
                dialog.setTitle("알림!")
                        .setMessage("**조끼 입력시 지퍼를 열고 촬영해 주세요**")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "사진을 촬영하세요", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(i, 0);

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.create();
                dialog.show();
            }
        });

        getWeatherData(lat,lon);
    }
    @Override
    public void onLocationChanged(Location location) {
        String msg = "New Latitude: " + location.getLatitude()
                + "New Longitude: " + location.getLongitude();
        lat = location.getLatitude();
        lon = location.getLongitude();
        Toast.makeText(getBaseContext(), msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

        Toast.makeText(getBaseContext(), "Gps is turned on!! ",
                Toast.LENGTH_SHORT).show();
    }

    // GPS가 꺼져있는지 확인한다.
    @Override
    public void onProviderDisabled(String provider) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(intent);
        Toast.makeText(getBaseContext(), "Gps is turned off!! ",
                Toast.LENGTH_SHORT).show();
    }




    // 위도 경도에 따른 현재 온도 가져오기
    private void getWeatherData (double lat, double lng) {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+
                lng+"&appid=10f6fc8cfbff125d7da532526029b553&units=imperial&lang=kr";
//        String url = "http://api.openweathermap.org/data/2.5/weather?lat=37.5665&lon=126.978&appid=10f6fc8cfbff125d7da532526029b553&units=metric&lang=kr";
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

                try {
                    minTemp = result.getJSONObject("main").getString("temp_min");
                    maxTemp = result.getJSONObject("main").getString("temp_max");
                    description = result.getJSONArray("weather").getJSONObject(0).getString("description");

                    double averDouble = (Double.parseDouble(minTemp) + Double.parseDouble(maxTemp))/2.0;
                    String average = Double.toString(averDouble);

                    averageTemperature.setText(String.valueOf(average));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String msg = description + "최저" +minTemp + "/ 최고:"+maxTemp;
            }
        }
    }


}
