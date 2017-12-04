package com.sujikim.ttt.model;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.exceptions.RealmException;

/**
 * Created by SujiKim on 2017-12-05.
 */

public class DBHelper {
    Realm realm;
    RealmResults<LongT> longTs;
    Boolean saved = null;
    private int number = 1;

    public DBHelper(Realm realm) {
        this.realm = realm;
    }

    //DB에 저장하기
    public Boolean saveLongT (final byte[] longT){
        if (longT == null){
            saved = false;
        }
        else {
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    try {
                        LongT lt = realm.createObject(LongT.class);
                        lt.setNum(number);
                        lt.setImageData(longT);
                        number++;
                        saved = true;

                    }catch (RealmException e) {
                        e.printStackTrace();
                        saved = false;
                    }
                }
            });
        }
        return saved;
    }

    //DB 읽기
    public void getLongT() {
        longTs = realm.where(LongT.class).findAll();
    }
}
