package org.example.service.main;

import java.util.Scanner;

public class MainMenuService {

    Scanner scanner = new Scanner(System.in);

    public void start() {
        while (true) {
            printMainMenu();
            int choice = getUserInput();
            switch (choice) {
                case 1:
                    new SignUpService().start();
                    break;
                case 2:
                    new LoginService().start();
                    break;
                case 3:
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("1~3 사이의 값만 입력해주세요.");
                    System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
                    scanner.nextLine();
            }
        }
    }

    private void printMainMenu() {
        System.out.println("----------------------");
        System.out.println("       Scheduler      ");
        System.out.println("----------------------");
        System.out.println("1) 회원가입 ");
        System.out.println("2) 로그인 ");
        System.out.println("3) 종료 ");
        System.out.print("메뉴를 입력하세요: ");
    }

    private int getUserInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
