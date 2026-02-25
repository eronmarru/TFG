package com.example.underworldstrack;

/**
 * Clase que representa un Mazo creado por el Usuario.
 * Vincula una banda con hasta dos mazos rivales (Nemesis).
 */
public class UserDeck {
    private int id; // Identificador del mazo de usuario
    private String name; // Nombre personalizado del mazo
    private Integer bandId; // ID de la banda seleccionada
    private int rival1Id; // ID del primer mazo rival seleccionado
    private int rival2Id; // ID del segundo mazo rival seleccionado

    /**
     * Constructor para crear un Mazo de Usuario.
     * @param id Identificador
     * @param name Nombre del mazo
     * @param bandId ID de la banda
     * @param rival1Id ID del primer mazo rival
     * @param rival2Id ID del segundo mazo rival
     */
    public UserDeck(int id, String name, Integer bandId, int rival1Id, int rival2Id) {
        this.id = id;
        this.name = name;
        this.bandId = bandId;
        this.rival1Id = rival1Id;
        this.rival2Id = rival2Id;
    }

    // Métodos Getter

    public int getId() { return id; }
    public String getName() { return name; }
    public Integer getBandId() { return bandId; }
    public int getRival1Id() { return rival1Id; }
    public int getRival2Id() { return rival2Id; }
}
