package com.emasjid.Fragment;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.Adapters.fav_adapter;
import com.Class.DBHelper;
import com.Class.PrayerTimesBean;
import com.emasjid.R;
import com.philliphsu.numberpadtimepicker.NumberPadTimePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import http.CommenRequest.GetXmlData;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.CommenRequest.SetDataClass;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

public class prayertimes_fragment extends Fragment {

    RecyclerView fav_rec;
    fav_adapter adapter;
    LinearLayoutManager mLayoutManager;

    String[][] dataarr;
    Map<String, View> viewlist = new HashMap<String, View>();

    String[] dbparam = {"id"};

    String userid;
    String hourString;
    String minuteString;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_prayer_time, null);

        dataarr = GetXmlData.getXmlData(getActivity(), "activity_prayer_time.xml");
        SetDataClass.findallviewonxml(view, dataarr, viewlist);


        getPrayerTimes();


        ((Button) viewlist.get("update")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePrayerTimes();
            }
        });


        ((ImageView) viewlist.get("edit_fajr")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerCustom("fajr");

               /* NumberPadTimePickerDialog dialog = new NumberPadTimePickerDialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog,new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        Log.e("selected time",selectedHour+":"+selectedMinute);
                    }
                },true);//Yes 24 hour time
                dialog.show();

*/
            }

        });

        ((ImageView) viewlist.get("edit_duhr")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerCustom("duhr");
            }
        });
        ((ImageView) viewlist.get("edit_asar")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerCustom("asar");
            }
        });
        ((ImageView) viewlist.get("edit_magrib")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerCustom("magrib");
            }
        });
        ((ImageView) viewlist.get("edit_isha")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickerCustom("isha");
            }
        });


        return view;

    }


    public void pickerCustom(final String type) {

        final Calendar mcurrentTime = Calendar.getInstance();
        // hour = Integer.toString(mcurrentTime.get(Calendar.HOUR_OF_DAY));
        //minute = Integer.toString(mcurrentTime.get(Calendar.MINUTE));
        final TimePickerDialog mTimePicker;

        NumberPadTimePickerDialog dialog = new NumberPadTimePickerDialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                Log.e("selected time", selectedHour + ":" + selectedMinute);


                if (selectedHour < 10) {
                    hourString = "0" + selectedHour;
                } else {
                    hourString = selectedHour + "";

                }

                if (selectedMinute < 10) {
                    minuteString = "0" + selectedMinute;
                } else {
                    minuteString = selectedMinute + "";
                }


                switch (type) {

                    case "fajr":
                        ((TextView) viewlist.get("fajr_time")).setText(hourString + ":" + minuteString + ":00");
                        break;

                    case "duhr":
                        ((TextView) viewlist.get("duhr_time")).setText(hourString + ":" + minuteString + ":00");

                        break;

                    case "asar":
                        ((TextView) viewlist.get("asar_time")).setText(hourString + ":" + minuteString + ":00");

                        break;

                    case "magrib":
                        ((TextView) viewlist.get("magrib_time")).setText(hourString + ":" + minuteString + ":00");

                        break;

                    case "isha":
                        ((TextView) viewlist.get("isha_time")).setText(hourString + ":" + minuteString + ":00");

                        break;
                }
            }
        }, true);//Yes 24 hour time
        dialog.show();
    }


    private void updatePrayerTimes() {
        if (AppConstants.isNetworkAvailable(getActivity())) {

            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("masjid_id", getUserId());
            mapParams.put("fajr_time", ((TextView) viewlist.get("fajr_time")).getText().toString());
            mapParams.put("duhr_time", ((TextView) viewlist.get("duhr_time")).getText().toString());
            mapParams.put("asar_time", ((TextView) viewlist.get("asar_time")).getText().toString());
            mapParams.put("magrib_time", ((TextView) viewlist.get("magrib_time")).getText().toString());
            mapParams.put("isha_time", ((TextView) viewlist.get("isha_time")).getText().toString());


            RequestClass.requesttoserver(getActivity(), mapParams, "update-prayer-time.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {


                    if (data.commanbeandata.result1.equals("success")) {

                        Toast.makeText(getActivity(), data.commanbeandata.message + "", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getActivity(), data.commanbeandata.message + "", Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onError(ResData data) {

                }
            });


        }
    }


    private void getPrayerTimes() {

        if (AppConstants.isNetworkAvailable(getActivity())) {

            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("masjid_id", getUserId());

            RequestClass.requesttoserverList(getActivity(), mapParams, "prayer-time.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {


                    if (data.data.selectedresponcefiel.size() > 0) {


                        for (int i = 0; i < data.data.selectedresponcefiel.size(); i++) {


                            PrayerTimesBean bean = new PrayerTimesBean();

                            bean.fajr_time = data.data.selectedresponcefiel.get(i).get("fajr_time");
                            bean.duhr_time = data.data.selectedresponcefiel.get(i).get("duhr_time");
                            bean.asar_time = data.data.selectedresponcefiel.get(i).get("asar_time");
                            bean.magrib_time = data.data.selectedresponcefiel.get(i).get("magrib_time");
                            bean.isha_time = data.data.selectedresponcefiel.get(i).get("isha_time");


                            ((TextView) viewlist.get("fajr_time")).setText(bean.fajr_time);
                            ((TextView) viewlist.get("duhr_time")).setText(bean.duhr_time);
                            ((TextView) viewlist.get("asar_time")).setText(bean.asar_time);
                            ((TextView) viewlist.get("magrib_time")).setText(bean.magrib_time);
                            ((TextView) viewlist.get("isha_time")).setText(bean.isha_time);


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



}
