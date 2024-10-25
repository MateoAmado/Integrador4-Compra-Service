package org.compra.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class CompraId implements Serializable {

    @Column(name = "id_producto")
    private Long idProducto;

    @Column(name = "id_cliente")
    private Long idCliente;


}