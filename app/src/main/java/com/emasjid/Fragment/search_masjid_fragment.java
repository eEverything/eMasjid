package com.emasjid.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.Adapters.searchmasjid_adapter;
import com.Class.DBHelper;
import com.Class.SearchMasjidBean;
import com.emasjid.R;

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

public class search_masjid_fragment extends Fragment {

    RecyclerView searchlist;
    searchmasjid_adapter adapter;
    LinearLayoutManager mLayoutManager;
    String[] dbparam = {"id"};
    String[][] dataarr;
    Map<String, View> viewlist = new HashMap<String, View>();

    LinearLayout lin_location;


    String userid;
    SharedPreferences myprf;
    SharedPreferences.Editor myedit;

    String latitude, longitude, radius = "80";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        View view = inflater.inflate(R.layout.search, null);
        myprf = getActivity().getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        myedit = myprf.edit();


        searchlist = (RecyclerView) view.findViewById(R.id.searchlist);
        lin_location = view.findViewById(R.id.lin_location);
        mLayoutManager = new LinearLayoutManager(getActivity());
        searchlist.setLayoutManager(mLayoutManager);

        lin_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                    //
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
//                        1000,
//                        0, (LocationListener) getActivity());


                if (latitude != null && latitude.length() > 0 && longitude != null && longitude.length() > 0 && radius != null && radius.length() > 0) {
                    getMasjidByLocation("");

                }


            }
        });

        dataarr = GetXmlData.getXmlData(getActivity(), "search.xml");
        SetDataClass.findallviewonxml(view, dataarr, viewlist);


        ((EditText) viewlist.get("search_text")).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchMasjid(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


//        adapter = new searchmasjid_adapter(getActivity());
        //searchlist.setAdapter(adapter);
        if (myprf.getBoolean("islogout", true)) {

        } else {


            DBHelper db1 = DBHelper.getInstance(getActivity());
            db1.open();

            Map<String, String> data = db1.getUser();

            String[] arrays = db1.getUserTablecolomsstringarray();


            for (int i = 0; i < dbparam.length; i++) {
                for (int j = 0; j < data.size(); j++) {

                    if (dbparam[i].equals(arrays[j])) {
                        userid = data.get(arrays[j]);
                    }

                }

            }


            db1.close();
        }

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();


    }


    private void getMasjidByLocation(String searchtext) {


        if (AppConstants.isNetworkAvailable(getActivity())) {

            Map<String, String> mapParams = new HashMap<String, String>();


            if (myprf.getBoolean("islogout", true)) {
                mapParams.put("search_text", "");
                mapParams.put("lat", latitude);
                mapParams.put("long", longitude);
                mapParams.put("radius", radius);
                mapParams.put("device_id", Settings.Secure.getString(getActivity().getContentResolver(),
                        Settings.Secure.ANDROID_ID));
            } else {


                mapParams.put("search_text", searchtext);
                mapParams.put("user_id", userid);
                mapParams.put("lat", latitude);
                mapParams.put("long", longitude);
                mapParams.put("radius", radius);
            }

            RequestClass.requesttoserverList_no_progress(getActivity(), mapParams, "search_masjid.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    List<SearchMasjidBean> list = new ArrayList<>();

                    if (data.data.selectedresponcefiel.size() > 0) {


                        for (int i = 0; i < data.data.selectedresponcefiel.size(); i++) {


                            SearchMasjidBean bean = new SearchMasjidBean();
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

                            list.add(bean);
                        }

                        Log.e("List size", list.size() + "");

                        adapter = new searchmasjid_adapter(getActivity(), list);
                        searchlist.setAdapter(adapter);

                    } else {
                        Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                        adapter = new searchmasjid_adapter(getActivity(), list);
                        searchlist.setAdapter(adapter);

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


    private void searchMasjid(String searchtext) {


        if (AppConstants.isNetworkAvailable(getActivity())) {

            Map<String, String> mapParams = new HashMap<String, String>();


            if (myprf.getBoolean("islogout", true)) {
                mapParams.put("search_text", searchtext);
                mapParams.put("device_id", Settings.Secure.getString(getActivity().getContentResolver(),
                        Settings.Secure.ANDROID_ID));
            } else {


                mapParams.put("search_text", searchtext);
                mapParams.put("user_id", userid);
            }

            RequestClass.requesttoserverList_no_progress(getActivity(), mapParams, "search_masjid.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    List<SearchMasjidBean> list = new ArrayList<>();

                    if (data.data.selectedresponcefiel.size() > 0) {


                        for (int i = 0; i < data.data.selectedresponcefiel.size(); i++) {


                            SearchMasjidBean bean = new SearchMasjidBean();
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

                            Log.e("count=", "" + data.data.selectedresponcefiel.get(i).get("follow_counter"));

                            list.add(bean);
                        }

                        Log.e("List size", list.size() + "");

                        adapter = new searchmasjid_adapter(getActivity(), list);
                        searchlist.setAdapter(adapter);

                    } else {
                        Toast.makeText(getActivity(), "No data found", Toast.LENGTH_SHORT).show();
                        adapter = new searchmasjid_adapter(getActivity(), list);
                        searchlist.setAdapter(adapter);

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

}
