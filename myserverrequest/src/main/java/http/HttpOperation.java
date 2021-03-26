package http;

import android.graphics.Bitmap;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import http.bean.ResponseData;
import http.constant.AppConstants;
import http.constant.MessageConstants;


// class used to generate and send url with or without image / file
//update `masjids` set isha_time = '15:20:00' where id = 1
public abstract class HttpOperation {

    protected static final String QUE = "?";
    protected static final String AND = "&";
    protected static final String EQ = "=";

    //  protected static final String MAIN_URL = "http://keshavinfotechdemo.com/KESHAV/KG2/WEI/webservices/";
    //"http://keshavinfotechdemo.com/KESHAV/KG2/CUR/webservices/";
    // "http://kurtscave.com/brainwave/webservices/"

    // protected static final String MAIN_URL = "http://s679558170.onlinehome.us/vch/webservices/";
    protected static final String MAIN_URL = "http://keshavinfotechdemo.com/KESHAV/KG2/TRU/webservices/";

    //  http://keshavinfotechdemo.com/KESHAV/Fresher_training/dharmesh_joshi/webservice_demo/product_listing.php
    // http://keshavinfotechdemo.com/KESHAV/KG2/syn/webservices/
    protected static final String URL_FILLER = "1=1";

    protected String generateUrlWithParams(String url,
                                           Map<String, String> mapValues) {
        String finalUrl = MAIN_URL + url + QUE;


        if (mapValues != null && mapValues.size() > 0) {

            for (String paramName : mapValues.keySet()) {

                finalUrl += AND + paramName + EQ
                        + mapValues.get(paramName);
            }
        }
        String newUrl = finalUrl.replaceAll(" ", "%20");
        newUrl = newUrl.replaceAll("\\r", "");
        newUrl = newUrl.replaceAll("\\t", "");
        newUrl = newUrl.replaceAll("\\n\\n", "%20");
        newUrl = newUrl.replaceAll("\\n", "%20");
        Log.e("URL: ", newUrl);
        // Debugger.message("URL : " + newUrl);
        return newUrl;
    }


    protected HttpObject request(HttpObject httpObject) {

        StringBuilder builder = new StringBuilder();
        String newUrl = httpObject.getUrl().replaceAll(" ", "%20");
        newUrl = newUrl.replaceAll("\\r", "");
        newUrl = newUrl.replaceAll("\\t", "");
        newUrl = newUrl.replaceAll("\\n\\n", "%20");
        newUrl = newUrl.replaceAll("\\n", "%20");

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(newUrl);
        HttpResponse response = null;
        StatusLine statusLine = null;
        HttpEntity entity = null;
        InputStream content = null;
        BufferedReader reader = null;

        int STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;

        try {
            response = client.execute(httpPost);
            statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {

                entity = response.getEntity();
                content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                STATUS = AppConstants.INT_STATUS_SUCCESS;
                reader.close();
                content.close();

            } else {
                STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
            }
        } catch (ClientProtocolException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_CLIENT;

        } catch (ConnectTimeoutException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_TIMEOUT;

        } catch (IOException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_IO;

        } catch (Exception e) {
            STATUS = AppConstants.INT_STATUS_FAILED_IO;
            e.printStackTrace();
        } finally {

            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            } finally {
                reader = null;
            }
            try {
                if (content != null) {
                    content.close();
                }
            } catch (Exception e) {
            } finally {
                content = null;
            }
            response = null;
            statusLine = null;
            entity = null;

            httpPost = null;
            client = null;
            newUrl = null;
            System.gc();
        }

        httpObject.setResponseString(builder.toString());
        httpObject.setStatus(STATUS);

