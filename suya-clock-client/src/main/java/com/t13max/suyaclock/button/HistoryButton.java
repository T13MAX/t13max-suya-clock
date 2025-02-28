package com.t13max.suyaclock.button;

import com.t13max.suyaclock.entity.PlanEntity;
import com.t13max.suyaclock.frame.MainFrame;
import com.t13max.suyaclock.util.DataUtils;
import com.t13max.util.TimeUtil;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author t13max
 * @since 16:25 2025/2/28
 */
public class HistoryButton extends JButton {

    private final static SimpleDateFormat SDF = new SimpleDateFormat("HH:mm");

    public HistoryButton(MainFrame mainFrame) {
        super("历史");

        this.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNewWindow();
            }
        });
    }


    /**
     * 历史信息 后续优化
     *
     * @Author t13max
     * @Date 18:22 2025/2/28
     */
    private static void openNewWindow() {

        // 新窗口
        JFrame newFrame = new JFrame("历史");
        newFrame.setSize(600, 400);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 左侧数据列表
        DefaultListModel<String> listModel = new DefaultListModel<>();

        JList<String> dataList = new JList<>(listModel);
        JScrollPane scrollPane = new JScrollPane(dataList);

        // 右侧按钮，选择日期
        JPanel datePanel = new JPanel(); // 为日期选择器创建一个面板

        JButton chooseDateButton = new JButton("确认");

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("yyyy-MM-dd");

        // 设置日期选择器按钮的布局方式
        datePanel.setLayout(new FlowLayout(FlowLayout.LEFT));  // 左对齐
        datePanel.add(dateChooser);
        datePanel.add(chooseDateButton);

        // 设置左右布局
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        // 设置左边面板固定宽度
        mainPanel.add(scrollPane, BorderLayout.CENTER);  // 左侧部分占满剩余空间
        mainPanel.add(datePanel, BorderLayout.EAST);  // 右侧部分是自适应大小

        // 按钮点击事件：选择日期后过滤数据
        chooseDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date selectedDate = dateChooser.getDate();
                if (selectedDate != null) {
                    long zeroOfDayTime = TimeUtil.getZeroOfDayTime(selectedDate.getTime());
                    // 根据选择的日期过滤数据
                    List<PlanEntity> planEntities = DataUtils.getPlansByIdRange(zeroOfDayTime, zeroOfDayTime + TimeUnit.DAYS.toMillis(1));
                    listModel.clear();
                    for (PlanEntity planEntity : planEntities) {
                        listModel.addElement(getItemStr(planEntity));
                    }

                } else {
                    JOptionPane.showMessageDialog(newFrame, "请选择一个日期");
                }
            }
        });

        dataList.setCellRenderer((list1, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value);
            label.setOpaque(true);

            // 设置选中和未选中的背景颜色
            if (value.contains("完成")) {
                label.setBackground(Color.GREEN);
            }
            return label;
        });
        newFrame.add(mainPanel);
        newFrame.setVisible(true);

    }

    private static String getItemStr(PlanEntity planEntity) {
        String clockIn = planEntity.getClockIn();
        if (clockIn == null) {
            clockIn = "";
        }
        String finishedText = "";
        long finishMills = planEntity.getFinishMills();
        if (finishMills != 0) {
            finishedText = " 完成时间:" + SDF.format(new Date(planEntity.getFinishMills()));
        }
        return planEntity.getId() % 100 + ". " + planEntity.getTimeScope() + " " + planEntity.getDesc() + " " + clockIn + finishedText;
    }
}
