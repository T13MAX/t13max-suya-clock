package com.t13max.suyaclock.panel;

import com.t13max.suyaclock.button.ClearButton;
import com.t13max.suyaclock.button.FinishedButton;
import com.t13max.suyaclock.button.HistoryButton;
import com.t13max.suyaclock.button.SaveTemplateButton;
import com.t13max.suyaclock.consts.Const;
import com.t13max.suyaclock.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author t13max
 * @since 16:43 2025/2/28
 */
public class ButtonPanel extends JPanel {

    public ButtonPanel(MainFrame mainFrame) {

        this.setLayout(new FlowLayout(FlowLayout.CENTER, Const.BUTTON_GAP, Const.BUTTON_GAP));
        this.setPreferredSize(new Dimension(Const.BUTTON_PANEL_WIDTH, Const.BUTTON_PANEL_HEIGHT));

        ClearButton clearButton = new ClearButton(mainFrame);

        JButton button2 = new JButton("嘻嘻");
        button2.setPreferredSize(new Dimension(Const.BUTTON_WIDTH, Const.BUTTON_HEIGHT));

        JButton finishedButton = new FinishedButton(mainFrame);

        JButton button4 = new HistoryButton(mainFrame);
        JButton saveTemplateButton = new SaveTemplateButton(mainFrame);

        JButton button5 = new JButton("嘿嘿");
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                SwingUtilities.invokeLater(() -> {
                    // 弹出一个提示框，显示 "XXX" 消息，并提供确认按钮
                    JOptionPane.showMessageDialog(null, "哈哈!!", "喵喵喵", JOptionPane.INFORMATION_MESSAGE);
                });
            }
        });
        this.add(clearButton);
        this.add(button2);
        this.add(finishedButton);
        this.add(button4);
        this.add(saveTemplateButton);
        this.add(button5);
    }
}
