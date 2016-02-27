package com.oceancx.androidlib.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.oceancx.androidlib.R;
import com.oceancx.rangeseekbar.RangeSeekBar;

/**
 * Created by oceancx on 16/2/2.
 */
public class RangeSeekActivity extends AppCompatActivity {
    RangeSeekBar seekBar;

    int state = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.range_seekbar_activity);
        seekBar = (RangeSeekBar) findViewById(R.id.seekbar);

    }


    public void onToggleButtonClick(View v) {
        if (state == 0) {
            Toast.makeText(RangeSeekActivity.this, "state:" + state, Toast.LENGTH_SHORT).show();
            state++;
            seekBar.setSeekRange(RangeSeekBar.MODE_TIME, 0, 24 * 60 * 60, 10 * 60 * 60, 20 * 60 * 60);
        } else if (state == 1) {
            Toast.makeText(RangeSeekActivity.this, "state:" + state, Toast.LENGTH_SHORT).show();
            state++;
            seekBar.setSeekRange(RangeSeekBar.MODE_MAX_MIN_VALUE, 9, 200, 10, 20);
        } else {
            state = 0;
            Toast.makeText(RangeSeekActivity.this, "min:" + seekBar.getInitMinValue() + " \t max:" + seekBar.getInitMaxValue(), Toast.LENGTH_SHORT).show();
        }
    }
}
