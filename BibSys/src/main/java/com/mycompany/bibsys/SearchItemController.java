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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class SearchItemController {

    @FXML
    private MenuItem menuLogIn;

    @FXML
    private Button searchItemButton;

    @FXML
    private TextField searchItemTextField;

    @FXML
    private TableView<SearchResultItem> searchResultsTable;

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

    
    public void initialize(){
        searchTypeChoiceBox.getItems().addAll("Book", "DVD");
        searchTypeChoiceBox.setValue("Book");
        
        searchItemButton.setOnAction(e -> searchItems());
        setBookColumns();
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
        
        if ("Book".equals(searchType)){
            List<Book> books = BookDAO.searchBooks(searchQuery);
            updateTableWithBooks(books);
            setBookColumns();
        }
        else if ("DVD".equalsIgnoreCase(searchType)){
            List<DVD> dvds = DVDDAO.searchDVDs(searchQuery); 
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
                bookResults.add(new SearchResultItem(book.getTitle(), book.getAuthor(), book.getYear(), book.getIsbn(), availability));
            }
        }
        searchResultsTable.setItems(bookResults);
    }
    
    private void updateTableWithDvd(List<DVD> dvds){
        ObservableList<SearchResultItem> dvdResults = FXCollections.observableArrayList(); 
        for (DVD dvd : dvds){
            for(DVDCopies copy : dvd.getCopies()){
                String availability = copy.isAvailable() ? "Available" : "On loan.";
                dvdResults.add(new SearchResultItem(dvd.getTitle(), dvd.getDirector(), dvd.getYear(), dvd.getGenre(), availability));
            }
        }
        searchResultsTable.setItems(dvdResults);
    }
   

}
