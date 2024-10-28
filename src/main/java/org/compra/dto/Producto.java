package org.compra.dto;

import lombok.Data;

@Data
public class Producto {

    private Long id;

    private String nombre;

    private int cantidad_stock;

    private float valor;


    public Producto() {
    }


    public Producto(Long id, String nombre, int cantidadStock, float valor) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad_stock = cantidadStock;
        this.valor = valor;
    }
}

