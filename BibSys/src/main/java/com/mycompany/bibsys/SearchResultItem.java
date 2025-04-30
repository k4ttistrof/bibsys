/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys;

/**
 *
 * @author katay
 */

public class SearchResultItem {
    private String title;
    private String authorOrDirector;
    private Integer year;
    private Long isbn; // Kan vara null för DVD
    private String genre; //Kan vara null för böcker
    private String availability; 

    public SearchResultItem(String title, String authorOrDirector, int year, Long isbn, String availability) {
        this.title = title;
        this.authorOrDirector = authorOrDirector;
        this.year = year;
        this.isbn = isbn;
        this.genre = null;
        this.availability = availability;
    }
    
    public SearchResultItem(String title, String director, int year, String genre, String availability) {
        this.title = title;
        this.authorOrDirector = director;
        this.year = year;
        this.isbn = null; // Visa inget för ISBN
        this.genre = genre;
        this.availability = availability;
    }

    public String getTitle() {
        return title;
    }
    public String getAuthorOrDirector() {
        return authorOrDirector;
    }
    public int getYear() {
        return year;
    }
    public Long getIsbn() {
        return isbn;
    }
    
    public String getGenre(){
        return genre; 
    }
    public String getAvailability(){
        return availability; 
    }
}
