/* 
 */
package com.mycompany.mavenproject1;

import com.aylien.textapi.TextAPIException;
import com.sun.xml.internal.ws.util.StringUtils;
import twitter4j.Status;
import java.util.Arrays;

public class Tweet {
    private String screenName ="";
    private String tweetText = "";
    private String createdOn = "";
    private String sentiment = "" ;
    private String location = "";  // may move location to this level, if we want to do database
    
    
    public Tweet(String sn, String text, String date, String sent, String loc){
        this.screenName = sn;
        this.tweetText = text;
        this.createdOn = date;
        this.sentiment = sent;
        this.location = loc;
    }
    public Tweet(Status status)
    {   // set fields
        this.screenName = status.getUser().getScreenName();
        this.tweetText = status.getText();
        this.createdOn = formatDate(status.getCreatedAt().toString());
        this.location = status.getUser().getLocation();
    
        // analyze tweet and set sentiment field
        try {
            this.sentiment = sentiment(tweetText);
        }catch(TextAPIException e) {
            System.out.print(e.getMessage());
        }
        
        // get geolocation from Google API
        
  
        
    }
    // Format date i.e. 9/12/20017
    private String formatDate(String date) {

        String[] month = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};
        return (Arrays.asList(month).indexOf(date.substring(4,7)) + 1) +
                        "/" + date.substring(8, 10) + "/" + date.substring(24,date.length());
    }
    // Analyze Text
    private String sentiment(String text) throws TextAPIException {
        
        AylienAnalysis alienResults = new AylienAnalysis();
        return StringUtils.capitalize(alienResults.analyzeTweet(text).getPolarity()); 
        
    }
    // call Google API and retrieve geolocation
    private void geolocation(){
  
    }

    public String getScreenName() {
        return screenName;
    }

    public String getTweetText() {
        return tweetText;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getSentiment() {
        return sentiment;
    }
    
    public String getLocation() {
        return location;
    }
    
    @Override
    public String toString(){
        return "sn: " + screenName + "\ntext: " + tweetText + "\ncreatedOn: " +
                createdOn + "\nsentiment: " + sentiment;
    }
}

