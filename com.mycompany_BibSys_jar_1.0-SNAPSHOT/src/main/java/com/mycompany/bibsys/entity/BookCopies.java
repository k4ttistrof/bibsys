/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys.entity;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author katay
 */
public class BookCopies extends Copies {
    private boolean isReference;
    private Book book;
    private LocalDate dueDate;
    
    public BookCopies(int barcode, boolean isReference, Book book){
        super(barcode); 
        this.isReference = isReference;
        this.book = book;
    }
    
    public boolean isReference(){
        return isReference; 
    }
    public void setReference(boolean isReference){
        this.isReference = isReference; 
    }
    public String getBookTitle(){
        return book.getTitle(); 
    }

         //Ignorera allt nedanför. bara massa onödigt kladd och förvirrad, irrelevant kod 
    @Override
    public Object getItem(){
        return "Book";
    }
    
    @Override 
    public void loan(){
        if(isReference){
            throw new IllegalStateException("This book is a reference copy"
                    + "and cannot be loaned out."); 
        }
        if(!isAvailable()){
            throw new IllegalStateException("This book is already on loan!");
        }
        setAvailable(false);
        int loanDays = book.getLoanTime();
        dueDate = LocalDate.now().plusDays(loanDays);
        
        System.out.println("Book" + book.getTitle() + " has been loaned.");
        System.out.println("Due date is: " + dueDate.format(DateTimeFormatter.ISO_DATE));
    }
    
    @Override 
    public void returnLoan(){
        if(isAvailable()){
            throw new IllegalStateException("This book is not on loan "
                    + "and therefore cannot be returned!");
        }
        setAvailable(true); 
        dueDate = null; 
        System.out.println("Book" + book.getTitle() + " has been returned.");
    }
}
