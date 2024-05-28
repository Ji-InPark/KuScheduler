package org.example.service.schedule.friend;

import java.util.Scanner;
import org.example.entity.User;
import org.example.repository.FriendRepository;
import org.example.repository.UserRepository;

public class FriendDeleteService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
        printFriendDeleteMenu();
        var friendLoginId = scanner.nextLine().trim();
        var friend = UserRepository.getInstance().findUserByLoginId(friendLoginId);

        if (friend == null || !FriendRepository.getInstance().isFriend(user.id, friend.id)) {
            System.out.println("친구로 등록되어 있지 않습니다.");
            System.out.println("엔터키를 누르면 스케줄 화면으로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        FriendRepository.getInstance().deleteFriend(user.id, friend.id);
        System.out.println("친구 삭제가 완료되었습니다.");
        System.out.println("엔터키를 누르면 스케줄 화면으로 돌아갑니다.");
        scanner.nextLine();
    }

    private void printFriendDeleteMenu() {
        System.out.println("----------------------");
        System.out.println("   친구 삭제   ");
        System.out.println("----------------------");
        System.out.print("삭제하려는 친구의 ID를 입력하세요: ");
    }
}
