package com.example.dtcbuspass;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MoreInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_info);


        WebView view =  findViewById(R.id.webView);


        view.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
        view.getSettings().setJavaScriptEnabled(true);

        //redirect on dtc site
        view.loadUrl("http://www.dtc.nic.in/content/get-dtc-pass");

    }
}
