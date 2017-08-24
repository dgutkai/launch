package com.dgutkai.launch.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;

import com.dgutkai.launch.base.BaseActivity;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by lin on 2017/8/21.
 * 联系人模型类，这里将APP和联系人整合到该模型中。
 */

public class ContactsInfo {
    private String name;
    private String number;
    private String sortKey;
    private String id;
    private boolean isSelected;
    private Bitmap icon;
    public ContactsInfo(String name, String number, String sortKey, String id){
        setName(name);
        setNumber(number);
        setSortKey(sortKey);
        setId(id);
        if ("null".equals(number) && "null".equals(sortKey)){
            PackageManager pm = BaseActivity.baseContext.getPackageManager();
            try {
                ApplicationInfo appInfo = pm.getApplicationInfo(id, PackageManager.GET_META_DATA);
//            String name = pm.getApplicationLabel(appInfo).toString();
                Drawable icon = pm.getApplicationIcon(appInfo);
                setIcon(icon);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();

            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    /**
     * 返回ICON，若APP则直接返回APP图标，联系人若有图片的直接返回图片，否则绘画第一个字。
     * @param context
     * @return
     */
    public Bitmap getIcon(Context context){
        if (icon != null){
            return icon;
        }
        ContentResolver cr = context.getContentResolver();
        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,
                Long.parseLong(getId()));
        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(cr, uri);
        Bitmap photo = BitmapFactory.decodeStream(input);

        if (photo == null){
            Bitmap nameBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(nameBitmap);
            Paint paint = new Paint();

            int r = (int) Math.round(Math.random() * 256);
            int g = (int) Math.round(Math.random() * 256);
            int b = (int) Math.round(Math.random() * 256);
            paint.setARGB(255, r, g, b);
            Paint textpaint = new Paint();
            textpaint.setARGB(255, 255-r, 255-g, 255-b);
            textpaint.setTextSize(63);
            textpaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawArc(0, 0, 100, 100, 0, 360, true, paint);
            if (name != null){
                Rect rect = new Rect();
                String showStr = name.substring(0, 1);
                textpaint.getTextBounds(showStr, 0, 1, rect);
                int height = rect.height();
                canvas.drawText(showStr, 50, 45+height/2, textpaint);
            }


            return nameBitmap;
        }
        return makeRoundCorner(photo);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setIcon(Drawable icon) {
        this.icon = drawableToBitmap(icon);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
            100,
            100,
            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, 100, 100);
        drawable.draw(canvas);
        return bitmap;

    }

    public static Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height/2;
        if (width > height) {
            left = (width - height)/2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width)/2;
            right = width;
            bottom = top + width;
            roundPx = width/2;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
