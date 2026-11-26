/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.console;

import com.tienda.service.ProductoService;
import com.tienda.service.ClienteService;
import com.tienda.service.CarritoService;

import com.tienda.model.Producto;
import com.tienda.model.Cliente;
import com.tienda.model.Carrito;
import com.tienda.model.DetalleCarrito;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import com.tienda.dao.MetodoPagoDAO;

import com.tienda.model.Pedido;
import com.tienda.model.DetallePedido;
import com.tienda.model.Extra;
import com.tienda.model.MetodoPago;
import com.tienda.model.Pago;
import com.tienda.model.Cupon;

import com.tienda.service.CompraService;
import com.tienda.service.ExtraService;
import com.tienda.service.DescuentoService;

import com.tienda.strategy.EstrategiaPago;
import com.tienda.strategy.PagoTarjeta;
import com.tienda.strategy.PagoTransferencia;
import com.tienda.strategy.PagoContraEntrega;
import com.tienda.strategy.EstrategiaDescuento;
import com.tienda.strategy.DescuentoPorCupon;

import com.tienda.service.PedidoService;


import java.util.ArrayList;

import com.tienda.service.FacturaService;
import com.tienda.model.Factura;



public class MenuConsola {

    private final Scanner scanner;
    private final ProductoService productoService;
    private final ClienteService clienteService;
    private final CarritoService carritoService;
    private final PedidoService pedidoService;
    private final FacturaService facturaService;
    public MenuConsola() {
        this.scanner = new Scanner(System.in);
        this.productoService = new ProductoService();
        this.clienteService = new ClienteService();
        this.carritoService = new CarritoService();
        this.pedidoService = new PedidoService();
        this.facturaService = new FacturaService();
    }

    public void iniciar() {

        int opcion;

        do {
            mostrarMenu();

            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {

                case 1:
                    registrarCliente();
                    break;

                case 2:
                    consultarProductos();
                    break;

                case 3:
                    buscarProducto();
                    break;

                case 4:
                    gestionarCarrito();
                    break;

                case 5:
                    realizarCompra();
                    break;

                case 6:
                    consultarPedidos();
                    break;

                case 7:
                    consultarFacturas();
                    break;

                case 8:
                    System.out.println("\nGracias por utilizar la tienda.");
                    break;

                default:
                    System.out.println("\nOpcion no valida.");
            }

        } while (opcion != 8);
    }

    // ==========================================
    // MÉTODO PARA LEER NÚMEROS ENTEROS
    // ==========================================

    private int leerEntero(String mensaje) {

        while (true) {

            System.out.print(mensaje);

            if (scanner.hasNextInt()) {
                int numero = scanner.nextInt();
                scanner.nextLine();
                return numero;
            }

            System.out.println("Error: debe ingresar un numero valido.");
            scanner.nextLine();
        }
    }

    // ==========================================
    // MENÚ PRINCIPAL
    // ==========================================

    private void mostrarMenu() {

        System.out.println("\n================================");
        System.out.println("          TIENDA EN LINEA");
        System.out.println("================================");
        System.out.println("1. Registrar cliente");
        System.out.println("2. Consultar productos");
        System.out.println("3. Buscar producto");
        System.out.println("4. Gestionar carrito");
        System.out.println("5. Realizar compra");
        System.out.println("6. Consultar pedidos");
        System.out.println("7. Consultar facturas");
        System.out.println("8. Salir");
        System.out.println("================================");
    }

    // ==========================================
    // REGISTRAR CLIENTE
    // ==========================================

