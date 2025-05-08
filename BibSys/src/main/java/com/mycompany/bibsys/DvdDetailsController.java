/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys;

/**
 *
 * @author katay
 */

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

public class DvdDetailsController {

    @FXML
    private Button backButton;

    @FXML
    private Button borrowItemButton;

    @FXML
    private Label directorLabel;
    
    @FXML
    private Label dvdNrLabel;

    @FXML
    private Label genreLabel;

    @FXML
    private Label isAvailableLabel;
    
    @FXML
    private MenuItem menuItemLogIn;

    @FXML
    private Label placementLabel;

    @FXML
    private Label releaseYearLabel;

    @FXML
    private Label titleLabel;

    @FXML
    void handleBack(){
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
    
    public void setDvdDetails(SearchResultItem item){
        this.item = item; 
        titleLabel.setText(item.getTitle());
        directorLabel.setText(item.getAuthorOrDirector());
        releaseYearLabel.setText(String.valueOf(item.getYear()));
        genreLabel.setText(item.getGenre());
        placementLabel.setText(item.getPlacement());
        dvdNrLabel.setText(String.valueOf(item.getdvdNr()));
        isAvailableLabel.setText(item.getAvailability());
        
        borrowItemButton.setDisable(!"Available".equalsIgnoreCase(item.getAvailability()));
    }
    
    public void setMainRoot(Parent mainRoot){
        this.mainRoot = mainRoot; 
        updateMenuForUser();
    }
    
    public void setMainController(SearchItemController controller){
        this.mainController = controller;
    }

    private void updateMenuForUser() {
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
