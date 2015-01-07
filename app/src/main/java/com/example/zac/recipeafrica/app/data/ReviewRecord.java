package com.example.zac.recipeafrica.app.data;

import java.io.Serializable;

/**
 * Created by ZAC on 12/23/2014.
 */
public class ReviewRecord implements Serializable {
    Long recipeID;
    String username;
    String comment;
    Long rating;


    public ReviewRecord(Long recipeID, String username, String comment, Long rating) {
        this.recipeID = recipeID;
        this.username = username;
        this.comment = comment;
        this.rating = rating;
    }

    public Long getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(Long recipeID) {
        this.recipeID = recipeID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }
}
