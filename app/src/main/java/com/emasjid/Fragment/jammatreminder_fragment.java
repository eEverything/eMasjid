package com.emasjid.Fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.fav_adapter;
import com.Adapters.jammatReminder_adapter;
import com.Class.DBHelper;
import com.Class.JammatReminderBean;
import com.Class.MyReceiver;
import com.Class.PrayerTimesBean;
import com.emasjid.R;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.CommenRequest.GetXmlData;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.CommenRequest.SetDataClass;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

import static android.content.Context.ALARM_SERVICE;

public class jammatreminder_fragment extends Fragment {

    RecyclerView fav_rec;
    jammatReminder_adapter adapter;
    LinearLayoutManager mLayoutManager;

    String[][] dataarr;
    Map<String, View> viewlist = new HashMap<String, View>();

    String[] dbparam = {"id"};

    String userid;

    SharedPreferences myprf;
    SharedPreferences.Editor myedit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.favorites_list, null);

        myprf = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        myedit = myprf.edit();

        fav_rec = (RecyclerView) view.findViewById(R.id.favoritelist);
        mLayoutManager = new LinearLayoutManager(getActivity());
        fav_rec.setLayoutManager(mLayoutManager);

        dataarr = GetXmlData.getXmlData(getActivity(), "favorites_list.xml");
        SetDataClass.findallviewonxml(view, dataarr, viewlist);

        getFavoriteMsjidList();

        return view;
    }

    private void getFavoriteMsjidList() {

        if (AppConstants.isNetworkAvailable(getActivity())) {

            Map<String, String> mapParams = new HashMap<String, String>();

            if (myprf.getBoolean("islogout", true)) {
                mapParams.put("device_id", Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
            } else {
                mapParams.put("user_id", getUserId());
            }
            RequestClass.requesttoserverList(getActivity(), mapParams, "get_favourites.php", new ServiceCallback() {
                @Override
                public void onSuccesss(final ResData data) {

                    Map<String, String> mapParams1 = new HashMap<String, String>();

                    if (myprf.getBoolean("islogout", true)) {
                        mapParams1.put("user_id", "");
                        mapParams1.put("device_id", "" + Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
                        mapParams1.put("regId", "" + FirebaseInstanceId.getInstance().getToken());
                    } else {
                        mapParams1.put("user_id", "" + getUserId());
                        mapParams1.put("device_id", "" + Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
                        mapParams1.put("regId", "" + FirebaseInstanceId.getInstance().getToken());
                    }

                    RequestClass.requesttoserver_noprocees_bar(getActivity(), mapParams1, "Pushnotification_Android/register1.php", new ServiceCallback() {
                        @Override
                        public void onSuccesss(ResData datafirebas) {

                            List<JammatReminderBean> list = new ArrayList<>();

                            if (data.data.selectedresponcefiel.size() > 0) {

                                for (int i = 0; i < data.data.selectedresponcefiel.size(); i++) {

                                    JammatReminderBean bean = new JammatReminderBean();
                                    bean.masjid_id = data.data.selectedresponcefiel.get(i).get("masjid_id");
                                    bean.masjid_name = data.data.selectedresponcefiel.get(i).get("masjid_name");
                                    bean.email = data.data.selectedresponcefiel.get(i).get("email");
                                    bean.password = data.data.selectedresponcefiel.get(i).get("password");
                                    bean.address = data.data.selectedresponcefiel.get(i).get("address");
                                    bean.lattitude = data.data.selectedresponcefiel.get(i).get("lattitude");
                                    bean.longitude = data.data.selectedresponcefiel.get(i).get("longitude");
                                    bean.city = data.data.selectedresponcefiel.get(i).get("city");
                                    bean.town = data.data.selectedresponcefiel.get(i).get("town");
                                    bean.phone_number = data.data.selectedresponcefiel.get(i).get("phone_number");
                                    bean.website = data.data.selectedresponcefiel.get(i).get("website");
                                    bean.about_us = data.data.selectedresponcefiel.get(i).get("about_us");
                                    bean.profile_picture = data.data.selectedresponcefiel.get(i).get("profile_picture");
                                    bean.is_favourite = data.data.selectedresponcefiel.get(i).get("is_favourite");
                                    bean.follow_counter = data.data.selectedresponcefiel.get(i).get("follow_counter");
                                    bean.latest_msg = data.data.selectedresponcefiel.get(i).get("latest_masjid_post");


                                    if (bean.masjid_id.equals(myprf.getString("jammat", ""))) {
                                        bean.status = "1";
                                    }

                                    list.add(bean);
                                }

                                Log.e("List size", list.size() + "");

                                adapter = new jammatReminder_adapter(getActivity(), list);
                                fav_rec.setAdapter(adapter);

                            } else {
                                //  Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                                adapter = new jammatReminder_adapter(getActivity(), list);
                                fav_rec.setAdapter(adapter);

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
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        }

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

                }

            }

        }

        db1.close();
        return "";
    }


    private void getPrayerTimes(final String id) {

        if (AppConstants.isNetworkAvailable(getActivity())) {

            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("masjid_id", id);

            RequestClass.requesttoserverList(getActivity(), mapParams, "prayer-time.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    for (int i = 525; i < 530; i++) {
                        cancel(i);

                    }

                    if (data.data.selectedresponcefiel.size() > 0) {

                        myedit.putString("jammat", id);
                        myedit.apply();

                        for (int i = 0; i < data.data.selectedresponcefiel.size(); i++) {

                            PrayerTimesBean bean = new PrayerTimesBean();

                            bean.fajr_time = data.data.selectedresponcefiel.get(i).get("fajr_time");
                            bean.duhr_time = data.data.selectedresponcefiel.get(i).get("duhr_time");
                            bean.asar_time = data.data.selectedresponcefiel.get(i).get("asar_time");
                            bean.magrib_time = data.data.selectedresponcefiel.get(i).get("magrib_time");
                            bean.isha_time = data.data.selectedresponcefiel.get(i).get("isha_time");

                            Log.e("fajr_time ", bean.fajr_time);
                            Log.e("duhr_time ", bean.duhr_time);
                            Log.e("asar_time ", bean.asar_time);
                            Log.e("magrib_time ", bean.magrib_time);
                            Log.e("isha_time ", bean.isha_time);

                            String[] fajr_alarm = bean.fajr_time.split(":");
                            String[] duhr_alarm = bean.duhr_time.split(":");
                            String[] asar_alarm = bean.asar_time.split(":");
                            String[] magrib_alarm = bean.magrib_time.split(":");
                            String[] isha_alarm = bean.isha_time.split(":");

                            SharedPreferences jammat_reminder = getContext().getSharedPreferences("jammat_reminder", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = jammat_reminder.edit();
                            editor.putString("fajr_time", bean.fajr_time);
                            editor.putString("duhr_time", bean.duhr_time);
                            editor.putString("asar_time", bean.asar_time);
                            editor.putString("magrib_time", bean.magrib_time);
                            editor.putString("isha_time", bean.isha_time);
                            editor.apply();

                            Calendar fajr_calendar = Calendar.getInstance();
                            if (fajr_alarm[0].startsWith("0")) {
                                String hoursFinal = fajr_alarm[0].substring(1);
                                Log.e("HHHAHAHHA", hoursFinal);
                                fajr_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hoursFinal));
                            } else {
                                fajr_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fajr_alarm[0]));
                            }
                            if (fajr_alarm[1].startsWith("0")) {
                                String minutesFinal = fajr_alarm[1].substring(1);
                                fajr_calendar.set(Calendar.MINUTE, Integer.parseInt(minutesFinal));
                            } else {
                                fajr_calendar.set(Calendar.MINUTE, Integer.parseInt(fajr_alarm[1]));
                            }
                            //fajr_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(fajr_alarm[0]));
                            //fajr_calendar.set(Calendar.MINUTE, Integer.parseInt(fajr_alarm[1]));
                            fajr_calendar.set(Calendar.SECOND, 0);
                            fajr_calendar.add(Calendar.MINUTE, -30);

                            setAlarm(fajr_calendar, "Jamaat reminder : Next prayer will be at " + fajr_alarm[0] + ":" + fajr_alarm[1], 525);

                            Calendar duhr_calendar = Calendar.getInstance();
                            if (duhr_alarm[0].startsWith("0")) {
                                String hoursFinal = duhr_alarm[0].substring(1);
                                duhr_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hoursFinal));
                            } else {
                                duhr_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(duhr_alarm[0]));
                            }
                            if (duhr_alarm[1].startsWith("0")) {
                                String minutesFinal = duhr_alarm[1].substring(1);
                                duhr_calendar.set(Calendar.MINUTE, Integer.parseInt(minutesFinal));
                            } else {
                                duhr_calendar.set(Calendar.MINUTE, Integer.parseInt(duhr_alarm[1]));
                            }
                            //duhr_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(duhr_alarm[0]));
                            //duhr_calendar.set(Calendar.MINUTE, Integer.parseInt(duhr_alarm[1]));
                            duhr_calendar.set(Calendar.SECOND, 0);
                            duhr_calendar.add(Calendar.MINUTE, -30);

                            setAlarm(duhr_calendar, "Jamaat reminder : Next prayer will be at " + duhr_alarm[0] + ":" + duhr_alarm[1], 526);

                            Calendar asar_calendar = Calendar.getInstance();
                            if (asar_alarm[0].startsWith("0")) {
                                String hoursFinal = asar_alarm[0].substring(1);
                                asar_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hoursFinal));
                            } else {
                                asar_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(asar_alarm[0]));
                            }
                            if (asar_alarm[1].startsWith("0")) {
                                String minutesFinal = asar_alarm[1].substring(1);
                                asar_calendar.set(Calendar.MINUTE, Integer.parseInt(minutesFinal));
                            } else {
                                asar_calendar.set(Calendar.MINUTE, Integer.parseInt(asar_alarm[1]));
                            }
                            //asar_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(asar_alarm[0]));
                            //asar_calendar.set(Calendar.MINUTE, Integer.parseInt(asar_alarm[1]));
                            asar_calendar.set(Calendar.SECOND, 0);
                            asar_calendar.add(Calendar.MINUTE, -30);

                            setAlarm(asar_calendar, "Jamaat reminder : Next prayer will be at " + asar_alarm[0] + ":" + asar_alarm[1], 527);

                            Calendar magrib_calendar = Calendar.getInstance();
                            if (magrib_alarm[0].startsWith("0")) {
                                String hoursFinal = magrib_alarm[0].substring(1);
                                magrib_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hoursFinal));
                            } else {
                                magrib_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(magrib_alarm[0]));
                            }
                            if (magrib_alarm[1].startsWith("0")) {
                                String minutesFinal = magrib_alarm[1].substring(1);
                                magrib_calendar.set(Calendar.MINUTE, Integer.parseInt(minutesFinal));
                            } else {
                                magrib_calendar.set(Calendar.MINUTE, Integer.parseInt(magrib_alarm[1]));
                            }
                            //magrib_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(magrib_alarm[0]));
                            //magrib_calendar.set(Calendar.MINUTE, Integer.parseInt(magrib_alarm[1]));
                            magrib_calendar.set(Calendar.SECOND, 0);
                            magrib_calendar.add(Calendar.MINUTE, -30);

                            setAlarm(magrib_calendar, "Jamaat reminder : Next prayer will be at " + magrib_alarm[0] + ":" + magrib_alarm[1], 528);

                            Calendar isha_calendar = Calendar.getInstance();
                            if (isha_alarm[0].startsWith("0")) {
                                String hoursFinal = isha_alarm[0].substring(1);
                                isha_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hoursFinal));
                            } else {
                                isha_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(isha_alarm[0]));
                            }
                            if (isha_alarm[1].startsWith("0")) {
                                String minutesFinal = isha_alarm[1].substring(1);
                                isha_calendar.set(Calendar.MINUTE, Integer.parseInt(minutesFinal));
                            } else {
                                isha_calendar.set(Calendar.MINUTE, Integer.parseInt(isha_alarm[1]));
                            }
                            //isha_calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(isha_alarm[0]));
                            //isha_calendar.set(Calendar.MINUTE, Integer.parseInt(isha_alarm[1]));
                            isha_calendar.set(Calendar.SECOND, 0);
                            isha_calendar.add(Calendar.MINUTE, -30);

                            setAlarm(isha_calendar, "Jamaat reminder : Next prayer will be at " + isha_alarm[0] + ":" + isha_alarm[1], 529);

                        }

                    } else {
                        Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onError(ResData data) {

                }
            });

        } else {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void setAlarm(Calendar calendar, String msg, int requestCode) {
        PendingIntent pendingIntent;
        Intent myIntent = new Intent(getActivity(), MyReceiver.class);
        myIntent.putExtra("message", msg);


        pendingIntent = PendingIntent.getBroadcast(getActivity(), requestCode, myIntent, 0);

        AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

    }


    public void cancel(int reqCode) {
        Intent myIntent = new Intent(getActivity(), MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(),
                reqCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();

    }


    public class jammatReminder_adapter extends RecyclerView.Adapter<jammatReminder_adapter.ViewHolder> {
        private List<String> values;
        Activity context;

        List<JammatReminderBean> list;
        SharedPreferences myprf;
        SharedPreferences.Editor myedit;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public ImageView image_path;
            public TextView title;
            public View layout;
            public LinearLayout lin_parent, ll_main;
            public ImageView msjid_image, msjid_unfollow;
            public TextView msjid_name, msjid_town, latestmsg;

            public ViewHolder(View v) {
                super(v);
                layout = v;

                lin_parent = (LinearLayout) v.findViewById(R.id.line_fav_parent);
                msjid_image = v.findViewById(R.id.msjid_image);
                msjid_name = v.findViewById(R.id.msjid_name);
                msjid_town = v.findViewById(R.id.msjid_town);
                msjid_unfollow = v.findViewById(R.id.msjid_unfollow);
                latestmsg = v.findViewById(R.id.latestmsg);
            }
        }


        public jammatReminder_adapter(Activity context, List<JammatReminderBean> list) {
            this.context = context;
            this.list = list;
            if (context != null) {
                myprf = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
                myedit = myprf.edit();
            }


        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
            // create a new view
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());

            View v = inflater.inflate(R.layout.reminder_item, parent, false);

            ViewHolder vh = new ViewHolder(v);

            return vh;
        }


        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {


            if (list.get(position).town != null && list.get(position).town.length() > 0) {
                holder.msjid_town.setText(list.get(position).town);

            }

            if (list.get(position).masjid_name != null && list.get(position).masjid_name.length() > 0) {
                holder.msjid_name.setText(list.get(position).masjid_name);

            }

            holder.lin_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (list.get(position).status.equals("1")) {

                        showDialog(position);


                    } else {
                        updateJammatStatus(position);

                    }


                    // msjidUnfollow(list.get(position).masjid_id, position);


                }
            });

            if (list.get(position).status.equals("1")) {
                holder.msjid_unfollow.setImageResource(R.mipmap.reminder_on);
            } else {
                holder.msjid_unfollow.setImageResource(R.mipmap.reminder_off);

            }


            if (list.get(position).profile_picture != null && list.get(position).profile_picture.length() > 0) {

                Picasso.get().load(list.get(position).profile_picture).resize(100, 100).into(holder.msjid_image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get().load(R.drawable.user_dummy).into(holder.msjid_image);
                    }
                });
            }


        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return list.size();
        }

        public void cancel(int requestCode) {
            Intent myIntent = new Intent(context, MyReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
            am.cancel(pendingIntent);
            pendingIntent.cancel();

        }


        private void updateJammatStatus(int position) {

            for (int i = 0; i < list.size(); i++) {

                if (i == position) {
                    list.get(i).status = "1";
                } else {
                    list.get(i).status = "0";

                }


//                if ((i == position)) {
//                    JammatReminderBean bean = new JammatReminderBean();
//                    bean.masjid_name = list.get(i).masjid_name;
//                    bean.masjid_id = list.get(i).masjid_id;
//                    bean.city = list.get(i).city;
//                    bean.profile_picture = list.get(i).profile_picture;
//                    bean.status = "1";
//
//                    list.set(i, bean);
//                } else {
//                    JammatReminderBean bean = new JammatReminderBean();
//                    bean.masjid_name = list.get(i).masjid_name;
//                    bean.masjid_id = list.get(i).masjid_id;
//                    bean.city = list.get(i).city;
//                    bean.profile_picture = list.get(i).profile_picture;
//                    bean.status = "0";
//
//                    Log.e("boolan==>", "false");
//                    list.set(i, bean);
//
//                }

            }

            notifyDataSetChanged();

            getPrayerTimes(list.get(position).masjid_id);

        }


        private void showDialog(final int position) {
            final Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.reminder_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#11000000")));
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);

            final TextView text = dialog.findViewById(R.id.alert_text);
            final Button yes = dialog.findViewById(R.id.yes);
            final Button no = dialog.findViewById(R.id.no);
            Log.e("list.get(position).masjid_name", "" + list.get(position).masjid_name);
            text.setText(list.get(position).masjid_name + " is currently set as your default jamaat reminder, are you sure you want to deactivate reminder?");

            no.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

            yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();

                    list.get(position).status = "0";

                    for (int i = 525; i < 530; i++) {

                        cancel(i);

                    }

                    myedit.putString("jammat", "");
                    myedit.apply();

                    notifyDataSetChanged();


                }
            });


            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.MATCH_PARENT;
            lp.gravity = Gravity.CENTER;


            if (!dialog.isShowing())
                dialog.show();
            dialog.getWindow().setAttributes(lp);
        }

        String[] dbparam = {"id"};

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


}
