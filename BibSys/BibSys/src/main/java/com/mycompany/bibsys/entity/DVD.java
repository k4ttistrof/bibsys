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
public class DVD {
    private int dvdNo; 
    private String title; 
    private String director; 
    private int year; 
    private String genre; 
    private String placement; 
    private List<DVDCopies> copies = new ArrayList<>();
    
    public DVD(int dvdNo, String title, String director, int year, String genre, String placement){
        this.dvdNo = dvdNo; 
        this.title = title; 
        this.director = director; 
        this.year = year; 
        this.genre = genre; 
        this.placement = placement; 
    }
    
    public int getDvdNo(){
        return dvdNo; 
    }
    public String getTitle(){
        return title; 
    }
    public String getDirector(){
        return director; 
    }
    public int getYear(){
        return year;
    }
    public String getGenre(){
        return genre; 
    }
    public String getPlacement(){
        return placement; 
    }
    public void addCopy(DVDCopies copy){
        copies.add(copy);
    }
    public List<DVDCopies> getCopies(){
        return copies;
    }
    
    @Override
    public String toString() {
        return "DVD [Title: " + title + ", director: " + director + "]";
    }
}
