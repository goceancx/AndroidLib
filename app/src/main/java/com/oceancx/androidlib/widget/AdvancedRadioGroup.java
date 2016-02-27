package com.oceancx.androidlib.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.IdRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;

/**
 * 大部分代码是从RadioGroup中复制过来的,可以把这个自定义控件当做一个RadioGroup来使用,
 * 只不过它支持孩子节点为ViewGroup,只要孩子里面有RadioButton就可以
 * 注意:在RadioGroup中的孩子RadioButton不能设置onCheckedChangeListener
 * 只能对RadioGroup设置AdvancedRadioGroupCheckChangedListener
 * Created by oceancx on 16/1/7.
 */
public class AdvancedRadioGroup extends FrameLayout {

    public AdvancedRadioGroup(Context context) {
        super(context);
        init();
    }

    public AdvancedRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public interface AdvancedRadioGroupCheckChangedListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked);
    }


    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;


    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {

        if (child instanceof RadioButton) {
            final RadioButton button = (RadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        } else if (child instanceof ViewGroup) {
            addViewGroup((ViewGroup) child, index, params);
            return;
        }
        super.addView(child, index, params);
    }


    public void addViewGroup(ViewGroup parent, int index, ViewGroup.LayoutParams params) {

        RadioButton child = lookforRadioButton(parent);

        if (child instanceof RadioButton) {
            final RadioButton button = (RadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }
        super.addView(parent, index, params);
    }


    private RadioButton lookforRadioButton(ViewGroup child) {
        if (child instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) child;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (viewGroup.getChildAt(i) instanceof RadioButton) {
                    return (RadioButton) viewGroup.getChildAt(i);
                } else {
                    return lookforRadioButton((ViewGroup) viewGroup.getChildAt(i));
                }
            }
        }
        return null;
    }


    /**
     * <p>Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.</p>
     *
     * @param id the unique id of the radio button to select in this group
     * @see #getCheckedRadioButtonId()
     * @see #clearCheck()
     */
    public void check(@IdRes int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(@IdRes int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    /**
     * <p>Returns the identifier of the selected radio button in this group.
     * Upon empty selection, the returned value is -1.</p>
     *
     * @return the unique id of the selected radio button in this group
     * @attr ref android.R.styleable#RadioGroup_checkedButton
     * @see #check(int)
     * @see #clearCheck()
     */
    @IdRes
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    /**
     * <p>Clears the selection. When the selection is cleared, no radio button
     * in this group is selected and {@link #getCheckedRadioButtonId()} returns
     * null.</p>
     *
     * @see #check(int)
     * @see #getCheckedRadioButtonId()
     */
    public void clearCheck() {
        check(-1);
    }

    /**
     * <p>Register a callback to be invoked when the checked radio button
     * changes in this group.</p>
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return AdvancedRadioGroup.class.getName();
    }


    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.</p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group     the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(AdvancedRadioGroup group, @IdRes int checkedId);
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }

            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    public static int id_sand = 12345;

    private class PassThroughHierarchyChangeListener implements
            OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        public void onChildViewAdded(View parent, View child) {
            if (parent == AdvancedRadioGroup.this && child instanceof RadioButton) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                        id = View.generateViewId();
                    }
                    child.setId(id);
                }
                ((RadioButton) child).setOnCheckedChangeListener(
                        mChildOnCheckedChangeListener);
            } else if (parent == AdvancedRadioGroup.this && child instanceof ViewGroup) {
                ViewGroup p = (ViewGroup) child;
                RadioButton radioButton = lookforRadioButton(p);
                if (radioButton != null) {
                    int id = radioButton.getId();
                    if (id == View.NO_ID) {
                        id = id_sand++;
                    }
                    radioButton.setId(id);
                    radioButton.setOnCheckedChangeListener(mChildOnCheckedChangeListener);
                }
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            if (parent == AdvancedRadioGroup.this && child instanceof RadioButton) {
                ((RadioButton) child).setOnCheckedChangeListener(null);
            } else if (parent == AdvancedRadioGroup.this && child instanceof ViewGroup) {
                RadioButton radioButton = lookforRadioButton((ViewGroup) child);
                if (radioButton != null) radioButton.setOnCheckedChangeListener(null);
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
