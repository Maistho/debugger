package com.debugger;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends ActionBarActivity
        implements
        DownloaderFragment.DownloaderListener,
        SubmitterFragment.SubmitterListener,
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        BugpickerFragment.BugpickerListener,
        BuglistFragment.BuglistListener,
        SettingsFragment.SettingsListener,
        EditorFragment.EditorListener {

    private static final int NEW_BUG = 0;
    private static final int RESUME_BUG = 1;
    private static final int SETTINGS = 2;

    // Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private NavigationDrawerFragment mNavigationDrawerFragment;

    // Used to store the last screen title. For use in {@link #restoreActionBar()}.
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }


    void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle action bar item clicks here
     * Clicks on the Home/Up button are handled automatically, as long as
     * you specify a parent activity in AndroidManifest.xml
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    public void swapMainFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.w("swapMainFragment", Integer.toString(fragmentManager.getBackStackEntryCount()));
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void setTitle(String title) {
        mTitle = title;
    }


    /**
     * Callback methods for NavigationDrawerFragment
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Fragment fragment;

        switch (position) {
            case NEW_BUG:
                fragment = BugpickerFragment.newInstance();
                swapMainFragment(fragment);
                break;
            case RESUME_BUG:
                fragment = BuglistFragment.newInstance();
                swapMainFragment(fragment);
                break;
            case SETTINGS:
                fragment = SettingsFragment.newInstance();
                swapMainFragment(fragment);
                break;
            default:
                break;
        }

    }

    @Override
    public void onBugPicked(String id, String language, String difficulty) {
        Log.w("onBugPicked", "enter");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container,
                        DownloaderFragment.newInstance(id, language, difficulty),
                        "downloadProgressBar")
                .commit();
    }

    @Override
    public void onBugDownloaded(Bug bug) {
        Log.w("MainFragment", "Bug download succeeded");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(fragmentManager.findFragmentByTag("downloadProgressBar"))
                .commit();
        startEditor(bug);
    }

    public void onBugDownloadFailed() {
        Log.w("MainFragment", "Bug download failed");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(fragmentManager.findFragmentByTag("downloadProgressBar"))
                .commit();
//        swapMainFragment(BugpickerFragment.newInstance());
        Toast.makeText(this, "Connection Timeout", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResumeBug(Bug bug) {
        startEditor(bug);
    }

    private void startEditor(Bug bug) {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f =  fm.findFragmentById(R.id.editor);
        if (f == null) {
            Log.w("startEditor", "new editor");
            f = EditorFragment.newInstance(bug);
        } else {
            Log.w("startEditor", "old editor");
            ((EditorFragment) f).setBug(bug);
        }

        swapMainFragment(f);
    }


    /**
     * Callback methods for EditorFragment
     */
    @Override
    public void submitBug(Bug bug) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.container,
                        SubmitterFragment.newInstance(bug),
                        "submitProgressBar")
                .commit();
    }

    @Override
    public void onBugSubmitted(JSONObject output) {
        Log.w("Response", output.toString());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(fragmentManager.findFragmentByTag("submitProgressBar"))
                .commit();
        try {
            String tst = output.getString("output");
            String[] tstarr = tst.split("\n");
            Toast.makeText(this, tstarr[tstarr.length-1], Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            //TODO: proper catch
        }
    }
    @Override
    public void onBugSubmitFailed() {
        Log.w("Failed", "aww");
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .remove(fragmentManager.findFragmentByTag("submitProgressBar"))
                .commit();

        Toast.makeText(this, "Connection Timeout", Toast.LENGTH_SHORT).show();
    }

}
