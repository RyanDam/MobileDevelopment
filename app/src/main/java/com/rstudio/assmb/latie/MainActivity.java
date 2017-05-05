package com.rstudio.assmb.latie;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.rstudio.assmb.latie.contentfragment.ItemModelFragment;
import com.rstudio.assmb.latie.contentfragment.dummy.DummyContent;

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
                Toast.makeText(this, "You received text!", Toast.LENGTH_SHORT).show();
                String receivedText = receivedIntent.getStringExtra(Intent.EXTRA_TEXT);
                if(receivedText != null) {
                    Toast.makeText(this, receivedText, Toast.LENGTH_SHORT).show();
                }
            }
        }else if(receivedAction.equals(Intent.ACTION_MAIN)){
            //app has been launched directly, not from share list
            //Toast.makeText(this, "HAHAHAHAHAHAHAHAHA", Toast.LENGTH_SHORT).show();
        }
    }

    public void onListFragmentInteraction(DummyContent.DummyItem item) {
        Intent intent = new Intent(this, ContentDetailActivity.class);

        Bundle itemBundle = item.toBundle();
        intent.putExtra("ITEM", itemBundle);

        startActivity(intent);
    }

}
