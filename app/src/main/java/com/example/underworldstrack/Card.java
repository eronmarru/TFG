package com.example.underworldstrack;

public class Card {
    private int id;
    private String name;
    private String type;
    private String subtype;
    private int cardNumber;
    private int rivalDeckId;
    private int glory;
    private String effect;
    private String costRestriction;
    private String image;

    public Card(int id, String name, String type, String subtype, int cardNumber, int rivalDeckId, int glory, String effect, String costRestriction, String image) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.subtype = subtype;
        this.cardNumber = cardNumber;
        this.rivalDeckId = rivalDeckId;
        this.glory = glory;
        this.effect = effect;
        this.costRestriction = costRestriction;
        this.image = image;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getType() { return type; }
    public String getSubtype() { return subtype; }
    public int getCardNumber() { return cardNumber; }
    public int getRivalDeckId() { return rivalDeckId; }
    public int getGlory() { return glory; }
    public String getEffect() { return effect; }
    public String getCostRestriction() { return costRestriction; }
    public String getImage() { return image; }
}
