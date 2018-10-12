package com.loksarkar.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.loksarkar.R;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.base.BaseApp;
import com.loksarkar.constants.AppConstants;
import com.loksarkar.listener.OnProgressCompleteListener;
import com.loksarkar.localeutils.LocaleChanger;
import com.loksarkar.models.LoginModel;
import com.loksarkar.models.RegistrationModel;
import com.loksarkar.ui.GenerateProcessButton;
import com.loksarkar.ui.RotateLoaderDialog;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.PrefUtils;
import com.loksarkar.utils.ProgressGenerator;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.loksarkar.constants.AppConstants.NOTIFICATION_MSG;

public class SigninActivity extends AppCompatActivity implements OnProgressCompleteListener{

    private ProgressGenerator progressGenerator;
    private GenerateProcessButton loginBtn;
    private AppCompatEditText etUsername,etPassword;
    private RotateLoaderDialog rotateLoaderDialog;
    private Context mContext;
    private String typeId = "";
    private String firebase_RegId = "";
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = this;
        rotateLoaderDialog = new RotateLoaderDialog(this);
        try{
            typeId = getIntent().getStringExtra("blog_id");
        }catch (Exception ex){
            ex.getLocalizedMessage();
        }
        //progressGenerator = new ProgressGenerator(this);
        loginBtn = (GenerateProcessButton) findViewById(R.id.btnLogin);
        loginBtn.setBackgroundColor(ContextCompat.getColor(this,R.color.btn_color));
        etUsername = (AppCompatEditText) findViewById(R.id.et_username);
        etPassword = (AppCompatEditText) findViewById(R.id.et_pswd);


        findViewById(R.id.tv_create_acount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(2));
                startActivity(new Intent(SigninActivity.this,SignupActivity.class));
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validateLogin()) {

                    //progressGenerator.start(loginBtn);
                    loginBtn.setEnabled(false);
                    etUsername.setEnabled(false);
                    etPassword.setEnabled(false);
                    loginBtn.setText("Loading..");

                    callLoginApi();

                }
            }
        });

        try {
            pref = getApplicationContext().getSharedPreferences(PrefUtils.SHARED_PREF, 0);
            firebase_RegId = pref.getString("regId","");

        }catch (Exception ex){
            ex.printStackTrace();
        }


//        Intent intentDashboard = new Intent(SigninActivity.this,DashBoardActivity.class);
//        if(typeId != null) {
//            intentDashboard.putExtra("blog_id", typeId);
//        }
//        intentDashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intentDashboard);
//        finish();

    }

    @Override
    public void onComplete() {


//        Intent intentDashboard = new Intent(SigninActivity.this,DashBoardActivity.class);
//        intentDashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intentDashboard);
//        finish();

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onResume(){
        super.onResume();
        try {
            if(NOTIFICATION_MSG != null){

                if(!TextUtils.isEmpty(NOTIFICATION_MSG)){

                    AppUtils.notify(SigninActivity.this,getString(R.string.success_msg),NOTIFICATION_MSG,R.color.material_light_green,ContextCompat.getDrawable(SigninActivity.this,R.drawable.ic_check));
                    NOTIFICATION_MSG = "";


                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    private boolean validateLogin(){

        if(etUsername.getText().toString().length() <= 0 || etPassword.getText().toString().length() <= 0){
            etUsername.setError("Required");
            etPassword.setError("Required");
            AppUtils.notify(SigninActivity.this,"Error","Userid and Password required",R.color.error_color,ContextCompat.getDrawable(SigninActivity.this,R.drawable.ic_error));
            return false;
        }
        return true;
    }


    private void callLoginApi() {

        if (!AppUtils.isNetworkConnected(SigninActivity.this)) {
            AppUtils.notify(SigninActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }
        rotateLoaderDialog.showLoader();
//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().signInUser(setUserDetails(),new retrofit.Callback<LoginModel>() {
            @Override
            public void success(LoginModel loginypeModel, Response response) {
                AppUtils.dismissDialog();
                if (loginypeModel == null) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    loginBtn.setEnabled(true);
                    etUsername.setEnabled(true);
                    etPassword.setEnabled(true);
                    loginBtn.setText(getString(R.string.login));
                    return;
                }
                if (loginypeModel.getSuccess() == null) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    loginBtn.setText(getString(R.string.login));
                    loginBtn.setEnabled(true);
                    etUsername.setEnabled(true);
                    etPassword.setEnabled(true);
                    return;
                }
                if (loginypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    rotateLoaderDialog.dismissLoader();
                    loginBtn.setText(getString(R.string.login));
                    loginBtn.setEnabled(true);
                    etUsername.setEnabled(true);
                    etPassword.setEnabled(true);
                    AppUtils.notify(SigninActivity.this,"Error","Userid and Password Not Match",R.color.error_color,ContextCompat.getDrawable(SigninActivity.this,R.drawable.ic_error));


                    return;
                }
                if (loginypeModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();
                    loginBtn.setText(getString(R.string.login));
                    loginBtn.setEnabled(true);
                    etUsername.setEnabled(true);
                    etPassword.setEnabled(true);
                    //sto
                    PrefUtils.getInstance(SigninActivity.this).setValue(PrefUtils.KEY_USERID,etUsername.getText().toString());

                    PrefUtils.getInstance(SigninActivity.this).setUserLogin();

                    PrefUtils.getInstance(SigninActivity.this).setValue(PrefUtils.REFERRAL_ID_KEY,loginypeModel.getFinalAry().get(0).getReferralID());
                    PrefUtils.getInstance(SigninActivity.this).setValue(PrefUtils.EMAIL_KEY,loginypeModel.getFinalAry().get(0).getEmailAddress());
                    PrefUtils.getInstance(SigninActivity.this).setValue(PrefUtils.MOB_KEY,loginypeModel.getFinalAry().get(0).getMobileNo());
                    PrefUtils.getInstance(SigninActivity.this).setValue(PrefUtils.USERNAME_KEY,loginypeModel.getFinalAry().get(0).getName());
                    PrefUtils.getInstance(SigninActivity.this).setValue(PrefUtils.ADDRESS_KEY,loginypeModel.getFinalAry().get(0).getAddress());

                    //change locale of app.
                    //LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(0));

                    Intent intentDashboard = new Intent(SigninActivity.this,DashBoardActivity.class);
                    intentDashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intentDashboard);
                    finish();
                }



            }
            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                loginBtn.setText(getString(R.string.login));
                loginBtn.setEnabled(true);
                etUsername.setEnabled(true);
                etPassword.setEnabled(true);
                error.getMessage();
                rotateLoaderDialog.dismissLoader();

                AppUtils.ping(mContext, getString(R.string.something_wrong));
            }
        });


    }

    private Map<String, String> setUserDetails() {
        Map<String, String> map = new HashMap<>();
        map.put("UserID",etUsername.getText().toString());
        map.put("Password",etPassword.getText().toString());
//        if(firebase_RegId != null){
//            map.put("DeviceToken",firebase_RegId);
//
//        }else{
//            map.put("DeviceToken","");
//        }
        return map;
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        //LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(0));
    }


}
