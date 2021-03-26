package http.bean;

//import module.common.http.Response.RESPONSE_RESULT;


import http.Response;

public class ResponseData implements DataInterface {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public Response.RESPONSE_RESULT result;
    public String resultMsg;
    public String message;
    public String email;
    public int status;

    public String userid, last_inserted_id;

    /**
     * Object Release Code
     */
    public void release() {

        //result = null;
        resultMsg = null;
        message = null;
        callGC();
    }

    public void callGC() {
    }
}
