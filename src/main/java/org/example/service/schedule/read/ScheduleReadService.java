package org.example.service.schedule.read;

import java.util.Scanner;
import org.example.entity.User;
import org.example.respository.ScheduleRepository;
import org.example.service.schedule.common.PrintService;

public class ScheduleReadService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
        while (true) {
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
                    if (new ScheduleReadByPriorityService().start(user)) {
                        return;
                    }
                    break;
                case 3:
                    return;
                default:
                    System.out.println("1,2,q 값중 하나만 입력해주세요.");
                    System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
                    scanner.nextLine();
            }
        }
    }

    public void printAllSchedule() {
        var schedules = ScheduleRepository.getInstance().findAllByUserId(user.id);
        PrintService.getInstance().printScheduleHeaderWithUserInfo(user, null);
        PrintService.getInstance().printSchedules(schedules);
    }

    private void printScheduleMenu() {
        System.out.println("1) 날짜별 스케줄 조회 ");
        System.out.println("2) 중요도별 스케줄 조회 ");
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
