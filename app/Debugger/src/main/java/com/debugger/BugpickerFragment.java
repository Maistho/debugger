package com.debugger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
//import android.widget.Switch;
import android.widget.ToggleButton;


/**
 * Fragment containing the bugpicker view
 */
public class BugpickerFragment extends Fragment
        implements View.OnClickListener {
    private static final String TITLE = "New Bug";
    private BugpickerListener callback; //callback to activity


    public static BugpickerFragment newInstance() {
        BugpickerFragment fragment = new BugpickerFragment();
        return fragment;
    }


    /**
     * Interface to communicate bug selection to the activity
     * Activity must implement this interface!
     */
    public interface BugpickerListener {
        void setTitle(String title);
        void onRandomBugPicked();
        //void onConditionedBugPicked(String language, String difficulty);
        // TODO: Add arguments for language and difficulty
        //public void onSpecificBugPicked(String);
        // TODO: Specific bug picker
    }

    /**
     * onAttach - Called when attached to activity
     * Checks to make sure container implements callback interface
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (BugpickerListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement BugpickerListener");
        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_bugpicker, container, false);

        // TODO: replace content with enum Language
        Spinner language_spinner = (Spinner) rootView.findViewById(R.id.language_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.language_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language_spinner.setAdapter(adapter);

        // TODO: replace content with enum Difficulty (NYI)
        Spinner difficulty_spinner = (Spinner) rootView.findViewById(R.id.difficulty_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.difficulty_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty_spinner.setAdapter(adapter);


        ToggleButton random_switch = (ToggleButton) rootView.findViewById(R.id.randomness_switch);
        random_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    rootView.findViewById(R.id.random_bug).setVisibility(View.GONE);
                    rootView.findViewById(R.id.chosen_bug).setVisibility(View.VISIBLE);
                } else {
                    rootView.findViewById(R.id.chosen_bug).setVisibility(View.GONE);
                    rootView.findViewById(R.id.random_bug).setVisibility(View.VISIBLE);
                }
            }
        });

        // Set new_bug_btn onClick handler to this.onClick(View)
        rootView.findViewById(R.id.new_bug_btn).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setTitle(TITLE);
    }

    //TODO: Could be inner class?
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_bug_btn:
                /**
                 * TODO: Lift data from Language and Difficulty selectors
                 * Use Language and Difficulty to decide between onRandomBugPicked,
                 * onConditionedBugPicked, and onSpecificBugPicked
                 */
                callback.onRandomBugPicked();
                //callback.onConditionedBugPicked(language, difficulty);
                //callback.onSpecificBugPicked(id);
                break;
            default:
                //No handler for View
                break;
        }
    }

}