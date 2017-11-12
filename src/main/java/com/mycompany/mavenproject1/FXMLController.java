package com.mycompany.mavenproject1;

import com.aylien.textapi.TextAPIException;
import com.sun.xml.internal.ws.util.StringUtils;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
//import javafx.collections.FXCollections.observableArrayList;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.scene.control.CheckBox;
import java.util.ArrayList;

import twitter4j.Status;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;


import java.lang.StringBuffer;

public class FXMLController implements Initializable {   
    
    static ArrayList<String[]> mapMarkerPositive = new ArrayList<>();
    static ArrayList<String[]> mapMarkerNegative= new ArrayList<>();
    static ArrayList<String[]> mapMarkerNeutral = new ArrayList<>();

    private XYChart.Series series1 = new XYChart.Series();
    private XYChart.Series series2 = new XYChart.Series();
    private XYChart.Series series3 = new XYChart.Series();
    ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
    private String searchTerm = "";
    private String newSearchTerm;

    private int positiveCount = 0;
    private int negativeCount = 0;
    private int neutralCount = 0;
    StringBuffer allLocations = new StringBuffer();
    ArrayList<String> tweetLocation;
    WebEngine engine;

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
    private Button deleteButton;
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
    private TableColumn<TableObject, Boolean> checkBox;
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
    //@FXML 
    //private ColumnSelectRow columSelect;
        
    
    TableObjectCollection toc;
    TweetCollection tweetCollection;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Initialize Table columns
        screenName.setCellValueFactory(new PropertyValueFactory<TableObject, String>("screenName"));
        tweetText.setCellValueFactory(new PropertyValueFactory<TableObject, String>("tweetText"));
        createdOn.setCellValueFactory(new PropertyValueFactory<TableObject, String>("createdOn"));
        createdOn.setStyle("-fx-alignment: CENTER;");
        sentiment.setCellValueFactory(new PropertyValueFactory<TableObject, String>("sentiment"));
        sentiment.setStyle("-fx-alignment: CENTER;");
        TableColumn<TableObject, CheckBox> column = (TableColumn<TableObject, CheckBox>) table.getColumns().get(4);
        column.setCellValueFactory(new TweetDeleteCellValueFactory());
    }  
    @FXML
    private void handleActionButton(ActionEvent event) throws TextAPIException {

        // Query Twitter by topic and create a collection
        searchTerm = searchField.getText();
        tweetCollection = new TweetCollection(searchTerm,TwitterQuery.getTweets(searchTerm));
        // Create Table Objects
         toc = new TableObjectCollection(tweetCollection);
        
        //if new search term clear all
        if (table.getItems().isEmpty() == false){
            table.refresh();
            series1.getData().clear();
            barChart.getData().clear();
            pieChartData.clear();
            pieChart.getData().clear();
            webView.getEngine().load("");
        }
        /******** load all views *******/ 
        // update Table view
        table.setItems(toc.getTweetObjects());

        
        // update Bar Chart view

        series1 = new XYChart.Series<>();
        series1.setName("Positive");
        series2 = new XYChart.Series<>();
        series2.setName("Negative");
        series3 = new XYChart.Series<>();
        series3.setName("Neutral");
        XYChart.Data<String, Number> dataPOS = new XYChart.Data("",toc.getPosCount());
        XYChart.Data<String, Number> dataNEG = new XYChart.Data("", toc.getNegCount());
        XYChart.Data<String, Number> dataNEU = new XYChart.Data("", toc.getNeuCount());  
        series1.getData().add(dataPOS);
	series2.getData().add(dataNEG);
	series3.getData().add(dataNEU);  
        barChart.setTitle(StringUtils.capitalize(searchTerm) + " Sentiment Summary");
        barChart.getData().addAll(series1,series2,series3);
         

        // update piechart View
         pieChartData = FXCollections.observableArrayList(

            new PieChart.Data("Positive ", toc.getPosCount()),
            new PieChart.Data("Negative",toc.getNegCount()),
            new PieChart.Data("Neutral", toc.getNeuCount())
         );
        pieChart.setTitle(StringUtils.capitalize(searchTerm) + " Sentiment Percentages");
        pieChart.setData(pieChartData);
        
        
        // Update map view 
        tweetLocation = tweetCollection.getLocations();
        System.out.print("\nFIRSTSETLOCATIONS: " + tweetCollection.getLocations().toString());
        Maps mapper = new Maps();
        StringBuffer b = mapper.getCoordinates(tweetLocation);
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
                "      ['New York', 30.984298, -91.96233, 2],\n" +
                "      ['California', 30.984298, -91.96233, 1],\n" +
               
                "    ];\n";
                String style= "styles: [{\"featureType\":\"all\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#3c3c3c\"}]},{\"featureType\":\"all\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"},{\"hue\":\"#0000ff\"}]},{\"featureType\":\"all\",\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"on\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"saturation\":36},{\"color\":\"#000000\"},{\"lightness\":40},{\"visibility\":\"off\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"visibility\":\"off\"},{\"color\":\"#000000\"},{\"lightness\":16}]},{\"featureType\":\"all\",\"elementType\":\"labels.icon\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"administrative\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":20}]},{\"featureType\":\"administrative\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":17},{\"weight\":1.2}]},{\"featureType\":\"administrative.country\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#ffffff\"},{\"weight\":\"0.62\"},{\"gamma\":\"3.89\"}]},{\"featureType\":\"administrative.country\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#ffffff\"}]},{\"featureType\":\"administrative.country\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"administrative.locality\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#c4c4c4\"}]},{\"featureType\":\"administrative.neighborhood\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"landscape\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":20}]},{\"featureType\":\"poi\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":21},{\"visibility\":\"on\"}]},{\"featureType\":\"poi.business\",\"elementType\":\"geometry\",\"stylers\":[{\"visibility\":\"on\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#e5c163\"},{\"lightness\":\"0\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":18}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#575757\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"color\":\"#2c2c2c\"}]},{\"featureType\":\"road.local\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":16}]},{\"featureType\":\"road.local\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#999999\"}]},{\"featureType\":\"transit\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":19}]},{\"featureType\":\"water\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":17}]},{\"featureType\":\"water\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"water\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"visibility\":\"off\"}]}]\n" +
"                };";
                String mapOptions = "var mapOptions=  {zoom: 10,\n" +
                "      center: new google.maps.LatLng(37.09024, -91.962333),\n" + 
                "styles: [{\"featureType\":\"all\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#3c3c3c\"}]},{\"featureType\":\"all\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"},{\"hue\":\"#0000ff\"}]},{\"featureType\":\"all\",\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"on\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"saturation\":36},{\"color\":\"#000000\"},{\"lightness\":40},{\"visibility\":\"off\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"visibility\":\"off\"},{\"color\":\"#000000\"},{\"lightness\":16}]},{\"featureType\":\"all\",\"elementType\":\"labels.icon\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"administrative\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":20}]},{\"featureType\":\"administrative\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":17},{\"weight\":1.2}]},{\"featureType\":\"administrative.country\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#ffffff\"},{\"weight\":\"0.62\"},{\"gamma\":\"3.89\"}]},{\"featureType\":\"administrative.country\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#ffffff\"}]},{\"featureType\":\"administrative.country\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"administrative.locality\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#c4c4c4\"}]},{\"featureType\":\"administrative.neighborhood\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"landscape\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":20}]},{\"featureType\":\"poi\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":21},{\"visibility\":\"on\"}]},{\"featureType\":\"poi.business\",\"elementType\":\"geometry\",\"stylers\":[{\"visibility\":\"on\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#e5c163\"},{\"lightness\":\"0\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":18}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#575757\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"color\":\"#2c2c2c\"}]},{\"featureType\":\"road.local\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":16}]},{\"featureType\":\"road.local\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#999999\"}]},{\"featureType\":\"transit\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":19}]},{\"featureType\":\"water\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":17}]},{\"featureType\":\"water\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"water\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"visibility\":\"off\"}]}]      "+
                "    };\n";
                
              
                
                String mapO =  " var mapElement = document.getElementById('map');"+"\n"+"\nvar map = new google.maps.Map(mapElement, mapOptions);";

                
                String part2="\n" +
//                "    var map = new google.maps.Map(document.getElementById('map'), {\n" +
//                "      zoom: 10,\n" +
//                "      center: new google.maps.LatLng(37.09024, -91.962333),\n" + "mapTypeControlOptions: {"+
//                "      mapTypeIds: ['roadmap', 'satellite', 'hybrid', 'terrain',\n" +
//"                    'styled_map']\n" + "}"+
//                "    });\n" +
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
                "\n" +"bounds.extend(marker.getPosition());" + "\n"+
                "      google.maps.event.addListener(marker, 'click', (function(marker, i) {\n" +
                "        return function() {\n" +
                //info windows set the data for the tweet
                "          infowindow.setContent(locations[i][0]);\n" +
                "          infowindow.open(map, marker);\n" +
                "        }\n" +
                "      })(marker, i));\n" +
                "    }\n" +
                     "map.fitBounds(bounds);"+"\n"+
                      "map.mapTypes.set('styled_map',styledMapType);" + "/n"
                       + "map.setMapTypeId('styled_map');"+"/n"+ 
                "  </script>\n" +
                   "<script>"
                        + "src=https://maps.googleapis.com/maps/api/js?key=AIzaSyDXjTelK25-Ypa5ykkmbi-tG6GHgbqKZ00"+     
                
                       "\n" +"</script>"+
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
        engine = webView.getEngine();
        //URL url = getClass().getResource("map.html");
        //String url = getClass().getResource("/TweetAnalysisApplication/map.html").toExternalForm();
        b.deleteCharAt(b.length()-1);
        b.append("]");
        //System.out.println(b.toString());
        String link = part1+mapOptions+mapO+b+part2;
        engine.loadContent(link);
    }

    
    
    @FXML 
    private void handleDeleteButton() {
        
        System.out.println("Delete button is being accessed. \n");
        ObservableList<TableObject> removeList = FXCollections.observableArrayList();
        TableObject temp;
        
        for(int i=0; i< table.getItems().size(); i++){
            if(table.itemsProperty().get().get(i).isSelected()){
                temp=table.itemsProperty().get().get(i);
                removeList.add(temp);
            }
        }  
        table.getItems().removeAll(removeList);

        //update bar charts
        XYChart.Data<String, Number> dataPOS = new XYChart.Data("",toc.getPosCount());
        XYChart.Data<String, Number> dataNEG = new XYChart.Data("", toc.getNegCount());
        XYChart.Data<String, Number> dataNEU = new XYChart.Data("", toc.getNeuCount());
        series1.getData().setAll(dataPOS);
	series2.getData().setAll(dataNEG);
	series3.getData().setAll(dataNEU);  
        barChart.getData().setAll(series1,series2,series3);
         
        // update piechart View
         pieChartData = FXCollections.observableArrayList(
            new PieChart.Data("Positive ", toc.getPosCount()),
            new PieChart.Data("Negative",toc.getNegCount()),
            new PieChart.Data("Neutral", toc.getNeuCount())
         );
        pieChart.setData(pieChartData);
        
        // Update map view 
        webView.getEngine().load(""); //reset view
        StringBuffer newbuffer; 
        Maps mapper = new Maps();
        tweetLocation = toc.getLocations();
        System.out.print("\nTweets: " + tweetCollection.getCollection() + "\nAFTERREMOVELOCATIONS: " + tweetCollection.getLocations().toString());
        newbuffer = mapper.getCoordinates(tweetLocation);
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
                "      ['New York', 30.984298, -91.96233, 2],\n" +
                "      ['California', 30.984298, -91.96233, 1],\n" +
               
                "    ];\n";
                String style= "styles: [{\"featureType\":\"all\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#3c3c3c\"}]},{\"featureType\":\"all\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"},{\"hue\":\"#0000ff\"}]},{\"featureType\":\"all\",\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"on\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"saturation\":36},{\"color\":\"#000000\"},{\"lightness\":40},{\"visibility\":\"off\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"visibility\":\"off\"},{\"color\":\"#000000\"},{\"lightness\":16}]},{\"featureType\":\"all\",\"elementType\":\"labels.icon\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"administrative\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":20}]},{\"featureType\":\"administrative\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":17},{\"weight\":1.2}]},{\"featureType\":\"administrative.country\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#ffffff\"},{\"weight\":\"0.62\"},{\"gamma\":\"3.89\"}]},{\"featureType\":\"administrative.country\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#ffffff\"}]},{\"featureType\":\"administrative.country\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"administrative.locality\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#c4c4c4\"}]},{\"featureType\":\"administrative.neighborhood\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"landscape\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":20}]},{\"featureType\":\"poi\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":21},{\"visibility\":\"on\"}]},{\"featureType\":\"poi.business\",\"elementType\":\"geometry\",\"stylers\":[{\"visibility\":\"on\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#e5c163\"},{\"lightness\":\"0\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":18}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#575757\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"color\":\"#2c2c2c\"}]},{\"featureType\":\"road.local\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":16}]},{\"featureType\":\"road.local\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#999999\"}]},{\"featureType\":\"transit\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":19}]},{\"featureType\":\"water\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":17}]},{\"featureType\":\"water\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"water\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"visibility\":\"off\"}]}]\n" +
"                };";
                String mapOptions = "var mapOptions=  {zoom: 10,\n" +
                "      center: new google.maps.LatLng(37.09024, -91.962333),\n" + 
                "styles: [{\"featureType\":\"all\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#3c3c3c\"}]},{\"featureType\":\"all\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"},{\"hue\":\"#0000ff\"}]},{\"featureType\":\"all\",\"elementType\":\"labels\",\"stylers\":[{\"visibility\":\"on\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"saturation\":36},{\"color\":\"#000000\"},{\"lightness\":40},{\"visibility\":\"off\"}]},{\"featureType\":\"all\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"visibility\":\"off\"},{\"color\":\"#000000\"},{\"lightness\":16}]},{\"featureType\":\"all\",\"elementType\":\"labels.icon\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"administrative\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":20}]},{\"featureType\":\"administrative\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":17},{\"weight\":1.2}]},{\"featureType\":\"administrative.country\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#ffffff\"},{\"weight\":\"0.62\"},{\"gamma\":\"3.89\"}]},{\"featureType\":\"administrative.country\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"on\"},{\"color\":\"#ffffff\"}]},{\"featureType\":\"administrative.country\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"administrative.locality\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#c4c4c4\"}]},{\"featureType\":\"administrative.neighborhood\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"landscape\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":20}]},{\"featureType\":\"poi\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":21},{\"visibility\":\"on\"}]},{\"featureType\":\"poi.business\",\"elementType\":\"geometry\",\"stylers\":[{\"visibility\":\"on\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#e5c163\"},{\"lightness\":\"0\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"geometry.stroke\",\"stylers\":[{\"visibility\":\"off\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"road.highway\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"color\":\"#e5c163\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":18}]},{\"featureType\":\"road.arterial\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#575757\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"road.arterial\",\"elementType\":\"labels.text.stroke\",\"stylers\":[{\"color\":\"#2c2c2c\"}]},{\"featureType\":\"road.local\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":16}]},{\"featureType\":\"road.local\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"color\":\"#999999\"}]},{\"featureType\":\"transit\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":19}]},{\"featureType\":\"water\",\"elementType\":\"geometry\",\"stylers\":[{\"color\":\"#000000\"},{\"lightness\":17}]},{\"featureType\":\"water\",\"elementType\":\"geometry.fill\",\"stylers\":[{\"color\":\"#ffffff\"}]},{\"featureType\":\"water\",\"elementType\":\"labels.text.fill\",\"stylers\":[{\"visibility\":\"off\"}]}]      "+
                "    };\n";
                
              
                
                String mapO =  " var mapElement = document.getElementById('map');"+"\n"+"\nvar map = new google.maps.Map(mapElement, mapOptions);";

                
                String part2="\n" +
//                "    var map = new google.maps.Map(document.getElementById('map'), {\n" +
//                "      zoom: 10,\n" +
//                "      center: new google.maps.LatLng(37.09024, -91.962333),\n" + "mapTypeControlOptions: {"+
//                "      mapTypeIds: ['roadmap', 'satellite', 'hybrid', 'terrain',\n" +
//"                    'styled_map']\n" + "}"+
//                "    });\n" +
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
                "\n" +"bounds.extend(marker.getPosition());" + "\n"+
                "      google.maps.event.addListener(marker, 'click', (function(marker, i) {\n" +
                "        return function() {\n" +
                //info windows set the data for the tweet
                "          infowindow.setContent(locations[i][0]);\n" +
                "          infowindow.open(map, marker);\n" +
                "        }\n" +
                "      })(marker, i));\n" +
                "    }\n" +
                     "map.fitBounds(bounds);"+"\n"+
                      "map.mapTypes.set('styled_map',styledMapType);" + "/n"
                       + "map.setMapTypeId('styled_map');"+"/n"+ 
                "  </script>\n" +
                   "<script>"
                        + "src=https://maps.googleapis.com/maps/api/js?key=AIzaSyDXjTelK25-Ypa5ykkmbi-tG6GHgbqKZ00"+     
                
                       "\n" +"</script>"+
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
        engine = webView.getEngine();
        //URL url = getClass().getResource("map.html");
        //String url = getClass().getResource("/TweetAnalysisApplication/map.html").toExternalForm();
        newbuffer.deleteCharAt(newbuffer.length()-1);
        newbuffer.append("]");
        //System.out.println(b.toString());
        String link = part1+mapOptions+mapO+newbuffer+part2;
        engine.loadContent(link);
        
    }
         
}

