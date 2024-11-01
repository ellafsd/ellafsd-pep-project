package DAO;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public Message saveMessage(Message message) {
        // Insert message into database and return it

        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
            stmt.setInt(1, message.getPosted_by());
            stmt.setString(2, message.getMessage_text());
            stmt.setLong(3, message.getTime_posted_epoch());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                message.setMessage_id(rs.getInt(1));
            }
            return message;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;

    }

    public List<Message> getAllMessages() {
        // Retrieve all messages from the database

         List<Message> messages = new ArrayList<>();
    String sql = "SELECT * FROM Message";
    try (Connection conn = ConnectionUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        
        while (rs.next()) {
            Message message = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
            );
            messages.add(message);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return messages;


    }

    public Message getMessageById(int messageId) {
        // Retrieve message by message_id

        String sql = "SELECT * FROM Message WHERE message_id = ?";
    try (Connection conn = ConnectionUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, messageId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }

    public void deleteMessageById(int messageId) {
        // Delete message by message_id

        String sql = "DELETE FROM Message WHERE message_id = ?";
    try (Connection conn = ConnectionUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setInt(1, messageId);
        stmt.executeUpdate();
    } catch (SQLException e) {
        e.printStackTrace();
    }

    }

    public Message updateMessage(int messageId, String newText) {
        // Update message text in the database and return it

        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
    try (Connection conn = ConnectionUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, newText);
        stmt.setInt(2, messageId);
        int rowsUpdated = stmt.executeUpdate();

        if (rowsUpdated > 0) {
            return getMessageById(messageId); // Return the updated message
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }

    public List<Message> getMessagesByUserId(int accountId) {
        // Retrieve messages by account_id

        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                );
                messages.add(message);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
    
}
