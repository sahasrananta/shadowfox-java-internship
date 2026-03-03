package com.shadowfox.library.model;

public class Member {
    private int id;
    private String name;
    private String email;
    private String phone;

    public Member(int id, String name, String email, String phone) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public Member(String name, String email, String phone) {
        this(0, name, email, phone);
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return String.format("| %-4d | %-20s | %-25s | %-15s |",
                id, name, email, phone);
    }
}