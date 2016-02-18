package com.oceancx.androidlib.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.oceancx.androidlib.R;
import com.oceancx.rangeseekbar.RangeSeekBar;

/**
 * Created by oceancx on 16/2/2.
 */
public class RangeSeekActivity extends AppCompatActivity {
    RangeSeekBar seekBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.range_seekbar_activity);
        seekBar = (RangeSeekBar) findViewById(R.id.seekbar);
    }

}
