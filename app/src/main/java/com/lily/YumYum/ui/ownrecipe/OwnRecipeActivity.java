package com.lily.YumYum.ui.ownrecipe;

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
import com.lily.YumYum.model.Food;
import com.lily.YumYum.ui.details.DetailActivity;
import com.lily.YumYum.databinding.OwnRecipeActivityBinding;

import java.util.ArrayList;
import java.util.List;

public class OwnRecipeActivity extends AppCompatActivity {

    private OwnFoodAdapter mFoodAdapter;

    private static OwnFoodListViewModel mViewModel;

    private  OwnRecipeActivity mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.own_recipe_activity);

        mViewModel = obtainViewModel(this);


        setupToolbar();

        setupListAdapter();

        // subscribe to food observable livedata
        mViewModel.getfoodList().observe(this, new Observer<List<Food>>() {
            @Override
            public void onChanged(@Nullable List<Food> foods) {
                if (foods != null) {
//                    Log.i("FFFFFOOD", String.valueOf(foods));
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

        Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        FloatingActionButton add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFoodActivity();
            }
        });

    }

    private OwnFoodListViewModel obtainViewModel(FragmentActivity activity) {
        return ViewModelProviders.of(activity).get(OwnFoodListViewModel.class);
    }

    private void setupListAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recycler_food_list);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mFoodAdapter = new OwnFoodAdapter(this,
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
        intent.putExtra(DetailActivity.EXTRA_FILE, "own4.txt");
        startActivity(intent);
    }

    private void newFoodActivity() {
        Intent intent = new Intent(this, AddFoodActivity.class);

        startActivity(intent);
    }

    public static OwnFoodListViewModel getmViewModel() {
        return mViewModel;
    }
}
