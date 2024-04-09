package org.example.service.schedule.read;

import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.User;
import org.example.respository.ScheduleRepository;

public class ScheduleReadService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
        printAllSchedule();
        printScheduleMenu();
        int choice = getUserInput();
        switch (choice) {
            case 1:
                if (new ScheduleReadByDateService().start(user)) {
                    return;
                }
                break;
            case 2:
                new ScheduleReadByPriorityService().start(user);
                break;
            case 3:
                return;
            default:
                System.out.println("1,2,q 값중 하나만 입력해주세요.");
                System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
                scanner.nextLine();
        }
    }

    public void printAllSchedule() {
        printAllScheduleHeader();
        printAllScheduleContent();
    }

    private void printAllScheduleHeader() {
        printHorizontalLine();
        System.out.printf("|%-23s%-29s|\n", user.name + "님 환영합니다.", "");
        printHorizontalLine();
        System.out.println("|                        스 케 줄                         |");
        printHorizontalLine();
        System.out.printf("|%-4s|%-15s|%-23s|%-5s|\n",
                StringUtils.center("ID", 4),
                StringUtils.center("스케줄명", 15),
                StringUtils.center("날짜/시간", 23),
                StringUtils.center("중요도", 5)
        );
        printHorizontalLine();
    }

    private void printAllScheduleContent() {
        var schedules = ScheduleRepository.getInstance().findAllByUserId(user.id);
        if (schedules.isEmpty()) {
            System.out.println("|                    스케줄이 없습니다.                      |");
            printHorizontalLine();
            return;
        }
        for (var schedule : schedules) {
            System.out.printf("|%-4s|%-15s|%-23s|%-7s|\n",
                    StringUtils.center(String.valueOf(schedule.id), 4),
                    StringUtils.center(schedule.name, 15),
                    StringUtils.center(schedule.getStartDate() + " ~ " + schedule.getEndDate(), 25),
                    StringUtils.center(String.valueOf(schedule.priority), 7)
            );
        }
        printHorizontalLine();
    }

    private void printHorizontalLine() {
        System.out.println("---------------------------------------------------------");
    }

    private void printScheduleMenu() {
        System.out.println("1) 날짜별 스케쥴 조회 ");
        System.out.println("2) 중요도별 스케쥴 조회 ");
        System.out.println("* 'q'를 입력시 스케줄 메뉴로 돌아갑니다.");
        System.out.print("메뉴를 선택해주세요: ");
    }

    private int getUserInput() {
        try {
            var input = scanner.nextLine().trim();
            if (input.equals("q")) {
                return 3;
            }
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
