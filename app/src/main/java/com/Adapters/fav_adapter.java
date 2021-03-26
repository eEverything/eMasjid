package com.Adapters;

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
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.Class.DBHelper;
import com.Class.FavoriteMsjidBean;
import com.Class.MyReceiver;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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

import static android.content.Context.ALARM_SERVICE;


public class fav_adapter extends RecyclerView.Adapter<fav_adapter.ViewHolder> {
    private List<String> values;
    Activity context;

    List<FavoriteMsjidBean> list;
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


    public fav_adapter(Activity context, List<FavoriteMsjidBean> list) {
        this.context = context;
        this.list = list;
        if (context != null) {
            myprf = context.getSharedPreferences("UserInfo", Context.MODE_PRIVATE);
            myedit = myprf.edit();
        }


    }

    // Create new views (invoked by the layout manager)
    @Override
    public fav_adapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.favorites_item, parent, false);

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        if (list.get(position).latest_msg != null && list.get(position).latest_msg.length() > 0) {
            holder.latestmsg.setText(list.get(position).latest_msg);

        }

        if (list.get(position).town != null && list.get(position).town.length() > 0) {
            holder.msjid_town.setText(list.get(position).town);

        }

        if (list.get(position).masjid_name != null && list.get(position).masjid_name.length() > 0) {
            holder.msjid_name.setText(list.get(position).masjid_name);

        }

        holder.msjid_unfollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!list.get(position).masjid_id.equals(myprf.getString("jammat", "")))
                    msjidUnfollow(list.get(position).masjid_id, position, false);
                else {
                    showDialog(position);
                }

            }
        });

        if (list.get(position).status.equals("1")) {
            holder.msjid_unfollow.setImageResource(R.mipmap.unfollow_new);
        } else {
            holder.msjid_unfollow.setImageResource(R.mipmap.follow);

        }

        holder.lin_parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, MasjidDetail_userside.class);
                i.putExtra("masjidid", list.get(position).masjid_id);
                i.putExtra("masjidstatus", list.get(position).status);
                context.startActivity(i);
            }
        });


        if (position == list.size() - 1) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                    95, context.getResources().getDisplayMetrics()));
            holder.lin_parent.setLayoutParams(params);
        }


        if (list.get(position).profile_picture != null && list.get(position).profile_picture.length() > 0) {


            Glide.with(context).load(list.get(position).profile_picture)
                    .apply(new RequestOptions()
                            .centerCrop()
                            .override(100, 100)
                            .error(R.drawable.user_dummy)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(holder.msjid_image);

        }


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


        text.setText(list.get(position).masjid_name + " is currently set as your default jamaat reminder, are you sure you want to unfollow " + list.get(position).masjid_name + "? ");

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
                msjidUnfollow(list.get(position).masjid_id, position, true);


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


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return list.size();
    }


    private void msjidUnfollow(String masjid_id, final int pos, final boolean isFavorite) {
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

                        if (status.equalsIgnoreCase("1")) {
                            Toast.makeText(context, "Added to favourites", Toast.LENGTH_LONG).show();

                        } else if (status.equalsIgnoreCase("0")) {
                            Toast.makeText(context, "Removed from Favourites", Toast.LENGTH_LONG).show();

                        }
                        // Toast.makeText(context, "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();
                        list.remove(pos);
                        notifyDataSetChanged();


                        for (int i = 525; i < 530; i++) {

                            cancel(i);

                        }

                        if (isFavorite) {
                            myedit.putString("jammat", "0");
                            myedit.commit();
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

    public void cancel(int requestCode) {
        Intent myIntent = new Intent(context, MyReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                requestCode, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();

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
