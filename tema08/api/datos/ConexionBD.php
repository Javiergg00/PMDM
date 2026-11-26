<?php
require_once __DIR__ . '/login_mysql.php';
require_once __DIR__ . '/mensajes.php';

// Se conecta a MySQL al crear el objeto y se desconecta al destruirlo
class ConexionBD
{
    private ?PDO $conexion;

    public function __construct()
    {
        $dsn = 'mysql:host=' . BD_HOST . ';dbname=' . BD_NOMBRE . ';charset=utf8mb4';
        $this->conexion = new PDO($dsn, BD_USUARIO, BD_CLAVE, [
            PDO::ATTR_ERRMODE => PDO::ERRMODE_EXCEPTION,        // si algo falla, lanza una excepción
            PDO::ATTR_DEFAULT_FETCH_MODE => PDO::FETCH_ASSOC,   // cada fila, como un array asociativo
        ]);
    }

    public function getConexion(): PDO
    {
        return $this->conexion;
    }

    // El destructor se ejecuta solo al terminar: cierra la conexión
    public function __destruct()
    {
        $this->conexion = null;
    }
}
