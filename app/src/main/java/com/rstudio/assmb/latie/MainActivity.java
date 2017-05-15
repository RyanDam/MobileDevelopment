package com.rstudio.assmb.latie;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.webkit.URLUtil;
import android.widget.Toast;
import com.rstudio.assmb.latie.contentfragment.ItemModelFragment;
import com.rstudio.assmb.latie.contentfragment.dummy.AllItemContent;
import com.rstudio.assmb.latie.contentfragment.dummy.ArchivedItemContent;
import com.rstudio.assmb.latie.contentfragment.dummy.DatabaseHandler;
import com.rstudio.assmb.latie.contentfragment.dummy.DummyContent;
import com.rstudio.assmb.latie.contentfragment.dummy.StaredItemContent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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

        mAllItemContentFragment = ItemModelFragment.newInstance(1, false);
        mStaredItemContentFragment = ItemModelFragment.newInstance(1, false);
        mArchiveItemContentFragment = ItemModelFragment.newInstance(1, true);

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.fragment_container, mAllItemContentFragment).commit();

        mNavigation = ((BottomNavigationView) findViewById(R.id.navigation));
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        receivedContent();


        mArchiveItemContentFragment.setContent(new ArchivedItemContent());
        mAllItemContentFragment.setContent(new AllItemContent());
        mStaredItemContentFragment.setContent(new StaredItemContent());
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    DatabaseHandler dbHandler = new DatabaseHandler(this);

                    if(Patterns.WEB_URL.matcher(receivedText).matches()) {

                        if(receivedText.startsWith("http://") || receivedText.startsWith("https://")) {
                            DownloadHTML handle = new DownloadHTML(this,receivedText,dbHandler);
                            handle.execute(receivedText);

                        }else {
                            String newReceived = "http://".concat(receivedText);
                            DownloadHTML handle = new DownloadHTML(this,newReceived,dbHandler);
                            handle.execute(newReceived);
                        }



//                       Toast.makeText(this, "URL", Toast.LENGTH_SHORT).show();

                    }else {

                        Date date = new Date();
                        long dateTime = Long.valueOf(date.getTime());
                        DummyContent.DummyItem newItem = new DummyContent.DummyItem("1","Received Text",receivedText,"",false,dateTime);
                        dbHandler.addDummyItem(newItem);

//                        Toast.makeText(this, "TExxt", Toast.LENGTH_SHORT).show();


                        reloadData();
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

    public void reloadData(){

        mAllItemContentFragment.reloadData();
        mArchiveItemContentFragment.reloadData();
        mStaredItemContentFragment.reloadData();

    }


}

