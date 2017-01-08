package jp.techacademy.obata.tomohisa.taskapp;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by tomohisa on 2017/01/04.
 */

public class CategoryAdapter extends ArrayAdapter<Category> {
    public CategoryAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }
    public CategoryAdapter(Context context, int textViewResourceId, List<Category>list) {
        super(context, textViewResourceId,list);
    }
    /**
     * Spinerに表示するViewを取得します。
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setText(getItem(position).getCategory());
        return view;
    }
    /**
     * Spinerのドロップダウンアイテムに表示するViewを取得します。
     */
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setText(getItem(position).getCategory());
        return view;
    }
    /**
     * keyに一致するインデックスを取得します。
     */
    public int getPosition(int key){
        int position = -1;
        for (int i = 0 ; i < this.getCount(); i++){
            if (this.getItem(i).getId() == key) {
                position = i;
                break;
            }
        }
        return position;

    }
}
