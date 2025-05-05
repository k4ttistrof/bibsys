/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys;

/**
 *
 * @author katay
 */

import com.mycompany.bibsys.database.BookDAO;
import com.mycompany.bibsys.database.DVDDAO;
import com.mycompany.bibsys.entity.Book;
import com.mycompany.bibsys.entity.BookCopies;
import com.mycompany.bibsys.entity.DVD;
import com.mycompany.bibsys.entity.DVDCopies;
import java.io.IOException;
import java.util.*;
import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class SearchItemController {

    @FXML
    private MenuItem menuLogIn;

    @FXML
    private Button searchItemButton;

    @FXML
    private TextField searchItemTextField;

    @FXML
    private TableView<SearchResultItem> searchResultsTable;
    private Parent mainRoot; 
    @FXML
    private ChoiceBox<String> searchTypeChoiceBox;

    @FXML
    private TableColumn<SearchResultItem, String> titleColumn;
    
    @FXML
    private TableColumn<SearchResultItem, String> authorOrDirectorColumn;
    
    @FXML
    private TableColumn<SearchResultItem, Integer> yearColumn;

    @FXML
    private TableColumn<SearchResultItem, Long> isbnColumn;
    
    @FXML
    private TableColumn<SearchResultItem, String> genreColumn;
    
    @FXML
    private TableColumn<SearchResultItem, String> availabilityColumn;
    
     @FXML
    void menuLogInPressed(ActionEvent event) {
        try {
            if(Session.getCurrentUser() != null){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("UserLoggedInPage.fxml"));
                Parent userLoggedInRoot = loader.load();

                UserLoggedInPageController controller = loader.getController();
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
    

    public void initialize(){
        updateMenuForUser();
        searchTypeChoiceBox.getItems().addAll("Book", "DVD");
        searchTypeChoiceBox.setValue("Book");
        
        searchItemButton.setOnAction(e -> searchItems());
        setBookColumns();
        
        searchResultsTable.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2){
                SearchResultItem selectedItem = searchResultsTable.getSelectionModel().getSelectedItem();
                if (selectedItem != null && "Book".equalsIgnoreCase(searchTypeChoiceBox.getValue())){
                    openBookDetailsPage(selectedItem);
                }
                else if (selectedItem != null && "DVD".equalsIgnoreCase(searchTypeChoiceBox.getValue())){
                    openDvdDetailsPage(selectedItem);
                }
            }
        });
    }
    
    private void updateMenuForUser(){
        if (Session.getCurrentUser() != null){
            menuLogIn.setText("My Page");
            menuLogIn.setOnAction(event -> menuLogInPressed(event));
        }
        else{
            menuLogIn.setText("Log In");
            menuLogIn.setOnAction(event -> menuLogInPressed(event));
        }
    }
    public void setMainRoot(Parent root){
        this.mainRoot = root; 
    }
    private void setBookColumns(){
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorOrDirectorColumn.setCellValueFactory(new PropertyValueFactory<>("authorOrDirector"));
        authorOrDirectorColumn.setText("Author");
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
        availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
        
        isbnColumn.setVisible(true);
        genreColumn.setVisible(false);
    }
    
    private void setDvdColumns(){
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorOrDirectorColumn.setCellValueFactory(new PropertyValueFactory<>("authorOrDirector"));
        authorOrDirectorColumn.setText("Director");
        genreColumn.setCellValueFactory(new PropertyValueFactory<>("genre"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        
        isbnColumn.setVisible(false);
        genreColumn.setVisible(true);

    }
   
    private void searchItems(){
        String searchQuery = searchItemTextField.getText();
        String searchType = searchTypeChoiceBox.getValue(); 
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION); 
        alert.setTitle("Search Results");
        alert.setHeaderText(null);
        alert.setContentText("No matches found for: " + searchQuery);
        alert.getButtonTypes().setAll(ButtonType.OK);
        
        if ("Book".equals(searchType)){
            List<Book> books = BookDAO.searchBooks(searchQuery);
            if (books.isEmpty()){
                alert.showAndWait();
            }
            updateTableWithBooks(books);
            setBookColumns();
        }
        else if ("DVD".equalsIgnoreCase(searchType)){
            List<DVD> dvds = DVDDAO.searchDVDs(searchQuery); 
            if (dvds.isEmpty()){
                alert.showAndWait();
            }
            updateTableWithDvd(dvds);
            setDvdColumns();     
        }
    }
    
    private void updateTableWithBooks(List<Book> books){
        ObservableList<SearchResultItem> bookResults = FXCollections.observableArrayList();
        Set<String> addedBooks = new HashSet<>();
        
        for (Book book : books){
            if (!addedBooks.contains(book.getTitle())){
                addedBooks.add(book.getTitle());
                
                boolean isAvailable = false; 
                for (BookCopies copy : book.getCopies()){
                    if (copy.isAvailable()){
                        isAvailable = true; 
                        break; //eg. borde man kanske kolla vidare om man vill veta hur många som är available
                    }
                }
                
                String availability = isAvailable ? "Available" : "On loan"; 
                bookResults.add(new SearchResultItem(book.getTitle(), book.getAuthor(), book.getPublisher(), book.getYear(), book.getIsbn(), availability, book.getPlacement()));
            }
        }
        searchResultsTable.setItems(bookResults);
    }
    
    private void updateTableWithDvd(List<DVD> dvds){
        ObservableList<SearchResultItem> dvdResults = FXCollections.observableArrayList();
        Set<String> addedDvds = new HashSet<>();
        
        for (DVD dvd : dvds){
            if (!addedDvds.contains(dvd.getTitle())){
                addedDvds.add(dvd.getTitle());
                
                boolean isAvailable = false; 
                for (DVDCopies copy : dvd.getCopies()){
                    if (copy.isAvailable()){
                        isAvailable = true; 
                        break; //eg. borde man kanske kolla vidare om man vill veta hur många som är available
                    }
                }
                
                String availability = isAvailable ? "Available" : "On loan"; 
                dvdResults.add(new SearchResultItem(dvd.getTitle(), dvd.getDirector(), dvd.getYear(), dvd.getGenre(), availability, dvd.getPlacement()));
            }
        }
        searchResultsTable.setItems(dvdResults);
    }
    
    private void openBookDetailsPage(SearchResultItem selectedItem){
        try{
            if(mainRoot == null){
                mainRoot = searchResultsTable.getScene().getRoot();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("BookDetails.fxml"));
            Parent bookDetailsRoot = loader.load();
            
            BookDetailsController controller = loader.getController();
            controller.setMainRoot(mainRoot);
            controller.setBookDetails(selectedItem);
            controller.setMainController(this);

            searchItemButton.getScene().setRoot(bookDetailsRoot);
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
     
    private void openDvdDetailsPage(SearchResultItem selectedItem){
        try{
            if(mainRoot == null){
                mainRoot = searchResultsTable.getScene().getRoot();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("DVDDetails.fxml"));
            Parent dvdDetailsRoot = loader.load();
            
            DvdDetailsController controller = loader.getController();
            controller.setDvdDetails(selectedItem);
            controller.setMainRoot(mainRoot);
            controller.setMainController(this);
            
            searchItemButton.getScene().setRoot(dvdDetailsRoot);
            
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
