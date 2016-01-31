package com.oceancx.androidlib.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.oceancx.androidlib.R;


/**
 * Created by oceancx on 16/1/27.
 */
public class DialogTemplate extends DialogFragment {

    public static DialogTemplate newInstance() {
        DialogTemplate fragment = new DialogTemplate();
        Bundle bundle = new Bundle();

        fragment.setArguments(bundle);//把参数传递给该DialogFragment
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        try {

        } catch (NullPointerException e) {
            throw new IllegalArgumentException("传递参数失败");
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_template, null);
        builder.setView(view);

        init(view);

        Dialog dialog = builder.create();
        setStyle(STYLE_NO_FRAME, 0);

        return dialog;
    }

    private void init(View view) {

    }

    public interface DialogTemplateImp {

        void onEvent();
    }
}
