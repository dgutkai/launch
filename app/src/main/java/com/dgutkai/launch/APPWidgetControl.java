package com.dgutkai.launch;

import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

/**
 * APPWidget控制类，主要进行APPWidget的添加操作
 * @author 吴妙灵
 *
 */
public class APPWidgetControl {
	public static final int widget_get = 21;
	public static final int widget_ok = 22;
	static final String EXTRA_CUSTOM_WIDGET = "custom_widget";
	static final String SEARCH_WIDGET = "search_widget";
	
	private Context myContext;
	AppWidgetManager mAppWidgetManager;
	AppWidgetHost mAppWidgetHost;
	
	public APPWidgetControl(Context context){
		myContext = context;
		mAppWidgetManager = AppWidgetManager.getInstance(myContext);
		mAppWidgetHost = new AppWidgetHost(myContext, 1024);
		mAppWidgetHost.startListening();
	}
	
	public void addWidget(Activity a) {
	    int appWidgetId = mAppWidgetHost.allocateAppWidgetId();
	    Intent pickIntent = new Intent(AppWidgetManager.ACTION_APPWIDGET_PICK);
	    pickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	    // start the pick activity
	    a.startActivityForResult(pickIntent, widget_get);
	}
	
	public Intent addAppWidget(Activity a, Intent data) {
	    int appWidgetId = data.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);

	    String customWidget = data.getStringExtra(EXTRA_CUSTOM_WIDGET);
	    if (SEARCH_WIDGET.equals(customWidget)) {
	        // We don't need this any more, since this isn't a real app widget.
	        mAppWidgetHost.deleteAppWidgetId(appWidgetId);
	        // add the search widget
//	        addSearch();
	    } else {
	        AppWidgetProviderInfo appWidget = mAppWidgetManager.getAppWidgetInfo(appWidgetId);

	        if (appWidget != null && appWidget.configure != null) {
	            // Launch over to configure widget, if needed
	            Intent intent = new Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE);
	            intent.setComponent(appWidget.configure);
	            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
	            a.startActivityForResult(intent, widget_ok);
	        } else {
	            // Otherwise just add it
	            return data;
	        }
	    }
	    return null;
	}
	
	/**
	 * 获取AppWidget的ID
	 * @param data
	 * @return
	 */
	public int getAppWidget(Intent data){
		Bundle extras = data.getExtras();
	    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);
	    return appWidgetId;
	}
	 /** 
	  * 添加widget信息到layout中
	  */       
	public int completeAddAppWidget(int appWidgetId, FrameLayout framelayout) {
//	    Bundle extras = data.getExtras();            
//	    int appWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, -1);              
//	    Log.d(TAG, "dumping extras content=" + extras.toString());              
	    AppWidgetProviderInfo appWidgetInfo = mAppWidgetManager.getAppWidgetInfo(appWidgetId);
	    if (appWidgetInfo == null)
	    	return -1;
	    Log.i("sdrt", String.valueOf(appWidgetInfo.minHeight));
	    Log.i("sdrt", String.valueOf(appWidgetInfo.minWidth));
	     // Perform actual inflation because we're live             
//	    synchronized (true) {     
	    AppWidgetHostView mHostView = null;
	    //获取显示widget的view                 
	    mHostView = mAppWidgetHost.createView(myContext, appWidgetId, appWidgetInfo);                
	    mHostView.setAppWidget(appWidgetId, appWidgetInfo);
	    //将获取的view添加早layout中                
	    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	    lp.setMargins(0, 10, 10, 0);
//	    FrameLayout mainLayout = (FrameLayout)this.findViewById(R.id.framelayout1);
		framelayout.removeAllViews();
	    framelayout.addView(mHostView, lp);                  
	    framelayout.requestLayout();  
	    return 0;
//	     }       
	 }
}
