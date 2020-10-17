package com.github.lzyzsd.jsbridge.example;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.github.lzyzsd.jsbridge.BridgeHandler;
import com.github.lzyzsd.jsbridge.BridgeWebView;
import com.github.lzyzsd.jsbridge.BridgeWebViewClient;
import com.github.lzyzsd.jsbridge.CallBackFunction;
import com.tencent.smtt.export.external.interfaces.JsResult;
import com.tencent.smtt.export.external.interfaces.SslError;
import com.tencent.smtt.export.external.interfaces.SslErrorHandler;
import com.tencent.smtt.export.external.interfaces.WebResourceError;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;


/**
 * ATST 课程专用（appId, token均与超学不同）
 */
public class WebViewJSBridgeUtils {

  private static final String MD5_KEY = "b39401f1";

  public WebViewJSBridgeUtils() {
  }

  public void initWebView(BridgeWebView webView) {
    WebSettings webSettings = webView.getSettings();
    webSettings.setMixedContentMode(WebSettings.LOAD_NORMAL);
    webSettings.setJavaScriptEnabled(true);
    webSettings.setDomStorageEnabled(true);
    webSettings.setAllowFileAccess(true);
    webSettings.setBuiltInZoomControls(true);
    webSettings.setDisplayZoomControls(false);
    webSettings.setBuiltInZoomControls(true);// 打开缩放按钮
    webSettings.setSupportZoom(true);// 支持缩放
    // H5 缓存，需要设置缓存路径
    webSettings.setAppCacheEnabled(true);
    webSettings.setDatabaseEnabled(true);
    webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
    webSettings.setLoadsImagesAutomatically(false);
    webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);//软件解码
    webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);//硬件解码
    webView.setWebChromeClient(new WebChromeClient() {
      @Override
      public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
      }
    });
    webView.setWebViewClient(new MyWebViewClient(webView));
    registerHandler(webView);
  }


  private void registerHandler(BridgeWebView pWebView) {
    pWebView.registerHandler("getHeaders", new BridgeHandler() {
      @Override
      public void handler(final String data, final CallBackFunction function) {
        function.onCallBack(JSON.toJSONString(getHeaders()));
      }
    });
  }

  /**
   * 自定义的WebViewClient
   */
  class MyWebViewClient extends BridgeWebViewClient {

    public MyWebViewClient(BridgeWebView webView) {
      super(webView);
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
      super.onReceivedError(view, request, error);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
      handler.proceed();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
      super.onPageFinished(view, url);
      if (!view.getSettings().getLoadsImagesAutomatically()) {
        view.getSettings().setLoadsImagesAutomatically(true);
      }
    }
  }

  public void releaseWebView(BridgeWebView mWebView) {
    if (mWebView != null) {
      // 如果先调用destroy()方法，则会命中if (isDestroyed()) return;    这一行代码，需要先onDetachedFromWindow()，再
      // destory()
      ViewParent parent = mWebView.getParent();
      if (parent != null) {
        ((ViewGroup) parent).removeView(mWebView);
      }
      mWebView.stopLoading();
      // 退出时调用此方法，移除绑定的服务，否则某些特定系统会报错
      mWebView.getSettings().setJavaScriptEnabled(false);
      mWebView.clearHistory();
      mWebView.clearView();
      mWebView.removeAllViews();
      mWebView.destroy();
    }
  }

  private Params getHeaders() {

    Params p = new Params();
    p.a = "12";
    p.p = "1";
    p.t = "token123";
    p.u = "ffff-mmmm-ttt";
    p.v = "4030500";
    p.n = "4.3.5";
    p.m = "android";
    p.s = "29";
    p.h = "debug";
    return p;
  }

  private class Params {

    @JSONField(name = "t")
    public String t;//用户token
    @JSONField(name = "v")
    public String v;//应用版本号
    @JSONField(name = "p")
    public String p;//平台
    @JSONField(name = "u")
    public String u;//设备唯一标识
    @JSONField(name = "a")
    public String a;
    @JSONField(name = "n")
    public String n;
    @JSONField(name = "m")
    public String m;
    @JSONField(name = "s")
    public String s;
    @JSONField(name = "d")
    public String d;
    @JSONField(name = "c")
    public String c;
    @JSONField(name = "h")
    public String h;
  }

}
