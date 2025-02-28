package com.t13max.suyaclock.menu;

import com.t13max.suyaclock.entity.PlanEntity;
import com.t13max.suyaclock.frame.MainFrame;
import com.t13max.suyaclock.panel.PlanListPanel;
import com.t13max.util.TimeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.Optional;

/**
 * @author t13max
 * @since 17:21 2025/2/28
 */
public class PlanItemMenu extends JPopupMenu {

    public PlanItemMenu(MainFrame mainFrame, PlanListPanel planListPanel) {

        JMenuItem editMenuItem = new JMenuItem("编辑");
        JMenuItem deleteMenuItem = new JMenuItem("删除");
        JMenuItem addMenuItem = new JMenuItem("新增");

        JList<PlanEntity> list = planListPanel.list;
        // 编辑菜单项
        editMenuItem.addActionListener(e -> {

            PlanEntity selectedPlan = list.getSelectedValue();
            if (selectedPlan != null) {
                // 执行编辑操作
                showEditDialog(mainFrame, selectedPlan);
            }
        });

        // 删除菜单项
        deleteMenuItem.addActionListener(e -> {
            PlanEntity selectedPlan = list.getSelectedValue();
            if (selectedPlan != null) {
                // 执行删除操作
                planListPanel.listModel.removeElement(selectedPlan);
            }
            planListPanel.removePlanEntity(selectedPlan);
            planListPanel.list.repaint();  // 更新 JList
        });

        // 新增菜单项
        addMenuItem.addActionListener(e -> {
            // 执行新增操作
            showEditDialog(mainFrame, null);
        });

        this.add(addMenuItem);
        this.add(editMenuItem);
        this.add(deleteMenuItem);

        // 给 JList 添加右键点击事件监听
        list.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopupMenu(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopupMenu(e);
                }
            }

            private void showPopupMenu(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                list.setSelectedIndex(index);
                if (index >= 0 && index < planListPanel.listModel.getSize()) {
                    PlanItemMenu.this.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    private void showEditDialog(MainFrame mainFrame, PlanEntity planEntity) {
        // 创建一个 JDialog辑
        JDialog editDialog = new JDialog(mainFrame, "编计划", true);
        editDialog.setSize(300, 200);
        editDialog.setLocationRelativeTo(mainFrame);
        editDialog.setLayout(new FlowLayout());

        JLabel descLabel = new JLabel("描述:");
        JTextField descField = new JTextField(planEntity == null ? "" : planEntity.getDesc(), 20);
        editDialog.add(descLabel);
        editDialog.add(descField);

        JLabel timeScopeLabel = new JLabel("时间:");
        JTextField timeScopeField = new JTextField(planEntity == null ? "" : planEntity.getTimeScope(), 20);
        editDialog.add(timeScopeLabel);
        editDialog.add(timeScopeField);

        // 确认按钮
        JButton confirmButton = new JButton("确认");
        confirmButton.addActionListener(e -> {
            String timeScope = timeScopeField.getText();
            if (!checkTimeScope(timeScope)) {
                SwingUtilities.invokeLater(() -> {
                    // 弹出一个提示框，显示 "XXX" 消息，并提供确认按钮
                    JOptionPane.showMessageDialog(null, "时间格式错误, 应该是 10:00-12:30 的格式", "提示", JOptionPane.INFORMATION_MESSAGE);
                });
                return;
            }
            if (planEntity == null) {
                PlanEntity newPlanEntity = new PlanEntity();
                long zeroOfDayTime = TimeUtil.getZeroOfDayTime(TimeUtil.nowMills());
                Optional<PlanEntity> maxOp = mainFrame.planListPanel.planEntities.stream().max(Comparator.comparingLong(PlanEntity::getId));
                if (maxOp.isEmpty()) {
                    return;
                }
                newPlanEntity.setId(zeroOfDayTime + (maxOp.get().getId() % 100) + 1);
                newPlanEntity.setDesc(descField.getText());  // 更新描述
                newPlanEntity.setTimeScope(timeScope);  // 更新描述
                mainFrame.planListPanel.addPlanEntity(newPlanEntity);
            } else {
                planEntity.setDesc(descField.getText());  // 更新描述
                planEntity.setTimeScope(timeScope);  // 更新描述
            }

            mainFrame.planListPanel.list.repaint();  // 更新 JList
            editDialog.dispose();  // 关闭对话框
        });
        editDialog.add(confirmButton);

        // 关闭按钮
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(e -> editDialog.dispose());
        editDialog.add(closeButton);

        editDialog.setVisible(true);
    }

    private boolean checkTimeScope(String timeScope) {
        String[] split = timeScope.split("-");
        if (split.length != 2) {
            return false;
        }

        try {
            String[] split1 = split[0].split(":");
            if (split1.length != 2) {
                return false;
            }
            int hours = Integer.parseInt(split1[0]);
            if (hours <= 0 || hours > 23) {
                return false;
            }
            int minutes = Integer.parseInt(split1[1]);
            if (minutes < 0 || minutes >= 60) {
                return false;
            }

            String[] split2 = split[1].split(":");
            if (split2.length != 2) {
                return false;
            }
            int hours2 = Integer.parseInt(split2[0]);
            if (hours2 <= 0 || hours2 > 23) {
                return false;
            }
            int minutes2 = Integer.parseInt(split2[1]);
            if (minutes2 < 0 || minutes2 >= 60) {
                return false;
            }

            if (hours2 < hours) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        return true;
    }

}
