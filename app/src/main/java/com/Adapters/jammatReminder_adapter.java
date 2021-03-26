package com.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Class.DBHelper;
import com.Class.FavoriteMsjidBean;
import com.Class.JammatReminderBean;
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

public class jammatReminder_adapter extends RecyclerView.Adapter<jammatReminder_adapter.ViewHolder>

{
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
        public LinearLayout lin_parent;
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
    public jammatReminder_adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
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

        holder.msjid_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateJammatStatus(position);


              //  msjidUnfollow(list.get(position).masjid_id, position);
            }
        });

        Toast.makeText(context, "This", Toast.LENGTH_SHORT).show();
        
        if (list.get(position).status.equals("1")) {
            
            
            
            holder.msjid_unfollow.setImageResource(R.mipmap.reminder_off);
        } else {
            holder.msjid_unfollow.setImageResource(R.mipmap.reminder_on);

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


    private void msjidUnfollow(String masjid_id, final int pos) {
        if (AppConstants.isNetworkAvailable(context)) {

            final String status;

            if (list.get(pos).status.equals("1")) {
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

                        list.get(pos).status = status;
                        Toast.makeText(context, "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();
                        list.remove(pos);
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

    private void updateJammatStatus(int position)
    {

        Toast.makeText(context, position+"", Toast.LENGTH_SHORT).show();

        for (int i = 0; i < list.size(); i++) {


            if ((i == position)) {

                Log.e("boolan==>", "true");

                list.get(position).status = "1";
                list.get(position).masjid_name = "if";
            }
            else
            {

                Log.e("boolan==>", "false");

                list.get(position).status = "0";
                list.get(position).masjid_name = "else";



            }

        }

        for (int i = 0; i < list.size(); i++) {

            Log.e("status==>",list.get(position).status);

        }


        notifyDataSetChanged();


//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//
//                notifyDataSetChanged();
//
//            }
//        },1000);

    }


}
