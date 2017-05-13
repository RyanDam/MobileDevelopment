package com.rstudio.assmb.latie;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.rstudio.assmb.latie.contentfragment.dummy.DatabaseHandler;
import com.rstudio.assmb.latie.contentfragment.dummy.DummyContent;
import com.rstudio.assmb.latie.parser.HTMLParser;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;

/**
 * Created by admin on 5/7/17.
 */

public class DownloadHTML extends AsyncTask <String, Void , String> {
    private static final String LOG_TAG = "HAHAHAHA";
    Context context;
    private String returnStr;
    private String url;

    public DownloadHTML( Context context,String url){

        this.context = context;
        this.url = url;
    }
    protected String doInBackground(String... urlStr){
        // do stuff on non-UI thread
        StringBuffer htmlCode = new StringBuffer();
        try{
            URL url = new URL(urlStr[0]);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                htmlCode.append(inputLine);
                Log.d(LOG_TAG, "html: " + inputLine);
            }

            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(LOG_TAG, "Error: " + e.getMessage());
            Log.d(LOG_TAG, "HTML CODE: " + htmlCode);
        }
        return htmlCode.toString();
    }

    @Override
    protected void onPostExecute(String htmlCode){
        // do stuff on UI thread with the html

        HTMLParser parser = new HTMLParser(htmlCode);
        parser.getContent();
        Date date = new Date();
        long dateTime = Long.valueOf(date.getTime());
        DummyContent.DummyItem newItem = new DummyContent.DummyItem("26",parser.getTitle(),parser.getContent(),url,false,dateTime);
        DatabaseHandler dbHandler = new DatabaseHandler(context);
        dbHandler.addDummyItem(newItem);
        DummyContent.DummyItem  data = dbHandler.getDummyItemById("25");


        Log.d(LOG_TAG,"HMTL HERE"+data.content);
    }
}



