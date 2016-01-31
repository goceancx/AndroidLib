package com.oceancx.androidlib.dialog;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by oceancx on 15/12/30.
 */
public class FgHelper {

    public static void showDialogFragment(DialogFragment dialogFragment, String tag, AppCompatActivity mAty) {
        FragmentTransaction mFragTransaction = mAty.getSupportFragmentManager().beginTransaction();
        Fragment fragment = mAty.getSupportFragmentManager().findFragmentByTag(tag);
        if (fragment != null) {
            //为了不重复显示dialog，在显示对话框之前移除正在显示的对话框
            mFragTransaction.remove(fragment);
        }
        dialogFragment.show(mFragTransaction, tag);
    }


    public static Fragment findFragmentByTag(AppCompatActivity mAty, String tag) {
        Fragment fragment = mAty.getSupportFragmentManager().findFragmentByTag(tag);
        return fragment;
    }

}
