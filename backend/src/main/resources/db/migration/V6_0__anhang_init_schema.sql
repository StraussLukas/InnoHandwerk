CREATE TABLE anhang(
                        id INTEGER NOT NULL PRIMARY KEY,
                        datei VARCHAR(255) NOT NULL,
                        beitrag_id INTEGER NOT NULL,
                        FOREIGN KEY (beitrag_id) REFERENCES beitrag(id)
);