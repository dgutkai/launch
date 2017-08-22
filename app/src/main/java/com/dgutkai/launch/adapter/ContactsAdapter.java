package com.dgutkai.launch.adapter;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dgutkai.launch.R;
import com.dgutkai.launch.contacts.ContactsInfo;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lin on 2017/8/21.
 */

public class ContactsAdapter extends BaseAdapter {

    private ArrayList<ContactsInfo> listData;
    private Context mContext;
    private LayoutInflater inflater;

    public ContactsAdapter(Context context, ArrayList<ContactsInfo> data){
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
            convertView = inflater.inflate(R.layout.item_applist, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.app_name);
            holder.icon = (ImageView) convertView.findViewById(R.id.app_icon);
            convertView.setTag(holder);
        } else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        String contact_name = listData.get(position).getName();
        Bitmap contact_icon = listData.get(position).getIcon(mContext);
        final int currentPos = position;

        holder.icon.setImageBitmap(contact_icon);
        holder.name.setText(contact_name);

        boolean isSelect = listData.get(position).isSelected();
        if (isSelect){
            convertView.setBackgroundColor(0x7fffffff);
        }else{
            convertView.setBackgroundColor(0x00ffffff);
        }
        return convertView;
    }

    private class ViewHolder{
        TextView name;
        ImageView icon;

    }
}
