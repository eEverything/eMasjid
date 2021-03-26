package http.CommenRequest;

import android.app.Activity;

import java.util.Map;

import http.customprogressdialog.CustomProgressDialog;
import http.social_login.ServiceCallback;

/**
 * Created by Ki-Admin on 30/01/2017.
 */

public class RequestClass {

    public static MainTask aboutus_task;
    public static CustomProgressDialog processDailog;
    public static void requesttoserver_doc_images(Activity context, Map<String, String> mapParams, String doc_path, String[] cameraimage, String url, final ServiceCallback callback) {

        processDailog = new CustomProgressDialog(context, "");
        processDailog.show();

        aboutus_task = new MainTask(context, mapParams, doc_path, cameraimage, url, callback, processDailog, false);
        aboutus_task.execute();

    }


    public static void requesttoserver_doc_images(Activity context, Map<String, String> mapParams, String doc_path,  String url, final ServiceCallback callback) {

        processDailog = new CustomProgressDialog(context, "");
        processDailog.show();

        aboutus_task = new MainTask(context, mapParams, doc_path, url, callback, processDailog, false);
        aboutus_task.execute();

    }




    public static void requesttoserver(Activity context, Map<String, String> mapParams, String url, final ServiceCallback callback) {

        processDailog = new CustomProgressDialog(context, "");
        processDailog.show();

        aboutus_task = new MainTask(context, mapParams, url, callback, processDailog, false);
        aboutus_task.execute();

    }


    public static void requesttoserver_noprocees_bar(Activity context, Map<String, String> mapParams, String url, final ServiceCallback callback) {


        aboutus_task = new MainTask(context, mapParams, url, callback, false);
        aboutus_task.execute();

    }


    // for single image , with process bar
    public static void requesttoserver_image(Activity context, Map<String, String> mapParams, String url, String image_path, final ServiceCallback callback) {

        processDailog = new CustomProgressDialog(context, "");
        processDailog.show();

        aboutus_task = new MainTask(context, image_path, mapParams, url, callback, processDailog, false);
        aboutus_task.execute();

    }

    // for single image , with NO process bar
    public static void requesttoserver_image_noprocess_bar(Activity context, Map<String, String> mapParams, String url, String image_path, final ServiceCallback callback) {


        aboutus_task = new MainTask(context, image_path, mapParams, url, callback, false);
        aboutus_task.execute();

    }


    // for single and multiple image uploading , with proceesbar

    public static void requesttoserver_multipleimage(Activity context, Map<String, String> mapParams, String url, String image_path, String[] multipalimage, final ServiceCallback callback) {

        processDailog = new CustomProgressDialog(context, "");
        processDailog.show();

        aboutus_task = new MainTask(context, image_path, multipalimage, mapParams, url, callback, processDailog, false);
        aboutus_task.execute();


    }


    // for single and multiple image uploading , NO proceesbar

    public static void requesttoserver_multipleimage_noprocees_bar(Activity context, Map<String, String> mapParams, String url, String image_path, String[] multipalimage, final ServiceCallback callback) {


        aboutus_task = new MainTask(context, image_path, multipalimage, mapParams, url, callback, false);
        aboutus_task.execute();


    }


    public static void requesttoserverList(Activity context, Map<String, String> mapParams, String url, final ServiceCallback callback) {


        processDailog = new CustomProgressDialog(context, "");
        processDailog.show();


        aboutus_task = new MainTask(context, mapParams, url, callback, processDailog, true);
        aboutus_task.execute();

    }

    // for single image , with NO process bar
    public static void requesttoserverList_no_progress(Activity context, Map<String, String> mapParams, String url, final ServiceCallback callback) {

        aboutus_task = new MainTask(context, mapParams, url, callback, true);
        aboutus_task.execute();

    }


    public static void requesttoserverList_image(Activity context, Map<String, String> mapParams, String url, String image_path, final ServiceCallback callback) {

        processDailog = new CustomProgressDialog(context, "");
        processDailog.show();

        aboutus_task = new MainTask(context, image_path, mapParams, url, callback, processDailog, true);
        aboutus_task.execute();

    }

    public static void requesttoserverList_image_no_progress(Activity context, Map<String, String> mapParams, String url, String image_path, final ServiceCallback callback) {


        aboutus_task = new MainTask(context, image_path, mapParams, url, callback, true);
        aboutus_task.execute();

    }




}
