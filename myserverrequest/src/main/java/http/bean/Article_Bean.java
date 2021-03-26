package http.bean;

import java.util.ArrayList;

public class Article_Bean extends ResponseData {

    private static final long serialVersionUID = 1L;

    public String result1, location, post_id, post_title, path, date, is_sync, split_path;
    public ArrayList<String> carImages = new ArrayList<>();

    public void release() {
        result1 = null;
        location = null;
        date = null;
        post_id = null;
        post_title = null;
        is_sync = null;
        path = null;
        split_path = null;

        result = null;
        message = null;

        super.release();
        ;
        callGC();
    }

}