    private void registrarCliente() {

        System.out.println("\n================================");
        System.out.println("        REGISTRAR CLIENTE");
        System.out.println("================================");

        System.out.print("Ingrese los nombres: ");
        String nombres = scanner.nextLine().trim();

        System.out.print("Ingrese los apellidos: ");
        String apellidos = scanner.nextLine().trim();

        System.out.print("Ingrese el correo electronico: ");
        String correo = scanner.nextLine().trim();

        System.out.print("Ingrese el telefono: ");
        String telefono = scanner.nextLine().trim();

        System.out.print("Ingrese la direccion: ");
        String direccion = scanner.nextLine().trim();

        if (nombres.isEmpty() || apellidos.isEmpty()
                || correo.isEmpty()) {

            System.out.println("\nError: los nombres, apellidos "
                    + "y correo son obligatorios.");
            return;
        }

        if (!correo.matches("^[\\w.+-]+@[\\w.-]+\\.[A-Za-z]{2,}$")) {

            System.out.println("\nError: ingrese un correo electrónico válido.");
            return;
        }

        Cliente cliente = new Cliente(
                nombres,
                apellidos,
                correo,
                telefono,
                direccion
        );

        cliente.setFechaRegistro(LocalDateTime.now());

        boolean registrado = clienteService.registrarCliente(cliente);

        if (registrado) {

            System.out.println("\n¡Cliente registrado exitosamente!");
            System.out.println("Nombre: " + cliente.getNombres()
                    + " " + cliente.getApellidos());
            System.out.println("Correo: " + cliente.getCorreo());

        } else {

            System.out.println("\nNo fue posible registrar el cliente.");
            System.out.println("Verifique los datos o compruebe "
                    + "si el correo ya esta registrado.");
        }
    }

    // ==========================================
    // CONSULTAR PRODUCTOS
    // ==========================================

    private void consultarProductos() {

        System.out.println("\n================================");
        System.out.println("       CATALOGO DE PRODUCTOS");
        System.out.println("================================");

        List<Producto> productos =
                productoService.listarProductos();

        if (productos == null || productos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }

        for (Producto producto : productos) {

            System.out.println("\nID: " + producto.getIdProducto());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Descripcion: " + producto.getDescripcion());
            System.out.println("Precio: $" + producto.getPrecio());
            System.out.println("Stock disponible: " + producto.getStock());

            System.out.println("--------------------------------");
        }

        System.out.println("Total de productos: " + productos.size());
    }

    // ==========================================
    // BUSCAR PRODUCTO
    // ==========================================

    private void buscarProducto() {

        System.out.println("\n================================");
        System.out.println("         BUSCAR PRODUCTO");
        System.out.println("================================");

        int id = leerEntero("Ingrese el ID del producto: ");

        if (id <= 0) {
            System.out.println("El ID debe ser mayor que cero.");
            return;
        }

        Producto producto = productoService.buscarProducto(id);

        if (producto == null) {
            System.out.println("No se encontro un producto con ese ID.");
            return;
        }

        System.out.println("\n--- PRODUCTO ENCONTRADO ---");
        System.out.println("ID: " + producto.getIdProducto());
        System.out.println("Nombre: " + producto.getNombre());
        System.out.println("Descripcion: " + producto.getDescripcion());
        System.out.println("Precio: $" + producto.getPrecio());
        System.out.println("Stock disponible: " + producto.getStock());
    }

    // ==========================================
    // GESTIONAR CARRITO
    // ==========================================

    private void gestionarCarrito() {

        System.out.println("\n================================");
        System.out.println("         GESTIONAR CARRITO");
        System.out.println("================================");

        int idCliente = leerEntero("Ingrese el ID del cliente: ");

        if (idCliente <= 0) {
            System.out.println("El ID del cliente no es válido.");
            return;
        }

        Carrito carrito = carritoService.obtenerOCrearCarrito(idCliente);

        if (carrito == null) {
            System.out.println("No se encontro el cliente o no fue posible "
                    + "obtener su carrito.");
            return;
        }

        System.out.println("\nCarrito disponible.");
        System.out.println("ID del carrito: " + carrito.getIdCarrito());

        int opcion;

        do {

            System.out.println("\n================================");
            System.out.println("          MENU DEL CARRITO");
            System.out.println("================================");
            System.out.println("1. Agregar producto");
            System.out.println("2. Consultar carrito");
            System.out.println("3. Modificar cantidad");
            System.out.println("4. Eliminar producto");
            System.out.println("5. Volver al menu principal");
            System.out.println("================================");

            opcion = leerEntero("Seleccione una opcion: ");

            switch (opcion) {

                case 1:
                    agregarProductoCarrito(carrito.getIdCarrito());
                    break;

                case 2:
                    consultarCarrito(carrito.getIdCarrito());
                    break;

                case 3:
                    modificarCantidad();
                    break;

                case 4:
                    eliminarProducto();
                    break;

                case 5:
                    System.out.println("Regresando al menu principal...");
                    break;

                default:
                    System.out.println("Opcion no válida.");
            }

        } while (opcion != 5);
    }

