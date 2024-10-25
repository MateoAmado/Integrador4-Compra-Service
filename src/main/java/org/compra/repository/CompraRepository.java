package org.compra.repository;

import org.compra.model.Compra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CompraRepository extends JpaRepository<Compra, Long>{
    @Query("SELECT c FROM Compra c WHERE c.idProducto=:idProducto AND c.idCliente = :idCliente")
    public Compra findById(Long idProducto, Long idCliente);

    @Query("DELETE FROM Compra c WHERE c.idProducto=:idProducto AND c.idCliente = :idCliente")
    public Compra deleteBy(Long idProducto, Long idCliente);

    @Query("SELECT c.idProducto, COUNT(c.idProducto) AS cant_producto" +
            " FROM Compra c GROUP BY c.idProducto " +
            "ORDER BY cant_producto DESC LIMIT 1" )
    public Long productoMasVendido();
}
