package http.social_login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myserverrequest.R;
import com.facebook.CallbackManager;


import java.util.HashMap;

import http.social_login.instagram.ApplicationData;
import http.social_login.instagram.InstagramApp;


public class InstaGramLogin extends Activity {

    private static Context mContext;
    InstagramApp mApp;
    CallbackManager callbackManager;
    public static Callback calla;

    HashMap<String, String> userInfoHashmap = new HashMap<String, String>();
    private Handler handler = new Handler(new Handler.Callback() {

        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == InstagramApp.WHAT_FINALIZE) {
                userInfoHashmap = mApp.getUserInfo();
            } else if (msg.what == InstagramApp.WHAT_FINALIZE) {
                Toast.makeText(InstaGramLogin.this, "Check your network.",
                        Toast.LENGTH_SHORT).show();
            }
            return false;
        }
    });


    public static void startactivity(Context context, Callback call) {
        calla = call;
        mContext = context;
        Intent i = new Intent(mContext, InstaGramLogin.class);
        mContext.startActivity(i);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.sociallogin);

        callbackManager = CallbackManager.Factory.create();

        mApp = new InstagramApp(this, ApplicationData.CLIENT_ID,
                ApplicationData.CLIENT_SECRET, ApplicationData.CALLBACK_URL);

        final LoginDataClass dataClass = new LoginDataClass();

        mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

            @Override
            public void onSuccess() {
                // tvSummary.setText("Connected as " + mApp.getUserName());
                //  btnConnect.setText("Disconnect");
                // userInfoHashmap = mApp.


                dataClass.instatoken = mApp.getTOken();

                Log.e("LOG :instatoken ", dataClass.instatoken + "");

                dataClass.instaid = mApp.getId();

                Log.e("LOG :instaid ", dataClass.instaid + "");

                dataClass.insta_namen = mApp.getUserName();

                Log.e("LOG :insta_namen ", dataClass.insta_namen + "");

                calla.onSuccesss(dataClass);

                //   Toast.makeText(InstaGramLogin.this, mApp.getTOken() + mApp.getId() + mApp.getUserName() + "Instagram login success", Toast.LENGTH_LONG).show();
                // mApp.fetchUserName(handler);
                finish();
            }

            @Override
            public void onFail(String error) {
                Toast.makeText(InstaGramLogin.this, error, Toast.LENGTH_SHORT)
                        .show();
            }
        });

        mApp.authorize();

        if (mApp.hasAccessToken()) {

            dataClass.insta_profile = InstagramApp.TAG_PROFILE_PICTURE;
            Log.e("LOG :profile 111", dataClass.insta_profile + "");
            Log.e("LOG :profile 222", InstagramApp.TAG_PROFILE_PICTURE + "");


/*            tvSummary.setText("Connected as " + mApp.getUserName());
            //  btnConnect.setText("Disconnect");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                    InstaGramLogin.this);
            alertDialog.setTitle("Profile Info");

            Toast.makeText(InstaGramLogin.this, "Clicked", Toast.LENGTH_SHORT);
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.profile_view, null);

            alertDialog.setView(view);
            ImageView ivProfile = (ImageView) view
                    .findViewById(R.id.ivProfileImage);

            TextView tvName = (TextView) view.findViewById(R.id.tvUserName);

            TextView tvNoOfFollwers = (TextView) view
                    .findViewById(R.id.tvNoOfFollowers);

            TextView tvNoOfFollowing = (TextView) view
                    .findViewById(R.id.tvNoOfFollowing);

            new ImageLoader(MainActivity.this).DisplayImage(
                    userInfoHashmap.get(InstagramApp.TAG_PROFILE_PICTURE),
                    ivProfile);

            tvName.setText(userInfoHashmap.get(InstagramApp.TAG_USERNAME));

            Toast.makeText(MainActivity.this, " TAG_USERNAME " + InstagramApp.TAG_USERNAME, Toast.LENGTH_SHORT).show();

            Toast.makeText(MainActivity.this, " TAG_ID " + InstagramApp.TAG_ID, Toast.LENGTH_SHORT).show();

            tvNoOfFollowing.setText(userInfoHashmap.get(InstagramApp.TAG_FOLLOWS));
            tvNoOfFollwers.setText(userInfoHashmap
                    .get(InstagramApp.TAG_FOLLOWED_BY));
            alertDialog.create().show();*/


            mApp.fetchUserName(handler);

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

}
