package com.mycompany.egyc_dutyfree5;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import javax.swing.JOptionPane;

public class DBConnection {

    private Connection connection;
    private static DBConnection instance;
    private String host;
    private final String uPass = "3395603"; 

    private DBConnection() {
        try {
            Path filePath = Paths.get("C:\\Users\\Public\\Documents\\Data.txt");
            if (!Files.exists(filePath)) {
                throw new IOException("ملف Data.txt غير موجود.");
            }
            
            List<String> lines = Files.readAllLines(filePath);
            if (lines.isEmpty()) {
                throw new IOException("ملف Data.txt فارغ.");
            }
            
            String baseHost = lines.get(0).trim();
            
            // التعديل 1: نضع الـ Opener فقط في الـ URL ونترك كلمة السر للـ getConnection
            this.host = baseHost + ";jackcessOpener=com.mycompany.egyc_dutyfree5.CryptCodecOpener";
            
            System.out.println("Connecting to Secured DB: " + baseHost);

            // التعديل 2: استخدام التعريف المحدث (بدون sf)
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            
            // التعديل 3: تمرير كلمة السر كباراميتر ثالث
            this.connection = DriverManager.getConnection(this.host, "", uPass);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "خطأ في الوصول للملف: " + e.getMessage());
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "خطأ في قاعدة البيانات: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (this.connection == null || this.connection.isClosed()) {
                // التعديل 4: إعادة الاتصال بكلمة السر أيضاً
                this.connection = DriverManager.getConnection(this.host, "", uPass);
                System.out.println("تم إعادة فتح الاتصال بنجاح.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this.connection;
    }

    public Statement createStatement() throws SQLException {
        Connection conn = getConnection();
        if (conn == null) throw new SQLException("لا يمكن إنشاء اتصال.");
        return conn.createStatement();
    }

    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
                System.out.println("تم إغلاق الاتصال.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}