package org.example.service.schedule.repeat;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import org.apache.commons.lang3.time.DateUtils;
import org.example.entity.User;
import org.example.repository.ScheduleRepository;

public class ScheduleMonthlyCreateService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public boolean start(User user) {
        this.user = user;
        System.out.println(
                "(일정 제목은 한글, 알파벳 대소문자('a'-'z', 'A'-'Z')와 숫자('0'-'9')로 조합하여 15자 이내로 작성해주세요!)");
        System.out.println("========================================");

        System.out.print("반복할 일정 제목을 입력해주세요: ");
        var title = scanner.nextLine().trim();
        if (!isValidTitle(title)) {
            System.out.println("Error! 스케줄 이름이 형식에 맞게 입력되지 않았습니다.");
            System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
            scanner.nextLine();
            return false;
        }

        System.out.print("반복할 스케줄의 시작 날짜와 시간(예: 4/22 15:30)을 입력해주세요: ");
        var startDateInput = scanner.nextLine().trim();
        var startDate = parseDateAndValidate(startDateInput);
        if (startDate == null) {
            System.out.println("Error! 시작 날짜 및 시간이 형식에 맞게 입력되지 않았습니다.");
            System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
            scanner.nextLine();
            return false;
        }

        System.out.print("반복할 스케줄의 종료 날짜와 시간(예: 4/22 15:30)을 입력해주세요: ");
        var endDateInput = scanner.nextLine().trim();
        var endDate = parseDateAndValidate(endDateInput);
        if (endDate == null) {
            System.out.println("Error! 종료 날짜 및 시간이 형식에 맞게 입력되지 않았습니다.");
            System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
            scanner.nextLine();
            return false;
        }

        if (startDate.after(endDate)) {
            System.out.println("Error! 종료 시간이 시작 시간보다 앞서있습니다.");
            System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
            scanner.nextLine();
            return false;
        }

        if (startDate.getMonth() != endDate.getMonth()) {
            System.out.println("Error! 시작 날짜과 종료 날짜가 같은 달이어야 합니다.");
            System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
            scanner.nextLine();
            return false;
        }

        System.out.print("반복할 스케줄의 중요도를 입력해주세요: ");
        var priorityInput = scanner.nextLine().trim();
        if (!isValidPriority(priorityInput)) {
            System.out.println("Error! 중요도가 형식에 맞게 입력되지 않았습니다.");
            System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
            scanner.nextLine();
            return false;
        }
        var priority = Integer.parseInt(priorityInput);

        var validSchedule = isValidSchedule(startDate, endDate, priority);

        if (validSchedule == null) {
            System.out.println("Error! 반복이 불가능한 날짜가 포함되어 있습니다.");
            System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
            scanner.nextLine();
            return false;
        }

        if (!validSchedule) {
            System.out.println("Error! 해당 기간에 중복된 중요도의 스케줄이 이미 존재합니다.");
            System.out.println("기존 일정의 중요도를 수정하시려면 변경창에서 수정해주시길 바랍니다.");
            System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
            scanner.nextLine();
            return false;
        }

        do {
            System.out.print("추가한 일정을 저장하시겠습니까? (예: 1, 아니오: 2): ");
            var saveInput = scanner.nextLine().trim();
            try {
                var save = Integer.parseInt(saveInput);
                if (save == 1) {
                    var maxRepeatedId = ScheduleRepository.getInstance().getMaxRepeatedId();

                    while (1900 + endDate.getYear() == 2024) {
                        ScheduleRepository.getInstance()
                                .addSchedule(title, startDate, endDate, priority, user.id,
                                        maxRepeatedId + 1);
                        startDate = DateUtils.addMonths(startDate, 1);
                        endDate = DateUtils.addMonths(endDate, 1);
                    }
                    System.out.println("성공적으로 저장되었습니다.");
                    return false;
                } else if (save == 2) {
                    System.out.println("저장이 취소되었습니다.");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Error! 형식에 맞게 입력해주세요.");
                continue;
            }

            System.out.println("Error! 형식에 맞게 입력해주세요.");
        } while (true);
    }

    private Boolean isValidSchedule(Date startDate, Date endDate, int priority) {
        if (priority != 3) {
            return true;
        }
        var monthFormat = new SimpleDateFormat("MM");
        var dayFormat = new SimpleDateFormat("dd");

        if (monthFormat.format(endDate).equals("02")
                && Integer.parseInt(dayFormat.format(endDate)) > 29) {
            return null;
        }

        if (Integer.parseInt(monthFormat.format(endDate)) < 12
                && Integer.parseInt(dayFormat.format(endDate)) > 30) {
            return null;
        }

        var sdf = new SimpleDateFormat("ddHHmmss");

        return ScheduleRepository.getInstance().findAllByUserIdAndPriority(user.id, priority)
                .stream()
                .filter(schedule -> {
                            var start = Integer.parseInt(sdf.format(startDate));
                            var end = Integer.parseInt(sdf.format(endDate));
                            var scheduleStart = Integer.parseInt(sdf.format(schedule.startDate));
                            var scheduleEnd = Integer.parseInt(sdf.format(schedule.endDate));
                            return startDate.before(schedule.endDate) && (start >= scheduleStart
                                    && start <= scheduleEnd)
                                    || (end >= scheduleStart && end <= scheduleEnd)
                                    || (start <= scheduleStart && end >= scheduleEnd);
                        }
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

    private boolean isValidTitle(String title) {
        return title.matches("^[a-zA-Z0-9가-힣 ]{1,15}$");
    }
}
