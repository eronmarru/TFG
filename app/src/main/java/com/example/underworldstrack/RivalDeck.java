package com.example.underworldstrack;

public class RivalDeck {
    private int id;
    private String name;
    private String description;
    private int bandId;
    private String type;

    public RivalDeck(int id, String name, String description, int bandId, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bandId = bandId;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getBandId() {
        return bandId;
    }

    public String getType() {
        return type;
    }
}
