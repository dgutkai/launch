package com.dgutkai.launch;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.dgutkai.launch.adapter.MainItemAdapter;
import com.dgutkai.launch.base.BaseActivity;
import com.dgutkai.launch.contacts.ContactsInfo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends BaseActivity {

    private GridView mGridView;
    private ArrayList<ContactsInfo> appData;
    private MainItemAdapter gridviewAdapter;
    private APPWidgetControl widgetcontrol;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        widgetcontrol = new APPWidgetControl(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
    }

    public void widgetAction(View v){

    }
    private void initView(){
        mGridView = (GridView) findViewById(R.id.app_list);
        appData = new ArrayList<>();
        gridviewAdapter = new MainItemAdapter(this, appData);
        mGridView.setAdapter(gridviewAdapter);

        String widgetID = dbUtil.getValue("show");
        if (widgetID != null){
            final FrameLayout fl = (FrameLayout) findViewById(R.id.timer_view);

            widgetcontrol.completeAddAppWidget(Integer.parseInt(widgetID), fl);
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String packageName = appData.get(i).getId();
                String name = appData.get(i).getName();
                String number = appData.get(i).getNumber();
                String sortkey = appData.get(i).getSortKey();
                if ("null".equals(number) && "null".equals(sortkey)){
                    Intent resolveIntent = getPackageManager().getLaunchIntentForPackage(packageName);// 这里的packname就是从上面得到的目标apk的包名
                    // 启动目标应用
                    startActivity(resolveIntent);
                }

            }
        });
    }

    private void initData(){
        appData.clear();
        appData.addAll(dbUtil.getContacts());
        gridviewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {

    }

    public void setupAction(View v){
        Intent setupIntent = new Intent(this, SetupActivity.class);
        startActivity(setupIntent);
    }
}
