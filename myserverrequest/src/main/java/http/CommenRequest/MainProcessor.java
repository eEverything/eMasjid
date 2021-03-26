package http.CommenRequest;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import http.HttpObject;
import http.HttpOperation;
import http.HttpProcessor;
import http.Response;
import http.constant.MessageConstants;


public class MainProcessor extends HttpOperation implements HttpProcessor {

    private String imagepathdata = "", sticker_pathdata = "";
    private String[] multipulimage;
    private boolean is_doc = false;


    public MainProcessor(String imagepath, boolean is_doc) {
        multipulimage = new String[0];
        imagepathdata = imagepath;
        this.is_doc = is_doc;
    }

    public MainProcessor(String[] multipulimage1, boolean is_doc) {
        imagepathdata = "";
        multipulimage = multipulimage1;
    }


    public MainProcessor(String imagepath, String[] multipulimage1) {
        imagepathdata = imagepath;
        multipulimage = multipulimage1;
    }

    JSONObject resData;
    int j = 0;
    ListBean data;
    JSONArray sub_gallary, sub_list_parameter, sub_array_news;


    @Override
    public HttpObject getHttp(Map<String, String> mapParams, String url) {
        HttpObject object = new HttpObject();
        //  object.setInfo(HttpRequester.//LogIN);
        object.setUrl(generateUrlWithParams(url, mapParams));
        return object;
    }

