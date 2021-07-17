package com.lily.YumYum.ui.foodlist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.lily.YumYum.databinding.ItemFoodBinding;
import com.lily.YumYum.model.Food;
import com.lily.YumYum.utils.GlideApp;

import java.util.List;

import timber.log.Timber;


public class FoodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context mContext;

    private List<Food> mFoodList;

    private FoodListViewModel mViewModel;

    public FoodAdapter(Context context, List<Food> foods, FoodListViewModel viewModel) {
        mContext = context;
        mViewModel = viewModel;
        replaceData(foods);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Create the binding
        ItemFoodBinding binding =
                ItemFoodBinding.inflate(layoutInflater, parent, false);
        return new FoodViewHolder(binding);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
        final Food food = mFoodList.get(position);
        FoodViewHolder foodViewHolder = (FoodViewHolder) holder;

        // food image
        GlideApp.with(mContext)
                .load(food.getImage())
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(16)))
                .into(foodViewHolder.binding.imageFood);

        // food name
        foodViewHolder.binding.textName.setText(food.getMainName());

        // food description
        foodViewHolder.binding.textDescription.setText(Html.fromHtml(food.getDescription(), Html.FROM_HTML_MODE_COMPACT));//food.getDescription());
//        Drawable starDrawable = foodViewHolder.binding.BookmarkStar.getBackground();
        foodViewHolder.binding.BookmarkStar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Timber.d("Position clicked: %s", position);
//                mViewModel.getOpenfoodEvent().setValue(position);
            }
        });

        // click event
        foodViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Timber.d("Position clicked: %s", position);
                mViewModel.getOpenfoodEvent().setValue(position);
            }
        });

        foodViewHolder.binding.executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mFoodList != null ? mFoodList.size() : 0;
    }

    public void replaceData(List<Food> foods) {
        mFoodList = foods;
        notifyDataSetChanged();
    }

    /**
     * The {@link FoodViewHolder} class.
     * Provides a binding reference to each view in food cardView.
     */
    public class FoodViewHolder extends RecyclerView.ViewHolder {

        private final ItemFoodBinding binding;

        public FoodViewHolder(final ItemFoodBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }

}
