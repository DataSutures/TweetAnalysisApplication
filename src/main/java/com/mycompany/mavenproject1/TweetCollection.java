/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.util.List;
import twitter4j.Status;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.stream.Collectors;

/**
 *
 * @author kimberlysmith
 */
public class TweetCollection {
    
    private String collectionName = "";
    private List<Tweet> tweets = new ArrayList<>();
    
    
    public TweetCollection(){}
    public TweetCollection(String searchTerm, List<Status> tweets) {
        
        // Set collectionName
        this.collectionName = searchTerm;
        
        // Add all tweets to the collection
        for (Status tweet : tweets) {
            Tweet t = new Tweet(tweet);
            this.tweets.add(t);
        }
    }
    public TweetCollection(TableObjectCollection tableOCollection){
        // Add all TableObjects to the collection
        for(int i = 0; i < tableOCollection.getTweetObjects().size(); i++){
            
            TableObject to = tableOCollection.getTweetObjects().get(i);
            Tweet t = new Tweet(to.getScreenName(), to.getTweetText(), 
                                to.getCreatedOn(), to.getSentiment(), 
                                to.getLocation()
                            );
            this.tweets.add(t);
        }
 
    }
    public boolean addAll(List<Tweet> newTweets){
        return tweets.addAll(newTweets);
    }
    public boolean remove(Tweet tweet){
        return tweets.remove(tweet);
    }
    public String getCollectionName(){
        return this.collectionName;
    }
    public int getPosCount() {
        return (int)tweets.stream().filter(t -> t.getSentiment().equals("Positive")).count();
    }
    public int getNegCount() {
        return (int)tweets.stream().filter(t -> t.getSentiment().equals("Negative")).count();
    }
    public int getNeuCount() {
        return (int)tweets.stream().filter(t -> t.getSentiment().equals("Neutral")).count();
    }
    public ArrayList<String> getLocations() {
       return (ArrayList)tweets.stream().map(t -> t.getLocation()).collect(Collectors.toList());
    }
    public int size() {
        return this.tweets.size();
    }
    public List<Tweet> getCollection() {
        return tweets;
    }
    
}
