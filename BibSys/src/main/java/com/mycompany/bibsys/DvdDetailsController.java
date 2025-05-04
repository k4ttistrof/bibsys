/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys;

/**
 *
 * @author katay
 */

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class DvdDetailsController {

    @FXML
    private Button backButton;

    @FXML
    private Button borrowItemButton;

    @FXML
    private Label directorLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private Label isAvailableLabel;

    @FXML
    private Label placementLabel;

    @FXML
    private Label releaseYearLabel;

    @FXML
    private Label titleLabel;

    @FXML
    void handleBack(ActionEvent event) {
        backButton.getScene().setRoot(mainRoot);
    }
    
    private SearchResultItem item; 
    private Parent mainRoot; 
    private SearchItemController mainController; 
    
    public void setDvdDetails(SearchResultItem item){
        this.item = item; 
        
        titleLabel.setText(item.getTitle());
        directorLabel.setText(item.getAuthorOrDirector());
        releaseYearLabel.setText(String.valueOf(item.getYear()));
        genreLabel.setText(item.getGenre());
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
