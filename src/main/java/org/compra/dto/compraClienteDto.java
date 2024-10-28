package org.compra.dto;


import java.sql.Date;

import lombok.Data;

@Data
public class compraClienteDto {

    private Long idCliente;

    private String nombre;

    private String email;

    private int montoDeCompras;
    }


