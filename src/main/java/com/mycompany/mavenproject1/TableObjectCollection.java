/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.SimpleIntegerProperty;

/**
 *
 * @author kimberlysmith
 */
public class TableObjectCollection {
    
    private final ObservableList<TableObject> toc = FXCollections.observableArrayList();
    private SimpleIntegerProperty posCount = new SimpleIntegerProperty();
    private SimpleIntegerProperty negCount = new SimpleIntegerProperty();
    private SimpleIntegerProperty neuCount = new SimpleIntegerProperty();
    public TableObjectCollection(TweetCollection tweets){
        
        // create a single tableObject and add to collection
        for (int i = 0; i < tweets.getCollection().size(); i++){
            Tweet t = tweets.getCollection().get(i);
            TableObject to = new TableObject(t.getScreenName(),t.getTweetText(),t.getCreatedOn(),t.getSentiment());
            toc.add(to);
        }
        // count and store Sentiments
        System.out.print("\nOriginal\nPositive: " + tweets.getPosCount() + "\nNegCOunt: " + tweets.getNegCount() + "\nNeut: " + tweets.getNeuCount());
        posCount.setValue(tweets.getPosCount());
        negCount.setValue(tweets.getNegCount());
        neuCount.setValue(tweets.getNeuCount());
         System.out.print("\n InitialCOunt\nPositiveCount: " + posCount + "\nNegativeCount: " + negCount + "\nNeutralCount: " + negCount);
    }
    // Get Collection
    public ObservableList<TableObject> getTweetObjects() {
        return toc;
    }
   
    public void updateCounts(ObservableList<TableObject> toRemove){

        // count sentiments to subtract
        long pos = toRemove.stream().filter(t -> t.getSentiment().equals("Positive")).count();
        long neg = toRemove.stream().filter(t -> t.getSentiment().equals("Negative")).count();
        long neu = toRemove.stream().filter(t -> t.getSentiment().equals("Neutral")).count();
        System.out.print("\nPos: " + pos + "\nNeg: " + neg + "\nNeut: " + neu);
        //set new sentiment value by subtracting remove tweet sentiments
        posCount.setValue(posCount.get() - (int)pos);
        negCount.setValue(negCount.get() - (int)neg);
        neuCount.setValue(neuCount.get() - (int)neu);
        
        System.out.print("\nafter delete count\nPositiveCount: " + posCount + "\nNegativeCount: " + negCount + "\nNeutralCount: " + negCount);
        
    }
    public int getPosCount(){
        return this.posCount.get();
    }
    public int getNegCount(){
        return this.negCount.get();
    }
    public int getNeuCount(){
        return this.neuCount.get();
    }
}
