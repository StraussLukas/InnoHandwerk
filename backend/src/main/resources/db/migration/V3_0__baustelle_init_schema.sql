CREATE TABLE baustelle(
                                    id INTEGER NOT NULL Primary KEY,
                                    name_bauherr VARCHAR(255) NOT NULL,
                                    adresse VARCHAR(255) NOT NULL,
                                    telefon VARCHAR(255) NOT NULL,
                                    email VARCHAR(255) NOT NULL,
                                    arbeitsaufwand INTEGER
);