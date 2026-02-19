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
    private static final int DATABASE_VERSION = 22; // Incrementado a 22 para asegurar regeneración limpia tras fixes de FK

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


        // Insertar Mazos Rivales proporcionados por el usuario
        // Asumimos que son universales a menos que se indique lo contrario.
        // El usuario proporcionará las reglas especiales y cartas más adelante.
        
        // (Eliminados duplicados vacíos que estaban aquí)

        // --- Inserción de The Wurmspat (Band, Fighters, Stats, Weapons) ---
        long wurmspatId = insertBandInternal(db, "The Wurmspat",
                "Inspirar: Si 3 o más luchadores enemigos están eliminados o heridos, Inspira a cada luchador amigo. Después de que un luchador enemigo con alguna ficha de bilis sea eliminado por un luchador amigo, Inspira a ese luchador.",
                "Caos", "the_wurmspat_0");

        // Fecula
        insertFighterInternal(db, (int)wurmspatId, "Fecula", "the_wurmspat_1");

        // Ghulgoch
        insertFighterInternal(db, (int)wurmspatId, "Ghulgoch", "the_wurmspat_2");

        // Sepsimus
        insertFighterInternal(db, (int)wurmspatId, "Sepsimus", "the_wurmspat_3");

        // --- Perfiles INSPIRADOS ---
        // Fecula Inspirada

        // Ghulgoch Inspirado

        // Sepsimus Inspirado

        // --- Inserción de Spiteclaw's Swarm (Band) ---
        long spiteclawId = insertBandInternal(db, "Spiteclaw's Swarm",
                "Inspirar: Inmediatamente después de resolver una Ardid que eligió a un luchador amigo, Inspira a ese luchador. \n" +
                "Enjambre: Elige un líder amigo para usar esta habilidad. Elige un esbirro amigo eliminado. Resucita a ese luchador y colócalo en un hexágono inicial vacío, luego dale una ficha de Resurrección. \n" +
                "Manada Maquinadora: Las armas de los maquinadores amigos tienen Heridas Graves Críticas mientras el objetivo está Flanqueado o Rodeado. \n" +
                "Promoción Inoportuna: Mientras Skritch amigo esté eliminado, Krrk es un líder.",
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

        // --- FORMAS INSPIRADAS ---

        // Skritch (Inspirado)

        // Krrk (Inspirado)

        // Lurking Skaven (Inspirado)

        // Hungering Skaven (Inspirado)

        // Festering Skaven (Inspirado)

        // --- Inserción de The Dread Pageant (Band) ---
        long dreadPageantId = insertBandInternal(db, "The Dread Pageant",
                "Inspirar: Después de un paso de Acción, si todos los luchadores amigos están heridos, o si hay más luchadores heridos que ilesos, Inspira a cada luchador amigo. \n" +
                "Velocidad de Slaanesh: Los luchadores amigos heridos e Inspirados tienen +1 al Movimiento mientras usan habilidades de Carga. \n" +
                "Dolores Crueles: Usa esto en un paso de Poder. Elige un luchador que controle un objetivo. Inflige 1 daño a ese luchador. \n" +
                "Rápido como el Deseo: Usa esto en un paso de Poder. Empuja a cada luchador amigo herido 1 hexágono. \n" +
                "Ahogarse en Dolor: Usa esto en un paso de Poder. Dale a cada luchador amigo herido una ficha de Guardia. \n" +
                "Arrogancia Burda: Usa esto inmediatamente después de hacer una tirada de Ataque para un luchador amigo herido si esa tirada no contiene éxitos. Puedes volver a tirar cualquier dado de Ataque de esa tirada.",
                "Caos", "the_dread_pageant_0");

        // Vasillac (Líder)
        insertFighterInternal(db, (int)dreadPageantId, "Vasillac", "the_dread_pageant_1");

        // Slakeslash
        insertFighterInternal(db, (int)dreadPageantId, "Slakeslash", "the_dread_pageant_2");

        // Glissete
        insertFighterInternal(db, (int)dreadPageantId, "Glissete", "the_dread_pageant_3");

        // Hadzu
        insertFighterInternal(db, (int)dreadPageantId, "Hadzu", "the_dread_pageant_4");

        // --- FORMAS INSPIRADAS ---

        // Vasillac (Inspirado)

        // Slakeslash (Inspirado)

        // Glissete (Inspirado)

        // Hadzu (Inspirado)




        // --- Inserción de The Gnarlspirit Pack (Band) ---
        long gnarlspiritBandId = insertBandInternal(db, "The Gnarlspirit Pack",
                "Inspirar: Inmediatamente después de retirar una ficha de espíritu de un luchador amigo, Inspira a ese luchador. \n" +
                "Desinspirar: Inmediatamente después de dar una ficha de espíritu a un luchador amigo Inspirado, Desinspira a ese luchador. \n" +
                "Fichas de Espíritu: Inmediatamente después de elegir un luchador amigo para usar una habilidad Principal, puedes retirar las fichas de espíritu de ese luchador. Después de resolverla, puedes darle una ficha de espíritu. Los luchadores con fichas de espíritu no pueden Inspirarse ni controlar objetivos. \n" +
                "Sarrakkar Desatado: +2 al Movimiento mientras tenga alguna ficha de espíritu. \n" +
                "Gorl Desatado: +1 a la Defensa mientras tenga alguna ficha de espíritu. \n" +
                "Kheira Desatada: Las armas cuerpo a cuerpo tienen +1 Dado de ataque mientras tenga alguna ficha de espíritu. \n" +
                "Lupan Desatado: Las tiradas de Medio Apoyo cuentan como éxitos mientras tenga alguna ficha de espíritu. \n" +
                "En Control (Una vez por partida): Paso de Poder. Si hay luchadores amigos Inspirados, empuja a cada luchador amigo No Inspirado hasta 1 hexágono. \n" +
                "Autocontrol (Una vez por partida): Paso de Poder. Elige un luchador amigo No Inspirado. Retira sus fichas de espíritu y dale una ficha de Guardia. \n" +
                "Instintos Aflorados (Una vez por partida): Paso de Poder. Elige un luchador amigo. Las tiradas de Defensa no pueden verse afectadas por Romper o Atrapares en el siguiente turno.",
                "Caos", "gnarlspirit_pack_0");

        // Sarrakkar (Líder)
        insertFighterInternal(db, (int)gnarlspiritBandId, "Sarrakkar", "gnarlspirit_pack_1");

        // Gorl
        insertFighterInternal(db, (int)gnarlspiritBandId, "Gorl", "gnarlspirit_pack_2");

        // Kheira
        insertFighterInternal(db, (int)gnarlspiritBandId, "Kheira", "gnarlspirit_pack_3");

        // Lupan
        insertFighterInternal(db, (int)gnarlspiritBandId, "Lupan", "gnarlspirit_pack_4");

        // --- FORMAS INSPIRADAS (The Gnarlspirit Pack) ---
        // Sarrakkar (Inspirado)

        // Gorl (Inspirado)

        // Kheira (Inspirado)

        // Lupan (Inspirado)



        // --- Inserción de Kamandora's Blades (Band) ---
        long kamandoraBandId = insertBandInternal(db, "Kamandora's Blades",
                "Inspirar: Inmediatamente después de un paso de Poder, Inspira a cada luchador amigo adyacente a la calavera digna enemiga. Inmediatamente después de que la calavera digna enemiga sea eliminada por un luchador amigo, Inspira a ese luchador. \n" +
                "Una Calavera Digna: Al inicio del primer paso de Acción de cada ronda, elige un luchador enemigo para ser la calavera digna hasta el final de la ronda. \n" +
                "Peregrinaje Sangriento: Después de elegir la calavera digna, empuja a cada luchador amigo 1 hexágono más cerca de ella. \n" +
                "¡Sangre para Khorne! (Tajo): Las armas con Tajo dan una ficha de sangrado al dañar. Si un enemigo con ficha de sangrado usa una habilidad Principal, inflige 1 daño y retira la ficha de sangrado. \n" +
                "Llamar a la Persecución (Una vez por partida): Paso de Poder, en lugar de jugar carta, empuja a un esbirro amigo hasta 4 hexágonos para estar adyacente al líder.",
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
                "Inspire: At the start of your turn, Inspire each friendly fighter that is adjacent to any enemy fighters. \n" +
                "Wave of Terror: If target is adjacent to another friendly fighter with a Charge token, target is Surrounded. \n" +
                "Soul Warden: Pick Varclav to push up to 2 friendly minions up to 2 hexes. Then give them a Charge token (once per round). \n" +
                "Mugged: After enemy is picked to be mugged, place a non-leader friendly fighter adjacent to them and give that fighter a Charge token. \n" +
                "Paired Thugs / No Room at the Top / Reprisals / Leave Well Alone: Various conditions to pick an enemy to be mugged (once per game each).",
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
                "Inspire: After the last Action step in a round, Inspire X friendly fighters, where X is the combined Bounty of slain enemies and enemies in enemy/neutral territory. \n" +
                "In the Name of the King!: Power step (leader on board). Either Inspire a friendly fighter or Raise a slain fighter to an empty edge hex. (Both if underdog). \n" +
                "Delusions (Pick 1 per battle round): \n" +
                "- Defenders of the Realm: Re-roll 1 Save dice in friendly territory. \n" +
                "- The Royal Hunt: +1 Attack dice for melee weapons if target is damaged. \n" +
                "- To the Walls!: Can use 'In the Name of the King!' twice this round.",
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
                "Inspire: At the end of each battle round, Inspire each friendly fighter that has no hunger tokens. \n" +
                "The Curse: Start with 2 hunger tokens. Gain 1 at start of rounds 2 and 3. \n" +
                "The Hunger: Successful attack removes 1 hunger token (all if target slain). \n" +
                "Bloodthirst: While having 3+ hunger tokens, +1 Move but Guard tokens have no effect. \n" +
                "Bestial Transformation (Once per game): Discard upgrades for +1 Attack dice (+1 Move if bloodthirsty) until end of round. \n" +
                "Vampiric Might (Once per game): Bloodthirsty attack gets Cleave and Ensnare. \n" +
                "Teneborous Form (Once per game): Inspired fighter being attacked has 3 Dodge for that attack. \n" +
                "Dark Transfusion (Once per game): Inflict 1 damage to enemy within 1 hex (2 if inspired) and heal 1.",
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
                "Inspire: Friendly minions begin the game Inspired. After an enemy is slain by a minion, Inspire Deintalos and Marcov. \n" +
                "Dynamic Enhancer: Conductive fighters have +X Move (X = number of slain conductive fighters). \n" +
                "Overload: Conductive fighters cannot hold treasure. Melee weapons have Grievous against targets with Stagger tokens. \n" +
                "Dynamic Surge: Re-roll 1 Attack dice for conductive fighters if another conductive fighter is adjacent to the attacker. \n" +
                "Puppeteer (Once per round): Marcov can use a Core ability or Raise a slain Regulus adjacent to him. \n" +
                "Danse Dynamic (Once per game): Deintalos grants Move or Attack to all conductive fighters, then can Raise one with 1 damage.",
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



        String countdownRules = "While using this Rivals deck, during the Muster Warbands step of 'Setting Up', place a generic token on the 1st step on the countdown card included in this deck. This token is your Cataclysm tracker.\n\n" +
                "You must advance your Cataclysm tracker 1 step the first time a friendly fighter is slain in each combat phase, and you must advance your Cataclysm tracker 1 step after the last power step in each battle round for each feature token that has no enemy fighters on it.\n\n" +
                "While your tracker is:\n" +
                "- on the 1st to 5th step, your Cataclysm value is 1.\n" +
                "- on the 6th to 9th step, your Cataclysm value is 2.\n" +
                "- on the 10th to 13th step, your Cataclysm value is 3.\n" +
                "- on the final step, your Cataclysm value is 4.";
        long countdownDeckId = insertRivalDeckInternal(db, "Countdown to Cataclysm Rivals Deck", "Mazo Universal", null, "UNIVERSAL", countdownRules);

        // Cartas de Countdown to Cataclysm (Lote 1)
        insertCardInternal(db, "Spread Havoc", "Objective", "End Phase", 1, (int)countdownDeckId, 0, "Score this in an end phase. Gain a number of Glory points equal to your Cataclysm value, to a maximum of 2.", null, "card_cc1");
        insertCardInternal(db, "Nowhere to Run", "Objective", "Surge", 10, (int)countdownDeckId, 1, "Score this immediately after an Action step if all friendly fighters have Move and/or Charge tokens and there is a friendly fighter in each territory.", null, "card_cc10");
        insertCardInternal(db, "The Perfect Cut", "Objective", "Surge", 11, (int)countdownDeckId, 1, "Score this immediately after a friendly fighter's successful melee Attack if no result in the target's Save roll was a success.", null, "card_cc11");
        insertCardInternal(db, "Overwhelming Force", "Objective", "Surge", 12, (int)countdownDeckId, 1, "Score this immediately after a friendly fighter's successful melee Attack if the Attack roll contained 4 or more dice.", null, "card_cc12");
        insertCardInternal(db, "Hounds of War", "Objective", "End Phase", 2, (int)countdownDeckId, 1, "Score this in an end phase if 2 or more enemy fighters are slain and/or damaged and any of those fighters were slain in the preceding combat phase.", null, "card_cc2");

        // Cartas de Countdown to Cataclysm (Lote 2)
        insertCardInternal(db, "Set Explosives", "Objective", "End Phase", 3, (int)countdownDeckId, 2, "Score this in an end phase if your warband holds 2 or more treasure tokens and holds all of the treasure tokens in any territories.", null, "card_cc3");
        insertCardInternal(db, "Wreckers", "Objective", "End Phase", 4, (int)countdownDeckId, 2, "Score this in an end phase if the number of damaged and/or slain enemy fighters is greater than your Cataclysm value.", null, "card_cc4");
        insertCardInternal(db, "Uneven Contest", "Objective", "End Phase", 5, (int)countdownDeckId, 2, "Score this in an end phase if your warband holds each odd-numbered treasure token.", null, "card_cc5");
        insertCardInternal(db, "Loaded for Bear", "Objective", "End Phase", 6, (int)countdownDeckId, 1, "Score this in an end phase if any friendly fighters are each equipped with 3 or more Upgrades.", null, "card_cc6");
        insertCardInternal(db, "Collateral Damage", "Objective", "Surge", 7, (int)countdownDeckId, 1, "Score this immediately after you advance your Cataclysm tracker 1 step as a result of a friendly fighter being slain. If you are the underdog, score this after you advance your Cataclysm tracker 1 step for any reason instead.", null, "card_cc7");

        // Cartas de Countdown to Cataclysm (Lote 3)
        insertCardInternal(db, "Too Close for Comfort", "Objective", "Surge", 8, (int)countdownDeckId, 1, "Score this immediately after your opponent's Power step if each friendly fighter is within 2 hexes of any enemy fighters.", null, "card_cc8");
        insertCardInternal(db, "Shocking Assault", "Objective", "Surge", 9, (int)countdownDeckId, 1, "Score this immediately after your opponent's Action step if your warband holds all of the treasure tokens in neutral territory.", null, "card_cc9");
        insertCardInternal(db, "Savage Blow", "Gambit", null, 13, (int)countdownDeckId, null, "Play this immediately after you pick a weapon as part of an Attack. Rolls of Double Support count as successes for that Attack.", null, "card_cc13");
        insertCardInternal(db, "The End is Nigh", "Gambit", null, 14, (int)countdownDeckId, null, "Domain: After each Action step, the player whose turn it is rolls a number of Attack dice equal to their Cataclysm value, or 1 Attack dice if they have no such value. If the roll contains any Sword or Critical success, their opponent must discard a Power card. This effect persists until the end of the battle round, until another Domain card is played, or until you advance your Cataclysm tracker.", null, "card_cc14");
        insertCardInternal(db, "Growing Concerns", "Gambit", null, 15, (int)countdownDeckId, null, "Enemy fighters have -X Move in the next Action step, where X is your Cataclysm value.", null, "card_cc15");

        // Cartas de Countdown to Cataclysm (Lote 4)
        insertCardInternal(db, "Total Collapse", "Gambit", null, 16, (int)countdownDeckId, null, "Roll a number of Attack dice equal to your Cataclysm value. If the roll contains any Sword or Critical success, pick a fighter. Inflict 1 damage on that fighter. If you picked a friendly fighter, you can inflict damage on that fighter up to your Cataclysm value instead.", null, "card_cc16");
        insertCardInternal(db, "Violent Blast", "Gambit", null, 17, (int)countdownDeckId, null, "Pick a stagger hex. Push each fighter within 1 hex of that stagger hex 1 hex.", null, "card_cc17");
        insertCardInternal(db, "Sunder the Realm", "Gambit", null, 18, (int)countdownDeckId, null, "Roll a number of Attack dice equal to your Cataclysm value for each fighter within 1 hex of neutral territory. If the roll contains any Hammer or Critical success, inflict 1 damage on that fighter.", null, "card_cc18");
        insertCardInternal(db, "Raging Tremors", "Gambit", null, 19, (int)countdownDeckId, null, "Pick a number of enemy fighters up to your Cataclysm value. Give each of those fighters a Stagger token.", null, "card_cc19");
        insertCardInternal(db, "Counter-charge", "Gambit", null, 20, (int)countdownDeckId, null, "Play this immediately after a friendly fighter is picked to be the target of an Attack. Pick another friendly fighter. Push that fighter up to 3 hexes. That push must end adjacent to the attacker.", null, "card_cc20");

        // Cartas de Countdown to Cataclysm (Lote 5)
        insertCardInternal(db, "Do or Die", "Gambit", null, 21, (int)countdownDeckId, null, "Pick a friendly fighter. Inspire that fighter. This effect persists for X Action steps where X is your Cataclysm value. After that effect ends, do not discard this card. Instead, Uninspire that fighter. That fighter cannot be Inspired again. This effect persists until the end of the game.", null, "card_cc21");
        insertCardInternal(db, "Improvised Attack", "Gambit", null, 22, (int)countdownDeckId, null, "Pick a friendly fighter. That fighter immediately Attacks with the following weapon, where X is your Cataclysm value. Weapon: Range 3, Hammer, X Dice, 1 Damage. This weapon cannot be modified.", null, "card_cc22");
        insertCardInternal(db, "Bringer of Doom", "Upgrade", null, 23, (int)countdownDeckId, 1, "Tick Tock: After this card is discarded during a Combat Phase, you can immediately advance your Cataclysm tracker 1 step.", null, "card_cc23");
        insertCardInternal(db, "Visions of Ruin", "Upgrade", null, 24, (int)countdownDeckId, 1, "This fighter has +X Move, where X is your Cataclysm value. Immediately after this fighter Moves, give this fighter a Stagger token.", null, "card_cc24");
        insertCardInternal(db, "Extinction's Edge", "Upgrade", null, 25, (int)countdownDeckId, 1, "Ever Closer: Immediately advance your Cataclysm tracker 1 step after an enemy fighter is slain by this fighter. Then, if your Cataclysm value is 2 or greater, discard this card.", null, "card_cc25");

        // Cartas de Countdown to Cataclysm (Lote 6)
        insertCardInternal(db, "Driven by Pain", "Upgrade", null, 26, (int)countdownDeckId, 1, "Insensate: Immediately after this fighter is driven back, you can heal this fighter.", null, "card_cc26");
        insertCardInternal(db, "Inescapable Grasp", "Upgrade", null, 27, (int)countdownDeckId, 1, "This fighter's melee weapons have Ensnare.", null, "card_cc27");
        insertCardInternal(db, "Utter Conviction", "Upgrade", null, 28, (int)countdownDeckId, 1, "This fighter's Save characteristic is X, where X is your Cataclysm value, and cannot be modified further. This fighter cannot use Critical Weapon abilities.", null, "card_cc28");
        insertCardInternal(db, "Burnt Out", "Upgrade", null, 29, (int)countdownDeckId, 1, "Smoulder: Pick a friendly fighter. Give that fighter a Stagger token. Then, draw 2 Power cards. If you are the underdog, draw 3 Power cards instead.", null, "card_cc29");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 30, (int)countdownDeckId, 2, "This fighter has +1 Health.", null, "card_cc30");

        // Cartas de Countdown to Cataclysm (Lote 7 - Final)
        insertCardInternal(db, "Hurled Weapon", "Upgrade", null, 31, (int)countdownDeckId, 1, "Ranged Attack action: Range 3, Hammer, 2 Dice, 1 Damage. Critical success: Cleave.", null, "card_cc31");
        insertCardInternal(db, "Desperate Rage", "Upgrade", null, 32, (int)countdownDeckId, 1, "Melee Attack action: Range 1, Hammer, 2 Dice, 3 Damage. Immediately after this fighter has Attacked with this weapon, inflict 1 damage on this fighter.", null, "card_cc32");

        // --- Inserción de Deadly Synergy Rivals Deck ---
        String deadlySynergyRules = "While using this Rivals deck, adjacent friendly fighters are united. \n\n" +
                "Enemy fighters adjacent to a united friendly fighter are Flanked. \n\n" +
                "If each other friendly fighter is slain, the remaining friendly fighter is united.";
        long deadlySynergyDeckId = insertRivalDeckInternal(db, "Deadly Synergy Rivals Deck", "Mazo Universal", null, "UNIVERSAL", deadlySynergyRules);

        // --- Cartas de Deadly Synergy ---
        insertCardInternal(db, "Aggressive Unity", "Objective", "End Phase", 1, (int)deadlySynergyDeckId, 2, "Score this in an end phase if 3 or more melee Attacks were made by united friendly fighters this battle round.", null, "card_dy1");
        insertCardInternal(db, "Beatdown", "Objective", "End Phase", 2, (int)deadlySynergyDeckId, 1, "Score this in an end phase if the same enemy fighter was picked to be the target of 3 or more different friendly fighters' melee Attacks this battle round.", null, "card_dy2");
        insertCardInternal(db, "Closed Down", "Objective", "End Phase", 3, (int)deadlySynergyDeckId, 2, "Score this in an end phase if a united friendly fighter is holding a treasure token that was held by an enemy fighter this battle round.", null, "card_dy3");
        insertCardInternal(db, "Got Your Back!", "Objective", "Surge", 4, (int)deadlySynergyDeckId, 1, "Score this immediately after an enemy fighter's failed Attack if the target was a united friendly fighter.", null, "card_dy4");
        insertCardInternal(db, "Helping Hand", "Objective", "Surge", 5, (int)deadlySynergyDeckId, 1, "Score this immediately after a united friendly fighter's successful Attack if the Attack roll contained any Single Support.", null, "card_dy5");
        insertCardInternal(db, "Hemmed In", "Objective", "End Phase", 6, (int)deadlySynergyDeckId, 2, "Score this in an end phase if an enemy leader was slain by a united friendly fighter this battle round.", null, "card_dy6");
        insertCardInternal(db, "Inevitable Outcome", "Objective", "End Phase", 7, (int)deadlySynergyDeckId, 1, "Score this in an end phase if 2 or more united friendly fighters are adjacent to the same enemy fighter.", null, "card_dy7");
        insertCardInternal(db, "Infiltrate", "Objective", "Surge", 8, (int)deadlySynergyDeckId, 1, "Score this immediately after a friendly fighter's successful Attack if the target was in neutral or enemy territory and either the attacker was united or the target was Flanked and/or Surrounded.", null, "card_dy8");
        insertCardInternal(db, "Outmuscle", "Objective", "End Phase", 9, (int)deadlySynergyDeckId, 2, "Score this in an end phase if an enemy fighter was driven back as part of a united friendly fighter's successful Attack this battle round.", null, "card_dy9");
        insertCardInternal(db, "Tag Team", "Objective", "Surge", 10, (int)deadlySynergyDeckId, 1, "Score this immediately after a friendly fighter's successful Attack if they have no Move or Charge tokens and either the attacker was united or the target was Flanked and/or Surrounded.", null, "card_dy10");
        insertCardInternal(db, "Tandem Assault", "Objective", "Surge", 11, (int)deadlySynergyDeckId, 1, "Score this immediately after an enemy fighter is slain by a friendly fighter's Attack if the attacker was united or the target was Flanked and/or Surrounded.", null, "card_dy11");
        insertCardInternal(db, "United Aid", "Objective", "Surge", 12, (int)deadlySynergyDeckId, 1, "Score this immediately after you make a Save roll for a united friendly fighter if there were more successes in the Save roll than the Attacker roll and the Save roll contained any Single Support. If you are the underdog, the Save roll does not have to contain any Single Support.", null, "card_dy12");

        // --- Gambitos (Ploys) de Deadly Synergy ---
        insertCardInternal(db, "Another Swing", "Gambit", null, 13, (int)deadlySynergyDeckId, null, "Play this immediately after a united friendly fighter's failed melee Attack if the attacker is adjacent to 2 united friendly fighters. That fighter Attacks again using a melee weapon, but the Damage characteristic of that weapon is reduced to 1.", null, "card_dy13");
        insertCardInternal(db, "Army of Two", "Gambit", null, 14, (int)deadlySynergyDeckId, null, "Pick a friendly fighter. That fighter is united in the next turn.", null, "card_dy14");
        insertCardInternal(db, "Brother-in-Arms", "Gambit", null, 15, (int)deadlySynergyDeckId, null, "Play this immediately before you pick a melee weapon as part of a united friendly fighter's Attack. Pick a united friendly fighter adjacent to the attacker. Use 1 of that fighter's melee weapons for that Attack instead.", null, "card_dy15");
        insertCardInternal(db, "Defiant Duo", "Gambit", null, 16, (int)deadlySynergyDeckId, null, "Pick 2 adjacent united friendly fighters. If you are the underdog, pick 2 united friendly fighters instead. Give 1 of those fighters a Guard token and then heal the other fighter.", null, "card_dy16");
        insertCardInternal(db, "Out of Nowhere!", "Gambit", null, 17, (int)deadlySynergyDeckId, null, "Play this immediately after you make an Attack roll for a united friendly fighter. Change 1 dice in that Attack roll to Single Support or Double Support.", null, "card_dy17");
        insertCardInternal(db, "Selfless Parry", "Gambit", null, 18, (int)deadlySynergyDeckId, null, "Play this immediately after a united friendly fighter is picked to be the target of an Attack. Pick a united friendly fighter adjacent to the target. The target has that fighter's Save characteristic for that Attack.", null, "card_dy18");
        insertCardInternal(db, "Sidestep", "Gambit", null, 19, (int)deadlySynergyDeckId, null, "Pick a friendly fighter. Push that fighter 1 hex.", null, "card_dy19");
        insertCardInternal(db, "Take One for the Team", "Gambit", null, 20, (int)deadlySynergyDeckId, null, "Play this immediately after a united friendly fighter is picked to be the target of an Attack. Pick a united friendly fighter adjacent to the target and within range of the weapon being used for the Attack. That fighter must be the target of that Attack instead.", null, "card_dy20");
        insertCardInternal(db, "Timed Strike", "Gambit", null, 21, (int)deadlySynergyDeckId, null, "Play this immediately after you pick a melee weapon as part of an Attack for a friendly fighter that is not united. That weapon has +1 Attack dice for that Attack.", null, "card_dy21");
        insertCardInternal(db, "Unified Front", "Gambit", null, 22, (int)deadlySynergyDeckId, null, "United friendly fighters have +1 Save in the next turn, to a maximum of 3.", null, "card_dy22");

        // --- Mejoras (Upgrades) de Deadly Synergy ---
        insertCardInternal(db, "Battering Ram", "Upgrade", null, 23, (int)deadlySynergyDeckId, null, "Attack rolls made for this fighter as part of melee Attacks always count as having more Critical success than the Save roll for purposes of Drive Back and Overrun.", null, "card_dy23");
        insertCardInternal(db, "Coordinated Deathblow", "Upgrade", "Weapon", 24, (int)deadlySynergyDeckId, null, "Range 1, 3 Fury, 2 Damage. This weapon has Grievous while this fighter is united.", null, "card_dy24");
        insertCardInternal(db, "Duellist", "Upgrade", null, 25, (int)deadlySynergyDeckId, null, "Footwork: Immediately after this fighter has Attacked, you can push this fighter 1 hex.", null, "card_dy25");
        insertCardInternal(db, "Entangling Strike", "Upgrade", "Weapon", 26, (int)deadlySynergyDeckId, null, "Range 1, 3 Smash, 1 Damage. This weapon has Cleave and Ensnare if the target is Flanked and/or Surrounded.", null, "card_dy26");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 27, (int)deadlySynergyDeckId, null, "This fighter has +1 Health.", null, "card_dy27");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 28, (int)deadlySynergyDeckId, null, "This fighter's melee weapons have +1 Attack dice.", null, "card_dy28");
        insertCardInternal(db, "Rush to Aid", "Upgrade", null, 29, (int)deadlySynergyDeckId, null, "Assist: Immediately after this fighter has Attacked, you can pick a united friendly fighter. Push this fighter up to 2 hexes. That push must end adjacent to that united friendly fighter.", null, "card_dy29");
        insertCardInternal(db, "Shared Incentive", "Upgrade", null, 30, (int)deadlySynergyDeckId, null, "This upgrade can only be given to your leader. This fighter has +1 Move. United friendly fighters have +1 Move if they are adjacent to this fighter when they start that Move.", null, "card_dy30");
        insertCardInternal(db, "Spurred Momentum", "Upgrade", null, 31, (int)deadlySynergyDeckId, null, "Driven: Immediately after this fighter's successful Attack, if this fighter was united, remove their Move and Stagger tokens and then discard this card.", null, "card_dy31");
        insertCardInternal(db, "Titan of Combat", "Upgrade", null, 32, (int)deadlySynergyDeckId, null, "This fighter is united.", null, "card_dy32");

        // --- Inserción de Raging Slayers Rivals Deck ---
        String ragingSlayersRules = "Raging Charge: Immediately after picking a friendly fighter to Charge, you can give that fighter a Rage token. Fighters with any Rage tokens are enraged. \n\n" +
                "Raging Strike: After you make an Attack roll as part of a melee Attack for an enraged friendly fighter, you can immediately re-roll 1 Attack dice in that Attack roll. \n\n" +
                "Poor Footing: You must use this immediately after an enraged friendly fighter was Attacked if that fighter was not driven back or grappled. Push the target 1 hex away from the attacker. Your opponent chooses the direction of that push. \n\n" +
                "Remove all Rage tokens from enraged friendly fighters at the end of the battle round.";
        long ragingSlayersDeckId = insertRivalDeckInternal(db, "Raging Slayers Rivals Deck", "Mazo Universal", null, "UNIVERSAL", ragingSlayersRules);

        // --- Objetivos (Objectives) de Raging Slayers ---
        insertCardInternal(db, "Aggressive Expansion", "Objective", "End Phase", 1, (int)ragingSlayersDeckId, 1, "Score this in an end phase if 3 or more enraged friendly fighters are in enemy territory.", null, "card_rs1");
        insertCardInternal(db, "Best Foot Forward", "Objective", "Surge", 2, (int)ragingSlayersDeckId, 1, "Score this immediately after a friendly fighter's successful Attack if that fighter has any Charge tokens and is in enemy territory.", null, "card_rs2");
        insertCardInternal(db, "Blinded by Rage", "Objective", "Surge", 3, (int)ragingSlayersDeckId, 1, "Score this immediately after an enemy fighter is slain by an enraged friendly fighter's melee Attack if that friendly fighter was enraged at the start of the turn.", null, "card_rs3");
        insertCardInternal(db, "Coordinated Assault", "Objective", "Surge", 4, (int)ragingSlayersDeckId, 1, "Score this immediately after an Action step if each friendly fighter is enraged and in enemy territory.", null, "card_rs4");
        insertCardInternal(db, "Into the Fire", "Objective", "End Phase", 5, (int)ragingSlayersDeckId, 1, "Score this in an end phase if your leader is in enemy territory and within 2 hexes of 2 or more other fighters.", null, "card_rs5");
        insertCardInternal(db, "No Contest", "Objective", "End Phase", 6, (int)ragingSlayersDeckId, 1, "Score this in an end phase if an enemy leader is slain.", null, "card_rs6");
        insertCardInternal(db, "No Escape", "Objective", "End Phase", 7, (int)ragingSlayersDeckId, 2, "Score this in an end phase if each friendly fighter is enraged and in enemy territory, and no enemy fighters are in your territory.", null, "card_rs7");
        insertCardInternal(db, "No Respite", "Objective", "Surge", 8, (int)ragingSlayersDeckId, 1, "Score this immediately after an opponent's Action step if there is an enraged friendly fighter in each territory.", null, "card_rs8");
        insertCardInternal(db, "Overwhelming Presence", "Objective", "End Phase", 9, (int)ragingSlayersDeckId, 2, "Score this in an end phase if each friendly fighter is enraged and there are no fighters holding treasure tokens.", null, "card_rs9");
        insertCardInternal(db, "Sever the Head", "Objective", "Surge", 10, (int)ragingSlayersDeckId, 1, "Score this immediately after an enemy leader is slain by an enraged friendly fighter's melee Attack.", null, "card_rs10");
        insertCardInternal(db, "Supreme Slayer", "Objective", "Surge", 11, (int)ragingSlayersDeckId, 1, "Score this immediately after your leader's successful Attack if the target was slain and that was the second or subsequent fighter slain by your leader.", null, "card_rs11");
        insertCardInternal(db, "Unrelenting Massacre", "Objective", "End Phase", 12, (int)ragingSlayersDeckId, 3, "Score this in an end phase if all fighters have Charge tokens.", null, "card_rs12");

        // --- Gambitos (Ploys) de Raging Slayers ---
        insertCardInternal(db, "Adrenaline Rush", "Gambit", null, 13, (int)ragingSlayersDeckId, null, "Pick up to 2 enraged friendly fighters. Remove their Stagger tokens.", null, "card_rs13");
        insertCardInternal(db, "Honed Reflexes", "Gambit", null, 14, (int)ragingSlayersDeckId, null, "Domain: Each time you make a Save roll for an enraged friendly fighter, you can re-roll 1 Save dice in that Save roll. This effect persists until the end of the battle round or until another Domain card is played.", null, "card_rs14");
        insertCardInternal(db, "Knife to the Heart", "Gambit", null, 15, (int)ragingSlayersDeckId, null, "Pick an enemy fighter that is not vulnerable and that is adjacent to an enraged friendly fighter. Inflict 1 damage on that enemy fighter. Then, remove that friendly fighter's Rage tokens.", null, "card_rs15");
        insertCardInternal(db, "Murderlust", "Gambit", null, 16, (int)ragingSlayersDeckId, null, "Play this immediately after an enraged friendly fighter's melee Attack fails. Friendly fighters' melee weapons have +1 Attack dice while targeting the target of that failed Attack until the end of the battle round.", null, "card_rs16");
        insertCardInternal(db, "Senseless Haste", "Gambit", null, 17, (int)ragingSlayersDeckId, null, "Friendly fighters have +1 Move in the next turn. After any friendly fighters Move in your next turn, give that fighter a Stagger token.", null, "card_rs17");
        insertCardInternal(db, "Slayer's Aid", "Gambit", null, 18, (int)ragingSlayersDeckId, null, "Pick an enraged friendly fighter. Push that fighter up to 3 hexes. That push must end adjacent to your leader.", null, "card_rs18");
        insertCardInternal(db, "Slayer's Arena", "Gambit", null, 19, (int)ragingSlayersDeckId, null, "Domain: After each failed Attack, give the attacker a Stagger token. This effect persists until the end of the battle round or until another Domain card is played.", null, "card_rs19");
        insertCardInternal(db, "Venting Strike", "Gambit", null, 20, (int)ragingSlayersDeckId, null, "Play this immediately after you pick a melee weapon as part of an Attack made by an enraged friendly fighter. That weapon has +1 Attack dice for that Attack. Immediately after that Attack, remove that fighter's Rage tokens.", null, "card_rs20");
        insertCardInternal(db, "What Pain?", "Gambit", null, 21, (int)ragingSlayersDeckId, null, "Pick up to 2 enraged friendly fighters. Heal those fighters. Then, remove their Rage tokens and give them a Stagger token.", null, "card_rs21");
        insertCardInternal(db, "Wrong-footed Stance", "Gambit", null, 22, (int)ragingSlayersDeckId, null, "Domain: If a fighter enters or is placed in a stagger hex, give them a Move token in addition to a Stagger token. This effect persists until the end of the battle round or until another Domain card is played.", null, "card_rs22");

        // --- Mejoras (Upgrades) de Raging Slayers ---
        insertCardInternal(db, "Agile", "Upgrade", null, 23, (int)ragingSlayersDeckId, 2, "Deft: After you make a Save roll for this fighter, you can immediately re-roll 1 Save dice in that Save roll.", null, "card_rs23");
        insertCardInternal(db, "Aggressive Ambusher", "Upgrade", null, 24, (int)ragingSlayersDeckId, 1, "While this fighter is enraged, their melee weapons have Cleave and Ensnare if the target is Flanked and/or Surrounded.", null, "card_rs24");
        insertCardInternal(db, "Angered Swing", "Upgrade", null, 25, (int)ragingSlayersDeckId, 1, "Melee Attack action \n\n Hex 1 SwordFury 3 Damage 2 \n A fighter can only use this weapon while enraged. When using the Raging Strike ability with this weapon, you must re-roll each Attack dice instead.", null, "card_rs25");
        insertCardInternal(db, "Assured Bloodshed", "Upgrade", null, 26, (int)ragingSlayersDeckId, 1, "While this fighter is enraged, their melee weapons have Grapple and Brutal.", null, "card_rs26");
        insertCardInternal(db, "Gifted Vitality", "Upgrade", null, 27, (int)ragingSlayersDeckId, 2, "Heal this fighter at the end of each battle round.", null, "card_rs27");
        insertCardInternal(db, "Haymaker", "Upgrade", null, 28, (int)ragingSlayersDeckId, 2, "This fighter's melee weapons (excluding Upgrades) have the Lethal Weapon ability. After this fighter make a successful Attack with Lethal, discard this card. \n\n Lethal: That fighter's weapon has +2 Damage for that Attack, to a maximum of 4.", null, "card_rs28");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 29, (int)ragingSlayersDeckId, 2, "This fighter's melee weapons have +1 Attack dice.", null, "card_rs29");
        insertCardInternal(db, "Murderous Instincts", "Upgrade", null, 30, (int)ragingSlayersDeckId, 1, "This fighter's Range 1 melee weapons (excluding Upgrades) have Grievous. \n\n This fighter cannot use Core abilities other than the Charge ability.", null, "card_rs30");
        insertCardInternal(db, "Stubborn to the Bone", "Upgrade", null, 31, (int)ragingSlayersDeckId, 1, "While this fighter is enraged, this fighter cannot be pushed or given Stagger tokens.", null, "card_rs31");
        insertCardInternal(db, "United in Anger", "Upgrade", null, 32, (int)ragingSlayersDeckId, 1, "Rally Together: Immediately after an enraged friendly fighter is pushed, you can push this fighter up to 1 hex closer to that fighter.", null, "card_rs32");

        // --- Inserción de Realmstone Raiders Rivals Deck ---
        String realmstoneRaidersRules = "While using this Rivals deck, immediately after the first successful friendly melee Attack in an Action step, you can raid a number of times equal to the Bounty characteristic of the target. \n\n" +
                "Raid: Reveal the top card of your Power deck. That card is raided. If that card is an Emberstone card, you can put that card back on top or on the bottom of your Power deck. \n\n" +
                "Emberstone cards have the following additional rules: \n\n" +
                "Emberstone Ploy: You can play this Ploy immediately after it is raided. \n\n" +
                "Emberstone Upgrade: You can equip this Upgrade immediately after it is raided if you have the required Glory points. \n\n" +
                "If a raided card is not an Emberstone card, put that card on the bottom of your Power deck.";
        long realmstoneRaidersDeckId = insertRivalDeckInternal(db, "Realmstone Raiders Rivals Deck", "Mazo Universal", null, "UNIVERSAL", realmstoneRaidersRules);

        // --- Objetivos (Objectives) de Realmstone Raiders ---
        insertCardInternal(db, "A Sure Bet", "Objective", "Surge", 1, (int)realmstoneRaidersDeckId, 1, "Score this immediately after raiding an Emberstone card after a melee Attack if the Attack roll contained 3 or more Attack dice.", null, "card_rr1");
        insertCardInternal(db, "Certain Aggression", "Objective", "End Phase", 2, (int)realmstoneRaidersDeckId, 2, "Score this in an end phase if you raided 2 or more times this battle round and there are no enemy fighters in friendly territory.", null, "card_rr2");
        insertCardInternal(db, "Critical Risk", "Objective", "Surge", 3, (int)realmstoneRaidersDeckId, 1, "Score this immediately after raiding an Emberstone card if the preceding Attack was successful and the Attack roll included any Critical success.", null, "card_rr3");
        insertCardInternal(db, "Emberstone Stash", "Objective", "End Phase", 4, (int)realmstoneRaidersDeckId, 1, "Score this in an end phase if you raided 4 or more different Emberstone cards this battle round.", null, "card_rr4");
        insertCardInternal(db, "Hoarder's Hovel", "Objective", "End Phase", 5, (int)realmstoneRaidersDeckId, 2, "Score this in an end phase if any friendly fighters are holding a treasure token that has a value equal to that fighter's Bounty characteristic.", null, "card_rr5");
        insertCardInternal(db, "Invade", "Objective", "End Phase", 6, (int)realmstoneRaidersDeckId, 1, "Score this in an end phase if you raided any Emberstone cards this battle round after a melee Attack if the target was holding a treasure token.", null, "card_rr6");
        insertCardInternal(db, "Looted Realmstone", "Objective", "Surge", 7, (int)realmstoneRaidersDeckId, 1, "Score this immediately after raiding 2 or more different Emberstone cards from the same melee Attack.", null, "card_rr7");
        insertCardInternal(db, "Pillage", "Objective", "Surge", 8, (int)realmstoneRaidersDeckId, 1, "Score this immediately after raiding 2 or more different Emberstone cards after a melee Attack if the target was in neutral or enemy territory.", null, "card_rr8");
        insertCardInternal(db, "Ragerock Strike", "Objective", "Surge", 9, (int)realmstoneRaidersDeckId, 1, "Score this immediately after resolving the Emberstone Ploy ability or the Emberstone Upgrade ability (see Plot card).", null, "card_rr9");
        insertCardInternal(db, "Realmstone Raid", "Objective", "End Phase", 10, (int)realmstoneRaidersDeckId, 2, "Score this in an end phase if you raided 3 or more times after different friendly fighters Attacked this battle round.", null, "card_rr10");
        insertCardInternal(db, "Reckless Gambit", "Objective", "Surge", 11, (int)realmstoneRaidersDeckId, 1, "Score this immediately after raiding an Emberstone card for the second or subsequent time this battle round and each Emberstone card raided was a different card.", null, "card_rr11");
        insertCardInternal(db, "Roused Violence", "Objective", "End Phase", 12, (int)realmstoneRaidersDeckId, 2, "Score this in an end phase if the number of treasure tokens on the battlefield is equal to or less than the number of Emberstone cards you raided this battle round.", null, "card_rr12");

        // --- Gambitos (Gambits) de Realmstone Raiders ---
        insertCardInternal(db, "Ambush", "Gambit", null, 13, (int)realmstoneRaidersDeckId, null, "Play this immediately after picking a ranged weapon as part of an Attack. You can raid after a successful friendly ranged Attack instead of a melee Attack this turn.", null, "card_rr13");
        insertCardInternal(db, "Angered Focus", "Gambit", null, 14, (int)realmstoneRaidersDeckId, null, "Emberstone Ploy \n Pick a friendly fighter holding a treasure token in friendly territory that does not have any Move, Charge or Stagger tokens. Raid a number of cards equal to that fighter's Bounty characteristic. Cards raided this way do not count as having been raided as a result of a successful Attack.", null, "card_rr14");
        insertCardInternal(db, "A Step Ahead", "Gambit", null, 15, (int)realmstoneRaidersDeckId, null, "Emberstone Ploy \n Pick a friendly fighter in neutral or enemy territory. Push that fighter 1 hex. That push must end closer to an enemy fighter.", null, "card_rr15");
        insertCardInternal(db, "Fortune Faded", "Gambit", null, 16, (int)realmstoneRaidersDeckId, null, "Pick a friendly fighter adjacent to an enemy fighter that is not vulnerable. Inflict 1 damage on both fighters.", null, "card_rr16");
        insertCardInternal(db, "Hidden Knowledge", "Gambit", null, 17, (int)realmstoneRaidersDeckId, null, "Emberstone Ploy \n Pick an enemy fighter adjacent to a friendly fighter. Give that fighter a Move token.", null, "card_rr17");
        insertCardInternal(db, "Intoxicated with Rage", "Gambit", null, 18, (int)realmstoneRaidersDeckId, null, "Emberstone Ploy \n Pick a friendly fighter that has no Move and/or Charge tokens. Give that fighter a Guard token.", null, "card_rr18");
        insertCardInternal(db, "Manipulated Fate", "Gambit", null, 19, (int)realmstoneRaidersDeckId, null, "Play this immediately after a friendly fighter is picked as the target of an Attack. Save rolls made for that Attack count as having more Critical success than the Attack roll for the purposes of the Stand Fast ability.", null, "card_rr19");
        insertCardInternal(db, "Misstep", "Gambit", null, 20, (int)realmstoneRaidersDeckId, null, "Emberstone Ploy \n Pick an enemy fighter. Give that fighter a Stagger token.", null, "card_rr20");
        insertCardInternal(db, "Raider's Rapture", "Gambit", null, 21, (int)realmstoneRaidersDeckId, null, "Domain: When you raid, you can reveal 1 additional card. This effect persists until the end of the round or until another Domain card is played.", null, "card_rr21");
        insertCardInternal(db, "Raider's Premonition", "Gambit", null, 22, (int)realmstoneRaidersDeckId, null, "Emberstone Ploy \n Pick an Objective card from your hand and put it on the bottom of your Objective deck. Then draw 1 Objective card.", null, "card_rr22");

        // --- Mejoras (Upgrades) de Realmstone Raiders ---
        insertCardInternal(db, "Armour Piercer", "Upgrade", null, 23, (int)realmstoneRaidersDeckId, 2, "Emberstone Upgrade \n Breached: Immediately after a drawn Attack made by this fighter, if the Damage characteristic of the attacker's weapon is greater than the target's Save characteristic, inflict 1 damage on the target. Then discard this card.", null, "card_rr23");
        insertCardInternal(db, "Brightstone Vigour", "Upgrade", null, 24, (int)realmstoneRaidersDeckId, 2, "Emberstone Upgrade \n Soothed by Slaughter: Immediately after a successful Attack made by this fighter, if the Damage characteristic of the weapon used is greater than the Save characteristic of the target, you can heal the attacker. Then discard this card.", null, "card_rr24");
        insertCardInternal(db, "Call to Power", "Upgrade", null, 25, (int)realmstoneRaidersDeckId, 1, "Emberstone Upgrade \n Heed the Call: Immediately after a successful Attack made by this fighter, you can draw 1 Power card. Then discard this card.", null, "card_rr25");
        insertCardInternal(db, "Emberstone Edge", "Upgrade", null, 26, (int)realmstoneRaidersDeckId, 2, "Emberstone Upgrade \n Melee Attack action \n Hex 1 Hammer 2 Damage 1 \n This weapon has +1 Attack dice if you raided in the same phase.", null, "card_rr26");
        insertCardInternal(db, "Forgotten Fortune", "Upgrade", null, 27, (int)realmstoneRaidersDeckId, 1, "Emberstone Upgrade \n Lingering Power: Immediately after a successful Attack made by this fighter, you can pick 1 Emberstone Ploy from your Power discard pile. Add it to your hand. Then discard this card.", null, "card_rr27");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 28, (int)realmstoneRaidersDeckId, 2, "This fighter has +1 Health.", null, "card_rr28");
        insertCardInternal(db, "Great Speed", "Upgrade", null, 29, (int)realmstoneRaidersDeckId, 0, "This fighter has +1 Move.", null, "card_rr29");
        insertCardInternal(db, "Great Strength", "Upgrade", null, 30, (int)realmstoneRaidersDeckId, 2, "This fighter's melee weapons have Grievous.", null, "card_rr30");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 31, (int)realmstoneRaidersDeckId, 2, "This fighter's melee weapons have +1 Attack dice.", null, "card_rr31");
        insertCardInternal(db, "Reforged Aid", "Upgrade", null, 32, (int)realmstoneRaidersDeckId, 1, "Emberstone Upgrade \n Salvaged Arms: Immediately after a successful Attack made by this fighter, you can pick 1 Emberstone Upgrade from your Power discard pile. Add it to your hand. Then discard this card.", null, "card_rr32");

        // --- Inserción de Edge of the Knife Rivals Deck ---
        String edgeKnifeRules = "While using this Rivals deck, fighters with a Health characteristic of 2 or less and/or 2 or more damage tokens are tempered.";
        long edgeKnifeDeckId = insertRivalDeckInternal(db, "Edge of the Knife Rivals Deck", "Mazo Universal", null, "UNIVERSAL", edgeKnifeRules);

        // --- Objetivos (Objectives) de Edge of the Knife ---
        insertCardInternal(db, "Aggressive Defender", "Objective", "Surge", 1, (int)edgeKnifeDeckId, 1, "Score this immediately after a friendly fighter's Attack if the attacker is holding a treasure token.", null, "card_ek1");
        insertCardInternal(db, "All In", "Objective", "End Phase", 2, (int)edgeKnifeDeckId, 1, "Score this in an end phase if there are no tempered friendly fighters in friendly territory and any tempered friendly fighters are in neutral and/or enemy territory.", null, "card_ek2");
        insertCardInternal(db, "Trial of the Tempered", "Objective", "End Phase", 10, (int)edgeKnifeDeckId, 3, "Score this in an end phase if each fighter is tempered.", null, "card_ek10");
        insertCardInternal(db, "Two-pronged Assault", "Objective", "End Phase", 11, (int)edgeKnifeDeckId, 1, "Score this in an end phase if 2 or more tempered friendly fighters are in enemy territory.", null, "card_ek11");
        insertCardInternal(db, "Usurper", "Objective", "End Phase", 12, (int)edgeKnifeDeckId, 2, "Score this in an end phase if an enemy leader was slain by a tempered friendly fighter this battle round.", null, "card_ek12");
        insertCardInternal(db, "Behind Enemy Lines", "Objective", "Surge", 3, (int)edgeKnifeDeckId, 1, "Score this immediately after an opponent's Action step if a tempered friendly fighter holds a feature token in enemy territory.", null, "card_ek3");
        insertCardInternal(db, "Calm Before The Storm", "Objective", "End Phase", 4, (int)edgeKnifeDeckId, 2, "Score this in an end phase if there are any damaged fighters and those fighters are not adjacent.", null, "card_ek4");
        insertCardInternal(db, "Double Team", "Objective", "Surge", 5, (int)edgeKnifeDeckId, 1, "Score this immediately after a friendly fighter's successful Attack if the target was Flanked by a friendly fighter.", null, "card_ek5");
        insertCardInternal(db, "Immovable", "Objective", "Surge", 6, (int)edgeKnifeDeckId, 1, "Score this immediately after an opponent's Action step if a friendly fighter was the target of an Attack in that Action step while they were tempered and the target was not slain.", null, "card_ek6");
        insertCardInternal(db, "Power in Numbers", "Objective", "Surge", 7, (int)edgeKnifeDeckId, 1, "Score this immediately after an Action step if 3 or more tempered fighters with no Move tokens are adjacent. If you are the underdog score this if 2 or more tempered fighters with no Move tokens are adjacent instead.", null, "card_ek7");
        insertCardInternal(db, "Risky Position", "Objective", "End Phase", 8, (int)edgeKnifeDeckId, 1, "Score this in an end phase if a tempered friendly fighter is in enemy territory.", null, "card_ek8");
        insertCardInternal(db, "Sneak Into Position", "Objective", "Surge", 9, (int)edgeKnifeDeckId, 1, "Score this immediately after an opponent's Action step if 2 or more tempered friendly fighters are adjacent to the same enemy fighter.", null, "card_ek9");

        // --- Gambitos (Ploys) de Edge of the Knife ---
        insertCardInternal(db, "Death Throes", "Ploy", null, 13, (int)edgeKnifeDeckId, null, "Pick a tempered friendly fighter. In the next Action step, Attacks that target this fighter have -2 Attack dice.", null, "card_ek13");
        insertCardInternal(db, "Fake Out!", "Ploy", null, 14, (int)edgeKnifeDeckId, null, "Pick an enemy fighter with a Health characteristic of 3 or more that is adjacent to a tempered friendly fighter, and then roll an Attack dice. If the roll contains any Hammer or Swords, inflict 1 damage on that fighter, and then give that fighter a stagger token. If you are the underdog, the roll can also contain Critical Success.", null, "card_ek14");
        insertCardInternal(db, "Final Stand", "Ploy", null, 15, (int)edgeKnifeDeckId, null, "In the next Action step, tempered friendly fighters cannot be pushed.", null, "card_ek15");
        insertCardInternal(db, "Opportunity Strikes", "Ploy", null, 16, (int)edgeKnifeDeckId, null, "Play this immediately after you pick a weapon as part of an Attack for a tempered friendly fighter. That weapon has +1 Attack dice for that Attack.", null, "card_ek16");
        insertCardInternal(db, "Power From Death", "Ploy", null, 17, (int)edgeKnifeDeckId, null, "Play this immediately after a friendly fighter with a bounty of 1 or more is slain by an enemy fighter, if that fighter was tempered before inflicting the damage that would slay them. Draw up to 3 Power cards.", null, "card_ek17");
        insertCardInternal(db, "Running Riot", "Ploy", null, 18, (int)edgeKnifeDeckId, null, "Play this immediately after a tempered friendly fighter's successful Attack. Pick another friendly fighter. That fighter is tempered.", null, "card_ek18");
        insertCardInternal(db, "Sidestep", "Ploy", null, 19, (int)edgeKnifeDeckId, null, "Pick a friendly fighter. Push that fighter 1 hex.", null, "card_ek19");
        insertCardInternal(db, "Spiteful Traps", "Ploy", null, 20, (int)edgeKnifeDeckId, null, "Pick an enemy fighter with a Health characteristic of 3 or more within 2 hexes of your leader. Give that fighter a Move token.", null, "card_ek20");
        insertCardInternal(db, "Synchronised Effort", "Ploy", null, 21, (int)edgeKnifeDeckId, null, "Pick 2 tempered friendly fighters within 4 hexes of each other. Remove those fighters from the battlefield and then place each in the hex the other was removed from.", null, "card_ek21");
        insertCardInternal(db, "The Uprising!", "Ploy", null, 22, (int)edgeKnifeDeckId, null, "All friendly fighters are tempered in the next Action step.", null, "card_ek22");

        // --- Mejoras (Upgrades) de Edge of the Knife ---
        insertCardInternal(db, "Dark Horse", "Upgrade", null, 23, (int)edgeKnifeDeckId, 1, "This fighter is tempered.", null, "card_ek23");
        insertCardInternal(db, "Deadly Aim", "Upgrade", null, 24, (int)edgeKnifeDeckId, 1, "This fighter's weapons have Ensnare.", null, "card_ek24");
        insertCardInternal(db, "Fuelled by Pain", "Upgrade", null, 25, (int)edgeKnifeDeckId, 1, "Melee Attack action: Range 1, Dice X (Hammer), Damage 2. Keywords: Ensnare. X is equal to the number of damage tokens this fighter has.", null, "card_ek25");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 26, (int)edgeKnifeDeckId, 1, "This fighter has +1 Health.", null, "card_ek26");
        insertCardInternal(db, "Impervious", "Upgrade", null, 27, (int)edgeKnifeDeckId, 1, "While this fighter is tempered, Save rolls for this fighter cannot be affected by Cleave and Ensnare.", null, "card_ek27");
        insertCardInternal(db, "Lash Out", "Upgrade", null, 28, (int)edgeKnifeDeckId, 1, "Thrash: Immediately after this fighter is slain, before removing them from the battlefield, roll a number of Save dice equal to the battle round. For each Block, give an enemy fighter within 2 hexes of this fighter a Move token.", null, "card_ek28");
        insertCardInternal(db, "Mobbed!", "Upgrade", null, 29, (int)edgeKnifeDeckId, 1, "When this fighter Attacks, if the target is within 3 hexes of any other friendly fighters, the target is Flanked and Surrounded for that Attack.", null, "card_ek29");
        insertCardInternal(db, "Parting Shot", "Upgrade", null, 30, (int)edgeKnifeDeckId, 1, "Pyrrhic Strike: Immediately after this fighter is slain by an enemy fighter, before removing them from the battlefield, pick an enemy fighter within 2 hexes of them. Give that fighter a stagger token. If this fighter was tempered before inflicting the damage that would slay them, inflict 1 damage on that fighter.", null, "card_ek30");
        insertCardInternal(db, "Pesky Nuisance", "Upgrade", null, 31, (int)edgeKnifeDeckId, 2, "Enemy fighters adjacent to this fighter must target this fighter with any Attacks.", null, "card_ek31");
        insertCardInternal(db, "Sharpened Points", "Upgrade", null, 32, (int)edgeKnifeDeckId, 1, "This fighter's weapons have Cleave.", null, "card_ek32");

        // --- Inserción de Hunting Grounds Rivals Deck ---
        long huntingGroundsDeckId = insertRivalDeckInternal(db, "Hunting Grounds Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);

        // --- Objetivos (Objectives) de Hunting Grounds ---
        insertCardInternal(db, "Back Off!", "Objective", "Surge", 1, (int)huntingGroundsDeckId, 1, "Score this immediately after a friendly fighter's Attack if the target was pushed into a different territory.", null, "card_hg1");
        insertCardInternal(db, "Bloodscent", "Objective", "End Phase", 2, (int)huntingGroundsDeckId, 1, "Score this in an end phase if any enemy fighters in friendly territory have 2 or more damage tokens and/or are vulnerable. If you are the underdog, those enemy fighters can be in enemy territory instead.", null, "card_hg2");
        insertCardInternal(db, "Hands Off!", "Objective", "Surge", 3, (int)huntingGroundsDeckId, 1, "Score this immediately after a friendly fighter's Attack if the target was on a feature token and was driven back.", null, "card_hg3");
        insertCardInternal(db, "Home Advantage", "Objective", "Surge", 4, (int)huntingGroundsDeckId, 1, "Score this immediately after an enemy fighter's failed Attack if the target was a friendly fighter in friendly territory.", null, "card_hg4");
        insertCardInternal(db, "Lead by Example", "Objective", "End Phase", 5, (int)huntingGroundsDeckId, 2, "Score this in an end phase if your leader is in friendly territory and Attacked 2 or more times this battle round.", null, "card_hg5");
        insertCardInternal(db, "No Business Here", "Objective", "Surge", 6, (int)huntingGroundsDeckId, 1, "Score this immediately after a friendly fighter's Attack if the target was in friendly territory and was Flanked and/or Surrounded.", null, "card_hg6");
        insertCardInternal(db, "No Trespassers", "Objective", "Surge", 7, (int)huntingGroundsDeckId, 1, "Score this immediately after a friendly fighter's Attack if the target was in friendly territory and was slain.", null, "card_hg7");
        insertCardInternal(db, "Pinned!", "Objective", "End Phase", 8, (int)huntingGroundsDeckId, 3, "Score this in an end phase if an enemy fighter was driven back into an edge hex in friendly territory this battle round.", null, "card_hg8");
        insertCardInternal(db, "Ready or Not", "Objective", "End Phase", 9, (int)huntingGroundsDeckId, 2, "Score this in an end phase if 2 or more enemy fighters are in friendly territory and each enemy fighter is damaged and/or adjacent to any friendly fighters.", null, "card_hg9");
        insertCardInternal(db, "Spoiling for a Fight", "Objective", "End Phase", 10, (int)huntingGroundsDeckId, 1, "Score this in an end phase if your leader is in friendly territory and is within 2 hexes of 2 or more other fighters from 2 or more warbands.", null, "card_hg10");
        insertCardInternal(db, "This is Our Turf!", "Objective", "End Phase", 11, (int)huntingGroundsDeckId, 1, "Score this in an end phase if there are more friendly fighters with Move and/or Charge tokens in friendly territory than there are enemy fighters with Move and/or Charge tokens in friendly territory.", null, "card_hg11");
        insertCardInternal(db, "Usurped", "Objective", "Surge", 12, (int)huntingGroundsDeckId, 1, "Score this immediately after a friendly fighter's Attack if the attacker is on a feature token that the target was on when you picked them to be the target of that Attack.", null, "card_hg12");

        // --- Ardides (Gambits) de Hunting Grounds ---
        insertCardInternal(db, "Audacious Denial", "Gambit", "Ploy", 13, (int)huntingGroundsDeckId, null, "If your leader is in friendly territory, pick an enemy fighter adjacent to them in friendly territory. Your leader immediately Attacks that fighter with 1 of their melee weapons that has a Damage characteristic of 1.", null, "card_hg13");
        insertCardInternal(db, "Deny Invaders", "Gambit", "Ploy", 14, (int)huntingGroundsDeckId, null, "In the next turn, friendly fighters' weapons have +1 Attack dice while they are in friendly territory.", null, "card_hg14");
        insertCardInternal(db, "Hidden Snares", "Gambit", "Ploy", 15, (int)huntingGroundsDeckId, null, "Pick an enemy fighter in friendly territory and within 1 hex of a feature token. If you are the underdog, you can pick an enemy fighter in enemy territory and within 1 hex of a feature token instead. That fighter's weapons have -1 Attack dice in the next turn.", null, "card_hg15");
        insertCardInternal(db, "Keep Them at Bay", "Gambit", "Ploy", 16, (int)huntingGroundsDeckId, null, "In the next turn, friendly fighters' melee weapons have +1 Range, to a maximum of 2, for Attacks that target enemy fighters in friendly territory.", null, "card_hg16");
        insertCardInternal(db, "Mind Your Step", "Gambit", "Ploy", 17, (int)huntingGroundsDeckId, null, "Pick an enemy fighter in friendly territory. Push that fighter up to 2 hexes. That push must end in neutral or enemy territory.", null, "card_hg17");
        insertCardInternal(db, "Mystical Misdirection", "Gambit", "Ploy", 18, (int)huntingGroundsDeckId, null, "Pick 2 treasure tokens in friendly territory. Swap the positions of those treasure tokens.", null, "card_hg18");
        insertCardInternal(db, "Paths Unknown", "Gambit", "Ploy", 19, (int)huntingGroundsDeckId, null, "Pick a friendly fighter in friendly territory. If you are the underdog, you can pick a friendly fighter in enemy territory instead. In the next turn, that fighter cannot be picked to be the target of an Attack.", null, "card_hg19");
        insertCardInternal(db, "Poor Footing", "Gambit", "Ploy", 20, (int)huntingGroundsDeckId, null, "Pick an enemy fighter in friendly territory and within 1 hex of a feature token. Give that fighter a Move token.", null, "card_hg20");
        insertCardInternal(db, "Secrets of the Realm", "Gambit", "Ploy", 21, (int)huntingGroundsDeckId, null, "Give each enemy fighter in friendly territory that is adjacent to a friendly fighter and/or feature token a Stagger token.", null, "card_hg21");
        insertCardInternal(db, "Sidestep", "Gambit", "Ploy", 22, (int)huntingGroundsDeckId, null, "Pick a friendly fighter. Push that fighter 1 hex.", null, "card_hg22");

        // --- Mejoras (Upgrades) de Hunting Grounds ---
        insertCardInternal(db, "Balance of Ghyran", "Upgrade", null, 23, (int)huntingGroundsDeckId, 1, "Equilibrium: Immediately after an enemy fighter's successful Attack, if this fighter was the target of that Attack and the attacker is within 3 hexes of them, roll 3 Attack dice. If the roll contains any Swords, inflict 1 damage on the attacker.", null, "card_hg23");
        insertCardInternal(db, "Blocked!", "Upgrade", null, 24, (int)huntingGroundsDeckId, 1, "Hinder: The first time each enemy fighter enters a hex adjacent to this fighter as part of a Move, roll an Attack dice. On a Hammers or Criticals, that fighter must end that Move in that hex.", null, "card_hg24");
        insertCardInternal(db, "Bounty of Ghyran", "Upgrade", null, 25, (int)huntingGroundsDeckId, 1, "This fighter is Inspired while they are in friendly territory. Immediately after your next Action step, Uninspire this fighter and discard this card.", null, "card_hg25");
        insertCardInternal(db, "Crippling Blow", "Upgrade", null, 26, (int)huntingGroundsDeckId, 1, "Range: 2, Dice: 3 Swords, Damage: 1. If the target is in friendly territory, give that fighter a Move token.", null, "card_hg26");
        insertCardInternal(db, "Goading Defender", "Upgrade", null, 27, (int)huntingGroundsDeckId, 1, "This fighter has +1 Save while they are in friendly territory. Discard this card if this fighter is picked to be the target of a Ploy or is damaged.", null, "card_hg27");
        insertCardInternal(db, "Great Speed", "Upgrade", null, 28, (int)huntingGroundsDeckId, 1, "This fighter has +1 Move.", null, "card_hg28");
        insertCardInternal(db, "Hidden Aid", "Upgrade", null, 29, (int)huntingGroundsDeckId, 1, "Enemy fighters adjacent to this fighter are Flanked.", null, "card_hg29");
        insertCardInternal(db, "Hidden Traps", "Upgrade", null, 30, (int)huntingGroundsDeckId, 1, "Range: 1, Dice: 3 Hammers, Damage: 1. This weapon has Grievous if the target is within 1 hex of a feature token in friendly territory.", null, "card_hg30");
        insertCardInternal(db, "Killing Blow", "Upgrade", null, 31, (int)huntingGroundsDeckId, 1, "This fighter's melee weapons have Grievous if the target is damaged.", null, "card_hg31");
        insertCardInternal(db, "True Grit", "Upgrade", null, 32, (int)huntingGroundsDeckId, 1, "This Upgrade can only be given to your leader. Rolls of Block and Dodge count as successes in Save rolls for friendly fighters adjacent to this fighter.", null, "card_hg32");

        // --- Inserción de Reckless Fury Rivals Deck ---
        long recklessFuryDeckId = insertRivalDeckInternal(db, "Reckless Fury Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);

        // --- Objetivos (Objectives) de Reckless Fury ---
        insertCardInternal(db, "Aim for the Top", "Objective", "End Phase", 1, (int)recklessFuryDeckId, 1, "Score this in an end phase if the total Bounty characteristic of slain and/or damaged enemy fighters is 4 or more.", null, "card_rf1");
        insertCardInternal(db, "Arena Mortis", "Objective", "End Phase", 2, (int)recklessFuryDeckId, 2, "Score this in an end phase if all fighters in the same territory have Charge tokens and their total Bounty characteristic is 4 or more.", null, "card_rf2");
        insertCardInternal(db, "Best Foot Forward", "Objective", "Surge", 3, (int)recklessFuryDeckId, 1, "Score this immediately after a friendly fighter's successful Attack if that fighter has any Charge tokens and is in enemy territory.", null, "card_rf3");
        insertCardInternal(db, "Bloodbathed Rampager", "Objective", "End Phase", 4, (int)recklessFuryDeckId, 1, "Score this in an end phase if a friendly fighter has 2 or more Charge tokens.", null, "card_rf4");
        insertCardInternal(db, "Bloody Momentum", "Objective", "End Phase", 5, (int)recklessFuryDeckId, 2, "Score this in an end phase if 2 or more friendly fighters with a total Bounty characteristic of 4 or more have Charge tokens and are in enemy territory.", null, "card_rf5");
        insertCardInternal(db, "Frenzied Rush", "Objective", "Surge", 6, (int)recklessFuryDeckId, 1, "Score this immediately after an Action step if friendly fighters with a total Bounty characteristic of 3 or more have Charge tokens and are in enemy territory.", null, "card_rf6");
        insertCardInternal(db, "Living Bludgeon", "Objective", "Surge", 7, (int)recklessFuryDeckId, 1, "Score this immediately after an Action step if a friendly fighter has a Guard token and a Charge token and is not in friendly territory.", null, "card_rf7");
        insertCardInternal(db, "Red Aftermath", "Objective", "End Phase", 8, (int)recklessFuryDeckId, 1, "Score this in an end phase if the total Bounty characteristic of slain enemy fighters is 2 or more.", null, "card_rf8");
        insertCardInternal(db, "Sally Forth", "Objective", "Surge", 9, (int)recklessFuryDeckId, 1, "Score this immediately after your opponent's Action step if a friendly fighter with any Charge tokens holds a treasure token in enemy territory.", null, "card_rf9");
        insertCardInternal(db, "Savage Sprinter", "Objective", "Surge", 10, (int)recklessFuryDeckId, 1, "Score this immediately after an Action step if a friendly fighter in enemy territory has 2 or more Move tokens.", null, "card_rf10");
        insertCardInternal(db, "Unrelenting Massacre", "Objective", "End Phase", 11, (int)recklessFuryDeckId, 3, "Score this in an end phase if all fighters have Charge tokens.", null, "card_rf11");
        insertCardInternal(db, "Vicious Brawl", "Objective", "Surge", 12, (int)recklessFuryDeckId, 1, "Score this immediately after an Action step if there are 3 or more fighters with Charge tokens adjacent to each other. If you are the underdog, there can be 2 or more fighters instead.", null, "card_rf12");

        // --- Ardides (Gambits) de Reckless Fury ---
        insertCardInternal(db, "Braced", "Gambit", "Ploy", 13, (int)recklessFuryDeckId, null, "Pick a friendly fighter. Give that fighter a Charge token.", null, "card_rf13");
        insertCardInternal(db, "Catch Weapon", "Gambit", "Ploy", 14, (int)recklessFuryDeckId, null, "Play this immediately after a fighter's successful Attack. Give that fighter a Charge token.", null, "card_rf14");
        insertCardInternal(db, "Diving In", "Gambit", "Ploy", 15, (int)recklessFuryDeckId, null, "Pick a friendly fighter. Push that fighter up to 2 hexes. That push must end adjacent to any fighters with Charge tokens.", null, "card_rf15");
        insertCardInternal(db, "Get It Done", "Gambit", "Ploy", 16, (int)recklessFuryDeckId, null, "Pick 2 friendly fighters. Remove a Charge token from 1 of those fighters and then give the other fighter a Charge token.", null, "card_rf16");
        insertCardInternal(db, "Lost Legacy", "Gambit", "Ploy", 17, (int)recklessFuryDeckId, null, "Play this immediately after you discard a slain friendly fighter's Upgrades. Pick 1 of those Upgrades, put it in your hand then draw 1 Power card.", null, "card_rf17");
        insertCardInternal(db, "Outburst", "Gambit", "Ploy", 18, (int)recklessFuryDeckId, null, "Pick a friendly fighter with any Charge tokens. Roll an Attack dice for each enemy fighter adjacent to them. If you are the underdog, roll a number of dice equal to the battle round number instead. If the roll contains any Hammers, inflict 1 damage on that fighter.", null, "card_rf18");
        insertCardInternal(db, "Over to You", "Gambit", "Ploy", 19, (int)recklessFuryDeckId, null, "Play this immediately before removing a slain friendly fighter's tokens if that fighter was slain by an attacker and had any Move, Charge, Guard and/or Stagger tokens. Pick a friendly fighter or the attacker. Give the fighter you picked all of the slain fighter's Move, Charge, Guard and Stagger tokens.", null, "card_rf19");
        insertCardInternal(db, "Push Through", "Gambit", "Ploy", 20, (int)recklessFuryDeckId, null, "Pick a friendly fighter with a Bounty characteristic of 2 or less. Inflict 1 damage on that fighter. In your next Action step, that fighter can use Core abilities as if they did not have any Move or Charge tokens.", null, "card_rf20");
        insertCardInternal(db, "Quick Shift", "Gambit", "Ploy", 21, (int)recklessFuryDeckId, null, "Pick a friendly fighter that has any Move tokens. Remove 1 of that fighter's Move tokens and then give that fighter a Charge token.", null, "card_rf21");
        insertCardInternal(db, "Reckless Attitudes", "Gambit", "Ploy", 22, (int)recklessFuryDeckId, null, "Enemy fighters cannot use Core abilities other than the Charge ability in the next Action step.", null, "card_rf22");

        // --- Mejoras (Upgrades) de Reckless Fury ---
        insertCardInternal(db, "Bellowing Tyrant", "Upgrade", null, 23, (int)recklessFuryDeckId, 1, "Bellow: This fighter can use this Core ability if they have any Charge tokens. Push each other friendly fighter with any Charge tokens up to 2 hexes. Then, pick 1 of those fighters. Remove 1 of that fighter's Charge tokens.", null, "card_rf23");
        insertCardInternal(db, "Bladecatcher", "Upgrade", null, 24, (int)recklessFuryDeckId, 1, "While this fighter is the target of an Attack, the attacker cannot use Weapon abilities.", null, "card_rf24");
        insertCardInternal(db, "Blades of Wrath", "Upgrade", null, 25, (int)recklessFuryDeckId, 1, "If this fighter is slain, before removing them from the battlefield, roll a number of Attack dice equal to the battle round number for each enemy fighter adjacent to them. If the roll contains any Hammers, inflict 1 damage on that fighter.", null, "card_rf25");
        insertCardInternal(db, "Furious Might", "Upgrade", null, 26, (int)recklessFuryDeckId, 1, "This fighter's melee weapons have Grievous while this fighter has any Charge tokens and is not using the Charge ability.", null, "card_rf26");
        insertCardInternal(db, "Fury of Aqshy", "Upgrade", null, 27, (int)recklessFuryDeckId, 1, "Fighters adjacent to this fighter cannot use Core abilities other than the Charge ability.", null, "card_rf27");
        insertCardInternal(db, "Headcase", "Upgrade", null, 28, (int)recklessFuryDeckId, 0, "If this fighter has no Charge tokens, instead of playing a Power card in a Power step, you can give this fighter a Charge token.", null, "card_rf28");
        insertCardInternal(db, "Headlong Charge", "Upgrade", null, 29, (int)recklessFuryDeckId, 0, "This fighter has +2 Move while using the Charge ability.", null, "card_rf29");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 30, (int)recklessFuryDeckId, 2, "This fighter's melee weapons have +1 Attack dice.", null, "card_rf30");
        insertCardInternal(db, "Still Swinging", "Upgrade", null, 31, (int)recklessFuryDeckId, 1, "This fighter's melee weapons have Ensnare while this fighter has any Charge tokens and is not using the Charge ability.", null, "card_rf31");
        insertCardInternal(db, "Utter Ignorance", "Upgrade", null, 32, (int)recklessFuryDeckId, 2, "If this fighter would be slain, they are not slain. Remove damage tokens from this fighter until they are vulnerable and then discard this card.", null, "card_rf32");

        // --- Inserción de Wrack and Ruin Rivals Deck ---
        long wrackAndRuinDeckId = insertRivalDeckInternal(db, "Wrack and Ruin Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);

        // --- Objetivos (Objectives) de Wrack and Ruin ---
        insertCardInternal(db, "Alone in the Dark", "Objective", "End Phase", 1, (int)wrackAndRuinDeckId, 2, "Score this in an end phase if no fighters are adjacent.", null, "card_wr1");
        insertCardInternal(db, "Bloody and Bruised", "Objective", "Surge", 2, (int)wrackAndRuinDeckId, 1, "Score this immediately after your warband inflicts damage on an enemy fighter if 3 or more fighters are damaged.", null, "card_wr2");
        insertCardInternal(db, "Careful Advance", "Objective", "Surge", 3, (int)wrackAndRuinDeckId, 1, "Score this immediately after a friendly fighter Moves if 2 or more friendly fighters that have Move tokens are in enemy territory.", null, "card_wr3");
        insertCardInternal(db, "Living on the Edge", "Objective", "End Phase", 4, (int)wrackAndRuinDeckId, 2, "Score this in an end phase if a vulnerable friendly fighter is in enemy territory.", null, "card_wr4");
        insertCardInternal(db, "Low on Options", "Objective", "Surge", 5, (int)wrackAndRuinDeckId, 1, "Score this immediately after you discard a Power card if 5 or more Ploy cards are in your Power discard pile.", null, "card_wr5");
        insertCardInternal(db, "Out of the Frying Pan", "Objective", "End Phase", 6, (int)wrackAndRuinDeckId, 2, "Score this in an end phase if 3 or more damaged friendly fighters that have Move and/or Charge tokens are in enemy territory.", null, "card_wr6");
        insertCardInternal(db, "Ploymaster", "Objective", "End Phase", 7, (int)wrackAndRuinDeckId, 1, "Score this in an end phase if you played 3 or more Ploys this battle round.", null, "card_wr7");
        insertCardInternal(db, "Predictable End", "Objective", "Surge", 8, (int)wrackAndRuinDeckId, 1, "Score this immediately after an ability on a friendly Wrack and Ruin card inflicts damage on an enemy fighter holding a treasure token.", null, "card_wr8");
        insertCardInternal(db, "Spread Out!", "Objective", "End Phase", 9, (int)wrackAndRuinDeckId, 1, "Score this in an end phase if there is a friendly fighter in each territory.", null, "card_wr9");
        insertCardInternal(db, "Stay Close", "Objective", "End Phase", 10, (int)wrackAndRuinDeckId, 2, "Score this in an end phase if there are no fighters in edge hexes.", null, "card_wr10");
        insertCardInternal(db, "Strong Start", "Objective", "Surge", 11, (int)wrackAndRuinDeckId, 1, "Score this immediately after an enemy fighter is slain if that fighter was the first fighter slain this combat phase.", null, "card_wr11");
        insertCardInternal(db, "Unsafe Ground", "Objective", "Surge", 12, (int)wrackAndRuinDeckId, 1, "Score this immediately after your warband inflicts damage on an enemy fighter in an edge hex. If you are the underdog, that enemy fighter can be within 1 hex of an edge hex instead.", null, "card_wr12");

        // --- Gambitos (Ploys) de Wrack and Ruin ---
        insertCardInternal(db, "Confusion", "Ploy", null, 13, (int)wrackAndRuinDeckId, 0, "Pick 2 adjacent fighters. Remove those fighters from the battlefield and then place each in the hex the other was removed from.", null, "card_wr13");
        insertCardInternal(db, "Damned if You Do", "Ploy", null, 14, (int)wrackAndRuinDeckId, 0, "Your opponent must pick 1 of the following abilities for you to resolve: Pick an enemy fighter. Push that fighter 1 hex. Pick an enemy fighter that is not vulnerable. Inflict 1 damage on that fighter.", null, "card_wr14");
        insertCardInternal(db, "Deadly Traps", "Ploy", null, 15, (int)wrackAndRuinDeckId, 0, "Play this immediately after a friendly fighter's drawn Attack if the target is not vulnerable and was driven back. Inflict 1 damage on the target.", null, "card_wr15");
        insertCardInternal(db, "Fault Lines", "Ploy", null, 16, (int)wrackAndRuinDeckId, 0, "Pick an undamaged enemy fighter. Inflict 1 damage on that fighter. Then, your opponent can pick a fighter. Inflict 1 damage on that fighter.", null, "card_wr16");
        insertCardInternal(db, "Fireproof", "Ploy", null, 17, (int)wrackAndRuinDeckId, 0, "The first time damage is inflicted on a friendly fighter in the next turn, reduce that damage to 1.", null, "card_wr17");
        insertCardInternal(db, "Flee!", "Ploy", null, 18, (int)wrackAndRuinDeckId, 0, "Pick a friendly fighter in friendly territory. That fighter has +3 Move in your next Action step. If that fighter Moves in that Action step, that Move cannot end in friendly territory.", null, "card_wr18");
        insertCardInternal(db, "Ominous Rumbling", "Ploy", null, 19, (int)wrackAndRuinDeckId, 0, "Your opponent must pick 1 of the following abilities for you to resolve: Pick 2 enemy fighters. Give each of those fighters a Stagger token. Pick an enemy fighter that is not vulnerable. Inflict 1 damage on that fighter.", null, "card_wr19");
        insertCardInternal(db, "Sidle Up", "Ploy", null, 20, (int)wrackAndRuinDeckId, 0, "Pick a friendly fighter. Push that fighter up to 2 hexes. That push must end adjacent to 2 or more fighters.", null, "card_wr20");
        insertCardInternal(db, "Vicious Intent", "Ploy", null, 21, (int)wrackAndRuinDeckId, 0, "Play this immediately after you pick a melee weapon as part of an Attack. That weapon has +1 Attack dice for that Attack. If the target is undamaged, that weapon has +2 Attack dice for that Attack instead.", null, "card_wr21");
        insertCardInternal(db, "Volcanic Eruption", "Ploy", null, 22, (int)wrackAndRuinDeckId, 0, "Pick a fighter. Then your opponent can pick an enemy fighter. Starting with the fighter you picked, roll a number of Attack dice for each of those fighters equal to their Bounty characteristic, to a minimum of 1. If the roll contains any HammerSmash, inflict 1 damage on that fighter.", null, "card_wr22");

        // --- Mejoras (Upgrades) de Wrack and Ruin ---
        insertCardInternal(db, "Barge", "Upgrade", null, 23, (int)wrackAndRuinDeckId, 2, "Barge: This fighter can use this Core ability if they have no Move and/or Charge tokens. This fighter Moves. That Move must end adjacent to an enemy fighter. Give this fighter a Charge token. Then, pick an enemy fighter adjacent to this fighter. Push that fighter 1 hex then give them a Stagger token.", null, "card_wr23");
        insertCardInternal(db, "Desperate Defence", "Upgrade", null, 24, (int)wrackAndRuinDeckId, 2, "While this fighter is the target of an Attack, the attacker's weapons have -1 Damage. The next time damage is inflicted on this fighter as part on an Attack, discard this card.", null, "card_wr24");
        insertCardInternal(db, "Fiery Temper", "Upgrade", null, 25, (int)wrackAndRuinDeckId, 0, "Belligerent: Immediately after this fighter has been picked to be pushed, you can inflict 1 damage on this fighter. If you do so, this fighter is not pushed.", null, "card_wr25");
        insertCardInternal(db, "Great Speed", "Upgrade", null, 26, (int)wrackAndRuinDeckId, 0, "This fighter has +1 Move.", null, "card_wr26");
        insertCardInternal(db, "Henchman", "Upgrade", null, 27, (int)wrackAndRuinDeckId, 1, "Disciplined: Immediately after you make an Attack roll for this fighter, you can change 1 result to Single Support. You cannot re-roll Attack rolls for this fighter.", null, "card_wr27");
        insertCardInternal(db, "Misfortune", "Upgrade", null, 28, (int)wrackAndRuinDeckId, 0, "Ill-fated: Immediately after this fighter uses a Core ability, you can inflict 1 damage on this fighter. Then, you can remove this Upgrade from this fighter and equip another friendly fighter with this Upgrade.", null, "card_wr28");
        insertCardInternal(db, "Rock-splitting Tread", "Upgrade", null, 29, (int)wrackAndRuinDeckId, 1, "Stomp: Immediately after your last Action step in a battle round, pick an adjacent enemy fighter that is not vulnerable. Inflict 1 damage on that fighter. Then, roll an Attack dice. On an HammerSmash, discard this card.", null, "card_wr29");
        insertCardInternal(db, "Sundering Weapon", "Upgrade", null, 30, (int)wrackAndRuinDeckId, 1, "This fighter's melee weapons have Cleave.", null, "card_wr30");
        insertCardInternal(db, "Unstoppable", "Upgrade", null, 31, (int)wrackAndRuinDeckId, 2, "While this fighter is vulnerable, each time 1 damage is inflicted on this fighter, reduce that damage to 0.", null, "card_wr31");
        insertCardInternal(db, "Wary Tread", "Upgrade", null, 32, (int)wrackAndRuinDeckId, 1, "You can use this ability after the last Power step in a battle round. Push this fighter 1 hex. That push cannot end adjacent to any fighters.", null, "card_wr32");

        // --- Inserción de Blazing Assault Rivals Deck ---
        long blazingAssaultDeckId = insertRivalDeckInternal(db, "Blazing Assault Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);

        // --- Objetivos (Objectives) de Blazing Assault ---
        insertCardInternal(db, "Strike the Head", "Objective", "Surge", 1, (int)blazingAssaultDeckId, 1, "Score this immediately after an enemy fighter is slain by a friendly fighter if the target was a leader or the target's Health characteristic was equal to or greater than the attacker's.", null, "card_bl1");
        insertCardInternal(db, "Branching Fate", "Objective", "Surge", 2, (int)blazingAssaultDeckId, 1, "Score this immediately after you make an Attack roll that contained 3 or more dice if each result was a different symbol. If you are the underdog, the Attack roll can contain 2 or more dice instead.", null, "card_bl2");
        insertCardInternal(db, "Perfect Strike", "Objective", "Surge", 3, (int)blazingAssaultDeckId, 1, "Score this immediately after you make an Attack roll if all of the results were successes.", null, "card_bl3");
        insertCardInternal(db, "Critical Effort", "Objective", "Surge", 4, (int)blazingAssaultDeckId, 1, "Score this immediately after you make an Attack roll if any of the results was a Critical success.", null, "card_bl4");
        insertCardInternal(db, "Get Stuck In", "Objective", "Surge", 5, (int)blazingAssaultDeckId, 1, "Score this immediately after a friendly fighter's Attack if the target was in enemy territory.", null, "card_bl5");
        insertCardInternal(db, "Strong Start", "Objective", "Surge", 6, (int)blazingAssaultDeckId, 1, "Score this immediately after an enemy fighter is slain if that fighter was the first fighter slain this combat phase.", null, "card_bl6");
        insertCardInternal(db, "Keep Choppin'", "Objective", "End Phase", 7, (int)blazingAssaultDeckId, 1, "Score this in an end phase if your warband Attacked 3 or more times this combat phase.", null, "card_bl7");
        insertCardInternal(db, "Fields of Blood", "Objective", "End Phase", 8, (int)blazingAssaultDeckId, 1, "Score this in an end phase if 4 or more fighters are damaged and/or slain.", null, "card_bl8");
        insertCardInternal(db, "Go All Out", "Objective", "End Phase", 9, (int)blazingAssaultDeckId, 1, "Score this in an end phase if 5 or more fighters have Move and/or Charge tokens.", null, "card_bl9");
        insertCardInternal(db, "On the Edge", "Objective", "End Phase", 10, (int)blazingAssaultDeckId, 1, "Score this in an end phase if any enemy fighters are vulnerable.", null, "card_bl10");
        insertCardInternal(db, "Denial", "Objective", "End Phase", 11, (int)blazingAssaultDeckId, 1, "Score this in an end phase if there are no enemy fighters in friendly territory.", null, "card_bl11");
        insertCardInternal(db, "Annihilation", "Objective", "End Phase", 12, (int)blazingAssaultDeckId, 5, "Score this in an end phase if each enemy fighter is slain.", null, "card_bl12");

        // --- Ardides (Gambits) de Blazing Assault ---
        insertCardInternal(db, "Determined Effort", "Gambit", "Ploy", 13, (int)blazingAssaultDeckId, null, "Play this immediately after you pick a weapon as part of an Attack. That weapon has +1 Attack dice for that Attack. If you are the underdog, that weapon has +2 Attack dice for that Attack instead.", null, "card_bl13");
        insertCardInternal(db, "Twist the Knife", "Gambit", "Ploy", 14, (int)blazingAssaultDeckId, null, "Play this immediately after you pick a melee weapon as part of an Attack. That weapon has Grievous for that Attack.", null, "card_bl14");
        insertCardInternal(db, "Lure of Battle", "Gambit", "Ploy", 15, (int)blazingAssaultDeckId, null, "Pick 1 friendly fighter that is within 2 hexes of another fighter. Push the other fighter 1 hex closer to that friendly fighter.", null, "card_bl15");
        insertCardInternal(db, "Sidestep", "Gambit", "Ploy", 16, (int)blazingAssaultDeckId, null, "Pick a friendly fighter. Push that fighter 1 hex.", null, "card_bl16");
        insertCardInternal(db, "Commanding Stride", "Gambit", "Ploy", 17, (int)blazingAssaultDeckId, null, "Push your leader up to 3 hexes. That push must end in a starting hex.", null, "card_bl17");
        insertCardInternal(db, "Illusory Fighter", "Gambit", "Ploy", 18, (int)blazingAssaultDeckId, null, "Pick a friendly fighter. Remove that fighter from the battlefield, and then place that fighter in an empty starting hex in friendly territory.", null, "card_bl18");
        insertCardInternal(db, "Wings of War", "Gambit", "Ploy", 19, (int)blazingAssaultDeckId, null, "Play this immediately after you pick a fighter to Move. That fighter has +2 Move for that Move.", null, "card_bl19");
        insertCardInternal(db, "Shields Up!", "Gambit", "Ploy", 20, (int)blazingAssaultDeckId, null, "Pick a friendly fighter. Give that fighter a Guard token.", null, "card_bl20");
        insertCardInternal(db, "Scream of Anger", "Gambit", "Ploy", 21, (int)blazingAssaultDeckId, null, "Pick a friendly fighter. Inflict 2 damage on that fighter and then remove 1 of that fighter's Move or Charge tokens.", null, "card_bl21");
        insertCardInternal(db, "Healing Potion", "Gambit", "Ploy", 22, (int)blazingAssaultDeckId, null, "Pick a friendly fighter. Heal that fighter. If you are the underdog, you can roll a Save dice. On a Block or Critical success, heal that fighter again.", null, "card_bl22");

        // --- Mejoras (Upgrades) de Blazing Assault ---
        insertCardInternal(db, "Brawler", "Upgrade", null, 23, (int)blazingAssaultDeckId, 1, "This fighter cannot be Flanked or Surrounded.", null, "card_bl23");
        insertCardInternal(db, "Hidden Aid", "Upgrade", null, 24, (int)blazingAssaultDeckId, 1, "Enemy fighters adjacent to this fighter are Flanked.", null, "card_bl24");
        insertCardInternal(db, "Accurate", "Upgrade", null, 25, (int)blazingAssaultDeckId, 1, "Strike True: After you make an Attack roll for this fighter, you can immediately re-roll 1 Attack dice in that Attack roll.", null, "card_bl25");
        insertCardInternal(db, "Great Strength", "Upgrade", null, 26, (int)blazingAssaultDeckId, 2, "This fighter's melee weapons have Grievous.", null, "card_bl26");
        insertCardInternal(db, "Deadly Aim", "Upgrade", null, 27, (int)blazingAssaultDeckId, 1, "This fighter's weapons have Ensnare.", null, "card_bl27");
        insertCardInternal(db, "Sharpened Points", "Upgrade", null, 28, (int)blazingAssaultDeckId, 1, "This fighter's weapons have Cleave.", null, "card_bl28");
        insertCardInternal(db, "Duellist", "Upgrade", null, 29, (int)blazingAssaultDeckId, 1, "Footwork: Immediately after this fighter has Attacked, you can push this fighter 1 hex.", null, "card_bl29");
        insertCardInternal(db, "Tough", "Upgrade", null, 30, (int)blazingAssaultDeckId, 2, "No more than 3 damage can be inflicted on this fighter in the same turn.", null, "card_bl30");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 31, (int)blazingAssaultDeckId, 2, "This fighter has +1 Health.", null, "card_bl31");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 32, (int)blazingAssaultDeckId, 2, "This fighter's melee weapons have +1 Attack dice.", null, "card_bl32");

        // --- Inserción de Emberstone Sentinels Rivals Deck ---
        long emberstoneSentinelsDeckId = insertRivalDeckInternal(db, "Emberstone Sentinels Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);

        // --- Objetivos (Objectives) de Emberstone Sentinels ---
        insertCardInternal(db, "Sally Forth", "Objective", "Surge", 1, (int)emberstoneSentinelsDeckId, 1, "Score this immediately after your opponent's Action step if a friendly fighter with any Charge tokens holds a treasure token in enemy territory.", null, "card_es1");
        insertCardInternal(db, "Stand Firm", "Objective", "Surge", 2, (int)emberstoneSentinelsDeckId, 1, "Score this immediately after your opponent's Action step if a friendly fighter with any Stagger tokens holds a treasure token in enemy territory.", null, "card_es2");
        insertCardInternal(db, "Step by Step", "Objective", "Surge", 3, (int)emberstoneSentinelsDeckId, 1, "Score this immediately after your opponent's Action step if a friendly fighter with any Move tokens holds a treasure token in enemy territory. If you are the underdog, that friendly fighter can have any Charge tokens instead.", null, "card_es3");
        insertCardInternal(db, "Unassailable", "Objective", "Surge", 4, (int)emberstoneSentinelsDeckId, 1, "Score this immediately after an enemy fighter's Attack if a friendly fighter holding a treasure token was the target of that Attack.", null, "card_es4");
        insertCardInternal(db, "Aggressive Defender", "Objective", "Surge", 5, (int)emberstoneSentinelsDeckId, 1, "Score this immediately after a friendly fighter's Attack if the attacker is holding a treasure token.", null, "card_es5");
        insertCardInternal(db, "Careful Advance", "Objective", "Surge", 6, (int)emberstoneSentinelsDeckId, 1, "Score this immediately after a friendly fighter Moves if 2 or more friendly fighters that have Move tokens are in enemy territory.", null, "card_es6");
        insertCardInternal(db, "Hold Treasure Token 1 or 2", "Objective", "End Phase", 7, (int)emberstoneSentinelsDeckId, 1, "Score this in an end phase if a friendly fighter holds treasure token 1 or 2.", null, "card_es7");
        insertCardInternal(db, "Hold Treasure Token 3 or 4", "Objective", "End Phase", 8, (int)emberstoneSentinelsDeckId, 1, "Score this in an end phase if a friendly fighter holds treasure token 3 or 4.", null, "card_es8");
        insertCardInternal(db, "Hold Treasure Token 5", "Objective", "End Phase", 9, (int)emberstoneSentinelsDeckId, 1, "Score this in an end phase if a friendly fighter holds treasure token 5.", null, "card_es9");
        insertCardInternal(db, "Slow Advance", "Objective", "End Phase", 10, (int)emberstoneSentinelsDeckId, 2, "Score this in an end phase if your warband holds any treasure tokens in both neutral territory and enemy territory.", null, "card_es10");
        insertCardInternal(db, "Iron Grasp", "Objective", "End Phase", 11, (int)emberstoneSentinelsDeckId, 2, "Score this in an end phase if your warband holds all of the treasure tokens in friendly territory and/or enemy territory.", null, "card_es11");
        insertCardInternal(db, "Supremacy", "Objective", "End Phase", 12, (int)emberstoneSentinelsDeckId, 3, "Score this in an end phase if 2 or more friendly fighters with a total Bounty characteristic of 3 or more hold treasure tokens.", null, "card_es12");

        // --- Ardides (Gambits) de Emberstone Sentinels ---
        insertCardInternal(db, "Switch Things Up", "Gambit", "Ploy", 13, (int)emberstoneSentinelsDeckId, null, "Pick 2 treasure tokens. Swap the positions of those treasure tokens.", null, "card_es13");
        insertCardInternal(db, "Sidestep", "Gambit", "Ploy", 14, (int)emberstoneSentinelsDeckId, null, "Pick a friendly fighter. Push that fighter 1 hex.", null, "card_es14");
        insertCardInternal(db, "The Extra Mile", "Gambit", "Ploy", 15, (int)emberstoneSentinelsDeckId, null, "Play this immediately after a friendly fighter Moves. Push that fighter 1 hex. That push must end on a feature token.", null, "card_es15");
        insertCardInternal(db, "Settle In", "Gambit", "Ploy", 16, (int)emberstoneSentinelsDeckId, null, "Pick a friendly fighter on a feature token. Give that fighter a Guard token.", null, "card_es16");
        insertCardInternal(db, "Healing Potion", "Gambit", "Ploy", 17, (int)emberstoneSentinelsDeckId, null, "Pick a friendly fighter. Heal that fighter. If you are the underdog, you can roll a Save dice. On a Block or Critical success, heal that fighter again.", null, "card_es17");
        insertCardInternal(db, "Hidden Paths", "Gambit", "Ploy", 18, (int)emberstoneSentinelsDeckId, null, "Pick a friendly fighter in an edge hex. Remove that fighter from the battlefield, and then place that fighter in a different empty edge hex. Then, give that fighter a Move token, unless you are the underdog.", null, "card_es18");
        insertCardInternal(db, "Confusion", "Gambit", "Ploy", 19, (int)emberstoneSentinelsDeckId, null, "Pick 2 adjacent fighters. Remove those fighters from the battlefield and then place each in the hex the other was removed from.", null, "card_es19");
        insertCardInternal(db, "Hold the Line!", "Gambit", "Ploy", 20, (int)emberstoneSentinelsDeckId, null, "Fighters cannot be driven back. This effect persists until the end of the next Action step.", null, "card_es20");
        insertCardInternal(db, "Shoulder Throw", "Gambit", "Ploy", 21, (int)emberstoneSentinelsDeckId, null, "Play this immediately after a friendly fighter's successful Attack if the target is adjacent. Remove the target from the battlefield, and then place them in a different empty hex adjacent to the attacker.", null, "card_es21");
        insertCardInternal(db, "By the Numbers", "Gambit", "Ploy", 22, (int)emberstoneSentinelsDeckId, null, "Draw a number of Power cards equal to the number of treasure tokens held by your warband.", null, "card_es22");

        insertCardInternal(db, "Stubborn to the End", "Upgrade", null, 23, (int)emberstoneSentinelsDeckId, 1, "If this fighter is the target of an Attack, the attacker cannot use Overrun.", null, "card_es23");
        insertCardInternal(db, "Inviolate", "Upgrade", null, 24, (int)emberstoneSentinelsDeckId, 1, "This fighter cannot be Flanked or Surrounded while they hold a treasure token.", null, "card_es24");
        insertCardInternal(db, "Great Speed", "Upgrade", null, 25, (int)emberstoneSentinelsDeckId, 0, "This fighter has +1 Move.", null, "card_es25");
        insertCardInternal(db, "Sharp Reflexes", "Upgrade", null, 26, (int)emberstoneSentinelsDeckId, 2, "This fighter has +1 Save, to a maximum of 2.", null, "card_es26");
        insertCardInternal(db, "Brute Momentum", "Upgrade", null, 27, (int)emberstoneSentinelsDeckId, 1, "This fighter cannot be driven back while they have any Charge tokens.", null, "card_es27");
        insertCardInternal(db, "Agile", "Upgrade", null, 28, (int)emberstoneSentinelsDeckId, 2, "Deft: After you make a Save roll for this fighter, you can immediately re-roll 1 Save dice in that Save roll.", null, "card_es28");
        insertCardInternal(db, "Duellist", "Upgrade", null, 29, (int)emberstoneSentinelsDeckId, 1, "Footwork: Immediately after this fighter has Attacked, you can push this fighter 1 hex.", null, "card_es29");
        insertCardInternal(db, "Great Fortitude", "Upgrade", null, 30, (int)emberstoneSentinelsDeckId, 2, "This fighter has +1 Health.", null, "card_es30");
        insertCardInternal(db, "Keen Eye", "Upgrade", null, 31, (int)emberstoneSentinelsDeckId, 2, "This fighter's melee weapons have +1 Attack dice.", null, "card_es31");
        insertCardInternal(db, "Great Strength", "Upgrade", null, 32, (int)emberstoneSentinelsDeckId, 2, "This fighter's melee weapons have Grievous.", null, "card_es32");

        long pillageAndPlunderDeckId = insertRivalDeckInternal(db, "Pillage and Plunder Rivals Deck", "Mazo Universal", null, "UNIVERSAL", null);
        insertCardInternal(db, "Broken Prospects", "Objective", "End Phase", 1, (int)pillageAndPlunderDeckId, 2, "Score this in an end phase if 3 or more different treasure tokens were Delved by your warband this battle round or if a treasure token held by an enemy fighter at the start of the battle round was Delved by your warband this battle round.", null, "card_pl1");
        insertCardInternal(db, "Against the Odds", "Objective", "End Phase", 2, (int)pillageAndPlunderDeckId, 1, "Score this in an end phase if an odd-numbered treasure token was Delved by your warband this battle round.", null, "card_pl2");
        insertCardInternal(db, "Lost in the Depths", "Objective", "End Phase", 3, (int)pillageAndPlunderDeckId, 1, "Score this in an end phase if no friendly fighters are adjacent and any friendly fighters are not slain.", null, "card_pl3");
        insertCardInternal(db, "Desolate Homeland", "Objective", "End Phase", 4, (int)pillageAndPlunderDeckId, 1, "Score this in an end phase if there are 1 or fewer treasure tokens in friendly territory.", null, "card_pl4");
        insertCardInternal(db, "Torn Landscape", "Objective", "End Phase", 5, (int)pillageAndPlunderDeckId, 2, "Score this in an end phase if there are 2 or fewer treasure tokens on the battlefield.", null, "card_pl5");
        insertCardInternal(db, "Strip the Realm", "Objective", "End Phase", 6, (int)pillageAndPlunderDeckId, 3, "Score this in an end phase if there are no treasure tokens on the battlefield or if no enemy fighters hold any treasure tokens.", null, "card_pl6");
        insertCardInternal(db, "Aggressive Claimant", "Objective", "Surge", 7, (int)pillageAndPlunderDeckId, 1, "Score this immediately after a friendly fighter's successful Attack if the target was in neutral territory, or the target was holding a treasure token when you picked them to be the target of that Attack and is no longer holding that treasure token.", null, "card_pl7");
        insertCardInternal(db, "Claim the Prize", "Objective", "Surge", 8, (int)pillageAndPlunderDeckId, 1, "Score this immediately after a friendly fighter Delves in enemy territory. If you are the underdog, that Delve can be in friendly territory instead.", null, "card_pl8");
        insertCardInternal(db, "Delving for Wealth", "Objective", "Surge", 9, (int)pillageAndPlunderDeckId, 1, "Score this immediately after your warband Delves for the third or subsequent time this combat phase.", null, "card_pl9");
        insertCardInternal(db, "Share the Load", "Objective", "Surge", 10, (int)pillageAndPlunderDeckId, 1, "Score this immediately after a friendly fighter Moves, if that fighter and any other friendly fighters are each on feature tokens.", null, "card_pl10");
        insertCardInternal(db, "Hostile Takeover", "Objective", "Surge", 11, (int)pillageAndPlunderDeckId, 1, "Score this immediately after the second or subsequent Attack made by your warband that was not part of a Charge.", null, "card_pl11");
        insertCardInternal(db, "Careful Survey", "Objective", "Surge", 12, (int)pillageAndPlunderDeckId, 1, "Score this immediately after an Action step if there is a friendly fighter in each territory.", null, "card_pl12");

        insertCardInternal(db, "Sidestep", "Gambit", "Ploy", 13, (int)pillageAndPlunderDeckId, null, "Pick a friendly fighter. Push that fighter 1 hex.", null, "card_pl13");
        insertCardInternal(db, "Prideful Duellist", "Gambit", "Ploy", 14, (int)pillageAndPlunderDeckId, null, "Play this immediately after a friendly fighter's Attack if the attacker is in enemy territory. Heal the attacker.", null, "card_pl14");
        insertCardInternal(db, "Commanding Stride", "Gambit", "Ploy", 15, (int)pillageAndPlunderDeckId, null, "Push your leader up to 3 hexes. That push must end in a starting hex.", null, "card_pl15");
        insertCardInternal(db, "Crumbling Mine", "Gambit", "Ploy", 16, (int)pillageAndPlunderDeckId, null, "Pick a treasure token that is not held. Flip that treasure token.", null, "card_pl16");
        insertCardInternal(db, "Explosive Charges", "Gambit", "Ploy", 17, (int)pillageAndPlunderDeckId, null, "Domain: Friendly fighters have +1 Move while using Charge abilities. This effect persists until the end of the battle round or until another Domain card is played.", null, "card_pl17");
        insertCardInternal(db, "Wary Delver", "Gambit", "Ploy", 18, (int)pillageAndPlunderDeckId, null, "Pick a friendly fighter with any Charge tokens. Give that fighter a Guard token.", null, "card_pl18");
        insertCardInternal(db, "Brash Scout", "Gambit", "Ploy", 19, (int)pillageAndPlunderDeckId, null, "Play this immediately after you make an Attack roll for a fighter in enemy territory. Re-roll 1 dice in that Attack roll. If you are the underdog, you can re-roll each dice in that Attack roll instead.", null, "card_pl19");
        insertCardInternal(db, "Sudden Blast", "Gambit", "Ploy", 20, (int)pillageAndPlunderDeckId, null, "Pick an enemy fighter adjacent to a friendly fighter. Give that enemy fighter a Stagger token.", null, "card_pl20");
        insertCardInternal(db, "Tunnelling Terror", "Gambit", "Ploy", 21, (int)pillageAndPlunderDeckId, null, "Pick a friendly fighter with no Move or Charge tokens. Remove that fighter from the battlefield, and then place that fighter in an empty stagger hex. Then, give that fighter a Charge token. If you are the underdog, you can give that fighter a Move token instead.", null, "card_pl21");
        insertCardInternal(db, "Trapped Cache", "Gambit", "Ploy", 22, (int)pillageAndPlunderDeckId, null, "Pick an undamaged enemy fighter within 1 hex of a treasure token. Inflict 1 damage on that fighter.", null, "card_pl22");

        insertCardInternal(db, "Great Speed", "Upgrade", null, 23, (int)pillageAndPlunderDeckId, 0, "This fighter has +1 Move.", null, "card_pl23");
        insertCardInternal(db, "Swift Step", "Upgrade", null, 24, (int)pillageAndPlunderDeckId, 1, "Quick: Immediately after this fighter has Charged, you can push this fighter 1 hex.", null, "card_pl24");
        insertCardInternal(db, "Burrowing Strike", "Upgrade", null, 25, (int)pillageAndPlunderDeckId, 1, "Melee Attack action. Range 2, 2 Dice, 2 Damage. This weapon has +1 Attack dice while this fighter has any Stagger tokens or is on a feature token.", null, "card_pl25");
        insertCardInternal(db, "Tough Enough", "Upgrade", null, 26, (int)pillageAndPlunderDeckId, 1, "While this fighter is in enemy territory, Save rolls for this fighter cannot be affected by Cleave and Ensnare.", null, "card_pl26");
        insertCardInternal(db, "Canny Sapper", "Upgrade", null, 27, (int)pillageAndPlunderDeckId, 0, "Sneaky: Immediately after you play a Ploy in a Power step, you can remove this fighter from the battlefield. Place this fighter in an empty stagger hex or in an empty starting hex in friendly territory, and then discard this card.", null, "card_pl27");
        insertCardInternal(db, "Impossibly Quick", "Upgrade", null, 28, (int)pillageAndPlunderDeckId, 1, "This fighter has +1 Save. Immediately discard this Upgrade after an enemy fighter's failed Attack if this fighter was the target.", null, "card_pl28");
        insertCardInternal(db, "Linebreaker", "Upgrade", null, 29, (int)pillageAndPlunderDeckId, 1, "This fighter's weapons have Brutal.", null, "card_pl29");
        insertCardInternal(db, "Excavating Blast", "Upgrade", null, 30, (int)pillageAndPlunderDeckId, 1, "Ranged Attack action. Range 3, 2 Dice, 1 Damage. This weapon has Stagger while this fighter is in enemy territory.", null, "card_pl30");
        insertCardInternal(db, "Gloryseeker", "Upgrade", null, 31, (int)pillageAndPlunderDeckId, 1, "This fighter's melee weapons have Grievous if the target has a Health characteristic of 4 or more.", null, "card_pl31");
        insertCardInternal(db, "Frenzy of Greed", "Upgrade", null, 32, (int)pillageAndPlunderDeckId, 2, "While this fighter is on a treasure token in enemy territory or is in a stagger hex, Save rolls for this fighter are not affected by Cleave and Ensnare and this fighter cannot be given Stagger tokens.", null, "card_pl32");


        long zarbagBandId = insertBandInternal(db, "Zarbag's Gitz",
                "Inspire: Each time you gain a sixth or subsequent Glory point, Inspire a friendly fighter. \n" +
                "Squig Herder: After you deploy a friendly Drizgit, you must immediately place each friendly Squig in an empty hex adjacent to them that is not a starting hex and that does not contain a feature token. \n" +
                "Spinnin': Snirk cannot be given Guard tokens. Each time a fighter is placed in, is pushed into or enters a hex adjacent to a friendly Snirk, inflict 1 damage on that fighter. \n" +
                "Volley: If a friendly fighter's weapon has the Volley runemark, they have access to the Volley Weapon ability. \n" +
                "Slippery Gitz: After picking a friendly fighter to Move, pick a friendly Grot with no Move/Charge tokens adjacent to them. After the first fighter Moves, that Grot can Move (1/turn). \n" +
                "Fungal Burst: After a friendly Grot is slain by an adjacent attacker, give the attacker a Stagger token (1/game). \n" +
                "Gang Up: In a Power step, friendly melee weapons have +1 Attack dice for each other friendly Grot adjacent to the target in the next turn (1/game). \n" +
                "Make Some Noise!: In a Power step, push each friendly Squig up to 2 hexes. They cannot hold treasure or Delve this round (1/game).",
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
                "Inspire: Immediately after you resolve a 'The Captain's Treasure' Power card, Inspire Gorlok Blackpowder. After an enemy fighter is slain by Gorlok, Inspire each other friendly fighter. \n" +
                "The Captain's Treasure: Start with 2 gold tokens. Gain 1 when an enemy is slain by Gorlok. \n" +
                "Spend Gold: \n" +
                "- 1 Gold: Re-roll 1 Attack dice. \n" +
                "- 1 Gold: Push a friendly minion 2 hexes. \n" +
                "- 2 Gold: Heal 1 Gorlok. \n" +
                "Light the Fuse: Peggz and Mange can use the tracker value for their ranged weapons. \n" +
                "Monkey Business: Kagey can move through enemy fighters.",
                "Destruccion", "blackpowders_buccaneers_0");

        insertFighterInternal(db, (int)blackpowderBandId, "Gorlok Blackpowder", "blackpowders_buccaneers_1");
        insertFighterInternal(db, (int)blackpowderBandId, "Kagey", "blackpowders_buccaneers_2");
        insertFighterInternal(db, (int)blackpowderBandId, "Peggz", "blackpowders_buccaneers_3");
        insertFighterInternal(db, (int)blackpowderBandId, "Mange", "blackpowders_buccaneers_4");
        insertFighterInternal(db, (int)blackpowderBandId, "Shreek", "blackpowders_buccaneers_5");
        long bloodBullBandId = insertBandInternal(db, "Blood of the Bull",
                "Inspire: When you use a daemonforge dice in the Attack or Save roll for a friendly fighter, Inspire that fighter. \n" +
                "Also, after an Attack by a friendly Chaos Duardin, if the target was adjacent to a friendly Grisk, Inspire that Grisk. \n" +
                "Empowered by Spite: Start with 1 daemonforge dice. Power step: if fighter Delves a treasure token (no Stagger), gain 1 dice. Lost at end of round. \n" +
                "Enhance Arsenal: Use 1 daemonforge dice for +1 Attack dice. \n" +
                "Reinforce Armour: Use 1 daemonforge dice for +1 Save. \n" +
                "Infernal Conflagration: Tokkor's ranged weapons have Grievous if target is adjacent. \n" +
                "Hobgrot Cunning: Hammer and Sword are successes for Chaos Duardin if target is adjacent to Grisk.",
                "Caos", "blood_of_the_bull_0");

        insertFighterInternal(db, (int)bloodBullBandId, "Zuldrakka the Hateful", "blood_of_the_bull_1");
        insertFighterInternal(db, (int)bloodBullBandId, "Morudo", "blood_of_the_bull_2");
        insertFighterInternal(db, (int)bloodBullBandId, "Imindrin", "blood_of_the_bull_3");
        insertFighterInternal(db, (int)bloodBullBandId, "Tokkor the Immolator", "blood_of_the_bull_4");

        // Grisk Back-stabba
        insertFighterInternal(db, (int)bloodBullBandId, "Grisk Back-stabba", "blood_of_the_bull_5");

        // --- Inserción de Brethren of the Bolt ---
        long brethrenBandId = insertBandInternal(db, "Brethren of the Bolt",
                "Inspire: When this fighter makes a successful Attack action with a Range 3+ weapon, or when a friendly fighter makes a successful Attack action with a Range 3+ weapon and this fighter is adjacent to the target.",
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

        // --- Inserción de Cyreni's Razors (Ampliación) ---
        long cyreniBandId = insertBandInternal(db, "Cyreni's Razors",
                "Inspire: At the end of an action phase, if there are no enemy fighters in your territory.",
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
                "Inspirar: Cuando este guerrero realiza una acción de Ataque que tiene como objetivo a un guerrero con uno o más contadores de Herida. \n" +
                "Astucia Kruleboy: Reacciones y trucos sucios para controlar el tablero.",
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
                "Inspire: Daggok: deals damage to a second or subsequent fighter in the same phase. Hurrk: a friendly fighter he supports makes a successful attack. Grakk: makes the Snag reaction. Jagz: an enemy fighter with 1+ Wounds is taken out of action.",
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
                "Inspire: After this fighter delves. Trophy Huntaz: Pick enemy (3+ HP) at start, +1 Attack die vs them. Da Bait: If minion slain, push leader towards enemy.",
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

        // --- Inserción de Elathain's Soulraid ---
        long elathainBandId = insertBandInternal(db, "Elathain's Soulraid",
                "Inspirar: Todos los guerreros se inspiran en la Ronda 2. \n" +
                "Marea Creciente: Pueden mover/cargar incluso con ficha de movimiento.",
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
                "Inspire: A wound token is removed from this fighter (even if they have none).",
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
                "Inspire: Varies by fighter.",
                "Orden", "the_emberwatch_0");

        // Ardorn Flamerunner
        insertFighterInternal(db, (int)emberwatchBandId, "Ardorn Flamerunner", "the_emberwatch_1");

        // Farasa Twice-Risen
        insertFighterInternal(db, (int)emberwatchBandId, "Farasa Twice-Risen", "the_emberwatch_2");

        // Yurik Velzaine
        insertFighterInternal(db, (int)emberwatchBandId, "Yurik Velzaine", "the_emberwatch_3");

        // --- Inserción de Zikkit's Tunnelpack ---
        long zikkitBandId = insertBandInternal(db, "Zikkit's Tunnelpack",
                "Inspire: Varies by fighter (often related to upgrades or damage).",
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
                "Inspire: Attack fails or wound token removed or fighter out of action.",
                "Chaos", "the_thricefold_discord_0");

        // Vexmor
        insertFighterInternal(db, (int)thricefoldBandId, "Vexmor", "the_thricefold_discord_1");

        // Vashtiss the Coiled
        insertFighterInternal(db, (int)thricefoldBandId, "Vashtiss the Coiled", "the_thricefold_discord_2");

        // Lascivyr the Bladed Blessing
        insertFighterInternal(db, (int)thricefoldBandId, "Lascivyr", "the_thricefold_discord_3");

        // --- Inserción de Jaws of Itzl ---
        long jawsBandId = insertBandInternal(db, "Jaws of Itzl",
                "Inspire: Savage Mauling or other conditions.",
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
                "Inspire: Spread the garden.",
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
                "Inspire: Prophecy fulfilled.",
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
                "Inspirar: Recolecta 3+ contadores de Diezmo.\n" +
                "Diezmo de Hueso: Después de que un luchador enemigo sea eliminado, gana un número de fichas de diezmo de hueso igual a su característica de Recompensa. Después de tu paso de Acción, puedes elegir un luchador amigo y retirar un número de fichas de diezmo de hueso igual a su característica de Recompensa. Entonces, Inspira a ese luchador.\n" +
                "Avance Mortek: Usa esto inmediatamente después de tu paso de Acción si Kainan o Khenta amigos usaron una habilidad Principal en ese paso de Acción. Cada esbirro amigo puede Moverse, pero cada uno debe terminar ese Movimiento adyacente a 1 o más luchadores amigos.\n" +
                "Ultimátum Temible: Usa esto inmediatamente después de tu paso de Acción. Los luchadores amigos no pueden ser elegidos como objetivo de un Ataque en el siguiente paso de Acción a menos que sean tu líder. Solo puedes usar esta habilidad una vez por partida.\n" +
                "Filas Inquebrantables: Usa esto inmediatamente después de elegir un arma como parte de un Ataque cuerpo a cuerpo. Si el objetivo está Flanqueado y/o Rodeado, ese arma tiene Heridas Graves para ese Ataque. Solo puedes usar esta habilidad una vez por partida.\n" +
                "Postura de Monolito: Usa esto inmediatamente después de tu paso de Acción. Elige un número de esbirros amigos igual o menor al número de ronda de batalla. Dale a cada uno de esos luchadores una ficha de Guardia. Solo puedes usar esta habilidad una vez por partida.\n" +
                "Asalto Implacable: Usa esto inmediatamente después de tu paso de Acción. Elige un esbirro amigo con cualquier ficha de Movimiento y/o Carga. Retira las fichas de Movimiento y/o Carga de ese luchador. Solo puedes usar esta habilidad una vez por partida.",
                "Ossiarch Bonereapers", "kainans_reapers_0");

        // Mir Kainan
        insertFighterInternal(db, (int)kainanBandId, "Mir Kainan", "kainans_reapers_1");

        // Binar Khenta
        insertFighterInternal(db, (int)kainanBandId, "Binar Khenta", "kainans_reapers_2");

        // Morteks (Generic stats for simplicity, referencing specific names)
        String[] morteks = {"Senha", "Karu", "Nohem", "Hakor"};
        int mortekImgIdx = 3;
        for (String mortekName : morteks) {
            insertFighterInternal(db, (int)kainanBandId, mortekName, "kainans_reapers_" + mortekImgIdx);
            mortekImgIdx++; // Assuming images are sequential or reused
        }

        // --- Inserción de Khagra's Ravagers ---
        long khagraBandId = insertBandInternal(db, "Khagra's Ravagers",
                "Inspirar: Después del último paso de Acción de una ronda de batalla, si hay 2 o más fichas de objetivo profanadas, Inspira a cada luchador amigo. \n" +
                "Reino Arrasado: Puedes usar esta habilidad una vez por paso de Poder. Elige una ficha de objetivo controlada por un luchador amigo. Esa ficha de objetivo queda profanada hasta que sea volteada. \n" +
                "Profanación Ritual: Inmediatamente después de que un luchador enemigo sea eliminado, si el atacante o el objetivo controlaban una ficha de objetivo, puedes elegir esa ficha de objetivo. Esa ficha de objetivo queda profanada hasta que sea volteada. \n" +
                "Aprobación Oscura: Usa esto en un paso de Poder. Roba 1 carta de Poder por cada ficha de objetivo profanada. \n" +
                "El Ojo de los Dioses: Elige 1 de lo siguiente: +1 a la Defensa para un objetivo amigo que controle un objetivo, o +1 Dado de ataque para un arma que tenga como objetivo a un enemigo que controle un objetivo. \n" +
                "Avance Arrasador: Usa esto en un paso de Poder. Elige un luchador amigo. Empuja a ese luchador hasta un número de hexágonos igual al número de fichas de objetivo profanadas.",
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
                "Inspire: Attack from friendly territory.",
                "Sylvaneth", "kurnoths_heralds_0");

        // Ylarin
        insertFighterInternal(db, (int)kurnothBandId, "Ylarin", "kurnoths_heralds_1");

        // Cullon
        insertFighterInternal(db, (int)kurnothBandId, "Cullon", "kurnoths_heralds_2");

        // Lenwythe
        insertFighterInternal(db, (int)kurnothBandId, "Lenwythe", "kurnoths_heralds_3");

        // --- Inserción de Mollog's Mob ---
        long mollogBandId = insertBandInternal(db, "Mollog's Mob",
                "Inspire: Mollog takes 3+ damage.",
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
                "Inspire: Raise enough Inspired Minions.",
                "Disciples of Tzeentch", "ephilims_pandaemonium_0"); // Assuming image exists

        // Ephilim
        insertFighterInternal(db, (int)ephilimBandId, "Ephilim the Unknowable", "ephilims_pandaemonium_1");

        // Minions (Apo'trax, Spawnmaw, Kindlefinger, Flamespooler)
        String[] ephilimMinions = {"Apo'trax", "Spawnmaw", "Kindlefinger", "Flamespooler"};
        int ephMinIdx = 2;
        for (String minion : ephilimMinions) {
            insertFighterInternal(db, (int)ephilimBandId, minion, "ephilims_pandaemonium_" + ephMinIdx);
            ephMinIdx++;
        }


        // --- Inserción de Gorechosen of Dromm ---
        long gorechosenBandId = insertBandInternal(db, "Gorechosen of Dromm",
                "Inspire: Ultimate act of violence.",
                "Blades of Khorne", "gorechosen_of_dromm_0");

        // Dromm
        insertFighterInternal(db, (int)gorechosenBandId, "Dromm, Wounder of Worlds", "gorechosen_of_dromm_1");

        // Gorehulk
        insertFighterInternal(db, (int)gorechosenBandId, "Gorehulk", "gorechosen_of_dromm_2");

        // Herax
        insertFighterInternal(db, (int)gorechosenBandId, "Herax", "gorechosen_of_dromm_3");

        // --- Inserción de Grinkrak's Looncourt ---
        long grinkrakBandId = insertBandInternal(db, "Grinkrak's Looncourt",
                "Inspire: Complete Quest objectives.",
                "Gloomspite Gitz", "grinkraks_looncourt_0");

        // Grinkrak
        insertFighterInternal(db, (int)grinkrakBandId, "Grinkrak the Great", "grinkraks_looncourt_1");

        // The Looncourt (Generic stats for minions)
        String[] looncourt = {"Snorbo da Spore", "Pokin' Snark", "Pointy Burk", "Moonface Nagz", "Skolko and Pronk", "Grib"};
        int loonIdx = 2;
        for (String loon : looncourt) {
            insertFighterInternal(db, (int)grinkrakBandId, loon, "grinkraks_looncourt_" + loonIdx);
            loonIdx++;
        }

        // --- Inserción de Hexbane's Hunters ---
        long hexbaneBandId = insertBandInternal(db, "Hexbane's Hunters",
                "Inspire: (Hexbane) Attack takes enemy out of action. (Others) Various conditions linked to Hexbane or deaths.",
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
                "Inspire: Roll a Crit in attack or defence.",
                "Stormcast Eternals", "ironsouls_condemners_0");

        // Gwynne Ironsoul
        insertFighterInternal(db, (int)ironsoulBandId, "Gwynne Ironsoul", "ironsouls_condemners_1");

        // Brodus Blightbane
        insertFighterInternal(db, (int)ironsoulBandId, "Brodus Blightbane", "ironsouls_condemners_2");

        // Tavian of Sarnassus
        insertFighterInternal(db, (int)ironsoulBandId, "Tavian of Sarnassus", "ironsouls_condemners_3");

        // --- Inserción de Morgok's Krushas ---
        long morgokBandId = insertBandInternal(db, "Morgok's Krushas",
                "Inspire: Have 2+ WAAAGH! counters.",
                "Ironjawz", "morgoks_krushas_0");

        // Morgok
        insertFighterInternal(db, (int)morgokBandId, "Morgok", "morgoks_krushas_1");

        // 'Ardskull
        insertFighterInternal(db, (int)morgokBandId, "'Ardskull", "morgoks_krushas_2");

        // Thugg
        insertFighterInternal(db, (int)morgokBandId, "Thugg", "morgoks_krushas_3");

        // --- Inserción de Myari's Purifiers ---
        long myariBandId = insertBandInternal(db, "Myari's Purifiers",
                "Inspire: Roll contains only successes.",
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
                "Inspirar: Rippa: Tiene 2 o más mejoras. Stabbit/Mean-Eye: El líder es objetivo de un ataque. \n" +
                "Salto Feroz: La banda puede atacar desde 6\" (en lugar de 3\") y moverse 3\" extra. \n" +
                "Mordiscos de Snarlfang: Reacción. Después de una acción de Ataque, realiza un ataque extra con el Snarlfang.",
                "Gloomspite Gitz", "rippas_snarlfangs_0");

        // Rippa Narkbad
        insertFighterInternal(db, (int)rippaBandId, "Rippa Narkbad", "rippas_snarlfangs_1");

        // Stabbit
        insertFighterInternal(db, (int)rippaBandId, "Stabbit", "rippas_snarlfangs_2");

        // Mean-Eye
        insertFighterInternal(db, (int)rippaBandId, "Mean-Eye", "rippas_snarlfangs_3");

        // --- Inserción de Sepulchral Guard ---
        long sepulchralBandId = insertBandInternal(db, "Sepulchral Guard",
                "Inspirar: Inmediatamente después de dar a un luchador amigo una ficha de Resurrección, Inspira a ese luchador. Después de dar a luchadores amigos 3+ fichas de Resurrección, Inspira a tu líder. \n" +
                "¡Adelante!: Elige al líder (sin Carga). Elige hasta otros 2 luchadores amigos. Esos luchadores se Mueven inmediatamente. \n" +
                "¡Levantaos!: Elige al líder (sin Carga). Elige hasta 2 luchadores amigos eliminados. Resucítalos y colócalos en hexágonos iniciales vacíos en tu territorio. \n" +
                "Manos Aferradoras (Una vez por partida): Después de elegir un objetivo para un Ataque, ese objetivo está Rodeado para ese Ataque. \n" +
                "Reforma Sorprendente (Una vez por partida): Paso de poder. Coloca al líder adyacente a 2 luchadores amigos. Dale al líder una ficha de Carga. \n" +
                "Metralla de Hueso (Una vez por partida): Después de que un luchador amigo (sin ficha de Resurrección) sea eliminado por un Ataque cuerpo a cuerpo, inflige 1 de daño al atacante.",
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
                "Inspirar: Realiza un ataque con Crítico o apoya a un amigo que saque Crítico. \n" +
                "Comando Mortal: Reacción. Después de un ataque exitoso de un amigo con fichas de Apoyo, otro amigo adyacente al objetivo puede atacar.",
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
                "Inspire: Make two attack actions in a phase.",
                "Stormcast Eternals", "the_farstriders_0");

        // Sanson Farstrider
        insertFighterInternal(db, (int)farstridersBandId, "Sanson Farstrider", "the_farstriders_1");

        // Almeric Eagle-Eye
        insertFighterInternal(db, (int)farstridersBandId, "Almeric Eagle-Eye", "the_farstriders_2");

        // Elias Swiftblade
        insertFighterInternal(db, (int)farstridersBandId, "Elias Swiftblade", "the_farstriders_3");

        // --- Inserción de The Headsmen's Curse ---
        long headsmenBandId = insertBandInternal(db, "The Headsmen's Curse",
                "Inspire: 3+ Condemned counters on card.",
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
                "Inspire: Attack enemy in cover or be in cover.",
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
                "Inspirar: 3+ contadores de Anca en la tarjeta de banda. \n" +
                "Recolección: Gana contadores al eliminar enemigos o controlar objetivos.",
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
                "Inspire: Hold 3 or more objectives.",
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
                "Inspire: Score a goal.",
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
                "Inspire: One of your fighters is taken out of action.",
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
                "Inspire: Varies (Ferlain upgrades vs Zondara upgrades).",
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
                "Inspire: After an Action step, if an enemy leader is slain or has 3 or more damage tokens, Inspire each friendly fighter. \n" +
                "Marked for Death: Use this immediately after making an Attack roll for a friendly fighter if the target is an enemy leader. Re-roll 1 Attack dice in that Attack roll. \n" +
                "Spiketraps and Snares: Immediately after a friendly fighter's successful melee Attack, give the target a barb token. Immediately after an enemy fighter with any barb tokens Moves, inflict 1 damage on that fighter, then remove that fighter's barb tokens. Remove all barb tokens from enemy fighters at the end of the battle round. \n" +
                "Smoke Bombs (Once per game): Immediately after a friendly fighter's Attack, give each enemy fighter adjacent to the attacker a Stagger token. \n" +
                "The Jaws Snap (Once per game): Use this in your Power step. Pick an enemy leader in enemy territory. Push that fighter 1 hex. \n" +
                "Skittering Blur (Once per game): Use this in your Power step. In the next turn, friendly assassins cannot be picked to be the target of Attacks or Ploys.",
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
                "Inspire: An enemy fighter adjacent to Hrothgorn is taken out of action.",
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
