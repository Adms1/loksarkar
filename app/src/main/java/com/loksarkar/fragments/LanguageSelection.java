package com.loksarkar.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loksarkar.R;
import com.loksarkar.activities.DashBoardActivity;
import com.loksarkar.activities.LocalizationAcitivity;
import com.loksarkar.base.BaseApp;
import com.loksarkar.localeutils.LocaleChanger;
import com.loksarkar.ui.fullscreendialog.FullScreenDialogContent;
import com.loksarkar.ui.fullscreendialog.FullScreenDialogController;
import com.loksarkar.utils.PrefUtils;



public class LanguageSelection extends Fragment implements FullScreenDialogContent,View.OnClickListener {


    public static final String EXTRA_NAME = "EXTRA_NAME";
    public static final String RESULT_FULL_NAME = "RESULT_FULL_NAME";

    private FullScreenDialogController dialogController;
    private RelativeLayout flowLayout;
    private TextView mTVGujarati,mTVHindi,mTVEnglish;
    private Context mContext;
    private View view;

    public LanguageSelection() {
        // Required empty public constructor
    }


    public static LanguageSelection newInstance(String param1, String param2) {
        LanguageSelection fragment = new LanguageSelection();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_language_selection, container, false);
        flowLayout  = (RelativeLayout)view.findViewById(R.id.flowlayout);

        mTVEnglish  = (TextView)view.findViewById(R.id.tv_english);
        mTVGujarati = (TextView)view.findViewById(R.id.tv_gujarati);
        mTVHindi  = (TextView)view.findViewById(R.id.tv_hindi);

        mTVGujarati.setOnClickListener(this);
        mTVEnglish.setOnClickListener(this);
        mTVHindi.setOnClickListener(this);


       return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onDialogCreated(FullScreenDialogController dialogController) {
        this.dialogController = dialogController;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dialogController.setConfirmButtonEnabled(true);

    }

    @Override
    public boolean onConfirmClick(FullScreenDialogController dialog) {
        return true;
    }

    @Override
    public boolean onDiscardClick(FullScreenDialogController dialog) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.app_name)
                .setMessage(R.string.discard_confirmation_message)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogController.discard();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

        return true;
    }

    @Override
    public boolean onExtraActionClick(MenuItem actionItem, FullScreenDialogController dialogController) {

        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_english:
                PrefUtils.getInstance(getActivity()).setLangugae();
                LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(2));
                redirectToDashBoard();
                break;
            case R.id.tv_gujarati:
                PrefUtils.getInstance(getActivity()).setLangugae();
                LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(0));
                redirectToDashBoard();
                break;
            case R.id.tv_hindi:
                PrefUtils.getInstance(getActivity()).setLangugae();
                LocaleChanger.setLocale(BaseApp.SUPPORTED_LOCALES.get(1));
                redirectToDashBoard();
                break;
        }
    }

    private void redirectToDashBoard(){
        Intent intentDashboard = new Intent(getActivity(),DashBoardActivity.class);
        startActivity(intentDashboard);
        getActivity().finishAffinity();
    }
}
