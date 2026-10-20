/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package vista;

/**
 *
 * @author linda
 */

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

import dao.CategoriaDAO;
import dao.ProductoDAO;
import model.Categoria;
import model.Producto;
import java.util.List;
import java.sql.SQLException;

public class VentanaInventario extends JFrame {

    private JTextField txtNombre;
    private JTextField txtPrecio;
    private JTextField txtCantidad;

    private JComboBox<Categoria> cmbCategoria;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    private JButton btnGuardar;
    private JButton btnActualizar;
    private JButton btnEliminar;
    private JButton btnLimpiar;
    
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final ProductoDAO productoDAO = new ProductoDAO();

    
public VentanaInventario() {
    setTitle("Sistema de Inventario");
    setSize(800, 550);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    inicializarComponentes();

    cargarCategorias();
    cargarProductos();
}

    private void inicializarComponentes() {

        // Panel principal
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        );

        // Titulo
        JLabel lblTitulo = new JLabel(
                "GESTIÓN DE INVENTARIO",
                SwingConstants.CENTER
        );
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));

        panelPrincipal.add(lblTitulo, BorderLayout.NORTH);

        // Formulario
        JPanel panelFormulario = new JPanel(
                new GridLayout(4, 2, 10, 10)
        );

        txtNombre = new JTextField();
        txtPrecio = new JTextField();
        txtCantidad = new JTextField();
        cmbCategoria = new JComboBox<>();

        panelFormulario.add(new JLabel("Nombre del producto:"));
        panelFormulario.add(txtNombre);

        panelFormulario.add(new JLabel("Precio:"));
        panelFormulario.add(txtPrecio);

        panelFormulario.add(new JLabel("Cantidad:"));
        panelFormulario.add(txtCantidad);

        panelFormulario.add(new JLabel("Categoría:"));
        panelFormulario.add(cmbCategoria);

        // Tabla de productos
        modeloTabla = new DefaultTableModel(
                new String[]{
                    "ID", "Nombre", "Precio", "Cantidad", "Categoría"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int fila, int columna) {
                return false;
            }
        };

        tablaProductos = new JTable(modeloTabla);
        JScrollPane scrollTabla = new JScrollPane(tablaProductos);

        // Botones
        JPanel panelBotones = new JPanel(new FlowLayout());

        btnGuardar = new JButton("Guardar");
        btnActualizar = new JButton("Actualizar");
        btnEliminar = new JButton("Eliminar");
        btnLimpiar = new JButton("Limpiar");
        
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        btnGuardar.addActionListener(e -> guardarProducto());
        btnActualizar.addActionListener(e -> actualizarProducto());
        btnEliminar.addActionListener(e -> eliminarProducto());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnLimpiar);

        // Panel central
        JPanel panelCentral = new JPanel(new BorderLayout(10, 10));
        panelCentral.add(panelFormulario, BorderLayout.NORTH);
        panelCentral.add(scrollTabla, BorderLayout.CENTER);
        panelCentral.add(panelBotones, BorderLayout.SOUTH);

        panelPrincipal.add(panelCentral, BorderLayout.CENTER);

        add(panelPrincipal);
    }
    private void cargarCategorias() {
    try {
        cmbCategoria.removeAllItems();

        List<Categoria> categorias = categoriaDAO.listar();

        for (Categoria categoria : categorias) {
            cmbCategoria.addItem(categoria);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Error al cargar las categorías: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}

private void cargarProductos() {
    try {
        modeloTabla.setRowCount(0);

        List<Producto> productos = productoDAO.listar();

        for (Producto producto : productos) {
            modeloTabla.addRow(new Object[]{
                producto.getIdProducto(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getCantidad(),
                producto.getCategoria().getNombre()
            });
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(
                this,
                "Error al cargar los productos: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
private void guardarProducto() {
    try {
        String nombre = txtNombre.getText().trim();
        double precio = Double.parseDouble(txtPrecio.getText().trim());
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());

        Categoria categoriaSeleccionada =
                (Categoria) cmbCategoria.getSelectedItem();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this, "Ingresa el nombre del producto."
            );
            return;
        }

        if (categoriaSeleccionada == null) {
            JOptionPane.showMessageDialog(
                    this, "Selecciona una categoría."
            );
            return;
        }

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setCantidad(cantidad);
        producto.setCategoria(categoriaSeleccionada);

        boolean guardado = productoDAO.insertar(producto);

        if (guardado) {
            JOptionPane.showMessageDialog(
                    this, "Producto guardado correctamente."
            );
            cargarProductos();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(
                    this, "No se pudo guardar el producto."
            );
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(
                this,
                "El precio debe ser numérico y la cantidad debe ser un entero."
        );
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

private void actualizarProducto() {
    int fila = tablaProductos.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this,
                "Selecciona un producto de la tabla para actualizar.");
        return;
    }

    try {
        int id = Integer.parseInt(
                modeloTabla.getValueAt(fila, 0).toString());

        String nombre = txtNombre.getText().trim();
        double precio = Double.parseDouble(txtPrecio.getText().trim());
        int cantidad = Integer.parseInt(txtCantidad.getText().trim());
        Categoria categoria = (Categoria) cmbCategoria.getSelectedItem();

        if (nombre.isEmpty() || categoria == null) {
            JOptionPane.showMessageDialog(this,
                    "Completa todos los campos.");
            return;
        }

        Producto producto = new Producto(
                id, nombre, precio, cantidad, categoria);

        if (productoDAO.actualizar(producto)) {
            JOptionPane.showMessageDialog(this,
                    "Producto actualizado correctamente.");
            cargarProductos();
            limpiarFormulario();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo actualizar el producto.");
        }

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this,
                "El precio debe ser numérico y la cantidad un número entero.");
    } catch (IllegalArgumentException e) {
        JOptionPane.showMessageDialog(this, e.getMessage());
    }
}

private void eliminarProducto() {
    int fila = tablaProductos.getSelectedRow();

    if (fila == -1) {
        JOptionPane.showMessageDialog(this,
                "Selecciona un producto de la tabla para eliminar.");
        return;
    }

    int confirmacion = JOptionPane.showConfirmDialog(
            this,
            "¿Seguro que deseas eliminar este producto?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION);

    if (confirmacion != JOptionPane.YES_OPTION) {
        return;
    }

    int id = Integer.parseInt(
            modeloTabla.getValueAt(fila, 0).toString());

    if (productoDAO.eliminar(id)) {
        JOptionPane.showMessageDialog(this,
                "Producto eliminado correctamente.");
        cargarProductos();
        limpiarFormulario();
    } else {
        JOptionPane.showMessageDialog(this,
                "No se pudo eliminar el producto.");
    }
}

private void limpiarFormulario() {
    txtNombre.setText("");
    txtPrecio.setText("");
    txtCantidad.setText("");
    if (cmbCategoria.getItemCount() > 0) {
        cmbCategoria.setSelectedIndex(0);
    }
    tablaProductos.clearSelection();
}
}
