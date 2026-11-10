/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.egyc_dutyfree5;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
/**
 *
 * @author Ahmed
 */
public class RoundedPanel extends JPanel {

    private int cornerRadius = 15; // يمكنك تعديل نصف قطر الزاوية حسب رغبتك
    private Color backgroundColor;

    public RoundedPanel(int radius, Color bgColor) {
        super();
        this.cornerRadius = radius;
        this.backgroundColor = bgColor;
        setOpaque(false); // مهم لجعل الخلفية شفافة حتى تظهر الحواف الدائرية
    }

    public RoundedPanel(Color bgColor) {
        this(25, bgColor); // نصف قطر افتراضي 15 إذا لم يتم تحديده
    }

    public RoundedPanel() {
        this(25, UIManager.getColor("[255,255,255]")); // يستخدم لون خلفية اللوحة الافتراضي
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // لتحسين جودة الرسم (تنعيم الحواف)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // قم بتعيين لون الخلفية
        if (backgroundColor != null) {
            g2.setColor(backgroundColor);
        } else {
            g2.setColor(getBackground()); // استخدم اللون المحدد للوحة أو اللون الافتراضي
        }

        // ارسم المستطيل ذو الحواف الدائرية
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        // ارسم الحدود إذا كانت اللوحة بها حدود
        if (getBorder() != null) {
            // قم بتعيين لون الحدود
            g2.setColor(getForeground()); // يمكنك تغيير هذا لتعيين لون مختلف للحدود
            g2.draw(new RoundRectangle2D.Double(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));
        }
    }
}