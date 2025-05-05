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
    private String postCode;
    private int activeLoans;
    private String role;
    private String password;
    private List<Copies> loans; //bör nog ändras. loans ska väl innehålla lista på alla Lån. 
    
    public User(int userId, int userCategory, String email, String firstName, 
                String lastName, String streetName, String city, 
                String postCode, int activeLoans, String role, String password) {
        this.userId = userId;
        this.userCategory = userCategory;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.streetName = streetName;
        this.city = city;
        this.postCode = postCode;
        this.activeLoans = activeLoans;
        this.role = role; 
        this.password = password; 
        this.loans = new ArrayList<>();
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
    public String getEmail(){
        return email; 
    }
    public int getActiveLoans(){
        return activeLoans;
    }
    public int getUserCategory() {
        return userCategory;
    }
    public String getRole(){
        return role; 
    }
    public List<Copies> getLoans() {
        return loans;
    }
    public void addLoan(BookCopies copy) {
        loans.add(copy);
    }
    public void removeLoan(BookCopies copy) {
        loans.remove(copy);
    }
    
    @Override 
    public String toString(){
        return getFirstName() + getLastName() + "with UserId: " + getUserId();
    }


}
