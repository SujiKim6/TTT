package com.sujikim.ttt;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.GridView;

import com.sujikim.ttt.model.ShortT;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class ShortTActivity extends AppCompatActivity {
    Realm realm;
    Bitmap realmImage;
    RealmResults<ShortT> shortTs;
    ArrayList<Bitmap> realmImages = new ArrayList<>();
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_t);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final GridView gridView = (GridView) findViewById(R.id.grid_view);

        realm = Realm.getDefaultInstance();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                shortTs = realm.where(ShortT.class).findAll();
                for(ShortT st : shortTs) {
                    realmImage = byteToBitmap(st.getImageData());
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
