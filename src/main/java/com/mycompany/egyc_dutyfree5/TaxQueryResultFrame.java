package com.mycompany.egyc_dutyfree5;

//import static com.mycompany.egyc_dutyfree5.StartFrame.img;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class TaxQueryResultFrame extends javax.swing.JFrame {

    public TaxQueryResultFrame() {
        initComponents();
        sidePanelDetails.setVisible(false);
        

        centerCell(mainTaxTable, SwingConstants.CENTER);
        centerheader(mainTaxTable, SwingConstants.CENTER);
        centerCell(tableSmallDetails, SwingConstants.CENTER);
        centerheader(tableSmallDetails, SwingConstants.CENTER);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        mainTaxTable = new javax.swing.JTable();
        sidePanelDetails = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        tableSmallDetails = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        Total1 = new javax.swing.JLabel();
        export = new javax.swing.JButton();
        print1 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        Total = new javax.swing.JLabel();

        setTitle("نتيجة الاستعلام");
        setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(216, 212, 212));

        jPanel1 = new RoundedPanel();
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane1.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N

        mainTaxTable.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        mainTaxTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "المستخدم", "عدد اللترات", "المحصل بالجنيه", "المحصل بالدولار", "الوردية", "التاريخ", "أسم الفرع"
            }
        ));
        mainTaxTable.setRowHeight(30);
        mainTaxTable.setShowGrid(false);
        mainTaxTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mainTaxTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(mainTaxTable);
        if (mainTaxTable.getColumnModel().getColumnCount() > 0) {
            mainTaxTable.getColumnModel().getColumn(5).setPreferredWidth(140);
            mainTaxTable.getColumnModel().getColumn(6).setPreferredWidth(100);
        }

        jScrollPane11.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N

        tableSmallDetails.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        tableSmallDetails.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "عدد اللترات", " م بالجنيه", "م بالدولار", "الأسم", "رقم الفاتورة", "الوردية"
            }
        ));
        tableSmallDetails.setRowHeight(30);
        tableSmallDetails.setShowGrid(false);
        jScrollPane11.setViewportView(tableSmallDetails);

        javax.swing.GroupLayout sidePanelDetailsLayout = new javax.swing.GroupLayout(sidePanelDetails);
        sidePanelDetails.setLayout(sidePanelDetailsLayout);
        sidePanelDetailsLayout.setHorizontalGroup(
            sidePanelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 421, Short.MAX_VALUE)
        );
        sidePanelDetailsLayout.setVerticalGroup(
            sidePanelDetailsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 345, Short.MAX_VALUE)
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        Total1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Total1.setForeground(new java.awt.Color(0, 102, 255));
        Total1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(sidePanelDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 422, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Total1, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 635, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(10, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(Total1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12)
                        .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(sidePanelDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))))
                .addGap(12, 12, 12))
        );

        export.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        export.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_save_24dp_78A75A_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        export.setToolTipText("حفظ كملف Excel");
        export.setFocusPainted(false);
        export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportActionPerformed(evt);
            }
        });

        print1.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        print1.setForeground(new java.awt.Color(0, 51, 255));
        print1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/print_30dp_000000_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        print1.setToolTipText("طبـــاعة");
        print1.setFocusPainted(false);
        print1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                print1ActionPerformed(evt);
            }
        });

        jPanel3 = new RoundedPanel();
        jPanel3.setBackground(new java.awt.Color(255, 255, 255));

        Total.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        Total.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        Total.setText("الإجمالى : ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Total, javax.swing.GroupLayout.PREFERRED_SIZE, 565, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(Total, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(export, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(print1, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(print1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(export, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void centerCell(JTable table, int alignment) {

        // إنشاء كائن من المُصمم الجديد الذي يحتوي على التلوين والمحاذاة
        // ونمرر له نوع المحاذاة المطلوب (مثلاً SwingConstants.CENTER)
        AlternatingColorRenderer customRenderer = new AlternatingColorRenderer(alignment);

        // إذا كنت تريد محاذاة رؤوس الأعمدة، فاستخدم مُصمم بسيط منفصل
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setHorizontalAlignment(alignment);
        table.setDefaultRenderer(Object.class, customRenderer);

        // (اختياري) يمكنك أيضًا تطبيق المُصمم على أنواع محددة مثل String و Integer
        table.setDefaultRenderer(String.class, customRenderer);
        table.setDefaultRenderer(Integer.class, customRenderer);
        TableModel TableModel = table.getModel();

        for (int col = 0; col < TableModel.getColumnCount(); col++) {

            // تطبيق المُصمم الجديد على خلايا الجدول
            table.getColumnModel().getColumn(col).setCellRenderer(customRenderer);

            // (مهم!) تطبيق مُصمم رؤوس الأعمدة المنفصل (إذا كنت ترغب في محاذاتها)
            // أو يمكنك حذف هذا السطر إذا لم تكن بحاجة لمحاذاة رؤوس الأعمدة
            table.getColumnModel().getColumn(col).setHeaderRenderer(headerRenderer);
        }
        table.repaint();

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
    
    public static void animateWidthFromRight(Component component, int startWidth, int endWidth, int duration) {

        final int totalSteps = 30;
        final int delay = duration / totalSteps;
        final int deltaPerStep = (endWidth - startWidth) / totalSteps;

        // 1. تحديد الـ X الثابت من جهة اليمين بناءً على الموضع الحالي قبل بدء الأنميشن
        // هذه هي النقطة التي لن تتحرك أبداً.
        final int anchorRightX = component.getX() + component.getWidth();

        component.setVisible(true);
        Timer timer = new Timer(delay, null);

        timer.addActionListener(new ActionListener() {
            private int currentStep = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentStep >= totalSteps) {
                    timer.stop();
                    // التأكد من العرض النهائي وموضعه الصحيح
                    int finalX = anchorRightX - endWidth;
                    component.setBounds(finalX, component.getY(), endWidth, component.getHeight());
                    return;
                }

                // 2. حساب العرض المتغير
                int newWidth = startWidth + (deltaPerStep * currentStep);

                // 3. تحديث X ليكون دائماً (نقطة الارتكاز - العرض الحالي)
                // هذا يضمن أن الحافة اليمنى للمكون لا تتحرك من مكانها
                int newX = anchorRightX - newWidth;

                component.setBounds(newX, component.getY(), newWidth, component.getHeight());

                currentStep++;
            }
        });

        timer.start();
    }

    public void fillArray(int[][] list) {

        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list.length; j++) {
                if (mainTaxTable.getValueAt(i, j) == mainTaxTable.getValueAt(2, j)) {
                    list[i][j] = (int) mainTaxTable.getValueAt(i, j);

                }

            }

        }
    }

    private void exportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportActionPerformed
        try {

            JFileChooser JFileChooser = new JFileChooser();
            JFileChooser.showSaveDialog(this);
            File saveFile = JFileChooser.getSelectedFile();
            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");
                Workbook wb = new XSSFWorkbook();
                Sheet sheet = wb.createSheet("بيان الضريبة المحصلة");

                if (jScrollPane1.isVisible()&&jScrollPane11.isVisible()==false) {

                 Row rowcol = sheet.createRow(0);
                for (int i = 0; i < mainTaxTable.getColumnCount(); i++) {
                    Cell cell;
                    cell = rowcol.createCell(i);

                    cell.setCellValue(mainTaxTable.getColumnName(i));
                }
                for (int jj = 0; jj < mainTaxTable.getRowCount(); jj++) {
                    Row row = sheet.createRow(jj + 1);

                    for (int k = 0; k < mainTaxTable.getColumnCount(); k++) {
                        Cell cell = row.createCell(k);
                        if (mainTaxTable.getValueAt(jj, k) != null) {
                            cell.setCellValue(mainTaxTable.getValueAt(jj, k).toString());

                        }

                    }

                }

            } else if (jScrollPane11.isVisible()&&jScrollPane1.isVisible()) {
             Row rowcol = sheet.createRow(0);
                for (int i = 0; i < tableSmallDetails.getColumnCount(); i++) {
                    Cell cell;
                    cell = rowcol.createCell(i);

                    cell.setCellValue(tableSmallDetails.getColumnName(i));
                }
                for (int jj = 0; jj < tableSmallDetails.getRowCount(); jj++) {
                    Row row = sheet.createRow(jj + 1);

                    for (int k = 0; k < tableSmallDetails.getColumnCount(); k++) {
                        Cell cell = row.createCell(k);
                        if (tableSmallDetails.getValueAt(jj, k) != null) {
                            cell.setCellValue(tableSmallDetails.getValueAt(jj, k).toString());

                        }

                    }

                }
               
            }
               
                FileOutputStream out = new FileOutputStream(new File(saveFile.toString()));
                wb.write(out);
                wb.close();
                out.close();
                JOptionPane.showMessageDialog(null, "تم الحفظ بنجاح.");

//                openFile(saveFile.toString());
            } else {
                JOptionPane.showMessageDialog(null, "لم يتم الحفظ");

            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex);
            JOptionPane.showMessageDialog(null, ex);
        } catch (IOException io) {

            System.out.println(io);
            JOptionPane.showMessageDialog(null, io);

        }

    }//GEN-LAST:event_exportActionPerformed

    private void mainTaxTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mainTaxTableMouseClicked

