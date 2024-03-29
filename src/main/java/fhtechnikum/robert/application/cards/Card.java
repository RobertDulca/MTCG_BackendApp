package fhtechnikum.robert.application.cards;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {
    @JsonProperty("Id")
    private String id;
    @JsonProperty("Name")
    private String cardName;
    @JsonProperty("Damage")
    private float damage;
    @JsonIgnore
    private String element_type;
    @JsonIgnore
    private boolean monster_type;

    public Card(){}
    public Card(String id, String cardName, float damage) {
        this.id = id;
        this.cardName = cardName;
        this.damage = damage;
    }

    public String getId() {
        return id;
    }

    public String getCardName() {
        return cardName;
    }

    public float getDamage() {
        return damage;
    }

    public String getElement_type() {
        return element_type;
    }

    public void setElement_type(String element_type) {
        this.element_type = element_type;
    }

    public boolean isMonster_type() {
        return monster_type;
    }

    public void setMonster_type(boolean monster_type) {
        this.monster_type = monster_type;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }


    public void getElements() {
        if (cardName.endsWith("Spell")) {
            monster_type = false;
            element_type = cardName.substring(0, cardName.indexOf("Spell"));
        } else {
            monster_type = true;

            if (cardName.contains("Water"))
                element_type = "Water";
            else if (cardName.contains("Fire") || cardName.contains("Dragon"))
                element_type = "Fire";
            else
                element_type = "Regular";
        }
    }
}
