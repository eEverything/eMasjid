package com.emasjid;

import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.Adapters.MasjidDetailAdapter;
import com.Adapters.NewsListAdapter;
import com.Class.DBHelper;
import com.Class.MasjidBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import http.CommenRequest.GetXmlData;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.CommenRequest.SetDataClass;
import http.Response;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

public class MasjidDetail_userside extends AppCompatActivity {

    RecyclerView fav_rec;
    LinearLayoutManager mLayoutManager;
    RelativeLayout rel_top_header;
    LinearLayout bottom;
    MasjidDetailAdapter adapter;
    ImageView back, donate_gift;
    ToggleButton togle_button;
    String push_notification_status;
    TextView donate_text;

    String[][] dataarr;
    Map<String, View> viewlist = new HashMap<String, View>();

    String[] dbparam = {"id", "user_type"};

    String userid, masjidid, masjidstatus, status;

    Activity context = MasjidDetail_userside.this;

    MasjidBean bean = new MasjidBean();

    SharedPreferences myprf;
    SharedPreferences.Editor myedit;

    private boolean checkWriteExternalPermission() {
        String permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
        int res = getApplicationContext().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    String proURL = "";

    public void openDialogBox(final Activity context) {

        final Dialog dialog = new Dialog(context);
        // Include dialog.xml file
        dialog.setContentView(R.layout.custom_dialog);
        // Set dialog title
        dialog.setTitle("Custom Dialog");
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_back);

        final LinearLayout main_dialog = dialog.findViewById(R.id.main_dialog);
        ImageView icon_img = dialog.findViewById(R.id.icon_img);
        ImageView spn_img = dialog.findViewById(R.id.spn_img);
        ImageView img_download = dialog.findViewById(R.id.img_download);
        ImageView img_close = dialog.findViewById(R.id.img_close);

        if (proURL != null && !proURL.equalsIgnoreCase(""))
            Picasso.get().load(proURL).placeholder(R.drawable.lg).into(icon_img);

        spn_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
            }
        });

        img_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkWriteExternalPermission()) {
                    /*main_dialog.setDrawingCacheEnabled(true);

                    main_dialog.measure(LinearLayout.MeasureSpec.makeMeasureSpec(0, LinearLayout.MeasureSpec.UNSPECIFIED),
                            LinearLayout.MeasureSpec.makeMeasureSpec(0, LinearLayout.MeasureSpec.UNSPECIFIED));

                    main_dialog.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

                    main_dialog.buildDrawingCache(true);
                    Bitmap b = Bitmap.createBitmap(main_dialog.getDrawingCache());
                    main_dialog.setDrawingCacheEnabled(false); // clear drawing cache


                    String root = Environment.getExternalStorageDirectory().toString();
                    File myDir = new File(root + "/emasjid");
                    if (!myDir.exists()) {
                        myDir.mkdirs();
                    }
                    Random generator = new Random();
                    int n = 10000;
                    n = generator.nextInt(n);
                    String fname = "Image-" + n + ".jpg";
                    File file = new File(myDir, fname);
                    if (file.exists())
                        file.delete();
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        b.compress(Bitmap.CompressFormat.JPEG, 90, out);
                        out.flush();
                        out.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                    File file = saveBitMap(context.getApplicationContext(), main_dialog);    //which view you want to pass that view as parameter
                    if (file != null) {
                        Log.i("TAG", "Drawing saved to the gallery!");
                        Toast.makeText(getApplicationContext(),"Picture is saved to emasjid folder.",Toast.LENGTH_SHORT).show();
                    } else {
                        Log.i("TAG", "Oops! Image could not be saved.");
                        File filee = saveBitMap(context.getApplicationContext(), main_dialog);    //which view you want to pass that view as parameter
                        if (filee == null) {
                            Toast.makeText(getApplicationContext(),"Something went wrong.",Toast.LENGTH_SHORT).show();
                        }

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Need to accept the permission to continue.", Toast.LENGTH_SHORT).show();
                }


                dialog.dismiss();
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private File saveBitMap(Context context, View drawView){
        File pictureFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),"emasjid");
        if (!pictureFileDir.exists()) {
            boolean isDirectoryCreated = pictureFileDir.mkdirs();
            if(!isDirectoryCreated)
                Log.i("ATG", "Can't create directory to save the image");
            return null;
        }
        String root = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES).toString();
       /* File myDir = new File(root + "/emasjid");

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
*/

        Log.e("test2e839040","              =="+root);
        String filename = root + "/emasjid" +File.separator+ System.currentTimeMillis()+".jpg";
        Log.e("test2e839040","              =="+filename);
        File pictureFile = new File(filename);
        Bitmap bitmap =getBitmapFromView(drawView);
        try {
            //path.mkdirs();

            pictureFile.createNewFile();
            FileOutputStream oStream = new FileOutputStream(pictureFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, oStream);
            oStream.flush();
            oStream.close();
            Log.i("TAG", "DONE!!");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("TAG", "There was an issue saving the image.");
        }
        scanGallery( context,pictureFile.getAbsolutePath());
        return pictureFile;
    }
    //create bitmap from view and returns it
    private Bitmap getBitmapFromView(View view) {
        //Define a bitmap with the same size as the view
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(),Bitmap.Config.ARGB_8888);
        //Bind a canvas to it
        Canvas canvas = new Canvas(returnedBitmap);
        //Get the view's background
        Drawable bgDrawable =view.getBackground();
        if (bgDrawable!=null) {
            //has background drawable, then draw it on the canvas
            bgDrawable.draw(canvas);
        }   else{
            //does not have background drawable, then draw white background on the canvas
            canvas.drawColor(Color.WHITE);
        }
        // draw the view on the canvas
        view.draw(canvas);
        //return the bitmap
        return returnedBitmap;
    }
    // used for scanning gallery
    private void scanGallery(Context cntx, String path) {
        try {
            MediaScannerConnection.scanFile(cntx, new String[] { path },null, new MediaScannerConnection.OnScanCompletedListener() {
                public void onScanCompleted(String path, Uri uri) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.masjid_detail);

        myprf = getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        myedit = myprf.edit();


        fav_rec = (RecyclerView) findViewById(R.id.detaillist);
        togle_button = (ToggleButton) findViewById(R.id.togle_button);
        bottom = (LinearLayout) findViewById(R.id.bottom);
        rel_top_header = (RelativeLayout) findViewById(R.id.rel_top_header);
        back = (ImageView) findViewById(R.id.back);
        donate_text = (TextView) findViewById(R.id.donate_text);
        donate_gift = (ImageView) findViewById(R.id.donate_gift);


        donate_gift.setVisibility(View.VISIBLE);
        donate_text.setVisibility(View.GONE);
        bottom.setVisibility(View.GONE);
        rel_top_header.setVisibility(View.VISIBLE);

        dataarr = GetXmlData.getXmlData(MasjidDetail_userside.this, "masjid_detail.xml");
        SetDataClass.findallviewonxml(getWindow().getDecorView().getRootView(), dataarr, viewlist);
        if (myprf.getBoolean("islogout", true)) {


        } else {
            userid = getUserId();

        }
        if (getIntent().hasExtra("masjidid") && getIntent().hasExtra("masjidstatus")) {
            masjidid = getIntent().getStringExtra("masjidid");
            masjidstatus = getIntent().getStringExtra("masjidstatus");
            PrepareData();
            getNotificationStatus();
        }

        if (getIntent().hasExtra("type")) {
            if (getIntent().getStringExtra("type").equals("search")) {
                togle_button.setVisibility(View.GONE);
            }
        }

        mLayoutManager = new LinearLayoutManager(MasjidDetail_userside.this);
        fav_rec.setLayoutManager(mLayoutManager);

//        adapter = new MasjidDetailAdapter(MasjidDetail_userside.this);
//        fav_rec.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        donate_gift.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (bean.donatelink != null && bean.donatelink.length() > 0) {

                    if (!bean.donatelink.startsWith("http://") && !bean.donatelink.startsWith("https://"))
                        bean.donatelink = "http://" + bean.donatelink;

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bean.donatelink));
                    startActivity(browserIntent);


                } else {
                    Toast.makeText(context, "No donation link is available", Toast.LENGTH_SHORT).show();
                }


