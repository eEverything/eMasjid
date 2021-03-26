package com.emasjid.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.NewsListAdapter;
import com.Class.DBHelper;
import com.Class.MasjidBean;
import com.bumptech.glide.Glide;
import com.custom_widgets.CustomButton;
import com.custom_widgets.CustomTextView;
import com.emasjid.Action;
import com.emasjid.AdminHome;
import com.emasjid.R;
import com.emasjid.ViewImages;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import http.CommenRequest.GetXmlData;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.CommenRequest.SetDataClass;
import http.Response;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

public class masjid_detail_fragment extends Fragment {
    RecyclerView fav_rec;
    NewsListAdapter adapter;
    LinearLayoutManager mLayoutManager;
    private View view;
    LinearLayout doc_layout, galay_layout, camera_layout, lin_share;
    public static final int XIAOMI_CODE = 6, SAMSUNG_CODE = 5, PICK_IMAGE_MULTIPLE = 4, CAMERA_CAPTURE = 3, PICKFILE_REQUEST_CODE = 2;
    private Paint p = new Paint();
    RelativeLayout send, pin;
    Animation slide_down, slide_up;
    int width, height;
    String description = "", doc_path = "", title = "", id = "";
    String[][] dataarr;
    /*String[] camera_imagepath;
     */
    ArrayList<String> camera_imagepathArr = new ArrayList<>();
    TextView donate_text;
    FilePickerDialog filePickerDialog;
    DialogProperties properties;

    // int pickImage_count=0;

    ImageView f_icon;
    Map<String, View> viewlist = new HashMap<String, View>();

    String[] dbparam = {"id", "user_type"};

    String userid, usertype;

    MasjidBean bean = new MasjidBean();

    ConstraintLayout attach_layout;
    CustomTextView clearAttach, viewAttach, attach_count;

