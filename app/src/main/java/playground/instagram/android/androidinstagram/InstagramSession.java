package playground.instagram.android.androidinstagram;

import android.content.Context;
import android.content.SharedPreferences;

public class InstagramSession {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private static final String SHARED = "Instagram_Preferences";
    private static final String API_USERNAME = "username";
    private static final String API_ID = "id";
    private static final String API_NAME = "name";
    private static final String API_ACCESS_TOKEN = "access_token";

    public InstagramSession(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void storeInstagramDetails(String accessToken, String id, String username, String name) {
        editor.putString(API_ID, id);
        editor.putString(API_NAME, name);
        editor.putString(API_ACCESS_TOKEN, accessToken);
        editor.putString(API_USERNAME, username);

        editor.commit();
    }

    public void storeAccessToken(String accessToken) {
        editor.putString(API_ACCESS_TOKEN, accessToken);

        editor.commit();
    }

    /**
     * Reset access token and username
     */
    public void resetInstagramDetails() {
        editor.putString(API_ID, null);
        editor.putString(API_NAME, null);
        editor.putString(API_ACCESS_TOKEN, null);
        editor.putString(API_USERNAME, null);

        editor.commit();
    }

    /**** Getters ****/

    public String getUsername() {
        return sharedPreferences.getString(API_USERNAME, null);
    }

    public String getId() {
        return sharedPreferences.getString(API_ID, null);
    }

    public String getName() {
        return sharedPreferences.getString(API_NAME, null);
    }

    public String getAccessToken() {
        return sharedPreferences.getString(API_ACCESS_TOKEN, null);
    }
}
