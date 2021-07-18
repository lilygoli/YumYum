package com.lily.YumYum.ui.favorite;

import com.lily.YumYum.model.Food;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class FavoriteListViewModel {
    ArrayList<Food> AllFoods;
    ArrayList<Food> favFoods = new ArrayList<Food>();
    Set<String> favFoodNames;
    public FavoriteListViewModel(ArrayList<Food> foods, ArrayList<String> fav_foods){
        AllFoods = foods;
        favFoodNames =  new HashSet<String>(fav_foods);
    }


    public ArrayList<Food> getFavFoods() {
        for (int i = 0; i < AllFoods.size(); i++){
            if (favFoodNames.contains(AllFoods.get(i).getMainName())){
                favFoods.add(AllFoods.get(i));
            }
        }
        return favFoods;
    }
}
