package com.sujikim.ttt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
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
import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class RecommendActivity extends AppCompatActivity {

    // 평균 기온 가져오기
    private double averageTemp;
    // GPS를 위한 변수들
    private LocationManager locationManager;
    private double lat = 37.6291;
    private double lon = 127.0897;

    // 화면 뿌려주기 위한 변수
    private ImageView topOne;
    private ImageView topTwo;
    private ImageView topThree;
    private ImageView bottomOne;
    private ImageView bottomTwo;
    private ImageView bottomThree;
    private ImageView outerOne;
    private ImageView outerTwo;
    private ImageView outerThree;
    private TextView showAverage;



    // Random한 숫자들 저장
    private int[] randomShortT = {0,0,0};
    private int[] randomShortPants = {0,0,0};
    private int[] randomLongT= {0,0,0};
    private int[] randomLongPants= {0,0,0};
    private int[] randomPadding= {0,0,0};
    private int[] randomJackets= {0,0,0};

    // 램덤한 숫자 배정을 위해 준비
    private Random random = new Random();
    private int lpCount = 0;
    private int ltCount = 0;
    private int spCount = 0;
    private int stCount = 0;
    private int padCount = 0;
    private int jacCount = 0;

    // DB에서 옷을 가져오기 위한 변수들
    private Realm realm;
    LongPants longPantses;
    LongT longTs;
    ShortPants shortPantses;
    ShortT shortTs;
    Padding paddings;
    Jackets jacketses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        topOne = (ImageView)findViewById(R.id.top1);
        topTwo = (ImageView)findViewById(R.id.top2);
        topThree = (ImageView)findViewById(R.id.top3);
        bottomOne = (ImageView)findViewById(R.id.bottom1);
        bottomTwo = (ImageView)findViewById(R.id.bottom2);
        bottomThree = (ImageView)findViewById(R.id.bottom3);
        outerOne = (ImageView)findViewById(R.id.outer1);
        outerTwo = (ImageView)findViewById(R.id.outer2);
        outerThree = (ImageView)findViewById(R.id.outer3);

        showAverage = (TextView)findViewById(R.id.averageTempText);

        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                Number buf;
                buf = realm.where(LongPants.class).max("num");
                if (buf != null) {
                    lpCount = buf.intValue();
                }
                buf =  realm.where(LongT.class).max("num");
                if (buf != null) {
                    ltCount = buf.intValue();
                }
                buf =  realm.where(ShortPants.class).max("num");
                if (buf != null) {
                    spCount = buf.intValue();
                }
                buf =  realm.where(ShortT.class).max("num");
                if (buf != null) {
                    stCount = buf.intValue();
                }
                buf =  realm.where(Padding.class).max("num");
                if (buf != null) {
                    padCount = buf.intValue();
                }
                buf =  realm.where(Jackets.class).max("num");
                if (buf != null) {
                    jacCount = buf.intValue();
                }
                if (buf != null) {
                    getRandomNumber();
                    getClothes();
                }
            }
        });
