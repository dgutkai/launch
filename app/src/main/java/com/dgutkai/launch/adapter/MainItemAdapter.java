package com.dgutkai.launch.adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dgutkai.launch.R;
import com.dgutkai.launch.contacts.ContactsInfo;

import java.util.ArrayList;

/**
 * Created by lin on 2017/8/21.
 */

public class MainItemAdapter extends BaseAdapter {

    private ArrayList<ContactsInfo> listData;
    private Context mContext;
    private LayoutInflater inflater;

    public MainItemAdapter(Context context, ArrayList<ContactsInfo> data){
        this.inflater = LayoutInflater.from(context);
        this.mContext = context;
        if (data == null){
            listData = new ArrayList<ContactsInfo>();
        }else{
            listData = data;
        }

    }
    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int i) {
        return listData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.main_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.item_text);
            holder.icon = (ImageView) convertView.findViewById(R.id.item_icon);
            holder.flage = (TextView) convertView.findViewById(R.id.flage_text);

            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        String contact_name = listData.get(position).getName();
        Bitmap contact_icon = listData.get(position).getIcon(mContext);
        String number = listData.get(position).getNumber();
        if (number != null && !"null".equals(number)){
            Log.e("Logs", contact_name + " " + number);
            int flage = readMissCall(number);
            if (flage > 0){
                holder.flage.setText(flage + "");
                holder.flage.setVisibility(View.VISIBLE);
            }else{
                holder.flage.setText("0");
                holder.flage.setVisibility(View.GONE);
            }
        }else{
            holder.flage.setText("0");
            holder.flage.setVisibility(View.GONE);
        }

        final int currentPos = position;

        holder.icon.setImageBitmap(contact_icon);
        holder.name.setText(contact_name);
        return convertView;
    }

    private class ViewHolder{
        TextView name;
        ImageView icon;
        TextView flage;
    }

    /**
     * 获取未接来电
     * @param number 电话号码
     * @return
     */
    private int readMissCall(String number) {


        int result = 0;
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            return 0;
        }
        number = number.replaceAll("[( )-]", "");
        Log.e("Logs", number);
        Cursor cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI,
                null, " type=? and number=? and new=1",
                new String[]{CallLog.Calls.MISSED_TYPE + "", number}, "date desc");


        if (cursor != null) {
            result = cursor.getCount();
//            while (cursor.moveToNext()) {
//                String callLogID = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls._ID));
//                String callNumber = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NUMBER));
//                String callDate = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.DATE));
//                String callType = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.TYPE));
//                String isCallNew = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.NEW));
////                if(Integer.parseInt(callType) == CallLog.Calls.MISSED_TYPE && Integer.parseInt(isCallNew) > 0){
//                    Log.e("cursor", "Missed Call Found: " + callNumber + "--" + isCallNew);
////                }
//            }
            cursor.close();
        }
        return result;
    }
}
