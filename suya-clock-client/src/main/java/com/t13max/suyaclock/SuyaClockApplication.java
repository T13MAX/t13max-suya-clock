package com.t13max.suyaclock;

import com.t13max.suyaclock.frame.MainFrame;

/**
 * @author t13max
 * @since 10:13 2025/2/25
 */
public class SuyaClockApplication {

    public static void main(String[] args) {

        MainFrame mainFrame = new MainFrame();

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

}
