/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;
import javafx.beans.property.SimpleBooleanProperty;
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
    public Boolean selected=false;
    
    
    /* private final SimpleStringProperty place;*/

    public TableObject(String screenName,String tweetText,String createdOn,String sentiment/*, String place*/)
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
    
    public Boolean isSelected(){
        return selected;
    }
   
    
    public void setSelected(boolean b){
        this.selected = b;
        
        /*if (check.getValue() == true){
            check.set(false);
        }
        else
            check.set(true);*/
    }

    /*public void Clear(){
        
    }*/
    /*
    public String getPlace() {
        return place.get();
    */
    
    
    
    @Override
    public String toString(){
        return "sn: " + screenName.get() + "\ntext: " + tweetText.get() + "\ncreatedOn: " +
                createdOn.get() + "\nsentiment: " + sentiment.get()+"\nSelected:"+selected;
    }
}
