package com.emasjid.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.Adapters.fav_adapter;

import com.Adapters.searchmasjid_adapter;
import com.Class.DBHelper;
import com.Class.FavoriteMsjidBean;
import com.Class.SearchMasjidBean;
import com.emasjid.R;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.CommenRequest.GetXmlData;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.CommenRequest.SetDataClass;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

public class favourite_fragment extends Fragment {

    RecyclerView fav_rec;
    fav_adapter adapter;
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


//        getFavoriteMsjidList();


        return view;

    }

    @Override
    public void onResume() {
        getFavoriteMsjidList();
        super.onResume();
    }

    private void getFavoriteMsjidList() {

        if (AppConstants.isNetworkAvailable(getActivity())) {

            Map<String, String> mapParams = new HashMap<String, String>();

            if (myprf.getBoolean("islogout", true)) {
                mapParams.put("device_id", Settings.Secure.getString(getActivity().getContentResolver(),
                        Settings.Secure.ANDROID_ID));
            } else {
                mapParams.put("user_id", getUserId());
            }
            RequestClass.requesttoserverList(getActivity(), mapParams, "get_favourites.php", new ServiceCallback() {
                @Override
                public void onSuccesss(final ResData data) {


                    Map<String, String> mapParams1 = new HashMap<String, String>();

                    if (myprf.getBoolean("islogout", true)) {
                        mapParams1.put("user_id", "");

                        mapParams1.put("device_id", "" + Settings.Secure.getString(getActivity().getContentResolver(),
                                Settings.Secure.ANDROID_ID));

                        mapParams1.put("regId", "" + FirebaseInstanceId.getInstance().getToken());

                    } else {
                        mapParams1.put("user_id", "" + getUserId());

                        mapParams1.put("device_id", "" + Settings.Secure.getString(getActivity().getContentResolver(),
                                Settings.Secure.ANDROID_ID));

                        mapParams1.put("regId", "" + FirebaseInstanceId.getInstance().getToken());
                    }

                    RequestClass.requesttoserver_noprocees_bar(getActivity(), mapParams1, "Pushnotification_Android/register1.php", new ServiceCallback() {
                        @Override
                        public void onSuccesss(ResData datafirebas) {

                            List<FavoriteMsjidBean> list = new ArrayList<>();

                            if (data.data.selectedresponcefiel.size() > 0) {


                                for (int i = 0; i < data.data.selectedresponcefiel.size(); i++) {


                                    FavoriteMsjidBean bean = new FavoriteMsjidBean();

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


                                    list.add(bean);
                                }

                                Log.e("List size", list.size() + "");

                                adapter = new fav_adapter(getActivity(), list);
                                fav_rec.setAdapter(adapter);

                            } else {
                                //  Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                                adapter = new fav_adapter(getActivity(), list);
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
}
