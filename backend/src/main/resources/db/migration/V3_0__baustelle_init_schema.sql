CREATE SEQUENCE IF NOT EXISTS baustelle_id_seq START WITH 5 INCREMENT BY 1;
CREATE TABLE baustelle(
                                    id INTEGER NOT NULL Primary KEY,
                                    titel VARCHAR(255) NOT NULL,
                                    name_bauherr VARCHAR(255) NOT NULL,
                                    adresse VARCHAR(255) NOT NULL,
                                    status VARCHAR(255) NOT NULL,
                                    telefon VARCHAR(255) NOT NULL,
                                    email VARCHAR(255) NOT NULL,
                                    arbeitsaufwand INTEGER,
                                    zeitstempel TIMESTAMP NOT NULL
);