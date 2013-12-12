package com.debugger;


import android.app.Activity;
import android.content.Context;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.sql.SQLException;
import java.util.List;

// TODO: implement performance enhancements from
// http://www.vogella.com/articles/AndroidListView/article.html

public class BuglistFragment extends ListFragment {

    private static final String TITLE = "Saved Bugs";
    private BuglistListener callback;
    private List<Bug> bugList;

    public interface BuglistListener {
        public void setTitle(String title);
        public void onResumeBug(Bug bug);
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

        BugDataSource dataSource = new BugDataSource(getActivity());

        try {
            dataSource.open();
            bugList = dataSource.getAllBugs();
            dataSource.close();

            BugArrayAdapter adapter = new BugArrayAdapter(getActivity(), bugList);
            setListAdapter(adapter);
        } catch (SQLException e) {

        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        callback.onResumeBug(bugList.get(position));
    }

    private class BugArrayAdapter extends ArrayAdapter<Bug> {
        private final Context context;
        private final List<Bug> bugs;

        public BugArrayAdapter(Context context, List<Bug> bugs) {
            super(context, R.layout.layout_bug_list_item, bugs);
            this.context = context;
            this.bugs = bugs;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            View listItem = inflater.inflate(R.layout.layout_bug_list_item, parent, false);

            TextView label = (TextView) listItem.findViewById(R.id.label);
            label.setText(bugs.get(position).getBugId());
            TextView description = (TextView) listItem.findViewById(R.id.description);
            description.setText(bugs.get(position).getLanguage().toString());

            return listItem;
        }
    }

}