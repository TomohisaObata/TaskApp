package jp.techacademy.obata.tomohisa.taskapp;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by tomohisa on 2016/12/25.
 */

public class Category extends RealmObject implements Serializable  {
    private String categoryText; //カテゴリ

    // id をプライマリーキーとして設定
    @PrimaryKey
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public  String getCategory(){
        return categoryText;
    }

    public void setCategory(String categoryText){
        this.categoryText = categoryText;
    }
}
