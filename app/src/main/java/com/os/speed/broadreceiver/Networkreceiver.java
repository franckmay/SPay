package com.os.speed.broadreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.os.speed.Home;

public class Networkreceiver extends BroadcastReceiver {

    private static final String NOMCOMPAGNY = "MSGBRD";
    public Home main;

    public Networkreceiver(Home main) {
        this.main = main;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("networkkkkk","je passe");
      //  if (checkNetworkConnection(context)) {

            main.getALclientfromOnline();
            main. getALpubOnline();
        //}

    }

}
