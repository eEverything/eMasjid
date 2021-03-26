package com.emasjid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Class.DBHelper;
import com.emasjid.Fragment.contactus_fragment;
import com.emasjid.Fragment.editprofile_masjid_fragment;
import com.emasjid.Fragment.editprofile_user_fragment;
import com.emasjid.Fragment.favourite_fragment;
import com.emasjid.Fragment.jammatreminder_fragment;
import com.emasjid.Fragment.masjid_bookmark_fragment;
import com.emasjid.Fragment.masjid_detail_fragment;
import com.emasjid.Fragment.prayertimes_fragment;

import com.emasjid.Fragment.search_masjid_fragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.Map;

import static http.constant.AppConstants.USERID;

public class UserHome extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout topPanel;
    ImageView toggle_menu_icon, taj_img;
    TextView txt_login;
    TextView fav_textview;

    @Override
    protected void onPause() {
        super.onPause();
        try {
            sendBroadcast(new Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse("file://" + Environment.getExternalStorageDirectory())));
        } catch (Exception e) {
        }
    }

    TextView follow_textview;
    LinearLayout lin_fav, lin_edit, lin_masjid_search, lin_contact_us, lin_home, lin_editprayers, lin_logout, lin_register, lin_jammatReminder, lin_bookmark;
    FrameLayout wrapper;
    int width, height;

    SlidingMenu menu;

    FragmentManager fm;

    FragmentTransaction ft;

    String[] dbparam = {"id", "user_type"};

    String userid, usertype;
    SharedPreferences myprf;
    SharedPreferences.Editor myedit;
    boolean issearchopen = false;
    ImageView addbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base);

        myprf = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        myedit = myprf.edit();

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;

        topPanel = (RelativeLayout) findViewById(R.id.topPanel);
        follow_textview = (TextView) findViewById(R.id.follow_text);
        fav_textview = (TextView) findViewById(R.id.fav_text);


        taj_img = (ImageView) findViewById(R.id.taj_img);
        toggle_menu_icon = (ImageView) findViewById(R.id.toggle_menu_icon);

        wrapper = (FrameLayout) findViewById(R.id.wrapper);

        addbtn = (ImageView) findViewById(R.id.addbtn);

        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addbtn.setVisibility(View.GONE);
                fm = getSupportFragmentManager();
                ft = fm.beginTransaction();
                issearchopen = true;
                ft.replace(R.id.wrapper, new search_masjid_fragment())
                        .commit();
            }
        });


        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setShadowDrawable(getResources().getDrawable(R.drawable.shadow_drawable));
        menu.setShadowWidth((width * 2) / 100);
        menu.setFadeDegree(0.15f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.layout_drawer_user);

        lin_fav = (LinearLayout) menu.findViewById(R.id.lin_fav);
        lin_edit = (LinearLayout) menu.findViewById(R.id.lin_edit);
        lin_masjid_search = (LinearLayout) menu.findViewById(R.id.lin_masjid_search);
        lin_contact_us = (LinearLayout) menu.findViewById(R.id.lin_contact_us);
        lin_home = (LinearLayout) menu.findViewById(R.id.lin_home);
        lin_editprayers = (LinearLayout) menu.findViewById(R.id.lin_editprayers);
        lin_jammatReminder = (LinearLayout) menu.findViewById(R.id.lin_jammatReminder);
        lin_bookmark = (LinearLayout) menu.findViewById(R.id.lin_bookmark);
        lin_logout = (LinearLayout) menu.findViewById(R.id.lin_logout);
        txt_login = (TextView) menu.findViewById(R.id.txt_login);
        lin_register = (LinearLayout) menu.findViewById(R.id.lin_register);

        lin_fav.setOnClickListener(this);
        lin_edit.setOnClickListener(this);
        lin_masjid_search.setOnClickListener(this);
        lin_contact_us.setOnClickListener(this);
        toggle_menu_icon.setOnClickListener(this);
        lin_home.setOnClickListener(this);
        lin_editprayers.setOnClickListener(this);
        lin_logout.setOnClickListener(this);
        lin_bookmark.setOnClickListener(this);
        lin_jammatReminder.setOnClickListener(this);

        if (myprf.getBoolean("islogout", true)) {
            txt_login.setText("Login");
            lin_edit.setVisibility(View.GONE);
            lin_register.setVisibility(View.VISIBLE);

            lin_register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(UserHome.this, Signup.class));
                    // finishAffinity();

                }
            });

        } else {
            DBHelper db1 = DBHelper.getInstance(UserHome.this);
            db1.open();

            Map<String, String> data = db1.getUser();

            String[] arrays = db1.getUserTablecolomsstringarray();


            for (int i = 0; i < dbparam.length; i++) {
                for (int j = 0; j < data.size(); j++) {

                    if (dbparam[i].equals(arrays[j])) {

                        if (dbparam[i].equals("id")) {

                            userid = data.get(arrays[j]);
                            USERID = userid;
                        }

                        if (dbparam[i].equals("user_type")) {

                            usertype = data.get(arrays[j]);
                        }
                    }
                }
            }
            db1.close();
        }
        // Toast.makeText(this, userid + "", Toast.LENGTH_SHORT).show();


        lin_home.setVisibility(View.GONE);
        lin_editprayers.setVisibility(View.GONE);

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();


        addbtn.setVisibility(View.VISIBLE);
        ft.replace(R.id.wrapper, new favourite_fragment()).commit();

        toggle_menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show/hide the menu

                menu.toggle();

            }
        });
    }

    @Override
    public void onClick(View view) {

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (view.getId()) {

            case R.id.lin_fav:
                menu.toggle();
                addbtn.setVisibility(View.VISIBLE);
                issearchopen = false;
                ft.replace(R.id.wrapper, new favourite_fragment()).commit();
                break;

            case R.id.lin_contact_us:

                menu.toggle();
                addbtn.setVisibility(View.GONE);
                issearchopen = true;
                ft.replace(R.id.wrapper, new contactus_fragment()).commit();
                break;

            case R.id.lin_edit:
                addbtn.setVisibility(View.GONE);

                if (myprf.getBoolean("islogout", true)) {

                    Toast.makeText(getApplicationContext(), "You are not login in application", Toast.LENGTH_LONG).show();

                } else {
                    issearchopen = true;

                    menu.toggle();

                    ft.replace(R.id.wrapper, new editprofile_user_fragment()).commit();
                }
                break;

            case R.id.lin_masjid_search:
                addbtn.setVisibility(View.GONE);

                menu.toggle();
                issearchopen = true;
                ft.replace(R.id.wrapper, new search_masjid_fragment())
                        .commit();

                break;
            case R.id.lin_bookmark:
                addbtn.setVisibility(View.GONE);

                menu.toggle();
                issearchopen = true;
                ft.replace(R.id.wrapper, new masjid_bookmark_fragment())
                        .commit();
               /* if (userid != null && userid.length() > 0) {

                } else {
//                    Toast.makeText(UserHome.this, "Please login first", Toast.LENGTH_SHORT).show();

                    AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle(getResources().getString(R.string.app_name));
                    alertDialog.setMessage("Please login first");
                    alertDialog.setIcon(R.drawable.login_pro);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface aDialogInterface, int aI) {
                            aDialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                }*/

                break;


            case R.id.lin_jammatReminder:
                addbtn.setVisibility(View.GONE);

                menu.toggle();
                issearchopen = true;
                ft.replace(R.id.wrapper, new jammatreminder_fragment())
                        .commit();

                break;


            case R.id.lin_logout:
                addbtn.setVisibility(View.GONE);

                if (myprf.getBoolean("islogout", true)) {
                    menu.toggle();

                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finish();

                } else {
                    menu.toggle();

                    SharedPreferences myprf;
                    SharedPreferences.Editor myedit;
                    myprf = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                    myedit = myprf.edit();
                    myedit.putBoolean("islogout", true);
                    myedit.commit();
                    myedit.apply();

                    DBHelper db = DBHelper.getInstance(UserHome.this);
                    db.open();
                    SQLiteDatabase dbs = db.getdatabase();
                    dbs.execSQL("DROP TABLE IF EXISTS " + "user_detail");
                    db.close();

                    startActivity(new Intent(getApplicationContext(), Login.class));
                    finishAffinity();
                }


                break;


            default:

                break;
        }

    }


    @Override
    public void onBackPressed() {

        if (issearchopen) {


            Intent i = new Intent(UserHome.this, UserHome.class);
            i.putExtra("fromseach", "fromseach");
            startActivity(i);
            finish();


        }

        super.onBackPressed();
    }

    public void showDialog() {

        final Dialog dialog = new Dialog(UserHome.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_majid_info);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#33000000")));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }


}
