package com.loksarkar.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.google.android.gms.tasks.OnCompleteListener;
import com.loksarkar.R;
import com.loksarkar.ui.bottomSheetView.BottomSheetFragment;
import com.loksarkar.utils.AppUtils;
import com.loksarkar.utils.DateUtils;
import com.loksarkar.utils.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import fr.ganfra.materialspinner.MaterialSpinner;


public class FilterBottomSheetFragment extends BottomSheetFragment implements AdapterView.OnItemSelectedListener,View.OnClickListener  {

    private AppCompatEditText etFromDate,etTodate;
    private AppCompatButton btnDone;
    private View rootView;
    private MaterialSpinner statusSpinner;
    private String[] ITEMS = {"All", "No Action", "Pending", "Close"};
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date,date2;
    private int statusPosition;
    private OnCompleteFilter mCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bottom_sheet, container, false);
        etFromDate = (AppCompatEditText)rootView.findViewById(R.id.edt_fromDate);
        etTodate = (AppCompatEditText)rootView.findViewById(R.id.edt_toDate);
        btnDone = (AppCompatButton)rootView.findViewById(R.id.btn_done);
        statusSpinner = (MaterialSpinner)rootView.findViewById(R.id.spinner_complaints);


        etFromDate.setText(DateUtils.getCurrentDateMinusOneMonth());
        etTodate.setText(DateUtils.getCurrentDate());

        btnDone.setOnClickListener(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, ITEMS);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setSelection(1);

        statusSpinner.setOnItemSelectedListener(this);

        myCalendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel2();
            }

        };


        etFromDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etTodate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date2, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        try {
            String fromDate = PrefUtils.getInstance(getActivity()).getStringValue("fromdate","");
            String toDate = PrefUtils.getInstance(getActivity()).getStringValue("todate","");
            String complaint_status = PrefUtils.getInstance(getActivity()).getStringValue("complaint_status","");

            if(fromDate != null && !TextUtils.isEmpty(fromDate)){
                etFromDate.setText(fromDate);
            }

            if(toDate != null && !TextUtils.isEmpty(toDate)){
                etTodate.setText(toDate);
            }

            if(complaint_status != null && !TextUtils.isEmpty(complaint_status)){

                for(int i=0; i < ITEMS.length; i++) {
                    if (ITEMS[i].contains(complaint_status)) {
                        statusPosition = i;
                        statusSpinner.setSelection(statusPosition + 1);
                        break;
                    }
                }

            }

        }catch (Exception ex){

        }

        return rootView;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCompleteFilter) {
            mCallback = (OnCompleteFilter) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        switch (adapterView.getId()){
            case R.id.spinner_complaints:

                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etFromDate.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel2() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        etTodate.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_done:
                if(DateUtils.CheckDates(etFromDate.getText().toString(),etTodate.getText().toString())) {
                    PrefUtils.getInstance(getActivity()).setValue("fromdate", etFromDate.getText().toString());
                    PrefUtils.getInstance(getActivity()).setValue("todate", etTodate.getText().toString());
                    PrefUtils.getInstance(getActivity()).setValue("complaint_status", (String) statusSpinner.getSelectedItem());
                    mCallback.onCompleted(etFromDate.getText().toString(), etTodate.getText().toString(), (String) statusSpinner.getSelectedItem());
                    dismiss();
                }else{
                    AppUtils.ping(getActivity(),"Please select valid dates");
                }
                break;
        }
    }

   public interface OnCompleteFilter{
        void onCompleted(String fromDate,String toDate,String status);
    }
}