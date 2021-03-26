package com.Class;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by ki-user on 21-03-2018.
 */

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent)
    {

        Log.i("App", "called receiver method");
        try{
            Utils.sendNotification(context, intent.getStringExtra("message"));
            Log.e("msg_reciever", intent.getStringExtra("message"));

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}