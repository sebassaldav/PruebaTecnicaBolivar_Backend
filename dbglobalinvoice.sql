-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 03-09-2026 a las 21:39:55
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `dbglobalinvoice`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `invoices`
--

CREATE TABLE `invoices` (
  `id` int(11) NOT NULL,
  `consecutive` varchar(10) NOT NULL,
  `created_at` date NOT NULL,
  `created_by` varchar(50) NOT NULL,
  `iva` decimal(38,2) DEFAULT NULL,
  `subtotal` decimal(38,2) NOT NULL,
  `total` decimal(38,2) NOT NULL,
  `type` enum('EXPORTACION','GUBERNAMENTAL','NACIONAL') NOT NULL,
  `withholding` decimal(38,2) DEFAULT NULL,
  `customs_code` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `invoices`
--

INSERT INTO `invoices` (`id`, `consecutive`, `created_at`, `created_by`, `iva`, `subtotal`, `total`, `type`, `withholding`, `customs_code`) VALUES
(21, 'FAC-000020', '2026-09-02', 'operador@bolivar.com', 19000.00, 100000.00, 119000.00, 'NACIONAL', 0.00, ''),
(22, '', '2026-09-02', 'operador@bolivar.com', 19000.00, 100000.00, 119000.00, 'NACIONAL', 0.00, ''),
(23, 'FAC-000022', '2026-09-02', 'operador@bolivar.com', 19000.00, 100000.00, 119000.00, 'NACIONAL', 0.00, ''),
(24, 'FAC-000024', '2026-09-02', 'operador@bolivar.com', 0.00, 100000.00, 100000.00, 'EXPORTACION', 0.00, ''),
(25, 'FAC-000026', '2026-09-02', 'operador@bolivar.com', 19000.00, 100000.00, 113050.00, 'GUBERNAMENTAL', 5950.00, ''),
(26, 'FAC-000027', '2026-09-03', 'operador@bolivar.com', 19000.00, 100000.00, 119000.00, 'NACIONAL', 0.00, NULL),
(27, 'FAC-000028', '2026-09-03', 'operador@bolivar.com', 0.00, 120000.00, 120000.00, 'EXPORTACION', 0.00, 'CODIGO-ADUANERO'),
(28, 'FAC-000029', '2026-09-03', 'operador@bolivar.com', 38000.00, 200000.00, 226100.00, 'GUBERNAMENTAL', 11900.00, NULL),
(29, 'FAC-000030', '2026-09-03', 'operador@bolivar.com', 0.00, 2456789.00, 2456789.00, 'EXPORTACION', 0.00, 'codigoaduanero'),
(30, 'FAC-000031', '2026-09-03', 'operador@bolivar.com', 19000.00, 100000.00, 113050.00, 'GUBERNAMENTAL', 5950.00, NULL),
(31, 'FAC-000032', '2026-09-03', 'operador@bolivar.com', 47500.00, 250000.00, 297500.00, 'NACIONAL', 0.00, NULL),
(32, 'FAC-000033', '2026-09-03', 'operador@bolivar.com', 0.00, 860000.00, 860000.00, 'EXPORTACION', 0.00, 'codigo123'),
(33, 'FAC-000034', '2026-09-03', 'operador@bolivar.com', 12350.00, 65000.00, 77350.00, 'NACIONAL', 0.00, NULL),
(34, 'FAC-000035', '2026-09-03', 'operador@bolivar.com', 0.00, 175000.00, 175000.00, 'EXPORTACION', 0.00, '60010001'),
(35, 'FAC-000036', '2026-09-03', 'operador@bolivar.com', 23750.00, 125000.00, 142500.00, 'GUBERNAMENTAL', 6250.00, NULL),
(36, 'FAC-000037', '2026-09-03', 'operador@bolivar.com', 30400.00, 160000.00, 190400.00, 'NACIONAL', 0.00, NULL),
(37, 'FAC-000038', '2026-09-03', 'operador@bolivar.com', 0.00, 310000.00, 310000.00, 'EXPORTACION', 0.00, '60010002'),
(38, 'FAC-000039', '2026-09-03', 'operador@bolivar.com', 39900.00, 210000.00, 239400.00, 'GUBERNAMENTAL', 10500.00, NULL),
(39, 'FAC-000040', '2026-09-03', 'operador@bolivar.com', 57000.00, 300000.00, 357000.00, 'NACIONAL', 0.00, NULL),
(40, 'FAC-000041', '2026-09-03', 'operador@bolivar.com', 0.00, 450000.00, 450000.00, 'EXPORTACION', 0.00, '60010003'),
(41, 'FAC-000042', '2026-09-03', 'operador@bolivar.com', 68400.00, 360000.00, 410400.00, 'GUBERNAMENTAL', 18000.00, NULL),
(42, 'FAC-000043', '2026-09-03', 'operador@bolivar.com', 83600.00, 440000.00, 523600.00, 'NACIONAL', 0.00, NULL),
(43, 'FAC-000044', '2026-09-03', 'operador@bolivar.com', 0.00, 575000.00, 575000.00, 'EXPORTACION', 0.00, '60010004'),
(44, 'FAC-000045', '2026-09-03', 'operador@bolivar.com', 104500.00, 550000.00, 627000.00, 'GUBERNAMENTAL', 27500.00, NULL),
(45, 'FAC-000046', '2026-09-03', 'operador@bolivar.com', 133000.00, 700000.00, 833000.00, 'NACIONAL', 0.00, NULL),
(46, 'FAC-000047', '2026-09-03', 'operador@bolivar.com', 0.00, 825000.00, 825000.00, 'EXPORTACION', 0.00, '60010005'),
(47, 'FAC-000048', '2026-09-03', 'operador@bolivar.com', 161500.00, 850000.00, 969000.00, 'GUBERNAMENTAL', 42500.00, NULL),
(48, 'FAC-000049', '2026-09-03', 'operador@bolivar.com', 199500.00, 1050000.00, 1249500.00, 'NACIONAL', 0.00, NULL),
(49, 'FAC-000050', '2026-09-03', 'operador@bolivar.com', 0.00, 1250000.00, 1250000.00, 'EXPORTACION', 0.00, '60010006'),
(50, 'FAC-000051', '2026-09-03', 'operador@bolivar.com', 218500.00, 1150000.00, 1311000.00, 'GUBERNAMENTAL', 57500.00, NULL),
(51, 'FAC-000052', '2026-09-03', 'operador@bolivar.com', 266000.00, 1400000.00, 1666000.00, 'NACIONAL', 0.00, NULL),
(52, 'FAC-000053', '2026-09-03', 'operador@bolivar.com', 0.00, 1600000.00, 1600000.00, 'EXPORTACION', 0.00, '60010007');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `roles`
--

CREATE TABLE `roles` (
  `id` int(11) NOT NULL,
  `nombre` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `roles`
--

INSERT INTO `roles` (`id`, `nombre`) VALUES
(1, 'AUDITOR'),
(2, 'OPERADOR');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `active` bit(1) NOT NULL,
  `email` varchar(70) NOT NULL,
  `password` varchar(150) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `users`
--

INSERT INTO `users` (`id`, `active`, `email`, `password`) VALUES
(1, b'1', 'auditor@bolivar.com', '$2a$10$Dy/JUNkJLhF1BY7.hgG9P.WYE9z5iKtssZRkiYAhuy8UqeCklYEfi'),
(2, b'1', 'operador@bolivar.com', '$2a$10$1XBhkMiDGPLvjdhvtLy8FexKr3w3Z86Eol7Vf2FmEkXExqjaLxN3W');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `user_roles`
--

CREATE TABLE `user_roles` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_spanish_ci;

--
-- Volcado de datos para la tabla `user_roles`
--

INSERT INTO `user_roles` (`user_id`, `role_id`) VALUES
(1, 1),
(2, 2);

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `invoices`
--
ALTER TABLE `invoices`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `roles`
--
ALTER TABLE `roles`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indices de la tabla `user_roles`
--
ALTER TABLE `user_roles`
  ADD PRIMARY KEY (`user_id`,`role_id`),
  ADD KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `invoices`
--
ALTER TABLE `invoices`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;

--
-- AUTO_INCREMENT de la tabla `roles`
--
ALTER TABLE `roles`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT de la tabla `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `user_roles`
--
ALTER TABLE `user_roles`
  ADD CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  ADD CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
