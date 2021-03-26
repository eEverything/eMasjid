package com.emasjid;

public class media_object {

    String data,type;

    public media_object(){}

    public media_object(String type, String data){
        this.data=data;
        this.type=type;
    }


    public String getData() {
        return data;
    }

    public String getType() {
        return type;
    }

}
