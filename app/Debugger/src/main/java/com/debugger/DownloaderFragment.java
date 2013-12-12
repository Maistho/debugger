package com.debugger;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.SQLException;


interface BugDownloadListener {
    void onPostExecute(Bug bug);
}

public class DownloaderFragment extends Fragment implements BugDownloadListener {
    private static final String BUG_ID = "id";
    private static final String BUG_LANGUAGE = "language";
    private static final String BUG_DIFFICULTY = "difficulty";
    private static final String BUG_CODE = "code";

    private static final String METHOD_GET_BUG = "getBug";

    private DownloaderListener callback;
    private BugDownloadTask download = null;

    public interface DownloaderListener {
        void onBugDownloaded(Bug bug);
        void onBugDownloadFailed();
    }

    public static DownloaderFragment newInstance(String id, String language, String difficulty) {
        Bundle b = new Bundle(3);
        b.putString(BUG_ID, id);
        b.putString(BUG_LANGUAGE, language);
        b.putString(BUG_DIFFICULTY, difficulty);
        DownloaderFragment fragment = new DownloaderFragment();
        fragment.setArguments(b);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (DownloaderListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DownloaderListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_downloader, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.w("DownloadFragment.onActivityCreated", "entered");

        if (download == null) {
            download = new BugDownloadTask(getActivity(), this);

            JSONObject request = new JSONObject();
            try {
                request.put("method", METHOD_GET_BUG);
                request.put(BUG_ID, getArguments().get(BUG_ID));
                request.put(BUG_LANGUAGE, getArguments().get(BUG_LANGUAGE));
                request.put(BUG_DIFFICULTY, getArguments().get(BUG_DIFFICULTY));

            } catch (JSONException e) {
                Log.w("DownloadFragment.onActivityCreated", e.toString());
                callback.onBugDownloadFailed();
            }

            Log.w("DownloadFragment.onActivityCreated", request.toString());
            download.execute(request);
        }
    }

    /**
     *
     */
    @Override
    public void onPostExecute(Bug bug) {
        download = null;
        if (bug != null) {
            callback.onBugDownloaded(bug);
        } else {
            callback.onBugDownloadFailed();
        }
    }

    /**
     *
     */
    private class BugDownloadTask extends AsyncTask<JSONObject, Void, Bug> {

        private static final String SERVER_IP = "109.124.136.140";
        private static final int SERVER_PORT = 56852;

        private final Context context;
        private final BugDownloadListener callback;

        private BugDownloadTask(Context context, BugDownloadListener callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected Bug doInBackground(JSONObject... params) {

            try {
                InetAddress server = InetAddress.getByName(SERVER_IP);
                Socket socket = new Socket(server, SERVER_PORT);
                socket.setSoTimeout(10000); //10 second timeout
                Log.w("DownloadFragment", "Connected");

                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())), true);
                    out.println(params[0].toString() + "\n");
                    out.flush();
                    Log.w(params[0].toString(), "cool");

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        Log.w("download", line);
                    }


                    JSONObject jsonBug = new JSONObject(sb.toString());
                    BugDataSource bds = new BugDataSource(context);
                    try {
                        bds.open();
                        return bds.createBug(jsonBug.getString(BUG_ID),
                                jsonBug.getString(BUG_LANGUAGE),
                                jsonBug.getString(BUG_CODE)
                        );

                    } catch (SQLException e) {
                        Log.w("DownloaderFragment", "Unable to establish database connection");
                        Log.w("DownloaderFragment", e);
                    } finally {
                        bds.close();
                    }

                } catch(IOException e) {
                    Log.w("DownloaderFragment", e);
                } catch(JSONException e) {
                    Log.w("DownloaderFragment", e);
                } finally {
                    socket.close();
                }

            } catch (UnknownHostException e) {
                Log.w("DownloaderFragment", "Unable to connect to host");
                Log.w("DownloaderFragment", e);
            } catch (SocketException e) {
                Log.w("DownloaderFragment", "Server connection failed");
                Log.w("DownloaderFragment", e);
            } catch (IOException e) {
                Log.w("DownloaderFragment", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bug bug) {
            callback.onPostExecute(bug);
        }
    }
}