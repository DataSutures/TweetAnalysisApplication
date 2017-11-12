/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author kimberlysmith
 */
public class TableObjectCollection {
    
    private final ObservableList<TableObject> toc = FXCollections.observableArrayList();

    public TableObjectCollection(TweetCollection tweets){
        
        // create a single tableObject and add to collection
        for (int i = 0; i < tweets.getCollection().size(); i++){
            Tweet t = tweets.getCollection().get(i);
            TableObject to = new TableObject(t.getScreenName(),t.getTweetText(),t.getCreatedOn(),t.getSentiment(),t.getLocation());
            toc.add(to);
        }
        
}
    // Get Collection
    public ObservableList<TableObject> getTweetObjects() {
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
}
