package com.rstudio.assmb.latie;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.rstudio.assmb.latie.contentfragment.ItemModelFragment;
import com.rstudio.assmb.latie.contentfragment.dummy.DummyContent;
import com.rstudio.assmb.latie.parser.HTMLParser;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

public class MainActivity extends AppCompatActivity implements ItemModelFragment.OnListFragmentInteractionListener {

    private String TAG = "MainActivity";

    private BottomNavigationView mNavigation;
    private ItemModelFragment mAllItemContentFragment;
    private ItemModelFragment mStaredItemContentFragment;
    private ItemModelFragment mArchiveItemContentFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager manager = getSupportFragmentManager();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    manager.beginTransaction().replace(R.id.fragment_container, mAllItemContentFragment).commit();
                    return true;
                case R.id.navigation_dashboard:
                    manager.beginTransaction().replace(R.id.fragment_container, mStaredItemContentFragment).commit();
                    return true;
                case R.id.navigation_notifications:
                    manager.beginTransaction().replace(R.id.fragment_container, mArchiveItemContentFragment).commit();
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAllItemContentFragment = ItemModelFragment.newInstance(1);
        mStaredItemContentFragment = ItemModelFragment.newInstance(2);
        mArchiveItemContentFragment = ItemModelFragment.newInstance(3);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, mAllItemContentFragment).commit();

        mNavigation = ((BottomNavigationView) findViewById(R.id.navigation));
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        receivedContent();

    }

    public void receivedContent() {
        //get the received intent
        Intent receivedIntent = getIntent();
        String receivedAction = receivedIntent.getAction();
        //find out what we are dealing with
        String receivedType = receivedIntent.getType();
        if(receivedAction.equals(Intent.ACTION_SEND)){
            //content is being shared
            if(receivedType.startsWith("text/")){
                sendNotification();
                Toast.makeText(this, "You received text!", Toast.LENGTH_SHORT).show();
                String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                if(receivedText != null) {
                    String abc="";
                    if(URLUtil.isValidUrl(receivedText)) {
                        DownloadHTML handle = new DownloadHTML(this,receivedText);
                        handle.execute(receivedText);
                        Log.d("HEHEHE",abc);
                        Toast.makeText(this, "URL", Toast.LENGTH_SHORT).show();

                    }else {
                        Toast.makeText(this, receivedText, Toast.LENGTH_SHORT).show();
                    }


                }
            }
        }else if(receivedAction.equals(Intent.ACTION_MAIN)){
            //app has been launched directly, not from share list
            //Toast.makeText(this, "HAHAHAHAHAHAHAHAHA", Toast.LENGTH_SHORT).show();
        }
    }
    public void sendNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_home_black_24dp)
                        .setContentTitle("You has received something!")
                        .setContentText("This is your content");
        Intent notificationInetent = new Intent(this, MainActivity.class);
        notificationInetent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |  Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this,0, notificationInetent,PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);


        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(0, mBuilder.build());


    }

    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Intent intent = new Intent(this, ContentDetailActivity.class);

        Bundle itemBundle = item.toBundle();
        intent.putExtra("ITEM", itemBundle);

        startActivity(intent);
    }



}

