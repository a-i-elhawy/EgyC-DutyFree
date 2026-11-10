/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.egyc_dutyfree5;

import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @author Ahmed
 */
public class InactivityManager {
    private static Timer inactivityTimer;
    private static final int TIMEOUT_MINUTES = 5; // مدة الخمول بالدقائق
    private static final int TIMEOUT_MILLIS = TIMEOUT_MINUTES * 60 * 1000; // التحويل إلى ميلي ثانية

    public static void startInactivityMonitor(java.awt.Window mainFrameWindow) {
        // إيقاف أي مؤقت سابق لمنع التداخل
        if (inactivityTimer != null) {
            inactivityTimer.stop();
        }

        // إنشاء المؤقت (عند انتهاء الـ 5 دقائق يتم تنفيذ دالة تسجيل الخروج)
        inactivityTimer = new Timer(TIMEOUT_MILLIS, e -> {
            triggerLogout(mainFrameWindow);
        });
        
        inactivityTimer.setRepeats(false); // تنفيذ مرة واحدة عند انتهاء الوقت
        inactivityTimer.start();

        // مراقبة حركة الماوس، والنقرات، ولوحة المفاتيح على مستوى التطبيق بالكامل
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {
            @Override
            public void eventDispatched(AWTEvent event) {
                // مع كل حركة أو تفاعل، يتم إعادة ضبط العد التنازلي للمؤقت من البداية
                if (inactivityTimer != null && inactivityTimer.isRunning()) {
                    inactivityTimer.restart();
                }
            }
        }, AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK | AWTEvent.KEY_EVENT_MASK);
    }

    private static void triggerLogout(java.awt.Window currentWindow) {
        // إيقاف المؤقت
        if (inactivityTimer != null) {
            inactivityTimer.stop();
        }

        // إظهار رسالة تنبيه للمستخدم
        JOptionPane.showMessageDialog(null, 
                "تم تسجيل الخروج تلقائياً.", 
                "انتهاء الجلسة", 
                JOptionPane.WARNING_MESSAGE);

        // التحقق مما إذا كانت النافذة الحالية هي MainFrame واستدعاء دالة الخروج فيها
        if (currentWindow instanceof MainFrame) {
            ((MainFrame) currentWindow).performLogout();
        } else if (currentWindow != null) {
            currentWindow.dispose(); // كاحتياطي إذا لم تكن النافذة هي MainFrame
        }
    }
}