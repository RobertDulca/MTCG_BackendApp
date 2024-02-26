package fhtechnikum.robert.application.trading;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Trade {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("CardToTrade")
    private String cardToTrade;
    @JsonProperty("Type")
    private String type;
    @JsonProperty("MinimumDamage")
    private int minDamage;

    public Trade() {
    }
    public Trade(String id, String cardToTrade, String type, int minDamage, String element) {
        this.id = id;
        this.cardToTrade = cardToTrade;
        this.type = type;
        this.minDamage = minDamage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardToTrade() {
        return cardToTrade;
    }

    public void setCardToTrade(String cardToTrade) {
        this.cardToTrade = cardToTrade;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }
}
