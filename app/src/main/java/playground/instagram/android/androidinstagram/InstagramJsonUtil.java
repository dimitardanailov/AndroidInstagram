package playground.instagram.android.androidinstagram;

import org.json.JSONException;
import org.json.JSONObject;

public class InstagramJsonUtil {

    private static final String JSON_KEY_ACCESS_TOKEN = "access_token";
    private static final String JSON_KEY_ACCESS_USER = "user";
    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_USERNAME = "username";
    private static final String JSON_KEY_FULL_NAME = "full_name";
    private static final String JSON_KEY_DATA = "data";
    private static final String JSON_KEY_PROFILE_PICTURE = "profile_picture";
    private static final String JSON_KEY_BIO = "bio";
    private static final String JSON_KEY_WEBSITE = "website";
    private static final String JSON_KEY_COUNTS = "counts";
    private static final String JSON_KEY_FOLLOWS = "follows";
    private static final String JSON_KEY_FOLLOWED_BY = "";
    private static final String JSON_KEY_MEDIA = "media";
    private static final String JSON_KEY_META = "meta";
    private static final String JSON_KEY_CODE = "code";

    public static InstagramUser createBasicUser(JSONObject jsonObject) {
        InstagramUser instagramUser = new InstagramUser();

        if (jsonObject.has(JSON_KEY_ACCESS_USER)) {
            try {
                JSONObject jsonUser = jsonObject.getJSONObject(JSON_KEY_ACCESS_USER);

                instagramUser.setId(jsonUser.optString(JSON_KEY_ID));
                instagramUser.setUsername(jsonUser.optString(JSON_KEY_USERNAME));
                instagramUser.setFullName(jsonUser.optString(JSON_KEY_FULL_NAME));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return instagramUser;
    }

    public static InstagramUser createUser(JSONObject object) {
        InstagramUser instagramUser = new InstagramUser();

        return instagramUser;
    }
}
