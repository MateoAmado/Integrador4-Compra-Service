package org.compra.model;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;

@IdClass(CompraId.class)
@Entity
@Data
public class Compra {
    @Id
    @Column(name = "id_producto")
    private Long idProducto;

    @Id
    @Column(name = "id_cliente")
    private Long idCliente;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "fecha")
    private Date fecha;
}
