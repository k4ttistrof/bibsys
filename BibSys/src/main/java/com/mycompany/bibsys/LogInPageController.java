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
import com.mysql.cj.protocol.Resultset;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    private TextField passwordField;
    
    private Parent mainRoot; 
    void setMainRoot(Parent root) {
        this.mainRoot = root;
    }

    @FXML
    void logInButtonPressed(ActionEvent event) throws SQLException {
        String username = eMailTextField.getText().trim();
        String password = passwordField.getText().trim(); 
        
        User user = validateLogin(username, password); //returnerar en ny user med uppgifterna fr. db
        if (user != null){
            Session.setCurrentUser(user);
            try{
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserLoggedInPage.fxml"));
                Parent userLoginRoot = loader.load();
                UserLoggedInPageController controller = loader.getController(); 
                controller.setUser(user);
                
                Stage stage = (Stage) logInButton.getScene().getWindow();
                stage.setScene(new Scene(userLoginRoot));
                stage.setTitle("User Login Page");
                stage.show();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
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
    
    

    private User validateLogin(String username, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbd", "root", "#Katot99")){
            String sql = "SELECT * FROM user WHERE email = ?"; 
            PreparedStatement stmt = connection.prepareStatement(sql); 
            stmt.setString(1, username);
            
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                String dbPassword = rs.getString("password");
                if (dbPassword.equals(password)){
                    return new User(
                        rs.getInt("userID"),
                        rs.getInt("userCategory"),
                        rs.getString("email"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("streetName"),
                        rs.getString("city"),
                        rs.getString("postcode"),
                        rs.getInt("activeLoans"),
                        rs.getString("role"),
                        rs.getString("password")
                    );
                }
                else{
                    showAlert("Wrong password", "Password i wrong! Please try again.");
                }
            }
            else {
                showAlert("Wrong username", "Username is wrong. Your username is your emailadress. Please try again.");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            showAlert("Connection Failure", "Could not connect to database.");
        }
        return null; 
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
