package fhtechnikum.robert.application.trading;

import fhtechnikum.robert.application.data.Database;
import fhtechnikum.robert.system.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TradingRepository extends Repository {
    public List<Trade> getDeals() {
        List<Trade> trades = new ArrayList<>();
        String query = "SELECT * FROM trade";
        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    Trade trade = new Trade();
                    trade.setId(result.getString("trade_id"));
                    trade.setCardToTrade(result.getString("card_id"));
                    trade.setType(result.getString("type"));
                    trade.setMinDamage(result.getInt("minDmg"));
                    trades.add(trade);
                }
            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trades;
    }

    public boolean dealExists(String id) {
        String query = "SELECT COUNT(*) FROM trade WHERE trade_id = ?";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, id);

                ResultSet result = stmt.executeQuery();
                int count = 0;
                while (result.next()) {
                    count = result.getInt(1);
                    if (count == 1)
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

    public boolean createDeal(Trade trade) {
        String query = "INSERT INTO trade (trade_id, card_id, type, minDmg) VALUES (?, ?, ?, ?)";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, trade.getId());
                stmt.setString(2, trade.getCardToTrade());
                stmt.setString(3, trade.getType());
                stmt.setInt(4, trade.getMinDamage());

                int result = stmt.executeUpdate();

                if (result == 1) {
                    lockCard(trade.getCardToTrade());
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

    private void lockCard(String cardID) {
        String query = "UPDATE cards SET locked = ? WHERE card_id = ?";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setBoolean(1, true);
                stmt.setString(2, cardID);

                stmt.executeUpdate();

            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteDeal(String id, String username) {
        String query = "SELECT card_id FROM trade WHERE trade_id = ?";
        String cardID = "";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, id);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    cardID = result.getString("card_id");

                    if (isOwner(cardID, username)) {
                        query = "DELETE FROM trade WHERE trade_id = ?";
                        try (PreparedStatement stmt2 = connection.prepareStatement(query)){
                            stmt2.setString(1, id);
                            stmt2.executeUpdate();

                            unlockCard(cardID);

                            return true;
                        }
                    }
                }

            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private boolean isOwner(String cardID, String username) {
        String query = "SELECT username FROM cards WHERE card_id = ?";
        String owner = "";
        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, cardID);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    owner = result.getString("username");
                    if (owner.equals(username))
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

    private void unlockCard(String cardID) {
        String query = "UPDATE cards SET locked = ? WHERE card_id = ?";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setBoolean(1, false);
                stmt.setString(2, cardID);

                stmt.executeUpdate();

            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
