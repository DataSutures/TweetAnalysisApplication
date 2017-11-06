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
    
    private final ObservableList<TableObject> tweets = FXCollections.observableArrayList();
    
    public TableObjectCollection(TweetCollection toc){
        
        // create a single tableObject
        for (int i = 0; i < toc.getCollection().size(); i++){
            Tweet t = toc.getCollection().get(i);
            TableObject to = new TableObject(t.getScreenName(),t.getTweetText(),t.getCreatedOn(),t.getSentiment());
            tweets.add(to);
        }
    }
    public ObservableList<TableObject> getTweetObjects() {
        return tweets;
    }
}
