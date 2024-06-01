CREATE TABLE baustellenBessetzung(
                          personalnummer INTEGER NOT NULL Primary KEY,
                          baustellen_id VARCHAR(255) NOT NULL,
                          datum DOUBLE NOT NULL,
                          uhrzeit_von TIME NOT NULL,
                          uhrzeit_bis TIME NOT NULL
);