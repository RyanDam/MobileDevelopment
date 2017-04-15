package com.rstudio.assmb.latie.contentfragment.dummy;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lazyguy on 08/04/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static class Lib{
        public static ContentValues DummyItem2ContentValues(DummyContent.DummyItem dummyItem){
            ContentValues values = new ContentValues();
            values.put(Constant.FeedEntry.KEY_TITLE, dummyItem.title);
            values.put(Constant.FeedEntry.KEY_TIME, dummyItem.time);
            values.put(Constant.FeedEntry.KEY_CONTENT, dummyItem.content);
            values.put(Constant.FeedEntry.KEY_LINK, dummyItem.originLink);
            values.put(Constant.FeedEntry.KEY_ISLIKED, dummyItem.isLiked);
            return values;
        }
    }

    public DatabaseHandler(Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Constant.Database.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(Constant.Database.SQL_DROP_ENTRIES);
        onCreate(db);
    }

    public void addDummyItem(DummyContent.DummyItem dummyItem) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = Lib.DummyItem2ContentValues(dummyItem);

        db.beginTransaction();
        try{
            db.insert(Constant.FeedEntry.TABLE_NAME,null,values);
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
    }

    public DummyContent.DummyItem getDummyItemById(String id){
        SQLiteDatabase db = this.getReadableDatabase();
        DummyContent.DummyItem dummyItem = null;


        String sql = Constant.Database.SQL_SELECT + " WHERE " + Constant.FeedEntry._ID + " = " + id;

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            String title = cursor.getString(1);
            long time = cursor.getLong(2);
            String content = cursor.getString(3);
            String link = cursor.getString(4);
            Boolean isLiked = cursor.getInt(5) != 0;

            dummyItem = new DummyContent.DummyItem(id, title, content, link, isLiked, time);
        }

        cursor.close();
        db.close();
        return dummyItem;
    }

    public List<DummyContent.DummyItem> getAllDummyItem() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(Constant.Database.SQL_SELECT, null);
        List<DummyContent.DummyItem> dummyItemList = null;

        if (cursor.moveToFirst()) {
            dummyItemList = new ArrayList<DummyContent.DummyItem>();
            do {
                String id = cursor.getString(0);
                String title = cursor.getString(1);
                long time = cursor.getLong(2);
                String content = cursor.getString(3);
                String link = cursor.getString(4);
                Boolean isLiked = cursor.getInt(5) != 0;

                dummyItemList.add(new DummyContent.DummyItem(id, title, content, link, isLiked, time));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return dummyItemList;
    }

    public boolean update(DummyContent.DummyItem oldDI, DummyContent.DummyItem newDI) {

        boolean success = false;


        ContentValues values = Lib.DummyItem2ContentValues(newDI);

        String whereClause = Constant.FeedEntry._ID + " = ?";
        String[] whereArgs = new String[]{oldDI.id};

        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try{
            success = db.update(Constant.FeedEntry.TABLE_NAME, values, whereClause, whereArgs) > 0;
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            db.close();
        }
        return success;
    }

    public boolean delete(DummyContent.DummyItem dummyItem) {
        return deleteById(dummyItem.id);
    }

    public boolean deleteById(String id){
        boolean success = false;

        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            success = db.delete(Constant.FeedEntry.TABLE_NAME, Constant.FeedEntry._ID + " = ?", new String[]{id}) > 0;
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
            db.close();
        }
        return success;
    }

}
