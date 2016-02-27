package com.oceancx.androidlib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 可以放在NestedScrollView中,这样就可以在CoordinatorLayout中联动了
 * Created by oceancx on 15/12/26.
 */
public class ExpandableListViewForScrollView extends ExpandableListView {


    public ExpandableListViewForScrollView(Context context) {
        super(context);
    }

    public ExpandableListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpandableListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }


}
