package http.social_login;

/**
 * Created by Ki-Master on 31/03/2017.
 */
public interface Callback {
    void onSuccesss(LoginDataClass data);

    void onError();

    public static class EmptyCallback implements Callback {

        @Override
        public void onSuccesss(LoginDataClass data) {
        }

        @Override
        public void onError() {
        }
    }
}
