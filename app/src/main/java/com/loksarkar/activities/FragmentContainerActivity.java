package com.loksarkar.activities;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loksarkar.R;

public abstract class FragmentContainerActivity extends BaseActivity {

    private Fragment fragment;

    @Override
    public void onCreate(Bundle savedInstace) {
        super.onCreate(savedInstace);
        initFragment();
    }


    private void initFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Integer containerId = R.id.container;
        if (getContainerId() != null) {
            containerId = getContainerId();
        }
        fragment = fragmentManager.findFragmentById(containerId);
        if (fragment == null) {
            fragment = getFragmentInstance();
            fragmentManager.beginTransaction().add(containerId, fragment).commit();
        }
    }



    @IdRes
    protected Integer getContainerId() {
        return R.id.container;
    }

    /**
     * @return The new fragment to place in the container
     */

    protected abstract Fragment getFragmentInstance();

    /**
     * @return nested target fragment
     */
    public Fragment getNestedTargetFragment() {
        return fragment;
    }
}
