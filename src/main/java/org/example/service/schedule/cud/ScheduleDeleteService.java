package org.example.service.schedule.cud;

import java.util.Scanner;
import org.example.entity.User;
import org.example.respository.ScheduleRepository;

public class ScheduleDeleteService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
        System.out.println("<스케줄 삭제>");
        System.out.println("----------------------------");
        System.out.print("삭제할 스케줄 id를 입력해주세요: ");
        var scheduleId = scanner.nextLine().trim();
        if (!isValidScheduleId(scheduleId)) {
            System.out.println("Error! 스케줄 id가 형식에 맞게 입력되지 않았습니다.");
            System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
            scanner.nextLine();
            return;
        }
        var id = Integer.parseInt(scheduleId);
        var schedule = ScheduleRepository.getInstance().findByIdAndUserId(id, user.id);
        if (schedule == null) {
            System.out.println("존재하지 않는 스케줄입니다.");
            System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        ScheduleRepository.getInstance().deleteSchedule(id);
        System.out.printf("'%s' 스케줄이 삭제되었습니다.\n", schedule.name);
        System.out.println("----------------------------");
        System.out.println("엔터키를 누르면 이전 화면으로 돌아갑니다.");
        scanner.nextLine();
    }

    private boolean isValidScheduleId(String scheduleId) {
        return scheduleId.matches("\\d+");
    }
}
