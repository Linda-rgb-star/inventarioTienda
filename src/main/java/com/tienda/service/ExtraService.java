/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


package com.tienda.service;

import com.tienda.dao.ExtraDAO;
import com.tienda.dao.DetallePedidoExtraDAO;
import com.tienda.model.Extra;
import com.tienda.model.DetallePedidoExtra;
import java.util.List;

public class ExtraService {

    private final ExtraDAO extraDAO;
    private final DetallePedidoExtraDAO detalleExtraDAO;

    public ExtraService() {
        this.extraDAO = new ExtraDAO();
        this.detalleExtraDAO = new DetallePedidoExtraDAO();
    }

    public List<Extra> listarExtrasDisponibles() {
        return extraDAO.listarActivos();
    }

    public Extra buscarExtra(int idExtra) {

        if (idExtra <= 0) {
            return null;
        }

        return extraDAO.buscarPorId(idExtra);
    }

    public boolean agregarExtraPedido(
            DetallePedidoExtra detalleExtra) {

        if (detalleExtra == null
                || detalleExtra.getExtra() == null
                || detalleExtra.getExtra().getIdExtra() <= 0
                || detalleExtra.getPrecioAplicado() == null
                || detalleExtra.getPrecioAplicado().signum() < 0) {
            return false;
        }

        return detalleExtraDAO.registrar(
        detalleExtra.getIdDetallePedido(),
        detalleExtra
        );
    }
}
