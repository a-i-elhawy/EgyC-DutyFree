package com.mycompany.egyc_dutyfree5;

import static com.mycompany.egyc_dutyfree5.MainFrame.ComboB_SubBranch1;
import static com.mycompany.egyc_dutyfree5.MainFrame.DateFrom;
import static com.mycompany.egyc_dutyfree5.MainFrame.DateTo;
import static com.mycompany.egyc_dutyfree5.MainFrame.detectedBranch;
import static com.mycompany.egyc_dutyfree5.MainFrame.CigPriceTxt;
import static com.mycompany.egyc_dutyfree5.MainFrame.exchangePriceTxt;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.JOptionPane;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.JTableHeader;
import java.text.DecimalFormat;
import static com.mycompany.egyc_dutyfree5.MainFrame.table;
import static com.mycompany.egyc_dutyfree5.MainFrame.tablefor;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
//import static jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.header;

public class ReportsFrame extends javax.swing.JFrame {

    public ReportsFrame() {

        initComponents();

        AddRow.setVisible(false);
        deleteRow.setVisible(false);
        CountCol.setVisible(false);
        jSeparator2.setVisible(false);
        filDataJTable1(viewtable1);
        centerCell(viewtable1, SwingConstants.CENTER);
        centerheader(viewtable1, SwingConstants.CENTER);
    }

    double[] value = new double[15];
    double[] dollarprice = new double[15];
    double pri;
    int month, year;
    double totalValue, totalExemptions;
    JTable jt = new JTable();

    DecimalFormat df = new DecimalFormat("#.#");

