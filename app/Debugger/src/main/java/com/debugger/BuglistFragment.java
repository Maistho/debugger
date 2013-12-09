package com.debugger;


import android.app.Activity;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class BuglistFragment extends ListFragment {

    private static final String TITLE = "Saved Bugs";
    private BuglistListener callback;

    public interface BuglistListener {
        public void setTitle(String title);
    }

    public static BuglistFragment newInstance() {
        return new BuglistFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (BuglistListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement BuglistListener");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        callback.setTitle(TITLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        String[] vals = new String[] {"ABC", "DEF", "GHI", "JKL", "MNO", "PQR", "STU"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, vals);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
    }
}