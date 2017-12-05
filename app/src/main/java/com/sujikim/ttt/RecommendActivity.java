package com.sujikim.ttt;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sujikim.ttt.model.Jackets;
import com.sujikim.ttt.model.LongPants;
import com.sujikim.ttt.model.LongT;
import com.sujikim.ttt.model.Padding;
import com.sujikim.ttt.model.ShortPants;
import com.sujikim.ttt.model.ShortT;

import java.util.Random;

import io.realm.Realm;
import io.realm.RealmResults;

public class RecommendActivity extends AppCompatActivity {

    // MainAcitivity에서 평균 기온 가져오기
    private double averageTemp = 3;

    // Random한 숫자들 저장
    private int[] randomShortT = {0,0,0};
    private int[] randomShortPants = {0,0,0};
    private int[] randomLongT= {0,0,0};
    private int[] randomLongPants= {0,0,0};
    private int[] randomPadding= {0,0,0};
    private int[] randomJackets= {0,0,0};

    // 램덤한 숫자 배정을 위해 준비
    private Random random = new Random();
    private int lpCount;
    private int ltCount;
    private int spCount;
    private int stCount;
    private int padCount;
    private int jacCount;

    // DB에서 옷을 가져오기 위한 변수들
    private Realm realm;
    RealmResults<LongPants> longPantses;
    RealmResults<LongT> longTs;
    RealmResults<ShortPants> shortPantses;
    RealmResults<ShortT> shortTs;
    RealmResults<Padding> paddings;
    RealmResults<Jackets> jacketses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend);
        realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction(){
            @Override
            public void execute(Realm realm) {
                lpCount = realm.where(LongPants.class).max("num").intValue();
                ltCount = realm.where(LongT.class).max("num").intValue();
                spCount = realm.where(ShortPants.class).max("num").intValue();
                stCount = realm.where(ShortT.class).max("num").intValue();
                padCount = realm.where(Padding.class).max("num").intValue();
                jacCount = realm.where(Jackets.class).max("num").intValue();
            }
        });
    }

    public void setAverageTemp(double averageTemp) {
        this.averageTemp = averageTemp;
    }

    //날씨에 따라 랜덤한 숫자를 가져오는 함수 + 추운날씨에는 text도 설정하기
    protected void getRandomNumber() {
        // 27도 이상일 경우 반팔, 반바지
        // 23도에서 26도 사이일 경우도 반팔, 반바지
        if (averageTemp >= 23) {
            for(int i = 0; i < 3; i++) {
                randomShortT[i] = random.nextInt(stCount) + 1;
                randomShortPants[i] = random.nextInt(spCount) + 1;
            }
        }

        // 20도에서 22도는 반팔두개, 긴팔 한개/ 긴바지 3개
        else if ((averageTemp >= 20) && (averageTemp <= 22)) {
            for(int i = 0; i < 2; i++) { // 반팔 두개 추출
                randomShortT[i] = random.nextInt(stCount) + 1;
            }
            randomShortT[2] = 0;
            randomLongT[0] = random.nextInt(ltCount) + 1;
            for(int i = 0; i < 3; i++) { // 긴바지 3개 추출
                randomLongPants[i] = random.nextInt(lpCount) + 1;
            }
        }


        // 17도에서 19도는 긴팔 세개 긴바지 3개
        else if ((averageTemp >= 17) && (averageTemp <= 19)) {
            for(int i = 0; i < 3; i++) {
                randomLongT[i] = random.nextInt(ltCount) + 1;
                randomLongPants[i] = random.nextInt(lpCount) + 1;
            }
        }

        // 12도에서 16도는 자켓 3개 긴팔3개 긴바지3개
        // 10도에서 11도는 자켓 3개 긴팔3개 긴바지 3개
        // 6도에서 9도는 자켓 3개, 긴팔3개 긴바지 3개
        else if ((averageTemp >= 6) && (averageTemp <= 16)) {
            for(int i = 0; i < 3; i++) {
                randomLongT[i] = random.nextInt(ltCount) + 1;
                randomLongPants[i] = random.nextInt(lpCount) + 1;
                randomJackets[i] = random.nextInt(jacCount) + 1;
            }
        }

        // 5도 이하는 패딩 3개, 긴팔3개 긴바지3개
        else if(averageTemp <= 5) {
            for(int i = 0; i < 3; i++) {
                randomLongT[i] = random.nextInt(ltCount) + 1;
                randomLongPants[i] = random.nextInt(lpCount) + 1;
                randomPadding[i] = random.nextInt(padCount) + 1;
            }
        }
    }

    //옷들을 DB에서 가져오기
    protected void getClothes() {
        // 27도 이상일 경우 반팔, 반바지
        // 23도에서 26도 사이일 경우도 반팔, 반바지
        if (averageTemp >= 23) {

        }

        // 20도에서 22도는 반팔두개, 긴팔 한개/ 긴바지 3개
        else if ((averageTemp >= 20) && (averageTemp <= 22)) {

        }


        // 17도에서 19도는 긴팔 세개 긴바지 3개
        else if ((averageTemp >= 17) && (averageTemp <= 19)) {

        }

        // 12도에서 16도는 자켓 3개 긴팔3개 긴바지3개
        // 10도에서 11도는 자켓 3개 긴팔3개 긴바지 3개
        // 6도에서 9도는 자켓 3개, 긴팔3개 긴바지 3개
        else if ((averageTemp >= 6) && (averageTemp <= 16)) {

        }

        // 5도 이하는 패딩 3개, 긴팔3개 긴바지3개
        else if(averageTemp <= 5) {

        }
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
