package com.sujikim.ttt;

import android.content.DialogInterface;
import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //ImageView imageView;
    Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addButton = (Button)findViewById(R.id.addButton);
       // imageView = (ImageView)findViewById(R.id.view);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view){

                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog  .setTitle("알림!")
                        .setMessage("**조끼 입력시 지퍼를 열고 촬영해 주세요**")
                        .setPositiveButton("ok", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "사진을 촬영하세요", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(i, 0);

//                                @Override
//                                protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//                                    imageView.setImageURI(data.getData());
//                                }

                            }
                        })
//                        .setNeutralButton("no", new DialogInterface.OnClickListener(){
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(MainActivity.this, "사진을 촬영하세요", Toast.LENGTH_SHORT).show();
//                            }
//                        })
                        .setNegativeButton("cancle", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(MainActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
                            }
                        });

                dialog.create();
                dialog.show();
            }
        });

    }

}




//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        Button addButton = (Button)findViewById(R.id.addButton);
//
//        addButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view){
//
//                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
//                dialog  .setTitle("알림!")
//                        .setMessage("**조끼 입력시 지퍼를 열고 촬영해 주세요**")
//                        .setPositiveButton("ok", new DialogInterface.OnClickListener(){
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(MainActivity.this, "사진을 촬영하세요", Toast.LENGTH_SHORT).show();
//                                Intent addIntent = new Intent(MainActivity.this, AddClothesActivity.class);
//                                MainActivity.this.startActivity(addIntent);
//                            }
//                        })
////                        .setNeutralButton("no", new DialogInterface.OnClickListener(){
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                Toast.makeText(MainActivity.this, "사진을 촬영하세요", Toast.LENGTH_SHORT).show();
////                            }
////                        })
//                        .setNegativeButton("cancle", new DialogInterface.OnClickListener(){
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Toast.makeText(MainActivity.this, "취소하였습니다", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                dialog.create();
//                dialog.show();
//            }
//        });
//    }
//}