//                if (bean.donatelink != null && bean.donatelink.length() > 0) {
//                    Intent intent = new Intent(MasjidDetail_userside.this, Donate.class);
//                    intent.putExtra("link", bean.donatelink);
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(context, "No donation link is available", Toast.LENGTH_SHORT).show();
//                }
            }
        });

        ((ImageView) viewlist.get("folowicon")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FollowDialo();
                // ChangeStatusFollow();

            }
        });

        ((ImageView) viewlist.get("masjidinfo")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

            }
        });
    }

    private void getNotificationStatus() {


        if (AppConstants.isNetworkAvailable(context)) {
            Map<String, String> mapParams = new HashMap<String, String>();

            if (myprf.getBoolean("islogout", true)) {

                mapParams.put("device_id", Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                mapParams.put("masjid_id", masjidid);
            } else {


                mapParams.put("user_id", getUserId());
                mapParams.put("masjid_id", masjidid);
            }
            RequestClass.requesttoserver(context, mapParams, "get_notification_status.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {


                    if (data.commanbeandata.viewlist.get("status") != null && data.commanbeandata.viewlist.get("status").length() > 0) {
                        if (data.commanbeandata.viewlist.get("status").equals("0")) {
                            push_notification_status = "0";
                            ((ToggleButton) viewlist.get("toggle")).setChecked(false);
                        } else {
                            push_notification_status = "1";
                            ((ToggleButton) viewlist.get("toggle")).setChecked(true);

                        }
                    }


                    ((ToggleButton) viewlist.get("toggle")).setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                push_notification_status = "enable";
                            } else {
                                push_notification_status = "disable";

                            }

                            ChangeStatusNotification(push_notification_status);
                        }
                    });


