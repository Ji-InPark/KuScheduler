package org.example.service.schedule.friend;

import java.util.Scanner;
import org.example.entity.User;
import org.example.repository.FriendRepository;
import org.example.repository.UserRepository;

public class FriendAddService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
        printFriendAddMenu();
        var friendLoginId = scanner.nextLine().trim();
        var friend = UserRepository.getInstance().findUserByLoginId(friendLoginId);

        if (friend == null) {
            System.out.println("해당 ID의 사용자가 존재하지 않습니다.");
            System.out.println("엔터키를 누르면 스케줄 화면으로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        if (friend.id == user.id) {
            System.out.println("자기 자신을 친구로 추가할 수 없습니다.");
            System.out.println("엔터키를 누르면 스케줄 화면으로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        if (FriendRepository.getInstance().isFriend(user.id, friend.id)) {
            System.out.println("이미 친구로 등록되어 있습니다.");
            System.out.println("엔터키를 누르면 스케줄 화면으로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        FriendRepository.getInstance().addFriend(user.id, friend.id);
        System.out.println("친구 추가가 완료되었습니다.");
        System.out.println("엔터키를 누르면 스케줄 화면으로 돌아갑니다.");
        scanner.nextLine();
    }

    private void printFriendAddMenu() {
        System.out.println("----------------------");
        System.out.println("   친구 추가   ");
        System.out.println("----------------------");
        System.out.print("추가하려는 친구의 ID를 입력하세요: ");
    }
}
