package com.debugger;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;


public class BugDownloader {
    public Bug getBugById(String id) {
        return null;
    }

    public Bug getBugByCondition(String a, String b) {
        return null;
    }

    public Bug getRandomBug() {
        new RandomBugDownloaderTask().execute();
        return null;
    }



    private abstract class BugDownloaderTask extends AsyncTask<Void,Void,Bug> {
        private static final String SERVER_IP = "192.168.0.1";
        private static final int SERVER_PORT = 1234;

        private static final String BUG_ID = "id";
        private static final String BUG_LANGUAGE = "language";
        private static final String BUG_DIFFICULTY = "difficulty";

        private Bug downloadBug(String id, String language, String difficulty) {
            Bug bug;
            JSONObject request = new JSONObject();

            try {
                request.put(BUG_ID, id);
                request.put(BUG_LANGUAGE, language);
                request.put(BUG_DIFFICULTY, difficulty);
            } catch (JSONException e) {
                //TODO: Return unsuccessfully from bug
            }

            try {
                InetAddress server = InetAddress.getByName(SERVER_IP);
                Socket socket = new Socket(server, SERVER_PORT);

                try {
                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                            socket.getOutputStream())), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            socket.getInputStream()));

                    if (!out.checkError()) {
                        out.println(request.toString());
                        out.flush();
                    } else {
                        //TODO: Return from error
                    }

                    //TODO: add timeout
                    

                } catch (Exception e) {
                    //
                } finally {
                    socket.close();
                }

            } catch (Exception e) {
                //
            }

            return null;
        }


        @Override
        protected void onPostExecute(Bug b) {
            super.onPostExecute(b);
        }
    }

    private class RandomBugDownloaderTask extends AsyncTask<String,Integer,Bug> {

        @Override
        protected Bug doInBackground(String... params) {
            return null;
        }
    }
}
