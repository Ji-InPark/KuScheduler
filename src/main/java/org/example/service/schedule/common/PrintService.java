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

    public void printSchedule(Schedule schedule) {
        printScheduleHeader();
        System.out.printf("|%-4s|%-" + (34 - schedule.getKoreanLength()) + "s|%-27s|%-8s|\n",
                StringUtils.center(String.valueOf(schedule.id), 4),
                schedule.name,
                StringUtils.center(schedule.getStartDate() + " ~ " + schedule.getEndDate(), 27),
                StringUtils.center(priorityString(schedule.priority), 8));
        printHorizontalLine();
    }

    public void printSchedules(List<Schedule> schedules) {
        for (var schedule : schedules) {
            System.out.printf("|%-4s|%-" + (34 - schedule.getKoreanLength()) + "s|%-27s|%-8s|\n",
                    StringUtils.center(String.valueOf(schedule.id), 4),
                    schedule.name,
                    StringUtils.center(schedule.getStartDate() + " ~ " + schedule.getEndDate(), 27),
                    StringUtils.center(priorityString(schedule.priority), 8)
            );
        }
        printHorizontalLine();
    }

    public void printScheduleHeaderWithUserInfo(User user, String subject) {
        printHorizontalLine();
        System.out.printf("|%-44s%-26s|\n", user.name + "님 환영합니다.", "");
        printHorizontalLine();
        System.out.printf("|%34s스 케 줄%34s|\n", "", "");
        if (subject != null) {
            System.out.println(subject);
        }
        printScheduleHeader();
    }

    public void printScheduleHeaderWithSubject(String subject) {
        printHorizontalLine();
        System.out.println(subject);
        printScheduleHeader();
    }

    private void printScheduleHeader() {
        printHorizontalLine();
        System.out.printf("|%-4s|%-30s|%-23s|%-5s|\n",
                StringUtils.center("ID", 4),
                StringUtils.center("스케줄명", 30),
                StringUtils.center("날짜/시간", 23),
                StringUtils.center("중요도", 5)
        );
        printHorizontalLine();
    }

    public void printFriendHeader() {
        printHorizontalLine();
        System.out.printf("|%-72s|\n", StringUtils.center("<친구 목록>", 72));
        printHorizontalLine();
        System.out.printf("|%-35s|%-35s|\n", StringUtils.center("이름", 35),
                StringUtils.center("아이디", 35));
        printHorizontalLine();
    }

    public void printFriends(List<User> friends) {
        for (var friend : friends) {
            System.out.printf("|%-" + (37 - friend.getKoreanLength()) + "s|%-38s|\n",
                    StringUtils.center(friend.name, (37 - friend.getKoreanLength())),
                    StringUtils.center(String.valueOf(friend.loginId), 38));
        }
        printHorizontalLine();
    }

    private void printHorizontalLine() {
        System.out.println(
                "------------------------------------------------------------------------------");
    }
}
