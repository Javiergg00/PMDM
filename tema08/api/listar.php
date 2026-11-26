<?php
// GET listar.php → todos los videojuegos en JSON
require_once __DIR__ . '/datos/ConexionBD.php';

header('Content-Type: application/json; charset=utf-8');

try {
    $bd = new ConexionBD();
    $consulta = $bd->getConexion()->query('SELECT id, titulo, plataforma, imagen FROM videojuegos ORDER BY id');
    $filas = $consulta->fetchAll();
    echo json_encode($filas, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => ERROR_CONEXION], JSON_UNESCAPED_UNICODE);
}
