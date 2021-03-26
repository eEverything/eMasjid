package http.CommenRequest;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.myserverrequest.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ki-Admin on 30/01/2017.
 */

public class SetDataClass {


    public static View setXmlView(Activity context, ViewGroup parent, int layout_id) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(layout_id, parent, false);


        return row;
    }


    public static void setRequestparam(Map<String, String> mapParams, String[] alldata, String[] requestparam) {

        for (int i = 0; i < requestparam.length; i++) {


            if (alldata[i].equals("")) {

            } else {
                mapParams.put(requestparam[i], alldata[i]);
            }


        }


    }


    public static String[] getValueFromXmlFom(ArrayList<View> viewlist, String[][] xmldata, String[] requestparam, String data) {

        String[] alldata = new String[requestparam.length];


        for (int i = 0; i < requestparam.length; i++) {


            for (int j = 0; j < xmldata.length; j++) {

                if (requestparam[i].equals(xmldata[j][1])) {


                    switch (xmldata[j][0]) {

                        case "TextView":

                            alldata[i] = ((TextView) viewlist.get(j)).getText().toString();

                            break;


                        case "EditText":
                            alldata[i] = ((EditText) viewlist.get(j)).getText().toString();

                            break;


                        default:
                            alldata[i] = "";

                            break;


                    }


                } else {

                }


            }


        }


        return alldata;
    }

    public static String[] getValueFromXmlFom(ArrayList<View> viewlist, String[][] xmldata, String[] requestparam) {
        return null;
    }

    public static String[] getValueFromXmlFom(Map<String, View> viewlist, String[][] xmldata, String[] requestparam) {

        String[] alldata = new String[requestparam.length];


        for (int i = 0; i < requestparam.length; i++) {


            for (int j = 0; j < xmldata.length; j++) {

                if (requestparam[i].equals(xmldata[j][1])) {


                    switch (xmldata[j][0]) {

                        case "TextView":

                            alldata[i] = ((TextView) viewlist.get(xmldata[j][1])).getText().toString();

                            break;


                        case "EditText":
                            alldata[i] = ((EditText) viewlist.get(xmldata[j][1])).getText().toString();

                            break;
                        case "ProgressBar":
                            alldata[i] = "" + ((ProgressBar) viewlist.get(xmldata[j][1])).getProgress();

                            break;


                        case "RatingBar":
                            alldata[i] = "" + ((RatingBar) viewlist.get(xmldata[j][1])).getRating();

                            break;

                        case "AutoCompleteTextView":
                            alldata[i] = "" + ((AutoCompleteTextView) viewlist.get(xmldata[j][1])).getText().toString();

                            break;


                        default:
                            alldata[i] = "";

                            break;


                    }


                } else {

                }


            }


        }


        return alldata;
    }


    public static void findallviewonxml(View view, String[][] xmldata, ArrayList<View> viewlist) {

        for (int i = 0; i < xmldata.length; i++) {
            for (int j = 0; j < 1; j++) {

                View v = findviewbytag(view, xmldata[i][j], xmldata[i][j + 1]);

                viewlist.add(v);

            }

        }


    }


    public static void findallviewonxml(View view, String[][] xmldata, Map<String, View> viewlist) {

        for (int i = 0; i < xmldata.length; i++) {
            for (int j = 0; j < 1; j++) {

                View v = findviewbytag(view, xmldata[i][j], xmldata[i][j + 1]);

                viewlist.put(xmldata[i][j + 1], v);

            }

        }


    }


    public static View findviewbytag(View view, String type, String tag) {

        View findview = null;
        switch (type) {

            default:
                findview = view.findViewWithTag(tag);

                return findview;


        }


    }

    public static void setalldataonview(Activity context, Map<String, View> viewlist, String[] listdata, String[][] xmldata) {

//        for (int i = 0; i < xmldata.length; i++) {
//            setDataOnView(context, viewsobj.get(i), listdata[i], xmldata[i][0]);
//        }

    }


    public static void setalldataonview(Activity context, Map<String, View> viewlist, Map<String, String> listdata, String[][] xmldata, String[] view_ids) {


        for (int i = 0; i < xmldata.length; i++) {


            for (int j = 0; j < view_ids.length; j++) {
                if (xmldata[i][1].equals(view_ids[j])) {


                    setDataOnView(context, viewlist.get(xmldata[i][1]), listdata.get(view_ids[j]), xmldata[i][0]);

                } else if (xmldata[i][0].equals("ProgressBar")) {


                    ProgressBar pb = (ProgressBar) viewlist.get(xmldata[i][1]);
                    Log.e("ProgressBar==", "ProgressBar");
                    processbartag = pb;


                }


            }


        }


    }


    public static ProgressBar processbartag;

    private static void setDataOnView(Activity context, View view, String data, String type) {


        switch (type) {


            case "ImageView":
                if (data != null && data.length() > 0 ) {

                    if (data.contains("http://") || data.contains("https://")) {
                        final ImageView image = (ImageView) view;

                        if (processbartag != null) {
                            processbartag.setVisibility(View.VISIBLE);
                        }

                        Picasso.get().load(data).into(image, new Callback() {
                            @Override
                            public void onSuccess() {
                                if (processbartag != null) {
                                    processbartag.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void onError(Exception e) {
                                if (processbartag != null) {

                               /*     image.setImageResource(R.drawable.placeholder_cam);*/
                                    processbartag.setVisibility(View.GONE);
                                }
                            }
                        });


                    } else {
                        ImageView image = (ImageView) view;
                        if (processbartag != null) {
                            processbartag.setVisibility(View.VISIBLE);
                        }

                        Picasso.get().load("http://" + data).into(image, new Callback() {
                            @Override
                            public void onSuccess() {

                                if (processbartag != null) {
                                    processbartag.setVisibility(View.GONE);

                                }
                            }

                            @Override
                            public void onError(Exception e) {

                                if (processbartag != null) {
                                    processbartag.setVisibility(View.GONE);
                                }
                            }
                        });


                    }


                }


                break;

            case "EditText":

                EditText textView = (EditText) view;

                textView.setText(data);

                break;

            case "TextView":

                TextView textView1 = (TextView) view;

                textView1.setText(data);

                break;


            case "Button":

                Button button = (Button) view;

                button.setText(data);

                break;


            case "CheckBox":

                CheckBox ck = (CheckBox) view;

                ck.setSelected(true);

                break;


        }


    }

}
