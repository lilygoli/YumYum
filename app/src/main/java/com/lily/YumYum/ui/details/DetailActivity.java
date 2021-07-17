package com.lily.YumYum.ui.details;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.flexbox.FlexboxLayout;
import com.lily.YumYum.R;
import com.lily.YumYum.databinding.ActivityDetailBinding;
import com.lily.YumYum.model.Food;
import com.lily.YumYum.utils.GlideApp;
import com.lily.YumYum.utils.UiUtils;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;

    public static final String EXTRA_FILE = "extra_file";


    private ActivityDetailBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        final int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);

        final String file = intent.getStringExtra(EXTRA_FILE);

        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        setupToolbar();

        FoodViewModel mViewModel = obtainViewModel(this, position, file);

        // Subscribe to food changes
        mViewModel.getfood().observe(this, new Observer<Food>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(@Nullable Food food) {
                if (food != null) {
                    populateUI(food);
                } else {
                    closeOnError();
                }
            }
        });
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private FoodViewModel obtainViewModel(FragmentActivity activity, int position, String file) {
        ViewModelFactory factory = ViewModelFactory.getInstance(activity.getApplication(), position, file);
        return ViewModelProviders.of(activity, factory).get(FoodViewModel.class);
    }

    private void setupToolbar() {
        Toolbar toolbar = mBinding.toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void populateUI(Food food) {
        // food image
        GlideApp.with(this)
                .load(food.getImage())
                .placeholder(R.color.md_grey_200)
                .into(mBinding.imageRecipe);

        // food main name
        mBinding.textFoodName.setText(food.getMainName());

        // food origin
        String placeOfOrigin = food.getPlaceOfOrigin();
        if (placeOfOrigin.isEmpty()) {
            mBinding.textOrigin.setVisibility(View.GONE);
        } else {
            UiUtils.setTextViewDrawableColor(this, mBinding.textOrigin, R.color.text_black_54);
            mBinding.textOrigin.setText(placeOfOrigin);
        }

        // Programmatically create & add "also known as" labels
        List<String> names = food.getDiets();
        if (!names.isEmpty()) {
            for (String name : names) {
                TextView textView = new TextView(this);
                textView.setText(name);
                textView.setBackground(ContextCompat.getDrawable(this, R.drawable.chip_shape));
                TextViewCompat.setTextAppearance(textView, R.style.Chips);
                FlexboxLayout.LayoutParams layoutParams = new FlexboxLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(0, 0, 8, 8);
                textView.setLayoutParams(layoutParams);
                mBinding.flexbox.addView(textView);
            }
        } else {
            mBinding.flexbox.setVisibility(View.GONE);
        }

        // food description
        UiUtils.setTextViewDrawableColor(this, mBinding.textView3, R.color.text_black_54);
        mBinding.descriptionTv.setText(Html.fromHtml(food.getDescription(), Html.FROM_HTML_MODE_COMPACT));

        // Ingredients List
        UiUtils.setTextViewDrawableColor(this, mBinding.textView4, R.color.text_black_54);
        List<String> ingredients = food.getIngredients();
        for (String ingredient : ingredients) {
            TextView textView = new TextView(this);
            textView.setText(ingredient);
            TextViewCompat.setTextAppearance(textView, R.style.Chips);
            textView.setPadding(0, 32, 0, 32);
            textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(
                    this, R.drawable.bullet), null, null, null);
            textView.setBackground(ContextCompat.getDrawable(this, R.drawable.dashed_line));
            textView.setCompoundDrawablePadding(32);
            mBinding.ingredientsList.addView(textView);
        }

        // Steps List
        UiUtils.setTextViewDrawableColor(this, mBinding.textView5, R.color.text_black_54);
        List<String> steps = food.getSteps();
        for (String step : steps) {
            TextView textView = new TextView(this);
            textView.setText(step);
            TextViewCompat.setTextAppearance(textView, R.style.Chips);
            textView.setPadding(0, 32, 0, 32);
            textView.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(
                    this, R.drawable.bullet), null, null, null);
            textView.setBackground(ContextCompat.getDrawable(this, R.drawable.dashed_line));
            textView.setCompoundDrawablePadding(32);
            mBinding.stepsList.addView(textView);
        }

        mBinding.executePendingBindings();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
