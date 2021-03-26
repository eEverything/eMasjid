package com.emasjid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

public class Donate extends AppCompatActivity {

    WebView webview;
    ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        webview=findViewById(R.id.webview);
        back = findViewById(R.id.back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent=getIntent();

        if(intent.hasExtra("link"))
        {
            String linkText=intent.getStringExtra("link");

            webview.getSettings().setJavaScriptEnabled(true);
            webview.setWebViewClient(new MyWebViewClient());
            webview.setWebChromeClient(new WebChromeClient());

            if(!linkText.contains("http")) {
                linkText = "https://" + linkText;
            }

            Log.e("weburl", linkText);

                webview.loadUrl(linkText);

        }


    }

    public class MyWebViewClient extends WebViewClient {
        @Override
        //Run script on every page, similar to Greasemonkey:
        public void onPageFinished(WebView view, String url) {
        }

        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

}
