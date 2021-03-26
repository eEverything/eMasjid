//package http.social_login;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.Signature;
//import android.os.Bundle;
//import android.util.Log;
//
//import com.example.myserverrequest.R;
//import com.linkedin.platform.APIHelper;
//import com.linkedin.platform.errors.LIApiError;
//import com.linkedin.platform.listeners.ApiListener;
//import com.linkedin.platform.listeners.ApiResponse;
//import com.linkedin.platform.utils.Scope;
//
//import org.json.JSONObject;
//
//import java.security.MessageDigest;
//import java.security.NoSuchAlgorithmException;
//
//import static android.R.attr.host;
//
//
//public class Activity_linkedIn extends Activity {
//    //replace package string with your package string
//    public static final String PACKAGE = "com.linkingapp";
//    private static final String url = "https://" + host + "/v1/people/~:" + "(email-address,formatted-name,phone-numbers,picture-url)";
//   public static Callback calla;
//    public static void startActivity(Context context, String paramA, String paramB,Callback call) {
//        calla=call;
//        // Create and start intent for this activity
//        Intent intent = new Intent(context, Activity_linkedIn.class);
//        context.startActivity(intent);
//
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//
//        super.onCreate(savedInstanceState);
//        // setContentView(R.layout.activity_main);
//
//        generateHashkey();
//
//
//        linkededinApiHelper();
//
//    }
//
//    public void linkededinApiHelper() {
//        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
//        apiHelper.getRequest(Activity_linkedIn.this, url, new ApiListener() {
//            @Override
//            public void onApiSuccess(ApiResponse result) {
//                try {
//                    showResult(result.getResponseDataAsJson());
//                    // progress.dismiss();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onApiError(LIApiError error) {
//
//            }
//        });
//    }
//
//    public void showResult(JSONObject response) {
//
//        try {
//            /*user_email.setText(response.get("emailAddress").toString());
//            user_name.setText(response.get("formattedName").toString());
//
//            Picasso.with(this).load(response.getString("pictureUrl"))
//                    .into(profile_picture);*/
//            Log.e("Pic url", response.getString("pictureUrl"));
//            final LoginDataClass dataClass = new LoginDataClass();
//            calla.onSuccesss(dataClass);
//          /*  user_email.setText(femail);
//            user_name.setText(funame);
//
//            Picasso.with(this).load(fpic)
//                    .into(profile_picture);
//            Log.e("Pic url", response.getString("pictureUrl"));*/
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /*public void login() {
//        LISessionManager.getInstance(getApplicationContext())
//                .init(this, buildScope(), new AuthListener() {
//                    @Override
//                    public void onAuthSuccess() {
//
//                        Toast.makeText(getApplicationContext(), "success" +
//                                        LISessionManager.getInstance(getApplicationContext())
//                                                .getSession().getAccessToken().toString(),
//                                Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onAuthError(LIAuthError error) {
//                        Toast.makeText(getApplicationContext(), "failed "
//                                        + error.toString(),
//                                Toast.LENGTH_LONG).show();
//                    }
//
//                }, true);
//    }*/
//
//    public void generateHashkey() {
//        try {
//            PackageInfo info = getPackageManager().getPackageInfo(
//                    PACKAGE,
//                    PackageManager.GET_SIGNATURES);
//            for (Signature signature : info.signatures) {
//                MessageDigest md = MessageDigest.getInstance("SHA");
//                md.update(signature.toByteArray());
//
//               /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
//                    ((TextView) findViewById(R.id.handleSignInResulthKey))
//                            .setText(Base64.encodeToString(md.digest(),
//                                    Base64.NO_WRAP));
//                }*/
//            }
//        } catch (PackageManager.NameNotFoundException e) {
//            Log.d("Name not found", e.getMessage(), e);
//
//        } catch (NoSuchAlgorithmException e) {
//            Log.d("Error", e.getMessage(), e);
//        }
//    }
//
//
//    // set the permission to retrieve basic -
//    //information of User's linkedIn account
//    private static Scope buildScope() {
//        return Scope.build(Scope.R_BASICPROFILE,
//                Scope.R_EMAILADDRESS);
//    }
//}
