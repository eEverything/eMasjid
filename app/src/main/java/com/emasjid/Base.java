package com.emasjid;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.emasjid.Fragment.contactus_fragment;
import com.emasjid.Fragment.editprofile_user_fragment;
import com.emasjid.Fragment.favourite_fragment;
import com.emasjid.Fragment.masjid_bookmark_fragment;
import com.emasjid.Fragment.masjid_detail_fragment;
import com.emasjid.Fragment.prayertimes_fragment;
import com.emasjid.Fragment.search_masjid_fragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class Base extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout topPanel;
    ImageView toggle_menu_icon, taj_img;
    TextView fav_textview, follow_textview;
    LinearLayout lin_fav, lin_edit, lin_search_user, lin_masjid_search, lin_edit_masjid, lin_editprayers,lin_bookmark;
    FrameLayout wrapper;
    int width, height;
    WebView webView;

    SlidingMenu menu;
    TextView
            txt_home,
            txt_fav,
            txt_search,
            txt_cont,
            txt_login,
            txt_edit_prayer, edit_masjid, txt_edit, search_user, txt_search_masjid,txt_bookmark;
    ImageView
            img_home,
            img_fav,
            img_search,
            img_cont,
            img_login,
            add_search_btn;
    FragmentManager fm;
    FragmentTransaction ft;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base);

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
        add_search_btn=(ImageView)findViewById(R.id.addbtn);



        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.LEFT);
//        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
//        menu.setShadowWidthRes(R.dimen.shadow_width);
//        menu.setShadowDrawable(R.drawable.shadow);
//        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//        menu.setFadeDegree(0.15f);
//        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
//        menu.setMenu(R.layout.menu);

        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setShadowDrawable(getResources().getDrawable(R.drawable.shadow_drawable));
        menu.setShadowWidth((width * 2) / 100);
        menu.setFadeDegree(0.15f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.layout_drawer);


        txt_home = (TextView) menu.findViewById(R.id.txt_home);
        txt_cont = (TextView) menu.findViewById(R.id.txt_contact_us);
        txt_fav = (TextView) menu.findViewById(R.id.txt_fav);
        txt_login = (TextView) menu.findViewById(R.id.txt_login);
        txt_search = (TextView) menu.findViewById(R.id.txt_search_masjid);
        txt_edit = (TextView) menu.findViewById(R.id.txt_edit);
        txt_search_masjid = (TextView) findViewById(R.id.txt_search_masjid);
        txt_edit_prayer = (TextView) menu.findViewById(R.id.txt_edit_prayer);


        img_cont = (ImageView) menu.findViewById(R.id.imag_contact_us);
        img_fav = (ImageView) menu.findViewById(R.id.imag_fav);
        img_home = (ImageView) menu.findViewById(R.id.imag_home);
        img_search = (ImageView) menu.findViewById(R.id.imag_search);
        img_login = (ImageView) menu.findViewById(R.id.img_login);

        lin_fav = (LinearLayout) menu.findViewById(R.id.lin_fav);
        lin_edit = (LinearLayout) menu.findViewById(R.id.lin_edit);
        lin_masjid_search = (LinearLayout) menu.findViewById(R.id.lin_masjid_search);
        lin_editprayers = (LinearLayout) menu.findViewById(R.id.lin_editprayers);
        lin_bookmark = (LinearLayout) menu.findViewById(R.id.lin_bookmark);
        txt_bookmark =  menu.findViewById(R.id.txt_bookmark);

        toggle_menu_icon.setOnClickListener(this);
        txt_home.setOnClickListener(this);
        txt_cont.setOnClickListener(this);
        txt_fav.setOnClickListener(this);
        txt_login.setOnClickListener(this);
        txt_search.setOnClickListener(this);
        txt_bookmark.setOnClickListener(this);
        txt_edit_prayer.setOnClickListener(this);
        txt_home.setOnClickListener(this);

        txt_search_masjid.setOnClickListener(this);
        txt_edit.setOnClickListener(this);



        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        ft.replace(R.id.wrapper, new masjid_detail_fragment()).commit();

        toggle_menu_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                menu.toggle();

            }
        });


        lin_fav.setVisibility(View.GONE);
        lin_edit.setVisibility(View.GONE);
        lin_masjid_search.setVisibility(View.GONE);


    }
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

    @Override
    public void onClick(View view) {

        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch (view.getId()) {

            case R.id.txt_home:

                menu.toggle();
                ft.replace(R.id.wrapper, new masjid_detail_fragment())
                        .commit();
                break;

            case R.id.txt_contact_us:

                menu.toggle();
                ft.replace(R.id.wrapper, new contactus_fragment())
                        .commit();
                break;

            case R.id.txt_fav:

                menu.toggle();
                add_search_btn.setVisibility(View.VISIBLE);
                ft.replace(R.id.wrapper, new favourite_fragment())
                        .commit();

                break;

            case R.id.txt_edit:

                menu.toggle();

                ft.replace(R.id.wrapper, new editprofile_user_fragment())
                        .commit();

                break;
            case R.id.txt_bookmark:

                menu.toggle();

                ft.replace(R.id.wrapper, new masjid_bookmark_fragment())
                        .commit();

                break;


            case R.id.txt_search_masjid:

                menu.toggle();

                ft.replace(R.id.wrapper, new search_masjid_fragment())
                        .commit();

                break;

            case R.id.txt_login:

                menu.toggle();
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;

            case R.id.txt_edit_prayer:

                menu.toggle();

                ft.replace(R.id.wrapper, new prayertimes_fragment())
                        .commit();

                break;

            default:

                break;
        }

    }


    public void showDialog() {

        final Dialog dialog = new Dialog(Base.this);
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
