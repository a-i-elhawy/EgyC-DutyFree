
package com.mycompany.egyc_dutyfree5;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.SwingConstants; // تأكد من استيراد هذه

public class AlternatingColorRenderer extends DefaultTableCellRenderer {
    
    // الألوان التي تريد استخدامها
    private static final Color EVEN_ROW_COLOR = Color.WHITE;
    // يمكن اختيار لون رمادي خفيف أو أزرق فاتح
    private static final Color ODD_ROW_COLOR = new Color(240, 240, 240); // رمادي خفيف
    
    // متغير لتخزين نوع المحاذاة (مثل SwingConstants.CENTER)
    private final int alignment;

    // مُنشئ الكلاس ليأخذ نوع المحاذاة كمدخل
    public AlternatingColorRenderer(int alignment) {
        this.alignment = alignment;
        setOpaque(true);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        
        // 1. استدعاء الدالة الأصلية لضبط المظهر الأساسي
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

        // 2. تطبيق المحاذاة على كل خلية
        setHorizontalAlignment(this.alignment);

        // 3. تطبيق التلوين المتناوب
        if (!isSelected) {
            // صف زوجي (0, 2, 4, ...)
            if (row % 2 == 0) {
                c.setBackground(EVEN_ROW_COLOR);
            } 
            // صف فردي (1, 3, 5, ...)
            else {
                c.setBackground(ODD_ROW_COLOR);
            }
        } 
        // عندما يكون الصف محددًا (مختارًا)، يُترك اللون الافتراضي للتحديد
        
        return c;
    }
}