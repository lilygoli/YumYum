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
    ArrayList<Food> AllFoods;
    Set<String> favFoodNames;

    private final Context mContext;
    private final MutableLiveData<List<Food>> mObservablefoodes;

    private final SingleLiveEvent<Integer> mOpenfoodEvent = new SingleLiveEvent<>();

    public FavoriteListViewModel(@NonNull Application application, ArrayList<Food> foods, ArrayList<String> fav_foods) {
        super(application);
        Timber.d("Creating viewModel");

        // initialize data
        mContext = application.getApplicationContext();
        AppExecutors mExecutors = AppExecutors.getInstance();
        mObservablefoodes = new MutableLiveData<>();
        AllFoods = foods;
        favFoodNames =  new HashSet<String>(fav_foods);
        final ArrayList<Food> favFoods = new ArrayList<>();

        mExecutors.mainThread().execute(new Runnable() {
            @Override
            public void run() {
//                String x = readFromFile(mContext, "own4.txt");
//                if(!x.equals("")) {
//                    String[] y = x.split("#");
//
//                    for (String s : y
//                    ) {
//                        Food food = null;
//                        try {
//                            food = JsonUtils.parsefoodJson(s);
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        foodeList.add(food);
//                    }
//                }
                for (int i = 0; i < AllFoods.size(); i++){
                    if (favFoodNames.contains(AllFoods.get(i).getMainName())){
                        favFoods.add(AllFoods.get(i));
                    }
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

//    private String readFromFile(Context context, String file) {
//
//        String ret = "";
//
//        try {
//            InputStream inputStream = context.openFileInput(file);
//
//            if (inputStream != null) {
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//                String receiveString = "";
//                StringBuilder stringBuilder = new StringBuilder();
//
//                while ((receiveString = bufferedReader.readLine()) != null) {
//                    stringBuilder.append("\n").append(receiveString);
//                }
//
//                inputStream.close();
//                ret = stringBuilder.toString();
//            }
//        } catch (FileNotFoundException e) {
//            Timber.e("File not found: %s", e.toString());
//        } catch (IOException e) {
//            Timber.e("Can not read file: %s", e.toString());
//        }
//
//        return ret;
//    }
}
