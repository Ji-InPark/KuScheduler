package org.example.entity;

import java.util.Date;
import java.util.GregorianCalendar;

public class Schedule {

    public int id;
    public int userId;
    public String name;
    public Date startDate;
    public Date endDate;
    public int priority;

    public Schedule(int id, String name, String startDate, String endDate, int priority,
            int userId) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.startDate = convertStringToDate(startDate);
        this.endDate = convertStringToDate(endDate);
        this.userId = userId;
    }

    public Schedule(int id, String name, Date startDate, Date endDate, int priority, int userId) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userId = userId;
    }

    public String convertToCsvRow() {
        return id + "," + name + "," + convertDateToString(startDate) + ","
                + convertDateToString(endDate) + "," + priority + "," + userId;
    }

    public String getStartDate() {
        return convertDateToString(startDate);
    }

    public String getEndDate() {
        return convertDateToString(endDate);
    }

    public int getKoreanLength() {
        return (name.getBytes().length - name.length()) / 2;
    }

    private String convertDateToString(Date date) {
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var hour = date.getHours();
        var minute = date.getMinutes();

        return String.format("%02d", month) + "/" + String.format("%02d", day) + "_"
                + String.format("%02d", hour) + ":" + String.format("%02d", minute);
    }

    private Date convertStringToDate(String date) {
        var dateAndTime = date.split("_");
        var monthAndDay = dateAndTime[0].split("/");
        var timeAndMinute = dateAndTime[1].split(":");
        var year = 2024;
        var month = Integer.parseInt(monthAndDay[0]) - 1;
        var day = Integer.parseInt(monthAndDay[1]);
        var time = Integer.parseInt(timeAndMinute[0]);
        var minute = Integer.parseInt(timeAndMinute[1]);

        return new GregorianCalendar(year, month, day, time, minute).getTime();
    }
}
