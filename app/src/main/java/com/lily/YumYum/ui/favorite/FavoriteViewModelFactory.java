package com.lily.YumYum.ui.favorite;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.lily.YumYum.model.Food;

import java.util.ArrayList;

public class FavoriteViewModelFactory implements ViewModelProvider.Factory {
    private Application mApplication;
    private ArrayList<String> mfav_foods_recipes;


    public FavoriteViewModelFactory(Application application, ArrayList<String> fav_foods_recipes) {
        mApplication = application;
        mfav_foods_recipes = fav_foods_recipes;
    }


    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        return (T) new FavoriteListViewModel(mApplication, mfav_foods_recipes);
    }
}
