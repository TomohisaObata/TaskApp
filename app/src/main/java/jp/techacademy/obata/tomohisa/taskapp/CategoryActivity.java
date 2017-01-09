package jp.techacademy.obata.tomohisa.taskapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import io.realm.Realm;
import io.realm.RealmResults;


public class CategoryActivity extends AppCompatActivity {

    private EditText mCategoryText;
    private Button mRegistButton;
    private Button mCancelButton;
    private Realm mRealm;
    private RealmResults mRealmResults;
    private Category mCategory;
    private View.OnClickListener mOnButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId()==R.id.cancel_button){
                finish();
            } else {
                //Realm
                mRealm = Realm.getDefaultInstance();
                mRealmResults = mRealm.where(Category.class).findAll();
                int identifier = 0;
                if (mRealmResults.size() > 0){
                    identifier = mRealmResults.max("id").intValue() + 1;
                }
                mCategory = new Category();
                mCategory.setId(identifier);
                mCategory.setCategory(mCategoryText.getText().toString());

                mRealm.beginTransaction();
                mRealm.copyToRealmOrUpdate(mCategory);
                mRealm.commitTransaction();

                mRealm.close();
                finish();
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // ActionBarを設定する
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        mCategoryText = (EditText)findViewById(R.id.category_edit_text);
        mRegistButton = (Button) findViewById(R.id.regist_button);
        mCancelButton = (Button)findViewById(R.id.cancel_button);
        mRegistButton.setOnClickListener(mOnButtonClickListener);
        mCancelButton.setOnClickListener(mOnButtonClickListener);
    }
}
