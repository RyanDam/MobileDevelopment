package com.rstudio.assmb.latie.contentfragment.dummy;

import android.provider.BaseColumns;

/**
 * Created by lazyguy on 08/04/2017.
 */

public class Constant {

    //    id (string), title (string), time (long), content (string), link (string)
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "MAD.db";

    public class FeedEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String KEY_TITLE = "title";
        public static final String KEY_TIME = "time";
        public static final String KEY_CONTENT = "content";
        public static final String KEY_LINK = "link";
    }

    public class Database {
        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE IF NOT EXISTS " + FeedEntry.TABLE_NAME + " (" +
                        FeedEntry._ID + " INTEGER PRIMARY KEY," +
                        FeedEntry.KEY_TITLE + " TEXT," +
                        FeedEntry.KEY_TIME + " INTEGER," +
                        FeedEntry.KEY_CONTENT + " TEXT," +
                        FeedEntry.KEY_LINK + " TEXT" +
                        ");";

        public static final String SQL_DROP_ENTRIES =
                "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

        public static final String SQL_SELECT = "SELECT * FROM " + FeedEntry.TABLE_NAME;
    }
}

