package com.t13max.suyaclock.util;

import com.t13max.suyaclock.entity.PlanEntity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author t13max
 * @since 16:42 2025/2/28
 */
public class ToastUtils {


    public static void showToast(PlanEntity planEntity) {
        // 创建一个新的JWindow，作为弹出的提示框
        JWindow toast = new JWindow();

        // 设置JWindow大小、背景色和透明度
        toast.setSize(250, 100);
        toast.getContentPane().setBackground(new Color(0, 0, 0, 150)); // 半透明黑色背景
        toast.setLayout(new BorderLayout());

        // 在JWindow中添加一个标签显示提示信息
        JLabel label = new JLabel(planEntity.getDesc() + " 时间到!", SwingConstants.CENTER);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        toast.add(label, BorderLayout.CENTER);

        // 获取屏幕的大小，计算右下角的位置
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = screenSize.width - toast.getWidth() - 20; // 距离屏幕右边20像素
        int y = screenSize.height - toast.getHeight() - 50; // 距离屏幕底部50像素
        toast.setLocation(x, y);

        // 添加鼠标点击事件，点击时关闭提示框
        toast.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toast.dispose(); // 关闭提示框
            }
        });

        // 显示提示框
        toast.setVisible(true);

        // 使用 Timer 设置延迟，几秒钟后自动关闭
        new Timer(30000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (toast.isVisible()) {
                    toast.dispose(); // 关闭提示框
                }
            }
        }).start();
    }

}
