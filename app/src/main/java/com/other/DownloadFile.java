package com.other;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import com.emasjid.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadFile {
    private void scanFile(String path,Context context) {

        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    public void onScanCompleted(String path, Uri uri) {
                        Log.d("Tag", "Scan finished. You can view the image in the gallery now.");
                    }
                });
    }

    private static final String TAG = "DownloadFile";
    public DownloadFile(Context context, String url, String id, String fromWhere) {
        if(url==null)
            url="";
        if(id==null)
            id="";

        Log.e("testdcjbhdsjb", "==>>"+id+"==>"+fromWhere+"==>"+FileCounter.counter+"    ==>"+url);

        Log.e("File URL: ", url);

        Uri uri= Uri.parse(url);

        // Create request for android download manager
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        //Extract file name from URL
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        String fileExtension = url.substring(url.lastIndexOf("."));
        String timestamp = new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
        //Append timestamp to file name
        fileName = "emasjid_"+id+"_"+FileCounter.counter + "" + fileExtension;

        // set title and description
        request.setTitle(fileName);
        request.setDescription("Downloading...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //set the local destination for download file to a path within the application's external files directory
        //String filePath =  File.separator + context.getString(R.string.app_name) + File.separator + fileName;
        //File directory = new File(Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.app_name));

        String filePath =  File.separator + "Download" + File.separator + fileName;
        scanFile(filePath,context);
        Log.d(TAG, "DownloadFile: jnr3jne3>>"+filePath);
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Download");
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Log.e("File Dir: ", "Directory created.");
            }
        } else {
            Log.e("File Dir: ", "Already Exists.");
        }

        Log.e("File Path: ", context.getExternalFilesDir(null).getAbsolutePath() + filePath);
        Log.e("File Name: ", fileName);

        File destinationDirectory = new File(Environment.getExternalStorageDirectory() + filePath);
        request.setDestinationUri(Uri.fromFile(destinationDirectory));
        //  request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setMimeType("*/*");
        downloadManager.enqueue(request);

        FileCounter.counter++;






        /*Log.e("File URL: ", url);

        Uri uri= Uri.parse(url);

        // Create request for android download manager
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);

        //Extract file name from URL
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        String fileExtension = url.substring(url.lastIndexOf("."));
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //Append timestamp to file name
        if(FileCounter.counter!=null){
            fileName = timestamp + "file"+FileCounter.counter + ".jpg";
            FileCounter.counter++;
        }else {
            FileCounter.counter=50;
            fileName = timestamp + "file"+FileCounter.counter + ".jpg";
            FileCounter.counter++;
        }



        // set title and description
        request.setTitle(fileName);
        request.setDescription("Downloading...");

        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        //set the local destination for download file to a path within the application's external files directory
        String filePath =  File.separator + context.getString(R.string.app_name) + File.separator + fileName;
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + context.getString(R.string.app_name));
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Log.e("File Dir: ", "Directory created.");
            }
        } else {
            Log.e("File Dir: ", "Already Exists.");
        }

        Log.e("File Path: ", context.getExternalFilesDir(null).getAbsolutePath() + filePath);
        Log.e("File Name: ", fileName);

        File destinationDirectory = new File(Environment.getExternalStorageDirectory() + filePath);
        request.setDestinationUri(Uri.fromFile(destinationDirectory));*/

        //  request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);


        //open below from comment 17-09-2020
        //request.setMimeType("*/*");
        //downloadManager.enqueue(request);
    }
}