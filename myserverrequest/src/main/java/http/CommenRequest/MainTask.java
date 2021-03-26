package http.CommenRequest;

import android.app.Activity;

import java.util.Map;

import http.bean.ResponseData;
import http.customprogressdialog.CustomProgressDialog;
import http.factory.SimpleObjectFactory;
import http.social_login.ServiceCallback;
import http.task.BaseTask;


public class MainTask extends BaseTask {
    private ListBean listdata;
    private ResData data;
    private CommenBean logindata;
    private Activity context;
    private String[] multipalimage;
    private Map<String, String> mapParams;
    private boolean is_list;
    private String url;
    private CustomProgressDialog processDailog;
    private String image_path = "", doc_path = "";
    private int is_continue = 1, hasmoredata = 0;
    private ServiceCallback callback;

    public MainTask(Activity context, Map<String, String> mapParams, String doc_path, String[] camerapath, String url, ServiceCallback callback, CustomProgressDialog processDailog, boolean is_list) {
        this.context = context;
        this.mapParams = mapParams;
        this.url = url;
        this.is_list = is_list;
        this.doc_path = doc_path;
        image_path = "";
        multipalimage = camerapath;
        this.callback = callback;
        this.processDailog = processDailog;

    }

    public MainTask(Activity context, Map<String, String> mapParams, String doc_path, String url, ServiceCallback callback, CustomProgressDialog processDailog, boolean is_list) {
        this.context = context;
        this.mapParams = mapParams;
        this.url = url;
        this.is_list = is_list;
        this.doc_path = doc_path;
        image_path = "";
        multipalimage = new String[0];
        this.callback = callback;
        this.processDailog = processDailog;

    }


    public MainTask(Activity context, Map<String, String> mapParams, String url, ServiceCallback callback, CustomProgressDialog processDailog, boolean is_list) {
        this.context = context;
        this.mapParams = mapParams;
        this.url = url;
        multipalimage = new String[0];
        this.is_list = is_list;
        image_path = "";
        this.callback = callback;
        this.processDailog = processDailog;

    }

    public MainTask(Activity context, Map<String, String> mapParams, String url, ServiceCallback callback, boolean is_list) {
        this.context = context;
        this.mapParams = mapParams;
        this.url = url;
        multipalimage = new String[0];
        this.is_list = is_list;
        image_path = "";
        this.callback = callback;
    }


    public MainTask(Activity context, String image_path, Map<String, String> mapParams, String url, ServiceCallback callback, CustomProgressDialog processDailog, boolean is_list) {
        this.context = context;
        this.mapParams = mapParams;
        this.url = url;
        multipalimage = new String[0];
        this.image_path = image_path;
        this.is_list = is_list;
        this.callback = callback;
        this.processDailog = processDailog;

    }


    public MainTask(Activity context, String image_path, String[] multipalimage, Map<String, String> mapParams, String url, ServiceCallback callback, CustomProgressDialog processDailog, boolean is_list) {
        this.context = context;
        this.mapParams = mapParams;
        this.url = url;
        this.multipalimage = multipalimage;
        this.image_path = image_path;
        this.is_list = is_list;
        this.callback = callback;
        this.processDailog = processDailog;
    }

    public MainTask(Activity context, String image_path, String[] multipalimage, Map<String, String> mapParams, String url, ServiceCallback callback, boolean is_list) {
        this.context = context;
        this.mapParams = mapParams;
        this.url = url;
        this.multipalimage = multipalimage;
        this.image_path = image_path;
        this.is_list = is_list;
        this.callback = callback;
    }


    public MainTask(Activity context, String image_path, Map<String, String> mapParams, String url, ServiceCallback callback, boolean is_list) {
        this.context = context;
        this.mapParams = mapParams;
        this.url = url;
        multipalimage = new String[0];
        this.image_path = image_path;
        this.is_list = is_list;
        this.callback = callback;
    }


    @Override
    protected void onPreExecute() {
        is_continue = 1;


    }

    @Override
    protected Void doInBackground(String... params) {
        SimpleObjectFactory factory = SimpleObjectFactory.getInstance();
        if (is_list) {

            listdata = factory.getListData(mapParams, url, image_path);


        } else {
            if (doc_path.length() > 0 && multipalimage.length > 0) {
                logindata = factory.request(mapParams, url, doc_path, multipalimage);

            } else if (multipalimage.length > 0) {
                logindata = factory.request_doc(mapParams, url, multipalimage);

            } else if (doc_path.length() > 0) {
                logindata = factory.request_doc(mapParams, url, doc_path);

            } else {
                logindata = factory.request(mapParams, url, image_path);
            }
        }


        return null;
    }

    @Override
    protected void onPostExecute(Void result) {


        if (is_list) {

            if (listdata.selectedresponcefiel.size() > 0) {
                hasmoredata = 1;
            } else {
                hasmoredata = 0;
            }

            is_continue = 0;


            data = new ResData();

            data.data = listdata;

            int[] pa = new int[2];
            pa[0] = hasmoredata;
            pa[1] = is_continue;


            data.param = pa;

            callback.onSuccesss(data);

            if (processDailog != null) {

                if (!(context.isDestroyed())) {

                    processDailog.dismiss();
                }

            }


        } else {


            data = new ResData();


            data.commanbeandata = logindata;

            callback.onSuccesss(data);
            if (processDailog != null) {
                processDailog.dismiss();
            }


        }


    }


    @Override
    public <T extends ResponseData> T getData(int pos) {
        return null;
    }

    @Override
    public void release() {

    }

}
