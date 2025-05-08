/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys.database;

/**
 *
 * @author katay
 */

import com.mycompany.bibsys.entity.Book;
import com.mycompany.bibsys.entity.BookCopies;
import java.util.*; 
import java.sql.*;

public class BookDAO { //DAO = Data Access Object
    private static final String URL = "jdbc:mysql://localhost:3306/bbd";
    private static final String USER = "root";
    private static final String PASSWORD =""; //ändra till rätt lösenord 
    
    public static List<Book> searchBooks(String query){
        List<Book> books = new ArrayList<>();
        Map<Long, Book> bookMap = new HashMap<>();
        
        String sql = "SELECT b.*, bc.* " + 
                "FROM book b " + 
                "LEFT JOIN bookcopy bc on b.ISBN = bc.ISBN " + 
                "WHERE b.title LIKE ? OR b.author LIKE ?";
        
        if(isValidISBN(query)){
            sql += " OR b.isbn = ?";
        }
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(sql)){
            
            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern); //för titel
            stmt.setString(2, searchPattern); //för author
            
            if(isValidISBN(query)){
                stmt.setLong(3, Long.parseLong(query));
            }
   
            try (ResultSet rs = stmt.executeQuery()){
                while (rs.next()){
                    long isbn = rs.getLong("ISBN");
                
                    Book book = bookMap.get(isbn);
                    if (book == null){
                        String title = rs.getString("title");
                        String author = rs.getString("author");
                        String publisher = rs.getString("publisher"); 
                        int year = rs.getInt("publishingYear");
                        int category = rs.getInt("bookCategory");
                        String placement = rs.getString("placement");
   
                
                        book = new Book(isbn, title, author, publisher, year, category, placement);
                        bookMap.put(isbn, book);
                    }
                    int copyId = rs.getInt("bookCopyID");
        
                    if (copyId != 0){
                        boolean isAvailable = rs.getInt("onLoan") == 0;
                        boolean isReference = rs.getInt("isReferenceCopy") == 1;
                    
                        BookCopies copy = new BookCopies(copyId, isReference, book);
                        copy.setAvailable(isAvailable);
                    
                        book.addCopy(copy);
                    }
                }
            }
        }
         catch (SQLException e){
            e.printStackTrace();
        }
        return new ArrayList<>(bookMap.values());
        }
    
    public static boolean isValidISBN(String query){
        try{
            Long.parseLong(query);
            return true; 
        } catch (NumberFormatException e){
            return false;
        }
    }
 
}

