/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;
import java.lang.StringBuilder;
import java.lang.Object;

//AIzaSyAP151023bUGcXb0m1_lNxfJi5LXuzzStw	
import com.google.maps.GeocodingApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.GeoApiContext.RequestHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeolocationApiRequest;
import com.google.maps.model.GeocodingResult;
public class Maps {
   String location;
   String clientID =  "https://maps.googleapis.com/maps/api/directions/json"
      +"?origin=Toronto"
      +"&destination=Montreal"
      +"&client=gme-YOUR_CLIENT_ID"
      +"&signature=YOUR_URL_SIGNATURE";
    
   public Maps(){
       location ="New York, NY";
   }
   
    public Maps(String location){
        this.location = location;
    }
    
    public void getCoordinates()
    {
        
        GeoApiContext context = new GeoApiContext.Builder()
            .apiKey("AIzaSyAP151023bUGcXb0m1_lNxfJi5LXuzzStw")
            .build();
        try{
            GeocodingResult[] results =  GeocodingApi.geocode(context,
            "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(results[0].addressComponents));
        }catch(Exception e){
            System.out.println("Error");
        }
   
        
    }
}
