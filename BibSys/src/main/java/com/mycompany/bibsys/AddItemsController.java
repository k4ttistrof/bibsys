/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys;

import com.mycompany.bibsys.database.BookDAO;
import com.mycompany.bibsys.database.DVDDAO;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

public class AddItemsController {

    @FXML
    private Button addBookButton;
    @FXML
    private Button addCopyButton;
    @FXML
    private Button addItemButton;
    @FXML
    private Button addDvdButton;
    @FXML
    private Button addBookCopyButton;
    @FXML
    private Button addDvdCopyButton;
    @FXML
    private GridPane itemType;
    @FXML
    private CheckBox copyIsReferenceBox;
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
    private GridPane bookCopyForm;
    @FXML
    private TextField bookCopyTextField;
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
    private GridPane dvdCopyForm;
    @FXML
    private TextField dvdCopyTextField;
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
    
    private boolean isAddingWholeItem = false;

    @FXML
    private void addItemButtonPressed(ActionEvent event) {
        isAddingWholeItem = true;
        addCopyButton.setDisable(true);
        itemType.setVisible(true); itemType.setManaged(true);
        hideAllForms();}
    
    @FXML 
    private void addCopyButtonPressed(ActionEvent event){
        addItemButton.setDisable(true);
        itemType.setVisible(true); itemType.setManaged(true);
        hideAllForms();}
    
    @FXML
    void addBookButtonPressed(ActionEvent event) {
            addBookToDatabase();}

    @FXML
    void addDvdButtonPressed(ActionEvent event){
            addDvdToDatabase();}
    @FXML
    void addBookCopyButtonPressed(ActionEvent event) {
        addSingleBookCopy();}
        
    @FXML
    void addDvdCopyButtonPressed(ActionEvent event) {
        addSingleDvdCopy();}
    
    public void initialize(){
        searchTypeChoiceBox.getItems().addAll("Book", "DVD");
        categoryChoiceBox.getItems().addAll(1, 2);
        genreChoiceBox.getItems().addAll("documentary", "romance", "comedy", "drama", "thriller", "horror");
        Tooltip bcToolTip = new Tooltip("Book Category 1 = Regular book. Book Category 2 = Course literature.");
        Tooltip.install(bcInfoIcon, bcToolTip);
        Tooltip ircToolTip = new Tooltip("Check the box if the book should have a reference copy that cannot be borrowed.");
        Tooltip.install(ircInfoIcon, ircToolTip);
        
        searchTypeChoiceBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (isAddingWholeItem) {
                if ("Book".equals(newVal)) {
                    showOnly(bookForm);
                } else if ("DVD".equals(newVal)) {
                    showOnly(dvdForm);
                }
            } else {
                if ("Book".equals(newVal)) {
                    showOnly(bookCopyForm);
                } else if ("DVD".equals(newVal)) {
                    showOnly(dvdCopyForm);
                }
            }
        });
        
        hideAllForms();
        itemType.setVisible(false); itemType.setManaged(false);
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

        try {
            isbnLong = Long.parseLong(isbn);
            yearInt = Integer.parseInt(year);
            nrOfCopies = Integer.parseInt(copiesStr);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Incorrect!", "Year, ISBN and Number of Copies must be numbers!");
            return;
        }
        
        if (BookDAO.bookExists(isbnLong)) {
            showAlert(Alert.AlertType.ERROR, "Book Already Exists", "A book with this ISBN already exists in the database.");
            return;
        }
        
        boolean success = BookDAO.addBookWithCopies(isbnLong, title, author, publisher, yearInt, category, placement, nrOfCopies, isReference);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success!", "Book added with " + nrOfCopies + " copies.");
            clearBookForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not add books to database.");
        }
    }
    
    private void addSingleBookCopy(){
        try {
            String isbnText = bookCopyTextField.getText().trim();
            if (isbnText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid ISBN.");
                return;
            }
            long isbn = Long.parseLong(isbnText);
            boolean isReference = copyIsReferenceBox.isSelected();

            String title = BookDAO.getBookTitleByISBN(isbn);

            if (title == null) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "No book copy with this ISBN was found in the database.");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Add");
            confirmAlert.setHeaderText("Add Book Copy");

            StringBuilder content = new StringBuilder();
            content.append("You are about to add a copy of the book titled:\n\n");
            content.append(title).append("\n\n");
            if (isReference) {
            content.append("This is a reference copy and cannot be borrowed.\n\n");
            }
            content.append("Are you sure you want to continue?");

            TextArea textArea = new TextArea(content.toString());
            textArea.setWrapText(true);
            textArea.setEditable(false);
            textArea.setMaxWidth(Double.MAX_VALUE);
            textArea.setMaxHeight(Double.MAX_VALUE);

            confirmAlert.getDialogPane().setContent(textArea);
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = BookDAO.addBookCopyByISBN(isbn, isReference);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Book copy added successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Book copy could not be added.");
                }
            }
        } 
        catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Input Error", "ISBN must be numeric!");
        } 
        catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Could not connect to database."); 
        e.printStackTrace();
        }
        bookCopyTextField.clear();
        copyIsReferenceBox.setSelected(false);
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

        try {
            releaseYearInt = Integer.parseInt(releaseYear);
            nrOfCopies = Integer.parseInt(copies);
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Release year and Number of Copies must be numbers!");
            return;
        }

        boolean success = DVDDAO.addDvdWithCopies(title, director, releaseYearInt, genre, placement, nrOfCopies);

        if (success) {
            showAlert(Alert.AlertType.INFORMATION, "Success!", "DVD added with " + nrOfCopies + " copies.");
            clearDvdForm();
        } else {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not add DVD to database.");
        }
    }
    
    private void addSingleDvdCopy(){
        try {
            String dvdNoText = dvdCopyTextField.getText().trim();
            if (dvdNoText.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Input Error", "Please enter a valid DVD number.");
                return;
            }

            int dvdNo = Integer.parseInt(dvdNoText);
            String title = DVDDAO.getDvdTitleByDvdNo(dvdNo);

            if (title == null) {
                showAlert(Alert.AlertType.ERROR, "Not Found", "No DVD with this number was found.");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Add");
            confirmAlert.setHeaderText("Add DVD Copy");
            confirmAlert.setContentText("You are about to add a copy of the DVD titled: '" + title + "'. \nAre you sure?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                boolean success = DVDDAO.addDvdCopyByDvdNo(dvdNo);
                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "DVD copy added successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Could not add DVD copy.");
                }
            }
        } catch (NumberFormatException e) {
        showAlert(Alert.AlertType.ERROR, "Input Error", "DVD number must be numeric!");
        } catch (SQLException e) {
        showAlert(Alert.AlertType.ERROR, "Something went wrong!", "Could not connect to database.");
        e.printStackTrace();
        }
        dvdCopyTextField.clear();
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

    private void hideAllForms() {
        bookForm.setVisible(false); bookForm.setManaged(false);
        dvdForm.setVisible(false); dvdForm.setManaged(false);
        bookCopyForm.setVisible(false); bookCopyForm.setManaged(false);
        dvdCopyForm.setVisible(false); dvdCopyForm.setManaged(false);
    }
    
    private void showOnly(Node form) {
        hideAllForms();
        form.setVisible(true);
        form.setManaged(true);
    }
}
