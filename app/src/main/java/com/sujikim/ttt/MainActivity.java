package com.sujikim.ttt;

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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button addClothes;
    TextView degree;
    MainActivity mContext;

    // 현재 위치 받아오기 위해 변수 선언
    LocationManager locationManager;
    // 현재 GPS 사용유무
    boolean isGPSEnabled = false;
    // 네트워크 사용유무
    boolean isNetworkEnabled = false;
    // GPS 상태값
    boolean isGetLocation = false;
    Location location;
    double lat; //위도
    double lon; //경도

    // 최소 GPS 정보 업데이트 거리 1000미터
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 1000;
    // 최소 업데이트 시간 1분
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

    // 현재 위치를 가져오는 함수 선언
    public Location getLocation() {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            // GPS 정보 가져오기
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            //현재 네트워크 상태 값 알아오기
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
                // GPS와 네트워크 사용이 가능하지 않을때 소스 구현
            }
            else {
                this.isGetLocation = true;
                //네트워크 정보로부터 위치값 가져오기
                if (isNetworkEnabled) {
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                    if (locationManager != null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if (location != null) {
                            // 위도 경도 저장
                            lat = location.getLatitude();
                            lon = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
                        if (locationManager != null) {
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (location != null) {
                                lat = location.getLatitude();
                                lon = location.getLongitude();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    private LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Toast("onLocationChanged");
            getWeatherData(location.getLatitude(), location.getLongitude());
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
            //Toast("onStatusChanged");
        }

        @Override
        public void onProviderEnabled(String s) {
            //Toast("onProviderEnabled");
        }

        @Override
        public void onProviderDisabled(String s) {
            //Toast("onProviderDisabled");
        }
    };

    private void getWeatherData (double lat, double lng) {
        String url = "http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+
                lon+"&units=metric&appid=10f6fc8cfbff125d7da532526029b553";
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
//                        String result = jObject.getJSONArray("weather").getJSONObject(0).getString("icon");
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
            if (result != null) {
                String maxTemp = "";
                String minTemp = "";
                String description = "";

                try {
                    minTemp = result.getJSONObject("main").getString("temp_min");
                    maxTemp = result.getJSONObject("main").getString("temp_max");
                    description = result.getJSONArray("weather").getJSONObject(0).getString("description");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String msg = description + "최저" +minTemp + "/ 최고:"+maxTemp;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addClothes = (Button)findViewById(R.id.addClothesBtn);

        addClothes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog  .setTitle("알림!")
                        .setMessage("**조끼 입력시 지퍼를 열고 촬영해 주세요**")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "사진을 촬영하세요", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(i, 0);

                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        });
                dialog.create();
                dialog.show();
            }
        });

        degree = (TextView)findViewById(R.id.weatherText);
//        find_weather();
//        getWeatherData(lat, lon);




    }

//    public void find_weather(){
//        String url = "api.openweathermap.org/data/2.5/weather?q=Seoul";
//
//        JsonObjectRequest jor = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONObject main_object = response.getJSONObject("main");
//                    JSONArray array = response.getJSONArray("weather");
//                    JSONObject object = array.getJSONObject(0);
//                    String temp = String.valueOf(main_object.getDouble("temp"));
//                    String description = object.getString("description");
//                    String city = response.getString("name");
//
//                    double temp_int = Double.parseDouble(temp);
//                    double temp_degree = (temp_int - 273.15);
//
//                    degree.setText(String.valueOf(temp_degree));
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        }
//        );
//        RequestQueue queue = Volley.newRequestQueue(this);
//        queue.add(jor);
//    }


}
