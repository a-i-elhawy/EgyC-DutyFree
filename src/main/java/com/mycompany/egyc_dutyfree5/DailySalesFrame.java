package com.mycompany.egyc_dutyfree5;

import java.awt.Color;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;
//import static database.MainFrame;

/**
 *
 * @author Ahmed
 */
public final class DailySalesFrame extends javax.swing.JFrame {

    /**
     * Creates new form JFrameDailySales
     */
    public DailySalesFrame() {
        initComponents();

        centerCell(EgyTable, SwingConstants.CENTER);
        centerheader(EgyTable, SwingConstants.CENTER);
        centerCell(ForTabel, SwingConstants.CENTER);
        centerheader(ForTabel, SwingConstants.CENTER);

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tab2 = new javax.swing.JTabbedPane();
        jScrollPane9 = new javax.swing.JScrollPane();
        EgyTable = new javax.swing.JTable();
        jScrollPane11 = new javax.swing.JScrollPane();
        ForTabel = new javax.swing.JTable();
        print = new javax.swing.JButton();
        export = new javax.swing.JButton();
        export1 = new javax.swing.JButton();

        setTitle("تفاصيل يومية محددة");
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(214, 203, 195));

        tab2.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N

        EgyTable.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        EgyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ض  1 % بالجنية", "ض  1 % بالدولار", "القيمة", "العدد", "اسم الصنف"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        EgyTable.setRowHeight(35);
        EgyTable.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(EgyTable);

        tab2.addTab("مشتريات المصرى", jScrollPane9);

        ForTabel.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        ForTabel.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ض  1 % بالجنية", "ض  1 % بالدولار", "القيمة", "العدد", "اسم الصنف"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ForTabel.setRowHeight(35);
        ForTabel.getTableHeader().setReorderingAllowed(false);
        jScrollPane11.setViewportView(ForTabel);

        tab2.addTab("مشتريات الأجنبى", jScrollPane11);

        print.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        print.setForeground(new java.awt.Color(0, 51, 255));
        print.setIcon(new javax.swing.ImageIcon(getClass().getResource("/print_30dp_000000_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        print.setToolTipText("طبـــاعة");
        print.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printActionPerformed(evt);
            }
        });

