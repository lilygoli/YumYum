package com.lily.YumYum;

import android.content.Context;

import com.lily.YumYum.database.DbHelper;
import com.lily.YumYum.ui.foodlist.MainActivity;

import java.lang.ref.WeakReference;

public class dbHolder {
    static DbHelper myFavorites;

    public dbHolder(MainActivity context){
        myFavorites = new DbHelper(context);
    }
}