    // ==========================================
    // AGREGAR PRODUCTO AL CARRITO
    // ==========================================

    private void agregarProductoCarrito(int idCarrito) {

        System.out.println("\n--- AGREGAR PRODUCTO ---");

        consultarProductos();

        int idProducto = leerEntero("\nIngrese el ID del producto: ");
        int cantidad = leerEntero("Ingrese la cantidad: ");

        if (idProducto <= 0 || cantidad <= 0) {
            System.out.println("El ID y la cantidad deben ser mayores que cero.");
            return;
        }

        boolean agregado = carritoService.agregarProducto(
                idCarrito,
                idProducto,
                cantidad
        );

        if (agregado) {
            System.out.println("\n¡Producto agregado al carrito exitosamente!");
        } else {
            System.out.println("\nNo fue posible agregar el producto.");
            System.out.println("Verifique que exista y que tenga stock suficiente.");
        }
    }

    // ==========================================
    // CONSULTAR CARRITO
    // ==========================================

    private void consultarCarrito(int idCarrito) {

        System.out.println("\n================================");
        System.out.println("        CONTENIDO DEL CARRITO");
        System.out.println("================================");

        List<DetalleCarrito> detalles =
                carritoService.consultarCarrito(idCarrito);

        if (detalles == null || detalles.isEmpty()) {
            System.out.println("El carrito esta vacío.");
            return;
        }

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleCarrito detalle : detalles) {

            BigDecimal subtotal = detalle.calcularSubtotal();
            total = total.add(subtotal);

            System.out.println("\nID del detalle: " + detalle.getIdDetalle());
            System.out.println("Producto: " + detalle.getProducto().getNombre());
            System.out.println("ID del producto: "
                    + detalle.getProducto().getIdProducto());
            System.out.println("Cantidad: " + detalle.getCantidad());
            System.out.println("Precio unitario: $"
                    + detalle.getProducto().getPrecio());
            System.out.println("Subtotal: $" + subtotal);

            System.out.println("--------------------------------");
        }

        System.out.println("\nTOTAL DEL CARRITO: $" + total);
    }

    // ==========================================
    // MODIFICAR CANTIDAD
    // ==========================================

    private void modificarCantidad() {

        System.out.println("\n--- MODIFICAR CANTIDAD ---");

        int idDetalle = leerEntero("Ingrese el ID del detalle: ");
        int cantidad = leerEntero("Ingrese la nueva cantidad: ");

        if (idDetalle <= 0 || cantidad <= 0) {
            System.out.println("Los valores deben ser mayores que cero.");
            return;
        }

        boolean actualizado = carritoService.actualizarCantidad(
                idDetalle,
                cantidad
        );

        if (actualizado) {
            System.out.println("Cantidad actualizada correctamente.");
        } else {
            System.out.println("No fue posible actualizar la cantidad.");
        }
    }

    // ==========================================
    // ELIMINAR PRODUCTO
    // ==========================================

    private void eliminarProducto() {

        System.out.println("\n--- ELIMINAR PRODUCTO ---");

        int idDetalle = leerEntero(
                "Ingrese el ID del detalle que desea eliminar: "
        );

        if (idDetalle <= 0) {
            System.out.println("El ID debe ser mayor que cero.");
            return;
        }

        boolean eliminado = carritoService.eliminarProducto(idDetalle);

        if (eliminado) {
            System.out.println("Producto eliminado del carrito.");
        } else {
            System.out.println("No fue posible eliminar el producto.");
        }
    }

    // ==========================================
    // REALIZAR COMPRA
    // ==========================================

    
