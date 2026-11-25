/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.tienda.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;

public class InicializadorBD {

    // =====================================================
    // 1. CREAR LA BASE DE DATOS
    // =====================================================

    public static void crearBaseDatos() throws SQLException {

        String sql = """
            CREATE DATABASE IF NOT EXISTS tienda_online
            CHARACTER SET utf8mb4
            COLLATE utf8mb4_unicode_ci
            """;

        try (Connection conexion = ConexionBD.conectarServidor();
             Statement sentencia = conexion.createStatement()) {

            sentencia.executeUpdate(sql);

            System.out.println("Base de datos creada o ya existente.");
        }
    }

    // =====================================================
    // 2. CREAR LAS TABLAS
    // =====================================================

    public static void crearTablas() throws SQLException {

        List<String> tablas = Arrays.asList(

            // -------------------------------------------------
            // TABLA: CLIENTES
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS clientes (
                id_cliente INT AUTO_INCREMENT PRIMARY KEY,
                nombres VARCHAR(100) NOT NULL,
                apellidos VARCHAR(100) NOT NULL,
                correo VARCHAR(150) NOT NULL UNIQUE,
                telefono VARCHAR(20),
                direccion VARCHAR(250),
                fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: CATEGORIAS
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS categorias (
                id_categoria INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(80) NOT NULL UNIQUE,
                descripcion VARCHAR(250)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: PRODUCTOS
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS productos (
                id_producto INT AUTO_INCREMENT PRIMARY KEY,
                id_categoria INT NOT NULL,
                nombre VARCHAR(150) NOT NULL,
                descripcion TEXT,
                precio DECIMAL(12,2) NOT NULL,
                stock INT NOT NULL DEFAULT 0,
                activo BOOLEAN NOT NULL DEFAULT TRUE,

                FOREIGN KEY (id_categoria)
                    REFERENCES categorias(id_categoria),

                CONSTRAINT chk_producto_precio
                    CHECK (precio >= 0),

                CONSTRAINT chk_producto_stock
                    CHECK (stock >= 0)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: PRODUCTOS DE ROPA
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS producto_ropa (
                id_producto INT PRIMARY KEY,
                talla VARCHAR(30) NOT NULL,
                color VARCHAR(50),
                material VARCHAR(100),

                FOREIGN KEY (id_producto)
                    REFERENCES productos(id_producto)
                    ON DELETE CASCADE
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: PRODUCTOS DE TECNOLOGIA
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS producto_tecnologia (
                id_producto INT PRIMARY KEY,
                marca VARCHAR(100),
                modelo VARCHAR(100),
                garantia_meses INT DEFAULT 0,

                FOREIGN KEY (id_producto)
                    REFERENCES productos(id_producto)
                    ON DELETE CASCADE,

                CONSTRAINT chk_garantia
                    CHECK (garantia_meses >= 0)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: PRODUCTOS DE ALIMENTOS
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS producto_alimento (
                id_producto INT PRIMARY KEY,
                fecha_vencimiento DATE,
                peso_gramos DECIMAL(10,2),
                ingredientes TEXT,

                FOREIGN KEY (id_producto)
                    REFERENCES productos(id_producto)
                    ON DELETE CASCADE,

                CONSTRAINT chk_peso
                    CHECK (peso_gramos IS NULL OR peso_gramos > 0)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: CARRITOS
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS carritos (
                id_carrito INT AUTO_INCREMENT PRIMARY KEY,
                id_cliente INT NOT NULL,
                fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVO',

                FOREIGN KEY (id_cliente)
                    REFERENCES clientes(id_cliente)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: DETALLE DEL CARRITO
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS detalle_carrito (
                id_detalle_carrito INT AUTO_INCREMENT PRIMARY KEY,
                id_carrito INT NOT NULL,
                id_producto INT NOT NULL,
                cantidad INT NOT NULL,

                FOREIGN KEY (id_carrito)
                    REFERENCES carritos(id_carrito)
                    ON DELETE CASCADE,

                FOREIGN KEY (id_producto)
                    REFERENCES productos(id_producto),

                CONSTRAINT uq_carrito_producto
                    UNIQUE (id_carrito, id_producto),

                CONSTRAINT chk_cantidad_carrito
                    CHECK (cantidad > 0)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: DESCUENTOS
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS descuentos (
                id_descuento INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL,
                tipo VARCHAR(40) NOT NULL,
                valor DECIMAL(12,2) NOT NULL,
                acumulable BOOLEAN NOT NULL DEFAULT FALSE,
                fecha_inicio DATE,
                fecha_fin DATE,
                activo BOOLEAN NOT NULL DEFAULT TRUE,

                CONSTRAINT chk_valor_descuento
                    CHECK (valor > 0),

                CONSTRAINT chk_tipo_descuento
                    CHECK (tipo IN ('PORCENTAJE', 'FIJO'))
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: CUPONES
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS cupones (
                id_cupon INT AUTO_INCREMENT PRIMARY KEY,
                id_descuento INT NOT NULL,
                codigo VARCHAR(50) NOT NULL UNIQUE,
                usos_maximos INT,
                usos_actuales INT NOT NULL DEFAULT 0,
                fecha_expiracion DATE,
                activo BOOLEAN NOT NULL DEFAULT TRUE,

                FOREIGN KEY (id_descuento)
                    REFERENCES descuentos(id_descuento),

                CONSTRAINT chk_usos
                    CHECK (usos_actuales >= 0)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: PEDIDOS
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS pedidos (
                id_pedido INT AUTO_INCREMENT PRIMARY KEY,
                id_cliente INT NOT NULL,
                fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                estado VARCHAR(40) NOT NULL DEFAULT 'PENDIENTE',
                subtotal DECIMAL(12,2) NOT NULL DEFAULT 0,
                total_descuentos DECIMAL(12,2) NOT NULL DEFAULT 0,
                total DECIMAL(12,2) NOT NULL DEFAULT 0,
                direccion_entrega VARCHAR(250),

                FOREIGN KEY (id_cliente)
                    REFERENCES clientes(id_cliente),

                CONSTRAINT chk_totales_pedido
                    CHECK (
                        subtotal >= 0
                        AND total_descuentos >= 0
                        AND total >= 0
                    )
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: DETALLE DEL PEDIDO
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS detalle_pedido (
                id_detalle_pedido INT AUTO_INCREMENT PRIMARY KEY,
                id_pedido INT NOT NULL,
                id_producto INT NOT NULL,
                nombre_producto VARCHAR(150) NOT NULL,
                cantidad INT NOT NULL,
                precio_unitario DECIMAL(12,2) NOT NULL,
                subtotal DECIMAL(12,2) NOT NULL,

                FOREIGN KEY (id_pedido)
                    REFERENCES pedidos(id_pedido)
                    ON DELETE CASCADE,

                FOREIGN KEY (id_producto)
                    REFERENCES productos(id_producto),

                CONSTRAINT chk_cantidad_pedido
                    CHECK (cantidad > 0),

                CONSTRAINT chk_precio_pedido
                    CHECK (precio_unitario >= 0 AND subtotal >= 0)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: DESCUENTOS APLICADOS A PEDIDOS
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS pedido_descuentos (
                id_pedido INT NOT NULL,
                id_descuento INT NOT NULL,
                id_cupon INT NULL,
                valor_aplicado DECIMAL(12,2) NOT NULL,

                PRIMARY KEY (id_pedido, id_descuento),

                FOREIGN KEY (id_pedido)
                    REFERENCES pedidos(id_pedido)
                    ON DELETE CASCADE,

                FOREIGN KEY (id_descuento)
                    REFERENCES descuentos(id_descuento),

                FOREIGN KEY (id_cupon)
                    REFERENCES cupones(id_cupon),

                CONSTRAINT chk_valor_aplicado
                    CHECK (valor_aplicado >= 0)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: METODOS DE PAGO
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS metodos_pago (
                id_metodo_pago INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(80) NOT NULL UNIQUE,
                descripcion VARCHAR(200),
                activo BOOLEAN NOT NULL DEFAULT TRUE
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: PAGOS
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS pagos (
                id_pago INT AUTO_INCREMENT PRIMARY KEY,
                id_pedido INT NOT NULL,
                id_metodo_pago INT NOT NULL,
                fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                monto DECIMAL(12,2) NOT NULL,
                estado VARCHAR(40) NOT NULL DEFAULT 'PENDIENTE',
                referencia VARCHAR(100),

                FOREIGN KEY (id_pedido)
                    REFERENCES pedidos(id_pedido),

                FOREIGN KEY (id_metodo_pago)
                    REFERENCES metodos_pago(id_metodo_pago),

                CONSTRAINT chk_monto_pago
                    CHECK (monto >= 0)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: EXTRAS
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS extras (
                id_extra INT AUTO_INCREMENT PRIMARY KEY,
                nombre VARCHAR(100) NOT NULL UNIQUE,
                descripcion VARCHAR(250),
                precio DECIMAL(12,2) NOT NULL,
                activo BOOLEAN NOT NULL DEFAULT TRUE,

                CONSTRAINT chk_precio_extra
                    CHECK (precio >= 0)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: EXTRAS APLICADOS A PRODUCTOS DEL PEDIDO
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS detalle_pedido_extras (
                id_detalle_pedido INT NOT NULL,
                id_extra INT NOT NULL,
                precio_aplicado DECIMAL(12,2) NOT NULL,

                PRIMARY KEY (id_detalle_pedido, id_extra),

                FOREIGN KEY (id_detalle_pedido)
                    REFERENCES detalle_pedido(id_detalle_pedido)
                    ON DELETE CASCADE,

                FOREIGN KEY (id_extra)
                    REFERENCES extras(id_extra),

                CONSTRAINT chk_precio_extra_aplicado
                    CHECK (precio_aplicado >= 0)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: NOTIFICACIONES DE STOCK
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS notificaciones_stock (
                id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
                id_cliente INT NOT NULL,
                id_producto INT NOT NULL,
                fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                fecha_notificacion TIMESTAMP NULL,
                estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',

                FOREIGN KEY (id_cliente)
                    REFERENCES clientes(id_cliente),

                FOREIGN KEY (id_producto)
                    REFERENCES productos(id_producto),

                CONSTRAINT uq_notificacion
                    UNIQUE (id_cliente, id_producto)
            ) ENGINE=InnoDB
            """,

            // -------------------------------------------------
            // TABLA: FACTURAS
            // -------------------------------------------------

            """
            CREATE TABLE IF NOT EXISTS facturas (
                id_factura INT AUTO_INCREMENT PRIMARY KEY,
                id_pedido INT NOT NULL UNIQUE,
                numero_factura VARCHAR(50) NOT NULL UNIQUE,
                fecha_emision TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                total DECIMAL(12,2) NOT NULL,

                FOREIGN KEY (id_pedido)
                    REFERENCES pedidos(id_pedido),

                CONSTRAINT chk_total_factura
                    CHECK (total >= 0)
            ) ENGINE=InnoDB
            """
        );

        try (Connection conexion = ConexionBD.conectarBD();
             Statement sentencia = conexion.createStatement()) {

            int contador = 0;

            for (String sql : tablas) {
                sentencia.executeUpdate(sql);
                contador++;

                System.out.println(
                    "Tabla verificada: " + contador + " de " + tablas.size()
                );
            }

            System.out.println("Todas las tablas fueron creadas o verificadas.");
        }
    }

    // =====================================================
    // 3. INSERTAR DATOS INICIALES
    // =====================================================

    public static void insertarDatosIniciales() throws SQLException {

        List<String> datos = Arrays.asList(

            """
            INSERT IGNORE INTO categorias (nombre, descripcion)
            VALUES
                ('Ropa', 'Productos de vestuario'),
                ('Tecnologia', 'Dispositivos y accesorios tecnologicos'),
                ('Alimentos', 'Productos alimenticios')
            """,

            """
            INSERT IGNORE INTO metodos_pago (nombre, descripcion)
            VALUES
                ('Tarjeta', 'Pago simulado con tarjeta'),
                ('Transferencia', 'Pago simulado por transferencia'),
                ('Contra entrega', 'Pago al recibir el pedido')
            """,

            """
            INSERT IGNORE INTO extras (nombre, descripcion, precio)
            VALUES
                ('Garantia extendida', 'Proteccion adicional del producto', 15000),
                ('Empaque de regalo', 'Presentacion especial para regalo', 5000),
                ('Envio express', 'Entrega prioritaria', 10000)
            """
        );

        try (Connection conexion = ConexionBD.conectarBD();
             Statement sentencia = conexion.createStatement()) {

            for (String sql : datos) {
                sentencia.executeUpdate(sql);
            }

            System.out.println("Datos iniciales insertados correctamente.");
        }
    }

    // =====================================================
    // 4. INICIALIZAR TODA LA BASE DE DATOS
    // =====================================================

    public static void inicializarBD() throws SQLException {

        System.out.println("====================================");
        System.out.println(" INICIALIZANDO BASE DE DATOS");
        System.out.println("====================================");

        crearBaseDatos();

        crearTablas();

        insertarDatosIniciales();

        System.out.println("====================================");
        System.out.println(" BASE DE DATOS LISTA");
        System.out.println("====================================");
    }

    // =====================================================
    // 5. EJECUTAR ESTA CLASE PARA PROBAR
    // =====================================================

    public static void main(String[] args) {

        try {

            inicializarBD();

        } catch (SQLException e) {

            System.out.println("ERROR AL INICIALIZAR LA BASE DE DATOS:");
            e.printStackTrace();

        }
    }
}
