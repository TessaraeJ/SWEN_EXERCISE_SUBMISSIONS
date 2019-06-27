package com.bignerdranch.android.beatbox;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {
    static MediaPlayer mMediaPlayer;
    protected abstract Fragment createFragment();

    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            manager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit();
        }
        backgroundMusic();
    }

    protected void backgroundMusic(){
        mMediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.start();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();

        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
        mMediaPlayer.release();
    }

}
