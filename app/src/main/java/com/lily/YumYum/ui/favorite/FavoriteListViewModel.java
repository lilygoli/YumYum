package com.lily.YumYum.ui.favorite;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Pair;

import com.lily.YumYum.model.Food;
import com.lily.YumYum.utils.AppExecutors;
import com.lily.YumYum.utils.JsonUtils;
import com.lily.YumYum.utils.SingleLiveEvent;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.OkHttpClient;
import timber.log.Timber;

public class FavoriteListViewModel extends AndroidViewModel {
    ArrayList<String> favFood_recipes;

    private final Context mContext;
    private final MutableLiveData<List<Food>> mObservablefoodes;

    private final SingleLiveEvent<Integer> mOpenfoodEvent = new SingleLiveEvent<>();

    public FavoriteListViewModel(@NonNull Application application, ArrayList<String> fav_foods_recipes) {
        super(application);
        Timber.d("Creating viewModel");

        // initialize data
        mContext = application.getApplicationContext();
        AppExecutors mExecutors = AppExecutors.getInstance();
        mObservablefoodes = new MutableLiveData<>();
        favFood_recipes = fav_foods_recipes;
        final ArrayList<Food> favFoods = new ArrayList<>();

        mExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < favFood_recipes.size(); i++){
                    Food food = null;
                    try {
                        food = JsonUtils.parsefoodJson(favFood_recipes.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    favFoods.add(food);
                }
                if (!favFoods.isEmpty()) {

                    mObservablefoodes.postValue(favFoods);
                }
            }
        });

    }

    public LiveData<List<Food>> getfoodList() {
        return mObservablefoodes;
    }

    public void postFoodValue(List<Food> f){
        mObservablefoodes.postValue(f);
    }
    public MutableLiveData<Integer> getOpenfoodEvent() {
        return mOpenfoodEvent;
    }

    public MutableLiveData<List<Food>> getmObservablefoodes() {
        return mObservablefoodes;
    }
}
