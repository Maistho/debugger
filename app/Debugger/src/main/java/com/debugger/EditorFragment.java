package com.debugger;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.support.v4.view.MenuItemCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class EditorFragment extends Fragment {

    public static final int MENU_SUBMIT = Menu.FIRST;
    private Bug bug;
    private EditorListener callback;


    /**
     * Interface to communicate editor changes to the activity
     * Activity must implement this interface!
     *
     * TODO: Implement callback functions for EditorFragment
     */
    public interface EditorListener {
        void setTitle(String title);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //This fragment wants to populate the options menu
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        //useful reading: supportInvalidateOptionsMenu - force refresh ActionBar menu
        //inflater.inflate(R.menu.activity_itemdetail, menu); - add xml MenuItem

        MenuItem item = menu.add(Menu.NONE, MENU_SUBMIT, Menu.NONE, "Submit");
        MenuItemCompat.setShowAsAction(item,
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_SUBMIT:
                //TODO: Placeholder action for submit button
                Toast.makeText(getActivity(), "Submit code - NYI", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /**
     * onAttach - Called when attached to activity
     * Checks to make sure container implements callback interface
     */
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (EditorListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
            + " must implement EditorListener");
        }

        ((MainActivity) activity).setTitle(bug.toString());
    }

    /**
     * Constructor for new EditorFragments
     */
    public EditorFragment() {}

    /**
     * newInstance
     * @param bug - Bug to edit
     * @return - newly created EditorFragment
     *
     * TODO: Set bug on existing fragment with separate method?
     * Allows reusing of one EditorFragment
     */
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