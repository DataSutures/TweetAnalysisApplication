package com.mycompany.mavenproject1;

import com.aylien.textapi.TextAPIException;
import com.sun.xml.internal.ws.util.StringUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.Iterator;
import java.util.ArrayList;

import twitter4j.Status;
import java.util.LinkedHashSet;
import java.util.List;
import javafx.event.Event;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;

public class FXMLController implements Initializable {   
    
    final static String positive ="Positive";
    final static String negative ="Negative";
    final static String neutral = "Neutral";
    static ArrayList<String[]> mapMarkerPositive = new ArrayList<>();
    static ArrayList<String[]> mapMarkerNegative= new ArrayList<>();
    static ArrayList<String[]> mapMarkerNeutral = new ArrayList<>();
    private int positiveCount = 0;
    private int negativeCount = 0;
    private int neutralCount = 0;
    
    private final ObservableList<TableObject> tweets = FXCollections.observableArrayList();
    @FXML
    private Tab TweetTab; 
    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> filterBox;
    @FXML
    private Button submitButton;
    @FXML
    private TableView<TableObject> table;
    @FXML 
    private TableColumn<TableObject, String> screenName;
    @FXML 
    private TableColumn<TableObject, String> tweetText;
    @FXML
    private TableColumn<TableObject, String> createdOn; 
    @FXML
    private TableColumn<TableObject, String> sentiment;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private AnchorPane anchorPane1;
    @FXML
    private Tab ChartTab;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private NumberAxis yAxis;
    @FXML
    private CategoryAxis xAxis;
    @FXML
    private PieChart pieChart;
    @FXML
    private Tab MapTab;
    @FXML
    private WebView webView;

        
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Initialize Table columns
        screenName.setCellValueFactory(new PropertyValueFactory<TableObject, String>("screenName"));
        tweetText.setCellValueFactory(new PropertyValueFactory<TableObject, String>("tweetText"));
        createdOn.setCellValueFactory(new PropertyValueFactory<TableObject, String>("createdOn"));
        sentiment.setCellValueFactory(new PropertyValueFactory<TableObject, String>("sentiment"));
    }  

    @FXML
    private void handleActionButton(ActionEvent event) throws TextAPIException {
        if (tweets.isEmpty() == false) {
            // popup "Do you want to save your current session?"
            // if "Save" Save to mongo else clear Table, Charts and map for new search
            tweets.clear();
            table.refresh();
        }
        String toSearch = searchField.getText();
        // Query Twitter by topic
        List<Status> tweetResult = TwitterQuery.getTweets(toSearch);
        String sn,text,date,sent,sen;
        AylienAnalysis alienResults = new AylienAnalysis();
        for (Iterator<Status> it = tweetResult.iterator(); it.hasNext();) {
            Status s = it.next();
            sn = s.getUser().getScreenName();
            text = s.getText();
            date = s.getCreatedAt().toString();
            String[] textLoc =  {text, s.getUser().getLocation()};
            sen = alienResults.analyzeTweet(text).getPolarity().toString(); // Analyze Text
            sent = StringUtils.capitalize(sen);
            // Add to appropriate Marker List based on Sentiment and increase counts
            switch (sent){
                case "Positive": mapMarkerPositive.add(textLoc); positiveCount++; break;
                case "Negative":mapMarkerNegative.add(textLoc); negativeCount++; break;
                default: mapMarkerNeutral.add(textLoc); neutralCount++; break;
                
            }
            TableObject to = new TableObject(sn,text,date,sent);
            //System.out.print(to.toString());
            tweets.add(to);
            

        }
       
         table.setItems(tweets);
         
         //bar chart code
        XYChart.Series set1 = new XYChart.Series<>();
        /* use later to change color
        final XYChart.Data<String, Number> dataP = new XYChart.Data(positive, 25601.34);
        final XYChart.Data<String, Number> dataNE = new XYChart.Data(neutral, 41941.19);
        final XYChart.Data<String, Number> dataNU = new XYChart.Data(negative, 35000.19);
        set1.getData().add(dataP);
	set1.getData().add(dataNE);
	set1.getData().add(dataNU);
        */
        
        
        //to change the values on the bar chart change numbers
        // set values of 200, 300,100
        set1.getData().add(new XYChart.Data<>(positive,positiveCount));
        set1.getData().add(new XYChart.Data<>(negative, negativeCount));
        set1.getData().add(new XYChart.Data<>(neutral,neutralCount));
        barChart.getData().add(set1);
    
        //piechart code
        //to change value of pie chart adjust 100,200,300
         ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Positive", 200),
             new PieChart.Data("Negative",300),
            new PieChart.Data("Neutral", 100)
           
    
        );
        pieChart.setData(pieChartData);
         
    }

    @FXML
    private void loadMaps(Event event) {
        System.out.println("hi");                 
               //look at location array
               String part1 =  "<!DOCTYPE html>\n" +
                "<html> \n" +
                "<head> \n" +
                "  <meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\" /> \n" +
                "  <title>Google Maps Multiple Markers</title> \n" +
                "  <script src=\"http://maps.google.com/maps/api/js?sensor=false\" \n" +
                "          type=\"text/javascript\"></script>\n" +
                "</head> \n" +
                "<body>\n" +
                "  <div id=\"map\" style=\" width: 100%; height: 100%; margin:0; padding:0; position:absolute;\"></div>\n" +
                "\n"+
                "<script type=\"text/javascript\">\n";
                String location="    var locations = [\n" +
                "      ['Tweet Data, Sentiment', 30.984298, -91.96233, 3],\n" +
                "      ['New York', 40.712775, -74.005973, 2],\n" +
                "      ['California', 36.778261, -119.417932, 1],\n" +
               
                "    ];\n";
                String part2="\n" +
                "    var map = new google.maps.Map(document.getElementById('map'), {\n" +
                "      zoom: 10,\n" +
                "      center: new google.maps.LatLng(37.09024, -91.962333),\n" +
                "      mapTypeId: google.maps.MapTypeId.ROADMAP\n" +
                "    });\n" +
                "\n" +
                "    var infowindow = new google.maps.InfoWindow();\n" +
                "\n" +
                "    var marker, i;\n" +
                "\n" +
                "\n var bounds = new google.maps.LatLngBounds();" +       
                "    for (i = 0; i < locations.length; i++) {  \n" +
                "      marker = new google.maps.Marker({\n" +
                "        position: new google.maps.LatLng(locations[i][1], locations[i][2]),\n" +
                "        map: map\n" +
                "      });\n" +
                "\n" +
                "      google.maps.event.addListener(marker, 'click', (function(marker, i) {\n" +
                "        return function() {\n" +
                //info windows set the data for the tweet
                "          infowindow.setContent(locations[i][0]);\n" +
                "          infowindow.open(map, marker);\n" +
                "        }\n" +
                "      })(marker, i));\n" +
                "    }\n" +
                "  </script>\n" +
                "</body>\n" +
                "</html>";




             /*
                code centers around the markers
                var markers = [];//some array
                var bounds = new google.maps.LatLngBounds();
                for (var i = 0; i < markers.length; i++) {
                    bounds.extend(markers[i].getPosition());
                }
                map.fitBounds(bounds);
                
                */



        WebEngine engine = webView.getEngine();
        //URL url = getClass().getResource("map.html");
        //String url = getClass().getResource("/TweetAnalysisApplication/map.html").toExternalForm();
        String link = part1+location+part2;
        engine.loadContent(link);
        
    }
}