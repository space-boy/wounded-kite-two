package com.sunrise.ex.screengifv2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ShutdownReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent in = new Intent(context, GifService.class);
        context.stopService(in);
        //2
        Intent in2 = new Intent(context, GifService2.class);
        context.stopService(in2);
        //3
        Intent in3 = new Intent(context, GifService3.class);
        context.stopService(in3);
    }
}
