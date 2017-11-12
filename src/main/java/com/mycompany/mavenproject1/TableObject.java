/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;
import com.aylien.textapi.TextAPIException;
import com.sun.xml.internal.ws.util.StringUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author kimberlysmith
 */
public class TableObject {
    private SimpleStringProperty screenName = new SimpleStringProperty("");
    private SimpleStringProperty tweetText = new SimpleStringProperty("");
    private SimpleStringProperty createdOn = new SimpleStringProperty("");
    private SimpleStringProperty sentiment = new SimpleStringProperty("");
    private String location = "";
    public Boolean selected=false;
    
    public TableObject(String screenName,String tweetText,String createdOn, String location)
    {
        this.screenName.set(screenName);
        this.tweetText.set(tweetText);
        this.createdOn.set(createdOn);
        // analyze tweet and set sentiment field
        try {
            this.sentiment.set(sentiment(tweetText));
        }catch(TextAPIException e) {
            System.out.print(e.getMessage());
        }
        this.location = location;

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
    public String getLocation() {
        return location;
    }
    
    public Boolean isSelected(){
        return selected;
    }
   
    public void setSelected(boolean b){
        this.selected = b;
    }
    
    // Analyze Text
    private String sentiment(String text) throws TextAPIException {
        
        AylienAnalysis alienResults = new AylienAnalysis();
        return StringUtils.capitalize(alienResults.analyzeTweet(text).getPolarity()); 
        
    }
    @Override
    public String toString(){
        return "sn: " + screenName.get() + "\ntext: " + tweetText.get() + "\ncreatedOn: " +
                createdOn.get() + "\nsentiment: " + sentiment.get()+"\nSelected:"+selected;
    }
    
}
