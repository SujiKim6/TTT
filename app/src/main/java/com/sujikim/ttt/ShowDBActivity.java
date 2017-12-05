package com.sujikim.ttt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import io.realm.Realm;

public class ShowDBActivity extends AppCompatActivity {
    Realm realm;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db);

        imageView = (ImageView)findViewById(R.id.resultImage);
        realm = Realm.getDefaultInstance();

        showResult();

    }

    protected void showResult(){


    }
}
