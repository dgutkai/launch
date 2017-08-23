package com.dgutkai.launch.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.dgutkai.launch.contacts.ContactsInfo;

import java.util.ArrayList;

/**
 * 数据库操作，单例
 */
public class DatabaseUtil {
	private MyHelper helper;
	private static DatabaseUtil instance;

	public static DatabaseUtil shareInstance(Context context){
		if (instance == null){
			instance = new DatabaseUtil(context);
		}
		return instance;
	}

	private DatabaseUtil(Context context) {
		super();
		helper = new MyHelper(context);
	}

	public boolean saveContacts(ContactsInfo contactsInfo){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "replace into "+ MyHelper.TABLE_CONTACTS
				+"(id,name,number,sortKey) values ("
				+ "'" + contactsInfo.getId()
				+ "' ,'" + contactsInfo.getName()
				+ "' ,'" + contactsInfo.getNumber()
				+ "' ,'" + contactsInfo.getSortKey() + "')";
		try {
			db.execSQL(sql);
			return true;
		} catch (SQLException e){
			Log.e("err", "insert failed");
			return false;
		}finally{
			db.close();
		}
	}

	public boolean deleteContacts(ContactsInfo contactsInfo){
		SQLiteDatabase db = helper.getWritableDatabase();

		String sql = "delete from " + MyHelper.TABLE_CONTACTS
				+ " where id = ?";
		try {
			db.execSQL(sql, new String[]{contactsInfo.getId()});
			return true;
		} catch (SQLException e){
			Log.e("err", "insert failed");
			return false;
		}finally{
			db.close();
		}
	}

	public ArrayList<ContactsInfo> getContacts(){
		ArrayList<ContactsInfo> results = new ArrayList<ContactsInfo>();
		SQLiteDatabase db = helper.getReadableDatabase();
		// 调用查找书库代码并返回数据源
		Cursor cursor = db.rawQuery("select * from " + MyHelper.TABLE_CONTACTS, null);
		if (cursor.getCount() <= 0) {
			db.close();
			return results;
		}
		while (cursor.moveToNext()){
			String id = cursor.getString(0);
			String name = cursor.getString(1);
			String number = cursor.getString(2);
			String sortkey = cursor.getString(3);
			results.add(new ContactsInfo(name, number, sortkey, id));
		}
		db.close();

		return results;
	}

	public boolean saveSetting(String key, String value){
		SQLiteDatabase db = helper.getWritableDatabase();
		String sql = "replace into "+ MyHelper.TABLE_NAME
					+"(key,value) values ("
					+ "'" + key
					+ "' ,'" + value +  "')";
		try {            
			db.execSQL(sql); 
			return true;
		} catch (SQLException e){
			Log.e("err", "insert failed");
			return false;
		}finally{
			db.close();
		}
		
	}

	/**获取设置值*/
	public String getValue(String key){
		if (key == null){
			return null;
		}
		SQLiteDatabase db = helper.getReadableDatabase();
		// 调用查找书库代码并返回数据源
		Cursor cursor = db.rawQuery("select value from " + MyHelper.TABLE_NAME + " where key =?",
				new String[]{key});
		if (cursor.getCount() <= 0) {
			db.close();
			return null;
		}
		//游标移到第一条记录准备获取数据
		cursor.moveToFirst();
		String value = cursor.getString(0);
		db.close();
		if (value == null){
			value = "";
		}
		return value;
	}
}