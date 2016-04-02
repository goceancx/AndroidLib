package com.oceancx.androidlib.QA;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * EditText在ListView中的处理
 * Created by oceancx on 16/4/2.
 */
public class ListViewContainsEditText extends AppCompatActivity {
    ListView lv;
    ArrayList<String> items = new ArrayList<>();
    String content;
    int index = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lv = new ListView(this);
        for (int i = 0; i < 30; i++)
            items.add("" + i);
        lv.setAdapter(new BaseAdapter() {

            @Override
            public int getCount() {
                return items.size();
            }

            @Override
            public Object getItem(int position) {
                return null;
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = new LinearLayout(parent.getContext());

                } else {
                    // 因为项⽬目中每⼀一⾏行的控件究竟有什么都不确定,所以清掉layout⾥里的所有控件,你的项⽬目视情况⽽而定。

                    ((LinearLayout) convertView).removeAllViews();
                }


                // 不要直接new⼀一个Layout去赋值给convertView!!那样就不是重⽤用了,否则,后果⾃自负~~
                EditText et = new EditText(parent.getContext());
                // 你可以试试把addView放到这个函数的return之前,我保证你会后悔的~~
                // addView的先后对画⾯面的结果是有影响的。
                ((LinearLayout) convertView).addView(et);
                et.setTag(position);



                et.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // 在TOUCH的UP事件中,要保存当前的⾏行下标,因为弹出软键盘后,整个画⾯面会被重画
                        // 在getView⽅方法的最后,要根据index和当前的⾏行下标⼿手动为EditText设置焦点
                        if (event.getAction() == MotionEvent.ACTION_UP) {
                            index = (int) v.getTag();
                        }
                        return false;
                    }
                });

                //这句要放在addTextWatcher之前
                et.setText(items.get(position));
                et.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        // 在这个地⽅方添加你的保存⽂文本内容的代码,如果不保存,你就等着重新输⼊入吧
                        // ⽽而且不管你输⼊入多少次,也不会有⽤用的,因为getView全清了~~
                        if (index != -1) {
                            items.set(index, s.toString());
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });


                et.clearFocus();
                if (position == index && index != -1) {
                    // 如果当前的⾏行下标和点击事件中保存的index⼀一致,⼿手动为EditText设置焦点。
                    et.requestFocus();
                }

                return convertView;
            }
        });
        setContentView(lv);
    }
}
