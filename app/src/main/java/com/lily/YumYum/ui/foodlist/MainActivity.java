package com.lily.YumYum.ui.foodlist;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.lily.YumYum.ui.details.DetailActivity;
import com.lily.YumYum.R;
import com.lily.YumYum.model.Food;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FoodAdapter mFoodAdapter;

    private FoodListViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = obtainViewModel(this);

        setupToolbar();

        setupListAdapter();

        // subscribe to food observable livedata
        mViewModel.getfoodList().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foods) {
                if (foods != null) {
                    mFoodAdapter.replaceData(foods);
                }
            }
        });

        // Subscribe to "open food" event
        mViewModel.getOpenfoodEvent().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer position) {
                if (position != null) {
                    launchDetailActivity(position);
                }
            }
        });
    }

    private FoodListViewModel obtainViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(FoodListViewModel.class);
    }

    private void setupListAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recycler_food_list);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mFoodAdapter = new FoodAdapter(this,
                new ArrayList<Food>(0),
                mViewModel
        );
        recyclerView.setAdapter(mFoodAdapter);
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
        startActivity(intent);
    }
}
