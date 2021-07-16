package com.lily.YumYum.ui.details;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;

import com.lily.YumYum.model.Food;
import com.lily.YumYum.utils.AppExecutors;
import com.lily.YumYum.utils.JsonUtils;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import timber.log.Timber;

public class FoodViewModel extends AndroidViewModel {

    private final Context mContext;

    private final MutableLiveData<Food> mfood = new MutableLiveData<>();

    public FoodViewModel(@NonNull Application application, final int position) {
        super(application);
        Timber.d("Creating viewModel");

        // initialize data
        mContext = application.getApplicationContext();
        AppExecutors mExecutors = AppExecutors.getInstance();

        // parse json array on background thread
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                Timber.d("Start parsing Json");

//                String[] foodes = mContext.getResources().getStringArray(R.array.food_details);
                String x = readFromFile(mContext);
                String y = x.split("#")[position];
                Food food = null;
                try {
                    food = JsonUtils.parsefoodJson(y);//foodes[position]);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Timber.d("Json parsing finished");

                if (food != null) {
                    Timber.d("Json not null");
                    // update food livedata from background thread
                    mfood.postValue(food);
                }
            }
        });
    }

    public LiveData<Food> getfood() {
        return mfood;
    }

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("random.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Timber.e("File not found: %s", e.toString());
        } catch (IOException e) {
            Timber.e("Can not read file: %s", e.toString());
        }

        return ret;
    }
}
