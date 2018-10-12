package com.loksarkar.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import com.loksarkar.R;
import com.loksarkar.models.RegistrationModel;
import com.loksarkar.models.RegistrationTypeModel;
import com.loksarkar.ui.RotateLoaderDialog;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.api.ApiHandler;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class CommitteeRegistrationActivity extends BaseActivity {

  Spinner  spinner_registrationtype,spinner_district, spinner_assembly, spinner_taluka_t,spinner_districtapanchayat_t,spinner_zone_t,spinner_taluka_panchyat_t,spinner_ward_t,spinner_dward_t;

    EditText input_name, input_address, input_mobnum, input_email, input_dob, input_Id_Proof_Num, input_fb_link, input_twitter_link;
    TextView input_loksabha_link;
    LinearLayout loksabha_linear,taluka_linear,ward_linear,taluka_panchayt_linear,zone_linear,districtapanchayat_linear,districtward_linear;
    RadioGroup rg_options;
    RadioButton rb_op1, rb_op2, rb_op3, rb_op4;

    private String day = "",month= "",year = "";

    Button btn_submit;
    RotateLoaderDialog rotateLoaderDialog;

    HashMap<Integer, String> spinnerVidhanTypeMap;
    HashMap<Integer, String> spinnerTalukaTypeMap;
    HashMap<Integer, String> spinnerCityWardTypeMap;
    HashMap<Integer, String> spinnerCityZoneTypeMap;
    HashMap<Integer, String> spinnerDistrictPanchayatTypeMap;
    HashMap<Integer, String> spinnerDistrictTypeMap;
    HashMap<Integer, String> spinnerTalukaPanchayatTypeMap;
    HashMap<Integer, String> spinnerRegisterTypeMap;
    HashMap<Integer, String> spinnerDistrictWardTypeMap;

    List<RegistrationTypeModel> finalArrayregistrationTypeList,finalArrayvidhanTypeList,finalArraydistrictTypeList,finalArraytalukaTypeList,finalArrayCityWardList,finalArrayCityZoneList,finalArrayDistrictPanchayatTypeList,finalArrayTalukaPanchayatTypeList,finalDistrictWardTypeList;
    String finalRegisterIdStr= "", finaldistrictIdStr= "",finaldistrictnameStr= "", finalVidhanIdStr= "", finalTalukaIdStr= "", finalNameStr= "",
            finalAddressStr= "", finalMobileStr = "", finalEmailIdStr = "", finalDobStr = "",
            finalIdentityproofStr = "", finalIdentitynuberStr ="", finalfacebookStr ="",
            finaltwitterStr ="",finalCityWardNoStr = "",finalCityZoneNameStr = "",finalDistrictpanchayatStr = "",finalTalukaPanchayatStr = "";
    private Context mContext;
    private EditText mEtDob;
    private Calendar myCalendar = Calendar.getInstance();
    private DatePickerDialog.OnDateSetListener bdate;
    private RadioGroup rgBtn;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        mContext = this;

        rotateLoaderDialog = new RotateLoaderDialog(this);
        initViews();
        setListners();
    }

    public void initViews() {
        spinner_registrationtype = (Spinner) findViewById(R.id.spinner_registrationtype);
        spinner_district = (Spinner) findViewById(R.id.spinner_district);
        spinner_assembly = (Spinner) findViewById(R.id.spinner_assembly);
        spinner_taluka_t = (Spinner) findViewById(R.id.spinner_taluka_t);
        spinner_zone_t= (Spinner) findViewById(R.id.spinner_zone_t);
        spinner_taluka_panchyat_t = (Spinner) findViewById(R.id.spinner_taluka_panchyat_t);
        spinner_districtapanchayat_t = (Spinner) findViewById(R.id.spinner_districtapanchayat_t);
        spinner_ward_t =(Spinner)findViewById(R.id.spinner_ward_t);
        spinner_dward_t = (Spinner)findViewById(R.id.spinner_district_ward);
        mEtDob = (EditText)findViewById(R.id.input_dob);

        input_name = (EditText) findViewById(R.id.input_name);
        input_address = (EditText) findViewById(R.id.input_address);
        input_mobnum = (EditText) findViewById(R.id.input_mobnum);
        input_email = (EditText) findViewById(R.id.input_email);
        input_dob = (EditText) findViewById(R.id.input_dob);
        input_Id_Proof_Num = (EditText) findViewById(R.id.input_Id_Proof_Num);
        input_fb_link = (EditText) findViewById(R.id.input_fb_link);
        input_twitter_link = (EditText) findViewById(R.id.input_twitter_link);

        rg_options = (RadioGroup) findViewById(R.id.rg_options);
        rb_op1 = (RadioButton) findViewById(R.id.rb_op1);
        rb_op2 = (RadioButton) findViewById(R.id.rb_op2);
        rb_op3 = (RadioButton) findViewById(R.id.rb_op3);
        rb_op4 = (RadioButton) findViewById(R.id.rb_op4);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        loksabha_linear =(LinearLayout)findViewById(R.id.loksabha_linear);
        taluka_linear =(LinearLayout)findViewById(R.id.taluka_linear);
        ward_linear = (LinearLayout)findViewById(R.id.ward_linear);
        taluka_panchayt_linear=(LinearLayout)findViewById(R.id.taluka_panchayt_linear);
        zone_linear = (LinearLayout)findViewById(R.id.zone_linear);
        districtapanchayat_linear=(LinearLayout)findViewById(R.id.districtapanchayat_linear);
        districtward_linear = (LinearLayout)findViewById(R.id.dward_linear);

        input_loksabha_link=(TextView)findViewById(R.id.input_loksabha_link);

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
        rb_op1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    finalIdentityproofStr = rb_op1.getText().toString();
                }
            }
        });

        rb_op2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    finalIdentityproofStr = rb_op1.getText().toString();
                }
            }
        });
        rb_op3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    finalIdentityproofStr = rb_op1.getText().toString();
                }
            }
        });

        rb_op4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    finalIdentityproofStr = rb_op1.getText().toString();
                }
            }
        });


        callRegisterTypeApi();
        callDistrictTypeApi();
    }

    public void setListners() {
        spinner_registrationtype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = spinner_registrationtype.getSelectedItem().toString();
                String getid = spinnerRegisterTypeMap.get(spinner_registrationtype.getSelectedItemPosition());

                Log.d("value", name + " " + getid);
                finalRegisterIdStr = getid;
                Log.d("FinalRegisterIdStr", finalRegisterIdStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinner_district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = spinner_district.getSelectedItem().toString();
                String getid = spinnerDistrictTypeMap.get(spinner_district.getSelectedItemPosition());

                Log.d("value", name + " " + getid);
                finaldistrictIdStr = getid;
                Log.d("finaldistrictIdStr", finaldistrictIdStr);
                for (int j=0;j<finalArraydistrictTypeList.size();j++){
                    if (finalArraydistrictTypeList.get(i).getWardStatus().equalsIgnoreCase("1")){
                        callWardApi();
                        ward_linear.setVisibility(View.VISIBLE);
                        taluka_linear.setVisibility(View.GONE);

                    }else{
                        ward_linear.setVisibility(View.GONE);
                        taluka_linear.setVisibility(View.VISIBLE);
                    }
                }
                callVidhanTypeApi();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_assembly.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = spinner_assembly.getSelectedItem().toString();
                String getid = spinnerVidhanTypeMap.get(spinner_assembly.getSelectedItemPosition());

                Log.d("value", name + " " + getid);
                finalVidhanIdStr = getid;
                Log.d("finalVidhanIdStr", finalVidhanIdStr);

                if (!name.equalsIgnoreCase("-- વિધાનસભા પસંદ કરો --")) {
                    loksabha_linear.setVisibility(View.VISIBLE);
                    for (int j=0;j<finalArraydistrictTypeList.size();j++){
                        if (finalArraydistrictTypeList.get(j).getDistrictName().equalsIgnoreCase(finaldistrictnameStr)){
                            if (finalArraydistrictTypeList.get(j).getWardStatus().equalsIgnoreCase("1")) {
                                ward_linear.setVisibility(View.VISIBLE);
                                taluka_linear.setVisibility(View.GONE);
                            }else{
                                ward_linear.setVisibility(View.GONE);
                                taluka_linear.setVisibility(View.VISIBLE);
                            }
                        }
                    }

                    for (int k = 0; k < finalArrayvidhanTypeList.size(); k++) {
                        if (finalArrayvidhanTypeList.get(k).getAssemblyName().equalsIgnoreCase(name)) {
                            input_loksabha_link.setText(finalArrayvidhanTypeList.get(k).getLoksabha());
                        }
                    }
                }
                callTalukaTypeApi();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinner_taluka_t.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = spinner_taluka_t.getSelectedItem().toString();
                String getid = spinnerTalukaTypeMap.get(spinner_taluka_t.getSelectedItemPosition());

                Log.d("value", name + " " + getid);
                finalTalukaIdStr = getid;
                Log.d("finalTalukaIdStr", finalTalukaIdStr);

                ward_linear.setVisibility(View.GONE);

                districtapanchayat_linear.setVisibility(View.VISIBLE);

                if(i != -1){
                   if(i > 0){
                       if(finalArraytalukaTypeList != null){
                           if(finalArraytalukaTypeList.size() > 0){
                               if(finalArraytalukaTypeList.get(i).getWardStatus().equalsIgnoreCase("1")){
                                   districtapanchayat_linear.setVisibility(View.GONE);
                                    districtward_linear.setVisibility(View.VISIBLE);
                                    callDistrictWardApi();
                               }else{
                                   if(finalArraytalukaTypeList.get(i).getWardStatus().equalsIgnoreCase("0")) {
                                       districtapanchayat_linear.setVisibility(View.VISIBLE);
                                       districtward_linear.setVisibility(View.GONE);
                                       callDistrictPanchayatApi();
                                    }
                                 }
                              }
                           }
                       }else{
                          districtapanchayat_linear.setVisibility(View.GONE);

                     }


                   }else{
                       districtapanchayat_linear.setVisibility(View.GONE);
                   }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        spinner_districtapanchayat_t.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != -1) {
                    if (position > 0) {
                        //finalDistrictpanchayatStr  = (String)spinner_districtapanchayat_t.getSelectedItem().toString();
                        finalDistrictpanchayatStr = finalArrayDistrictPanchayatTypeList.get(position).getDPId();
                        taluka_panchayt_linear.setVisibility(View.VISIBLE);
                        callTalukaPanchayatApi(finalDistrictpanchayatStr);

                    }else{
                        taluka_panchayt_linear.setVisibility(View.GONE);

                    }
                }else{
                    taluka_panchayt_linear.setVisibility(View.GONE);

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_taluka_panchyat_t.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != -1) {
                    if (position > 0) {
                        finalTalukaPanchayatStr  = (String)spinner_taluka_panchyat_t.getSelectedItem().toString();
                    }else{

                    }
                }else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_ward_t.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String wardname =  spinner_ward_t.getSelectedItem().toString();
                String wardid = spinnerCityWardTypeMap.get( spinner_ward_t.getSelectedItemPosition());

                if(position != -1){
                    if(position > 0){
                        finalCityWardNoStr =(String)spinner_ward_t.getSelectedItem().toString();
                        zone_linear.setVisibility(View.VISIBLE);
                        callZoneTypeApi(finalCityWardNoStr);
                    }else{
                        zone_linear.setVisibility(View.GONE);

                    }
                }else{
                    zone_linear.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinner_zone_t.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position != -1){
                    if(position > 0){
                        finalCityZoneNameStr = (String) spinner_zone_t.getSelectedItem().toString();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        rg_options.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateForum()){
                    callSubmitApi();
                }

            }
        });


        mEtDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CommitteeRegistrationActivity.this,bdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


    private void fillDobEditext() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        mEtDob.setText(sdf.format(myCalendar.getTime()));

        String dob =  mEtDob.getText().toString();

        String[]split = dob.split("/");

        day = split[0];
        month = split[1];
        year = split[2];

    }


    // CALL RegisterType API HERE
    private void callRegisterTypeApi() {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }

       //  AppUtils.showDialog(getActivity());
        ApiHandler.getApiService().getRegistrationType(getRegisterTypeDetail(), new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel registertypeModel, Response response) {
                //AppUtils.dismissDialog();
                if (registertypeModel == null) {
                   AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (registertypeModel.getSuccess() == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (registertypeModel.getSuccess().equalsIgnoreCase("false")) {
                    AppUtils.ping(mContext, getString(R.string.false_msg));
                    return;
                }
                if (registertypeModel.getSuccess().equalsIgnoreCase("True")) {
                    finalArrayregistrationTypeList = registertypeModel.getFinalArray();
                    if (finalArrayregistrationTypeList != null) {
                        fillRegisterTypeSpinner();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();

                error.printStackTrace();
                error.getMessage();
                AppUtils.ping(mContext, error.getMessage());
            }
        });

    }

    private Map<String, String> getRegisterTypeDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Language", "gujarati");

        return map;
    }

    //Use for fill the RegisterType Spinner
    public void fillRegisterTypeSpinner() {

        ArrayList<String> registername = new ArrayList<>();

        for (int i = 0; i < finalArrayregistrationTypeList.size(); i++) {
            registername.add(finalArrayregistrationTypeList.get(i).getName());
        }

        ArrayList<String> registerId = new ArrayList<>();
        for (int j = 0; j < finalArrayregistrationTypeList.size(); j++) {
            registerId.add(finalArrayregistrationTypeList.get(j).getValue());

        }
        String[] spinnerstandardIdArray = new String[registerId.size()];

        spinnerRegisterTypeMap = new HashMap<Integer, String>();
        for (int i = 0; i < registerId.size(); i++) {
            spinnerRegisterTypeMap.put(i, String.valueOf(registerId.get(i)));
            spinnerstandardIdArray[i] = registername.get(i).trim();
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_registrationtype);

            popupWindow.setHeight(spinnerstandardIdArray.length > 4 ? 500 : spinnerstandardIdArray.length * 100);
//            popupWindow1.setHeght(200);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {

        }


        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext,R.layout.spinner_layout, spinnerstandardIdArray);
        spinner_registrationtype.setAdapter(adapterstandard);
    }


    // CALL DistrictType API HERE
    private void callDistrictTypeApi() {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getDistrictList(getDistrictTypeDetail(), new retrofit.Callback<RegistrationModel>() {
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
                        for (int i=0;i<finalArraydistrictTypeList.size();i++){
                            if (finalArraydistrictTypeList.get(i).getWardStatus().equalsIgnoreCase("1")){
                                ward_linear.setVisibility(View.VISIBLE);
                            }else{
                                ward_linear.setVisibility(View.GONE);
                            }
                        }
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
        map.put("Language", "gujarati");

        return map;
    }


    private void callSubmitApi() {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }

        rotateLoaderDialog.showLoader();
//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().saveRegistrationForm(setSubmitDetail(), new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel DistricttypeModel, Response response) {
                AppUtils.dismissDialog();
                if (DistricttypeModel == null) {
                    rotateLoaderDialog.dismissLoader();

                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (DistricttypeModel.getSuccess() == null) {
                    rotateLoaderDialog.dismissLoader();

                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (DistricttypeModel.getSuccess().equalsIgnoreCase("false")) {
                    AppUtils.ping(mContext, getString(R.string.false_msg));
                    rotateLoaderDialog.dismissLoader();

                    return;
                }
                if (DistricttypeModel.getSuccess().equalsIgnoreCase("True")) {
                    rotateLoaderDialog.dismissLoader();
                    AppUtils.ping(getApplicationContext(),getString(R.string.success_msg));
                    Intent intentDashBoard = new Intent(CommitteeRegistrationActivity.this,DashBoardActivity.class);
                    startActivity(intentDashBoard);
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


        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnerdistrictIdArray);
        spinner_district.setAdapter(adapterstandard);
    }

    // CALL VidhanType API HERE
    private void callVidhanTypeApi() {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getAssemblyLoksabhaByDistrict(getVidhanTypeDetail(), new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel VidhantypeModel, Response response) {
                AppUtils.dismissDialog();
                if (VidhantypeModel == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (VidhantypeModel.getSuccess() == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (VidhantypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    loksabha_linear.setVisibility(View.GONE);
                    taluka_linear.setVisibility(View.GONE);
                    return;
                }
                if (VidhantypeModel.getSuccess().equalsIgnoreCase("True")) {
                    finalArrayvidhanTypeList = VidhantypeModel.getFinalArray();
                    if (finalArrayvidhanTypeList != null) {
                        fillVidhanTypeSpinner();
                    }else{
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

    private Map<String, String> getVidhanTypeDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Language", "gujarati");
        map.put("DistrictID", finaldistrictIdStr);

        return map;
    }

    //Use for fill the VidhanType Spinner
    public void fillVidhanTypeSpinner() {

        ArrayList<String> vidhanname = new ArrayList<>();

        for (int i = 0; i < finalArrayvidhanTypeList.size(); i++) {
            vidhanname.add(finalArrayvidhanTypeList.get(i).getAssemblyName());
        }

        ArrayList<String> vidhanId = new ArrayList<>();
        for (int j = 0; j < finalArrayvidhanTypeList.size(); j++) {
            vidhanId.add(finalArrayvidhanTypeList.get(j).getAssemblyID());

        }
        String[] spinnerassemblyIdArray = new String[vidhanId.size()];

        spinnerVidhanTypeMap = new HashMap<Integer, String>();
        for (int i = 0; i < vidhanId.size(); i++) {
            spinnerVidhanTypeMap.put(i, String.valueOf(vidhanId.get(i)));
            spinnerassemblyIdArray[i] = vidhanname.get(i).trim();
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_assembly);

            popupWindow.setHeight(spinnerassemblyIdArray.length > 4 ? 500 : spinnerassemblyIdArray.length * 100);
//            popupWindow1.setHeght(200);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnerassemblyIdArray);
        spinner_assembly.setAdapter(adapterstandard);
    }


    // CALL TalukaType API HERE
    private void callTalukaTypeApi() {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error) );
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getTaluka(getTalukaTypeDetail(), new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel talukatypeModel, Response response) {
                AppUtils.dismissDialog();
                if (talukatypeModel == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (talukatypeModel.getSuccess() == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("True")) {
                    finalArraytalukaTypeList = talukatypeModel.getFinalArray();
                    if (finalArraytalukaTypeList != null) {
                        fillTalukaTypeSpinner();
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


    private void callDistrictPanchayatTypeApi() {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error),getResources().getString(R.string.internet_connection_error));
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getDistrictPanchayatORWardd(getDistrictTypeDetail(), new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel talukatypeModel, Response response) {
                AppUtils.dismissDialog();
                if (talukatypeModel == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (talukatypeModel.getSuccess() == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("True")) {
                    finalArrayDistrictPanchayatTypeList = talukatypeModel.getFinalArray();
                    if (finalArrayDistrictPanchayatTypeList != null) {
                        fillTalukaTypeSpinner();
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

    private Map<String, String> getDistrictType() {
        Map<String, String> map = new HashMap<>();
        map.put("Language", "gujarati");
        map.put("DistrictID",finaldistrictIdStr);
        map.put("TalukaID",finalTalukaIdStr);
        return map;
    }


    private void callWardApi() {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error) );
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getCityWard(setWardDetail(), new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel talukatypeModel, Response response) {
                AppUtils.dismissDialog();
                if (talukatypeModel == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (talukatypeModel.getSuccess() == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("True")) {
                    finalArrayCityWardList = talukatypeModel.getFinalArray();
                    if (finalArrayCityWardList != null) {
                        fillWardTypeSpinner();
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
    private Map<String, String> setWardDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Language", "gujarati");
        map.put("DistrictID",finaldistrictIdStr);
        return map;
    }


    private void callDistrictWardApi() {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error) );
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getDistrictWard(setDistrictWardDetail(), new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel talukatypeModel, Response response) {
                AppUtils.dismissDialog();
                if (talukatypeModel == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (talukatypeModel.getSuccess() == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("True")) {
                    finalDistrictWardTypeList = talukatypeModel.getFinalArray();
                    if (finalDistrictWardTypeList != null) {
                        fillDistrictWardSpinner();
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
    private Map<String, String> setDistrictWardDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Language", "gujarati");
        map.put("DistrictID",finaldistrictIdStr);
        map.put("TalukaID",finalTalukaIdStr);
        return map;
    }


    private Map<String, String> getTalukaTypeDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Language", "gujarati");
        map.put("DistrictID",finaldistrictIdStr);
        map.put("AssemblyID",finalVidhanIdStr);
        return map;
    }


    public void fillDistrictWardSpinner() {

        ArrayList<String> districtWardNames = new ArrayList<>();

        for (int i = 0; i < finalDistrictWardTypeList.size(); i++) {
            districtWardNames.add(finalDistrictWardTypeList.get(i).getWardName());
        }

        ArrayList<String> wardId = new ArrayList<>();
        for (int j = 0; j < finalDistrictWardTypeList.size(); j++) {
            wardId.add(finalDistrictWardTypeList.get(j).getWardID());

        }
        String[] spinnerwardIdArray = new String[wardId.size()];

        spinnerDistrictWardTypeMap = new HashMap<Integer, String>();
        for (int i = 0; i < wardId.size(); i++) {
            spinnerDistrictWardTypeMap.put(i, String.valueOf(wardId.get(i)));
            spinnerwardIdArray[i] = districtWardNames.get(i).trim();
        }



        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_assembly);

            popupWindow.setHeight(spinnerwardIdArray.length > 4 ? 500 : spinnerwardIdArray.length * 100);
//            popupWindow1.setHeght(200);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnerwardIdArray);
        spinner_dward_t.setAdapter(adapterstandard);
    }



    //Use for fill the TalukaType Spinner
    public void fillTalukaTypeSpinner() {

        ArrayList<String> talukaname = new ArrayList<>();

        for (int i = 0; i < finalArraytalukaTypeList.size(); i++) {
            talukaname.add(finalArraytalukaTypeList.get(i).getTalukaName());
        }

        ArrayList<String> talukaId = new ArrayList<>();
        for (int j = 0; j < finalArraytalukaTypeList.size(); j++) {
            talukaId.add(finalArraytalukaTypeList.get(j).getTalukaID());

        }
        String[] spinnertalukaIdArray = new String[talukaId.size()];

        spinnerTalukaTypeMap = new HashMap<Integer, String>();
        for (int i = 0; i < talukaId.size(); i++) {
            spinnerTalukaTypeMap.put(i, String.valueOf(talukaId.get(i)));
            spinnertalukaIdArray[i] = talukaname.get(i).trim();
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_assembly);

            popupWindow.setHeight(spinnertalukaIdArray.length > 4 ? 500 : spinnertalukaIdArray.length * 100);
//            popupWindow1.setHeght(200);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnertalukaIdArray);
        spinner_taluka_t.setAdapter(adapterstandard);
    }


    //Use for fill the TalukaType Spinner
    public void fillDistrictPanchayatTypeSpinner() {

        ArrayList<String>name = new ArrayList<>();

        for (int i = 0; i < finalArrayTalukaPanchayatTypeList.size(); i++) {
           name.add(finalArrayTalukaPanchayatTypeList.get(i).getTalukaName());
        }

        ArrayList<String> talukaId = new ArrayList<>();
        for (int j = 0; j < finalArrayTalukaPanchayatTypeList.size(); j++) {
            talukaId.add(finalArrayTalukaPanchayatTypeList.get(j).getTalukaID());

        }
        String[] spinnertalukaIdArray = new String[talukaId.size()];

        spinnerTalukaTypeMap = new HashMap<Integer, String>();
        for (int i = 0; i < talukaId.size(); i++) {
            spinnerTalukaTypeMap.put(i, String.valueOf(talukaId.get(i)));
            spinnertalukaIdArray[i] = name.get(i).trim();
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_assembly);

            popupWindow.setHeight(spinnertalukaIdArray.length > 4 ? 500 : spinnertalukaIdArray.length * 100);
//            popupWindow1.setHeght(200);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }


        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnertalukaIdArray);
        spinner_taluka_t.setAdapter(adapterstandard);
    }


    //Use for fill the TalukaType Spinner
    public void fillWardTypeSpinner() {

        ArrayList<String> wardName = new ArrayList<>();

        for (int i = 0; i < finalArrayCityWardList.size(); i++) {
            wardName.add(finalArrayCityWardList.get(i).getWardName());
        }

        ArrayList<String> wardId = new ArrayList<>();
        for (int j = 0; j < finalArrayCityWardList.size(); j++) {
            wardId.add(finalArrayCityWardList.get(j).getWardID());

        }
        String[] spinnerWardIdArray = new String[wardId.size()];

        spinnerCityWardTypeMap = new HashMap<Integer, String>();
        for (int i = 0; i < wardId.size(); i++) {
            spinnerCityWardTypeMap.put(i, String.valueOf(wardId.get(i)));
            spinnerWardIdArray[i] = wardName.get(i).trim();
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_assembly);

            popupWindow.setHeight(spinnerWardIdArray.length > 4 ? 500 : spinnerWardIdArray.length * 100);
//            popupWindow1.setHeght(200);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {

        }


        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnerWardIdArray);
        spinner_ward_t.setAdapter(adapterstandard);
    }



    // CALL ZoneType API HERE
    private void callZoneTypeApi(String wardNo) {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getCityZoneByCityWard(getZoneTypeDetail(wardNo), new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel talukatypeModel, Response response) {
                AppUtils.dismissDialog();
                if (talukatypeModel == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (talukatypeModel.getSuccess() == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("True")) {
                    finalArrayCityZoneList = talukatypeModel.getFinalArray();
                    if (finalArrayCityZoneList != null) {
                        fillZoneTypeSpinner();
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

    private Map<String, String> getZoneTypeDetail(String wardNo) {
        Map<String, String> map = new HashMap<>();
        map.put("Language", "gujarati");
        map.put("DistrictID",finaldistrictIdStr);
        map.put("WardNo",wardNo);
        return map;
    }

    private void callDistrictPanchayatApi() {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getDistrictPanchayatORWardd(getDistrictPanchayat(),new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel talukatypeModel, Response response) {
                AppUtils.dismissDialog();
                if (talukatypeModel == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    districtapanchayat_linear.setVisibility(View.GONE);

                    return;
                }
                if (talukatypeModel.getSuccess() == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    districtapanchayat_linear.setVisibility(View.GONE);

                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                   // AppUtils.ping(mContext, getString(R.string.something_wrong));
                    districtapanchayat_linear.setVisibility(View.GONE);

                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("True")) {
                    districtapanchayat_linear.setVisibility(View.VISIBLE);

                    finalArrayDistrictPanchayatTypeList = talukatypeModel.getFinalArray();
                    if (finalArrayDistrictPanchayatTypeList != null) {
                        fillDistrictPanchayatSpinner();
                    }
                }
            }

            @Override
            public void failure(RetrofitError error) {
                AppUtils.dismissDialog();
                error.printStackTrace();
                error.getMessage();
                districtapanchayat_linear.setVisibility(View.GONE);

                AppUtils.ping(mContext, getString(R.string.something_wrong));
            }
        });

    }

    private Map<String, String> getDistrictPanchayat() {
        Map<String, String> map = new HashMap<>();
        map.put("Language","gujarati");
        map.put("DistrictID",finaldistrictIdStr);
        map.put("TalukaID",finalTalukaIdStr);
        return map;
    }



    private void callTalukaPanchayatApi(String districtPanchayatId) {

        if (!AppUtils.isNetworkConnected(mContext)) {
            AppUtils.notify(CommitteeRegistrationActivity.this,getResources().getString(R.string.internet_error), getResources().getString(R.string.internet_connection_error));
            return;
        }

//        Utils.showDialog(getActivity());
        ApiHandler.getApiService().getTalukaPanchayat(getTalukaPanchayat(districtPanchayatId),new retrofit.Callback<RegistrationModel>() {
            @Override
            public void success(RegistrationModel talukatypeModel, Response response) {
                AppUtils.dismissDialog();
                if (talukatypeModel == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    taluka_panchayt_linear.setVisibility(View.GONE);

                    return;
                }
                if (talukatypeModel.getSuccess() == null) {
                    AppUtils.ping(mContext, getString(R.string.something_wrong));
                    taluka_panchayt_linear.setVisibility(View.GONE);

                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("false")) {
//                    Utils.ping(mContext, getString(R.string.false_msg));
                    taluka_panchayt_linear.setVisibility(View.GONE);
                    return;
                }
                if (talukatypeModel.getSuccess().equalsIgnoreCase("True")) {
                    taluka_panchayt_linear.setVisibility(View.VISIBLE);

                    finalArrayTalukaPanchayatTypeList = talukatypeModel.getFinalArray();
                    if (finalArrayTalukaPanchayatTypeList != null) {
                        fillTalukaPanchayatSpinner();
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

    private Map<String, String> getTalukaPanchayat(String Id) {
        Map<String, String> map = new HashMap<>();
        map.put("Language","gujarati");
        map.put("DistrictID",finaldistrictIdStr);
        map.put("TalukaID",finalTalukaIdStr);
        map.put("DistPanchayatID",Id);
        return map;
    }


    //Use for fill the ZoneType Spinner
    public void fillDistrictPanchayatSpinner() {


        ArrayList<String> wardName = new ArrayList<>();

        for (int i = 0; i <finalArrayDistrictPanchayatTypeList.size(); i++) {
            wardName.add(finalArrayDistrictPanchayatTypeList.get(i).getDPName());
        }

        ArrayList<String> wardId = new ArrayList<>();
        for (int j = 0; j < finalArrayDistrictPanchayatTypeList.size(); j++) {
            wardId.add(finalArrayDistrictPanchayatTypeList.get(j).getDPId());

        }
        String[] spinnerWardIdArray = new String[wardId.size()];

        spinnerDistrictPanchayatTypeMap = new HashMap<Integer, String>();
        for (int i = 0; i < wardId.size(); i++) {
            spinnerDistrictPanchayatTypeMap.put(i, String.valueOf(wardId.get(i)));
            spinnerWardIdArray[i] = wardName.get(i).trim();
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_districtapanchayat_t);

            popupWindow.setHeight(spinnerWardIdArray.length > 4 ? 500 : spinnerWardIdArray.length * 100);
//            popupWindow1.setHeght(200);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {


        }


        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnerWardIdArray);
        spinner_districtapanchayat_t.setAdapter(adapterstandard);
    }



    public void fillTalukaPanchayatSpinner() {


        ArrayList<String> wardName = new ArrayList<>();

        for (int i = 0; i < finalArrayTalukaPanchayatTypeList.size(); i++) {
            wardName.add(finalArrayTalukaPanchayatTypeList.get(i).getTPName());
        }

        ArrayList<String> wardId = new ArrayList<>();
        for (int j = 0; j < finalArrayTalukaPanchayatTypeList.size(); j++) {
            wardId.add(finalArrayTalukaPanchayatTypeList.get(j).getTPId());

        }
        String[] spinnerWardIdArray = new String[wardId.size()];

        spinnerTalukaPanchayatTypeMap = new HashMap<Integer, String>();
        for (int i = 0; i < wardId.size(); i++) {
            spinnerTalukaPanchayatTypeMap.put(i, String.valueOf(wardId.get(i)));
            spinnerWardIdArray[i] = wardName.get(i).trim();
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_assembly);

            popupWindow.setHeight(spinnerWardIdArray.length > 4 ? 500 : spinnerWardIdArray.length * 100);
//            popupWindow1.setHeght(200);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {

        }


        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnerWardIdArray);
        spinner_taluka_panchyat_t.setAdapter(adapterstandard);
    }



    //Use for fill the ZoneType Spinner
    public void fillZoneTypeSpinner() {

        ArrayList<String> zoneNames = new ArrayList<>();

        for (int i = 0; i < finalArrayCityZoneList.size(); i++) {
            zoneNames.add(finalArrayCityZoneList.get(i).getZoneName());
        }


        String[] spinnerzoneNameArray = new String[zoneNames.size()];

        spinnerCityZoneTypeMap = new HashMap<Integer, String>();
        for (int i = 0; i < zoneNames.size(); i++) {
           // spinnerTalukaTypeMap.put(i, String.valueOf(talukaId.get(i)));
            spinnerzoneNameArray[i] = zoneNames.get(i).trim();
        }

        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(spinner_assembly);

            popupWindow.setHeight(spinnerzoneNameArray.length > 4 ? 500 : spinnerzoneNameArray.length * 100);
//            popupWindow1.setHeght(200);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
        }


        ArrayAdapter<String> adapterstandard = new ArrayAdapter<String>(mContext, R.layout.spinner_layout, spinnerzoneNameArray);
        spinner_zone_t.setAdapter(adapterstandard);
    }




    private void getInsertedValue() {
        finalNameStr = input_name.getText().toString();
        finalAddressStr = input_address.getText().toString();
        finalMobileStr = input_mobnum.getText().toString();
        finalEmailIdStr = input_name.getText().toString();
        finalDobStr = input_name.getText().toString();
        finalIdentityproofStr = input_name.getText().toString();
        finalIdentitynuberStr = input_name.getText().toString();
        finalfacebookStr = input_name.getText().toString();
        finaltwitterStr = input_name.getText().toString();
    }

    private boolean validateForum(){

        if(input_name.getText().toString().length() <= 0){
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
         }else if(mEtDob.getText().toString().length() <= 0){
             //   AppUtils.ping(mContext,"B");
             mEtDob.setError("Please enter DOB.");
             mEtDob.requestFocus();
             return false;
         }else if(finalIdentityproofStr.length() <= 0 || finalIdentityproofStr.equals("")){
            AppUtils.ping(CommitteeRegistrationActivity.this,"Please select Id Proof");
            return  false;

         }else if(input_Id_Proof_Num.getText().toString().length() <= 0){
             input_Id_Proof_Num.setError("Please enter ID Proof num.");
             input_Id_Proof_Num.requestFocus();
             return  false;

         }else if(input_Id_Proof_Num.getText().toString().length() <= 0){
             input_Id_Proof_Num.setError("Please enter ID Proof num.");
             input_Id_Proof_Num.requestFocus();
             return  false;
         } else if(spinner_district.getSelectedItemPosition() == 0){
             spinner_district.requestFocus();
             AppUtils.ping(CommitteeRegistrationActivity.this,"Please select district");
             return false;
          }else if(spinner_assembly.getSelectedItemPosition() == 0){
             spinner_assembly.requestFocus();
             AppUtils.ping(CommitteeRegistrationActivity.this,"Please select assembly");
             return false;
         } else{
             return true;
         }


    }




    private Map<String, String> setSubmitDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("Language", "gujarati");
        map.put("RegistrationTypeID",finalRegisterIdStr);
        map.put("Name",input_name.getText().toString());
        map.put("Organisation","");
        map.put("Address", input_address.getText().toString());
        map.put("MobileNo",input_mobnum.getText().toString());
        map.put("EmailAddress",input_email.getText().toString());
        map.put("Month",month);
        map.put("Day",day);
        map.put("Year",year);
        map.put("IDProofeType", finalIdentityproofStr);
        map.put("IDProofeNo",finalIdentitynuberStr);
        map.put("FileName", "");
        map.put("WebsiteLink", "");
        map.put("FaceBookLink","");
        map.put("TwitterLink", "");
        map.put("RegistrationNo",finalRegisterIdStr);
        map.put("DistrictID",finaldistrictIdStr);
        map.put("AssemblyID",finalVidhanIdStr);
        map.put("Loksabha",input_loksabha_link.getText().toString());

        if(spinner_taluka_t.getVisibility() == View.VISIBLE){

            if(spinner_taluka_t.getSelectedItemPosition() <= 0) {
                AppUtils.ping(CommitteeRegistrationActivity.this,"Please select taluka");
            }else{
                String taluka = (String) spinner_taluka_t.getSelectedItem().toString();
                map.put("Taluka", taluka);
            }

        }else{
            map.put("Taluka","");

        }


        if(zone_linear.getVisibility() == View.VISIBLE){

            if(spinner_zone_t.getSelectedItemPosition() <=0){
                AppUtils.ping(CommitteeRegistrationActivity.this,"Please select zone");
            }else{
                String zone = (String)spinner_zone_t.getSelectedItem().toString();
                map.put("Zone",zone);
            }


        }else{
            map.put("Zone","");

        }


        if(ward_linear.getVisibility() == View.VISIBLE){
            if(spinner_ward_t.getSelectedItemPosition() <= 0){
                AppUtils.ping(CommitteeRegistrationActivity.this,"Please select ward");

            }else {
                String ward = (String) spinner_ward_t.getSelectedItem().toString();
                map.put("CityWard", ward);
            }

        }else{
            map.put("CityWard","");

        }


        if(districtapanchayat_linear.getVisibility() == View.VISIBLE){
            if(spinner_districtapanchayat_t.getSelectedItemPosition() <= 0) {
                AppUtils.ping(CommitteeRegistrationActivity.this,"Please select district panchayat");

            }else{
                String dPanchayat = (String) spinner_districtapanchayat_t.getSelectedItem().toString();
                map.put("DistrictPanchayat",dPanchayat);
            }

        }else{
            map.put("DistrictPanchayat","");

        }


        if(taluka_panchayt_linear.getVisibility() == View.VISIBLE){

            if(spinner_taluka_panchyat_t.getSelectedItemPosition()  <= 0) {
                AppUtils.ping(CommitteeRegistrationActivity.this,"Please select taluka panchayat");

            }else{
                String tPanchayat = (String) spinner_taluka_panchyat_t.getSelectedItem().toString();
                map.put("TalukaPanchayat", tPanchayat);
            }

        }else{
            map.put("TalukaPanchayat", "");

        }

        if(districtward_linear.getVisibility() == View.VISIBLE){
            if(spinner_dward_t.getSelectedItemPosition() <= 0) {
                AppUtils.ping(CommitteeRegistrationActivity.this,"Please select district ward");

            }else{
                String dward = (String) spinner_dward_t.getSelectedItem().toString();
                map.put("DistrictWard", dward);
            }

        }else{
            map.put("DistrictWard", "");

        }
        return map;
    }
}

