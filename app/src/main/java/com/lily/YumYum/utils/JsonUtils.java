package com.lily.YumYum.utils;

import android.support.annotation.NonNull;
import android.util.Log;

import com.lily.YumYum.model.Food;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private final static String KEY_NAME = "title";
    private final static String KEY_DIET = "diets";
    private final static String KEY_PLACE_OF_ORIGIN = "cuisines";
    private final static String KEY_DESCRIPTION = "summary";
    private final static String KEY_IMAGE_URL = "image";
    private final static String KEY_INGREDIENTS = "extendedIngredients";
    private final static String KEY_STEPS = "analyzedInstructions";

    public static Food parsefoodJson(String json) throws JSONException {
        JSONObject foodObjects = new JSONObject(json);

        JSONArray foodObjectArray = foodObjects.getJSONArray("recipes");

        JSONObject foodObject = foodObjectArray.getJSONObject(0);

        // name
        String name = foodObject.getString(KEY_NAME);

        // Also known as
        JSONArray diets = foodObject.getJSONArray(JsonUtils.KEY_DIET);
        List<String> dietTypes = getStrings(diets);

        // Place of origin
        JSONArray origin = foodObject.getJSONArray(KEY_PLACE_OF_ORIGIN);
        String placeOfOrigin = getCommaStrings(origin);
        // Description
        String description = foodObject.getString(KEY_DESCRIPTION);

        // Image
        String image = "";
        try {
            image = foodObject.getString(KEY_IMAGE_URL);
        } catch (Exception e) {
            Log.i("Image", "NO IMAGEEEEEEEEEE");
            image = "https://icon-library.com/images/order-food-icon/order-food-icon-21.jpg";
        }


        // Ingredients
        JSONArray ingredientsJson = foodObject.getJSONArray(KEY_INGREDIENTS);
        List<String> ingredients = getObjStrings(ingredientsJson);

        JSONArray stepList = foodObject.getJSONArray(KEY_STEPS);
        List<String> steps = getObjSteps(stepList);

        return new Food(name, dietTypes, placeOfOrigin, description, image, ingredients, steps);
    }

    @NonNull
    private static List<String> getStrings(JSONArray jsonArray) throws JSONException {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            strings.add(jsonArray.getString(i));
        }
        return strings;
    }

    @NonNull
    private static List<String> getObjStrings(JSONArray jsonArray) throws JSONException {
        List<String> strings = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            String amount = "";
            String unit = "";
            try {
                amount = jsonArray.getJSONObject(i).getString("amount");
            }catch (Exception e){
                amount = "arbitrary";
            }
            try {
                unit = jsonArray.getJSONObject(i).getString("unit");
            }catch (Exception e){
                unit = "";
            }
            strings.add(jsonArray.getJSONObject(i).getString("name").concat(": ").concat(amount).concat(" ").concat(unit));
        }
        return strings;
    }

    @NonNull
    private static List<String> getObjSteps(JSONArray jsonArray) throws JSONException {
        List<String> strings = new ArrayList<>();
        if (jsonArray.length() > 0) {
            JSONObject wrapper = jsonArray.getJSONObject(0);
            JSONArray steps = wrapper.getJSONArray("steps");
            for (int i = 0; i < steps.length(); i++) {
                strings.add(steps.getJSONObject(i).getString("step"));
            }
        } else {
            strings.add("Mix the ingredients! Voila!");
        }
        return strings;
    }

    @NonNull
    private static String getCommaStrings(JSONArray jsonArray) throws JSONException {
        String strings = "";
        for (int i = 0; i < jsonArray.length(); i++) {
            strings = strings.concat(jsonArray.getString(i));
            if (i != jsonArray.length() - 1) {
                strings = strings.concat(", ");
            }
        }
        return strings;
    }
}