//                    if(data.commanbeandata.result1.equals("success"))
//                    {
//                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
//
//                    }


                }

                @Override
                public void onError(ResData data) {

                }
            });
        } else {
            Toast.makeText(context, "Please check internet connection.", Toast.LENGTH_SHORT).show();
        }


    }

    public void FollowDialo() {

        final ImageView profile_picture;
        TextView name, cancel, follow;

        final Dialog dialog = new Dialog(MasjidDetail_userside.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.follow_dialo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#33000000")));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        name = dialog.findViewById(R.id.name);
        cancel = dialog.findViewById(R.id.cancel);
        follow = dialog.findViewById(R.id.follow);
        profile_picture = dialog.findViewById(R.id.profile_picture);


        if (masjidstatus.equals("0")) {
            name.setText("Follow @" + bean.name + " ?");
            follow.setText("Follow");
        } else {
            name.setText("Unfollow @" + bean.name + " ?");
            follow.setText("Unfollow");

        }


        Glide.with(MasjidDetail_userside.this)
                .load(bean.profile_picture)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(300, 300).error(R.drawable.user_dummy))
                .into(profile_picture);


//        Picasso.get().load(bean.profile_picture).into(profile_picture, new Callback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Picasso.get().load(R.drawable.user_dummy).into(profile_picture);
//            }
//        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ChangeStatusFollow();
                dialog.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void showDialog() {

        final ImageView profile_picture, close;
        final TextView name,
                town,
                city,
                about_us,
                address,
                phone_number,
                email,
                website;

        final Dialog dialog = new Dialog(MasjidDetail_userside.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_majid_info);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#33000000")));
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        name = dialog.findViewById(R.id.name);
        town = dialog.findViewById(R.id.town);
        city = dialog.findViewById(R.id.city);
        about_us = dialog.findViewById(R.id.about_us);
        address = dialog.findViewById(R.id.address);
        phone_number = dialog.findViewById(R.id.phone_number);
        email = dialog.findViewById(R.id.email);
        website = dialog.findViewById(R.id.website);
        close = dialog.findViewById(R.id.close);
        profile_picture = dialog.findViewById(R.id.profile_picture);


        name.setText(bean.name);
        town.setText(bean.town);
        city.setText(bean.city);
        about_us.setText(bean.about_us);
        address.setText(bean.address);
        phone_number.setText(bean.phone_number);
        email.setText(bean.email);
        website.setText(bean.website);

//        Picasso.get().load(bean.profile_picture).into(profile_picture, new Callback() {
//            @Override
//            public void onSuccess() {
//
//            }
//
//            @Override
//            public void onError(Exception e) {
//                Picasso.get().load(R.drawable.user_dummy).into(profile_picture);
//            }
//        });


        Glide.with(MasjidDetail_userside.this)
                .load(bean.profile_picture)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).override(300, 300).error(R.drawable.user_dummy).centerCrop())
                .into(profile_picture);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void ChangeStatusFollow() {
        if (AppConstants.isNetworkAvailable(context)) {


            if (masjidstatus.equals("1")) {
                masjidstatus = "0";
            } else {
                masjidstatus = "1";
            }


            Map<String, String> mapParams = new HashMap<String, String>();

            if (myprf.getBoolean("islogout", true)) {

                mapParams.put("device_id", Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                mapParams.put("masjid_id", masjidid);
                mapParams.put("status", masjidstatus);
            } else {


                mapParams.put("masjid_id", masjidid);
                mapParams.put("status", masjidstatus);
                mapParams.put("user_id", getUserId());
            }

            RequestClass.requesttoserver(context, mapParams, "follow_masjid.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    if (data.commanbeandata.result1.equals("success")) {

//                        Toast.makeText(context, "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();

                        Toast.makeText(context, getString(R.string.status_changed_successfully), Toast.LENGTH_SHORT).show();

                        if (masjidstatus.equals("1")) {
                            ((ImageView) viewlist.get("folowicon")).setImageResource(R.mipmap.unfollow_new);
                        } else {
                            ((ImageView) viewlist.get("folowicon")).setImageResource(R.mipmap.follow);
                        }


                    } else {
                        Toast.makeText(context, "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onError(ResData data) {

                }
            });


        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void ChangeStatusNotification(final String status) {
        if (AppConstants.isNetworkAvailable(context)) {


            Map<String, String> mapParams = new HashMap<String, String>();


            if (myprf.getBoolean("islogout", true)) {

                mapParams.put("device_id", Settings.Secure.getString(getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                mapParams.put("masjid_id", masjidid);
                mapParams.put("status", status);
            } else {


                mapParams.put("masjid_id", masjidid);
                mapParams.put("status", status);
                mapParams.put("user_id", getUserId());
            }

            RequestClass.requesttoserver(context, mapParams, "updatenotification_status.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    if (data.commanbeandata.result1.equals("success")) {

                        if (status.equalsIgnoreCase("disable")) {
                            Toast.makeText(context, "Alert off", Toast.LENGTH_LONG).show();

                        } else if (status.equalsIgnoreCase("enable")) {
                            Toast.makeText(context, "Alert on", Toast.LENGTH_LONG).show();

                        }

                    } else {
                        Toast.makeText(context, "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();

                    }


                }

                @Override
                public void onError(ResData data) {

                }
            });


        } else {
            Toast.makeText(context, "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }


    private void PrepareData() {


        if (masjidstatus.equals("1")) {
            ((ImageView) viewlist.get("folowicon")).setImageResource(R.mipmap.unfollow);
        } else {
            ((ImageView) viewlist.get("folowicon")).setImageResource(R.mipmap.follow);
        }


        if (AppConstants.isNetworkAvailable(MasjidDetail_userside.this)) {
            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("id", masjidid);
            mapParams.put("user_id", userid);

            RequestClass.requesttoserverList(MasjidDetail_userside.this, mapParams, "masjid_detail.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {


                    if (data.data.result.equals(Response.RESPONSE_RESULT.success)) {
                        if (data.data.selectedresponcefiel.get(0).containsKey("fajr_time") && (data.data.selectedresponcefiel.get(0).get("fajr_time") != null && data.data.selectedresponcefiel.get(0).get("fajr_time").length() > 0)) {
                            ((TextView) viewlist.get("fajr_time")).setText(getFormatedDate(data.data.selectedresponcefiel.get(0).get("fajr_time")));
                        }
                        if (data.data.selectedresponcefiel.get(0).containsKey("duhr_time") && (data.data.selectedresponcefiel.get(0).get("duhr_time") != null && data.data.selectedresponcefiel.get(0).get("duhr_time").length() > 0)) {
                            ((TextView) viewlist.get("duhr_time")).setText(getFormatedDate(data.data.selectedresponcefiel.get(0).get("duhr_time")));
                        }
                        if (data.data.selectedresponcefiel.get(0).containsKey("asar_time") && (data.data.selectedresponcefiel.get(0).get("asar_time") != null && data.data.selectedresponcefiel.get(0).get("asar_time").length() > 0)) {
                            ((TextView) viewlist.get("asar_time")).setText(getFormatedDate(data.data.selectedresponcefiel.get(0).get("asar_time")));
                        }
                        if (data.data.selectedresponcefiel.get(0).containsKey("magrib_time") && (data.data.selectedresponcefiel.get(0).get("magrib_time") != null && data.data.selectedresponcefiel.get(0).get("magrib_time").length() > 0)) {
                            ((TextView) viewlist.get("magrib_time")).setText(getFormatedDate(data.data.selectedresponcefiel.get(0).get("magrib_time")));
                        }
                        if (data.data.selectedresponcefiel.get(0).containsKey("isha_time") && (data.data.selectedresponcefiel.get(0).get("isha_time") != null && data.data.selectedresponcefiel.get(0).get("isha_time").length() > 0)) {
                            ((TextView) viewlist.get("isha_time")).setText(getFormatedDate(data.data.selectedresponcefiel.get(0).get("isha_time")));
                        }

                        bean.id = data.data.selectedresponcefiel.get(0).get("id");
                        bean.name = data.data.selectedresponcefiel.get(0).get("name");
                        bean.email = data.data.selectedresponcefiel.get(0).get("email");
                        bean.password = data.data.selectedresponcefiel.get(0).get("password");
                        bean.address = data.data.selectedresponcefiel.get(0).get("address");
                        bean.latitude = data.data.selectedresponcefiel.get(0).get("latitude");
                        bean.longitude = data.data.selectedresponcefiel.get(0).get("longitude");
                        bean.city = data.data.selectedresponcefiel.get(0).get("city");
                        bean.town = data.data.selectedresponcefiel.get(0).get("town");
                        bean.phone_number = data.data.selectedresponcefiel.get(0).get("phone_number");
                        bean.website = data.data.selectedresponcefiel.get(0).get("website");
                        bean.about_us = data.data.selectedresponcefiel.get(0).get("about_us");
                        bean.profile_picture = data.data.selectedresponcefiel.get(0).get("profile_picture");
                        bean.prayer_date = data.data.selectedresponcefiel.get(0).get("prayer_date");
                        bean.fajr_time = data.data.selectedresponcefiel.get(0).get("fajr_time");
                        bean.duhr_time = data.data.selectedresponcefiel.get(0).get("duhr_time");
                        bean.asar_time = data.data.selectedresponcefiel.get(0).get("asar_time");
                        bean.magrib_time = data.data.selectedresponcefiel.get(0).get("magrib_time");
                        bean.isha_time = data.data.selectedresponcefiel.get(0).get("isha_time");
                        bean.donatelink = data.data.selectedresponcefiel.get(0).get("link");
                        bean.is_bookmarked = data.data.selectedresponcefiel.get(0).get("is_bookmarked");


                        proURL = bean.profile_picture;
                    }


                    mLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                    ((RecyclerView) viewlist.get("detaillist")).setLayoutManager(mLayoutManager);

                    if (data.data.news.size() > 0) {

                        Log.e("news", "news" + data.data.news.get(0).size());
                        ((TextView) viewlist.get("name")).setText(data.data.selectedresponcefiel.get(0).get("name"));
                        adapter = new MasjidDetailAdapter(MasjidDetail_userside.this, data.data, masjidid, userid);
                        ((RecyclerView) viewlist.get("detaillist")).setAdapter(adapter);


                    }


                    openDialogBox(MasjidDetail_userside.this);
                }

                @Override
                public void onError(ResData data) {

                }
            });
        } else {
            Toast.makeText(MasjidDetail_userside.this, "Please check internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    public String getFormatedDate(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        try {
            date1 = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        String formattedDate = df.format(date1);
        return formattedDate;
    }

    private String getUserId() {
        DBHelper db1 = DBHelper.getInstance(context);
        db1.open();
        Map<String, String> data = db1.getUser();
        String[] arrays = db1.getUserTablecolomsstringarray();
        for (int i = 0; i < dbparam.length; i++) {
            for (int j = 0; j < data.size(); j++) {
                if (dbparam[i].equals(arrays[j])) {
                    if (dbparam[i].equals("id")) {
                        return data.get(arrays[j]);
                    }
                }
            }
        }
        db1.close();
        return "";
    }
}
