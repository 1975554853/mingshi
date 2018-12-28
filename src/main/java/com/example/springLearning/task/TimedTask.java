package com.example.springLearning.task;

import com.example.springLearning.domain.OfficeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TimedTask{

    @Autowired
    private OfficeService officeService;

    // 定时任务

    @Scheduled(cron = " 0 0 0 * * ? * ")
    public void clearOfficeViewsDay(){
        officeService.clearOfficeViewsDay();
    }

}