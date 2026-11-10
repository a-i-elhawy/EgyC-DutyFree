/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.egyc_dutyfree5;

import javax.swing.JButton;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {

    public RoundedButton() {
    }
    


    public RoundedButton(String text) {
        super(text);
        // هذه الخطوة ضرورية لجعل الزر شفافًا حتى نتمكن من رسمه
        setContentAreaFilled(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        // إنشاء كائن Graphics2D للرسم المتقدم
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // تحديد اللون
        if (getModel().isArmed()) {
            g2.setColor(Color.lightGray); // لون عند الضغط
        } else if (getModel().isRollover()) {
            g2.setColor(Color.gray); // لون عند مرور الماوس
        } else {
            g2.setColor(Color.WHITE); // اللون الأساسي (أزرق داكن)
        }

        // رسم الشكل المستدير
        g2.fill(new RoundRectangle2D.Double(0, 0, getWidth()-1, getHeight()-1, 20, 20));
        
        // رسم النص فوق الشكل
        g2.setColor(Color.black); // لون النص
        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
    
    // إزالة الحدود الافتراضية
    @Override
    protected void paintBorder(Graphics g) {
        // لا تفعل شيئًا هنا لمنع رسم الحدود الافتراضية
    }

    // مثال للاستخدام
    public static void main(String[] args) {
        JFrame frame = new JFrame("Rounded Button Example");
        frame.setLayout(new FlowLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        RoundedButton myButton = new RoundedButton("زر مستدير");
        myButton.setPreferredSize(new Dimension(150, 40));
        myButton.setForeground(Color.white);

        frame.add(myButton);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}