/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibsys.entity;

/**
 *
 * @author katay
 */
    public abstract class Copies {
    private int barcode; 
    private boolean isAvailable;  
    
    //konstruktor
    public Copies(int barcode) {
        this.barcode = barcode;
    }
    
    public void setBarcode(int barcode) {
    this.barcode = barcode;
    }
    public int getBarcode() {
        return barcode;
    }
    
    public boolean isAvailable() {
        return isAvailable;
    }
    public void setAvailable(boolean available){
        isAvailable = available;
    }
    
    public abstract Object getItem();
    public abstract void loan(); 
    public abstract void returnLoan(); 
    
}