//        getRandomNumber();
//        getClothes();
//
        currentMyLocation();
        getWeatherData(lat,lon);
    }

    //날씨에 따라 랜덤한 숫자를 가져오는 함수 + 추운날씨에는 text도 설정하기
    protected void getRandomNumber() {
        // 27도 이상일 경우 반팔, 반바지
        // 23도에서 26도 사이일 경우도 반팔, 반바지
        if (averageTemp >= 23) {
            for(int i = 0; i < 3; i++) {
                randomShortT[i] = random.nextInt(stCount) + 1;
                randomShortPants[i] = random.nextInt(spCount) + 1;
            }
        }

        // 20도에서 22도는 반팔두개, 긴팔 한개/ 긴바지 3개
        else if ((averageTemp >= 20) && (averageTemp <= 22)) {
            for(int i = 0; i < 2; i++) { // 반팔 두개 추출
                randomShortT[i] = random.nextInt(stCount) + 1;
            }
            randomShortT[2] = 0;
            randomLongT[0] = random.nextInt(ltCount) + 1;
            for(int i = 0; i < 3; i++) { // 긴바지 3개 추출
                randomLongPants[i] = random.nextInt(lpCount) + 1;
            }
        }


        // 17도에서 19도는 긴팔 세개 긴바지 3개
        else if ((averageTemp >= 17) && (averageTemp <= 19)) {
            for(int i = 0; i < 3; i++) {
                randomLongT[i] = random.nextInt(ltCount) + 1;
                randomLongPants[i] = random.nextInt(lpCount) + 1;
            }
        }

        // 12도에서 16도는 자켓 3개 긴팔3개 긴바지3개
        // 10도에서 11도는 자켓 3개 긴팔3개 긴바지 3개
        // 6도에서 9도는 자켓 3개, 긴팔3개 긴바지 3개
        else if ((averageTemp >= 6) && (averageTemp <= 16)) {
            for(int i = 0; i < 3; i++) {
                randomLongT[i] = random.nextInt(ltCount) + 1;
                randomLongPants[i] = random.nextInt(lpCount) + 1;
                randomJackets[i] = random.nextInt(jacCount) + 1;
            }
        }

        // 5도 이하는 패딩 3개, 긴팔3개 긴바지3개
        else if(averageTemp <= 5) {
            for(int i = 0; i < 3; i++) {
                randomLongT[i] = random.nextInt(ltCount) + 1;
                randomLongPants[i] = random.nextInt(lpCount) + 1;
                randomPadding[i] = random.nextInt(padCount) + 1;
            }
        }
    }

    //옷들을 DB에서 가져오기
    protected void getClothes() {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                // 27도 이상일 경우 반팔, 반바지
                // 23도에서 26도 사이일 경우도 반팔, 반바지
                if (averageTemp >= 23) {
                    shortTs = realm.where(ShortT.class).equalTo("num",randomShortT[0]).findFirst();
                    topOne.setImageBitmap(byteToBitmap(shortTs.getImageData()));
                    shortTs = realm.where(ShortT.class).equalTo("num",randomShortT[1]).findFirst();
                    topTwo.setImageBitmap(byteToBitmap(shortTs.getImageData()));
                    shortTs = realm.where(ShortT.class).equalTo("num",randomShortT[2]).findFirst();
                    topThree.setImageBitmap(byteToBitmap(shortTs.getImageData()));

                    shortPantses = realm.where(ShortPants.class).equalTo("num",randomShortPants[0]).findFirst();
                    bottomOne.setImageBitmap(byteToBitmap(shortPantses.getImageData()));
                    shortPantses = realm.where(ShortPants.class).equalTo("num",randomShortPants[1]).findFirst();
                    bottomTwo.setImageBitmap(byteToBitmap(shortPantses.getImageData()));
                    shortPantses = realm.where(ShortPants.class).equalTo("num",randomShortPants[2]).findFirst();
                    bottomThree.setImageBitmap(byteToBitmap(shortPantses.getImageData()));
                }

                // 20도에서 22도는 반팔두개, 긴팔 한개/ 긴바지 3개
                else if ((averageTemp >= 20) && (averageTemp <= 22)) {
                    shortTs = realm.where(ShortT.class).equalTo("num",randomShortT[0]).findFirst();
                    topOne.setImageBitmap(byteToBitmap(shortTs.getImageData()));
                    shortTs = realm.where(ShortT.class).equalTo("num",randomShortT[1]).findFirst();
                    longTs = realm.where(LongT.class).equalTo("num",randomLongT[0]).findFirst();
                    topThree.setImageBitmap(byteToBitmap(longTs.getImageData()));

                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[0]).findFirst();
                    bottomOne.setImageBitmap(byteToBitmap(longPantses.getImageData()));
                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[1]).findFirst();
                    bottomTwo.setImageBitmap(byteToBitmap(longPantses.getImageData()));
                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[2]).findFirst();
                    bottomThree.setImageBitmap(byteToBitmap(longPantses.getImageData()));
                }


                // 17도에서 19도는 긴팔 세개 긴바지 3개
                else if ((averageTemp >= 17) && (averageTemp <= 19)) {
                    longTs = realm.where(LongT.class).equalTo("num", randomLongT[0]).findFirst();
                    topOne.setImageBitmap(byteToBitmap(longTs.getImageData()));
                    longTs = realm.where(LongT.class).equalTo("num", randomLongT[1]).findFirst();
                    topTwo.setImageBitmap(byteToBitmap(longTs.getImageData()));
                    longTs = realm.where(LongT.class).equalTo("num", randomLongT[2]).findFirst();
                    topThree.setImageBitmap(byteToBitmap(longTs.getImageData()));

                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[0]).findFirst();
                    bottomOne.setImageBitmap(byteToBitmap(longPantses.getImageData()));
                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[1]).findFirst();
                    bottomTwo.setImageBitmap(byteToBitmap(longPantses.getImageData()));
                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[2]).findFirst();
                    bottomThree.setImageBitmap(byteToBitmap(longPantses.getImageData()));
                }

                // 12도에서 16도는 자켓 3개 긴팔3개 긴바지3개
                // 10도에서 11도는 자켓 3개 긴팔3개 긴바지 3개
                // 6도에서 9도는 자켓 3개, 긴팔3개 긴바지 3개
                else if ((averageTemp >= 6) && (averageTemp <= 16)) {
                    longTs = realm.where(LongT.class).equalTo("num", randomLongT[0]).findFirst();
                    topOne.setImageBitmap(byteToBitmap(longTs.getImageData()));
                    longTs = realm.where(LongT.class).equalTo("num", randomLongT[1]).findFirst();
                    topTwo.setImageBitmap(byteToBitmap(longTs.getImageData()));
                    longTs = realm.where(LongT.class).equalTo("num", randomLongT[2]).findFirst();
                    topThree.setImageBitmap(byteToBitmap(longTs.getImageData()));

                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[0]).findFirst();
                    bottomOne.setImageBitmap(byteToBitmap(longPantses.getImageData()));
                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[1]).findFirst();
                    bottomTwo.setImageBitmap(byteToBitmap(longPantses.getImageData()));
                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[2]).findFirst();
                    bottomThree.setImageBitmap(byteToBitmap(longPantses.getImageData()));

                    jacketses = realm.where(Jackets.class).equalTo("num", randomJackets[0]).findFirst();
                    outerOne.setImageBitmap(byteToBitmap(jacketses.getImageData()));
                    jacketses = realm.where(Jackets.class).equalTo("num", randomJackets[1]).findFirst();
                    outerTwo.setImageBitmap(byteToBitmap(jacketses.getImageData()));
                    jacketses = realm.where(Jackets.class).equalTo("num", randomJackets[2]).findFirst();
                    outerThree.setImageBitmap(byteToBitmap(jacketses.getImageData()));
                }

                // 5도 이하는 패딩 3개, 긴팔3개 긴바지3개
                else if(averageTemp <= 5) {
                    longTs = realm.where(LongT.class).equalTo("num", randomLongT[0]).findFirst();
                    topOne.setImageBitmap(byteToBitmap(longTs.getImageData()));
                    longTs = realm.where(LongT.class).equalTo("num", randomLongT[1]).findFirst();
                    topTwo.setImageBitmap(byteToBitmap(longTs.getImageData()));
                    longTs = realm.where(LongT.class).equalTo("num", randomLongT[2]).findFirst();
                    topThree.setImageBitmap(byteToBitmap(longTs.getImageData()));

                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[0]).findFirst();
                    bottomOne.setImageBitmap(byteToBitmap(longPantses.getImageData()));
                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[1]).findFirst();
                    bottomTwo.setImageBitmap(byteToBitmap(longPantses.getImageData()));
                    longPantses = realm.where(LongPants.class).equalTo("num",randomLongPants[2]).findFirst();
                    bottomThree.setImageBitmap(byteToBitmap(longPantses.getImageData()));

                    paddings = realm.where(Padding.class).equalTo("num", randomPadding[0]).findFirst();
                    outerOne.setImageBitmap(byteToBitmap(paddings.getImageData()));
                    paddings = realm.where(Padding.class).equalTo("num", randomPadding[1]).findFirst();
                    outerTwo.setImageBitmap(byteToBitmap(paddings.getImageData()));
                    paddings = realm.where(Padding.class).equalTo("num", randomPadding[2]).findFirst();
                    outerThree.setImageBitmap(byteToBitmap(paddings.getImageData()));
                }
            }
        });

    }

    protected Bitmap byteToBitmap(byte[] original) {
        //        ByteArrayInputStream in = new ByteArrayInputStream(picture);
        Bitmap bitmap = BitmapFactory.decodeByteArray(original, 0, original.length);
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    // 위치 정보 확인 메서드
    private void currentMyLocation() {

        // LocationManager 객체 생성 (LOCATION_SERVICE 사용)
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // GPSListener 객체 생성 (LocationListener 인터페이스 정의 필요)
        RecommendActivity.GPSListener gpsListener = new RecommendActivity.GPSListener();
        long minTime = 1000; //1마다
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

                Toast.makeText(getApplicationContext(), "마지막 위치 : " + "Latitude : " + lat + "\nLongitude:" + lon, Toast.LENGTH_LONG).show();
                getWeatherData(lat,lon);
            }
        } catch(SecurityException ex) {
            ex.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "위치 확인 시작", Toast.LENGTH_SHORT).show();
    }

    // LocationListener 정의
    private class GPSListener implements LocationListener {

        // LocationManager 에서 위치정보가 변경되면 호출
        public void onLocationChanged(Location location) {
            lat = location.getLatitude();
            lon = location.getLongitude();

            String msg = "Latitude : "+ lat + "\nLongitude:"+ lon;
            Log.i("GPSListener", msg);


            Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
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
        RecommendActivity.ReceiveWeatherTask receiverUseTask = new RecommendActivity.ReceiveWeatherTask();
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

                String maxTemp = "";
                String minTemp = "";


                try {
                    minTemp = result.getJSONObject("main").getString("temp_min");
                    maxTemp = result.getJSONObject("main").getString("temp_max");



                    averageTemp = Double.parseDouble(minTemp) + ((Double.parseDouble(maxTemp) - Double.parseDouble(minTemp))/2.0);
                    showAverage.setText("  오늘의 평균 기온은   " + averageTemp +"°C 입니다. ");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String msg = "최저" +minTemp + "/ 최고:"+maxTemp;
            }
        }
    }


}
