package com.sujikim.ttt.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by SujiKim on 2017-12-05.
 */

public class ShortPants extends RealmObject {
    @PrimaryKey
    private int num;
    private byte[] imageData;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public byte[] getImageData() {
        return imageData;
    }

    public void setImageData(byte[] imageData) {
        this.imageData = imageData;
    }
}
