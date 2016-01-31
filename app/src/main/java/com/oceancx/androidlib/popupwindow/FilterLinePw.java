package com.oceancx.androidlib.popupwindow;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.oceancx.androidlib.R;


/**
 * 最简单的PopupWindow
 * popupwindow的作用是构造出来s007请求
 * 然后通知fg更新列表
 * Created by oceancx on 15/12/30.
 */
public class FilterLinePw extends PopupWindow {


    RadioGroup radioGroup;
    CheckBox outCbx;

    // could be any number,but cant be the same
    int[] rbtIds = {0, 1, 2};

    public FilterLinePw(Context context, CheckBox outCbx, String[] items) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View contentview = LayoutInflater.from(context).inflate(R.layout.line_popupwindow, null);
        setContentView(contentview);
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

        /**
         * 用数据初始化ui
         */
        radioGroup = (RadioGroup) contentview.findViewById(R.id.filter_layout1);
        radioGroup.clearCheck();
        if (items == null) return;
        for (int i = 0; i < items.length; i++) {

            RadioButton radioButton = (RadioButton) LayoutInflater.from(context).inflate(R.layout.line_popupwindow_rbt, null);
            radioButton.setId(rbtIds[i]);
            radioButton.setText(items[i]);

            radioGroup.addView(radioButton, -1, radioGroup.getLayoutParams());

            if (items[i].equals(outCbx.getText())) {
                outCbx.setTag(i);
                radioButton.setChecked(true);
            }

        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dismiss();
            }
        });

    }


    @Override
    public void dismiss() {
        if (outCbx != null && outCbx.isChecked()) {
            int id = radioGroup.getCheckedRadioButtonId();
            RadioButton rbt = (RadioButton) radioGroup.findViewById(id);
            Integer old_tag = (Integer) outCbx.getTag();
            if (old_tag == null) {
                super.dismiss();
                return;
            }
            if (old_tag != id) {
                outCbx.setTag(id);
                outCbx.setText(rbt.getText());
            } else {
                outCbx.setTag(-1);
            }
            outCbx.setChecked(false);
        }
        super.dismiss();
    }

}