    private String imageName = "";
    private byte[] imageByte;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.masjid_detail, null);

        dataarr = GetXmlData.getXmlData(getActivity(), "masjid_detail.xml");
        SetDataClass.findallviewonxml(view, dataarr, viewlist);

        donate_text = (TextView) view.findViewById(R.id.donate_text);

        donate_text.setVisibility(View.GONE);
        f_icon = view.findViewById(R.id.f_icon);
        attach_count = view.findViewById(R.id.attach_count);
        attach_layout = view.findViewById(R.id.attach_layout);
        clearAttach = view.findViewById(R.id.clearAttach);
        viewAttach = view.findViewById(R.id.viewAttach);

        f_icon.setVisibility(View.GONE);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
        height = size.y;
        id = getUserId();

        filePickerDialog = new FilePickerDialog(getActivity());

        properties = new DialogProperties();

        properties.root = new File(DialogConfigs.DEFAULT_DIR);


        properties.selection_mode = DialogConfigs.SINGLE_MODE;
        properties.selection_type = DialogConfigs.FILE_SELECT;
        properties.error_dir = new File(DialogConfigs.DEFAULT_DIR);
        properties.offset = new File(DialogConfigs.DEFAULT_DIR);
        properties.extensions = null;


        filePickerDialog = new FilePickerDialog(getActivity(), properties);
        filePickerDialog.setTitle("Select Files");

        (viewlist.get("toggle")).setVisibility(View.GONE);


        if (AppConstants.isNetworkAvailable(getActivity())) {
            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("id", id);

            RequestClass.requesttoserverList(getActivity(), mapParams, "masjid_detail.php", new ServiceCallback() {
                @Override
                public void onSuccesss(final ResData data) {


                    Map<String, String> mapParams1 = new HashMap<String, String>();


                    mapParams1.put("user_id", "" + id);
                    mapParams1.put("device_id", "" + Settings.Secure.getString(getActivity().getContentResolver(),
                            Settings.Secure.ANDROID_ID));
                    mapParams1.put("regId", "" + FirebaseInstanceId.getInstance().getToken());


                    RequestClass.requesttoserver_noprocees_bar(getActivity(), mapParams1, "Pushnotification_Android/register1.php", new ServiceCallback() {
                        @Override
                        public void onSuccesss(ResData datafirebas) {
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
                                Log.e("link", bean.donatelink);

                            }
                            /*WORK HERE*/
                            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            ((RecyclerView) viewlist.get("detaillist")).setLayoutManager(mLayoutManager);

                            // Log.e("news", "news" + data.data.news.size());

                            if (data.data.selectedresponcefiel.size() != 0) {
                                title = data.data.selectedresponcefiel.get(0).get("name");
                                ((TextView) viewlist.get("name")).setText(data.data.selectedresponcefiel.get(0).get("name"));
                            }

                            if (data.data.news != null && data.data.news.size() != 0) {
                                adapter = new NewsListAdapter(getActivity(), data.data, masjid_detail_fragment.this);
                                ((RecyclerView) viewlist.get("detaillist")).setAdapter(adapter);
                            }

                        }

                        @Override
                        public void onError(ResData data) {

                        }

                    });

                }

                @Override
                public void onError(ResData data) {

                }
            });
        } else {
            Toast.makeText(getActivity(), "Please check internet connection.", Toast.LENGTH_SHORT).show();
        }

        fav_rec = (RecyclerView) view.findViewById(R.id.detaillist);
        mLayoutManager = new LinearLayoutManager(getActivity());
        fav_rec.setLayoutManager(mLayoutManager);
        lin_share = (LinearLayout) view.findViewById(R.id.lin_share);
        doc_layout = (LinearLayout) view.findViewById(R.id.doc_layout);
        camera_layout = (LinearLayout) view.findViewById(R.id.camera_layout);
        galay_layout = (LinearLayout) view.findViewById(R.id.galay_layout);
        pin = (RelativeLayout) view.findViewById(R.id.pin);
        send = (RelativeLayout) view.findViewById(R.id.send);


        slide_down = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_up);

        ((ImageView) viewlist.get("masjidinfo")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialog();

            }
        });

        pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lin_share.isShown()) {

                    AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    alphaAnimation.setDuration(500);
                    lin_share.startAnimation(alphaAnimation);
                    lin_share.setVisibility(View.GONE);

                    // lin_share.startAnimation(slide_down);
                } else {

                    lin_share.setVisibility(View.VISIBLE);
                    AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    alphaAnimation.setDuration(500);
                    lin_share.startAnimation(alphaAnimation);
                    //lin_share.startAnimation(slide_up);

                }
            }
        });


        doc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);
            }
        });

        filePickerDialog.setDialogSelectionListener(new DialogSelectionListener() {
            @Override
            public void onSelectedFilePaths(String[] files) {
                if (files.length > 0) {
                    doc_path = files[0];
                    Log.e("testRoot", "  " + doc_path);
                    if (camera_imagepathArr != null) {
                        attach_count.setText(String.valueOf(camera_imagepathArr.size() + 1));
                        attach_layout.setVisibility(View.VISIBLE);
                    } else {
                        attach_count.setText("1");
                        attach_layout.setVisibility(View.VISIBLE);
                    }
                }
            }
        });


        galay_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(Action.ACTION_MULTIPLE_PICK);
                startActivityForResult(i, 200);*/

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);

            }
        });


        camera_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = checkPermission(getActivity());
                if (result) {
                    pickImage(CAMERA_CAPTURE);
                    // Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //    startActivityForResult(intent, CAMERA_CAPTURE);
                }
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Map<String, String> mapParams = new HashMap<String, String>();
                description = ((EditText) viewlist.get("enternews")).getText().toString();


                if (!description.equals("") || camera_imagepathArr != null || !doc_path.equals("")) {
                    // if (description.length() > 0)
                    //  {
                    if (camera_imagepathArr != null) {
                        Log.e("Got_Image", camera_imagepathArr + "===");
                        mapParams.put("masjid_id", id);
                        mapParams.put("title", title);
                        mapParams.put("description", description);
                        mapParams.put("num_images", "" + camera_imagepathArr.size());

                        String[] arrray = new String[camera_imagepathArr.size()];
                        for (int t = 0; t < camera_imagepathArr.size(); t++) {
                            arrray[t] = camera_imagepathArr.get(t);
                        }
                        RequestClass.requesttoserver_doc_images(getActivity(), mapParams, doc_path, arrray, "add_newsfeeds.php", new ServiceCallback() {
                            @Override
                            public void onSuccesss(ResData data) {
                                Intent mainIntent = new Intent(getActivity(), AdminHome.class);
                                startActivity(mainIntent);
                                getActivity().finish();
                            }

                            @Override
                            public void onError(ResData data) {

                            }
                        });


                    } else {

                        Log.e("Else_Got_Image", camera_imagepathArr + "===");
                        mapParams.put("masjid_id", id);
                        mapParams.put("title", title);
                        mapParams.put("description", description);
                        mapParams.put("num_images", "0");


                        RequestClass.requesttoserver_doc_images(getActivity(), mapParams, doc_path, "add_newsfeeds.php", new ServiceCallback() {
                            @Override
                            public void onSuccesss(ResData data) {
                                Intent mainIntent = new Intent(getActivity(), AdminHome.class);
                                startActivity(mainIntent);
                                getActivity().finish();
                            }

                            @Override
                            public void onError(ResData data) {

                            }
                        });
                    }
                } else {
                    Toast.makeText(getContext(), "Please write a message or add attachments to procced", Toast.LENGTH_LONG).show();
                }
                // }
                /*else {
                    Toast.makeText(getActivity(), "Please enter massage", Toast.LENGTH_LONG).show();
                }*/
            }
        });

        clearAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                camera_imagepathArr.clear();
                doc_path = "";
                attach_layout.setVisibility(View.GONE);
                Toast.makeText(getContext(), "All attachments are removed.", Toast.LENGTH_SHORT).show();

            }
        });

        viewAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (doc_path == null) {
                    if (camera_imagepathArr != null) {

                        Intent intent = new Intent(getActivity(), ViewImages.class);
                        intent.putExtra("fileType", "1");
                        //intent.putExtra("dataDoc", doc_path);
                        String[] stringArray = camera_imagepathArr.toArray(new String[0]);
                        intent.putExtra("dataMedia", stringArray);
                        startActivity(intent);
                        Log.e("viewimage", "" + stringArray + "-stringArray-");
                    }
                } else if (camera_imagepathArr == null) {
                    /*ONLY DOC*/
                    if (doc_path != null) {
                        Intent intent = new Intent(getActivity(), ViewImages.class);
                        intent.putExtra("fileType", "2");
                        intent.putExtra("dataDoc", doc_path);
                        //intent.putExtra("dataMedia", camera_imagepath);
                        startActivity(intent);
                        Log.e("viewimage", "" + doc_path + "-doc_path-");
                    }
                } else {
                    /*BOTH*/
                    Intent intent = new Intent(getActivity(), ViewImages.class);
                    intent.putExtra("fileType", "3");
                    intent.putExtra("dataDoc", doc_path);
                    String[] stringArray = camera_imagepathArr.toArray(new String[0]);
                    intent.putExtra("dataMedia", stringArray);
                    startActivity(intent);
                    Log.e("viewimage", "" + doc_path + "-doc_path-");
                    Log.e("viewimage", "" + stringArray + "-stringArray-");
                }
            }
        });


        return view;

    }

    public void onAdapterDelete() {
        if (AppConstants.isNetworkAvailable(getActivity())) {
            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("id", id);

            RequestClass.requesttoserverList(getActivity(), mapParams, "masjid_detail.php", new ServiceCallback() {
                @Override
                public void onSuccesss(final ResData data) {


                    Map<String, String> mapParams1 = new HashMap<String, String>();


                    mapParams1.put("user_id", "" + id);
                    mapParams1.put("device_id", "" + Settings.Secure.getString(getActivity().getContentResolver(),
                            Settings.Secure.ANDROID_ID));
                    mapParams1.put("regId", "" + FirebaseInstanceId.getInstance().getToken());


                    RequestClass.requesttoserver_noprocees_bar(getActivity(), mapParams1, "Pushnotification_Android/register1.php", new ServiceCallback() {
                        @Override
                        public void onSuccesss(ResData datafirebas) {
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
                                Log.e("link", bean.donatelink);
                            }

                            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            ((RecyclerView) viewlist.get("detaillist")).setLayoutManager(mLayoutManager);
                            //  Log.e("news", "news" + data.data.news.get(0).size());
                            title = data.data.selectedresponcefiel.get(0).get("name");
                            ((TextView) viewlist.get("name")).setText(data.data.selectedresponcefiel.get(0).get("name"));
                            adapter = new NewsListAdapter(getActivity(), data.data, masjid_detail_fragment.this);
                            ((RecyclerView) viewlist.get("detaillist")).setAdapter(adapter);


                        }

                        @Override
                        public void onError(ResData data) {
                        }
                    });
                }

                @Override
                public void onError(ResData data) {
                }
            });
        } else {
            Toast.makeText(getActivity(), "Please check internet connection.", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("path2path2", "imageFilePath" + imageFilePath);

        switch (requestCode) {

            case PICKFILE_REQUEST_CODE:

                if (data != null) {
                    File file = null;
                    Uri uri = Uri.parse(data.getData().toString());
                    File myfile = new File(uri.getPath());

                    String path = uri.toString();

                    file = new File(path);
                    Log.e("testRoot", " file" + file.getPath() + "---");

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                         doc_path = getPath(getActivity(), uri);
                        //  doc_path = file.getPath();
//                        camera_imagepathArr.add(doc_path);
                    }

                    Toast.makeText(getActivity(), "Doc Saved!", Toast.LENGTH_SHORT).show();
                    if (camera_imagepathArr != null ) {
                        attach_count.setText(String.valueOf(camera_imagepathArr.size() + 1));
                        attach_layout.setVisibility(View.VISIBLE);
                    } else {
                        attach_count.setText("1");
                        attach_layout.setVisibility(View.VISIBLE);
                    }
                    attach_layout.setVisibility(View.VISIBLE);
                }
                Log.e("document", "" + PICKFILE_REQUEST_CODE + "==PICKFILE_REQUEST_CODE==");
                Log.e("document", "" + camera_imagepathArr.size()  + "==PICKFILE_REQUEST_CODE==");
                break;

            case CAMERA_CAPTURE: {
                try {

                    File gpxfile = new File(imageFilePath);
                    int size = (int) gpxfile.length();
                    byte[] bytes = new byte[size];
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(gpxfile));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();
                    long temp = System.currentTimeMillis();
                    imageName = temp + ".png";
                    imageByte = bytes;

                    if (gpxfile != null) {
                        Uri uriimage = FileProvider.getUriForFile(getActivity(), "com.emasjid.fileprovider", gpxfile);
                        camera_imagepathArr.add(imageFilePath);
                        Log.e("uriimage", "====" + uriimage + "====");
                    }
                    if (doc_path.equals("")) {
                        attach_count.setText(String.valueOf(camera_imagepathArr.size()));
                    } else {
                        attach_count.setText(String.valueOf(camera_imagepathArr.size() + 1));
                    }
                    attach_layout.setVisibility(View.VISIBLE);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
               /* if (data != null && data.hasExtra("data")) {
                    Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                    long temp = System.currentTimeMillis();
                    String imageName = temp + ".png";
                    byte[] imageByte = getFileDataFromDrawable(thumbnail);

                    File destination = new File(Environment.getExternalStorageDirectory(), imageName);
                    FileOutputStream fo;
                    try {
                        fo = new FileOutputStream(destination);
                        fo.write(imageByte);
                        fo.close();
                        camera_imagepathArr.add(destination.getAbsolutePath());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    Log.e("Camera_String_Image", camera_imagepathArr.get(0) + "===");
                    if (doc_path.equals("")) {
                        attach_count.setText(String.valueOf(camera_imagepathArr.size()));
                    } else {
                        attach_count.setText(String.valueOf(camera_imagepathArr.size() + 1));
                    }
                    attach_layout.setVisibility(View.VISIBLE);
                }
                break;*/
            }
            break;
            case 200: {
                Log.e("content-->", "200");

                if (resultCode == Activity.RESULT_OK) {
                    Log.e("content-->", "RESULT_OK");

                    if (data.getClipData() != null) {
                        Log.e("content-->", "getClipData() " + data.getClipData());

                        int count = data.getClipData().getItemCount();
                        Log.e("content-->", "data.getClipData().getItemCount()  " + data.getClipData().getItemCount());
                        //                        List<String> fileList = new ArrayList<>();

                        for (int i = 0; i < count; i++) {
                            Log.e("content-->", "data.getClipData().getItemAt(i).getUri()  " + data.getClipData().getItemAt(i).getUri());
                            Uri tempUri = data.getClipData().getItemAt(i).getUri();
                            String tempFilePath = getPathFromUri(getActivity(), tempUri);
//                            fileList.add(tempFilePath);
                            camera_imagepathArr.add(tempFilePath);
                            Log.e("fileList-->", tempFilePath + "");
                        }

                        attach_layout.setVisibility(View.VISIBLE);

                        if (doc_path.equals("")) {
                            attach_count.setText(String.valueOf(camera_imagepathArr.size()));
                        } else {
                            attach_count.setText(String.valueOf(camera_imagepathArr.size() + 1));
                        }
                        Toast.makeText(getActivity(), camera_imagepathArr.size() + " " + "Image has been attach", Toast.LENGTH_SHORT).show();

                    } else if (data.getData() != null) {
                        Log.e("content-->", "data.getData() " + data.getData() + "====");

//                        List<String> fileList = new ArrayList<>();
                        Uri imageUri = data.getData();
                        String tempFilePath = getPathFromUri(getActivity(), imageUri);

//                        fileList.add(tempFilePath);
                        camera_imagepathArr.add(tempFilePath);

                        attach_layout.setVisibility(View.VISIBLE);

                        if (doc_path.equals("")) {
                            attach_count.setText(String.valueOf(camera_imagepathArr.size()));
                        } else {
                            attach_count.setText(String.valueOf(camera_imagepathArr.size() + 1));
                        }

                        Toast.makeText(getActivity(), camera_imagepathArr.size() + " " + "Image has been attach", Toast.LENGTH_SHORT).show();

                    }
                }
            }
            break;
            default:
                break;
        }
        Log.e("TAG", "" + doc_path + "=----doc_path------");
        Log.e("TAG", "" + camera_imagepathArr + "-===camera_imagepathArr=====");
        super.onActivityResult(requestCode, resultCode, data);


    }


    private void pickImage(int code) {

        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pictureIntent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), "com.emasjid.fileprovider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        photoURI);
                startActivityForResult(pictureIntent,
                        code);
            }
        }
    }

    String imageFilePath;

    private File createImageFile() throws IOException {
        String timeStamp =
                new SimpleDateFormat("yyyyMMdd_HHmmss",
                        Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().
                getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        imageFilePath = image.getAbsolutePath();
        Log.e("TAG", "imagepath:" + imageFilePath);
        return image;
    }


    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == FilePickerDialog.EXTERNAL_READ_PERMISSION_GRANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (filePickerDialog != null) {
                    //Show dialog if the read permission has been granted.
                    filePickerDialog.show();
                }
            } else {
                //Permission has not been granted. Notify the user.
                Toast.makeText(getActivity(), "Permission is Required for getting list of files", Toast.LENGTH_SHORT).show();
            }
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

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.N) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (adapter != null) {
            adapter.saveStates(outState);
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (adapter != null) {
            adapter.restoreStates(savedInstanceState);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {
        Log.e("TAG", "" + uri + "-uri-");
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {


                if (isGoogleDriveUri(uri)) {
                    return getDriveFilePath(uri, context);
                }
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    final String id;
                    Cursor cursor = null;
                    try {
                        cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String fileName = cursor.getString(0);
                            String path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                            if (!TextUtils.isEmpty(path)) {
                                return path;
                            }
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                    id = DocumentsContract.getDocumentId(uri);
                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "");
                        }
                        String[] contentUriPrefixesToTry = new String[]{
                                "content://downloads/public_downloads",
                                "content://downloads/my_downloads"
                        };
                        for (String contentUriPrefix : contentUriPrefixesToTry) {
                            try {
                                final Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));

                         /*   final Uri contentUri = ContentUris.withAppendedId(
                                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));*/

                                return getDataColumn(context, contentUri, null, null);
                            } catch (NumberFormatException e) {
                                //In Android 8 and Android P the id is not a number
                                return uri.getPath().replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "");
                            }
                        }


                    }

                } else {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final boolean isOreo = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    Uri contentUri = null;
                    try {
                        contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (contentUri != null) {
                        return getDataColumn(context, contentUri, null, null);
                    }
                }
                /*final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);*/
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {

                if (isGoogleDriveUri(uri)) {
                    return getDriveFilePath(uri, context);
                }
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }


    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri
                .getAuthority());
    }


    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri
                .getAuthority());
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    public void showDialog() {

        final ImageView profile_picture, close;
        TextView name,
                town,
                city,
                about_us,
                address,
                phone_number,
                email,
                website;

        final Dialog dialog = new Dialog(getActivity());
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

        Picasso.get().load(bean.profile_picture).into(profile_picture, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError(Exception e) {
                Picasso.get().load(R.drawable.user_dummy).into(profile_picture);

            }

        });

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

    private String getUserId() {
        DBHelper db1 = DBHelper.getInstance(getActivity());
        db1.open();

        Map<String, String> data = db1.getUser();

        String[] arrays = db1.getUserTablecolomsstringarray();


        for (int i = 0; i < dbparam.length; i++) {
            for (int j = 0; j < data.size(); j++) {

                if (dbparam[i].equals(arrays[j])) {

                    if (dbparam[i].equals("id")) {

                        return data.get(arrays[j]);

                    }

                    if (dbparam[i].equals("user_type")) {

                        usertype = data.get(arrays[j]);

                    }


                }

            }
        }

        db1.close();
        return "";
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPathFromUri(final Context context, final Uri uri) {
        Log.e("content-->", "uri " + uri + "====");
        Log.e("content-->", "content" + uri.getScheme() + "===");
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            } else if (isDownloadsDocument(uri)) {


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    final String id;
                    Cursor cursor = null;
                    try {
                        cursor = context.getContentResolver().query(uri, new String[]{MediaStore.MediaColumns.DISPLAY_NAME}, null, null, null);
                        if (cursor != null && cursor.moveToFirst()) {
                            String fileName = cursor.getString(0);
                            String path = Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
                            if (!TextUtils.isEmpty(path)) {

                                return path;
                            }
                        }
                    } finally {
                        if (cursor != null)
                            cursor.close();
                    }
                    id = DocumentsContract.getDocumentId(uri);
                    if (!TextUtils.isEmpty(id)) {
                        if (id.startsWith("raw:")) {
                            return id.replaceFirst("raw:", "");
                        }
                        String[] contentUriPrefixesToTry = new String[]{
                                "content://downloads/public_downloads",
                                "content://downloads/my_downloads"
                        };
                        for (String contentUriPrefix : contentUriPrefixesToTry) {
                            try {
                                final Uri contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));

                         /*   final Uri contentUri = ContentUris.withAppendedId(
                                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));*/

                                return getDataColumn(context, contentUri, null, null);
                            } catch (NumberFormatException e) {
                                //In Android 8 and Android P the id is not a number
                                return uri.getPath().replaceFirst("^/document/raw:", "").replaceFirst("^raw:", "");
                            }
                        }


                    }

                } else {
                    final String id = DocumentsContract.getDocumentId(uri);
                    final boolean isOreo = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
                    if (id.startsWith("raw:")) {
                        return id.replaceFirst("raw:", "");
                    }
                    Uri contentUri = null;
                    try {
                        contentUri = ContentUris.withAppendedId(
                                Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    if (contentUri != null) {
                        return getDataColumn(context, contentUri, null, null);
                    }
                }
            } else if (isMediaDocument(uri)) {

                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            } else if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address

            if (isGoogleDriveUri(uri)) {
                return getDriveFilePath(uri, context);
            }
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     *//*
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
*/
    private static boolean isGoogleDriveUri(Uri uri) {

        Log.e("isGoogleDriveUri ", "uri " + uri);
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }

    //getDriveFilePath method
    private static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);
        /*
         * Get the column indexes of the data in the Cursor,
         *     * move to the first row in the Cursor, get the data,
         *     * and display it.
         * */
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }
/*       try {
                    File gpxfile = new File(imageFilePath);
                    int size = (int) gpxfile.length();
                    byte[] bytes = new byte[size];
                    BufferedInputStream buf = new BufferedInputStream(new FileInputStream(gpxfile));
                    buf.read(bytes, 0, bytes.length);
                    buf.close();
                    long temp = System.currentTimeMillis();
                    imageName = temp + ".png";
                    imageByte = bytes;
                    if (gpxfile != null) {
                        Uri uriimage = FileProvider.getUriForFile(getActivity(), "com.emasjid.fileprovider", gpxfile);
                        camera_imagepathArr.add(String.valueOf(uriimage));
                        Log.e("uriimage", "====" + uriimage + "====");
                    }
                    if (doc_path.equals("")) {
                        attach_count.setText(String.valueOf(camera_imagepathArr.size()));
                    } else {
                        attach_count.setText(String.valueOf(camera_imagepathArr.size() + 1));
                    }
                    attach_layout.setVisibility(View.VISIBLE);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
*/
}
/*


        doc_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("");
        startActivityForResult(intent, PICKFILE_REQUEST_CODE);
        String manufacturer = android.os.Build.MANUFACTURER;
        Intent testhuawai = getContext().getPackageManager().getLaunchIntentForPackage("com.huawei.hidisk");
/*
                Log.e("testingManu", "          " + manufacturer+"        "+testhuawai);
                if (testhuawai != null) {
                    Intent launchIntent = new Intent(Intent.EXTRA_ALLOW_MULTIPLE);
                    launchIntent.setPackage("com.huawei.hidisk");
                    launchIntent.addCategory(Intent.CATEGORY_DEFAULT);
                    startActivityForResult(launchIntent, PICKFILE_REQUEST_CODE);
                } else*/
