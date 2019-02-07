package com.loksarkar.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;

import com.loksarkar.R;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.listener.OnAlertNotificationClick;
import com.loksarkar.models.OTPModel;
import com.loksarkar.models.RegistrationModel;
import com.loksarkar.models.RegistrationTypeModel;
import com.loksarkar.ui.MultiLineRadioGroup;
import com.loksarkar.ui.RotateLoaderDialog;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.DialogUtils;
import com.tapadoo.alerter.Alerter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MediaRegistration extends BaseActivity implements OnAlertNotificationClick {


    private MultiLineRadioGroup multiLineRadioGroup, multiLineRadioGroup1;
    private Context mContext;
    private List<RegistrationTypeModel> finalArraydistrictTypeList;


    private EditText input_name, input_address, input_mobnum, input_email, input_dob, input_fb_link, input_twitter_link, input_media_house, input_website_link;
    private String finalIdentityproofStr = "", finalcategoryTypeStr = "Other", finalmediatypeTypeStr = "National Representative";
    private AppCompatButton mBtnSignUp;
    private String REC_OTP = "", day = "", month = "", year = "", finaldistrictIdStr = "", districtName = "";
    private DatePickerDialog.OnDateSetListener bdate;
    private Calendar myCalendar = Calendar.getInstance();
    private RotateLoaderDialog rotateLoaderDialog;
    private Dialog OTPdialog;
    private OnAlertNotificationClick onAlertNotificationClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_registartion);

        onAlertNotificationClick = (OnAlertNotificationClick) this;
        mContext = this;
        input_name = (EditText) findViewById(R.id.input_name);
        input_address = (EditText) findViewById(R.id.input_address);
        input_mobnum = (EditText) findViewById(R.id.input_mobnum);
        input_email = (EditText) findViewById(R.id.input_email);
        input_dob = (EditText) findViewById(R.id.input_dob1);
        input_fb_link = (EditText) findViewById(R.id.input_fb_link);
        input_twitter_link = (EditText) findViewById(R.id.input_twitter_link);
        input_media_house = (EditText) findViewById(R.id.input_media_housename);
        input_website_link = (EditText) findViewById(R.id.input_web_link);
        multiLineRadioGroup = (MultiLineRadioGroup) findViewById(R.id.rg_options);
        multiLineRadioGroup1 = (MultiLineRadioGroup) findViewById(R.id.rg_options1);

        mBtnSignUp = (AppCompatButton) findViewById(R.id.btn_submit);

        rotateLoaderDialog = new RotateLoaderDialog(this);


        bdate = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                fillDobEditext();
            }

        };

        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateForum()) {
                    callOTPApi(input_mobnum.getText().toString(), input_email.getText().toString());

                }
            }
        });

        multiLineRadioGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ViewGroup group, RadioButton button) {
                finalcategoryTypeStr = button.getText().toString();
            }
        });

        multiLineRadioGroup1.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ViewGroup group, RadioButton button) {
                finalmediatypeTypeStr = button.getText().toString();
            }
        });

        input_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MediaRegistration.this, bdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }


    private void fillDobEditext() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        input_dob.setText(sdf.format(myCalendar.getTime()));

        String dob = input_dob.getText().toString();

        String[] split = dob.split("/");

        day = split[0];
        month = split[1];
        year = split[2];

    }

    private void callMediaSignUpApi() {

        if (!AppUtils.isNetworkConnected(MediaRegistration.this)) {
            AppUtils.notify(MediaRegistration.this, getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }
        rotateLoaderDialog.showLoader();

        ApiHandler.getApiService().mediaSignUpNew(setUserDetails(), new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel registrationModel, Response response) {
                AppUtils.dismissDialog();
                if (registrationModel == null) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (registrationModel.getSuccess() == null) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (registrationModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    AppUtils.ping(mContext, getString(R.string.something_wrong));

                    rotateLoaderDialog.dismissLoader();

                    return;
                }
                if (registrationModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();

                    // AppUtils.notify(MediaRegistration.this,getString(R.string.success_msg),registrationModel.getMessage(),R.color.material_light_green,ContextCompat.getDrawable(MediaRegistration.this,R.drawable.ic_check),20000,onAlertNotificationClick);
                    DialogUtils.showMessageDialog(MediaRegistration.this, R.drawable.ic_check, getString(R.string.success_msg), registrationModel.getMessage(), onAlertNotificationClick);

                    //LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(0));

//                    Intent intentDashboard = new Intent(MediaRegistration.this,DashBoardActivity.class);
//                    intentDashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intentDashboard);s
//                    finish();

                }
            }

            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                rotateLoaderDialog.dismissLoader();
                AppUtils.ping(mContext, getString(R.string.something_wrong));
            }
        });


    }

    private Map<String, String> setUserDetails() {
        Map<String, String> map = new HashMap<>();
        map.put("Name", input_name.getText().toString());
        map.put("Address", input_address.getText().toString());
        map.put("MobileNo", input_mobnum.getText().toString());
        map.put("EmailAddress", input_email.getText().toString());
        map.put("DateofBirth", input_dob.getText().toString());

        if (input_fb_link.getText().toString().length() > 0) {
            map.put("FBLink", input_fb_link.getText().toString());
        } else {
            map.put("FBLink", "");

        }
        if (input_twitter_link.getText().toString().length() > 0) {
            map.put("TwitterLink", input_twitter_link.getText().toString());
        } else {
            map.put("TwitterLink", "");
        }
        map.put("MediaType", finalcategoryTypeStr);
        map.put("Category", finalcategoryTypeStr);
        map.put("MediaHouseName", finalcategoryTypeStr);

        if (input_website_link.getText().toString().length() > 0) {
            map.put("WebsiteLink", input_website_link.getText().toString());

        } else {
            map.put("WebsiteLink", "");

        }
        return map;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Alerter.clearCurrent(MediaRegistration.this);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(0));
    }


    private boolean validateForum() {
        if (input_name.getText().toString().length() <= 0) {
            //   AppUtils.ping(mContext,"B");
            input_name.setError("Please enter name");
            input_name.requestFocus();
            return false;
        } else if (input_address.getText().toString().length() <= 0) {
            //   AppUtils.ping(mContext,"B");
            input_address.setError("Please enter address");
            input_address.requestFocus();
            return false;
        } else if (input_mobnum.getText().toString().length() < 10) {
            //   AppUtils.ping(mContext,"B");
            input_mobnum.setError("Please enter valid phone no");
            input_mobnum.requestFocus();
            return false;
        } else if (input_email.getText().toString().length() <= 0) {
            //   AppUtils.ping(mContext,"B");
            input_email.setError("Please enter email address.");
            input_email.requestFocus();
            return false;
        } else if (!AppUtils.isValidEmail(input_email.getText().toString())) {
            //   AppUtils.ping(mContext,"B");
            input_email.setError("Please enter valid email address.");
            input_email.requestFocus();
            return false;
        } else if (input_dob.getText().toString().length() <= 0) {
            //   AppUtils.ping(mContext,"B");
            input_dob.setError("Please enter DOB.");
            input_dob.requestFocus();
            return false;
        } else if (finalcategoryTypeStr.length() <= 0 || finalcategoryTypeStr.equals("")) {
            AppUtils.ping(MediaRegistration.this, "Please select category");
            return false;

        } else if (input_media_house.getText().toString().length() <= 0) {
            input_media_house.setError("Please enter media house name.");
            input_media_house.requestFocus();
            return false;

        } else {
            return true;
        }
    }

    private void callOTPApi(String mobileNo, String email) {

        if (!AppUtils.isNetworkConnected(MediaRegistration.this)) {
            AppUtils.notify(MediaRegistration.this, getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }
        rotateLoaderDialog.showLoader();
//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().sendMediaOTP(setOTPDetails(mobileNo, email), new retrofit.Callback<OTPModel>() {
            @Override
            public void success(OTPModel otptypeModel, Response response) {
                AppUtils.dismissDialog();
                if (otptypeModel == null) {
                    rotateLoaderDialog.dismissLoader();

                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (otptypeModel.getSuccess() == null) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (otptypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    AppUtils.notify(MediaRegistration.this, "Error", getString(R.string.something_wrong), R.color.error_color, ContextCompat.getDrawable(MediaRegistration.this, R.drawable.ic_error));
                    rotateLoaderDialog.dismissLoader();
                    return;
                }
                if (otptypeModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();

                    REC_OTP = otptypeModel.getOtp();

                    if (REC_OTP != null) {
                        if (!REC_OTP.equalsIgnoreCase("")) {
                            AppUtils.notify(MediaRegistration.this, "OTP Confirmation", "OTP send to your  " + input_mobnum.getText().toString());
                            showOTPDialog();
                        }
                    }


                }
            }

            ;

            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                rotateLoaderDialog.dismissLoader();
                AppUtils.ping(mContext, getString(R.string.something_wrong));
            }
        });

    }

    private Map<String, String> setOTPDetails(String mobileNo, String email) {
        Map<String, String> map = new HashMap<>();
        map.put("MobileNo", mobileNo);
        map.put("EmailAddress", email);
        return map;
    }


    private void showOTPDialog() {
        OTPdialog = new Dialog(this);
        OTPdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        OTPdialog.setContentView(R.layout.dialog_otp);

        final EditText edtOtp = (EditText) OTPdialog.findViewById(R.id.edt_otp);
        ImageView close = (ImageView) OTPdialog.findViewById(R.id.iv_close);

        OTPdialog.findViewById(R.id.btn_verify_otp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtOtp.getText().toString().equals(REC_OTP)) {

                    AppUtils.hideKeyboard(MediaRegistration.this);
                    OTPdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    OTPdialog.dismiss();
                    callMediaSignUpApi();
                } else {
                    edtOtp.requestFocus();
                    edtOtp.setError("OTP not match");
                }


            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OTPdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                OTPdialog.dismiss();
            }
        });
        OTPdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        OTPdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        OTPdialog.setCancelable(false);
        OTPdialog.setCanceledOnTouchOutside(false);
        OTPdialog.show();


    }


    @Override
    public void OnAlertOkClick() {
        Intent intentDashboard = new Intent(MediaRegistration.this, DashBoardActivity.class);
        intentDashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intentDashboard);
        finish();
    }
}
