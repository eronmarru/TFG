package com.example.underworldstrack;

public class UserDeck {
    private int id;
    private String name;
    private Integer bandId;
    private int rival1Id;
    private int rival2Id;

    public UserDeck(int id, String name, Integer bandId, int rival1Id, int rival2Id) {
        this.id = id;
        this.name = name;
        this.bandId = bandId;
        this.rival1Id = rival1Id;
        this.rival2Id = rival2Id;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public Integer getBandId() { return bandId; }
    public int getRival1Id() { return rival1Id; }
    public int getRival2Id() { return rival2Id; }
}
