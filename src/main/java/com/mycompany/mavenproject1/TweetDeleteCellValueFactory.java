/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.mavenproject1;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
/**
 * Keeps track of when check boxes are selected and deselected.
 */
public class TweetDeleteCellValueFactory implements Callback<TableColumn.CellDataFeatures<Tweet, CheckBox>, ObservableValue<CheckBox>> {
     @Override
    public ObservableValue<CheckBox> call(TableColumn.CellDataFeatures<Tweet, CheckBox> param) {
        //System.out.print("MADE IT");
        Tweet to = param.getValue();
        CheckBox checkBox = new CheckBox();
        checkBox.selectedProperty().setValue(to.isSelected());
        checkBox.selectedProperty().addListener((ov, old_val, new_val) -> {
            //System.out.println("Changed Boolean");
            //System.out.println(old_val);
            //System.out.println(new_val);
            to.setSelected(new_val);
        });
        return new SimpleObjectProperty<>(checkBox);
    }
}