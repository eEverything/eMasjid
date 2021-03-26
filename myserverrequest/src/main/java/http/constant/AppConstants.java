package http.constant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AppConstants {

    public static final String TAG_APP = "Keencaster";
    public static final int INT_STATUS_SUCCESS = 1;
    public static final int INT_STATUS_FAILED_DOWNLOAD = -10;
    public static final int INT_STATUS_FAILED_CLIENT = -11;
    public static final int INT_STATUS_FAILED_TIMEOUT = -13;
    public static final int INT_STATUS_FAILED_IO = -12;
    public static int startValue = 0;
    public static boolean isConnected = false;
    public static boolean iscreateprofile = false;
    public static String user_type = "";
    public static String[] toastfrom;
    public static String startLat = "";
    public static String startLong = "";
    public static String USERID = "";

    public enum PAGINATION {
        First_Load, Previous, Next
    }

    public enum IMAGES {
        ProductImages1, eventImages1, category;
    }

    public static boolean canMakeSmores() {

        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    public static boolean isNetworkAvailable(Context context) {

        boolean isMobile = false, isWifi = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] infoAvailableNetworks = cm.getAllNetworkInfo();

        if (infoAvailableNetworks != null) {
            for (NetworkInfo network : infoAvailableNetworks) {

                if (network.getType() == ConnectivityManager.TYPE_WIFI) {
                    if (network.isConnected() && network.isAvailable())
                        isWifi = true;
                }
                if (network.getType() == ConnectivityManager.TYPE_MOBILE) {
                    if (network.isConnected() && network.isAvailable())
                        isMobile = true;
                }
            }
        }
        return isMobile || isWifi;
    }


    public static Animation startBlicking() {
        Animation fadeIn = new AlphaAnimation(1, 0);
        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
        fadeIn.setDuration(200);
        fadeIn.setRepeatCount(0);
        ScaleAnimation scaleAnimation = new ScaleAnimation(0.4f, 1f, 0.4f, 1f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(300);
        return scaleAnimation;
    }

    public static String encode(String str) {
        String s = "";
        try {
            s = URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return s;
    }


    public static String convertTOtime(long millis) {
        String formatted;
        Date date = new Date(millis);
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        return formatted = formatter.format(date);
    }

    public static String convertSecondsToHMmSs(long millis) {
        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
        return hms;
    }


    public static String timeConverter(String time) {
        String finalTime = null;
        int timeAd = 0;
        String[] timeArray;
        String[] hourArray;
        if (time.contains("PM")) {
            timeArray = time.split(" ");
            hourArray = time.split(":");
            if (hourArray[0].equalsIgnoreCase("12")) {
                timeAd = Integer.parseInt(hourArray[0]);
            } else {
                timeAd = Integer.parseInt(hourArray[0]) + 12;
            }
            finalTime = timeAd + ":" + "00";
        } else {
            timeArray = time.split(" ");
            finalTime = timeArray[0];
        }
        if (time.equalsIgnoreCase("12:00 AM")) {
            finalTime = "00:00";
        }
        return finalTime;
    }

    public static File SaveImage(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }


    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static boolean nullCheck(String text) {

        if (text != null && text.length() > 0) {
            return true;
        } else {
            return false;
        }
    }


    public static boolean isEmailValid(String email) {
        String regExpn = "^[\\w\\.'-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;
        Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches())
            return true;
        else
            return false;
    }

    public static Bitmap getBitmapFromView(View view, Bitmap returnedBitmap) {
//        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }


    public static int getFileSize(String URL) {
        int size = 0;
        try {
            java.net.URL url = new URL(URL);
            URLConnection urlConnection = url.openConnection();
            urlConnection.connect();
            size = urlConnection.getContentLength();
        } catch (final IOException e1) {
        }
        return size;
    }

    public static String getDate()
    {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => "+c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        // formattedDate have current date/time
        return formattedDate;
    }
}
