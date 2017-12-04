package com.sujikim.ttt;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sujikim.ttt.model.DBHelper;
import com.sujikim.ttt.model.LongT;

import java.io.ByteArrayOutputStream;

import io.realm.Realm;

public class AddClothesActivity extends AppCompatActivity implements View.OnClickListener{
    private Button takePicture;
    private TextView resultShow;
    private ImageView picture;
    private Bitmap thumbnail;


    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);

        realm = Realm.getDefaultInstance();

        takePicture = (Button)findViewById(R.id.btnTakePicture);
        resultShow = (TextView)findViewById(R.id.resultTextview);
        picture = (ImageView)findViewById(R.id.showTakenPicture);

        takePicture.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTakePicture:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                onCaptureImageResult(data);
            }
        }
    }
    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap)data.getExtras().get("data");
        picture.setImageBitmap(thumbnail);
        // 찍어온 사진을 DB에 저장하는 거 연습
        picture.setDrawingCacheEnabled(true);
        picture.buildDrawingCache();
        Bitmap bitmap = picture.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 200, baos);
        byte[] pictureData = baos.toByteArray();

        // LongT DB에 저장
//        LongT longT = new LongT();
//        longT.setNum(number);
//        longT.setImageData(pictureData);
        DBHelper helper = new DBHelper(realm);
        if(helper.saveLongT(pictureData)) {
            Toast.makeText(this, "DB에 사진 저장 성공!", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "DB에 사진 저장 실패!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }
}
