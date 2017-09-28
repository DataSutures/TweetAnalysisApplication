/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Iterator;

import twitter4j.Status;

import com.mongodb.util.JSON;
import com.mongodb.DBObject;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.AggregationOutput;
import org.bson.Document;

import org.json.JSONObject;

import com.aylien.textapi.responses.Sentiment;
import com.aylien.textapi.TextAPIException;

/**
 *
 * @author kimberlysmith
 */
public class MongoDB {
    
    private final MongoClient mongoClient;
    private final DB database;
    private final DBCollection tweetsTable;
    
    /* Creates a new database titled by topic if one does not exist 
    / otherwise returns instance of existing 
    */
    public  MongoDB(String topic) throws UnknownHostException{
        
        mongoClient = new MongoClient() ;
        database = mongoClient.getDB("AnalyzedTweetsCollection");
        tweetsTable = database.getCollection(topic); 
        
    }
    // Insert tweets and new feild "Sentiment" with analysis results
    public void insertTweetCollection(List<Status> tweets){     
    }
    // get totals by sentiment and return Iterable list
    public Iterator<DBObject> sentimentTotals(){
        
        DBObject groupFields = new BasicDBObject( "_id", "$sentiment");
        groupFields.put("count", new BasicDBObject( "$sum", 1));
        DBObject group = new BasicDBObject("$group", groupFields );
        DBObject sortFields = new BasicDBObject("count", -1);
        DBObject sort = new BasicDBObject("$sort", sortFields );
        AggregationOutput output = tweetsTable.aggregate(group,sort);
        return output.results().iterator();
    }
    // Query DB by Sentiment String 
    // Equivalent to MySQL SELECT text, Sentiment FROM tweetsTable WHERE sentiment = inputSentiment
    public Iterator<DBObject> querySentiment(String inputSentiment){
        
        BasicDBList sentimentList = new BasicDBList();
        BasicDBObject searchObject = new BasicDBObject();
        searchObject.put("sentiment", inputSentiment);
        BasicDBObject fieldObject = new BasicDBObject();
        fieldObject.put("text", 1);
        fieldObject.put("sentiment", 1);
          
        return tweetsTable.find(searchObject,fieldObject).iterator();

    }   
    public void printSentimentTotals(){
        Iterator<DBObject> it = sentimentTotals();
        while (it.hasNext())
            System.out.print(it.next());
    }
    public void printPositive(){
        Iterator<DBObject> it = querySentiment("Positive");
        while (it.hasNext())
            System.out.print(it.next());
    }
    public void printNegative(){
        Iterator<DBObject> it = querySentiment("Negative");
        while (it.hasNext())
            System.out.print(it.next());
    }
    public void printNeutral(){
        Iterator<DBObject> it = querySentiment("Neutral");
        while (it.hasNext())
            System.out.print(it.next());
    }
    // close connection to DB
    public void close(){
        mongoClient.close();
    }
    // Creates a new document containing a tweet with reduced fields and inserts it into DB
//    private void insertReduced(Status tweet){
//        Document newDoc = new Document();
//        newDoc.append("_id", String.valueOf(tweet.getId()))
//                .append("screenName",tweet.getUser().getScreenName())
//                .append("userId", String.valueOf(tweet.getUser().getId()))
//                .append("text", tweet.getText())
//                .append("created_on", tweet.getCreatedAt().toInstant().toString())
//                .append("sentiment", getSentimentStanford(tweet.getText()));
//        tweetsTable.insert((DBObject)JSON.parse(newDoc.toJson()));
//    }
    // Return sentiment string of tweet text feild using Standford NLP
//    private static String getSentimentStanford(String text){
//        
//        StanfordNLP.init();
//        int score = StanfordNLP.getSentiment(text);
//        if(score < 2){
//            return "Negative";
//        }
//        else if(score == 2){
//            return "Neutral";
//        }else return  "Positive";
//    }
     // Return sentiment string of tweet text feild using AylienAPI
    private static String getSentimentAylien(String text) throws TextAPIException{
        Sentiment sent;
        AylienAnalysis alienResults = new AylienAnalysis();
        sent = alienResults.analyzeTweet(text);
        return sent.getText();
    }
   
}
