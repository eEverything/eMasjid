package com.other;

public class General {

    public static boolean findWord(String string, String word) {

        if (string.toLowerCase().indexOf(word.toLowerCase()) > -1) {

            return true;
        }

        return false;
    }

    public static boolean isImage(String image) {

        if (findWord( image, ".jpg" )
                || findWord( image, ".jpeg" )
                || findWord( image, ".png" )
                || findWord( image, ".gif" ))
            return true;

        return false;
    }

    public static boolean isDocument(String document) {

        if (findWord( document, ".doc" )
                || findWord( document, ".pdf" )
                || findWord( document, ".docx" )
                || findWord( document, ".rtf" )
                || findWord( document, ".txt" )
                || findWord( document, ".jpg" )
                || findWord( document, ".jpeg" )
                || findWord( document, ".png" )
                || findWord( document, ".gif" )
                || findWord( document, ".csv" ))
            return true;

        return false;
    }

    public static boolean isVideo(String video) {

        if (findWord( video, ".mp4" )
                || findWord( video, ".3gp" ))
            return true;

        return false;
    }
}
