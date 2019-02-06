package com.loksarkar.activities;

import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loksarkar.R;
import com.loksarkar.fragments.RefferalUserListFragment;

public class FragmentContainerActivity extends BaseActivity {

    private Fragment fragment;
    private int fragmentid;

    @Override
    public void onCreate(Bundle savedInstace) {
        super.onCreate(savedInstace);
        setContentView(R.layout.activity_fragment_container);


        fragment = new RefferalUserListFragment();
        fragmentid = fragment.getId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (fragment instanceof RefferalUserListFragment) {
            fragmentManager.beginTransaction().setCustomAnimations(0, 0).replace(R.id.container,fragment).commit();
        }



        //initFragment();
    }


    private void initFragment() {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        Integer containerId = R.id.container;
        if (getContainerId() != null) {
            containerId = getContainerId();
        }
        fragment = fragmentManager.findFragmentById(containerId);
        if (fragment == null) {
         //   fragment = getFragmentInstance();
            fragmentManager.beginTransaction().add(containerId,fragment).commit();
        }
    }



    @IdRes
    protected Integer getContainerId() {
        return R.id.container;
    }




    public Fragment getNestedTargetFragment() {
        return fragment;
    }
}
