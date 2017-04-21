package com.example.vanlong.controllers;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;

/**
 * Created by vanlong on 4/13/2017.
 */

public class FragmentController {
    private FragmentManager mFragmentManager;
    private FragmentTransaction mFragmentTransaction;
    private Activity activity;

    public FragmentController(Activity activity) {
        this.activity = activity;
    }

    public void replaceFragmentContent(int id, Fragment fragment,String name,String backName) {
        mFragmentManager = activity.getFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(id, fragment,name);
        if (backName!=null) {
           mFragmentTransaction.addToBackStack(backName);
        }
        mFragmentTransaction.commit();
    }

}
