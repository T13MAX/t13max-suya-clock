package com.t13max.suyaclock.panel;

import com.t13max.suyaclock.entity.PlanEntity;
import com.t13max.suyaclock.frame.MainFrame;
import com.t13max.suyaclock.menu.PlanItemMenu;
import com.t13max.suyaclock.util.DataUtils;
import com.t13max.suyaclock.util.PlanUtils;
import com.t13max.suyaclock.util.ToastUtils;
import com.t13max.util.TimeUtil;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * @author t13max
 * @since 16:30 2025/2/28
 */
public class PlanListPanel extends JScrollPane {

    private final static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    public final List<PlanEntity> planEntities;
    public final JList<PlanEntity> list;
    public final DefaultListModel<PlanEntity> listModel;

    private final Map<Long, ScheduledFuture<?>> futureMap = new HashMap<>();

    public PlanListPanel(MainFrame mainFrame) {

        planEntities = PlanUtils.checkGetPlan();

        initTask();

        // 左边的竖着文字部分 (JList)
        listModel = new DefaultListModel<>();
        for (PlanEntity planEntity : planEntities) {
            listModel.addElement(planEntity);
        }
        list = new JList<>(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setCellRenderer((list1, value, index, isSelected, cellHasFocus) -> {
            JLabel label = new JLabel(value.toString());
            label.setOpaque(true);

            // 设置选中和未选中的背景颜色
            if (isSelected) {
                label.setBackground(Color.LIGHT_GRAY);
            } else if (value.getFinishMills() > 0) {
                label.setBackground(Color.GREEN);
            } else {
                label.setBackground(Color.WHITE);
            }
            return label;
        });
        // 监听 JList 选择事件
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                PlanEntity planEntity = list.getSelectedValue();
                if (planEntity != null) {
                    mainFrame.operatePanel.selectedLabel.setText("计划: " + planEntity.getDesc());
                }
            }
        });

        this.setViewportView(list);

        // 右键菜单
        JPopupMenu popupMenu = new PlanItemMenu(mainFrame, this);

    }

    private void initTask() {
        long nowMills = TimeUtil.nowMills();
        for (PlanEntity planEntity : planEntities) {
            if (planEntity.getExpectFinishMills() < nowMills) {
                continue;
            }
            addScheduleTask(planEntity, nowMills);
        }
    }

    private void addScheduleTask(PlanEntity planEntity, long nowMills) {
        ScheduledFuture<?> schedule = scheduledExecutorService.schedule(() -> ToastUtils.showToast(planEntity), planEntity.getExpectFinishMills() - nowMills, TimeUnit.MILLISECONDS);
        futureMap.put(planEntity.getId(), schedule);
    }

    public void removePlanEntity(PlanEntity planEntity) {
        this.planEntities.remove(planEntity);
        listModel.removeElement(planEntity);
        ScheduledFuture<?> scheduledFuture = futureMap.remove(planEntity.getId());
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
        }
        DataUtils.deletePlan(planEntity.getId());
    }

    public void addPlanEntity(PlanEntity planEntity) {
        long zeroOfDayTime = TimeUtil.getZeroOfDayTime(TimeUtil.nowMills());
        String[] split = planEntity.getTimeScope().split("-");
        String[] split1 = split[1].split(":");
        int hours = Integer.parseInt(split1[0]);
        int minutes = Integer.parseInt(split1[1]);
        long triggerMills = zeroOfDayTime + TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);
        planEntity.setExpectFinishMills(triggerMills);
        this.planEntities.add(planEntity);
        addScheduleTask(planEntity, TimeUtil.nowMills());
        listModel.addElement(planEntity);
        DataUtils.insertPlan(planEntity);
    }
}
