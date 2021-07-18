package com.lily.YumYum.ui.favorite;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.lily.YumYum.model.Food;

import java.util.ArrayList;

public class FavoriteViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private ArrayList<Food> mfoods;
    private ArrayList<String> mfav_foods;


    public FavoriteViewModelFactory(Application application, ArrayList<Food> foods, ArrayList<String> fav_foods) {
        mApplication = application;
        mfoods = foods;
        mfav_foods = fav_foods;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new FavoriteListViewModel(mApplication, mfoods, mfav_foods);
    }
}
