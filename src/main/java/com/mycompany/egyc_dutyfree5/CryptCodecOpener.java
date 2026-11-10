/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.egyc_dutyfree5;

import net.ucanaccess.jdbc.JackcessOpenerInterface; 
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.crypt.CryptCodecProvider;
import java.io.File;
import java.io.IOException;

public class CryptCodecOpener implements JackcessOpenerInterface {
    
    @Override
    public Database open(File fl, String pwd) throws IOException {
        DatabaseBuilder db = new DatabaseBuilder(fl);
        // استخدام مزود التشفير الجديد بدلاً من setPassword القديمة
        db.setCodecProvider(new CryptCodecProvider(pwd));
        return db.open();
    }
}