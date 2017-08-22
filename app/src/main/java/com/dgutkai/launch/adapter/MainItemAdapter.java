package com.dgutkai.launch.adapter;

import android.content.Context;
import android.graphics.Bitmap;
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
        return convertView;
    }

    private class ViewHolder{
        TextView name;
        ImageView icon;

    }
}
