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


public class Maps {
   String location="New York,NY";
//   String clientID =  "https://maps.googleapis.com/maps/api/directions/json"
//      +"?origin=Toronto"
//      +"&destination=Montreal"
//      +"&client=gme-YOUR_CLIENT_ID"
//      +"&signature=YOUR_URL_SIGNATURE";
     List<GeocodingResult[]> code = new ArrayList();
     ArrayList<String> initLocation = new ArrayList();

     StringBuffer temp = new StringBuffer();
        GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("AIzaSyAP151023bUGcXb0m1_lNxfJi5LXuzzStw")
            .build();

   public Maps(){
       location ="Los Angles, CA";
       
   }
   //handles one address
    public Maps(String location){
        this.location = location;
    }
    
    //handles multiple address
    public Maps(ArrayList a){
        initLocation = a;
    }
    
    //geocode address
    //update needs to take in string list ideally in this format
    //"Lafayette,LA", "New York,NY", "Los Angles,CA"
    public StringBuffer getCoordinates(ArrayList<String> tweetLocation)
    {
        //
        StringBuffer temp = new StringBuffer();
       

        try
        {
            //GeocodingResult[] results =  GeocodingApi.geocode(context,
            //location).await();
            //String[] loc = {"Lafayette,LA", "New York,NY", "Los Angles,CA"};
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            //GeocodingApiRequest request = new GeocodingApiRequest(context);
            //GeocodingApiRequest r2 = GeocodingApi.geocode(context, "Lafayette,LA");
            //r2.toString()
            //System.out.println(gson.toString(results[0].geometry.location));
          // System.out.println(gson.toJson(results[0].geometry.location));
         temp.append("var locations = ");
          temp.append("[");
          GeocodingResult[] r;
      
          
           for(int i=0; i<tweetLocation.size(); i++){
              r= GeocodingApi.geocode(context,tweetLocation.get(i)).await();;
               temp.append("[");
               temp.append(gson.toJson(r[0].formattedAddress));
               temp.append(",");
               temp.append(gson.toJson(r[0].geometry.location.lat));
               temp.append(",");
               temp.append(gson.toJson(r[0].geometry.location.lng));
               temp.append(", ");
               temp.append("]");
               temp.append(",");           
           }
           //int k=code.size();
           
          
           //System.out.print(temp.toString());
           return temp;
           
        }
        
        catch(Exception e)
        {
            System.out.println("Error");
        }
        return temp;
        //return null;
    }
}
//https://maps.googleapis.com/maps/api/geocode/json?address=Lafayette,LA&key=AIzaSyAP151023bUGcXb0m1_lNxfJi5LXuzzStw