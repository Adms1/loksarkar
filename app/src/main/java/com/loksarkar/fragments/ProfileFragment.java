package com.loksarkar.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loksarkar.R;


public class ProfileFragment extends BaseFragment {



    public ProfileFragment() {
        // Required empty public constructor
    }




    @Override
    protected void initFragment(@NonNull View view) {
        super.initFragment(view);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    protected int getLayoutResId() {
        return R.layout.fragment_profile;
    }
}
