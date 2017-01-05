package jp.techacademy.obata.tomohisa.taskapp;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by tomohisa on 2016/12/25.
 */

public class TaskApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
    }
}
