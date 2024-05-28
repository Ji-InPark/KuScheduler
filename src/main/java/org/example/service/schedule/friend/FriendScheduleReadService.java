package org.example.service.schedule.friend;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;
import org.example.entity.User;
import org.example.repository.FriendRepository;
import org.example.repository.ScheduleRepository;
import org.example.repository.UserRepository;
import org.example.service.schedule.common.PrintService;

public class FriendScheduleReadService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
        printFriendScheduleMenu();
        var friendLoginId = scanner.nextLine().trim();
        var friend = UserRepository.getInstance().findUserByLoginId(friendLoginId);
        if (!FriendRepository.getInstance().isFriend(user.id, friend.id)) {
            System.out.println("등록되지 않은 친구입니다.");
            System.out.println("엔터키를 누르면 스케줄 메뉴로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        System.out.print("조회할 날짜를 입력하세요(예: 4/30): ");
        var inputDate = scanner.nextLine().trim();
        var dateArray = inputDate.split("/");
        if (dateArray.length != 2) {
            System.out.println("날짜 입력 오류");
            System.out.println("엔터키를 누르면 스케줄 메뉴로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        var month = Integer.parseInt(dateArray[0]);
        var day = Integer.parseInt(dateArray[1]);

        Date date;
        try {
            date = new GregorianCalendar(2024, month - 1, day).getTime();
        } catch (IllegalArgumentException e) {
            System.out.println("날짜 입력 오류");
            System.out.println("엔터키를 누르면 스케줄 메뉴로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        var schedules = ScheduleRepository.getInstance()
                .findAllByUserIdAndDate(friend.id, date);
        var subject = String.format(
                "|%-" + (69 - friend.getKoreanLength()) + "s|",
                StringUtils.center(
                        friend.name + "(" + friend.loginId + ")님의 " + month + "월 " + day + "일 스케줄",
                        69 - friend.getKoreanLength()));
        PrintService.getInstance().printScheduleHeaderWithSubject(subject);
        PrintService.getInstance().printSchedules(schedules);

        System.out.println("엔터키를 누르면 스케줄 메뉴로 돌아갑니다.");
        scanner.nextLine();
    }

    private void printFriendScheduleMenu() {
        System.out.println("----------------------");
        System.out.println("   친구 스케줄 조회   ");
        System.out.println("----------------------");
        System.out.print("스케줄을 조회할 친구의 ID를 입력하세요: ");
    }

}
