package playground.instagram.android.androidinstagram;

import io.reactivex.Single;
import retrofit2.http.POST;

public interface InstagramAPI {

    String ENDPOINT = "https://api.instagram.com";

    @POST("/oauth/authorize/")
    Single<InstagramUser> authorize();

    @POST("/oauth/access_token")
    Single<InstagramUser> accessToken();
}
