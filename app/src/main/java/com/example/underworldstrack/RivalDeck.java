package com.example.underworldstrack;

/**
 * Clase que representa un Mazo Rival preconstruido.
 * Contiene información sobre el mazo, como su nombre, descripción y tipo.
 */
public class RivalDeck {
    private int id; // Identificador del mazo
    private String name; // Nombre del mazo
    private String description; // Descripción del mazo
    private int bandId; // ID de la banda asociada (si hay)
    private String type; // Tipo de mazo

    /**
     * Constructor para crear un Mazo Rival.
     * @param id Identificador
     * @param name Nombre
     * @param description Descripción
     * @param bandId ID de la banda
     * @param type Tipo
     */
    public RivalDeck(int id, String name, String description, int bandId, String type) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.bandId = bandId;
        this.type = type;
    }

    // Métodos Getter

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
