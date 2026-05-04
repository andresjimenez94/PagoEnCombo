CREATE TABLE `usuarios` (
  `username` varchar(10) NOT NULL,
  `primernombre` varchar(45) DEFAULT NULL,
  `segundonombre` varchar(45) DEFAULT NULL,
  `primerapellido` varchar(45) DEFAULT NULL,
  `segundoapellido` varchar(45) DEFAULT NULL,
  `password` varchar(200) NOT NULL,
  `documento` varchar(10) NOT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT '1',
  `correo` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`username`),
  UNIQUE KEY `userName_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `cuentas` (
  `numero_cuenta` varchar(10) NOT NULL,
  `saldo` decimal(19,4) NOT NULL DEFAULT '0.0000',
  `fecha_activacion` datetime DEFAULT NULL,
  `estatus` int DEFAULT NULL,
  `username` varchar(10) NOT NULL,
  PRIMARY KEY (`numero_cuenta`),
  CONSTRAINT `fk_cuentas_usuarios` FOREIGN KEY (`username`) 
    REFERENCES `usuarios` (`username`) 
    ON DELETE RESTRICT ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;