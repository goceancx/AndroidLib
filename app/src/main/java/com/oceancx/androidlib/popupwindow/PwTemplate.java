package com.oceancx.androidlib.popupwindow;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;

import com.oceancx.androidlib.R;


/**
 * Created by oceancx on 16/1/27.
 */
public class PwTemplate extends PopupWindow {
    CheckBox outCbx;

    public PwTemplate(Context context, CheckBox outCbx) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View contentview = LayoutInflater.from(context).inflate(R.layout.line_popupwindow, null);
        setContentView(contentview);
        setTouchable(true);
        setFocusable(true);
        setOutsideTouchable(true);
        this.outCbx = outCbx;
        contentview.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });
        contentview.setFocusable(true); // 这个很重要
        contentview.setFocusableInTouchMode(true);
        contentview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        init(context);
    }

    private void init(Context context) {

    }

    public void dismiss() {
        if (outCbx != null && outCbx.isChecked()) {
            outCbx.setTag(null);
            outCbx.setChecked(false);
        }
        super.dismiss();
    }

    public void dismiss(Object tag) {
        if (outCbx != null && outCbx.isChecked()) {

            // TODO: 15/12/31 warnning: the two statement's postion really matters
            outCbx.setTag(tag);
            // TODO: 15/12/31
            outCbx.setChecked(false);
        }
        super.dismiss();
    }


}
