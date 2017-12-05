package com.sujikim.ttt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class RecommendActivity extends AppCompatActivity {

    private double averageTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
    }

    public void setAverageTemp(double averageTemp) {
        this.averageTemp = averageTemp;
    }
}
