package com.debugger;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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


interface BugSubmitListener {
    void onPostExecute(JSONObject output);
}

public class SubmitterFragment extends Fragment implements BugSubmitListener {
    private static final String BUG_ID = "id";
    private static final String BUG_CODE = "code";

    private static final String METHOD_POST_BUG = "postBug";
    private static final String OUTPUT = "output";

    private SubmitterListener callback;
    private BugSubmitTask submit = null;

    public interface SubmitterListener {
        void onBugSubmitted(JSONObject output);
        void onBugSubmitFailed();
    }

    public static SubmitterFragment newInstance(Bug bug) {
        Bundle b = new Bundle(2);
        b.putString(BUG_ID, bug.getBugId());
        b.putString(BUG_CODE, bug.getCurrentCode());
        SubmitterFragment fragment = new SubmitterFragment();
        fragment.setArguments(b);
        return fragment;
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callback = (SubmitterListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement SubmitterListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_submitter, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (submit == null) {
            submit = new BugSubmitTask(getActivity(), this);

            JSONObject request = new JSONObject();
            try {
                request.put("method", METHOD_POST_BUG);
                request.put(BUG_ID, getArguments().get(BUG_ID));
                request.put(BUG_CODE, getArguments().get(BUG_CODE));

            } catch (JSONException e) {
                callback.onBugSubmitFailed();
            }

            submit.execute(request);
        }
    }

    /**
     *
     */
    @Override
    public void onPostExecute(JSONObject output) {
        submit = null;
        callback.onBugSubmitted(output);
    }

    /**
     *
     */
    private class BugSubmitTask extends AsyncTask<JSONObject, Void, JSONObject> {

        private static final String SERVER_IP = "109.124.136.140";
        private static final int SERVER_PORT = 56852;

        private final Context context;
        private final BugSubmitListener callback;

        private BugSubmitTask(Context context, BugSubmitListener callback) {
            this.context = context;
            this.callback = callback;
        }

        @Override
        protected JSONObject doInBackground(JSONObject... params) {

            try {
                InetAddress server = InetAddress.getByName(SERVER_IP);
                Socket socket = new Socket(server, SERVER_PORT);
                socket.setSoTimeout(10000); //10 second timeout
                Log.w("SubmitterFragment", "Connected");

                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())), true);
                    out.println(params[0].toString() + "\n");
                    out.flush();

                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                    }

                    return new JSONObject(sb.toString());

                } catch(IOException e) {
                    Log.w("SubmitterFragment", e);
                } catch(JSONException e) {
                    Log.w("SubmitterFragment", e);
                } finally {
                    socket.close();
                }

            } catch (UnknownHostException e) {
                Log.w("SubmitterFragment", "Unable to connect to host");
                Log.w("SubmitterFragment", e);
            } catch (SocketException e) {
                Log.w("SubmitterFragment", "Server connection failed");
                Log.w("SubmitterFragment", e);
            } catch (IOException e) {
                Log.w("SubmitterFragment", e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject output) {
            callback.onPostExecute(output);
        }
    }
}