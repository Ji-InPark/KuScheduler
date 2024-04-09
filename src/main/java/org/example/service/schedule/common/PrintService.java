package org.example.service.schedule.common;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.Schedule;
import org.example.entity.User;

public class PrintService {

    private static final PrintService instance = new PrintService();

    private PrintService() {
    }

    public static PrintService getInstance() {
        return instance;
    }

    private String priorityString(int priority) {
        switch (priority) {
            case 1:
                return "★";
            case 2:
                return "★★";
            default:
                return "★★★";
        }

    }

    public void printSchedules(List<Schedule> schedules) {
        for (var schedule : schedules) {
            System.out.printf("|%-4s|%-15s|%-23s|%-7s|\n",
                    StringUtils.center(String.valueOf(schedule.id), 4),
                    StringUtils.center(schedule.name, 15),
                    StringUtils.center(schedule.getStartDate() + " ~ " + schedule.getEndDate(), 25),
                    StringUtils.center(priorityString(schedule.priority), 7)
            );
        }
        printHorizontalLine();
    }

    public void printScheduleHeader(User user, String subject) {
        printHorizontalLine();
        System.out.printf("|%-23s%-29s|\n", user.name + "님 환영합니다.", "");
        printHorizontalLine();
        System.out.println("|                        스 케 줄                         |");
        if (subject != null) {
            System.out.println(subject);
        }
        printHorizontalLine();
        System.out.printf("|%-4s|%-15s|%-23s|%-5s|\n",
                StringUtils.center("ID", 4),
                StringUtils.center("스케줄명", 15),
                StringUtils.center("날짜/시간", 23),
                StringUtils.center("중요도", 5)
        );
        printHorizontalLine();
    }

    private void printHorizontalLine() {
        System.out.println("---------------------------------------------------------");
    }
}
