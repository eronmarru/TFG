package com.example.underworldstrack;

/**
 * Clase que representa una Banda en el juego.
 * Contiene información sobre la banda, como su nombre, descripción, facción e imágenes asociadas.
 */
public class Band {
    private int id; // Identificador único de la banda
    private int imageResId; // ID del recurso de imagen de la banda
    private int leaderImageResId; // ID del recurso de imagen del líder de la banda
    private String name; // Nombre de la banda
    private String description; // Descripción de la banda
    private String faction; // Facción a la que pertenece la banda

    /**
     * Constructor para crear una instancia de Band sin imagen de líder específica.
     * @param id Identificador de la banda
     * @param imageResId Recurso de imagen principal
     * @param name Nombre de la banda
     * @param description Descripción
     * @param faction Facción
     */
    public Band(int id, int imageResId, String name, String description, String faction) {
        this.id = id;
        this.imageResId = imageResId;
        this.leaderImageResId = 0;
        this.name = name;
        this.description = description;
        this.faction = faction;
    }

    /**
     * Constructor completo para crear una instancia de Band.
     * @param id Identificador de la banda
     * @param imageResId Recurso de imagen principal
     * @param leaderImageResId Recurso de imagen del líder
     * @param name Nombre de la banda
     * @param description Descripción
     * @param faction Facción
     */
    public Band(int id, int imageResId, int leaderImageResId, String name, String description, String faction) {
        this.id = id;
        this.imageResId = imageResId;
        this.leaderImageResId = leaderImageResId;
        this.name = name;
        this.description = description;
        this.faction = faction;
    }

    // Métodos Getter para acceder a los atributos privados

    public int getId() {
        return id;
    }

    public int getImageResId() {
        return imageResId;
    }

    public int getLeaderImageResId() {
        return leaderImageResId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getFaction() {
        return faction;
    }

    /**
     * Obtiene el nombre de la facción para mostrar.
     * Normaliza los nombres de facciones y mapea nombres específicos de bandas a sus facciones principales.
     * @return Nombre de la facción formateado
     */
    public String getDisplayFaction() {
        if (faction == null) return "";
        String lower = faction.toLowerCase();
        // Comprueba si la facción es una de las grandes alianzas
        if (lower.equals("caos") || lower.equals("chaos") ||
                lower.equals("orden") || lower.equals("muerte") ||
                lower.equals("destruccion") || lower.equals("destrucción")) {
            if (name == null) return faction;
            // Mapeo específico de bandas a sus facciones detalladas
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
                    return "Blades of Khorne";
                case "Blood of the Bull":
                    return "Helsmiths of Hashut";
                case "Thorns of the Briar Queen":
                    return "Nighthaunt";
                case "The Grymwatch":
                    return "Flesh-eater Courts";
                case "The Crimson Court":
                case "The Exiled Dead":
                    return "Soulblight Gravelords";
                case "Brethren of the Bolt":
                    return "Cities of Sigmar";
                case "Hexbane's Hunters":
                    return "Cities of Sigmar";
                case "Cyreni's Razors":
                case "Elathain's Soulraid":
                    return "Idoneth Deepkin";
                case "Ylthari's Guardians":
                    return "Sylvaneth";
                case "Knives of the Crone":
                    return "Daughters of Khaine";
                case "Da Kunnin' Krew":
                    return "Kruleboyz";
                case "Daggok's Stab-ladz":
                    return "Kruleboyz";
                case "Borgit's Beastgrabbaz":
                case "Zarbag's Gitz":
                case "Rippa's Snarlfangs":
                case "Grinkrak's Looncourt":
                    return "Gloomspite Gitz";
                case "Mollog's Mob":
                    return "Gloomspite Gitz";
                case "Blackpowder's Buccaneers":
                    return "Ogor Mawtribes";
                case "Hrothgorn's Mantrappers":
                    return "Ogor Mawtribes";
                case "Jaws of Itzl":
                    return "Seraphon";
                case "Starblood Stalkers":
                    return "Seraphon";
                case "Zikkit's Tunnelpack":
                    return "Skaven";
                case "Skittershank's Clawpack":
                    return "Skaven";
                case "The Thricefold Discord":
                    return "Hedonites of Slaanesh";
                case "Ephilim's Pandaemonium":
                    return "Disciples of Tzeentch";
                case "Grandfather's Gardeners":
                    return "Maggotkin of Nurgle";
                case "Khagra's Ravagers":
                    return "Slaves to Darkness";
                case "The Emberwatch":
                    return "Stormcast Eternals";
                case "Ironsoul's Condemners":
                    return "Stormcast Eternals";
                case "The Farstriders":
                    return "Stormcast Eternals";
                case "Xandire's Truthseekers":
                    return "Stormcast Eternals";
                case "Kainan's Reapers":
                    return "Ossiarch Bonereapers";
                case "The Headsmen's Curse":
                    return "Nighthaunt";
                case "The Shadeborn":
                    return "Daughters of Khaine";
                case "Sepulchral Guard":
                    return "Soulblight Gravelords";
                case "Sons of Velmorn":
                    return "Soulblight Gravelords";
                case "Zondara's Gravebreakers":
                    return "Soulblight Gravelords";
                case "The Skinnerkin":
                    return "Flesh-eater Courts";
                case "Morgok's Krushas":
                    return "Ironjawz";
                case "Myari's Purifiers":
                    return "Lumineth Realm-lords";
                case "Thundrik's Profiteers":
                    return "Kharadron Overlords";
                default:
                    return faction;
            }
        }
        return faction;
    }
}
