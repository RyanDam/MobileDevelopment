package com.rstudio.assmb.latie.contentfragment.dummy;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by admin on 5/13/17.
 */

public class AllItemContent extends DummyContent {

   @Override
   public void initData(Context context){
        ITEMS.clear();
        ITEM_MAP.clear();

        DatabaseHandler handler = new DatabaseHandler(context);
        List<DummyItem> list = handler.getAllDummyItem();
        Log.d("LENGTH",String.valueOf(list.size()));
        for (int i = list.size(); i > 1; i--) {
            addItem(handler.getDummyItemById(String.valueOf(i)));
        }
    }

}
