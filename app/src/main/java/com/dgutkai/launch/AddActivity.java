package com.dgutkai.launch;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.Nullable;
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
import java.util.List;

/**
 * Created by lanmi on 2017/8/22.
 */

public class AddActivity extends BaseActivity {

//    private ArrayList<HashMap<String, Object>> datas;
    private ArrayList<ContactsInfo> contacts;
    private List<ResolveInfo> mApps;
    private ListView listView;
    private ContactsAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        initData();
    }

    private void initView(){
        listView = (ListView) this.findViewById(R.id.data_listview);
        contacts = new ArrayList<ContactsInfo>();
//        datas = new ArrayList<HashMap<String, Object>>();
        adapter = new ContactsAdapter(this, contacts);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (contacts.get(i).isSelected()) {
                    contacts.get(i).setSelected(false);
                }else{
                    contacts.get(i).setSelected(true);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }
    private void initData(){
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", 1);
        if (type == 1){
            getContacts();
        }else{
            getAPPList();
        }

    }

    private void getAPPList(){
        contacts.clear();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
        for (ResolveInfo resolveInfo: mApps){
            String appname = resolveInfo.activityInfo.loadLabel(this.getPackageManager()).toString();
            String packagename = resolveInfo.activityInfo.packageName;
            Drawable icon = resolveInfo.activityInfo.loadIcon(this.getPackageManager());
            ContactsInfo contactsInfo = new ContactsInfo(appname, null, null, packagename);
            contactsInfo.setIcon(icon);
            contacts.add(contactsInfo);
        }
        adapter.notifyDataSetChanged();
    }
    private void getContacts(){
        contacts.clear();
        try {
            Uri contactUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            Cursor cursor = this.getContentResolver().query(contactUri,
                    new String[]{"display_name", "sort_key", "contact_id","data1"},
                    null, null, "sort_key");
            String contactName;
            String contactNumber;
            String contactSortKey;
            String contactPhotoURL;
            String contactId;
            while (cursor.moveToNext()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                contactNumber = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                contactSortKey = getSortkey(cursor.getString(1));
//                contactPhotoURL = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));
                ContactsInfo contactsInfo = new ContactsInfo(contactName, contactNumber, contactSortKey, contactId);
                contacts.add(contactsInfo);

                Log.e("SetupActivity", contactName + ":" + contactNumber);
            }
            cursor.close();//使用完后一定要将cursor关闭，不然会造成内存泄露等问题

        }catch (Exception e){
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
    }

    private static String getSortkey(String sortKeyString){
        String key =sortKeyString.substring(0,1).toUpperCase();
        if (key.matches("[A-Z]")){
            return key;
        }else
            return "#";   //获取sort key的首个字符，如果是英文字母就直接返回，否则返回#。
    }


    public void cancelAction(View v){
        for (ContactsInfo contactsInfo : contacts){
            contactsInfo.setSelected(false);
        }
        finish();
    }

    public void okAction(View v){
        for (ContactsInfo contactsInfo : contacts){
            if (contactsInfo.isSelected()){
                boolean result = dbUtil.saveContacts(contactsInfo);
                Log.e("AddActivity", result + "");
            }
        }
        finish();
    }
}
