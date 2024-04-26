package org.example.service.schedule.read;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import org.example.entity.User;
import org.example.respository.ScheduleRepository;
import org.example.service.schedule.common.PrintService;

public class ScheduleReadByDateService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public boolean start(User user) {
        this.user = user;
        while (true) {
            printScheduleReadByDateMenu();
            int choice = getUserInput();
            switch (choice) {
                case 1:
                    return readByMonth();
                case 2:
                    return readByDay();
                case 3:
                    return false;
                case 4:
                    return true;
                default:
                    System.out.println("1,2,b,q 값중 하나만 입력해주세요.");
                    System.out.println("엔터키를 누르면 스케줄 조회 메뉴로 돌아갑니다.");
                    scanner.nextLine();
            }
        }
    }

    private boolean readByDay() {
        do {
            System.out.println("* 'b'를 입력시 스케줄 조회 메뉴로 돌아갑니다.");
            System.out.println("* 'q'를 입력시 스케줄 메뉴로 돌아갑니다.");
            System.out.print("검색할 일을 입력해주세요(ex: 12 25): ");
            var input = scanner.nextLine().trim();

            if (input.equals("b")) {
                return false;
            }

            if (input.equals("q")) {
                return true;
            }

            var arr = input.split(" ");
            if (arr.length != 2) {
                System.out.println("올바른 형식으로 입력해주세요.");
                continue;
            }

            var month = Integer.parseInt(arr[0]);
            var day = Integer.parseInt(arr[1]);

            if (month < 1 || month > 12 || day < 1 || day > 31) {
                System.out.println("1~12월, 1~31일 사이의 값을 입력해주세요.");
                continue;
            }

            Date date;
            try {
                date = new GregorianCalendar(2024, month - 1, day).getTime();
            } catch (IllegalArgumentException e) {
                System.out.println("올바른 날짜를 입력해주세요.");
                continue;
            }

            var schedules = ScheduleRepository.getInstance()
                    .findAllByUserIdAndDate(user.id, date);
            if (schedules.isEmpty()) {
                System.out.println(month + "월 " + day + "일에 스케줄이 없습니다.");
                continue;
            }

            var subject = String.format(
                    "|%33s%02d月 %02d日%34s|",
                    "", month, day, "");
            PrintService.getInstance().printScheduleHeaderWithUserInfo(user, subject);
            PrintService.getInstance().printSchedules(schedules);
        } while (true);
    }

    private boolean readByMonth() {
        do {
            System.out.println("* 'b'를 입력시 스케줄 조회 메뉴로 돌아갑니다.");
            System.out.println("* 'q'를 입력시 스케줄 메뉴로 돌아갑니다.");
            System.out.print("검색할 월을 입력해주세요(1~12): ");
            var input = scanner.nextLine().trim();

            if (input.equals("b")) {
                return false;
            }

            if (input.equals("q")) {
                return true;
            }

            try {
                var month = Integer.parseInt(input);
                if (month < 1 || month > 12) {
                    System.out.println("1~12 사이의 값을 입력해주세요.");
                    continue;
                }

                var schedules = ScheduleRepository.getInstance()
                        .findAllByUserIdAndMonth(user.id, month);
                if (schedules.isEmpty()) {
                    System.out.println(month + "월에 스케줄이 없습니다.");
                    continue;
                }

                var subject = String.format(
                        "|%36s%02d月%36s|", "", month, "");
                PrintService.getInstance().printScheduleHeaderWithUserInfo(user, subject);
                PrintService.getInstance().printSchedules(schedules);
            } catch (NumberFormatException e) {
                System.out.println("입력한 값이 잘못되었습니다.");
            }
        } while (true);
    }

    private void printScheduleReadByDateMenu() {
        System.out.println("1) 월별 검색");
        System.out.println("2) 일별 검색");
        System.out.println("* 'b'를 입력시 스케줄 조회 메뉴로 돌아갑니다.");
        System.out.println("* 'q'를 입력시 스케줄 메뉴로 돌아갑니다.");
        System.out.print("메뉴를 선택해주세요: ");
    }

    private int getUserInput() {
        try {
            var input = scanner.nextLine().trim();
            if (input.equals("b")) {
                return 3;
            }
            if (input.equals("q")) {
                return 4;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
