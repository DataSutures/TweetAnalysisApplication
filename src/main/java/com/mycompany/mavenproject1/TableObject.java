/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
/**
 *
 * @author kimberlysmith
 */
public class TableObject {
    private final SimpleStringProperty screenName;
    private final SimpleStringProperty tweetText;
    private final SimpleStringProperty createdOn;
    private final SimpleStringProperty sentiment;

    public TableObject(String screenName,String tweetText,String createdOn,String sentiment)
    {
        this.screenName = new SimpleStringProperty(screenName);
        this.tweetText = new SimpleStringProperty(tweetText);
        this.createdOn = new SimpleStringProperty(createdOn);
        this.sentiment = new SimpleStringProperty(sentiment);
    }
    public String getScreenName() {
        return screenName.get();
    }

    public String getTweetText() {
        return tweetText.get();
    }

    public String getCreatedOn() {
        return createdOn.get();
    }

    public String getSentiment() {
        return sentiment.get();
    }
    @Override
    public String toString(){
        return "sn: " + screenName.get() + "\ntext: " + tweetText.get() + "\ncreatedOn: " +
                createdOn.get() + "\nsentiment: " + sentiment.get();
    }
}
