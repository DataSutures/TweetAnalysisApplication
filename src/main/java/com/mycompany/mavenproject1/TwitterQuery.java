/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.Status;
import twitter4j.Query;
import twitter4j.QueryResult;

public class TwitterQuery {
    
    //Search twitter by topic and return Status List
    public static List<Status> getTweets(String topic){
        
        Twitter twitter = new TwitterFactory().getInstance();
        List<Status> tweetsList = new ArrayList<>();
        try {
            Query q = new Query(topic + " -filter:links -filter:retweets -filter:replies ");
            q.setCount(25);
            q.setLang("en");
            QueryResult r = twitter.search(q);  
            tweetsList = r.getTweets();
        }catch(TwitterException e){
            e.printStackTrace();
        }
        return tweetsList;
    }
}
