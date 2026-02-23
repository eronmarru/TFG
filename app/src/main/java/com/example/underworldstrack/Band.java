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
