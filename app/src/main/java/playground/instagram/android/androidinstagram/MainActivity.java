package playground.instagram.android.androidinstagram;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mLayoutAfterLoginLayer;
    private Button mButtonInstagramLogin;
    private Button mButtonViewInformation;

    private InstagramApp mInstagramApp;
    private InstagramUser user;

    private static final String TAG = MainActivity.class.getName();

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            if (message.what == InstagramApp.WHAT_FINALIZE) {
                user = mInstagramApp.getUser();
            } else if (message.what == InstagramApp.WHAT_ERROR) {
                Toast.makeText(MainActivity.this, "Check your network", Toast.LENGTH_LONG).show();
            }

            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInstagramApp = new InstagramApp(this,
            AppConfig.CLIENT_ID,
            AppConfig.CLIENT_SECRET,
            AppConfig.CALLBACK_URL
        );
        mInstagramApp.setListener(new InstagramApp.OAuthAuthenticationListener() {
            @Override
            public void onSuccess() {
                mLayoutAfterLoginLayer.setVisibility(View.VISIBLE);
                mButtonInstagramLogin.setVisibility(View.GONE);
                mInstagramApp.fetchUserName(handler);
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
            }
        });

        setupUI();
    }

    private void setupUI() {
        mLayoutAfterLoginLayer = (RelativeLayout) findViewById(R.id.layoutAfterLoginLayer);

        // Instagram login
        mButtonInstagramLogin = (Button) findViewById(R.id.buttonInstagramLogin);
        mButtonInstagramLogin.setOnClickListener(this);

        // View information
        mButtonViewInformation = (Button) findViewById(R.id.buttonViewInformation);
        mButtonViewInformation.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonInstagramLogin:
                mInstagramApp.authorize();
                break;
            case R.id.buttonViewInformation:
                displayInstagramInfo();
                break;
        }
    }

    private void displayInstagramInfo() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (RelativeLayout) inflater.inflate(R.layout.profile_view, null);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Profile info");
        dialog.setView(view);

        TextView textViewUsername = (TextView) view.findViewById(R.id.textViewValueUsername);
        // textViewUsername.setText(user.getUsername());

        TextView textViewFollowers = (TextView) view.findViewById(R.id.textViewLabelFollowers);
        //textViewFollowers.setText(user.getFollowers());

        TextView textViewFollowing = (TextView) view.findViewById(R.id.textViewValueFollowing);
        // textViewFollowing.setText(user.getFollowedBy());

        Toast.makeText(MainActivity.this, "Display user data", Toast.LENGTH_LONG).show();

        dialog.create().show();
    }
}
