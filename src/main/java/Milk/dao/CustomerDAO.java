package Milk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import Milk.model.Customer;
import Milk.connection.ConnectionManager;

public class CustomerDAO {

    public boolean registerCustomer(String name, String email, String password, String address, String phone) {
        String query = "INSERT INTO CUSTOMERS (cust_name, cust_email, cust_password, cust_address, cust_phoneNum) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, name);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, address);
            ps.setString(5, phone);
            
            int rowCount = ps.executeUpdate();
            return rowCount > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean authenticateCustomer(String email, String password) {
        String query = "SELECT * FROM CUSTOMERS WHERE cust_email = ? AND cust_password = ?";
        
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, email);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Customer getCustomerByEmail(String email) {
        String query = "SELECT * FROM CUSTOMERS WHERE cust_email = ?";
        
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(query)) {
            
            ps.setString(1, email);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Customer(
                        rs.getInt("cust_id"),
                        rs.getString("cust_name"),
                        rs.getString("cust_address"),
                        email,
                        rs.getString("cust_password"),
                        rs.getString("cust_phoneNum")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}