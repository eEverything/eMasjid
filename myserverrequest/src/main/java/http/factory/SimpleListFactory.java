package http.factory;

import java.util.List;
import java.util.Map;

import http.HttpObject;
import http.HttpProcessor;
import http.Request;
import http.bean.ResponseData;


public class SimpleListFactory implements BaseFactory {

    private static SimpleListFactory factory;

    private SimpleListFactory() {
    }

    public static SimpleListFactory getInstance() {

        if (factory == null)
            factory = new SimpleListFactory();
        return factory;
    }

    public void setParameter(Request request, String value,
                             Map<Request, String> mapParams) {

        if (value != null && value.length() > 0) {
            mapParams.put(request, value);
        }
    }

    public <T extends ResponseData> T getResponseObject(
            HttpProcessor processor, Map<String, String> params) {

        HttpObject object = processor.getHttp(params, "");
        T resData = processor.parseObject(object);
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

    public <T extends ResponseData> List<T> getList(HttpProcessor processor,
                                                    Map<String, String> params) {
        HttpObject object = processor.getHttp(params, "");
        List<T> resData = processor.parseList(object);
        releaseProcessor(processor);
        return resData;
    }


//    public List<AudioBean> getAudio(String user_id, String cat_id) {
//        Map<Request, String> mapParams = new HashMap<Request, String>();
//        mapParams.put(AudioProcessor.AUDIO_REQ.user_id, user_id);
//        mapParams.put(AudioProcessor.AUDIO_REQ.category_id, cat_id);
//        return getList(new AudioProcessor(), mapParams);
//    }

}
