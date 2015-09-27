package com.sunrise.ex.screengifv2;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * created to amend the problem of android activityManager reclaiming the widget provider activity, thus
 * reclaiming any broadcast receivers, which means when the user turns the screen on
 * there was nothing there to start the activity
 */

public class GifUserPresentReceiver extends BroadcastReceiver {

    BroadcastReceiver screenoffReceiver;
    IntentFilter filter;
    boolean mIsRegistered;

    @Override
    public void onReceive(Context context, Intent intent) {

          if(filter == null){
              filter = new IntentFilter();
          }

        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);

        if (intent.getAction() != null) {
            if(intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {

                startGifService1(context);
                startGifService2(context);
                startGifService3(context);

            }
        }

            screenoffReceiver = new BroadcastReceiver() {

                @Override
                public void onReceive(Context context, Intent intent) {
                    if (intent.getAction() != null) {
                        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {

                            //1
                            Intent in = new Intent(context, GifService.class);
                            context.stopService(in);
                            //2
                            Intent in2 = new Intent(context, GifService2.class);
                            context.stopService(in2);
                            //3
                            Intent in3 = new Intent(context, GifService3.class);
                            context.stopService(in3);

                            cleanUpReceivers(context);
                        }
                    }
                }
            };

        context.getApplicationContext().registerReceiver(screenoffReceiver, filter);
        mIsRegistered = true;
    }

    public void startGifService1(Context context){

                int[] wids;
                AppWidgetManager am = AppWidgetManager.getInstance(context);
                wids = am.getAppWidgetIds(new ComponentName(context, ScreenGifWidgetProvider.class));

                if(wids.length < 1)
                    return;

                Intent in2 = new Intent(context, GifService.class);
                context.startService(in2);

    }

    public void startGifService2(Context context){

        int[] wids;
        AppWidgetManager am = AppWidgetManager.getInstance(context);
        wids = am.getAppWidgetIds(new ComponentName(context, ScreenGifWidgetProvider2.class));

        if(wids.length < 1)
            return;

        Intent in2 = new Intent(context, GifService2.class);
        context.startService(in2);
    }

    public void startGifService3(Context context){

        int[] wids;
        AppWidgetManager am = AppWidgetManager.getInstance(context);
        wids = am.getAppWidgetIds(new ComponentName(context, ScreenGifWidgetProvider3.class));

        if(wids.length < 1)
            return;

        Intent in3 = new Intent(context, GifService3.class);
        context.startService(in3);
    }

    public void cleanUpReceivers(Context context){

        if(mIsRegistered) {
            context.getApplicationContext().unregisterReceiver(screenoffReceiver);
            screenoffReceiver = null;
        }
    }
}
