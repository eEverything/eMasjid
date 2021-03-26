package com.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Class.DBHelper;
import com.Class.SearchMasjidBean;
import com.emasjid.MasjidDetail_userside;
import com.emasjid.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

/**
 * Created by ki-jayu on 28/08/2017.
 */

public class searchmasjid_adapter extends RecyclerView.Adapter<searchmasjid_adapter.ViewHolder>

{

    private List<String> values;
    Activity context;
    List<SearchMasjidBean> list;
    SharedPreferences myprf;
    SharedPreferences.Editor myedit;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView plus_img, msjid_image;
        public TextView folowecount, msjid_name, msjid_town;
        public View layout;
        public LinearLayout lin_main;

        public ViewHolder(View v) {
            super(v);
            layout = v;

            msjid_image = v.findViewById(R.id.msjid_image);
            plus_img = v.findViewById(R.id.plus_img);
            msjid_name = v.findViewById(R.id.msjid_name);
            msjid_town = v.findViewById(R.id.msjid_town);
            folowecount = v.findViewById(R.id.folowecount);
            lin_main = v.findViewById(R.id.ll_main);

        }
    }

    public searchmasjid_adapter(Activity context, List<SearchMasjidBean> list) {
        this.context = context;
        this.list = list;

        myprf = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
        myedit = myprf.edit();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public searchmasjid_adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.search_masjid_item, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.msjid_name.setText(list.get(position).masjid_name);
        holder.msjid_town.setText(list.get(position).town);
        holder.folowecount.setText(list.get(position).follow_counter + " Follower");

        holder.lin_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, MasjidDetail_userside.class);
                i.putExtra("masjidid", list.get(position).masjid_id);
                i.putExtra("masjidstatus", "" + list.get(position).is_favourite);
                i.putExtra("type", "search");
                context.startActivity(i);

            }
        });

        if (list.get(position).is_favourite != null && list.get(position).is_favourite.length() > 0 && list.get(position).is_favourite.equals("1")) {
            holder.plus_img.setImageResource(R.mipmap.unfollow);
        } else {
            holder.plus_img.setImageResource(R.mipmap.follow);
        }

        holder.plus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msjidUnfollow(list.get(position).masjid_id, position);
            }
        });


        if (list.get(position).profile_picture != null && list.get(position).profile_picture.length() > 0) {

            Picasso.get().load(list.get(position).profile_picture).resize(150, 150).into(holder.msjid_image, new Callback() {
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


    private void msjidUnfollow(String masjid_id, final int pos) {
        if (AppConstants.isNetworkAvailable(context)) {

            final String status;

            if (list.get(pos).is_favourite != null && list.get(pos).is_favourite.length() > 0 && list.get(pos).is_favourite.equals("1")) {
                status = "0";
            } else {
                status = "1";
            }


            Map<String, String> mapParams = new HashMap<String, String>();

            if (myprf.getBoolean("islogout", true)) {
                mapParams.put("device_id", Settings.Secure.getString(context.getContentResolver(),
                        Settings.Secure.ANDROID_ID));
                mapParams.put("masjid_id", masjid_id);
                mapParams.put("status", status);
            } else {


                mapParams.put("masjid_id", masjid_id);
                mapParams.put("status", status);
                mapParams.put("user_id", getUserId());
            }

            RequestClass.requesttoserver(context, mapParams, "follow_masjid.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    if (data.commanbeandata.result1.equals("success")) {

                        list.get(pos).is_favourite = status;
                        if(status.equalsIgnoreCase("1"))
                        {
                            Toast.makeText(context, "Added to favourites", Toast.LENGTH_LONG).show();

                        }
                        else if(status.equalsIgnoreCase("0"))
                        {
                            Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_LONG).show();

                        }
                      //  Toast.makeText(context, "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();
                        notifyDataSetChanged();


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
