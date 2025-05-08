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
    private String publisher; //null för dvder
    private Integer year;
    private Long isbnOrDvdNr; // Kan vara null för DVD
    private String genre; //Kan vara null för böcker
    private String availability; 
    private String placement;

    //böcker
    public SearchResultItem(String title, String author, String publisher, int year, Long isbn, String availability, String placement) {
        this.title = title;
        this.authorOrDirector = author;
        this.publisher = publisher;
        this.year = year;
        this.isbnOrDvdNr = isbn;
        this.genre = null;
        this.availability = availability;
        this.placement = placement;
    }
    
    //dvder
    public SearchResultItem(String title, String director, int year, 
            Long dvdNr, String genre, String availability, String placement) {
        this.title = title;
        this.authorOrDirector = director;
        this.publisher = null; //ingen publisher
        this.year = year;
        this.isbnOrDvdNr = dvdNr; 
        this.genre = genre;
        this.availability = availability;
        this.placement = placement;
    }

    public String getTitle() {
        return title;
    }
    public String getAuthorOrDirector() {
        return authorOrDirector;
    }
    public String getPublisher(){
        return publisher;
    }
    public int getYear() {
        return year;
    }
    public Long getIsbn() {
        return isbnOrDvdNr;
    }
    public Long getdvdNr(){
        return isbnOrDvdNr;
    }
    public String getGenre(){
        return genre; 
    }
    public String getAvailability(){
        return availability; 
    }
    public String getPlacement(){
        return placement;
    }
}
