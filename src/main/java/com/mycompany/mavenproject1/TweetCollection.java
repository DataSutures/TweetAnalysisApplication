/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;
import java.util.ArrayList;
import java.util.List;
import twitter4j.Status;
import java.util.HashMap;
import java.util.ArrayList;

/**
 *
 * @author kimberlysmith
 */
public class TweetCollection {
    
    private List<Tweet> tweets = new ArrayList<>();
    private int positiveCount;
    private int negativeCount;
    private int neutralCount;
    private final ArrayList<String> locations = new ArrayList<>();
    private int SIZE = 0;
    
    public TweetCollection(List<Status> tweets) {
        
        // Add all tweets to the collection
        for (Status tweet : tweets) {
            Tweet t = new Tweet(tweet);
            this.tweets.add(t);
        }
        // count sentiments
        loadData();
        // set size of collection
        this.SIZE = tweets.size();
        
    }
    // 
    private void loadData() {
        
        for (Tweet tweet : tweets) {
            
            // load sentiment total data
            String sentiment = tweet.getSentiment();
            switch(sentiment) {
                case "Positive": this.positiveCount++;
                case "Negative": this.negativeCount++;
                case "Neitral": this.neutralCount++;
            }       
            // load locations data
            locations.add(tweet.getLocation());
        }

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
        return this.SIZE;
    }
    public List<Tweet> getCollection() {
        return tweets;
    }
    
}
