package org.example.service.schedule.repeat;

import java.util.Scanner;
import org.example.entity.User;

public class ScheduleRepeatService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public boolean start(User user) {
        this.user = user;
        while (true) {
            printScheduleRepeatMenu();
            try {
                var input = scanner.nextLine().trim();
                switch (input) {
                    case "1":
                        new ScheduleDailyCreateService().start(user);
                        break;
                    case "2":
                        new ScheduleWeeklyCreateService().start(user);
                        break;
                    case "3":
                        new ScheduleMonthlyCreateService().start(user);
                        break;
                    case "q":
                        return true;
                    default:
                        System.out.println("1~3 사이의 값만 입력해주세요.");
                        System.out.println("엔터키를 누르면 메인 메뉴로 돌아갑니다.");
                        scanner.nextLine();
                        return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("1,2,3,q 값중 하나만 입력해주세요.");
                System.out.println("엔터키를 누르면 스케줄 추가/변경/삭제 메뉴로 돌아갑니다.");
                scanner.nextLine();
            }
        }
    }

    private void printScheduleRepeatMenu() {
        System.out.println("----------------------");
        System.out.println("   스케줄 반복   ");
        System.out.println("----------------------");
        System.out.println("1) 매일 반복 ");
        System.out.println("2) 매주 반복 ");
        System.out.println("3) 매월 반복 ");
        System.out.print("해당하는 번호를 입력하세요: ");
    }

}
