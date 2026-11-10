/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.egyc_dutyfree5;

/**
 *
 * @author Ahmed
 */
public class Items {
    private final int OldMaterial,Material,Quantity;
    
    private final double FobPrice,SalesPrice;

    private final String Description, AddDate, Category,EAN;

    public Items(int OldMaterial, int Material, String EAN, int Quantity, double FobPrice, double SalesPrice, String Description, String AddDate, String Category) {
        this.OldMaterial = OldMaterial;
        this.Material = Material;
        this.EAN = EAN;
        this.Quantity = Quantity;
        this.FobPrice = FobPrice;
        this.SalesPrice = SalesPrice;
        this.Description = Description;
        this.AddDate = AddDate;
        this.Category = Category;
    }

    public int getOldMaterial() {
        return OldMaterial;
    }

    public int getMaterial() {
        return Material;
    }

    public String getEAN() {
        return EAN;
    }

    public int getQuantity() {
        return Quantity;
    }

    public double getFobPrice() {
        return FobPrice;
    }

    public double getSalesPrice() {
        return SalesPrice;
    }

    public String getDescription() {
        return Description;
    }

    public String getAddDate() {
        return AddDate;
    }

    public String getCategory() {
        return Category;
    }

     
}
