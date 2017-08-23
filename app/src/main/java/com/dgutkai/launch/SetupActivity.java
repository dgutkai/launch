package com.dgutkai.launch;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.dgutkai.launch.adapter.ContactsAdapter;
import com.dgutkai.launch.base.BaseActivity;
import com.dgutkai.launch.contacts.ContactsInfo;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by lin on 2017/8/16.
 */

public class SetupActivity extends BaseActivity {
    private ListView mListView;
    private ArrayList<ContactsInfo> appData;
    private ContactsAdapter mAdapter;
    private APPWidgetControl widgetcontrol;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        widgetcontrol = new APPWidgetControl(this);
        initView();

    }


    private void initView(){
        mListView = (ListView) this.findViewById(R.id.app_list);
        appData = new ArrayList<ContactsInfo>();
        mAdapter = new ContactsAdapter(this, appData);
//        mAdapter = new SimpleAdapter(this, appData, R.layout.item_applist,
//                new String[]{"icon", "name"}, new int[]{R.id.item_icon, R.id.app_name});
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int index = i;
                final AlertDialog alertDialog = new AlertDialog.Builder(SetupActivity.this).setTitle("删除")
                        .setMessage("确认删除该联系人")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbUtil.deleteContacts(appData.get(index));
                                initData();
                            }
                        })
                        .setNegativeButton("取消", null).create();
                alertDialog.show();
            }
        });
    }

    private void initData(){
        appData.clear();
        appData.addAll(dbUtil.getContacts());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void widgetAction(View v){
        widgetcontrol.addWidget(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {

            case APPWidgetControl.widget_get:
                if (widgetcontrol.addAppWidget(this, data) != null){
                    onActivityResult(APPWidgetControl.widget_ok, Activity.RESULT_OK, data);
                }
                break;
            case APPWidgetControl.widget_ok:
                dbUtil.saveSetting("show", widgetcontrol.getAppWidget(data) + "");
                break;
            default:
                break;


        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    public void AddContactsAction(View v){
        AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("请选择")
                .setMessage("请选择添加类别")
                .setNeutralButton("添加联系人", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SetupActivity.this, AddActivity.class);
                        intent.putExtra("type", 1);
                        startActivity(intent);
                    }
                })
                .setPositiveButton("添加应用程序", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(SetupActivity.this, AddActivity.class);
                        intent.putExtra("type", 2);
                        startActivity(intent);
                    }
                }).create();
        alertDialog.show();
    }


}
