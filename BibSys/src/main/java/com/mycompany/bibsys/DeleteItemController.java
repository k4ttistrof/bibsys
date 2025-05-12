package com.mycompany.bibsys;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.bibsys.database.BookDAO;
import com.mycompany.bibsys.database.DVDDAO;
import com.mycompany.bibsys.entity.Book;
import com.mycompany.bibsys.entity.DVD;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private Label directorLabel;

    @FXML
    private GridPane dvdDetails;

    @FXML
    private Label dvdNoLabel;

    @FXML
    private TextField dvdNoTextField;

    @FXML
    private Label dvdTitleLabel;

    @FXML
    private GridPane enterDvdNo;

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
        String selectedItemType = itemTypeChoiceBox.getSelectionModel().getSelectedItem();
        
        if ("Book".equals(selectedItemType)){
            long isbn = Long.parseLong(isbnLabel.getText());
            String title = titleLabel.getText();
            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm deletion");
            confirm.setHeaderText("You are about to delete a book from the database");
            confirm.setContentText("This action will delete the book titled '" + title + "' and all its copies. Are you sure?");
            
            Optional<ButtonType> result = confirm.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                boolean success = BookDAO.deleteBookAndCopies(isbn);
                if (success){
                    showAlert(Alert.AlertType.INFORMATION, "Success.", "Book " + title + " and its copies have been deleted.");
                    resetView();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Could not delete book.");
                }
            }
        }
        else if ("DVD".equals(selectedItemType)){
            int dvdNo = Integer.parseInt(dvdNoLabel.getText());
            String title = dvdTitleLabel.getText();
            
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Confirm deletion");
            confirm.setHeaderText("You are about to delete a DVD from the database");
            confirm.setContentText("This action will delete the DVD titled '" + title + "' and all its copies. Are you sure?" );
            
            Optional<ButtonType> result = confirm.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                boolean success = DVDDAO.deleteDvdAndCopies(dvdNo);
                if (success){
                    showAlert(Alert.AlertType.INFORMATION, "Success.", "DVD " + title + " and all its copies have been deleted.");
                    resetView(); 
                }
                else{
                    showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Could not delete DVD."); 
                }
            }
        }
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
                enterDvdNo.setVisible(false);
                enterDvdNo.setManaged(false);
                
            } else if ("DVD".equals(newVal)){
                enterDvdNo.setVisible(true);
                enterDvdNo.setManaged(true);
                okButton.setVisible(true);
                okButton.setManaged(true);
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
        enterDvdNo.setVisible(false);
        enterDvdNo.setManaged(false);
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
        String dvdNoStr = dvdNoTextField.getText().trim();
        if (dvdNoStr.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Missing DVD number", "Please enter a valid DVD number.");
            return;
        }
        
        int dvdNo; 
        try{
            dvdNo = Integer.parseInt(dvdNoStr);
        } catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Invalid input.", "DVD number must be a number.");
            return;
        }
        
        List<DVD> dvds = DVDDAO.searchDvdNo(dvdNo);
        if (dvds.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Not Found", "The DVD number you provided does not match any dvds in the database.");
            return; 
        }
        
        DVD dvd = dvds.get(0);
        dvdTitleLabel.setText(dvd.getTitle());
        directorLabel.setText(dvd.getDirector());
        releaseYearLabel.setText(String.valueOf(dvd.getYear()));
        dvdNoLabel.setText(String.valueOf(dvd.getDvdNo()));

        dvdDetails.setVisible(true);
        dvdDetails.setManaged(true);
        confirmDeleteItemButton.setVisible(true);
        confirmDeleteItemButton.setManaged(true);
        bookDetails.setVisible(false);
        bookDetails.setManaged(false);
    }
    
    private void showAlert(Alert.AlertType type, String title, String message){
        Alert alert = new Alert(type); 
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void resetView() {
        itemTypeChoiceBox.getSelectionModel().clearSelection();
        dvdNoTextField.clear();
        isbnTextField.clear();
        
        enterDvdNo.setVisible(false);
        enterDvdNo.setManaged(false);
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

}
