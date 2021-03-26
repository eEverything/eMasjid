package com.emasjid;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.VideoView;

import com.custom_widgets.CustomButton;
import com.custom_widgets.CustomTextView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class ViewImages extends AppCompatActivity {
    String[] camera_imagepath;
    String dataDoc;
    RecyclerView media_recView;
    Transaction_adapter media_adapter;
    ArrayList<media_object> hashArray = new ArrayList<>();
    Integer fileType = 0;
    Context context;

    @Override
    public void onBackPressed() {
        ViewImages.this.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images);
        context = getApplicationContext();
        Intent intent = getIntent();
        camera_imagepath = intent.getStringArrayExtra("dataMedia");
        dataDoc = intent.getStringExtra("dataDoc");
        fileType = Integer.parseInt(intent.getStringExtra("fileType"));

        media_recView = findViewById(R.id.media_rec);
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false);
        media_recView.setLayoutManager(horizontalLayoutManager);


        if (fileType == 1) {
            for (int i = 0; i < camera_imagepath.length; i++) {
                media_object temp = new media_object("img", camera_imagepath[i]);
                hashArray.add(temp);
            }
            media_adapter = new Transaction_adapter(hashArray, ViewImages.this);
            media_recView.setAdapter(media_adapter);
            media_recView.setItemAnimator(new DefaultItemAnimator());

        } else if (fileType == 2) {

            File ff = new File(dataDoc);
            String extension = ff.getAbsolutePath().substring(ff.getAbsolutePath().lastIndexOf("."));
            media_object temp = new media_object("doc", dataDoc);
            hashArray.add(temp);
            media_adapter = new Transaction_adapter(hashArray, ViewImages.this);
            media_recView.setAdapter(media_adapter);
            media_recView.setItemAnimator(new DefaultItemAnimator());


        } else if (fileType == 3) {
            for (int i = 0; i < camera_imagepath.length; i++) {
                media_object temp = new media_object("img", camera_imagepath[i]);
                hashArray.add(temp);
            }
            if (dataDoc != null) {
                String filename = dataDoc.substring(dataDoc.lastIndexOf("/") + 1);

                File ff = new File(dataDoc);
//                String extension = ff.getAbsolutePath().substring(ff.getAbsolutePath().lastIndexOf("."));
                media_object temp = new media_object("doc", dataDoc);
                hashArray.add(temp);
                media_adapter = new Transaction_adapter(hashArray, ViewImages.this);
                media_recView.setAdapter(media_adapter);
                media_recView.setItemAnimator(new DefaultItemAnimator());

            }
        }

    }

    class Transaction_adapter extends RecyclerView.Adapter<Transaction_adapter.ViewHolder> {
        ArrayList<media_object> camera_imageArray;
        Context context;

        Transaction_adapter(ArrayList<media_object> camera_imageArray, Context context) {
            this.camera_imageArray = camera_imageArray;
            this.context = context;
        }

        @Override
        public Transaction_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.view_media_item, parent, false);

            Transaction_adapter.ViewHolder viewHolder = new Transaction_adapter.ViewHolder(listItem);

            return viewHolder;
        }


        @Override
        public void onBindViewHolder(Transaction_adapter.ViewHolder holder, final int position) {

            if (camera_imageArray.get(position).getType().equals("img")) {
                if (camera_imageArray.get(position).getData() != null && camera_imageArray.get(position).getData().length() > 0) {
                    File imgFile = new File(camera_imageArray.get(position).getData());
                    Log.e("mdlmdl", "             " + imgFile);
                    if (imgFile.exists()) {

                        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                        holder.media_img.setImageBitmap(myBitmap);

//                holder.onPreviewImage.setVisibility(View.GONE);
                    }
                } else if (camera_imageArray.get(position).getType().equals("doc")) {
                    String filename = camera_imageArray.get(position).getData().substring(camera_imageArray.get(position).getData().lastIndexOf("/") + 1);

                    holder.doc_text.setText(filename);

                    holder.doc_text.setVisibility(View.VISIBLE);
                    holder.media_img.setVisibility(View.GONE);
                }

                holder.onPreviewImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File imgFile = new File(camera_imageArray.get(position).getData());
                        Uri uri = Uri.parse(camera_imageArray.get(position).getData());
                        Log.e("testing_viewImages", "      " + imgFile + "             " + imgFile.getPath() + "        " + camera_imageArray.get(position).getData());
                        openFile(imgFile.getPath());
//                    openFile(imgFile.getPath());
                    }
                });

            }
        }

        private void openFile(String url) {
            //private void openFile(File url) {

            try {

                Log.e("testing_viewImages", "      " + url);
//            Uri uri = Uri.fromFile(url);
                Uri uri = Uri.parse(url);
                Log.e("testing_viewImages", "      " + uri);

                Intent intent = new Intent(Intent.ACTION_VIEW);


                if (url.contains(".doc") || url.contains(".docx")) {
                    // Word document
                    intent.setDataAndType(Uri.parse("file:///" + url), "application/msword");
                } else if (url.contains(".pdf")) {
                    // PDF file
                    intent.setDataAndType(uri, "application/pdf");
//                    intent.setDataAndType(Uri.parse("file:///" + url), "application/pdf");
                } else if (url.contains(".ppt") || url.contains(".pptx")) {
                    // Powerpoint file
                    intent.setDataAndType(Uri.parse("file:///" + url), "application/vnd.ms-powerpoint");
                } else if (url.contains(".xls") || url.contains(".xlsx")) {
                    // Excel file
                    intent.setDataAndType(Uri.parse("file:///" + url), "application/vnd.ms-excel");
                } else if (url.contains(".zip")) {
                    // ZIP file
                    intent.setDataAndType(Uri.parse("file:///" + url), "application/zip");
                } else if (url.contains(".rar")) {
                    // RAR file
                    intent.setDataAndType(Uri.parse("file:///" + url), "application/x-rar-compressed");
                } else if (url.contains(".rtf")) {
                    // RTF file
                    intent.setDataAndType(uri, "application/rtf");
                } else if (url.contains(".wav") || url.contains(".mp3")) {
                    // WAV audio file
                    intent.setDataAndType(uri, "audio/x-wav");
                } else if (url.contains(".gif")) {
                    // GIF file
                    intent.setDataAndType(uri, "image/gif");
                } else if (url.contains(".jpg") || url.contains(".jpeg") || url.contains(".png")) {
                    // JPG file
                    intent.setDataAndType(uri, "image/jpeg");
                } else if (url.contains(".txt")) {
                    // Text file
                    intent.setDataAndType(uri, "text/plain");
                } else if (url.contains(".3gp") || url.contains(".mpg") ||
                        url.contains(".mpeg") || url.contains(".mpe") || url.contains(".mp4") || url.contains(".avi")) {
                    // Video files
                    intent.setDataAndType(uri, "video/*");
                } else {
                    intent.setDataAndType(uri, "*/*");
                }

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(context, "No application found which can open the file", Toast.LENGTH_SHORT).show();
            }
        }

        private String fileExt(String url) {
            if (url.indexOf("?") > -1) {
                url = url.substring(0, url.indexOf("?"));
            }
            if (url.lastIndexOf(".") == -1) {
                return null;
            } else {
                String ext = url.substring(url.lastIndexOf(".") + 1);
                if (ext.indexOf("%") > -1) {
                    ext = ext.substring(0, ext.indexOf("%"));
                }
                if (ext.indexOf("/") > -1) {
                    ext = ext.substring(0, ext.indexOf("/"));
                }
                return ext.toLowerCase();

            }
        }

        @Override
        public int getItemCount() {
            return camera_imageArray.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView media_img;
            CustomTextView doc_text;
            CustomButton onPreviewImage;

            ViewHolder(View itemView) {
                super(itemView);

                onPreviewImage = itemView.findViewById(R.id.onPreviewImage);
                doc_text = itemView.findViewById(R.id.doc_text);
                media_img = itemView.findViewById(R.id.media_img);


            }
        }
    }
}
