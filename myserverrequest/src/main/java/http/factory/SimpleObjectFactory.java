package http.factory;

import java.util.Map;

import http.CommenRequest.CommenBean;
import http.CommenRequest.ListBean;
import http.CommenRequest.MainProcessor;
import http.HttpObject;
import http.HttpProcessor;
import http.bean.ResponseData;


public class SimpleObjectFactory implements BaseFactory {

    private static SimpleObjectFactory factory;

    private SimpleObjectFactory() {
    }

    public static SimpleObjectFactory getInstance() {

        if (factory == null)
            factory = new SimpleObjectFactory();
        return factory;
    }

    public <T extends ResponseData> T getResponseObject(
            HttpProcessor processor, Map<String, String> params, String url) {

        HttpObject object = processor.getHttp(params, url);
        T resData = processor.parseObject(object);
        releaseProcessor(processor);
        return resData;
    }


    public <T extends ResponseData> T getList(HttpProcessor processor,
                                              Map<String, String> params, String url) {
        HttpObject object = processor.getHttp(params, url);
        T resData = processor.parseList(object);
        releaseProcessor(processor);
        return resData;
    }


    public void releaseProcessor(HttpProcessor processor) {
        processor = null;
        callGC();
    }

    public void callGC() {
        System.gc();
    }


    public ListBean getListData(Map<String, String> mapParams, String url, String image_path) {

        if (image_path.length() > 0) {
            return getList(new MainProcessor(image_path, false), mapParams, url);
        } else {
            return getList(new MainProcessor("", false), mapParams, url);
        }


    }



    public CommenBean request(Map<String, String> mapParams, String url, String image_path) {

        if (image_path.length() > 0) {
            return getResponseObject(new MainProcessor(image_path, false), mapParams, url);
        } else {
            return getResponseObject(new MainProcessor("", false), mapParams, url);
        }

    }

    public CommenBean request_doc(Map<String, String> mapParams, String url, String image_path) {


        return getResponseObject(new MainProcessor(image_path, true), mapParams, url);


    }

    public CommenBean request_doc(Map<String, String> mapParams, String url, String[] image_path) {


        return getResponseObject(new MainProcessor(image_path, true), mapParams, url);


    }

    public CommenBean request(Map<String, String> mapParams, String url, String docpath, String[] multipluimages) {

        if (multipluimages.length > 0 && docpath.length() > 0) {
            return getResponseObject(new MainProcessor(docpath, multipluimages), mapParams, url);
        } else {
            return getResponseObject(new MainProcessor("", false), mapParams, url);
        }

    }
}
