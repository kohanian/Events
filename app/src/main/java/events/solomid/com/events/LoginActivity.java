package events.solomid.com.events;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class LoginActivity extends Activity {
    private CallbackManager callbackManager;
    TextView title;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (AccessToken.getCurrentAccessToken() != null) {
            go_to_home();
        }

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        go_to_account();
                    }

                    @Override
                    public void onCancel() {
                        //go to normal page
                    }

                    @Override
                    public void onError(FacebookException error) {
                        //go to error page
                    }
                });

        Button fb_login_button = (Button) findViewById(R.id.fb_login_button);

        title = (TextView)findViewById(R.id.textView);
        Typeface typeface= Typeface.createFromAsset(getAssets(),"fonts/Orbitron-Regular.ttf");
        title.setTypeface(typeface);

        fb_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this,
                        Arrays.asList("public_profile", "user_friends", "user_events"));
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppEventsLogger.deactivateApp(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppEventsLogger.activateApp(this);
    }

    private void go_to_home() {
        Intent intent = new Intent(this, EventListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
//        startService(new Intent(this, LocationService.class));
    }

    private void go_to_account() {
        Intent intent = new Intent(LoginActivity.this, account_info.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
        //startService(new Intent(this, LocationService.class));
    }
}