    @SuppressWarnings("unchecked")
    @Override
    public CommenBean parseObject(HttpObject object) {
        CommenBean data = new CommenBean();


        if (multipulimage.length > 0 && imagepathdata.length() > 0) {
            object = request(object, imagepathdata, multipulimage);
        } else {
            if (imagepathdata.length() > 0 && is_doc) {
                object = request_doc(object, imagepathdata);
            } else if (multipulimage.length > 0) {
                object = request(object, multipulimage);
            } else if (imagepathdata.length() > 0) {
                object = request(object, imagepathdata);
            } else {
                object = request(object);
            }
        }


        checkHttpStatus(object, data);

        if (data.result == Response.RESPONSE_RESULT.failed) {
            data.message = MessageConstants.No_Data_Found;
            data.result = Response.RESPONSE_RESULT.failed;
            return data;
        }
        try {
            //Log.e("Result", "Result");
            JSONObject responseObj = new JSONObject(object.getResponseString());
            JSONObject responseData = responseObj
                    .getJSONObject(Response.STANDARD.responseData.name());


            data.responceData = responseData.toString();


            if (responseObj.has("responseparameters")) {
                JSONArray arr = responseObj.getJSONArray("responseparameters");

                data.value = new String[arr.length()];


                for (int i = 0; i < arr.length(); i++) {
                    data.value[i] = arr.getString(i);
                    //Log.e("json", i + "=" + data.value[i]);
                }

                data.getdatalist = new String[arr.length()];

                for (int i = 0; i < data.value.length; i++) {

                    if (responseData.has(data.value[i])) {
                        data.getdatalist[i] = get(data.value[i], responseData);
                        data.viewlist.put(data.value[i], get(data.value[i], responseData));
                        //Log.e("name=", "" + data.getdatalist[i]);
                    }
                }
            } else {
                data.viewlist = new HashMap<String, String>();
            }


            if (responseData.has("result")) {

                data.result1 = responseData.getString("result");

                if (data.result1.equals("success")) {
                    data.result = Response.RESPONSE_RESULT.success;


                    if (responseData.has("message"))
                        data.message = responseData.getString("message");


                    if (responseData.has("link"))
                        data.link = responseData.getString("link");


                } else {
                    data.result = Response.RESPONSE_RESULT.failed;
                    data.message = responseData.getString("message");
                }
            }


        } catch (Exception e) {
            data.message = MessageConstants.UNABLE_TO_LOGIN;
            //Log.e("In Elase", "Error: " + e.getMessage());
            data.result = Response.RESPONSE_RESULT.failed;

        } finally {
            object.release();
            object = null;
        }
        return data;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListBean parseList(HttpObject object) {

        data = new ListBean();


        if (imagepathdata.length() > 0) {
            object = request(object, imagepathdata);
        } else {
            object = request(object);
        }
        checkHttpStatus(object, data);

        if (data.result == Response.RESPONSE_RESULT.failed) {
            return data;
        }


        try {
            JSONObject resObj = new JSONObject(object.getResponseString());
            resData = resObj.getJSONObject(Response.STANDARD.responseData
                    .name());


            if (resData.has("result")) {
                data.resultMsg = resData.getString("result");

                if (data.resultMsg.equals("success")) {
                    data.result = Response.RESPONSE_RESULT.success;


                    if (resData.has("message"))
                        data.message = resData.getString("message");


                } else {
                    data.result = Response.RESPONSE_RESULT.failed;
                    data.message = resData.getString("message");
                }
            }


            if (resObj.has("responseparameters")) {

                JSONArray arr = resObj.getJSONArray("responseparameters");
                data.dataresponce = new String[resData.length()][arr.length()];


                data.value = new String[arr.length()];
                data.view_ids = new String[arr.length()];

                for (int i = 0; i < arr.length(); i++) {
                    data.value[i] = arr.getString(i);
                    data.view_ids[i] = arr.getString(i);
                }


            }

            if (resObj.has("newsparameter")) {

                sub_array_news = resObj.getJSONArray("newsparameter");
                data.value_news = new String[sub_array_news.length()];


                for (int i = 0; i < sub_array_news.length(); i++) {
                    data.value_news[i] = sub_array_news.getString(i);
                    //Log.e("json", i + "=" + data.value_sub[i]);
                }


            }

            if (resObj.has("galleryparameter")) {

                sub_gallary = resObj.getJSONArray("galleryparameter");
                data.value_gallery = new String[sub_gallary.length()];


                for (int i = 0; i < sub_gallary.length(); i++) {
                    data.value_gallery[i] = sub_gallary.getString(i);
                    //Log.e("json", i + "=" + data.value_sub[i]);
                }


            }


            for (int k = 0; k < resData.length(); k++) {

                String s2 = String.valueOf(k + 1);
                JSONObject item = resData.getJSONObject(s2);
                parseObject(item);

            }

//
//            Iterator<String> resIter = resData.keys();
//            while (resIter.hasNext()) {
//                String key = resIter.next();
//                JSONObject resItem = resData.getJSONObject(key);
//                parseObject(resItem);
//                // map.put(Integer.parseInt(key), dataObject);
//            }
//

            //  resIter = null;
            resData = null;
            resObj = null;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // data = null;
            object.release();
            object = null;
        }
        return data;

    }


    @SuppressWarnings("unchecked")
    @Override
    public void parseObject(JSONObject object) throws JSONException {
        Map<String, String> viewlist = new HashMap<String, String>();

        for (int i = 0; i < data.value.length; i++) {

            if (object.has(data.value[i])) {
                data.dataresponce[j][i] = get(data.value[i], object);
                viewlist.put(data.value[i], get(data.value[i], object));
                //Log.e("name=", "" + data.dataresponce[j][i]);
            }

        }

        data.selectedresponcefiel.add(viewlist);


        if (object.has("news")) {
            ArrayList<Map<String, String>> listnews = new ArrayList<Map<String, String>>();
            JSONObject resData1 = object.getJSONObject("news");
            if (resData1.has("result")) {

            } else {

                for (int k = 0; k < resData1.length(); k++) {

                    String s2 = String.valueOf(k + 1);
                    JSONObject item = resData1.getJSONObject(s2);

                    Map<String, String> datlistnn = new HashMap<String, String>();

                    for (int i = 0; i < data.value_news.length; i++) {

                        if (item.has(data.value_news[i])) {

                            datlistnn.put(data.value_news[i], get(data.value_news[i], item));

                        }


                    }


                    if (item.has("gallery")) {
                        ArrayList<Map<String, String>> listgallay = new ArrayList<Map<String, String>>();
                        JSONObject resgalay = item.getJSONObject("gallery");

                        if (resgalay.has("result")) {

                        } else {

                            for (int n = 0; n < resgalay.length(); n++) {

                                String s3 = String.valueOf(n + 1);
                                JSONObject itemgg = resgalay.getJSONObject(s3);

                                Map<String, String> datlistgg = new HashMap<String, String>();

                                for (int o = 0; o < data.value_gallery.length; o++) {

                                    if (itemgg.has(data.value_gallery[o])) {

                                        datlistgg.put(data.value_gallery[o], get(data.value_gallery[o], itemgg));


                                        // //Log.e("sub===", "" + datlist[i]);
                                    }

                                }

                                listgallay.add(datlistgg);


                            }
                        }

                        data.gallery.add(listgallay);


                    }


                    listnews.add(datlistnn);


                }
            }

            data.news.add(listnews);


        }


        j++;


    }


}