// 1. إعداد موديل الجدول الجانبي
        DefaultTableModel model = (DefaultTableModel) tableSmallDetails.getModel();
        model.setRowCount(0);

// 2. الحصول على الصف المحدد
        int selectedRow = mainTaxTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null, "يرجى تحديد صف من الجدول.");
            return;
        }

// 3. استخراج القيم (العمود 5 للتاريخ والعمود 4 للشفت)
        String dateSearch = String.valueOf(mainTaxTable.getValueAt(selectedRow, 5));
        int shiftId = Integer.parseInt(mainTaxTable.getValueAt(selectedRow, 4).toString());

// 4. متغيرات الحسابات
        int totalBottles = 0, totalPound = 0, totalDollar = 0;
        String DateOfDetails = null;

// 5. الاستعلام
        String query = "SELECT * FROM [Table3] WHERE [addDate] = ? AND [shift] = ?";

        try (Connection conn = DBConnection.getInstance().getConnection(); PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, dateSearch);
            ps.setInt(2, shiftId);

            try (ResultSet rst = ps.executeQuery()) {
                boolean found = false;
                while (rst.next()) {
                    found = true;
                    int winNum = rst.getInt("output");
                    int winTaxP = rst.getInt("winTaxP");
                    int winTaxD = rst.getInt("winTaxD");
                    DateOfDetails = rst.getString("addDate");

                    model.addRow(new Object[]{
                        winNum, winTaxP, winTaxD,
                        rst.getString("name"),
                        rst.getInt("billNum"), 
                        rst.getInt("shift"), DateOfDetails
                    });

                    totalBottles += winNum;
                    totalPound += winTaxP;
                    totalDollar += winTaxD;
                }

                if (found) {
                    Total.setText("الإجمالى : " + totalDollar + " $" + " | " + totalPound + " ج" + " | " + totalBottles + " زجاجة");
                    Total1.setText("تفاصيل يوم "+ " " +DateOfDetails );
                    Total1.setForeground(Color.blue);

                    // --- بدء حركة التوسع من الوسط ---

                  animateWidthFromRight(sidePanelDetails,0,421,3);
                   

                } else {
                    sidePanelDetails.setVisible(false);
//                    JOptionPane.showMessageDialog(null, "لا يوجد نتائج مطابقة.");
                    Total1.setText("لا يوجد نتائج مطابقة.");
                    Total1.setForeground(Color.red);
                     Total.setText("الإجمالى : " + totalDollar + " $" + " | " + totalPound + " ج" + " | " + totalBottles + " زجاجة");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "حدث خطأ: " + e.getMessage());
        }

    }//GEN-LAST:event_mainTaxTableMouseClicked

    private void print1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_print1ActionPerformed

        try {
//    boolean printAttempted = false;
//    boolean printSuccess = false;
mainTaxTable.print();
    // الحالة الأولى: jScrollPane1 ظاهرة و jScrollPane11 مخفية
//    if (jScrollPane1.isVisible() && !jScrollPane11.isVisible()) {
//        printAttempted = true;
//        printSuccess = mainTaxTable.print(); // طباعة الجدول الرئيسي
//    } 
//    // الحالة الثانية: jScrollPane11 ظاهرة (سواء كانتScrollPane1 ظاهرة أو لا حسب رغبتك)
//    else if (jScrollPane11.isVisible()) {
//        printAttempted = true;
//        printSuccess = tableSmallDetails.print(); // طباعة الجدول الفرعي
//    }
//
//    // إظهار رسالة الإلغاء فقط إذا حاول الطباعة وقام المستخدم بالإلغاء (printSuccess == false)
//    if (printAttempted && !printSuccess) {
//        JOptionPane.showMessageDialog(null, "تم إلغاء أمر الطباعة.");
//    }

} catch (PrinterException ex) {
    Logger.getLogger(ReportsFrame.class.getName()).log(Level.SEVERE, null, ex);
    JOptionPane.showMessageDialog(null, "خطأ في الطباعة: " + ex.getMessage(), "خطأ", JOptionPane.ERROR_MESSAGE);
}

    }//GEN-LAST:event_print1ActionPerformed

    /**
     * @param args the command line arguments
     */
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
            java.util.logging.Logger.getLogger(TaxQueryResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TaxQueryResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TaxQueryResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TaxQueryResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new TaxQueryResultFrame().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JLabel Total;
    public static javax.swing.JLabel Total1;
    public static javax.swing.JButton export;
    public static javax.swing.JPanel jPanel1;
    public static javax.swing.JPanel jPanel2;
    public static javax.swing.JPanel jPanel3;
    public static javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JScrollPane jScrollPane11;
    public static javax.swing.JSeparator jSeparator1;
    public static javax.swing.JSeparator jSeparator2;
    public static javax.swing.JTable mainTaxTable;
    public static javax.swing.JButton print1;
    public static javax.swing.JPanel sidePanelDetails;
    public static javax.swing.JTable tableSmallDetails;
    // End of variables declaration//GEN-END:variables

    private void openFile(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
