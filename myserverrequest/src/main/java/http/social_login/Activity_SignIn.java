package http.social_login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Activity_SignIn extends Activity {


    String email = "";
    String fb_token, fb_id, last_name, first_name, picture;
    CallbackManager callbackManager;
    public static Callback calla;

    public static void startActivity(Context context, String paramA, String paramB, Callback call) {
        calla = call;
        Intent intent = new Intent(context, Activity_SignIn.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(this.getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {

                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        final LoginDataClass dataClass = new LoginDataClass();

                        //  Toast.makeText(Activity_SignIn.this, "FaceBook login Success", Toast.LENGTH_LONG).show();


                        Log.d("onSuccess", "onSuccess");

                        fb_token = AccessToken.getCurrentAccessToken().getToken();
                        dataClass.fbtoken = fb_token;
                        Profile.fetchProfileForCurrentAccessToken();

                        Log.d("fb_token==", "" + fb_token + "fb_id===" + fb_id);

                        GraphRequest request = GraphRequest.newMeRequest(
                                AccessToken.getCurrentAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {

                                    @Override
                                    public void onCompleted(JSONObject object,
                                                            GraphResponse response) {
                                        // TODO Auto-generated method stub
                                        try {
                                            Log.e("response",
                                                    "" + response.toString());
                                            if (object.has("email")) {
                                                email = object.getString("email");
                                                dataClass.useremail = email;
                                            }
                                            if (object.has("id")) {
                                                fb_id = object.getString("id");
                                                dataClass.fbid = fb_id;
                                            }
                                            if (object.has("first_name")) {
                                                first_name = object.getString("first_name");
                                                dataClass.fusername = first_name;
                                            }
                                            if (object.has("last_name")) {
                                                last_name = object.getString("last_name");
                                            }
                                            if (object.has("picture")) {

                                                picture = object.getJSONObject("picture").getJSONObject("data").getString("url");
                                                dataClass.image_path = picture;
                                                Log.e("LOG : picture", picture);
                                            }


                                            calla.onSuccesss(dataClass);

                                        } catch (JSONException je) {
                                            je.printStackTrace();
                                        }


                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,first_name,link,email,picture,last_name");
                        request.setParameters(parameters);
                        request.executeAsync();


                        finish();

                    }

                    @Override
                    public void onCancel() {
                        // App code
                        Log.d("onCancel", "onCancel");
                        LoginManager.getInstance().logOut();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code gggg
                        exception.printStackTrace();
                        LoginManager.getInstance().logOut();
                        Log.d("onError", "onError");
                    }


                });


        LoginManager.getInstance().logOut();

        LoginManager.getInstance().logInWithPublishPermissions(Activity_SignIn.this,
                Arrays.asList("publish_actions,public_profile,email"));

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
