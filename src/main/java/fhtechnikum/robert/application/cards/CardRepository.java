package fhtechnikum.robert.application.cards;

import fhtechnikum.robert.application.data.Database;
import fhtechnikum.robert.system.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CardRepository extends Repository {
    public List<Card> showCards(String username, String table) {
        List<Card> cards = new ArrayList<>();
        String query;
        if (table.equals("cards"))
            query = "SELECT * FROM " + table + " WHERE username = ?";
        else
            query = "SELECT cards.card_id, cards.name, cards.damage, cards.monster_type, cards.element_type FROM cards JOIN decks " +
                    "ON cards.card_id IN (decks.card1_id, decks.card2_id, decks.card3_id, decks.card4_id) " +
                    "WHERE decks.username = ?";
        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                while (result.next()) {
                    Card card = new Card();
                    card.setId(result.getString("card_id"));
                    card.setCardName(result.getString("name"));
                    card.setDamage(result.getFloat("damage"));
                    card.setMonster_type(result.getBoolean("monster_type"));
                    card.setElement_type(result.getString("element_type"));
                    cards.add(card);
                }
            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }
}
