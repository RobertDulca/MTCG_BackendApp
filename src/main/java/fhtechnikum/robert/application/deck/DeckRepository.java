package fhtechnikum.robert.application.deck;

import fhtechnikum.robert.application.data.Database;
import fhtechnikum.robert.system.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class DeckRepository extends Repository {
    public boolean configureDeck(String username, List<String> card_IDs) {
        boolean ownsCards = checkCards(username, card_IDs);

        if (ownsCards) {
            String query = "INSERT INTO decks (username, card1_id, card2_id, card3_id, card4_id) VALUES (?, ?, ?, ?, ?) " +
                    "ON CONFLICT (username) DO UPDATE SET " +
                    "card1_id = EXCLUDED.card1_id, " +
                    "card2_id = EXCLUDED.card2_id, " +
                    "card3_id = EXCLUDED.card3_id, " +
                    "card4_id = EXCLUDED.card4_id";

            try (Connection connection = Database.getConnection()) {
                assert connection != null;
                try (PreparedStatement stmt = connection.prepareStatement(query)){
                    stmt.setString(1, username);
                    for (int i = 0; i < 4; i++)
                        stmt.setString(i+2, card_IDs.get(i));

                    int result = stmt.executeUpdate();

                    if (result != 0)
                        return true;
                } finally {
                    Database.closeConnection(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkCards(String username, List<String> card_IDs) {
        String query = "SELECT COUNT(*) FROM cards WHERE card_id in (?, ?, ?, ?) AND username = ?";

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                for (int i = 0; i < 4; i++)
                    stmt.setString(i+1, card_IDs.get(i));
                stmt.setString(5, username);

                ResultSet result = stmt.executeQuery();
                int count;
                while (result.next()) {
                    count = result.getInt(1);

                    if (count == 4)
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
}
