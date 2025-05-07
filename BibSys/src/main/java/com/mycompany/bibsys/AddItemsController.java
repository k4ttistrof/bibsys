/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AddItemsController {

    @FXML
    private Button addBookButton;

    @FXML
    private Button addDvdButton;

    @FXML
    private TextField authorTextField;

    @FXML
    private Label bcInfoIcon;

    @FXML
    private TextField bnrOfCopiesTextField;

    @FXML
    private ChoiceBox<Integer> categoryChoiceBox;

    @FXML
    private GridPane bookForm;

    @FXML
    private TextField bookPlacementTextField;

    @FXML
    private TextField bookTitleTextField;

    @FXML
    private TextField directorTextField;

    @FXML
    private TextField dnrOfCopiesTextField;

    @FXML
    private TextField dplacementTextField;

    @FXML
    private GridPane dvdForm;

    @FXML
    private TextField dvdTitleTextField;

    @FXML
    private ChoiceBox<String> genreChoiceBox;

    @FXML
    private Label ircInfoIcon;

    @FXML
    private CheckBox isReferenceCopyCheckBox;

    @FXML
    private TextField isbnTextField;

    @FXML
    private TextField publisherTextField;

    @FXML
    private TextField publishingYearTextField;

    @FXML
    private TextField releaseYearTextField;

    @FXML
    private ChoiceBox<String> searchTypeChoiceBox;

    @FXML
    void addBookButtonPressed(ActionEvent event) {
        addBookToDatabase();

    }

    @FXML
    void addDvdButtonPressed(ActionEvent event){
            addDvdToDatabase();
    }
    
    public void initialize(){
        
        searchTypeChoiceBox.getItems().addAll("Book", "DVD");
        categoryChoiceBox.getItems().addAll(1, 2);
        genreChoiceBox.getItems().addAll("documentary", "romance", "comedy", "drama", "thriller", "horror");

        bookForm.setVisible(false);
        bookForm.setManaged(false);
        dvdForm.setVisible(false);
        dvdForm.setManaged(false);
        
        searchTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Book".equals(newVal)){
                bookForm.setVisible(true);
                bookForm.setManaged(true);
                dvdForm.setVisible(false);
                dvdForm.setManaged(false);
            } else if ("DVD".equals(newVal)){
                dvdForm.setVisible(true);
                dvdForm.setManaged(true);
                bookForm.setVisible(false);
                bookForm.setManaged(false);
            }
        });
    }
    
    private void addBookToDatabase(){
        String title = bookTitleTextField.getText();
        String author = authorTextField.getText();
        String publisher = publisherTextField.getText();
        String year = publishingYearTextField.getText();
        String isbn = isbnTextField.getText();
        Integer category = categoryChoiceBox.getValue();
        String placement = bookPlacementTextField.getText();
        String copiesStr = bnrOfCopiesTextField.getText();
        boolean isReference = isReferenceCopyCheckBox.isSelected();
        
        if (title.isEmpty() || author.isEmpty() || publisher.isEmpty() || year.isEmpty() ||
        isbn.isEmpty() || category == null || placement.isEmpty() || copiesStr.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, "Input Incorrect!", "All fields must be filled.");
        return;
        }
        
        long isbnLong; 
        int yearInt; 
        int nrOfCopies;
        
        try{
            isbnLong = Long.parseLong(isbn);
            yearInt = Integer.parseInt(year);
            nrOfCopies = Integer.parseInt(copiesStr);
        } catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Input Incorrect!", "Year, ISBN and number of copies must be numbers!");
            return;
        }
        
        try(Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbd", "root", "#Katot99")){
            conn.setAutoCommit(false);
            
            String insertBookSQL = "INSERT INTO book (ISBN, title, author, publisher, publishingYear, bookCategory, placement) VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertBookSQL)) {
                ps.setLong(1, isbnLong);
                ps.setString(2, title);
                ps.setString(3, author);
                ps.setString(4, publisher);
                ps.setInt(5, yearInt);
                ps.setInt(6, category);
                ps.setString(7, placement);
                ps.executeUpdate();
            }
            
            String insertCopySQL = "INSERT INTO bookcopy (isbn, isReferenceCopy, onLoan, title, bookCategory) VALUES (?, ?, 0, ?, ?)";
            try (PreparedStatement psCopy = conn.prepareStatement(insertCopySQL)){
                for (int i = 0; i<nrOfCopies; i++){
                    boolean isRefCopy = isReference && i == 0; 
                    psCopy.setLong(1, isbnLong);
                    psCopy.setInt(2, isRefCopy ? 1 : 0);
                    psCopy.setString(3, title);
                    psCopy.setInt(4, category);
                    psCopy.addBatch();
                }
                psCopy.executeBatch();
            }
            catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Could not add books to database.");
                return;
            }
            conn.commit();
            showAlert(Alert.AlertType.INFORMATION, "Success!", "Book added with " + nrOfCopies + " copies.");
            clearBookForm();
        }
        catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Could not connect to the database.");
        }

    }
    
    private void addDvdToDatabase(){
        String title = dvdTitleTextField.getText();
        String director = directorTextField.getText();
        String releaseYear = releaseYearTextField.getText();
        String genre = (String) genreChoiceBox.getValue();
        String placement = dplacementTextField.getText();
        String copies = dnrOfCopiesTextField.getText();
        
        if (title.isEmpty() || director.isEmpty() || releaseYear.isEmpty() || genre == null || placement.isEmpty() || copies.isEmpty()) {
        showAlert(Alert.AlertType.ERROR, "Input Error", "All fields must be filled.");
        return;
        }
        
        int releaseYearInt; 
        int nrOfCopies;
        
        try{
            releaseYearInt = Integer.parseInt(releaseYear);
            nrOfCopies = Integer.parseInt(copies);
        }catch(NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Input Error", "Release Year and number of copies must be numbers!");
            return;}
        
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bbd", "root", "#Katot99")){
            conn.setAutoCommit(false);
            
            String insertDvdSQL = "INSERT INTO dvd (title, director, releaseYear, genre, loanTime, placement) VALUES (?, ?, ?, ?, ?, ?)"; 
            try (PreparedStatement ps = conn.prepareStatement(insertDvdSQL, Statement.RETURN_GENERATED_KEYS)){
                ps.setString(1, title);
                ps.setString(2, director);
                ps.setInt(3, releaseYearInt);
                ps.setString(4, genre);
                ps.setInt(5, 14);
                ps.setString(6, placement);
                ps.executeUpdate();
                
                ResultSet rs = ps.getGeneratedKeys(); 
                if (rs.next()){
                    int dvdNo = rs.getInt(1);
                    rs.close();
                    
                    String insertCopySQL = "INSERT INTO dvdcopy (dvdNo, title, onLoan) VALUES (?, ?, 0)";
                    try (PreparedStatement psCopy = conn.prepareStatement(insertCopySQL)){
                        for (int i = 0; i < nrOfCopies; i++){
                            psCopy.setInt(1, dvdNo); 
                            psCopy.setString(2, title);
                            psCopy.addBatch();
                        }
                        psCopy.executeBatch();
                    }
                    conn.commit();
                    showAlert(Alert.AlertType.INFORMATION, "Success!", "DVD added with " + nrOfCopies + " copies.");
                    clearDvdForm();
                }
                else {
                    conn.rollback();
                    showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Failed to Retrieve Dvd Id.");
                }
            }
            catch(Exception e){
                conn.rollback();
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Could not add DVD to Database.");
            }
        }
        catch (SQLException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Could not connect to database.");
        }
    }
    private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type); 
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearDvdForm() {
        dvdTitleTextField.clear();
        directorTextField.clear();
        releaseYearTextField.clear();
        genreChoiceBox.setValue(null);
        dplacementTextField.clear();
        dnrOfCopiesTextField.clear();
    }

    private void clearBookForm() {
        bookTitleTextField.clear();
        authorTextField.clear();
        publisherTextField.clear();
        publishingYearTextField.clear();
        isbnTextField.clear();
        categoryChoiceBox.setValue(null);
        bookPlacementTextField.clear();
        isReferenceCopyCheckBox.setSelected(false);
        bnrOfCopiesTextField.clear();
    }
}
