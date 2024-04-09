package org.example.respository;

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
import org.example.entity.User;

public class UserRepository {

    static UserRepository instance;
    HashMap<Integer, User> userInfos = new HashMap<>();

    private UserRepository() {
        initUserInfos();
    }

    public static UserRepository getInstance() {
        if (instance == null) {
            instance = new UserRepository();
        }
        return instance;
    }

    public User findUserByLoginId(String loginId) {
        return userInfos.values().stream()
                .filter(user -> user.loginId.equals(loginId))
                .findFirst().orElse(null);
    }

    public void addUser(String name, String loginId, String password) {
        var maxId = userInfos.keySet().stream()
                .max(Integer::compare)
                .orElse(0);
        var user = new User(maxId + 1, name, loginId, password);
        userInfos.put(user.id, user);
        saveUserInfos();
    }

    public boolean isDuplicateLoginId(String loginId) {
        return userInfos.values().stream()
                .anyMatch(user -> user.loginId.equals(loginId));
    }

    private void initUserInfos() {
        try {
            var file = new File("UserData.csv");
            file.createNewFile();
            var fileInputStream = new FileInputStream(file);
            var br = new BufferedReader(new InputStreamReader(fileInputStream));

            String line;
            while ((line = br.readLine()) != null) {
                var data = line.split(",");
                var user = new User(Integer.parseInt(data[0]), data[1], data[2], data[3]);
                userInfos.put(user.id, user);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveUserInfos() {
        try {
            var file = new File("UserData.csv");

            clearFile(file);
            var fileOutputStream = new FileOutputStream(file);
            var bw = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

            for (var user : userInfos.values()) {
                bw.write(user.convertToCsvRow());
                bw.newLine();
            }
            bw.close();
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
}
