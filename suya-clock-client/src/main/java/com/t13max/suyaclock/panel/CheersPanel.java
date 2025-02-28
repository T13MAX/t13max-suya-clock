package com.t13max.suyaclock.panel;

import com.t13max.suyaclock.consts.Const;
import com.t13max.util.RandomUtil;
import com.t13max.util.TextUtil;

import javax.swing.*;
import java.awt.*;

/**
 * @author t13max
 * @since 16:53 2025/2/28
 */
public class CheersPanel extends JPanel {

    public CheersPanel() {

        this.setLayout(new BorderLayout());

        // 创建一个标签显示图片
        /*ImageIcon imageIcon = new ImageIcon(Objects.requireNonNull(SuyaClockApplication.class.getResource("/images/test.png"))); // 替换为实际路径
        JLabel imageLabel = new JLabel(imageIcon);
        bottomPanel.add(imageLabel, BorderLayout.CENTER);*/
        // 创建一个标签显示艺术字
        String jarDirectory = System.getProperty(Const.SCRIPT_DIR);
        System.out.println(jarDirectory);
        String cheerText = TextUtil.readOutText(jarDirectory + "/cheer.txt");
        String[] split = cheerText.split("\n");
        JTextArea artLabel = new JTextArea(RandomUtil.random(split));
        artLabel.setFont(new Font("Serif", Font.BOLD, Const.FONT_SIZE));  // 设置字体，粗体和大小
        artLabel.setForeground(Color.PINK);  // 设置文字颜色
        artLabel.setBackground(null);  // 去掉背景色
        artLabel.setEditable(false);  // 设置为不可编辑
        artLabel.setLineWrap(true);  // 开启自动换行
        artLabel.setWrapStyleWord(true);  // 按单词换行
        artLabel.setBorder(null);  // 去掉边框
        //artLabel.setHorizontalAlignment(SwingConstants.CENTER);  // 设置居中显示
        this.add(artLabel, BorderLayout.CENTER);
    }
}
