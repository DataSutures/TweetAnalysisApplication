/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;
import java.lang.StringBuilder;
import java.lang.Object;
import java.lang.StringBuffer;
//AIzaSyAP151023bUGcXb0m1_lNxfJi5LXuzzStw	
import com.google.maps.GeocodingApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.GeoApiContext.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeolocationApiRequest;
import com.google.maps.model.GeocodingResult;
import java.util.*;
import java.util.Iterator;
import javafx.util.Pair;

public class Maps {
   //String location = "";
//   String clientID =  "https://maps.googleapis.com/maps/api/directions/json"
//      +"?origin=Toronto"
//      +"&destination=Montreal"
//      +"&client=gme-YOUR_CLIENT_ID"
//      +"&signature=YOUR_URL_SIGNATURE";
    
     //ArrayList<String> initLocation = new ArrayList();

     //StringBuffer temp = new StringBuffer();
        GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("AIzaSyAP151023bUGcXb0m1_lNxfJi5LXuzzStw")
            .build();

   public Maps(){
      
       
   }
   //handles one address
    
    //handles multiple address
  
   
    
    public StringBuffer applySentiment(ArrayList<Pair> sentLocation){
        StringBuffer returnString = new StringBuffer();
        
        returnString.append("var locations = ");
        returnString.append("[");
        GeocodingResult[] r;
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        String maybeLat;
        String maybeLng;
        Iterator it = sentLocation.iterator();
        int k=sentLocation.size();
          while(it.hasNext()){
           Pair pair = (Pair<String, String>)it.next();
           //if (allLettersorDigits){
                try{
                r = GeocodingApi.geocode(context,(String)pair.getKey()).await();
                maybeLat = gson.toJson(r[0].geometry.location.lat);
                maybeLng = gson.toJson(r[0].geometry.location.lng);
                returnString.append("[")
                    .append("'"+pair.getValue()+"'")
                    .append(",")
                    .append(maybeLat)
                    .append(",")
                    .append(maybeLng)
                    .append(", ")
                    .append(k)
                    .append("]")
                    .append(","); 
                k=k-1;
                
                
                }catch(Exception e){ 
                    //temp.delete(0, temp.length()-1);
                    continue;
                    
                }
                    
                
            }
        return returnString;
        }
       
    }
        
    

//https://maps.googleapis.com/maps/api/geocode/json?address=Lafayette,LA&key=AIzaSyAP151023bUGcXb0m1_lNxfJi5LXuzzStw