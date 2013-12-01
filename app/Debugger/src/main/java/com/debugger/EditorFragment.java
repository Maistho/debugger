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
    public static final int MENU_REDO = Menu.FIRST+1;
    public static final int MENU_UNDO = Menu.FIRST+2;

    private Bug bug;
    private EditorListener callback;
    private EditText code;
    private UndoRedoEnabler undoRedoEnabler;

    /**
     * Private constructor, use newInstance
     */
    public EditorFragment() {}

    public static Fragment newInstance(Bug bug) {
        EditorFragment fragment = new EditorFragment();
        fragment.bug = bug;
        return fragment;
    }

    /**
     * Make sure container implements callback interface
     */
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (EditorListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement EditorListener");
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
            bug = savedInstanceState.getParcelable("bug");


        //This fragment wants to populate the options menu
        setHasOptionsMenu(true);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem undo = menu.add(Menu.NONE, MENU_UNDO, Menu.NONE, "Undo");
        MenuItemCompat.setShowAsAction(undo,
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);

        MenuItem redo = menu.add(Menu.NONE, MENU_REDO, Menu.NONE, "Redo");
        MenuItemCompat.setShowAsAction(redo,
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);


        MenuItem submit = menu.add(Menu.NONE, MENU_SUBMIT, Menu.NONE, "Submit");
        MenuItemCompat.setShowAsAction(submit,
                MenuItemCompat.SHOW_AS_ACTION_ALWAYS | MenuItemCompat.SHOW_AS_ACTION_WITH_TEXT);

    }

    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("bug", bug);
    }



    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case MENU_SUBMIT:
                //TODO: Placeholder action for submit button
                Toast.makeText(getActivity(), "Submit code - NYI", Toast.LENGTH_SHORT).show();
                return true;
            case MENU_REDO:
                undoRedoEnabler.redo();
                //Toast.makeText(getActivity(), "Redo - NYI", Toast.LENGTH_SHORT).show();
                return true;
            case MENU_UNDO:
                undoRedoEnabler.undo();
                //Toast.makeText(getActivity(), "Undo - NYI", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_editor, container, false);
        if (rootView != null) {
            code = (EditText) rootView.findViewById(R.id.code_field);
            code.setText(bug.getCurrentCode());
            undoRedoEnabler = new UndoRedoEnabler(code);
            callback.setTitle(bug.toString());
        }
        return rootView;
    }

    public void onDestroyView() {
        bug.setCurrentCode(code.getText().toString());
        super.onDestroyView();
    }

    /**
     * Interface to communicate editor changes to the activity
     * Activity must implement this interface!
     *
     * //TODO: callback method for "Submit" button
     */
    public interface EditorListener {
        void setTitle(String title);
        //void submitBug(bug);
    }
}