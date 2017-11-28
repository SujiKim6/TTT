package com.sujikim.ttt;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    Button addClothes;
    TextView degree;

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
