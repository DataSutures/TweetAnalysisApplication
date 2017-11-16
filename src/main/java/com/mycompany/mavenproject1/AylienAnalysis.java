/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import com.aylien.textapi.TextAPIClient;
import com.aylien.textapi.TextAPIException;
import com.aylien.textapi.parameters.SentimentParams;
import com.aylien.textapi.responses.Sentiment;

/**
 *
 * @author Danny
 */
public class AylienAnalysis {
    
final TextAPIClient aylienClient;

    /**
     * Calls upon the use of the aylien analysis system.
     */
    public AylienAnalysis(){
        aylienClient = new TextAPIClient("5a72f6ec", "8bf727229b434801d9cf732a01279f37");
    }
  // Analyzes the sentiment of a tweets and return Sentiment Object

    /**
     * Analyzes a tweet object to then give it a sentiment value.
     * @param text
     * The text associated with a tweet object.
     * @return Sentiment
     * @throws TextAPIException
     * If it fails to analyze the tweet, this exception will be thrown.
     */
    public Sentiment analyzeTweet(String text) throws TextAPIException{
        SentimentParams.Builder builder = SentimentParams.newBuilder();
        builder.setText(text);
        return aylienClient.sentiment(builder.build());
    }
}
