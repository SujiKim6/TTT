package com.sujikim.ttt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sujikim.ttt.model.Jackets;
import com.sujikim.ttt.model.LongPants;
import com.sujikim.ttt.model.LongT;
import com.sujikim.ttt.model.Padding;
import com.sujikim.ttt.model.ShortPants;
import com.sujikim.ttt.model.ShortT;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import io.realm.Realm;

public class AddClothesActivity extends AppCompatActivity implements View.OnClickListener{
    private Button takePicture;
    private Button insertDB;
    private TextView resultShow;
    private ImageView picture;
    private Bitmap thumbnail;


    // DB
    Realm realm;
    Context context = this;

    // TensorFlow
    private static final int INPUT_SIZE = 299; //224
    private static final int IMAGE_MEAN = 128; //117
    private static final float IMAGE_STD = 128f; //1
    private static final String INPUT_NAME = "Mul";
    private static final String OUTPUT_NAME = "final_result";

    private static final String MODEL_FILE = "file:///android_asset/optimized_graph.pb";
    private static final String LABEL_FILE =
            "file:///android_asset/retrained_labels.txt";

    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_clothes);

        //DB 사용을 위해 인스턴스 얻기
        realm = Realm.getDefaultInstance();

        takePicture = (Button)findViewById(R.id.btnTakePicture);
        resultShow = (TextView)findViewById(R.id.resultTextview);
        picture = (ImageView)findViewById(R.id.showTakenPicture);
        insertDB = (Button)findViewById(R.id.btnInsertDB);

        takePicture.setOnClickListener(this);
        insertDB.setOnClickListener(this);
        initTensorFlowAndLoadModel();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnTakePicture:
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
                break;
            case R.id.btnInsertDB:
                realm.executeTransaction(new Realm.Transaction(){

                    @Override
                    public void execute(Realm realm) {
                        String buf = resultShow.getText().toString();
                        String find = buf.substring(2,3);
                        int nextNum;
                        Number  num;
                        byte[] pictureData = changeBitmapToByte();
                        switch (find) {
                            case "0": // jacket 일 경우 jacket DB에 저장
                                num = realm.where(Jackets.class).max("num");
                                if(num == null) {
                                    nextNum = 1;
                                } else {
                                    nextNum = num.intValue() + 1;
                                }
                                Jackets jac = realm.createObject(Jackets.class, nextNum);
                                jac.setImageData(pictureData);

                                Toast.makeText(context,"JacketDB에 저장 성공!",Toast.LENGTH_SHORT).show();
                                break;
                            case "1": // LongPants 일 경우
                                num = realm.where(LongPants.class).max("num");
                                if(num == null) {
                                    nextNum = 1;
                                } else {
                                    nextNum = num.intValue() + 1;
                                }
                                LongPants longP = realm.createObject(LongPants.class, nextNum);
                                longP.setImageData(pictureData);

                                Toast.makeText(context, "LongPants DB에 사진 저장 성공!", Toast.LENGTH_SHORT).show();
                                break;
                            case "2": // LongT 일 경우
                                num = realm.where(LongT.class).max("num");
                                if(num == null) {
                                    nextNum = 1;
                                } else {
                                    nextNum = num.intValue() + 1;
                                }
                                LongT longT = realm.createObject(LongT.class, nextNum);
                                longT.setImageData(pictureData);

                                Toast.makeText(context, "LongT DB에 사진 저장 성공!", Toast.LENGTH_SHORT).show();
                                break;
                            case "3": // Padding 일 경우
                                num = realm.where(Padding.class).max("num");
                                if(num == null) {
                                    nextNum = 1;
                                } else {
                                    nextNum = num.intValue() + 1;
                                }
                                Padding pad = realm.createObject(Padding.class, nextNum);
                                pad.setImageData(pictureData);

                                Toast.makeText(context, "Padding DB에 사진 저장 성공!", Toast.LENGTH_SHORT).show();
                                break;
                            case "4": // ShortPants 일 경우
                                num = realm.where(ShortPants.class).max("num");
                                if(num == null) {
                                    nextNum = 1;
                                } else {
                                    nextNum = num.intValue() + 1;
                                }
                                ShortPants shortP = realm.createObject(ShortPants.class, nextNum);
                                shortP.setImageData(pictureData);

                                Toast.makeText(context, "ShortPants DB에 사진 저장 성공!", Toast.LENGTH_SHORT).show();
                                break;
                            case "5": // ShortT 일 경우
                                num = realm.where(ShortT.class).max("num");
                                if(num == null) {
                                    nextNum = 1;
                                } else {
                                    nextNum = num.intValue() + 1;
                                }
                                ShortT shortT = realm.createObject(ShortT.class, nextNum);
                                shortT.setImageData(pictureData);

                                Toast.makeText(context, "ShortT DB에 사진 저장 성공!", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(context, "DB에 사진 저장 실패!", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }
                });



        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                onCaptureImageResult(data);
            }
            else {
                Toast.makeText(this,"취소하셨습니다.",Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void onCaptureImageResult(Intent data) {
        thumbnail = (Bitmap)data.getExtras().get("data");
        Bitmap finalBitmap = GetRotatedBitmap(thumbnail, 90);
        picture.setImageBitmap(finalBitmap);
        final List<Classifier.Recognition> results = classifier.recognizeImage(finalBitmap);
        resultShow.setText(results.toString());
    }

    // 이미지 뷰에 있는 것을 가져와서 byte로 변환 후 저장
    private byte[] changeBitmapToByte() {
        picture.setDrawingCacheEnabled(true);
        picture.buildDrawingCache();
        Bitmap bitmap = picture.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] resultData = baos.toByteArray();
        return resultData;
    }
//
//    private void setThumbnailImage(String orgImagePath) {
//        // 회전 각도 취득
//        int degrees = GetExifOrientation(orgImagePath);
//
//        // 이미지 취득 옵션 설정 (사이즈 정보 취득)
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//
//        // 원본 이미지 정보 취득
//        BitmapFactory.decodeFile(orgImagePath, options);
//
//        // 리사이즈 비율 취득
//        int sampleSize = getSampliSize(options.outWidth, options.outHeight);
//
//        // 이미지 취득 옵션 설정 (사이즈 정보 취득 해제, 리사이즈)
//        options.inJustDecodeBounds = false;
//        options.inSampleSize = sampleSize;
//
//        // 리사이즈된 원본 이미지 취득
//        Bitmap orgImage = BitmapFactory.decodeFile(orgImagePath, options);
//
//        // 회전한 이미지 취득
//        orgImage = GetRotatedBitmap(orgImage, degrees);
//
//        if (orgImage != null) {
//            // 섬네일 이미지 표시
//            mThumbnailImage.setImageBitmap(orgImage);
//        }
//    }
//
//    private int GetExifOrientation(String filepath) {
//        int degree = 0;
//        ExifInterface exif = null;
//
//        try {
//            exif = new ExifInterface(filepath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        if (exif != null) {
//            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//
//            if (orientation != -1) {
//                switch(orientation) {
//                    case ExifInterface.ORIENTATION_ROTATE_90:
//                        degree = 90;
//                        break;
//
//                    case ExifInterface.ORIENTATION_ROTATE_180:
//                        degree = 180;
//                        break;
//
//                    case ExifInterface.ORIENTATION_ROTATE_270:
//                        degree = 270;
//                        break;
//                }
//            }
//        }
//
//        return degree;
//    }

//    private int getSampliSize(int width, int height) {
//        // 화면 크기 취득
//        Display currentDisplay = getWindowManager().getDefaultDisplay();
//
//        float dw = currentDisplay.getWidth();
//        float dh = currentDisplay.getHeight();
//
//        // 가로/세로 축소 비율 취득
//        int widthtRatio = (int) Math.ceil(width / dw);
//        int heightRatio = (int) Math.ceil(height / dh);
//
//        // 초기 리사이즈 비율
//        int sampleSize = 1;
//
//        // 가로 세로 비율이 화면보다 큰경우에만 처리
//        if (widthtRatio > 1 && height > 1) {
//            if (widthtRatio > heightRatio) {
//                // 가로 축소 비율이 큰 경우
//                sampleSize = widthtRatio;
//            } else {
//                // 세로 축소 비율이 큰 경우
//                sampleSize = heightRatio;
//            }
//        }
//
//        return sampleSize;
//    }


    private Bitmap GetRotatedBitmap(Bitmap bitmap, int degrees) {
        if (degrees != 0 && bitmap != null) {
            Matrix m = new Matrix();
            m.setRotate(degrees, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);

            try {
                Bitmap b2 = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), m, true);

                if (bitmap != b2) {
                    bitmap.recycle();
                    bitmap = b2;
                }
            } catch (OutOfMemoryError e) {
                // 메모리 부족에러시, 원본을 반환
            }
        }
        return bitmap;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }
    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_FILE,
                            LABEL_FILE,
                            INPUT_SIZE,
                            IMAGE_MEAN,
                            IMAGE_STD,
                            INPUT_NAME,
                            OUTPUT_NAME);
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }
}
