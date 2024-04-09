package org.example.service.main;

import java.util.Scanner;
import org.example.respository.UserRepository;
import org.example.service.schedule.ScheduleMenuService;

public class LoginService {

    private final Scanner scanner = new Scanner(System.in);

    public void start() {
        printLoginMenu();

        String id = getLoginId();
        String password = getLoginPassword();

        var user = UserRepository.getInstance().findUserByLoginId(id);

        if (user == null) {
            System.out.println("등록되지 않은 아이디입니다.");
            System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        if (!user.password.equals(password)) {
            System.out.println("비밀번호가 일치하지 않습니다.");
            System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
            scanner.nextLine();
            return;
        }

        new ScheduleMenuService().start(user);
    }

    private void printLoginMenu() {
        System.out.println("<로그인>");
        System.out.println("----------------------");
    }

    private String getLoginId() {
        System.out.print("아이디: ");
        return scanner.nextLine().trim();
    }

    private String getLoginPassword() {
        var console = System.console();
        if (console == null) {
            System.out.print("비밀번호: ");
            return scanner.nextLine().trim();
        }

        return new String(console.readPassword("비밀번호: "));
    }
}
