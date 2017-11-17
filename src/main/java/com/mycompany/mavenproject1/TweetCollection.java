/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.aylien.textapi.TextAPIException;
import com.sun.xml.internal.ws.util.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Pair;
import twitter4j.Status;



/**
 * Creates a list of tweets based on the search term entered and returns 
 * counts of positive, negative, and neutral tweets.
 * @author kimberlysmith
 */
public class TweetCollection {
  
    private final ObservableList<Tweet> toc = FXCollections.observableArrayList();
    private String collectionName = "";

    /**
     * Creates a list of tweets based upon the search term entered by the user.
     * @param searchTerm
     * String pertaining to the specified search term set by the user.
     * @param tweets
     * List statuses based upon the the search term entered.
     */
    public TweetCollection(String searchTerm, List<Status> tweets){
        this.collectionName = searchTerm;
        
        tweets.forEach(t -> toc.add(new Tweet(t.getUser().getScreenName(),
                                            t.getText(),
                                            formatDate(t.getCreatedAt().toString()),
                                            t.getUser().getLocation()
                                            )));
        }
        

    // Get Collection

    /**
     * Returns an ObservableList of Tweet objects.
     * @return ObservableList
     */
    public ObservableList<Tweet> getTweetObjects() {
        return toc;
    }

    /**
     * Returns the count of all the tweets with positive sentiment.
     * @return sentiment(Positive)
     */
    public int getPosCount(){
        return (int)toc.stream().filter(t -> t.getSentiment().equals("Positive")).count();
    }

    /**
     * Returns the count of all the tweets with negative sentiment.
     * @return sentiment(Negative)
     */
    public int getNegCount(){
        return (int)toc.stream().filter(t -> t.getSentiment().equals("Negative")).count();
    }

    /**
     * Returns the count of all the tweets with neutral sentiment.
     * @return sentiment(Neutral)
     */
    public int getNeuCount(){
        return (int)toc.stream().filter(t -> t.getSentiment().equals("Neutral")).count();
    }

    /**
     * Returns the sentiment and location data in the form of an ArrayList of type Pair.
     * @return ArrayList
     */
    public ArrayList<Pair> getLocSentPairs() {
        return (ArrayList<Pair>)toc.stream()
                                   .map(obj -> new Pair(obj.getLocation(), obj.getSentiment()))
                                                                               .collect(Collectors.toList());
         
    }

    /**
     * Returns the name of the collection.
     * @return collectionName
     */
    public String getCollectionName(){
        return this.collectionName;
    }
    private String formatDate(String date) {

        String[] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        return (Arrays.asList(month).indexOf(date.substring(4,7)) + 1) +
                        "/" + date.substring(8, 10) + "/" + date.substring(24,date.length());
    }
}
