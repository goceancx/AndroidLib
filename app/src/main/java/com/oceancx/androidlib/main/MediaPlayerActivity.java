package com.oceancx.androidlib.main;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.oceancx.androidlib.R;

import java.io.File;
import java.io.IOException;


/**
 * Created by oceancx on 16/2/27.
 */
public class MediaPlayerActivity extends AppCompatActivity implements View.OnClickListener {


    MediaPlayer mMediaPlayer;
    String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "1.mp3";
    Button play_bt, pause_bt, stop_bt;
    private int state = STATE_PLAY;
    private static final int STATE_PLAY = 1;
    private static final int STATE_PAUSE = 2;
    private static final int STATE_STOP = 3;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.media_player_activity);
        play_bt = (Button) findViewById(R.id.play_bt);
        pause_bt = (Button) findViewById(R.id.pause_bt);
        stop_bt = (Button) findViewById(R.id.stop_bt);

        play_bt.setOnClickListener(this);
        pause_bt.setOnClickListener(this);
        stop_bt.setOnClickListener(this);

        mMediaPlayer = new MediaPlayer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaPlayer.release();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.play_bt: {
                try {

                    if (state == STATE_PAUSE || state == STATE_STOP) {
                        mMediaPlayer.start();
                    } else {
                        mMediaPlayer.setDataSource(this, Uri.parse(path));
                        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        mMediaPlayer.prepare();
                        mMediaPlayer.start();
                    }
                    state = STATE_PLAY;
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.pause_bt: {
                mMediaPlayer.pause();
                state = STATE_PAUSE;

                break;
            }
            case R.id.stop_bt: {
                mMediaPlayer.stop();
                mMediaPlayer.reset();
                state = STATE_STOP;

                break;
            }

        }

    }
}
