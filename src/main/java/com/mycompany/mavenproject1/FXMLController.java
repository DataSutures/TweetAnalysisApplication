package com.mycompany.mavenproject1;

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
import javafx.scene.chart.BarChart;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;

public class FXMLController implements Initializable {   
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
    private Tab BarChartTab;
    @FXML
    private BarChart<?, ?> barGraph;

        
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
    private void handleActionButton(ActionEvent event) {
        String toSearch = searchField.getText();
        List<Status> tweetResult = TwitterQuery.getTweets(toSearch);
        String sn,text,date,sent;
        for (Iterator<Status> it = tweetResult.iterator(); it.hasNext();) {
            Status s = it.next();
            sn = s.getUser().getScreenName();
            text = s.getText();
            date = s.getCreatedAt().toString(); 
            // Add Sentiment Analysis Here 
            sent = "Coming Soon"; 
            TableObject to = new TableObject(sn,text,date,sent);
            System.out.print(to.toString());
            tweets.add(to);
        }
       
         table.setItems(tweets);
    }
}