SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS pmdm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pmdm;

DROP TABLE IF EXISTS videojuegos;
CREATE TABLE videojuegos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    plataforma VARCHAR(50) NOT NULL,
    imagen VARCHAR(255) NOT NULL
);

INSERT INTO videojuegos (titulo, plataforma, imagen) VALUES
('Zelda', 'Nintendo Switch', 'https://picsum.photos/id/1011/300/300'),
('Mario Kart', 'Nintendo Switch', 'https://picsum.photos/id/1015/300/300'),
('FIFA', 'PlayStation', 'https://picsum.photos/id/10/300/300'),
('Minecraft', 'PC', 'https://picsum.photos/id/11/300/300'),
('Fortnite', 'Xbox', 'https://picsum.photos/id/12/300/300'),
('Hollow Knight', 'PC', 'https://picsum.photos/id/13/300/300'),
('God of War', 'PlayStation', 'https://picsum.photos/id/14/300/300'),
('Stardew Valley', 'PC', 'https://picsum.photos/id/15/300/300');
