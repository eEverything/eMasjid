package com.Adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Class.DBHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.custom_widgets.CustomTextView;
import com.emasjid.Document;
import com.emasjid.ImagesGellery;
import com.emasjid.MasjidDetail_userside;
import com.emasjid.R;
import com.other.DownloadFile;
import com.other.General;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
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

/**
 * Created by ki-user on 12/30/17.
 */

public class MasjidDetailAdapter extends RecyclerView.Adapter<MasjidDetailAdapter.ViewHolder> {
    private ListBean lists;
    Activity context;
    String[] dbparam = {"id", "user_type"};

    String masjidid, USERID;

    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public static CustomProgressDialog processDailog;

    public MasjidDetailAdapter(Activity context, ListBean lists, String masjidid, String USERID) {
        this.context = context;
        this.lists = lists;
        this.masjidid = masjidid;
        this.USERID = USERID;

    }

    public MasjidDetailAdapter(Activity context, ListBean lists) {
        this.context = context;
        this.lists = lists;
        this.masjidid = masjidid;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image1, image2, image3, user_image, img_bookmarked, img_bookmark;
        public TextView masjid_name, imagecount, masjid_detail, days, doc_name;
        public View layout;
        RelativeLayout r_layout3;
        public LinearLayout gimage_layout, lin_forground, lin_parent, alarm_share, lin_alarm, lin_share, lin_document,
                llDownload, llbookmark;
        public CustomTextView tvDownload;
        //  public FrameLayout lin_menu;

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
            // lin_menu = v.findViewById(R.id.lin_menu);
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


        }


    }

    public void callToUpdateBookmark(String news_id, final ViewHolder holder,String android_id) {
         /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);*/
        processDailog = new CustomProgressDialog(context, "");
        processDailog.show();
        String Tag = "UpdateUserPassUrl";
        RequestQueue mRequestQueue = Volley.newRequestQueue(context);




        String url = "http://keshavinfotechdemo.com/KESHAV/KG2/TRU/webservices/book_marked.php?device_id=" + android_id + "&news_id=" + news_id;
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

                        if (message.equalsIgnoreCase("News bookmarked successfully")) {
                            Toast.makeText(context, "Pin Saved" , Toast.LENGTH_SHORT).show();
                            holder.img_bookmarked.setVisibility(View.VISIBLE);
                            holder.img_bookmark.setVisibility(View.GONE);
                        } else if (message.equalsIgnoreCase("News removed from bookmarke.")) {
                            Toast.makeText(context, "Pin Removed", Toast.LENGTH_SHORT).show();
                            holder.img_bookmarked.setVisibility(View.GONE);
                            holder.img_bookmark.setVisibility(View.VISIBLE);
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


    public void bookmarkData(String news_id) {
        if (AppConstants.isNetworkAvailable(context)) {
            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("id", masjidid);
            mapParams.put("news_id", news_id);

            RequestClass.requesttoserverList(context, mapParams, "book_marked.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    Log.e("", "         " + data);
                }

                @Override
                public void onError(ResData data) {

                }
            });
        }
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.masjid_detail_item_without_slide, parent, false);


        ViewHolder vh = new ViewHolder(v);

        SharedPreferences myprf;


