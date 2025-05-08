package com.mycompany.bibsys;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.bibsys.database.BookDAO;
import com.mycompany.bibsys.entity.Book;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class DeleteItemController {

    @FXML
    private Label authorLabel;

    @FXML
    private GridPane bookDetails;

    @FXML
    private Button confirmDeleteItemButton;

    @FXML
    private Button deleteCopyOfItemButton;

    @FXML
    private Button deleteItemButton;

    @FXML
    private Label directorTitleLabel;

    @FXML
    private GridPane dvdDetails;

    @FXML
    private Label dvdNrLabel;

    @FXML
    private TextField dvdNrTextField;

    @FXML
    private Label dvdTitleLabel;

    @FXML
    private GridPane enterDvdNr;

    @FXML
    private GridPane enterIsbn;

    @FXML
    private Label isbnLabel;

    @FXML
    private TextField isbnTextField;

    @FXML
    private ChoiceBox<String> itemTypeChoiceBox;
    
    @FXML
    private Button okButton;

    @FXML
    private Label publisherLabel;

    @FXML
    private Label publishingYearLabel;

    @FXML
    private Label releaseYearLabel;

    @FXML
    private Label titleLabel;
    @FXML
    private GridPane typeOfItemGrid;

    @FXML
    void confirmDeleteItemPressed(ActionEvent event) {

    }

    @FXML
    void deleteCopyOfItemButtonPressed(ActionEvent event) {

    }

    @FXML
    void deleteItemButtonPressed(ActionEvent event) {
        typeOfItemGrid.setVisible(true);
        typeOfItemGrid.setManaged(true);
        deleteCopyOfItemButton.setDisable(true);
        
        itemTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if ("Book".equals(newVal)){
                enterIsbn.setVisible(true);
                enterIsbn.setManaged(true);
                okButton.setVisible(true);
                okButton.setManaged(true);
                enterDvdNr.setVisible(false);
                enterDvdNr.setManaged(false);
                
            } else if ("DVD".equals(newVal)){
                enterDvdNr.setVisible(true);
                enterDvdNr.setManaged(true);
                enterIsbn.setVisible(false);
                enterIsbn.setManaged(false);
            }
        });
    }
    
    @FXML
    void okButtonPressed(ActionEvent event) {
        String input = ""; 
        String selectedItemType = itemTypeChoiceBox.getSelectionModel().getSelectedItem();

        if ("Book".equals(selectedItemType)){
            findBook();
        }
        else if ("DVD".equals(selectedItemType)){
            findDvd();
        }

    }
     public void initialize(){
        
        itemTypeChoiceBox.getItems().addAll("Book", "DVD");

        typeOfItemGrid.setVisible(false);
        typeOfItemGrid.setManaged(false);
        enterDvdNr.setVisible(false);
        enterDvdNr.setManaged(false);
        enterIsbn.setVisible(false);
        enterIsbn.setManaged(false);
        okButton.setVisible(false);
        okButton.setManaged(false);
        bookDetails.setVisible(false);
        bookDetails.setManaged(false);
        dvdDetails.setVisible(false);
        dvdDetails.setManaged(false);
        confirmDeleteItemButton.setVisible(false);
        confirmDeleteItemButton.setManaged(false);
    }

    private void findBook() {
        String isbn = isbnTextField.getText().trim();
        if (isbn.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Missing ISBN", "Please enter an ISBN number.");
            return;
        }
        
        List<Book> books = BookDAO.searchBooks(isbn);
        if (books.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Not Found", "The isbn you provided does not match any books in the database.");
            return; 
        }
        
        Book book = books.get(0);
        titleLabel.setText(book.getTitle());
        authorLabel.setText(book.getAuthor());
        publisherLabel.setText(book.getPublisher());
        publishingYearLabel.setText(String.valueOf(book.getYear()));
        isbnLabel.setText(String.valueOf(book.getIsbn()));
        
        bookDetails.setVisible(true);
        bookDetails.setManaged(true);
        confirmDeleteItemButton.setVisible(true);
        confirmDeleteItemButton.setManaged(true);
        dvdDetails.setVisible(false);
        dvdDetails.setManaged(false);
    }

    private void findDvd() {
        //inte implementerad än: gör samma som för books
    }
    
    private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type); 
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
