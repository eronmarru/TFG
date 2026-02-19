package com.example.underworldstrack;

public class Band {
    // Campos privados que almacenan los datos de cada banda
    private int id;          // ID de la base de datos
    private int imageResId;      // ID del recurso de imagen (R.drawable.nombre_imagen)
    private int leaderImageResId; // ID de la imagen del líder
    private String name;         // Nombre de la banda (ej: "Ironsoul's Condemners")
    private String description;  // Habilidad especial larga de la banda
    private String faction;      // Facción: "Orden", "Destruccion", "Muerte", "Caos"

    // Constructor que inicializa todos los campos al crear una nueva banda
    public Band(int id, int imageResId, String name, String description, String faction) {
        this.id = id;
        this.imageResId = imageResId;    // Asigna ID de imagen
        this.leaderImageResId = 0;       // Por defecto 0
        this.name = name;                // Asigna nombre
        this.description = description;  // Asigna descripción completa
        this.faction = faction;          // Asigna facción
    }

    public Band(int id, int imageResId, int leaderImageResId, String name, String description, String faction) {
        this.id = id;
        this.imageResId = imageResId;
        this.leaderImageResId = leaderImageResId;
        this.name = name;
        this.description = description;
        this.faction = faction;
    }

    public int getId() {
        return id;
    }

    // Getters públicos para acceder a los datos privados desde BandAdapter
    public int getImageResId() { 
        return imageResId;  // Devuelve ID de imagen para ImageView.setImageResource()
    }

    public int getLeaderImageResId() {
        return leaderImageResId;
    }
    
    public String getName() { 
        return name;        // Devuelve nombre para TextView
    }
    
    public String getDescription() { 
        return description; // Devuelve habilidad para TextView
    }
    
    public String getFaction() { 
        return faction;     // Devuelve facción para notificación
    }

    public String getDisplayFaction() {
        if (faction == null) return "";
        String lower = faction.toLowerCase();
        if (lower.equals("caos") || lower.equals("chaos") ||
                lower.equals("orden") || lower.equals("muerte") ||
                lower.equals("destruccion") || lower.equals("destrucción")) {
            if (name == null) return faction;
            switch (name) {
                case "The Wurmspat":
                    return "Maggotkin of Nurgle";
                case "Spiteclaw's Swarm":
                    return "Skaven";
                case "The Dread Pageant":
                    return "Hedonites of Slaanesh";
                case "The Gnarlspirit Pack":
                    return "Slaves to Darkness";
                case "Kamandora's Blades":
                case "Blood of the Bull":
                    return "Blades of Khorne";
                case "Thorns of the Briar Queen":
                    return "Nighthaunt";
                case "The Grymwatch":
                    return "Flesh-eater Courts";
                case "The Crimson Court":
                case "The Exiled Dead":
                    return "Soulblight Gravelords";
                case "Brethren of the Bolt":
                case "The Emberwatch":
                    return "Cities of Sigmar";
                case "Cyreni's Razors":
                case "Elathain's Soulraid":
                    return "Idoneth Deepkin";
                case "Ylthari's Guardians":
                    return "Sylvaneth";
                case "Da Kunnin' Krew":
                case "Daggok's Stab-ladz":
                case "Borgit's Beastgrabbaz":
                case "Zarbag's Gitz":
                case "Rippa's Snarlfangs":
                case "Grinkrak's Looncourt":
                    return "Gloomspite Gitz";
                case "Jaws of Itzl":
                    return "Seraphon";
                case "Zikkit's Tunnelpack":
                    return "Skaven";
                case "The Thricefold Discord":
                    return "Hedonites of Slaanesh";
                case "Grandfather's Gardeners":
                    return "Maggotkin of Nurgle";
                default:
                    return faction;
            }
        }
        return faction;
    }
}
