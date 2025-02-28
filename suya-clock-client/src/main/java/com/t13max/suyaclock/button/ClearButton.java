package com.t13max.suyaclock.button;

import com.t13max.suyaclock.consts.Const;
import com.t13max.suyaclock.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author t13max
 * @since 16:46 2025/2/28
 */
public class ClearButton extends JButton {

    public ClearButton(MainFrame mainFrame) {
        super("清空0w0");
        this.setPreferredSize(new Dimension(Const.BUTTON_WIDTH, Const.BUTTON_HEIGHT));
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.operatePanel.textField.setText("");
            }
        });
    }
}
