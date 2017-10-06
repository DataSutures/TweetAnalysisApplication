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

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

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

public class FXMLController implements Initializable {   
    final static String positive ="Positive";
    final static String negative ="Negative";
    final static String neutral = "Neutral";
    
    
    private final ObservableList<TableObject> tweets = FXCollections.observableArrayList();
    private final ObservableList<Status> cols = FXCollections.observableArrayList();
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

        
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        filterBox.getItems().addAll(
                "All",
                "Positive", 
                "Negative", 
                "Neutral"
        );
        // I added this to make it work --yeah ur suppose to add thecell manually there was a website that had
        //fx to scenebuilder that showed how to mplement this that i found last night 
        screenName.setCellValueFactory(new PropertyValueFactory<TableObject, String>("screenName"));
        tweetText.setCellValueFactory(new PropertyValueFactory<TableObject, String>("tweetText"));
        createdOn.setCellValueFactory(new PropertyValueFactory<TableObject, String>("createdOn"));
        sentiment.setCellValueFactory(new PropertyValueFactory<TableObject, String>("sentiment"));
    }  

    @FXML
    private void handleActionButton(ActionEvent event) throws TextAPIException {
        String toSearch = searchField.getText();
        List<Status> tweetResult = TwitterQuery.getTweets(toSearch);
        String sn,text,date,sent,sen;
        AylienAnalysis alienResults = new AylienAnalysis();
        for (Iterator<Status> it = tweetResult.iterator(); it.hasNext();) {
            Status s = it.next();
            sn = s.getUser().getScreenName();
            text = s.getText();
            date = s.getCreatedAt().toString(); 
            // Add Sentiment Analysis Here 
            sen = alienResults.analyzeTweet(text).getPolarity().toString();
            sent = StringUtils.capitalize(sen); 
            
            
                TableObject to = new TableObject(sn,text,date,sent);
                System.out.print(to.toString());
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
        set1.getData().add(new XYChart.Data<>(positive,200));
        set1.getData().add(new XYChart.Data<>(negative, 300));
        set1.getData().add(new XYChart.Data<>(neutral,100));
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

}