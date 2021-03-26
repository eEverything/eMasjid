package com.emasjid;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class Bookmark_object implements Serializable {


    public String masjid_id , news_id, news_title, description, document, day_ago, bookmark , profile_picture;
    public HashMap<String, String> aimage;
    public List<String> gallery_list;
    public String status = "1";

    public Bookmark_object() {
    }

    public Bookmark_object(String masjid_id,String news_id, String news_title, String description, String document, String day_ago, String bookmark,List<String> gallery_list , String profile_picture) {
        this.masjid_id = masjid_id;
        this.news_id = news_id;
        this.news_title = news_title;
        this.description = description;
        this.document = document;
        this.day_ago = day_ago;
        this.bookmark = bookmark;
        this.gallery_list = gallery_list;
        this.profile_picture = profile_picture;
    }

    public HashMap<String, String> getImage() {
        return aimage;
    }

    public void setImage(HashMap<String, String> image) {
        this.aimage = image;
    }

    public List<String> getGalleryList() {
        return gallery_list;
    }
}
