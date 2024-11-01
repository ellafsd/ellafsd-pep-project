package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    public Account getAccountByUsername(String username) {
        // Retrieve account by username

        String sql = "SELECT * FROM Account WHERE username = ?";
    try (Connection conn = ConnectionUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return new Account(
                    rs.getInt("account_id"),
                    rs.getString("username"),
                    rs.getString("password")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }

    public Account saveAccount(Account account) {
        // Insert account into database and return it

        String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
    try (Connection conn = ConnectionUtil.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        
        stmt.setString(1, account.getUsername());
        stmt.setString(2, account.getPassword());
        stmt.executeUpdate();

        // Retrieve generated account_id
        ResultSet rs = stmt.getGeneratedKeys();
        if (rs.next()) {
            account.setAccount_id(rs.getInt(1));
        }
        return account;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
    }
}
