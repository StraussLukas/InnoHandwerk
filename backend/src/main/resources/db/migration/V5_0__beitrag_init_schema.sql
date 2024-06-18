CREATE TABLE beitrag(
    id INTEGER NOT NULL PRIMARY KEY,
    freitext VARCHAR(255) NOT NULL,
    zeitstempel TIMESTAMP NOT NULL,
    baustelle_id INTEGER NOT NULL,
    personalnummer INTEGER NOT NULL,
    FOREIGN KEY (baustelle_id) REFERENCES baustelle(id),
    FOREIGN KEY (personalnummer) REFERENCES mitarbeiter(personalnummer)
);