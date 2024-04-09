package org.example.service.main;

import java.util.Scanner;
import org.example.respository.UserRepository;

public class SignUpService {

    Scanner scanner = new Scanner(System.in);

    public void start() {
        printSignUpMenu();

        var name = getUserName();
        var id = getUserId();
        var password = getUserPassword();
        UserRepository.getInstance().addUser(name, id, password);

        System.out.println("회원 가입에 성공했습니다.");
        System.out.println("엔터키를 누르면 메인메뉴로 돌아갑니다.");
        scanner.nextLine();
    }

    private boolean isValidName(String name) {
        if (name.isEmpty() || name.length() > 15) {
            System.out.println("이름은 1자 이상 15자 이하로 입력해주세요.");
            return false;
        }

        if (name.matches(".*\\d.*")) {
            System.out.println("이름에 숫자가 포함되어 있습니다.");
            return false;
        }

        if (name.matches(".*[!@#$%^&*()].*")) {
            System.out.println("이름에 특수문자가 포함되어 있습니다.");
            return false;
        }

        if (name.matches(".*[A-Z].*")) {
            System.out.println("이름에 대문자가 포함되어 있습니다.");
            return false;
        }

        return true;
    }

    private boolean isValidId(String id) {
        if (id.length() < 3 || id.length() > 10) {
            System.out.println("아이디는 4자 이상 10자 이하로 입력해주세요.");
            return false;
        }

        if (!id.matches(".*[0-9a-zA-Z].*")) {
            System.out.println("아이디는 영문자와 숫자로만 입력해주세요.");
            return false;
        }

        if (UserRepository.getInstance().isDuplicateLoginId(id)) {
            System.out.println("이미 사용중인 아이디입니다.");
            return false;
        }

        return true;
    }

    private boolean isValidPassword(String password) {
        if (password.length() < 8 || password.length() > 16) {
            System.out.println("비밀번호는 8자 이상 16자 이하로 입력해주세요.");
            return false;
        }

        if (!password.matches(".*[0-9].*")) {
            System.out.println("비밀번호는 숫자를 포함해야 합니다.");
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            System.out.println("비밀번호는 소문자를 포함해야 합니다.");
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            System.out.println("비밀번호는 대문자를 포함해야 합니다.");
            return false;
        }

        if (!password.matches(".*[!@#$%^&*()].*")) {
            System.out.println("비밀번호는 특수문자를 포함해야 합니다.");
            return false;
        }

        return true;
    }

    private void printSignUpMenu() {
        System.out.println("<회원가입>");
        System.out.println("----------------------");
    }

    private String getUserName() {
        String name;
        do {
            System.out.print("이름: ");
            name = scanner.nextLine().trim();
        } while (!isValidName(name));
        return name;
    }

    private String getUserId() {
        String id;
        do {
            System.out.print("아이디: ");
            id = scanner.nextLine().trim();
        } while (!isValidId(id));
        return id;
    }

    private String getUserPassword() {
        var console = System.console();
        if (console == null) {
            System.out.print("비밀번호: ");
            var password = new Scanner(System.in).nextLine();
            return password;
        }

        String password;
        do {
            password = new String(console.readPassword("비밀번호: ")).trim();
        } while (!isValidPassword(password));
        return password;
    }
}
