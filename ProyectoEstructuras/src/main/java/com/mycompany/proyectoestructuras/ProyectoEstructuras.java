/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.proyectoestructuras;

import Controlador.Controlador;
import Modelo.Modelo;
import Vista.Vista;

public class ProyectoEstructuras {
    public static void main(String[] args) {
        Controlador con = new Controlador(new Modelo(), new Vista());
    }
}
