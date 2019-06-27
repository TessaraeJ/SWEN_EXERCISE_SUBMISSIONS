package com.bignerdranch.android.beatbox;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v14.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat {

    public final String TAG = "Settings";
    public static SingleFragmentActivity hostActivity;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, getHost().toString());

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        addPreferencesFromResource(R.xml.preferences);
        findPreference("theme_select").setOnPreferenceChangeListener(preferenceChangeListener);
    }



    private Preference.OnPreferenceChangeListener preferenceChangeListener = new Preference.OnPreferenceChangeListener() {
//        public SingleFragmentActivity hostActivity;
//
//        public void setHostActivity(SingleFragmentActivity hostActivity) {
//            this.hostActivity = hostActivity;
//        }


        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            Log.i(TAG, preference.toString() + " : " + newValue.toString());
            SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
            String theme = pref.getString("theme_select", "Dark Theme");
            Log.i(TAG, "THEME IS: " + theme);
            String stringVal = newValue.toString();
            if (preference instanceof ListPreference){
                ListPreference listPreference = (ListPreference) preference;
                int selectedIndex = listPreference.findIndexOfValue(stringVal);
                Log.d("TAG", listPreference.getEntries()[selectedIndex].toString());

                Intent intent = new Intent(getContext(), BeatBoxActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }

            return true;
        }
    };

}
