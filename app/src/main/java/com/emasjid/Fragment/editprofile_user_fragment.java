package com.emasjid.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.fav_adapter;
import com.Class.DBHelper;
import com.emasjid.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import http.CommenRequest.GetXmlData;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.CommenRequest.SetDataClass;
import http.CommenRequest.ValidationClass;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

import static com.emasjid.Signup.checkPermission;

public class editprofile_user_fragment extends Fragment {

    RecyclerView fav_rec;
    fav_adapter adapter;
    LinearLayoutManager mLayoutManager;

    String[] validationdata, alldata;
    String[][] dataarr;
    String[] dbparam = {"id"};
    Map<String, String> mapParams_getdata = new HashMap<String, String>();

    String[] requestparam = {};
    String[] validationparam = {"firstname", "lastname", "email", "old_password", "password", "confirmpassword"};
    Map<String, View> viewlist = new HashMap<String, View>();

    final int CAMERA_CAPTURE = 5, CROP_PIC = 3, CROP_PIC_intertimage = 4;
    private Activity activity;
    String image_path = "", userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        activity = getActivity();

        View view = inflater.inflate(R.layout.edit_profile_user, null);

        dataarr = GetXmlData.getXmlData(getActivity(), "edit_profile_user.xml");
        SetDataClass.findallviewonxml(view, dataarr, viewlist);

        DBHelper db1 = DBHelper.getInstance(getActivity());
        db1.open();

        Map<String, String> data = db1.getUser();

        String[] arrays = db1.getUserTablecolomsstringarray();


        for (int i = 0; i < dbparam.length; i++) {
            for (int j = 0; j < data.size(); j++) {
                if (dbparam[i].equals(arrays[j])) {
                    mapParams_getdata.put("user_id", data.get(arrays[j]));

                    userid = data.get(arrays[j]);
                }
            }

        }


        db1.close();


        getProfile();


