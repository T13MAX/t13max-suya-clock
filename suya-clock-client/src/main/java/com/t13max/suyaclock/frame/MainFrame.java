package com.t13max.suyaclock.frame;

import com.t13max.suyaclock.panel.MainPanel;
import com.t13max.suyaclock.panel.OperatePanel;
import com.t13max.suyaclock.panel.PlanListPanel;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

/**
 * @author t13max
 * @since 16:25 2025/2/28
 */
public class MainFrame extends JFrame {

    private final static int FRAME_WIDTH = 500;
    private final static int FRAME_HEIGHT = 300;
    private final static int RIGHT_WIDTH = 300;

    public final PlanListPanel planListPanel;
    public final OperatePanel operatePanel;

    public MainFrame() throws HeadlessException {

        super("Suya Clock");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        planListPanel = new PlanListPanel(this);
        operatePanel = new OperatePanel(this);
        // 将分割面板加入到主框架中
        this.add(new MainPanel(planListPanel, operatePanel));
        //禁止改变大小
        this.setResizable(false);
        //居中
        this.setLocationRelativeTo(null);
        // 显示窗口
        this.setVisible(true);
    }


}
