CREATE SEQUENCE IF NOT EXISTS baustellenbesetzung_id_seq START WITH 5 INCREMENT BY 1;
CREATE TABLE baustellenbesetzung(
                          id INTEGER NOT NULL PRIMARY KEY,
                          personalnummer INTEGER NOT NULL,
                          baustellen_id INTEGER NOT NULL,
                          datum DOUBLE NOT NULL,
                          uhrzeit_von TIME NOT NULL,
                          uhrzeit_bis TIME NOT NULL
);