        ((Button) viewlist.get("update")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateProfile();
            }
        });


        ((ImageView) viewlist.get("profile_picture")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePick();
            }
        });

        return view;

    }

    private void updateProfile() {


        alldata = new String[requestparam.length];

        alldata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, requestparam);

        validationdata = new String[validationparam.length];

        validationdata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, validationparam);


        boolean valid = ValidationClass.fromvalidation(getActivity(), dataarr, validationparam, validationdata);


        if (valid) {

            String firstname = ((TextView) viewlist.get("first_name")).getText().toString();
            String lastname = ((TextView) viewlist.get("last_name")).getText().toString();
            String email = ((TextView) viewlist.get("email")).getText().toString();
            String password = ((TextView) viewlist.get("password")).getText().toString();
            //String firstname = ((TextView) viewlist.get("first_name")).getText().toString();

            Map<String, String> mapParams = new HashMap<String, String>();
            mapParams.put("user_id", userid);
            mapParams.put("username", firstname);
            mapParams.put("lastname", lastname);
            mapParams.put("email", email);
            mapParams.put("password", password);

            SetDataClass.setRequestparam(mapParams, alldata, requestparam);

            if (AppConstants.isNetworkAvailable(getActivity())) {


                RequestClass.requesttoserver_image(getActivity(), mapParams, "update-user-profile.php", image_path, new ServiceCallback() {
                    @Override
                    public void onSuccesss(ResData data) {

                        if (data.commanbeandata.result1.equals("success")) {

                            Toast.makeText(getActivity(), "User profile updates successfully", Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();

                        }


                    }

                    @Override
                    public void onError(ResData data) {

                    }
                });


            } else {
                Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
            }


        } else {

        }


    }


    private void getProfile() {

        if (AppConstants.isNetworkAvailable(getActivity())) {


            RequestClass.requesttoserverList(getActivity(), mapParams_getdata, "get_user_profile.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    //  SetDataClass.setalldataonview(getActivity(), viewlist, data.data.selectedresponcefiel.get(0), dataarr, data.data.view_ids);

                    if (data.data.selectedresponcefiel.size() > 0) {

                        // SetDataClass.setalldataonview(getActivity(), viewlist, data.data.selectedresponcefiel.get(0), dataarr, data.data.view_ids);

                        ((TextView) viewlist.get("first_name")).setText(data.data.selectedresponcefiel.get(0).get("username"));
                        ((TextView) viewlist.get("old_password")).setText(data.data.selectedresponcefiel.get(0).get("password"));
                        ((TextView) viewlist.get("email")).setText(data.data.selectedresponcefiel.get(0).get("email"));


                        if (data.data.selectedresponcefiel.get(0).get("lastname") != null && data.data.selectedresponcefiel.get(0).get("lastname").length() > 0) {
                            ((TextView) viewlist.get("last_name")).setText(data.data.selectedresponcefiel.get(0).get("lastname"));
                        }
                        Log.e("image_path_online", data.data.selectedresponcefiel.get(0).get("profile_picture"));
                        Picasso.get().load(data.data.selectedresponcefiel.get(0).get("profile_picture")).resize(100, 100).into(((ImageView) viewlist.get("profile_picture")), new Callback() {
                            @Override
                            public void onSuccess() {
                            }

                            @Override
                            public void onError(Exception e) {
                                Picasso.get().load(R.drawable.user_dummy).into(((ImageView) viewlist.get("profile_picture")));

//                                Toast.makeText(activity, "Failure", Toast.LENGTH_SHORT).show();

                            }
                        });

                    } else {

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


    private void ImagePick() {
        ((ImageView) viewlist.get("profile_picture")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.camera_dialog);
                dialog.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color
                                .parseColor("#33000000")));
                dialog.setCanceledOnTouchOutside(true);
                dialog.setCancelable(true);


                final TextView galary = (TextView) dialog.findViewById(R.id.gallery);
                final TextView camera = (TextView) dialog.findViewById(R.id.camera);

                galary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean result = checkPermission(getActivity());
                        if (result) {
                            galleryIntentinterstimage();
                        }
                        dialog.dismiss();
                    }
                });

                camera.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        boolean result = checkPermission(getActivity());
                        if (result) {
                            try {
                                Intent intent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                                intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                                startActivityForResult(intent, CAMERA_CAPTURE_interst);
                                Log.e("media file 12312313131", mediaFile + "");

                            } catch (ActivityNotFoundException anfe) {


                            }
                        }


                        dialog.dismiss();

                    }
                });


                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(dialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.MATCH_PARENT;
                lp.gravity = Gravity.CENTER;


                dialog.show();
                dialog.getWindow().setAttributes(lp);


            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMAGE_intertimage) {

                try {
                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    cropIntent.setDataAndType(data.getData(), "image/*");
                    cropIntent.putExtra("crop", "true");
                    cropIntent.putExtra("aspectX", 0);
                    cropIntent.putExtra("aspectY", 0);
                    cropIntent.putExtra("scale", true);
                    cropIntent.putExtra("return-data", false);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    startActivityForResult(cropIntent, CROP_PIC_intertimage);

                } catch (ActivityNotFoundException anfe) {

                }

            } else if (requestCode == CAMERA_CAPTURE) {


                performCrop();


            } else if (requestCode == CAMERA_CAPTURE_interst) {


                try {
                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    cropIntent.setDataAndType(fileUri, "image/*");
                    cropIntent.putExtra("crop", "true");
                    cropIntent.putExtra("aspectX", 0);
                    cropIntent.putExtra("aspectY", 0);
                    cropIntent.putExtra("scale", true);
                    cropIntent.putExtra("return-data", false);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

                    startActivityForResult(cropIntent, CROP_PIC_intertimage);
                } catch (ActivityNotFoundException anfe) {

                }


            } else if (requestCode == CROP_PIC) {
                System.out.println("Step>>7");
                File finalFile = null;

                if (mediaFile != null && mediaFile.length() > 0) {
                    File imgFile = new File(mediaFile.getAbsolutePath());
                    Log.e("LOG", "IMAGE FILE=> " + imgFile);
                    if (imgFile.getPath() != null) {
                        System.out.println("Step>>8");

                        image_path = imgFile.getPath();

                        File image = new File(image_path);

                        Picasso.get().load(image).into(((ImageView) viewlist.get("profile_picture")));


                    } else {
                        System.out.println("Step>>9");


                    }
                }

            } else if (requestCode == CROP_PIC_intertimage) {
                System.out.println("Step>>7");
                File finalFile = null;

                if (mediaFile != null && mediaFile.length() > 0) {
                    File imgFile = new File(mediaFile.getAbsolutePath());
                    Log.e("LOG", "IMAGE FILE=> " + imgFile);
                    if (imgFile.getPath() != null) {
                        System.out.println("Step>>8");

                        image_path = imgFile.getPath();

                        Log.e("imagepath", image_path);

                        File image = new File(image_path);

                        Picasso.get().load(image).into(((ImageView) viewlist.get("profile_picture")));

                    } else {
                        System.out.println("Step>>9");
                    }
                }
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    public static Uri fileUri = null;
    static String timeStamp;
    static File mediaFile;
    final int CAMERA_CAPTURE_interst = 6;
    private static final String IMAGE_DIRECTORY_NAME = "TRU";
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed to create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        timeStamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss",
                Locale.getDefault()).format(new Date());

        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    int RESULT_LOAD_IMAGE_intertimage = 2;

    private void galleryIntentinterstimage() {
        Intent photoPickerIntent = new Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");

        startActivityForResult(photoPickerIntent,
                RESULT_LOAD_IMAGE_intertimage);


    }

    private void performCrop() {
        try {
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            cropIntent.setDataAndType(fileUri, "image/*");
            cropIntent.putExtra("crop", "true");
            cropIntent.putExtra("aspectX", 0);
            cropIntent.putExtra("aspectY", 0);
            cropIntent.putExtra("scale", true);
            cropIntent.putExtra("return-data", false);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

            cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            startActivityForResult(cropIntent, CROP_PIC);
        } catch (ActivityNotFoundException anfe) {

        }
    }

}
