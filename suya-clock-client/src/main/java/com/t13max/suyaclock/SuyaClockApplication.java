package com.t13max.suyaclock;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.t13max.suyaclock.entity.PlanEntity;
import com.t13max.suyaclock.util.DataUtils;
import com.t13max.util.JarUtils;
import com.t13max.util.RandomUtil;
import com.t13max.util.TextUtil;
import com.t13max.util.TimeUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author t13max
 * @since 10:13 2025/2/25
 */
public class SuyaClockApplication {

    private final static int FRAME_WIDTH = 500;
    private final static int FRAME_HEIGHT = 300;
    private final static int RIGHT_WIDTH = 300;
    private final static int TEXT_FIELD_WIDTH = 250;
    private final static int TEXT_FIELD_HEIGHT = 30;
    private final static int BUTTON_PANEL_WIDTH = 250;
    private final static int BUTTON_PANEL_HEIGHT = 100;
    private final static int BUTTON_WIDTH = 76;
    private final static int BUTTON_HEIGHT = 30;
    private final static int BUTTON_GAP = 3;
    private final static int ITEM_WIDTH = 250;
    private final static int FONT_SIZE = 40;

    private final static String SCRIPT_DIR = "SCRIPT_DIR";

    private final static ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    static JTextField textField;

    public static void main(String[] args) {

        List<PlanEntity> planEntities = checkGetPlan();

        initTask(planEntities);

        // 创建一个 JFrame 窗口
        JFrame frame = new JFrame("Suya Clock");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);

        // 左边的竖着文字部分 (JList)
        DefaultListModel<PlanEntity> listModel = new DefaultListModel<>();
        for (PlanEntity planEntity : planEntities) {
            listModel.addElement(planEntity);
        }
        JList<PlanEntity> list = new JList<>(listModel);
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
        JScrollPane listScrollPane = new JScrollPane(list);

        // 右边的输入框和按钮部分（上部分）
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // 显示选中的文字 (居中)
        JPanel labelPanel = new JPanel();
        JLabel selectedLabel = new JLabel("请选择一项计划");
        labelPanel.add(selectedLabel);
        labelPanel.setLayout(new FlowLayout(FlowLayout.CENTER));  // 设置居中对齐
        topPanel.add(labelPanel);

        // 上方空白区域（用于留出空白）
        /*JPanel emptyPanel = new JPanel();
        emptyPanel.setPreferredSize(new Dimension(0, 10));  // 这里设置空白的高度
        topPanel.add(emptyPanel);*/

        // 输入框（高度是原来的两倍）
        textField = new JTextField();
        textField.setPreferredSize(new Dimension(TEXT_FIELD_WIDTH, TEXT_FIELD_HEIGHT));  // 设置输入框高度
        topPanel.add(textField);

        // 按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, BUTTON_GAP, BUTTON_GAP));
        buttonPanel.setPreferredSize(new Dimension(BUTTON_PANEL_WIDTH, BUTTON_PANEL_HEIGHT));

