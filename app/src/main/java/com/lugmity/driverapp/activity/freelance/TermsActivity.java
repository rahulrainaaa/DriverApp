package com.lugmity.driverapp.activity.freelance;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lugmity.driverapp.R;
import com.lugmity.driverapp.constants.Constants;

public class TermsActivity extends FragmentActivity {

    private WebView webView = null;
    private int flag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);
        webView = (WebView) findViewById(R.id.webView);
        String doc = "";

        if (Constants.strLanguage.contains("arabic")) {
            //load arabic document
            doc = "https://docs.google.com/document/d/1IZDm5Hqz5AWlVn7yKjvFZW3dew3WsQyqBTWGcfL5CAs/view";
        } else {
            //load English document
            doc = "https://docs.google.com/document/d/1mvU-56r-DSYpMJ1ll0rPY1bLpM75Va-tcmGW5paUA9Q/view";
        }
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(doc);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (flag == 1) {
            finish();
            return;
        } else if (flag == 0) {
            flag = 1;
        }
    }
}
