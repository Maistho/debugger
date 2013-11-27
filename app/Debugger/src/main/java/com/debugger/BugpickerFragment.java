package com.debugger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
//import android.widget.Switch;
import android.widget.ToggleButton;


/**
 * Fragment containing the bugpicker view
 */
public class BugpickerFragment extends Fragment
        implements View.OnClickListener {

    //callback to container activity
    private OnBugPickedListener callback;

    /**
     * Currently available languages
     */
    public enum Language {
        PYTHON2 ("Python 2.7", "2.7.6");
        /*
        PYTHON3 ("Python 3.3", "3.3.3"),
        JAVA ("Java 7", "1.7.45"),
        CPP ("C++11", "C++11"),
        RUBY ("Ruby 2.0", "2.0.0-p353");
        */

        private String name;
        private String version;

        Language(String name, String version) {
            this.name = name;
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public String toString() {
            return name + " - " + version;
        }
    }


    /**
     * Interface to communicate bug selection to the activity
     * Activity must implement this interface!
     */
    public interface OnBugPickedListener {
        public void onRandomBugPicked();
        // TODO: Add arguments for language and difficulty
        public void onConditionedBugPicked();
        // TODO: Specific bug picker
        //public void onSpecificBugPicked(String);
    }

    /**
     * onAttach - Called when attached to activity
     * Checks to make sure container implements callback interface
     */
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (OnBugPickedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnBugPickedListener");
        }
    }

    /**
     * Constructor for new BugpickerFragments
     */
    private BugpickerFragment() {}

    /**
     * newInstance
     * @return Newly created fragment
     */
    public static BugpickerFragment newInstance() {
        BugpickerFragment fragment = new BugpickerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_bugpicker, container, false);

        // TODO: replace content with enum Language (top of this file)
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_bug_btn:
                /**
                 * TODO: Lift data from Language and Difficulty selectors
                 * Use Language and Difficulty to decide between onRandomBugPicked,
                 * onConditionedBugPicked, and onSpecificBugPicked
                 */
                callback.onRandomBugPicked();
                break;
            default:
                // TODO: throw exception - no handler for View v
                break;
        }
    }

}