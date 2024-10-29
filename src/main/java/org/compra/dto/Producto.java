package org.compra.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class Producto {

    private Long id;
    private String nombre;
    private int cantidad_stock;
    private float valorEnPesos;
    private float valorEnDolares;


    public Producto() {}

    public Producto(Long id, String nombre, int cantidad_stock, float valorP, float valorD) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad_stock = cantidad_stock;
        this.valorEnPesos = valorP;
        this.valorEnDolares = valorD;
    }

}

