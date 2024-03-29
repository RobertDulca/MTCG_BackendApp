package fhtechnikum.robert.application.battle;

import fhtechnikum.robert.application.cards.Card;
import fhtechnikum.robert.application.cards.CardRepository;
import fhtechnikum.robert.application.data.Database;
import fhtechnikum.robert.system.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BattleRepository extends Repository {
    private static final Map<String, String> autoWin = new HashMap<>() {{
        put("Dragon", "Goblin");
        put("Wizard", "Ork");
        put("WaterSpell", "Knight");
        put("FireElf", "Dragon");
    }};

    private static final Map<String, String> specialities = new HashMap<>() {{
        put("Dragon", "The Goblin is too afraid to attack the Dragon.");
        put("Wizard", "Because the Wizard is controlling the Ork, the Ork can't deal any damage.");
        put("WaterSpell", "The heavy armor of the Knight made him drown in the water.");
        put("Kraken", "The Kraken is immune to every Spell");
        put("FireElf", "Because the FireElves have known the Dragons since they were little, the FireElf easily evades all attacks");
    }};

    private static final Map<String, String> effective = new HashMap<>() {{
        put("Water", "Fire");
        put("Fire", "Regular");
        put("Regular", "Water");
    }};

    public enum BattleOutcome {
        PLAYER1_WIN, PLAYER2_WIN, DRAW
    }
    public BattleRepository(CardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    CardRepository cardRepo;
    StringBuilder battleLogger = new StringBuilder();
    private static final double luck = 0.18;

    public String startBattle(String player1, String player2) {
        List<Card> player1_Deck = cardRepo.showCards(player1, "decks");
        List<Card> player2_Deck = cardRepo.showCards(player2, "decks");

        BattleOutcome result = battle(player1, player2, player1_Deck, player2_Deck);
        updateStats(player1, player2, result);

        return battleLogger.toString();
    }

    private BattleOutcome battle(String player1, String player2, List<Card> player1_deck, List<Card> player2_deck) {
        long seed1 = System.currentTimeMillis();
        long seed2 = System.currentTimeMillis();
        Random random1 = new Random(seed1);
        Random random2 = new Random(seed2);
        Random luckRandom = new Random(seed1);
        int round = 0;

        while (round < 100) {
            Card player1_card = player1_deck.get(random1.nextInt(player1_deck.size()));
            Card player2_card = player2_deck.get(random2.nextInt(player2_deck.size()));

            battleLogger.append("Round").append(round).append(": ").append(player1).append("'s ").append(player1_card.getCardName()).append(" (")
                    .append(player1_card.getDamage()).append(" Damage) VS ").append(player2).append("'s ")
                    .append(player2_card.getCardName()).append(" (").append(player2_card.getDamage()).append(" Damage):\n");

            if (luckRandom.nextDouble() < luck) {
                luckyMoment(player1, player1_card, player2, player2_card);
            }

            BattleOutcome result = fight(player1_card, player2_card);

            if (result == BattleOutcome.PLAYER1_WIN) {
                player1_deck.add(player2_card);
                player2_deck.remove(player2_card);

            } else if (result == BattleOutcome.PLAYER2_WIN) {
                player2_deck.add(player1_card);
                player1_deck.remove(player1_card);
            }
            battleLogger.append(player1).append("'s cards: ").append(player1_deck.size()).append("\n");
            battleLogger.append(player2).append("'s cards: ").append(player2_deck.size()).append("\n\n");

            if (player1_deck.isEmpty()) {
                battleLogger.append(player2).append(" won the match!\n\n").append("\n");
                return BattleOutcome.PLAYER2_WIN;
            } else if (player2_deck.isEmpty()) {
                battleLogger.append(player1).append(" won the match!\n\n").append("\n");
                return BattleOutcome.PLAYER1_WIN;
            }
            round++;
        }

        return BattleOutcome.DRAW;
    }

    private void luckyMoment(String player1, Card player1Card, String player2, Card player2Card) {
        Random random = new Random();
        int boost = random.nextInt(2);

        if (boost == 0) {
            battleLogger.append(player1).append("'s ").append(player1Card.getCardName()).append(" has a lucky moment and deals double damage!\n");
            player1Card.setDamage((float) (player1Card.getDamage() * 2));
        } else {
            battleLogger.append(player2).append("'s ").append(player2Card.getCardName()).append(" has a lucky moment and deals double damage!\n");
            player2Card.setDamage((float) (player2Card.getDamage() * 2));
        }
    }

    private BattleOutcome fight(Card player1_card, Card player2_card) {
        // Kraken Spell Battles
        if (player1_card.getCardName().equals("Kraken") && player2_card.getCardName().contains("Spell")) {
            battleLogger.append(specialities.get("Kraken")).append("\n");
            return BattleOutcome.PLAYER1_WIN;
        } else if (player2_card.getCardName().equals("Kraken") && player1_card.getCardName().contains("Spell")) {
            battleLogger.append(specialities.get("Kraken")).append("\n");
            return BattleOutcome.PLAYER2_WIN;
        }

        // Special Battles
        if (autoWin.containsKey(player1_card.getCardName()) && autoWin.get(player1_card.getCardName()).equals(player2_card.getCardName())) {
            battleLogger.append(specialities.get(player1_card.getCardName())).append("\n");
            return BattleOutcome.PLAYER1_WIN;
        } else if (autoWin.containsKey(player2_card.getCardName()) && autoWin.get(player2_card.getCardName()).equals(player1_card.getCardName())) {
            battleLogger.append(specialities.get(player2_card.getCardName())).append("\n");
            return BattleOutcome.PLAYER2_WIN;
        }

        // Monster or same Element Battles
        if ((player1_card.isMonster_type() && player2_card.isMonster_type()) ||
                (player1_card.getElement_type().equals(player2_card.getElement_type()))) {
            return normalDmg(player1_card, player2_card);
        }

        // Effectiveness Battles
        return specialDmg(player1_card, player2_card);
    }

    private BattleOutcome normalDmg(Card player1_card, Card player2_card) {
        if (player1_card.getDamage() > player2_card.getDamage()) {
            battleLogger.append(player1_card.getCardName()).append(" beats ").append(player2_card.getCardName()).append("\n");
            return BattleOutcome.PLAYER1_WIN;
        }
        if (player2_card.getDamage() > player1_card.getDamage()) {
            battleLogger.append(player2_card.getCardName()).append(" beats ").append(player1_card.getCardName()).append("\n");
            return BattleOutcome.PLAYER2_WIN;
        }

        battleLogger.append("The Battle results in a draw\n");
        return BattleOutcome.DRAW;

    }

    private BattleOutcome specialDmg(Card player1_card, Card player2_card) {
        String card1_element = player1_card.getElement_type();
        String card2_element = player2_card.getElement_type();
        float card1Dmg = player1_card.getDamage();
        float card2Dmg = player2_card.getDamage();

        if (effective.containsKey(card1_element) && effective.get(card1_element).equals(card2_element)) {
            card1Dmg *= 2;
            card2Dmg /= 2;
            battleLogger.append(player1_card.getCardName()).append(" (").append(card1Dmg).append(") is very effective against ").append(player2_card.getCardName()).append(" (").append(card2Dmg).append(")!\n");
        } else if (effective.containsKey(card2_element) && effective.get(card2_element).equals(card1_element)) {
            card1Dmg /= 2;
            card2Dmg *= 2;
            battleLogger.append(player2_card.getCardName()).append(" (").append(card2Dmg).append(") is very effective against ").append(player1_card.getCardName()).append(" (").append(card1Dmg).append(")!\n");
        }

        if (card1Dmg > card2Dmg) {
            battleLogger.append(player1_card.getCardName()).append(" beats ").append(player2_card.getCardName()).append("\n");
            return BattleOutcome.PLAYER1_WIN;
        } else if (card1Dmg < card2Dmg) {
            battleLogger.append(player2_card.getCardName()).append(" beats ").append(player1_card.getCardName()).append("\n");
            return BattleOutcome.PLAYER2_WIN;
        }

        return BattleOutcome.DRAW;
    }
    public void updateStats(String player1, String player2, BattleOutcome result) {
        String query, winner = player1, loser = player2;

        if (result == BattleOutcome.DRAW) {
            query = "UPDATE stats SET games_played = games_played + 1 WHERE username IN (?, ?)";
        } else {
            if (result == BattleOutcome.PLAYER2_WIN) {
                winner = player2;
                loser = player1;
            }
            query = "UPDATE stats SET games_played = games_played + 1, games_won = games_won + 1, elo = elo + 3 WHERE username = ?; " +
                    "UPDATE stats SET games_played = games_played + 1, games_lost = games_lost + 1, elo = elo - 5 WHERE username = ?;";
        }

        try (Connection connection = Database.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, winner);
                stmt.setString(2, loser);

                stmt.executeUpdate();

            } finally {
                Database.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
