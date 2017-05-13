package com.rstudio.assmb.latie;

import android.content.Context;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Ryan on 4/15/17.
 */

public class Utils {

    public interface OnReadStringCallBack {
        void onReadSuccess(String ret);
    }

    public static class ReadFileASync extends AsyncTask<Integer, Void, String> {

        private Context mContext;
        private OnReadStringCallBack mCb;

        public ReadFileASync(Context context, OnReadStringCallBack cb) {
            this.mContext = context;
            this.mCb = cb;
        }

        @Override
        protected String doInBackground(Integer... ints) {
            int id = ints[0];

            String ret;

            try {
                Resources res = mContext.getResources();
                InputStream in_s = res.openRawResource(id);

                byte[] b = new byte[in_s.available()];
                in_s.read(b);

                ret = new String(b);

                in_s.close();

            } catch (Exception e) {

                ret = e.getMessage();
            }

            return ret;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (mCb != null) {
                mCb.onReadSuccess(s);
            }
        }
    }

    public static String connect(String url) {

        HttpClient httpclient = new DefaultHttpClient();

        // Prepare a request object
        HttpGet httpget = new HttpGet(url);

        // Execute the request
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);
            // Examine the response status
            Log.i("Praeda",response.getStatusLine().toString());

            // Get hold of the response entity
            HttpEntity entity = response.getEntity();
            // If the response does not enclose an entity, there is no need
            // to worry about connection release

            if (entity != null) {

                // A Simple JSON Response Read
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                // now you have the string representation of the HTML request
                instream.close();

                return result;
            }

            return null;
        } catch (Exception e) {
            return null;
        }
    }

    private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;

        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

}
