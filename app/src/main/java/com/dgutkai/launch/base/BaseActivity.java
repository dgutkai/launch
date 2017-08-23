package com.dgutkai.launch.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.dgutkai.launch.contacts.ContactsInfo;
import com.dgutkai.launch.db.DatabaseUtil;

/**
 * Created by lin on 2017/8/15.
 */

public class BaseActivity extends AppCompatActivity {
    public DatabaseUtil dbUtil;
    public static Context baseContext;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (baseContext == null) {
            baseContext = this;
        }
        dbUtil = DatabaseUtil.shareInstance(this);

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    1);
        }
        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // 用户取消授权
                }
                break;

        }
    }

}
