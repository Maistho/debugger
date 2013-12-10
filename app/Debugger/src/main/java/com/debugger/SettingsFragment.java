package com.debugger;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class SettingsFragment extends Fragment {
    private static final String TITLE = "Settings";
    private SettingsListener callback; //callback to activity

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        return fragment;
    }

    public interface SettingsListener {
        void setTitle(String title);
    }

    /**
     * Make sure Activity implements callback interface
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (SettingsListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BugpickerListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setTitle(TITLE);
    }

}