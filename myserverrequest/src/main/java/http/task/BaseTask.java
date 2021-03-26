package http.task;


import android.content.Context;
import android.os.AsyncTask;

import http.application.ApplicationHandler;
import http.bean.ResponseData;
import http.factory.BaseFactory;


public abstract class BaseTask extends AsyncTask<String, Context, Void> {

    protected ApplicationHandler appHandler = ApplicationHandler.getInstance();

    @Override
    protected Void doInBackground(String... params) {
        return null;
    }

    public abstract <T extends ResponseData> T getData(int pos);

    public void releaseFactory(BaseFactory factory) {

        if (factory == null)
            return;


        factory = null;
        callGC();
    }

    abstract public void release();

    public void callGC() {
    }
}
