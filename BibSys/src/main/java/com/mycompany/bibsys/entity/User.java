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

//började smått med detta men släppte allt och fokuserade på sökning istället
public class User {
    private int userId; 
    private int userCategory; 
    private String email; 
    private String firstName; 
    private String lastName; 
    private String streetName; 
    private String city; 
    private int postCode;
    private List<Copies> activeLoans;
    
    public User(int userId, int userCategory, String email, String firstName, 
                String lastName, String streetName, String city, 
                int postCode) {
        this.userId = userId;
        this.userCategory = userCategory;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetName = streetName;
        this.city = city;
        this.postCode = postCode;
        this.activeLoans = new ArrayList<>();
    }
    
    public int getUserId() {
        return userId;
    }
    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public int getUserCategory() {
        return userCategory;
    }
    public List<Copies> getActiveLoans() {
        return activeLoans;
    }
    public void addActiveLoan(BookCopies copy) {
        activeLoans.add(copy);
    }
    public void removeActiveLoan(BookCopies copy) {
        activeLoans.remove(copy);
    }
    
    @Override 
    public String toString(){
        return getFirstName() + getLastName() + "with UserId: " + getUserId();
    }


}
