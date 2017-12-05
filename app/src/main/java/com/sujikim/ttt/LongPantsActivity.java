package com.sujikim.ttt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.sujikim.ttt.model.LongPants;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class LongPantsActivity extends AppCompatActivity {
    Realm realm;
    Bitmap realmImage;
    RealmResults<LongPants> longPants;
    ArrayList<Bitmap> realmImages = new ArrayList<>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_pants);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
}
