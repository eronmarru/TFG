package com.example.underworldstrack;

/**
 * Clase que representa una Carta en el juego.
 * Almacena todas las propiedades de una carta, como su nombre, tipo, gloria, efecto, etc.
 */
public class Card {
    private int id; // Identificador único de la carta
    private String name; // Nombre de la carta
    private String type; // Tipo de carta (ej. Objetivo, Mejora, Gambito)
    private String subtype; // Subtipo de carta
    private int cardNumber; // Número de la carta en la colección
    private int rivalDeckId; // ID del mazo rival al que pertenece (si aplica)
    private int glory; // Puntos de gloria que otorga
    private String effect; // Texto del efecto de la carta
    private String costRestriction; // Coste o restricciones de uso
    private String image; // Ruta o nombre del recurso de imagen

    /**
     * Constructor para crear una nueva Carta.
     * @param id Identificador
     * @param name Nombre
     * @param type Tipo
     * @param subtype Subtipo
     * @param cardNumber Número de carta
     * @param rivalDeckId ID del mazo rival
     * @param glory Gloria
     * @param effect Efecto
     * @param costRestriction Coste/Restricción
     * @param image Imagen
     */
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

    // Métodos Getter para acceder a las propiedades

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
