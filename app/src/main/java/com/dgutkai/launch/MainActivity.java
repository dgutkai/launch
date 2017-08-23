package com.dgutkai.launch;

import android.Manifest;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
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
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        widgetcontrol = new APPWidgetControl(this);
        /*
         * 想设置震动大小可以通过改变pattern来设定，如果开启时间太短，震动效果可能感觉不到
         * */
        vibrator = (Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
        initData();
    }

    public void widgetAction(View v) {

    }

    private void initView() {
        mGridView = (GridView) findViewById(R.id.app_list);
        appData = new ArrayList<>();
        gridviewAdapter = new MainItemAdapter(this, appData);
        mGridView.setAdapter(gridviewAdapter);

        String widgetID = dbUtil.getValue("show");
        if (widgetID != null) {
            final FrameLayout fl = (FrameLayout) findViewById(R.id.timer_view);

            widgetcontrol.completeAddAppWidget(Integer.parseInt(widgetID), fl);
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                long [] pattern = {0,100};   // 停止 开启 停止 开启
                vibrator.vibrate(pattern,-1);           //重复两次上面的pattern 如果只想震动一次，index设为-1
                String packageName = appData.get(i).getId();
                String name = appData.get(i).getName();
                String number = appData.get(i).getNumber();
                String sortkey = appData.get(i).getSortKey();
                if ("null".equals(number) && "null".equals(sortkey)) {
                    Intent resolveIntent = getPackageManager().getLaunchIntentForPackage(packageName);// 这里的packname就是从上面得到的目标apk的包名
                    // 启动目标应用
                    startActivity(resolveIntent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
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
