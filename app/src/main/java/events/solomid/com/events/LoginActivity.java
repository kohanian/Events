package events.solomid.com.events;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

public class LoginActivity extends Activity {
    private CallbackManager callbackManager;

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

        LoginButton fb_login_button = (LoginButton) findViewById(R.id.facebook_login_button);
        //set user permissions

        fb_login_button.setReadPermissions("user_friends");
        fb_login_button.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                go_to_home();
            }

            @Override
            public void onCancel() {
                //stay on this page
            }

            @Override
            public void onError(FacebookException error) {
                //do something
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
        Intent intent = new Intent(this, TestLoginActivity.class);
        startActivity(intent);
        startService(new Intent(this, LocationService.class));
    }
}
