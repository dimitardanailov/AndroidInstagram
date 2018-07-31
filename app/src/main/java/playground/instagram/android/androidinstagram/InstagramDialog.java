package playground.instagram.android.androidinstagram;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InstagramDialog extends Dialog {

    static final float[] DIMENSIONS_LANDSCAPE = { 460, 260 };
    static final float[] DIMENSIONS_PORTRAIT = { 280, 420 };
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
    );
    static final int MARGIN = 4;
    static final int PADDING = 2;

    private String mUrl;
    private OAuthDialogListener mListener;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private RelativeLayout mContent;
    private TextView mTitle;

    private static final String TAG = InstagramDialog.class.getName();

    public InstagramDialog(Context context, String url,
                           OAuthDialogListener listener) {
        super(context);
        mUrl = url;
        mListener = listener;
    }



    private class OAuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // if (url.startsWith(Inst))
        }
    }


}

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Log.d(TAG, "Redirecting URL " + url);

        if (url.startsWith(InstagramApp.mCallbackUrl)) {
            String urls[] = url.split("=");
            mListener.onComplete(urls[1]);
            InstagramDialog.this.dismiss();
            return true;
        }
        return false;
    }

    private class OAuthWebViewClient extends WebViewClient {

    }

    public interface OAuthDialogListener {
        public abstract void onComplete(String accessToken);
        public abstract void onError(String error);
    }
}