package com.lily.YumYum.ui.favorite;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;


import com.lily.YumYum.R;
import com.lily.YumYum.database.DbHelper;
import com.lily.YumYum.model.Food;
import com.lily.YumYum.ui.details.DetailActivity;
import com.lily.YumYum.ui.ownrecipe.AddFoodActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class favoritesActivity extends AppCompatActivity {
    private favoriteAdapter mFavoriteAdapter;

    private static FavoriteListViewModel fModel;

    DbHelper db;
    ArrayList<Food> foodList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foodList = (ArrayList<Food>) this.getIntent().getSerializableExtra("EXTERA_FOODLIST");
        final HashMap<String, Integer> namePos = new HashMap<>();
        for (int i = 0; i < foodList.size(); i++){
            namePos.put(foodList.get(i).getMainName(), i);
        }
        DataBindingUtil.setContentView(this, R.layout.favorites);
        db = new DbHelper(this);
        fModel = obtainViewModel(this);


        setupToolbar();

        setupListAdapter();

        // subscribe to food observable livedata
        fModel.getfoodList().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foods) {
                if (foods != null) {
                    mFavoriteAdapter.replaceData(foods);
                }
            }
        });

        // Subscribe to "open food" event
        fModel.getOpenfoodEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer position) {
                if (position != null) {
                    String name = db.getAllFoods().get(position);
                    Integer p = namePos.get(name);
                    launchDetailActivity(p);
                }
            }
        });

        Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private FavoriteListViewModel obtainViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(this, new FavoriteViewModelFactory(this.getApplication(), foodList, db.getAllFoods())).get(FavoriteListViewModel.class);
    }


    private void setupListAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recycler_favorite_list);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mFavoriteAdapter = new favoriteAdapter(this,
                new ArrayList<Food>(0),
                fModel
        );
        recyclerView.setAdapter(mFavoriteAdapter);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void launchDetailActivity(int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_POSITION, position);
        intent.putExtra(DetailActivity.EXTRA_FILE, "random.txt");
        startActivity(intent);
    }

    private void newFoodActivity() {
        Intent intent = new Intent(this, AddFoodActivity.class);

        startActivity(intent);
    }

    public static FavoriteListViewModel getfModel() {
        return fModel;
    }
}
