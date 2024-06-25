CREATE SEQUENCE IF NOT EXISTS anhangs_id_seq START WITH 6 INCREMENT BY 1;
CREATE TABLE anhang(
                        id INTEGER NOT NULL PRIMARY KEY,
                        datei VARCHAR(255) NOT NULL,
                        beitrag_id INTEGER NOT NULL,
                        FOREIGN KEY (beitrag_id) REFERENCES beitrag(id)
);