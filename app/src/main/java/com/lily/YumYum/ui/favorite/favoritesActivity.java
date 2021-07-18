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
import com.lily.YumYum.ui.ownrecipe.OwnFoodAdapter;
import com.lily.YumYum.ui.ownrecipe.OwnFoodListViewModel;
import com.lily.YumYum.ui.ownrecipe.OwnRecipeActivity;

import java.util.ArrayList;
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
//        Log.d("datadata", foodList.get(0).getMainName());
        DataBindingUtil.setContentView(this, R.layout.favorites);
        db = new DbHelper(this);
        fModel = new FavoriteListViewModel(foodList, db.getAllFoods());


        setupToolbar();

        setupListAdapter();

        Button back_button = findViewById(R.id.back_button);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void setupListAdapter() {
        RecyclerView recyclerView = findViewById(R.id.recycler_favorite_list);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mFavoriteAdapter = new favoriteAdapter(this,
                fModel.getFavFoods()
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
        intent.putExtra(DetailActivity.EXTRA_FILE, "own4.txt");
        startActivity(intent);
    }

    private void newFoodActivity() {
        Intent intent = new Intent(this, AddFoodActivity.class);

        startActivity(intent);
    }

//    public static OwnFoodListViewModel getmViewModel() {
//        return mViewModel;
//    }
}
