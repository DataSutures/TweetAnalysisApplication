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
 *
 * @author kimberlysmith
 */
public class TweetCollection {
  
    private final ObservableList<Tweet> toc = FXCollections.observableArrayList();
    private String collectionName = "";

    public TweetCollection(String searchTerm, List<Status> tweets){
        this.collectionName = searchTerm;
        
        tweets.forEach(t -> toc.add(new Tweet(t.getUser().getScreenName(),
                                            t.getText(),
                                            formatDate(t.getCreatedAt().toString()),
                                            t.getUser().getLocation()
                                            )));
        }
        

    // Get Collection
    public ObservableList<Tweet> getTweetObjects() {
        return toc;
    }
    public int getPosCount(){
        return (int)toc.stream().filter(t -> t.getSentiment().equals("Positive")).count();
    }
    public int getNegCount(){
        return (int)toc.stream().filter(t -> t.getSentiment().equals("Negative")).count();
    }
    public int getNeuCount(){
        return (int)toc.stream().filter(t -> t.getSentiment().equals("Neutral")).count();
    }
    public ArrayList<Pair> getLocSentPairs() {
        return (ArrayList<Pair>)toc.stream()
                                   .map(obj -> new Pair(obj.getLocation(), obj.getSentiment()))
                                                                               .collect(Collectors.toList());
         
    }
   public String getCollectionName(){
        return this.collectionName;
    }
    private String formatDate(String date) {

        String[] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        return (Arrays.asList(month).indexOf(date.substring(4,7)) + 1) +
                        "/" + date.substring(8, 10) + "/" + date.substring(24,date.length());
    }
}