   public void filDataJTable1(JTable jt) {
    int sum_col[] = new int[20];
    double sum_colD[] = new double[20];

    // 1. الحصول على تاريخ البداية والنهاية من الـ MainFrame
    String dateFromStr = MainFrame.DateFrom;
    String dateToStr = MainFrame.DateTo;

    // التحقق من حالة الاختيارات (المصري / الأجنبي)
    boolean isEgySelected = MainFrame.isEgyNationalitySelected;
    boolean isForSelected = MainFrame.isForNationalitySelected;

    // إذا لم يتم اختيار أي مربع إطلاقاً، نفترض عرض الاثنين معاً كالسابق
    if (!isEgySelected && !isForSelected) {
        isEgySelected = true;
        isForSelected = true;
    }

    // 2. بناء استعلام الـ SQL ديناميكياً بناءً على الجنسية المختارة
    String query = "";

    if (isEgySelected && isForSelected) {
        // إذا تم اختيار الاثنين معاً، يتم دمج الجدولين Table1 (المصري) و Table2 (الأجنبي)
        query = "SELECT "
                + "SUM(cigPrice), SUM(cigNum), "
                + "SUM(winPrice), SUM(winNum), "
                + "SUM(beerPrice), SUM(beerNum), "
                + "SUM(foodPrice), SUM(foodNum), "
                + "SUM(prfuPrice), SUM(prfuNum), "
                + "SUM(devicePrice), SUM(deviceNum), "
                + "SUM(gifPrice), SUM(gifNum) "
                + "FROM ("
                + "   SELECT cigPrice, cigNum, winPrice, winNum, beerPrice, beerNum, foodPrice, foodNum, prfuPrice, prfuNum, devicePrice, deviceNum, gifPrice, gifNum, addDate FROM Table1 "
                + "   UNION ALL "
                + "   SELECT cigPrice, cigNum, winPrice, winNum, beerPrice, beerNum, foodPrice, foodNum, prfuPrice, prfuNum, devicePrice, deviceNum, gifPrice, gifNum, addDate FROM Table2 "
                + ") CombinedTables WHERE addDate BETWEEN ? AND ?";
    } else if (isEgySelected && !isForSelected) {
        // إذا تم اختيار المصري فقط (Table1)
        query = "SELECT "
                + "SUM(cigPrice), SUM(cigNum), "
                + "SUM(winPrice), SUM(winNum), "
                + "SUM(beerPrice), SUM(beerNum), "
                + "SUM(foodPrice), SUM(foodNum), "
                + "SUM(prfuPrice), SUM(prfuNum), "
                + "SUM(devicePrice), SUM(deviceNum), "
                + "SUM(gifPrice), SUM(gifNum) "
                + "FROM Table1 WHERE addDate BETWEEN ? AND ?";
    } else if (!isEgySelected && isForSelected) {
        // إذا تم اختيار الأجنبي فقط (Table2)
        query = "SELECT "
                + "SUM(cigPrice), SUM(cigNum), "
                + "SUM(winPrice), SUM(winNum), "
                + "SUM(beerPrice), SUM(beerNum), "
                + "SUM(foodPrice), SUM(foodNum), "
                + "SUM(prfuPrice), SUM(prfuNum), "
                + "SUM(devicePrice), SUM(deviceNum), "
                + "SUM(gifPrice), SUM(gifNum) "
                + "FROM Table2 WHERE addDate BETWEEN ? AND ?";
    }

    Connection con = DBConnection.getInstance().getConnection();
    
    // تنفيذ الاستعلام فقط إذا تم تحديد جدول واحد على الأقل
    if (isEgySelected || isForSelected) {
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, dateFromStr);
            pstmt.setString(2, dateToStr);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // توزيع النتائج بالتسلسل الصحيح المطابق لجملة SELECT في الـ SQL
                    sum_colD[14] = rs.getDouble(1);
                    sum_col[15] = rs.getInt(2);    // سجائر (قيمة، كمية)
                    sum_colD[12] = rs.getDouble(3);
                    sum_col[13] = rs.getInt(4);    // خمور (قيمة، كمية)
                    sum_colD[10] = rs.getDouble(5);
                    sum_col[11] = rs.getInt(6);    // بيرة (قيمة، كمية)
                    sum_colD[8] = rs.getDouble(7);
                    sum_col[9] = rs.getInt(8);     // مأكولات (قيمة، كمية)
                    sum_colD[6] = rs.getDouble(9);
                    sum_col[7] = rs.getInt(10);    // عطور (قيمة، كمية)
                    sum_colD[4] = rs.getDouble(11);
                    sum_col[5] = rs.getInt(12);    // أجهزة (قيمة، كمية)
                    sum_colD[2] = rs.getDouble(13);
                    sum_col[3] = rs.getInt(14);    // هدايا (قيمة، كمية)
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "خطأ في استعلام قاعدة البيانات للتقرير : " + e.getMessage());
        }
    }

    double[] dollar = new double[15];
    for (int i = 0; i < value.length; i++) {
        switch (i) {
            case 0: dollar[0] = sum_colD[14]; break;
            case 1: dollar[1] = sum_colD[12]; break;
            case 2: dollar[2] = sum_colD[10]; break;
            case 3: dollar[3] = sum_colD[8]; break;
            case 4: dollar[4] = sum_colD[6]; break;
            case 5: dollar[5] = sum_colD[4]; break;
            case 6: dollar[6] = sum_colD[2]; break;
        }
        dollarprice[i] = dollar[i] * Double.parseDouble(exchangePriceTxt.getText());
        value[i] = dollarprice[i] * 80 / 100;
    }

    ItemTax Item = new ItemTax();

    Item.setName("سجائر");
    TaxesResult cigTaxes = Item.getTaxes("سجائر", value[0], sum_col[15]);

    Item.setName("مشروبات كحولية");
    TaxesResult wineTaxes = Item.getTaxes("مشروبات كحولية", value[1], sum_col[13]); 

    Item.setName("بيرة");
    TaxesResult beerTaxes = Item.getTaxes("بيرة", value[2], sum_col[11]);

    Item.setName("مأكولات");
    TaxesResult foodTaxes = Item.getTaxes("مأكولات", value[3], sum_col[9]);

    Item.setName("عطور");
    TaxesResult perfTaxes = Item.getTaxes("عطور", value[4], sum_col[7]);

    Item.setName("أجهزة");
    TaxesResult devicesTaxes = Item.getTaxes("أجهزة", value[5], sum_col[5]);

    Item.setName("هدايا");
    TaxesResult gifTaxes = Item.getTaxes("هدايا", value[6], sum_col[3]);

    String[] columns = new String[]{
        "الإجمالى ", "", "ض ق المضافة ", "", "ضريبة الجدول ", "", "ضريبة الوارد", "القيمة الجمركية", "المبيعات بالجنيه", "المبيعات بالدولار", "وصف الوحدة", "الواحدات المباعة", "الصنف", "م "};

    Object[][] data = new Object[][]{
        {df.format(cigTaxes.getTotal()), 0, 0, df.format(cigTaxes.getTableTax()), 0, df.format(cigTaxes.getImportTax()), 0, df.format(value[0]), df.format(dollarprice[0]), df.format(sum_colD[14]), "خرطوشة", sum_col[15], "السجائر", 1},
        {df.format(wineTaxes.getTotal()), df.format(wineTaxes.getAddValueTax()), 0, df.format(wineTaxes.getTableTax()), 0, df.format(wineTaxes.getImportTax()), 0, df.format(value[1]), df.format(dollarprice[1]), df.format(sum_colD[12]), "زجاجة", sum_col[13], "المشروبات كحولية", 2},
        {df.format(beerTaxes.getTotal()), df.format(beerTaxes.getAddValueTax()), 0, df.format(beerTaxes.getTableTax()), 0, df.format(beerTaxes.getImportTax()), 0, df.format(value[2]), df.format(dollarprice[2]), df.format(sum_colD[10]), "كرتونة", sum_col[11], "البيرة", 3},
        {df.format(foodTaxes.getTotal()), df.format(foodTaxes.getAddValueTax()), 0, df.format(foodTaxes.getTableTax()), 0, df.format(foodTaxes.getImportTax()), 0, df.format(value[3]), df.format(dollarprice[3]), df.format(sum_colD[8]), "وحدة", sum_col[9], "المأكولات", 4},
        {df.format(perfTaxes.getTotal()), df.format(perfTaxes.getAddValueTax()), 0, df.format(perfTaxes.getTableTax()), 0, df.format(perfTaxes.getImportTax()), 0, df.format(value[4]), df.format(dollarprice[4]), df.format(sum_colD[6]), "زجاجة", sum_col[7], "عطور", 5},
        {df.format(devicesTaxes.getTotal()), df.format(devicesTaxes.getAddValueTax()), 0, df.format(devicesTaxes.getTableTax()), 0, df.format(devicesTaxes.getImportTax()), 0, df.format(value[5]), df.format(dollarprice[5]), df.format(sum_colD[4]), "وحدة", sum_col[5], "الأجهزة", 6},
        {df.format(gifTaxes.getTotal()), df.format(gifTaxes.getAddValueTax()), 0, df.format(gifTaxes.getTableTax()), 0, df.format(gifTaxes.getImportTax()), 0, df.format(value[6]), df.format(dollarprice[6]), df.format(sum_colD[2]), "وحدة", sum_col[3], "هدايا", 7},
        {"\n"},
        {df.format(gifTaxes.getTotal() + devicesTaxes.getTotal() + perfTaxes.getTotal() + foodTaxes.getTotal() + beerTaxes.getTotal() + wineTaxes.getTotal() + cigTaxes.getTotal()), df.format(cigTaxes.getAddValueTax() + wineTaxes.getAddValueTax() + beerTaxes.getAddValueTax() + foodTaxes.getAddValueTax() + perfTaxes.getAddValueTax() + devicesTaxes.getAddValueTax() + gifTaxes.getAddValueTax()), 0,
            df.format(cigTaxes.getTableTax() + wineTaxes.getTableTax() + beerTaxes.getTableTax() + foodTaxes.getTableTax() + perfTaxes.getTableTax() + devicesTaxes.getTableTax() + gifTaxes.getTableTax()), 0, df.format(cigTaxes.getImportTax() + wineTaxes.getImportTax() + beerTaxes.getImportTax() + foodTaxes.getImportTax() + perfTaxes.getImportTax() + devicesTaxes.getImportTax() + gifTaxes.getImportTax()), 0,
            df.format(value[0] + value[1] + value[2] + value[3] + value[4] + value[5] + value[6]), df.format(dollarprice[0] + dollarprice[1] + dollarprice[2] + dollarprice[3] + dollarprice[4] + dollarprice[5] + dollarprice[6]), df.format(sum_colD[2] + sum_colD[4] + sum_colD[6] + sum_colD[8] + sum_colD[10] + sum_colD[12] + sum_colD[14]), "", sum_col[3] + sum_col[5] + sum_col[7] + sum_col[9] + sum_col[11] + sum_col[13] + sum_col[15], "الاجمالى", 8},};

    totalExemptions = Double.parseDouble(df.format(gifTaxes.getTotal() + devicesTaxes.getTotal() + perfTaxes.getTotal() + foodTaxes.getTotal() + beerTaxes.getTotal() + wineTaxes.getTotal() + cigTaxes.getTotal()));
    totalValue = Double.parseDouble(df.format(value[0] + value[1] + value[2] + value[3] + value[4] + value[5] + value[6]));

    DefaultTableModel model = new DefaultTableModel(data, columns);
    jt.setModel(model);
}
public void filDataJTable2(JTable jt) {
    double sum_col_EgyD[] = new double[20];
    int sum_col_Egy[] = new int[20];
    double sum_col_ForD[] = new double[20];
    int sum_col_For[] = new int[20];

    // 1. الحصول على تاريخ البداية والنهاية من الـ MainFrame
    String dateFromStr = MainFrame.DateFrom;
    String dateToStr = MainFrame.DateTo;

    // التحقق من حالة الاختيارات (المصري / الأجنبي)
    boolean isEgySelected = MainFrame.isEgyNationalitySelected;
    boolean isForSelected = MainFrame.isForNationalitySelected;

    // إذا لم يتم اختيار أي مربع إطلاقاً، نفترض عرض الاثنين معاً
    if (!isEgySelected && !isForSelected) {
        isEgySelected = true;
        isForSelected = true;
    }

    Connection con = DBConnection.getInstance().getConnection();

    // 2. استعلام للجدول الأول Table1 (المصري) - يتم تنفيذه فقط إذا كان المصري مختاراً
    if (isEgySelected) {
        String queryEgy = "SELECT SUM(cigPrice), SUM(cigNum), SUM(winPrice), SUM(winNum), SUM(beerPrice), SUM(beerNum), "
                + "SUM(foodPrice), SUM(foodNum), SUM(prfuPrice), SUM(prfuNum), SUM(devicePrice), SUM(deviceNum), "
                + "SUM(gifPrice), SUM(gifNum) FROM Table1 WHERE addDate BETWEEN ? AND ?";

        try (PreparedStatement pstmt = con.prepareStatement(queryEgy)) {
            pstmt.setString(1, dateFromStr);
            pstmt.setString(2, dateToStr);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    sum_col_EgyD[14] = rs.getDouble(1);
                    sum_col_Egy[15] = rs.getInt(2);    // سجائر
                    sum_col_EgyD[12] = rs.getDouble(3);
                    sum_col_Egy[13] = rs.getInt(4);    // خمور
                    sum_col_EgyD[10] = rs.getDouble(5);
                    sum_col_Egy[11] = rs.getInt(6);    // بيرة
                    sum_col_EgyD[8] = rs.getDouble(7);
                    sum_col_Egy[9] = rs.getInt(8);     // مأكولات
                    sum_col_EgyD[6] = rs.getDouble(9);
                    sum_col_Egy[7] = rs.getInt(10);    // عطور
                    sum_col_EgyD[4] = rs.getDouble(11);
                    sum_col_Egy[5] = rs.getInt(12);    // أجهزة
                    sum_col_EgyD[2] = rs.getDouble(13);
                    sum_col_Egy[3] = rs.getInt(14);    // هدايا
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "خطأ في بيانات جدول المصري Table1: " + e.getMessage());
        }
    }

    // 3. استعلام للجدول الثاني Table2 (الأجنبي) - يتم تنفيذه فقط إذا كان الأجنبي مختاراً
    if (isForSelected) {
        String queryFor = "SELECT SUM(cigPrice), SUM(cigNum), SUM(winPrice), SUM(winNum), SUM(beerPrice), SUM(beerNum), "
                + "SUM(foodPrice), SUM(foodNum), SUM(prfuPrice), SUM(prfuNum), SUM(devicePrice), SUM(deviceNum), "
                + "SUM(gifPrice), SUM(gifNum) FROM Table2 WHERE addDate BETWEEN ? AND ?";

        try (PreparedStatement pstmt = con.prepareStatement(queryFor)) {
            pstmt.setString(1, dateFromStr);
            pstmt.setString(2, dateToStr);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    sum_col_ForD[14] = rs.getDouble(1);
                    sum_col_For[15] = rs.getInt(2);    // سجائر
                    sum_col_ForD[12] = rs.getDouble(3);
                    sum_col_For[13] = rs.getInt(4);    // خمور
                    sum_col_ForD[10] = rs.getDouble(5);
                    sum_col_For[11] = rs.getInt(6);    // بيرة
                    sum_col_ForD[8] = rs.getDouble(7);
                    sum_col_For[9] = rs.getInt(8);     // مأكولات
                    sum_col_ForD[6] = rs.getDouble(9);
                    sum_col_For[7] = rs.getInt(10);    // عطور
                    sum_col_ForD[4] = rs.getDouble(11);
                    sum_col_For[5] = rs.getInt(12);    // أجهزة
                    sum_col_ForD[2] = rs.getDouble(13);
                    sum_col_ForD[3] = rs.getInt(14);   // هدايا
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "خطأ في بيانات جدول الأجنبي Table2: " + e.getMessage());
        }
    }

    JTableHeader header = jt.getTableHeader();
    header.setForeground(Color.BLUE);
    header.setFont(new Font("Traditional Arabic", Font.BOLD, 14));

    double[] dollar = new double[15];
    for (int i = 0; i < value.length; i++) {
        switch (i) {
            case 0: dollar[0] = sum_col_EgyD[14]; break;
            case 1: dollar[1] = sum_col_EgyD[12]; break;
            case 2: dollar[2] = sum_col_EgyD[10]; break;
            case 3: dollar[3] = sum_col_EgyD[8]; break;
            case 4: dollar[4] = sum_col_EgyD[6]; break;
            case 5: dollar[5] = sum_col_EgyD[4]; break;
            case 6: dollar[6] = sum_col_EgyD[2]; break;
            case 7: dollar[7] = sum_col_ForD[14]; break;
            case 8: dollar[8] = sum_col_ForD[12]; break;
            case 9: dollar[9] = sum_col_ForD[10]; break;
            case 10: dollar[10] = sum_col_ForD[8]; break;
            case 11: dollar[11] = sum_col_ForD[6]; break;
            case 12: dollar[12] = sum_col_ForD[4]; break;
            case 13: dollar[13] = sum_col_ForD[2]; break;
        }
        dollarprice[i] = dollar[i] * Double.parseDouble(exchangePriceTxt.getText());
        value[i] = dollarprice[i] * 80 / 100;
    }

    ItemTax Item = new ItemTax();

    Item.setName("سجائر");
    TaxesResult E_cigTaxes = Item.getTaxes("سجائر", value[0], sum_col_Egy[15]);
    TaxesResult F_cigTaxes = Item.getTaxes("سجائر", value[7], sum_col_For[15]);

    Item.setName("مشروبات كحولية");
    TaxesResult E_wineTaxes = Item.getTaxes("مشروبات كحولية", value[1], sum_col_Egy[13]);
    TaxesResult F_wineTaxes = Item.getTaxes("مشروبات كحولية", value[8], sum_col_For[13]);

    Item.setName("بيرة");
    TaxesResult E_beerTaxes = Item.getTaxes("بيرة", value[2], sum_col_Egy[11]);
    TaxesResult F_beerTaxes = Item.getTaxes("بيرة", value[9], sum_col_For[11]);

    Item.setName("مأكولات");
    TaxesResult E_foodTaxes = Item.getTaxes("مأكولات", value[3], sum_col_Egy[9]);
    TaxesResult F_foodTaxes = Item.getTaxes("مأكولات", value[10], sum_col_For[9]);

    Item.setName("عطور");
    TaxesResult E_perfTaxes = Item.getTaxes("عطور", value[4], sum_col_Egy[7]);
    TaxesResult F_perfTaxes = Item.getTaxes("عطور", value[11], sum_col_For[7]);

    Item.setName("أجهزة");
    TaxesResult E_devicesTaxes = Item.getTaxes("أجهزة", value[5], sum_col_Egy[5]);
    TaxesResult F_devicesTaxes = Item.getTaxes("أجهزة", value[12], sum_col_For[5]);

    Item.setName("هدايا");
    TaxesResult E_gifTaxes = Item.getTaxes("هدايا", value[6], sum_col_Egy[3]);
    TaxesResult F_gifTaxes = Item.getTaxes("هدايا", value[13], sum_col_For[3]);

    String[] columns = new String[]{"", "", "", "", "الاجمالى", "", "", "", "", "الأجنبى", "", "", "", "", "المصرى", "الصنف", "م "};

    Object[][] data = new Object[][]{
        {"الإجمالى ", "ق المضافة ", "ض الجدول ", "ض الوارد", "ق الجمركية", "الإجمالى ", "ق المضافة ", "ض الجدول ", "ض الوارد", "ق الجمركية", "الإجمالى ", "ق المضافة ", "ض الجدول ", "ض الوارد", "ق الجمركية", "", ""},
        {df.format(F_cigTaxes.getTotal() + E_cigTaxes.getTotal()), 0, df.format(F_cigTaxes.getTableTax() + E_cigTaxes.getTableTax()), df.format(F_cigTaxes.getImportTax() + E_cigTaxes.getImportTax()), df.format(value[0] + value[7]), df.format(F_cigTaxes.getTotal()), 0, df.format(F_cigTaxes.getTableTax()), df.format(F_cigTaxes.getImportTax()), df.format(value[7]), df.format(E_cigTaxes.getTotal()), 0, df.format(E_cigTaxes.getTableTax()), df.format(E_cigTaxes.getImportTax()), df.format(value[0]), "السجائر", 1},
        {df.format(E_wineTaxes.getTotal() + F_wineTaxes.getTotal()), df.format(E_wineTaxes.getAddValueTax() + F_wineTaxes.getAddValueTax()), df.format(E_wineTaxes.getTableTax() + F_wineTaxes.getTableTax()), df.format(E_wineTaxes.getImportTax() + F_wineTaxes.getImportTax()), df.format(value[1] + value[8]), df.format(F_wineTaxes.getTotal()), df.format(F_wineTaxes.getAddValueTax()), df.format(F_wineTaxes.getTableTax()), df.format(F_wineTaxes.getImportTax()), df.format(value[8]), df.format(E_wineTaxes.getTotal()), df.format(E_wineTaxes.getAddValueTax()), df.format(E_wineTaxes.getTableTax()), df.format(E_wineTaxes.getImportTax()), df.format(value[1]), "المشروبات كحولية", 2},
        {df.format(E_beerTaxes.getTotal() + F_beerTaxes.getTotal()), df.format(E_beerTaxes.getAddValueTax() + F_beerTaxes.getAddValueTax()), df.format(E_beerTaxes.getTableTax() + F_beerTaxes.getTableTax()), df.format(E_beerTaxes.getImportTax() + F_beerTaxes.getImportTax()), df.format(value[2] + value[9]), df.format(F_beerTaxes.getTotal()), df.format(F_beerTaxes.getAddValueTax()), df.format(F_beerTaxes.getTableTax()), df.format(F_beerTaxes.getImportTax()), df.format(value[9]), df.format(E_beerTaxes.getTotal()), df.format(E_beerTaxes.getAddValueTax()), df.format(E_beerTaxes.getTableTax()), df.format(E_beerTaxes.getImportTax()), df.format(value[2]), "البيرة", 3},
        {df.format(E_foodTaxes.getTotal() + F_foodTaxes.getTotal()), df.format(E_foodTaxes.getAddValueTax() + F_foodTaxes.getAddValueTax()), df.format(E_foodTaxes.getTableTax() + F_foodTaxes.getTableTax()), df.format(E_foodTaxes.getImportTax() + F_foodTaxes.getImportTax()), df.format(value[3] + value[10]), df.format(F_foodTaxes.getTotal()), df.format(F_foodTaxes.getAddValueTax()), df.format(F_foodTaxes.getTableTax()), df.format(F_foodTaxes.getImportTax()), df.format(value[10]), df.format(E_foodTaxes.getTotal()), df.format(E_foodTaxes.getAddValueTax()), df.format(E_foodTaxes.getTableTax()), df.format(E_foodTaxes.getImportTax()), df.format(value[3]), "المأكولات", 4},
        {df.format(E_perfTaxes.getTotal() + F_perfTaxes.getTotal()), df.format(E_perfTaxes.getAddValueTax() + F_perfTaxes.getAddValueTax()), df.format(E_perfTaxes.getTableTax() + F_perfTaxes.getTableTax()), df.format(E_perfTaxes.getImportTax() + F_perfTaxes.getImportTax()), df.format(value[4] + value[11]), df.format(F_perfTaxes.getTotal()), df.format(F_perfTaxes.getAddValueTax()), df.format(F_perfTaxes.getTableTax()), df.format(F_perfTaxes.getImportTax()), df.format(value[11]), df.format(E_perfTaxes.getTotal()), df.format(E_perfTaxes.getAddValueTax()), df.format(E_perfTaxes.getTableTax()), df.format(E_perfTaxes.getImportTax()), df.format(value[4]), "عطور", 5},
        {df.format(E_devicesTaxes.getTotal() + F_devicesTaxes.getTotal()), df.format(E_devicesTaxes.getAddValueTax() + F_devicesTaxes.getAddValueTax()), df.format(E_devicesTaxes.getTableTax() + F_devicesTaxes.getTableTax()), df.format(E_devicesTaxes.getImportTax() + F_devicesTaxes.getImportTax()), df.format(value[5] + value[12]), df.format(F_devicesTaxes.getTotal()), df.format(F_devicesTaxes.getAddValueTax()), df.format(F_devicesTaxes.getTableTax()), df.format(F_devicesTaxes.getImportTax()), df.format(value[12]), df.format(E_devicesTaxes.getTotal()), df.format(E_devicesTaxes.getAddValueTax()), df.format(E_devicesTaxes.getTableTax()), df.format(E_devicesTaxes.getImportTax()), df.format(value[5]), "الأجهزة", 6},
        {df.format(E_gifTaxes.getTotal() + F_gifTaxes.getTotal()), df.format(E_gifTaxes.getAddValueTax() + F_gifTaxes.getAddValueTax()), df.format(E_gifTaxes.getTableTax() + F_gifTaxes.getTableTax()), df.format(E_gifTaxes.getImportTax() + F_gifTaxes.getImportTax()), df.format(value[6] + value[13]), df.format(F_gifTaxes.getTotal()), df.format(F_gifTaxes.getAddValueTax()), df.format(F_gifTaxes.getTableTax()), df.format(F_gifTaxes.getImportTax()), df.format(value[13]), df.format(E_gifTaxes.getTotal()), df.format(E_gifTaxes.getAddValueTax()), df.format(E_gifTaxes.getTableTax()), df.format(E_gifTaxes.getImportTax()), df.format(value[6]), "هدايا", 7},
        {"\n"},
        {
            // 1. الإجمالي الكلي (المجموع العام)
            df.format(F_cigTaxes.getTotal() + E_cigTaxes.getTotal() + E_wineTaxes.getTotal() + F_wineTaxes.getTotal() + E_beerTaxes.getTotal() + F_beerTaxes.getTotal() + E_foodTaxes.getTotal() + F_foodTaxes.getTotal() + E_perfTaxes.getTotal() + F_perfTaxes.getTotal() + E_devicesTaxes.getTotal() + F_devicesTaxes.getTotal() + E_gifTaxes.getTotal() + F_gifTaxes.getTotal()),
            df.format(0 + E_wineTaxes.getAddValueTax() + F_wineTaxes.getAddValueTax() + E_beerTaxes.getAddValueTax() + F_beerTaxes.getAddValueTax() + E_foodTaxes.getAddValueTax() + F_foodTaxes.getAddValueTax() + E_perfTaxes.getAddValueTax() + F_perfTaxes.getAddValueTax() + E_devicesTaxes.getAddValueTax() + F_devicesTaxes.getAddValueTax() + E_gifTaxes.getAddValueTax() + F_gifTaxes.getAddValueTax()),
            df.format(F_cigTaxes.getTableTax() + E_cigTaxes.getTableTax() + E_wineTaxes.getTableTax() + F_wineTaxes.getTableTax() + E_beerTaxes.getTableTax() + F_beerTaxes.getTableTax() + E_foodTaxes.getTableTax() + F_foodTaxes.getTableTax() + E_perfTaxes.getTableTax() + F_perfTaxes.getTableTax() + E_devicesTaxes.getTableTax() + F_devicesTaxes.getTableTax() + E_gifTaxes.getTableTax() + F_gifTaxes.getTableTax()),
            df.format(F_cigTaxes.getImportTax() + E_cigTaxes.getImportTax() + E_wineTaxes.getImportTax() + F_wineTaxes.getImportTax() + E_beerTaxes.getImportTax() + F_beerTaxes.getImportTax() + E_foodTaxes.getImportTax() + F_foodTaxes.getImportTax() + E_perfTaxes.getImportTax() + F_perfTaxes.getImportTax() + E_devicesTaxes.getImportTax() + F_devicesTaxes.getImportTax() + E_gifTaxes.getImportTax() + F_gifTaxes.getImportTax()),
            df.format(value[0] + value[1] + value[2] + value[3] + value[4] + value[5] + value[6] + value[7] + value[8] + value[9] + value[10] + value[11] + value[12] + value[13]),
            
            // 2. إجمالي الأجنبي (Foreign) - تم تصحيح الخطأ هنا باستعمال F_cigTaxes بدلاً من E_cigTaxes
            df.format(F_cigTaxes.getTotal() + F_wineTaxes.getTotal() + F_beerTaxes.getTotal() + F_foodTaxes.getTotal() + F_perfTaxes.getTotal() + F_devicesTaxes.getTotal() + F_gifTaxes.getTotal()),
            df.format(0 + F_wineTaxes.getAddValueTax() + F_beerTaxes.getAddValueTax() + F_foodTaxes.getAddValueTax() + F_perfTaxes.getAddValueTax() + F_devicesTaxes.getAddValueTax() + F_gifTaxes.getAddValueTax()),
            df.format(F_cigTaxes.getTableTax() + F_wineTaxes.getTableTax() + F_beerTaxes.getTableTax() + F_foodTaxes.getTableTax() + F_perfTaxes.getTableTax() + F_devicesTaxes.getTableTax() + F_gifTaxes.getTableTax()),
            df.format(F_cigTaxes.getImportTax() + F_wineTaxes.getImportTax() + F_beerTaxes.getImportTax() + F_foodTaxes.getImportTax() + F_perfTaxes.getImportTax() + F_devicesTaxes.getImportTax() + F_gifTaxes.getImportTax()), // <-- التصحيح هنا
            df.format(value[7] + value[8] + value[9] + value[10] + value[11] + value[12] + value[13]),
            
            // 3. إجمالي المصري (Egyptian)
            df.format(E_cigTaxes.getTotal() + E_wineTaxes.getTotal() + E_beerTaxes.getTotal() + E_foodTaxes.getTotal() + E_perfTaxes.getTotal() + E_devicesTaxes.getTotal() + E_gifTaxes.getTotal()),
            df.format(0 + E_wineTaxes.getAddValueTax() + E_beerTaxes.getAddValueTax() + E_foodTaxes.getAddValueTax() + E_perfTaxes.getAddValueTax() + E_devicesTaxes.getAddValueTax() + E_gifTaxes.getAddValueTax()),
            df.format(E_cigTaxes.getTableTax() + E_wineTaxes.getTableTax() + E_beerTaxes.getTableTax() + E_foodTaxes.getTableTax() + E_perfTaxes.getTableTax() + E_devicesTaxes.getTableTax() + E_gifTaxes.getTableTax()),
            df.format(E_cigTaxes.getImportTax() + E_wineTaxes.getImportTax() + E_beerTaxes.getImportTax() + E_foodTaxes.getImportTax() + E_perfTaxes.getImportTax() + E_devicesTaxes.getImportTax() + E_gifTaxes.getImportTax()),
            df.format(value[0] + value[1] + value[2] + value[3] + value[4] + value[5] + value[6]), 
            "الاجمالى", 6
        }
    };

    DefaultTableModel model = new DefaultTableModel(data, columns);
    jt.setModel(model);
}
   public void filDataJTable3(JTable jt) {
    // 1. الحصول على تاريخ البداية والنهاية من الـ MainFrame
    String dateFromStr = MainFrame.DateFrom;
    String dateToStr = MainFrame.DateTo;
    String currentPeriod = dateFromStr + " to " + dateToStr;

    // استخراج القيمة النصية لاسم الفرع من الـ ComboBox
    String branchNameVal = "";
    if (ComboB_SubBranch1.getSelectedItem() != null) {
        branchNameVal = ComboB_SubBranch1.getSelectedItem().toString();
    }

    // التحقق من حالة الاختيارات (المصري / الأجنبي)
    boolean isEgySelected = MainFrame.isEgyNationalitySelected;
    boolean isForSelected = MainFrame.isForNationalitySelected;

    if (!isEgySelected && !isForSelected) {
        isEgySelected = true;
        isForSelected = true;
    }

    // تحديد القيم التي سيتم إرسالها بناءً على الجنسية المختارة
    double targetExemptions = totalExemptions;
    double targetValue = totalValue;

    // (ملاحظة: إذا كنت ترغب في فصل حساب totalExemptions لكل جدول، يمكنك تعديل المتغيرات هنا، لكننا سنطبق الشرط على طريقة الحفظ والتحديث)
    double customsExemptions = isEgySelected ? totalExemptions : 0.0; // مثال لتوجيه القيمة حسب الاختيار

    Connection con = DBConnection.getInstance().getConnection();

    try {
        // 2. التحقق مما إذا كانت البيانات لنفس الفترة واسم الفرع موجودة مسبقاً
        String checkQuery = "SELECT COUNT(*) FROM TotalExemptionsTable WHERE ExemptionsDate = ? AND branchName = ?";
        boolean recordExists = false;

        try (PreparedStatement pstmtCheck = con.prepareStatement(checkQuery)) {
            pstmtCheck.setString(1, currentPeriod);
            pstmtCheck.setString(2, branchNameVal);
            try (ResultSet rs = pstmtCheck.executeQuery()) {
                if (rs.next()) {
                    recordExists = rs.getInt(1) > 0;
                }
            }
        }

        if (recordExists) {
            // إذا كانت موجودة مسبقاً، قم بتحديثها (UPDATE)
            String updateQuery = "UPDATE TotalExemptionsTable SET "
                    + "TotalExemptions = ?, TotalValue = ?, "
                    + "AgreementsTotalExemptions = ?, AgreementsValue = ?, "
                    + "InvestTotalExemptions = ?, InvestValue = ?, "
                    + "CustomsTotalExemptions = ?, CustomsValue = ?, "
                    + "numberOfBills = ?, ItemName = ? "
                    + "WHERE ExemptionsDate = ? AND branchName = ?";

            try (PreparedStatement pstmtUpdate = con.prepareStatement(updateQuery)) {
                pstmtUpdate.setDouble(1, targetExemptions);
                pstmtUpdate.setDouble(2, targetValue);
                pstmtUpdate.setDouble(3, 0.0);
                pstmtUpdate.setDouble(4, 0.0);
                pstmtUpdate.setDouble(5, 0.0);
                pstmtUpdate.setDouble(6, 0.0);
                pstmtUpdate.setDouble(7, targetExemptions);
                pstmtUpdate.setDouble(8, targetValue);
                pstmtUpdate.setInt(9, 0);
                pstmtUpdate.setObject(10, "سلع متنوعة");
                pstmtUpdate.setString(11, currentPeriod);
                pstmtUpdate.setString(12, branchNameVal);

                pstmtUpdate.executeUpdate();
            }
        } else {
            // إذا لم تكن موجودة، قم بإضافتها كجديدة (INSERT)
            String insertQuery = "INSERT INTO TotalExemptionsTable "
                    + "(TotalExemptions, TotalValue, AgreementsTotalExemptions, AgreementsValue, "
                    + "InvestTotalExemptions, InvestValue, CustomsTotalExemptions, CustomsValue, "
                    + "numberOfBills, ItemName, ExemptionsDate, branchName) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            try (PreparedStatement pstmtInsert = con.prepareStatement(insertQuery)) {
                pstmtInsert.setDouble(1, targetExemptions);
                pstmtInsert.setDouble(2, targetValue);
                pstmtInsert.setDouble(3, 0.0);
                pstmtInsert.setDouble(4, 0.0);
                pstmtInsert.setDouble(5, 0.0);
                pstmtInsert.setDouble(6, 0.0);
                pstmtInsert.setDouble(7, targetExemptions);
                pstmtInsert.setDouble(8, targetValue);
                pstmtInsert.setInt(9, 0);
                pstmtInsert.setObject(10, 0);
                pstmtInsert.setString(11, currentPeriod);
                pstmtInsert.setString(12, branchNameVal);

                pstmtInsert.executeUpdate();
            }
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "خطأ أثناء حفظ أو تحديث البيانات في قاعدة البيانات: " + e.getMessage());
    }

    // 3. استعراض وعرض كافة البيانات من الجدول بشرط تطابق الفترة (ExemptionsDate)
    String selectQuery = "SELECT TotalExemptions, TotalValue, AgreementsTotalExemptions, AgreementsValue, "
            + "InvestTotalExemptions, InvestValue, CustomsTotalExemptions, CustomsValue, "
            + "numberOfBills, ItemName, branchName FROM TotalExemptionsTable WHERE ExemptionsDate = ?";

    java.util.List<Object[]> rowsList = new java.util.ArrayList<>();

    try (PreparedStatement pstmtSelect = con.prepareStatement(selectQuery)) {
        pstmtSelect.setString(1, currentPeriod);
        try (ResultSet rs = pstmtSelect.executeQuery()) {
            while (rs.next()) {
                Object[] rowData = new Object[]{
                    rs.getDouble(1), 
                    rs.getDouble(2), 
                    rs.getDouble(3), 
                    rs.getDouble(4), 
                    rs.getDouble(5), 
                    rs.getDouble(6), 
                    rs.getDouble(7), 
                    rs.getDouble(8), 
                    rs.getInt(9), 
                    rs.getObject(10), 
                    rs.getString(11) 
                };
                rowsList.add(rowData);
            }
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "خطأ أثناء استعلام البيانات من قاعدة البيانات: " + e.getMessage());
    }

    JTableHeader header = jt.getTableHeader();
    header.setForeground(Color.BLUE);
    header.setFont(new Font("Traditional Arabic", Font.BOLD, 14));

    String[] columns = new String[]{"", "", "", "", "", "", "", "", "", "", ""};

    Object[][] data = rowsList.toArray(new Object[0][0]);

    DefaultTableModel model = new DefaultTableModel(data, columns) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
    };

    jt.setModel(model);
}

   public void filDataJTable4(JTable jt) {
    // 1. الحصول على تاريخ البداية والنهاية من الـ MainFrame
    String dateFromStr = MainFrame.DateFrom;
    String dateToStr = MainFrame.DateTo;

    int totalBeerNum = 0;
    int totalWinNum = 0;
    double totalWinTaxP = 0.0;
    double totalWinTaxD = 0.0;
    Object branchName = "";

    // التحقق من حالة اختيار الأجنبي
    boolean isForSelected = MainFrame.isForNationalitySelected;
    // إذا لم يتم اختيار أي شيء نهائياً، نفترض العرض افتراضياً أو نجعله يعتمد على الاختيار
    boolean isEgySelected = MainFrame.isEgyNationalitySelected;
    if (!isEgySelected && !isForSelected) {
        isForSelected = true;
    }

    Connection con = DBConnection.getInstance().getConnection();

    // 2. التنفيذ فقط إذا كان الأجنبي مختاراً، وإلا ستظل القيم صفراً
    if (isForSelected) {
        String queryFor = "SELECT COALESCE(SUM(beerNum), 0), COALESCE(SUM(winNum), 0), COALESCE(SUM(winTaxP), 0), COALESCE(SUM(winTaxD), 0), MIN(branchName) FROM Table2 WHERE addDate BETWEEN ? AND ?";
        try (PreparedStatement pstmt = con.prepareStatement(queryFor)) {
            pstmt.setString(1, dateFromStr);
            pstmt.setString(2, dateToStr);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    totalBeerNum = rs.getInt(1);
                    totalWinNum = rs.getInt(2);
                    totalWinTaxP = rs.getDouble(3);
                    totalWinTaxD = rs.getDouble(4);
                    branchName = rs.getObject(5);
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "خطأ في بيانات جدول Table2 للتقرير الثالث: " + e.getMessage());
        }
    }

    // إجمالي الوحدات = مجموع البيرة + مجموع الخمور من الجدول الثاني فقط
    int finalTotalUnits = totalBeerNum + totalWinNum;

    JTableHeader header = jt.getTableHeader();
    header.setForeground(Color.BLUE);
    header.setFont(new Font("Traditional Arabic", Font.BOLD, 14));

    String[] columns = new String[]{"", "", "", ""};

    Object[][] data = new Object[][]{
        {finalTotalUnits, totalWinTaxP, totalWinTaxD, branchName},
    };

    DefaultTableModel model = new DefaultTableModel(data, columns) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return true;
        }
    };

    jt.setModel(model);
}
    public void centerCell(JTable table, int alignment) {

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(alignment);
        TableModel TableModel = table.getModel();
        for (int col = 0; col < TableModel.getColumnCount(); col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(centerR);

        }

    }

    public void centerheader(JTable table, int alignment) {

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(alignment);
        centerR.setForeground(Color.BLUE);
        centerR.setBackground(Color.LIGHT_GRAY);

        TableModel TableModel = table.getModel();
        for (int col = 0; col < TableModel.getColumnCount(); col++) {
            table.getColumnModel().getColumn(col).setHeaderRenderer(centerR);

        }
    }

    public void printDiagram(Component Component) {

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setJobName("Print Component");
        pj.setPrintable(new Printable() {
            @Override
            public int print(Graphics g, PageFormat pf, int pageNumber) throws PrinterException {
                if (pageNumber > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                Graphics2D g2 = (Graphics2D) g;
                g2.translate(pf.getImageableX(), pf.getImageableY());
                double sx = pf.getImageableWidth() / Component.getWidth();
                double sy = pf.getImageableHeight() / Component.getHeight() - 0.45;
//              double sy = pf.getImageableHeight() / jPanel6.getHeight()-0.1;
                g2.scale(sx, sy);
                Component.paint(g2);
                return Printable.PAGE_EXISTS;
            }
        });
        if (!pj.printDialog()) {
            return;
        }
        try {
            pj.print();
        } catch (PrinterException ex) {
            System.out.println(ex);
        }
    }

    public void getmonth() {

        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        month = localDate.getMonthValue();
        year = localDate.getYear();
        txtPeriod.setText("شهر " + String.valueOf(month) + " - " + String.valueOf(year));

//        txtyear.setText(String.valueOf(year));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel40 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtPeriod = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        txtMarketName = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        AddRow = new javax.swing.JButton();
        deleteRow = new javax.swing.JButton();
        CountCol = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        SaveAsExcel = new javax.swing.JButton();
        print = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        tab1 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jPanel12 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        priceLabel = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        viewtable1 = new javax.swing.JTable();
        jLabel48 = new javax.swing.JLabel();
        jLabel49 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        viewtable2 = new javax.swing.JTable();
        jLabel17 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jPanel25 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jPanel27 = new javax.swing.JPanel();
        jPanel28 = new javax.swing.JPanel();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        jLabel60 = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        jPanel29 = new javax.swing.JPanel();
        viewtable3 = new javax.swing.JTable();
        jLabel63 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jLabel65 = new javax.swing.JLabel();
        jLabel78 = new javax.swing.JLabel();
        jLabel79 = new javax.swing.JLabel();
        jLabel80 = new javax.swing.JLabel();
        jLabel81 = new javax.swing.JLabel();
        jLabel82 = new javax.swing.JLabel();
        jLabel83 = new javax.swing.JLabel();
        jLabel84 = new javax.swing.JLabel();
        jLabel87 = new javax.swing.JLabel();
        jLabel88 = new javax.swing.JLabel();
        jLabel89 = new javax.swing.JLabel();
        jLabel90 = new javax.swing.JLabel();
        jLabel91 = new javax.swing.JLabel();
        jLabel85 = new javax.swing.JLabel();
        jLabel86 = new javax.swing.JLabel();
        jPanel35 = new javax.swing.JPanel();
        jPanel36 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jPanel37 = new javax.swing.JPanel();
        jPanel38 = new javax.swing.JPanel();
        jLabel53 = new javax.swing.JLabel();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jLabel56 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jPanel39 = new javax.swing.JPanel();
        jLabel72 = new javax.swing.JLabel();
        jLabel73 = new javax.swing.JLabel();
        jLabel74 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        viewtable4 = new javax.swing.JTable();
        jLabel76 = new javax.swing.JLabel();
        jLabel77 = new javax.swing.JLabel();

        setTitle("التقرير");
        setBackground(new java.awt.Color(216, 212, 212));
        setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        setIconImages(null);

        jPanel1.setBackground(new java.awt.Color(216, 212, 212));

        jPanel40 = new RoundedPanel();
        jPanel40.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jPanel40.add(jLabel1);

        txtPeriod.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        txtPeriod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtPeriod.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel40.add(txtPeriod);

        jLabel41.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel41.setText("الفترة عن");
        jPanel40.add(jLabel41);

        txtMarketName.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        txtMarketName.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtMarketName.setPreferredSize(new java.awt.Dimension(100, 30));
        jPanel40.add(txtMarketName);

        jLabel40.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel40.setText("أسم السوق");
        jPanel40.add(jLabel40);

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator1.setMinimumSize(new java.awt.Dimension(10, 30));
        jSeparator1.setPreferredSize(new java.awt.Dimension(10, 30));
        jPanel40.add(jSeparator1);

        AddRow.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        AddRow.setForeground(new java.awt.Color(51, 153, 0));
        AddRow.setText("+");
        AddRow.setToolTipText("إضافة صف فارغ للجدول");
        AddRow.setFocusPainted(false);
        AddRow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        AddRow.setPreferredSize(new java.awt.Dimension(50, 28));
        AddRow.setRolloverEnabled(false);
        AddRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddRowActionPerformed(evt);
            }
        });
        jPanel40.add(AddRow);

        deleteRow.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        deleteRow.setForeground(new java.awt.Color(204, 0, 0));
        deleteRow.setText("-");
        deleteRow.setToolTipText("حذف صف من الجدول");
        deleteRow.setFocusPainted(false);
        deleteRow.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        deleteRow.setOpaque(true);
        deleteRow.setPreferredSize(new java.awt.Dimension(50, 28));
        deleteRow.setRolloverEnabled(false);
        deleteRow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteRowActionPerformed(evt);
            }
        });
        jPanel40.add(deleteRow);

        CountCol.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        CountCol.setForeground(new java.awt.Color(51, 102, 255));
        CountCol.setText("الإجمالى");
        CountCol.setToolTipText("عملية جمع أعمدة الجدول");
        CountCol.setFocusPainted(false);
        CountCol.setPreferredSize(new java.awt.Dimension(80, 30));
        CountCol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CountColActionPerformed(evt);
            }
        });
        jPanel40.add(CountCol);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator2.setPreferredSize(new java.awt.Dimension(10, 30));
        jPanel40.add(jSeparator2);

        SaveAsExcel.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        SaveAsExcel.setForeground(new java.awt.Color(0, 0, 204));
        SaveAsExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_save_24dp_78A75A_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        SaveAsExcel.setToolTipText("حفظ كملف (Excel) ");
        SaveAsExcel.setFocusPainted(false);
        SaveAsExcel.setPreferredSize(new java.awt.Dimension(80, 30));
        SaveAsExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveAsExcelActionPerformed(evt);
            }
        });
        jPanel40.add(SaveAsExcel);

        print.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/print_30dp_000000_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        print.setToolTipText("طباعة");
        print.setFocusPainted(false);
        print.setPreferredSize(new java.awt.Dimension(80, 30));
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });
        jPanel40.add(print);

        jPanel2= new RoundedPanel();

        tab1.setBackground(new java.awt.Color(255, 255, 255));
        tab1.setForeground(new java.awt.Color(0, 0, 153));
        tab1.setFont(new java.awt.Font("Traditional Arabic", 1, 18)); // NOI18N
        tab1.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tab1StateChanged(evt);
            }
        });

        jPanel11.setBackground(new java.awt.Color(216, 212, 212));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel12.setBackground(new java.awt.Color(255, 255, 255));
        jPanel12.setPreferredSize(new java.awt.Dimension(1250, 426));

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setPreferredSize(new java.awt.Dimension(1250, 84));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("مصلحة الجمارك ");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setText("بيان العائد  ");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("الإدارة المركزية لجمارك الركاب والأسواق الحرة ");

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("الإدارة العامة للأسواق الحرة");

        priceLabel.setFont(new java.awt.Font("Segoe UI", 1, 15)); // NOI18N
        priceLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, 278, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(priceLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel42, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel14.setBackground(new java.awt.Color(224, 224, 224));
        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel14.setPreferredSize(new java.awt.Dimension(1250, 350));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("ضريبة الوارد");
        jLabel5.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("ضريبة الجدول");
        jLabel10.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("ضريبة ق المضافة");
        jLabel15.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("الإجمالى");
        jLabel26.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("معفى");
        jLabel27.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel28.setText("محصل");
        jLabel28.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel29.setText("محصل");
        jLabel29.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel30.setText("معفى");
        jLabel30.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("محصل");
        jLabel31.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("معفى");
        jLabel32.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("القيمة الجمركية");
        jLabel33.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel33.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel34.setText("المبيعات بالجنيه");
        jLabel34.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel34.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("المبيعات بالدولار");
        jLabel35.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel35.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("وصف الوحدة");
        jLabel36.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("الوحدات المباعة");
        jLabel37.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel37.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("الصنف");
        jLabel38.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("م");
        jLabel39.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        viewtable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        viewtable1.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        viewtable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        viewtable1.setRowHeight(30);
        viewtable1.setShowGrid(true);

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel32, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel31, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel30, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel29, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                        .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel28, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel33, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel35, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel36, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel38, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, 87, Short.MAX_VALUE)
                .addGap(12, 12, 12))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel14Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(viewtable1, javax.swing.GroupLayout.DEFAULT_SIZE, 1230, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel27)
                            .addComponent(jLabel28, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel30)
                            .addComponent(jLabel29, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel32)
                            .addComponent(jLabel31, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jLabel26, javax.swing.GroupLayout.DEFAULT_SIZE, 52, Short.MAX_VALUE)
                    .addComponent(jLabel39, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel38, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel37, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel35, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel33, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(290, 290, 290))
            .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel14Layout.createSequentialGroup()
                    .addContainerGap(66, Short.MAX_VALUE)
                    .addComponent(viewtable1, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("رئيس اللجنة");

        jLabel49.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("مدير الإدارة");

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel12Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(824, 824, 824)
                .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, 1244, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel13, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel49, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jScrollPane3.setViewportView(jPanel12);

        jPanel11.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 0, 1270, 507));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addComponent(jPanel11, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tab1.addTab("بيان العائد", jPanel10);

        jPanel21.setBackground(new java.awt.Color(216, 212, 212));
        jPanel21.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setPreferredSize(new java.awt.Dimension(1250, 426));

        jPanel23.setBackground(new java.awt.Color(255, 255, 255));
        jPanel23.setPreferredSize(new java.awt.Dimension(1250, 84));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("مصلحة الجمارك ");

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel12.setText("بيان الإعفاءات");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("الإدارة المركزية لجمارك الركاب والأسواق الحرة ");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("الإدارة العامة للأسواق الحرة");

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel23Layout.createSequentialGroup()
                .addGap(254, 254, 254)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                    .addComponent(jLabel43, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel24.setBackground(new java.awt.Color(224, 224, 224));
        jPanel24.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        viewtable2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        viewtable2.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        viewtable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        viewtable2.setRowHeight(30);
        viewtable2.setShowGrid(true);

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("الأجانب");
        jLabel17.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("المصريين");
        jLabel16.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel18.setBackground(new java.awt.Color(204, 204, 204));
        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("الاجمالى");
        jLabel18.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("القيمة");
        jLabel19.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("الضرائب المعفاه");
        jLabel20.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("الضرائب المعفاه");
        jLabel21.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("القيمة");
        jLabel22.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel23.setBackground(new java.awt.Color(204, 204, 204));
        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("الضرائب المعفاه");
        jLabel23.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel24.setBackground(new java.awt.Color(204, 204, 204));
        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("القيمة");
        jLabel24.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("الصنف");
        jLabel25.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addComponent(jLabel23, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel24, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel18, javax.swing.GroupLayout.PREFERRED_SIZE, 362, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addComponent(jLabel21, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel24Layout.createSequentialGroup()
                                .addComponent(jLabel20, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 356, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(viewtable2, javax.swing.GroupLayout.PREFERRED_SIZE, 1235, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel16, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel19)
                            .addComponent(jLabel24)
                            .addComponent(jLabel23)
                            .addComponent(jLabel20)
                            .addComponent(jLabel22)))
                    .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewtable2, javax.swing.GroupLayout.PREFERRED_SIZE, 303, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        viewtable2.getAccessibleContext().setAccessibleDescription("");

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("رئيس اللجنة");

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("مدير الإدارة");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel22Layout.createSequentialGroup()
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(849, 849, 849)
                        .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel24, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel23, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(12, Short.MAX_VALUE))
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel46, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jScrollPane2.setViewportView(jPanel22);

        jPanel21.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 0, 1270, 507));

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addComponent(jPanel21, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tab1.addTab("بيان الإعفاءات", jPanel20);

        jPanel26.setBackground(new java.awt.Color(216, 212, 212));
        jPanel26.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setPreferredSize(new java.awt.Dimension(1250, 426));

        jPanel28.setBackground(new java.awt.Color(255, 255, 255));
        jPanel28.setPreferredSize(new java.awt.Dimension(1250, 84));

        jLabel58.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel58.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel58.setText("مصلحة الجمارك ");

        jLabel59.setFont(new java.awt.Font("Segoe UI", 1, 16)); // NOI18N
        jLabel59.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel59.setText("إجمالى الإعفاءات الممنوحة");

        jLabel60.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("الإدارة المركزية لجمارك الركاب والأسواق الحرة ");

        jLabel61.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel61.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel61.setText("الإدارة العامة للأسواق الحرة");

        jLabel62.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel28Layout.createSequentialGroup()
                .addGap(254, 254, 254)
                .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(186, 186, 186)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel58, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel61, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel60, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel62, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addComponent(jLabel58, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel60, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel61, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel62, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel59, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel29.setBackground(new java.awt.Color(224, 224, 224));
        jPanel29.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        viewtable3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        viewtable3.setFont(new java.awt.Font("Segoe UI", 1, 11)); // NOI18N
        viewtable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        viewtable3.setRowHeight(30);
        viewtable3.setShowGrid(true);

        jLabel63.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel63.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel63.setText("قانون الاستثمار");
        jLabel63.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel63.setPreferredSize(new java.awt.Dimension(220, 22));

        jLabel64.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel64.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel64.setText("قانون الجمارك ");
        jLabel64.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel64.setPreferredSize(new java.awt.Dimension(220, 22));

        jLabel65.setBackground(new java.awt.Color(204, 204, 204));
        jLabel65.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel65.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel65.setText("الاتفاقيات");
        jLabel65.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel65.setPreferredSize(new java.awt.Dimension(220, 22));

        jLabel78.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel78.setText("القيمة النهائية");
        jLabel78.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel78.setPreferredSize(new java.awt.Dimension(107, 22));

        jLabel79.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel79.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel79.setText("الرسوم المعفاه");
        jLabel79.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel79.setPreferredSize(new java.awt.Dimension(107, 22));

        jLabel80.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel80.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel80.setText("الرسوم المعفاة");
        jLabel80.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel80.setPreferredSize(new java.awt.Dimension(107, 22));

        jLabel81.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel81.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel81.setText("القيمة النهائية");
        jLabel81.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel81.setPreferredSize(new java.awt.Dimension(107, 22));

        jLabel82.setBackground(new java.awt.Color(204, 204, 204));
        jLabel82.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel82.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel82.setText("الرسوم المعفاه");
        jLabel82.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel82.setPreferredSize(new java.awt.Dimension(107, 22));

        jLabel83.setBackground(new java.awt.Color(204, 204, 204));
        jLabel83.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel83.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel83.setText("القيمة النهائية");
        jLabel83.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel83.setPreferredSize(new java.awt.Dimension(107, 22));

        jLabel84.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel84.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel84.setText("عدد الشهادات");
        jLabel84.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel84.setPreferredSize(new java.awt.Dimension(107, 50));

        jLabel87.setBackground(new java.awt.Color(204, 204, 204));
        jLabel87.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel87.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel87.setText("الاجمالي");
        jLabel87.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel87.setPreferredSize(new java.awt.Dimension(220, 22));

        jLabel88.setBackground(new java.awt.Color(204, 204, 204));
        jLabel88.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel88.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel88.setText("الرسوم");
        jLabel88.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel88.setPreferredSize(new java.awt.Dimension(107, 22));

        jLabel89.setBackground(new java.awt.Color(204, 204, 204));
        jLabel89.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel89.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel89.setText("القيمة ");
        jLabel89.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel89.setPreferredSize(new java.awt.Dimension(107, 22));

        jLabel90.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel90.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel90.setText("الجهة");
        jLabel90.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel90.setPreferredSize(new java.awt.Dimension(107, 50));

        jLabel91.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel91.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel91.setText("السلعة وطبيعتها");
        jLabel91.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel91.setPreferredSize(new java.awt.Dimension(107, 50));

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addComponent(viewtable3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel88, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel89, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel82, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel83, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel80, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel78, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel29Layout.createSequentialGroup()
                                .addComponent(jLabel79, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel81, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel84, javax.swing.GroupLayout.DEFAULT_SIZE, 106, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7))))
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel29Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel29Layout.createSequentialGroup()
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel63, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel64, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel65, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel87, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel88, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel80, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel78, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel83, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel82, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel79, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel81, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel89, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel84, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel90, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel91, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewtable3, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jLabel85.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel85.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel85.setText("رئيس اللجنة");

        jLabel86.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel86.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel86.setText("مدير الإدارة");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel27Layout.createSequentialGroup()
                        .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(849, 849, 849)
                        .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel28, javax.swing.GroupLayout.DEFAULT_SIZE, 1251, Short.MAX_VALUE))
                .addContainerGap(11, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel28, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addGroup(jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel85, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel86, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jScrollPane6.setViewportView(jPanel27);

        jPanel26.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 0, 1270, 507));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel26, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tab1.addTab("اجمالى الاعفاءات", jPanel25);

        jPanel36.setBackground(new java.awt.Color(216, 212, 212));
        jPanel36.setPreferredSize(new java.awt.Dimension(1266, 507));
        jPanel36.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel37.setBackground(new java.awt.Color(255, 255, 255));
        jPanel37.setPreferredSize(new java.awt.Dimension(1250, 426));

        jPanel38.setBackground(new java.awt.Color(255, 255, 255));
        jPanel38.setPreferredSize(new java.awt.Dimension(1250, 84));

        jLabel53.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("مصلحة الجمارك ");

        jLabel54.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel54.setText("بيان ضريبة 1% و  25% عن المشروبات الكحولية ");

        jLabel55.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel55.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel55.setText("الإدارة المركزية لجمارك الركاب والأسواق الحرة ");

        jLabel56.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel56.setText("الإدارة العامة للأسواق الحرة");

        jLabel57.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        javax.swing.GroupLayout jPanel38Layout = new javax.swing.GroupLayout(jPanel38);
        jPanel38.setLayout(jPanel38Layout);
        jPanel38Layout.setHorizontalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 698, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(189, 189, 189)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel56, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel55, javax.swing.GroupLayout.DEFAULT_SIZE, 266, Short.MAX_VALUE)
                    .addComponent(jLabel53, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel57, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0))
        );
        jPanel38Layout.setVerticalGroup(
            jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel38Layout.createSequentialGroup()
                .addComponent(jLabel53, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel55, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jLabel56, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addGroup(jPanel38Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel57, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel54, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel39.setBackground(new java.awt.Color(224, 224, 224));
        jPanel39.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel39.setPreferredSize(new java.awt.Dimension(1250, 373));

        jLabel72.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel72.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel72.setText("عدد اللترات");
        jLabel72.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel73.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel73.setText("المحصل بالجنيه");
        jLabel73.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jLabel73.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel74.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel74.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel74.setText("المحصل بالدولار");
        jLabel74.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        jLabel75.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel75.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel75.setText("أسم الفرع");
        jLabel75.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));

        viewtable4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        viewtable4.setFont(new java.awt.Font("Segoe UI", 1, 13)); // NOI18N
        viewtable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        viewtable4.setRowHeight(30);
        viewtable4.setShowGrid(true);

        javax.swing.GroupLayout jPanel39Layout = new javax.swing.GroupLayout(jPanel39);
        jPanel39.setLayout(jPanel39Layout);
        jPanel39Layout.setHorizontalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(viewtable4, javax.swing.GroupLayout.PREFERRED_SIZE, 1236, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel39Layout.createSequentialGroup()
                        .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel73, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel75, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel39Layout.setVerticalGroup(
            jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel39Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel72, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel73, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel39Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel74, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel75, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(viewtable4, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jLabel76.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel76.setText("رئيس اللجنة");

        jLabel77.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel77.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel77.setText("مدير الإدارة");

        javax.swing.GroupLayout jPanel37Layout = new javax.swing.GroupLayout(jPanel37);
        jPanel37.setLayout(jPanel37Layout);
        jPanel37Layout.setHorizontalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel37Layout.createSequentialGroup()
                        .addGap(0, 29, Short.MAX_VALUE)
                        .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(824, 824, 824)
                        .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(21, 21, 21))
                    .addGroup(jPanel37Layout.createSequentialGroup()
                        .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, 1238, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel37Layout.setVerticalGroup(
            jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel37Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel38, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel39, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel37Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel76, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel77, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jScrollPane5.setViewportView(jPanel37);

        jPanel36.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(3, 0, 1270, 507));

        javax.swing.GroupLayout jPanel35Layout = new javax.swing.GroupLayout(jPanel35);
        jPanel35.setLayout(jPanel35Layout);
        jPanel35Layout.setHorizontalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel35Layout.createSequentialGroup()
                .addComponent(jPanel36, javax.swing.GroupLayout.PREFERRED_SIZE, 1273, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel35Layout.setVerticalGroup(
            jPanel35Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel36, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tab1.addTab("بيان الضريبة", jPanel35);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tab1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tab1, javax.swing.GroupLayout.PREFERRED_SIZE, 481, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel40, javax.swing.GroupLayout.DEFAULT_SIZE, 1282, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel40, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void SaveAsExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveAsExcelActionPerformed

        try {

            JFileChooser JFileChooser = new JFileChooser();
            JFileChooser.showSaveDialog(this);
            File saveFile = JFileChooser.getSelectedFile();
            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");
                FileOutputStream out;
                try (Workbook wb = new XSSFWorkbook()) {
                    Sheet sheet = wb.createSheet("تقرير");
                    Row rowcol = sheet.createRow(0);
                    if (tab1.getSelectedIndex() == 0) {

//                ColumnName
                        for (int i = 0; i < viewtable1.getColumnCount(); i++) {
                            Cell cell;
                            cell = rowcol.createCell(i);

                            cell.setCellValue(viewtable1.getColumnName(i));
                        }
//                row
                        for (int jj = 0; jj < viewtable1.getRowCount(); jj++) {
                            Row row = sheet.createRow(jj + 0);
//                        Row row = sheet.createRow(jj + 13);

//                    Column begain 
//                        k=1
                            for (int k = 0; k < viewtable1.getColumnCount(); k++) {
                                Cell cell = row.createCell(k);
                                if (viewtable1.getValueAt(jj, k) != null) {
                                    cell.setCellValue(viewtable1.getValueAt(jj, k).toString());

                                }

                            }

                        }
                    } else if (tab1.getSelectedIndex() == 1) {
//                    SaveAsExcel.setEnabled(true);
//                    i = 5

                        for (int i = 0; i < viewtable2.getColumnCount(); i++) {
                            Cell cell;
                            cell = rowcol.createCell(i);

                            cell.setCellValue(viewtable2.getColumnName(i));
                        }
//                row
                        for (int jj = 0; jj < viewtable2.getRowCount(); jj++) {
                            Row row = sheet.createRow(jj + 0);

//                    Column begain
                            for (int k = 0; k < viewtable2.getColumnCount(); k++) {
                                Cell cell = row.createCell(k);
                                if (viewtable2.getValueAt(jj, k) != null) {
                                    cell.setCellValue(viewtable2.getValueAt(jj, k).toString());

                                }

                            }

                        }
                    } else if (tab1.getSelectedIndex() == 2) {
//                    SaveAsExcel.setEnabled(true);
//                    i = 5

                        for (int i = 0; i < viewtable3.getColumnCount(); i++) {
                            Cell cell;
                            cell = rowcol.createCell(i);

                            cell.setCellValue(viewtable3.getColumnName(i));
                        }
//                row
                        for (int jj = 0; jj < viewtable3.getRowCount(); jj++) {
                            Row row = sheet.createRow(jj + 0);

//                    Column begain
                            for (int k = 0; k < viewtable3.getColumnCount(); k++) {
                                Cell cell = row.createCell(k);
                                if (viewtable3.getValueAt(jj, k) != null) {
                                    cell.setCellValue(viewtable3.getValueAt(jj, k).toString());

                                }

                            }

                        }

                    } else {
                        SaveAsExcel.setEnabled(false);
                    }
                    out = new FileOutputStream(new File(saveFile.toString()));
                    wb.write(out);
                }
                out.close();
                JOptionPane.showMessageDialog(null, "تم الحفظ بنجاح.");
//                openFile(saveFile.toString());

            } else {
                JOptionPane.showMessageDialog(null, "لم يتم الحفظ");

            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(null, ex);
        } catch (IOException io) {

            JOptionPane.showMessageDialog(null, io);

        }


    }//GEN-LAST:event_SaveAsExcelActionPerformed

    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed

        boolean print;
        if (tab1.getSelectedIndex() == 0) {
            printDiagram(jPanel12);
        } else if (tab1.getSelectedIndex() == 1) {
            printDiagram(jPanel22);
        } else if (tab1.getSelectedIndex() == 2) {
            printDiagram(jPanel27);
        } else if (tab1.getSelectedIndex() == 3) {
            printDiagram(jPanel37);
        }
    }//GEN-LAST:event_printActionPerformed

    private void AddRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddRowActionPerformed

        // تحديد الجدول بناءً على التبويب النشط
        JTable targetTable = null;
        int selectedIndex = tab1.getSelectedIndex();

        if (selectedIndex == 2) {
            targetTable = viewtable3;
        } else if (selectedIndex == 3) {
            targetTable = viewtable4;
        }

        if (targetTable != null) {
            try {
                DefaultTableModel model = (DefaultTableModel) targetTable.getModel();

                int colCount = model.getColumnCount();

                // إنشاء مصفوفة فارغة بنفس عدد أعمدة الجدول النشط (مثلاً 11 عموداً)
                Object[] emptyRow = new Object[colCount];
                for (int i = 0; i < colCount; i++) {
                    emptyRow[i] = "";
                }

                // إضافة الصف الفارغ
                model.addRow(emptyRow);

                // تحديث الواجهة
                targetTable.revalidate();
                targetTable.repaint();

                // طباعة للتأكد في الـ Output أن الكود نُفذ
                System.out.println("Row Added to Tab " + selectedIndex + ". Total rows: " + model.getRowCount());

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }//GEN-LAST:event_AddRowActionPerformed

    private void deleteRowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteRowActionPerformed
        // تحديد الجدول بناءً على التبويب النشط
        JTable targetTable = null;
        int selectedIndex = tab1.getSelectedIndex();

        if (selectedIndex == 2) {
            targetTable = viewtable3;
        } else if (selectedIndex == 3) {
            targetTable = viewtable4;
        }

        if (targetTable != null) {
            DefaultTableModel model = (DefaultTableModel) targetTable.getModel();

            // الحصول على رقم الصف المحدد في الجدول النشط
            int selectedRow = targetTable.getSelectedRow();

            // التأكد من أن المستخدم قام بتحديد صف فعلاً
            if (selectedRow != -1) {
                model.removeRow(selectedRow);
            } else {
                // إذا لم يحدد صفاً، تظهر رسالة تنبيه
                javax.swing.JOptionPane.showMessageDialog(this, "يرجى تحديد صف لحذفه");
            }
        }
    }//GEN-LAST:event_deleteRowActionPerformed

    private void CountColActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CountColActionPerformed
// تحديد الجدول بناءً على التبويب النشط
        JTable targetTable = null;
        int selectedIndex = tab1.getSelectedIndex();

        if (selectedIndex == 2) {
            targetTable = viewtable3;
        } else if (selectedIndex == 3) {
            targetTable = viewtable4;
        }

        if (targetTable != null) {
            DefaultTableModel model = (DefaultTableModel) targetTable.getModel();

            int rowCount = model.getRowCount();
            int colCount = model.getColumnCount();

            // مصفوفة لتخزين المجموع لكل عمود
            double[] totals = new double[colCount];

            try {
                // الجدول الثالث يجمع أول 8 أعمدة فقط (0 إلى 7)، والجدول الرابع يجمع الكل ما عدا الأخيرة
                int limitCol = (selectedIndex == 2) ? Math.min(8, colCount) : colCount - 1;

                // 1. حساب مجموع كل عمود من الصفوف الموجودة حالياً
                for (int col = 0; col < limitCol; col++) {
                    double sum = 0;
                    for (int row = 0; row < rowCount; row++) {
                        Object value = model.getValueAt(row, col);
                        if (value != null && !value.toString().isEmpty()) {
                            // تحويل القيمة إلى Double للجمع
                            sum += Double.parseDouble(value.toString());
                        }
                    }
                    totals[col] = sum;
                }

                // 2. إضافة صف جديد يحتوي على نواتج الجمع
                Object[] rowData = new Object[colCount];

                for (int i = 0; i < colCount; i++) {
                    rowData[i] = ""; // تهيئة الخلايا بفراغ
                }

                for (int i = 0; i < limitCol; i++) {
                    rowData[i] = totals[i];
                }

                // تحديد موقع كلمة "الإجمالى" بناءً على الجدول النشط
                if (selectedIndex == 2) {
                    // للجدول الثالث: وضع كلمة "الإجمالى" في الخانة رقم 10 (أي Index 9)
                    if (colCount > 10) {
                        rowData[10] = "الإجمالى";
                    }
                } else if (selectedIndex == 3) {
                    // للجدول الرابع: وضع كلمة "الإجمالى" في الخانة رقم 4 (أي Index 3)
                    if (colCount > 3) {
                        rowData[3] = "الإجمالى";
                    }
                }

                model.addRow(rowData);

                // تنبيه بسيط
                System.out.println("تم إضافة صف الإجماليات بنجاح للتبويب رقم: " + selectedIndex);

            } catch (NumberFormatException e) {
                javax.swing.JOptionPane.showMessageDialog(this, "خطأ: تأكد أن جميع الخلايا تحتوي على أرقام فقط ليتم الجمع بشكل صحيح.");
            }

            targetTable.setSelectionMode(0);
        }
    }//GEN-LAST:event_CountColActionPerformed

    private void tab1StateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tab1StateChanged

        try {
            if (tab1.getSelectedIndex() == 0) {
                SaveAsExcel.setEnabled(true);
                AddRow.setVisible(false);
                deleteRow.setVisible(false);
                CountCol.setVisible(false);
                jSeparator2.setVisible(false);
                if (!txtMarketName.getText().equals("")) {
                    jLabel7.setText("بيان العائد عن " + txtPeriod.getText() + " - " + "فرع " + txtMarketName.getText());
                    jLabel42.setText("اللجنة الجمركية بفرع " + txtMarketName.getText());
                } else {
                    jLabel7.setText("بيان العائد عن " + DateFrom + " إلى " + DateTo + " - " + "فرع " + ComboB_SubBranch1.getSelectedItem().toString());
                    jLabel42.setText("اللجنة الجمركية بفرع " + ComboB_SubBranch1.getSelectedItem().toString());
                    //                            jLabel45.setText("بيان ضريبة 1% و  25% عن المشروبات الكحولية  "+ DateFrom + " إلى "+ DateTo);
                }
                filDataJTable1(viewtable1);
                centerCell(viewtable1, SwingConstants.CENTER);
                centerheader(viewtable1, SwingConstants.CENTER);

            } else if (tab1.getSelectedIndex() == 1) {
                SaveAsExcel.setEnabled(true);
                AddRow.setVisible(false);
                deleteRow.setVisible(false);
                CountCol.setVisible(false);
                jSeparator2.setVisible(false);
                if (!txtMarketName.getText().equals("")) {
                    jLabel43.setText("اللجنة الجمركية بفرع " + txtMarketName.getText());
                    jLabel12.setText("بيان الإعفاءات عن " + txtPeriod.getText() + " - " + "فرع " + txtMarketName.getText());
                } else {
                    jLabel43.setText("اللجنة الجمركية بفرع " + ComboB_SubBranch1.getSelectedItem().toString());
                    jLabel12.setText("بيان الإعفاءات عن " + DateFrom + " إلى " + DateTo + " - " + "فرع " + ComboB_SubBranch1.getSelectedItem().toString());

                }
                filDataJTable2(viewtable2);
                centerCell(viewtable2, SwingConstants.CENTER);
                centerheader(viewtable2, SwingConstants.CENTER);

            } else if (tab1.getSelectedIndex() == 2) {
                 AddRow.setVisible(true);
                deleteRow.setVisible(true);
                CountCol.setVisible(true);
                jSeparator2.setVisible(true);

                filDataJTable3(viewtable3);
                centerCell(viewtable3, SwingConstants.CENTER);
                centerheader(viewtable3, SwingConstants.CENTER);
                jLabel59.setText("إجمالى الإعفاءات الممنوحة من " + DateFrom + " إلى " + DateTo);

            } else if (tab1.getSelectedIndex() == 3) {

                AddRow.setVisible(true);
                deleteRow.setVisible(true);
                CountCol.setVisible(true);
                jSeparator2.setVisible(true);

                if (!txtMarketName.getText().equals("")) {
                    jLabel57.setText("اللجنة الجمركية بفرع " + txtMarketName.getText());
                    jLabel54.setText("بيان ضريبة 1% و  25% عن المشروبات الكحولية عن " + txtPeriod.getText() + " - " + "فرع " + txtMarketName.getText());
                } else {
                    jLabel57.setText("اللجنة الجمركية بفرع " + ComboB_SubBranch1.getSelectedItem().toString());
                    jLabel54.setText("بيان ضريبة 1% و  25% عن المشروبات الكحولية  " + DateFrom + " إلى " + DateTo + " - " + "فرع " + ComboB_SubBranch1.getSelectedItem().toString());
                }
                
                filDataJTable4(viewtable4);
                centerCell(viewtable4, SwingConstants.CENTER);
                centerheader(viewtable4, SwingConstants.CENTER);

            } else {

                SaveAsExcel.setEnabled(false);

            }
        } catch (Exception e) {
            tab1.setSelectedIndex(0);
            JOptionPane.showMessageDialog(null, e);

        }

    }//GEN-LAST:event_tab1StateChanged

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ReportsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ReportsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ReportsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ReportsFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ReportsFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AddRow;
    private javax.swing.JButton CountCol;
    private javax.swing.JButton SaveAsExcel;
    private javax.swing.JButton deleteRow;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel73;
    private javax.swing.JLabel jLabel74;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel83;
    private javax.swing.JLabel jLabel84;
    private javax.swing.JLabel jLabel85;
    private javax.swing.JLabel jLabel86;
    private javax.swing.JLabel jLabel87;
    private javax.swing.JLabel jLabel88;
    private javax.swing.JLabel jLabel89;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel90;
    private javax.swing.JLabel jLabel91;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel35;
    private javax.swing.JPanel jPanel36;
    private javax.swing.JPanel jPanel37;
    private javax.swing.JPanel jPanel38;
    private javax.swing.JPanel jPanel39;
    private javax.swing.JPanel jPanel40;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel priceLabel;
    private javax.swing.JButton print;
    private javax.swing.JTabbedPane tab1;
    private javax.swing.JTextField txtMarketName;
    public javax.swing.JTextField txtPeriod;
    public javax.swing.JTable viewtable1;
    public javax.swing.JTable viewtable2;
    public javax.swing.JTable viewtable3;
    public javax.swing.JTable viewtable4;
    // End of variables declaration//GEN-END:variables

    private void openFile(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
