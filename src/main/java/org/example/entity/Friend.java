package org.example.entity;

public class Friend {

    public int id;
    public int userId;
    public int friendId;

    public Friend(int id, int userId, int friendId) {
        this.id = id;
        this.userId = userId;
        this.friendId = friendId;
    }

    public String convertToCsvRow() {
        return id + "," + userId + "," + friendId;
    }
}
