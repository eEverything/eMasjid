package com.emasjid.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.emasjid.R;

import java.util.HashMap;
import java.util.Map;

import http.CommenRequest.GetXmlData;
import http.CommenRequest.RequestClass;
import http.CommenRequest.ResData;
import http.CommenRequest.SetDataClass;
import http.CommenRequest.ValidationClass;
import http.constant.AppConstants;
import http.social_login.ServiceCallback;

public class contactus_fragment extends Fragment {

    String[] validationdata, alldata;
    String[][] dataarr;
    String[] requestparam = {"name", "email", "message"};
    String[] validationparam = {"name", "email", "message"};
    Map<String, View> viewlist = new HashMap<String, View>();

    RelativeLayout llThankYou;
    LinearLayout llContactUs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_contact_us, null);

        dataarr = GetXmlData.getXmlData(getActivity(), "activity_contact_us.xml");
        SetDataClass.findallviewonxml(view, dataarr, viewlist);

        llThankYou = view.findViewById(R.id.llThankYou);
        llContactUs = view.findViewById(R.id.llContactUs);

        llThankYou.setVisibility(View.GONE);
        llContactUs.setVisibility(View.VISIBLE);

        ((Button) viewlist.get("send")).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alldata = new String[requestparam.length];

                alldata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, requestparam);

                validationdata = new String[validationparam.length];

                validationdata = SetDataClass.getValueFromXmlFom(viewlist, dataarr, validationparam);

                boolean valid = ValidationClass.fromvalidation(getActivity(), dataarr, validationparam, validationdata);

                if (AppConstants.isNetworkAvailable(getActivity())) {

                    if (valid) {

                        Map<String, String> mapParams = new HashMap<String, String>();

                        SetDataClass.setRequestparam(mapParams, alldata, requestparam);

                        RequestClass.requesttoserver(getActivity(), mapParams, "contact-us.php", new ServiceCallback() {
                            @Override
                            public void onSuccesss(ResData data) {

                                if (data.commanbeandata.result1.equals("success")) {

//                                    Toast.makeText(getActivity(), "Request sent successfully", Toast.LENGTH_LONG).show();
                                    Toast.makeText(getActivity(), "Message Sent", Toast.LENGTH_LONG).show();
                                    llThankYou.setVisibility(View.VISIBLE);
                                    llContactUs.setVisibility(View.GONE);

                                } else {
                                    Toast.makeText(getActivity()
                                            , "" + data.commanbeandata.message, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onError(ResData data) {

                            }
                        });
                    } else {

                    }
                } else {
                    Toast.makeText(getActivity(), "Please check internet connection", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;

    }
}
