package com.sujikim.ttt;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AddClothesActivity extends AppCompatActivity implements View.OnClickListener {

    Button Button_Camera;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);

        Button_Camera = (Button)findViewById(R.id.camera);
        imageView = (ImageView)findViewById(R.id.view);
        Button_Camera.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        imageView.setImageURI(data.getData());
    }
}
