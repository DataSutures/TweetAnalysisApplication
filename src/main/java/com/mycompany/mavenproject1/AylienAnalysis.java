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

public class AylienAnalysis {
    
final TextAPIClient aylienClient;

    public AylienAnalysis(){
        aylienClient = new TextAPIClient("5a72f6ec", "8bf727229b434801d9cf732a01279f37");
    }
  // Analyzes the sentiment of a tweets and return Sentiment Object
    public Sentiment analyzeTweet(String text) throws TextAPIException{
        SentimentParams.Builder builder = SentimentParams.newBuilder();
        builder.setText(text);
        return aylienClient.sentiment(builder.build());
    }
}
