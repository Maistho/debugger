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
import android.widget.ToggleButton;

//import android.widget.Switch;


/**
 * Fragment containing the bugpicker view
 */
public class BugpickerFragment_B extends Fragment
        implements View.OnClickListener {
    private static final String TITLE = "New Bug";
    private BugpickerListener callback; //callback to activity


    public static BugpickerFragment_B newInstance() {
        return new BugpickerFragment_B();
    }


    /**
     * Interface to communicate bug selection to the activity
     * Activity must implement this interface!
     */
    public interface BugpickerListener {
        void setTitle(String title);
        void onBugPicked(String id, String language, String difficulty);
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

        final View rootView = inflater.inflate(R.layout.fragment_bugpicker_b, container, false);

        Spinner language_spinner = (Spinner) rootView.findViewById(R.id.language_spinner);
        ArrayAdapter<Language> languageArrayAdapter = new ArrayAdapter<Language>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                Language.values());
        languageArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        language_spinner.setAdapter(languageArrayAdapter);

        Spinner difficulty_spinner = (Spinner) rootView.findViewById(R.id.difficulty_spinner);
        ArrayAdapter<Difficulty> difficultyArrayAdapter = new ArrayAdapter<Difficulty>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                Difficulty.values());
        difficultyArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficulty_spinner.setAdapter(difficultyArrayAdapter);



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

        rootView.findViewById(R.id.new_bug_btn).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setTitle(TITLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.new_bug_btn:
                Spinner language = (Spinner) getView().findViewById(R.id.language_spinner);
                Spinner difficulty = (Spinner) getView().findViewById(R.id.difficulty_spinner);

                //TODO: implement id selector
                callback.onBugPicked(
                        null,
                        Language.values()[language.getSelectedItemPosition()].name(),
                        Difficulty.values()[difficulty.getSelectedItemPosition()].name());
                break;
            default:
                break;
        }
    }
}