package playground.instagram.android.androidinstagram;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;


import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class InstagramApp {

    private InstagramSession mSession;
    private InstagramDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressBar mProgressBar;
    private HashMap<String, String> userInfo = new HashMap<String, String>();
    private Context mContext;

    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken;
    private String mClientId;
    private String mClientSecret;

    private static int WHAT_FINALIZE = 0;
    private static int WHAT_ERROR = 1;
    private static int WHAT_FETCH_INFO = 2;

    // End points
    private static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";

    // API Request Keys
    private static final String TAG_ID = "id";
    private static final String TAG_USERNAME = "username";
    private static final String TAG_DATA = "data";
    private static final String TAG_PROFILE_PICTURE = "";
    private static final String TAG_BIO = "bio";
    private static final String TAG_WEBSITE = "website";
    private static final String TAG_COUNTS = "counts";
    private static final String TAG_FOLLOWS = "follows";
    private static final String TAG_FOLLOWED_BY = "";
    private static final String TAG_MEDIA = "media";
    private static final String TAG_FULL_NAME = "full_name";
    private static final String TAG_META = "meta";
    private static final String TAG_CODE = "code";

    private static final String TAG = InstagramApp.class.getName();

    public InstagramApp(Context context, String clientId, String clientSecret,
                        String callbackUrl) {
        mClientId = clientId;
        mClientSecret = clientSecret;
        mContext = context;

        mSession = new InstagramSession(context);
        mAccessToken = mSession.getAccessToken();
        mCallbackUrl = callbackUrl;

        mTokenUrl = TOKEN_URL + "?client_id=" + clientId + "&client_secret="
                + clientSecret + "&redirect_uri=" + mCallbackUrl
                + "&grant_type=authorization_code";

        mAuthUrl = AUTH_URL
                + "?client_id="
                + clientId
                + "&redirect_uri="
                + mCallbackUrl
                + "&response_type=code&display=touch&scope=likes+comments+relationships";

        InstagramDialog.OAuthDialogListener listener = new InstagramDialog.OAuthDialogListener() {
            @Override
            public void onComplete(String code) {
                getAccessToken(code);
            }

            @Override
            public void onError(String error) {
                mListener.onFail("Authorization failed");
            }
        };

        mDialog = new InstagramDialog(context, mAuthUrl, listener);
        mProgressBar = new ProgressBar(context);

        ////// mProgress.setCancelable(false);
    }

    private void getAccessToken(final String code) {
        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int what = WHAT_FETCH_INFO;

                try {
                    URL url = new URL(TOKEN_URL);
                    Log.i(TAG, "Opening Token URL " + url.toString());
                    HttpURLConnection urlConnection =
                            (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);

                    OutputStreamWriter writer =
                            new OutputStreamWriter(urlConnection.getOutputStream());
                    writer.write("client_id=" + mClientId + "&client_secret="
                            + mClientSecret + "&grant_type=authorization_code"
                            + "&redirect_uri=" + mCallbackUrl + "&code=" + code);
                    writer.flush();

                    String response = Utils.streamToString();

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
