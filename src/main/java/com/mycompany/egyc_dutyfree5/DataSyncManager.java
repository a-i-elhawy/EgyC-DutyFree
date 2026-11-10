package com.mycompany.egyc_dutyfree5;

import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ClearValuesRequest;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.JOptionPane;

public class DataSyncManager {

    public static void syncTableToGoogle(String accessTableName, String spreadsheetId, String googleSheetName) throws Exception {
        System.out.println("📤 بدء عملية الرفع الشامل (استبدال) للجدول: " + accessTableName);
        
        Sheets service = GoogleSheetsService.getSheetsService();
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "SELECT winTaxP, winTaxD, gifPrice, gifNum, devicePrice, deviceNum, "
                   + "prfuPrice, prfuNum, foodPrice, foodNum, beerPrice, beerNum, "
                   + "winPrice, winNum, cigPrice, cigNum, addDate, id, branchName, users, EditDate "
                   + "FROM " + accessTableName;

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<List<Object>> allData = new ArrayList<>();

        List<Object> headers = Arrays.asList(
            "winTaxP", "winTaxD", "gifPrice", "gifNum", "devicePrice", "deviceNum",
            "prfuPrice", "prfuNum", "foodPrice", "foodNum", "beerPrice", "beerNum",
            "winPrice", "winNum", "cigPrice", "cigNum", "addDate", "id", "branchName", "users", "EditDate"
        );
        allData.add(headers);

        int rowCount = 0;
        while (rs.next()) {
            List<Object> row = new ArrayList<>();
            for (int i = 1; i <= 16; i++) {
                Object val = rs.getObject(i);
                row.add(val == null ? 0 : val);
            }
            for (int i = 17; i <= 21; i++) {
                Object val = rs.getObject(i);
                row.add(val == null ? "" : val.toString());
            }
            allData.add(row);
            rowCount++;
        }

        if (allData.size() > 1) {
            String range = googleSheetName + "!A1";
            service.spreadsheets().values()
                    .clear(spreadsheetId, googleSheetName + "!A1:U", new ClearValuesRequest())
                    .execute();
            
            ValueRange body = new ValueRange().setValues(allData);
            service.spreadsheets().values()
                    .update(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute();

            System.out.println("✅ تم الرفع الاستبدالي لـ " + rowCount + " صف بنجاح.");
        } else {
            System.out.println("⚠️ الجدول المحلي فارغ، لم يتم رفع شيء.");
        }
    }

    public static void appendTableToGoogle(String accessTableName, String spreadsheetId, String googleSheetName) throws Exception {
        System.out.println("➕ بدء عملية الإضافة الذكية بالمفتاح المركب للجدول: " + accessTableName);
        
        Sheets service = GoogleSheetsService.getSheetsService();
        Connection conn = DBConnection.getInstance().getConnection();

        Set<String> existingKeys = new HashSet<>();
        try {
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, googleSheetName + "!A:U")
                    .execute();
            List<List<Object>> sheetRows = response.getValues();
            if (sheetRows != null && !sheetRows.isEmpty()) {
                for (int i = 1; i < sheetRows.size(); i++) {
                    List<Object> row = sheetRows.get(i);
                    if (row.size() > 18) {
                        String addDate = row.get(16) != null ? row.get(16).toString().trim() : "";
                        String id = row.get(17) != null ? row.get(17).toString().trim() : "";
                        String branchName = row.get(18) != null ? row.get(18).toString().trim() : "";
                        
                        if (!id.isEmpty()) {
                            String compositeKey = branchName + "_" + id + "_" + addDate;
                            existingKeys.add(compositeKey);
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("تنبيه: الشيت فارغ أو تعذر جلب البيانات السابقة: " + e.getMessage());
        }

        String sql = "SELECT winTaxP, winTaxD, gifPrice, gifNum, devicePrice, deviceNum, "
                   + "prfuPrice, prfuNum, foodPrice, foodNum, beerPrice, beerNum, "
                   + "winPrice, winNum, cigPrice, cigNum, addDate, id, branchName, users, EditDate "
                   + "FROM " + accessTableName;

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<List<Object>> newRows = new ArrayList<>();
        int newAddedCount = 0;

        while (rs.next()) {
            String addDate = rs.getObject(17) != null ? rs.getObject(17).toString().trim() : "";
            String id = rs.getObject(18) != null ? rs.getObject(18).toString().trim() : "";
            String branchName = rs.getObject(19) != null ? rs.getObject(19).toString().trim() : "";
            
            String currentKey = branchName + "_" + id + "_" + addDate;
            
            if (!id.isEmpty() && !existingKeys.contains(currentKey)) {
                List<Object> row = new ArrayList<>();
                for (int i = 1; i <= 16; i++) {
                    Object val = rs.getObject(i);
                    row.add(val == null ? 0 : val);
                }
                for (int i = 17; i <= 21; i++) {
                    Object val = rs.getObject(i);
                    row.add(val == null ? "" : val.toString());
                }
                newRows.add(row);
                newAddedCount++;
            }
        }

        if (!newRows.isEmpty()) {
            ValueRange existingData = service.spreadsheets().values()
                    .get(spreadsheetId, googleSheetName + "!A1:A1")
                    .execute();
            
            if (existingData.getValues() == null || existingData.getValues().isEmpty()) {
                List<Object> headers = Arrays.asList(
                    "winTaxP", "winTaxD", "gifPrice", "gifNum", "devicePrice", "deviceNum",
                    "prfuPrice", "prfuNum", "foodPrice", "foodNum", "beerPrice", "beerNum",
                    "winPrice", "winNum", "cigPrice", "cigNum", "addDate", "id", "branchName", "users", "EditDate"
                );
                newRows.add(0, headers);
            }

            ValueRange body = new ValueRange().setValues(newRows);
            AppendValuesResponse response = service.spreadsheets().values()
                    .append(spreadsheetId, googleSheetName + "!A1", body)
                    .setValueInputOption("RAW")
                    .setInsertDataOption("INSERT_ROWS")
                    .execute();

            System.out.println("✅ " + newAddedCount + " new records appended .");
        } else {
            System.out.println("⚠️ no new records to append.");
        }
    }

    /**
     * دوال مساعدة آمنة لتحويل النصوص والمربعات الفارغة إلى أرقام آمنة لحقول أكسس
     */
    private static long safeParseLong(Object val) {
        if (val == null) return 0L;
        String str = val.toString().trim();
        if (str.isEmpty() || str.equalsIgnoreCase("N/A") || str.equals("-")) return 0L;
        try {
            str = str.replace(",", "");
            return (long) Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    private static double safeParseDouble(Object val) {
        if (val == null) return 0.0;
        String str = val.toString().trim();
        if (str.isEmpty() || str.equalsIgnoreCase("N/A") || str.equals("-")) return 0.0;
        try {
            str = str.replace(",", "");
            return Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    private static int safeParseInt(Object val) {
        if (val == null) return 0;
        String str = val.toString().trim();
        if (str.isEmpty() || str.equalsIgnoreCase("N/A") || str.equals("-")) return 0;
        try {
            str = str.replace(",", "");
            return (int) Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 3. دالة مزامنة وتحديث أصناف المخزن من جوجل شيت إلى Access (متوافقة تماماً مع حقول أكسس الرقمية)
     * A: OldMaterial (Long) | B: Material (Long) | C: EAN (Text) | D: Description (Text)
     * E: SalesPrice (Double) | F: FobPrice (Double) | G: Category (Text) | H: Quantity (Int) | I: AddDate (Text)
     */
    public static void syncItemsFromGoogleToAccess(String spreadsheetId, String googleSheetName) throws Exception {
        System.out.println("📥 بدء مزامنة وتحديث أصناف المخزن من جوجل شيت بالترتيب الجديد...");
        
        Sheets service = GoogleSheetsService.getSheetsService();
        
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, googleSheetName + "!A:I")
                .execute();
        
        List<List<Object>> sheetRows = response.getValues();
        if (sheetRows == null || sheetRows.size() <= 1) {
            System.out.println("⚠️ الشيت فارغ أو يحتوي على رأس الأعمدة فقط.");
            return;
        }

        Connection conn = DBConnection.getInstance().getConnection();
        
        String checkSql = "SELECT COUNT(*) FROM ItemsTable WHERE EAN = ? OR Material = ?";
        String updateSql = "UPDATE ItemsTable SET OldMaterial=?, Description=?, SalesPrice=?, FobPrice=?, Category=?, Quantity=?, AddDate=? WHERE EAN = ? OR Material = ?";
        String insertSql = "INSERT INTO ItemsTable (OldMaterial, Material, EAN, Description, SalesPrice, FobPrice, Category, Quantity, AddDate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        int updatedCount = 0;
        int insertedCount = 0;

        for (int i = 1; i < sheetRows.size(); i++) {
            List<Object> row = sheetRows.get(i);
            if (row == null || row.isEmpty()) continue;

            // تحويل قيم الحقول باستخدام الدوال الآمنة لملائمة أنواع البيانات في أكسس
            long oldMaterial = row.size() > 0 ? safeParseLong(row.get(0)) : 0L;
            long material = row.size() > 1 ? safeParseLong(row.get(1)) : 0L;
            String ean = row.size() > 2 && row.get(2) != null ? row.get(2).toString().trim() : "";
            String description = row.size() > 3 && row.get(3) != null ? row.get(3).toString().trim() : "";
            double salesPrice = row.size() > 4 ? safeParseDouble(row.get(4)) : 0.0;
            double fobPrice = row.size() > 5 ? safeParseDouble(row.get(5)) : 0.0;
            String category = row.size() > 6 && row.get(6) != null ? row.get(6).toString().trim() : "";
            int quantity = row.size() > 7 ? safeParseInt(row.get(7)) : 0;
            String addDate = row.size() > 8 && row.get(8) != null ? row.get(8).toString().trim() : "";

            if (ean.isEmpty() && material == 0L) continue;

            boolean exists = false;
            try (PreparedStatement checkPs = conn.prepareStatement(checkSql)) {
                checkPs.setString(1, ean);
                checkPs.setLong(2, material); // Material رقمي في قاعدة أكسس
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        exists = rs.getInt(1) > 0;
                    }
                }
            }

            if (exists) {
                try (PreparedStatement updatePs = conn.prepareStatement(updateSql)) {
                    updatePs.setLong(1, oldMaterial);
                    updatePs.setString(2, description);
                    updatePs.setDouble(3, salesPrice);
                    updatePs.setDouble(4, fobPrice);
                    updatePs.setString(5, category);
                    updatePs.setInt(6, quantity);
                    updatePs.setString(7, addDate);
                    updatePs.setString(8, ean);
                    updatePs.setLong(9, material); // Material رقمي في قاعدة أكسس
                    updatePs.executeUpdate();
                    updatedCount++;
                }
            } else {
                try (PreparedStatement insertPs = conn.prepareStatement(insertSql)) {
                    insertPs.setLong(1, oldMaterial);
                    insertPs.setLong(2, material); // Material رقمي في قاعدة أكسس
                    insertPs.setString(3, ean);
                    insertPs.setString(4, description);
                    insertPs.setDouble(5, salesPrice);
                    insertPs.setDouble(6, fobPrice);
                    insertPs.setString(7, category);
                    insertPs.setInt(8, quantity);
                    insertPs.setString(9, addDate);
                    insertPs.executeUpdate();
                    insertedCount++;
                }
            }
        }

        System.out.println("✅ تمت مزامنة الأصناف بنجاح: تم تحديث " + updatedCount + " صنف، وإضافة " + insertedCount + " صنف جديد.");
    }

    /**
     * 4. دالة رفع وتصدير أصناف المخزن من Access إلى جوجل شيت بالترتيب الجديد
     */
    public static void syncItemsToGoogle(String spreadsheetId, String googleSheetName) throws Exception {
        System.out.println("📤 بدء عملية رفع وتصدير أصناف المخزن إلى جوجل شيت بالترتيب الجديد...");
        
        Sheets service = GoogleSheetsService.getSheetsService();
        Connection conn = DBConnection.getInstance().getConnection();
        
        String sql = "SELECT OldMaterial, Material, EAN, Description, SalesPrice, FobPrice, Category, Quantity, AddDate FROM ItemsTable";

        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        List<List<Object>> allData = new ArrayList<>();

        List<Object> headers = Arrays.asList(
            "OldMaterial", "Material", "EAN", "Description", "SalesPrice", "FobPrice", "Category", "Quantity", "AddDate"
        );
        allData.add(headers);

        int rowCount = 0;
        while (rs.next()) {
            List<Object> row = new ArrayList<>();
            row.add(rs.getObject("OldMaterial") != null ? rs.getLong("OldMaterial") : 0);
            row.add(rs.getObject("Material") != null ? rs.getLong("Material") : 0);
            row.add(rs.getString("EAN") != null ? rs.getString("EAN") : "");
            row.add(rs.getString("Description") != null ? rs.getString("Description") : "");
            row.add(rs.getObject("SalesPrice") != null ? rs.getDouble("SalesPrice") : 0.0);
            row.add(rs.getObject("FobPrice") != null ? rs.getDouble("FobPrice") : 0.0);
            row.add(rs.getString("Category") != null ? rs.getString("Category") : "");
            row.add(rs.getObject("Quantity") != null ? rs.getInt("Quantity") : 0);
            row.add(rs.getString("AddDate") != null ? rs.getString("AddDate") : "");
            
            allData.add(row);
            rowCount++;
        }

        if (allData.size() > 1) {
            String range = googleSheetName + "!A1";
            service.spreadsheets().values()
                    .clear(spreadsheetId, googleSheetName + "!A1:I", new ClearValuesRequest())
                    .execute();
            
            ValueRange body = new ValueRange().setValues(allData);
            service.spreadsheets().values()
                    .update(spreadsheetId, range, body)
                    .setValueInputOption("RAW")
                    .execute();

            System.out.println("✅ تم رفع " + rowCount + " صنف إلى جوجل شيت بالترتيب الجديد بنجاح.");
        } else {
            System.out.println("⚠️ جدول الأصناف المحلي فارغ، لم يتم رفع شيء.");
        }
    }

    public static List<List<Object>> readSheetData(String spreadsheetId, String range) throws Exception {
        Sheets service = GoogleSheetsService.getSheetsService();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            return new ArrayList<>(); 
        }
        return new ArrayList<>(values); 
    }

    public static void testConnection(String spreadsheetId) {
        try {
            Sheets service = GoogleSheetsService.getSheetsService();
            var response = service.spreadsheets().get(spreadsheetId).execute();
            String title = response.getProperties().getTitle();
            JOptionPane.showMessageDialog(null, "نجح الاتصال!\nاسم الملف على جوجل: " + title, "حالة الاتصال", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "فشل الاتصال: " + e.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
        }
    }
}