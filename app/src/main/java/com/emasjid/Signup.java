package com.emasjid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.Class.DBHelper;
import com.custom_widgets.CustomEditText;

import java.util.HashMap;
import java.util.Map;

import http.CommenRequest.GetXmlData;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.CommenRequest.SetDataClass;
import http.CommenRequest.ValidationClass;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

public class Signup extends Activity  {

    Button login_button;
    String[] validationdata, alldata;
    String[][] dataarr;

    String[] requestparam = {"username", "email", "password", "usertype"};

    String[] validationparam = {"username", "email", "password", "confirmpassword"};

    Map<String, View> viewlist = new HashMap<String, View>();
    String latitude, longitude;
    String usertype = "";

    ImageView back,img11;

    CustomEditText pwd_edit_txt;

    Boolean flagEdit=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);





        dataarr = GetXmlData.getXmlData(Signup.this, "signup.xml");
        SetDataClass.findallviewonxml(getWindow().getDecorView().getRootView(), dataarr, viewlist);

        login_button = (Button) findViewById(R.id.login_button);
        img11 = (ImageView) findViewById(R.id.img11);
        pwd_edit_txt = (CustomEditText) findViewById(R.id.pwd_edit_txt);

        img11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(flagEdit){
                    pwd_edit_txt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    flagEdit=false;
                    img11.setImageResource(R.drawable.ic_eye_open);
                }else {
                    pwd_edit_txt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    flagEdit=true;
                    img11.setImageResource(R.drawable.ic_eye_close);
                }

            }
        });

        back = (ImageView) findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ((RadioGroup) viewlist.get("user_type")).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.msjid) {
                    usertype = "masjid";
                    ((EditText) viewlist.get("username")).setVisibility(View.VISIBLE);
                    ((EditText) viewlist.get("confirmemail")).setVisibility(View.VISIBLE);
                } else {
                    usertype = "user";

                    ((EditText) viewlist.get("username")).setVisibility(View.GONE);
                    ((EditText) viewlist.get("confirmemail")).setVisibility(View.GONE);


                }
            }
        });

        ((TextView) viewlist.get("skip")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signup.this, UserHome.class);
                startActivity(i);
                finishAffinity();
            }
        });


        ((Button) viewlist.get("register")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alldata = new String[requestparam.length];

                alldata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, requestparam);


                validationdata = new String[validationparam.length];

                validationdata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, validationparam);


                //  boolean valid = ValidationClass.fromvalidation(Signup.this, dataarr, validationparam, validationdata);


                String username = ((EditText) viewlist.get("username")).getText().toString();
                String email = ((EditText) viewlist.get("email")).getText().toString();
                String confirmemail = ((EditText) viewlist.get("confirmemail")).getText().toString();
                String password = ((EditText) viewlist.get("password")).getText().toString();


                if (usertype.equals("user")) {


                    //  if (username != null && username.length() > 0) {
                    if (email != null && email.length() > 0) {
                        // if (confirmemail != null && confirmemail.length() > 0) {
                        //   if (email.equals(confirmemail)) {
                        if (password != null && password.length() > 0) {

                            if (usertype != null && usertype.length() > 0) {
                                Map<String, String> mapParams = new HashMap<String, String>();
                                mapParams.put("user_type", usertype);
                                mapParams.put("email", email);
                                mapParams.put("password", password);
                                //   mapParams.put("username", username);
                                //  mapParams.put("device_id", Settings.Secure.getString(Signup.this.getContentResolver(), Settings.Secure.ANDROID_ID));

                                //   SetDataClass.setRequestparam(mapParams, alldata, requestparam);


                                if (AppConstants.isNetworkAvailable(Signup.this)) {


                                    RequestClass.requesttoserver(Signup.this, mapParams, "sign_up.php", new ServiceCallback() {
                                        @Override
                                        public void onSuccesss(ResData data) {

                                            if (data.commanbeandata.result1.equals("success")) {

                                                Toast.makeText(getApplicationContext(), "You are Signed up Successfully", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(Signup.this, Login.class);
                                                // intent.putExtra("user_id", data.data.selectedresponcefiel.get(0).get("user_id"));
                                                startActivity(intent);

                                            } else {
                                                Toast.makeText(getApplicationContext(), "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();

                                            }
                                        }

                                        @Override
                                        public void onError(ResData data) {

                                        }
                                    });
                                } else {
                                    Toast.makeText(Signup.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                Toast.makeText(Signup.this, "Please select user type", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Signup.this, "Please enter password", Toast.LENGTH_SHORT).show();
                        }
//                                } else {
//                                    Toast.makeText(Signup.this, "Both email are different", Toast.LENGTH_SHORT).show();
//                                }
//                            } else {
//                                Toast.makeText(Signup.this, "Please confirm email", Toast.LENGTH_SHORT).show();
//                            }
                    } else {
                        Toast.makeText(Signup.this, "Please enter email", Toast.LENGTH_SHORT).show();
                    }
//                    } else {
//                        Toast.makeText(Signup.this, "Please enter username", Toast.LENGTH_SHORT).show();
//                    }


                } else {
                    if (username != null && username.length() > 0) {
                        if (email != null && email.length() > 0) {
                            if (confirmemail != null && confirmemail.length() > 0) {
                                if (email.equals(confirmemail)) {
                                    if (password != null && password.length() > 0) {

                                        if (usertype != null && usertype.length() > 0) {
                                            Map<String, String> mapParams = new HashMap<String, String>();
                                            mapParams.put("user_type", usertype);
                                            mapParams.put("email", email);
                                            mapParams.put("password", password);
                                            mapParams.put("username", username);
                                            mapParams.put("longitude", "" + longitude);
                                            mapParams.put("lattitude", "" + latitude);

                                            //  mapParams.put("device_id", Settings.Secure.getString(Signup.this.getContentResolver(), Settings.Secure.ANDROID_ID));

                                            //   SetDataClass.setRequestparam(mapParams, alldata, requestparam);


                                            if (AppConstants.isNetworkAvailable(Signup.this)) {


                                                RequestClass.requesttoserver(Signup.this, mapParams, "sign_up.php", new ServiceCallback() {
                                                    @Override
                                                    public void onSuccesss(ResData data) {

                                                        if (data.commanbeandata.result1.equals("success")) {

                                                            Toast.makeText(getApplicationContext(), "You are Signed up Successfully", Toast.LENGTH_LONG).show();
                                                            Intent intent = new Intent(Signup.this, Login.class);
                                                            // intent.putExtra("user_id", data.data.selectedresponcefiel.get(0).get("user_id"));
                                                            startActivity(intent);

                                                        } else {
                                                            Toast.makeText(getApplicationContext(), "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();

                                                        }
                                                    }

                                                    @Override
                                                    public void onError(ResData data) {

                                                    }
                                                });
                                            } else {
                                                Toast.makeText(Signup.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            Toast.makeText(Signup.this, "Please select user type", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(Signup.this, "Please enter password", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(Signup.this, "Both email are different", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Signup.this, "Please confirm email", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Signup.this, "Please enter email", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(Signup.this, "Please enter username", Toast.LENGTH_SHORT).show();
                    }

                }


            }
        });

    }



    private void imagepicker() {

        final Dialog dialog2 = new Dialog(Signup.this);
        dialog2.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog2.setContentView(R.layout.camera_dialog);
        dialog2.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.parseColor("#33000000")));
        dialog2.setCanceledOnTouchOutside(true);
        dialog2.setCancelable(true);

        TextView gallery = (TextView) dialog2.findViewById(R.id.gallery);
        TextView camera = (TextView) dialog2.findViewById(R.id.camera);


        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent photoPickerIntent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent,
                        3);
                dialog2.dismiss();

            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean result = checkPermission(Signup.this);
                if (result) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, 0);
                } else {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                    Toast.makeText(Signup.this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                }
                dialog2.dismiss();
            }
        });


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog2.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.gravity = Gravity.CENTER;


        dialog2.show();
        dialog2.getWindow().setAttributes(lp);

    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static boolean checkPermission(final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
