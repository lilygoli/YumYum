package com.lily.YumYum.ui.foodlist;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
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
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import timber.log.Timber;

public class FoodListViewModel extends AndroidViewModel {

    private final static int RANDOM_LIST_NUM = 10;

    private final Context mContext;

    private final MutableLiveData<List<Food>> mObservablefoodes;

    private final SingleLiveEvent<Integer> mOpenfoodEvent = new SingleLiveEvent<>();

    private OkHttpClient client = new OkHttpClient();

//    private MainActivity mainActivity;
    public FoodListViewModel(@NonNull Application application) {
        super(application);
        Timber.d("Creating viewModel");

        // initialize data
        mContext = application.getApplicationContext();
//        mainActivity = (MainActivity)mContext;
        AppExecutors mExecutors = AppExecutors.getInstance();
        mObservablefoodes = new MutableLiveData<>();
        final List<Food> foodeList = new ArrayList<>();

        final List<Pair<Food, String>> data = new ArrayList<>();


//
//        for (int i = 0; i < RANDOM_LIST_NUM; i++) {
//
//            mExecutors.networkIO().execute(new Runnable() {
//                @Override
//                public void run() {
//                    Timber.d("Start parsing Json");
//                    String recipe = "";
//
//                    try {
//                        recipe = getRecipes("https://api.spoonacular.com/recipes/random?apiKey=c45b4e0cd05a4cacba82a55aac26dbd4");
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//
//                    Food food = null;
//                    try {
//                        food = JsonUtils.parsefoodJson(recipe);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    data.add(new Pair<>(food, recipe));
//                    Timber.d("Json parsing finished");
//
//                }
//            });
//        }
//        mExecutors.diskIO().execute(new Runnable() {
//            @Override
//            public void run() {
//                while (data.size() < RANDOM_LIST_NUM);
//
//                try {
//                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput("random.txt", Context.MODE_PRIVATE));
//                    outputStreamWriter.write("");
//                    outputStreamWriter.close();
//                }
//                catch (IOException e) {
//                    Timber.e("File write failed: %s", e.toString());
//                }
//                for (Pair<Food, String> date:data
//                     ) {
//                    foodeList.add(date.first);
//                    try {
//                    OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mContext.openFileOutput("random.txt", Context.MODE_APPEND));
//                    outputStreamWriter.write(date.second);
//                    outputStreamWriter.write("#");
//                    outputStreamWriter.close();
//                    }
//                    catch (IOException e) {
//                        Timber.e("File write failed: %s", e.toString());
//                    }
//                }
//                if (!foodeList.isEmpty()) {
//                    Timber.d("Json not null and has " + foodeList.size() + " items");
//                    // update food MutableLiveData from background thread
//                    mObservablefoodes.postValue(foodeList);
//                }
//            }
//        });


        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
            String x = readFromFile(mContext);
            String[] y = x.split("#");
//            Log.i("RUNNN", "RUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUUN");
//            Log.i("RUNNN", x);
                for (String s:y
                     ) {
                    Food food = null;
                    try {
                        food = JsonUtils.parsefoodJson(s);
                        Log.i("NAAAME", food.getSteps().get(0));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    foodeList.add(food);
                }

            if (!foodeList.isEmpty()) {

            mObservablefoodes.postValue(foodeList);
            }
            }
        });
    }

    public LiveData<List<Food>> getfoodList() {
        return mObservablefoodes;
    }

    public MutableLiveData<Integer> getOpenfoodEvent() {
        return mOpenfoodEvent;
    }


    private String getRecipes(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
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
