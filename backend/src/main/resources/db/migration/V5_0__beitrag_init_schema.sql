CREATE TABLE beitrag(
    id INTEGER NOT NULL PRIMARY KEY,
    freitext VARCHAR(255) NOT NULL,
    zeitstempel TIMESTAMP NOT NULL,
    baustelle_id INTEGER NOT NULL
);