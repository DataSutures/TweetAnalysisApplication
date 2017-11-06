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

import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.text.Format;

import twitter4j.Status;
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
    
    static ArrayList<String[]> mapMarkerPositive = new ArrayList<>();
    static ArrayList<String[]> mapMarkerNegative= new ArrayList<>();
    static ArrayList<String[]> mapMarkerNeutral = new ArrayList<>();
    private XYChart.Series series1;
    private XYChart.Series series2;
    private XYChart.Series series3;
    ObservableList<PieChart.Data> pieChartData;
    private String searchTerm;
    private final ObservableList<TableObject> tweets = FXCollections.observableArrayList();
    
    //private final ObservableList<TableObject> tweets = FXCollections.observableArrayList();
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
        createdOn.setStyle("-fx-alignment: CENTER;");
        sentiment.setCellValueFactory(new PropertyValueFactory<TableObject, String>("sentiment"));
        sentiment.setStyle("-fx-alignment: CENTER;");
    }  

    @FXML
    private void handleActionButton(ActionEvent event) throws TextAPIException {
        //if its a new search term clear all else add more to current data
        /*if (!searchField.getText().equals(searchTerm) ){
            // popup "Do you want to save your current session?"
            // if "Save" Save to mongo else clear Table, Charts and map for new search
            table.refresh();
            series1.getData().clear();
            barChart.getData().clear();
            pieChartData.clear();
            pieChart.getData().clear();

        }
        */
        // Query Twitter by topic and create a collection
        searchTerm = searchField.getText();
        TweetCollection tweetCollection = new TweetCollection(TwitterQuery.getTweets(searchTerm));

        // Create Table Objects
        TableObjectCollection toc = new TableObjectCollection(tweetCollection);
        table.setItems(toc.getTweetObjects());
         
        //bar chart code
        series1 = new XYChart.Series<>();
        series1.setName("Positive");
        series2 = new XYChart.Series<>();
        series2.setName("Negative");
        series3 = new XYChart.Series<>();
        series3.setName("Neutral");
        XYChart.Data<String,Number> dataPOS = new XYChart.Data("",tweetCollection.getPosCount());
        XYChart.Data<String, Number> dataNEG = new XYChart.Data("", tweetCollection.getNegCount());
        XYChart.Data<String, Number> dataNEU = new XYChart.Data("", tweetCollection.getNeuCount());
        series1.getData().add(dataPOS);
	series2.getData().add(dataNEG);
	series3.getData().add(dataNEU);  
        barChart.setTitle(StringUtils.capitalize(searchTerm) + " Sentiment Summary");
        barChart.getData().addAll(series1,series2,series3);
         
        //piechart code
         pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Positive ", tweetCollection.getPosCount()),
            new PieChart.Data("Negative",tweetCollection.getNegCount()),
            new PieChart.Data("Neutral", tweetCollection.getNeuCount())
        );
        pieChart.setTitle(StringUtils.capitalize(searchTerm) + " Sentiment Percentages");
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