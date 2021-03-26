package com.emasjid;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Class.DBHelper;
import com.Class.MyReceiver;

import java.util.HashMap;
import java.util.Map;

import http.CommenRequest.GetXmlData;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.CommenRequest.SetDataClass;
import http.CommenRequest.ValidationClass;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

public class Login extends Activity {

    Button login_button;
    TextView text_forgotpass, resgister_masjid;
    EditText pwd_edit_txt;
    ImageView img11;

    String[] validationdata, alldata;
    String[] requestparam = {"email", "password"};
    String[] validationparam = {"email", "password"};
    String[][] dataarr;
    Map<String, View> viewlist = new HashMap<String, View>();

    LinearLayout lin_skip ,lin_register;

    Boolean flagEdit=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        dataarr = GetXmlData.getXmlData(Login.this, "signin.xml");
        SetDataClass.findallviewonxml(getWindow().getDecorView().getRootView(), dataarr, viewlist);

        img11 = (ImageView) findViewById(R.id.img11);
        pwd_edit_txt = (EditText) findViewById(R.id.pwd_edit_txt);
        login_button = (Button) findViewById(R.id.login_button);
        text_forgotpass = (TextView) findViewById(R.id.text_forgotpass);
        resgister_masjid = (TextView) findViewById(R.id.resgister_masjid);

        lin_skip = (LinearLayout) findViewById(R.id.lin_skip);
        lin_register = (LinearLayout) findViewById(R.id.lin_register);

        img11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagEdit){
                    pwd_edit_txt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flagEdit=false;
                    img11.setImageResource(R.drawable.ic_eye_open);
                }else {
                    pwd_edit_txt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flagEdit=true;
                    img11.setImageResource(R.drawable.ic_eye_close);
                }

            }
        });

        ((Button) viewlist.get("signin")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alldata = new String[requestparam.length];

                alldata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, requestparam);


                validationdata = new String[validationparam.length];

                validationdata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, validationparam);


                boolean valid = ValidationClass.fromvalidation(Login.this, dataarr, validationparam, validationdata);


                if (valid) {

                    Map<String, String> mapParams = new HashMap<String, String>();

                    SetDataClass.setRequestparam(mapParams, alldata, requestparam);


                    if (AppConstants.isNetworkAvailable(Login.this)) {


                        RequestClass.requesttoserverList(Login.this, mapParams, "login.php", new ServiceCallback() {
                            @Override
                            public void onSuccesss(ResData data) {

                                if (data.data.selectedresponcefiel.size() > 0) {


                                    DBHelper db = DBHelper.getInstance(Login.this);
                                    db.open();

                                    String query = "CREATE TABLE user_detail" + "(";
                                    for (int i = 0; i < data.data.view_ids.length; i++) {

                                        if (i == (data.data.view_ids.length - 1)) {
                                            query = query + data.data.view_ids[i] + " TEXT);";

                                        } else {
                                            query = query + data.data.view_ids[i] + " TEXT, ";
                                        }

                                    }

                                    SQLiteDatabase dbs = db.getdatabase();
                                    dbs.execSQL("DROP TABLE IF EXISTS " + "user_detail");
                                    dbs.execSQL(query);
                                    ContentValues cv = new ContentValues();

                                    Map<String, String> datsu = data.data.selectedresponcefiel.get(0);

                                    for (int i = 0; i < data.data.view_ids.length; i++) {

                                        cv.put(data.data.view_ids[i], datsu.get(data.data.view_ids[i]));


                                    }

                                    dbs.insert("user_detail", null, cv);
                                    dbs.close();
                                    db.close();


                                    SharedPreferences myprf;
                                    SharedPreferences.Editor myedit;
                                    myprf = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                                    myedit = myprf.edit();


                                    myedit.putBoolean("islogout", false);


                                    myedit.commit();


                                    for (int i = 525; i < 530; i++) {

                                        cancel(i);

                                    }




                                    if(data.data.selectedresponcefiel.get(0).get("user_type").equals("user"))
                                    {
                                        Toast.makeText(getApplicationContext(), "You are successfully logged in", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Login.this, UserHome.class);
                                        intent.putExtra("id", data.data.selectedresponcefiel.get(0).get("id"));
                                        intent.putExtra("usertype", data.data.selectedresponcefiel.get(0).get("user_type"));

                                        startActivity(intent);
                                        finishAffinity();

                                    }
                                    else
                                    {
                                        Toast.makeText(getApplicationContext(), "You are successfully logged in", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Login.this, AdminHome.class);
                                        intent.putExtra("id", data.data.selectedresponcefiel.get(0).get("id"));
                                        intent.putExtra("usertype", data.data.selectedresponcefiel.get(0).get("user_type"));
                                        startActivity(intent);
                                        finishAffinity();

                                    }

                                } else {
                                    Toast.makeText(getApplicationContext(), "" + data.data.message, Toast.LENGTH_LONG).show();

                                }

                            }

                            @Override
                            public void onError(ResData data) {

                            }
                        });


                    } else {
                        Toast.makeText(Login.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }


                } else {

                }


            }
        });


        lin_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, Signup.class);
                startActivity(i);

            }
        });


        ((TextView) viewlist.get("forgot_password")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, Forgotpass.class);
                startActivity(i);
            }
        });


        lin_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Login.this, UserHome.class);
                startActivity(i);
                finish();
            }
        });

    }

    public void cancel(int requestCode) {
        Intent myIntent = new Intent(Login.this, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(Login.this,
                requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();

    }

}
