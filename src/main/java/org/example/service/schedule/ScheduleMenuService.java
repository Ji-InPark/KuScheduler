package org.example.service.schedule;

import java.util.Scanner;
import org.example.entity.User;
import org.example.service.schedule.cud.ScheduleCudService;
import org.example.service.schedule.friend.FriendAddService;
import org.example.service.schedule.friend.FriendService;
import org.example.service.schedule.read.ScheduleReadService;

public class ScheduleMenuService {

    private final Scanner scanner = new Scanner(System.in);

    public void start(User user) {
        while (true) {
            printScheduleMenu();
            int choice = getUserInput();
            switch (choice) {
                case 1:
                    new ScheduleReadService().start(user);
                    break;
                case 2:
                    new ScheduleCudService().start(user);
                    break;
                case 3:
                    new FriendService().start(user);
                    break;
                case 4:
                    new FriendAddService().start(user);
                    break;
                case 5:
                    System.out.println("정상적으로 로그아웃 되었습니다.");
                    System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
                    scanner.nextLine();
                    return;
                default:
                    System.out.println("1~5 사이의 값만 입력해주세요.");
                    System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
                    scanner.nextLine();
            }
        }
    }

    private void printScheduleMenu() {
        System.out.println("----------------------");
        System.out.println("       Scheduler      ");
        System.out.println("----------------------");
        System.out.println("1) 스케줄 조회 ");
        System.out.println("2) 스케줄 추가/변경/삭제/반복 ");
        System.out.println("3) 친구 목록 ");
        System.out.println("4) 친구 추가 ");
        System.out.println("5) 로그아웃 ");
        System.out.print("메뉴를 선택해주세요: ");
    }

    private int getUserInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
