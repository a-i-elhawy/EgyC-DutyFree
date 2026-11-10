package com.mycompany.egyc_dutyfree5;

import static com.mycompany.egyc_dutyfree5.MainFrame.CigPriceTxt;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;
import static com.mycompany.egyc_dutyfree5.MainFrame.Description;

public class ItemTax {

    private Connection con;
    String SQL4 = "SELECT * FROM taxTable";

    private String name;
    private double value, importTax, tableTax, addValueTax, developTax, totalOfTaxes, tableCig, unitCount;

    public ItemTax() {

        try {
            this.con = DBConnection.getInstance().getConnection();
            if (this.con == null) {
                JOptionPane.showMessageDialog(null, "خطأ: فشل الاتصال بقاعدة البيانات.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "خطأ في الحصول على الاتصال: " + e.getMessage());
        }
    }

    public void search_code() {

        if (con == null) {
            JOptionPane.showMessageDialog(null, "خطأ: لم يتم تعيين اتصال قاعدة البيانات في كلاس Item.");
            return; // Exit the method if no connection
        }

        try (Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); ResultSet rs = stmt.executeQuery(SQL4)) {

            int searchid = 2;

            int found = 0;
            rs.absolute(0);
            while (rs.next()) {
                if (searchid == rs.getInt("iD")) {

                    found = 1;
                    int id_col = rs.getInt("iD");
                    int importtax = rs.getInt("importTax");
                    int table = rs.getInt("tableTax");
                    int addvalue = rs.getInt("addValueTax");

                    System.out.println(id_col);
                    System.out.println(importtax);
                    System.out.println(table);
                    System.out.println("1" + addvalue);

                    break;
                }
            }
            if (found == 0) {

                JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void setName(String name) {

        this.name = name;

    }

    public void setValue(double Value) {

        this.value = Value;

    }

    public void setUniteCount(double unitCount) {

        this.unitCount = unitCount;

    }

    public TaxesResult getTaxes(String itemName, double itemValue, int itemCount) {

        if (con == null) {
            JOptionPane.showMessageDialog(null, "خطأ: لم يتم تعيين اتصال قاعدة البيانات فى Class Item.");

        }

        int id_col = 0;
       double importRate = 0;
        int tableRate = 0;
        int addvalueTRate = 0;
        int developRate = 0;
        int ClassTableTax1 = 0;
        int ClassTableTax2 = 0;
        int ClassTableTax3 = 0;
        int ClassTableTax4 = 0;
        int ClassTableTax5 = 0;
        int ClassTableTax6 = 0;

        try (Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); ResultSet rs = stmt.executeQuery(SQL4)) {

            String searchid = itemName;

            int found = 0;
            rs.absolute(0);
            while (rs.next()) {
                if (searchid.equals(rs.getString("Name"))) {
                    found = 1;

                    id_col = rs.getInt("iD");
                    importRate = rs.getDouble("importTax");
                    tableRate = rs.getInt("tableTax");
                    addvalueTRate = rs.getInt("addValueTax");
                    developRate = rs.getInt("developTax");
                    ClassTableTax1 = rs.getInt("ClassTableTax1");
                    ClassTableTax2 = rs.getInt("ClassTableTax2");
                    ClassTableTax3 = rs.getInt("ClassTableTax3");
                    ClassTableTax4 = rs.getInt("ClassTableTax4");
                    ClassTableTax5 = rs.getInt("ClassTableTax5");
                    ClassTableTax6 = rs.getInt("ClassTableTax6");

                    break;
                }
            }
            if (found == 0) {

                JOptionPane.showMessageDialog(null, "لايوجد نتائج مطابقة");
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        if ("سجائر".equals(this.name)) {

            this.importTax = itemCount * importRate;

            if (Double.parseDouble(CigPriceTxt.getText()) > 0 && Double.parseDouble(CigPriceTxt.getText()) <= ClassTableTax1) {

                tableRate = ClassTableTax4;

            } else if (Double.parseDouble(CigPriceTxt.getText()) > ClassTableTax1 && Double.parseDouble(CigPriceTxt.getText()) <= ClassTableTax2) {
                tableRate = ClassTableTax5;

            } else if (Double.parseDouble(CigPriceTxt.getText()) >= ClassTableTax3) {
                tableRate = ClassTableTax6;
            }

            this.tableTax = ((Double.parseDouble(CigPriceTxt.getText()) / 2) + tableRate) * itemCount;
            this.addValueTax = addvalueTRate;
            this.developTax = developRate;

        } else {
            this.importTax = itemValue * importRate / 100;

            if ("مشروبات كحولية".equals(this.name )||"بيرة".equals(this.name)) {
                this.tableTax = itemCount * ClassTableTax1;

         
            } else {
                this.tableTax = (itemValue + this.importTax) * tableRate / 100;

            }

            this.addValueTax = (itemValue + this.importTax + this.tableTax) * addvalueTRate / 100;
            this.developTax = (itemValue + this.importTax + this.tableTax + this.addValueTax) * developRate / 100;

        }
        this.totalOfTaxes = importTax + tableTax + addValueTax + developTax;

        double ImportTax = this.importTax;
        double TabelTax = this.tableTax;
        double AddValueTax = this.addValueTax;
        double developeTax = this.developTax;
        double total = this.totalOfTaxes;

        return new TaxesResult(ImportTax, TabelTax, AddValueTax, developeTax, total);
    }
}
