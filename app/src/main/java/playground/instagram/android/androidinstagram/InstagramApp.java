package playground.instagram.android.androidinstagram;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ProgressBar;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;

public class InstagramApp {

    private InstagramSession mSession;
    private InstagramDialog mDialog;
    private OAuthAuthenticationListener mListener;
    private ProgressBar mProgressBar;
    private InstagramUser user;
    private Context mContext;

    private String mAuthUrl;
    private String mTokenUrl;
    private String mAccessToken = "";
    private String mClientId = "";
    private String mClientSecret = "";

    public static int WHAT_FINALIZE = 0;
    public static int WHAT_ERROR = 1;
    public static int WHAT_FETCH_INFO = 2;
    private String ERROR_INFO = "";

    // End points
    public static String mCallbackUrl = "";
    private static final String AUTH_URL = "https://api.instagram.com/oauth/authorize/";
    private static final String TOKEN_URL = "https://api.instagram.com/oauth/access_token";
    private static final String API_URL = "https://api.instagram.com/v1";

    // API Request Keys
    private static final String JSON_KEY_ACCESS_TOKEN = "access_token";

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
                Log.d(TAG, "Code: " + code);
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

    public InstagramUser getUser() {
        return user;
    }

    public boolean hasAccessToken() {
        return (mAccessToken == null) ? false : true;
    }

    public void setListener(OAuthAuthenticationListener listener) {
        mListener = listener;
    }

    public String getUserName() {
        return mSession.getUsername();
    }

    public String getId() {
        return mSession.getId();
    }

    public String getName() {
        return mSession.getName();
    }

    public String getToken() {
        return mSession.getAccessToken();
    }

    public void authorize() {
        mDialog.show();
    }

    public void resetAccessToken() {
        if (mAccessToken != null) {
            mSession.resetInstagramDetails();
            mAccessToken = null;
        }
    }

    private void getAccessToken(final String code) {
        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Getting access token");
                int what = WHAT_FETCH_INFO;

                try {
                    Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl(TOKEN_URL)
                            .build();

                    URL url = new URL(TOKEN_URL);
                    // URL url = new URL(mTokenUrl + "&code=" + code);
                    Log.i(TAG, "Opening Token URL " + url.toString());
                    HttpURLConnection urlConnection = (HttpURLConnection) url
                            .openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setDoInput(true);
                    urlConnection.setDoOutput(true);
                    // urlConnection.connect();
                    OutputStreamWriter writer = new OutputStreamWriter(
                            urlConnection.getOutputStream());

                    String writeParams = "client_id=" + mClientId + "&client_secret="
                            + mClientSecret + "&grant_type=authorization_code"
                            + "&redirect_uri=" + mCallbackUrl + "&code=" + code;

                    Log.i(TAG, writeParams);

                    writer.write(writeParams);
                    writer.flush();
                    String response = Utils.streamToString(urlConnection
                            .getInputStream());
                    Log.i(TAG, " getAccessToken _____ response " + response);
                    JSONObject jsonObj = (JSONObject) new JSONTokener(response)
                            .nextValue();

                    mAccessToken = jsonObj.getString("access_token");
                    Log.i(TAG, "Got access token: " + mAccessToken);

                    String id = jsonObj.getJSONObject("user").getString("id");
                    String user = jsonObj.getJSONObject("user").getString(
                            "username");
                    String name = jsonObj.getJSONObject("user").getString(
                            "full_name");

                    mSession.storeInstagramDetails(mAccessToken, id, user, name);


                    /*
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

                    mAccessToken = jsonObject.optString(JSON_KEY_ACCESS_TOKEN);
                    Log.i(TAG, "Got access token: " + mAccessToken);

                    user = InstagramJsonUtil.createBasicUser(jsonObject);

                    Log.d(TAG, "Username: + " + user.getUsername());

                    mSession.storeInstagramDetails(
                        mAccessToken,
                        user.getId(),
                        user.getUsername(),
                        user.getFullName()
                    ); */

                } catch (MalformedURLException e) {
                    what = WHAT_ERROR;
                    e.printStackTrace();
                    ERROR_INFO = e.toString();

                    Log.e(TAG, "MalformedURLException: " + e.toString());

                } catch (IOException e) {
                    what = WHAT_ERROR;
                    e.printStackTrace();
                    ERROR_INFO = e.toString();

                    Log.e(TAG, "IOException: " + e.toString());

                } catch (Exception e) {
                    what = WHAT_ERROR;
                    e.printStackTrace();
                    ERROR_INFO = e.toString();

                    Log.e(TAG, "Exception: " + e.toString());
                }

                mHandler.sendMessage(mHandler.obtainMessage(what, 1, 0));
            }
        }.start();
    }

    public void fetchUserName(final Handler handler) {
        new Thread() {
            @Override
            public void run() {
                Log.i(TAG, "Fetching user info");
                int what = WHAT_FINALIZE;

                try {
                    URL url = new URL(API_URL + "/users/" + mSession.getId() + "/?access_token=" + mAccessToken);
                    Log.d(TAG, "Opening URL " + url.toString());

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();

                    String response = Utils.streamToString(urlConnection
                            .getInputStream());
                    System.out.println(response);
                    JSONObject jsonObject = (JSONObject) new JSONTokener(response).nextValue();

                } catch (Exception ex) {
                    what = WHAT_ERROR;
                    ex.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * A Handler allows you to send and process Message and Runnable objects associated with a thread's MessageQueue.
     */
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            if (message.what == WHAT_ERROR) {
                if (message.arg1 == 1) {
                    mListener.onFail("Failed to get access token." + ERROR_INFO);
                } else if (message.arg2 == 2) {
                    mListener.onFail("Failed to get user information." + ERROR_INFO);
                }
            } else if (message.what == WHAT_FETCH_INFO) {
                mListener.onSuccess();
            }
        }
    };

    public interface OAuthAuthenticationListener {
        public abstract void onSuccess();

        public abstract void onFail(String error);
    }
}


