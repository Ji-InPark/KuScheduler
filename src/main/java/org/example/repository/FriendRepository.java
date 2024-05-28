package org.example.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import org.example.entity.Friend;

public class FriendRepository {

    private static final FriendRepository instance = new FriendRepository();
    HashMap<Integer, Friend> friends = new HashMap<>();

    private FriendRepository() {
        initSchedules();
    }

    public static FriendRepository getInstance() {
        return instance;
    }

    public List<Friend> findAllByUserId(int userId) {
        return friends.values().stream()
                .filter(friend -> friend.userId == userId)
                .toList();
    }

    public void saveFriend() {
        try {
            var file = new File("FriendData.csv");
            clearFile(file);

            var fileOutputStream = new FileOutputStream(file);
            var bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            for (var schedule : friends.values()) {
                bw.write(schedule.convertToCsvRow());
                bw.newLine();
            }

            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initSchedules() {
        try {
            var file = new File("FriendData.csv");
            file.createNewFile();
            var fileInputStream = new FileInputStream(file);
            var br = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = br.readLine()) != null) {
                var data = line.split(",");
                var friend = new Friend(Integer.parseInt(data[0]), Integer.parseInt(data[1]),
                        Integer.parseInt(data[2]));
                friends.put(friend.id, friend);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFile(File file) {
        try {
            new FileWriter(file, false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isFriend(int id, int friendId) {
        return friends.values().stream()
                .anyMatch(friend -> friend.userId == id && friend.friendId == friendId);
    }
}
