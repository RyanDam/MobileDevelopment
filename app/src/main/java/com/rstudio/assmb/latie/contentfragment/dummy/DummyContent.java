package com.rstudio.assmb.latie.contentfragment.dummy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.rstudio.assmb.latie.parser.HTMLParser;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DummyContent {
    private String TAG = "MainActivity";

    /**
     * An array of sample (dummy) items.
     */
    public ArrayList<DummyItem> ITEMS;

    /**
     * A map of sample (dummy) items, by ID.
     */
    public Map<String, DummyItem> ITEM_MAP;

    //private static final int COUNT = 25;

    public DummyContent() {
        ITEMS = new ArrayList<DummyItem>();
        ITEM_MAP = new HashMap<String, DummyItem>();
    }

    public void initData(Context context) {
        ITEMS.clear();
        DatabaseHandler handler = new DatabaseHandler(context);
        List<DummyItem> list = handler.getAllDummyItem();
        Log.d("LENGTH",String.valueOf(list.size()));
        for (int i = list.size(); i > 1; i--) {
            addItem(handler.getDummyItemById(String.valueOf(i)));
        }
    }

    protected void addItem(DummyItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    protected DummyItem createDummyItem(int position) {
        Date date = new Date();
        return new DummyItem(String.valueOf(position)
                , "Item " + position
                , makeDetails(position)
                , "http://abc.com/aS4d"
                , true
                , date.getTime());
    }

    protected String makeDetails(int position) {
        StringBuilder builder = new StringBuilder();
        builder.append("Details about Item: ").append(position);
        for (int i = 0; i < position; i++) {
            builder.append("\nMore details information here.");
        }
        return builder.toString();
    }

    /**
     * A dummy item representing a piece of content.
     */
    public static class DummyItem {
        public final String id;
        public final String content;
        public final String title;
        public final String originLink;
        public final long time;
        public boolean isLiked;

        public DummyItem(String id, String title, String content, String originLink, boolean isLiked, long time) {
            this.id = id;
            this.title = title;
            this.content = content;
            this.originLink = originLink;
            this.time = time;
            this.isLiked = isLiked;
        }

        public DummyItem(Bundle bundle) {
            this(bundle.getString("ID")
                    , bundle.getString("DETAILS")
                    , bundle.getString("CONTENT")
                    , bundle.getString("ORIGIN_LINK")
                    , bundle.getBoolean("LIKED")
                    , bundle.getLong("TIME"));
        }

        public String getSummary() {
            HTMLParser parser = new HTMLParser(this.content);
            return parser.getParagraph();
        }

        public CharSequence getDateTime() {
            CharSequence ret = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss a", new java.util.Date());
            return ret;
        }

        public Bundle toBundle() {
            Bundle ret = new Bundle();
            ret.putString("ID", this.id);
            ret.putString("CONTENT", this.content);
            ret.putString("DETAILS", this.title);
            ret.putString("ORIGIN_LINK", this.originLink);
            ret.putLong("TIME", this.time);
            ret.putBoolean("LIKED", this.isLiked);
            return ret;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
