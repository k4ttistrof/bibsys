/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys.database;

/**
 *
 * @author katay
 */

import com.mycompany.bibsys.entity.DVD;
import com.mycompany.bibsys.entity.DVDCopies;
import com.mysql.cj.protocol.Resultset;
import java.util.*;
import java.sql.*;

public class DVDDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/bbd";
    private static final String USER = "root";
    private static final String PASSWORD = ""; //ändra till rätt lösenord 
    
    public static List<DVD> searchDVDs(String query){
        List<DVD> dvds = new ArrayList<>(); 
        Map<Integer, DVD> dvdMap = new HashMap<>();
        
        String sql = "SELECT d.*, dc.* " + 
                "FROM dvd d " + 
                "LEFT JOIN dvdCopy dc ON d.dvdNo =dc.dvdNo " + 
                "WHERE d.title LIKE ? OR d.director LIKE ?"; 
        
        try(Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement stmt = connection.prepareStatement(sql)){
            
            String searchPattern = "%" + query + "%"; 
            stmt.setString(1, searchPattern); 
            stmt.setString(2, searchPattern); 
            
            try (ResultSet rs = stmt.executeQuery()){ 
                while (rs.next()){
                    int dvdNo = rs.getInt("dvdNo");
                
                    DVD dvd = dvdMap.get(dvdNo);
                    if (dvd == null){
                        String title = rs.getString("title");
                        String director = rs.getString("director");
                        int year = rs.getInt("releaseYear");
                        String genre = rs.getString("genre");
                        String placement = rs.getString("placement");
                
                        dvd = new DVD(dvdNo, title, director, year, genre, placement);
                        dvdMap.put(dvdNo, dvd);
                    }
                
                    int copyId = rs.getInt("dvdNo");
                    if(copyId != 0){
                        boolean isAvailable = rs.getInt("onLoan") == 0;
                    
                        DVDCopies copy = new DVDCopies(copyId, dvd);
                        copy.setAvailable(isAvailable);
                    
                        dvd.addCopy(copy);
                    }
                }
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return new ArrayList<>(dvdMap.values()); 
    }
    
    public static List<DVD> searchDvdNo(int dvdNo){
        List<DVD> dvds = new ArrayList<>();
        Map<Integer, DVD> dvdMap = new HashMap<>();
        
        String sql = "SELECT d.*, dc.* " +
                "FROM dvd d " + 
                "LEFT JOIN dvdCopy dc ON d.dvdNo = dc.dvdNo " + 
                "WHERE d.dvdNo = ?";
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)){
            
            stmt.setInt(1, dvdNo);
            
            try(ResultSet rs = stmt.executeQuery()){
                while (rs.next()){
                    int currentDvdNo = rs.getInt("dvdNo");
                    
                    DVD dvd = dvdMap.get(currentDvdNo); 
                    if (dvd == null){
                        String title = rs.getString("title");
                        String director = rs.getString("director");
                        int year = rs.getInt("releaseYear");
                        String genre = rs.getString("genre");
                        String placement = rs.getString("placement");
                        
                        dvd = new DVD(currentDvdNo, title, director, year, genre, placement);
                        dvdMap.put(currentDvdNo, dvd);
                    }
                    
                    int copyId = rs.getInt("dvdCopyID");
                    if (copyId != 0){
                        boolean isAvailable = rs.getInt("onLoan") == 0; 
                        
                        DVDCopies copy = new DVDCopies(copyId, dvd);
                        copy.setAvailable(isAvailable);
                        
                        dvd.addCopy(copy);
                    }
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return new ArrayList<>(dvdMap.values());
    }

    public static boolean hasLoanedCopies(int dvdNo) {
        String sql = "SELECT COUNT(*) FROM dvdcopy WHERE dvdNo = ? AND onLoan = 1";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, dvdNo);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean deleteDvdAndCopies(int dvdNo){
        String deleteCopiesSql = "DELETE FROM dvdCopy WHERE dvdNo = ?";
        String deleteDvdSql = "DELETE FROM dvd WHERE dvdNo = ?";
        
        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD)){
            connection.setAutoCommit(false);
            try(
                    PreparedStatement deleteCopiesStmt = connection.prepareStatement(deleteCopiesSql);
                    PreparedStatement deleteDvdStmt = connection.prepareStatement(deleteDvdSql)){
                
                deleteCopiesStmt.setLong(1, dvdNo);
                deleteCopiesStmt.executeUpdate();
                
                deleteDvdStmt.setLong(1, dvdNo);
                int affectedRows = deleteDvdStmt.executeUpdate();
                
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
    
    public static DVDCopies getDVDCopy(int copyId) {
        String sql = "SELECT dc.dvdCopyID, dc.dvdNo, dc.onLoan, d.title, d.director, d.releaseYear, d.genre, d.placement " +
                     "FROM dvdCopy dc JOIN dvd d ON dc.dvdNo = d.dvdNo " +
                     "WHERE dc.dvdCopyID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, copyId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int dvdNo = rs.getInt("dvdNo");
                    String title = rs.getString("title");
                    String director = rs.getString("director");
                    int year = rs.getInt("releaseYear");
                    String genre = rs.getString("genre");
                    String placement = rs.getString("placement");
                    boolean isAvailable = rs.getInt("onLoan") == 0;

                    DVD dvd = new DVD(dvdNo, title, director, year, genre, placement);
                    DVDCopies copy = new DVDCopies(copyId, dvd);
                    copy.setAvailable(isAvailable);

                    return copy;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null; // Om kopian inte hittas
    }
    
    public static boolean deleteDVDCopy(int copyId) {
        String sql = "DELETE FROM dvdCopy WHERE dvdCopyID = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, copyId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public static String getDvdTitleByDvdNo(int dvdNo) {
        String sql = "SELECT title FROM dvd WHERE dvdNo = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, dvdNo);
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

    public static boolean addDvdWithCopies(String title, String director, int releaseYear, String genre, String placement, int nrOfCopies) {
        String insertDvdSQL = "INSERT INTO dvd (title, director, releaseYear, genre, loanTime, placement) VALUES (?, ?, ?, ?, ?, ?)";
        String insertCopySQL = "INSERT INTO dvdcopy (dvdNo, title, onLoan) VALUES (?, ?, 0)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            conn.setAutoCommit(false);

            try (PreparedStatement ps = conn.prepareStatement(insertDvdSQL, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, title);
                ps.setString(2, director);
                ps.setInt(3, releaseYear);
                ps.setString(4, genre);
                ps.setInt(5, 14); // standard lånetid
                ps.setString(6, placement);
                ps.executeUpdate();

                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int dvdNo = rs.getInt(1);

                        try (PreparedStatement psCopy = conn.prepareStatement(insertCopySQL)) {
                            for (int i = 0; i < nrOfCopies; i++) {
                                psCopy.setInt(1, dvdNo);
                                psCopy.setString(2, title);
                                psCopy.addBatch();
                            }
                            psCopy.executeBatch();
                        }

                        conn.commit();
                        return true;
                    } else {
                        conn.rollback();
                        return false;
                    }
                }
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


    public static boolean addDvdCopyByDvdNo(int dvdNo) throws SQLException {
        String sqlGet = "SELECT title FROM dvd WHERE dvdNo = ?";
        String sqlInsert = "INSERT INTO dvdCopy (dvdNo, title, onLoan) VALUES (?, ?, 0)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement psGet = conn.prepareStatement(sqlGet)) {

            psGet.setInt(1, dvdNo);
            try (ResultSet rs = psGet.executeQuery()) {
                if (rs.next()) {
                    String title = rs.getString("title");

                    try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                        psInsert.setInt(1, dvdNo);
                        psInsert.setString(2, title);
                        psInsert.executeUpdate();
                        return true;
                    }
                } else {
                    return false; // ingen matchande DVD hittad
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
            return false; 
        }
    }
  
}
