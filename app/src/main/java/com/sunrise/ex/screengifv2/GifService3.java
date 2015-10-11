package com.sunrise.ex.screengifv2;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.io.File;

public class GifService3 extends Service {

    public GifView mGifView;
    public AppWidgetManager app;
    Bitmap b;
    Bitmap icon;
    Bitmap scaled;
    String LastHeapWith;
    private String sharedPath;
    int prevHeight;
    int prevWidth;
    int curHeight;
    int curWidth;
    int numappwids;
    int inumtimes;

    private Handler mHandle = new Handler();

    private Runnable BitmapStr = new Runnable() {
        @Override
        public void run() {

            mHandle.removeCallbacks(this);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

            sharedPath = preferences.getString("com.sunrise.ex.screengifv2.PATH_KEY3", "");

            if(!sharedPath.equals("")) {
                File f = new File(sharedPath);
                if (f.exists()) {
                    getBitmap();
                    mHandle.postDelayed(this,preferences.getInt("com.sunrise.ex.screengifv2.DELAY_KEY3",100));
                } else {
                    Toast.makeText(getApplicationContext(), "Error Decoding Gif: File Not Found at" + sharedPath,
                            Toast.LENGTH_LONG).show();
                    return;
                }


            }

        }
    };

    public GifService3() {

    }

    @Override
    public void onDestroy() {

        mHandle.removeCallbacks(BitmapStr);
        mHandle = null;

        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        inumtimes++;
        AppWidgetManager aw = AppWidgetManager.getInstance(getApplicationContext());

        aw.updateAppWidget(aw.getAppWidgetIds(new ComponentName(getApplicationContext(),ScreenGifWidgetProvider3.class)),
                new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_template));

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mHandle.postDelayed(BitmapStr, preferences.getInt("com.sunrise.ex.screengifv2.DELAY_KEY3",100) );

        Intent ReceIntent = new Intent(getApplicationContext(),ScreenGifWidgetProvider3.class);

        getApplicationContext().sendBroadcast(ReceIntent);

        return super.onStartCommand(intent, flags, startId);

    }

    public void getBitmap(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        if(!preferences.getString("com.sunrise.ex.screengifv2.PATH_KEY3", "").equals("")) {
            sharedPath = preferences.getString("com.sunrise.ex.screengifv2.PATH_KEY3", "");

            if(!sharedPath.equals(LastHeapWith))
                mGifView = null;
        }

        if(mGifView == null){

            mGifView = new GifView(getApplicationContext());
            icon = BitmapFactory.decodeFile(sharedPath);

            mGifView.setGif(sharedPath, icon);
            mGifView.decode();
        }

        LastHeapWith = sharedPath;

        while (mGifView.decodeStatus != 2) {

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        mGifView.destroyDrawingCache();
        mGifView.setDrawingCacheEnabled(true);
        mGifView.nextFrame();
        mGifView.invalidate();
        mGifView.buildDrawingCache(true);
        Bitmap bit = Bitmap.createBitmap(preferences.getInt("com.sunrise.ex.screengifv2.WIDTH_KEY3",400)
                , preferences.getInt("com.sunrise.ex.screengifv2.HIGH_KEY3",300), Bitmap.Config.ARGB_8888);

        Canvas c = new Canvas(bit);
        mGifView.layout(0, 0, mGifView.getMeasuredWidth(), mGifView.getMeasuredHeight());
        mGifView.draw(c);
        b = bit;

        final AppWidgetManager appWidgetMan = AppWidgetManager.getInstance(getApplicationContext());

        int[] appWidgetIDs = appWidgetMan
                .getAppWidgetIds(new ComponentName(getApplicationContext(), ScreenGifWidgetProvider3.class));

        final RemoteViews views = new RemoteViews(getApplicationContext().getPackageName(), R.layout.widget_template);

        if(appWidgetIDs != null) {

            for (int e = 0; e < appWidgetIDs.length; e++) {
                numappwids++;
                Bundle ops = appWidgetMan.getAppWidgetOptions(appWidgetIDs[e]);

                curHeight = ops.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
                curWidth = ops.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH);

                if(curHeight < curWidth)
                    if((prevHeight != curHeight) || (prevWidth != curWidth)){

                        scaled = Bitmap.createScaledBitmap(b,(curWidth * 2),(curHeight * 2),false);
                        b = scaled;
                    }

                prevHeight = ops.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);
                prevWidth = ops.getInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT);

                if(b != null)
                    views.setImageViewBitmap(R.id.img_gif_wd,b);

            }
        }
        appWidgetMan.updateAppWidget(appWidgetIDs, views);

    }
}