        return httpObject;
    }

    protected HttpObject request(HttpObject httpObject, String imageFile, int i) {

        StringBuilder builder = new StringBuilder();
        String newUrl = httpObject.getUrl().replaceAll(" ", "%20");
        newUrl = newUrl.replaceAll("\\r", "");
        newUrl = newUrl.replaceAll("\\t", "");
        newUrl = newUrl.replaceAll("\\n\\n", "%20");
        newUrl = newUrl.replaceAll("\\n", "%20");

        Bitmap bm;

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(newUrl);
        HttpResponse response = null;
        StatusLine statusLine = null;
        HttpEntity entity = null;
        InputStream content = null;
        BufferedReader reader = null;

        int STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
        try {

            MultipartEntity entityMulti = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            if (i == 0) {
                // Log.e("httpoperation000", "fhgfhdgf");
                entityMulti.addPart("image_path", new FileBody(new File(
                        imageFile)));
            } else if (i == 1) {
                // Log.e("httpoperation", "bgfhghg");
                entityMulti.addPart("store_image", new FileBody(new File(
                        imageFile)));
            }
            httpPost.setEntity(entityMulti);
            response = client.execute(httpPost);
            statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {

                entity = response.getEntity();
                content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));
                String line;

                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                STATUS = AppConstants.INT_STATUS_SUCCESS;
                reader.close();
                content.close();

            } else {
                STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
            }

        } catch (ClientProtocolException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_CLIENT;

        } catch (ConnectTimeoutException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_TIMEOUT;

        } catch (IOException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_IO;

        } finally {

            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            } finally {
                reader = null;
            }

            try {
                if (content != null) {
                    content.close();
                }
            } catch (Exception e) {
            } finally {
                content = null;
            }

            response = null;
            statusLine = null;
            entity = null;

            httpPost = null;
            client = null;
            newUrl = null;
            System.gc();
        }
        httpObject.setResponseString(builder.toString());
        httpObject.setStatus(STATUS);
        return httpObject;
    }


    protected HttpObject request(HttpObject httpObject, String[] multipulimages) {

        StringBuilder builder = new StringBuilder();
        String newUrl = httpObject.getUrl().replaceAll(" ", "%20");
        newUrl = newUrl.replaceAll("\\r", "");
        newUrl = newUrl.replaceAll("\\t", "");
        newUrl = newUrl.replaceAll("\\n\\n", "%20");
        newUrl = newUrl.replaceAll("\\n", "%20");

        Bitmap bm;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(newUrl);
        HttpResponse response = null;
        StatusLine statusLine = null;
        HttpEntity entity = null;
        InputStream content = null;
        BufferedReader reader = null;
        int STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
        try {

            MultipartEntity entityMulti = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);


         //   Log.e("multipulimages",multipulimages.toString()+"");
            for (int i = 0; i < multipulimages.length; i++) {
                entityMulti
                        .addPart("image_" + (i+1), new FileBody(new File(multipulimages[i])));
                Log.e("multipulimages",multipulimages[i]+"");
                Log.e("image_",+ (i+1)+"");
            }



            httpPost.setEntity(entityMulti);

            response = client.execute(httpPost);
            statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {

                entity = response.getEntity();
                content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                STATUS = AppConstants.INT_STATUS_SUCCESS;

                reader.close();
                content.close();

            } else {
                STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
            }

        } catch (ClientProtocolException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_CLIENT;

        } catch (ConnectTimeoutException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_TIMEOUT;

        } catch (IOException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_IO;

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            } finally {
                reader = null;
            }
            try {
                if (content != null) {
                    content.close();
                }
            } catch (Exception e) {
            } finally {
                content = null;
            }
            response = null;
            statusLine = null;
            entity = null;

            httpPost = null;
            client = null;
            newUrl = null;
            System.gc();
        }
        httpObject.setResponseString(builder.toString());
        httpObject.setStatus(STATUS);
        return httpObject;
    }


    protected HttpObject request(HttpObject httpObject, String imageFile, String[] multipulimages) {

        StringBuilder builder = new StringBuilder();
        String newUrl = httpObject.getUrl().replaceAll(" ", "%20");
        newUrl = newUrl.replaceAll("\\r", "");
        newUrl = newUrl.replaceAll("\\t", "");
        newUrl = newUrl.replaceAll("\\n\\n", "%20");
        newUrl = newUrl.replaceAll("\\n", "%20");

        Bitmap bm;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(newUrl);
        HttpResponse response = null;
        StatusLine statusLine = null;
        HttpEntity entity = null;
        InputStream content = null;
        BufferedReader reader = null;
        int STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
        try {

            MultipartEntity entityMulti = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entityMulti
                    .addPart("document", new FileBody(new File(imageFile)));


            for (int i = 0; i < multipulimages.length; i++) {
                entityMulti
                        .addPart("image_" + (i+1), new FileBody(new File(multipulimages[i])));
            }


            httpPost.setEntity(entityMulti);

            response = client.execute(httpPost);
            statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {

                entity = response.getEntity();
                content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                STATUS = AppConstants.INT_STATUS_SUCCESS;

                reader.close();
                content.close();

            } else {
                STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
            }

        } catch (ClientProtocolException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_CLIENT;

        } catch (ConnectTimeoutException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_TIMEOUT;

        } catch (IOException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_IO;

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            } finally {
                reader = null;
            }
            try {
                if (content != null) {
                    content.close();
                }
            } catch (Exception e) {
            } finally {
                content = null;
            }
            response = null;
            statusLine = null;
            entity = null;

            httpPost = null;
            client = null;
            newUrl = null;
            System.gc();
        }
        httpObject.setResponseString(builder.toString());
        httpObject.setStatus(STATUS);
        return httpObject;
    }


    protected HttpObject request_sticker_path(HttpObject httpObject, String imageFile, String sticker_pathdata) {

        StringBuilder builder = new StringBuilder();
        String newUrl = httpObject.getUrl().replaceAll(" ", "%20");
        newUrl = newUrl.replaceAll("\\r", "");
        newUrl = newUrl.replaceAll("\\t", "");
        newUrl = newUrl.replaceAll("\\n\\n", "%20");
        newUrl = newUrl.replaceAll("\\n", "%20");

        Bitmap bm;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(newUrl);
        HttpResponse response = null;
        StatusLine statusLine = null;
        HttpEntity entity = null;
        InputStream content = null;
        BufferedReader reader = null;
        int STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
        try {

            MultipartEntity entityMulti = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entityMulti
                    .addPart("image_path", new FileBody(new File(imageFile)));
            entityMulti
                    .addPart("sticker_path", new FileBody(new File(sticker_pathdata)));
            httpPost.setEntity(entityMulti);

            response = client.execute(httpPost);
            statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {

                entity = response.getEntity();
                content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                STATUS = AppConstants.INT_STATUS_SUCCESS;

                reader.close();
                content.close();

            } else {
                STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
            }

        } catch (ClientProtocolException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_CLIENT;

        } catch (ConnectTimeoutException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_TIMEOUT;

        } catch (IOException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_IO;

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            } finally {
                reader = null;
            }
            try {
                if (content != null) {
                    content.close();
                }
            } catch (Exception e) {
            } finally {
                content = null;
            }
            response = null;
            statusLine = null;
            entity = null;

            httpPost = null;
            client = null;
            newUrl = null;
            System.gc();
        }
        httpObject.setResponseString(builder.toString());
        httpObject.setStatus(STATUS);
        return httpObject;
    }

    protected HttpObject request_doc(HttpObject httpObject, String imageFile) {

        StringBuilder builder = new StringBuilder();
        String newUrl = httpObject.getUrl().replaceAll(" ", "%20");
        newUrl = newUrl.replaceAll("\\r", "");
        newUrl = newUrl.replaceAll("\\t", "");
        newUrl = newUrl.replaceAll("\\n\\n", "%20");
        newUrl = newUrl.replaceAll("\\n", "%20");

        Bitmap bm;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(newUrl);
        HttpResponse response = null;
        StatusLine statusLine = null;
        HttpEntity entity = null;
        InputStream content = null;
        BufferedReader reader = null;
        int STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
        try {

            MultipartEntity entityMulti = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entityMulti
                    .addPart("document", new FileBody(new File(imageFile)));
            httpPost.setEntity(entityMulti);

            response = client.execute(httpPost);
            statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {

                entity = response.getEntity();
                content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                STATUS = AppConstants.INT_STATUS_SUCCESS;

                reader.close();
                content.close();

            } else {
                STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
            }

        } catch (ClientProtocolException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_CLIENT;

        } catch (ConnectTimeoutException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_TIMEOUT;

        } catch (IOException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_IO;

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            } finally {
                reader = null;
            }
            try {
                if (content != null) {
                    content.close();
                }
            } catch (Exception e) {
            } finally {
                content = null;
            }
            response = null;
            statusLine = null;
            entity = null;

            httpPost = null;
            client = null;
            newUrl = null;
            System.gc();
        }
        httpObject.setResponseString(builder.toString());
        httpObject.setStatus(STATUS);
        return httpObject;
    }

    protected HttpObject request(HttpObject httpObject, String imageFile) {

        StringBuilder builder = new StringBuilder();
        String newUrl = httpObject.getUrl().replaceAll(" ", "%20");
        newUrl = newUrl.replaceAll("\\r", "");
        newUrl = newUrl.replaceAll("\\t", "");
        newUrl = newUrl.replaceAll("\\n\\n", "%20");
        newUrl = newUrl.replaceAll("\\n", "%20");

        Bitmap bm;
        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(newUrl);
        HttpResponse response = null;
        StatusLine statusLine = null;
        HttpEntity entity = null;
        InputStream content = null;
        BufferedReader reader = null;
        int STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
        try {

            MultipartEntity entityMulti = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            entityMulti
                    .addPart("profile_picture", new FileBody(new File(imageFile)));
            httpPost.setEntity(entityMulti);

            response = client.execute(httpPost);
            statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();

            if (statusCode == 200) {

                entity = response.getEntity();
                content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                STATUS = AppConstants.INT_STATUS_SUCCESS;

                reader.close();
                content.close();

            } else {
                STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
            }

        } catch (ClientProtocolException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_CLIENT;

        } catch (ConnectTimeoutException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_TIMEOUT;

        } catch (IOException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_IO;

        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            } finally {
                reader = null;
            }
            try {
                if (content != null) {
                    content.close();
                }
            } catch (Exception e) {
            } finally {
                content = null;
            }
            response = null;
            statusLine = null;
            entity = null;

            httpPost = null;
            client = null;
            newUrl = null;
            System.gc();
        }
        httpObject.setResponseString(builder.toString());
        httpObject.setStatus(STATUS);
        return httpObject;
    }

