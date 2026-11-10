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
class Bills {

    private final int id, input, output, count, price, unitPrice, billNum,winTaxD,winTaxP;

    private final String addDate, nation, notes, type, name, passport, birthDate;
    private final byte[] image;

     public Bills(byte[] image, String nation, String notes, String type,int winTaxP,int winTaxD, int unitPrice, int price, int count, int output, int input, String addDate, int id, int billNum, String name, String passport, String birthDate) {
        this.birthDate = birthDate;
        this.passport = passport;
        this.name = name;
        this.billNum = billNum;
        this.id = id;
        this.addDate = addDate;
        this.input = input;
        this.output = output;
        this.count = count;
        this.price = price;
        this.unitPrice = unitPrice;
        this.winTaxD= winTaxD;
        this.winTaxP= winTaxP;
        this.type = type;
        this.notes = notes;
        this.nation = nation;
        this.image = image;
    }

   public String getbirthDate() {
        return birthDate;

    }

    public String getpassport() {
        return passport;

    }

    public String getname() {
        return name;

    }

    public int getbillNum() {
        return billNum;
    }

    public int getId() {
        return id;
    }

    public String getaddDate() {
        return addDate;

    }

    public int getinput() {
        return input;
    }

    public int getoutput() {
        return output;
    }

    public int getcount() {
        return count;

    }

    public int getprice() {
        return price;
    }

    public int getunitPrice() {
        return unitPrice;
    }

    public int getwinTaxD() {
        return winTaxD;
    }

    public int getwinTaxP() {
        return winTaxP;
    }

    public String gettype() {
        return type;

    }

    public String getnotes() {
        return notes;
    }

    public String getnation() {
        return nation;

    }

    public byte[] getimage() {
        return image;

    }

}