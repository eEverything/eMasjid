package com.emasjid;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

public class Document extends AppCompatActivity {

    WebView webview;

    ImageView back;

    String link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.document);

        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        webview = findViewById(R.id.webview);

        if (getIntent().hasExtra("link")) {
            link = getIntent().getStringExtra("link");
        }


        if (link != null && link.length() > 0) {

            String[] ext = link.split("\\.");


            if (ext[2].contains("pdf") || ext[2].contains("doc"))

            {
                webview.setWebViewClient(new myWebClient());
                WebSettings webSettings = webview.getSettings();
                webview.getSettings().setLoadsImagesAutomatically(true);
                webview.getSettings().setJavaScriptEnabled(true);

                webSettings.setJavaScriptEnabled(true);
                //  webView.loadUrl("http://google.com");
                webview.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + link);
            }
            else
            {

                webview.loadUrl(link);

            }

        } else {
            Toast.makeText(this, "Link not found", Toast.LENGTH_SHORT).show();
        }

    }

    public class myWebClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }


}
