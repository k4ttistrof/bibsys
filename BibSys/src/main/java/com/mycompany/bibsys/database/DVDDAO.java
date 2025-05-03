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
import java.util.*;
import java.sql.*;

public class DVDDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/bbd";
    private static final String USER = "root";
    private static final String PASSWORD = "#Katot99"; //ändra till rätt lösenord 
    
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
            
            ResultSet rs = stmt.executeQuery(); 
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
        catch (SQLException e){
            e.printStackTrace();

        }
        
        return new ArrayList<>(dvdMap.values()); 
    
    }
    
}
