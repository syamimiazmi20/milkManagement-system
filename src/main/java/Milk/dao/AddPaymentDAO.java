package Milk.dao;

import java.sql.*;
import Milk.connection.ConnectionManager;
import Milk.model.payment;

public class AddPaymentDAO {
    public void addPayment(payment newPayment) {
        String sql = "INSERT INTO payment (payment_date, payment_type) VALUES (?, ?)";
        
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            if (con != null) {
                System.out.println("Database connection established.");
                
                ps.setTimestamp(1, new java.sql.Timestamp(newPayment.getPayment_date().getTime()));
                ps.setString(2, newPayment.getPayment_type());
                
                int rowsAffected = ps.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
                
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int payid = generatedKeys.getInt(1);
                            System.out.println("FROM addpaymentDAO payid: " + payid);
                        }
                    }
                }
            } else {
                System.err.println("Failed to establish database connection.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}