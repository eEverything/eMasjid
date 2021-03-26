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
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Class.DBHelper;
import com.Class.MasjidBean;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.custom_widgets.CustomTextView;
import com.emasjid.AdminHome;
import com.emasjid.Bookmark_object;
import com.emasjid.Document;
import com.emasjid.ImagesGellery;
import com.emasjid.MasjidDetail_userside;
import com.emasjid.R;
import com.emasjid.ViewImages;
import com.github.angads25.filepicker.controller.DialogSelectionListener;
import com.github.angads25.filepicker.model.DialogConfigs;
import com.github.angads25.filepicker.model.DialogProperties;
import com.github.angads25.filepicker.view.FilePickerDialog;
import com.google.firebase.iid.FirebaseInstanceId;
import com.other.DownloadFile;
import com.other.General;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.CommenRequest.ListBean;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.constant.AppConstants;
import http.customprogressdialog.CustomProgressDialog;
import http.social_login.ServiceCallback;

import static http.constant.AppConstants.USERID;

public class masjid_bookmark_fragment extends Fragment {
    RecyclerView recyclerViewlist;
    MyRecyclerAdapter adapter;
    CustomTextView txt_msg;
    private final String TAG = getClass().getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.masjid_book_mark_fragment, null);
        recyclerViewlist = view.findViewById(R.id.detaillist);
        txt_msg = view.findViewById(R.id.txt_msg);
        recyclerViewlist.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));

        String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        callToGetAllBookmarks(android_id, getContext());

        return view;

    }

    public CustomProgressDialog processDailog;

    public void callToGetAllBookmarks(String userid, Context context) {
         /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
        processDailog = new CustomProgressDialog(context, "");
        processDailog.show();
        String Tag = "UpdateUserPassUrl";
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);


        String url = "http://keshavinfotechdemo.com/KESHAV/KG2/TRU/webservices/get_all_book_marked.php?device_id=" + userid;
        Log.e("callToGetAllBookmarks", "           " + url);
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processDailog.dismiss();
                try {
                    JSONObject main = new JSONObject(response);

                    Log.e("callToGetAllBookmarks:", "    success     " + main);
                    JSONObject responseData = main.getJSONObject("responseData");
                    Log.e(TAG, "onResponse: response pin === " + responseData);
                    if (responseData.has("result")) {
                        if (responseData.getString("result").equalsIgnoreCase("fail")) {
                            txt_msg.setVisibility(View.VISIBLE);
                            recyclerViewlist.setVisibility(View.GONE);
                        }
                    } else {
                        txt_msg.setVisibility(View.GONE);
                        recyclerViewlist.setVisibility(View.VISIBLE);
                        ArrayList<Bookmark_object> allData = new ArrayList<>();
                        allData.clear();
                        Log.e("responseData:", "" + responseData.length()+"");
                        for (int i = 0; i < responseData.length(); i++) {
                            Bookmark_object singleData;
                            JSONObject temp = responseData.getJSONObject((i + 1) + "");
                            Log.e("BookmarksIndi:", "    temp     " + temp);
                            String masjid_id = temp.getString("masjid_id");
                            String profile_picture = temp.getString("profile_picture");
                            String news_id = temp.getString("news_id");
                            String news_title = temp.getString("news_title");
                            String description = temp.getString("description");
                            String document = temp.getString("document");
                            String day_ago = temp.getString("day_ago");
                            String bookmark = temp.getString("bookmark");
                            JSONObject object = temp.getJSONObject("gallery");
                            String image = "";
                            Log.e("TAG", "" + object + "---");
                            List<String> gallery_list = new ArrayList<>();
                            for (int j = 0; j <object.length() ; j++) {
                                if (object.has((j+1)+"")) {
                                    JSONObject galleryObj = object.getJSONObject((j + 1) + "");
                                    gallery_list.add(galleryObj.getString("image"));
                                }
                            }

                            /*if (object.has("1")) {
                                if (object.getString("1") != null && object.getString("1").length() > 0) {
                                    Log.e("TAG", "" + object.getString("1") + "---");
                                    JSONObject obj = object.getJSONObject("1");
                                    image = obj.getString("image");
                                    Log.e("TAG", "" + obj.getString("image") + "---");
                                }



                            }*/


                            singleData = new Bookmark_object(masjid_id , news_id, news_title, description, document, day_ago, bookmark, gallery_list , profile_picture);
                            allData.add(singleData);
                        }

                        /*if (allData != null && allData.size() > 0) {
                            adapter = new MyRecyclerAdapter(getContext(), allData);
                            recyclerViewlist.setAdapter(adapter);
                            txt_msg.setVisibility(View.GONE);
                            recyclerViewlist.setVisibility(View.VISIBLE);
                        } else {
                            txt_msg.setVisibility(View.VISIBLE);
                            recyclerViewlist.setVisibility(View.GONE);
                        }*/
                        /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/

                        if (allData != null && allData.size() > 0) {
                            MasjidDetailAdapter adapter = new MasjidDetailAdapter(getActivity(), allData);
                            recyclerViewlist.setAdapter(adapter);
                            txt_msg.setVisibility(View.GONE);
                            recyclerViewlist.setVisibility(View.VISIBLE);
                        } else {
                            txt_msg.setVisibility(View.VISIBLE);
                            recyclerViewlist.setVisibility(View.GONE);
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                processDailog.dismiss();
            }
        }) {

          /*  @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                params.put("token", token);
                params.put("user_type", user_type);
                params.put("current_date", current_date);
                Log.e(Tag + "params", params + "");
                return params;
            }*/
        };
        mRequestQueue.add(mStringRequest);


    }


    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.ViewHolder> {
        Context mContext;
        ArrayList<Bookmark_object> allData = new ArrayList<>();

        MyRecyclerAdapter(Context mContext, ArrayList<Bookmark_object> allData) {
            this.mContext = mContext;
            this.allData = allData;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.masjid_detail_item_bookmark, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

          /*  if (allData.size() <= position) {
                holder.lin_parent.setVisibility(View.GONE);
            } else {*/
            if (allData.get(position).news_title != null && allData.get(position).news_title.length() > 0) {

                holder.masjid_name.setText(allData.get(position).news_title);
            }
            if (allData.get(position).day_ago != null && allData.get(position).day_ago.length() > 0) {

                holder.days.setText(allData.get(position).day_ago);
            }
            if (allData.get(position).description != null && allData.get(position).description.length() > 0) {

                holder.masjid_detail.setText(allData.get(position).description);
            }
            holder.imag_bookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.imag_bookmark.setVisibility(View.GONE);
                    holder.imag_notbookmark.setVisibility(View.VISIBLE);
                    callToUpdateBookmark(allData, mContext, position);
                }
            });
            /*if (allData.get(position).image != null && allData.get(position).image.length() > 0) {
                Picasso.get().load(allData.get(position).image).into(holder.user_image);
            }*/

        }


        public CustomProgressDialog processDailog;

        public void callToUpdateBookmark(final ArrayList<Bookmark_object> data, final Context context, final int pos) {
         /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
            processDailog = new CustomProgressDialog(context, "");
            processDailog.show();
            String Tag = "UpdateUserPassUrl";
            RequestQueue mRequestQueue = Volley.newRequestQueue(context);

            String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            String url = "http://keshavinfotechdemo.com/KESHAV/KG2/TRU/webservices/book_marked.php?device_id=" + android_id + "&news_id=" + data.get(pos).news_id;
            Log.e("MasjidDetailAdapter", "           " + url);
            StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject main = new JSONObject(response);

                        Log.e("callToUpdateBookmark:", "    success     " + main);
                        JSONObject responseData = main.getJSONObject("responseData");
                        String result = responseData.getString("result");
                        String message = responseData.getString("message");

                        if (result.equalsIgnoreCase("success")) {

                            if (message.equalsIgnoreCase("News removed from bookmarke.")) {
//                                Toast.makeText(context, "Removed pin successfully", Toast.LENGTH_SHORT).show();
                                Toast.makeText(context, "Pin Removed", Toast.LENGTH_SHORT).show();
                                String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                                        Settings.Secure.ANDROID_ID);
                                callToGetAllBookmarks(android_id, context);
                            } else if (message.equalsIgnoreCase("News bookmarked successfully")) {
                                Toast.makeText(context, "Pin Saved", Toast.LENGTH_SHORT).show();
                            }
                        }
                        /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
                        processDailog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/

                    }

                    processDailog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {

          /*  @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                params.put("token", token);
                params.put("user_type", user_type);
                params.put("current_date", current_date);
                Log.e(Tag + "params", params + "");
                return params;
            }*/
            };
            mRequestQueue.add(mStringRequest);


        }

        @Override
        public int getItemCount() {
            return allData.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            CustomTextView masjid_name, days, masjid_detail;
            ImageView imag_bookmark, imag_notbookmark, user_image;
            FrameLayout lin_parent;

            ViewHolder(View v) {
                super(v);
                user_image = v.findViewById(R.id.user_image);
                lin_parent = v.findViewById(R.id.lin_parent);
                imag_notbookmark = v.findViewById(R.id.imag_notbookmark);
                masjid_name = v.findViewById(R.id.masjid_name);
                days = v.findViewById(R.id.days);
                masjid_detail = v.findViewById(R.id.masjid_detail);
                imag_bookmark = v.findViewById(R.id.imag_bookmark);

            }
        }
    }


    public class MasjidDetailAdapter extends RecyclerView.Adapter<MasjidDetailAdapter.ViewHolder> {
//        List<Bookmark_object> lists;
        ArrayList<Bookmark_object> lists = new ArrayList<>();
        Activity context;
        String[] dbparam = {"id", "user_type"};

        public MasjidDetailAdapter(Activity context, ArrayList<Bookmark_object> lists) {
            this.context = context;
            this.lists = lists;
            /*Log.e("Constructor: " , lists.size() + " == ");
            Log.e("Constructor: " , this.lists.size() + " == ");*/
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public ImageView image1, image2, image3, user_image, img_bookmarked, img_bookmark;
            public TextView masjid_name, imagecount, masjid_detail, days, doc_name , tvbookmark;
            public View layout;
            RelativeLayout r_layout3;
            public LinearLayout gimage_layout, lin_forground, alarm_share, lin_alarm, lin_share, lin_document,
                    llDownload, llbookmark;
            public CustomTextView tvDownload;
            private FrameLayout lin_parent;

            public ViewHolder(View v) {
                super(v);
                layout = v;

                lin_forground = (LinearLayout) v.findViewById(R.id.lin_forground);
                llbookmark = (LinearLayout) v.findViewById(R.id.llbookmark);
                gimage_layout = (LinearLayout) v.findViewById(R.id.gimage_layout);
                alarm_share = (LinearLayout) v.findViewById(R.id.alarm_share);
                lin_document = (LinearLayout) v.findViewById(R.id.lin_document);

                r_layout3 = (RelativeLayout) v.findViewById(R.id.r_layout3);
                doc_name = (TextView) v.findViewById(R.id.doc_name);
                masjid_name = v.findViewById(R.id.masjid_name);
                user_image = v.findViewById(R.id.user_image);
                image1 = v.findViewById(R.id.image1);
                image2 = v.findViewById(R.id.image2);
                imagecount = v.findViewById(R.id.imagecount);
                image3 = v.findViewById(R.id.image3);
                masjid_detail = v.findViewById(R.id.masjid_detail);
                days = v.findViewById(R.id.days);

                lin_alarm = v.findViewById(R.id.lin_alarm);
                lin_share = v.findViewById(R.id.lin_share);

                llDownload = v.findViewById(R.id.llDownload);
                tvDownload = v.findViewById(R.id.tvDownload);
                img_bookmark = v.findViewById(R.id.img_bookmark);
                img_bookmarked = v.findViewById(R.id.img_bookmarked);
                lin_parent = v.findViewById(R.id.lin_parent);

                tvbookmark = v.findViewById(R.id.tvbookmark);


            }
        }

        // Create new views (invoked by the layout manager)
        @Override
        public MasjidDetailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                              int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View v = inflater.inflate(R.layout.masjid_detail_item_without_slide, parent, false);


            MasjidDetailAdapter.ViewHolder vh = new MasjidDetailAdapter.ViewHolder(v);

            SharedPreferences myprf;

            vh.alarm_share.setVisibility(View.VISIBLE);


            return vh;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final MasjidDetailAdapter.ViewHolder holder, final int position) {
//            Log.e("Position: " + position, lists.get(position).news_title + " == ");
            holder.masjid_name.setText(lists.get(position).news_title);
            holder.masjid_detail.setText(lists.get(position).description);
            final int imageCount = lists.get(position).gallery_list.size();
            int documentCount = 0;

            if (lists.get(position).document!=null && lists.get(position).document.length() >= 1)
                documentCount = 1;
            else
                documentCount = 0;

            holder.tvDownload.setText(context.getString(R.string.save) + "(" + (imageCount + documentCount) + ")");


            holder.llDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (lists.get(position).gallery_list.size() > 0 || lists.get(position).document.length() > 0) {

                        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                            } else {
                                //deprecated in API 26
                                v.vibrate(500);
                            }
                        }

                        for (int j = 0; j < lists.get(position).gallery_list.size(); j++) {
                            String url = lists.get(position).gallery_list.get(j);
                            if (url.length() >= 1) {
                                Log.e("File Image: ", url + " == " + position);
                                if (url.substring(url.lastIndexOf(".") + 1).length() > 0 && General.isImage(url.substring(url.lastIndexOf(".")))) {
                                    if (j == 0) {
                                        Toast.makeText(context, "Download Started.", Toast.LENGTH_SHORT).show();
                                    }
                                    new DownloadFile(context,lists.get(position).gallery_list.get(j),lists.get(position).news_id,"masjid bookmark fragment1");
                                } else
                                    Log.e("File Image URL: ", "Not a file URL.");

                            }
                        }

                        String url = lists.get(position).document;
                        String iidd = lists.get(position).news_id;
                        if (url.length() >= 1) {
                            Log.e("File Document: ", url + " == " + position);
                            if (url.substring(url.lastIndexOf(".") + 1).length() > 0 && General.isDocument(url.substring(url.lastIndexOf(".")))) {
                                Toast.makeText(context, "Download Started.", Toast.LENGTH_SHORT).show();
                                new DownloadFile(context, url,iidd,"masjid bookmark fragment2");
                            } else {
                                Log.e("File Document URL: ", "Not a file URL.");
                            }
                        }

                    } else {
                        Toast.makeText(context, "No Attachments Available.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.days.setText(lists.get(position).day_ago);

            if (lists.get(position).profile_picture.length() > 0) {
                Picasso.get().load(lists.get(position).profile_picture).resize(100, 100).into(holder.user_image,
                        new Callback() {
                            @Override
                            public void onSuccess() {


                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(R.drawable.user_dummy).into(holder.user_image);

                            }
                        });

            }

            holder.lin_alarm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vb.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vb.vibrate(500);
                        }
                    }
                    fireALarm(position);
                }
            });

            holder.lin_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, MasjidDetail_userside.class);
                    i.putExtra("masjidid", lists.get(position).masjid_id);
                    i.putExtra("masjidstatus", lists.get(position).status);
                    context.startActivity(i);
                }
            });

            holder.lin_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Vibrator vb = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            vb.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            vb.vibrate(500);
                        }
                    }
                    Map<String, String> mapParams = new HashMap<String, String>();
                    mapParams.put("masjid_id", lists.get(position).masjid_id);

                    RequestClass.requesttoserver(context, mapParams, "create_link.php", new ServiceCallback() {
                        @Override
                        public void onSuccesss(ResData data) {

                            if (data.commanbeandata.result1.equals("success")) {

                                Intent intent = new Intent(Intent.ACTION_SEND);
                                intent.setType("text/plain");
                                intent.putExtra(Intent.EXTRA_SUBJECT, "Firebase Deep Link");
                                intent.putExtra(Intent.EXTRA_TEXT, "" + data.commanbeandata.link);
                                context.startActivity(intent);


                            } else {
                                Toast.makeText(context, "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onError(ResData data) {

                        }
                    });

                }
            });

            if (lists.get(position).document.length() > 0) {
                String fileName = lists.get(position).document.
                        substring(lists.get(position).document.lastIndexOf('/') + 1);
                holder.doc_name.setText(fileName);

            } else {
                holder.lin_document.setVisibility(View.GONE);

            }
            holder.lin_document.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(context, Document.class);
                    i.putExtra("link", lists.get(position).document);
                    Log.e("document:", lists.get(position).document);
                    context.startActivity(i);
                }
            });


            holder.image1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (lists.get(position).document.length() > 0) {
                        Intent i = new Intent(context, Document.class);
                        i.putExtra("link", lists.get(position).document);
                        context.startActivity(i);
                    } else {
                        int pos = 0;
                        Intent i = new Intent(context, ImagesGellery.class);
                        ArrayList<String> list = new ArrayList<>();
                        for (int j = 0; j < lists.get(position).gallery_list.size(); j++) {
                            list.add(lists.get(position).gallery_list.get(j));
                        }
                        i.putStringArrayListExtra("bean", list);
                        i.putExtra("position", pos);
                        context.startActivity(i);
                    }
                }
            });


            holder.image2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = 1;
                    if (lists.get(position).document.length() > 0)
                        pos--;

                    Intent i = new Intent(context, ImagesGellery.class);

                    ArrayList<String> list = new ArrayList<>();

                    for (int j = 0; j < lists.get(position).gallery_list.size(); j++) {

                        list.add(lists.get(position).gallery_list.get(j));

                    }
                    i.putStringArrayListExtra("bean", list);
                    i.putExtra("position", pos);


                    context.startActivity(i);

                }
            });


            holder.image3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = 2;

                    if (lists.get(position).document.length() > 0)
                        pos--;

                    Intent i = new Intent(context, ImagesGellery.class);

                    ArrayList<String> list = new ArrayList<>();

                    for (int j = 0; j < lists.get(position).gallery_list.size(); j++) {

                        list.add(lists.get(position).gallery_list.get(j));

                    }
                    i.putStringArrayListExtra("bean", list);
                    i.putExtra("position", pos);
                    context.startActivity(i);

                }
            });

            if (lists.get(position).document.length() > 0) {
                holder.gimage_layout.setVisibility(View.GONE);

                if (lists.get(position).gallery_list.size() >= 3) {
                    holder.r_layout3.setVisibility(View.VISIBLE);
                    holder.image2.setVisibility(View.VISIBLE);
                    holder.imagecount.setText("+" + (lists.get(position).gallery_list.size() - 2));

                    Picasso.get().load(lists.get(position).gallery_list.get(0)).into(holder.image2);
                    Picasso.get().load(lists.get(position).gallery_list.get(1)).into(holder.image3);


                } else if (lists.get(position).gallery_list.size() == 2) {
                    holder.r_layout3.setVisibility(View.VISIBLE);
                    holder.image2.setVisibility(View.VISIBLE);


                    Picasso.get().load(lists.get(position).gallery_list.get(0)).into(holder.image2);
                    Picasso.get().load(lists.get(position).gallery_list.get(1)).into(holder.image3);

                } else if (lists.get(position).gallery_list.size() == 1) {
                    holder.image2.setVisibility(View.VISIBLE);


                    Picasso.get().load(lists.get(position).gallery_list.get(0)).into(holder.image2);
                    holder.r_layout3.setVisibility(View.INVISIBLE);
                } else {
                    holder.r_layout3.setVisibility(View.INVISIBLE);
                    holder.image2.setVisibility(View.INVISIBLE);
                }


            } else {

                holder.gimage_layout.setVisibility(View.VISIBLE);
                if (lists.get(position).gallery_list.size() >= 3) {

                    holder.image1.setVisibility(View.VISIBLE);
                    holder.r_layout3.setVisibility(View.VISIBLE);
                    holder.image2.setVisibility(View.VISIBLE);
                    holder.imagecount.setText("+" + (lists.get(position).gallery_list.size() - 3));
                    Picasso.get().load(lists.get(position).gallery_list.get(0)).into(holder.image1);
                    Picasso.get().load(lists.get(position).gallery_list.get(1)).into(holder.image2);
                    Picasso.get().load(lists.get(position).gallery_list.get(2)).into(holder.image3);


                } else if (lists.get(position).gallery_list.size() == 2) {


                    Picasso.get().load(lists.get(position).gallery_list.get(0)).into(holder.image1);
                    Picasso.get().load(lists.get(position).gallery_list.get(1)).into(holder.image2);
                    holder.image1.setVisibility(View.VISIBLE);
                    holder.r_layout3.setVisibility(View.INVISIBLE);
                    holder.image2.setVisibility(View.VISIBLE);
                } else if (lists.get(position).gallery_list.size() == 1) {

                    Picasso.get().load(lists.get(position).gallery_list.get(0)).into(holder.image1);
                    holder.image1.setVisibility(View.VISIBLE);
                    holder.r_layout3.setVisibility(View.INVISIBLE);
                    holder.image2.setVisibility(View.INVISIBLE);
                } else {
                    holder.gimage_layout.setVisibility(View.GONE);
                    Log.e("gsize=", "=" + lists.get(position).gallery_list.size());
                }
            }


            holder.img_bookmark.setImageResource(R.drawable.ic_bookmarked);
            holder.llbookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    callToUpdateBookmark(lists, getActivity(), position);
                }
            });


            /*



            String bookmarkBool = lists.news.get(0).get(position).get("bookmark");
            if (bookmarkBool.equalsIgnoreCase("true")) {
                holder.img_bookmarked.setVisibility(View.VISIBLE);
                holder.img_bookmark.setVisibility(View.GONE);
            } else {
                holder.img_bookmarked.setVisibility(View.GONE);
                holder.img_bookmark.setVisibility(View.VISIBLE);
            }

            holder.llbookmark.setVisibility(View.GONE);*/
        }


        private void fireALarm(int position) {

            String userid = "";


            SharedPreferences myprf;

            myprf = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            if (!myprf.getBoolean("islogout", true)) {
                userid = getUserId();
            }

            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("user_id", userid);
            mapParams.put("post_id", lists.get(position).news_id);
            mapParams.put("message", "Report");
            mapParams.put("date", AppConstants.getDate());
            mapParams.put("device_id", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

            if (AppConstants.isNetworkAvailable(context)) {


                RequestClass.requesttoserver(context, mapParams, "report.php", new ServiceCallback() {
                    @Override
                    public void onSuccesss(ResData data) {

                        if (data.commanbeandata.result1.equals("success")) {

                            Toast.makeText(context, "eMasjid Admin Team Alerted", Toast.LENGTH_LONG).show();


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

        @Override
        public int getItemCount() {
            return lists.size();
        }

    }

    public void callToUpdateBookmark(final ArrayList<Bookmark_object> data, final Context context, final int pos) {

        processDailog = new CustomProgressDialog(context, "");
        processDailog.show();
        String Tag = "UpdateUserPassUrl";
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);
        String android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        String url = "http://keshavinfotechdemo.com/KESHAV/KG2/TRU/webservices/book_marked.php?device_id=" + android_id + "&news_id=" + data.get(pos).news_id;
        Log.e("MasjidDetailAdapter", "           " + url);
        StringRequest mStringRequest = new StringRequest(Request.Method.GET, url, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                processDailog.dismiss();
                try {
                    JSONObject main = new JSONObject(response);

                    Log.e("callToUpdateBookmark:", "    success     " + main);
                    JSONObject responseData = main.getJSONObject("responseData");
                    String result = responseData.getString("result");
                    String message = responseData.getString("message");

                    if (result.equalsIgnoreCase("success")) {

                        if (message.equalsIgnoreCase("News removed from bookmarke.")) {
                            Toast.makeText(context, "Pin Removed", Toast.LENGTH_SHORT).show();
                            String android_id = Settings.Secure.getString(getContext().getContentResolver(),
                                    Settings.Secure.ANDROID_ID);
                            callToGetAllBookmarks(android_id, context);
                        } else if (message.equalsIgnoreCase("News bookmarked successfully")) {
                            Toast.makeText(context, "Pin Saved", Toast.LENGTH_SHORT).show();
                        }
                    }
                    /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/

                } catch (JSONException e) {
                    e.printStackTrace();
                    /*getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                processDailog.dismiss();
            }
        }) {

          /*  @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("userid", userid);
                params.put("token", token);
                params.put("user_type", user_type);
                params.put("current_date", current_date);
                Log.e(Tag + "params", params + "");
                return params;
            }*/
        };
        mRequestQueue.add(mStringRequest);


    }


}