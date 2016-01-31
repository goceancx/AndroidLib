package com.oceancx.androidlib.popupwindow;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.oceancx.androidlib.DebugLog;
import com.oceancx.androidlib.R;

import java.util.ArrayList;

/**
 * Created by oceancx on 15/12/30.
 */
public class FilterGridPw extends PopupWindow implements CompoundButton.OnCheckedChangeListener {

    String tagTitle[] = {"城市", "景点", "酒店", "饭店", "机场", "火车站", "租车公司", "长途汽车站", "购物"};

    CheckBox outCbx;

    TextView tag_pw_header[];
    int tag_pw_headerIds[] = {R.id.tag_pw_header_01, R.id.tag_pw_header_02};

    GridLayout tag_pw_grid[];

    int tag_pw_gridIds[] = {R.id.tag_pw_grid_1,
            R.id.tag_pw_grid_2};
    Button tag_pw_bt;

    int maxHeaderAndGrid = 2;
    int showCount;
    // could be any number,but cant be the same
    CheckBox[] no_limit_cbx = new CheckBox[2];

    public FilterGridPw(Context context, CheckBox outCbx) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        View contentview = LayoutInflater.from(context).inflate(R.layout.tag_popupwindow, null);
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
        init(context,outCbx);
    }

    private void init(Context context, CheckBox outCbx) {
        View view = getContentView();


        tag_pw_header = new TextView[maxHeaderAndGrid];
        tag_pw_grid = new GridLayout[maxHeaderAndGrid];


        for (int i = 0; i < maxHeaderAndGrid; i++) {
            tag_pw_header[i] = (TextView) view.findViewById(tag_pw_headerIds[i]);
            tag_pw_grid[i] = (GridLayout) view.findViewById(tag_pw_gridIds[i]);

        }

        tag_pw_bt = (Button) view.findViewById(R.id.tag_pw_bt);
        tag_pw_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 对outCheck设置tag
                 */
            }
        });
    }



    private void initGrid(Context context, GridLayout gridLayout) {
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        int w_screen = dm.widthPixels;
        int h_screen = dm.heightPixels;

        DebugLog.e("paleft:" + gridLayout.getPaddingLeft());

        int marRight = 15*3;
        int marBottom = marRight;
        int width = marRight;
        int row = 0;
        int col = 0;

    ArrayList<String > tags =new ArrayList<>();

        for (String tag : tags) {
            CheckBox checkBox = (CheckBox) LayoutInflater.from(context).inflate(R.layout.tag_pw_checkbox, null);
            checkBox.setOnCheckedChangeListener(this);
            checkBox.setText(tag);
            checkBox.setTag(tag+1);

            int spec = View.MeasureSpec.makeMeasureSpec(w_screen, View.MeasureSpec.AT_MOST);
            checkBox.measure(spec, spec);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = GridLayout.LayoutParams.WRAP_CONTENT;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(0, 0, marRight, marBottom);
            params.rowSpec = GridLayout.spec(row);
            params.columnSpec = GridLayout.spec(col++);
            width += checkBox.getMeasuredWidth() + marRight;
            DebugLog.e("width: " + width);
            if (width >= w_screen) {
                width = marRight + checkBox.getMeasuredWidth() + marRight;
                row++;
                col = 0;
                params.rowSpec = GridLayout.spec(row);
                params.columnSpec = GridLayout.spec(col++);
            }
            gridLayout.addView(checkBox, params);

        }
    }

    private int calModeIndex(int mode) {
        int exp = -1;
        while (mode != 0) {
            mode >>= 1;
            exp++;
        }
        return exp;
    }


    @Override
    public void dismiss() {
        if (outCbx != null && outCbx.isChecked()) {
            outCbx.setTag(null);
            outCbx.setChecked(false);
        }
        super.dismiss();
    }



    boolean lockCheck = false;

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (buttonView == no_limit_cbx[0]) {
                lockCheck = true;
                GridLayout gridLayout = tag_pw_grid[0];
                for (int i = 1; i < gridLayout.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) gridLayout.getChildAt(i);
                    checkBox.setChecked(false);
                }
                lockCheck = false;
            } else if (buttonView == no_limit_cbx[1]) {
                lockCheck = true;
                GridLayout gridLayout = tag_pw_grid[1];
                for (int i = 1; i < gridLayout.getChildCount(); i++) {
                    CheckBox checkBox = (CheckBox) gridLayout.getChildAt(i);
                    checkBox.setChecked(false);
                }
                lockCheck = false;
            } else {
                GridLayout gridLayout = tag_pw_grid[0];
                for (int i = 1; i < gridLayout.getChildCount(); i++) {
                    CheckBox cbx = (CheckBox) gridLayout.getChildAt(i);
                    if (buttonView == cbx) {
                        lockCheck = true;
                        no_limit_cbx[0].setChecked(false);
                        lockCheck = false;
                        break;
                    }
                }

                gridLayout = tag_pw_grid[1];
                for (int i = 1; i < gridLayout.getChildCount(); i++) {
                    CheckBox cbx = (CheckBox) gridLayout.getChildAt(i);
                    if (buttonView == cbx) {
                        lockCheck = true;
                        no_limit_cbx[1].setChecked(false);
                        lockCheck = false;
                        break;
                    }
                }
            }
        }
    }
}
