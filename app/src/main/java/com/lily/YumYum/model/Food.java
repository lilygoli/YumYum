package com.lily.YumYum.model;

import java.util.List;

public class Food {

    private String mainName;
    private List<String> diets = null;
    private String placeOfOrigin;
    private String description;
    private String image;
    private List<String> ingredients = null;
    private List<String> steps = null;

    /**
     * No args constructor for use in serialization
     */
    public Food() {
    }

    public Food(String mainName, List<String> diets, String placeOfOrigin, String description, String image, List<String> ingredients, List<String> steps) {
        this.mainName = mainName;
        this.diets = diets;
        this.placeOfOrigin = placeOfOrigin;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public List<String> getDiets() {
        return diets;
    }

    public void setDiets(List<String> diets) {
        this.diets = diets;
    }

    public String getPlaceOfOrigin() {
        return placeOfOrigin;
    }

    public void setPlaceOfOrigin(String placeOfOrigin) {
        this.placeOfOrigin = placeOfOrigin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getSteps() {
        return steps;
    }

    public void setSteps(List<String> steps) {
        this.steps = steps;
    }
}
