package org.example.service.schedule.friend;

import java.util.Scanner;
import org.example.entity.User;

public class FriendAddService {

    private final Scanner scanner = new Scanner(System.in);
    User user;

    public void start(User user) {
        this.user = user;
    }

}
