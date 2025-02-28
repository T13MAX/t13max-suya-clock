package com.t13max.suyaclock.panel;

import javax.swing.*;
import java.awt.*;

/**
 * @author t13max
 * @since 16:28 2025/2/28
 */
public class MainPanel extends JSplitPane {

    private final static int ITEM_WIDTH = 250;

    public MainPanel(Component newLeftComponent, Component newRightComponent) {
        super(JSplitPane.HORIZONTAL_SPLIT, newLeftComponent, newRightComponent);
        this.setDividerLocation(ITEM_WIDTH);  // 设置分割线位置，使左侧占一半的空间
    }
}
