package com.example.springLearning.util;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;

public class Parameter {
    public static final String key = "beiyou";

    public String  getFreePhysicalMemorySize(){
        int kb = 1024;
        OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
        // 剩余的物理内存
        long freePhysicalMemorySize = osmxb.getFreePhysicalMemorySize() / kb;
        return freePhysicalMemorySize +"M";
    }
}
