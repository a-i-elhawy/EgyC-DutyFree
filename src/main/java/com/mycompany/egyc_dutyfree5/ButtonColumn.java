/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.egyc_dutyfree5;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Ahmed
 */
public class ButtonColumn {

    // 1. كلاس العرض (Renderer) للأزرار (شفافة، في المنتصف، وبجانب بعضها)
    public static class TableActionCellRenderer extends JPanel implements TableCellRenderer {
        public JButton btnDelete = new JButton();
        public JButton btnDetails = new JButton();
//      public JButton btnEdit = new JButton();

        public TableActionCellRenderer() {
            // ترتيب الأزرار في منتصف الخلية (أفقياً وعمودياً) بمسافة صغيرة
            setLayout(new FlowLayout(FlowLayout.CENTER, 4, 0));
            
            // جعل اللوحة الرئيسية شفافة افتراضياً لكي تندمج مع الجدول
            setOpaque(false);

            // تحميل أيقونة الحذف
            java.net.URL deleteImgUrl = getClass().getResource("/delete_24dp_EA3323_FILL0_wght400_GRAD0_opsz24.png");
            if (deleteImgUrl != null) {
                btnDelete.setIcon(new ImageIcon(deleteImgUrl));
            } else {
                btnDelete.setText("❌"); 
            }

            // تحميل أيقونة التفاصيل
            java.net.URL editImgUrl = getClass().getResource("/visibility_30dp_48752C_FILL0_wght400_GRAD0_opsz24.png");
            if (editImgUrl != null) {
                btnDetails.setIcon(new ImageIcon(editImgUrl));
            } else {
                btnDetails.setText("👁️");
            }

            // تحديد حجم مناسب للأزرار
            Dimension buttonSize = new Dimension(28, 24);
            btnDelete.setPreferredSize(buttonSize);
            btnDetails.setPreferredSize(buttonSize);

            // --- جعل الأزرار شفافة تماماً بدون خلفية أو إطار ---
            btnDelete.setContentAreaFilled(false);
            btnDelete.setOpaque(false);
            btnDelete.setBorderPainted(false);

            btnDetails.setContentAreaFilled(false);
            btnDetails.setOpaque(false);
            btnDetails.setBorderPainted(false);
            // --------------------------------------------------

            // إزالة التركيز
            btnDelete.setFocusable(false);
            btnDetails.setFocusable(false);

            // تلميحات عند تمرير الماوس
            btnDelete.setToolTipText("حذف");
            btnDetails.setToolTipText("التفاصيل");

            add(btnDelete);
            add(btnDetails);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            // ضبط لون الخلفية والشفافية بناءً على ما إذا كان الصف محددًا أم لا
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setOpaque(true);
            } else {
                setOpaque(false);
            }
            return this;
        }
    }

    // 2. كلاس التحرير (Editor) لتفعيل الأزرار واستجابتها للضغط
    public static class TableActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private TableActionCellRenderer renderer = new TableActionCellRenderer();
        private JTable table;
        private Runnable refreshCallback;

        public TableActionCellEditor(JTable table, Runnable refreshCallback) {
            this.table = table;
            this.refreshCallback = refreshCallback;

            // حدث زر الحذف
            renderer.btnDelete.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        Object idObj = table.getValueAt(row, 17); // عمود الـ ID
                        int confirm = JOptionPane.showConfirmDialog(null, "هل أنت متأكد من الحذف؟", "تأكيد", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            deleteFromDatabase(idObj);
                            fireEditingStopped();
                            if (refreshCallback != null) {
                                refreshCallback.run(); // تحديث الجدول بعد الحذف
                            }
                        }
                    }
                }
            });

            // حدث زر التفاصيل
            renderer.btnDetails.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        Object idObj = table.getValueAt(row, 17);
                        JOptionPane.showMessageDialog(null, "عرض تفاصيل الصنف رقم: " + idObj);
                        fireEditingStopped();
                    }
                }
            });
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            renderer.setBackground(table.getSelectionBackground());
            renderer.setOpaque(true);
            return renderer;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        // دالة الحذف الفعلية من قاعدة البيانات
        private void deleteFromDatabase(Object id) {
            String query = "DELETE FROM Sales WHERE ID = ?"; 
            Connection con = DBConnection.getInstance().getConnection();
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setObject(1, id);
                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null, "تم الحذف بنجاح!");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "خطأ في الحذف: " + e.getMessage());
            }
        }
    }
}