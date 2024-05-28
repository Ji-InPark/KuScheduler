package org.example.service.schedule.friend;

import java.util.Scanner;
import org.example.entity.User;
import org.example.repository.FriendRepository;
import org.example.repository.UserRepository;
import org.example.service.schedule.common.PrintService;

public class FriendService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
        printAllFriends();
        printFriendMenu();

        try {
            var input = scanner.nextLine().trim();
            switch (input) {
                case "1":
                    new FriendScheduleReadService().start(user);
                    break;
                case "2":
                    new FriendDeleteService().start(user);
                    break;
                default:
                    System.out.println("1,2 값중 하나만 입력해주세요.");
                    System.out.println("엔터키를 누르면 스케줄 메뉴로 돌아갑니다.");
                    scanner.nextLine();
            }
        } catch (NumberFormatException e) {
        }
    }

    private void printAllFriends() {
        var friendIds = FriendRepository.getInstance().findAllByUserId(user.id).stream()
                .map(friend -> friend.friendId).toList();
        var friends = UserRepository.getInstance().findAllByIds(friendIds);

        PrintService.getInstance().printFriendHeader();
        PrintService.getInstance().printFriends(friends);
    }

    private void printFriendMenu() {
        System.out.println("1) 친구 스케줄 조회 ");
        System.out.println("2) 친구 삭제 ");
        System.out.print("메뉴를 선택해주세요: ");
    }

}
