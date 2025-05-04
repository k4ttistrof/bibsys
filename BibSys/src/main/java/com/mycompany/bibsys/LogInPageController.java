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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogInPageController {

    @FXML
    private TextField eMailTextField;

    @FXML
    private Button logInButton;

    @FXML
    private MenuItem menuSearchForItem;
    
    @FXML
    private TextField passwordTextField;

    @FXML
    void logInButtonPressed(ActionEvent event) {
    }
    
    @FXML
    void menuSearchForItemPressed(ActionEvent event) {
        try{
            if (mainRoot != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("SearchItem.fxml"));
                Parent searchRoot = loader.load();
                
                SearchItemController searchController = loader.getController();
                searchController.setMainRoot(searchRoot);
                
                Stage stage = (Stage) ((MenuItem) event.getSource()).getParentPopup().getOwnerWindow();
                stage.setScene(new Scene(searchRoot));
                stage.setTitle("Bibliotekssystem");
                stage.show();
            }
        } 
        catch (Exception e){
            e.printStackTrace();
        }
    }
    
    private Parent mainRoot; 
    
    void setMainRoot(Parent root) {
        this.mainRoot = root;
    }

}
