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
public class branches {

    
          

    private final String BranchName,id1,id2,id3,status;

    public branches(String BranchName, String id1, String id2, String id3,String status) {
        this.BranchName = BranchName;
        this.id1 = id1;
        this.id2 = id2;
        this.id3 = id3;
        this.status = status;

    }

    public String getBranchName() {
        return BranchName;
    }

    public String getId1() {
        return id1;
    }

    public String getId2() {
        return id2;
    }

    public String getId3() {
        return id3;
    }

    public String getStatus() {
        return status;
    }

   


}
