import sqlite3
import os

# Nombre de la base de datos
DB_NAME = 'underworlds.db'

def create_connection():
    """Crea una conexión a la base de datos SQLite"""
    conn = None
    try:
        conn = sqlite3.connect(DB_NAME)
        print(f"Conexión establecida a {DB_NAME}")
        return conn
    except sqlite3.Error as e:
        print(e)
    return None

def create_table(conn, create_table_sql):
    """Crea una tabla desde la sentencia SQL proporcionada"""
    try:
        c = conn.cursor()
        c.execute(create_table_sql)
    except sqlite3.Error as e:
        print(e)

def insert_band(conn, band):
    """Inserta una nueva banda en la tabla bands"""
    sql = ''' INSERT INTO bands(name, description, faction, image_resource)
              VALUES(?,?,?,?) '''
    cur = conn.cursor()
    cur.execute(sql, band)
    conn.commit()
    return cur.lastrowid

def insert_deck(conn, deck):
    """Inserta un nuevo mazo en la tabla decks"""
    sql = ''' INSERT INTO decks(name, band_id, description)
              VALUES(?,?,?) '''
    cur = conn.cursor()
    cur.execute(sql, deck)
    conn.commit()
    return cur.lastrowid

def main():
    # Eliminar la base de datos si existe para empezar de cero (opcional)
    if os.path.exists(DB_NAME):
        os.remove(DB_NAME)

    conn = create_connection()

    if conn is not None:
        # Crear tabla de bandas
        create_bands_table = """ CREATE TABLE IF NOT EXISTS bands (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    name TEXT NOT NULL,
                                    description TEXT,
                                    faction TEXT,
                                    image_resource TEXT
                                ); """

        # Crear tabla de mazos
        create_decks_table = """ CREATE TABLE IF NOT EXISTS decks (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    name TEXT NOT NULL,
                                    band_id INTEGER,
                                    description TEXT,
                                    FOREIGN KEY (band_id) REFERENCES bands (id)
                                ); """

        create_table(conn, create_bands_table)
        create_table(conn, create_decks_table)

        # Insertar datos iniciales de las bandas (tomados de Bands.java)
        bands_data = [
            ("Ironsoul's Condemners", 
             "Despues de hacer una tirada de ataque o defensa para un luchador amigo, si la tirada incluia un critico, inspira a ese luchador.", 
             "Orden", "ironsoul"),
            ("Hrothgorn's Mantrappers", 
             "Despues de que un luchador enemigo muera por un ataque cuerpo a cuerpo realizado por tu lider, inspira a cada luchador amigo.", 
             "Destruccion", "hrothgorn"),
            ("The Sons of Velmorn", 
             "Despues de un ataque cuerpo a cuerpo exitoso de un luchador amigo, inspira al atacante y a los luchadores amigos adyacentes al blanco.", 
             "Muerte", "velmorn"),
            ("Kamandora's Blades", 
             "Inmediatamente despues de un paso de poder, inspira a cada luchador adjacente al 'craneo digno' enemigo. Inmediatamente despues de que el 'craneo digno' enemigo muera por un luchador aliado, inspira a ese luchador.", 
             "Caos", "kamandora"),
            ("Gnarlspirit Pack", 
             "Inmediatamente despues de que quites un toquen de espiritu de un luchador amigo, inspira a ese luchador.", 
             "Caos", "gnarlspirit"),
            ("Zondara's Gravebreakers", 
             "Inmediatamente despues de darle a un luchador aliado un toquen de levantado, inspira a ese luchador. Inmediatamente despues de que le equipes a un luchador destinado aliado su segunda o subsecuente mejora, inspira al otro luchador destinado aliado.", 
             "Muerte", "zondara"),
            ("Morgok's Krushas", 
             "En una fase de poder, en vez de jugar una carta de poder, puedes elegir a un luchador aliado con tres o mas marcadores de 'Waaagh'. Retira tres 'Waaagh' de ese luchador, entonces inspiralo.", 
             "Destruccion", "morgok"),
            ("Cyreni's Razors", 
             "Inspira un numero de luchadores aliados igual a la ronda de batalla al principio de cada fase de combate.", 
             "Orden", "cyreni")
        ]

        print("Insertando bandas...")
        for band in bands_data:
            band_id = insert_band(conn, band)
            print(f"Banda insertada: {band[0]} con ID: {band_id}")

        # Ejemplo de inserción de un mazo (puedes descomentar o agregar más aquí)
        # deck_data = ("Mazo Ofensivo Básico", 1, "Un mazo centrado en el ataque para Ironsoul")
        # insert_deck(conn, deck_data)

        print("\nBase de datos creada y poblada exitosamente.")
        conn.close()
    else:
        print("Error! No se puede crear la conexión a la base de datos.")

if __name__ == '__main__':
    main()
