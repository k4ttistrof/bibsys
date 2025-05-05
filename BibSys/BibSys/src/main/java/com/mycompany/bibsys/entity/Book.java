/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author katay
 */
public class Book {
    private long isbn;
    private String title;
    private String author; 
    private String publisher; 
    private int year;
    private int category; 
    private String placement;
    private List<BookCopies> copies; 
    
     public Book(long isbn, String title, String author,String publisher, 
             int year, int category, String placement) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.publisher = publisher; 
        this.year = year;
        this.placement = placement;
        
        if (category != 1 && category != 2) {
            throw new IllegalArgumentException("Category must be 1 or 2.");
        }
        this.category = category;
        this.copies = new ArrayList<>();
     }
     
     
    public long getIsbn() {
        return isbn;
    }
     public String getTitle() {
        return title;
    }
    public String getAuthor() {
        return author;
    }
    public String getPublisher(){
        return publisher; 
    }
    public int getYear(){
        return year; 
    }
    public String getPlacement(){
        return placement;
    }
    public List<BookCopies> getCopies(){
        return copies;
    }
    public void addCopy(BookCopies copy){
        copies.add(copy);
    }
    public void removeCopy(BookCopies copy){
        copies.remove(copy);
    }
    
         //Ignorera allt nedanför. bara massa onödigt kladd och förvirrad, irrelevant kod 
    public int getLoanTime(){
        switch (category){
            case 1: 
                return 30;
            case 2: 
                return 14; 
            default: 
                throw new IllegalArgumentException("Invalid category:" + category);   
        }
    }
    
    public BookCopies findAvaialbleCopy(){
        for(BookCopies copy : copies){
            if (copy.isAvailable()){
                return copy;
            }
        }
        System.out.println("No available copies of this book found.");
        return null; 
    }
    
    @Override
    public String toString() {
        return "Book [Title: " + title + ", Author: " + author + "]";
    }

}