//this is code of image upload from user masjid
    protected HttpObject request(HttpObject httpObject,
                                 ArrayList<String> imageFile) {

        StringBuilder builder = new StringBuilder();
        String newUrl = httpObject.getUrl().replaceAll(" ", "%20");

        Bitmap bm;

        HttpClient client = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(newUrl);
        HttpResponse response = null;
        StatusLine statusLine = null;
        HttpEntity entity = null;
        InputStream content = null;
        BufferedReader reader = null;

        int STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
        try {
            Log.e("LOG imageFile......", "" + imageFile);

            MultipartEntity entityMulti = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            for (int i = 0; i < imageFile.size(); i++) {

                if (i == 0) {
                    entityMulti.addPart("image_path", new FileBody(new File(
                            imageFile.get(i))));
                    System.out.println("image ::: file :: " + i + "::"
                            + imageFile.get(i));
                } else {
                    entityMulti.addPart("image_path" + i, new FileBody(
                            new File(imageFile.get(i))));
                    System.out.println("image ::: file :: " + i + "::"
                            + imageFile.get(i));
                }

            }

            httpPost.setEntity(entityMulti);

            response = client.execute(httpPost);
            statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.e("LOG statusCode......", "" + statusCode);
            if (statusCode == 200) {

                Log.e("LOG statusCode 22......", "" + statusCode);
                entity = response.getEntity();
                content = entity.getContent();
                reader = new BufferedReader(new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                STATUS = AppConstants.INT_STATUS_SUCCESS;

                reader.close();
                content.close();

            } else {
                STATUS = AppConstants.INT_STATUS_FAILED_DOWNLOAD;
            }

        } catch (ClientProtocolException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_CLIENT;

        } catch (ConnectTimeoutException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_TIMEOUT;

        } catch (IOException e) {
            STATUS = AppConstants.INT_STATUS_FAILED_IO;

        } finally {

            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception e) {
            } finally {
                reader = null;
            }

            try {
                if (content != null) {
                    content.close();
                }
            } catch (Exception e) {
            } finally {
                content = null;
            }

            response = null;
            statusLine = null;
            entity = null;

            httpPost = null;
            client = null;
            newUrl = null;
            System.gc();
        }

        httpObject.setResponseString(builder.toString());
        httpObject.setStatus(STATUS);
        return httpObject;
    }

    protected void checkHttpStatus(HttpObject httpObject, ResponseData data) {

        if (data == null) {
            data = new ResponseData();
        }
        data.result = Response.RESPONSE_RESULT.failed;
        data.resultMsg = MessageConstants.No_Data_Found;

        switch (httpObject.getStatus()) {

            case AppConstants.INT_STATUS_FAILED_DOWNLOAD:
                data.resultMsg = MessageConstants.Failed_To_Connect;
                break;

            case AppConstants.INT_STATUS_FAILED_TIMEOUT:
                data.resultMsg = MessageConstants.Failed_TimeOut;
                break;

            case AppConstants.INT_STATUS_FAILED_IO:
                data.resultMsg = MessageConstants.Failed_To_Read;
                break;

            case AppConstants.INT_STATUS_SUCCESS:
                data.resultMsg = null;
                data.result = Response.RESPONSE_RESULT.success;
                break;
        }
    }

    protected boolean isHttpError(HttpObject httpObject) {

        boolean isError = false;

        switch (httpObject.getStatus()) {

            case AppConstants.INT_STATUS_FAILED_DOWNLOAD:
                isError = true;
                break;

            case AppConstants.INT_STATUS_FAILED_TIMEOUT:
                isError = true;
                break;

            case AppConstants.INT_STATUS_FAILED_IO:
                isError = true;
                break;

            case AppConstants.INT_STATUS_SUCCESS:
                isError = false;
                break;
        }
        return isError;
    }

    protected String get(String key, JSONObject resItem) throws JSONException {
        return (resItem.has(key)) ? resItem.getString(key) : null;
    }

    protected double getDouble(String key, JSONObject resItem)
            throws JSONException {
        return (resItem.has(key)) ? resItem.getDouble(key) : null;
    }

    protected JSONObject getJOSNObject(String key, JSONObject resItem)
            throws JSONException {
        return (resItem.has(key)) ? resItem.getJSONObject(key) : null;
    }
}
