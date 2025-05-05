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
public class DVDCopies extends Copies {
    private DVD dvd; 
    private LocalDate dueDate;
    
    public DVDCopies(int barcode, DVD dvd){
    super(barcode); 
    this.dvd = dvd; 
    }
    
    public DVD getDvd(){
        return dvd; 
    }
    
    
     //Ignorera allt nedanför. bara massa onödigt kladd och förvirrad, irrelevant kod 
    @Override
     public Object getItem(){
        return "DVD";
    }
     
    @Override
    public void loan(){
        if(!isAvailable()){
            throw new IllegalStateException("This dvd is already on loan!");
        }
        setAvailable(false);
        dueDate = LocalDate.now().plusDays(7);
        
        System.out.println("Dvd" + dvd.getTitle() + " has been loaned.");
        System.out.println("Due date is: " + dueDate.format(DateTimeFormatter.ISO_DATE));
        
    }
    
    @Override 
    public void returnLoan(){
         if(isAvailable()){
            throw new IllegalStateException("This dvd is not on loan "
                    + "and therefore cannot be returned!");
        }
        setAvailable(true); 
        dueDate = null; 
        System.out.println("Dvd" + dvd.getTitle() + " has been returned.");
    }
}
