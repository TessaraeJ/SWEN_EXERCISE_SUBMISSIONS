package com.bignerdranch.android.beatbox;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;

public class BeatBoxActivity extends SingleFragmentActivity {
    public final String TAG = "BeatBoxActivity";

    @Override
    protected Fragment createFragment() { return BeatBoxFragment.newInstance(); }

    @Override
    protected void onNewIntent(Intent intent) {
        changeTheme();
        Log.d(TAG, "onNewIntent");
    }
}
