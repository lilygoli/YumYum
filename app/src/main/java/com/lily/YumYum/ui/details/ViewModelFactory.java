package com.lily.YumYum.ui.details;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

/**
 * A creator is used to inject the food ID into the ViewModel
 */
public class ViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Application mApplication;

    private final int mPosition;

    private final String mFile;

    public static ViewModelFactory getInstance(Application application, int position, String file) {

        return new ViewModelFactory(application, position, file);
    }

    private ViewModelFactory(Application application, int position, String file) {
        mApplication = application;
        mPosition = position;
        mFile = file;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(FoodViewModel.class)) {
            //noinspection unchecked
            return (T) new FoodViewModel(mApplication, mPosition, mFile);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
