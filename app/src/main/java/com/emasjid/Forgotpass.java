package com.emasjid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.FieldPosition;
import java.util.HashMap;
import java.util.Map;

import http.CommenRequest.GetXmlData;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.CommenRequest.SetDataClass;
import http.CommenRequest.ValidationClass;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

public class Forgotpass extends Activity {

    Button forgot_pass;
    ImageView back;



    String[] validationdata, alldata;
    String[][] dataarr;
    String[] requestparam = {"email"};
    String[] validationparam = {"email"};
    Map<String, View> viewlist = new HashMap<String, View>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        forgot_pass = (Button) findViewById(R.id.forgot_pass);
        back = (ImageView) findViewById(R.id.back);



        dataarr = GetXmlData.getXmlData(Forgotpass.this, "activity_forgot_password.xml");
        SetDataClass.findallviewonxml(getWindow().getDecorView().getRootView(), dataarr, viewlist);


        ((Button) viewlist.get("send")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                alldata = new String[requestparam.length];

                alldata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, requestparam);


                validationdata = new String[validationparam.length];

                validationdata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, validationparam);


                boolean valid = ValidationClass.fromvalidation(Forgotpass.this, dataarr, validationparam, validationdata);


                if(valid)
                {
                    if (AppConstants.isNetworkAvailable(Forgotpass.this)) {

                        Map<String, String> mapParams = new HashMap<String, String>();
                        mapParams.put("email", ((EditText) viewlist.get("email")).getText().toString());

                        RequestClass.requesttoserver(Forgotpass.this, mapParams, "forgetpassword.php", new ServiceCallback() {
                            @Override
                            public void onSuccesss(ResData data) {

                                if (data.commanbeandata.result1.equals("success")) {

                                    Toast.makeText(getApplicationContext(), "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(Forgotpass.this, Login.class);
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
                        Toast.makeText(Forgotpass.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {

                }




            }
        });

        ((ImageView) viewlist.get("back")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });
    }
}
