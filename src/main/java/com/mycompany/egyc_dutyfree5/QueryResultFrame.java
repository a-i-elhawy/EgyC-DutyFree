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
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class QueryResultFrame extends javax.swing.JFrame {

    public QueryResultFrame() {
        initComponents();

        centerCell(tablecount2, SwingConstants.CENTER);
        centerheader(tablecount2, SwingConstants.CENTER);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane10 = new javax.swing.JScrollPane();
        tablecount2 = new javax.swing.JTable();
        print = new javax.swing.JButton();
        export = new javax.swing.JButton();

        setTitle("نتيجة الاستعلام");
        setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        setResizable(false);

        jPanel2.setBackground(new java.awt.Color(216, 212, 212));

        jPanel1 = new RoundedPanel();
        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jScrollPane10.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N

        tablecount2.setFont(new java.awt.Font("Traditional Arabic", 1, 14)); // NOI18N
        tablecount2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ملاحظات", "الصنف", "سعر الوحدة", "السعر", "الرصيد", "المباع", "الوارد", "التاريخ", "الكود"
            }
        ));
        tablecount2.setRowHeight(30);
        tablecount2.setShowGrid(true);
        jScrollPane10.setViewportView(tablecount2);
        if (tablecount2.getColumnModel().getColumnCount() > 0) {
            tablecount2.getColumnModel().getColumn(7).setPreferredWidth(120);
            tablecount2.getColumnModel().getColumn(8).setPreferredWidth(170);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 754, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane10, javax.swing.GroupLayout.DEFAULT_SIZE, 519, Short.MAX_VALUE)
                .addContainerGap())
        );

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

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(export, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(export, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(print, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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

    public void fillArray(int[][] list) {

        for (int i = 0; i < list.length; i++) {
            for (int j = 0; j < list.length; j++) {
                if (tablecount2.getValueAt(i, j) == tablecount2.getValueAt(2, j)) {
                    list[i][j] = (int) tablecount2.getValueAt(i, j);

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
                Sheet sheet = wb.createSheet("بيان عمليات صنف محدد");

                Row rowcol = sheet.createRow(0);
                for (int i = 0; i < tablecount2.getColumnCount(); i++) {
                    Cell cell;
                    cell = rowcol.createCell(i);

                    cell.setCellValue(tablecount2.getColumnName(i));
                }
                for (int jj = 0; jj < tablecount2.getRowCount(); jj++) {
                    Row row = sheet.createRow(jj + 1);

                    for (int k = 0; k < tablecount2.getColumnCount(); k++) {
                        Cell cell = row.createCell(k);
                        if (tablecount2.getValueAt(jj, k) != null) {
                            cell.setCellValue(tablecount2.getValueAt(jj, k).toString());

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

    private void printActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printActionPerformed

        boolean print;

        try {
            print = tablecount2.print();

            if (!print) {
                JOptionPane.showMessageDialog(null, "تم إلغاء أمر الطباعة.");
            }
        } catch (PrinterException ex) {
            Logger.getLogger(ReportsFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }


    }//GEN-LAST:event_printActionPerformed

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
            java.util.logging.Logger.getLogger(QueryResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QueryResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QueryResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QueryResultFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                new QueryResultFrame().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton export;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JButton print;
    public javax.swing.JTable tablecount2;
    // End of variables declaration//GEN-END:variables

    private void openFile(String toString) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
