/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.tienda.strategy;

import com.tienda.model.Pago;

public class ProcesadorPago {

    private EstrategiaPago estrategia;

    public ProcesadorPago(EstrategiaPago estrategia) {
        this.estrategia = estrategia;
    }

    public void setEstrategia(EstrategiaPago estrategia) {
        this.estrategia = estrategia;
    }

    public boolean ejecutarPago(Pago pago) {

        if (estrategia == null) {
            System.out.println("No se ha seleccionado un método de pago.");
            return false;
        }

        System.out.println("Método seleccionado: "
                + estrategia.obtenerNombreMetodo());

        return estrategia.procesarPago(pago);
    }
}
