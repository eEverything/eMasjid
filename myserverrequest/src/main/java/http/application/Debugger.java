package http.application;

import android.util.Log;

import http.constant.AppConstants;


public class Debugger {

    public static final String TAG;

    static {
        TAG = AppConstants.TAG_APP;
    }


    public static void message(String msg) {
        Log.d(TAG, msg);
    }

    public static void messageEx(String msg) {
        //Log.d(TAG, msg);
    }
}
