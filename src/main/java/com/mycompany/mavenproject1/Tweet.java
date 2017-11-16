/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;
import com.aylien.textapi.TextAPIException;
import com.sun.xml.internal.ws.util.StringUtils;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author kimberlysmith
 */
public class Tweet {
    private SimpleStringProperty screenName = new SimpleStringProperty("");
    private SimpleStringProperty tweetText = new SimpleStringProperty("");
    private SimpleStringProperty createdOn = new SimpleStringProperty("");
    private SimpleStringProperty sentiment = new SimpleStringProperty("");
    private String location = "";

    /**
     * Initialized value for the check boxes for each tweet object.
     */
    public Boolean selected=false;
    
    /**
     * Constructor for a tweet object.
     * 
     * @param screenName 
     * User name of the creator of the tweet.
     * @param tweetText
     * The actual message from the tweet itself.
     * @param createdOn
     * Date the tweet was created on.
     * @param location
     * Location of origin that the tweet was created.
     */
    public Tweet(String screenName,String tweetText,String createdOn, String location)
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

    /**
     * Returns the screen name of the tweet object.
     * @return screenName
     */
    public String getScreenName() {
        return screenName.get();
    }

    /**
     * Returns the text associated with the tweet object.
     * @return tweetText
     */
    public String getTweetText() {
        return tweetText.get();
    }

    /**
     * Returns the date in which the tweet object was created on.
     * @return createdOn
     */
    public String getCreatedOn() {
        return createdOn.get();
    }

    /**
     * Returns the sentiment value assigned to the tweet object.
     * @return sentiment
     */
    public String getSentiment() {
        return sentiment.get();
    }

    /**
     * Returns the location where the tweet object was created.
     * @return location
     */
    public String getLocation() {
        return location;
    }
    
    /**
     * Returns whether or not the tweet object has been selected in the table.
     * @return selected
     */
    public Boolean isSelected(){
        return selected;
    }
   
    /**
     * Sets the tweet object to true/false based upon whether or not it's selected in the table.
     * @param b
     * Sets the value of selected to whatever b is.
     */
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
