package com.rstudio.assmb.latie;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import com.rstudio.assmb.latie.contentfragment.dummy.DummyContent;

import org.w3c.dom.Text;

public class ContentDetailActivity extends AppCompatActivity {

    private DummyContent.DummyItem mItem;
    private TextView mIdView;
    private TextView mDateView;
    private TextView mContent;
    private WebView mWebView;
    private CollapsingToolbarLayout barLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);

        Toolbar toolbar = ((Toolbar) findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        barLayout = ((CollapsingToolbarLayout) findViewById(R.id.collapsingToolbar));
        barLayout.setExpandedTitleTextAppearance(R.style.ExpandedTitle);
        barLayout.setCollapsedTitleTextAppearance(R.style.CollapsedTitle);
        barLayout.setTitle("Article Detail");

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        mIdView = ((TextView) findViewById(R.id.id));
        mDateView = ((TextView) findViewById(R.id.date));
        mContent = ((TextView) findViewById(R.id.content));
        mWebView = ((WebView) findViewById(R.id.webview));

        mWebView.getSettings().setJavaScriptEnabled(true);

        Bundle itemBundle = getIntent().getBundleExtra("ITEM");
        mItem = new DummyContent.DummyItem(itemBundle);

        setupItemView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if (mItem.isLiked) {
            getMenuInflater().inflate(R.menu.menu_flag_filled, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_flag, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.liked:
                // like article
                mItem.isLiked = !mItem.isLiked;
                supportInvalidateOptionsMenu();
                break;
            case R.id.like:
                // unlike article
                mItem.isLiked = !mItem.isLiked;
                supportInvalidateOptionsMenu();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setupItemView() {
        barLayout.setTitle(mItem.title);
        mIdView.setText(mItem.title);
        mDateView.setText(mItem.getDateTime() + " " + mItem.originLink);
        mContent.setText(mItem.content);

        mWebView.loadDataWithBaseURL("", mItem.content, "text/html", "UTF-8", "");

        new Utils.ReadFileASync(this, new Utils.OnReadStringCallBack() {
            @Override
            public void onReadSuccess(String ret) {
                mWebView.loadDataWithBaseURL("", ret, "text/html", "UTF-8", "");
            }
        }).execute(R.raw.temp);

    }
}
