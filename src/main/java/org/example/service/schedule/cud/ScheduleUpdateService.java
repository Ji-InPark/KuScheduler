package org.example.service.schedule.cud;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import org.example.entity.Schedule;
import org.example.entity.User;
import org.example.respository.ScheduleRepository;
import org.example.service.schedule.common.PrintService;

public class ScheduleUpdateService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
        System.out.println("<스케줄 변경>");

        Schedule schedule;
        while (true) {
            System.out.print("변경할 스케줄 id를 입력해주세요: ");
            var scheduleId = scanner.nextLine().trim();
            if (!isValidScheduleId(scheduleId)) {
                System.out.println("Error! 스케줄 id가 형식에 맞게 입력되지 않았습니다.");
                System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
                scanner.nextLine();
                return;
            }
            var id = Integer.parseInt(scheduleId);
            schedule = ScheduleRepository.getInstance().findByIdAndUserId(id, user.id);
            if (schedule == null) {
                System.out.println("Error! 해당 스케줄을 찾을 수 없습니다.");
                continue;
            }

            break;
        }

        var dummySchedule = new Schedule(schedule.id, schedule.name, schedule.startDate,
                schedule.endDate, schedule.priority, user.id);

        Loop:
        while (true) {
            PrintService.getInstance().printSchedule(schedule);
            System.out.println("[1: 스케줄명, 2: 시작날짜/시간, 3: 종료날짜/시간, 4: 중요도, q: 이전 화면]");
            System.out.print("변경할 내용을 선택하세요: ");
            var choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    System.out.print("변경할 스케줄명을 입력하세요: ");
                    var name = scanner.nextLine().trim();
                    if (!isValidTitle(name)) {
                        System.out.println("Error! 스케줄 이름이 형식에 맞게 입력되지 않았습니다.");
                        System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
                        scanner.nextLine();
                        break;
                    }
                    dummySchedule.name = name;
                    break Loop;
                case "2":
                    System.out.print("변경할 시작 날짜와 시간(예: 4/22 15:30)을 입력해주세요: ");
                    var startDateInput = scanner.nextLine().trim();
                    var startDate = parseDateAndValidate(startDateInput);
                    if (startDate == null) {
                        System.out.println("Error! 시작 날짜 및 시간이 형식에 맞게 입력되지 않았습니다.");
                        System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
                        scanner.nextLine();
                        break;
                    }

                    if (startDate.after(dummySchedule.endDate)) {
                        System.out.println("Error! 종료 시간이 시작 시간보다 앞서있습니다.");
                        System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
                        scanner.nextLine();
                        break;
                    }

                    dummySchedule.startDate = startDate;
                    break Loop;
                case "3":
                    System.out.print("변경할 종료 날짜와 시간(예: 4/22 15:30)을 입력해주세요: ");
                    var endDateInput = scanner.nextLine().trim();
                    var endDate = parseDateAndValidate(endDateInput);
                    if (endDate == null) {
                        System.out.println("Error! 종료 날짜 및 시간이 형식에 맞게 입력되지 않았습니다.");
                        System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
                        scanner.nextLine();
                        break;
                    }

                    if (dummySchedule.startDate.after(endDate)) {
                        System.out.println("Error! 종료 시간이 시작 시간보다 앞서있습니다.");
                        System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
                        scanner.nextLine();
                        break;
                    }

                    dummySchedule.endDate = endDate;
                    break Loop;
                case "4":
                    System.out.print("변경할 중요도를 입력해주세요: ");
                    var priorityInput = scanner.nextLine().trim();
                    if (!isValidPriority(priorityInput)) {
                        System.out.println("Error! 중요도가 형식에 맞게 입력되지 않았습니다.");
                        System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
                        scanner.nextLine();
                        break;
                    }

                    if (!isValidSchedule(dummySchedule.startDate, dummySchedule.endDate,
                            Integer.parseInt(priorityInput))) {
                        System.out.println("Error! 해당 기간에 중복된 중요도의 스케줄이 이미 존재합니다.");
                        System.out.println("기존 일정의 중요도를 수정하시려면 변경창에서 수정해주시길 바랍니다.");
                        System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
                        scanner.nextLine();
                        break;
                    }

                    dummySchedule.priority = Integer.parseInt(priorityInput);
                    break Loop;
                case "q":
                    return;
                default:
                    System.out.println("1~4, q 값중 하나만 입력해주세요.");
                    break;
            }
        }

        do {
            System.out.print("추가한 일정을 저장하시겠습니까? (예: 1, 아니오: 2): ");
            var saveInput = scanner.nextLine().trim();
            try {
                var save = Integer.parseInt(saveInput);

                if (save == 1) {
                    ScheduleRepository.getInstance().updateSchedule(schedule.id, dummySchedule.name,
                            dummySchedule.startDate, dummySchedule.endDate, dummySchedule.priority,
                            user.id);
                    System.out.println("성공적으로 저장되었습니다.");
                    return;
                } else if (save == 2) {
                    System.out.println("저장이 취소되었습니다.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error! 형식에 맞게 입력해주세요.");
            }
        } while (true);
    }

    private boolean isValidSchedule(Date startDate, Date endDate, int priority) {
        return ScheduleRepository.getInstance().findAllByUserIdAndPriority(user.id, priority)
                .stream()
                .filter(schedule ->
                        (startDate.after(schedule.startDate)
                                && startDate.before(schedule.endDate))
                                || (endDate.after(schedule.startDate)
                                && endDate.before(schedule.endDate))
                ).toList().isEmpty();
    }

    private boolean isValidPriority(String priority) {
        try {
            var priorityInt = Integer.parseInt(priority);
            return priorityInt >= 1 && priorityInt <= 3;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidTitle(String title) {
        return title.matches("^[a-zA-Z0-9가-힣 ]{1,15}$");
    }

    private Date parseDateAndValidate(String startDateInput) {
        var arr = startDateInput.split(" ");
        if (arr.length != 2) {
            return null;
        }

        var dateArr = arr[0].split("/");
        if (dateArr.length != 2) {
            return null;
        }

        var timeArr = arr[1].split(":");
        if (timeArr.length != 2) {
            return null;
        }

        Date date;
        try {
            var month = Integer.parseInt(dateArr[0]);
            var day = Integer.parseInt(dateArr[1]);
            var hour = Integer.parseInt(timeArr[0]);
            var minute = Integer.parseInt(timeArr[1]);
            if (month < 1 || month > 12 || day < 1 || day > 31 || hour < 0 || hour > 23
                    || minute < 0 || minute > 59) {
                return null;
            }

            date = new GregorianCalendar(2024, month - 1, day, hour, minute).getTime();
        } catch (NumberFormatException e) {
            return null;
        }

        return date;
    }

    private boolean isValidScheduleId(String scheduleId) {
        try {
            Integer.parseInt(scheduleId);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
