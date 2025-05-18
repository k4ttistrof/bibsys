package com.mycompany.bibsys;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import com.mycompany.bibsys.database.BookDAO;
import com.mycompany.bibsys.database.DVDDAO;
import com.mycompany.bibsys.entity.Book;
import com.mycompany.bibsys.entity.BookCopies;
import com.mycompany.bibsys.entity.DVD;
import com.mycompany.bibsys.entity.DVDCopies;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class DeleteItemController {
    @FXML
    private Button deleteBookCopyButton;
    @FXML
    private Label authorLabel;
    @FXML
    private GridPane bookCopyForm;
    @FXML
    private TextField bookCopyIdTextField;
    @FXML
    private GridPane bookDetails;
    @FXML
    private Button confirmDeleteItemButton;
    @FXML
    private Button deleteCopyOfItemButton;  
    @FXML
    private Button deleteDvdCopyButton;
    @FXML
    private Button deleteItemButton;   
    @FXML
    private TextField dvdCopyNoTextField;
    @FXML
    private Label directorLabel;
    @FXML
    private GridPane dvdCopyForm;
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
    
    private boolean isDeletingWholeItem = false;
    
    @FXML
    void deleteDvdCopyButtonPressed(ActionEvent event) {
        int copyId = Integer.parseInt(dvdCopyNoTextField.getText());
        DVDCopies copy = DVDDAO.getDVDCopy(copyId);

        if (copy == null) {
            showAlert(Alert.AlertType.ERROR, "Something went wrong!", "DVD copy not found.");
            return;
        }

        StringBuilder warning = new StringBuilder("You are about to delete this DVD copy:\n\n");
        warning.append("Title: ").append(copy.getDvd().getTitle()).append("\n");
        warning.append("Copy ID: ").append(copy.getBarcode()).append("\n");

        if (!copy.isAvailable()) {
            warning.append("\nThis copy is currently on loan!");
        }

        warning.append("\nAre you sure you want to delete this copy?");

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm deletion");
        confirm.setHeaderText("Warning");

        TextArea textArea = new TextArea(warning.toString());
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        confirm.getDialogPane().setContent(textArea);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            boolean success = DVDDAO.deleteDVDCopy(copyId); 
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Deleted", "DVD copy deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Something went wrong!", "DVD copy could not be deleted.");
            }
        }
    }

    @FXML
    void deleteBookCopyButtonPressed(ActionEvent event) {
        int copyId = Integer.parseInt(bookCopyIdTextField.getText());

        BookCopies copy = BookDAO.getBookCopy(copyId);
        if (copy == null){
            showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Book copy not found.");
            return;
        }

        StringBuilder warning = new StringBuilder("You are about to delete this book copy:\n\n");
        warning.append("Title: ").append(copy.getBookTitle()).append("\n");
        warning.append("Copy ID: ").append(copy.getBarcode()).append("\n");

        if (copy.isReference()){
            warning.append("\nWARNING: This is a reference copy!");
        }
        if (!copy.isAvailable()){
            warning.append("\nWARNING: This copy is currently on loan!");
        }

        warning.append("\nAre you sure you want to delete this copy?");

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm deletion");
        confirm.setHeaderText("Warning");

        // Skapa TextArea för att visa varningen
        TextArea textArea = new TextArea(warning.toString());
        textArea.setWrapText(true);       // Gör så att texten radbryts snyggt
        textArea.setEditable(false);      // Användaren kan inte ändra texten
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        // Sätt TextArea som innehåll i dialogen
        confirm.getDialogPane().setContent(textArea);

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK){
            boolean success = BookDAO.deleteBookCopy(copyId);
            if(success){
                showAlert(Alert.AlertType.INFORMATION, "Deleted", "Book copy deleted successfully.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Book copy could not be deleted.");
            }
        }
    }

    @FXML
    void confirmDeleteItemPressed(ActionEvent event) {
        String selectedItemType = itemTypeChoiceBox.getSelectionModel().getSelectedItem();
        
        if ("Book".equals(selectedItemType)){
            long isbn = Long.parseLong(isbnLabel.getText());
            String title = titleLabel.getText();
            
            if (BookDAO.hasLoanedCopies(isbn)) {
                showAlert(Alert.AlertType.WARNING, "Cannot delete Book", "The book '" + title + "' has one or more copies currently on loan and cannot be deleted.");
                return;
            }
            
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
            
            if (DVDDAO.hasLoanedCopies(dvdNo)) {
                showAlert(Alert.AlertType.WARNING, "Cannot delete DVD", "One or more copies of '" + title + "' are currently on loan and cannot be deleted.");
                return;
            }
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
        deleteItemButton.setDisable(true);
        typeOfItemGrid.setVisible(true);
        typeOfItemGrid.setManaged(true);
        
        /*itemTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
        if ("Book".equals(newVal)){
            bookCopyForm.setVisible(true);
                
        } else if ("DVD".equals(newVal)){
            dvdCopyForm.setVisible(true);  
            }
        }); */
    }

    @FXML
    void deleteItemButtonPressed(ActionEvent event) {   
        isDeletingWholeItem = true; 
        deleteCopyOfItemButton.setDisable(true);
        typeOfItemGrid.setVisible(true);
        typeOfItemGrid.setManaged(true);
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
        itemTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (isDeletingWholeItem) {//radera ett helt item + kopior
                if ("Book".equals(newVal)) {
                    enterIsbn.setVisible(true);
                    enterIsbn.setManaged(true);
                    enterDvdNo.setVisible(false);
                    enterDvdNo.setManaged(false);
                } else if ("DVD".equals(newVal)) {
                    enterDvdNo.setVisible(true);
                    enterDvdNo.setManaged(true);
                    enterIsbn.setVisible(false);
                    enterIsbn.setManaged(false);
                }
                okButton.setVisible(true);
                okButton.setManaged(true);
                bookCopyForm.setVisible(false);
                bookCopyForm.setManaged(false);
                dvdCopyForm.setVisible(false);
                dvdCopyForm.setManaged(false);
                } else { //radera kopia
                    if ("Book".equals(newVal)) {
                        bookCopyForm.setVisible(true);
                        bookCopyForm.setManaged(true);
                        dvdCopyForm.setVisible(false);
                        dvdCopyForm.setManaged(false);
                    } else if ("DVD".equals(newVal)) {
                        dvdCopyForm.setVisible(true);
                        dvdCopyForm.setManaged(true);
                        bookCopyForm.setVisible(false);
                        bookCopyForm.setManaged(false);
                    }
                    enterIsbn.setVisible(false);
                    enterIsbn.setManaged(false);
                    enterDvdNo.setVisible(false);
                    enterDvdNo.setManaged(false);
                    okButton.setVisible(false);
                    okButton.setManaged(false);
                }
        });

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
        bookCopyForm.setVisible(false);
        bookCopyForm.setManaged(false);
        dvdCopyForm.setVisible(false);
        dvdCopyForm.setManaged(false);
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
