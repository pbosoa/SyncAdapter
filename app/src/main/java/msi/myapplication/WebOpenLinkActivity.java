package msi.myapplication;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebChromeClient;
import android.webkit.WebView;


public class WebOpenLinkActivity extends ActionBarActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_open_link);

        webView = (WebView) findViewById(R.id.webview);

        String url = getIntent().getExtras().getString("link");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
    }
}
