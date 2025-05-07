/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys;

/**
 *
 * @author katay
 */

import com.mycompany.bibsys.entity.User;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class UserLoggedInPageController {

    @FXML
    private Label activeLoansLabel;

    @FXML
    private Button addItemButton;

    @FXML
    private Button deleteItemButton;

    @FXML
    private Label eMailLabel;

    @FXML
    private Label overDueLoansLabel;

    @FXML
    private Button searchForItemButton;

    @FXML
    private Label userCategoryLabel;

    @FXML
    private Label welcomeLabel;
    
    @FXML
    private Button logOutButton;
    
    private User currentUser;
    
    @FXML
    void logOutButtonPressed(ActionEvent event) {
        Session.clear();
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Log out Succesful");
        alert.setHeaderText(null);
        alert.setContentText("You have now been logged out!");
        alert.showAndWait();
        
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchItem.fxml"));
            Parent searchRoot = loader.load();
            
            SearchItemController controller = loader.getController();
            controller.setMainRoot(searchRoot);
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(searchRoot));
            stage.setTitle("Library System");
            stage.show();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    void addItemButtonPressed(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddItem.fxml"));
        Parent addRoot = loader.load();

        AddItemsController controller = loader.getController();

        Stage addItemStage = new Stage();
        addItemStage.setScene(new Scene(addRoot));
        addItemStage.setTitle("Add Items");
        addItemStage.initModality(Modality.APPLICATION_MODAL); // Gör popupen modal (stänger inte bakomliggande fönster)
        addItemStage.show();

        } 
        catch (Exception e) {
        e.printStackTrace();
        }

    }

    @FXML
    void deleteItemButtonPressed(ActionEvent event) {

    }


    @FXML
    void searchForItemButtonPressed(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchItem.fxml"));
        Parent searchRoot = loader.load();

        SearchItemController searchController = loader.getController();
        searchController.setMainRoot(searchRoot);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(searchRoot));
        stage.setTitle("Library System");
        stage.show();
        } 
        catch (Exception e) {
        e.printStackTrace();
        }
    }
    
    public void setUser(User user){
        this.currentUser = user; 
        welcomeLabel.setText("Welcome, " + user.getFirstName() + " "+ user.getLastName()+ "!");
        eMailLabel.setText(user.getEmail());
        activeLoansLabel.setText(String.valueOf(user.getActiveLoans()));
        userCategoryLabel.setText(String.valueOf(user.getUserCategory()) + user.getCategoryDesc());
        overDueLoansLabel.setText("EJ IMPLEMENTERAD ÄN!! LÖS DETTA SENARE!");
        
        if("staff".equalsIgnoreCase(user.getRole())){
            addItemButton.setVisible(true);
            addItemButton.setManaged(true);
            deleteItemButton.setVisible(true);
            deleteItemButton.setManaged(true);
        }
        else {
            addItemButton.setVisible(false);
            addItemButton.setManaged(false);
            deleteItemButton.setVisible(false);
            deleteItemButton.setManaged(false);
        }
    }

   

}
