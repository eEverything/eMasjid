package http.CommenRequest;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import http.bean.ResponseData;

public class CommenBean extends ResponseData {

    private static final long serialVersionUID = 1L;
    public String message, link, responceData, result1;

    public String[] getdatalist;
    public Map<String, String> viewlist = new HashMap<String, String>();
    public String[] servicearray;
    public String[] value;
    public String[] value_sub;
    public String[][] getsublistdata;
    public ArrayList<Map<String, String>> detaillistdata = new ArrayList<Map<String, String>>();

    public void release() {
        getdatalist = null;
        getsublistdata = null;
        message = null;
        result1 = null;
        responceData = null;
        viewlist.clear();
        super.release();
        callGC();
    }
}