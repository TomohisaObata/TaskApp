package jp.techacademy.obata.tomohisa.taskapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tomohisa on 2016/12/24.
 */

public class TaskAdapter extends BaseAdapter {
    private ArrayList<Task> mTaskArrayList;
    private LayoutInflater mLayoutInflater;

    public TaskAdapter(Context context){
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setmTaskArrayList(ArrayList<Task> taskArrayList){
        mTaskArrayList = taskArrayList;
    }
    @Override
    public int getCount() {
        return mTaskArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mTaskArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mTaskArrayList.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = mLayoutInflater.inflate(android.R.layout.simple_list_item_2,null);
        }

        TextView textView1 = (TextView) convertView.findViewById(android.R.id.text1);
        TextView textView2 = (TextView) convertView.findViewById(android.R.id.text2);

        // 後でTaskクラスから情報を取得するように変更する
        textView1.setText(mTaskArrayList.get(position).getTitle());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.JAPANESE);
        Date date = mTaskArrayList.get(position).getDate();
        textView2.setText(simpleDateFormat.format(date));

        return convertView;
    }
}