private void realizarCompra() {

    System.out.println("\n================================");
    System.out.println("          REALIZAR COMPRA");
    System.out.println("================================");

    // 1. Identificar cliente y carrito
    int idCliente = leerEntero("Ingrese el ID del cliente: ");

    if (idCliente <= 0) {
        System.out.println("El ID del cliente no es valido.");
        return;
    }

    Cliente cliente = clienteService.buscarCliente(idCliente);

    if (cliente == null) {
        System.out.println("No existe un cliente con ese ID.");
        return;
    }

    Carrito carrito = carritoService.obtenerOCrearCarrito(idCliente);

    if (carrito == null) {
        System.out.println("No fue posible obtener el carrito.");
        return;
    }

    // 2. Consultar productos del carrito
    List<DetalleCarrito> lineas =
            carritoService.consultarCarrito(carrito.getIdCarrito());

    if (lineas == null || lineas.isEmpty()) {
        System.out.println("El carrito esta vacío.");
        return;
    }

    System.out.println("\n--- PRODUCTOS DE LA COMPRA ---");

    BigDecimal subtotal = BigDecimal.ZERO;
    List<DetallePedido> detalles = new ArrayList<>();

    for (DetalleCarrito linea : lineas) {

        Producto producto = productoService.buscarProducto(
                linea.getProducto().getIdProducto()
        );

        if (producto == null
                || !producto.hayStock(linea.getCantidad())) {

            System.out.println("Stock insuficiente para: "
                    + linea.getProducto().getNombre());
            return;
        }

        DetallePedido detalle = new DetallePedido(
                null,
                producto,
                linea.getCantidad()
        );

        detalles.add(detalle);
        subtotal = subtotal.add(detalle.getSubtotal());

        System.out.println(producto.getNombre()
                + " | Cantidad: " + linea.getCantidad()
                + " | Subtotal: $" + detalle.getSubtotal());
    }

    System.out.println("--------------------------------");
    System.out.println("SUBTOTAL: $" + subtotal);

    // 3. Aplicar cupón
    DescuentoService descuentoService = new DescuentoService();
    EstrategiaDescuento estrategiaDescuento = null;

    BigDecimal descuento = BigDecimal.ZERO;

    System.out.print("\n¿Desea utilizar un cupon? (S/N): ");
    String respuestaCupon = scanner.nextLine().trim();

    if (respuestaCupon.equalsIgnoreCase("S")) {

        System.out.print("Ingrese el codigo del cupon: ");
        String codigo = scanner.nextLine().trim();

        Cupon cupon = descuentoService.buscarCupon(codigo);

        if (cupon != null) {

            estrategiaDescuento = new DescuentoPorCupon(cupon);

            descuento = descuentoService.calcularDescuento(
                    subtotal,
                    estrategiaDescuento
            );

            if (descuento.compareTo(BigDecimal.ZERO) > 0) {
                System.out.println("Cupon aplicado correctamente.");
                System.out.println("Descuento: $" + descuento);
            } else {
                estrategiaDescuento = null;
                System.out.println("El cupon no es valido o no esta vigente.");
            }

        } else {
            System.out.println("No se encontró el cupón.");
        }
    }

    // 4. Seleccionar extras
    ExtraService extraService = new ExtraService();
    List<Extra> extrasSeleccionados = new ArrayList<>();

    List<Extra> extrasDisponibles =
            extraService.listarExtrasDisponibles();

    System.out.print("\n¿Desea agregar extras? (S/N): ");
    String respuestaExtras = scanner.nextLine().trim();

    if (respuestaExtras.equalsIgnoreCase("S")) {

        if (extrasDisponibles.isEmpty()) {
            System.out.println("No hay extras disponibles.");
        } else {

            System.out.println("\n--- EXTRAS DISPONIBLES ---");

            for (Extra extra : extrasDisponibles) {
                System.out.println(
                        extra.getIdExtra() + ". "
                        + extra.getNombre()
                        + " - $" + extra.getPrecio()
                );
            }

            System.out.println(
                    "Ingrese los ID separados por comas (ejemplo: 1,2):"
            );

            String seleccion = scanner.nextLine().trim();

            if (!seleccion.isEmpty()) {

                String[] ids = seleccion.split(",");

                for (String textoId : ids) {

                    try {
                        int idExtra = Integer.parseInt(textoId.trim());

                        Extra extra = extraService.buscarExtra(idExtra);

                        if (extra != null && extra.isActivo()) {

                            extrasSeleccionados.add(extra);

                        } else {
                            System.out.println(
                                    "Extra no valido: " + idExtra
                            );
                        }

                    } catch (NumberFormatException e) {
                        System.out.println(
                                "Se ignoro un ID de extra incorrecto."
                        );
                    }
                }
            }
        }
    }

    // 5. Calcular extras y total
    // Los extras seleccionados se aplican a cada línea del pedido.
    BigDecimal totalExtras = BigDecimal.ZERO;

    List<List<Extra>> extrasPorDetalle = new ArrayList<>();

    for (int i = 0; i < detalles.size(); i++) {

        List<Extra> copiaExtras =
                new ArrayList<>(extrasSeleccionados);

        extrasPorDetalle.add(copiaExtras);

        for (Extra extra : copiaExtras) {
            totalExtras = totalExtras.add(extra.getPrecio());
        }
    }

    BigDecimal total = subtotal
            .subtract(descuento)
            .add(totalExtras);

    System.out.println("\n================================");
    System.out.println("       RESUMEN DE COMPRA");
    System.out.println("================================");
    System.out.println("Subtotal: $" + subtotal);
    System.out.println("Descuentos: $" + descuento);
    System.out.println("Extras: $" + totalExtras);
    System.out.println("TOTAL: $" + total);

    // 6. Dirección de entrega
    System.out.print("\nIngrese la direccion de entrega: ");
    String direccion = scanner.nextLine().trim();

    if (direccion.isEmpty()) {
        System.out.println("La direccióon es obligatoria.");
        return;
    }

    // 7. Consultar métodos de pago
    MetodoPagoDAO metodoPagoDAO = new MetodoPagoDAO();

    List<MetodoPago> metodos =
            metodoPagoDAO.listarActivos();

    if (metodos.isEmpty()) {
        System.out.println("No hay metodos de pago disponibles.");
        return;
    }

    System.out.println("\n--- MÉTODOS DE PAGO ---");

    for (MetodoPago metodo : metodos) {
        System.out.println(
                metodo.getIdMetodoPago() + ". "
                + metodo.getNombre()
        );
    }

    int idMetodo = leerEntero(
            "Seleccione el ID del método de pago: "
    );

    MetodoPago metodoSeleccionado = null;

    for (MetodoPago metodo : metodos) {
        if (metodo.getIdMetodoPago() == idMetodo) {
            metodoSeleccionado = metodo;
            break;
        }
    }

    if (metodoSeleccionado == null) {
        System.out.println("Método de pago no válido.");
        return;
    }

    // 8. Seleccionar estrategia de pago
    EstrategiaPago estrategiaPago;

    String nombreMetodo =
            metodoSeleccionado.getNombre().toLowerCase();

    if (nombreMetodo.contains("tarjeta")) {
        estrategiaPago = new PagoTarjeta();

    } else if (nombreMetodo.contains("transferencia")) {
        estrategiaPago = new PagoTransferencia();

    } else {
        estrategiaPago = new PagoContraEntrega();
    }

    // 9. Crear pedido, pago y factura
    Pedido pedido = new Pedido(
            cliente,
            subtotal,
            descuento,
            total,
            direccion
    );

    Pago pago = new Pago(
            pedido,
            metodoSeleccionado,
            total
    );

    String numeroFactura =
            "FAC-" + System.currentTimeMillis();

    Factura factura = new Factura(
            pedido,
            numeroFactura
    );

    // 10. Confirmar compra
    System.out.print(
            "\n¿Confirma la compra? (S/N): "
    );

    String confirmar = scanner.nextLine().trim();

    if (!confirmar.equalsIgnoreCase("S")) {
        System.out.println("Compra cancelada por el usuario.");
        return;
    }

    // 11. Registrar compra completa
    CompraService compraService = new CompraService();

    boolean resultado = compraService.registrarCompraCompleta(
            pedido,
            detalles,
            extrasPorDetalle,
            pago,
            estrategiaPago,
            factura
    );

    if (resultado) {

        System.out.println("\n================================");
        System.out.println("       RESULTADO DE LA COMPRA");
        System.out.println("================================");

        System.out.println("Pedido: " + pedido.getIdPedido());
        System.out.println("Estado del pago: " + pago.getEstado());
        System.out.println("Total: $" + total);

        if ("APROBADO".equalsIgnoreCase(pago.getEstado())) {
            System.out.println(
                    "Factura: " + factura.getNumeroFactura()
            );
        } else {
            System.out.println(
                    "La factura se generará cuando el pago sea aprobado."
            );
        }

    } else {
        System.out.println(
                "\nNo se pudo completar todo el proceso de compra."
        );
        System.out.println(
                "Consulte los mensajes anteriores para identificar el problema."
        );
    }
}


    // ==========================================
    // CONSULTAR PEDIDOS
    // ==========================================

    private void consultarPedidos() {

    System.out.println("\n--- CONSULTAR PEDIDOS ---");

    int idCliente = leerEntero(
            "Ingrese el ID del cliente: "
    );

    if (idCliente <= 0) {
        System.out.println("El ID del cliente debe ser mayor que cero.");
        return;
    }

    List<Pedido> pedidos =
            pedidoService.consultarPedidosCliente(idCliente);

    if (pedidos == null || pedidos.isEmpty()) {
        System.out.println("El cliente no tiene pedidos registrados.");
        return;
    }

    System.out.println("\n========== HISTORIAL DE PEDIDOS ==========");

    for (Pedido pedido : pedidos) {

        System.out.println("\n------------------------------------------");
        System.out.println("ID del pedido: " + pedido.getIdPedido());
        System.out.println("Fecha: " + pedido.getFechaPedido());
        System.out.println("Estado: " + pedido.getEstado());
        System.out.println("Dirección: " + pedido.getDireccionEntrega());

        System.out.println("\nProductos del pedido:");

        List<DetallePedido> detalles =
                pedidoService.consultarDetallesPedido(
                        pedido.getIdPedido()
                );

        if (detalles == null || detalles.isEmpty()) {
            System.out.println("No hay detalles registrados.");
        } else {

            for (DetallePedido detalle : detalles) {

                System.out.println(
                        "- " + detalle.getNombreProducto()
                        + " | Cantidad: " + detalle.getCantidad()
                        + " | Precio unitario: $"
                        + detalle.getPrecioUnitario()
                        + " | Subtotal: $"
                        + detalle.getSubtotal()
                );
            }
        }

        System.out.println("\nSubtotal: $" + pedido.getSubtotal());
        System.out.println("Descuentos: $" + pedido.getTotalDescuentos());
        System.out.println("TOTAL: $" + pedido.getTotal());

        System.out.println("------------------------------------------");
    }

    System.out.println("\nTotal de pedidos encontrados: " + pedidos.size());
}

    // ==========================================
    // CONSULTAR FACTURAS
    // ==========================================

    private void consultarFacturas() {

    System.out.println("\n========== CONSULTAR FACTURAS ==========");

    List<Factura> facturas = facturaService.listarFacturas();

    if (facturas == null || facturas.isEmpty()) {
        System.out.println("No hay facturas registradas.");
        return;
    }

    for (Factura factura : facturas) {

        System.out.println("\n------------------------------------------");
        System.out.println("ID de factura: " + factura.getIdFactura());
        System.out.println("Número de factura: " + factura.getNumeroFactura());
        System.out.println("Fecha de emisión: " + factura.getFechaEmision());

        if (factura.getPedido() != null) {
            System.out.println(
                    "ID del pedido: " + factura.getPedido().getIdPedido()
            );

            if (factura.getPedido().getCliente() != null) {
                System.out.println(
                        "Cliente: "
+ factura.getPedido().getCliente().getNombres()
+ " "
+ factura.getPedido().getCliente().getApellidos()
                );
            }
        }

        System.out.println("Total facturado: $" + factura.getTotal());
        System.out.println("------------------------------------------");
    }

    System.out.println("\nTotal de facturas encontradas: " + facturas.size());
}
}
