package com.example.zac.recipeafrica.app.data;

import java.io.Serializable;

/**
 * Created by ZAC on 12/17/2014.
 */
public class RecipeRecord implements Serializable {
    Long recipeID;
    String title;
    String description;
    String ingredients;
    String steps;
//    String photo;
//    String videoUrl;


    public RecipeRecord(Long recipeID, String title, String description, String ingredients, String steps) {
        this.recipeID = recipeID;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.steps = steps;
    }

    public Long getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        this.description = description;
    }


    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

//    public String getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }
//
//    public String getVideoUrl() {
//        return videoUrl;
//    }
//
//    public void setVideoUrl(String videoUrl) {
//        this.videoUrl = videoUrl;
//    }

}
