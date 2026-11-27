/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.service;

import com.tienda.dao.PedidoDAO;
import com.tienda.dao.DetallePedidoDAO;
import com.tienda.model.Pedido;
import com.tienda.model.DetallePedido;
import java.util.List;

public class PedidoService {

    private final PedidoDAO pedidoDAO;
    private final DetallePedidoDAO detallePedidoDAO;

    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
        this.detallePedidoDAO = new DetallePedidoDAO();
    }

    public boolean registrarPedido(Pedido pedido) {

        if (pedido == null || pedido.getCliente() == null
                || pedido.getSubtotal() == null
                || pedido.getTotal() == null
                || pedido.getTotal().signum() < 0
                || pedido.getSubtotal().signum() < 0) {
            return false;
        }

        return pedidoDAO.registrar(pedido);
    }

    public List<Pedido> consultarPedidosCliente(int idCliente) {

        if (idCliente <= 0) {
            return List.of();
        }

        return pedidoDAO.listarPorCliente(idCliente);
    }

    public Pedido buscarPedido(int idPedido) {

        if (idPedido <= 0) {
            return null;
        }

        return pedidoDAO.buscarPorId(idPedido);
    }

    public boolean actualizarEstado(int idPedido, String estado) {

        if (idPedido <= 0 || estado == null || estado.isBlank()) {
            return false;
        }

        return pedidoDAO.actualizarEstado(idPedido, estado);
    }

    public List<DetallePedido> consultarDetallesPedido(int idPedido) {

        if (idPedido <= 0) {
            return List.of();
        }

        return detallePedidoDAO.listarPorPedido(idPedido);
    }
}
