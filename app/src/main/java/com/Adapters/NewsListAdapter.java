package com.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.chauthai.swipereveallayout.ViewBinderHelper;
import com.emasjid.AdminHome;
import com.emasjid.Document;
import com.emasjid.Fragment.masjid_detail_fragment;
import com.emasjid.ImagesGellery;
import com.emasjid.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import http.CommenRequest.ListBean;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.ViewHolder> {
    private ListBean lists;
    String[] dbparam = {"id", "user_type"};
    Activity context;
    masjid_detail_fragment adminHome;
    private final ViewBinderHelper binderHelper = new ViewBinderHelper();

    public NewsListAdapter(Activity context, ListBean lists, masjid_detail_fragment adminHome) {
        this.context = context;
        this.lists = lists;
        this.adminHome = adminHome;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView image1, image2, image3, user_image;
        public TextView masjid_name, imagecount, masjid_detail, days, doc_name;
        public View layout;
        RelativeLayout r_layout3;
        public LinearLayout gimage_layout, lin_forground, lin_parent, lin_share, lin_alarm, alarm_share, lin_document;
        public FrameLayout lin_menu;
        public SwipeRevealLayout swipeRevealLayout;

        public ViewHolder(View v) {
            super(v);
            layout = v;

            lin_forground = (LinearLayout) v.findViewById(R.id.lin_forground);
            gimage_layout = (LinearLayout) v.findViewById(R.id.gimage_layout);
            alarm_share = (LinearLayout) v.findViewById(R.id.alarm_share);
            lin_document = (LinearLayout) v.findViewById(R.id.lin_document);
            r_layout3 = (RelativeLayout) v.findViewById(R.id.r_layout3);
            doc_name = (TextView) v.findViewById(R.id.doc_name);
            lin_menu = v.findViewById(R.id.lin_menu);
            masjid_name = v.findViewById(R.id.masjid_name);
            user_image = v.findViewById(R.id.user_image);
            image1 = v.findViewById(R.id.image1);
            image2 = v.findViewById(R.id.image2);
            imagecount = v.findViewById(R.id.imagecount);
            image3 = v.findViewById(R.id.image3);
            masjid_detail = v.findViewById(R.id.masjid_detail);
            days = v.findViewById(R.id.days);
            lin_share = v.findViewById(R.id.lin_share);
            lin_alarm = v.findViewById(R.id.lin_alarm);
            swipeRevealLayout = v.findViewById(R.id.swipe_layout);

        }


    }

    public void saveStates(Bundle outState) {
        binderHelper.saveStates(outState);
    }

    public void restoreStates(Bundle inState) {
        binderHelper.restoreStates(inState);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                         int viewType) {
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.masjid_detail_item, parent, false);

        ViewHolder vh = new ViewHolder(v);


        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.masjid_name.setText(lists.selectedresponcefiel.get(0).get("name"));

        holder.masjid_detail.setText(lists.news.get(0).get(position).get("description"));

        holder.alarm_share.setVisibility(View.GONE);


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

        if (lists.news.get(0).get(position).get("document").length() > 0) {
            String fileName = lists.news.get(0).get(position).get("document").substring(lists.news.get(0).get(position).get("document").lastIndexOf('/') + 1);

            holder.doc_name.setText(fileName);
            holder.lin_document.setVisibility(View.VISIBLE);
        } else {
            holder.lin_document.setVisibility(View.GONE);
        }

        holder.lin_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, Document.class);
                i.putExtra("link", lists.news.get(0).get(position).get("document"));
                context.startActivity(i);
            }
        });

