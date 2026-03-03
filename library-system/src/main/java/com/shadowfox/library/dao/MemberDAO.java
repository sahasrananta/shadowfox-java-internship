package com.shadowfox.library.dao;

import com.shadowfox.library.db.DatabaseConnection;
import com.shadowfox.library.model.Member;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    private Connection conn = DatabaseConnection.getInstance().getConnection();

    public boolean addMember(Member member) {
        String sql = "INSERT INTO members (name, email, phone) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setString(3, member.getPhone());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error adding member: " + e.getMessage());
            return false;
        }
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM members";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                members.add(new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching members: " + e.getMessage());
        }
        return members;
    }

    public Member getMemberById(int id) {
        String sql = "SELECT * FROM members WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Member(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone")
                );
            }
        } catch (SQLException e) {
            System.out.println("❌ Error fetching member: " + e.getMessage());
        }
        return null;
    }

    public boolean deleteMember(int id) {
        String sql = "DELETE FROM members WHERE id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("❌ Error deleting member: " + e.getMessage());
            return false;
        }
    }
}