//        myprf = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
//        if(myprf.getBoolean("islogout", true))
//        {
        vh.alarm_share.setVisibility(View.VISIBLE);
        // }


        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        holder.masjid_name.setText(lists.selectedresponcefiel.get(0).get("name"));

        holder.masjid_detail.setText(lists.news.get(0).get(position).get("description"));

        final int imageCount = lists.gallery.get(position).size();
        int documentCount = 0;

        if (lists.news.get(0).get(position).get("document").length() >= 1)
            documentCount = 1;
        else
            documentCount = 0;


        holder.tvDownload.setText(context.getString(R.string.save) + "(" + (imageCount + documentCount) + ")");

        holder.llDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (lists.gallery.get(position).size() > 0 || lists.news.get(0).get(position).get("document").length() > 0) {

                    Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(500);
                        }
                    }

                    for (int j = 0; j < lists.gallery.get(position).size(); j++) {
                        String url = lists.gallery.get(position).get(j).get("image");
                        if (url.length() >= 1) {
                            Log.e("File Image: ", url + " == " + position);
                            if (url.substring(url.lastIndexOf(".") + 1).length() > 0 && General.isImage(url.substring(url.lastIndexOf(".")))) {
                                if (j == 0) {
                                    Toast.makeText(context, "Download Started.", Toast.LENGTH_SHORT).show();
                                }
                                new DownloadFile(context, lists.gallery.get(position).get(j).get("image"),lists.selectedresponcefiel.get(0).get("id"),"MasjidDetailAdapter1");
                            } else
                                Log.e("File Image URL: ", "Not a file URL.");

                        }
                    }

                    String url = lists.news.get(0).get(position).get("document");
                    String iidd = lists.selectedresponcefiel.get(0).get("profile_picture");
                    if (url.length() >= 1) {
                        Log.e("File Document: ", url + " == " + position);
                        if (url.substring(url.lastIndexOf(".") + 1).length() > 0 && General.isDocument(url.substring(url.lastIndexOf(".")))) {
                            Toast.makeText(context, "Download Started.", Toast.LENGTH_SHORT).show();
                            new DownloadFile(context, url,iidd,"MasjidDetailAdapter2");
                        } else {
                            Log.e("File Document URL: ", "Not a file URL.");
                        }
                    }

                } else {
                    Toast.makeText(context, "No Attachments Available.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.days.setText(lists.news.get(0).get(position).get("day_ago"));

        if (lists.selectedresponcefiel.get(0).get("profile_picture").length() > 0) {
            Picasso.get().load(lists.selectedresponcefiel.get(0).get("profile_picture")).resize(100, 100).into(holder.user_image,
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
                mapParams.put("masjid_id", masjidid);

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

        if (lists.news.get(0).get(position).get("document").length() > 0) {
            String fileName = lists.news.get(0).get(position).get("document").
                    substring(lists.news.get(0).get(position).get("document").lastIndexOf('/') + 1);
            holder.doc_name.setText(fileName);

            Log.e("alisha", "" + fileName + "=fileName=");
        } else {
            holder.lin_document.setVisibility(View.GONE);

        }
        holder.lin_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, Document.class);
                i.putExtra("link", lists.news.get(0).get(position).get("document"));
                Log.e("document:", lists.news.get(0).get(position).get("document"));
                context.startActivity(i);
            }
        });

        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lists.news.get(0).get(position).get("document").length() > 0) {
                    Intent i = new Intent(context, Document.class);
                    i.putExtra("link", lists.news.get(0).get(position).get("document"));
                    context.startActivity(i);
                } else {
                    int pos = 0;
                    Intent i = new Intent(context, ImagesGellery.class);
                    ArrayList<String> list = new ArrayList<>();
                    for (int j = 0; j < lists.gallery.get(position).size(); j++) {
                        list.add(lists.gallery.get(position).get(j).get("image"));
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
                if (lists.news.get(0).get(position).get("document").length() > 0)
                    pos--;

                Intent i = new Intent(context, ImagesGellery.class);

                ArrayList<String> list = new ArrayList<>();

                for (int j = 0; j < lists.gallery.get(position).size(); j++) {

                    list.add(lists.gallery.get(position).get(j).get("image"));

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

                if (lists.news.get(0).get(position).get("document").length() > 0)
                    pos--;

                Intent i = new Intent(context, ImagesGellery.class);

                ArrayList<String> list = new ArrayList<>();

                for (int j = 0; j < lists.gallery.get(position).size(); j++) {

                    list.add(lists.gallery.get(position).get(j).get("image"));

                }
                i.putStringArrayListExtra("bean", list);
                i.putExtra("position", pos);
                context.startActivity(i);

            }
        });

        if (lists.news.get(0).get(position).get("document").length() > 0) {
            holder.gimage_layout.setVisibility(View.GONE);

            if (lists.gallery.get(position).size() >= 3) {
                holder.r_layout3.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.VISIBLE);
                holder.imagecount.setText("+" + (lists.gallery.get(position).size() - 2));
                /*Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image2);
                Picasso.get().load(lists.gallery.get(position).get(1).get("image")).resize(100, 100).into(holder.image3);*/
                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).into(holder.image2);
                Picasso.get().load(lists.gallery.get(position).get(1).get("image")).into(holder.image3);


            } else if (lists.gallery.get(position).size() == 2) {
                holder.r_layout3.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.VISIBLE);

