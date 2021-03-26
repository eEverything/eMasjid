package http.CommenRequest;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import http.bean.ResponseData;

public class ListBean extends ResponseData {

    private static final long serialVersionUID = 1L;
    //  public String id, name, responceData, image;
    public String[][] dataresponce;
    public String[] view_ids;
    public String[] value_news, value, value_gallery;
    public ArrayList<Map<String, String>> selectedresponcefiel = new ArrayList<Map<String, String>>();
    public List<ArrayList<Map<String, String>>> news = new ArrayList<ArrayList<Map<String, String>>>();
    public List<ArrayList<Map<String, String>>> gallery = new ArrayList<ArrayList<Map<String, String>>>();


    public void release() {
        dataresponce = null;
        gallery.clear();
        news.clear();

        super.release();
        callGC();
    }
}