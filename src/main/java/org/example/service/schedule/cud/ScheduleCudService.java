package org.example.service.schedule.cud;

import java.util.Scanner;
import org.example.entity.User;

public class ScheduleCudService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
        while (true) {
            printScheduleCudMenu();
            int choice = getUserInput();
            switch (choice) {
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
                    return;
                default:
                    System.out.println("1,2,3,q 값중 하나만 입력해주세요.");
                    System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
                    scanner.nextLine();
            }
        }
    }

    private void printScheduleCudMenu() {
        System.out.println("----------------------");
        System.out.println("   스케줄 변경/추가/삭제   ");
        System.out.println("----------------------");
        System.out.println("1) 스케줄 추가 ");
        System.out.println("2) 스케줄 변경 ");
        System.out.println("3) 스케줄 삭제 ");
        System.out.print("메뉴를 입력하세요 (q를 입력하면 스케줄메뉴로 돌아갑니다.): ");
    }

    private int getUserInput() {
        try {
            var input = scanner.nextLine().trim();
            if (input.equals("q")) {
                return 4;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
