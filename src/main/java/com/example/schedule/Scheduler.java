package com.example.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {
    @Scheduled(fixedRate = 1000)
    public void display() {
        System.out.println("Preetha");
    }
    @Scheduled(fixedDelay = 1000)
    public void show() {
        System.out.println("Welcome");
    }
        @Scheduled(cron = "0 0 9 * * MON-FRI")
        public void sendReminder() {

            System.out.println("Good Morning! Reminder sent.");

    }
}

