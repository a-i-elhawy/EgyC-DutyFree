/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.egyc_dutyfree5;

/**
 *
 * @author a
 */
public class Users {

    private final int password;

    private final String userName, Name, Category;

    public Users(String userName, int password, String Name, String Category) {
        this.userName = userName;
        this.password = password;
        this.Name = Name;
        this.Category = Category;

    }

    public String getuserName() {
        return userName;

    }

    public int getpassword() {
        return password;
    }

    public String getName() {
        return Name;
    }

    public String getCategory() {
        return Category;
    }


}
