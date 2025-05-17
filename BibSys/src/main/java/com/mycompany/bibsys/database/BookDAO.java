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
    private static final String PASSWORD ="#Katot99"; //ändra till rätt lösenord 
    
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
    
    public static boolean deleteBookAndCopies(long isbn){
        String deleteCopiesSql = "DELETE FROM bookcopy WHERE isbn = ?";
        String deleteBookSql = "DELETE FROM book WHERE isbn = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            connection.setAutoCommit(false);
            try(
                PreparedStatement deleteCopiesStmt = connection.prepareStatement(deleteCopiesSql);
                PreparedStatement deleteBookStmt = connection.prepareStatement(deleteBookSql)){
                deleteCopiesStmt.setLong(1, isbn);
                deleteCopiesStmt.executeUpdate();
                
                deleteBookStmt.setLong(1, isbn);
                int affectedRows = deleteBookStmt.executeUpdate();
                
                connection.commit();
                return affectedRows > 0;
            }
            catch (SQLException e){
                connection.rollback();
                e.printStackTrace();
                return false; 
            }   
        }
        catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public static BookCopies getBookCopy(int bookCopyId){
        String sql = "SELECT bc.bookCopyID, bc.isReferenceCopy, bc.onLoan,"
                + " b.isbn, b.title, b.author, b.publisher, b.publishingYear, b.bookCategory "
                + "FROM bookcopy bc JOIN book b ON bc.isbn = b.isbn "
                + "WHERE bc.bookCopyID = ?";
        
        try(Connection connection = DriverManager.getConnection(URL,USER, PASSWORD);
            PreparedStatement stmt = connection.prepareStatement(sql)){
            
            stmt.setInt(1, bookCopyId);
            try(ResultSet rs = stmt.executeQuery()){
                if (rs.next()){
                    long isbn = rs.getLong("isbn");
                    String title = rs.getString("title");
                    String author = rs.getString("author");
                    String publisher = rs.getString("publisher");
                    int year = rs.getInt("publishingYear");
                    int category = rs.getInt("bookCategory"); 
                    
                    Book book = new Book (isbn, title, author, publisher, year, category, null);
                    
                    int copyId = rs.getInt("bookCopyID");
                    boolean isReference = rs.getInt("isReferenceCopy") == 1; 
                    boolean isAvailable = rs.getInt("onLoan") == 0; 
                    
                    BookCopies copy = new BookCopies(copyId, isReference, book);
                    copy.setAvailable(isAvailable);
                    return copy;
                }
            }
                
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        return null; 
   
    }
    
    public static boolean deleteBookCopy(int copyId){
        String sql = "DELETE FROM bookcopy WHERE bookCopyID = ?";
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, copyId);
            return stmt.executeUpdate() > 0; 
        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    
    public static String getBookTitleByISBN(long isbn) { //hämtar boktiteln med sökning på isbn
        String sql = "SELECT title FROM book WHERE ISBN = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, isbn);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("title");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean addBookWithCopies(long isbn, String title, String author, String publisher,
                                        int year, int category, String placement, int nrOfCopies, boolean isReferenceCopy) {
        String insertBookSQL = "INSERT INTO book (ISBN, title, author, publisher, publishingYear, bookCategory, placement) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertCopySQL = "INSERT INTO bookcopy (isbn, isReferenceCopy, onLoan, title, bookCategory) VALUES (?, ?, 0, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            try (PreparedStatement psBook = conn.prepareStatement(insertBookSQL);
                 PreparedStatement psCopy = conn.prepareStatement(insertCopySQL)) {

                // Insert into book
                psBook.setLong(1, isbn);
                psBook.setString(2, title);
                psBook.setString(3, author);
                psBook.setString(4, publisher);
                psBook.setInt(5, year);
                psBook.setInt(6, category);
                psBook.setString(7, placement);
                psBook.executeUpdate();

                // Insert copies
                for (int i = 0; i < nrOfCopies; i++) {
                    boolean isRefCopy = isReferenceCopy && i == 0;
                    psCopy.setLong(1, isbn);
                    psCopy.setInt(2, isRefCopy ? 1 : 0);
                    psCopy.setString(3, title);
                    psCopy.setInt(4, category);
                    psCopy.addBatch();
                }

                psCopy.executeBatch();
                conn.commit();
                return true;

            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean addBookCopyByISBN(long isbn, boolean isReferenceCopy) throws SQLException {
        String sqlGet = "SELECT title, bookCategory FROM book WHERE ISBN = ?";
        String insertSQL = "INSERT INTO bookcopy (isbn, isReferenceCopy, onLoan, title, bookCategory) VALUES (?, ?, 0, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement psGet = conn.prepareStatement(sqlGet)) {

            psGet.setLong(1, isbn);

            try (ResultSet rs = psGet.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");
                    int category = rs.getInt("bookCategory");

                    try (PreparedStatement psInsert = conn.prepareStatement(insertSQL)) {
                        psInsert.setLong(1, isbn);
                        psInsert.setInt(2, isReferenceCopy ? 1 : 0);
                        psInsert.setString(3, title);
                        psInsert.setInt(4, category);
                        psInsert.executeUpdate();
                        return true;
                    }
                } else {
                    return false; // ingen bok hittad med detta isbn
                }
            }
        }
    }

}

