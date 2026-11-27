/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.tienda.gui;

import com.tienda.model.*;
import com.tienda.service.*;
import com.tienda.dao.MetodoPagoDAO;
import com.tienda.strategy.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentanaPrincipal extends JFrame {

    // Servicios
    private final ProductoService productoService = new ProductoService();
    private final ClienteService clienteService = new ClienteService();
    private final CarritoService carritoService = new CarritoService();
    private final PedidoService pedidoService = new PedidoService();
    private final FacturaService facturaService = new FacturaService();
    private final CompraService compraService = new CompraService();
    private final DescuentoService descuentoService = new DescuentoService();
    private final ExtraService extraService = new ExtraService();
    private final MetodoPagoDAO metodoPagoDAO = new MetodoPagoDAO();

    // Cliente y carrito actuales
    private Cliente clienteActual;
    private Carrito carritoActual;

    // Componentes
    private JPanel contenido;
    private CardLayout cardLayout;

    private JTable tablaProductos;
    private JTable tablaCarrito;
    private JTable tablaPedidos;
    private JTable tablaFacturas;

    private DefaultTableModel modeloProductos;
    private DefaultTableModel modeloCarrito;
    private DefaultTableModel modeloPedidos;
    private DefaultTableModel modeloFacturas;

    private JTextField campoBuscar;

    // Colores
    private final Color AZUL = new Color(35, 47, 62);
    private final Color AZUL_CLARO = new Color(45, 60, 78);
    private final Color FONDO = new Color(245, 247, 250);
    private final Color BLANCO = Color.WHITE;

    public VentanaPrincipal() {

        setTitle("Tienda en Línea");
        setSize(1200, 750);
        setMinimumSize(new Dimension(1000, 650));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        crearInterfaz();

        SwingUtilities.invokeLater(this::seleccionarCliente);
    }

    // ==========================================
    // INTERFAZ PRINCIPAL
    // ==========================================

    private void crearInterfaz() {

        // Encabezado
        JPanel encabezado = new JPanel(new BorderLayout());
        encabezado.setBackground(AZUL);
        encabezado.setPreferredSize(new Dimension(1200, 85));
        encabezado.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));

        JLabel titulo = new JLabel("TIENDA EN LÍNEA");
        titulo.setForeground(BLANCO);
        titulo.setFont(new Font("Arial", Font.BOLD, 27));

        JLabel usuario = new JLabel("Cliente: No seleccionado");
        usuario.setForeground(BLANCO);
        usuario.setFont(new Font("Arial", Font.PLAIN, 15));
        usuario.setName("usuarioActual");

        encabezado.add(titulo, BorderLayout.WEST);
        encabezado.add(usuario, BorderLayout.EAST);

        // Menú lateral
        JPanel menu = new JPanel(new GridLayout(8, 1, 0, 5));
        menu.setBackground(AZUL_CLARO);
        menu.setPreferredSize(new Dimension(220, 650));
        menu.setBorder(BorderFactory.createEmptyBorder(12, 8, 12, 8));

        agregarBotonMenu(menu, "Inicio", "inicio");
        agregarBotonMenu(menu, "Productos", "productos");
        agregarBotonMenu(menu, "Buscar productos", "productos");
        agregarBotonMenu(menu, "Mi carrito", "carrito");
        agregarBotonMenu(menu, "Mis pedidos", "pedidos");
        agregarBotonMenu(menu, "Mis facturas", "facturas");
        agregarBotonMenu(menu, "Registrar cliente", "registro");
        agregarBotonMenu(menu, "Cambiar cliente", "cliente");

        // Contenido
        cardLayout = new CardLayout();
        contenido = new JPanel(cardLayout);
        contenido.setBackground(FONDO);

        contenido.add(crearPanelInicio(), "inicio");
        contenido.add(crearPanelProductos(), "productos");
        contenido.add(crearPanelCarrito(), "carrito");
        contenido.add(crearPanelPedidos(), "pedidos");
        contenido.add(crearPanelFacturas(), "facturas");
        contenido.add(crearPanelRegistro(), "registro");

        add(encabezado, BorderLayout.NORTH);
        add(menu, BorderLayout.WEST);
        add(contenido, BorderLayout.CENTER);

        cardLayout.show(contenido, "inicio");
    }

    private void agregarBotonMenu(JPanel menu, String texto, String destino) {

        JButton boton = new JButton(texto);
        boton.setForeground(BLANCO);
        boton.setBackground(AZUL_CLARO);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createEmptyBorder(15, 8, 15, 8));
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        boton.addActionListener(e -> {

            if (destino.equals("cliente")) {
                seleccionarCliente();
            } else if (destino.equals("productos")) {
                cargarProductos();
                cardLayout.show(contenido, destino);
            } else if (destino.equals("carrito")) {
                if (asegurarCliente()) {
                    cargarCarrito();
                    cardLayout.show(contenido, destino);
                }
            } else if (destino.equals("pedidos")) {
                if (asegurarCliente()) {
                    cargarPedidos();
                    cardLayout.show(contenido, destino);
                }
            } else if (destino.equals("facturas")) {
                if (asegurarCliente()) {
                    cargarFacturas();
                    cardLayout.show(contenido, destino);
                }
            } else {
                cardLayout.show(contenido, destino);
            }
        });

        menu.add(boton);
    }

    // ==========================================
    // PANEL DE INICIO
    // ==========================================

    private JPanel crearPanelInicio() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FONDO);

        JLabel bienvenida = new JLabel(
                "<html><center>"
                + "<h1>¡Bienvenido a nuestra tienda!</h1>"
                + "<p>Encuentra los productos que necesitas.</p>"
                + "<p>Compra de manera fácil y segura.</p>"
                + "</center></html>"
        );

        bienvenida.setFont(new Font("Arial", Font.PLAIN, 23));
        panel.add(bienvenida);

        return panel;
    }

    // ==========================================
    // PANEL DE PRODUCTOS
    // ==========================================

    private JPanel crearPanelProductos() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = crearTitulo("Catálogo de productos");

        JPanel superior = new JPanel(new BorderLayout(10, 10));
        superior.setOpaque(false);

        campoBuscar = new JTextField();
        campoBuscar.setFont(new Font("Arial", Font.PLAIN, 15));

        JButton buscar = crearBoton("Buscar");
        JButton mostrarTodos = crearBoton("Mostrar todos");
        JButton agregar = crearBoton("Agregar al carrito");

        JPanel busqueda = new JPanel(new BorderLayout(8, 8));
        busqueda.setOpaque(false);
        busqueda.add(campoBuscar, BorderLayout.CENTER);
        busqueda.add(buscar, BorderLayout.EAST);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.setOpaque(false);
        acciones.add(mostrarTodos);
        acciones.add(agregar);

        superior.add(titulo, BorderLayout.NORTH);
        superior.add(busqueda, BorderLayout.CENTER);
        superior.add(acciones, BorderLayout.SOUTH);

        modeloProductos = new DefaultTableModel(
                new Object[]{"ID", "Producto", "Descripción", "Precio", "Stock"},
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloProductos);
        configurarTabla(tablaProductos);

        buscar.addActionListener(e -> buscarProductos());
        mostrarTodos.addActionListener(e -> {
            campoBuscar.setText("");
            cargarProductos();
        });
        agregar.addActionListener(e -> agregarProductoSeleccionado());

        panel.add(superior, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaProductos), BorderLayout.CENTER);

        return panel;
    }

    private void cargarProductos() {

        modeloProductos.setRowCount(0);

        List<Producto> productos = productoService.listarProductos();

        if (productos == null) {
            return;
        }

        for (Producto producto : productos) {

            if (producto == null || !producto.isActivo()) {
                continue;
            }

            modeloProductos.addRow(new Object[]{
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getDescripcion(),
                producto.getPrecio(),
                producto.getStock()
            });
        }
    }

    private void buscarProductos() {

        String texto = campoBuscar.getText().trim().toLowerCase();

        if (texto.isEmpty()) {
            cargarProductos();
            return;
        }

        modeloProductos.setRowCount(0);

        List<Producto> productos = productoService.listarProductos();

        for (Producto producto : productos) {

            if (producto != null
                    && producto.isActivo()
                    && (producto.getNombre().toLowerCase().contains(texto)
                    || producto.getDescripcion().toLowerCase().contains(texto)
                    || String.valueOf(producto.getIdProducto()).contains(texto))) {

                modeloProductos.addRow(new Object[]{
                    producto.getIdProducto(),
                    producto.getNombre(),
                    producto.getDescripcion(),
                    producto.getPrecio(),
                    producto.getStock()
                });
            }
        }
    }

    private void agregarProductoSeleccionado() {

        if (!asegurarCliente()) {
            return;
        }

        int fila = tablaProductos.getSelectedRow();

        if (fila == -1) {
            mostrarError("Seleccione un producto de la tabla.");
            return;
        }

        int idProducto = Integer.parseInt(
                modeloProductos.getValueAt(fila, 0).toString()
        );

        int cantidad = leerNumero("Ingrese la cantidad:");

        if (cantidad <= 0) {
            mostrarError("La cantidad debe ser mayor que cero.");
            return;
        }

        if (carritoActual == null) {
            carritoActual = carritoService.obtenerOCrearCarrito(
                    clienteActual.getIdCliente()
            );
        }

        if (carritoActual == null) {
            mostrarError("No fue posible obtener el carrito.");
            return;
        }

        boolean agregado = carritoService.agregarProducto(
                carritoActual.getIdCarrito(),
                idProducto,
                cantidad
        );

        if (agregado) {
            mostrarMensaje("Producto agregado al carrito correctamente.");
        } else {
            mostrarError("No se pudo agregar. Verifique el stock disponible.");
        }
    }

    // ==========================================
    // PANEL DEL CARRITO
    // ==========================================

    private JPanel crearPanelCarrito() {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titulo = crearTitulo("Mi carrito de compras");

        modeloCarrito = new DefaultTableModel(
                new Object[]{"ID detalle", "Producto", "Cantidad",
                    "Precio unitario", "Subtotal"},
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaCarrito = new JTable(modeloCarrito);
        configurarTabla(tablaCarrito);

        JButton actualizar = crearBoton("Actualizar cantidad");
        JButton eliminar = crearBoton("Eliminar producto");
        JButton comprar = crearBoton("Realizar compra");

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.setOpaque(false);
        acciones.add(actualizar);
        acciones.add(eliminar);
        acciones.add(comprar);

        actualizar.addActionListener(e -> modificarCantidadCarrito());
        eliminar.addActionListener(e -> eliminarProductoCarrito());
        comprar.addActionListener(e -> realizarCompra());

        panel.add(titulo, BorderLayout.NORTH);
        panel.add(new JScrollPane(tablaCarrito), BorderLayout.CENTER);
        panel.add(acciones, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarCarrito() {

        modeloCarrito.setRowCount(0);

        if (carritoActual == null) {
            carritoActual = carritoService.obtenerOCrearCarrito(
                    clienteActual.getIdCliente()
            );
        }

        if (carritoActual == null) {
            mostrarError("No se pudo obtener el carrito.");
            return;
        }

        List<DetalleCarrito> detalles =
                carritoService.consultarCarrito(carritoActual.getIdCarrito());

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleCarrito detalle : detalles) {

            BigDecimal subtotal = detalle.calcularSubtotal();
            total = total.add(subtotal);

            modeloCarrito.addRow(new Object[]{
                detalle.getIdDetalle(),
                detalle.getProducto().getNombre(),
                detalle.getCantidad(),
                detalle.getProducto().getPrecio(),
                subtotal
            });
        }

        if (detalles.isEmpty()) {
            mostrarMensaje("Tu carrito está vacío.");
        } else {
            mostrarTotal("Total del carrito: $" + total);
        }
    }

    private void modificarCantidadCarrito() {

        int fila = tablaCarrito.getSelectedRow();

        if (fila == -1) {
            mostrarError("Seleccione un producto del carrito.");
            return;
        }

        int idDetalle = Integer.parseInt(
                modeloCarrito.getValueAt(fila, 0).toString()
        );

        int cantidad = leerNumero("Ingrese la nueva cantidad:");

        if (cantidad <= 0) {
            mostrarError("La cantidad debe ser mayor que cero.");
            return;
        }

        boolean actualizado = carritoService.actualizarCantidad(
                idDetalle, cantidad
        );

        if (actualizado) {
            mostrarMensaje("Cantidad actualizada.");
            cargarCarrito();
        } else {
            mostrarError("No fue posible actualizar la cantidad.");
        }
    }

    private void eliminarProductoCarrito() {

        int fila = tablaCarrito.getSelectedRow();

        if (fila == -1) {
            mostrarError("Seleccione un producto del carrito.");
            return;
        }

        int confirmacion = JOptionPane.showConfirmDialog(
                this,
                "¿Desea eliminar este producto del carrito?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmacion != JOptionPane.YES_OPTION) {
            return;
        }

        int idDetalle = Integer.parseInt(
                modeloCarrito.getValueAt(fila, 0).toString()
        );

        if (carritoService.eliminarProducto(idDetalle)) {
            mostrarMensaje("Producto eliminado.");
            cargarCarrito();
        } else {
            mostrarError("No fue posible eliminar el producto.");
        }
    }

    // ==========================================
    // REALIZAR COMPRA
    // ==========================================

    private void realizarCompra() {

        if (!asegurarCliente()) {
            return;
        }

        if (carritoActual == null) {
            carritoActual = carritoService.obtenerOCrearCarrito(
                    clienteActual.getIdCliente()
            );
        }

        List<DetalleCarrito> lineas =
                carritoService.consultarCarrito(carritoActual.getIdCarrito());

        if (lineas == null || lineas.isEmpty()) {
            mostrarError("El carrito está vacío.");
            return;
        }

        List<DetallePedido> detalles = new ArrayList<>();
        BigDecimal subtotal = BigDecimal.ZERO;

        for (DetalleCarrito linea : lineas) {

            Producto producto = productoService.buscarProducto(
                    linea.getProducto().getIdProducto()
            );

            if (producto == null || !producto.hayStock(linea.getCantidad())) {
                mostrarError("Stock insuficiente para: "
                        + linea.getProducto().getNombre());
                return;
            }

            DetallePedido detalle = new DetallePedido(
                    null, producto, linea.getCantidad()
            );

            detalles.add(detalle);

            subtotal = subtotal.add(
                    producto.getPrecio().multiply(
                            BigDecimal.valueOf(linea.getCantidad())
                    )
            );
        }

        // Cupón opcional
        BigDecimal descuento = BigDecimal.ZERO;

        int usarCupon = JOptionPane.showConfirmDialog(
                this,
                "¿Desea utilizar un cupón de descuento?",
                "Cupón",
                JOptionPane.YES_NO_OPTION
        );

        if (usarCupon == JOptionPane.YES_OPTION) {

            String codigo = JOptionPane.showInputDialog(
                    this, "Ingrese el código del cupón:"
            );

            if (codigo != null && !codigo.trim().isEmpty()) {

                Cupon cupon = descuentoService.buscarCupon(codigo.trim());

                if (cupon != null && cupon.esValido()) {
                    EstrategiaDescuento estrategia =
                            new DescuentoPorCupon(cupon);

                    descuento = descuentoService.calcularDescuento(
                            subtotal, estrategia
                    );
                } else {
                    mostrarError("El cupón no existe o no es válido.");
                }
            }
        }

        // Extras opcionales
        List<Extra> extrasSeleccionados = new ArrayList<>();

        List<Extra> disponibles = extraService.listarExtrasDisponibles();

        if (disponibles != null && !disponibles.isEmpty()) {

            StringBuilder textoExtras = new StringBuilder(
                    "Extras disponibles:\n"
            );

            for (Extra extra : disponibles) {
                textoExtras.append(extra.getIdExtra())
                        .append(" - ")
                        .append(extra.getNombre())
                        .append(" ($")
                        .append(extra.getPrecio())
                        .append(")\n");
            }

            textoExtras.append(
                    "\nIngrese los ID separados por coma o deje vacío:"
            );

            String seleccion = JOptionPane.showInputDialog(
                    this, textoExtras.toString()
            );

            if (seleccion != null && !seleccion.trim().isEmpty()) {

                try {
                    String[] ids = seleccion.split(",");

                    for (String id : ids) {
                        Extra extra = extraService.buscarExtra(
                                Integer.parseInt(id.trim())
                        );

                        if (extra != null && extra.isActivo()) {
                            extrasSeleccionados.add(extra);
                        }
                    }
                } catch (NumberFormatException e) {
                    mostrarError("Formato de extras incorrecto.");
                    return;
                }
            }
        }

        List<List<Extra>> extrasPorDetalle = new ArrayList<>();

        for (int i = 0; i < detalles.size(); i++) {
            extrasPorDetalle.add(new ArrayList<>(extrasSeleccionados));
        }

        BigDecimal totalExtras = BigDecimal.ZERO;

        for (List<Extra> lista : extrasPorDetalle) {
            for (Extra extra : lista) {
                totalExtras = totalExtras.add(extra.getPrecio());
            }
        }

        BigDecimal total = subtotal.subtract(descuento).add(totalExtras);

        if (total.signum() <= 0) {
            mostrarError("El total de la compra no es válido.");
            return;
        }

        String direccion = JOptionPane.showInputDialog(
                this,
                "Dirección de entrega:",
                clienteActual.getDireccion()
        );

        if (direccion == null || direccion.trim().isEmpty()) {
            mostrarError("Debe ingresar una dirección de entrega.");
            return;
        }

        List<MetodoPago> metodos = metodoPagoDAO.listarActivos();

        if (metodos == null || metodos.isEmpty()) {
            mostrarError("No hay métodos de pago disponibles.");
            return;
        }

        MetodoPago metodo = (MetodoPago) JOptionPane.showInputDialog(
                this,
                "Seleccione el método de pago:",
                "Método de pago",
                JOptionPane.QUESTION_MESSAGE,
                null,
                metodos.toArray(),
                metodos.get(0)
        );

        if (metodo == null) {
            return;
        }

        EstrategiaPago estrategiaPago;
        String nombreMetodo = metodo.getNombre().toLowerCase();

        if (nombreMetodo.contains("tarjeta")) {
            estrategiaPago = new PagoTarjeta();
        } else if (nombreMetodo.contains("transferencia")) {
            estrategiaPago = new PagoTransferencia();
        } else {
            estrategiaPago = new PagoContraEntrega();
        }

        String resumen = "Cliente: " + clienteActual.getNombres()
                + " " + clienteActual.getApellidos()
                + "\nProductos: " + detalles.size()
                + "\nSubtotal: $" + subtotal
                + "\nDescuento: $" + descuento
                + "\nExtras: $" + totalExtras
                + "\nTotal: $" + total
                + "\nMétodo: " + metodo.getNombre()
                + "\nDirección: " + direccion
                + "\n\n¿Confirmar compra?";

        int confirmar = JOptionPane.showConfirmDialog(
                this,
                resumen,
                "Confirmar pedido",
                JOptionPane.YES_NO_OPTION
        );

        if (confirmar != JOptionPane.YES_OPTION) {
            return;
        }

        Pedido pedido = new Pedido(
                clienteActual,
                subtotal,
                descuento,
                total,
                direccion.trim()
        );

        Pago pago = new Pago(pedido, metodo, total);

        Factura factura = new Factura(
                pedido,
                "FAC-" + System.currentTimeMillis()
        );

        boolean resultado = compraService.registrarCompraCompleta(
                pedido,
                detalles,
                extrasPorDetalle,
                pago,
                estrategiaPago,
                factura
        );

        if (resultado) {

            if ("APROBADO".equalsIgnoreCase(pago.getEstado())) {
                mostrarMensaje(
                        "¡Compra completada!\n"
                        + "Pedido: " + pedido.getIdPedido()
                        + "\nFactura: " + factura.getNumeroFactura()
                        + "\nTotal: $" + total
                );
            } else {
                mostrarMensaje(
                        "Pedido registrado para pago contra entrega.\n"
                        + "El pago está pendiente."
                );
            }

            cargarCarrito();
            cargarProductos();

        } else {
            mostrarError(
                    "No se pudo completar la compra.\n"
                    + "Revise los mensajes de la consola."
            );
        }
    }

    // ==========================================
    // PANEL DE PEDIDOS
    // ==========================================

    private JPanel crearPanelPedidos() {

        JPanel panel = crearPanelBase("Mis pedidos");

        modeloPedidos = new DefaultTableModel(
                new Object[]{"ID pedido", "Fecha", "Estado",
                    "Subtotal", "Descuentos", "Total", "Dirección"},
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaPedidos = new JTable(modeloPedidos);
        configurarTabla(tablaPedidos);

        JButton actualizar = crearBoton("Actualizar pedidos");
        actualizar.addActionListener(e -> cargarPedidos());

        panel.add(new JScrollPane(tablaPedidos), BorderLayout.CENTER);
        panel.add(actualizar, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarPedidos() {

        modeloPedidos.setRowCount(0);

        if (clienteActual == null) {
            return;
        }

        List<Pedido> pedidos = pedidoService.consultarPedidosCliente(
                clienteActual.getIdCliente()
        );

        if (pedidos == null) {
            return;
        }

        for (Pedido pedido : pedidos) {

            modeloPedidos.addRow(new Object[]{
                pedido.getIdPedido(),
                pedido.getFechaPedido(),
                pedido.getEstado(),
                pedido.getSubtotal(),
                pedido.getTotalDescuentos(),
                pedido.getTotal(),
                pedido.getDireccionEntrega()
            });
        }

        if (pedidos.isEmpty()) {
            mostrarMensaje("No tienes pedidos registrados.");
        }
    }

    // ==========================================
    // PANEL DE FACTURAS
    // ==========================================

    private JPanel crearPanelFacturas() {

        JPanel panel = crearPanelBase("Mis facturas");

        modeloFacturas = new DefaultTableModel(
                new Object[]{"ID factura", "Número", "Pedido",
                    "Fecha emisión", "Total", "Estado"},
                0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaFacturas = new JTable(modeloFacturas);
        configurarTabla(tablaFacturas);

        JButton actualizar = crearBoton("Actualizar facturas");
        actualizar.addActionListener(e -> cargarFacturas());

        panel.add(new JScrollPane(tablaFacturas), BorderLayout.CENTER);
        panel.add(actualizar, BorderLayout.SOUTH);

        return panel;
    }

    private void cargarFacturas() {

        modeloFacturas.setRowCount(0);

        List<Factura> facturas = facturaService.listarFacturas();

        if (facturas == null) {
            return;
        }

        for (Factura factura : facturas) {

            if (factura.getPedido() == null
                    || factura.getPedido().getCliente() == null
                    || factura.getPedido().getCliente().getIdCliente()
                    != clienteActual.getIdCliente()) {
                continue;
            }

            modeloFacturas.addRow(new Object[]{
                factura.getIdFactura(),
                factura.getNumeroFactura(),
                factura.getPedido().getIdPedido(),
                factura.getFechaEmision(),
                factura.getTotal(),
                factura.getEstado()
            });
        }

        if (modeloFacturas.getRowCount() == 0) {
            mostrarMensaje("No tienes facturas registradas.");
        }
    }

    // ==========================================
    // REGISTRO DE CLIENTES
    // ==========================================

    private JPanel crearPanelRegistro() {

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(FONDO);

        JPanel formulario = new JPanel(new GridLayout(6, 2, 10, 15));
        formulario.setBackground(BLANCO);
        formulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        JTextField nombres = new JTextField(20);
        JTextField apellidos = new JTextField(20);
        JTextField correo = new JTextField(20);
        JTextField telefono = new JTextField(20);
        JTextField direccion = new JTextField(20);

        formulario.add(new JLabel("Nombres:"));
        formulario.add(nombres);
        formulario.add(new JLabel("Apellidos:"));
        formulario.add(apellidos);
        formulario.add(new JLabel("Correo:"));
        formulario.add(correo);
        formulario.add(new JLabel("Teléfono:"));
        formulario.add(telefono);
        formulario.add(new JLabel("Dirección:"));
        formulario.add(direccion);

        JButton registrar = crearBoton("Registrar cliente");
        formulario.add(new JLabel(""));
        formulario.add(registrar);

        registrar.addActionListener(e -> {

            if (nombres.getText().trim().isEmpty()
                    || apellidos.getText().trim().isEmpty()
                    || correo.getText().trim().isEmpty()) {

                mostrarError("Nombres, apellidos y correo son obligatorios.");
                return;
            }

            if (!correo.getText().trim().matches(
                    "^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {

                mostrarError("Ingrese un correo electrónico válido.");
                return;
            }

            Cliente cliente = new Cliente(
                    nombres.getText().trim(),
                    apellidos.getText().trim(),
                    correo.getText().trim(),
                    telefono.getText().trim(),
                    direccion.getText().trim()
            );

            cliente.setFechaRegistro(LocalDateTime.now());

            if (clienteService.registrarCliente(cliente)) {

                mostrarMensaje("Cliente registrado correctamente.");
                seleccionarCliente();

                nombres.setText("");
                apellidos.setText("");
                correo.setText("");
                telefono.setText("");
                direccion.setText("");

            } else {
                mostrarError("No fue posible registrar el cliente.");
            }
        });

        panel.add(formulario);

        return panel;
    }

    // ==========================================
    // SELECCIÓN DE CLIENTE
    // ==========================================

    private void seleccionarCliente() {

        String entrada = JOptionPane.showInputDialog(
                this,
                "Ingrese el ID del cliente para utilizar la tienda:",
                "Seleccionar cliente",
                JOptionPane.QUESTION_MESSAGE
        );

        if (entrada == null || entrada.trim().isEmpty()) {
            return;
        }

        try {

            int id = Integer.parseInt(entrada.trim());

            Cliente cliente = clienteService.buscarCliente(id);

            if (cliente == null) {
                mostrarError("No existe un cliente con ese ID.");
                return;
            }

            clienteActual = cliente;
            carritoActual = carritoService.obtenerOCrearCarrito(id);

            actualizarNombreCliente();

            mostrarMensaje(
                    "Bienvenido, " + cliente.getNombres()
                    + " " + cliente.getApellidos()
            );

        } catch (NumberFormatException e) {
            mostrarError("Ingrese un ID numérico válido.");
        }
    }

    private void actualizarNombreCliente() {

        if (getContentPane().getComponentCount() == 0) {
            return;
        }

        JPanel encabezado = (JPanel) getContentPane().getComponent(0);

        for (Component componente : encabezado.getComponents()) {

            if (componente instanceof JLabel
                    && ((JLabel) componente).getName() != null
                    && ((JLabel) componente).getName().equals("usuarioActual")) {

                ((JLabel) componente).setText(
                        "Cliente: " + clienteActual.getNombres()
                        + " " + clienteActual.getApellidos()
                );
            }
        }
    }

    private boolean asegurarCliente() {

        if (clienteActual == null) {
            seleccionarCliente();
        }

        if (clienteActual == null) {
            mostrarError("Primero debe seleccionar un cliente.");
            return false;
        }

        if (carritoActual == null) {
            carritoActual = carritoService.obtenerOCrearCarrito(
                    clienteActual.getIdCliente()
            );
        }

        return carritoActual != null;
    }

    // ==========================================
    // COMPONENTES AUXILIARES
    // ==========================================

    private JPanel crearPanelBase(String tituloTexto) {

        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBackground(FONDO);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(crearTitulo(tituloTexto), BorderLayout.NORTH);

        return panel;
    }

    private JLabel crearTitulo(String texto) {

        JLabel titulo = new JLabel(texto);
        titulo.setFont(new Font("Arial", Font.BOLD, 24));
        titulo.setForeground(AZUL);

        return titulo;
    }

    private JButton crearBoton(String texto) {

        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 13));
        boton.setForeground(BLANCO);
        boton.setBackground(AZUL);
        boton.setFocusPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return boton;
    }

    private void configurarTabla(JTable tabla) {

        tabla.setFont(new Font("Arial", Font.PLAIN, 13));
        tabla.setRowHeight(30);
        tabla.getTableHeader().setFont(
                new Font("Arial", Font.BOLD, 13)
        );
        tabla.getTableHeader().setBackground(AZUL);
        tabla.getTableHeader().setForeground(BLANCO);
        tabla.setSelectionBackground(new Color(205, 220, 235));
        tabla.setSelectionForeground(Color.BLACK);
    }

    private int leerNumero(String mensaje) {

        while (true) {

            String valor = JOptionPane.showInputDialog(this, mensaje);

            if (valor == null) {
                return -1;
            }

            try {
                return Integer.parseInt(valor.trim());
            } catch (NumberFormatException e) {
                mostrarError("Ingrese un número entero válido.");
            }
        }
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(
                this, mensaje, "Tienda en Línea",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(
                this, mensaje, "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    private void mostrarTotal(String mensaje) {
        JOptionPane.showMessageDialog(
                this, mensaje, "Resumen",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // ==========================================
    // MÉTODO PRINCIPAL
    // ==========================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new VentanaPrincipal().setVisible(true);
        });
    }
}
