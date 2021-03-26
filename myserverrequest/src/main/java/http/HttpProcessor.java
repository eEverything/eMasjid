package http;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import http.bean.ResponseData;


public interface HttpProcessor {

    public HttpObject getHttp(Map<String, String> mapParams, String url);

    public <T extends ResponseData> T parseObject(HttpObject object);

    public <T extends ResponseData> T parseList(HttpObject object);

    public void parseObject(JSONObject object) throws JSONException;
}
