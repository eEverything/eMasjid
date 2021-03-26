package com.emasjid;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.Class.DBHelper;

import java.util.Map;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class SplashActivity extends AppCompatActivity {

    String[] dbparam = {"id", "user_type"};

    String userid, usertype;

    SharedPreferences myprf;
    SharedPreferences.Editor myedit;

    RelativeLayout rl_main;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        rl_main = (RelativeLayout) findViewById(R.id.rl_main);



        myprf = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        myedit = myprf.edit();

        if (!checkPermission()) {

            requestPermission();

        } else {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (myprf.getBoolean("islogout", true)) {

                        Intent mainIntent = new Intent(SplashActivity.this, UserHome.class);
                        startActivity(mainIntent);
                        finish();

                    } else {

                        DBHelper db1 = DBHelper.getInstance(SplashActivity.this);
                        db1.open();

                        Map<String, String> data = db1.getUser();

                        String[] arrays = db1.getUserTablecolomsstringarray();


                        for (int i = 0; i < dbparam.length; i++) {
                            for (int j = 0; j < data.size(); j++) {

                                if (dbparam[i].equals(arrays[j])) {

                                    if (dbparam[i].equals("id")) {
                                        userid = data.get(arrays[j]);
                                    }

                                    if (dbparam[i].equals("user_type")) {

                                        usertype = data.get(arrays[j]);
                                    }
                                }
                            }
                        }

                        db1.close();

                        if (usertype != null && usertype.length() > 0) {
                            if (usertype.equals("user")) {
                                Intent mainIntent = new Intent(SplashActivity.this, UserHome.class);
                                startActivity(mainIntent);
                                finish();
                            } else {
                                Intent mainIntent = new Intent(SplashActivity.this, AdminHome.class);
                                startActivity(mainIntent);
                                finish();
                            }
                        } else {
//                            Intent mainIntent = new Intent(SplashActivity.this, Login.class);
//                            startActivity(mainIntent);
//                            finish();
                            Intent i = new Intent(SplashActivity.this, UserHome.class);
                            startActivity(i);
                            finish();
                        }
                    }
                }
            }, 2000);

        }


    }


    private boolean checkPermission() {
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int result2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);

        return result1 == PackageManager.PERMISSION_GRANTED && result2 == PackageManager.PERMISSION_GRANTED
                && result4 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE, CAMERA}, 500);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 500:
                if (grantResults.length > 0) {

                    boolean storage = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if ( cameraAccepted && storage) {
                        Snackbar.make(rl_main, "Permission Granted, Now you can access storage and camera.", Snackbar.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(SplashActivity.this, UserHome.class);
                        startActivity(mainIntent);
                        finish();
                    }
                    else{

                        Snackbar.make(rl_main, "Permission Denied, You cannot access storage and camera.", Snackbar.LENGTH_LONG).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(SplashActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
