package com.loksarkar.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.loksarkar.R;
import com.loksarkar.api.ApiHandler;
import com.loksarkar.constants.AppConstants;
import com.loksarkar.models.OTPModel;
import com.loksarkar.models.RegistrationModel;
import com.loksarkar.models.RegistrationTypeModel;
import com.loksarkar.ui.MultiLineRadioGroup;
import com.loksarkar.ui.RotateLoaderDialog;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.InstallReferrerHelper;
import com.loksarkar.utils.PrefUtils;
import com.tapadoo.alerter.Alerter;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import fr.ganfra.materialspinner.MaterialSpinner;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SignupActivity extends AppCompatActivity implements InstallReferrerHelper.InstallReferrerCallback {


    private MultiLineRadioGroup multiLineRadioGroup,rgUserType;
    private Context mContext;
    private List<RegistrationTypeModel> finalArraydistrictTypeList;
    private HashMap<Integer, String> spinnerDistrictTypeMap;
    private MaterialSpinner spinner_district;
    private EditText input_name,input_address,input_mobnum,input_email,input_dob,input_Id_Proof_Num,input_loksevak_code;
    private String finalIdentityproofStr = "",finaluserTypeStr = "Other";
    private AppCompatButton mBtnSignUp;
    private String REC_OTP = "",day ="",month = "",year = "",finaldistrictIdStr ="",districtName = "" ;
    private DatePickerDialog.OnDateSetListener bdate;
    private Calendar myCalendar = Calendar.getInstance();
    private RotateLoaderDialog rotateLoaderDialog;
    private Dialog OTPdialog;
    private String firebase_RegId = "";
    private SharedPreferences pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_signup);
        rotateLoaderDialog = new RotateLoaderDialog(this);


        try {
            pref = getApplicationContext().getSharedPreferences(PrefUtils.SHARED_PREF, 0);
            firebase_RegId = pref.getString("regId","");

        }catch (Exception ex){
            ex.printStackTrace();
        }

        mContext = this;
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        multiLineRadioGroup = (MultiLineRadioGroup)findViewById(R.id.rg_options);
        rgUserType = (MultiLineRadioGroup)findViewById(R.id.rg_userType);
        spinner_district = (MaterialSpinner) findViewById(R.id.spinner_district);
        input_name = (EditText)findViewById(R.id.input_name);
        input_address = (EditText)findViewById(R.id.input_address);
        input_mobnum =(EditText)findViewById(R.id.input_mobnum);
        input_email = (EditText)findViewById(R.id.input_email);
        input_dob = (EditText)findViewById(R.id.input_dob);
        input_Id_Proof_Num =(EditText)findViewById(R.id.input_Id_Proof_Num);
        input_loksevak_code = (EditText)findViewById(R.id.input_loksevak_code);
        mBtnSignUp = (AppCompatButton)findViewById(R.id.btn_submit);


        try {
            InstallReferrerHelper.fetchInstallReferrer(SignupActivity.this,this);
        }catch (Exception ex){
            ex.printStackTrace();
        }


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
                if(validateForum()){
                    callOTPApi(input_mobnum.getText().toString(),input_email.getText().toString());
                }
            }
        });

        multiLineRadioGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ViewGroup group, RadioButton button) {
                finalIdentityproofStr = button.getText().toString();
            }
        });

        rgUserType.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ViewGroup group, RadioButton button) {
                finaluserTypeStr = button.getText().toString();
            }
        });


        input_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignupActivity.this,bdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        // CALL DistrictType
        callDistrictTypeApi();


        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0) {

                    districtName = spinner_district.getSelectedItem().toString();
                    String getid = spinnerDistrictTypeMap.get(spinner_district.getSelectedItemPosition());

                    finaldistrictIdStr = getid;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    private void fillDobEditext() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        input_dob.setText(sdf.format(myCalendar.getTime()));

        String dob =  input_dob.getText().toString();

        String[]split = dob.split("/");

        day = split[0];
        month = split[1];
        year = split[2];

    }

    private void callSignUpApi() {

        if (!AppUtils.isNetworkConnected(SignupActivity.this)) {
            AppUtils.notify(SignupActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }
        rotateLoaderDialog.showLoader();
//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().signUpUser(setUserDetails(),new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel otptypeModel, Response response) {
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
                    rotateLoaderDialog.dismissLoader();

                    return;
                }
                if (otptypeModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();

                    Intent intentDashboard = new Intent(SignupActivity.this,SigninActivity.class);
                    intentDashboard.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    AppConstants.NOTIFICATION_MSG = otptypeModel.getMessage();

                    AppUtils.ping(getApplicationContext(),otptypeModel.getMessage());
                    startActivity(intentDashboard);
                    finish();
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
        map.put("UserType",finaluserTypeStr);

        map.put("Name", input_name.getText().toString());
        map.put("Address", input_address.getText().toString());
        map.put("MobileNo", input_mobnum.getText().toString());
        map.put("EmailAddress", input_email.getText().toString());
        map.put("IDProofType", finalIdentityproofStr);
        if(input_Id_Proof_Num.getText().toString().length() > 0) {
            map.put("IDProofNo", input_Id_Proof_Num.getText().toString());
        }else{
            map.put("IDProofNo", "");

        }
        map.put("IDProofeFileName", "");
        map.put("DistrictID", finaldistrictIdStr);
        map.put("DistrictName",districtName);
        if(firebase_RegId != null){
            map.put("DeviceToken",firebase_RegId);

        }else{
            map.put("DeviceToken","");
        }

        if(input_loksevak_code.getText().toString().length() > 0){
            map.put("ReferralID", input_loksevak_code.getText().toString());
        }else{
            map.put("ReferralID","");

        }
        return map;
    }





    private void callOTPApi(String mobileNo,String email) {

        if (!AppUtils.isNetworkConnected(SignupActivity.this)) {
            AppUtils.notify(SignupActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }
        rotateLoaderDialog.showLoader();
//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().sendOTP(setOTPDetails(mobileNo,email),new retrofit.Callback<OTPModel>() {
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
                    AppUtils.notify(SignupActivity.this,"Error","email or phone already exist",R.color.error_color,ContextCompat.getDrawable(SignupActivity.this,R.drawable.ic_error));

                    rotateLoaderDialog.dismissLoader();

                    return;
                }
                if (otptypeModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();

                    REC_OTP = otptypeModel.getOtp();

                    if(REC_OTP != null){
                    if(!REC_OTP.equalsIgnoreCase("")) {
                        AppUtils.notify(SignupActivity.this,"OTP Confirmation","OTP send to your  "+input_mobnum.getText().toString());
                         showOTPDialog();
                       }
                    }


                }
            };


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

    private Map<String, String> setOTPDetails(String mobileNo,String email) {
        Map<String, String> map = new HashMap<>();
        map.put("MobileNo", mobileNo);
        map.put("EmailAddress", email);
        return map;
    }



    private void showOTPDialog(){
        OTPdialog = new Dialog(this);
        OTPdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        OTPdialog.setContentView(R.layout.dialog_otp);

        final EditText edtOtp  = (EditText)OTPdialog.findViewById(R.id.edt_otp);
        ImageView close = (ImageView) OTPdialog.findViewById(R.id.iv_close);

        OTPdialog.findViewById(R.id.btn_verify_otp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edtOtp.getText().toString().equals(REC_OTP)){

                    //AppUtils.hideKeyboard(SignupActivity.this);
                    OTPdialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    OTPdialog.dismiss();

                   // AppUtils.notify(mContext,"Verify","Verified Successfully.");
                    callSignUpApi();
                }else{
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
        OTPdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        OTPdialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        OTPdialog.setCancelable(false);
        OTPdialog.setCanceledOnTouchOutside(false);
        OTPdialog.show();


    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Alerter.clearCurrent(SignupActivity.this);
    }


    private void callDistrictTypeApi() {

        if (!AppUtils.isNetworkConnected(SignupActivity.this)) {
            AppUtils.notify(SignupActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getDistrictList(getDistrictTypeDetail(),new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel DistricttypeModel, Response response) {
                AppUtils.dismissDialog();
                if (DistricttypeModel == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (DistricttypeModel.getSuccess() == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (DistricttypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    return;
                }
                if (DistricttypeModel.getSuccess().equalsIgnoreCase("True")) {
                    finalArraydistrictTypeList = DistricttypeModel.getFinalArray();
                    if (finalArraydistrictTypeList != null) {
                        fillDistrictTypeSpinner();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                AppUtils.ping(mContext, getString(R.string.something_wrong));
            }
        });

    }

    private Map<String, String> getDistrictTypeDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Language", "english");
        return map;
    }





    //Use for fill the DistrictType Spinner
    public void fillDistrictTypeSpinner() {

        ArrayList<String> districttname = new ArrayList<>();

        for (int i = 0; i < finalArraydistrictTypeList.size(); i++) {
            districttname.add(finalArraydistrictTypeList.get(i).getDistrictName());
        }

        ArrayList<String> districtId = new ArrayList<>();
        for (int j = 0; j < finalArraydistrictTypeList.size(); j++) {
            districtId.add(finalArraydistrictTypeList.get(j).getDistrictID());

        }
        String[] spinnerdistrictIdArray = new String[districtId.size()];

        spinnerDistrictTypeMap = new HashMap<Integer, String>();
        for (int i = 0; i < districtId.size(); i++) {
            spinnerDistrictTypeMap.put(i, String.valueOf(districtId.get(i)));
            spinnerdistrictIdArray[i] = districttname.get(i).trim();
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_district);

            popupWindow.setHeight(spinnerdistrictIdArray.length > 4 ? 500 : spinnerdistrictIdArray.length * 100);
//            popupWindow1.setHeght(200);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext,R.layout.spinner_layout,spinnerdistrictIdArray);
        spinner_district.setAdapter(adapterstandard);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
    }



    private boolean validateForum(){

        if(finaluserTypeStr.length() <= 0 || finaluserTypeStr.equalsIgnoreCase("")){
            AppUtils.ping(SignupActivity.this,"Please select User type");
            return false;
        }

       else if(input_name.getText().toString().length() <= 0){
            //   AppUtils.ping(mContext,"B");
            input_name.setError("Please enter name");
            input_name.requestFocus();
            return false;
        }else if(input_address.getText().toString().length() <= 0){
            //   AppUtils.ping(mContext,"B");
            input_address.setError("Please enter address");
            input_address.requestFocus();
            return false;
        }else if(input_mobnum.getText().toString().length() < 10){
            //   AppUtils.ping(mContext,"B");
            input_mobnum.setError("Please enter valid phone no");
            input_mobnum.requestFocus();
            return false;
        }else if(input_email.getText().toString().length() <= 0){
            //   AppUtils.ping(mContext,"B");
            input_email.setError("Please enter email address.");
            input_email.requestFocus();
            return false;
        }else if(!AppUtils.isValidEmail(input_email.getText().toString())){
            //   AppUtils.ping(mContext,"B");
            input_email.setError("Please enter valid email address.");
            input_email.requestFocus();
            return false;
        }
//        else if(input_dob.getText().toString().length() <= 0){
//            //   AppUtils.ping(mContext,"B");
//            input_dob.setError("Please enter DOB.");
//            input_dob.requestFocus();
//            return false;
//        }
//        else if(finalIdentityproofStr.length() <= 0 || finalIdentityproofStr.equals("")){
//            AppUtils.ping(SignupActivity.this,"Please select Id Proof");
//            return  false;
//
//        }else if(input_Id_Proof_Num.getText().toString().length() <= 0){
//            input_Id_Proof_Num.setError("Please enter ID Proof num.");
//            input_Id_Proof_Num.requestFocus();
//            return  false;
//
//        }
//        else if(input_Id_Proof_Num.getText().toString().length() <= 0){
//            input_Id_Proof_Num.setError("Please enter ID Proof num.");
//            input_Id_Proof_Num.requestFocus();
//            return  false;
//        }

        else if(spinner_district.getSelectedItemPosition() == 0){
            spinner_district.requestFocus();
            AppUtils.ping(SignupActivity.this,"Please select district / metropolitan");
            return false;
        } else{
            return true;
        }
    }


    @Override
    public void onReceived(String installReferrer) {
        if(installReferrer != null) {
            if (!installReferrer.contains("utm")) {
                InstallReferrerHelper.setReferrerData(SignupActivity.this, installReferrer);
                String refferCode = InstallReferrerHelper.getReferrerDataRaw(SignupActivity.this);
                if (refferCode != null) {
                    input_loksevak_code.setText(refferCode);
                    input_loksevak_code.setEnabled(false);
                }
            }
        }
    }

    @Override
    public void onFailed() {
//        input_loksevak_code.setText(refferCode);
//        input_loksevak_code.setEnabled(false);

    }
}
