/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private MenuItem menuItemLogIn;

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
    
    @FXML
    void menuItemLogInPressed(ActionEvent event) {
        try {
            if(Session.getCurrentUser() != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserLoggedInPage.fxml"));
                Parent userLoggedInRoot = loader.load();

                UserLoggedInPageController controller = loader.getController();
                controller.setUser(Session.getCurrentUser());
                
                Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
                stage.setScene(new Scene(userLoggedInRoot));
                stage.setTitle("Personal User Page");
                stage.show();
            }
            else {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInPage.fxml"));
            Parent loginRoot = loader.load();
            
            LogInPageController loginController = loader.getController();
            loginController.setMainRoot(mainRoot);
            
            Stage stage = (Stage)((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
            stage.setScene(new Scene(loginRoot));
            stage.setTitle("Log In");
            stage.show();
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }
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
    
    public void setMainRoot(Parent root){
        this.mainRoot = root; 
        updateMenuForUser();
    }
    
    public void setMainController(SearchItemController controller){
        this.mainController = controller;
    }
    
    private void updateMenuForUser(){
        if (Session.getCurrentUser() != null){
            menuItemLogIn.setText("My Page");
            menuItemLogIn.setOnAction(event -> menuItemLogInPressed(event));
        }
        else{
            menuItemLogIn.setText("Log In");
            menuItemLogIn.setOnAction(event -> menuItemLogInPressed(event));
        }
    }

}