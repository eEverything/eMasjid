package com.emasjid.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Adapters.fav_adapter;
import com.Class.DBHelper;
import com.Class.MasjidBean;
import com.Class.countryBean;
import com.emasjid.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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

public class editprofile_masjid_fragment extends Fragment {

    RecyclerView fav_rec;
    fav_adapter adapter;
    LinearLayoutManager mLayoutManager;
    String[] requestparam = {"email", "username", "address", "city", "town", "postcode", "phone_number", "about_us", "website"};
    String[] dbparam = {"id"};
    Map<String, String> mapParams = new HashMap<String, String>();
    Map<String, String> mapParams_getdata = new HashMap<String, String>();
    String[] validationdata, alldata;
    Map<String, View> viewlist = new HashMap<String, View>();
    String[][] dataarr;
    String masjid_id = "", Profile_picture = "";
    final int CAMERA_CAPTURE = 5, CROP_PIC = 3, CROP_PIC_intertimage = 4;
    String[] validationparam = {"email", "username", "address", "city", "town", "postcode", "phone_number", "about_us", "website"};

    String country_id = "";

    List<countryBean> countrylist = new ArrayList<>();

    EditText donate_link;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_profile_masjid, null);
        dataarr = GetXmlData.getXmlData(getActivity(), "edit_profile_masjid.xml");


        SetDataClass.findallviewonxml(view, dataarr, viewlist);

        donate_link = view.findViewById(R.id.donate_link);

        DBHelper db1 = DBHelper.getInstance(getActivity());
        db1.open();

        Map<String, String> data = db1.getUser();

        String[] arrays = db1.getUserTablecolomsstringarray();


        for (int i = 0; i < dbparam.length; i++) {
            for (int j = 0; j < data.size(); j++) {

                if (dbparam[i].equals(arrays[j])) {
                    mapParams_getdata.put("id", data.get(arrays[j]));
                    masjid_id = data.get(arrays[j]);

                }
            }
        }


        db1.close();


        GetCountry();


        ((Spinner) viewlist.get("country")).setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                country_id = countrylist.get(position).country_id;

                Log.e("country_id", country_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                country_id = "1";

            }
        });


        ((Button) viewlist.get("update")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String link_text = donate_link.getText().toString();

                alldata = new String[requestparam.length];

                alldata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, requestparam);

                validationdata = new String[validationparam.length];

                validationdata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, validationparam);

                boolean valid = ValidationClass.fromvalidation(getActivity(), dataarr, validationparam, validationdata);


                if (valid) {

                    Map<String, String> mapParams = new HashMap<String, String>();

                    mapParams.put("masjid_id", masjid_id);
                    mapParams.put("country_id", country_id);

                    mapParams.put("lattitude", latitude);
                    mapParams.put("longitude", longitude);
                    mapParams.put("link", link_text);


                    SetDataClass.setRequestparam(mapParams, alldata, requestparam);

                    if (AppConstants.isNetworkAvailable(getActivity())) {

                        RequestClass.requesttoserver_image(getActivity(), mapParams, "edit-profile.php", Profile_picture, new ServiceCallback() {
                            @Override
                            public void onSuccesss(ResData data) {

                                if (data.commanbeandata.result1.equals("success")) {

                                    Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_LONG).show();
                                    //getActivity().finish();

                                } else {

                                    Toast.makeText(getActivity(), data.commanbeandata.message, Toast.LENGTH_LONG).show();


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
        });

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
                final TextView cancle = (TextView) dialog.findViewById(R.id.cancel);

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

                cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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


        return view;

    }


    @Override
    public void onResume() {


        super.onResume();
    }

    private void GetCountry() {

        if (AppConstants.isNetworkAvailable(getActivity())) {

            RequestClass.requesttoserverList(getActivity(), mapParams_getdata, "get_country.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    //  SetDataClass.setalldataonview(getActivity(), viewlist, data.data.selectedresponcefiel.get(0), dataarr, data.data.view_ids);
                    if (data.data.selectedresponcefiel.size() > 0) {


                        for (int i = 0; i < data.data.selectedresponcefiel.size(); i++) {

                            countryBean bean = new countryBean();
                            bean.country_id = data.data.selectedresponcefiel.get(i).get("country_id");
                            bean.country_name = data.data.selectedresponcefiel.get(i).get("country_name");

                            countrylist.add(bean);

                        }

                        CustomAdapter adapter = new CustomAdapter(getActivity(), countrylist);
                        ((Spinner) viewlist.get("country")).setAdapter(adapter);


                    } else {

                    }
                    PrepareData();

                }

                @Override
                public void onError(ResData data) {

                }
            });


        } else {
            Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void PrepareData() {
        if (AppConstants.isNetworkAvailable(getActivity())) {

            RequestClass.requesttoserverList(getActivity(), mapParams_getdata, "get_masjid_profile.php", new ServiceCallback() {
                @Override
                public void onSuccesss(ResData data) {

                    //  SetDataClass.setalldataonview(getActivity(), viewlist, data.data.selectedresponcefiel.get(0), dataarr, data.data.view_ids);
                    if (data.data.selectedresponcefiel.size() > 0) {

                        SetDataClass.setalldataonview(getActivity(), viewlist, data.data.selectedresponcefiel.get(0), dataarr, data.data.view_ids);


                        ((EditText) viewlist.get("username")).setText(data.data.selectedresponcefiel.get(0).get("masjid_name"));

//                        data.data.selectedresponcefiel.get(0).get("donate_link")

                        donate_link.setText(data.data.selectedresponcefiel.get(0).get("link"));


                        Picasso.get().load(data.data.selectedresponcefiel.get(0).get("profile_picture")).into(((ImageView) viewlist.get("profile_picture")), new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                                Picasso.get().load(R.drawable.test).into(((ImageView) viewlist.get("profile_picture")));
                            }
                        });

                        if (data.data.selectedresponcefiel.get(0).get("country_id").length() > 0) {

                            String idd = data.data.selectedresponcefiel.get(0).get("country_id");

                            for (int i = 0; i < countrylist.size(); i++) {
                                if (idd.equals(countrylist.get(i).country_id)) {
                                    ((Spinner) viewlist.get("country")).setSelection(i);

                                    country_id = idd;

                                    break;
                                }
                            }


                        }


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

                        Profile_picture = imgFile.getPath();

                        File image = new File(Profile_picture);

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

                        Profile_picture = imgFile.getPath();

                        File image = new File(Profile_picture);


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

    String latitude, longitude;


    public static Uri fileUri = null;
    static String timeStamp;
    static File mediaFile;
    final int CAMERA_CAPTURE_interst = 6;
    private static final String IMAGE_DIRECTORY_NAME = "TRU";
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
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
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMAGE_intertimage);

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

    public class CustomAdapter extends BaseAdapter {

        private Activity activity;
        private ArrayList data;
        public Resources res;
        List<countryBean> countrylist;
        LayoutInflater inflater;

        public CustomAdapter(Activity context, List<countryBean> countrylist) {
            this.countrylist = countrylist;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return countrylist.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            convertView = inflater.inflate(R.layout.spinneritem, parent, false);

            TextView countryname = (TextView) convertView.findViewById(R.id.countryname);

            countryname.setText(countrylist.get(position).country_name);


            return convertView;
        }

        /*************  CustomAdapter Constructor *****************/

    }

}
