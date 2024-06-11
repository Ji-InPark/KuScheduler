package org.example.service.schedule.cud;

import java.util.Scanner;
import org.example.entity.User;
import org.example.service.schedule.repeat.ScheduleRepeatService;

public class ScheduleCudService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
        while (true) {
            printScheduleCudMenu();
            try {
                var input = scanner.nextLine().trim();
                if (input.equals("q")) {
                    return;
                }

                var inputNum = Integer.parseInt(input);
                switch (inputNum) {
                    case 1:
                        if (new ScheduleCreateService().start(user)) {
                            return;
                        }
                        break;
                    case 2:
                        new ScheduleUpdateService().start(user);
                        break;
                    case 3:
                        new ScheduleDeleteService().start(user);
                        break;
                    case 4:
                        if (!new ScheduleRepeatService().start(user)) {
                            return;
                        }
                        break;
                    default:
                        System.out.println("1,2,3,4,q 값중 하나만 입력해주세요.");
                        System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
                        scanner.nextLine();
                }
            } catch (NumberFormatException e) {
                System.out.println("1,2,3,4,q 값중 하나만 입력해주세요.");
                System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
                scanner.nextLine();
            }
        }
    }

    private void printScheduleCudMenu() {
        System.out.println("----------------------");
        System.out.println("   스케줄 추가/변경/삭제/반복   ");
        System.out.println("----------------------");
        System.out.println("1) 스케줄 추가 ");
        System.out.println("2) 스케줄 변경 ");
        System.out.println("3) 스케줄 삭제 ");
        System.out.println("4) 스케줄 반복 ");
        System.out.print("메뉴를 입력하세요 (q를 입력하면 스케줄메뉴로 돌아갑니다.): ");
    }
}
