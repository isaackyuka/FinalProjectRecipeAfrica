/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.ZAC.myapplication.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Transaction;

import java.util.ArrayList;
import java.util.List;


/**
 * An endpoint class we are exposing
 */
@Api(name = "recipeApi", version = "v1", namespace = @ApiNamespace(ownerDomain = "backend.myapplication.ZAC.example.com", ownerName = "backend.myapplication.ZAC.example.com", packagePath = ""))
public class MyEndpoint {

    @ApiMethod(name = "storeRecipe")
    public void storeRecipe(RecipeBean recipeBean) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {
            Key recipeBeanParentKey = KeyFactory.createKey("RecipeBeanParent", "RecipeAfrica");
            Entity recipeEntity = new Entity("RecipeBean", recipeBean.getId(), recipeBeanParentKey);
            recipeEntity.setProperty("title", recipeBean.getTitle());
            recipeEntity.setProperty("description", recipeBean.getDescription());
            recipeEntity.setProperty("ingredients", recipeBean.getIngredients());
            recipeEntity.setProperty("steps", recipeBean.getSteps());
//            recipeEntity.setProperty("photo", recipeBean.getPhoto());
//            recipeEntity.setProperty("videoUrl", recipeBean.getVideoUrl());
            datastoreService.put(recipeEntity);
            txn.commit();
        } finally {
            if(txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @ApiMethod(name = "getRecipes")
    public List<RecipeBean> getRecipes() {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key recipeBeanParentKey = KeyFactory.createKey("RecipeBeanParent", "RecipeAfrica");

        Query query = new Query(recipeBeanParentKey);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<RecipeBean> recipeBeans = new ArrayList<RecipeBean>();
        for (Entity result : results) {
            RecipeBean recipeBean = new RecipeBean();
            recipeBean.setId(result.getKey().getId());
            recipeBean.setTitle((String) result.getProperty("title"));
            recipeBean.setDescription((String) result.getProperty("description"));
            recipeBean.setIngredients((String) result.getProperty("ingredients"));
            recipeBean.setSteps((String) result.getProperty("steps"));
//            recipeBean.setPhoto((String) result.getProperty("photo"));
//            recipeBean.setVideoUrl((String) result.getProperty("videoUrl"));
            recipeBeans.add(recipeBean);
        }
        return recipeBeans;
    }
//
//    @ApiMethod(name = "clearRecipes")
//    public void clearRecipes() {
//
//    }

    @ApiMethod(name = "saveReview")
    public void saveReview(ReviewBean reviewBean) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Transaction txn = datastoreService.beginTransaction();
        try {
            Key reviewBeanParentKey = KeyFactory.createKey("ReviewBeanParent", "RecipeAfrica");
            Entity reviewEntity = new Entity("ReviewBean", reviewBean.getId(), reviewBeanParentKey);
            reviewEntity.setProperty("recipeId", reviewBean.getRecipeId());
            reviewEntity.setProperty("username", reviewBean.getUsername());
            reviewEntity.setProperty("comment", reviewBean.getComment());
            reviewEntity.setProperty("rating", reviewBean.getRating());
            datastoreService.put(reviewEntity);
            txn.commit();
        } finally {
            if(txn.isActive()) {
                txn.rollback();
            }
        }
    }

    @ApiMethod(name = "getReviews")
    public List<ReviewBean> getReviews() {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key reviewBeanParentKey = KeyFactory.createKey("ReviewBeanParent", "RecipeAfrica");

        Query query = new Query(reviewBeanParentKey);

      List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<ReviewBean> reviewBeans = new ArrayList<ReviewBean>();
        for (Entity result : results) {
            ReviewBean reviewBean = new ReviewBean();
            reviewBean.setId(result.getKey().getId());
            reviewBean.setRecipeId((Long) result.getProperty("recipeId"));
            reviewBean.setUsername((String) result.getProperty("username"));
            reviewBean.setComment((String) result.getProperty("comment"));
            reviewBean.setRating((Long) result.getProperty("rating"));
            reviewBeans.add(reviewBean);
        }
        return reviewBeans;
    }

    @ApiMethod(name = "getReviewsForRecipe")
    public List<ReviewBean> getReviewsForRecipe(@Named("recipeId") Long recipeId) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key reviewBeanParentKey = KeyFactory.createKey("ReviewBeanParent", "RecipeAfrica");

//        Query query = new Query(reviewBeanParentKey);
//        // Use PreparedQuery interface to retrieve results
//        PreparedQuery pq = datastore.prepare(query);
//
//
//        for (Entity result : pq.asIterable()) {
//            String firstName = (String) result.getProperty("firstName");
//            String lastName = (String) result.getProperty("lastName");
//            Long height = (Long) result.getProperty("height");
//
//            System.out.println(firstName + " " + lastName + ", " + height + " inches tall");
//        }
        Query.Filter propertyFilter =
                new Query.FilterPredicate("recipeId",
                        Query.FilterOperator.EQUAL,
                        recipeId);
//        Query q = new Query("Person").setFilter(propertyFilter);
        Query query = new Query("ReviewBean").setFilter(propertyFilter);
        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<ReviewBean> reviewBeans = new ArrayList<ReviewBean>();
        for (Entity result : results) {
            ReviewBean reviewBean = new ReviewBean();
            reviewBean.setId(result.getKey().getId());
            reviewBean.setRecipeId((Long) result.getProperty("recipeId"));
            reviewBean.setUsername((String) result.getProperty("username"));
            reviewBean.setComment((String) result.getProperty("comment"));
            reviewBean.setRating((Long) result.getProperty("rating"));
            reviewBeans.add(reviewBean);
        }
        return reviewBeans;
    }

    @ApiMethod(name = "searchRecipes")
    public List<RecipeBean> searchRecipes(@Named("searchTerm") String searchTerm) {
        DatastoreService datastoreService = DatastoreServiceFactory.getDatastoreService();
        Key recipeBeanParentKey = KeyFactory.createKey("RecipeBeanParent", "RecipeAfrica");
//        Query query = new Query("Customer");
        Query.Filter propertyFilter =
                new Query.FilterPredicate("title",
                        Query.FilterOperator.EQUAL,
                        searchTerm);
//        Query q = new Query("Person").setFilter(propertyFilter);
        Query query = new Query("RecipeBean").setFilter(propertyFilter);
//        query.addFilter("title", Query.FilterOperator.EQUAL, searchTerm);
//        PreparedQuery pq = datastoreService.prepare(query);
//        Entity recipe = pq.asSingleEntity();

        List<Entity> results = datastoreService.prepare(query).asList(FetchOptions.Builder.withDefaults());

        ArrayList<RecipeBean> recipeBeans = new ArrayList<RecipeBean>();
        for (Entity result : results) {
            RecipeBean recipeBean = new RecipeBean();
            recipeBean.setId(result.getKey().getId());
            recipeBean.setTitle((String) result.getProperty("title"));
            recipeBean.setDescription((String) result.getProperty("description"));
            recipeBean.setIngredients((String) result.getProperty("ingredients"));
            recipeBean.setSteps((String) result.getProperty("steps"));
//            recipeBean.setPhoto((String) result.getProperty("photo"));
//            recipeBean.setVideoUrl((String) result.getProperty("videoUrl"));
            recipeBeans.add(recipeBean);
        }
        return recipeBeans;
    }

}