//        if (lists.news.get(0).get(position).get("document").length() > 0) {
//
//
//            holder.image1.setImageResource(R.mipmap.doc_image);
//            holder.gimage_layout.setVisibility(View.VISIBLE);
//
//            if (lists.gallery.get(position).size() >= 3) {
//                holder.r_layout3.setVisibility(View.VISIBLE);
//                holder.image2.setVisibility(View.VISIBLE);
//                holder.imagecount.setText("+" + (lists.gallery.get(position).size() - 2));
//                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image2);
//                Picasso.get().load(lists.gallery.get(position).get(1).get("image")).resize(100, 100).into(holder.image3);
//
//            } else if (lists.gallery.get(position).size() == 2) {
//                holder.r_layout3.setVisibility(View.VISIBLE);
//                holder.image2.setVisibility(View.VISIBLE);
//
//                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image2);
//                Picasso.get().load(lists.gallery.get(position).get(1).get("image")).resize(100, 100).into(holder.image3);
//            } else if (lists.gallery.get(position).size() == 1) {
//                holder.image2.setVisibility(View.VISIBLE);
//                Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image2);
//                holder.r_layout3.setVisibility(View.INVISIBLE);
//            } else {
//                holder.r_layout3.setVisibility(View.INVISIBLE);
//                holder.image2.setVisibility(View.INVISIBLE);
//            }
//
//
//        } else {

        if (lists.gallery.get(position).size() >= 3) {

            holder.gimage_layout.setVisibility(View.VISIBLE);
            holder.image1.setVisibility(View.VISIBLE);
            holder.r_layout3.setVisibility(View.VISIBLE);
            holder.image2.setVisibility(View.VISIBLE);
            holder.imagecount.setText("+" + (lists.gallery.get(position).size() - 3));

            Picasso.get().load(lists.gallery.get(position).get(0).get("image")).into(holder.image1);
            Picasso.get().load(lists.gallery.get(position).get(1).get("image")).into(holder.image2);
            Picasso.get().load(lists.gallery.get(position).get(2).get("image")).into(holder.image3);

          /*  Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image1, new  Callback() {
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
            });
            Picasso.get().load(lists.gallery.get(position).get(2).get("image")).resize(100, 100).into(holder.image3, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(R.mipmap.dummy).into(holder.image3);
                }
            });
*/
        } else if (lists.gallery.get(position).size() == 2) {
            holder.gimage_layout.setVisibility(View.VISIBLE);

            Picasso.get().load(lists.gallery.get(position).get(0).get("image")).into(holder.image1);
            Picasso.get().load(lists.gallery.get(position).get(1).get("image")).into(holder.image2);

      /*      Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image1, new Callback() {
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
            holder.image1.setVisibility(View.VISIBLE);
            holder.r_layout3.setVisibility(View.INVISIBLE);
            holder.image2.setVisibility(View.VISIBLE);
        } else if (lists.gallery.get(position).size() == 1) {
            holder.gimage_layout.setVisibility(View.VISIBLE);

            Picasso.get().load(lists.gallery.get(position).get(0).get("image")).into(holder.image1);
          /*  Picasso.get().load(lists.gallery.get(position).get(0).get("image")).resize(100, 100).into(holder.image1, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(Exception e) {
                    Picasso.get().load(R.mipmap.dummy).into(holder.image1);
                }
            });*/
            holder.image1.setVisibility(View.VISIBLE);
            holder.r_layout3.setVisibility(View.INVISIBLE);
            holder.image2.setVisibility(View.INVISIBLE);
        } else {
            holder.gimage_layout.setVisibility(View.GONE);
            Log.e("gsize=", "=" + lists.gallery.get(position).size());
        }

        //  }

        holder.image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (lists.news.get(0).get(position).get("document").length() > 0) {
//
//                    Intent i = new Intent(context, Document.class);
//                    i.putExtra("link", lists.news.get(0).get(position).get("document"));
//                    context.startActivity(i);
//
//                } else {
                int pos = 0;


                Intent i = new Intent(context, ImagesGellery.class);

                ArrayList<String> list = new ArrayList<>();

                for (int j = 0; j < lists.gallery.get(position).size(); j++) {

                    list.add(lists.gallery.get(position).get(j).get("image"));

                }
                i.putStringArrayListExtra("bean", list);
                i.putExtra("position", pos);

                context.startActivity(i);

                //  }

            }
        });


        holder.image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int pos = 1;
                //  if (lists.news.get(0).get(position).get("document").length() > 0)
                //     pos--;

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

//                if (lists.news.get(0).get(position).get("document").length() > 0)
//                    pos--;

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
        holder.lin_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (holder.swipeRevealLayout.isOpened()) {

                    holder.swipeRevealLayout.close(true);

                }

                if (AppConstants.isNetworkAvailable(context)) {

                    Map<String, String> mapParams = new HashMap<String, String>();
                    mapParams.put("post_id", lists.news.get(0).get(position).get("news_id"));
                    mapParams.put("masjid_id", getUserId());

                    RequestClass.requesttoserver(context, mapParams, "delete-post.php", new ServiceCallback() {
                        @Override
                        public void onSuccesss(ResData data) {

                            if (data.commanbeandata.result1.equals("success")) {

                                Toast.makeText(context, "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();
                                /*lists.news.get(0).remove(position);
                                notifyDataSetChanged();
*/
                                adminHome.onAdapterDelete();
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
        });
    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {

        return lists.news.get(0).size();
    }

    public void removeItem(int position) {
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

}
