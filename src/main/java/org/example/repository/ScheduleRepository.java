package org.example.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import org.example.entity.Schedule;

public class ScheduleRepository {

    private static final ScheduleRepository instance = new ScheduleRepository();
    HashMap<Integer, Schedule> schedules = new HashMap<>();

    private ScheduleRepository() {
        initSchedules();
    }

    public static ScheduleRepository getInstance() {
        return instance;
    }

    public void addSchedule(String name, Date startDate, Date endDate, int priority,
            int userId) {
        addSchedule(name, startDate, endDate, priority, userId, null);
    }

    public void addSchedule(String name, Date startDate, Date endDate, int priority,
            int userId, Integer repeatedId) {
        var maxId = schedules.keySet().stream()
                .max(Integer::compare)
                .orElse(0);
        var schedule = new Schedule(maxId + 1, name, startDate, endDate, priority, userId,
                repeatedId);
        schedules.put(schedule.id, schedule);
        saveSchedule();
    }

    public int getMaxRepeatedId() {
        return schedules.values().stream()
                .map(schedule -> schedule.repeatedId)
                .filter(Objects::nonNull)
                .max(Integer::compare)
                .orElse(0);
    }

    public void updateSchedule(int id, String name, Date startDate, Date endDate, int priority,
            int userId) {
        schedules.put(id, new Schedule(id, name, startDate, endDate, priority, userId));
        saveSchedule();
    }

    public void deleteSchedule(int id) {
        var schedule = schedules.get(id);
        if (schedule.repeatedId != null) {
            schedules.values().removeIf(s -> s.repeatedId == schedule.repeatedId);
        } else {
            schedules.remove(id);
        }

        saveSchedule();
    }

    public Schedule findByIdAndUserId(int id, int userId) {
        return schedules.values().stream()
                .filter(schedule -> schedule.id == id && schedule.userId == userId)
                .findFirst()
                .orElse(null);
    }

    public List<Schedule> findAllByUserId(int userId) {
        return schedules.values().stream()
                .filter(schedule -> schedule.userId == userId).toList();
    }

    public List<Schedule> findAllByUserIdAndMonth(int userId, int month) {
        var yearMonth = YearMonth.of(2024, month);
        var firstDate = Date.from(
                yearMonth.atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        var lastDate = Date.from(
                yearMonth.atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant());

        return schedules.values().stream()
                .filter(schedule -> schedule.userId == userId)
                .filter(schedule -> firstDate.before(schedule.endDate) && lastDate.after(
                        schedule.startDate))
                .toList();
    }

    public List<Schedule> findAllByUserIdAndDate(int userId, Date date) {
        var formatter = new SimpleDateFormat("MMdd");
        var dateNumber = Integer.parseInt(formatter.format(date));

        return schedules.values().stream()
                .filter(schedule -> schedule.userId == userId)
                .filter(schedule ->
                        Integer.parseInt(formatter.format(schedule.startDate)) <= dateNumber
                                && dateNumber <= Integer.parseInt(
                                formatter.format(schedule.endDate))
                )
                .toList();
    }

    public List<Schedule> findAllByUserIdAndPriority(int userId, int priority) {
        return schedules.values().stream()
                .filter(schedule -> schedule.userId == userId)
                .filter(schedule -> schedule.priority == priority)
                .toList();
    }

    public void saveSchedule() {
        try {
            var file = new File("ScheduleData.csv");
            clearFile(file);

            var fileOutputStream = new FileOutputStream(file);
            var bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            for (var schedule : schedules.values()) {
                bw.write(schedule.convertToCsvRow());
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initSchedules() {
        try {
            var file = new File("ScheduleData.csv");
            file.createNewFile();
            var fileInputStream = new FileInputStream(file);
            var br = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = br.readLine()) != null) {
                var data = line.split(",");
                var schedule = new Schedule(Integer.parseInt(data[0]), data[1], data[2], data[3],
                        Integer.parseInt(data[4]), Integer.parseInt(data[5]),
                        data.length == 7 ? parseRepeatedId(data[6]) : null);
                schedules.put(schedule.id, schedule);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFile(File file) {
        try {
            new FileWriter(file, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Integer parseRepeatedId(String repeatedId) {
        try {
            return Integer.parseInt(repeatedId);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
