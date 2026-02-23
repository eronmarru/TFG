package com.example.underworldstrack;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "underworlds.db";
    private static final int DATABASE_VERSION = 23;

    // Tabla Fighters (Luchadores)
    public static final String TABLE_FIGHTERS = "fighters";
    public static final String COLUMN_FIGHTER_ID = "_id";
    public static final String COLUMN_FIGHTER_BAND_ID = "band_id";
    public static final String COLUMN_FIGHTER_NAME = "name";
    public static final String COLUMN_FIGHTER_IMAGE = "image"; // Referencia a recurso drawable

    // Tabla Fighter Stats (Estadísticas por estado: Inspirado/No Inspirado)

    // Tabla Weapons (Armas asociadas a un perfil de estadísticas)

    // Tabla Bands (Bandas)
    public static final String TABLE_BANDS = "bands";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_FACTION = "faction";
    public static final String COLUMN_IMAGE_RESOURCE = "image_resource";

    // Tabla Rival Decks (Mazos Rivales preconstruidos)
    public static final String TABLE_RIVAL_DECKS = "rival_decks";
    public static final String COLUMN_RIVAL_DECK_ID = "id";
    public static final String COLUMN_RIVAL_DECK_NAME = "name";
    public static final String COLUMN_RIVAL_DECK_DESCRIPTION = "description";
    public static final String COLUMN_RIVAL_DECK_BAND_ID = "band_id"; // Null si es Universal
    public static final String COLUMN_RIVAL_DECK_TYPE = "type"; // 'WARBAND' o 'UNIVERSAL'
    public static final String COLUMN_RIVAL_DECK_SPECIAL_RULES = "special_rules"; // Reglas especiales del mazo

    // Tabla Cards (Cartas)
    public static final String TABLE_CARDS = "cards";
    public static final String COLUMN_CARD_ID = "id";
    public static final String COLUMN_CARD_NAME = "name";
    public static final String COLUMN_CARD_TYPE = "type"; // 'Objective', 'Gambit', 'Upgrade'
    public static final String COLUMN_CARD_SUBTYPE = "subtype"; // 'Surge', 'End Phase' (Objectives)
    public static final String COLUMN_CARD_RIVAL_DECK_ID = "rival_deck_id";
    public static final String COLUMN_CARD_NUMBER = "card_number";
    public static final String COLUMN_CARD_GLORY = "glory_points"; // Puntos de gloria (Objectives)
    public static final String COLUMN_CARD_EFFECT = "effect"; // Efecto o descripción
    public static final String COLUMN_CARD_COST_RESTRICTION = "cost_restriction"; // Coste o restricción (Power cards)
    public static final String COLUMN_CARD_IMAGE = "image"; // Nombre del recurso de imagen (sin extensión)

    // Tabla User Decks (Mazos de Usuario / Combinaciones)
    public static final String TABLE_USER_DECKS = "user_decks";
    public static final String COLUMN_USER_DECK_ID = "id";
    public static final String COLUMN_USER_DECK_NAME = "name";
    public static final String COLUMN_USER_DECK_BAND_ID = "band_id"; // Banda que lo usa
    public static final String COLUMN_USER_DECK_RIVAL_1 = "rival_deck_1_id";
    public static final String COLUMN_USER_DECK_RIVAL_2 = "rival_deck_2_id"; // Puede ser null

    // Nueva Tabla User Deck Content (Cartas seleccionadas por el usuario)
    public static final String TABLE_USER_DECK_CONTENT = "user_deck_content";
    public static final String COLUMN_CONTENT_ID = "_id";
    public static final String COLUMN_CONTENT_USER_DECK_ID = "user_deck_id";
    public static final String COLUMN_CONTENT_CARD_ID = "card_id";

    // Sentencias CREATE (Movidas aquí para asegurar que las constantes de tablas estén inicializadas)
    private static final String TABLE_CREATE_FIGHTERS =
            "CREATE TABLE " + TABLE_FIGHTERS + " (" +
                    COLUMN_FIGHTER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_FIGHTER_BAND_ID + " INTEGER, " +
                    COLUMN_FIGHTER_NAME + " TEXT, " +
                    COLUMN_FIGHTER_IMAGE + " TEXT, " +
                    "FOREIGN KEY(" + COLUMN_FIGHTER_BAND_ID + ") REFERENCES " + TABLE_BANDS + "(" + COLUMN_ID + "));";



    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 1. Crear tabla Bands
        String CREATE_BANDS_TABLE = "CREATE TABLE " + TABLE_BANDS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_FACTION + " TEXT,"
                + COLUMN_IMAGE_RESOURCE + " TEXT" + ")";
        db.execSQL(CREATE_BANDS_TABLE);

        // 2. Crear tabla Rival Decks
        String CREATE_RIVAL_DECKS_TABLE = "CREATE TABLE " + TABLE_RIVAL_DECKS + "("
                + COLUMN_RIVAL_DECK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RIVAL_DECK_NAME + " TEXT,"
                + COLUMN_RIVAL_DECK_DESCRIPTION + " TEXT,"
                + COLUMN_RIVAL_DECK_BAND_ID + " INTEGER,"
                + COLUMN_RIVAL_DECK_TYPE + " TEXT,"
                + COLUMN_RIVAL_DECK_SPECIAL_RULES + " TEXT,"
                + " FOREIGN KEY (" + COLUMN_RIVAL_DECK_BAND_ID + ") REFERENCES " + TABLE_BANDS + "(" + COLUMN_ID + ")"
                + ")";
        db.execSQL(CREATE_RIVAL_DECKS_TABLE);

        // 3. Crear tabla Cards
        String CREATE_CARDS_TABLE = "CREATE TABLE " + TABLE_CARDS + "("
                + COLUMN_CARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CARD_NAME + " TEXT,"
                + COLUMN_CARD_TYPE + " TEXT,"
                + COLUMN_CARD_SUBTYPE + " TEXT,"
                + COLUMN_CARD_NUMBER + " INTEGER,"
                + COLUMN_CARD_RIVAL_DECK_ID + " INTEGER,"
                + COLUMN_CARD_GLORY + " INTEGER,"
                + COLUMN_CARD_EFFECT + " TEXT,"
                + COLUMN_CARD_COST_RESTRICTION + " TEXT,"
                + COLUMN_CARD_IMAGE + " TEXT,"
                + " FOREIGN KEY (" + COLUMN_CARD_RIVAL_DECK_ID + ") REFERENCES " + TABLE_RIVAL_DECKS + "(" + COLUMN_RIVAL_DECK_ID + ")"
                + ")";
        db.execSQL(CREATE_CARDS_TABLE);

        // 4. Crear tabla User Decks (Combinaciones)
        String CREATE_USER_DECKS_TABLE = "CREATE TABLE " + TABLE_USER_DECKS + "("
                + COLUMN_USER_DECK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_USER_DECK_NAME + " TEXT,"
                + COLUMN_USER_DECK_BAND_ID + " INTEGER,"
                + COLUMN_USER_DECK_RIVAL_1 + " INTEGER,"
                + COLUMN_USER_DECK_RIVAL_2 + " INTEGER,"
                + " FOREIGN KEY (" + COLUMN_USER_DECK_BAND_ID + ") REFERENCES " + TABLE_BANDS + "(" + COLUMN_ID + "),"
                + " FOREIGN KEY (" + COLUMN_USER_DECK_RIVAL_1 + ") REFERENCES " + TABLE_RIVAL_DECKS + "(" + COLUMN_RIVAL_DECK_ID + "),"
                + " FOREIGN KEY (" + COLUMN_USER_DECK_RIVAL_2 + ") REFERENCES " + TABLE_RIVAL_DECKS + "(" + COLUMN_RIVAL_DECK_ID + ")"
                + ")";
        db.execSQL(CREATE_USER_DECKS_TABLE);

        // 4b. Crear tabla User Deck Content
        String CREATE_USER_DECK_CONTENT_TABLE = "CREATE TABLE " + TABLE_USER_DECK_CONTENT + "("
                + COLUMN_CONTENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_CONTENT_USER_DECK_ID + " INTEGER,"
                + COLUMN_CONTENT_CARD_ID + " INTEGER,"
                + " FOREIGN KEY (" + COLUMN_CONTENT_USER_DECK_ID + ") REFERENCES " + TABLE_USER_DECKS + "(" + COLUMN_USER_DECK_ID + "),"
                + " FOREIGN KEY (" + COLUMN_CONTENT_CARD_ID + ") REFERENCES " + TABLE_CARDS + "(" + COLUMN_CARD_ID + ")"
                + ")";
        db.execSQL(CREATE_USER_DECK_CONTENT_TABLE);

        // 5. Crear tablas de Luchadores y Armas
        db.execSQL(TABLE_CREATE_FIGHTERS);

        // Insertar datos iniciales
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Eliminar tablas antiguas si existen (orden inverso a las dependencias)
        // Tablas que dependen de fighters
        db.execSQL("DROP TABLE IF EXISTS weapons");
        db.execSQL("DROP TABLE IF EXISTS fighter_stats");
        
        // Tablas principales
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FIGHTERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DECK_CONTENT); // Eliminar tabla nueva
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DECKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CARDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RIVAL_DECKS);
        db.execSQL("DROP TABLE IF EXISTS decks"); // Tabla antigua
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BANDS);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        // Insertar Bandas


        // --- Inserción de The Wurmspat (Band, Fighters, Stats, Weapons) ---
        long wurmspatId = insertBandInternal(db, "The Wurmspat",
                "Inspirar: Si 3 o más luchadores enemigos están muertos o heridos, Inspira a cada luchador amigo. Después de que un luchador enemigo con alguna ficha de bilis sea eliminado por un luchador amigo, Inspira a ese luchador.\n\n"+
                "Repungnantemente resistente: Inmediatamente antes de que se le vaya a inflijir daño a un luchador amigo, si la tirada de salvación tuvo algun escudo, reduce el daño de ese ataque en 1\n\n"+
                "Regalos de abuelo: \n"+
                        "Hoja de bilis: Cuando un arma con esta habilidad de arma inflinge daño a un luchador enemigo, dale al objetivo un token de bilis\n"+
                        "Los luchadores enemigos tienen -2 de movimiento, mientras tengan el token de bilis. Retira todos los tokens de bilis de los luchadores enemigos al final de la ronde de batalla\n\n"+
                        "Avance constante (Una vez por partida): Usa esto en tu paso de poder. Elige hasta 2 luchadores amigos. Empuja a cada uno 1 hexagono\n\n"+
                        "Repelencia nauseabunda (Una vez por partida): Usa esto en tu paso de poder, las armas cuerpo a cuerpo de los luchadores enemigos tienen -1 dado de ataque en el siguiente turno\n\n"+
                        "Hojas de putrefacción (Una vez por partida): Usa esto es una fase de poder, las armas cuerpo a cuerpo de los luchadores amigos tienen doloroso ademas de sus otras habilidades de arma en el siguiente turno",
                "Caos", "the_wurmspat_0");

        // Fecula
        insertFighterInternal(db, (int)wurmspatId, "Fecula", "the_wurmspat_1");

        // Ghulgoch
        insertFighterInternal(db, (int)wurmspatId, "Ghulgoch", "the_wurmspat_2");

        // Sepsimus
        insertFighterInternal(db, (int)wurmspatId, "Sepsimus", "the_wurmspat_3");

        // --- Inserción de Spiteclaw's Swarm (Band) ---
        long spiteclawId = insertBandInternal(db, "Spiteclaw's Swarm",
                "Inspirar: Inmediatamente después de resolver una Ardid que eligió a un luchador amigo, Inspira a ese luchador. \n \n" +
                "Enjambre: Elige un líder amigo para usar esta habilidad. Elige un esbirro amigo eliminado. Resucita a ese luchador y colócalo en un hexágono inicial vacío, luego dale una ficha de Resurrección. \n \n" +
                "Manada Maquinadora: Las armas de los maquinadores amigos tienen critico doloroso mientras el objetivo está Flanqueado o Rodeado. \n \n" +
                "Promoción Inoportuna: Mientras Skritch amigo esté eliminado, Krrk es un líder.\n\n"+
                "Skitter (Una vez por partida): Usala inmediatamente despues de que un luchador amigo adyacente a otro luchador amigo sea seleccionado para usar una habilidad basica, Empuja a ese luchador 1 hexagono.\n\n" +
                "Paranoia Justificada (Una vez por partida): Usala inmediatamente despues de que un luchador amigo adyacente a otro luchador amigo sea seleccionado para usar una habilidad basica, Dale a ese luchador un token de guardia. \n\n"+
                "¡Fuera de mi camino, cosas necias! (Una vez por partida): Usala en tu paso de poder. Elige 2 luchadores amigos adyacentes",
                "Caos", "spiteclaws_swarm_0");

        // Skritch (Líder)
        insertFighterInternal(db, (int)spiteclawId, "Skritch", "spiteclaws_swarm_1");

        // Krrk
        insertFighterInternal(db, (int)spiteclawId, "Krrk", "spiteclaws_swarm_2");

        // Lurking Skaven (Minion)
        insertFighterInternal(db, (int)spiteclawId, "Lurking Skaven", "spiteclaws_swarm_3");

        // Hungering Skaven (Minion)
        insertFighterInternal(db, (int)spiteclawId, "Hungering Skaven", "spiteclaws_swarm_4");

        // Festering Skaven (Minion)
        insertFighterInternal(db, (int)spiteclawId, "Festering Skaven", "spiteclaws_swarm_5");

        // --- Inserción de The Dread Pageant (Band) ---
        long dreadPageantId = insertBandInternal(db, "The Dread Pageant",
                "Inspirar: Después de un paso de Acción, si todos los luchadores amigos están heridos, o si hay más luchadores heridos que itactos, Inspira a cada luchador amigo. \n\n" +
                "Velocidad de Slaanesh: Los luchadores amigos heridos e Inspirados tienen +1 al Movimiento mientras usan habilidades de Carga. \n\n" +
                "Dolores Crueles (Una vez por partida): Usa esto en un paso de Poder. Elige un luchador que controle un objetivo. Inflige 1 daño a ese luchador.\n\n" +
                "Rápido como el Deseo (Una vez por partida): Usa esto en un paso de Poder. Empuja a cada luchador amigo herido 1 hexágono.\n\n" +
                "Ahogarse en Dolor (Una vez por partida): Usa esto en un paso de Poder. Dale a cada luchador amigo herido un token de Guardia.\n\n" +
                "Arrogancia Burda (Una vez por partida): Usa esto inmediatamente después de hacer una tirada de Ataque para un luchador amigo herido si esa tirada no contiene éxitos. Puedes volver a tirar cualquier dado de Ataque de esa tirada.",
                "Caos", "the_dread_pageant_0");

        // Vasillac (Líder)
        insertFighterInternal(db, (int)dreadPageantId, "Vasillac", "the_dread_pageant_1");

        // Slakeslash
        insertFighterInternal(db, (int)dreadPageantId, "Slakeslash", "the_dread_pageant_2");

        // Glissete
        insertFighterInternal(db, (int)dreadPageantId, "Glissete", "the_dread_pageant_3");

        // Hadzu
        insertFighterInternal(db, (int)dreadPageantId, "Hadzu", "the_dread_pageant_4");

        // --- Inserción de The Gnarlspirit Pack (Band) ---
        long gnarlspiritBandId = insertBandInternal(db, "The Gnarlspirit Pack",
                "Inspirar: Inmediatamente después de retirar una ficha de espíritu de un luchador amigo, Inspira a ese luchador. \n\n" +
                "Desinspirar: Inmediatamente después de dar una ficha de espíritu a un luchador amigo Inspirado, Desinspira a ese luchador. \n\n" +
                "Fichas de Espíritu: Inmediatamente después de elegir un luchador amigo para usar una habilidad Basica, puedes retirar las fichas de espíritu de ese luchador. Después de resolverla, puedes darle una ficha de espíritu. Los luchadores con fichas de espíritu no pueden Inspirarse ni controlar objetivos. \n\n" +
                "Sarrakkar Desatado: +2 al Movimiento mientras tenga alguna ficha de espíritu. \n\n" +
                "Gorl Desatado: +1 a la Defensa mientras tenga alguna ficha de espíritu. \n\n" +
                "Kheira Desatada: Las armas cuerpo a cuerpo (Excluyendo mejoras) tienen +1 Dado de ataque mientras tenga alguna ficha de espíritu. \n\n" +
                "Lupan Desatado: Las tiradas de Rodeado cuentan como éxitos mientras tenga alguna ficha de espíritu. \n\n" +
                "En Control (Una vez por partida): Usala en tu paso de Poder. Si hay luchadores amigos Inspirados, empuja a cada luchador amigo No Inspirado hasta 1 hexágono. \n\n" +
                "Autocontrol (Una vez por partida): Usala en tu paso de Poder. Elige un luchador amigo No Inspirado. Retira sus fichas de espíritu y dale una ficha de Guardia. \n\n" +
                "Instintos Aflorados (Una vez por partida): Usala en tu paso de Poder. Elige un luchador amigo. Las tiradas de Defensa no pueden verse afectadas por Romper o Apresar en el siguiente turno.",
                "Caos", "gnarlspirit_pack_0");

        // Sarrakkar (Líder)
        insertFighterInternal(db, (int)gnarlspiritBandId, "Sarrakkar", "gnarlspirit_pack_1");

        // Gorl
        insertFighterInternal(db, (int)gnarlspiritBandId, "Gorl", "gnarlspirit_pack_2");

        // Kheira
        insertFighterInternal(db, (int)gnarlspiritBandId, "Kheira", "gnarlspirit_pack_3");

        // Lupan
        insertFighterInternal(db, (int)gnarlspiritBandId, "Lupan", "gnarlspirit_pack_4");

        // --- Inserción de Kamandora's Blades (Band) ---
        long kamandoraBandId = insertBandInternal(db, "Kamandora's Blades",
                "Inspirar: Inmediatamente después de un paso de Poder, Inspira a cada luchador amigo adyacente a la calavera digna enemiga. Inmediatamente después de que la calavera digna enemiga sea eliminada por un luchador amigo, Inspira a ese luchador. \n\n" +
                "Una Calavera Digna: Al inicio del primer paso de Acción de cada ronda, Si hay algun luchador enemigo en el campo, tienes que elegir un luchador enemigo para ser la calavera digna hasta el final de la ronda. \n\n" +
                "Peregrinaje Sangriento: Después de elegir la calavera digna, tienes que empujar a cada luchador amigo 1 hexágono más cerca de ella. \n\n" +
                "¡Sangre para Khorne! (Tajo): Cuando un arma con esta habilidad de arma inflinje daño a un luchador enemigo, dale al objetivo un token de sangrado. Si un enemigo con token de sangrado usa una habilidad Basica, inflige 1 daño despues de resolver la habilidad basica, entonces retira el marcador de sangrado del luchador, De otra manera, retira todos los tokens de sangrado de luchadores enemigos inmediatamente despues de ultima fase de poder del enemigo, o al final de la ronda de batalla \n\n" +
                "Llamar a la Persecución (Una vez por partida): Usala en tu paso de poder en vez de jugar una carta. Elige un esbirro aliado. Empujalo hasta 4 hexágonos para estar adyacente al líder.",
                "Caos", "kamandoras_blades_0");

        // Kamandora (Líder)
        insertFighterInternal(db, (int)kamandoraBandId, "Kamandora", "kamandoras_blades_1");

        // Throkk
        insertFighterInternal(db, (int)kamandoraBandId, "Throkk", "kamandoras_blades_2");

        // Ghalista
        insertFighterInternal(db, (int)kamandoraBandId, "Ghalista", "kamandoras_blades_3");

        // Antro Krast
        insertFighterInternal(db, (int)kamandoraBandId, "Antro Krast", "kamandoras_blades_4");

        // Kannat
        insertFighterInternal(db, (int)kamandoraBandId, "Kannat", "kamandoras_blades_5");



        // --- Inserción de Thorns of the Briar Queen (Band) ---
        long thornsBandId = insertBandInternal(db, "Thorns of the Briar Queen",
                "Inspirar: Al principio de tu turno, Inspira cada luchador aliado que este adyacente a cualquier luchador enemigo. \n\n" +
                "Oleada de terror: Si el objetivo del ataque de un luchador aliado esta adyacente a otro luchador aliado con un token de Carga, el objetivo esta rodeado para ese ataque. \n\n" +
                "Guardián del alma (Acción basica): Elige un Varclav aliado para usar esta habilidad. Elige hasta 2 esbirros aliados. Empuja a cada uno de esos luchadores hasta 2 hexágonos. A continuación, puedes dar a cada uno de esos luchadores una ficha de Carga. Esta habilidad solo se puede usar una vez por ronda de combate. \n\n" +
                "Asaltado: Inmediatamente después de elegir a un luchador enemigo para ser asaltado (véase a la derecha), elige a un luchador aliado que no sea tu líder. Retira a ese luchador del campo de batalla y colócalo en un hexágono vacío adyacente al luchador enemigo. A continuación, puedes darle a ese luchador aliado una ficha de carga. \n\n" +
                "Matones emparejados (Una vez por partida): Úsala inmediatamente después de que un luchador aliado realice un movimiento. Elige un luchador enemigo adyacente a ese luchador para que sea asaltado.\n\n"+
                "No hay sitio en la cima (Una vez por partida): Úsalo inmediatamente después de que un líder enemigo utilice una habilidad básica. Elige al luchador que quieres que sea asaltado.\n\n"+
                "Represalias (Una vez por partida): Úsalo inmediatamente después de que un luchador enemigo haya realizado un ataque con éxito. Elige a ese luchador para que sea asaltado.\n\n"+
                "Déjalo estar (Una vez por partida): Úsalo inmediatamente después de que un luchador enemigo se mueva si lleva una ficha de tesoro. Elige a ese luchador para que sea asaltado.",
                "Muerte", "thorns_of_the_briar_queen_0");

        // Briar Queen (Líder)
        insertFighterInternal(db, (int)thornsBandId, "Briar Queen", "thorns_of_the_briar_queen_1");

        // Varclav
        insertFighterInternal(db, (int)thornsBandId, "Varclav", "thorns_of_the_briar_queen_2");

        // The Ever-hanged
        insertFighterInternal(db, (int)thornsBandId, "The Ever-hanged", "thorns_of_the_briar_queen_3");

        // The Ironwretch
        insertFighterInternal(db, (int)thornsBandId, "The Ironwretch", "thorns_of_the_briar_queen_4");

        // The Exhumed
        insertFighterInternal(db, (int)thornsBandId, "The Exhumed", "thorns_of_the_briar_queen_5");

        // The Silenced
        insertFighterInternal(db, (int)thornsBandId, "The Silenced", "thorns_of_the_briar_queen_6");

        // The Uncrowned
        insertFighterInternal(db, (int)thornsBandId, "The Uncrowned", "thorns_of_the_briar_queen_7");

        // --- Inserción de The Grymwatch (Band) ---
        long grymwatchBandId = insertBandInternal(db, "The Grymwatch",
                "Inspirar: Después del último paso de Acción en una ronda de batalla, elige hasta X luchadores aliados, donde X es la suma de las características de Recompensa de los luchadores enemigos muertos y los luchadores enemigos en territorio enemigo y neutral. Inspira a esos luchadores aliados. \n\n" +
                "¡En el nombre del rey!: Una vez por ronda de combate, en una fase de poder, si tu líder está en el campo de batalla, puedes usar una de las siguientes habilidades:\n" + //
                                        "• Elige un luchador aliado. Inspira a ese luchador.\n" + //
                                        "• Elige un luchador aliado muerto. Resucítalo y colócalo en un hexágono vacío del borde.\n" + //
                                        "\n" + //
                                        "Si eres el underdog, puedes usar ambas habilidades. \n\n" +
                "Al comienzo de cada ronda de batalla, puedes elegir una de las siguientes ilusiones para aplicar en esa ronda. Cada ilusión solo se puede elegir una vez por partida: \n" +
                "- Defensores del reino: Cada vez que realices una tirada de salvación para un luchador aliado en territorio amigo, puedes volver a tirar 1 dado en esa tirada. \n\n" +
                "- La caza real: Las armas cuerpo a cuerpo de los luchadores amigos tienen +1 dado de ataque si el objetivo del ataque estaba dañado previamente. \n\n" +
                "- ¡A las murallas!: Puedes usar la habilidad «¡En nombre del rey!» dos veces en esta ronda de batalla en lugar de una.",
                "Muerte", "the_grymwatch_0");

        // Duke Crakmarrow (Líder)
        insertFighterInternal(db, (int)grymwatchBandId, "Duke Crakmarrow", "the_grymwatch_1");

        // Gristlewel
        insertFighterInternal(db, (int)grymwatchBandId, "Gristlewel", "the_grymwatch_2");

        // Valreek
        insertFighterInternal(db, (int)grymwatchBandId, "Valreek", "the_grymwatch_3");

        // The Royal Butcher
        insertFighterInternal(db, (int)grymwatchBandId, "The Royal Butcher", "the_grymwatch_4");

        // Master Talon
        insertFighterInternal(db, (int)grymwatchBandId, "Master Talon", "the_grymwatch_5");

        // Night's Herald
        insertFighterInternal(db, (int)grymwatchBandId, "Night's Herald", "the_grymwatch_6");

        // The Duke's Harriers
        insertFighterInternal(db, (int)grymwatchBandId, "The Duke's Harriers", "the_grymwatch_7");

        // --- Inserción de The Crimson Court (Band) ---
        long crimsonBandId = insertBandInternal(db, "The Crimson Court",
                "Inspirar: Al final de cada ronda de combate, inspira a cada luchador aliado que no tenga fichas de hambre. \n\n" +
                "La maldición: Cada luchador aliado comienza el juego con 2 fichas de hambre. Al comienzo de la segunda y tercera ronda de combate, entrega a cada luchador aliado 1 ficha de hambre. \n\n" +
                "El hambre: Inmediatamente después de que un luchador aliado realice un ataque con éxito, retira 1 de las fichas de hambre de ese luchador. Si el objetivo de ese ataque ha sido asesinado, retira todas las fichas de hambre de ese luchador. \n\n" +
                "Sed de sangre: Mientras un luchador aliado tenga 3 o más fichas de hambre, estará sediento de sangre. Los luchadores aliados sedientos de sangre tienen +1 a Movimiento y las fichas de Guardia que tengan no tienen ningún efecto. \n\n" +
                "Transformación bestial (Una vez por partida): Descarta las mejoras de ese luchador. Hasta el final de la ronda de combate, las armas cuerpo a cuerpo de ese luchador tienen +1 dado de ataque. Si ese luchador esta sediento de sangre, también tiene +1 movimiento hasta el final de la ronda de combate. \n\n" +
                "Poder vampírico (Una vez por partida): Úsalo inmediatamente después de elegir un arma como parte del ataque de un luchador sediento de sangre. Esa arma tiene las habilidades Romper y Apresar para ese ataque. \n\n" +
                "Forma tenebrosa (Una vez por partida): Utiliza esta habilidad inmediatamente después de que un luchador aliado inspirado sea elegido como objetivo de un ataque. La característica de salvación de ese luchador es 3 Esquivas para ese ataque. \n\n" +
                "Transfusión oscura (Una vez por partida): Elige un luchador aliado para usar esta habilidad básica. Elige un luchador enemigo que se encuentre a 1 hexágono de distancia de ese luchador. Puedes elegir un luchador enemigo que se encuentre a 2 hexágonos de distancia si ese luchador aliado está inspirado. Inflige 1 punto de daño a ese luchador enemigo y luego cura a ese luchador aliado.",
                "Muerte", "the_crimson_court_0");

        // Prince Duvalle (Líder)
        insertFighterInternal(db, (int)crimsonBandId, "Prince Duvalle", "the_crimson_court_1");

        // Gorath the Enforcer
        insertFighterInternal(db, (int)crimsonBandId, "Gorath the Enforcer", "the_crimson_court_2");

        // Vellas von Faine
        insertFighterInternal(db, (int)crimsonBandId, "Vellas von Faine", "the_crimson_court_3");

        // Ennias Curse-born
        insertFighterInternal(db, (int)crimsonBandId, "Ennias Curse-born", "the_crimson_court_4");

        // --- Inserción de The Exiled Dead (Band) ---

        long exiledBandId = insertBandInternal(db, "The Exiled Dead",
                "Inspirar: Los esbirros aliados comienzan la partida Inspirados. Después de que un esbirro aliado mate a un luchador enemigo, inspira a Deintalos y Marcov. \n\n" +
                "Mejorador dinámico: Los luchadores conductores aliados tienen +X Movimiento, donde X es el número de luchadores conductores aliados muertos. \n\n" +
                "Sobrecarga: Los luchadores conductores amigos no pueden controlar fichas de tesoro ni Indagar. Las armas cuerpo a cuerpo de los luchadores conductores amigos (excepto las mejoras) tienen Doloroso si el objetivo tiene alguna ficha de Tambaleo. \n\n" +
                "Sobrecarga dinámica: Puedes volver a tirar 1 dado de ataque en una tirada de ataque para un luchador conductor aliado mientras haya otros luchadores conductores aliados adyacentes al atacante. \n\n" +
                "Titiritero (Una vez por ronda, habilidad basica): Elige un Marcov aliado para usar esta habilidad si no tiene fichas de Movimiento o Carga. Elige una de las siguientes opciones:\n" + //
                                        "• Ese luchador y un Regulus aliado pueden usar cada uno una habilidad básica que puedan usar.\n" + //
                                        "• Si un Regulus aliado esta muerto, resucítalo y colócalo en un hexágono vacío adyacente a ese luchador.\n" + //
                                        "Esta habilidad solo se puede usar una vez por ronda de batalla. \n\n" +
                "Danza dinamica (habilidad basica): Elige a tu líder para usar esta habilidad si no tiene fichas de Movimiento o Carga. Elige entre la habilidad Movimiento o la habilidad Ataque. Cada luchador conductor aliado puede usar esa habilidad.\n" + //
                                        "\n" + //
                                        "A continuación, puedes elegir un luchador conductor aliado muerto. Resucita a ese luchador conductor, colócalo en un hexágono vacío adyacente a tu líder y luego inflígele 1 punto de daño.",
                "Muerte", "the_exiled_dead_0");

        // Deintalos (Líder)
        insertFighterInternal(db, (int)exiledBandId, "Deintalos", "the_exiled_dead_1");

        // Marcov
        insertFighterInternal(db, (int)exiledBandId, "Marcov", "the_exiled_dead_2");

        // Regulus
        insertFighterInternal(db, (int)exiledBandId, "Regulus", "the_exiled_dead_3");

        // Bault
        insertFighterInternal(db, (int)exiledBandId, "Bault", "the_exiled_dead_4");

        // Vlash
        insertFighterInternal(db, (int)exiledBandId, "Vlash", "the_exiled_dead_5");

        // Ione
        insertFighterInternal(db, (int)exiledBandId, "Ione", "the_exiled_dead_6");

        // Coyl
        insertFighterInternal(db, (int)exiledBandId, "Coyl", "the_exiled_dead_7");



        String countdownRules =
                "Mientras uses este mazo de Rivales, durante el paso de Reunir Bandas de la preparación, coloca una ficha genérica en el primer escalón de la carta de cuenta atrás incluida en este mazo. Esa ficha es tu marcador de Cataclismo.\n\n" +
                "Debes avanzar tu marcador de Cataclismo 1 escalón la primera vez que un luchador aliado sea eliminado en cada fase de combate, y debes avanzar tu marcador de Cataclismo 1 escalón después del último paso de Poder de cada ronda de batalla por cada ficha de elemento que no tenga luchadores enemigos encima.\n\n" +
                "Cuando tu marcador esté:\n" +
                "- en el 1.º a 5.º escalón, tu valor de Cataclismo es 1.\n" +
                "- en el 6.º a 9.º escalón, tu valor de Cataclismo es 2.\n" +
                "- en el 10.º a 13.º escalón, tu valor de Cataclismo es 3.\n" +
                "- en el último escalón, tu valor de Cataclismo es 4.";
        long countdownDeckId = insertRivalDeckInternal(db, "Countdown to Cataclysm Rivals Deck", "Mazo Universal", null, "UNIVERSAL", countdownRules);

        insertCardInternal(db, "Spread Havoc", "Objective", "End Phase", 1, (int)countdownDeckId, 0,
                "Anota esta carta en una fase final. Gana un número de puntos de gloria igual a tu valor de Cataclismo, hasta un máximo de 2.", null, "card_cc1");
        insertCardInternal(db, "Hounds of War", "Objective", "End Phase", 2, (int)countdownDeckId, 1,
                "Anota esta carta en una fase final si 2 o más luchadores enemigos están eliminados y/o heridos y alguno de ellos fue eliminado en la fase de combate precedente.", null, "card_cc2");
        insertCardInternal(db, "Set Explosives", "Objective", "End Phase", 3, (int)countdownDeckId, 2,
                "Anota esta carta en una fase final si tu banda controla 2 o más fichas de tesoro y controla todas las fichas de tesoro de cualquier territorio.", null, "card_cc3");
        insertCardInternal(db, "Wreckers", "Objective", "End Phase", 4, (int)countdownDeckId, 2,
                "Anota esta carta en una fase final si el número de luchadores enemigos heridos y/o eliminados es mayor que tu valor de Cataclismo.", null, "card_cc4");
        insertCardInternal(db, "Uneven Contest", "Objective", "End Phase", 5, (int)countdownDeckId, 2,
                "Anota esta carta en una fase final si tu banda controla cada ficha de tesoro con número impar.", null, "card_cc5");
        insertCardInternal(db, "Loaded for Bear", "Objective", "End Phase", 6, (int)countdownDeckId, 1,
                "Anota esta carta en una fase final si cualquier luchador aliado está equipado con 3 o más Mejoras.", null, "card_cc6");
        insertCardInternal(db, "Collateral Damage", "Objective", "Surge", 7, (int)countdownDeckId, 1,
                "Anota esta carta inmediatamente después de avanzar tu marcador de Cataclismo 1 escalón como resultado de que un luchador aliado sea eliminado. Si eres el desventajado, anótala después de avanzar tu marcador de Cataclismo 1 escalón por cualquier motivo en su lugar.", null, "card_cc7");

        // Cartas de Countdown to Cataclysm (Lote 3)
        insertCardInternal(db, "Too Close for Comfort", "Objective", "Surge", 8, (int)countdownDeckId, 1,
                "Anota esta carta inmediatamente después del paso de Poder de tu oponente si cada luchador aliado está a 2 hexágonos o menos de cualquier luchador enemigo.", null, "card_cc8");
        insertCardInternal(db, "Shocking Assault", "Objective", "Surge", 9, (int)countdownDeckId, 1,
                "Anota esta carta inmediatamente después del paso de Acción de tu oponente si tu banda controla todas las fichas de tesoro en territorio neutral.", null, "card_cc9");
        insertCardInternal(db, "Nowhere to Run", "Objective", "Surge", 10, (int)countdownDeckId, 1,
                "Anota esta carta inmediatamente después de un paso de Acción si todos los luchadores aliados tienen fichas de Movimiento y/o Carga y hay al menos un luchador aliado en cada territorio.", null, "card_cc10");
        insertCardInternal(db, "The Perfect Cut", "Objective", "Surge", 11, (int)countdownDeckId, 1,
                "Anota esta carta inmediatamente después de un Ataque cuerpo a cuerpo exitoso de un luchador aliado si ningún resultado de la tirada de Salvación del objetivo fue un éxito.", null, "card_cc11");
        insertCardInternal(db, "Overwhelming Force", "Objective", "Surge", 12, (int)countdownDeckId, 1,
                "Anota esta carta inmediatamente después de un Ataque cuerpo a cuerpo exitoso de un luchador aliado si la tirada de Ataque contenía 4 o más dados.", null, "card_cc12");
        insertCardInternal(db, "Savage Blow", "Gambit", null, 13, (int)countdownDeckId, null,
                "Juega esta carta inmediatamente después de elegir un arma como parte de un Ataque. Los resultados de Doble Apoyo cuentan como éxitos para ese Ataque.", null, "card_cc13");
        insertCardInternal(db, "The End is Nigh", "Gambit", null, 14, (int)countdownDeckId, null,
                "Dominio: Después de cada paso de Acción, el jugador cuyo turno es tira un número de dados de Ataque igual a su valor de Cataclismo, o 1 dado de Ataque si no tiene dicho valor. Si la tirada contiene alguna Espada o éxito Crítico, su oponente debe descartar una carta de Poder. Este efecto persiste hasta el final de la ronda de batalla, hasta que se juegue otra carta de Dominio o hasta que avances tu marcador de Cataclismo.", null, "card_cc14");
        insertCardInternal(db, "Growing Concerns", "Gambit", null, 15, (int)countdownDeckId, null,
                "Los luchadores enemigos tienen -X Movimiento en el siguiente paso de Acción, donde X es tu valor de Cataclismo.", null, "card_cc15");

        // Cartas de Countdown to Cataclysm (Lote 4)
        insertCardInternal(db, "Total Collapse", "Gambit", null, 16, (int)countdownDeckId, null,
                "Tira un número de dados de Ataque igual a tu valor de Cataclismo. Si la tirada contiene alguna Espada o éxito Crítico, elige un luchador e inflígele 1 de daño. Si elegiste un luchador aliado, puedes infligirle hasta un número de puntos de daño igual a tu valor de Cataclismo en su lugar.", null, "card_cc16");
        insertCardInternal(db, "Violent Blast", "Gambit", null, 17, (int)countdownDeckId, null,
                "Elige un hexágono de Tambaleo. Empuja 1 hexágono a cada luchador que esté a 1 hexágono de ese hexágono de Tambaleo.", null, "card_cc17");
        insertCardInternal(db, "Sunder the Realm", "Gambit", null, 18, (int)countdownDeckId, null,
                "Tira un número de dados de Ataque igual a tu valor de Cataclismo por cada luchador que esté a 1 hexágono del territorio neutral. Si la tirada contiene algún Martillo o éxito Crítico, inflige 1 de daño a ese luchador.", null, "card_cc18");
        insertCardInternal(db, "Raging Tremors", "Gambit", null, 19, (int)countdownDeckId, null,
                "Elige hasta un número de luchadores enemigos igual a tu valor de Cataclismo. Da a cada uno de esos luchadores una ficha de Tambaleo.", null, "card_cc19");
        insertCardInternal(db, "Counter-charge", "Gambit", null, 20, (int)countdownDeckId, null,
                "Juega esta carta inmediatamente después de que se elija a un luchador aliado como objetivo de un Ataque. Elige otro luchador aliado y empújalo hasta 3 hexágonos, terminando adyacente al atacante.", null, "card_cc20");

        // Cartas de Countdown to Cataclysm (Lote 5)
        insertCardInternal(db, "Do or Die", "Gambit", null, 21, (int)countdownDeckId, null,
                "Elige un luchador aliado. Inspira a ese luchador. Este efecto persiste durante X pasos de Acción, donde X es tu valor de Cataclismo. Cuando termine ese efecto, no descartes esta carta; en su lugar, Desinspira a ese luchador. Ese luchador no puede inspirarse de nuevo. Este efecto persiste hasta el final de la partida.", null, "card_cc21");
        insertCardInternal(db, "Improvised Attack", "Gambit", null, 22, (int)countdownDeckId, null,
                "Elige un luchador aliado. Ese luchador realiza inmediatamente un Ataque con el siguiente arma, donde X es tu valor de Cataclismo: Alcance 3, Martillo, X dados, 1 Daño. Esta arma no puede modificarse.", null, "card_cc22");
        insertCardInternal(db, "Bringer of Doom", "Upgrade", null, 23, (int)countdownDeckId, 1,
                "Tic‑tac: Después de descartar esta carta durante una fase de combate, puedes avanzar inmediatamente tu marcador de Cataclismo 1 escalón.", null, "card_cc23");
        insertCardInternal(db, "Visions of Ruin", "Upgrade", null, 24, (int)countdownDeckId, 1,
                "Este luchador tiene +X Movimiento, donde X es tu valor de Cataclismo. Inmediatamente después de que este luchador se mueva, dale una ficha de Tambaleo.", null, "card_cc24");
        insertCardInternal(db, "Extinction's Edge", "Upgrade", null, 25, (int)countdownDeckId, 1,
                "Cada vez más cerca: Avanza inmediatamente tu marcador de Cataclismo 1 escalón después de que un luchador enemigo sea eliminado por este luchador. Luego, si tu valor de Cataclismo es 2 o más, descarta esta carta.", null, "card_cc25");

        // Cartas de Countdown to Cataclysm (Lote 6)
        insertCardInternal(db, "Driven by Pain", "Upgrade", null, 26, (int)countdownDeckId, 1,
                "Insensible: Inmediatamente después de que este luchador sea empujado hacia atrás, puedes curarlo.", null, "card_cc26");
        insertCardInternal(db, "Inescapable Grasp", "Upgrade", null, 27, (int)countdownDeckId, 1,
                "Las armas cuerpo a cuerpo de este luchador tienen Atrapador.", null, "card_cc27");
        insertCardInternal(db, "Utter Conviction", "Upgrade", null, 28, (int)countdownDeckId, 1,
                "La característica de Salvación de este luchador es X, donde X es tu valor de Cataclismo, y no puede modificarse más. Este luchador no puede usar habilidades de Arma Crítica.", null, "card_cc28");
        insertCardInternal(db, "Burnt Out", "Upgrade", null, 29, (int)countdownDeckId, 1,
                "Ardiendo: Elige un luchador aliado. Dale una ficha de Tambaleo. Luego roba 2 cartas de Poder. Si eres el desventajado, roba 3 cartas de Poder en su lugar.", null, "card_cc29");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 30, (int)countdownDeckId, 2,
                "Este luchador tiene +1 Herida.", null, "card_cc30");

        // Cartas de Countdown to Cataclysm (Lote 7 - Final)
        insertCardInternal(db, "Hurled Weapon", "Upgrade", null, 31, (int)countdownDeckId, 1,
                "Acción de Ataque a distancia: Alcance 3, Martillo, 2 dados, 1 Daño. Éxito Crítico: Perforante.", null, "card_cc31");
        insertCardInternal(db, "Desperate Rage", "Upgrade", null, 32, (int)countdownDeckId, 1,
                "Acción de Ataque cuerpo a cuerpo: Alcance 1, Martillo, 2 dados, 3 Daño. Inmediatamente después de que este luchador haya atacado con esta arma, inflige 1 de daño a este luchador.", null, "card_cc32");

        // --- Inserción de Deadly Synergy Rivals Deck ---
        String deadlySynergyRules =
                "Mientras uses este mazo de Rivales, los luchadores aliados adyacentes están unidos.\n\n" +
                "Los luchadores enemigos adyacentes a un luchador aliado unido están Flanqueados.\n\n" +
                "Si todos los demás luchadores aliados están eliminados, el luchador aliado restante está unido.";
        long deadlySynergyDeckId = insertRivalDeckInternal(db, "Deadly Synergy Rivals Deck", "Mazo Universal", null, "UNIVERSAL", deadlySynergyRules);

        // --- Cartas de Deadly Synergy ---
        insertCardInternal(db, "Aggressive Unity", "Objective", "End Phase", 1, (int)deadlySynergyDeckId, 2,
                "Anota esta carta en una fase final si en esta ronda de batalla se han realizado 3 o más Ataques cuerpo a cuerpo por luchadores aliados unidos.", null, "card_dy1");
        insertCardInternal(db, "Beatdown", "Objective", "End Phase", 2, (int)deadlySynergyDeckId, 1,
                "Anota esta carta en una fase final si en esta ronda de batalla el mismo luchador enemigo ha sido elegido como objetivo de 3 o más Ataques cuerpo a cuerpo de luchadores aliados distintos.", null, "card_dy2");
        insertCardInternal(db, "Closed Down", "Objective", "End Phase", 3, (int)deadlySynergyDeckId, 2,
                "Anota esta carta en una fase final si un luchador aliado unido sostiene una ficha de tesoro que en esta ronda de batalla estuvo en posesión de un luchador enemigo.", null, "card_dy3");
        insertCardInternal(db, "Got Your Back!", "Objective", "Surge", 4, (int)deadlySynergyDeckId, 1,
                "Anota esta carta inmediatamente después del fallo de un Ataque de un luchador enemigo si el objetivo era un luchador aliado unido.", null, "card_dy4");
        insertCardInternal(db, "Helping Hand", "Objective", "Surge", 5, (int)deadlySynergyDeckId, 1,
                "Anota esta carta inmediatamente después de un Ataque exitoso de un luchador aliado unido si la tirada de Ataque contenía al menos un resultado de Apoyo Sencillo.", null, "card_dy5");
        insertCardInternal(db, "Hemmed In", "Objective", "End Phase", 6, (int)deadlySynergyDeckId, 2,
                "Anota esta carta en una fase final si en esta ronda de batalla un líder enemigo ha sido eliminado por un luchador aliado unido.", null, "card_dy6");
        insertCardInternal(db, "Inevitable Outcome", "Objective", "End Phase", 7, (int)deadlySynergyDeckId, 1,
                "Anota esta carta en una fase final si 2 o más luchadores aliados unidos están adyacentes al mismo luchador enemigo.", null, "card_dy7");
        insertCardInternal(db, "Infiltrate", "Objective", "Surge", 8, (int)deadlySynergyDeckId, 1,
                "Anota esta carta inmediatamente después de un Ataque exitoso de un luchador aliado si el objetivo estaba en territorio neutral o enemigo y o bien el atacante estaba unido, o bien el objetivo estaba Flanqueado y/o Rodeado.", null, "card_dy8");
        insertCardInternal(db, "Outmuscle", "Objective", "End Phase", 9, (int)deadlySynergyDeckId, 2,
                "Anota esta carta en una fase final si en esta ronda de batalla un luchador enemigo fue empujado hacia atrás como parte del Ataque exitoso de un luchador aliado unido.", null, "card_dy9");
        insertCardInternal(db, "Tag Team", "Objective", "Surge", 10, (int)deadlySynergyDeckId, 1,
                "Anota esta carta inmediatamente después de un Ataque exitoso de un luchador aliado si no tiene fichas de Movimiento ni de Carga y o bien el atacante estaba unido, o bien el objetivo estaba Flanqueado y/o Rodeado.", null, "card_dy10");
        insertCardInternal(db, "Tandem Assault", "Objective", "Surge", 11, (int)deadlySynergyDeckId, 1,
                "Anota esta carta inmediatamente después de que un luchador enemigo sea eliminado por el Ataque de un luchador aliado si el atacante estaba unido o el objetivo estaba Flanqueado y/o Rodeado.", null, "card_dy11");
        insertCardInternal(db, "United Aid", "Objective", "Surge", 12, (int)deadlySynergyDeckId, 1,
                "Anota esta carta inmediatamente después de hacer una tirada de Salvación para un luchador aliado unido si hubo más éxitos en la tirada de Salvación que en la tirada de Ataque y la tirada de Salvación contenía al menos un resultado de Apoyo Sencillo. Si eres el desventajado, la tirada de Salvación no tiene por qué contener ningún Apoyo Sencillo.", null, "card_dy12");

        // --- Gambitos (Ploys) de Deadly Synergy ---
        insertCardInternal(db, "Another Swing", "Gambit", null, 13, (int)deadlySynergyDeckId, null,
                "Juega esta carta inmediatamente después de un Ataque cuerpo a cuerpo fallido de un luchador aliado unido si el atacante está adyacente a 2 luchadores aliados unidos. Ese luchador ataca de nuevo usando un arma cuerpo a cuerpo, pero la característica de Daño de esa arma se reduce a 1.", null, "card_dy13");
        insertCardInternal(db, "Army of Two", "Gambit", null, 14, (int)deadlySynergyDeckId, null,
                "Elige un luchador aliado. Ese luchador estará unido en el siguiente turno.", null, "card_dy14");
        insertCardInternal(db, "Brother-in-Arms", "Gambit", null, 15, (int)deadlySynergyDeckId, null,
                "Juega esta carta inmediatamente antes de elegir un arma cuerpo a cuerpo como parte del Ataque de un luchador aliado unido. Elige un luchador aliado unido adyacente al atacante. Usa 1 de las armas cuerpo a cuerpo de ese luchador para ese Ataque en su lugar.", null, "card_dy15");
        insertCardInternal(db, "Defiant Duo", "Gambit", null, 16, (int)deadlySynergyDeckId, null,
                "Elige 2 luchadores aliados unidos adyacentes. Si eres el desventajado, elige 2 luchadores aliados unidos cualesquiera en su lugar. Da a uno de esos luchadores una ficha de Guardia y luego cura al otro luchador.", null, "card_dy16");
        insertCardInternal(db, "Out of Nowhere!", "Gambit", null, 17, (int)deadlySynergyDeckId, null,
                "Juega esta carta inmediatamente después de hacer una tirada de Ataque para un luchador aliado unido. Cambia 1 dado de esa tirada de Ataque por un resultado de Apoyo Sencillo o Doble Apoyo.", null, "card_dy17");
        insertCardInternal(db, "Selfless Parry", "Gambit", null, 18, (int)deadlySynergyDeckId, null,
                "Juega esta carta inmediatamente después de que se elija a un luchador aliado unido como objetivo de un Ataque. Elige un luchador aliado unido adyacente al objetivo. El objetivo usa la característica de Salvación de ese luchador para ese Ataque.", null, "card_dy18");
        insertCardInternal(db, "Sidestep", "Gambit", null, 19, (int)deadlySynergyDeckId, null,
                "Elige un luchador aliado. Empuja a ese luchador 1 hexágono.", null, "card_dy19");
        insertCardInternal(db, "Take One for the Team", "Gambit", null, 20, (int)deadlySynergyDeckId, null,
                "Juega esta carta inmediatamente después de que se elija a un luchador aliado unido como objetivo de un Ataque. Elige un luchador aliado unido adyacente al objetivo y dentro del alcance del arma usada para el Ataque. Ese luchador debe ser el objetivo de ese Ataque en su lugar.", null, "card_dy20");
        insertCardInternal(db, "Timed Strike", "Gambit", null, 21, (int)deadlySynergyDeckId, null,
                "Juega esta carta inmediatamente después de elegir un arma cuerpo a cuerpo como parte del Ataque de un luchador aliado que no esté unido. Esa arma tiene +1 dado de Ataque para ese Ataque.", null, "card_dy21");
        insertCardInternal(db, "Unified Front", "Gambit", null, 22, (int)deadlySynergyDeckId, null,
                "Los luchadores aliados unidos tienen +1 a su Salvación en el siguiente turno, hasta un máximo de 3.", null, "card_dy22");

        // --- Mejoras (Upgrades) de Deadly Synergy ---
        insertCardInternal(db, "Battering Ram", "Upgrade", null, 23, (int)deadlySynergyDeckId, null,
                "Las tiradas de Ataque realizadas para este luchador como parte de Ataques cuerpo a cuerpo cuentan siempre como si tuvieran más éxitos Críticos que la tirada de Salvación a efectos de Empujar hacia atrás y Arrollar.", null, "card_dy23");
        insertCardInternal(db, "Coordinated Deathblow", "Upgrade", "Weapon", 24, (int)deadlySynergyDeckId, null,
                "Alcance 1, 3 Furia, 2 Daño. Esta arma tiene Doloroso mientras este luchador esté unido.", null, "card_dy24");
        insertCardInternal(db, "Duellist", "Upgrade", null, 25, (int)deadlySynergyDeckId, null,
                "Juego de pies: Inmediatamente después de que este luchador haya atacado, puedes empujarlo 1 hexágono.", null, "card_dy25");
        insertCardInternal(db, "Entangling Strike", "Upgrade", "Weapon", 26, (int)deadlySynergyDeckId, null,
                "Alcance 1, 3 Aplastar, 1 Daño. Esta arma tiene Perforante y Atrapador si el objetivo está Flanqueado y/o Rodeado.", null, "card_dy26");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 27, (int)deadlySynergyDeckId, null,
                "Este luchador tiene +1 Herida.", null, "card_dy27");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 28, (int)deadlySynergyDeckId, null,
                "Las armas cuerpo a cuerpo de este luchador tienen +1 dado de Ataque.", null, "card_dy28");
        insertCardInternal(db, "Rush to Aid", "Upgrade", null, 29, (int)deadlySynergyDeckId, null,
                "Ayuda: Inmediatamente después de que este luchador haya atacado, puedes elegir un luchador aliado unido. Empuja a este luchador hasta 2 hexágonos, terminando adyacente a ese luchador aliado unido.", null, "card_dy29");
        insertCardInternal(db, "Shared Incentive", "Upgrade", null, 30, (int)deadlySynergyDeckId, null,
                "Esta mejora solo puede darse a tu líder. Este luchador tiene +1 Movimiento. Los luchadores aliados unidos tienen +1 Movimiento si están adyacentes a este luchador cuando comienzan ese Movimiento.", null, "card_dy30");
        insertCardInternal(db, "Spurred Momentum", "Upgrade", null, 31, (int)deadlySynergyDeckId, null,
                "Impulso: Inmediatamente después del Ataque exitoso de este luchador, si estaba unido, retira sus fichas de Movimiento y de Tambaleo y luego descarta esta carta.", null, "card_dy31");
        insertCardInternal(db, "Titan of Combat", "Upgrade", null, 32, (int)deadlySynergyDeckId, null,
                "Este luchador está unido.", null, "card_dy32");

        // --- Inserción de Raging Slayers Rivals Deck ---
        String ragingSlayersRules =
                "Carga Rabiosa: Inmediatamente después de elegir a un luchador aliado para Cargar, puedes darle a ese luchador una ficha de Rabia. Los luchadores con cualquier ficha de Rabia están enloquecidos.\n\n" +
                "Golpe Rabioso: Después de hacer una tirada de Ataque como parte de un Ataque cuerpo a cuerpo de un luchador aliado enloquecido, puedes volver a tirar inmediatamente 1 dado de Ataque de esa tirada.\n\n" +
                "Mala Pisada: Debes usar esto inmediatamente después de que un luchador aliado enloquecido haya sido atacado si ese luchador no fue empujado hacia atrás ni agarrado. Empuja al objetivo 1 hexágono alejándolo del atacante. Tu oponente elige la dirección de ese empujón.\n\n" +
                "Retira todas las fichas de Rabia de los luchadores aliados enloquecidos al final de la ronda de batalla.";
        long ragingSlayersDeckId = insertRivalDeckInternal(db, "Raging Slayers Rivals Deck", "Mazo Universal", null, "UNIVERSAL", ragingSlayersRules);

        // --- Objetivos (Objectives) de Raging Slayers ---
        insertCardInternal(db, "Aggressive Expansion", "Objective", "End Phase", 1, (int)ragingSlayersDeckId, 1,
                "Anota esta carta en una fase final si 3 o más luchadores aliados enloquecidos están en territorio enemigo.", null, "card_rs1");
        insertCardInternal(db, "Best Foot Forward", "Objective", "Surge", 2, (int)ragingSlayersDeckId, 1,
                "Anota esta carta inmediatamente después de un Ataque exitoso de un luchador aliado si ese luchador tiene alguna ficha de Carga y está en territorio enemigo.", null, "card_rs2");
        insertCardInternal(db, "Blinded by Rage", "Objective", "Surge", 3, (int)ragingSlayersDeckId, 1,
                "Anota esta carta inmediatamente después de que un luchador enemigo sea eliminado por el Ataque cuerpo a cuerpo de un luchador aliado enloquecido si ese luchador aliado ya estaba enloquecido al comienzo del turno.", null, "card_rs3");
        insertCardInternal(db, "Coordinated Assault", "Objective", "Surge", 4, (int)ragingSlayersDeckId, 1,
                "Anota esta carta inmediatamente después de un paso de Acción si cada luchador aliado está enloquecido y en territorio enemigo.", null, "card_rs4");
        insertCardInternal(db, "Into the Fire", "Objective", "End Phase", 5, (int)ragingSlayersDeckId, 1,
                "Anota esta carta en una fase final si tu líder está en territorio enemigo y a 2 hexágonos o menos de 2 o más luchadores.", null, "card_rs5");
        insertCardInternal(db, "No Contest", "Objective", "End Phase", 6, (int)ragingSlayersDeckId, 1,
                "Anota esta carta en una fase final si un líder enemigo ha sido eliminado.", null, "card_rs6");
        insertCardInternal(db, "No Escape", "Objective", "End Phase", 7, (int)ragingSlayersDeckId, 2,
                "Anota esta carta en una fase final si cada luchador aliado está enloquecido y en territorio enemigo, y no hay luchadores enemigos en tu territorio.", null, "card_rs7");
        insertCardInternal(db, "No Respite", "Objective", "Surge", 8, (int)ragingSlayersDeckId, 1,
                "Anota esta carta inmediatamente después del paso de Acción de tu oponente si hay un luchador aliado enloquecido en cada territorio.", null, "card_rs8");
        insertCardInternal(db, "Overwhelming Presence", "Objective", "End Phase", 9, (int)ragingSlayersDeckId, 2,
                "Anota esta carta en una fase final si cada luchador aliado está enloquecido y no hay luchadores que sostengan fichas de tesoro.", null, "card_rs9");
        insertCardInternal(db, "Sever the Head", "Objective", "Surge", 10, (int)ragingSlayersDeckId, 1,
                "Anota esta carta inmediatamente después de que un líder enemigo sea eliminado por el Ataque cuerpo a cuerpo de un luchador aliado enloquecido.", null, "card_rs10");
        insertCardInternal(db, "Supreme Slayer", "Objective", "Surge", 11, (int)ragingSlayersDeckId, 1,
                "Anota esta carta inmediatamente después del Ataque exitoso de tu líder si el objetivo fue eliminado y ese era el segundo (o posterior) luchador eliminado por tu líder.", null, "card_rs11");
        insertCardInternal(db, "Unrelenting Massacre", "Objective", "End Phase", 12, (int)ragingSlayersDeckId, 3,
                "Anota esta carta en una fase final si todos los luchadores tienen fichas de Carga.", null, "card_rs12");

        // --- Gambitos (Ploys) de Raging Slayers ---
        insertCardInternal(db, "Adrenaline Rush", "Gambit", null, 13, (int)ragingSlayersDeckId, null,
                "Elige hasta 2 luchadores aliados enloquecidos. Retira sus fichas de Tambaleo.", null, "card_rs13");
        insertCardInternal(db, "Honed Reflexes", "Gambit", null, 14, (int)ragingSlayersDeckId, null,
                "Dominio: Cada vez que hagas una tirada de Salvación para un luchador aliado enloquecido, puedes repetir 1 dado de Salvación de esa tirada. Este efecto persiste hasta el final de la ronda de batalla o hasta que se juegue otra carta de Dominio.", null, "card_rs14");
        insertCardInternal(db, "Knife to the Heart", "Gambit", null, 15, (int)ragingSlayersDeckId, null,
                "Elige un luchador enemigo que no sea vulnerable y que esté adyacente a un luchador aliado enloquecido. Inflige 1 de daño a ese luchador enemigo. Luego, retira las fichas de Rabia de ese luchador aliado.", null, "card_rs15");
        insertCardInternal(db, "Murderlust", "Gambit", null, 16, (int)ragingSlayersDeckId, null,
                "Juega esta carta inmediatamente después de que falle el Ataque cuerpo a cuerpo de un luchador aliado enloquecido. Las armas cuerpo a cuerpo de los luchadores aliados tienen +1 dado de Ataque mientras tengan como objetivo al objetivo de ese Ataque fallido, hasta el final de la ronda de batalla.", null, "card_rs16");
        insertCardInternal(db, "Senseless Haste", "Gambit", null, 17, (int)ragingSlayersDeckId, null,
                "Los luchadores aliados tienen +1 Movimiento en el siguiente turno. Después de que cualquier luchador aliado se mueva en tu siguiente turno, dale a ese luchador una ficha de Tambaleo.", null, "card_rs17");
        insertCardInternal(db, "Slayer's Aid", "Gambit", null, 18, (int)ragingSlayersDeckId, null,
                "Elige un luchador aliado enloquecido. Empuja a ese luchador hasta 3 hexágonos, terminando adyacente a tu líder.", null, "card_rs18");
        insertCardInternal(db, "Slayer's Arena", "Gambit", null, 19, (int)ragingSlayersDeckId, null,
                "Dominio: Después de cada Ataque fallido, da al atacante una ficha de Tambaleo. Este efecto persiste hasta el final de la ronda de batalla o hasta que se juegue otra carta de Dominio.", null, "card_rs19");
        insertCardInternal(db, "Venting Strike", "Gambit", null, 20, (int)ragingSlayersDeckId, null,
                "Juega esta carta inmediatamente después de elegir un arma cuerpo a cuerpo como parte de un Ataque realizado por un luchador aliado enloquecido. Esa arma tiene +1 dado de Ataque para ese Ataque. Inmediatamente después de ese Ataque, retira las fichas de Rabia de ese luchador.", null, "card_rs20");
        insertCardInternal(db, "What Pain?", "Gambit", null, 21, (int)ragingSlayersDeckId, null,
                "Elige hasta 2 luchadores aliados enloquecidos. Cúralos. Luego, retira sus fichas de Rabia y dales una ficha de Tambaleo.", null, "card_rs21");
        insertCardInternal(db, "Wrong-footed Stance", "Gambit", null, 22, (int)ragingSlayersDeckId, null,
                "Dominio: Si un luchador entra en un hexágono de Tambaleo o es colocado en él, dale una ficha de Movimiento además de una ficha de Tambaleo. Este efecto persiste hasta el final de la ronda de batalla o hasta que se juegue otra carta de Dominio.", null, "card_rs22");

        // --- Mejoras (Upgrades) de Raging Slayers ---
        insertCardInternal(db, "Agile", "Upgrade", null, 23, (int)ragingSlayersDeckId, 2,
                "Desteza: Después de hacer una tirada de Salvación para este luchador, puedes volver a tirar inmediatamente 1 dado de Salvación de esa tirada.", null, "card_rs23");
        insertCardInternal(db, "Aggressive Ambusher", "Upgrade", null, 24, (int)ragingSlayersDeckId, 1,
                "Mientras este luchador esté enloquecido, sus armas cuerpo a cuerpo tienen Perforante y Atrapador si el objetivo está Flanqueado y/o Rodeado.", null, "card_rs24");
        insertCardInternal(db, "Angered Swing", "Upgrade", null, 25, (int)ragingSlayersDeckId, 1,
                "Acción de Ataque cuerpo a cuerpo:\n\nAlcance 1, Espada/Furia 3, Daño 2.\nUn luchador solo puede usar esta arma mientras esté enloquecido. Al usar la habilidad Golpe Rabioso con esta arma, debes volver a tirar cada dado de Ataque en su lugar.", null, "card_rs25");
        insertCardInternal(db, "Assured Bloodshed", "Upgrade", null, 26, (int)ragingSlayersDeckId, 1,
                "Mientras este luchador esté enloquecido, sus armas cuerpo a cuerpo tienen Agarre y Brutal.", null, "card_rs26");
        insertCardInternal(db, "Gifted Vitality", "Upgrade", null, 27, (int)ragingSlayersDeckId, 2,
                "Cura a este luchador al final de cada ronda de batalla.", null, "card_rs27");
        insertCardInternal(db, "Haymaker", "Upgrade", null, 28, (int)ragingSlayersDeckId, 2,
                "Las armas cuerpo a cuerpo de este luchador (excluyendo Mejoras) tienen la habilidad Letal. Después de que este luchador realice un Ataque exitoso con Letal, descarta esta carta.\n\nLetal: El arma de ese luchador tiene +2 Daño para ese Ataque, hasta un máximo de 4.", null, "card_rs28");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 29, (int)ragingSlayersDeckId, 2,
                "Las armas cuerpo a cuerpo de este luchador tienen +1 dado de Ataque.", null, "card_rs29");
        insertCardInternal(db, "Murderous Instincts", "Upgrade", null, 30, (int)ragingSlayersDeckId, 1,
                "Las armas cuerpo a cuerpo de Alcance 1 de este luchador (excluyendo Mejoras) tienen Doloroso.\n\nEste luchador no puede usar habilidades Básicas distintas de la habilidad de Carga.", null, "card_rs30");
        insertCardInternal(db, "Stubborn to the Bone", "Upgrade", null, 31, (int)ragingSlayersDeckId, 1,
                "Mientras este luchador esté enloquecido, no puede ser empujado ni recibir fichas de Tambaleo.", null, "card_rs31");
        insertCardInternal(db, "United in Anger", "Upgrade", null, 32, (int)ragingSlayersDeckId, 1,
                "Reunidos por la ira: Inmediatamente después de que un luchador aliado enloquecido sea empujado, puedes empujar a este luchador hasta 1 hexágono más cerca de ese luchador.", null, "card_rs32");

        // --- Inserción de Realmstone Raiders Rivals Deck ---
        String realmstoneRaidersRules =
                "Mientras uses este mazo de Rivales, inmediatamente después del primer Ataque cuerpo a cuerpo exitoso de un luchador aliado en un paso de Acción, puedes saquear un número de veces igual a la característica de Recompensa del objetivo.\n\n" +
                "Saqueo: Revela la primera carta de tu mazo de Poder. Esa carta se considera saqueada. Si esa carta es una carta de Emberstone, puedes colocarla de nuevo en la parte superior o en el fondo de tu mazo de Poder.\n\n" +
                "Las cartas de Emberstone tienen las siguientes reglas adicionales:\n\n" +
                "Ardid de Emberstone: Puedes jugar este Ardid inmediatamente después de que sea saqueado.\n\n" +
                "Mejora de Emberstone: Puedes equipar esta Mejora inmediatamente después de que sea saqueada si tienes los puntos de gloria necesarios.\n\n" +
                "Si una carta saqueada no es una carta de Emberstone, coloca esa carta en el fondo de tu mazo de Poder.";
        long realmstoneRaidersDeckId = insertRivalDeckInternal(db, "Realmstone Raiders Rivals Deck", "Mazo Universal", null, "UNIVERSAL", realmstoneRaidersRules);

        // --- Objetivos (Objectives) de Realmstone Raiders ---
        insertCardInternal(db, "A Sure Bet", "Objective", "Surge", 1, (int)realmstoneRaidersDeckId, 1,
                "Anota esta carta inmediatamente después de saquear una carta de Emberstone tras un Ataque cuerpo a cuerpo si la tirada de Ataque contenía 3 o más dados de Ataque.", null, "card_rr1");
        insertCardInternal(db, "Certain Aggression", "Objective", "End Phase", 2, (int)realmstoneRaidersDeckId, 2,
                "Anota esta carta en una fase final si has saqueado 2 o más veces en esta ronda de batalla y no hay luchadores enemigos en territorio aliado.", null, "card_rr2");
        insertCardInternal(db, "Critical Risk", "Objective", "Surge", 3, (int)realmstoneRaidersDeckId, 1,
                "Anota esta carta inmediatamente después de saquear una carta de Emberstone si el Ataque precedente fue exitoso y la tirada de Ataque incluía al menos un éxito Crítico.", null, "card_rr3");
        insertCardInternal(db, "Emberstone Stash", "Objective", "End Phase", 4, (int)realmstoneRaidersDeckId, 1,
                "Anota esta carta en una fase final si has saqueado 4 o más cartas de Emberstone diferentes en esta ronda de batalla.", null, "card_rr4");
        insertCardInternal(db, "Hoarder's Hovel", "Objective", "End Phase", 5, (int)realmstoneRaidersDeckId, 2,
                "Anota esta carta en una fase final si algún luchador aliado sostiene una ficha de tesoro cuyo valor es igual a la característica de Recompensa de ese luchador.", null, "card_rr5");
        insertCardInternal(db, "Invade", "Objective", "End Phase", 6, (int)realmstoneRaidersDeckId, 1,
                "Anota esta carta en una fase final si esta ronda de batalla has saqueado alguna carta de Emberstone tras un Ataque cuerpo a cuerpo cuyo objetivo sostenía una ficha de tesoro.", null, "card_rr6");
        insertCardInternal(db, "Looted Realmstone", "Objective", "Surge", 7, (int)realmstoneRaidersDeckId, 1,
                "Anota esta carta inmediatamente después de saquear 2 o más cartas de Emberstone diferentes a partir del mismo Ataque cuerpo a cuerpo.", null, "card_rr7");
        insertCardInternal(db, "Pillage", "Objective", "Surge", 8, (int)realmstoneRaidersDeckId, 1,
                "Anota esta carta inmediatamente después de saquear 2 o más cartas de Emberstone diferentes tras un Ataque cuerpo a cuerpo si el objetivo estaba en territorio neutral o enemigo.", null, "card_rr8");
        insertCardInternal(db, "Ragerock Strike", "Objective", "Surge", 9, (int)realmstoneRaidersDeckId, 1,
                "Anota esta carta inmediatamente después de resolver la habilidad de Ardid de Emberstone o la habilidad de Mejora de Emberstone (ver carta de Trama).", null, "card_rr9");
        insertCardInternal(db, "Realmstone Raid", "Objective", "End Phase", 10, (int)realmstoneRaidersDeckId, 2,
                "Anota esta carta en una fase final si has saqueado 3 o más veces después de que distintos luchadores aliados hayan atacado en esta ronda de batalla.", null, "card_rr10");
        insertCardInternal(db, "Reckless Gambit", "Objective", "Surge", 11, (int)realmstoneRaidersDeckId, 1,
                "Anota esta carta inmediatamente después de saquear una carta de Emberstone por segunda vez o más en esta ronda de batalla y que cada carta de Emberstone saqueada haya sido distinta.", null, "card_rr11");
        insertCardInternal(db, "Roused Violence", "Objective", "End Phase", 12, (int)realmstoneRaidersDeckId, 2,
                "Anota esta carta en una fase final si el número de fichas de tesoro en el campo de batalla es igual o menor que el número de cartas de Emberstone que has saqueado en esta ronda de batalla.", null, "card_rr12");

        // --- Gambitos (Gambits) de Realmstone Raiders ---
        insertCardInternal(db, "Ambush", "Gambit", null, 13, (int)realmstoneRaidersDeckId, null,
                "Juega esta carta inmediatamente después de elegir un arma a distancia como parte de un Ataque. Este turno puedes saquear después de un Ataque a distancia exitoso de un luchador aliado en lugar de después de un Ataque cuerpo a cuerpo.", null, "card_rr13");
        insertCardInternal(db, "Angered Focus", "Gambit", null, 14, (int)realmstoneRaidersDeckId, null,
                "Ardid de Emberstone\nElige un luchador aliado que sostenga una ficha de tesoro en territorio aliado y que no tenga fichas de Movimiento, Carga ni Tambaleo. Saquea un número de cartas igual a la característica de Recompensa de ese luchador. Las cartas saqueadas de este modo no cuentan como saqueadas como resultado de un Ataque exitoso.", null, "card_rr14");
        insertCardInternal(db, "A Step Ahead", "Gambit", null, 15, (int)realmstoneRaidersDeckId, null,
                "Ardid de Emberstone\nElige un luchador aliado en territorio neutral o enemigo. Empuja a ese luchador 1 hexágono, terminando más cerca de un luchador enemigo.", null, "card_rr15");
        insertCardInternal(db, "Fortune Faded", "Gambit", null, 16, (int)realmstoneRaidersDeckId, null,
                "Elige un luchador aliado adyacente a un luchador enemigo que no sea vulnerable. Inflige 1 de daño a ambos luchadores.", null, "card_rr16");
        insertCardInternal(db, "Hidden Knowledge", "Gambit", null, 17, (int)realmstoneRaidersDeckId, null,
                "Ardid de Emberstone\nElige un luchador enemigo adyacente a un luchador aliado. Dale a ese luchador una ficha de Movimiento.", null, "card_rr17");
        insertCardInternal(db, "Intoxicated with Rage", "Gambit", null, 18, (int)realmstoneRaidersDeckId, null,
                "Ardid de Emberstone\nElige un luchador aliado que no tenga fichas de Movimiento ni de Carga. Dale a ese luchador una ficha de Guardia.", null, "card_rr18");
        insertCardInternal(db, "Manipulated Fate", "Gambit", null, 19, (int)realmstoneRaidersDeckId, null,
                "Juega esta carta inmediatamente después de que se elija a un luchador aliado como objetivo de un Ataque. Las tiradas de Salvación hechas para ese Ataque cuentan como si tuvieran más éxitos Críticos que la tirada de Ataque a efectos de la habilidad Mantenerse Firme.", null, "card_rr19");
        insertCardInternal(db, "Misstep", "Gambit", null, 20, (int)realmstoneRaidersDeckId, null,
                "Ardid de Emberstone\nElige un luchador enemigo. Dale a ese luchador una ficha de Tambaleo.", null, "card_rr20");
        insertCardInternal(db, "Raider's Rapture", "Gambit", null, 21, (int)realmstoneRaidersDeckId, null,
                "Dominio: Cuando saques, puedes revelar 1 carta adicional. Este efecto persiste hasta el final de la ronda o hasta que se juegue otra carta de Dominio.", null, "card_rr21");
        insertCardInternal(db, "Raider's Premonition", "Gambit", null, 22, (int)realmstoneRaidersDeckId, null,
                "Ardid de Emberstone\nElige una carta de Objetivo de tu mano y colócala en el fondo de tu mazo de Objetivos. Luego roba 1 carta de Objetivo.", null, "card_rr22");

        // --- Mejoras (Upgrades) de Realmstone Raiders ---
        insertCardInternal(db, "Armour Piercer", "Upgrade", null, 23, (int)realmstoneRaidersDeckId, 2,
                "Mejora de Emberstone\nBrecha: Inmediatamente después de un Ataque empatado realizado por este luchador, si la característica de Daño del arma del atacante es mayor que la característica de Salvación del objetivo, inflige 1 de daño al objetivo. Luego descarta esta carta.", null, "card_rr23");
        insertCardInternal(db, "Brightstone Vigour", "Upgrade", null, 24, (int)realmstoneRaidersDeckId, 2,
                "Mejora de Emberstone\nCalmado por la matanza: Inmediatamente después de un Ataque exitoso realizado por este luchador, si la característica de Daño del arma usada es mayor que la característica de Salvación del objetivo, puedes curar al atacante. Luego descarta esta carta.", null, "card_rr24");
        insertCardInternal(db, "Call to Power", "Upgrade", null, 25, (int)realmstoneRaidersDeckId, 1,
                "Mejora de Emberstone\nAtiende la llamada: Inmediatamente después de un Ataque exitoso realizado por este luchador, puedes robar 1 carta de Poder. Luego descarta esta carta.", null, "card_rr25");
        insertCardInternal(db, "Emberstone Edge", "Upgrade", null, 26, (int)realmstoneRaidersDeckId, 2,
                "Mejora de Emberstone\nAcción de Ataque cuerpo a cuerpo:\nAlcance 1, Martillo 2, Daño 1.\nEsta arma tiene +1 dado de Ataque si has saqueado en la misma fase.", null, "card_rr26");
        insertCardInternal(db, "Forgotten Fortune", "Upgrade", null, 27, (int)realmstoneRaidersDeckId, 1,
                "Mejora de Emberstone\nPoder persistente: Inmediatamente después de un Ataque exitoso realizado por este luchador, puedes elegir 1 Ardid de Emberstone de tu pila de descarte de Poder. Añádelo a tu mano. Luego descarta esta carta.", null, "card_rr27");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 28, (int)realmstoneRaidersDeckId, 2,
                "Este luchador tiene +1 Herida.", null, "card_rr28");
        insertCardInternal(db, "Great Speed", "Upgrade", null, 29, (int)realmstoneRaidersDeckId, 0,
                "Este luchador tiene +1 Movimiento.", null, "card_rr29");
        insertCardInternal(db, "Great Strength", "Upgrade", null, 30, (int)realmstoneRaidersDeckId, 2,
                "Las armas cuerpo a cuerpo de este luchador tienen Doloroso.", null, "card_rr30");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 31, (int)realmstoneRaidersDeckId, 2,
                "Las armas cuerpo a cuerpo de este luchador tienen +1 dado de Ataque.", null, "card_rr31");
        insertCardInternal(db, "Reforged Aid", "Upgrade", null, 32, (int)realmstoneRaidersDeckId, 1,
                "Mejora de Emberstone\nArmas recuperadas: Inmediatamente después de un Ataque exitoso realizado por este luchador, puedes elegir 1 Mejora de Emberstone de tu pila de descarte de Poder. Añádela a tu mano. Luego descarta esta carta.", null, "card_rr32");

        // --- Inserción de Edge of the Knife Rivals Deck ---
        String edgeKnifeRules =
                "Mientras uses este mazo de Rivales, los luchadores con una característica de Heridas de 2 o menos y/o con 2 o más fichas de daño están templados.";
        long edgeKnifeDeckId = insertRivalDeckInternal(db, "Edge of the Knife Rivals Deck", "Mazo Universal", null, "UNIVERSAL", edgeKnifeRules);

        // --- Objetivos (Objectives) de Edge of the Knife ---
        insertCardInternal(db, "Aggressive Defender", "Objective", "Surge", 1, (int)edgeKnifeDeckId, 1,
                "Anota esta carta inmediatamente después del Ataque de un luchador aliado si el atacante sostiene una ficha de tesoro.", null, "card_ek1");
        insertCardInternal(db, "All In", "Objective", "End Phase", 2, (int)edgeKnifeDeckId, 1,
                "Anota esta carta en una fase final si no hay luchadores aliados templados en territorio aliado y hay luchadores aliados templados en territorio neutral y/o enemigo.", null, "card_ek2");
        insertCardInternal(db, "Trial of the Tempered", "Objective", "End Phase", 10, (int)edgeKnifeDeckId, 3,
                "Anota esta carta en una fase final si cada luchador está templado.", null, "card_ek10");
        insertCardInternal(db, "Two-pronged Assault", "Objective", "End Phase", 11, (int)edgeKnifeDeckId, 1,
                "Anota esta carta en una fase final si 2 o más luchadores aliados templados están en territorio enemigo.", null, "card_ek11");
        insertCardInternal(db, "Usurper", "Objective", "End Phase", 12, (int)edgeKnifeDeckId, 2,
                "Anota esta carta en una fase final si un líder enemigo ha sido eliminado por un luchador aliado templado en esta ronda de batalla.", null, "card_ek12");
        insertCardInternal(db, "Behind Enemy Lines", "Objective", "Surge", 3, (int)edgeKnifeDeckId, 1,
                "Anota esta carta inmediatamente después del paso de Acción de tu oponente si un luchador aliado templado sostiene una ficha de elemento en territorio enemigo.", null, "card_ek3");
        insertCardInternal(db, "Calm Before The Storm", "Objective", "End Phase", 4, (int)edgeKnifeDeckId, 2,
                "Anota esta carta en una fase final si hay algún luchador herido y esos luchadores no están adyacentes entre sí.", null, "card_ek4");
        insertCardInternal(db, "Double Team", "Objective", "Surge", 5, (int)edgeKnifeDeckId, 1,
                "Anota esta carta inmediatamente después de un Ataque exitoso de un luchador aliado si el objetivo estaba Flanqueado por un luchador aliado.", null, "card_ek5");
        insertCardInternal(db, "Immovable", "Objective", "Surge", 6, (int)edgeKnifeDeckId, 1,
                "Anota esta carta inmediatamente después del paso de Acción de tu oponente si un luchador aliado fue objetivo de un Ataque en ese paso de Acción mientras estaba templado y el objetivo no fue eliminado.", null, "card_ek6");
        insertCardInternal(db, "Power in Numbers", "Objective", "Surge", 7, (int)edgeKnifeDeckId, 1,
                "Anota esta carta inmediatamente después de un paso de Acción si 3 o más luchadores templados sin fichas de Movimiento están adyacentes. Si eres el desventajado, anótala si 2 o más luchadores templados sin fichas de Movimiento están adyacentes en su lugar.", null, "card_ek7");
        insertCardInternal(db, "Risky Position", "Objective", "End Phase", 8, (int)edgeKnifeDeckId, 1,
                "Anota esta carta en una fase final si un luchador aliado templado está en territorio enemigo.", null, "card_ek8");
        insertCardInternal(db, "Sneak Into Position", "Objective", "Surge", 9, (int)edgeKnifeDeckId, 1,
                "Anota esta carta inmediatamente después del paso de Acción de tu oponente si 2 o más luchadores aliados templados están adyacentes al mismo luchador enemigo.", null, "card_ek9");

        // --- Gambitos (Ploys) de Edge of the Knife ---
        insertCardInternal(db, "Death Throes", "Ploy", null, 13, (int)edgeKnifeDeckId, null,
                "Elige un luchador aliado templado. En el siguiente paso de Acción, los Ataques que tengan como objetivo a ese luchador tienen -2 dados de Ataque.", null, "card_ek13");
        insertCardInternal(db, "Fake Out!", "Ploy", null, 14, (int)edgeKnifeDeckId, null,
                "Elige un luchador enemigo con una característica de Heridas de 3 o más que esté adyacente a un luchador aliado templado y luego tira un dado de Ataque. Si la tirada contiene algún Martillo o Espada, inflige 1 de daño a ese luchador y dale una ficha de Tambaleo. Si eres el desventajado, la tirada también puede contener éxitos Críticos.", null, "card_ek14");
        insertCardInternal(db, "Final Stand", "Ploy", null, 15, (int)edgeKnifeDeckId, null,
                "En el siguiente paso de Acción, los luchadores aliados templados no pueden ser empujados.", null, "card_ek15");
        insertCardInternal(db, "Opportunity Strikes", "Ploy", null, 16, (int)edgeKnifeDeckId, null,
                "Juega esta carta inmediatamente después de elegir un arma como parte del Ataque de un luchador aliado templado. Esa arma tiene +1 dado de Ataque para ese Ataque.", null, "card_ek16");
        insertCardInternal(db, "Power From Death", "Ploy", null, 17, (int)edgeKnifeDeckId, null,
                "Juega esta carta inmediatamente después de que un luchador aliado con una Recompensa de 1 o más sea eliminado por un luchador enemigo, si ese luchador estaba templado antes de infligir el daño que lo eliminaría. Roba hasta 3 cartas de Poder.", null, "card_ek17");
        insertCardInternal(db, "Running Riot", "Ploy", null, 18, (int)edgeKnifeDeckId, null,
                "Juega esta carta inmediatamente después de un Ataque exitoso de un luchador aliado templado. Elige otro luchador aliado. Ese luchador pasa a estar templado.", null, "card_ek18");
        insertCardInternal(db, "Sidestep", "Ploy", null, 19, (int)edgeKnifeDeckId, null,
                "Elige un luchador aliado. Empuja a ese luchador 1 hexágono.", null, "card_ek19");
        insertCardInternal(db, "Spiteful Traps", "Ploy", null, 20, (int)edgeKnifeDeckId, null,
                "Elige un luchador enemigo con una característica de Heridas de 3 o más que esté a 2 hexágonos o menos de tu líder. Dale a ese luchador una ficha de Movimiento.", null, "card_ek20");
        insertCardInternal(db, "Synchronised Effort", "Ploy", null, 21, (int)edgeKnifeDeckId, null,
                "Elige 2 luchadores aliados templados que estén a 4 hexágonos o menos entre sí. Retira esos luchadores del campo de batalla y luego coloca cada uno en el hexágono del que fue retirado el otro.", null, "card_ek21");
        insertCardInternal(db, "The Uprising!", "Ploy", null, 22, (int)edgeKnifeDeckId, null,
                "Todos los luchadores aliados están templados en el siguiente paso de Acción.", null, "card_ek22");

        // --- Mejoras (Upgrades) de Edge of the Knife ---
        insertCardInternal(db, "Dark Horse", "Upgrade", null, 23, (int)edgeKnifeDeckId, 1,
                "Este luchador está templado.", null, "card_ek23");
        insertCardInternal(db, "Deadly Aim", "Upgrade", null, 24, (int)edgeKnifeDeckId, 1,
                "Las armas de este luchador tienen Atrapador.", null, "card_ek24");
        insertCardInternal(db, "Fuelled by Pain", "Upgrade", null, 25, (int)edgeKnifeDeckId, 1,
                "Acción de Ataque cuerpo a cuerpo: Alcance 1, X dados (Martillo), Daño 2. Palabra clave: Atrapador. X es igual al número de fichas de daño que tiene este luchador.", null, "card_ek25");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 26, (int)edgeKnifeDeckId, 1,
                "Este luchador tiene +1 Herida.", null, "card_ek26");
        insertCardInternal(db, "Impervious", "Upgrade", null, 27, (int)edgeKnifeDeckId, 1,
                "Mientras este luchador esté templado, las tiradas de Salvación para este luchador no pueden verse afectadas por Perforante ni Atrapador.", null, "card_ek27");
        insertCardInternal(db, "Lash Out", "Upgrade", null, 28, (int)edgeKnifeDeckId, 1,
                "Azote: Inmediatamente después de que este luchador sea eliminado, antes de retirarlo del campo de batalla, tira un número de dados de Salvación igual a la ronda de batalla. Por cada Escudo, da a un luchador enemigo a 2 hexágonos o menos de este luchador una ficha de Movimiento.", null, "card_ek28");
        insertCardInternal(db, "Mobbed!", "Upgrade", null, 29, (int)edgeKnifeDeckId, 1,
                "Cuando este luchador ataque, si el objetivo está a 3 hexágonos o menos de cualquier otro luchador aliado, el objetivo está Flanqueado y Rodeado para ese Ataque.", null, "card_ek29");
        insertCardInternal(db, "Parting Shot", "Upgrade", null, 30, (int)edgeKnifeDeckId, 1,
                "Golpe pírrico: Inmediatamente después de que este luchador sea eliminado por un luchador enemigo, antes de retirarlo del campo de batalla, elige un luchador enemigo a 2 hexágonos o menos. Da a ese luchador una ficha de Tambaleo. Si este luchador estaba templado antes de infligir el daño que lo eliminaría, inflige 1 de daño a ese luchador.", null, "card_ek30");
        insertCardInternal(db, "Pesky Nuisance", "Upgrade", null, 31, (int)edgeKnifeDeckId, 2,
                "Los luchadores enemigos adyacentes a este luchador deben elegirlo a él como objetivo de sus Ataques.", null, "card_ek31");
        insertCardInternal(db, "Sharpened Points", "Upgrade", null, 32, (int)edgeKnifeDeckId, 1,
                "Las armas de este luchador tienen Perforante.", null, "card_ek32");

        // --- Inserción de Hunting Grounds Rivals Deck ---
        long huntingGroundsDeckId = insertRivalDeckInternal(db, "Hunting Grounds Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);

        // --- Objetivos (Objectives) de Hunting Grounds ---
        insertCardInternal(db, "Back Off!", "Objective", "Surge", 1, (int)huntingGroundsDeckId, 1,
                "Anota esta carta inmediatamente después del Ataque de un luchador aliado si el objetivo fue empujado a un territorio diferente.", null, "card_hg1");
        insertCardInternal(db, "Bloodscent", "Objective", "End Phase", 2, (int)huntingGroundsDeckId, 1,
                "Anota esta carta en una fase final si algún luchador enemigo en territorio aliado tiene 2 o más fichas de daño y/o es vulnerable. Si eres el desventajado, esos luchadores enemigos pueden estar en territorio enemigo en su lugar.", null, "card_hg2");
        insertCardInternal(db, "Hands Off!", "Objective", "Surge", 3, (int)huntingGroundsDeckId, 1,
                "Anota esta carta inmediatamente después del Ataque de un luchador aliado si el objetivo estaba sobre una ficha de elemento y fue empujado hacia atrás.", null, "card_hg3");
        insertCardInternal(db, "Home Advantage", "Objective", "Surge", 4, (int)huntingGroundsDeckId, 1,
                "Anota esta carta inmediatamente después del fallo de un Ataque de un luchador enemigo si el objetivo era un luchador aliado en territorio aliado.", null, "card_hg4");
        insertCardInternal(db, "Lead by Example", "Objective", "End Phase", 5, (int)huntingGroundsDeckId, 2,
                "Anota esta carta en una fase final si tu líder está en territorio aliado y ha atacado 2 o más veces en esta ronda de batalla.", null, "card_hg5");
        insertCardInternal(db, "No Business Here", "Objective", "Surge", 6, (int)huntingGroundsDeckId, 1,
                "Anota esta carta inmediatamente después del Ataque de un luchador aliado si el objetivo estaba en territorio aliado y estaba Flanqueado y/o Rodeado.", null, "card_hg6");
        insertCardInternal(db, "No Trespassers", "Objective", "Surge", 7, (int)huntingGroundsDeckId, 1,
                "Anota esta carta inmediatamente después del Ataque de un luchador aliado si el objetivo estaba en territorio aliado y fue eliminado.", null, "card_hg7");
        insertCardInternal(db, "Pinned!", "Objective", "End Phase", 8, (int)huntingGroundsDeckId, 3,
                "Anota esta carta en una fase final si en esta ronda de batalla un luchador enemigo fue empujado hacia atrás hasta un hexágono de borde en territorio aliado.", null, "card_hg8");
        insertCardInternal(db, "Ready or Not", "Objective", "End Phase", 9, (int)huntingGroundsDeckId, 2,
                "Anota esta carta en una fase final si 2 o más luchadores enemigos están en territorio aliado y cada luchador enemigo está herido y/o adyacente a algún luchador aliado.", null, "card_hg9");
        insertCardInternal(db, "Spoiling for a Fight", "Objective", "End Phase", 10, (int)huntingGroundsDeckId, 1,
                "Anota esta carta en una fase final si tu líder está en territorio aliado y a 2 hexágonos o menos de 2 o más luchadores de 2 o más bandas.", null, "card_hg10");
        insertCardInternal(db, "This is Our Turf!", "Objective", "End Phase", 11, (int)huntingGroundsDeckId, 1,
                "Anota esta carta en una fase final si hay más luchadores aliados con fichas de Movimiento y/o Carga en territorio aliado que luchadores enemigos con fichas de Movimiento y/o Carga en territorio aliado.", null, "card_hg11");
        insertCardInternal(db, "Usurped", "Objective", "Surge", 12, (int)huntingGroundsDeckId, 1,
                "Anota esta carta inmediatamente después del Ataque de un luchador aliado si el atacante está sobre una ficha de elemento en la que estaba el objetivo cuando lo elegiste como objetivo de ese Ataque.", null, "card_hg12");

        // --- Ardides (Gambits) de Hunting Grounds ---
        insertCardInternal(db, "Audacious Denial", "Gambit", "Ploy", 13, (int)huntingGroundsDeckId, null,
                "Si tu líder está en territorio aliado, elige un luchador enemigo adyacente a él en territorio aliado. Tu líder ataca inmediatamente a ese luchador con una de sus armas cuerpo a cuerpo que tenga una característica de Daño de 1.", null, "card_hg13");
        insertCardInternal(db, "Deny Invaders", "Gambit", "Ploy", 14, (int)huntingGroundsDeckId, null,
                "En el siguiente turno, las armas de los luchadores aliados tienen +1 dado de Ataque mientras estén en territorio aliado.", null, "card_hg14");
        insertCardInternal(db, "Hidden Snares", "Gambit", "Ploy", 15, (int)huntingGroundsDeckId, null,
                "Elige un luchador enemigo en territorio aliado y a 1 hexágono o menos de una ficha de elemento. Si eres el desventajado, puedes elegir en su lugar un luchador enemigo en territorio enemigo y a 1 hexágono o menos de una ficha de elemento. Las armas de ese luchador tienen -1 dado de Ataque en el siguiente turno.", null, "card_hg15");
        insertCardInternal(db, "Keep Them at Bay", "Gambit", "Ploy", 16, (int)huntingGroundsDeckId, null,
                "En el siguiente turno, las armas cuerpo a cuerpo de los luchadores aliados tienen +1 Alcance, hasta un máximo de 2, para Ataques que tengan como objetivo luchadores enemigos en territorio aliado.", null, "card_hg16");
        insertCardInternal(db, "Mind Your Step", "Gambit", "Ploy", 17, (int)huntingGroundsDeckId, null,
                "Elige un luchador enemigo en territorio aliado. Empuja a ese luchador hasta 2 hexágonos, terminando en territorio neutral o enemigo.", null, "card_hg17");
        insertCardInternal(db, "Mystical Misdirection", "Gambit", "Ploy", 18, (int)huntingGroundsDeckId, null,
                "Elige 2 fichas de tesoro en territorio aliado. Intercambia las posiciones de esas fichas de tesoro.", null, "card_hg18");
        insertCardInternal(db, "Paths Unknown", "Gambit", "Ploy", 19, (int)huntingGroundsDeckId, null,
                "Elige un luchador aliado en territorio aliado. Si eres el desventajado, puedes elegir en su lugar un luchador aliado en territorio enemigo. En el siguiente turno, ese luchador no puede ser elegido como objetivo de un Ataque.", null, "card_hg19");
        insertCardInternal(db, "Poor Footing", "Gambit", "Ploy", 20, (int)huntingGroundsDeckId, null,
                "Elige un luchador enemigo en territorio aliado y a 1 hexágono o menos de una ficha de elemento. Dale a ese luchador una ficha de Movimiento.", null, "card_hg20");
        insertCardInternal(db, "Secrets of the Realm", "Gambit", "Ploy", 21, (int)huntingGroundsDeckId, null,
                "Da a cada luchador enemigo en territorio aliado que esté adyacente a un luchador aliado y/o a una ficha de elemento una ficha de Tambaleo.", null, "card_hg21");
        insertCardInternal(db, "Sidestep", "Gambit", "Ploy", 22, (int)huntingGroundsDeckId, null,
                "Elige un luchador aliado. Empuja a ese luchador 1 hexágono.", null, "card_hg22");

        // --- Mejoras (Upgrades) de Hunting Grounds ---
        insertCardInternal(db, "Balance of Ghyran", "Upgrade", null, 23, (int)huntingGroundsDeckId, 1,
                "Equilibrio: Inmediatamente después de un Ataque exitoso de un luchador enemigo, si este luchador fue el objetivo de ese Ataque y el atacante está a 3 hexágonos o menos de él, tira 3 dados de Ataque. Si la tirada contiene alguna Espada, inflige 1 de daño al atacante.", null, "card_hg23");
        insertCardInternal(db, "Blocked!", "Upgrade", null, 24, (int)huntingGroundsDeckId, 1,
                "Estorbar: La primera vez que cada luchador enemigo entra en un hexágono adyacente a este luchador como parte de un Movimiento, tira un dado de Ataque. Con Martillo o éxito Crítico, ese luchador debe terminar ese Movimiento en ese hexágono.", null, "card_hg24");
        insertCardInternal(db, "Bounty of Ghyran", "Upgrade", null, 25, (int)huntingGroundsDeckId, 1,
                "Este luchador está Inspirado mientras esté en territorio aliado. Inmediatamente después de tu siguiente paso de Acción, Desinspira a este luchador y descarta esta carta.", null, "card_hg25");
        insertCardInternal(db, "Crippling Blow", "Upgrade", null, 26, (int)huntingGroundsDeckId, 1,
                "Alcance 2, 3 Espadas, 1 Daño. Si el objetivo está en territorio aliado, dale a ese luchador una ficha de Movimiento.", null, "card_hg26");
        insertCardInternal(db, "Goading Defender", "Upgrade", null, 27, (int)huntingGroundsDeckId, 1,
                "Este luchador tiene +1 a su Salvación mientras esté en territorio aliado. Descarta esta carta si se elige a este luchador como objetivo de un Ardid o si sufre daño.", null, "card_hg27");
        insertCardInternal(db, "Great Speed", "Upgrade", null, 28, (int)huntingGroundsDeckId, 1,
                "Este luchador tiene +1 Movimiento.", null, "card_hg28");
        insertCardInternal(db, "Hidden Aid", "Upgrade", null, 29, (int)huntingGroundsDeckId, 1,
                "Los luchadores enemigos adyacentes a este luchador están Flanqueados.", null, "card_hg29");
        insertCardInternal(db, "Hidden Traps", "Upgrade", null, 30, (int)huntingGroundsDeckId, 1,
                "Alcance 1, 3 Martillos, 1 Daño. Esta arma tiene Doloroso si el objetivo está a 1 hexágono o menos de una ficha de elemento en territorio aliado.", null, "card_hg30");
        insertCardInternal(db, "Killing Blow", "Upgrade", null, 31, (int)huntingGroundsDeckId, 1,
                "Las armas cuerpo a cuerpo de este luchador tienen Doloroso si el objetivo está herido.", null, "card_hg31");
        insertCardInternal(db, "True Grit", "Upgrade", null, 32, (int)huntingGroundsDeckId, 1,
                "Esta Mejora solo puede darse a tu líder. Los resultados de Escudo y Esquiva cuentan como éxitos en las tiradas de Salvación de los luchadores aliados adyacentes a este luchador.", null, "card_hg32");

        // --- Inserción de Reckless Fury Rivals Deck ---
        long recklessFuryDeckId = insertRivalDeckInternal(db, "Reckless Fury Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);

        // --- Objetivos (Objectives) de Reckless Fury ---
        insertCardInternal(db, "Aim for the Top", "Objective", "End Phase", 1, (int)recklessFuryDeckId, 1,
                "Anota esta carta en una fase final si la característica total de Recompensa de los luchadores enemigos heridos y/o eliminados es 4 o más.", null, "card_rf1");
        insertCardInternal(db, "Arena Mortis", "Objective", "End Phase", 2, (int)recklessFuryDeckId, 2,
                "Anota esta carta en una fase final si todos los luchadores en el mismo territorio tienen fichas de Carga y su característica total de Recompensa es 4 o más.", null, "card_rf2");
        insertCardInternal(db, "Best Foot Forward", "Objective", "Surge", 3, (int)recklessFuryDeckId, 1,
                "Anota esta carta inmediatamente después de un Ataque exitoso de un luchador aliado si ese luchador tiene alguna ficha de Carga y está en territorio enemigo.", null, "card_rf3");
        insertCardInternal(db, "Bloodbathed Rampager", "Objective", "End Phase", 4, (int)recklessFuryDeckId, 1,
                "Anota esta carta en una fase final si un luchador aliado tiene 2 o más fichas de Carga.", null, "card_rf4");
        insertCardInternal(db, "Bloody Momentum", "Objective", "End Phase", 5, (int)recklessFuryDeckId, 2,
                "Anota esta carta en una fase final si 2 o más luchadores aliados con una característica total de Recompensa de 4 o más tienen fichas de Carga y están en territorio enemigo.", null, "card_rf5");
        insertCardInternal(db, "Frenzied Rush", "Objective", "Surge", 6, (int)recklessFuryDeckId, 1,
                "Anota esta carta inmediatamente después de un paso de Acción si luchadores aliados con una característica total de Recompensa de 3 o más tienen fichas de Carga y están en territorio enemigo.", null, "card_rf6");
        insertCardInternal(db, "Living Bludgeon", "Objective", "Surge", 7, (int)recklessFuryDeckId, 1,
                "Anota esta carta inmediatamente después de un paso de Acción si un luchador aliado tiene una ficha de Guardia y una ficha de Carga y no está en territorio aliado.", null, "card_rf7");
        insertCardInternal(db, "Red Aftermath", "Objective", "End Phase", 8, (int)recklessFuryDeckId, 1,
                "Anota esta carta en una fase final si la característica total de Recompensa de los luchadores enemigos eliminados es 2 o más.", null, "card_rf8");
        insertCardInternal(db, "Sally Forth", "Objective", "Surge", 9, (int)recklessFuryDeckId, 1,
                "Anota esta carta inmediatamente después del paso de Acción de tu oponente si un luchador aliado con alguna ficha de Carga sostiene una ficha de tesoro en territorio enemigo.", null, "card_rf9");
        insertCardInternal(db, "Savage Sprinter", "Objective", "Surge", 10, (int)recklessFuryDeckId, 1,
                "Anota esta carta inmediatamente después de un paso de Acción si un luchador aliado en territorio enemigo tiene 2 o más fichas de Movimiento.", null, "card_rf10");
        insertCardInternal(db, "Unrelenting Massacre", "Objective", "End Phase", 11, (int)recklessFuryDeckId, 3,
                "Anota esta carta en una fase final si todos los luchadores tienen fichas de Carga.", null, "card_rf11");
        insertCardInternal(db, "Vicious Brawl", "Objective", "Surge", 12, (int)recklessFuryDeckId, 1,
                "Anota esta carta inmediatamente después de un paso de Acción si hay 3 o más luchadores con fichas de Carga adyacentes entre sí. Si eres el desventajado, pueden ser 2 o más luchadores en su lugar.", null, "card_rf12");

        // --- Ardides (Gambits) de Reckless Fury ---
        insertCardInternal(db, "Braced", "Gambit", "Ploy", 13, (int)recklessFuryDeckId, null,
                "Elige un luchador aliado. Dale a ese luchador una ficha de Carga.", null, "card_rf13");
        insertCardInternal(db, "Catch Weapon", "Gambit", "Ploy", 14, (int)recklessFuryDeckId, null,
                "Juega esta carta inmediatamente después de un Ataque exitoso de un luchador. Dale a ese luchador una ficha de Carga.", null, "card_rf14");
        insertCardInternal(db, "Diving In", "Gambit", "Ploy", 15, (int)recklessFuryDeckId, null,
                "Elige un luchador aliado. Empuja a ese luchador hasta 2 hexágonos, terminando adyacente a cualquier luchador con fichas de Carga.", null, "card_rf15");
        insertCardInternal(db, "Get It Done", "Gambit", "Ploy", 16, (int)recklessFuryDeckId, null,
                "Elige 2 luchadores aliados. Retira una ficha de Carga de uno de esos luchadores y luego da una ficha de Carga al otro luchador.", null, "card_rf16");
        insertCardInternal(db, "Lost Legacy", "Gambit", "Ploy", 17, (int)recklessFuryDeckId, null,
                "Juega esta carta inmediatamente después de descartar las Mejoras de un luchador aliado eliminado. Elige 1 de esas Mejoras, añádela a tu mano y luego roba 1 carta de Poder.", null, "card_rf17");
        insertCardInternal(db, "Outburst", "Gambit", "Ploy", 18, (int)recklessFuryDeckId, null,
                "Elige un luchador aliado con alguna ficha de Carga. Tira un dado de Ataque por cada luchador enemigo adyacente a él. Si eres el desventajado, tira en su lugar un número de dados igual al número de la ronda de batalla. Si la tirada contiene algún Martillo, inflige 1 de daño a ese luchador enemigo.", null, "card_rf18");
        insertCardInternal(db, "Over to You", "Gambit", "Ploy", 19, (int)recklessFuryDeckId, null,
                "Juega esta carta inmediatamente antes de retirar las fichas de un luchador aliado eliminado si ese luchador fue eliminado por un atacante y tenía fichas de Movimiento, Carga, Guardia y/o Tambaleo. Elige un luchador aliado o al atacante. Da al luchador elegido todas las fichas de Movimiento, Carga, Guardia y Tambaleo del luchador eliminado.", null, "card_rf19");
        insertCardInternal(db, "Push Through", "Gambit", "Ploy", 20, (int)recklessFuryDeckId, null,
                "Elige un luchador aliado con una característica de Recompensa de 2 o menos. Inflige 1 de daño a ese luchador. En tu siguiente paso de Acción, ese luchador puede usar habilidades Básicas como si no tuviera fichas de Movimiento ni Carga.", null, "card_rf20");
        insertCardInternal(db, "Quick Shift", "Gambit", "Ploy", 21, (int)recklessFuryDeckId, null,
                "Elige un luchador aliado que tenga fichas de Movimiento. Retira 1 de esas fichas de Movimiento y luego da a ese luchador una ficha de Carga.", null, "card_rf21");
        insertCardInternal(db, "Reckless Attitudes", "Gambit", "Ploy", 22, (int)recklessFuryDeckId, null,
                "En el siguiente paso de Acción, los luchadores enemigos no pueden usar habilidades Básicas distintas de la habilidad de Carga.", null, "card_rf22");

        // --- Mejoras (Upgrades) de Reckless Fury ---
        insertCardInternal(db, "Bellowing Tyrant", "Upgrade", null, 23, (int)recklessFuryDeckId, 1,
                "Brama: Este luchador puede usar esta habilidad Básica si tiene fichas de Carga. Empuja hasta 2 hexágonos a cada otro luchador aliado con fichas de Carga. Luego elige 1 de esos luchadores y retira 1 de sus fichas de Carga.", null, "card_rf23");
        insertCardInternal(db, "Bladecatcher", "Upgrade", null, 24, (int)recklessFuryDeckId, 1,
                "Mientras este luchador sea el objetivo de un Ataque, el atacante no puede usar habilidades de Arma.", null, "card_rf24");
        insertCardInternal(db, "Blades of Wrath", "Upgrade", null, 25, (int)recklessFuryDeckId, 1,
                "Si este luchador es eliminado, antes de retirarlo del campo de batalla, tira un número de dados de Ataque igual al número de la ronda de batalla por cada luchador enemigo adyacente a él. Si la tirada contiene algún Martillo, inflige 1 de daño a ese luchador.", null, "card_rf25");
        insertCardInternal(db, "Furious Might", "Upgrade", null, 26, (int)recklessFuryDeckId, 1,
                "Las armas cuerpo a cuerpo de este luchador tienen Doloroso mientras este luchador tenga fichas de Carga y no esté usando la habilidad de Carga.", null, "card_rf26");
        insertCardInternal(db, "Fury of Aqshy", "Upgrade", null, 27, (int)recklessFuryDeckId, 1,
                "Los luchadores adyacentes a este luchador no pueden usar habilidades Básicas distintas de la habilidad de Carga.", null, "card_rf27");
        insertCardInternal(db, "Headcase", "Upgrade", null, 28, (int)recklessFuryDeckId, 0,
                "Si este luchador no tiene fichas de Carga, en lugar de jugar una carta de Poder en un paso de Poder, puedes darle a este luchador una ficha de Carga.", null, "card_rf28");
        insertCardInternal(db, "Headlong Charge", "Upgrade", null, 29, (int)recklessFuryDeckId, 0,
                "Este luchador tiene +2 Movimiento mientras usa la habilidad de Carga.", null, "card_rf29");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 30, (int)recklessFuryDeckId, 2,
                "Las armas cuerpo a cuerpo de este luchador tienen +1 dado de Ataque.", null, "card_rf30");
        insertCardInternal(db, "Still Swinging", "Upgrade", null, 31, (int)recklessFuryDeckId, 1,
                "Las armas cuerpo a cuerpo de este luchador tienen Atrapador mientras este luchador tenga fichas de Carga y no esté usando la habilidad de Carga.", null, "card_rf31");
        insertCardInternal(db, "Utter Ignorance", "Upgrade", null, 32, (int)recklessFuryDeckId, 2,
                "Si este luchador fuera a ser eliminado, no es eliminado. Retira fichas de daño de este luchador hasta que quede vulnerable y luego descarta esta carta.", null, "card_rf32");

        // --- Inserción de Wrack and Ruin Rivals Deck ---
        long wrackAndRuinDeckId = insertRivalDeckInternal(db, "Wrack and Ruin Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);

        // --- Objetivos (Objectives) de Wrack and Ruin ---
        insertCardInternal(db, "Alone in the Dark", "Objective", "End Phase", 1, (int)wrackAndRuinDeckId, 2,
                "Anota esta carta en una fase final si ningún luchador está adyacente a otro.", null, "card_wr1");
        insertCardInternal(db, "Bloody and Bruised", "Objective", "Surge", 2, (int)wrackAndRuinDeckId, 1,
                "Anota esta carta inmediatamente después de que tu banda inflija daño a un luchador enemigo si 3 o más luchadores están heridos.", null, "card_wr2");
        insertCardInternal(db, "Careful Advance", "Objective", "Surge", 3, (int)wrackAndRuinDeckId, 1,
                "Anota esta carta inmediatamente después de que un luchador aliado se Mueva si 2 o más luchadores aliados con fichas de Movimiento están en territorio enemigo.", null, "card_wr3");
        insertCardInternal(db, "Living on the Edge", "Objective", "End Phase", 4, (int)wrackAndRuinDeckId, 2,
                "Anota esta carta en una fase final si un luchador aliado vulnerable está en territorio enemigo.", null, "card_wr4");
        insertCardInternal(db, "Low on Options", "Objective", "Surge", 5, (int)wrackAndRuinDeckId, 1,
                "Anota esta carta inmediatamente después de descartar una carta de Poder si hay 5 o más Ardides en tu pila de descarte de Poder.", null, "card_wr5");
        insertCardInternal(db, "Out of the Frying Pan", "Objective", "End Phase", 6, (int)wrackAndRuinDeckId, 2,
                "Anota esta carta en una fase final si 3 o más luchadores aliados heridos con fichas de Movimiento y/o Carga están en territorio enemigo.", null, "card_wr6");
        insertCardInternal(db, "Ploymaster", "Objective", "End Phase", 7, (int)wrackAndRuinDeckId, 1,
                "Anota esta carta en una fase final si has jugado 3 o más Ardides en esta ronda de batalla.", null, "card_wr7");
        insertCardInternal(db, "Predictable End", "Objective", "Surge", 8, (int)wrackAndRuinDeckId, 1,
                "Anota esta carta inmediatamente después de que una habilidad de una carta aliada de Wrack and Ruin inflija daño a un luchador enemigo que sostiene una ficha de tesoro.", null, "card_wr8");
        insertCardInternal(db, "Spread Out!", "Objective", "End Phase", 9, (int)wrackAndRuinDeckId, 1,
                "Anota esta carta en una fase final si hay un luchador aliado en cada territorio.", null, "card_wr9");
        insertCardInternal(db, "Stay Close", "Objective", "End Phase", 10, (int)wrackAndRuinDeckId, 2,
                "Anota esta carta en una fase final si no hay luchadores en hexágonos de borde.", null, "card_wr10");
        insertCardInternal(db, "Strong Start", "Objective", "Surge", 11, (int)wrackAndRuinDeckId, 1,
                "Anota esta carta inmediatamente después de que un luchador enemigo sea eliminado si fue el primer luchador eliminado de esta fase de combate.", null, "card_wr11");
        insertCardInternal(db, "Unsafe Ground", "Objective", "Surge", 12, (int)wrackAndRuinDeckId, 1,
                "Anota esta carta inmediatamente después de que tu banda inflija daño a un luchador enemigo en un hexágono de borde. Si eres el desventajado, ese luchador enemigo puede estar a 1 hexágono de un hexágono de borde en su lugar.", null, "card_wr12");

        // --- Gambitos (Ploys) de Wrack and Ruin ---
        insertCardInternal(db, "Confusion", "Ploy", null, 13, (int)wrackAndRuinDeckId, 0,
                "Elige 2 luchadores adyacentes. Retira esos luchadores del campo de batalla y colócalos en el hexágono del que fue retirado el otro.", null, "card_wr13");
        insertCardInternal(db, "Damned if You Do", "Ploy", null, 14, (int)wrackAndRuinDeckId, 0,
                "Tu oponente debe elegir 1 de las siguientes habilidades para que la resuelvas: Elige un luchador enemigo y empújalo 1 hexágono. Elige un luchador enemigo que no sea vulnerable e inflígele 1 de daño.", null, "card_wr14");
        insertCardInternal(db, "Deadly Traps", "Ploy", null, 15, (int)wrackAndRuinDeckId, 0,
                "Juega esta carta inmediatamente después de un Ataque con éxito parcial de un luchador aliado si el objetivo no es vulnerable y fue empujado hacia atrás. Inflige 1 de daño al objetivo.", null, "card_wr15");
        insertCardInternal(db, "Fault Lines", "Ploy", null, 16, (int)wrackAndRuinDeckId, 0,
                "Elige un luchador enemigo sin heridas. Inflige 1 de daño a ese luchador. Luego tu oponente puede elegir un luchador e infligir 1 de daño a ese luchador.", null, "card_wr16");
        insertCardInternal(db, "Fireproof", "Ploy", null, 17, (int)wrackAndRuinDeckId, 0,
                "La primera vez que se inflija daño a un luchador aliado en el siguiente turno, reduce ese daño a 1.", null, "card_wr17");
        insertCardInternal(db, "Flee!", "Ploy", null, 18, (int)wrackAndRuinDeckId, 0,
                "Elige un luchador aliado en territorio aliado. Ese luchador tiene +3 Movimiento en tu siguiente paso de Acción. Si ese luchador se Mueve en ese paso de Acción, ese Movimiento no puede terminar en territorio aliado.", null, "card_wr18");
        insertCardInternal(db, "Ominous Rumbling", "Ploy", null, 19, (int)wrackAndRuinDeckId, 0,
                "Tu oponente debe elegir 1 de las siguientes habilidades para que la resuelvas: Elige 2 luchadores enemigos y dales a cada uno una ficha de Tambaleo. Elige un luchador enemigo que no sea vulnerable e inflígele 1 de daño.", null, "card_wr19");
        insertCardInternal(db, "Sidle Up", "Ploy", null, 20, (int)wrackAndRuinDeckId, 0,
                "Elige un luchador aliado. Empuja a ese luchador hasta 2 hexágonos, terminando adyacente a 2 o más luchadores.", null, "card_wr20");
        insertCardInternal(db, "Vicious Intent", "Ploy", null, 21, (int)wrackAndRuinDeckId, 0,
                "Juega esta carta inmediatamente después de elegir un arma cuerpo a cuerpo como parte de un Ataque. Esa arma tiene +1 dado de Ataque para ese Ataque. Si el objetivo no tiene heridas, esa arma tiene +2 dados de Ataque para ese Ataque en su lugar.", null, "card_wr21");
        insertCardInternal(db, "Volcanic Eruption", "Ploy", null, 22, (int)wrackAndRuinDeckId, 0,
                "Elige un luchador. Luego tu oponente puede elegir un luchador enemigo. Empezando por el luchador que elegiste, tira un número de dados de Ataque para cada uno de esos luchadores igual a su característica de Recompensa, con un mínimo de 1. Si la tirada contiene algún Martillo, inflige 1 de daño a ese luchador.", null, "card_wr22");

        // --- Mejoras (Upgrades) de Wrack and Ruin ---
        insertCardInternal(db, "Barge", "Upgrade", null, 23, (int)wrackAndRuinDeckId, 2,
                "Arrollar: Este luchador puede usar esta habilidad Básica si no tiene fichas de Movimiento y/o Carga. Este luchador se Mueve. Ese Movimiento debe terminar adyacente a un luchador enemigo. Dale a este luchador una ficha de Carga. Luego elige un luchador enemigo adyacente a este luchador, empújalo 1 hexágono y dale una ficha de Tambaleo.", null, "card_wr23");
        insertCardInternal(db, "Desperate Defence", "Upgrade", null, 24, (int)wrackAndRuinDeckId, 2,
                "Mientras este luchador sea el objetivo de un Ataque, las armas del atacante tienen -1 Daño. La próxima vez que se inflija daño a este luchador como parte de un Ataque, descarta esta carta.", null, "card_wr24");
        insertCardInternal(db, "Fiery Temper", "Upgrade", null, 25, (int)wrackAndRuinDeckId, 0,
                "Beligerante: Inmediatamente después de que se haya elegido a este luchador para ser empujado, puedes infligir 1 de daño a este luchador. Si lo haces, este luchador no es empujado.", null, "card_wr25");
        insertCardInternal(db, "Great Speed", "Upgrade", null, 26, (int)wrackAndRuinDeckId, 0,
                "Este luchador tiene +1 Movimiento.", null, "card_wr26");
        insertCardInternal(db, "Henchman", "Upgrade", null, 27, (int)wrackAndRuinDeckId, 1,
                "Disciplinado: Inmediatamente después de hacer una tirada de Ataque para este luchador, puedes cambiar 1 resultado a un único Apoyo. No puedes volver a tirar tiradas de Ataque para este luchador.", null, "card_wr27");
        insertCardInternal(db, "Misfortune", "Upgrade", null, 28, (int)wrackAndRuinDeckId, 0,
                "Infortunio: Inmediatamente después de que este luchador use una habilidad Básica, puedes infligir 1 de daño a este luchador. Luego puedes retirar esta Mejora de este luchador y equipar a otro luchador aliado con esta Mejora.", null, "card_wr28");
        insertCardInternal(db, "Rock-splitting Tread", "Upgrade", null, 29, (int)wrackAndRuinDeckId, 1,
                "Pisotear: Inmediatamente después de tu último paso de Acción de una ronda de batalla, elige un luchador enemigo adyacente que no sea vulnerable. Inflige 1 de daño a ese luchador. Luego tira un dado de Ataque. Con Martillo, descarta esta carta.", null, "card_wr29");
        insertCardInternal(db, "Sundering Weapon", "Upgrade", null, 30, (int)wrackAndRuinDeckId, 1,
                "Las armas cuerpo a cuerpo de este luchador tienen Perforante.", null, "card_wr30");
        insertCardInternal(db, "Unstoppable", "Upgrade", null, 31, (int)wrackAndRuinDeckId, 2,
                "Mientras este luchador sea vulnerable, cada vez que se le inflija 1 de daño, reduce ese daño a 0.", null, "card_wr31");
        insertCardInternal(db, "Wary Tread", "Upgrade", null, 32, (int)wrackAndRuinDeckId, 1,
                "Puedes usar esta habilidad después del último paso de Poder de una ronda de batalla. Empuja a este luchador 1 hexágono. Ese empuje no puede terminar adyacente a ningún luchador.", null, "card_wr32");

        // --- Inserción de Blazing Assault Rivals Deck ---
        long blazingAssaultDeckId = insertRivalDeckInternal(db, "Blazing Assault Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);

        // --- Objetivos (Objectives) de Blazing Assault ---
        insertCardInternal(db, "Strike the Head", "Objective", "Surge", 1, (int)blazingAssaultDeckId, 1,
                "Anota esta carta inmediatamente después de que un luchador aliado elimine a un luchador enemigo si el objetivo era un líder o si su característica de Salud era igual o mayor que la del atacante.", null, "card_bl1");
        insertCardInternal(db, "Branching Fate", "Objective", "Surge", 2, (int)blazingAssaultDeckId, 1,
                "Anota esta carta inmediatamente después de hacer una tirada de Ataque con 3 o más dados si cada resultado fue un símbolo diferente. Si eres el desventajado, la tirada de Ataque puede contener 2 o más dados en su lugar.", null, "card_bl2");
        insertCardInternal(db, "Perfect Strike", "Objective", "Surge", 3, (int)blazingAssaultDeckId, 1,
                "Anota esta carta inmediatamente después de hacer una tirada de Ataque si todos los resultados fueron éxitos.", null, "card_bl3");
        insertCardInternal(db, "Critical Effort", "Objective", "Surge", 4, (int)blazingAssaultDeckId, 1,
                "Anota esta carta inmediatamente después de hacer una tirada de Ataque si alguno de los resultados fue un éxito Crítico.", null, "card_bl4");
        insertCardInternal(db, "Get Stuck In", "Objective", "Surge", 5, (int)blazingAssaultDeckId, 1,
                "Anota esta carta inmediatamente después del Ataque de un luchador aliado si el objetivo estaba en territorio enemigo.", null, "card_bl5");
        insertCardInternal(db, "Strong Start", "Objective", "Surge", 6, (int)blazingAssaultDeckId, 1,
                "Anota esta carta inmediatamente después de que un luchador enemigo sea eliminado si fue el primer luchador eliminado de esta fase de combate.", null, "card_bl6");
        insertCardInternal(db, "Keep Choppin'", "Objective", "End Phase", 7, (int)blazingAssaultDeckId, 1,
                "Anota esta carta en una fase final si tu banda ha realizado 3 o más Ataques en esta fase de combate.", null, "card_bl7");
        insertCardInternal(db, "Fields of Blood", "Objective", "End Phase", 8, (int)blazingAssaultDeckId, 1,
                "Anota esta carta en una fase final si 4 o más luchadores están heridos y/o eliminados.", null, "card_bl8");
        insertCardInternal(db, "Go All Out", "Objective", "End Phase", 9, (int)blazingAssaultDeckId, 1,
                "Anota esta carta en una fase final si 5 o más luchadores tienen fichas de Movimiento y/o Carga.", null, "card_bl9");
        insertCardInternal(db, "On the Edge", "Objective", "End Phase", 10, (int)blazingAssaultDeckId, 1,
                "Anota esta carta en una fase final si algún luchador enemigo es vulnerable.", null, "card_bl10");
        insertCardInternal(db, "Denial", "Objective", "End Phase", 11, (int)blazingAssaultDeckId, 1,
                "Anota esta carta en una fase final si no hay luchadores enemigos en territorio aliado.", null, "card_bl11");
        insertCardInternal(db, "Annihilation", "Objective", "End Phase", 12, (int)blazingAssaultDeckId, 5,
                "Anota esta carta en una fase final si todos los luchadores enemigos han sido eliminados.", null, "card_bl12");

        // --- Ardides (Gambits) de Blazing Assault ---
        insertCardInternal(db, "Determined Effort", "Gambit", "Ploy", 13, (int)blazingAssaultDeckId, null,
                "Juega esta carta inmediatamente después de elegir un arma como parte de un Ataque. Esa arma tiene +1 dado de Ataque para ese Ataque. Si eres el desventajado, esa arma tiene +2 dados de Ataque para ese Ataque en su lugar.", null, "card_bl13");
        insertCardInternal(db, "Twist the Knife", "Gambit", "Ploy", 14, (int)blazingAssaultDeckId, null,
                "Juega esta carta inmediatamente después de elegir un arma cuerpo a cuerpo como parte de un Ataque. Esa arma tiene Doloroso para ese Ataque.", null, "card_bl14");
        insertCardInternal(db, "Lure of Battle", "Gambit", "Ploy", 15, (int)blazingAssaultDeckId, null,
                "Elige 1 luchador aliado que esté a 2 hexágonos o menos de otro luchador. Empuja al otro luchador 1 hexágono más cerca de ese luchador aliado.", null, "card_bl15");
        insertCardInternal(db, "Sidestep", "Gambit", "Ploy", 16, (int)blazingAssaultDeckId, null,
                "Elige un luchador aliado. Empuja a ese luchador 1 hexágono.", null, "card_bl16");
        insertCardInternal(db, "Commanding Stride", "Gambit", "Ploy", 17, (int)blazingAssaultDeckId, null,
                "Empuja a tu líder hasta 3 hexágonos. Ese empuje debe terminar en un hexágono de salida.", null, "card_bl17");
        insertCardInternal(db, "Illusory Fighter", "Gambit", "Ploy", 18, (int)blazingAssaultDeckId, null,
                "Elige un luchador aliado. Retira a ese luchador del campo de batalla y luego colócalo en un hexágono de salida vacío en territorio aliado.", null, "card_bl18");
        insertCardInternal(db, "Wings of War", "Gambit", "Ploy", 19, (int)blazingAssaultDeckId, null,
                "Juega esta carta inmediatamente después de elegir un luchador para que se Mueva. Ese luchador tiene +2 Movimiento para ese Movimiento.", null, "card_bl19");
        insertCardInternal(db, "Shields Up!", "Gambit", "Ploy", 20, (int)blazingAssaultDeckId, null,
                "Elige un luchador aliado. Dale a ese luchador una ficha de Guardia.", null, "card_bl20");
        insertCardInternal(db, "Scream of Anger", "Gambit", "Ploy", 21, (int)blazingAssaultDeckId, null,
                "Elige un luchador aliado. Inflige 2 de daño a ese luchador y luego retira 1 de sus fichas de Movimiento o Carga.", null, "card_bl21");
        insertCardInternal(db, "Healing Potion", "Gambit", "Ploy", 22, (int)blazingAssaultDeckId, null,
                "Elige un luchador aliado. Sana a ese luchador. Si eres el desventajado, puedes tirar un dado de Salvación. Con Escudo o éxito Crítico, sana a ese luchador otra vez.", null, "card_bl22");

        // --- Mejoras (Upgrades) de Blazing Assault ---
        insertCardInternal(db, "Brawler", "Upgrade", null, 23, (int)blazingAssaultDeckId, 1,
                "Este luchador no puede ser Flanqueado ni Rodeado.", null, "card_bl23");
        insertCardInternal(db, "Hidden Aid", "Upgrade", null, 24, (int)blazingAssaultDeckId, 1,
                "Los luchadores enemigos adyacentes a este luchador están Flanqueados.", null, "card_bl24");
        insertCardInternal(db, "Accurate", "Upgrade", null, 25, (int)blazingAssaultDeckId, 1,
                "Golpe Certero: Después de hacer una tirada de Ataque para este luchador, puedes volver a tirar inmediatamente 1 dado de Ataque de esa tirada.", null, "card_bl25");
        insertCardInternal(db, "Great Strength", "Upgrade", null, 26, (int)blazingAssaultDeckId, 2,
                "Las armas cuerpo a cuerpo de este luchador tienen Doloroso.", null, "card_bl26");
        insertCardInternal(db, "Deadly Aim", "Upgrade", null, 27, (int)blazingAssaultDeckId, 1,
                "Las armas de este luchador tienen Atrapador.", null, "card_bl27");
        insertCardInternal(db, "Sharpened Points", "Upgrade", null, 28, (int)blazingAssaultDeckId, 1,
                "Las armas de este luchador tienen Perforante.", null, "card_bl28");
        insertCardInternal(db, "Duellist", "Upgrade", null, 29, (int)blazingAssaultDeckId, 1,
                "Jequeo: Inmediatamente después de que este luchador haya atacado, puedes empujar a este luchador 1 hexágono.", null, "card_bl29");
        insertCardInternal(db, "Tough", "Upgrade", null, 30, (int)blazingAssaultDeckId, 2,
                "No se pueden infligir más de 3 de daño a este luchador en el mismo turno.", null, "card_bl30");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 31, (int)blazingAssaultDeckId, 2,
                "Este luchador tiene +1 Salud.", null, "card_bl31");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 32, (int)blazingAssaultDeckId, 2,
                "Las armas cuerpo a cuerpo de este luchador tienen +1 dado de Ataque.", null, "card_bl32");

        // --- Inserción de Emberstone Sentinels Rivals Deck ---
        long emberstoneSentinelsDeckId = insertRivalDeckInternal(db, "Emberstone Sentinels Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);

        // --- Objetivos (Objectives) de Emberstone Sentinels ---
        insertCardInternal(db, "Sally Forth", "Objective", "Surge", 1, (int)emberstoneSentinelsDeckId, 1,
                "Anota esta carta inmediatamente después del paso de Acción de tu oponente si un luchador aliado con alguna ficha de Carga sostiene una ficha de tesoro en territorio enemigo.", null, "card_es1");
        insertCardInternal(db, "Stand Firm", "Objective", "Surge", 2, (int)emberstoneSentinelsDeckId, 1,
                "Anota esta carta inmediatamente después del paso de Acción de tu oponente si un luchador aliado con alguna ficha de Tambaleo sostiene una ficha de tesoro en territorio enemigo.", null, "card_es2");
        insertCardInternal(db, "Step by Step", "Objective", "Surge", 3, (int)emberstoneSentinelsDeckId, 1,
                "Anota esta carta inmediatamente después del paso de Acción de tu oponente si un luchador aliado con alguna ficha de Movimiento sostiene una ficha de tesoro en territorio enemigo. Si eres el desventajado, ese luchador aliado puede tener fichas de Carga en su lugar.", null, "card_es3");
        insertCardInternal(db, "Unassailable", "Objective", "Surge", 4, (int)emberstoneSentinelsDeckId, 1,
                "Anota esta carta inmediatamente después del Ataque de un luchador enemigo si un luchador aliado que sostiene una ficha de tesoro fue el objetivo de ese Ataque.", null, "card_es4");
        insertCardInternal(db, "Aggressive Defender", "Objective", "Surge", 5, (int)emberstoneSentinelsDeckId, 1,
                "Anota esta carta inmediatamente después del Ataque de un luchador aliado si el atacante sostiene una ficha de tesoro.", null, "card_es5");
        insertCardInternal(db, "Careful Advance", "Objective", "Surge", 6, (int)emberstoneSentinelsDeckId, 1,
                "Anota esta carta inmediatamente después de que un luchador aliado se Mueva si 2 o más luchadores aliados con fichas de Movimiento están en territorio enemigo.", null, "card_es6");
        insertCardInternal(db, "Hold Treasure Token 1 or 2", "Objective", "End Phase", 7, (int)emberstoneSentinelsDeckId, 1,
                "Anota esta carta en una fase final si un luchador aliado sostiene la ficha de tesoro 1 o 2.", null, "card_es7");
        insertCardInternal(db, "Hold Treasure Token 3 or 4", "Objective", "End Phase", 8, (int)emberstoneSentinelsDeckId, 1,
                "Anota esta carta en una fase final si un luchador aliado sostiene la ficha de tesoro 3 o 4.", null, "card_es8");
        insertCardInternal(db, "Hold Treasure Token 5", "Objective", "End Phase", 9, (int)emberstoneSentinelsDeckId, 1,
                "Anota esta carta en una fase final si un luchador aliado sostiene la ficha de tesoro 5.", null, "card_es9");
        insertCardInternal(db, "Slow Advance", "Objective", "End Phase", 10, (int)emberstoneSentinelsDeckId, 2,
                "Anota esta carta en una fase final si tu banda sostiene fichas de tesoro tanto en territorio neutral como en territorio enemigo.", null, "card_es10");
        insertCardInternal(db, "Iron Grasp", "Objective", "End Phase", 11, (int)emberstoneSentinelsDeckId, 2,
                "Anota esta carta en una fase final si tu banda sostiene todas las fichas de tesoro en territorio aliado y/o en territorio enemigo.", null, "card_es11");
        insertCardInternal(db, "Supremacy", "Objective", "End Phase", 12, (int)emberstoneSentinelsDeckId, 3,
                "Anota esta carta en una fase final si 2 o más luchadores aliados con una característica total de Recompensa de 3 o más sostienen fichas de tesoro.", null, "card_es12");

        // --- Ardides (Gambits) de Emberstone Sentinels ---
        insertCardInternal(db, "Switch Things Up", "Gambit", "Ploy", 13, (int)emberstoneSentinelsDeckId, null,
                "Elige 2 fichas de tesoro. Intercambia las posiciones de esas fichas de tesoro.", null, "card_es13");
        insertCardInternal(db, "Sidestep", "Gambit", "Ploy", 14, (int)emberstoneSentinelsDeckId, null,
                "Elige un luchador aliado. Empuja a ese luchador 1 hexágono.", null, "card_es14");
        insertCardInternal(db, "The Extra Mile", "Gambit", "Ploy", 15, (int)emberstoneSentinelsDeckId, null,
                "Juega esta carta inmediatamente después de que un luchador aliado se Mueva. Empuja a ese luchador 1 hexágono. Ese empuje debe terminar sobre una ficha de elemento.", null, "card_es15");
        insertCardInternal(db, "Settle In", "Gambit", "Ploy", 16, (int)emberstoneSentinelsDeckId, null,
                "Elige un luchador aliado sobre una ficha de elemento. Dale a ese luchador una ficha de Guardia.", null, "card_es16");
        insertCardInternal(db, "Healing Potion", "Gambit", "Ploy", 17, (int)emberstoneSentinelsDeckId, null,
                "Elige un luchador aliado. Sana a ese luchador. Si eres el desventajado, puedes tirar un dado de Salvación. Con Escudo o éxito Crítico, sana a ese luchador otra vez.", null, "card_es17");
        insertCardInternal(db, "Hidden Paths", "Gambit", "Ploy", 18, (int)emberstoneSentinelsDeckId, null,
                "Elige un luchador aliado en un hexágono de borde. Retira a ese luchador del campo de batalla y colócalo en un hexágono de borde vacío diferente. Luego dale a ese luchador una ficha de Movimiento, a menos que seas el desventajado.", null, "card_es18");
        insertCardInternal(db, "Confusion", "Gambit", "Ploy", 19, (int)emberstoneSentinelsDeckId, null,
                "Elige 2 luchadores adyacentes. Retira a esos luchadores del campo de batalla y colócalos en el hexágono del que fue retirado el otro.", null, "card_es19");
        insertCardInternal(db, "Hold the Line!", "Gambit", "Ploy", 20, (int)emberstoneSentinelsDeckId, null,
                "Los luchadores no pueden ser empujados hacia atrás. Este efecto persiste hasta el final del siguiente paso de Acción.", null, "card_es20");
        insertCardInternal(db, "Shoulder Throw", "Gambit", "Ploy", 21, (int)emberstoneSentinelsDeckId, null,
                "Juega esta carta inmediatamente después del Ataque exitoso de un luchador aliado si el objetivo está adyacente. Retira al objetivo del campo de batalla y colócalo en un hexágono vacío diferente adyacente al atacante.", null, "card_es21");
        insertCardInternal(db, "By the Numbers", "Gambit", "Ploy", 22, (int)emberstoneSentinelsDeckId, null,
                "Roba un número de cartas de Poder igual al número de fichas de tesoro que sostiene tu banda.", null, "card_es22");

        insertCardInternal(db, "Stubborn to the End", "Upgrade", null, 23, (int)emberstoneSentinelsDeckId, 1,
                "Si este luchador es el objetivo de un Ataque, el atacante no puede usar Exceso.", null, "card_es23");
        insertCardInternal(db, "Inviolate", "Upgrade", null, 24, (int)emberstoneSentinelsDeckId, 1,
                "Este luchador no puede ser Flanqueado ni Rodeado mientras sostenga una ficha de tesoro.", null, "card_es24");
        insertCardInternal(db, "Great Speed", "Upgrade", null, 25, (int)emberstoneSentinelsDeckId, 0,
                "Este luchador tiene +1 Movimiento.", null, "card_es25");
        insertCardInternal(db, "Sharp Reflexes", "Upgrade", null, 26, (int)emberstoneSentinelsDeckId, 2,
                "Este luchador tiene +1 Salvación, hasta un máximo de 2.", null, "card_es26");
        insertCardInternal(db, "Brute Momentum", "Upgrade", null, 27, (int)emberstoneSentinelsDeckId, 1,
                "Este luchador no puede ser empujado hacia atrás mientras tenga fichas de Carga.", null, "card_es27");
        insertCardInternal(db, "Agile", "Upgrade", null, 28, (int)emberstoneSentinelsDeckId, 2,
                "Ágil: Después de hacer una tirada de Salvación para este luchador, puedes volver a tirar inmediatamente 1 dado de Salvación de esa tirada.", null, "card_es28");
        insertCardInternal(db, "Duellist", "Upgrade", null, 29, (int)emberstoneSentinelsDeckId, 1,
                "Jequeo: Inmediatamente después de que este luchador haya atacado, puedes empujar a este luchador 1 hexágono.", null, "card_es29");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 30, (int)emberstoneSentinelsDeckId, 2,
                "Este luchador tiene +1 Salud.", null, "card_es30");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 31, (int)emberstoneSentinelsDeckId, 2,
                "Las armas cuerpo a cuerpo de este luchador tienen +1 dado de Ataque.", null, "card_es31");
        insertCardInternal(db, "Great Strength", "Upgrade", null, 32, (int)emberstoneSentinelsDeckId, 2,
                "Las armas cuerpo a cuerpo de este luchador tienen Doloroso.", null, "card_es32");

        long pillageAndPlunderDeckId = insertRivalDeckInternal(db, "Pillage and Plunder Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);
        insertCardInternal(db, "Broken Prospects", "Objective", "End Phase", 1, (int)pillageAndPlunderDeckId, 2,
                "Anota esta carta en una fase final si tu banda ha excavado 3 o más fichas de tesoro diferentes en esta ronda de batalla, o si una ficha de tesoro sostenida por un luchador enemigo al inicio de la ronda de batalla ha sido excavada por tu banda en esta ronda de batalla.", null, "card_pl1");
        insertCardInternal(db, "Against the Odds", "Objective", "End Phase", 2, (int)pillageAndPlunderDeckId, 1,
                "Anota esta carta en una fase final si tu banda ha excavado una ficha de tesoro con número impar en esta ronda de batalla.", null, "card_pl2");
        insertCardInternal(db, "Lost in the Depths", "Objective", "End Phase", 3, (int)pillageAndPlunderDeckId, 1,
                "Anota esta carta en una fase final si ningún luchador aliado está adyacente y hay al menos un luchador aliado que no esté eliminado.", null, "card_pl3");
        insertCardInternal(db, "Desolate Homeland", "Objective", "End Phase", 4, (int)pillageAndPlunderDeckId, 1,
                "Anota esta carta en una fase final si hay 1 o menos fichas de tesoro en territorio aliado.", null, "card_pl4");
        insertCardInternal(db, "Torn Landscape", "Objective", "End Phase", 5, (int)pillageAndPlunderDeckId, 2,
                "Anota esta carta en una fase final si hay 2 o menos fichas de tesoro en el campo de batalla.", null, "card_pl5");
        insertCardInternal(db, "Strip the Realm", "Objective", "End Phase", 6, (int)pillageAndPlunderDeckId, 3,
                "Anota esta carta en una fase final si no hay fichas de tesoro en el campo de batalla o si ningún luchador enemigo sostiene fichas de tesoro.", null, "card_pl6");
        insertCardInternal(db, "Aggressive Claimant", "Objective", "Surge", 7, (int)pillageAndPlunderDeckId, 1,
                "Anota esta carta inmediatamente después de un Ataque exitoso de un luchador aliado si el objetivo estaba en territorio neutral, o si el objetivo sostenía una ficha de tesoro cuando lo elegiste como objetivo de ese Ataque y ya no sostiene esa ficha de tesoro.", null, "card_pl7");
        insertCardInternal(db, "Claim the Prize", "Objective", "Surge", 8, (int)pillageAndPlunderDeckId, 1,
                "Anota esta carta inmediatamente después de que un luchador aliado excave en territorio enemigo. Si eres el desventajado, esa excavación puede ser en territorio aliado en su lugar.", null, "card_pl8");
        insertCardInternal(db, "Delving for Wealth", "Objective", "Surge", 9, (int)pillageAndPlunderDeckId, 1,
                "Anota esta carta inmediatamente después de que tu banda excave por tercera vez o más en esta fase de combate.", null, "card_pl9");
        insertCardInternal(db, "Share the Load", "Objective", "Surge", 10, (int)pillageAndPlunderDeckId, 1,
                "Anota esta carta inmediatamente después de que un luchador aliado se Mueva, si ese luchador y cualquier otro luchador aliado están cada uno sobre una ficha de elemento.", null, "card_pl10");
        insertCardInternal(db, "Hostile Takeover", "Objective", "Surge", 11, (int)pillageAndPlunderDeckId, 1,
                "Anota esta carta inmediatamente después del segundo Ataque, o uno posterior, realizado por tu banda que no forme parte de una Carga.", null, "card_pl11");
        insertCardInternal(db, "Careful Survey", "Objective", "Surge", 12, (int)pillageAndPlunderDeckId, 1,
                "Anota esta carta inmediatamente después de un paso de Acción si hay un luchador aliado en cada territorio.", null, "card_pl12");

        insertCardInternal(db, "Sidestep", "Gambit", "Ploy", 13, (int)pillageAndPlunderDeckId, null,
                "Elige un luchador aliado. Empuja a ese luchador 1 hexágono.", null, "card_pl13");
        insertCardInternal(db, "Prideful Duellist", "Gambit", "Ploy", 14, (int)pillageAndPlunderDeckId, null,
                "Juega esta carta inmediatamente después del Ataque de un luchador aliado si el atacante está en territorio enemigo. Sana al atacante.", null, "card_pl14");
        insertCardInternal(db, "Commanding Stride", "Gambit", "Ploy", 15, (int)pillageAndPlunderDeckId, null,
                "Empuja a tu líder hasta 3 hexágonos. Ese empuje debe terminar en un hexágono de salida.", null, "card_pl15");
        insertCardInternal(db, "Crumbling Mine", "Gambit", "Ploy", 16, (int)pillageAndPlunderDeckId, null,
                "Elige una ficha de tesoro que no esté sostenida. Voltea esa ficha de tesoro.", null, "card_pl16");
        insertCardInternal(db, "Explosive Charges", "Gambit", "Ploy", 17, (int)pillageAndPlunderDeckId, null,
                "Dominio: Los luchadores aliados tienen +1 Movimiento mientras usan habilidades de Carga. Este efecto persiste hasta el final de la ronda de batalla o hasta que se juegue otra carta de Dominio.", null, "card_pl17");
        insertCardInternal(db, "Wary Delver", "Gambit", "Ploy", 18, (int)pillageAndPlunderDeckId, null,
                "Elige un luchador aliado con alguna ficha de Carga. Dale a ese luchador una ficha de Guardia.", null, "card_pl18");
        insertCardInternal(db, "Brash Scout", "Gambit", "Ploy", 19, (int)pillageAndPlunderDeckId, null,
                "Juega esta carta inmediatamente después de hacer una tirada de Ataque para un luchador en territorio enemigo. Vuelve a tirar 1 dado de esa tirada de Ataque. Si eres el desventajado, puedes volver a tirar cada dado de esa tirada de Ataque en su lugar.", null, "card_pl19");
        insertCardInternal(db, "Sudden Blast", "Gambit", "Ploy", 20, (int)pillageAndPlunderDeckId, null,
                "Elige un luchador enemigo adyacente a un luchador aliado. Dale a ese luchador enemigo una ficha de Tambaleo.", null, "card_pl20");
        insertCardInternal(db, "Tunnelling Terror", "Gambit", "Ploy", 21, (int)pillageAndPlunderDeckId, null,
                "Elige un luchador aliado sin fichas de Movimiento ni Carga. Retira a ese luchador del campo de batalla y colócalo en un hexágono de Tambaleo vacío. Luego dale a ese luchador una ficha de Carga. Si eres el desventajado, puedes darle en su lugar una ficha de Movimiento.", null, "card_pl21");
        insertCardInternal(db, "Trapped Cache", "Gambit", "Ploy", 22, (int)pillageAndPlunderDeckId, null,
                "Elige un luchador enemigo sin heridas a 1 hexágono o menos de una ficha de tesoro. Inflige 1 de daño a ese luchador.", null, "card_pl22");

        insertCardInternal(db, "Great Speed", "Upgrade", null, 23, (int)pillageAndPlunderDeckId, 0,
                "Este luchador tiene +1 Movimiento.", null, "card_pl23");
        insertCardInternal(db, "Swift Step", "Upgrade", null, 24, (int)pillageAndPlunderDeckId, 1,
                "Rápido: Inmediatamente después de que este luchador haya realizado una Carga, puedes empujar a este luchador 1 hexágono.", null, "card_pl24");
        insertCardInternal(db, "Burrowing Strike", "Upgrade", null, 25, (int)pillageAndPlunderDeckId, 1,
                "Acción de Ataque cuerpo a cuerpo. Alcance 2, 2 Dados, 2 Daño. Esta arma tiene +1 dado de Ataque mientras este luchador tenga fichas de Tambaleo o esté sobre una ficha de elemento.", null, "card_pl25");
        insertCardInternal(db, "Tough Enough", "Upgrade", null, 26, (int)pillageAndPlunderDeckId, 1,
                "Mientras este luchador esté en territorio enemigo, las tiradas de Salvación para este luchador no pueden verse afectadas por Perforante ni Atrapador.", null, "card_pl26");
        insertCardInternal(db, "Canny Sapper", "Upgrade", null, 27, (int)pillageAndPlunderDeckId, 0,
                "Sigiloso: Inmediatamente después de jugar un Ardid en un paso de Poder, puedes retirar a este luchador del campo de batalla. Coloca a este luchador en un hexágono de Tambaleo vacío o en un hexágono de salida vacío en territorio aliado y luego descarta esta carta.", null, "card_pl27");
        insertCardInternal(db, "Impossibly Quick", "Upgrade", null, 28, (int)pillageAndPlunderDeckId, 1,
                "Este luchador tiene +1 Salvación. Descarta inmediatamente esta Mejora después del fallo de un Ataque de un luchador enemigo si este luchador fue el objetivo.", null, "card_pl28");
        insertCardInternal(db, "Linebreaker", "Upgrade", null, 29, (int)pillageAndPlunderDeckId, 1,
                "Las armas de este luchador tienen Brutal.", null, "card_pl29");
        insertCardInternal(db, "Excavating Blast", "Upgrade", null, 30, (int)pillageAndPlunderDeckId, 1,
                "Acción de Ataque a distancia. Alcance 3, 2 Dados, 1 Daño. Esta arma tiene Tambaleo mientras este luchador esté en territorio enemigo.", null, "card_pl30");
        insertCardInternal(db, "Gloryseeker", "Upgrade", null, 31, (int)pillageAndPlunderDeckId, 1,
                "Las armas cuerpo a cuerpo de este luchador tienen Doloroso si la característica de Salud del objetivo es 4 o más.", null, "card_pl31");
        insertCardInternal(db, "Frenzy of Greed", "Upgrade", null, 32, (int)pillageAndPlunderDeckId, 2,
                "Mientras este luchador esté sobre una ficha de tesoro en territorio enemigo o en un hexágono de Tambaleo, las tiradas de Salvación para este luchador no se ven afectadas por Perforante ni Atrapador y este luchador no puede recibir fichas de Tambaleo.", null, "card_pl32");


        long zarbagBandId = insertBandInternal(db, "Zarbag's Gitz",
                "Inspirar: Ccuando ganes el sexto punto de gloria o uno posterior, inspira a un luchador aliado. \n\n" +
                "Pastor de squigs: Después de desplegar un Drizgit amigo, debes colocar inmediatamente cada Squig amigo en un hexágono vacío adyacente a ellos que no sea un hexágono inicial y que no contenga una ficha de característica. \n\n" +
                "Girando: Snirk no puede recibir fichas de Guardia. Cada vez que un luchador sea colocado, empujado o entre en un hexágono adyacente a un Snirk amigo, inflige 1 punto de daño a ese luchador. \n\n" +
                "Voleybol: Si el arma de un luchador aliado tiene la habilidad de arma Volley, tienes acceso a la siguiente habilidad de arma: Volley: Inmediatamente después del ataque, si ha tenido éxito, puedes elegir a otro luchador aliado cuyo arma tenga Volley. Ese luchador ataca con esa arma. Solo puedes usar esta habilidad una vez por turno. \n\n" +
                "Gitz escurridizos: Usa esta habilidad inmediatamente después de elegir a un luchador amigo para que se mueva. Elige a un grot amigo que no tenga fichas de Movimiento o Carga adyacentes. Después de que el primer luchador se mueva, ese grot amigo puede moverse. Solo puedes usar esta habilidad una vez por turno. \n\n" +
                "Explosión fúngica (Una vez por partida): Úsala inmediatamente después de que un grot aliado sea asesinado por un atacante adyacente a él. Dale al atacante una ficha de tambaleo \n\n" +
                "Ataque en grupo (Una vez por partida): Úsalo en una fase de Poder. En el siguiente turno, las armas cuerpo a cuerpo de los luchadores amigos tienen +1 dado de ataque por cada Grot amigo adyacente al objetivo. \n\n" +
                "¡Haz ruido! (Una vez por partida): Úsalo en un paso de poder. Empuja a cada squig aliado hasta 2 hexágonos. Durante el resto de la ronda de combate, esos squigs no pueden controlar fichas de tesoro ni indagar.",
                "Destruccion", "zarbags_gitz_0");

        insertFighterInternal(db, (int)zarbagBandId, "Zarbag", "zarbags_gitz_1");
        insertFighterInternal(db, (int)zarbagBandId, "Snirk Sourtongue", "zarbags_gitz_2");
        insertFighterInternal(db, (int)zarbagBandId, "Drizgit", "zarbags_gitz_3");
        insertFighterInternal(db, (int)zarbagBandId, "Prog", "zarbags_gitz_4");
        insertFighterInternal(db, (int)zarbagBandId, "Stikkit", "zarbags_gitz_5");
        insertFighterInternal(db, (int)zarbagBandId, "Dibbz", "zarbags_gitz_6");
        insertFighterInternal(db, (int)zarbagBandId, "Redkap", "zarbags_gitz_7");
        insertFighterInternal(db, (int)zarbagBandId, "Bonekrakka", "zarbags_gitz_8");
        insertFighterInternal(db, (int)zarbagBandId, "Gobbaluk", "zarbags_gitz_9");


        long blackpowderBandId = insertBandInternal(db, "Blackpowder's Buccaneers",
                "Inspirar: Después de retirar tu tercera ficha de botín o las siguientes, inspira a cada luchador amigo. \n\n" +
                "Saquear y robar: Empiezas el juego con 1 ficha de botín. \n" +
                "Si el arma de un luchador aliado tiene la marca de arma espadachin, tendrás acceso a la siguiente habilidad de arma:\n" + //
                                        "\n" + //
                                        "Espadachin: Si el ataque tiene éxito, obtienes 1 ficha de botín. Si eres el rezagado o el objetivo estaba equipado con alguna mejora, obtienes 2 fichas de botín en lugar de 1.\n\n" +
                "Puedes usar 1 de las siguientes habilidades inmediatamente después de elegir un arma a distancia como parte del ataque de tu líder. Cuando elijas la habilidad, puedes eliminar hasta 2 de tus fichas de botín. Solo puedes usar cada habilidad una vez por partida. \n\n" +
                "Metralla del botín (Una vez por partida): Después de ese ataque, puedes elegir hasta otros 2 luchadores enemigos dentro de 2 hexágonos del objetivo. A continuación, tira un número de dados de ataque igual al número de fichas de botín que hayas eliminado. Si la tirada contiene algún martillo o crítico, inflige 1 punto de daño a cada uno de esos luchadores y dale al objetivo y a cada uno de esos luchadores una ficha de tambaleo.\n\n"+
                "Explosión de botín (Una vez por partida): Esa arma tiene +1 de daño y Romper para ese ataque. Si el objetivo es presionado, dale una ficha de tambaleo.\n\n"+
                "Mortero de botín (Una vez por partida): Esa arma tiene +1 de daño y +X de alcance para ese ataque, donde X es el número de fichas de botín que hayas eliminado.",
                "Destruccion", "blackpowders_buccaneers_0");

        insertFighterInternal(db, (int)blackpowderBandId, "Gorlok Blackpowder", "blackpowders_buccaneers_1");
        insertFighterInternal(db, (int)blackpowderBandId, "Kagey", "blackpowders_buccaneers_2");
        insertFighterInternal(db, (int)blackpowderBandId, "Peggz", "blackpowders_buccaneers_3");
        insertFighterInternal(db, (int)blackpowderBandId, "Mange", "blackpowders_buccaneers_4");
        insertFighterInternal(db, (int)blackpowderBandId, "Shreek", "blackpowders_buccaneers_5");
        long bloodBullBandId = insertBandInternal(db, "Blood of the Bull",
                "Inspirar: Cuando uses un dado de forja demoniaca en la tirada de ataque o de salvación de un luchador aliado, después de ese ataque, inspira a ese luchador. Después de un ataque realizado por un Chaos Duardin aliado, si el objetivo estaba adyacente a un Grisk aliado, inspira a ese Grisk. \n\n" +
                "Empoderados por el rencor: Empiezas la partida con 1 dado de forja demoníaca. \n\n" +
                "En tu fase de poder, si un luchador aliado sin fichas de Tambaleo indaga una ficha de tesoro, obtienes 1 dado de forja demoníaca. Todos los dados de forja demoníaca se pierden al final de cada ronda de combate. \n\n" +
                "Mejorar arsenal: Inmediatamente después de elegir un arma como parte de un ataque realizado por un Chaos Duardin aliado, puedes usar 1 dado de forja demoníaca en la tirada de ataque. Si lo haces, esa arma tiene +1 dado de ataque para ese ataque. \n\n" +
                "Reforzar armería: Inmediatamente después de que un Chaos Duardin aliado sea elegido como objetivo de un ataque, puedes usar 1 dado de foja demoniaca en la tirada de salvación. Si lo haces, ese luchador tiene +1 a la salvación para ese ataque. \n\n" +
                "Conflagración infernal: Las armas a distancia de un Tokkor amigo (excluyendo las mejoras) tienen Doloroso si el objetivo está adyacente.\n\n"+
                "Astucia de Hobgrot: Las tiradas de espadas y martillos son éxitos en una tirada de ataque para un Chaos Duardin amigo mientras el objetivo esté adyacente a un Grisk amigo.",
                "Caos", "blood_of_the_bull_0");

        insertFighterInternal(db, (int)bloodBullBandId, "Zuldrakka the Hateful", "blood_of_the_bull_1");
        insertFighterInternal(db, (int)bloodBullBandId, "Morudo", "blood_of_the_bull_2");
        insertFighterInternal(db, (int)bloodBullBandId, "Imindrin", "blood_of_the_bull_3");
        insertFighterInternal(db, (int)bloodBullBandId, "Tokkor the Immolator", "blood_of_the_bull_4");

        // Grisk Back-stabba
        insertFighterInternal(db, (int)bloodBullBandId, "Grisk Back-stabba", "blood_of_the_bull_5");

        // --- Inserción de Brethren of the Bolt ---
        long brethrenBandId = insertBandInternal(db, "Brethren of the Bolt",
                "Inspirar: Cada luchador aliado comienza la partida Inspirado.\n" + //
                                        "Inspirar: Inmediatamente después de una fase de Acción en la que un luchador aliado haya sido un luchador conductor, inspira a ese luchador.\n"+
                                        "Desinspirar: Inmediatamente después de que un luchador aliado haya realizado un ataque con éxito, desinspira a ese luchador aliado.\n\n"+
                                        "Condensadores sagrados: Resta 1 a la distancia entre un atacante aliado y su objetivo por cada luchador aliado que se encuentre entre el atacante y el objetivo. Esos luchadores aliados son conductores.\n\n"+
                                        "Himno fulminante: Elige una de las siguientes habilidades al comienzo de la fase de combate. Mientras haya dos o más luchadores aliados, cada luchador puede usar esa habilidad.\n" + //
                                        "\n" + //
                                                                                        "- Aura cargada: los luchadores aliados no pueden ser flanqueados.\n" + //
                                                                                        "- Abandono temerario: cada vez que un luchador aliado vaya a recibir una ficha de carga, puedes infligir 1 punto de daño a ese luchador en lugar de darle una ficha de carga.\n" + //
                                                                                        "- Dinamos fieles: las armas de los luchadores aliados tienen tambaleo.\n\n"+
                                        "Explosión crepitante (Una vez por partida): Usa esto en un paso de Poder. Elige un luchador aliado. Ese luchador se mueve.\n\n"+
                                        "Carga celestial (Una vez por partida): Usa esto inmediatamente después de tu paso de Acción. Elige un luchador aliado. Desinspíralo y luego inflige 1 punto de daño a un luchador enemigo adyacente.\n\n"+
                                        "Resurrección resplandeciente (Una vez por partida): Úsalo inmediatamente después de tu fase de Acción. Elige un luchador aliado que fuera conductor durante esa fase. Cura a ese luchador.",
                "Orden", "brethren_of_the_bolt_0");

        // Pater Frewin
        insertFighterInternal(db, (int)brethrenBandId, "Pater Frewin", "brethren_of_the_bolt_1");

        // Zebede
        insertFighterInternal(db, (int)brethrenBandId, "Zebede", "brethren_of_the_bolt_2");

        // Ludo & Kraz
        insertFighterInternal(db, (int)brethrenBandId, "Ludo & Kraz", "brethren_of_the_bolt_3");

        // Hollis
        insertFighterInternal(db, (int)brethrenBandId, "Hollis", "brethren_of_the_bolt_4");

        // Zacarias
        insertFighterInternal(db, (int)brethrenBandId, "Zacarias", "brethren_of_the_bolt_5");

        // --- Inserción de Cyreni's Razors  ---
        long cyreniBandId = insertBandInternal(db, "Cyreni's Razors",
                "Inspirar: Inspira a un número de luchadores amigos igual al de la ronda de batalla al comienzo de cada fase de combate.\n"+
                "Desinspirar: Al comienzo de la ronda, desinspira a cada luchador amigo.\n\n"+
                "Marea que arrolla (Habilidad Basica): Puedes usar esta habilidad si tu líder está en el campo de batalla y no tiene fichas de carga. Traza una línea recta desde el hexágono de tu líder que pase por el centro de un hexágono adyacente y llegue hasta el borde del campo de batalla. Inflige 1 punto de daño al primer luchador enemigo que cruce la línea y dale una ficha de Tambaleo. La línea termina si toca un hexágono bloqueado.\n\n"+
                "Réplica mortal: Usa esto inmediatamente después de que un luchador enemigo falle un ataque dirigido a un Thrall aliado. Las armas de ese Thrall tienen Romper y Apresar durante el siguiente turno mientras ese luchador enemigo sea el objetivo de un ataque.\n\n"+
                "Tinta fantasmal: Úsalo inmediatamente después de que un luchador enemigo falle un ataque dirigido a un Cephanyr aliado. Empuja a Cephanyr un número de hexágonos igual al de la ronda de combate.\n\n"+
                "Cosecha de almas (Una vez por partida): Usa esto en una fase de poder. Elige un luchador enemigo inspirado. A continuación, tu oponente debe elegir una de las siguientes opciones: desinspirar a ese luchador o robar cartas de poder iguales a la ronda de batalla.",
                "Orden", "cyrenis_razors_0");

        // Cyreni
        insertFighterInternal(db, (int)cyreniBandId, "Cyreni", "cyrenis_razors_1");

        // Cephanyx
        insertFighterInternal(db, (int)cyreniBandId, "Cephanyx", "cyrenis_razors_2");

        // Riptide
        insertFighterInternal(db, (int)cyreniBandId, "Riptide", "cyrenis_razors_3");

        // Morla
        insertFighterInternal(db, (int)cyreniBandId, "Morla", "cyrenis_razors_4");

        // --- Inserción de Da Kunnin' Krew ---
        long kunninBandId = insertBandInternal(db, "Da Kunnin' Krew",
                "Inspirar: Tras un ataque exitoso de un luchador aliado, si el objetivo ha sido asesinado, flanqueado o rodeado, inspira al atacante y a cada luchador aliado adyacente al objetivo. \n\n" +
                "Dolor en la rodilla: Si un Mannok o Torka aliado es el atacante, los secuaces aliados cuentan como 2 luchadores a la hora de determinar si un objetivo está flanqueado o rodeado.\n\n" + //
                "Un plan astuto (Una vez por partida): Usa esto inmediatamente después de un paso de Acción si tu líder está en el campo de batalla. Empuja a cada esbirro amigo hasta un número de hexágonos igual al número de ronda de batalla.\n\n"+
                "Trucos sucios (Una vez por partida): Usa esto inmediatamente antes de que tú o tu oponente hagáis una tirada de ataque o de salvación. Tu oponente debe declarar «Mejora» o «Ardid». Coge 3 cartas de poder de tu mano y barájalas boca abajo. Tu oponente debe elegir 1 de esas cartas. A continuación, revela cada una de esas cartas. Si la carta que ha elegido tu oponente coincide con lo que ha declarado, roba 1 carta de poder. De lo contrario, no se realiza la tirada de ataque o de salvación y debes establecer el resultado en cada dado.\n\n"+
                "Acechadores tenebrosos (Una vez por partida): Úsalo inmediatamente después de que un luchador aliado sea elegido como objetivo de un ataque. Las tiradas de Flanqueado y Rodeado son éxitos en las tiradas de Salvacion realizadas por ese luchador para ese ataque.",
                "Destruccion", "da_kunnin_krew_0");

        // Mannok Da Kunnin
        insertFighterInternal(db, (int)kunninBandId, "Mannok Da Kunnin", "da_kunnin_krew_1");

        // Torg Bak-stabber
        insertFighterInternal(db, (int)kunninBandId, "Torg Bak-stabber", "da_kunnin_krew_2");

        // Shank
        insertFighterInternal(db, (int)kunninBandId, "Shank", "da_kunnin_krew_3");

        // Grakka
        insertFighterInternal(db, (int)kunninBandId, "Grakka", "da_kunnin_krew_4");

        // Krookish
        insertFighterInternal(db, (int)kunninBandId, "Krookish", "da_kunnin_krew_5");

        // --- Inserción de Daggok's Stab-ladz ---
        long daggokBandId = insertBandInternal(db, "Daggok's Stab-ladz",
                "Inspirar: Inmediatamente después de infligir daño a un luchador enemigo con la habilidad Puñalada trapera, inspira al luchador aliado.\n\n"+
                "Puñalada trapera: Un luchador aliado sin fichas de carga puede usar esta habilidad inmediatamente después de tu fase de acción. Elige un luchador enemigo que no sea vulnerable y que esté adyacente a ese luchador aliado y tira un dado de ataque. Si obtienes un martillo o un crítico, inflige 1 punto de daño a ese luchador enemigo. El mismo luchador no puede usar esta habilidad más de una vez por turno.\n\n"+
                "¡Dos contra uno, idiota! (Una vez por partida): Úsalo inmediatamente después de tu fase de Acción. Puedes empujar a un luchador amigo 1 hexágono.\n\n"+
                "Ladrón astuto (Una vez por parida): Úsala en un paso de Poder. Roba un número de cartas de Poder igual al número de luchadores enemigos dañados.\n\n"+
                "Venenos repugnantes (Una vez por partida): Úsala inmediatamente después de un paso de Acción. Tira un dado por cada luchador enemigo dañado. Si obtienes un resultado crítico, inflige 1 punto de daño a ese luchador.\n\n"+
                "Tipejos arteros: En lugar de jugar un ardid en la fase de Poder de tu oponente, puedes descartar ardides para usar las habilidades que se indican a continuación. El número de Ardides que descartes determina qué habilidades se aplican en el siguiente turno. Los efectos son acumulativos y el efecto de cada habilidad dura hasta el final de tu siguiente turno.\n" + //
                "\n" + //
                                        "- Descartar 1: los luchadores amigos tienen +1 a Movimiento.\n" + //
                                        "- Descartar 2: los luchadores enemigos están Flanqueados.\n" + //
                                        "- Descartar 3: las armas de los luchadores amigos tienen +1 a los dados de Ataque.",
                "Destruccion", "daggoks_stab_ladz_0");

        // Daggok Finksteala
        insertFighterInternal(db, (int)daggokBandId, "Daggok Finksteala", "daggoks_stab_ladz_1");

        // Hurrk da Howla
        insertFighterInternal(db, (int)daggokBandId, "Hurrk da Howla", "daggoks_stab_ladz_2");

        // Grakk da Hook
        insertFighterInternal(db, (int)daggokBandId, "Grakk da Hook", "daggoks_stab_ladz_3");

        // Jagz da Bleeda
        insertFighterInternal(db, (int)daggokBandId, "Jagz da Bleeda", "daggoks_stab_ladz_4");

        // --- Inserción de Borgit's Beastgrabbaz ---
        long borgitBandId = insertBandInternal(db, "Borgit's Beastgrabbaz",
                "Inspirar: Después de que un luchador aliado indague, inspira a ese luchador.\n\n"+
                "Cazador de trofeos: Al comienzo de tu primera fase de acción en la primera ronda de combate, elige a un luchador enemigo con una característica de salud de 3 o más para que sea el trofeo. Los ataques cuerpo a cuerpo que tengan como objetivo al trofeo tienen +1 dado de ataque.\n\n"+
                "Cebo: Inmediatamente después de que un esbirro aliado sea asesinado, empuja a tu líder un número de hexágonos igual a la característica de Salud del esbirro asesinado. Ese empujón debe terminar adyacente a un luchador enemigo.\n\n"+
                "Gittish Taktikz (Una vez por partida): Usa esto inmediatamente después de un ataque exitoso de un Uglug aliado o un Borgit aliado. Hasta el final de la ronda de batalla, el objetivo de ese ataque es el mugginz. Las armas cuerpo a cuerpo de los secuaces aliados tienen critico Doloroso cuando apuntan al mugginz.\n\n"+
                "Presencia tranquilizadora (Una vez por partida): Úsala inmediatamente después de tu fase de Acción si un Uglug aliado ha usado alguna habilidad básica en esa fase. Puedes empujar a cada esbirro aliado hasta 3 hexágonos. Cada luchador empujado de esta manera debe terminar ese empujón adyacente a un Uglug aliado.\n\n"+
                "Apuñálalos bien (Una vez por partida): Usa esta habilidad inmediatamente después de tu fase de Acción si un esbirro aliado ha usado alguna habilidad básica en esa fase. Hasta el final de la siguiente fase de Acción, la primera vez que un luchador enemigo sea empujado, colocado o entre en cualquier hexágono adyacente a un esbirro aliado, inflige 1 punto de daño a ese luchador.",
                "Destruccion", "borgits_beastgrabbaz_0");

        // Borgit Wolf-Killa
        insertFighterInternal(db, (int)borgitBandId, "Borgit Wolf-Killa", "borgits_beastgrabbaz_1");

        // Uglug
        insertFighterInternal(db, (int)borgitBandId, "Uglug", "borgits_beastgrabbaz_2");

        // Snagz
        insertFighterInternal(db, (int)borgitBandId, "Snagz", "borgits_beastgrabbaz_3");

        // Hobblin’ Dregg
        insertFighterInternal(db, (int)borgitBandId, "Hobblin’ Dregg", "borgits_beastgrabbaz_4");

        // Rigg & Shamm
        insertFighterInternal(db, (int)borgitBandId, "Rigg & Shamm", "borgits_beastgrabbaz_5");
//VOY POR AQUI
        // --- Inserción de Elathain's Soulraid ---
        long elathainBandId = insertBandInternal(db, "Elathain's Soulraid",
                "Inspirar: Inspira a cada luchador aliado al comienzo de la segunda ronda de combate.\n" + //
                                        "Desinspira a cada luchador aliado al comienzo de la tercera ronda de combate. \n\n" +
                "Marea de muerte: \n"+
                "Inundación: En la primera ronda de combate, inmediatamente después del ataque cuerpo a cuerpo de un luchador enemigo, dale al atacante una ficha de Tambaleo.\n"+
                "Marea alta: En la segunda ronda de combate, los luchadores aliados tienen +1 a Movimiento.\n"+
                "Marea baja En la tercera ronda de combate, las armas cuerpo a cuerpo de los luchadores aliados (excepto las mejoras) tienen Forcejeo.\n\n"+
                "Arpón de marea: Usa esto inmediatamente después de elegir a Tammael para cargar. Las armas cuerpo a cuerpo de ese luchador (excluyendo las mejoras) tienen +1 dado de ataque para esa carga.\n\n"+
                "Cosecha de almas: Úsalo inmediatamente después de que un luchador enemigo sea asesinado por tu líder con un arma que no sea una mejora, si un Tammael aliado esta muerto. Resucita a ese Tammael aliado y colócalo en un hexágono inicial en territorio aliado.\n\n"+
                "Depredadores del mar etéreo: Úsalo inmediatamente después de que un Idoneth amigo use una habilidad básica. Empuja a cada esbirro amigo 1 hexágono más cerca de ese luchador.\n\n"+
                "Formas fantasmales: Inmediatamente después de que un luchador enemigo realice un ataque exitoso que tenga como objetivo a un Fuirann amigo, ese Fuirann amigo puede usar Resistir.",
                "Orden", "elathains_soulraid_0");

        // Elathain
        insertFighterInternal(db, (int)elathainBandId, "Elathain", "elathains_soulraid_1");

        // Fuirann
        insertFighterInternal(db, (int)elathainBandId, "Fuirann", "elathains_soulraid_2");

        // Tammael
        insertFighterInternal(db, (int)elathainBandId, "Tammael", "elathains_soulraid_3");

        // Spinefin
        insertFighterInternal(db, (int)elathainBandId, "Spinefin", "elathains_soulraid_4");

        // Duardron
        insertFighterInternal(db, (int)elathainBandId, "Duardron", "elathains_soulraid_5");

        // --- Inserción de Ylthari's Guardians ---
        long ylthariBandId = insertBandInternal(db, "Ylthari's Guardians",
                "Inspirar: Después de que un luchador aliado use una habilidad básica, puedes colocar 1 carta de poder de tu mano o la carta superior de tu mazo de poder boca abajo junto a este warscroll, hasta un máximo de 3. Estas cartas ya no están en tu mano ni en tu mazo de poder y no se pueden revelar.\n"+
                "Al final de tu turno o después de que un luchador enemigo sea asesinado, puedes colocar 1 ficha de crecimiento en cada una de esas cartas. Después de colocar la quinta ficha de crecimiento en la misma carta, elige un luchador aliado. Inspira a ese luchador, retira esas fichas de crecimiento y luego añade esa carta a tu mano.\n\n"+
                "Puedes usar 1 de las siguientes habilidades una vez por ronda de batalla.\n\n" + //
                "El florecimiento: Usa esto inmediatamente después de tu fase de Acción. Elige un luchador aliado. Empuja a ese luchador X hexágonos, donde X es el número de luchadores aliados muertos. Si no hay luchadores aliados muertos, empuja a ese luchador 1 hexágono.\n\n"+
                "La Cosecha: Usa esto inmediatamente después de elegir un arma cuerpo a cuerpo como parte de un ataque. Esa arma tiene +X dados de ataque para ese ataque, donde X es el número de luchadores aliados muertos. Si no hay luchadores aliados muertos, esa arma tiene +1 dado de ataque para ese ataque.\n\n"+
                "El declive: Usa esto inmediatamente después de tu fase de Acción. Elige un luchador aliado. Retira ese luchador del campo de batalla y colócalo en un hexágono vacío adyacente a un luchador aliado.\n\n"+
                "El crepusculo: Úsalo inmediatamente después de elegir un arma cuerpo a cuerpo como parte de un ataque. Esa arma tiene +X de daño para ese ataque, donde X es el número de luchadores aliados muertos.",
                "Orden", "yltharis_guardians_0");

        // Ylthari
        insertFighterInternal(db, (int)ylthariBandId, "Ylthari", "yltharis_guardians_1");

        // Skhathael
        insertFighterInternal(db, (int)ylthariBandId, "Skhathael", "yltharis_guardians_2");

        // Gallanghann
        insertFighterInternal(db, (int)ylthariBandId, "Gallanghann", "yltharis_guardians_3");

        // Ahnslaine
        insertFighterInternal(db, (int)ylthariBandId, "Ahnslaine", "yltharis_guardians_4");

        // --- Inserción de The Emberwatch ---
        long emberwatchBandId = insertBandInternal(db, "The Emberwatch",
                "Inspirar: Después de un paso de Acción, si un luchador aliado comenzó ese paso de Acción en territorio enemigo y utilizó una habilidad básica, inspira a ese luchador.\n\n"+
                "¡Me quedo solo!: Puedes usar esto en el último paso de poder de una ronda de batalla. Elige un luchador aliado que se encuentre en un territorio diferente al de los demás luchadores aliados. Empuja a ese luchador 1 hexágono. Ese empujón no puede terminar adyacente a otro luchador aliado. Solo puedes usar esta habilidad una vez por ronda de batalla.\n\n"+
                "Vanguard Dash (Una vez por partida): Usa esto inmediatamente después de tu paso de Acción. Elige un luchador aliado. Retira a ese luchador del campo de batalla y luego colócalo en un hexágono vacío del borde.\n\n"+
                "Centinelas mortales (Una vez por partida): Úsala inmediatamente después de elegir un arma como parte de un ataque. Esa arma tiene +1 de alcance para ese ataque.\n\n"+
                "Aves rapaces de Sigmar (Una vez por partida): Usa esto inmediatamente después del ataque exitoso de un luchador aliado si el objetivo es vulnerable. Infringe 1 punto de daño al objetivo.",
                "Orden", "the_emberwatch_0");

        // Ardorn Flamerunner
        insertFighterInternal(db, (int)emberwatchBandId, "Ardorn Flamerunner", "the_emberwatch_1");

        // Farasa Twice-Risen
        insertFighterInternal(db, (int)emberwatchBandId, "Farasa Twice-Risen", "the_emberwatch_2");

        // Yurik Velzaine
        insertFighterInternal(db, (int)emberwatchBandId, "Yurik Velzaine", "the_emberwatch_3");

        // --- Inserción de Zikkit's Tunnelpack ---
        long zikkitBandId = insertBandInternal(db, "Zikkit's Tunnelpack",
                "Inspirar: Después de una fase de Acción, si un luchador aliado ha sufrido daño o ha sido asesinado en esa fase, elige a otro luchador aliado. Inspira a ese luchador.\n\n"+
                "¡Más y más poder!: Úsalo inmediatamente al comienzo de tu fase de Acción si cada luchador aliado tiene una ficha de Carga. Elige un luchador amigo. Retira las fichas de Movimiento y/o Carga de ese luchador. Después de ese paso de Acción, tira un dado de Ataque. Con un Martillo o un Crítico, ese luchador muere.\n\n"+
                "Escurridizo (Una vez por partida): Usa esto inmediatamente después de tu paso de Acción. Elige un luchador aliado que no tenga fichas de Movimiento o Carga. Ese luchador se mueve.\n\n"+
                "Carga deformada (Una vez por partida): Usa esto inmediatamente después de elegir un arma como parte de un ataque. Esa arma tiene las habilidades Romper, Apresar y Brutal para ese ataque. Después de resolver ese ataque, tira un dado de ataque. Con una espada o un crítico, inflige 2 puntos de daño al atacante.\n\n"+
                "¡Kaboom! (Una vez por partida): Elige un esbirro aliado sin fichas de Carga para usar esta habilidad. Ese esbirro se mueve. A continuación, tira un número de dados de Ataque igual al número de ronda de batalla por cada luchador que se encuentre a 2 hexágonos de ese esbirro. Si la tirada contiene algún Martillo, inflige 1 punto de daño a ese luchador. Si la tirada contiene algún Crítico, inflige 2 puntos de daño a ese luchador. A continuación, ese esbirro muere.",
                "Chaos", "zikkits_tunnelpack_0");

        // Zikkit Rockgnaw
        insertFighterInternal(db, (int)zikkitBandId, "Zikkit Rockgnaw", "zikkits_tunnelpack_1");

        // Rittak Verm
        insertFighterInternal(db, (int)zikkitBandId, "Rittak Verm", "zikkits_tunnelpack_2");

        // Krittatok
        insertFighterInternal(db, (int)zikkitBandId, "Krittatok", "zikkits_tunnelpack_3");

        // Nitch Singe-Snout
        insertFighterInternal(db, (int)zikkitBandId, "Nitch Singe-Snout", "zikkits_tunnelpack_4");

        // Tik-Tik
        insertFighterInternal(db, (int)zikkitBandId, "Tik-Tik", "zikkits_tunnelpack_5");

        // --- Inserción de The Thricefold Discord ---
        long thricefoldBandId = insertBandInternal(db, "The Thricefold Discord",
                "Inspirar: Después de una tirada de ataque o de salvación de un luchador aliado que no contenga éxitos, o después de que un luchador aliado sea asesinado, inspira a otro luchador aliado.\n\n"+
                "Banquete irresistible (Una vez por partida): Úsalo en tu fase de poder. Descarta 1 dado de tentación y luego inflige 1 punto de daño a un luchador enemigo que no sea vulnerable.\n\n"+
                "Sentidos sublimes (Una vez por partida): Úsalo en tu fase de Poder. Descarta 1 dado de tentación y luego dale una ficha de Guardia a un luchador aliado.\n\n"+
                "Pavane de Slaanesh (Una vez por partida): Úsalo en tu fase de Poder. Descarta 1 dado de tentación y luego elige un luchador aliado. Las tiradas de salvación de ese luchador no pueden verse afectadas por Romper y Apresar en el siguiente turno.\n\n"+
                "Tentaciones de Slaanesh: Al comienzo de la primera fase de Acción de cada ronda de batalla, obtienes una reserva de 6 dados de tentación.\n" + //
                                        "\n" + //
                                        "Al final de la ronda de batalla, se pierden todos los dados de tentación que no se hayan ofrecido.\n" + //
                                        "\n" + //
                                        "Tentar...: Una vez por turno, inmediatamente después de que tu oponente falle una tirada de ataque, puedes ofrecerle un dado de tentación.\n" + //
                                        "\n" + //
                                        "Aceptar: Si tu oponente acepta tu oferta, debe sustituir 1 resultado fallido de esa tirada de ataque por un resultado exitoso (solo Martillo o Espadas). Los dados de ataque sustituidos de esta manera no se pueden volver a tirar ni modificar. A continuación, inmediatamente después de que se haya resuelto ese ataque, puedes elegir un luchador amigo que no fuera el objetivo. Empuja a ese luchador 1 hexágono.\n" + //
                                        "\n" + //
                                        "Rechazar: Si tu oponente rechaza tu oferta, inflige 1 punto de daño al atacante inmediatamente después de que se haya resuelto el ataque.",
                "Chaos", "the_thricefold_discord_0");

        // Vexmor
        insertFighterInternal(db, (int)thricefoldBandId, "Vexmor", "the_thricefold_discord_1");

        // Vashtiss the Coiled
        insertFighterInternal(db, (int)thricefoldBandId, "Vashtiss the Coiled", "the_thricefold_discord_2");

        // Lascivyr the Bladed Blessing
        insertFighterInternal(db, (int)thricefoldBandId, "Lascivyr", "the_thricefold_discord_3");

        // --- Inserción de Jaws of Itzl ---
        long jawsBandId = insertBandInternal(db, "Jaws of Itzl",
                "Inspirar: Después de infligir daño a un luchador enemigo en la fase de poder o en la fase de acción de tu oponente, elige un luchador amigo. Inspira a ese luchador.\n\n"+
                "Luchadores depredadores: Úsalo inmediatamente después de que un luchador enemigo falle un ataque cuerpo a cuerpo si el objetivo era un saurus amigo y la tirada de salvación contenía algun escudo. Inflige 1 punto de daño a ese luchador enemigo.\n\n"+
                "Control instintivo: Úsalo inmediatamente después de elegir un objetivo como parte de un ataque realizado por un saurus amigo. Elige un esbirro amigo sin fichas de Movimiento o Carga. Ese luchador puede moverse.\n\n"+
                "Atacado salvaje (Una vez por partida): Úsalo en una fase de Poder. Elige un luchador enemigo adyacente a un saurus amigo dañado que no tenga fichas de Carga. Tira un dado de Ataque por cada ficha de daño que tenga ese luchador amigo. Infringe a ese luchador enemigo una cantidad de daño igual al número de espadas y criticos de la tirada. Cada vez que un luchador enemigo muera por esta habilidad, puedes usar esta habilidad una vez más en esta partida.\n\n"+
                "Obstinado (Una vez por partida): Usa esto en un paso de Poder. Elige un saurio aliado. Ese luchador no podrá ser presionado hasta el final de la ronda de combate.",
                "Orden", "jaws_of_itzl_0");

        // Kro-Jax
        insertFighterInternal(db, (int)jawsBandId, "Kro-Jax", "jaws_of_itzl_1");

        // So-Kar
        insertFighterInternal(db, (int)jawsBandId, "So-Kar", "jaws_of_itzl_2");

        // Ro-Tak
        insertFighterInternal(db, (int)jawsBandId, "Ro-Tak", "jaws_of_itzl_3");

        // Sotek's Venomites
        insertFighterInternal(db, (int)jawsBandId, "Sotek's Venomites", "jaws_of_itzl_4");

        // --- Inserción de Grandfather's Gardeners ---
        long gardenersBandId = insertBandInternal(db, "Grandfather's Gardeners",
                "Inspirar: Después de un paso de Acción, si un luchador aliado es vulnerable, inspira a ese luchador.\n\n"+
                "Plaga floreciente: Cuando un luchador aliado tiene la runa Jardinero en lugar de una característica de daño, es igual al espacio actual en tu marcador.\n\n"+
                "Imperturbable: No se puede infligir más de 2 puntos de daño a los luchadores aliados sin inspiración en el mismo turno.\n\n"+
                "Cosechar: Utilízalo inmediatamente después de colocar tu ficha en este espacio. Elige un luchador aliado. Cura a ese luchador.\n\n"+
                "En la fase de «Preparación» de las bandas de guerra, coloca una ficha genérica en el espacio «Inicio» del marcador de este warscroll. Avanza esa ficha 1 espacio en el sentido de las agujas del reloj inmediatamente:\n" + //
                                        "- Después de tu turno.\n" + //
                                        "- Después de infligir daño a un luchador aliado.\n" + //
                                        "- Después de tu turno si tu banda de guerra tiene más fichas de tesoro que tu oponente.",
                "Chaos", "grandfathers_gardeners_0");

        // Phleghmus
        insertFighterInternal(db, (int)gardenersBandId, "Phleghmus", "grandfathers_gardeners_1");

        // Slunge
        insertFighterInternal(db, (int)gardenersBandId, "Slunge", "grandfathers_gardeners_2");

        // Bug-Eyed Dripterus
        insertFighterInternal(db, (int)gardenersBandId, "Bug-Eyed Dripterus", "grandfathers_gardeners_3");

        // Strewg
        insertFighterInternal(db, (int)gardenersBandId, "Strewg", "grandfathers_gardeners_4");

        // Squort
        insertFighterInternal(db, (int)gardenersBandId, "Squort", "grandfathers_gardeners_5");

        // --- Inserción de Knives of the Crone ---
        long knivesBandId = insertBandInternal(db, "Knives of the Crone",
                "Inspirar: Si la Sabiduría predicha se predice tras un ataque exitoso de un luchador aliado como resultado de la habilidad Visiones en la sangre, inspira a ese luchador.\n\n"+
                "Visiones de la sangre: Úsalo inmediatamente después de un ataque exitoso de un luchador aliado. Haz una tirada de profecía (consulta Rituales de profecía).\n\n"+
                "Rituales de profecia: Al comienzo de la primera fase de acción de cada ronda de combate, puedes realizar una tirada de profecía lanzando 4 dados de ataque.\n" + //
                                        "Si la tirada contiene 1 de los resultados de la tabla de profecías, esa profecía se predice.\n" + //
                                        "Si la tirada contiene 2 resultados diferentes de la tabla de profecías, ambas profecías se predicen.\n" + //
                                        "Si la tirada contiene 3 símbolos iguales, puedes elegir 1 profecía de la tabla de profecías. Esa profecía se predice.\n" + //
                                        "Si la tirada contiene 4 símbolos iguales, puedes elegir 2 profecías diferentes de la tabla de profecías. Ambas profecías se predicen.\n" + //
                                        "•\n" + //
                                        "Si la tirada contiene algun critico, puedes sustituirlo por otro símbolo de tu elección .\n" + //
                                        "Cuando se predice una profecía, utiliza inmediatamente su habilidad.\n\n"+
                "TABLA DE PROFECÍAS\n"+
                "Matanza predicha: la próxima vez que elijas un arma cuerpo a cuerpo como parte de un ataque en esta ronda de combate, esa arma tendrá un golpe crítico doloroso para ese ataque.\n"+
                "Sabiduría predicha: Mira la carta superior de tu mazo de objetivos. Coloca esa carta en la parte superior o inferior de ese mazo.\n"+
                "Protección predicha: La próxima vez que hagas una tirada de salvación en esta ronda de batalla, puedes volver a tirar inmediatamente 1 dado de salvación en esa tirada de salvación.",
                "Orden", "knives_of_the_crone_0");

        // Kaerna Vix
        insertFighterInternal(db, (int)knivesBandId, "Kaerna Vix", "knives_of_the_crone_1");

        // Tazari
        insertFighterInternal(db, (int)knivesBandId, "Tazari", "knives_of_the_crone_2");

        // Krieta
        insertFighterInternal(db, (int)knivesBandId, "Krieta", "knives_of_the_crone_3");

        // Azphel
        insertFighterInternal(db, (int)knivesBandId, "Azphel", "knives_of_the_crone_4");


        // --- Inserción de Kainan's Reapers ---
        long kainanBandId = insertBandInternal(db, "Kainan's Reapers",
                "Inspirar: Después de matar a un luchador enemigo, obtén un número de fichas de tributo óseo igual a su característica de Recompensa. Después de tu fase de Acción, puedes elegir un luchador aliado y retirar un número de fichas de tributo óseo igual a su característica de Recompensa. A continuación, inspira a ese luchador.\n\n" +
                "Avance Mortek: Úsalo inmediatamente después de tu fase de acción si un Kainan o Khenta aliado ha usado una habilidad básica en esa fase de acción. Cada esbirro aliado puede moverse, pero cada uno debe terminar ese movimiento adyacente a uno o más esbirros aliados.\n\n" +
                "Último aviso (Una vez por partida): Úsalo inmediatamente después de tu fase de acción. Los luchadores aliados no pueden ser elegidos como objetivo de un ataque en la siguiente fase de acción a menos que sean tu líder. Esta habilidad no se puede usar si Kainan es asesinado.\n\n" +
                "Rangos inquebrantables por la disidencia (Una vez por partida): Úsala inmediatamente después de elegir un arma como parte de un ataque cuerpo a cuerpo. Si el objetivo está flanqueado y/o rodeado, esa arma tiene Doloroso para ese ataque.\n\n" +
                "Postura monolítica (Una vez por partida): Úsala inmediatamente después de tu fase de Acción. Elige un número de secuaces amigos igual o inferior al número de ronda de combate. Dale a cada uno de esos luchadores una ficha de Guardia.\n" +
                "Asalto implacable (Una vez por partida): Úsala inmediatamente después de tu fase de Acción. Elige un esbirro aliado con fichas de Movimiento y/o Carga. Retira las fichas de Movimiento y/o Carga de ese luchador." ,
                "Ossiarch Bonereapers", "kainans_reapers_0");

        // Mir Kainan
        insertFighterInternal(db, (int)kainanBandId, "Mir Kainan", "kainans_reapers_1");

        // Binar Khenta
        insertFighterInternal(db, (int)kainanBandId, "Binar Khenta", "kainans_reapers_2");

        // Morteks
        insertFighterInternal(db, (int)kainanBandId, "Senha", "kainans_reapers_3");
        insertFighterInternal(db, (int)kainanBandId, "Karu", "kainans_reapers_4");
        insertFighterInternal(db, (int)kainanBandId, "Nohem", "kainans_reapers_5");
        insertFighterInternal(db, (int)kainanBandId, "Hakor", "kainans_reapers_6");

        // --- Inserción de Khagra's Ravagers ---
        long khagraBandId = insertBandInternal(db, "Khagra's Ravagers",
                "Inspirar: Después del último paso de Acción en una ronda de batalla, si hay 2 o más fichas de tesoro profanadas, inspira a cada luchador amigo. \n\n" +
                "Reino Arrasado: Puedes usar esta habilidad una vez por paso de Poder. Elige una ficha de tesoro que controle un luchador amigo. Esa ficha de tesoro queda profanada hasta que se voltee. \n\n" +
                "Profanación Ritual:  Inmediatamente después de que un luchador enemigo sea asesinado, si el atacante o el objetivo controlaba una ficha de tesoro, puedes elegir esa ficha de tesoro. Esa ficha de tesoro queda profanada hasta que se dé la vuelta. \n\n" +
                "Aprobación Oscura (Una vez por partida): Úsalo en una fase de Poder. Roba 1 carta de Poder por cada ficha de tesoro profanada. \n\n" +
                "El Ojo de los Dioses (Una vez por aprtida): Elige 1 de las siguientes opciones:\n\n" + //
                                        "Úsalo inmediatamente después de que un luchador amigo que controle una ficha de tesoro sea elegido como objetivo de un ataque. El objetivo tiene +1 a la tirada de salvación para ese ataque.\n\n" + //
                                        "Úsalo inmediatamente después de elegir a un luchador enemigo que controle una ficha de tesoro como objetivo de un ataque. El arma que elegiste para ese ataque tiene +1 dado de ataque para ese ataque. \n\n" +
                "Avance devastador (Una vez por aprtida): Úsalo en una fase de Poder. Elige un luchador aliado. Empuja a ese luchador un número de hexágonos igual al número de fichas de tesoro profanadas. Ese empujón debe terminar en una ficha de tesoro y/o adyacente a un luchador enemigo.",
                "Slaves to Darkness", "khagras_ravagers_0");

        // Khagra the Usurper
        insertFighterInternal(db, (int)khagraBandId, "Khagra the Usurper", "khagras_ravagers_1");

        // Zarshia Bittersoul
        insertFighterInternal(db, (int)khagraBandId, "Zarshia Bittersoul", "khagras_ravagers_2");

        // Razek
        insertFighterInternal(db, (int)khagraBandId, "Razek", "khagras_ravagers_3");

        // Cragan
        insertFighterInternal(db, (int)khagraBandId, "Cragan", "khagras_ravagers_4");

        // --- Inserción de Kurnoth's Heralds ---
        long kurnothBandId = insertBandInternal(db, "Kurnoth's Heralds",
                "Inspirar: Después de una fase de Acción, si un luchador aliado comenzó esa fase de Acción en territorio amigo y luego realizó un Ataque con éxito, inspira a ese luchador.\n\n"+
                "Centinelas ágiles: Mientras se encuentran en territorio amigo, los luchadores aliados tienen la runa de vuelo y no pueden ser flanqueados (aunque sí pueden ser rodeados).\n\n"+
                "La caza sin fin (Una vez por partida): Úsalo inmediatamente después de que un luchador amigo haya atacado. Puedes empujar al atacante hasta 2 hexágonos.\n\n"+
                "Bolea de precisión (Una vez por partida): Úsala inmediatamente después del ataque a distancia de un Lenwythe aliado. Ese luchador vuelve a atacar con la misma arma. El objetivo de ese ataque debe ser diferente al del primer ataque.\n\n"+
                "Orgullo del heraldo (Una vez por partida): Úsala inmediatamente después de elegir un arma como parte del ataque de tu líder. Esa arma tiene Romper y Apresar para ese ataque.",
                "Sylvaneth", "kurnoths_heralds_0");

        // Ylarin
        insertFighterInternal(db, (int)kurnothBandId, "Ylarin", "kurnoths_heralds_1");

        // Cullon
        insertFighterInternal(db, (int)kurnothBandId, "Cullon", "kurnoths_heralds_2");

        // Lenwythe
        insertFighterInternal(db, (int)kurnothBandId, "Lenwythe", "kurnoths_heralds_3");

        // --- Inserción de Mollog's Mob ---
        long mollogBandId = insertBandInternal(db, "Mollog's Mob",
                "Inspirar: Después de que tu líder reciba una ficha de daño, si tiene 4 o más fichas de daño, inspira a cada luchador amigo.\n\n"+
                "Acechador: Cuando despliegues un Stalagsquig amigo, puedes colocarlo en un hexágono vacío que no contenga una ficha de característica y que no sea un hexágono inicial en territorio enemigo. Un Stalagsquig amigo no puede moverse, ser empujado ni ser retirado del campo de batalla a menos que sea asesinado. Siempre se considera que tiene una ficha de carga.\n\n"+
                "Regeneración Troggot: Úsalo al final de cada ronda de batalla después de seguir la secuencia de la fase final. Cura a tu líder.\n\n"+
                "Peligros antinaturales: Al comienzo del primer paso de acción de cada ronda de batalla, elige una de las siguientes opciones:\n"+
                                        "Hasta el final de la ronda de batalla, los luchadores enemigos adyacentes a un esbirro amigo quedan rodeados.\n" + //
                                        "Hasta el final de la ronda de combate, cada vez que un luchador enemigo sea colocado, empujado o entre en un hexágono adyacente a un esbirro amigo, dale a ese luchador enemigo una ficha de tambaleo.\n" + //
                                        "Hasta el final de la ronda de combate, la primera vez que cada luchador enemigo sea colocado, empujado o entre en un hexágono adyacente a un esbirro amigo, tira un dado de Ataque. Con un martillo o critico , inflige 1 daño a ese luchador.\n\n"+
                "Seguir: Úsalo inmediatamente después de que tu líder se mueva. Elige a otro luchador aliado. Empuja a ese luchador hasta 2 hexágonos. Ese empujón debe terminar adyacente a tu líder o adyacente a un luchador enemigo que esté adyacente a tu líder.\n\n"+
                "Infestación (Una vez por partida): Úsalo en una fase de poder. Elige un esbirro aliado muerto. Resucita a ese luchador, colócalo en un hexágono inicial vacío y dale una ficha de carga.",
                "Gloomspite Gitz", "mollogs_mob_0");

        // Mollog the Mighty
        insertFighterInternal(db, (int)mollogBandId, "Mollog the Mighty", "mollogs_mob_1");

        // Bat Squig
        insertFighterInternal(db, (int)mollogBandId, "Bat Squig", "mollogs_mob_2");

        // Spiteshroom
        insertFighterInternal(db, (int)mollogBandId, "Spiteshroom", "mollogs_mob_3");

        // Stalagsquig
        insertFighterInternal(db, (int)mollogBandId, "Stalagsquig", "mollogs_mob_4"); // Assuming image exists

        // --- Inserción de Ephilim's Pandaemonium ---
        long ephilimBandId = insertBandInternal(db, "Ephilim's Pandaemonium",
                "Inspirar: Si hay 2 o más esbirros aliados inspirados, inspira a tu líder.\n\n"+
                "Cambio glorioso: Inmediatamente después del primer paso de acción de cada ronda de batalla, elige un esbirro aliado. Inspira a ese luchador. A continuación, elige un esbirro aliado. Dale a ese luchador una ficha de tambaleo.\n\n"+
                "Succionador de poder: Inmediatamente después de que un esbirro aliado dentro de 2 hexágonos de tu líder sea asesinado, puedes robar 1 carta de Poder.\n\n"+
                "Abominaciones invocadas: Usa esto inmediatamente después de tu último paso de acción de cada ronda si tu líder no esta muerto. Elige un esbirro aliado asesinado. Resucita a ese luchador y colócalo en un hexágono inicial en territorio aliado. A continuación, inflige daño a ese luchador hasta que sea vulnerable.\n\n"+
                "Fuegos del cambio (Una vez por partida): Úsalo inmediatamente después de un ataque a distancia exitoso de un luchador amigo. Dale a cada luchador enemigo adyacente al objetivo 1 ficha de deformación. Después del primer paso de acción de tu oponente en la siguiente ronda de batalla, retira cada ficha de deformación de cada luchador enemigo, una por una. Después de retirar las fichas de un luchador de esta manera, inflige 1 punto de daño a ese luchador.\n\n"+
                "Mutaciones compartidas (Una vez por partida): Úsala inmediatamente después de tu fase de Acción. Inflige 1 punto de daño a cada luchador enemigo adyacente a cualquier esbirro amigo .",
                "Disciples of Tzeentch", "ephilims_pandaemonium_0"); // Assuming image exists

        // Ephilim
        insertFighterInternal(db, (int)ephilimBandId, "Ephilim the Unknowable", "ephilims_pandaemonium_1");

        // Minions (Apo'trax, Spawnmaw, Kindlefinger, Flamespooler)
        insertFighterInternal(db, (int)ephilimBandId, "Apo'trax", "ephilims_pandaemonium_2");
        insertFighterInternal(db, (int)ephilimBandId, "Spawnmaw", "ephilims_pandaemonium_3");
        insertFighterInternal(db, (int)ephilimBandId, "Kindlefinger", "ephilims_pandaemonium_4");
        insertFighterInternal(db, (int)ephilimBandId, "Flamespooler", "ephilims_pandaemonium_5");


        // --- Inserción de Gorechosen of Dromm ---
        long gorechosenBandId = insertBandInternal(db, "Gorechosen of Dromm",
                "Inspirar: Después de una fase de Acción, si tienes 8 o más fichas de tributo de sangre, inspira a cada luchador amigo.\n\n"+
                "El tributo de sangre: Inmediatamente después de se le inflija daño a un luchador, obtén 1 ficha de tributo de sangre.\n\n"+
                "Ajuste de cuentas brutales: Inmediatamente después de que un luchador enemigo sea asesinado, obtén un número de fichas de tributo de sangre igual a la característica de Recompensa de ese luchador.\n\n"+
                "Llamada de la sangre (Una vez por partida): Úsalo en una fase de Poder. Elige 2 luchadores sin fichas de Movimiento o Carga. Empuja a cada luchador 1 hexágono para que queden adyacentes. Puedes descartar 1 de tus fichas de tributo de sangre antes de cada empujón. Si lo haces, puedes empujar a ese luchador 2 hexágonos en su lugar.\n\n"+
                "Enfurecer (Una vez por partida): Úsalo en una fase de poder. Descarta 8 fichas de tributo de sangre y luego elige un luchador enemigo. Tu oponente resuelve una carga para ese luchador.\n\n"+
                "Frenesí final (Una vez por partida): Úsala inmediatamente después de infligir daño a un luchador aliado si ese luchador fuera a morir. Descarta cualquier cantidad de tus fichas de tributo de sangre. Tira un dado de ataque por cada ficha descartada de esta manera. Con un critico, quita las fichas de daño de ese luchador hasta que quede vulnerable. Ese luchador muere al final de la ronda de combate.",
                "Blades of Khorne", "gorechosen_of_dromm_0");

        // Dromm
        insertFighterInternal(db, (int)gorechosenBandId, "Dromm, Wounder of Worlds", "gorechosen_of_dromm_1");

        // Gorehulk
        insertFighterInternal(db, (int)gorechosenBandId, "Gorehulk", "gorechosen_of_dromm_2");

        // Herax
        insertFighterInternal(db, (int)gorechosenBandId, "Herax", "gorechosen_of_dromm_3");

        // --- Inserción de Grinkrak's Looncourt ---
        long grinkrakBandId = insertBandInternal(db, "Grinkrak's Looncourt",
                "Misiones: Antes de que un luchador aliado utilice una habilidad básica, puedes elegir una misión que aún no hayas intentado. Una vez completada la misión, utiliza inmediatamente su habilidad de recompensa. No puedes intentar varias misiones al mismo tiempo. Puedes abandonar una misión en una fase de poder en lugar de jugar una carta de poder.\n\n"+
                "Te nombro...: Puedes usar esta habilidad una vez por ronda de combate antes de que un Loonknight aliado use una habilidad básica. Ese luchador queda nombrado hasta el final de la ronda de combate.\n\n"+
                "Misión conquista el reino: Completa esta misión después de una fase de acción si tu banda controla 2 o más fichas de tesoro y controla más fichas de tesoro que la banda de tu oponente. Recompensa: puedes inspirar a un Loonknight aliado. Si un luchador aliado nombrado controla una ficha de tesoro cuando completas esta misión, también puedes inspirar a tu líder y luego robar hasta 2 cartas de poder.\n\n"+
                "Misión avanza y golpea: Si eliges esta misión, los luchadores aliados nombrados tienen +1 de movimiento durante el resto de la partida. Completa esta misión después de un paso de acción si hay 2 o más luchadores aliados en territorio enemigo. Recompensa: puedes inspirar hasta a 2 Loonknights aliados. Si tu líder se encuentra en territorio enemigo cuando completas esta misión, también puedes inspirar a tu líder.\n\n"+
                "Misión mata a las hordas: Si eliges esta misión, las armas cuerpo a cuerpo de los luchadores aliados tendrán +1 dado de ataque durante el resto de la partida. Completarás esta misión tras una fase de Acción si muere algún luchador enemigo con una característica de Recompensa total de 2 o más. Recompensa: puedes inspirar a un luchador aliado. Si alguno de esos luchadores enemigos ha muerto por el ataque de un luchador aliado nombrado, puedes inspirar a un Loonknight aliado y a tu líder en su lugar.\n\n"+
                "Misión matar al dedo aterrador: Si eliges esta misión, durante el resto de la partida, las armas cuerpo a cuerpo de los luchadores aliados nombrados tienen Doloroso si el objetivo tiene una característica de Recompensa de 2 o más. Completa esta misión tras una fase de Acción si un luchador enemigo con una característica de Recompensa de 2 o más es asesinado. Recompensa: puedes inspirar hasta a 2 Loonknights aliados y a tu líder.",
                "Gloomspite Gitz", "grinkraks_looncourt_0");

        // Grinkrak
        insertFighterInternal(db, (int)grinkrakBandId, "Grinkrak the Great", "grinkraks_looncourt_1");

        // The Looncourt
        insertFighterInternal(db, (int)grinkrakBandId, "Snorbo da Spore", "grinkraks_looncourt_2");
        insertFighterInternal(db, (int)grinkrakBandId, "Pokin' Snark", "grinkraks_looncourt_3");
        insertFighterInternal(db, (int)grinkrakBandId, "Pointy Burk", "grinkraks_looncourt_4");
        insertFighterInternal(db, (int)grinkrakBandId, "Moonface Nagz", "grinkraks_looncourt_5");
        insertFighterInternal(db, (int)grinkrakBandId, "Skolko and Pronk", "grinkraks_looncourt_6");
        insertFighterInternal(db, (int)grinkrakBandId, "Grib", "grinkraks_looncourt_7");

        // --- Inserción de Hexbane's Hunters ---
        long hexbaneBandId = insertBandInternal(db, "Hexbane's Hunters",
                "Inspirar: Después de que un agente Azyrite aliado sea asesinado, puedes elegir un agente Azyrite aliado. Inspira a ese luchador. Después de que un Pock aliado sea asesinado, inspira a cada esbirro aliado.\n\n"+
                "Herramientas del oficio: Suma la característica de recompensa total de los agentes Azyrite aliados asesinados a tus puntos de gloria totales para determinar cuántas mejoras pueden equipar los luchadores aliados.\n\n"+
                "Marcado para la venganza: Inmediatamente después de que un agente Azyrite aliado sea asesinado por un luchador enemigo, ese luchador enemigo queda marcado. Los demás luchadores enemigos marcados dejan de estarlo. Puedes volver a tirar 1 dado en una tirada de ataque si el objetivo está marcado. Después de que un luchador enemigo marcado sea asesinado, puedes eliminar todas las fichas de movimiento y/o carga de un luchador aliado o robar 1 carta de poder.\n\n"+
                "Manada unida: Inmediatamente después de que el primer esbirro amigo sea asesinado, debes sumar 1 a la característica Recompensa del otro esbirro amigo.\n\n"+
                "Sabuesos leales: Úsalo inmediatamente después de que un agente Azyrite aliado se mueva. Elige un esbirro aliado sin fichas de Carga. Ese luchador puede moverse.\n\n"+
                "Guardián de la sangre del mártir (Una vez por partida): Úsalo inmediatamente después de que un agente azyrita aliado sea elegido como objetivo de un ataque. El atacante no puede usar habilidades de arma para ese ataque.",
                "Order of Azyr", "hexbanes_hunters_0");

        // Haskel Hexbane
        insertFighterInternal(db, (int)hexbaneBandId, "Haskel Hexbane", "hexbanes_hunters_1");

        // Aemos Duncarrow
        insertFighterInternal(db, (int)hexbaneBandId, "Aemos Duncarrow", "hexbanes_hunters_2");

        // Brydget Axwold
        insertFighterInternal(db, (int)hexbaneBandId, "Brydget Axwold", "hexbanes_hunters_3");

        // Quiet Pock
        insertFighterInternal(db, (int)hexbaneBandId, "Quiet Pock", "hexbanes_hunters_4");

        // Ratspike
        insertFighterInternal(db, (int)hexbaneBandId, "Ratspike", "hexbanes_hunters_5");

        // Grotbiter
        insertFighterInternal(db, (int)hexbaneBandId, "Grotbiter", "hexbanes_hunters_6");

        // --- Inserción de Ironsoul's Condemners ---
        long ironsoulBandId = insertBandInternal(db, "Ironsoul's Condemners",
                "Inspirar: Inmediatamente después de realizar una tirada de ataque o una tirada de salvación para un luchador aliado, si dicha tirada incluía algun critico, inspira a ese luchador.\n\n"+
                "Bastion contra la oscuridad: Los luchadores aliados sin fichas de Movimiento y/o Carga no pueden ser presionados.\n\n"+
                "Golpe castigador (Una vez por partida): Úsala inmediatamente después de elegir un arma como parte de un ataque de un Brodus aliado. Esa arma tiene +X dados de ataque, donde X es igual a la ronda de batalla.\n\n"+
                "Escudo cargado de éter (Una vez por partida): Úsalo inmediatamente después de que tu oponente elija un arma como parte de un ataque que tenga como objetivo un escudo etérico aliado. Las habilidades de las armas no se pueden usar para ese ataque.\n\n"+
                "Maza cargada de energía etérea (Una vez por partida): Úsala inmediatamente después de elegir un arma como parte de un ataque de un escudo etéreo amigo. Esa arma tiene +1 de alcance para ese ataque.",
                "Stormcast Eternals", "ironsouls_condemners_0");

        // Gwynne Ironsoul
        insertFighterInternal(db, (int)ironsoulBandId, "Gwynne Ironsoul", "ironsouls_condemners_1");

        // Brodus Blightbane
        insertFighterInternal(db, (int)ironsoulBandId, "Brodus Blightbane", "ironsouls_condemners_2");

        // Tavian of Sarnassus
        insertFighterInternal(db, (int)ironsoulBandId, "Tavian of Sarnassus", "ironsouls_condemners_3");

        // --- Inserción de Morgok's Krushas ---
        long morgokBandId = insertBandInternal(db, "Morgok's Krushas",
                "Inspirar: En una fase de poder, en lugar de jugar una carta de poder, puedes elegir un luchador aliado con 3 o más fichas de Waaagh! Quita 3 fichas de Waaagh! de ese luchador y luego inspíralo.\n\n"+
                "Energía ¡WAAAGH!: Úsalo inmediatamente después de que un luchador amigo ataque o un luchador enemigo ataque y un luchador amigo sea el objetivo de ese ataque. Dale a ese luchador amigo una ficha Waaagh!\n\n"+
                "Tierra muerta (Una vez por partida): Úsalo inmediatamente después de que se inflija daño a un luchador amigo con fichas Waaagh! como parte de un ataque. Retira hasta 2 fichas Waaagh! de ese luchador. Por cada ficha Waaagh! que retires, elige 1 de las siguientes opciones:.\n" + //
                                        " Dale a ese luchador amigo una ficha de Guardia.\n"+
                                        " Dale al atacante una ficha de tambaleo.\n\n"+
                "¡Cállate, enano! (Una vez por partida): Úsalo inmediatamente después de que un oponente juegue un ardid. Retira hasta 2 fichas de ¡Waaagh! de un luchador aliado y luego tira un dado de Ataque por cada ficha que hayas retirado. Si la tirada contiene un martillo o un critico, ese ardid no tiene efecto.\n\n"+
                "¡Muévete, gitz! (Una vez por partida): Úsalo en una fase de Poder. Dale a tu líder una ficha de Carga. A continuación, empuja a cada luchador aliado hasta un número de hexágonos igual al número de ronda de batalla.",
                "Ironjawz", "morgoks_krushas_0");

        // Morgok
        insertFighterInternal(db, (int)morgokBandId, "Morgok", "morgoks_krushas_1");

        // 'Ardskull
        insertFighterInternal(db, (int)morgokBandId, "'Ardskull", "morgoks_krushas_2");

        // Thugg
        insertFighterInternal(db, (int)morgokBandId, "Thugg", "morgoks_krushas_3");

        // --- Inserción de Myari's Purifiers ---
        long myariBandId = insertBandInternal(db, "Myari's Purifiers",
                "Inspirar: Después de realizar una tirada de ataque o una tirada de salvación para un luchador aliado, si todos los resultados han sido exitosos, inspira a ese luchador.\n\n"+
                "Reserva de cuarzo éter: Después de desplegar a un luchador aliado, dale una ficha de cuarzo éter. Un luchador aliado que tenga una ficha de cuarzo éter puede usar la habilidad Sentidos agudizados:\n" + //
                                        "Sentidos agudizados: úsala inmediatamente después de realizar una tirada de ataque o una tirada de salvación. Descarta la ficha de cuarzo etéreo de este luchador. Vuelve a tirar cualquier dado de esa tirada de ataque o de salvación.\n\n"+
                "Corona cegadora: Si un Myari aliado tiene una ficha de cuarzo etéreo al comienzo de un turno, tiene +1 a la salvación hasta el final de ese turno.\n\n"+
                "Resistente como una roca: Mientras un Bahannar aliado tenga una ficha de cuarzo etéreo, no puede ser empujado por las habilidades enemigas.\n\n"+
                "Puntería impecable: Mientras una Senaela amiga tenga una ficha de cuarzo etéreo, sus armas a distancia tienen Brutal y Apresar.\n\n"+
                "Golpe magistral: Mientras un Ailenn amigo tenga una ficha de cuarzo etéreo, cuando ataque con un arma cuerpo a cuerpo, el objetivo esta Flanqueado.",
                "Lumineth Realm-lords", "myaris_purifiers_0");

        // Myari Lightcaller
        insertFighterInternal(db, (int)myariBandId, "Myari Lightcaller", "myaris_purifiers_1");

        // Bahannar
        insertFighterInternal(db, (int)myariBandId, "Bahannar", "myaris_purifiers_2");

        // Ailenn
        insertFighterInternal(db, (int)myariBandId, "Ailenn", "myaris_purifiers_3");

        // Senaela
        insertFighterInternal(db, (int)myariBandId, "Senaela", "myaris_purifiers_4");

        // --- Inserción de Rippa's Snarlfangs ---
        long rippaBandId = insertBandInternal(db, "Rippa's Snarlfangs",
                "Inspirar: Después de un paso de poder, si un luchador aliado está equipado con 2 o más mejoras, inspira a ese luchador. Después de que un luchador aliado ataque de nuevo como resultado de la habilidad de arma «Fauces de Snarlfang», si ese ataque tiene éxito, inspira a ese luchador. \n\n" +
                "Si el arma de un luchador aliado tiene la habilidad de arma Fauces de Snarlfang, puedes usar la siguiente habilidad de arma: \n" +
                "Fauces de Snarlfang: Si el ataque tiene éxito, el atacante puede volver a atacar inmediatamente utilizando el arma correspondiente que se indica a continuación. El objetivo de ese ataque debe ser el mismo que el del primer ataque. Estas armas no se pueden modificar.\n\n"+
                "Tácticas de manada (Una vez por partida): Utiliza esto en tu fase de poder. Elige un luchador enemigo. Empuja a cada luchador amigo 1 hexágono más cerca de ese luchador.\n\n"+
                "Olor a debilidad (Una vez por partida): Usa esto en un paso de poder. En el siguiente turno, las tiradas de flaqueado y rodeado no cuentan como éxitos en las tiradas de ataque o de salvación para los luchadores enemigos adyacentes a cualquier luchador amigo.\n\n"+
                "Agacharse y esquivar (Una vez por partida): Úsalo inmediatamente después de que un luchador aliado sea elegido como objetivo de un ataque. Las tiradas de salvación realizadas por ese luchador para ese ataque cuentan como si tuvieran más criticos que la tirada de ataque a efectos de presionar.",
                "Gloomspite Gitz", "rippas_snarlfangs_0");

        // Rippa Narkbad
        insertFighterInternal(db, (int)rippaBandId, "Rippa Narkbad", "rippas_snarlfangs_1");

        // Stabbit
        insertFighterInternal(db, (int)rippaBandId, "Stabbit", "rippas_snarlfangs_2");

        // Mean-Eye
        insertFighterInternal(db, (int)rippaBandId, "Mean-Eye", "rippas_snarlfangs_3");

        // --- Inserción de Sepulchral Guard ---
        long sepulchralBandId = insertBandInternal(db, "Sepulchral Guard",
                "Inspirar: Inmediatamente después de dar una ficha de resucitado a un luchador aliado, inspira a ese luchador. Después de dar a los luchadores aliados tres o más fichas de resucitado, inspira a tu líder. \n\n" +
                "¡Adelante! (accion Basica): Elige a tu líder para usar esta habilidad si no tiene fichas de Carga. Elige hasta otros 2 luchadores aliados. Esos luchadores se mueven inmediatamente. \n\n" +
                "¡Levantaos! (accion Basica): Elige a tu líder (MM) para usar esta habilidad si no tiene fichas de Carga. Elige hasta 2 luchadores aliados muertos. Resucita a esos luchadores y colócalos en diferentes hexágonos de inicio vacíos de tu territorio. \n\n" +
                "Manos agarradoras: (Una vez por partida): Usa esto inmediatamente después de elegir un objetivo como parte de un ataque. Ese objetivo queda rodeado para ese ataque. \n\n" +
                "Reforma Sorprendente (Una vez por partida): Úsalo en tu fase de Poder. Retira a tu líder del campo de batalla y colócalo en un hexágono vacío adyacente a 2 luchadores amigos. A continuación, dale a tu líder una ficha de Carga. \n\n" +
                "Metralla ósea: (Una vez por partida): Úsala inmediatamente después de que un luchador amigo sin fichas de resucitado sea asesinado por un ataque cuerpo a cuerpo realizado por un luchador enemigo. Inflige 1 punto de daño a ese luchador enemigo.",
                "Soulblight Gravelords", "sepulchral_guard_0");

        // Sepulchral Warden
        insertFighterInternal(db, (int)sepulchralBandId, "Sepulchral Warden", "sepulchral_guard_1");

        // Prince of Dust
        insertFighterInternal(db, (int)sepulchralBandId, "Prince of Dust", "sepulchral_guard_2");

        // The Champion
        insertFighterInternal(db, (int)sepulchralBandId, "The Champion", "sepulchral_guard_3");

        // The Harvester
        insertFighterInternal(db, (int)sepulchralBandId, "The Harvester", "sepulchral_guard_4");

        // The Inevitable Petitioner
        insertFighterInternal(db, (int)sepulchralBandId, "The Inevitable Petitioner", "sepulchral_guard_5");

        // The Zealous Petitioner
        insertFighterInternal(db, (int)sepulchralBandId, "The Zealous Petitioner", "sepulchral_guard_6");

        // The Rising Petitioner
        insertFighterInternal(db, (int)sepulchralBandId, "The Rising Petitioner", "sepulchral_guard_7");

        // --- Inserción de Sons of Velmorn ---
        long velmornBandId = insertBandInternal(db, "Sons of Velmorn",
                "Inspirar: Tras un ataque exitoso de un luchador aliado, inspira al atacante y a cada luchador aliado adyacente al objetivo. \n\n" +
                "Orden Mortal: Úsala inmediatamente después de que tu líder utilice una habilidad básica. Dale a ese luchador una ficha de orden. \n" + //
                                        "Mientras tu líder tenga fichas de mando, cada vez que realices una tirada de ataque para un Grave Guard aliado, tu líder se considerará adyacente al objetivo. Retira todas las fichas de mando al final de cada ronda de batalla.\n\n"+
                "Resurgir: Elige a tu líder para usar esta habilidad si no tiene fichas de carga. Quítale 1 ficha de mando y luego elige a un Grave Guard aliado muerto. Resucita a ese luchador y colócalo en un hexágono inicial en territorio aliado. \n\n"+
                "¡Levantad los escudos!: Usa esto inmediatamente después del ataque de un luchador enemigo si el objetivo era un baluarte amigo y la tirada de salvación contenía algun critico. Dale a ese luchador amigo una ficha de guardia.\n\n"+
                "La maldición de Velmorn: Úsala inmediatamente después de que un luchador aliado con una ficha de Resucitado sea asesinado por un atacante adyacente a él. Inflige 1 punto de daño al atacante.\n\n"+
                "Rapidez sorpresa (Una vez por partida): Úsalo en una fase de Poder. Los luchadores aliados tienen +2 a Movimiento en el siguiente turno.",
                "Soulblight Gravelords", "sons_of_velmorn_0");

        // King Velmorn
        insertFighterInternal(db, (int)velmornBandId, "King Velmorn", "sons_of_velmorn_1");

        // Sir Jedran Falseborn
        insertFighterInternal(db, (int)velmornBandId, "Sir Jedran Falseborn", "sons_of_velmorn_2");

        // Marshal Faulk
        insertFighterInternal(db, (int)velmornBandId, "Marshal Faulk", "sons_of_velmorn_3");

        // Helhelm
        insertFighterInternal(db, (int)velmornBandId, "Helhelm", "sons_of_velmorn_4");

        // Thain IV
        insertFighterInternal(db, (int)velmornBandId, "Thain IV", "sons_of_velmorn_5");

        // --- Inserción de The Farstriders ---
        long farstridersBandId = insertBandInternal(db, "The Farstriders",
                "Inspirar: Inmediatamente después del segundo ataque de un luchador aliado en la misma fase de combate y de haber elegido un arma diferente para esos ataques, inspira a ese luchador.\n\n"+
                "Vanguardia: Úsalo inmediatamente después de que un luchador aliado vulnerable inspirado en territorio enemigo haya usado una habilidad básica. Elige una opción:.\n" + //
                                        "- Desinspira y cura a ese luchador. Durante el resto de la ronda de combate, este luchador no puede ser curado ni inspirado.\n" + //
                                        "- Elige un luchador enemigo dentro de 3 hexágonos de este luchador y tira un número de dados de ataque igual al de la ronda de combate. Con una tirada de o más, inflige 1 punto de daño a ese luchador.\n\n"+
                "Explorador de elite (Una vez por partida): Úsala inmediatamente después de tu fase de acción. Empuja a cada luchador aliado un número de hexágonos igual al de la ronda de combate. Esos empujones deben terminar en territorio enemigo.\n\n"+
                "¡Adelante, vanguardia! (Una vez por partida): Úsala inmediatamente después de tu fase de acción. Elige un luchador aliado que no se encuentre en territorio enemigo y que no esté adyacente a ningún luchador enemigo. Ese luchador aliado puede moverse.\n\n"+
                "Detrás de las líneas enemigas (Una vez por partida): Úsalo inmediatamente después de tu fase de acción. Elige un luchador aliado dañado en territorio enemigo. Dale a ese luchador una ficha de guardia.",
                "Stormcast Eternals", "the_farstriders_0");

        // Sanson Farstrider
        insertFighterInternal(db, (int)farstridersBandId, "Sanson Farstrider", "the_farstriders_1");

        // Almeric Eagle-Eye
        insertFighterInternal(db, (int)farstridersBandId, "Almeric Eagle-Eye", "the_farstriders_2");

        // Elias Swiftblade
        insertFighterInternal(db, (int)farstridersBandId, "Elias Swiftblade", "the_farstriders_3");

        // --- Inserción de The Headsmen's Curse ---
        long headsmenBandId = insertBandInternal(db, "The Headsmen's Curse",
                "Inspirar: Después de una fase de Acción, si se ha infligido daño a algún luchador enemigo condenado por el ataque de un luchador amigo, inspira a ese luchador amigo.\n\n"+
                "Juicio y Ejecución: Al comienzo de tu primera fase de Acción en cada ronda de batalla, puedes elegir a un luchador enemigo que se encuentre a 5 hexágonos de un Scriptor of the Sentence amigo para condenarlo durante esa ronda de batalla. Después de que un luchador enemigo condenado sea asesinado, puedes robar un número de cartas de poder igual a la característica de recompensa de ese luchador.\n\n"+
                "Deber eterno (Una vez por partida): Úsalo inmediatamente después de que un luchador enemigo sea asesinado por el arma de tu líder. Elige un luchador aliado muerto. Resucita a ese luchador y colócalo en un hexágono vacío adyacente a tu líder.\n\n"+
                "Afilar la espada (Una vez por partida): Úsala inmediatamente después de elegir un arma como parte del ataque de tu líder si está adyacente a un Sharpener of the Blade aliado. Esa arma tiene Doloroso para ese ataque.\n\n"+
                "Desencarnarse (Una vez por partida, habilidad Basica): Elige un luchador aliado para usar esta habilidad básica. Retira ese luchador del campo de batalla y colócalo en un hexágono vacío.\n\n"+
                "Corte risueña (Una vez por partida): Úsala inmediatamente después de elegir a un luchador enemigo como objetivo de un ataque si ese luchador está flanqueado y/o rodeado. Las tiradas de espadas y martillo son éxitos en esa tirada de ataque.",
                "Nighthaunt", "the_headsmens_curse_0");

        // Wielder of the Blade
        insertFighterInternal(db, (int)headsmenBandId, "Wielder of the Blade", "the_headsmens_curse_1");

        // Bearer of the Block
        insertFighterInternal(db, (int)headsmenBandId, "Bearer of the Block", "the_headsmens_curse_2");

        // Scriptor of the Sentence
        insertFighterInternal(db, (int)headsmenBandId, "Scriptor of the Sentence", "the_headsmens_curse_3");

        // Sharpener of the Blade
        insertFighterInternal(db, (int)headsmenBandId, "Sharpener of the Blade", "the_headsmens_curse_4");

        // --- Inserción de The Shadeborn ---
        long shadebornBandId = insertBandInternal(db, "The Shadeborn",
                "Inspirar: Inmediatamente después de que un luchador aliado ataque a un luchador enemigo dañado, inspira a ese luchador aliado.\n\n"+
                "Marca sombía: Cada vez que realices una tirada de salvación para un luchador aliado en territorio enemigo o en una ficha de cobertura, puedes volver a tirar 1 dado de salvación en esa tirada.\n\n"+
                "Gloomweb Hex (Una vez por partida): Úsalo en un paso de poder. Los luchadores enemigos que estén adyacentes o sobre una ficha de rasgo en el siguiente turno tienen -1 dados de salvación.\n\n"+
                "Lamprea oscura (Una vez por partida): Úsalo en una fase de poder. Elige un luchador enemigo que no sea vulnerable y que se encuentre a 3 hexágonos de un luchador amigo. Infringe 1 punto de daño a ese luchador.\n\n"+
                "Oscuridad impenetrable (Una vez por partida): Úsalo en una fase de poder. Dale a cada luchador amigo una ficha de sombra. Los luchadores con fichas de sombra se tratan como si estuvieran en una ficha de cobertura. Retira todas las fichas de sombra al final de la ronda de combate.\n\n"+
                "Emboscada en las sombras (Una vez por partida): Usa esta habilidad inmediatamente después de que un luchador aliado sea elegido para moverse si ese luchador se encuentra sobre una ficha de cobertura. Retira a ese luchador del campo de batalla y colócalo en un hexágono vacío en territorio enemigo. A continuación, dale a ese luchador una ficha de movimiento.",
                "Daughters of Khaine", "the_shadeborn_0");

        // Slythael Shadestalker
        insertFighterInternal(db, (int)shadebornBandId, "Slythael Shadestalker", "the_shadeborn_1");

        // Drusylla
        insertFighterInternal(db, (int)shadebornBandId, "Drusylla", "the_shadeborn_2");

        // Valyssa
        insertFighterInternal(db, (int)shadebornBandId, "Valyssa", "the_shadeborn_3");

        // Sylarc
        insertFighterInternal(db, (int)shadebornBandId, "Sylarc", "the_shadeborn_4");

        // --- Inserción de The Skinnerkin ---
        long skinnerkinBandId = insertBandInternal(db, "The Skinnerkin",
                "Inspirar: Después de obtener tu tercera ficha de cuarto trasero o las siguientes, inspira a cada luchador aliado. \n\n" +
                "Cortes de primera calidad: Inmediatamente después de que un luchador enemigo flanqueado o rodeado sea asesinado, obtén una ficha de muslo. Inmediatamente después de que un chef aliado realice un ataque con éxito, obtén una ficha de muslo. Si el arma de un luchador aliado tiene la runa, tienes acceso a la siguiente habilidad de arma:\n" + //
                                        "Filete: si el ataque tuvo éxito, obtén una ficha de muslo.\n\n"+
                "Garras afiladas: Úsalo inmediatamente después de que un Carnskyr amigo se mueva. Elige un luchador enemigo en un hexágono por el que haya pasado el Carnskyr y tira un dado de ataque. Con un martillo, inflige 1 punto de daño a ese luchador y obtén una ficha de muslo. Con una espada o un critico, puedes empujar a ese luchador hasta 1 hexágono.\n\n"+
                "Puedes usar 1 de las siguientes habilidades por turno.\n\n"+
                "Prueba de sabor: Úsala inmediatamente después de resolver una habilidad básica para un luchador aliado. Descarta una ficha de muslo y luego inspira a ese luchador.\n\n"+
                "¡Este está arruinado, tráeme otro!: Úsalo inmediatamente después de realizar una tirada de ataque para un luchador aliado. Descarta una ficha de muslo y luego vuelve a tirar 1 dado de ataque en esa tirada de ataque.\n\n"+
                "¡El rey tiene hambre!: Úsalo inmediatamente después de elegir un arma cuerpo a cuerpo como parte de un ataque. Descarta una ficha de muslo. Esa arma tiene Doloroso para ese ataque.\n\n"+
                "Mordisco rápido: Úsalo en una fase de poder. Descarta una ficha de muslo y elige a un luchador aliado. Cura a ese luchador.",
                "Flesh-eater Courts", "the_skinnerkin_0");

        // Gristla Tenderhooke
        insertFighterInternal(db, (int)skinnerkinBandId, "Gristla Tenderhooke", "the_skinnerkin_1");

        // Young Master Kretch
        insertFighterInternal(db, (int)skinnerkinBandId, "Young Master Kretch", "the_skinnerkin_2");

        // Flensemaster Pewdrig
        insertFighterInternal(db, (int)skinnerkinBandId, "Flensemaster Pewdrig", "the_skinnerkin_3");

        // Seddrik the Chain
        insertFighterInternal(db, (int)skinnerkinBandId, "Seddrik the Chain", "the_skinnerkin_4");

        // The Carnskyr
        insertFighterInternal(db, (int)skinnerkinBandId, "The Carnskyr", "the_skinnerkin_5");

        // --- Inserción de The Starblood Stalkers ---
        long starbloodBandId = insertBandInternal(db, "The Starblood Stalkers",
                "Inspirar: Después de una fase de Poder, si tu banda controla 3 o más fichas de tesoro, inspira a cada luchador amigo.\n\n"+
                "Agilidad de los Skink: Inmediatamente después de realizar una tirada de Salvación como parte de un ataque cuerpo a cuerpo para un skink amigo sin fichas de Carga en territorio amigo, puedes volver a tirar 1 dado de Salvación en esa tirada.\n\n"+
                "El adivino (acción Basica): Elige una ficha de rasgo dentro de 3 hexágonos de tu líder. Dale la vuelta a esa ficha de rasgo o inflige 1 punto de daño a un luchador enemigo que se encuentre en esa ficha de rasgo o adyacente a ella. Esta habilidad solo se puede usar una vez por ronda de batalla.\n\n"+
                "El Baluarte Celestial (Una vez por partida): Úsala en tu fase de poder. Los luchadores amigos no pueden ser presioandos hasta el final de la ronda de combate o hasta que algún jugador use una habilidad de Asterismo.\n\n"+
                "El Gran Dragón (Una vez por partida): Úsalo en tu fase de poder. Las armas cuerpo a cuerpo de los luchadores aliados tienen +1 dado de ataque hasta el final de la ronda de combate o hasta que algún jugador use una habilidad Asterismo.\n\n"+
                "El corcel del cazador (Una vez por partida): Úsalo en tu fase de poder. Los luchadores aliados tienen +1 a Movimiento hasta el final de la ronda de combate o hasta que cualquier jugador use una habilidad Asterismo.",
                "Seraphon", "the_starblood_stalkers_0");

        // Kixi-Taka the Diviner
        insertFighterInternal(db, (int)starbloodBandId, "Kixi-Taka the Diviner", "the_starblood_stalkers_1");

        // Klaq-Trok
        insertFighterInternal(db, (int)starbloodBandId, "Klaq-Trok", "the_starblood_stalkers_2");

        // Otapatl
        insertFighterInternal(db, (int)starbloodBandId, "Otapatl", "the_starblood_stalkers_3");

        // Tok
        insertFighterInternal(db, (int)starbloodBandId, "Tok", "the_starblood_stalkers_4");

        // Xepic
        insertFighterInternal(db, (int)starbloodBandId, "Xepic", "the_starblood_stalkers_5");

        // Huachi
        insertFighterInternal(db, (int)starbloodBandId, "Huachi", "the_starblood_stalkers_6");

        // --- Inserción de Thundrik's Profiteers ---
        long thundrikBandId = insertBandInternal(db, "Thundrik's Profiteers",
                "Inspirar: Después de puntuar una carta de objetivo, si hay un líder aliado en el campo de batalla, puedes elegir un luchador aliado. Inspira a ese luchador.\n\n"+
                "ES MÍO, JUSTO Y LEGÍTIMO: Mientras un luchador aliado tenga una ficha de tesoro y no haya luchadores enemigos adyacentes a él, ese luchador no puede ser empujado por habilidades enemigas.\n\n"+
                "Aislamiento atmosférico: Úsalo inmediatamente después de tu fase de Acción si un líder aliado ha usado alguna habilidad básica en esa fase. Hasta el final de la siguiente fase de Acción, cada vez que un luchador enemigo sea colocado, empujado o entre en un hexágono adyacente a ese líder, inflige 1 punto de daño a ese luchador.\n\n"+
                "Balas de éter personalizadas (Una vez por partida): Úsala inmediatamente después de elegir un arma a distancia como parte de un ataque. Esa arma tiene +1 de alcance y +1 dado de ataque para ese ataque.\n\n"+
                "Lluvia de disparos de éter (Una vez por partida): Úsalo inmediatamente después del ataque a distancia de un luchador aliado si no tiene fichas de movimiento o carga. Ese luchador ataca de nuevo con la misma arma. El objetivo de ese ataque debe ser diferente al objetivo del primer ataque y debe estar a 2 hexágonos del primer objetivo.\n\n"+
                "Según el Código (Una vez por partida): Úsalo en una fase de Poder. Elige un luchador amigo. Empuja a ese luchador hasta 3 hexágonos. Ese empujón debe terminar en una ficha de característica.",
                "Kharadron Overlords", "thundriks_profiteers_0");

        // Bjorgen Thundrik
        insertFighterInternal(db, (int)thundrikBandId, "Bjorgen Thundrik", "thundriks_profiteers_1");

        // Dead-Eye Lund
        insertFighterInternal(db, (int)thundrikBandId, "Dead-Eye Lund", "thundriks_profiteers_2");

        // Enrik Ironhail
        insertFighterInternal(db, (int)thundrikBandId, "Enrik Ironhail", "thundriks_profiteers_3");

        // Khazgan Drakkskewer
        insertFighterInternal(db, (int)thundrikBandId, "Khazgan Drakkskewer", "thundriks_profiteers_4");

        // Garodd Alensen
        insertFighterInternal(db, (int)thundrikBandId, "Garodd Alensen", "thundriks_profiteers_5");

        // --- Inserción de Xandire's Truthseekers ---
        long xandireBandId = insertBandInternal(db, "Xandire's Truthseekers",
                "Inspirar: Después de un paso de poder, si algún luchador amigo (excepto Taros) muere, inspira a cada luchador amigo.\n\n"+
                "Ojo de raptor: Puedes volver a tirar inmediatamente 1 dado de ataque en una tirada de ataque si el objetivo está en una ficha de rasgo a 2 hexágonos de un Taros amigo.\n\n"+
                "Golpe del buscador: Usa esto inmediatamente después de un ataque cuerpo a cuerpo exitoso de un luchador amigo si el objetivo estaba en una ficha de rasgo. Da la vuelta a esa ficha de rasgo. Solo puedes usar esta habilidad una vez por ronda de batalla.\n\n"+
                "Llama de luz (Una vez por partida): Úsala en tu fase de poder si tu líder está en el campo de batalla. Las armas de los luchadores enemigos tienen -1 dado de ataque en el siguiente turno.\n\n"+
                "Pureza ardiente (Una vez por partida): Úsala inmediatamente después de que tu líder sea asesinado. Elige hasta 2 luchadores amigos y cura a cada uno de ellos, o elige 1 luchador amigo y cúralo dos veces.\n\n"+
                "Fuerza ardiente (Una vez por partida): Úsala inmediatamente después de que un Dhoraz aliado sea asesinado. Elige hasta 2 luchadores aliados y empuja a cada uno 1 hexágono, o elige 1 luchador aliado y empújalo hasta 2 hexágonos.\n\n"+
                "Venganza ardiente (Una vez por partida): Úsala inmediatamente después de que un Luxa aliado sea asesinado, antes de retirarlo del campo de batalla. Ese luchador ataca con un arma a distancia (excluyendo las mejoras).",
                "Stormcast Eternals", "xandires_truthseekers_0");

        // Calthia Xandire
        insertFighterInternal(db, (int)xandireBandId, "Calthia Xandire", "xandires_truthseekers_1");

        // Dhoraz Giant-fell
        insertFighterInternal(db, (int)xandireBandId, "Dhoraz Giant-fell", "xandires_truthseekers_2");

        // Luxa Stormrider
        insertFighterInternal(db, (int)xandireBandId, "Luxa Stormrider", "xandires_truthseekers_3");

        // Taros
        insertFighterInternal(db, (int)xandireBandId, "Taros", "xandires_truthseekers_4");

        // --- Inserción de Zondara's Gravebreakers ---
        long zondaraBandId = insertBandInternal(db, "Zondara's Gravebreakers",
                "Inspirar: Inmediatamente después de dar una ficha de Levantado a un luchador aliado, inspira a ese luchador. Inmediatamente después de dar a un luchador aliado destinado su segunda mejora o las siguientes, inspira al otro luchador aliado destinado.\n\n"+
                "Rompetumbas: Úsalo inmediatamente después de que un esbirro aliado indague. Ese luchador puede desenterrar o un líder aliado puede exhumar.\n\n"+
                "Destinado: Mientras un luchador aliado destinado sea asesinado, el otro luchador aliado destinado tiene +1 a Mover y sus armas tienen +1 a los dados de Ataque.\n\n"+
                "Amor eterno: Úsalo en un paso de Poder. Elige 2 luchadores aliados destinados. Cura a 1 de esos luchadores y empuja al otro luchador hasta 3 hexágonos hacia el otro luchador.\n\n"+
                "Exhumar: Elige un secuaz aliado muerto. Resucita a ese luchador y colócalo en un hexágono adyacente al luchador aliado que lo resucitó. Solo puedes usar esta habilidad si se elige al resolver la habilidad Rompetumbas.\n\n"+
                "Desenterrar: Roba un número de cartas de poder igual al número de luchadores aliados muertos. Solo puedes usar esta habilidad si se elige al resolver la habilidad Rompetumbas.",
                "Soulblight Gravelords", "zondaras_gravebreakers_0");

        // Zondara Rivenheart
        insertFighterInternal(db, (int)zondaraBandId, "Zondara Rivenheart", "zondaras_gravebreakers_1");

        // Lost Ferlain
        insertFighterInternal(db, (int)zondaraBandId, "Lost Ferlain", "zondaras_gravebreakers_2");

        // Pikk
        insertFighterInternal(db, (int)zondaraBandId, "Pikk", "zondaras_gravebreakers_3");

        // Toyle
        insertFighterInternal(db, (int)zondaraBandId, "Toyle", "zondaras_gravebreakers_4");

        // Cracktomb
        insertFighterInternal(db, (int)zondaraBandId, "Cracktomb", "zondaras_gravebreakers_5");

        // --- Inserción de Skittershank's Clawpack ---
        long skittershankBandId = insertBandInternal(db, "Skittershank's Clawpack",
                "Inspirar: Después de una fase de Acción, si un líder enemigo muere o tiene 3 o más fichas de daño, inspira a cada luchador amigo. \n\n" +
                "Marcado para morir: Úsalo inmediatamente después de realizar una tirada de Ataque para un luchador amigo si el objetivo es un líder enemigo. Vuelve a tirar 1 dado de Ataque en esa tirada de Ataque. \n\n" +
                "Trampa de puas y lazos: Inmediatamente después de que un luchador amigo realice con éxito un ataque cuerpo a cuerpo, dale al objetivo una ficha de púa. Inmediatamente después de que un luchador enemigo con fichas de púa se mueva, inflige 1 punto de daño a ese luchador y luego retira sus fichas de púa. Retira todas las fichas de púa de los luchadores enemigos al final de la ronda de combate. \n\n" +
                "Bombas de humo (Una ves por partida): Inmediatamente después del ataque de un luchador aliado, dale una ficha de tambaleo a cada luchador enemigo adyacente al atacante. \n\n" +
                "Las mandíbulas se cierran (Una vez por partida): Usa esto en tu fase de poder. Elige un líder enemigo en territorio enemigo. Empuja a ese luchador 1 hexágono. \n\n" +
                "Desenfoque brillante (Una vez por partida): Úsala en tu fase de poder. En el siguiente turno, los asesinos amigos no pueden ser elegidos como objetivo de ataques o ardides.",
                "Skaven", "skittershanks_clawpack_0");

        // Slynk Skittershank
        insertFighterInternal(db, (int)skittershankBandId, "Slynk Skittershank", "skittershanks_clawpack_1");

        // Snyp Padpaw
        insertFighterInternal(db, (int)skittershankBandId, "Snyp Padpaw", "skittershanks_clawpack_2");

        // Kreep Kinwhisper
        insertFighterInternal(db, (int)skittershankBandId, "Kreep Kinwhisper", "skittershanks_clawpack_3");

        // Krowch't
        insertFighterInternal(db, (int)skittershankBandId, "Krowch't", "skittershanks_clawpack_4");

        // Skulck
        insertFighterInternal(db, (int)skittershankBandId, "Skulck", "skittershanks_clawpack_5");

        // --- Inserción de Hrothgorn's Mantrappers ---
        long hrothgornBandId = insertBandInternal(db, "Hrothgorn's Mantrappers",
                "Inspirar: Después de que un luchador enemigo sea asesinado por un ataque cuerpo a cuerpo realizado por tu líder, inspira a cada luchador amigo.\n\n"+
                "Emboscada de nocheEterna: Cuando despliegues un Thrafnir amigo, puedes colocarlo en un hexágono vacío en territorio enemigo que no sea un hexágono de inicio y que no contenga una ficha de rasgo.\n\n"+
                "Trampas devoradoras: Al comienzo de la primera fase de acción de la primera ronda de combate, coloca una miniatura de trampa amiga en un hexágono vacío en territorio amigo.\n" + //
                                        "Si un luchador es colocado, empujado o entra en un hexágono que contenga una trampa amiga, inflige 2 puntos de daño a ese luchador y luego retira la trampa del campo de batalla.\n\n"+
                "Más trampas: Úsalo en tu fase de Poder si no hay trampas amigas en el campo de batalla. Coloca una trampa amiga en un hexágono vacío adyacente a un Gnoblar amigo.\n\n"+
                "Competencia sorprendente: Usa esto inmediatamente después de que un Gnoblar aliado se mueva si ese luchador está adyacente a una trampa aliada. Retira esa trampa del campo de batalla y colócala en un lugar diferente.",
                "Destruccion", "hrothgorns_mantrappers_0");

        // Hrothgorn
        insertFighterInternal(db, (int)hrothgornBandId, "Hrothgorn", "hrothgorns_mantrappers_1");

        // Thrafnir
        insertFighterInternal(db, (int)hrothgornBandId, "Thrafnir", "hrothgorns_mantrappers_2");

        // Bushwakka
        insertFighterInternal(db, (int)hrothgornBandId, "Bushwakka", "hrothgorns_mantrappers_3");

        // Quiv
        insertFighterInternal(db, (int)hrothgornBandId, "Quiv", "hrothgorns_mantrappers_4");

        // Luggit and Thwak
        insertFighterInternal(db, (int)hrothgornBandId, "Luggit and Thwak", "hrothgorns_mantrappers_5");
    }

    private long insertBandInternal(SQLiteDatabase db, String name, String description, String faction, String imageResource) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_FACTION, faction);
        values.put(COLUMN_IMAGE_RESOURCE, imageResource);
        return db.insert(TABLE_BANDS, null, values);
    }

    private long insertRivalDeckInternal(SQLiteDatabase db, String name, String description, Integer bandId, String type, String specialRules) {
        if (bandId != null && bandId == -1) {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_RIVAL_DECK_NAME, name);
        values.put(COLUMN_RIVAL_DECK_DESCRIPTION, description);
        if (bandId != null) values.put(COLUMN_RIVAL_DECK_BAND_ID, bandId);
        values.put(COLUMN_RIVAL_DECK_TYPE, type);
        if (specialRules != null) values.put(COLUMN_RIVAL_DECK_SPECIAL_RULES, specialRules);
        return db.insert(TABLE_RIVAL_DECKS, null, values);
    }

    private long insertCardInternal(SQLiteDatabase db, String name, String type, String subtype, int number, int rivalDeckId, Integer glory, String effect, String cost, String image) {
        if (rivalDeckId == -1) {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_CARD_NAME, name);
        values.put(COLUMN_CARD_TYPE, type);
        if (subtype != null) values.put(COLUMN_CARD_SUBTYPE, subtype);
        values.put(COLUMN_CARD_NUMBER, number);
        values.put(COLUMN_CARD_RIVAL_DECK_ID, rivalDeckId);
        if (glory != null) values.put(COLUMN_CARD_GLORY, glory);
        if (effect != null) values.put(COLUMN_CARD_EFFECT, effect);
        if (cost != null) values.put(COLUMN_CARD_COST_RESTRICTION, cost);
        if (image != null) values.put(COLUMN_CARD_IMAGE, image);
        return db.insert(TABLE_CARDS, null, values);
    }
    
    // --- Métodos Públicos para la App ---

    public long addRivalDeck(String name, String description, Integer bandId, String type, String specialRules) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RIVAL_DECK_NAME, name);
        values.put(COLUMN_RIVAL_DECK_DESCRIPTION, description);
        if (bandId != null) values.put(COLUMN_RIVAL_DECK_BAND_ID, bandId);
        values.put(COLUMN_RIVAL_DECK_TYPE, type);
        if (specialRules != null) values.put(COLUMN_RIVAL_DECK_SPECIAL_RULES, specialRules);
        long id = db.insert(TABLE_RIVAL_DECKS, null, values);
        db.close();
        return id;
    }

    // Métodos para insertar Luchadores y Armas
    public long addFighter(int bandId, String name, String image) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIGHTER_BAND_ID, bandId);
        values.put(COLUMN_FIGHTER_NAME, name);
        values.put(COLUMN_FIGHTER_IMAGE, image);
        long id = db.insert(TABLE_FIGHTERS, null, values);
        db.close();
        return id;
    }



    private long insertFighterInternal(SQLiteDatabase db, int bandId, String name, String image) {
        if (bandId == -1) {
            return -1;
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_FIGHTER_BAND_ID, bandId);
        values.put(COLUMN_FIGHTER_NAME, name);
        values.put(COLUMN_FIGHTER_IMAGE, image);
        return db.insert(TABLE_FIGHTERS, null, values);
    }



    public long createUserDeck(String name, int bandId, int rivalDeck1Id, Integer rivalDeck2Id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_DECK_NAME, name);
        values.put(COLUMN_USER_DECK_BAND_ID, bandId);
        values.put(COLUMN_USER_DECK_RIVAL_1, rivalDeck1Id);
        if (rivalDeck2Id != null) values.put(COLUMN_USER_DECK_RIVAL_2, rivalDeck2Id);
        long id = db.insert(TABLE_USER_DECKS, null, values);
        db.close();
        return id;
    }

    // Obtener todas las bandas (existente)
    public List<Band> getAllBands() {
        List<Band> bandList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_BANDS;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                String faction = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FACTION));
                String imageResourceName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_RESOURCE));

                int imageResId = context.getResources().getIdentifier(imageResourceName, "drawable", context.getPackageName());
                if (imageResId == 0) {
                    imageResId = R.drawable.ic_launcher_background; 
                }

                Band band = new Band(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)), imageResId, name, description, faction);
                bandList.add(band);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bandList;
    }

    // Obtener bandas por facción (con imagen del líder)
    public List<Band> getBandsByFaction(String faction) {
        List<Band> bandList = new ArrayList<>();
        List<String> args = new ArrayList<>();
        String selection;

        if ("Caos".equalsIgnoreCase(faction)) {
            selection = "b." + COLUMN_FACTION + " IN (?, ?, ?, ?, ?, ?, ?, ?)";
            args.add("Caos");
            args.add("Chaos");
            args.add("Slaves to Darkness");
            args.add("Disciples of Tzeentch");
            args.add("Blades of Khorne");
            args.add("Skaven");
            args.add("Beasts of Chaos");
            args.add("Maggotkin of Nurgle");
        } else if ("Orden".equalsIgnoreCase(faction)) {
            selection = "b." + COLUMN_FACTION + " IN (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            args.add("Orden");
            args.add("Order of Azyr");
            args.add("Sylvaneth");
            args.add("Stormcast Eternals");
            args.add("Lumineth Realm-lords");
            args.add("Daughters of Khaine");
            args.add("Seraphon");
            args.add("Kharadron Overlords");
            args.add("Idoneth Deepkin");
            args.add("Fyreslayers");
            args.add("Cities of Sigmar");
        } else if ("Muerte".equalsIgnoreCase(faction)) {
            selection = "b." + COLUMN_FACTION + " IN (?, ?, ?, ?, ?)";
            args.add("Muerte");
            args.add("Ossiarch Bonereapers");
            args.add("Soulblight Gravelords");
            args.add("Nighthaunt");
            args.add("Flesh-eater Courts");
        } else if ("Destruccion".equalsIgnoreCase(faction) || "Destrucción".equalsIgnoreCase(faction)) {
            selection = "b." + COLUMN_FACTION + " IN (?, ?, ?, ?, ?, ?)";
            args.add("Destruccion");
            args.add("Gloomspite Gitz");
            args.add("Ogor Mawtribes");
            args.add("Ironjawz");
            args.add("Bonesplitterz");
            args.add("Kruleboyz");
        } else {
            selection = "b." + COLUMN_FACTION + " = ?";
            args.add(faction);
        }

        // Consulta para obtener la banda y la imagen del primer luchador (líder)
        String selectQuery = "SELECT b.*, " +
                "(SELECT f." + COLUMN_FIGHTER_IMAGE + " FROM " + TABLE_FIGHTERS + " f WHERE f." + COLUMN_FIGHTER_BAND_ID + " = b." + COLUMN_ID + " ORDER BY f." + COLUMN_FIGHTER_ID + " ASC LIMIT 1) as leader_image " +
                "FROM " + TABLE_BANDS + " b WHERE " + selection;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, args.toArray(new String[0]));

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION));
                String fetchedFaction = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FACTION));
                
                String leaderImageName = null;
                int leaderImageIdx = cursor.getColumnIndex("leader_image");
                if (leaderImageIdx != -1) {
                    leaderImageName = cursor.getString(leaderImageIdx);
                }
                
                String bandImageName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_IMAGE_RESOURCE));

                int bandResId = 0;
                if (bandImageName != null) {
                    bandResId = context.getResources().getIdentifier(bandImageName, "drawable", context.getPackageName());
                }
                
                if (bandResId == 0) {
                    // Try to find a default image or just use 0
                    int defaultId = context.getResources().getIdentifier("ic_launcher_background", "drawable", context.getPackageName());
                    if (defaultId != 0) {
                        bandResId = defaultId;
                    }
                }

                int leaderResId = 0;
                if (leaderImageName != null) {
                    leaderResId = context.getResources().getIdentifier(leaderImageName, "drawable", context.getPackageName());
                }
                if (leaderResId == 0) {
                    leaderResId = bandResId; // Fallback si no hay líder
                }

                Band band = new Band(id, bandResId, leaderResId, name, description, fetchedFaction);
                bandList.add(band);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return bandList;
    }

    // Obtener todas las imágenes de luchadores de una banda
    public List<Integer> getFighterImagesForBand(int bandId) {
        List<Integer> images = new ArrayList<>();
        String selectQuery = "SELECT " + COLUMN_FIGHTER_IMAGE + " FROM " + TABLE_FIGHTERS + " WHERE " + COLUMN_FIGHTER_BAND_ID + " = ?";
        
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{String.valueOf(bandId)});
        
        if (cursor.moveToFirst()) {
            do {
                String imgName = cursor.getString(0);
                int imgId = context.getResources().getIdentifier(imgName, "drawable", context.getPackageName());
                if (imgId != 0) {
                    images.add(imgId);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return images;
    }

    // --- Métodos para el DeckBuilder ---

    public List<RivalDeck> getAllRivalDecks() {
        List<RivalDeck> decks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_RIVAL_DECKS, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RIVAL_DECK_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RIVAL_DECK_NAME));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RIVAL_DECK_DESCRIPTION));
                int bandId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_RIVAL_DECK_BAND_ID));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RIVAL_DECK_TYPE));

                decks.add(new RivalDeck(id, name, description, bandId, type));
            } while (cursor.moveToNext());
        }
        cursor.close();
        // db.close(); // Keep open or rely on helper management
        return decks;
    }

    public List<Card> getCardsForDecks(int deckId1, int deckId2) {
        List<Card> cards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        // Query para obtener cartas de ambos mazos
        String query = "SELECT * FROM " + TABLE_CARDS + " WHERE " + COLUMN_CARD_RIVAL_DECK_ID + " = ? OR " + COLUMN_CARD_RIVAL_DECK_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(deckId1), String.valueOf(deckId2)});

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_NAME));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_TYPE));
                String subtype = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_SUBTYPE));
                int number = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_NUMBER));
                int deckId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_RIVAL_DECK_ID));
                int glory = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_GLORY));
                String effect = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_EFFECT));
                String cost = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_COST_RESTRICTION));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_IMAGE));

                cards.add(new Card(id, name, type, subtype, number, deckId, glory, effect, cost, image));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cards;
    }

    public long saveUserDeck(String name, int rival1Id, int rival2Id, List<Integer> cardIds) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        long userDeckId = -1;

        try {
            // 1. Crear el mazo de usuario
            ContentValues deckValues = new ContentValues();
            deckValues.put(COLUMN_USER_DECK_NAME, name);
            // Asumimos 0 o null para band_id por ahora si es genérico, o podríamos pasar el ID de la banda si se supiera.
            // Para simplificar, lo dejamos en NULL para no violar la restricción de clave foránea
            deckValues.putNull(COLUMN_USER_DECK_BAND_ID); 
            deckValues.put(COLUMN_USER_DECK_RIVAL_1, rival1Id);
            deckValues.put(COLUMN_USER_DECK_RIVAL_2, rival2Id);

            userDeckId = db.insert(TABLE_USER_DECKS, null, deckValues);

            if (userDeckId != -1) {
                // 2. Insertar las cartas seleccionadas
                for (int cardId : cardIds) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(COLUMN_CONTENT_USER_DECK_ID, userDeckId);
                    contentValues.put(COLUMN_CONTENT_CARD_ID, cardId);
                    db.insert(TABLE_USER_DECK_CONTENT, null, contentValues);
                }
                db.setTransactionSuccessful();
            }
        } finally {
            db.endTransaction();
        }
        return userDeckId;
    }

    // Métodos para recuperar Mazos de Usuario

    public List<UserDeck> getUserDecks() {
        List<UserDeck> decks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        String[] columns = {
            COLUMN_USER_DECK_ID,
            COLUMN_USER_DECK_NAME,
            COLUMN_USER_DECK_BAND_ID,
            COLUMN_USER_DECK_RIVAL_1,
            COLUMN_USER_DECK_RIVAL_2
        };

        Cursor cursor = db.query(TABLE_USER_DECKS, columns, null, null, null, null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_DECK_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_USER_DECK_NAME));
                    
                    int bandIdIndex = cursor.getColumnIndexOrThrow(COLUMN_USER_DECK_BAND_ID);
                    Integer bandId = cursor.isNull(bandIdIndex) ? null : cursor.getInt(bandIdIndex);
                    
                    int rival1Id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_USER_DECK_RIVAL_1));
                    
                    int rival2IdIndex = cursor.getColumnIndexOrThrow(COLUMN_USER_DECK_RIVAL_2);
                    int rival2Id = cursor.isNull(rival2IdIndex) ? 0 : cursor.getInt(rival2IdIndex);

                    decks.add(new UserDeck(id, name, bandId, rival1Id, rival2Id));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return decks;
    }

    public List<Card> getCardsForUserDeck(int userDeckId) {
        List<Card> cards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Consulta JOIN para obtener las cartas asociadas al mazo de usuario
        String query = "SELECT c.* FROM " + TABLE_CARDS + " c " +
                       "INNER JOIN " + TABLE_USER_DECK_CONTENT + " udc " +
                       "ON c." + COLUMN_CARD_ID + " = udc." + COLUMN_CONTENT_CARD_ID + " " +
                       "WHERE udc." + COLUMN_CONTENT_USER_DECK_ID + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userDeckId)});

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_ID));
                    String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_NAME));
                    String type = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_TYPE));
                    String subtype = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_SUBTYPE));
                    int number = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_NUMBER));
                    int rivalDeckId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_RIVAL_DECK_ID));
                    int glory = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CARD_GLORY));
                    String effect = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_EFFECT));
                    String cost = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_COST_RESTRICTION));
                    String image = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CARD_IMAGE));

                    // Usamos el constructor de Card existente
                    cards.add(new Card(id, name, type, subtype, number, rivalDeckId, glory, effect, cost, image));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return cards;
    }

    public boolean deleteUserDeck(int userDeckId) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean success = false;
        db.beginTransaction();
        try {
            // Eliminar contenido del mazo (cartas)
            db.delete(TABLE_USER_DECK_CONTENT, COLUMN_CONTENT_USER_DECK_ID + " = ?", new String[]{String.valueOf(userDeckId)});
            
            // Eliminar el mazo
            int rowsAffected = db.delete(TABLE_USER_DECKS, COLUMN_USER_DECK_ID + " = ?", new String[]{String.valueOf(userDeckId)});
            
            if (rowsAffected > 0) {
                success = true;
                db.setTransactionSuccessful();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
        return success;
    }
}
