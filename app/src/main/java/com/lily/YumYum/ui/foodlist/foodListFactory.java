package com.lily.YumYum.ui.foodlist;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.lily.YumYum.ui.favorite.FavoriteListViewModel;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class foodListFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private boolean mIsConnected;


    public foodListFactory(Application application, boolean isConnected) {
        mApplication = application;
        mIsConnected = isConnected;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new FoodListViewModel(mApplication, mIsConnected);
    }
}
