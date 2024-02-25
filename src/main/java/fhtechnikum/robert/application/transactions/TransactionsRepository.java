package fhtechnikum.robert.application.transactions;

import fhtechnikum.robert.application.data.Database;
import fhtechnikum.robert.system.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TransactionsRepository extends Repository {
    public boolean buyPackage(String username) {
        int pack_ID = 0;
        if ((pack_ID = packsAvailable()) != 0) {
            String query = "UPDATE packages SET bought = ? WHERE id = ?";

            try (Connection connection = Database.getConnection()) {
                assert connection != null;
                try (PreparedStatement stmt = connection.prepareStatement(query)){
                    stmt.setBoolean(1, true);
                    stmt.setInt(2, pack_ID);

                    int result = stmt.executeUpdate();

                    if (result > 0) {
                        if (acquireCards(username, pack_ID))
                            return true;
                    }
                } finally {
                    Database.closeConnection(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean enoughMoney(String username) {
        String query = "SELECT coins FROM users WHERE username = ?";
        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    int coins = result.getInt("coins");
                    if (coins >= 5)
                        return true;
                }
            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public int packsAvailable() {
        String query = "SELECT id FROM packages WHERE bought = false LIMIT 1";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                ResultSet result = stmt.executeQuery();

                if (result.next())
                    return result.getInt("id");
            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean acquireCards(String username, int pack_ID) {
        String query = "UPDATE cards SET username = ? WHERE package_id = ?";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);
                stmt.setInt(2, pack_ID);

                int result = stmt.executeUpdate();

                if (result == 5) {
                    if (removeCoins(username))
                        return true;
                }
            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean removeCoins(String username) {
        String query = "UPDATE users SET coins = coins - ? WHERE username = ?";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setInt(1, 5);
                stmt.setString(2, username);

                int result = stmt.executeUpdate();

                if (result > 0)
                    return true;
            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
