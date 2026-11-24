/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.service;

import com.tienda.dao.ClienteDAO;
import com.tienda.model.Cliente;
import java.util.List;

public class ClienteService {

    private final ClienteDAO clienteDAO;

    public ClienteService() {
        this.clienteDAO = new ClienteDAO();
    }

    public boolean registrarCliente(Cliente cliente) {

        if (cliente == null
                || cliente.getNombres() == null
                || cliente.getNombres().isBlank()
                || cliente.getApellidos() == null
                || cliente.getApellidos().isBlank()
                || cliente.getCorreo() == null
                || cliente.getCorreo().isBlank()) {
            return false;
        }

        return clienteDAO.registrar(cliente);
    }

    public Cliente buscarCliente(int id) {

        if (id <= 0) {
            return null;
        }

        return clienteDAO.buscarPorId(id);
    }

    public List<Cliente> listarClientes() {
        return clienteDAO.listarTodos();
    }
}
