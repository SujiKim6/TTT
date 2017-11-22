package com.sujikim.ttt;


import android.app.Application;
import com.tsengvn.typekit.Typekit;

/**
 * Created by SujiKim on 2017-11-23.
 */

public class BaseActivity extends Application {
    @Override public void onCreate() {
        super.onCreate();
        // 폰트 정의
        Typekit.getInstance()
                .addNormal(Typekit.createFromAsset(this, "fonts/NotoSansCJKKr-Regular.otf"));
    }

}
