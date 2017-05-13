package com.rstudio.assmb.latie.contentfragment.dummy;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by admin on 5/13/17.
 */

public class StaredItemContent extends DummyContent {
    @Override
    public void initData(Context context){
        ITEMS.clear();
        ITEM_MAP.clear();

        DatabaseHandler handler = new DatabaseHandler(context);
        List<DummyItem> list = handler.getAllDummyItemIsLiked();
        for (int i = list.size() - 1; i >= 0; i--) {
            addItem(list.get(i));
        }
    }
}
