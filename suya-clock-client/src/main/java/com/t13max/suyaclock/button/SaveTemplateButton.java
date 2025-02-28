package com.t13max.suyaclock.button;

import com.t13max.suyaclock.entity.PlanEntity;
import com.t13max.suyaclock.frame.MainFrame;
import com.t13max.suyaclock.util.PlanUtils;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * @author t13max
 * @since 16:40 2025/2/28
 */
public class SaveTemplateButton extends JButton {

    public SaveTemplateButton(MainFrame mainFrame) {
        super("保存模板");

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                List<PlanEntity> planEntities = mainFrame.planListPanel.planEntities;

                try {
                    PlanUtils.saveTemplate(planEntities);
                } catch (Exception exception) {
                    SwingUtilities.invokeLater(() -> {
                        // 弹出一个提示框，显示 "XXX" 消息，并提供确认按钮
                        JOptionPane.showMessageDialog(null, "保存失败了: " + exception.getMessage(), "提示", JOptionPane.INFORMATION_MESSAGE);
                    });
                    return;
                }
                SwingUtilities.invokeLater(() -> {
                    // 弹出一个提示框，显示 "XXX" 消息，并提供确认按钮
                    JOptionPane.showMessageDialog(null, "保存成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                });
            }
        });
    }
}
