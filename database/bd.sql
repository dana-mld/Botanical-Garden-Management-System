CREATE DATABASE b_plante;
USE b_plante;

CREATE TABLE plante (
    id INT PRIMARY KEY AUTO_INCREMENT,
    denumire VARCHAR(100) NOT NULL,
    tip VARCHAR(50),
    specie VARCHAR(100) NOT NULL,
    carnivora BOOLEAN DEFAULT FALSE
);

INSERT INTO plante (denumire, tip, specie, carnivora) VALUES
('Trandafir', 'Floare', 'Rosa canina', FALSE),
('Lalea', 'Floare', 'Tulipa gesneriana', FALSE),
('Mușețel', 'Plantă medicinală', 'Matricaria chamomilla', FALSE),
('Lavandă', 'Plantă aromatică', 'Lavandula angustifolia', FALSE),
('Stejar', 'Copac', 'Quercus robur', FALSE),
('Brad', 'Conifer', 'Abies alba', FALSE),
('Urzică', 'Plantă medicinală', 'Urtica dioica', FALSE),
('Sunătoare', 'Plantă medicinală', 'Hypericum perforatum', FALSE),
('Viorea', 'Floare', 'Viola odorata', FALSE),
('Crin', 'Floare', 'Lilium candidum', FALSE),
('Floarea-soarelui', 'Plantă agricolă', 'Helianthus annuus', FALSE),
('Porumb', 'Plantă agricolă', 'Zea mays', FALSE),
('Grâu', 'Plantă agricolă', 'Triticum aestivum', FALSE),
('Menta', 'Plantă aromatică', 'Mentha piperita', FALSE),
('Rozmarin', 'Plantă aromatică', 'Salvia rosmarinus', FALSE),
('Busuioc', 'Plantă aromatică', 'Ocimum basilicum', FALSE),
('Drosera', 'Plantă carnivoră', 'Drosera rotundifolia', TRUE),
('Venus', 'Plantă carnivoră', 'Dionaea muscipula', TRUE),
('Nepenthes', 'Plantă carnivoră', 'Nepenthes rajah', TRUE),
('Pinguicula', 'Plantă carnivoră', 'Pinguicula vulgaris', TRUE);
INSERT INTO exemplare (plant_id, zona_gradina) VALUES
(3, 'Sera Tropicala'),
(4, 'Sera Tropicala'),
(5, 'Sera Tropicala'),
(6, 'Sera Tropicala'),
(7, 'Sera Tropicala'),
(8, 'Sera Tropicala'),
(9, 'Sera Tropicala'),
(10, 'Sera Tropicala'),
(11, 'Sera Tropicala'),
(12, 'Sera Tropicala'),
(13, 'Sera Tropicala'),
(14, 'Sera Tropicala'),
(15, 'Sera Tropicala'),
(16, 'Sera Tropicala'),
(17, 'Sera Tropicala'),
(18, 'Sera Tropicala'),
(19, 'Sera Tropicala'),
(20, 'Sera Tropicala');



CREATE DATABASE b_ex;
USE b_ex;
CREATE TABLE exemplare (
    id INT PRIMARY KEY AUTO_INCREMENT,
    plant_id INT NOT NULL, 
    zona_gradina VARCHAR(100)
);

CREATE TABLE imagini (
    id INT PRIMARY KEY AUTO_INCREMENT,
    exemplar_id INT NOT NULL,
    cale_fisier VARCHAR(255) NOT NULL,
    descriere TEXT,

    CONSTRAINT fk_exemplar
        FOREIGN KEY (exemplar_id)
        REFERENCES exemplare(id)
        ON DELETE CASCADE
);

INSERT INTO exemplare (plant_id, zona_gradina) VALUES
(1, 'Sector A - Rozarium'), 
(5, 'Sector Nord - Arbori'),
(18, 'Seră Tropicală');

INSERT INTO exemplare (plant_id, zona_gradina) VALUES
(18, 'Sera Tropicala');

INSERT INTO imagini (exemplar_id, cale_fisier, descriere) VALUES
(1, 'trandafir_boboc.jpg', 'Detaliu boboc trandafir'),
(1, 'trandafir_tufa.jpg', 'Vedere de ansamblu tufă'),
(2, 'stejar_batran.jpg', 'Stejar la începutul primăverii'),
(3, 'venus_capcana1.jpg', 'Capcană deschisă');

INSERT INTO imagini (exemplar_id, cale_fisier, descriere) VALUES
(4, 'venus_capcana2.jpg', 'Capcana deschisa');



CREATE DATABASE b_users;
USE b_users;
UPDATE users SET phone_number = '+40749559773' WHERE id = 2;
SELECT * FROM users WHERE role = 'ADMIN';
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(100) NOT NULL UNIQUE,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    role ENUM('VISITOR', 'EMPLOYEE', 'MANAGER', 'ADMIN') NOT NULL DEFAULT 'VISITOR',
    is_active BOOLEAN DEFAULT TRUE,
    phone_number VARCHAR(150) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE DATABASE b_log;
USE b_log;

CREATE TABLE notifications (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    channel ENUM('EMAIL','WHATSAPP'),
    message TEXT,
    status ENUM('PENDING', 'SENT','FAILED'),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
ALTER TABLE notifications
ADD COLUMN sent_at TIMESTAMP NULL,
ADD COLUMN error_message TEXT NULL;

CREATE TABLE export_jobs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    format ENUM('CSV','JSON','XML','DOC'),
    status ENUM('PENDING','DONE','FAILED'),
    file_path VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);