/*

        if (manufacturer.equalsIgnoreCase("xiaomi")) {
        Intent test = getContext().getPackageManager().getLaunchIntentForPackage("com.mi.android.globalFileexplorer");
        if (test != null) {
        Intent launchIntent = new Intent(Intent.ACTION_PICK);
        launchIntent.setPackage("com.mi.android.globalFileexplorer");
        launchIntent.putExtra("CONTENT_TYPE", "");
        launchIntent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(launchIntent, PICKFILE_REQUEST_CODE);
        } else {
        filePickerDialog.show();
        }
        } else if (manufacturer.equalsIgnoreCase("htc")) {
        Intent test = getContext().getPackageManager().getLaunchIntentForPackage("com.htc.filemanager");
        if (test != null) {
        Intent launchIntent = new Intent(Intent.ACTION_PICK);
        launchIntent.setPackage("com.htc.filemanager");
        launchIntent.putExtra("CONTENT_TYPE", "");
        launchIntent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(launchIntent, PICKFILE_REQUEST_CODE);
        } else {
        filePickerDialog.show();
        }
        } else if (manufacturer.equalsIgnoreCase("samsung")) {
        Intent test = getContext().getPackageManager().getLaunchIntentForPackage("com.sec.android.app.myfiles");
        if (test != null) {
        Intent launchIntent = new Intent(Intent.ACTION_PICK);
        launchIntent.setPackage("com.sec.android.app.myfiles");
        launchIntent.putExtra("CONTENT_TYPE", " ");
        launchIntent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(launchIntent, PICKFILE_REQUEST_CODE);
        } else {
        filePickerDialog.show();
        }
        } else if (manufacturer.equalsIgnoreCase("google")) {
        Intent test = getContext().getPackageManager().getLaunchIntentForPackage("com.google.android.apps.nbu.files");
        if (test != null) {
        Intent launchIntent = new Intent(Intent.ACTION_PICK);
        launchIntent.setPackage("com.google.android.apps.nbu.files");
        launchIntent.putExtra("CONTENT_TYPE", "");
        launchIntent.addCategory(Intent.CATEGORY_DEFAULT);
        startActivityForResult(launchIntent, PICKFILE_REQUEST_CODE);
        } else {
        filePickerDialog.show();
        }
        } else {
        filePickerDialog.show();
        }


        }
        });*/