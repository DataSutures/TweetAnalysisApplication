/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.util.List;
import twitter4j.Status;
import java.util.ArrayList;

/**
 *
 * @author kimberlysmith
 */
public class TweetCollection {
    
    private String collectionName = "";
    private List<Tweet> tweets = new ArrayList<>();
    private int positiveCount = 0;
    private int negativeCount = 0;
    private int neutralCount = 0;
    private final ArrayList<String> locations = new ArrayList<>();
    
    public TweetCollection(String searchTerm, List<Status> tweets) {
        
        // Set collectionName
        this.collectionName = searchTerm;
        
        // Add all tweets to the collection
        for (Status tweet : tweets) {
            Tweet t = new Tweet(tweet);
            this.tweets.add(t);
        }
        // count sentiments and get locations
        loadData();    
    }
    // load data for sentiment counts and locations list
    private void loadData() {
        
        for (Tweet tweet : tweets) {
            
            // load sentiment totals data
            String sentiment = tweet.getSentiment();
            switch(sentiment) {
                case "Positive": this.positiveCount++; break;
                case "Negative": this.negativeCount++; break;
                case "Neutral": this.neutralCount++; break;
            }       
            // Load location data, if has location
            String loc = tweet.getLocation();
            if(!loc.isEmpty()){
               locations.add(loc);
            }
        }

    }
    public String getCollectionName(){
        return this.collectionName;
    }
    public int getPosCount() {
        return this.positiveCount;
    }
    public int getNegCount() {
        return this.negativeCount;
    }
    public int getNeuCount() {
        return this.neutralCount;
    }
    public ArrayList<String> getLocations() {
        return locations;
    }
    public int size() {
        return this.tweets.size();
    }
    public List<Tweet> getCollection() {
        return tweets;
    }
    
}
