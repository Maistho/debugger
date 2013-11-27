package com.debugger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class EditorFragment extends Fragment {
    private Bug bug;
    private OnEditorEventListener callback;

    /**
     * Interface to communicate editor changes to the activity
     * Activity must implement this interface!
     */
    public interface OnEditorEventListener {
        //Callback interface - Add methods for activity communication here!
    }

    /**
     * onAttach - Called when attached to activity
     * Checks to make sure container implements callback interface
     */
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (OnEditorEventListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
            + " must implement OnEditorEventListener");
        }
    }

    public EditorFragment() {
    }

    public static Fragment newInstance(Bug bug) {
        EditorFragment fragment = new EditorFragment();
        fragment.bug = bug;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor, container, false);
        EditText code = (EditText) rootView.findViewById(R.id.code_field);
        code.setText(bug.getOriginalCode());
        return rootView;
    }


}