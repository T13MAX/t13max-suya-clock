package com.t13max.suyaclock.button;

import com.t13max.suyaclock.consts.Const;
import com.t13max.suyaclock.entity.PlanEntity;
import com.t13max.suyaclock.frame.MainFrame;
import com.t13max.suyaclock.util.DataUtils;
import com.t13max.util.TimeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * @author t13max
 * @since 16:50 2025/2/28
 */
public class FinishedButton extends JButton {

    public FinishedButton(MainFrame mainFrame) {
        super("完成!!");
        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long nowMills = TimeUtil.nowMills();

                int selectedIndex = mainFrame.planListPanel.list.getSelectedIndex();
                if (selectedIndex != -1) {
                    // 修改选中的项
                    PlanEntity planEntity = mainFrame.planListPanel.planEntities.get(selectedIndex);
                    if (planEntity.getExpectFinishMills() - nowMills > TimeUnit.MILLISECONDS.toMillis(10)) {
                        SwingUtilities.invokeLater(() -> {
                            // 弹出一个提示框，显示 "XXX" 消息，并提供确认按钮
                            JOptionPane.showMessageDialog(null, "不能提前完成!!", "提示", JOptionPane.INFORMATION_MESSAGE);
                        });
                        return;
                    }
                    if (nowMills - planEntity.getExpectFinishMills() > TimeUnit.MILLISECONDS.toMillis(10)) {
                        SwingUtilities.invokeLater(() -> {
                            // 弹出一个提示框，显示 "XXX" 消息，并提供确认按钮
                            JOptionPane.showMessageDialog(null, "完成啦!!", "提示", JOptionPane.INFORMATION_MESSAGE);
                        });
                    }
                    planEntity.setFinishMills(nowMills);
                    JTextField textField = mainFrame.operatePanel.textField;
                    String text = textField.getText();
                    planEntity.setClockIn(text);
                    DataUtils.updatePlan(planEntity);
                    textField.setText("");
                    mainFrame.planListPanel.listModel.setElementAt(planEntity, selectedIndex);
                }
            }
        });
        this.setPreferredSize(new Dimension(Const.BUTTON_WIDTH, Const.BUTTON_HEIGHT));
    }
}
