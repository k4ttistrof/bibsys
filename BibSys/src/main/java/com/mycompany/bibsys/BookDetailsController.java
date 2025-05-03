/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author katay
 */

public class BookDetailsController {

    @FXML
    private Label authorLabel;
    
    @FXML
    private Button borrowItemButton;

    @FXML
    private Label isAvailableLabel;

    @FXML
    private Label isbnLabel;

    @FXML
    private Label placementLabel;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label publishingYearLabel;

    @FXML
    private Label titleLabel;
    
    @FXML
    private Button backButton;
    
    @FXML
    private void handleBack(){
        backButton.getScene().setRoot(mainRoot);
    }

    private SearchResultItem item; 
    private Parent mainRoot; 
    private SearchItemController mainController; 
    
    public void setBookDetails(SearchResultItem item){
        this.item = item; 
        
        titleLabel.setText(item.getTitle());
        authorLabel.setText(item.getAuthorOrDirector());
        publisherLabel.setText(item.getPublisher());
        publishingYearLabel.setText(String.valueOf(item.getYear()));
        isbnLabel.setText(String.valueOf(item.getIsbn()));
        placementLabel.setText(item.getPlacement());
        isAvailableLabel.setText(item.getAvailability());
        
        borrowItemButton.setDisable(!"Available".equalsIgnoreCase(item.getAvailability()));
    }
    
    public void setMainRoot(Parent mainRoot){
        this.mainRoot = mainRoot; 
    }
    
    public void setMainController(SearchItemController controller){
        this.mainController = controller;
    }

}