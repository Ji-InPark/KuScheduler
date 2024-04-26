package org.example.service.schedule.read;

import java.util.Scanner;
import org.example.entity.User;
import org.example.respository.ScheduleRepository;
import org.example.service.schedule.common.PrintService;

public class ScheduleReadByPriorityService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public boolean start(User user) {
        this.user = user;
        while (true) {
            printScheduleReadByPriorityMenu();
            var input = scanner.nextLine().trim();
            if (input.equals("b")) {
                return false;
            }

            if (input.equals("q")) {
                return true;
            }

            try {
                var priority = Integer.parseInt(input);
                if (priority < 1 || priority > 3) {
                    System.out.println("입력한 값이 잘못되었습니다.");
                    continue;
                }

                var schedules = ScheduleRepository.getInstance()
                        .findAllByUserIdAndPriority(user.id, priority);

                if (schedules.isEmpty()) {
                    System.out.println("스케줄이 없습니다.");
                    continue;
                }

                var subject = String.format(
                        "|%32s중요도: %-6s%30s|",
                        "",
                        priorityString(priority),
                        "");
                PrintService.getInstance().printScheduleHeaderWithUserInfo(user, subject);
                PrintService.getInstance().printSchedules(schedules);
            } catch (NumberFormatException e) {
                System.out.println("입력한 값이 잘못되었습니다.");
            }

        }
    }

    private void printScheduleReadByPriorityMenu() {
        System.out.println("* 'b'를 입력시 스케줄 조회 메뉴로 돌아갑니다.");
        System.out.println("* 'q'를 입력시 스케줄 메뉴로 돌아갑니다.");
        System.out.print("검색하려는 중요도를 입력하세요: ");
    }

    private String priorityString(int priority) {
        switch (priority) {
            case 1:
                return "★";
            case 2:
                return "★★";
            default:
                return "★★★";
        }

    }

}