//                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image2);
//                Picasso.get().load(lists.gallery.get(position).get(1).get("image")).resize(100, 100).into(holder.image3);
                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).into(holder.image2);
                Picasso.get().load(lists.gallery.get(position).get(1).get("image")).into(holder.image3);

            } else if (lists.gallery.get(position).size() == 1) {
                holder.image2.setVisibility(View.VISIBLE);
//                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image2);

                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).into(holder.image2);
                holder.r_layout3.setVisibility(View.INVISIBLE);
            } else {
                holder.r_layout3.setVisibility(View.INVISIBLE);
                holder.image2.setVisibility(View.INVISIBLE);
            }


        } else {

            holder.gimage_layout.setVisibility(View.VISIBLE);
            if (lists.gallery.get(position).size() >= 3) {

                holder.image1.setVisibility(View.VISIBLE);
                holder.r_layout3.setVisibility(View.VISIBLE);
                holder.image2.setVisibility(View.VISIBLE);
                holder.imagecount.setText("+" + (lists.gallery.get(position).size() - 3));
                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).into(holder.image1);
                Picasso.get().load(lists.gallery.get(position).get(1).get("image")).into(holder.image2);
                Picasso.get().load(lists.gallery.get(position).get(2).get("image")).into(holder.image3);
                /*Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image1, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(R.mipmap.dummy).into(holder.image1);

                    }
                });*/
                /*Picasso.get().load(lists.gallery.get(position).get(1).get("image")).resize(100, 100).into(holder.image2, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(R.mipmap.dummy).into(holder.image2);
                    }
                });*/
                /*Picasso.get().load(lists.gallery.get(position).get(2).get("image")).resize(100, 100).into(holder.image3, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(R.mipmap.dummy).into(holder.image3);
                    }
                });*/

            } else if (lists.gallery.get(position).size() == 2) {
               /* Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image1,
                        new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(R.mipmap.dummy).into(holder.image1);
                            }
                        });
                Picasso.get().load(lists.gallery.get(position).get(1).get("image")).resize(100, 100).into(holder.image2, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(R.mipmap.dummy).into(holder.image2);
                    }
                });*/

                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).into(holder.image1);
                Picasso.get().load(lists.gallery.get(position).get(1).get("image")).into(holder.image2);
                holder.image1.setVisibility(View.VISIBLE);
                holder.r_layout3.setVisibility(View.INVISIBLE);
                holder.image2.setVisibility(View.VISIBLE);
            } else if (lists.gallery.get(position).size() == 1) {

               /* Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image1, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(R.mipmap.dummy).into(holder.image1);
                    }
                });
*/

                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).into(holder.image1);
                holder.image1.setVisibility(View.VISIBLE);
                holder.r_layout3.setVisibility(View.INVISIBLE);
                holder.image2.setVisibility(View.INVISIBLE);
            } else {
                holder.gimage_layout.setVisibility(View.GONE);
                Log.e("gsize=", "=" + lists.gallery.get(position).size());
            }

        }

        String bookmarkBool = lists.news.get(0).get(position).get("bookmark");
        if (bookmarkBool.equalsIgnoreCase("true")) {
            holder.img_bookmarked.setVisibility(View.VISIBLE);
            holder.img_bookmark.setVisibility(View.GONE);
        } else {
            holder.img_bookmarked.setVisibility(View.GONE);
            holder.img_bookmark.setVisibility(View.VISIBLE);
        }
        holder.llbookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                    } else {
                        //deprecated in API 26
                        v.vibrate(500);
                    }
                }

                    String android_id = Settings.Secure.getString(context.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    callToUpdateBookmark(lists.news.get(0).get(position).get("news_id"), holder,android_id);
               /* if (USERID != null && USERID.length() > 0) { } else {
                    Toast.makeText(context, "Please login first", Toast.LENGTH_SHORT).show();

                    AlertDialog alertDialog = new AlertDialog.Builder(context).create();
                    alertDialog.setCancelable(false);
                    alertDialog.setTitle(context.getResources().getString(R.string.app_name));
                    alertDialog.setMessage("Please login first");
                    alertDialog.setIcon(R.drawable.login_pro);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getResources().getString(R.string.txt_ok), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface aDialogInterface, int aI) {
                            aDialogInterface.dismiss();
                        }
                    });
                    alertDialog.show();
                }*/
            }
        });
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
        mapParams.put("post_id", lists.news.get(0).get(position).get("news_id"));
        mapParams.put("message", "Report");
        mapParams.put("date", AppConstants.getDate());
        mapParams.put("device_id", Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID));

        //   SetDataClass.setRequestparam(mapParams, alldata, requestparam);


        if (AppConstants.isNetworkAvailable(context)) {


            RequestClass.requesttoserver(context, mapParams, "report.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    if (data.commanbeandata.result1.equals("success")) {

//                            Toast.makeText(context, "You are Signed up Successfully", Toast.LENGTH_LONG).show();
//                            Intent intent = new Intent(context, Login.class);
//                            // intent.putExtra("user_id", data.data.selectedresponcefiel.get(0).get("user_id"));
//                            context.startActivity(intent);
                        //Toast.makeText(context, "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();
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
        return lists.news.get(0).size();
    }

}
