package com.sujikim.ttt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.sujikim.ttt.model.Jackets;
import com.sujikim.ttt.model.LongT;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class LongTActivity extends AppCompatActivity {
    Realm realm;
    Bitmap realmImage;
    RealmResults<LongT> longTs;
    ArrayList<Bitmap> realmImages = new ArrayList<>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_t);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final GridView gridView = (GridView) findViewById(R.id.grid_view);

        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                longTs = realm.where(LongT.class).findAll();
                for(LongT lt : longTs) {
                    realmImage = byteToBitmap(lt.getImageData());
                    realmImages.add(realmImage);
                }

            }
        });
        gridView.setAdapter(new ImageAdapter(context,realmImages));

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
