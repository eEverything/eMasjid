package http.social_login;

import http.CommenRequest.ResData;

/**
 * Created by Ki-Master on 31/03/2017.
 */
public interface ServiceCallback {
    void onSuccesss(ResData data);

    void onError(ResData data);

    public static class EmptyCallback implements ServiceCallback {

        @Override
        public void onSuccesss(ResData data) {
        }

        @Override
        public void onError(ResData data) {
        }
    }
}
