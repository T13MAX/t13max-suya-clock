package com.t13max.suyaclock.panel;

import com.t13max.suyaclock.consts.Const;
import com.t13max.suyaclock.frame.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * @author t13max
 * @since 16:31 2025/2/28
 */
public class OperatePanel extends JPanel {

    public final JTextField textField;
    public final JLabel selectedLabel;

    public OperatePanel(MainFrame mainFrame) {
        // 右边的输入框和按钮部分（上部分）
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // 显示选中的文字 (居中)
        JPanel labelPanel = new JPanel();
        selectedLabel = new JLabel("请选择一项计划");
        labelPanel.add(selectedLabel);
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // 设置居中对齐
        topPanel.add(labelPanel);

        // 上方空白区域（用于留出空白）
        /*JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(0, 10));  // 这里设置空白的高度
        topPanel.add(emptyPanel);*/

        // 输入框（高度是原来的两倍）
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(Const.TEXT_FIELD_WIDTH, Const.TEXT_FIELD_HEIGHT));  // 设置输入框高度
        topPanel.add(textField);

        // 按钮
        JPanel buttonPanel = new ButtonPanel(mainFrame);
        topPanel.add(buttonPanel);

        // 右边显示图片部分（下部分）
        JPanel bottomPanel = new CheersPanel();


        this.setLayout(new BorderLayout());
        this.add(topPanel, BorderLayout.NORTH);
        this.add(bottomPanel, BorderLayout.CENTER);
    }
}
