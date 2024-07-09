package Milk.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Milk.connection.ConnectionManager;
import Milk.model.MilkPurchase;
import Milk.model.payment;

public class MilkPurchaseDAO {

    private Connection connection;

    public MilkPurchaseDAO(Connection connection) {
        this.connection = connection;
    }

    public List<MilkPurchase> getAllMilkPurchases() throws SQLException {
        List<MilkPurchase> purchases = new ArrayList<>();
        String query = "SELECT * FROM MILK_PURCHASE";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                MilkPurchase purchase = new MilkPurchase();
                purchase.setPurchaseID(resultSet.getInt("purchase_id"));
                purchase.setCustID(resultSet.getInt("cust_id"));
                purchase.setAmountLiter(resultSet.getDouble("amount_liter"));
                purchase.setTotalPrice(resultSet.getDouble("total_price"));
                purchases.add(purchase);
            }
        }
        return purchases;
    }

    public void addMilkPurchase(MilkPurchase purchase) throws SQLException {
        String query = "INSERT INTO MILK_PURCHASE (production_id, cust_id, amount_liter, total_price, payment_id, shipping_id) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, purchase.getProductionId());
            statement.setInt(2, purchase.getCustID());
            statement.setDouble(3, purchase.getAmountLiter());
            statement.setDouble(4, purchase.getTotalPrice());
            int payid = getLatestPayment();
            statement.setInt(5, payid);
            statement.setInt(6, purchase.getShipping_id());
            System.out.println("PaymentID: " + payid);

            statement.executeUpdate();
        }
    }

    public void deleteMilkPurchase(int purchase_id) throws SQLException {
        String query = "DELETE FROM MILK_PURCHASE WHERE purchase_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, purchase_id);
            statement.executeUpdate();
        }
    }

    public MilkPurchase getMilkPurchaseById(int purchase_id) throws SQLException {
        MilkPurchase purchase = null;
        String query = "SELECT * FROM MILK_PURCHASE WHERE purchase_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, purchase_id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    purchase = new MilkPurchase();
                    purchase.setPurchaseID(resultSet.getInt("purchase_id"));
                    purchase.setCustID(resultSet.getInt("cust_id"));
                    purchase.setAmountLiter(resultSet.getDouble("amount_liter"));
                    purchase.setTotalPrice(resultSet.getDouble("total_price"));
                }
            }
        }
        return purchase;
    }

    public void updateMilkPurchase(MilkPurchase purchase) throws SQLException {
        String query = "UPDATE MILK_PURCHASE SET cust_id = ?, amount_liter = ?, total_price = ? WHERE purchase_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, purchase.getCustID());
            statement.setDouble(2, purchase.getAmountLiter());
            statement.setDouble(3, purchase.getTotalPrice());
            statement.setInt(4, purchase.getPurchaseID());
            statement.executeUpdate();
        }
    }

    public static int getLatestPayment() throws SQLException {
        int payid = 0;
        String query = "SELECT payment_id FROM payment ORDER BY payment_id DESC LIMIT 1";
        try (Connection con = ConnectionManager.getConnection();
             PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                payid = rs.getInt("payment_id");
                System.out.println("get PaymentID: " + payid);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return payid;
    }

    public double getTotalSalesLast30Days() throws SQLException {
        String query = "SELECT SUM(mp.total_price) FROM milk_purchase mp " +
                       "JOIN payment p ON mp.payment_id = p.payment_id " +
                       "WHERE p.payment_date >= CURRENT_DATE - INTERVAL '30 days'";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return 0;
    }
}
