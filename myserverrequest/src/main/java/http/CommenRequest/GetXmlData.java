package http.CommenRequest;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Ki-Admin on 30/01/2017.
 */

public class GetXmlData {


    public static String[][] getXmlData(Context context, String filename) {

        String[][] dataarr;
        int i = 0;

        try {
            InputStream fIn = context.getApplicationContext().getResources().getAssets()
                    .open(filename, Context.MODE_WORLD_READABLE);

            BufferedReader input = new BufferedReader(new InputStreamReader(fIn));
            String line = "";


            while ((line = input.readLine()) != null) {
                // process the line..

                line = line.replace(" ", "");

                if (line.contains("android:tag")) {

                    String[] ids = line.split("\"");

                    String lable = ids[1].replace("\"", "");

                    //Log.e("lable=", "" + lable);

                    if (lable.equals("label")) {

                    } else {
                        i++;
                    }


                }


            }


        } catch (Exception e) {
            e.getMessage();
        }

        //Log.e("iiiii=", "" + i);


        dataarr = new String[i][2];
        try {
            InputStream fIn = context.getApplicationContext().getResources().getAssets()
                    .open(filename, Context.MODE_WORLD_READABLE);

            BufferedReader input = new BufferedReader(new InputStreamReader(fIn));
            String line = "";

            int j = 0, k = 0;
            while ((line = input.readLine()) != null) {
                // process the line..

                line = line.replace(" ", "");

                if (line.contains("<de.hdodenhof.circleimageview.CircleImageView") ||  line.contains("<com.custom_widgets.CustomButton")|| line.contains("<com.custom_widgets.CustomEditText") || line.contains("<com.custom_widgets.CustomTextView") || line.contains("<com.twotoasters.jazzylistview.JazzyListView") || line.contains("<widgets.MyGridView") || line.contains("<widgets.GridRecyclerView") || line.contains("<android.support.v7.widget.RecyclerView") || line.contains("<Class.TextViewBold") || line.contains("<Class.TextViewLight") || line.contains("<com.google.android.gms.ads.AdView") || line.contains("<com.google.android.gms.maps.MapView") || line.contains("<fragment") || line.contains("<GridView") || line.contains("<AutoCompleteTextView") || line.contains("<Spinner") || line.contains("<RatingBar") || line.contains("<SeekBar") || line.contains("<ImageButton") || line.contains("<ToggleButton") || line.contains("<CheckBox") || line.contains("<Button") || line.contains("<RadioButton") || line.contains("<RadioGroup") || line.contains("<ProgressBar") || line.contains("<EditText") || line.contains("<widgets.ExpandableHeightListView") || line.contains("<ImageView") || line.contains("<ListView") || line.contains("<TextView") || line.contains("<android.support.v4.view.ViewPager")) {

                    if (line.contains("<ListView")) {

                        dataarr[j][k] = "ListView";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.substring(0, 6).equals("<TextV") || line.contains("<com.custom_widgets.CustomTextView") || line.contains("<Class.TextViewBold") || line.contains("<Class.TextViewLight")) {

                        dataarr[j][k] = "TextView";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.contains("<ProgressBar")) {

                        dataarr[j][k] = "ProgressBar";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.contains("<ImageView") || line.contains("<de.hdodenhof.circleimageview.CircleImageView")) {

                        dataarr[j][k] = "ImageView";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.contains("<widgets.ExpandableHeightListView")) {

                        dataarr[j][k] = "ExpandableHeightListView";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.substring(0, 6).equals("<EditT") || line.contains("<com.custom_widgets.CustomEditText")) {

                        dataarr[j][k] = "EditText";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.contains("RadioButton")) {

                        dataarr[j][k] = "RadioButton";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.contains("RadioGroup")) {

                        dataarr[j][k] = "RadioGroup";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.substring(0, 6).equals("<Butto") || line.contains("<com.custom_widgets.CustomButton")) {

                        dataarr[j][k] = "Button";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.substring(0, 6).equals("<Check")) {

                        dataarr[j][k] = "CheckBox";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.substring(0, 6).equals("<Toggl")) {

                        dataarr[j][k] = "ToggleButton";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.substring(0, 7).equals("<ImageB")) {

                        dataarr[j][k] = "ImageButton";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.substring(0, 6).equals("<SeekB")) {

                        dataarr[j][k] = "SeekBar";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.substring(0, 6).equals("<Ratin")) {

                        dataarr[j][k] = "RatingBar";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.substring(0, 6).equals("<Spinn")) {

                        dataarr[j][k] = "Spinner";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.substring(0, 6).equals("<AutoC")) {

                        dataarr[j][k] = "AutoCompleteTextView";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.contains("<android.support.v4.view.ViewPager")) {
                        dataarr[j][k] = "ViewPager";
                        //Log.e("VIEWPAGER", "" + line.substring(line.length() - 9) + "");
                        k++;
                    } else if (line.substring(0, 6).equals("<GridV") || line.contains("<widgets.MyGridView")) {

                        dataarr[j][k] = "GridView";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.contains("<com.google.android.gms.maps.MapView")) {

                        dataarr[j][k] = "MapView";
                        //Log.e("line222=", "" + line);
                        k++;

                    } else if (line.contains("<fragment")) {

                        dataarr[j][k] = "MapView";
                        //Log.e("line222=", "" + line);
                        k++;

                    } else if (line.contains("<com.google.android.gms.ads.AdView")) {

                        dataarr[j][k] = "AdView";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.contains("<android.support.v7.widget.RecyclerView") || line.contains("<widgets.GridRecyclerView")) {

                        dataarr[j][k] = "RecyclerView";
                        //Log.e("line222=", "" + line);
                        k++;
                    } else if (line.contains("<com.twotoasters.jazzylistview.JazzyListView")) {

                        dataarr[j][k] = "JazzyListView";
                        //Log.e("line222=", "" + line);
                        k++;
                    }


                } else if (line.contains("android:tag")) {


                    String[] ids = line.split("\"");
                    if (ids[1].replace("\"", "").equals("label")) {
                        k = 0;
                    } else {
                        dataarr[j][k] = ids[1].replace("\"", "");

                        k = 0;
                        j++;
                    }


                }


            }


        } catch (Exception e) {
            e.getMessage();
        }
        return dataarr;
    }


}
