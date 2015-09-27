package com.sunrise.ex.screengifv2;

import android.app.AlarmManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ScreenGifWidgetProvider3 extends AppWidgetProvider {

    public static final String FILE_KEY = "ScreenGifWidKey";

    AlarmManager alarm;
    static String pathToUse;
    static long updateTime;
    BroadcastReceiver screenoffReceiver;
    IntentFilter filter;
    public int iupdateWids;

    public ScreenGifWidgetProvider3() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if(filter == null){
            filter = new IntentFilter();
        }

        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_BOOT_COMPLETED);
        screenoffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                if(intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){
                    Intent in3 = new Intent(context, GifService3.class);
                    context.stopService(in3);

                }

                if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
                    Intent in3 = new Intent(context, GifService3.class);
                    context.stopService(in3);

                }

            }
        };

        context.getApplicationContext().registerReceiver(screenoffReceiver, filter);
        super.onReceive(context, intent);
    }

    public static void setters(String path,long time){
        pathToUse = path;
        updateTime = time;
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        iupdateWids = 0;
        int[] wids;
        AppWidgetManager am = AppWidgetManager.getInstance(context);
        wids = am.getAppWidgetIds(new ComponentName(context, ScreenGifWidgetProvider3.class));

        for(int id : wids){
            iupdateWids++;
        }

        context.getApplicationContext().registerReceiver(screenoffReceiver, filter);

        Intent in = new Intent(context, GifService3.class);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        pathToUse = preferences.getString("com.sunrise.ex.screengifv2.PATH_KEY3", "");

        in.putExtra(FILE_KEY, pathToUse);
        in.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
        if (!pathToUse.equals(""))
            context.startService(in);

    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {

        int inumwids = 0;
        int[] wids;
        AppWidgetManager am = AppWidgetManager.getInstance(context);
        wids = am.getAppWidgetIds(new ComponentName(context, ScreenGifWidgetProvider3.class));

        for(int id : wids){
            inumwids++;
        }

        if(inumwids == 0) {

            alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent in2 = new Intent(context, GifService3.class);
            in2.putExtra(FILE_KEY, pathToUse);
            in2.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            context.stopService(in2);

        }
        super.onDeleted(context, appWidgetIds);
    }
}