        JButton button1 = new JButton("清空0w0");
        button1.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textField.setText("");
            }
        });
        JButton button2 = new JButton("    ");
        button2.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
        JButton button3 = new JButton("完成!!");
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                long nowMills = TimeUtil.nowMills();

                int selectedIndex = list.getSelectedIndex();
                if (selectedIndex != -1) {
                    // 修改选中的项
                    PlanEntity planEntity = planEntities.get(selectedIndex);
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
                            JOptionPane.showMessageDialog(null, "任务虽然超时了 但是苏雅很努力 加油!!", "提示", JOptionPane.INFORMATION_MESSAGE);
                        });
                    }
                    planEntity.setFinishMills(nowMills);
                    String text = textField.getText();
                    planEntity.setClockIn(text);
                    DataUtils.updatePlan(planEntity);
                    textField.setText("");
                    listModel.setElementAt(planEntity, selectedIndex);
                }
            }
        });
        button3.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));

        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

        topPanel.add(buttonPanel);

        // 右边显示图片部分（下部分）
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BorderLayout());

        // 创建一个标签显示图片
        /*ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(SuyaClockApplication.class.getResource("/images/test.png"))); // 替换为实际路径
        JLabel imageLabel = new JLabel(imageIcon);
        bottomPanel.add(imageLabel, BorderLayout.CENTER);*/
        // 创建一个标签显示艺术字
        String jarDirectory = System.getProperty(SCRIPT_DIR);
        System.out.println(jarDirectory);
        String cheerText = TextUtil.readOutText(jarDirectory + "/cheer.txt");
        String[] split = cheerText.split("\n");
        JTextArea artLabel = new JTextArea(RandomUtil.random(split));
        artLabel.setFont(new Font("Serif", Font.BOLD, FONT_SIZE));  // 设置字体，粗体和大小
        artLabel.setForeground(Color.PINK);  // 设置文字颜色
        artLabel.setBackground(null);  // 去掉背景色
        artLabel.setEditable(false);  // 设置为不可编辑
        artLabel.setLineWrap(true);  // 开启自动换行
        artLabel.setWrapStyleWord(true);  // 按单词换行
        artLabel.setBorder(null);  // 去掉边框
        //artLabel.setHorizontalAlignment(SwingConstants.CENTER);  // 设置居中显示
        bottomPanel.add(artLabel, BorderLayout.CENTER);

        // 将上半部分和下半部分垂直排列
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        rightPanel.add(topPanel, BorderLayout.NORTH);
        rightPanel.add(bottomPanel, BorderLayout.CENTER);

        // 使用 JSplitPane 来分割左右区域
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, listScrollPane, rightPanel);
        splitPane.setDividerLocation(ITEM_WIDTH);  // 设置分割线位置，使左侧占一半的空间

        // 监听 JList 选择事件
        list.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                PlanEntity planEntity = list.getSelectedValue();
                selectedLabel.setText("计划: " + planEntity.getDesc());
            }
        });

        // 将分割面板加入到主框架中
        frame.add(splitPane);
        //禁止改变大小
        frame.setResizable(false);
        //居中
        frame.setLocationRelativeTo(null);
        // 显示窗口
        frame.setVisible(true);

        //打印内存信息
        //printMemoryInfo();
    }

    private static void printMemoryInfo() {
        Runtime runtime = Runtime.getRuntime();

        // 获取JVM的最大内存、已分配内存和空闲内存
        long maxMemory = runtime.maxMemory(); // 最大可用内存
        long allocatedMemory = runtime.totalMemory(); // 当前已分配的内存
        long freeMemory = runtime.freeMemory(); // 当前空闲内存

        System.out.println("最大内存: " + maxMemory / (1024 * 1024) + " MB");
        System.out.println("已分配内存: " + allocatedMemory / (1024 * 1024) + " MB");
        System.out.println("空闲内存: " + freeMemory / (1024 * 1024) + " MB");

        // 使用中内存 = 已分配内存 - 空闲内存
        long usedMemory = allocatedMemory - freeMemory;
        System.out.println("已使用内存: " + usedMemory / (1024 * 1024) + " MB");
    }

    private static void initTask(List<PlanEntity> planEntities) {
        long nowMills = TimeUtil.nowMills();
        for (PlanEntity planEntity : planEntities) {
            if (planEntity.getExpectFinishMills() < nowMills) {
                continue;
            }
            scheduledExecutorService.schedule(() -> showToast(planEntity), planEntity.getExpectFinishMills() - nowMills, TimeUnit.MILLISECONDS);
        }
    }

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

    private static List<PlanEntity> checkGetPlan() {
        long nowMills = TimeUtil.nowMills();
        long zeroOfDayTime = TimeUtil.getZeroOfDayTime(nowMills);
        List<PlanEntity> planEntities = DataUtils.getPlansByIdRange(zeroOfDayTime, zeroOfDayTime + TimeUnit.DAYS.toMillis(1) - 1);
        if (planEntities == null || planEntities.isEmpty()) {
            planEntities = initPlan(zeroOfDayTime);
            for (PlanEntity planEntity : planEntities) {
                DataUtils.insertPlan(planEntity);
            }
        }
        return planEntities;
    }

    private static List<PlanEntity> initPlan(long zeroOfDayTime) {
        List<PlanEntity> result = new LinkedList<>();
        // 获取 JAR 所在目录
        String jarDirectory = System.getProperty(SCRIPT_DIR);
        if (jarDirectory != null) {
            // 假设 config.json 文件放在 JAR 所在目录
            String filePath = jarDirectory + "/plan.json";

            try (Reader reader = new FileReader(filePath)) {
                // 使用 FastJSON 解析 JSON 文件
                JSONObject jsonObject = JSON.parseObject(reader);

                // 访问 JSON 数据
                JSONArray dayOfTheWeekJsonArray = jsonObject.getJSONArray("dayOfTheWeek");
                JSONArray planJsonArray = jsonObject.getJSONArray("plan");
                for (int i = 0; i < planJsonArray.size(); i++) {
                    JSONObject planJsonObject = planJsonArray.getJSONObject(i);
                    PlanEntity planEntity = createPlanEntity(zeroOfDayTime, planJsonObject);
                    result.add(planEntity);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static PlanEntity createPlanEntity(long zeroOfDayTime, JSONObject planJsonObject) {

        Integer id = planJsonObject.getInteger("id");
        String desc = planJsonObject.getString("desc");
        String timeScope = planJsonObject.getString("timeScope");

        String[] split = timeScope.split("-");
        String[] split1 = split[1].split(":");
        int hours = Integer.parseInt(split1[0]);
        int minutes = Integer.parseInt(split1[1]);
        long triggerMills = zeroOfDayTime + TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes);

        PlanEntity planEntity = new PlanEntity();
        planEntity.setId(zeroOfDayTime + id);
        planEntity.setDesc(desc);
        planEntity.setTimeScope(timeScope);
        planEntity.setExpectFinishMills(triggerMills);
        return planEntity;
    }
}
