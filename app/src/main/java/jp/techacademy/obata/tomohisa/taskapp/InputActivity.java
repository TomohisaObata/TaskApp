package jp.techacademy.obata.tomohisa.taskapp;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;
import io.realm.Sort;


public class InputActivity extends AppCompatActivity  {

    private int mYear,mMonth,mDay,mHour,mMinute;
    private Button mDateButton,mTimeButton;
    private EditText mTitleEdit,mContentEdit;
    private Spinner mSpinner1;
    private CategoryAdapter adapter;
    private Task mTask;
    private Realm mRealm;
    private RealmResults<Task> mTaskRealmResults;
    private RealmResults<Category> mCategoryRealmResults;
    private RealmChangeListener mRealmListener = new RealmChangeListener() {
        @Override
        public void onChange(Object element){
            reloadSpinnerView();
        }
    };
    private View.OnClickListener mOnDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(InputActivity.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            mYear = year;
                            mMonth = monthOfYear;
                            mDay = dayOfMonth;
                            String dateString = mYear + "/" + String.format("%02d",(mMonth + 1)) + "/" + String.format("%02d", mDay);
                            mDateButton.setText(dateString);
                        }
                    },mYear,mMonth,mDay);
            datePickerDialog.show();
        }
    };

    private View.OnClickListener mOnTimeClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            TimePickerDialog timePickerDialog = new TimePickerDialog(InputActivity.this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mHour = hourOfDay;
                            mMinute = minute;
                            String timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute);
                            mTimeButton.setText(timeString);
                        }
                    },mHour,mMinute,false);
            timePickerDialog.show();
        }
    };

    private View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.cancel_button){
                finish();
            } else if(v.getId()==R.id.done_button) {
                addTask();
                finish();
            }else{
                Intent intent = new Intent(InputActivity.this,CategoryActivity.class);
                startActivity(intent);
            }
        }
    };

    private AdapterView.OnItemSelectedListener mSpinnerItemSelectedListener = new AdapterView.OnItemSelectedListener(){

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int posision, long id) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        // ActionBarを設定する
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        //Realm
        mRealm = Realm.getDefaultInstance();
        mCategoryRealmResults = mRealm.where(Category.class).findAll();
        mCategoryRealmResults = mCategoryRealmResults.sort("categoryText", Sort.ASCENDING);
        mRealm.addChangeListener(mRealmListener);

        // UI部品の設定
        mDateButton = (Button)findViewById(R.id.date_button);
        mDateButton.setOnClickListener(mOnDateClickListener);
        mTimeButton = (Button)findViewById(R.id.times_button);
        mTimeButton.setOnClickListener(mOnTimeClickListener);
        findViewById(R.id.done_button).setOnClickListener(mOnButtonClickListener);
        findViewById(R.id.cancel_button).setOnClickListener(mOnButtonClickListener);
        findViewById(R.id.category_button).setOnClickListener(mOnButtonClickListener);
        mTitleEdit = (EditText)findViewById(R.id.title_edit_text);
        mContentEdit = (EditText)findViewById(R.id.content_edit_text);
        mSpinner1 = (Spinner)findViewById(R.id.spinner1);
        mSpinner1.setOnItemSelectedListener(mSpinnerItemSelectedListener);

        adapter = new CategoryAdapter(this,android.R.layout.simple_spinner_item);
        reloadSpinnerView();
        mSpinner1.setAdapter(adapter);

        Intent intent = getIntent();
        int intExtra = intent.getIntExtra(MainActivity.EXTRA_TASK,-1);
        if(intExtra == -1){
            mTask = null;
        }else{
            mTaskRealmResults = mRealm.where(Task.class).equalTo("id",intExtra).findAll();
            mTask = new Task();
            mTask.setId(mTaskRealmResults.get(0).getId());
            mTask.setTitle(mTaskRealmResults.get(0).getTitle());
            mTask.setContents(mTaskRealmResults.get(0).getContents());
            mTask.setCategory(mTaskRealmResults.get(0).getCategory());
            mTask.setDate(mTaskRealmResults.get(0).getDate());
        }

        if (mTask == null) {
            // 新規作成の場合
            Calendar calendar = Calendar.getInstance();
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);
            mSpinner1.setSelection(0);
        } else {
            // 更新の場合
            mTitleEdit.setText(mTask.getTitle());
            mContentEdit.setText(mTask.getContents());
            //mCategoryEdit.setText(mTask.getCategory());
            if(mTask.getCategory() == null){
                mSpinner1.setSelection(0);
            }else{
                mSpinner1.setSelection(adapter.getPosition(mTask.getCategory().getId()));
            }

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(mTask.getDate());
            mYear = calendar.get(Calendar.YEAR);
            mMonth = calendar.get(Calendar.MONTH);
            mDay = calendar.get(Calendar.DAY_OF_MONTH);
            mHour = calendar.get(Calendar.HOUR_OF_DAY);
            mMinute = calendar.get(Calendar.MINUTE);

            String dateString = mYear + "/" + String.format("%02d",(mMonth + 1)) + "/" + String.format("%02d", mDay);
            String timeString = String.format("%02d", mHour) + ":" + String.format("%02d", mMinute);
            mDateButton.setText(dateString);
            mTimeButton.setText(timeString);
        }
    }

    private void addTask() {

        if (mTask == null) {
            // 新規作成の場合
            mTask = new Task();

            mTaskRealmResults = mRealm.where(Task.class).findAll();

            int identifier;
            if (mTaskRealmResults.max("id") != null) {
                identifier = mTaskRealmResults.max("id").intValue() + 1;
            } else {
                identifier = 0;
            }
            mTask.setId(identifier);
        }

        String title = mTitleEdit.getText().toString();
        String content = mContentEdit.getText().toString();
        Category category = (Category)mSpinner1.getSelectedItem();

        mTask.setTitle(title);
        mTask.setContents(content);
        mTask.setCategory(category);
        GregorianCalendar calendar = new GregorianCalendar(mYear,mMonth,mDay,mHour,mMinute);
        Date date = calendar.getTime();
        mTask.setDate(date);

        mRealm.beginTransaction();
        mRealm.copyToRealmOrUpdate(mTask);
        mRealm.commitTransaction();

        mRealm.close();

        Intent resultIntent = new Intent(getApplicationContext(), TaskAlarmReceiver.class);
        resultIntent.putExtra(MainActivity.EXTRA_TASK, mTask);
        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                this,
                mTask.getId(),
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), resultPendingIntent);
    }

    private void reloadSpinnerView(){
        adapter.clear();

        if (mCategoryRealmResults.size()==0){
            Category categoryNull = new Category();
            categoryNull.setId(-1);
            categoryNull.setCategory("カテゴリ未登録");
            adapter.add(categoryNull);
        }

        for (int i = 0; i < mCategoryRealmResults.size(); i++) {
            if (!mCategoryRealmResults.get(i).isValid()) continue;
                Category category = new Category();
                category.setId(mCategoryRealmResults.get(i).getId());
                category.setCategory(mCategoryRealmResults.get(i).getCategory());
                adapter.add(category);
        }
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }
}
