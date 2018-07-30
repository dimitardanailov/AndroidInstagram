package playground.instagram.android.androidinstagram;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mLayoutAfterLoginLayer;
    private Button mButtonInstagramLogin;
    private Button mButtonViewInformation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                initializeLoginThroughInstagram();
                break;
            case R.id.buttonViewInformation:
                displayInstragramData();
                break;
        }
    }

    private void initializeLoginThroughInstagram() {

    }

    private void displayInstragramData() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = (RelativeLayout) inflater.inflate(R.layout.profile_view, null);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Profile info");
        dialog.setView(view);

        TextView textViewUsername = (TextView) view.findViewById(R.id.textViewValueUsername);
        TextView textViewFollowers = (TextView) view.findViewById(R.id.textViewLabelFollowers);
        TextView textViewFollowing = (TextView) view.findViewById(R.id.textViewValueFollowing);

    }
}