        export.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        export.setIcon(new javax.swing.ImageIcon(getClass().getResource("/file_save_24dp_78A75A_FILL0_wght400_GRAD0_opsz24.png"))); // NOI18N
        export.setToolTipText("حفظ كملف Excel");
        export.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportActionPerformed(evt);
            }
        });

        export1.setFont(new java.awt.Font("Traditional Arabic", 1, 16)); // NOI18N
        export1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/add_task.png"))); // NOI18N
        export1.setToolTipText("حفظ اليومية");
        export1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                export1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(export1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(export, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tab2, javax.swing.GroupLayout.PREFERRED_SIZE, 675, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tab2, javax.swing.GroupLayout.DEFAULT_SIZE, 351, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(export, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(print, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(export1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    public void centerCell(JTable table, int alignment) {

        DefaultTableCellRenderer centerR = new DefaultTableCellRenderer();
        centerR.setHorizontalAlignment(alignment);
        TableModel TableModel = table.getModel();
        for (int col = 0; col < TableModel.getColumnCount(); col++) {
            table.getColumnModel().getColumn(col).setCellRenderer(centerR);
            table.getColumnModel().getColumn(col).setHeaderRenderer(centerR);

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


    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed

        boolean printTable = false;

        try {

            if (tab2.getSelectedIndex() == 0) {
                printTable = EgyTable.print();

            } else if (tab2.getSelectedIndex() == 1) {
                printTable = ForTabel.print();
            }
            if (!printTable) {
                JOptionPane.showMessageDialog(null, "لم يتم إستكمال عملية الطباعة");
            }
        } catch (PrinterException ex) {
            Logger.getLogger(ReportsFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }

    }//GEN-LAST:event_printActionPerformed

    private void exportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportActionPerformed
        try {

            JFileChooser JFileChooser = new JFileChooser();
            JFileChooser.showSaveDialog(this);
            File saveFile = JFileChooser.getSelectedFile();
            if (saveFile != null) {
                saveFile = new File(saveFile.toString() + ".xlsx");
                FileOutputStream out;
                try (Workbook wb = new XSSFWorkbook()) {
                    Sheet sheet = wb.createSheet("تفاصيل يومية محددة");
                    Row rowcol = sheet.createRow(0);
                    if (tab2.getSelectedIndex() == 0) {

//                ColumnName
                        for (int i = 0; i < EgyTable.getColumnCount(); i++) {
                            Cell cell;
                            cell = rowcol.createCell(i);

                            cell.setCellValue(EgyTable.getColumnName(i));
                        }
//                row
                        for (int jj = 0; jj < EgyTable.getRowCount(); jj++) {
                            Row row = sheet.createRow(jj + 0);
//                        Row row = sheet.createRow(jj + 13);

//                    Column begain 
//                        k=1
                            for (int k = 0; k < EgyTable.getColumnCount(); k++) {
                                Cell cell = row.createCell(k);
                                if (EgyTable.getValueAt(jj, k) != null) {
                                    cell.setCellValue(EgyTable.getValueAt(jj, k).toString());

                                }

                            }

                        }
                    } else if (tab2.getSelectedIndex() == 1) {
//                    SaveAsExcel.setEnabled(true);
//                    i = 5

                        for (int i = 0; i < ForTabel.getColumnCount(); i++) {
                            Cell cell;
                            cell = rowcol.createCell(i);

                            cell.setCellValue(ForTabel.getColumnName(i));
                        }
//                row
                        for (int jj = 0; jj < ForTabel.getRowCount(); jj++) {
                            Row row = sheet.createRow(jj + 0);

//                    Column begain
                            for (int k = 0; k < ForTabel.getColumnCount(); k++) {
                                Cell cell = row.createCell(k);
                                if (ForTabel.getValueAt(jj, k) != null) {
                                    cell.setCellValue(ForTabel.getValueAt(jj, k).toString());

                                }

                            }

                        }

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
    }//GEN-LAST:event_exportActionPerformed

    private void export1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_export1ActionPerformed

        MainFrame.txt14.setText((EgyTable.getValueAt(0, 3)).toString());
        MainFrame.txt13.setText((EgyTable.getValueAt(0, 2)).toString());
        MainFrame.txt12.setText((EgyTable.getValueAt(1, 3)).toString());
        MainFrame.txt11.setText((EgyTable.getValueAt(1, 2)).toString());
        MainFrame.txt10.setText((EgyTable.getValueAt(2, 3)).toString());
        MainFrame.txt9.setText((EgyTable.getValueAt(2, 2)).toString());
        MainFrame.txt8.setText((EgyTable.getValueAt(3, 3)).toString());
        MainFrame.txt7.setText((EgyTable.getValueAt(3, 2)).toString());
        MainFrame.txt6.setText((EgyTable.getValueAt(4, 3)).toString());
        MainFrame.txt5.setText((EgyTable.getValueAt(4, 2)).toString());
        MainFrame.txt4.setText((EgyTable.getValueAt(5, 3)).toString());
        MainFrame.txt3.setText((EgyTable.getValueAt(5, 2)).toString());
        MainFrame.txt2.setText((EgyTable.getValueAt(6, 3)).toString());
        MainFrame.txt1.setText((EgyTable.getValueAt(6, 2)).toString());

        MainFrame.txt34.setText((ForTabel.getValueAt(0, 3)).toString());
        MainFrame.txt33.setText((ForTabel.getValueAt(0, 2)).toString());
        MainFrame.txt32.setText((ForTabel.getValueAt(1, 3)).toString());
        MainFrame.txt31.setText((ForTabel.getValueAt(1, 2)).toString());
        MainFrame.txt30.setText((ForTabel.getValueAt(2, 3)).toString());
        MainFrame.txt29.setText((ForTabel.getValueAt(2, 2)).toString());
        MainFrame.txt28.setText((ForTabel.getValueAt(3, 3)).toString());
        MainFrame.txt27.setText((ForTabel.getValueAt(3, 2)).toString());
        MainFrame.txt26.setText((ForTabel.getValueAt(4, 3)).toString());
        MainFrame.txt25.setText((ForTabel.getValueAt(4, 2)).toString());
        MainFrame.txt24.setText((ForTabel.getValueAt(5, 3)).toString());
        MainFrame.txt23.setText((ForTabel.getValueAt(5, 2)).toString());
        MainFrame.txt22.setText((ForTabel.getValueAt(6, 3)).toString());
        MainFrame.txt21.setText((ForTabel.getValueAt(6, 2)).toString());
        MainFrame.txt35.setText((ForTabel.getValueAt(1, 1)).toString());
        MainFrame.txt36.setText((ForTabel.getValueAt(1, 0)).toString());

        if (MainFrame.jPanelBills.isVisible()) {

     
           
        }
          this.setVisible(false);

    }//GEN-LAST:event_export1ActionPerformed

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
            java.util.logging.Logger.getLogger(DailySalesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DailySalesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DailySalesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DailySalesFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DailySalesFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JTable EgyTable;
    public static javax.swing.JTable ForTabel;
    private javax.swing.JButton export;
    private javax.swing.JButton export1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JButton print;
    private javax.swing.JTabbedPane tab2;
    // End of variables declaration//GEN-END:variables
}
